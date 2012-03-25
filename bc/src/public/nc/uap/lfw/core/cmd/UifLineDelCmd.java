package nc.uap.lfw.core.cmd;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.uap.lfw.core.uif.listener.IBodyInfo;
import nc.uap.lfw.core.uif.listener.MultiBodyInfo;
import nc.uap.lfw.core.uif.listener.SingleBodyInfo;
import nc.vo.pub.SuperVO;

/**
 * "删除行"菜单逻辑处理
 * @author gd 2010-4-1
 *
 */
public class UifLineDelCmd extends UifCommand {
	private IBodyInfo bodyInfo;
	//默认不进行删除
	private boolean delete = false;
	public UifLineDelCmd(IBodyInfo bodyInfo) {
		this.bodyInfo = bodyInfo;
	}
	
	public void execute() {
		ViewContext widgetctx = getLifeCycleContext().getViewContext();

		String dsId = getSlaveDataset(widgetctx);
		Dataset ds = widgetctx.getView().getViewModels().getDataset(dsId);
		Row selRow = ds.getSelectedRow();
		//未选择行，给出提示
		if(selRow == null)
			throw new LfwRuntimeException("请选择要删除的行!");
		// 持久化 
		Dataset2SuperVOSerializer ser = new Dataset2SuperVOSerializer();
		SuperVO vo = ser.serialize(ds, selRow)[0];
		if(vo.getPrimaryKey() == null)
			delete = true;
		if(delete)
			doDeleteVO(vo);
		else
			doCache(vo, ds,  selRow);
		if(selRow != null){
			int rowIndex = ds.getRowIndex(selRow);
			ds.removeRow(rowIndex);
			if(rowIndex > ds.getCurrentRowCount() - 1)
				rowIndex = ds.getCurrentRowCount() - 1;
			ds.setRowSelectIndex(rowIndex);
		}
		doAfterDelLine();
	}

	private void doCache(SuperVO vo, Dataset ds,  Row row) {
		ViewContext widgetCtx = getLifeCycleContext().getViewContext();
		//此子表的dsId
		String dsId = getSlaveDataset(widgetCtx);
		DatasetRelation[] rels = widgetCtx.getView().getViewModels().getDsrelations().getDsRelations();
		if(rels == null)
			return;
		String foreignKey = null;
		for (int i = 0; i < rels.length; i++) {
			DatasetRelation dsRel = rels[i];
			if(dsRel.getDetailDataset().equals(dsId)){
				foreignKey = dsRel.getDetailForeignKey();
				break;
			}
		}
		if(foreignKey == null)
			return;
		String foreignValue = (String) vo.getAttributeValue(foreignKey);
		List<SuperVO> list = (List<SuperVO>) LfwCacheManager.getSessionCache().get(foreignValue);
		if(list == null){
			list = new ArrayList<SuperVO>();
		}
		list.add(vo);
		LfwCacheManager.getSessionCache().put(foreignValue, list);
		
		//删除的子表的行信息
		String delRowForeignKey = foreignValue + "_" + dsId;
		List<Row> listDelRow = (List<Row>) LfwCacheManager.getSessionCache().get(delRowForeignKey);
		if(listDelRow == null){
			listDelRow = new ArrayList<Row>();
		}
		listDelRow.add(row);
		LfwCacheManager.getSessionCache().put(delRowForeignKey, listDelRow);
	}

	private void doDeleteVO(SuperVO vo) {
		try {
			CRUDHelper.getCRUDService().deleteVo(vo);
		} 
		catch (LfwBusinessException e) {
			LfwLogger.error(e.getMessage(), e);
		}
	}

	/**
	 * 根据当前选中的项获得对应的Dataset
	 * @param widgetCtx 
	 * @param item
	 * @return
	 */
	protected String getSlaveDataset(ViewContext widgetCtx) {
		String dsId = null;
		if(bodyInfo != null){
			if(bodyInfo instanceof MultiBodyInfo){
				MultiBodyInfo mbi = (MultiBodyInfo) bodyInfo;
				String bodyTabId = mbi.getBodyTabId();
//				TabLayout tab = null;//widgetCtx.getTab(bodyTabId);
//				if(tab == null)
//					throw new LfwRuntimeException("未获取页签控件!id=" + bodyTabId);
//				TabItem item = tab.getCurrentItem();
//				dsId = mbi.getDatasetByTabItem(item.getId());
			}
			else{
				SingleBodyInfo sbi = (SingleBodyInfo) bodyInfo;
				dsId = sbi.getBodyDataset();
			}
		}
		if(dsId == null)
			throw new LfwRuntimeException("没有找到待操作数据集,请确认配置正确", "没有找到待操作数据集");
		return dsId;
	}
	
	protected void doAfterDelLine(){
		
	}
}
