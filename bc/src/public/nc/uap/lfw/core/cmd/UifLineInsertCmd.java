package nc.uap.lfw.core.cmd;

import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.uif.listener.IBodyInfo;
import nc.uap.lfw.core.uif.listener.MultiBodyInfo;
import nc.uap.lfw.core.uif.listener.SingleBodyInfo;

/**
 * "新增行"菜单逻辑处理
 * @author gd 2010-4-1
 *
 */
public class UifLineInsertCmd extends UifCommand {
	private IBodyInfo bodyInfo;
	public UifLineInsertCmd(IBodyInfo bodyInfo) {
		this.bodyInfo = bodyInfo;
	}

	public void execute() {
		ViewContext widgetctx = getLifeCycleContext().getViewContext();
		
		String dsId = getSlaveDataset(widgetctx);
		if(dsId == null)
			throw new LfwRuntimeException("没有获得当前增行数据集");
		
		Dataset ds = widgetctx.getView().getViewModels().getDataset(dsId);
		String keyValue = getKeyValue(widgetctx, ds.getId());
		ds.setCurrentKey(keyValue);
		
		Row row = ds.getEmptyRow();
		processRow(ds, row);
		
		Integer index = getInsertIndex(ds);
		//如果不是第一行数据，并且这一列锁定的话，则默认取上一列的值
		if(index.intValue() >= 1){
			Row row1 = ds.getCurrentRowData().getRow(index - 1);
			if(row1 != null){
				Field[] fields = ds.getFieldSet().getFields();
				for (Field field : fields) {
					if(field.isLock()){
						String value = (String) row1.getValue(ds.nameToIndex(field.getId()));
						if(value == null)
							continue;
						row.setValue(ds.nameToIndex(field.getId()), value);
					}
				}
			}
		}
		
		ds.insertRow(index, row);
		ds.setEnabled(true);
		ds.setRowSelectIndex(index);
	}
	
	protected int getInsertIndex(Dataset ds) {
		Integer index = ds.getSelectedIndex();
		if(index == -1)
			index = ds.getCurrentRowCount();
		return index;
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
//				String bodyTabId = mbi.getBodyTabId();
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

	protected String getKeyValue(ViewContext widgetCtx, String dsId) {
		String keyValue = Dataset.MASTER_KEY;
		String masterDsId = widgetCtx.getView().getViewModels().getDsrelations().getMasterDsByDetailDs(dsId);
		if(masterDsId != null){
			DatasetRelation dr = widgetCtx.getView().getViewModels().getDsrelations().getDsRelation(masterDsId, dsId);
			Dataset masterDs = widgetCtx.getView().getViewModels().getDataset(masterDsId);
			Row masterSelRow = masterDs.getSelectedRow();
			if(masterSelRow == null)
				throw new LfwRuntimeException("主表未选中行");
			String masterKeyField = dr.getMasterKeyField();
			keyValue = (String) masterSelRow.getValue(masterDs.nameToIndex(masterKeyField));
			if(keyValue == null || keyValue.equals(""))
				keyValue = masterSelRow.getRowId();
		}
		return keyValue;
	}

	protected void processRow(Dataset ds, Row row) {
	}
}
