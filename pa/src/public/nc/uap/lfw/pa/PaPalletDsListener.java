package nc.uap.lfw.pa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.PageModelUtil;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.design.itf.IPaEditorService;

public class PaPalletDsListener {

	private static final String DEFINED = " ";
	private static final String UNDEFINED = "  ";
	public void onDataLoad(DataLoadEvent e) {
		Dataset ds = e.getSource();
		
		
		PageMeta pagemeta = null;
		String pk_template = LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("pk_template");
		if(pk_template != null)
			LfwRuntimeEnvironment.setFromDB(true);
		if(LfwRuntimeEnvironment.isFromDB()){
			if(pk_template != null){
				pagemeta = PageModelUtil.getPageMeta(pk_template);
			}
		}
		else{
			IPaEditorService ipaService = NCLocator.getInstance().lookup(IPaEditorService.class);
			String pageId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("_pageId");
			String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
			waitForInit(pageId, sessionId);
			pagemeta = ipaService.getOriPageMeta(pageId, sessionId);
		}
		
		if(pagemeta == null)
			return;
		
		WebSession session = LfwRuntimeEnvironment.getWebContext().getWebSession();
		String isEclipse = session.getOriginalParameter("eclipse");
		String viewId = session.getOriginalParameter("viewId");
		if(isEclipse != null && isEclipse.equals("1")){
			if(viewId != null){
				if(ds.getId().equals("bindds")){
					fillBindDs(ds, pagemeta, viewId);
				}
				else if(ds.getId().equals("ctrlds")){
					fillCtrlDs(ds, pagemeta, viewId);
				}
				else if(ds.getId().equals("layoutds")){
					fillLayoutDs(ds);
				}
			}
			else{
				if(ds.getId().equals("currds")){
					fillCurrDs(ds, pagemeta);
				}
				else if(ds.getId().equals("layoutds")){
					fillLayoutDs(ds);
				}
			}
		}
		else{
			
			if(ds.getId().equals("bindds")){
				fillBindDs(ds, pagemeta, null);
			}
			else if(ds.getId().equals("ctrlds")){
				fillCtrlDs(ds, pagemeta, null);
			}
			else if(ds.getId().equals("layoutds")){
				fillLayoutDs(ds);
			}
			else if(ds.getId().equals("currds")){
				fillCurrDs(ds, pagemeta);
			}
		}
	}

	private void fillCurrDs(Dataset ds, PageMeta pagemeta) {
		
		Row row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), DEFINED);
		row.setValue(ds.nameToIndex("name"), "已定义的View");
		ds.addRow(row);
		
		LfwWidget[] widgets = pagemeta.getWidgets();
//		Row row = null;
		if(widgets != null && widgets.length > 0){
			for(int i = 0; i < widgets.length; i++){
				row = ds.getEmptyRow();
				LfwWidget widget = widgets[i];
				String widgetId = widget.getId();
				row.setValue(ds.nameToIndex("id"), widgetId);
				row.setValue(ds.nameToIndex("name"), "View");
				row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_WIDGT);
				row.setValue(ds.nameToIndex("pid"), DEFINED);
				ds.addRow(row);
			}
		}
		
//		Row row = ds.getEmptyRow();
//		row.setValue(ds.nameToIndex("id"), "1");
//		row.setValue(ds.nameToIndex("name"), "表格");
//		ds.addRow(row);
//		
//		row = ds.getEmptyRow();
//		row.setValue(ds.nameToIndex("id"), "2");
//		row.setValue(ds.nameToIndex("name"), "树");
//		ds.addRow(row);
	}

	private void fillCtrlDs(Dataset ds, PageMeta pagemeta, String viewId) {
		fillDefCtrlDs(ds, pagemeta, viewId);
		fillUndefCtrlDs(ds);
	}

	private void fillDefCtrlDs(Dataset ds, PageMeta pagemeta, String viewId) {
		Row row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), DEFINED);
		row.setValue(ds.nameToIndex("name"), "已定义控件");
		ds.addRow(row);
		
		if(viewId != null){
			LfwWidget widget = pagemeta.getWidget(viewId);
			if(widget != null ){
				List<String> ctrlTypes = getCtrlTypes();
				
				if(widget.getViewMenus() != null){
					addMenuToRow(ds, widget.getViewMenus().getMenuBars());
					if(widget.getViewMenus().getContextMenus() != null){
						addContextMenuToRow(ds, widget.getViewMenus().getContextMenus());
					}
				}
				
				addCompToRow(ds, ctrlTypes, widget);
			}
		}
		else{
			MenubarComp[] menus = pagemeta.getViewMenus().getMenuBars();
			addMenuToRow(ds, menus);
			LfwWidget[] widgets = pagemeta.getWidgets();
			if(widgets != null && widgets.length > 0){
				for(LfwWidget widget : widgets){
					List<String> ctrlTypes = getCtrlTypes();
					if(widget.getViewMenus() != null){
						addMenuToRow(ds, widget.getViewMenus().getMenuBars());
						if(widget.getViewMenus().getContextMenus() != null){
							addContextMenuToRow(ds,  widget.getViewMenus().getContextMenus());
						}
					}
					addCompToRow(ds, ctrlTypes, widget);
				}
			}
		}
	}

	private void fillUndefCtrlDs(Dataset ds) {
		Row row;
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), UNDEFINED);
		row.setValue(ds.nameToIndex("name"), "未定义控件");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("name"), "按钮");
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_BUTTON);
		row.setValue(ds.nameToIndex("type2"), LfwPageContext.SOURCE_TYPE_BUTTON);
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("name"), "图片");
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_IMAGECOMP);
		row.setValue(ds.nameToIndex("type2"), LfwPageContext.SOURCE_TYPE_IMAGECOMP);
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("name"), "标签");
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_LABEL);
		row.setValue(ds.nameToIndex("type2"), LfwPageContext.SOURCE_TYPE_LABEL);
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("name"), "菜单");
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_MENUBAR);
		row.setValue(ds.nameToIndex("type2"), LfwPageContext.SOURCE_TYPE_MENUBAR);
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("name"), "超链接");
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_LINKCOMP);
		row.setValue(ds.nameToIndex("type2"), LfwPageContext.SOURCE_TYPE_LINKCOMP);
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("name"), "进度条");
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_PROGRESS_BAR);
		row.setValue(ds.nameToIndex("type2"), LfwPageContext.SOURCE_TYPE_PROGRESS_BAR);
//		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("name"), "下拉输入控件");
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_TEXT);
		row.setValue(ds.nameToIndex("type2"), EditorTypeConst.COMBODATA);
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("name"), "日期输入控件");
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_TEXT);
		row.setValue(ds.nameToIndex("type2"),EditorTypeConst.DATETEXT);
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_TEXT);
		row.setValue(ds.nameToIndex("type2"),EditorTypeConst.DECIMALTEXT);
		row.setValue(ds.nameToIndex("name"), "浮点输入控件");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_TEXT);
		row.setValue(ds.nameToIndex("type2"),EditorTypeConst.INTEGERTEXT);
		row.setValue(ds.nameToIndex("name"), "整型输入控件");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_TEXT);
		row.setValue(ds.nameToIndex("type2"),EditorTypeConst.STRINGTEXT);
		row.setValue(ds.nameToIndex("name"), "多语输入控件");
		ds.addRow(row);
		
//		row = ds.getEmptyRow();
//		row.setValue(ds.nameToIndex("id"), "");
//		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
//		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_TEXT);
//		row.setValue(ds.nameToIndex("type2"),EditorTypeConst.REFERENCE);
//		row.setValue(ds.nameToIndex("name"), "参照输入控件");
//		ds.addRow(row);
//		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_TEXT);
		row.setValue(ds.nameToIndex("type2"),EditorTypeConst.STRINGTEXT);
		row.setValue(ds.nameToIndex("name"), "字符输入控件");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_IFRAME);
		row.setValue(ds.nameToIndex("type2"), LfwPageContext.SOURCE_TYPE_IFRAME);
		row.setValue(ds.nameToIndex("name"), "子窗口控件");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_SELF_DEF_COMP);
		row.setValue(ds.nameToIndex("type2"),LfwPageContext.SOURCE_TYPE_SELF_DEF_COMP);
		row.setValue(ds.nameToIndex("name"), "自定义控件");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_HTMLCONTENT);
		row.setValue(ds.nameToIndex("type2"),LfwPageContext.SOURCE_TYPE_HTMLCONTENT);
		row.setValue(ds.nameToIndex("name"), "WebPart控件");
		ds.addRow(row);
	}

	private void addContextMenuToRow(Dataset ds, ContextMenuComp[] contextMenus) {
		if(contextMenus != null){
			for(int i = 0; i < contextMenus.length; i++){
				Row row = ds.getEmptyRow();
				ContextMenuComp menu = contextMenus[i];
				row.setValue(ds.nameToIndex("id"), menu.getId());
				row.setValue(ds.nameToIndex("name"), "右键菜单");
				row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_CONTEXTMENU_MENUITEM);
				row.setValue(ds.nameToIndex("pid"), DEFINED);
				ds.addRow(row);
			}
		}
		
	}

	private void addMenuToRow(Dataset ds, MenubarComp[] menus) {
		if(menus != null){
			for(int i = 0; i < menus.length; i++){
				Row row = ds.getEmptyRow();
				MenubarComp menu = menus[i];
				row.setValue(ds.nameToIndex("id"), menu.getId());
				row.setValue(ds.nameToIndex("name"), "菜单");
				row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_MENUBAR);
				row.setValue(ds.nameToIndex("pid"), DEFINED);
				ds.addRow(row);
			}
		}
	}

	private void fillLayoutDs(Dataset ds) {
		Row row = ds.getEmptyRow();
//		row.setValue(ds.nameToIndex("id"), PaConstant.LAYOUT_ABS);
//		row.setValue(ds.nameToIndex("name"), "绝对布局容器");
//		ds.addRow(row);
		
//		row = ds.getEmptyRow();
//		row.setValue(ds.nameToIndex("id"), LfwPageContext.SOURCE_TYPE_BORDERLAYOUT);
//		row.setValue(ds.nameToIndex("name"), "Border容器");
//		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LfwPageContext.SOURCE_TYPE_SPLITERLAYOUT);
		row.setValue(ds.nameToIndex("name"), "Spliter布局");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LfwPageContext.SOURCE_TYPE_FLOWHLAYOUT);
		row.setValue(ds.nameToIndex("name"), "横向流式容器");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LfwPageContext.SOURCE_TYPE_FLOWVLAYOUT);
		row.setValue(ds.nameToIndex("name"), "纵向流式容器");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LfwPageContext.SOURCE_TYPE_OUTLOOKBAR);
		row.setValue(ds.nameToIndex("name"), "百叶窗容器");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LfwPageContext.SOURCE_TYPE_TAG);
		row.setValue(ds.nameToIndex("name"), "页签容器");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LfwPageContext.SOURCE_TYPE_CARDLAYOUT);
		row.setValue(ds.nameToIndex("name"), "卡片容器");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LfwPageContext.SOURCE_TYPE_BORDER);
		row.setValue(ds.nameToIndex("name"), "边框");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LfwPageContext.SOURCE_TYPE_PANELLAYOUT);
		row.setValue(ds.nameToIndex("name"), "PANEL容器");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LfwPageContext.SOURCE_TYPE_CANVASLAYOUT);
		row.setValue(ds.nameToIndex("name"), "Canvas容器");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), LfwPageContext.SOURCE_TYPE_GRIDLAYOUT);
		row.setValue(ds.nameToIndex("name"), "Grid布局");
		ds.addRow(row);
		
	}

	private void fillBindDs(Dataset ds, PageMeta pagemeta, String viewId) {
		fillDefBindDs(ds, pagemeta, viewId);
//		fillUndefBindDs(ds);
	}

	private void fillDefBindDs(Dataset ds, PageMeta pagemeta, String viewId) {
		Row row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), DEFINED);
		row.setValue(ds.nameToIndex("name"), "已定义数据绑定控件");
		ds.addRow(row);
		
		if(viewId != null){
			LfwWidget widget = pagemeta.getWidget(viewId);
			if(widget != null){
				List<String> bindtypes = getBindTypes();
				addCompToRow(ds, bindtypes, widget);
			}
		}
		else{
			LfwWidget[] widgets = pagemeta.getWidgets();
			if(widgets != null && widgets.length > 0){
				List<String> bindtypes = getBindTypes();
				for(int i = 0; i < widgets.length; i++){
					LfwWidget widget = widgets[i];
					addCompToRow(ds, bindtypes, widget);
				}		
			}
		}
	}

	private void fillUndefBindDs(Dataset ds) {
		Row row;
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), UNDEFINED);
		row.setValue(ds.nameToIndex("name"), "未定义数据绑定控件");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_GRID);
		row.setValue(ds.nameToIndex("name"), "表格");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_FORMCOMP);
		row.setValue(ds.nameToIndex("name"), "表单");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_EXCEL);
		row.setValue(ds.nameToIndex("name"), "EXCEL");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("type"), LfwPageContext.SOURCE_TYPE_TREE);
		row.setValue(ds.nameToIndex("name"), "树");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("type"), PaConstant.BINDING_CHART_PIE);
		row.setValue(ds.nameToIndex("name"), "饼图");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("type"), PaConstant.BINDING_CHART_ZHU);
		row.setValue(ds.nameToIndex("name"), "柱图");
		ds.addRow(row);
		
		row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), "");
		row.setValue(ds.nameToIndex("pid"), UNDEFINED);
		row.setValue(ds.nameToIndex("type"), PaConstant.BINDING_CHART_WATCH);
		row.setValue(ds.nameToIndex("name"), "仪表盘");
		ds.addRow(row);
	}
		

	private void addCompToRow(Dataset ds, List<String> bindtypes,
			LfwWidget widget) {
		Row row = null;
		Map<String, WebComponent> map = widget.getViewComponents().getComponentsMap();
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			row = ds.getEmptyRow();
			String key = (String) it.next();
			String type = map.get(key).getClass().getSimpleName();
			
			//过滤调临时存储的form
			if(key.contains("_newFrm") && type.equals("FormComp")){
				
			}
			if(bindtypes.contains(type) && !(key.contains("_newFrm") && type.equals("FormComp"))){
				
				row.setValue(ds.nameToIndex("id"), key);
				row.setValue(ds.nameToIndex("name"), getLabelName(type));
				row.setValue(ds.nameToIndex("type"),  getTypeByWebType(type));
				row.setValue(ds.nameToIndex("pid"), DEFINED);
				ds.addRow(row);
			}
		}
	}
		

	private List<String> getBindTypes() {
		String[] bindTypes = {"GridComp", "TreeViewComp", "FormComp", "ExcelComp", "ChartComp"};
		List<String> bindtypes = new ArrayList<String>();
		for(int i = 0; i < bindTypes.length; i++){
			bindtypes.add(bindTypes[i]);
		}
		return bindtypes;
	}
	
	private List<String> getCtrlTypes(){
		String[] ctrlTypes = {"ButtonComp", "ImageComp","TextComp", "LabelComp", "MenubarComp", 
								"SelfDefComp", "IFrameComp", "ProgressBarComp", "ReferenceComp",
								"TextAreaComp", "RadioGroupComp", "CheckboxGroupComp", "RadioComp",
								"ComboBoxComp", "CheckBoxComp", "LinkComp", "ToolBarComp", "ChartComp"};
		List<String> ctrltypes = new ArrayList<String>();
		for(int i = 0; i < ctrlTypes.length; i++){
			ctrltypes.add(ctrlTypes[i]);
		}
		return ctrltypes;
	}

	private String getTypeByWebType(String webType){
		Map<String, String> map = new HashMap<String, String>();
		map.put("GridComp", LfwPageContext.SOURCE_TYPE_GRID);
		map.put("FormComp", LfwPageContext.SOURCE_TYPE_FORMCOMP);
		map.put("TreeViewComp", LfwPageContext.SOURCE_TYPE_TREE);
		map.put("ExcelComp", LfwPageContext.SOURCE_TYPE_EXCEL);
		map.put("ButtonComp", LfwPageContext.SOURCE_TYPE_BUTTON);
		map.put("ImageComp", LfwPageContext.SOURCE_TYPE_IMAGECOMP);
		map.put("LabelComp", LfwPageContext.SOURCE_TYPE_LABEL);
		map.put("MenubarComp", LfwPageContext.SOURCE_TYPE_MENUBAR_MENUITEM);
		map.put("IFrameComp", LfwPageContext.SOURCE_TYPE_IFRAME);
		map.put("ProgressBarComp", LfwPageContext.SOURCE_TYPE_PROGRESS_BAR);
		map.put("TextComp", LfwPageContext.SOURCE_TYPE_TEXT);
		map.put("SelfDefComp", LfwPageContext.SOURCE_TYPE_SELF_DEF_COMP);
		map.put("ReferenceComp", LfwPageContext.SOURCE_TYPE_TEXT);
		map.put("TextAreaComp", LfwPageContext.SOURCE_TYPE_TEXT);
		map.put("RadioGroupComp", LfwPageContext.SOURCE_TYPE_TEXT);
		map.put("RadioComp", LfwPageContext.SOURCE_TYPE_TEXT);
		map.put("CheckboxGroupComp", LfwPageContext.SOURCE_TYPE_TEXT);
		map.put("CheckBoxComp", LfwPageContext.SOURCE_TYPE_TEXT);
		map.put("ComboBoxComp", LfwPageContext.SOURCE_TYPE_TEXT);
		map.put("LinkComp", LfwPageContext.SOURCE_TYPE_LINKCOMP);
		map.put("ToolBarComp", LfwPageContext.SOURCE_TYPE_TOOLBAR_BUTTON);
		map.put("ChartComp", LfwPageContext.SOURCE_TYPE_CHART);
		
		return map.get(webType);
	}
	private String getLabelName(String type) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("GridComp", "表格");
		map.put("FormComp", "表单");
		map.put("TreeViewComp", "树");
		map.put("ExcelComp", "Excel");
		map.put("ButtonComp", "按钮");
		map.put("ImageComp", "图片");
		map.put("LabelComp", "标签");
		map.put("ProgressBarComp", "进度条");
		map.put("MenubarComp", "菜单");
		map.put("IFrameComp", "子窗口");
		map.put("TextComp", "文本框");
		map.put("ChartComp", "图表");
		return map.get(type);
	}


	public void onAfterRowSelect(DatasetEvent e) {
		Dataset ds = e.getSource();
		ds.setRowSelectIndex(-1);
	}
	
	private void waitForInit(String pageId, String sessionId) {
		int count = 0;
		PageMeta pagemeta = null;
		while(pagemeta == null && count < 5){
			IPaEditorService service = NCLocator.getInstance().lookup(IPaEditorService.class);
			pagemeta = service.getOriPageMeta(pageId, sessionId);
			if(pagemeta == null){
				count ++;
				try {
					Thread.sleep(1000);
				} 
				catch (InterruptedException e) {
					LfwLogger.error(e);
				}
			}
		}
	}

	

}
