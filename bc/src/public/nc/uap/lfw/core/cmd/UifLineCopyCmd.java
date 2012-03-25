package nc.uap.lfw.core.cmd;

import java.util.UUID;

import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.uif.listener.IBodyInfo;
import nc.uap.lfw.core.uif.listener.MultiBodyInfo;
import nc.uap.lfw.core.uif.listener.SingleBodyInfo;

/**
 * "复制行"菜单逻辑处理
 * @author gd 2010-4-1
 *
 */
public class UifLineCopyCmd extends UifCommand {
	private IBodyInfo bodyInfo;
	public UifLineCopyCmd(IBodyInfo bodyInfo) {
		this.bodyInfo = bodyInfo;
	}
	
	public void execute() {
//		/WindowContext windowctx = getLifeCycleContext().getWindowContext();
		ViewContext widgetctx = getLifeCycleContext().getViewContext();
		
		String dsId = getSlaveDataset(widgetctx);
		Dataset ds = widgetctx.getView().getViewModels().getDataset(dsId);
		// 复制选中行
		Row row = ds.getSelectedRow();
		if(row == null)
			throw new LfwRuntimeException("请选则要复制的行!");
		
		Row corpyRow = (Row) row.clone();
		// 删除复制行的主键字段
		FieldSet fields = ds.getFieldSet();
		Field primaryField = null;
		for(int i = 0, count = fields.getFieldCount(); i < count; i ++)
		{
			if(fields.getField(i).isPrimaryKey())
			{
				primaryField = fields.getField(i);
				break;
			}
		}
		if(primaryField == null)
			throw new LfwRuntimeException("数据集" + ds.getId() + "没有配置主键字段!");
		corpyRow.setValue(ds.nameToIndex(primaryField.getId()), null);
		
		// 保存复制行到webSession
		corpyRow.setRowId(row.getRowId() + UUID.randomUUID());
		getLifeCycleContext().getApplicationContext().addAppAttribute("$copyRow", corpyRow);
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
}
