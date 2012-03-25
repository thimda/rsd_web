package nc.uap.lfw.core.model.util;

import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.comp.ExcelColumn;
import nc.uap.lfw.core.comp.ExcelColumnGroup;
import nc.uap.lfw.core.comp.ExcelComp;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IExcelColumn;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ApplicationContext;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.ctx.WidgetContext;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.processor.EventRequestContext;
import nc.uap.lfw.core.tags.AppDynamicCompUtil;
import nc.uap.lfw.core.tags.DatasetMetaUtil;
import nc.uap.lfw.core.tags.DynamicCompUtil;
import nc.uap.lfw.core.tags.ExcelModelUtil;
import nc.uap.lfw.core.model.AppTypeUtil;

public final class ElementObserver {
	public static final String OBS_IN = "OBS_IN";
	private static final String GRIDCOLUMN_VISIBLE_SCRIPT = "gridcolumn_visible_script";
	private static final String GRIDCOLUMN_VISIBLE_INDEX = "gridcolumn_visible_index";
	private static final String GRIDCOLUMN_EDITABLE_SCRIPT = "gridcolumn_editable_script";
	private static final String GRIDCOLUMN_EDITABLE_INDEX = "gridcolumn_editable_index";
	private static final String GRIDCOLUMN_PRECISION_INDEX = "gridcolumn_precision_index";
	public static void notifyChange(String type, WebElement ele){
		notifyChange(type, ele, null);
	}
	
	public static void notifyChange(String type, WebElement ele, Object userObject){
//		if(!LfwRuntimeEnvironment.isPageAction())
//			return;
		if(ele.getExtendAttributeValue(OBS_IN) != null)
			return;
		ele.setExtendAttribute(OBS_IN, "1");
		if(ele instanceof ComboData){
			processComboData(ele);
		}
		else if(ele instanceof GridColumn){
			if(type != null && type.equals(GridColumn.COLUMN_VISIBLE)){
				if(AppTypeUtil.getApplicationType().equals(AppTypeUtil.APP_TYPE))
					appProcessColumnVisible((GridColumn) ele, (GridComp) userObject);
				else
					processColumnVisible((GridColumn) ele, (GridComp) userObject);
			}
			else if(type != null && type.equals(GridColumn.COLUMN_EDITABLE)){
//				if(AppTypeUtil.getApplicationType().equals(AppTypeUtil.APP_TYPE))
//					appProcessColumnEditable((GridColumn) ele, (GridComp) userObject);
//				else
					processColumnEditable((GridColumn) ele, (GridComp) userObject);
			}
			else if(type != null && type.equals(GridColumn.COLUMN_PRECISION)){	
				processColumnPrecision((GridColumn) ele, (GridComp) userObject);
			}
		}else if(ele instanceof Dataset){
			processDataset(type, (Dataset)ele, userObject);
		}
		else if(ele instanceof LfwWidget){
			LfwWidget widget = (LfwWidget) ele;
			if(widget.isDialog())
				processWidget(widget);
		}
		else if(ele instanceof ExcelComp){
			processExcelComp(type,(ExcelComp)ele,userObject);
		}
		
		ele.removeExtendAttribute(OBS_IN);
	}
	
	private static void processWidget(LfwWidget widget) {
		// TODO Auto-generated method stub
		LfwPageContext pctx = EventRequestContext.getLfwPageContext();
		pctx.addExecScript("pageUI.getDialog('" + widget.getId() + "').titleDiv.innerHTML='" + widget.getCaption() + "'");
	}

	public static void processDataset(String type,Dataset ds,Object userObject){
		if("adjustField".equals(type)){
			StringBuffer buf = new StringBuffer();
			LfwWidget widget = ds.getWidget();
			String varDs = "$c_"+ds.getId();
			if(widget == null){
				buf.append("var "+varDs).append(" = pageUI.getDataset('"+ds.getId()+"');\n");
			}else{
				buf.append("var "+varDs).append(" = pageUI.getWidget('"+widget.getId()+"').getDataset('"+ds.getId()+"');\n");
			}
			Field field = (Field)userObject;
			String varField = "$c_"+field.getId();
			buf.append("var "+varField).append(" = "+DatasetMetaUtil.generateField(field)+";\n");
			buf.append(varDs).append(".addField("+varField+");\n");
			addBeforeDynamicScript(buf.toString());
			
		}
	}
	
	public static void processExcelComp(String type,ExcelComp ec,Object userObject){
		if("addColumn".equals(type)){
			
			StringBuffer buf = new StringBuffer();
			LfwWidget widget = ec.getWidget();
			String varEc = "$c_"+ec.getId();
			String varModel = "model";
			String varHeader = varEc+"_header";
			if(widget != null){
				buf.append("var "+varEc).append(" = pageUI.getWidget('"+widget.getId()+"').getComponent('"+ec.getId()+"');\n");
				buf.append("var "+varModel+" = "+varEc+".model; \n");
				IExcelColumn header = (IExcelColumn) userObject;
				Dataset ds = widget.getViewModels().getDataset(ec.getDataset());
				String varDs = "$c_"+ds.getId();
				//buf.append("var "+varHeader).append(" = "+ExcelModelUtil.generalExcelHeader(ds, widget,header) + "; \n");
				buf.append(" "+ExcelModelUtil.generalExcelHeader(ds, widget,header) + "; \n");
				buf.append("var "+varDs).append(" = pageUI.getWidget('"+widget.getId()+"').getDataset('"+ds.getId()+"');\n");
				buf.append(varModel).append(".setDataSet("+varDs+");\n");
				buf.append(varEc).append(".setModel("+varModel+");\n");
				addDynamicScript(buf.toString());
			}
			
		}
	}
	
	/**
	 * 设置grid列显示属性
	 * 
	 * @param ele
	 * @param grid
	 */
	private static void processColumnVisible(GridColumn ele, GridComp grid) {
		LfwPageContext pctx = EventRequestContext.getLfwPageContext();
		
		Integer index = (Integer) pctx.getUserObject(GRIDCOLUMN_VISIBLE_INDEX + grid.getId());
		if(index != null){
			pctx.removeExecScript(index);
		}
		
		String script = null;
		String visibleScript = (String) pctx.getUserObject(GRIDCOLUMN_VISIBLE_SCRIPT + grid.getId());
		if (visibleScript == null || visibleScript.equals("")){
			visibleScript = "[]";
		}
		
		//清空之前的对该列的设置 
		visibleScript = visibleScript.replace(",\"" + ele.getId() + ":" +"true\"", "");
		visibleScript = visibleScript.replace(",\"" + ele.getId() + ":" +"false\"", "");
		visibleScript = visibleScript.replace("\"" + ele.getId() + ":" +"true\"", "");
		visibleScript = visibleScript.replace("\"" + ele.getId() + ":" +"false\"", "");
		
		if(ele.isVisible()){
			visibleScript = visibleScript.replace("]", ",\"" + ele.getId() + ":" +"true\"]"); 
		}
		else{
			visibleScript = visibleScript.replace("]", ",\"" + ele.getId() + ":" +"false\"]");
		}
		visibleScript = visibleScript.replace("[,", "[");
		script = "pageUI.getWidget('" + grid.getWidget().getId() + "').getComponent('" + grid.getId() + "').setColumnVisible(" + visibleScript + ")";
		pctx.addUserObject(GRIDCOLUMN_VISIBLE_SCRIPT + grid.getId(), visibleScript);
		
		index = pctx.addExecScript(script);
		pctx.addUserObject(GRIDCOLUMN_VISIBLE_INDEX + grid.getId(), index);
		
	}
	
	/**
	 * app 设置grid列显示属性
	 * 
	 * @param ele
	 * @param grid
	 */
	private static void appProcessColumnVisible(GridColumn ele, GridComp grid) {
		ApplicationContext appCtx = AppLifeCycleContext.current().getApplicationContext();
		
		
		Integer index = (Integer) appCtx.getAppAttribute(GRIDCOLUMN_VISIBLE_INDEX + grid.getId());
		if(index != null){
			appCtx.removeExecScript(index);
		}
		
		String script = null;
		String visibleScript = (String) appCtx.getAppAttribute(GRIDCOLUMN_VISIBLE_SCRIPT + grid.getId());
		if (visibleScript == null || visibleScript.equals("")){
			visibleScript = "[]";
		}
		
		//清空之前的对该列的设置 
		visibleScript = visibleScript.replace(",\"" + ele.getId() + ":" +"true\"", "");
		visibleScript = visibleScript.replace(",\"" + ele.getId() + ":" +"false\"", "");
		visibleScript = visibleScript.replace("\"" + ele.getId() + ":" +"true\"", "");
		visibleScript = visibleScript.replace("\"" + ele.getId() + ":" +"false\"", "");
		
		if(ele.isVisible()){
			visibleScript = visibleScript.replace("]", ",\"" + ele.getId() + ":" +"true\"]"); 
		}
		else{
			visibleScript = visibleScript.replace("]", ",\"" + ele.getId() + ":" +"false\"]");
		}
		visibleScript = visibleScript.replace("[,", "[");
		script = "pageUI.getWidget('" + grid.getWidget().getId() + "').getComponent('" + grid.getId() + "').setColumnVisible(" + visibleScript + ")";
		appCtx.addAppAttribute(GRIDCOLUMN_VISIBLE_SCRIPT + grid.getId(), visibleScript);
		
		index = appCtx.addExecScript(script);
		appCtx.addAppAttribute(GRIDCOLUMN_VISIBLE_INDEX + grid.getId(), index);
		
	}
	
	/**
	 * 设置grid列可编辑属性
	 * 
	 * @param ele
	 * @param grid
	 */
	private static void processColumnEditable(GridColumn ele, GridComp grid) {
		LfwPageContext pctx = EventRequestContext.getLfwPageContext();
		
		Integer index = (Integer) pctx.getUserObject(GRIDCOLUMN_EDITABLE_INDEX + grid.getId());
		if(index != null){
			pctx.removeExecScript(index);
		}
		
		String script = null;
		String editableScript = (String) pctx.getUserObject(GRIDCOLUMN_EDITABLE_SCRIPT + grid.getId());
		if (editableScript == null || editableScript.equals("")){
			editableScript = "[]";
		}
		
		//清空之前的对该列的设置 
		editableScript = editableScript.replace(",\"" + ele.getId() + ":" +"true\"", "");
		editableScript = editableScript.replace(",\"" + ele.getId() + ":" +"false\"", "");
		editableScript = editableScript.replace("\"" + ele.getId() + ":" +"true\"", "");
		editableScript = editableScript.replace("\"" + ele.getId() + ":" +"false\"", "");
		
		if(ele.isEditable()){
			editableScript = editableScript.replace("]", ",\"" + ele.getId() + ":" +"true\"]"); 
		}
		else{
			editableScript = editableScript.replace("]", ",\"" + ele.getId() + ":" +"false\"]");
		}
		editableScript = editableScript.replace("[,", "[");
		script = "pageUI.getWidget('" + grid.getWidget().getId() + "').getComponent('" + grid.getId() + "').setColumnEditable(" + editableScript + ")";
		pctx.addUserObject(GRIDCOLUMN_EDITABLE_SCRIPT + grid.getId(), editableScript);
		
		index = pctx.addExecScript(script);
		pctx.addUserObject(GRIDCOLUMN_EDITABLE_INDEX + grid.getId(), index);
		
	}
	
	/**
	 * 设置grid列精度
	 * 
	 * @param ele
	 * @param grid
	 */
	private static void processColumnPrecision(GridColumn ele, GridComp grid) {
		LfwPageContext pctx = EventRequestContext.getLfwPageContext();
		Integer index = (Integer) pctx.getUserObject(GRIDCOLUMN_PRECISION_INDEX + grid.getId() + "_" + ele.getId());
		if(index != null){
			pctx.removeExecScript(index);
		}
		String script = "try{ pageUI.getWidget('" + grid.getWidget().getId() + "').getComponent('" + grid.getId() + "').getBasicHeaderById('" + ele.getId() + "').setPrecision(" + ele.getPrecision() + ") }catch(e){};";
		index = pctx.addExecScript(script);
		pctx.addUserObject(GRIDCOLUMN_PRECISION_INDEX + grid.getId() + "_"  + ele.getId(), index);
	}
	
	private static void processComboData(WebElement ele) {
		if(((ComboData) ele).getWidget() == null)
			return;
		String widgetId = ((ComboData) ele).getWidget().getId();
		if (AppTypeUtil.getApplicationType().equals(AppTypeUtil.APP_TYPE)){
			ApplicationContext appCtx = AppLifeCycleContext.current().getApplicationContext();
			ViewContext viewCtx = appCtx.getCurrentWindowContext().getViewContext(widgetId);
			AppDynamicCompUtil util = new AppDynamicCompUtil(appCtx, viewCtx);
			util.replaceComboData(ele.getId(), widgetId, (ComboData) ele.clone());
		}
		else {
			LfwPageContext pc = EventRequestContext.getLfwPageContext();
			WidgetContext wc = pc.getWidgetContext(widgetId);
			if (wc == null && pc.getParentGlobalContext() != null)
				wc = pc.getParentGlobalContext().getWidgetContext(widgetId);
			DynamicCompUtil util = new DynamicCompUtil(pc, wc);
			util.replaceComboData(ele.getId(), widgetId, (ComboData) ele.clone());
		}
	}
	
	public static void addDynamicScript(String script){
		if(script == null || script.equals(""))
			return;
		if(AppTypeUtil.getApplicationType().equals(AppTypeUtil.APP_TYPE)){
			AppLifeCycleContext.current().getApplicationContext().addExecScript(script);
		}
		else{
			LfwPageContext ctx = EventRequestContext.getLfwPageContext();
			ctx.addExecScript(script);
		}
	}
	
	public static void addBeforeDynamicScript(String script){
		if(script == null || script.equals(""))
			return;
		if(AppTypeUtil.getApplicationType().equals(AppTypeUtil.APP_TYPE)){
			AppLifeCycleContext.current().getApplicationContext().addBeforeExecScript(script);
		}
		else{
			LfwPageContext ctx = EventRequestContext.getLfwPageContext();
			ctx.addBeforeExecScript(script);
		}
	}
}
