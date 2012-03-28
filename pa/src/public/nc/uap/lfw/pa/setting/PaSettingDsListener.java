/**
 * 
 */
package nc.uap.lfw.pa.setting;

import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ApplicationContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DatasetCellEvent;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.pa.PaConstant;
import nc.uap.lfw.pa.PaPropertyDatasetListener;
import nc.uap.lfw.pa.info.BaseInfo;
import nc.uap.lfw.pa.info.IPropertyInfo;
import nc.uap.lfw.pa.info.InfoCategory;

/**
 * @author wupeng1
 * @version 6.0 2011-9-21
 * @since 1.6
 */
public class PaSettingDsListener {

	public void onAfterDataChange(DatasetCellEvent e) {
		Dataset ds = e.getSource();
		
		//��ȡǰ���ֵ
		String oldValue = (String) e.getOldValue();
		String newValue = (String) e.getNewValue();

		//��ȡ�޸ĵ�row
		Row row = ds.getCurrentRowData().getSelectedRow();
		if(row == null)
			return;
		
		//��ȡ�޸ĵ������Ͷ�Ӧ��ds�е�code��dsCode
		int changeIndex = e.getColIndex();
		Field field = ds.getFieldSet().getField(changeIndex);
		String dsCode =field.getId();

		//��ȡ�޸ĵ�Ԫ�ص�ID
		int idIndex = ds.nameToIndex("string_ext1");
		//String compId = (String) row.getValue(idIndex);
		
		int itemIdIndex = ds.nameToIndex(PaPropertyDatasetListener.FIELD_CHILDID);
		String itemId = (String) row.getValue(itemIdIndex);
		
		int viewIndex = ds.nameToIndex("string_ext2");
		String viewId = (String) row.getValue(viewIndex);
		
		int prtIndex = ds.nameToIndex("parentid");
		String prtId = (String) row.getValue(prtIndex);
		
		//��ȡ�޸�Ԫ�ص����ͣ��Ѿ�ǰ̨��Ӧ������ֵ
		int stateIndex = ds.nameToIndex("ds_state");
		String state = (String) row.getValue(stateIndex);
		if(state.equals(PaConstant.DS_DEL))
			return;
		int typeIndex = ds.nameToIndex("comptype");
		String type = (String) row.getValue(typeIndex);
		BaseInfo bpi = InfoCategory.getInfo(type);
		IPropertyInfo[] ipis = bpi.getPropertyInfos();
		IPropertyInfo pi = null;
		for(int i = 0; i < ipis.length; i++){
			pi = ipis[i];
			if(pi.getDsField().equals(dsCode))
				break;
			pi = null;
		}
		if(pi != null && row != null){
			ApplicationContext ctx = AppLifeCycleContext.current().getApplicationContext();
			ctx.addExecScript("setEditorState();");
			LfwLogger.debug("���ID:"+itemId+";���ڵ�ID:"+prtId+"; �������:"+ type +"; ǰ̨����:"+pi.getId() + "; ��������:" + pi.getType() + "; ��ʾ��label:" + pi.getLabel()+ "; �޸�ǰ��ֵ:" + oldValue +"; �޸ĺ��ֵ: "+ newValue);
			System.out.println("���ID:"+itemId+";���ڵ�ID:"+prtId+"; �������:"+ type +"; ǰ̨����:"+pi.getId() + "; ��������:" + pi.getType() + "; ��ʾ��label:" + pi.getLabel()+ "; �޸�ǰ��ֵ:" + oldValue +"; �޸ĺ��ֵ: "+ newValue);
			row.setValue(stateIndex, PaConstant.DS_UPDATE);
//			String widgetId = (String) row.getValue(ds.nameToIndex(""));
			ctx.addExecScript("var obj ={widgetid :'" + viewId + "',updateid:" + (changeIndex == idIndex) + ", parentid : '" + prtId + "', compid:'" + itemId + "', type:'" + type + "', attr:'" + pi.getId()+ "', attrtype:'" + pi.getType() + "', oldvalue:'" + oldValue + "', newvalue:'" + newValue + "'};");
			ctx.addExecScript("document.getElementById('iframe_tmp').contentWindow.updateProperty(obj);");
		}

//		super.onAfterDataChange(e);
	}

}
