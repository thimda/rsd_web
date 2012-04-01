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
		
		//获取前后的值
		String oldValue = (String) e.getOldValue();
		String newValue = (String) e.getNewValue();

		//获取修改的row
		Row row = ds.getCurrentRowData().getSelectedRow();
		if(row == null)
			return;
		
		//获取修改的索引和对应的ds中的code：dsCode
		int changeIndex = e.getColIndex();
		Field field = ds.getFieldSet().getField(changeIndex);
		String dsCode =field.getId();

		//获取修改的元素的ID
		int idIndex = ds.nameToIndex("string_ext1");
		//String compId = (String) row.getValue(idIndex);
		
		int itemIdIndex = ds.nameToIndex(PaPropertyDatasetListener.FIELD_CHILDID);
		String itemId = (String) row.getValue(itemIdIndex);
		
		int viewIndex = ds.nameToIndex("string_ext2");
		String viewId = (String) row.getValue(viewIndex);
		
		int prtIndex = ds.nameToIndex("parentid");
		String prtId = (String) row.getValue(prtIndex);
		
		//获取修改元素的类型，已经前台对应的属性值
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
			LfwLogger.debug("组件ID:"+itemId+";父节点ID:"+prtId+"; 组件类型:"+ type +"; 前台属性:"+pi.getId() + "; 属性类型:" + pi.getType() + "; 显示的label:" + pi.getLabel()+ "; 修改前的值:" + oldValue +"; 修改后的值: "+ newValue);
//			LfwLogger.debug("组件ID:"+itemId+";父节点ID:"+prtId+"; 组件类型:"+ type +"; 前台属性:"+pi.getId() + "; 属性类型:" + pi.getType() + "; 显示的label:" + pi.getLabel()+ "; 修改前的值:" + oldValue +"; 修改后的值: "+ newValue);
			row.setValue(stateIndex, PaConstant.DS_UPDATE);
//			String widgetId = (String) row.getValue(ds.nameToIndex(""));
			ctx.addExecScript("var obj ={widgetid :'" + viewId + "',updateid:" + (changeIndex == idIndex) + ", parentid : '" + prtId + "', compid:'" + itemId + "', type:'" + type + "', attr:'" + pi.getId()+ "', attrtype:'" + pi.getType() + "', oldvalue:'" + oldValue + "', newvalue:'" + newValue + "'};");
			ctx.addExecScript("document.getElementById('iframe_tmp').contentWindow.updateProperty(obj);");
		}

//		super.onAfterDataChange(e);
	}

}
