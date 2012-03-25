package nc.uap.lfw.core.cmd;

import java.util.UUID;

import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.uif.listener.IBodyInfo;
import nc.uap.lfw.core.uif.listener.MultiBodyInfo;
import nc.uap.lfw.core.uif.listener.SingleBodyInfo;

/**
 * "复制行"菜单逻辑处理
 * @author gd 2010-4-22
 * @version NC6.0
 */
public class UifLinePasteCmd extends UifCommand { 
	private IBodyInfo bodyInfo;
	public UifLinePasteCmd(IBodyInfo bodyInfo) {
		this.bodyInfo = bodyInfo;
	}
	
	public void execute() {
		ViewContext widgetCtx = getLifeCycleContext().getViewContext();
		
		String dsId = getSlaveDataset(widgetCtx);
		Dataset ds = widgetCtx.getView().getViewModels().getDataset(dsId);
		
		Row copyRow = (Row) getLifeCycleContext().getApplicationContext().getAppAttribute("$copyRow");
		if(copyRow == null)
			throw new LfwRuntimeException("请先复制行!");
		//去掉此行，为了可以年贴多行数据
		//widgetCtx.getWebSession().removeAttribute("$copyRow");
		copyRow.setRowId(UUID.randomUUID().toString());
		ds.addRow(copyRow);
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
}
