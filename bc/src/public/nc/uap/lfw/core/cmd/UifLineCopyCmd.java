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
 * "������"�˵��߼�����
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
		// ����ѡ����
		Row row = ds.getSelectedRow();
		if(row == null)
			throw new LfwRuntimeException("��ѡ��Ҫ���Ƶ���!");
		
		Row corpyRow = (Row) row.clone();
		// ɾ�������е������ֶ�
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
			throw new LfwRuntimeException("���ݼ�" + ds.getId() + "û�����������ֶ�!");
		corpyRow.setValue(ds.nameToIndex(primaryField.getId()), null);
		
		// ���渴���е�webSession
		corpyRow.setRowId(row.getRowId() + UUID.randomUUID());
		getLifeCycleContext().getApplicationContext().addAppAttribute("$copyRow", corpyRow);
	}

	
	/**
	 * ���ݵ�ǰѡ�е����ö�Ӧ��Dataset
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
//					throw new LfwRuntimeException("δ��ȡҳǩ�ؼ�!id=" + bodyTabId);
//				TabItem item = tab.getCurrentItem();
//				dsId = mbi.getDatasetByTabItem(item.getId());
			}
			else{
				SingleBodyInfo sbi = (SingleBodyInfo) bodyInfo;
				dsId = sbi.getBodyDataset();
			}
		}
		if(dsId == null)
			throw new LfwRuntimeException("û���ҵ����������ݼ�,��ȷ��������ȷ", "û���ҵ����������ݼ�");
		return dsId;
	}
}
