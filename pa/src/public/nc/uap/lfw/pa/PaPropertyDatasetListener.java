/**
 * 
 */
package nc.uap.lfw.pa;

import java.lang.reflect.Method;
import java.util.Iterator;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowData;
import nc.uap.lfw.core.event.ScriptEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.design.itf.IPaEditorService;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIFormElement;
import nc.uap.lfw.jsp.uimeta.UIGridLayout;
import nc.uap.lfw.jsp.uimeta.UIGridPanel;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.pa.info.BaseInfo;
import nc.uap.lfw.pa.info.ComboPropertyInfo;
import nc.uap.lfw.pa.info.IPropertyInfo;
import nc.uap.lfw.pa.info.InfoCategory;
import nc.uap.lfw.ra.listener.RaDynamicScriptListener;
import nc.vo.pub.lang.UFBoolean;

/**
 * @author wupeng1
 * @version 6.0 2011-8-24
 * @since 1.6
 */
public class PaPropertyDatasetListener {
	public static final String FIELD_CHILDID = "childid";

	public void handlerEvent(ScriptEvent event) {
	
		//获取从前台传过来的数据属性
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		String oper = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_OPER));
		//初始化
		setFormElementInvisible();
		
		if(!oper.equals(RaDynamicScriptListener.INIT)){
			ctx.getApplicationContext().addExecScript("setEditorState();");
		}

		focusNavDs();
		/**
		 * 得到当前页面的settings Widget，以及数据集和控件
		 */
		
		if(oper.equals(RaDynamicScriptListener.DELETE)){
			deleteHandler();
		}
		else if(oper.equals(RaDynamicScriptListener.ADD)){
			addHandler();
		}
		else if(oper.equals(RaDynamicScriptListener.INIT)){
			boolean sign = focusSelectDs();
			if(!sign)
				return;
			
			setFormElementVisible();
		}
	}

	private void focusNavDs() {
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		String id = null;
		String uiId = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_UIID));
		String subuiId = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_SUBUIID));
		if(uiId == null && subuiId == null)
			return;
		
		if(subuiId != null)
			id = subuiId;
		else
			id = uiId;
		
		if(id == null)
			return;
		
		Dataset dsStruct = LfwRuntimeEnvironment.getWebContext().getPageMeta().getWidget("data").getViewModels().getDataset("ds_struct");
		if(dsStruct.getCurrentRowData() != null){
			Row[] rows = dsStruct.getCurrentRowData().getRows();
			if(rows == null || rows.length == 0)
				return;
			for(int i = 0; i < rows.length; i++){
				Row row = rows[i];
				int idIndex = dsStruct.nameToIndex("id");
				String value = (String) row.getValue(idIndex);
				if(value != null && value.equals(id)){
					dsStruct.setRowSelectIndex(i);
					return;
				}
			}
		}
		
	}
	
	private boolean focusSelectDs() {
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		String eleId = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_ELEID));
		String uiId = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_UIID));
		String subuiId = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_SUBUIID));
		String subEleId = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_SUBELEID));
		String widgetId = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_WIDGETID));
		String colIndex = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_COLINDEX));
		String rowIndex = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_ROWINDEX));
		IPaEditorService ipaService = NCLocator.getInstance().lookup(IPaEditorService.class);
		String pageId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("_pageId");
		String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
		PageMeta pagemeta = ipaService.getOriPageMeta(pageId, sessionId);
		Dataset dsMiddle = LfwRuntimeEnvironment.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_middle");;
		String type = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_TYPE));
		
		if(eleId == null && uiId == null && subEleId == null && subuiId == null)
			return false;
		String pid = (uiId == null) ? eleId : uiId;
		String cid = (subuiId == null) ? subEleId : subuiId;
		String itemId = pid;
		if(cid != null)
			itemId += "." + cid;
		boolean find = false;
		if(dsMiddle.getCurrentRowData() != null){
			Row[] rows = dsMiddle.getCurrentRowData().getRows();
			for(int i = 0; i < rows.length; i++){
				Row row = rows[i];
				int idIndex = dsMiddle.nameToIndex(FIELD_CHILDID);
				String value = (String) row.getValue(idIndex);
				if(value != null && value.equals(itemId)){
					dsMiddle.setRowSelectIndex(i);
					find = true;
					break;
				}
			}
		}
		if(!find){
			UIMeta uimeta = ipaService.getOriUIMeta(pageId, sessionId);
			
			UIElement uiEle = null;
			WebElement webEle = null;
			
			//表格数据
			if(rowIndex != null){
				uiEle = getGridElement(uimeta, uiId, rowIndex, colIndex);
			}
			else{
				if(subuiId != null){
					uiEle = UIElementFinder.findElementById(uimeta, uiId, subuiId);
				}
				else{
					uiEle = UIElementFinder.findElementById(uimeta, uiId);
				}
				
				if(eleId != null){
					if(subEleId != null)
						webEle = getWebElement(pagemeta, widgetId, eleId, subEleId);
					else
						webEle = getWebElement(pagemeta, widgetId, eleId);
				}
			}
			
			doAdd(uiEle, webEle, pid, cid, widgetId, uimeta, type);
			//再次调用
			focusSelectDs();
		}
		
		
		return true;
	}

	private UIElement getGridElement(UIMeta uimeta, String uiId,
			String rowIndex, String colIndex) {
		UIGridLayout grid = (UIGridLayout) UIElementFinder.findElementById(uimeta, uiId);
		UIGridPanel panel = (UIGridPanel) grid.getGridCell(Integer.valueOf(rowIndex), Integer.valueOf(colIndex));
		return panel;
	}

	private WebElement getWebElement(PageMeta pagemeta, String widgetId, String eleId, String subEleId) {
		WebElement comp = getWebElement(pagemeta, widgetId, eleId);
		if(comp instanceof GridComp){
			Iterator<IGridColumn> cit = ((GridComp)comp).getColumnList().iterator();
			while(cit.hasNext()){
				IGridColumn col = cit.next();
				if(col instanceof GridColumn){
					if(((GridColumn)col).getId().equals(subEleId))
						return (GridColumn) col;
				}
			}
		}
		else if(comp instanceof FormComp){
			FormElement formEle = ((FormComp) comp).getElementById(subEleId);
			if(formEle != null)
				return formEle;
		}
		return null;
	}

	private void doAdd(UIElement uiEle, WebElement webEle, String uiId, String subuiId, String widgetId, UIMeta uimeta, String type) {
		if(webEle != null){
			String itemId = uiId;
			String pid = null;
			if(subuiId != null){
				pid = uiId;
				itemId += "." + subuiId;
			}
			else{
				pid = getParentId(uiEle, uimeta);
			}
			setDsInfo(widgetId, type, webEle, uiEle, PaConstant.DS_UPDATE, itemId, pid);
			setNavTreeDs(pid, itemId, "add");
			setCtrlCompDs(type, itemId);
		}
		else{
			String itemId = uiId;
			if(subuiId != null)
				itemId += "." + subuiId;
			UIElement parentEle = UIElementFinder.findParent(uimeta, uiEle);
			String prtId = null;
			if(parentEle != null){
				if(parentEle instanceof UILayoutPanel){
					if(((UILayoutPanel) parentEle).getLayout() == null){
						prtId = parentEle.getId();
					}
					else
						prtId = ((UILayoutPanel)parentEle).getLayout().getId() + "." + parentEle.getId();
				}
				else
					prtId = parentEle.getId();
			}
			
			if(webEle == null && uiEle == null)
				throw new LfwRuntimeException("没有取到对应 元素");
			type = getTypeByEle(uiEle);
			setDsInfo(widgetId, type, null, uiEle, PaConstant.DS_UPDATE, itemId, prtId);
			if(parentEle == null)
				setNavTreeDs(null, uiId, "add");
			else
				setNavTreeDs(parentEle.getId(), uiId, "add");
			if(uiEle instanceof UILayout){
				Iterator<UILayoutPanel> it = ((UILayout)uiEle).getPanelList().iterator();
				while(it.hasNext()){
					UILayoutPanel panel = it.next();
					String cItemId = itemId + "." + panel.getId();
					type = getTypeByEle(panel);
					setDsInfo(widgetId, type, null, panel, PaConstant.DS_UPDATE, cItemId, itemId);
					setNavTreeDs(itemId, panel.getId(), "add");
				}
			}
		}
	}
	
	private void setCtrlCompDs(String type, String itemId) {
		LfwWidget widget =  LfwRuntimeEnvironment.getWebContext().getPageMeta().getWidget("data");
		Dataset currDs = widget.getViewModels().getDataset("ctrlds");
		/**
		 * 添加新建控件的ds
		 */
		
		//添加类型！
//		String type = webEle.
		Row row = currDs.getEmptyRow();
		row.setValue(currDs.nameToIndex("id"), itemId);
		row.setValue(currDs.nameToIndex("name"), PaConstantMap.labelNameMap.get(type));
		row.setValue(currDs.nameToIndex("type"), type);
		row.setValue(currDs.nameToIndex("pid"), " ");
		currDs.addRow(row);
		currDs.setSelectedIndex(-1);
		
	}

	@SuppressWarnings("restriction")
	private void addHandler() {
		IPaEditorService ipaService = NCLocator.getInstance().lookup(IPaEditorService.class);
		String pageId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("_pageId");
		String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
		PageMeta pagemeta = ipaService.getOriPageMeta(pageId, sessionId);
		UIMeta uimeta = ipaService.getOriUIMeta(pageId, sessionId);
		
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		String widgetId = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_WIDGETID));
		String eleId = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_ELEID));
		String uiId = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_UIID));
		String subuiId = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_SUBUIID));
		String type = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_TYPE));
		
		UIElement uiEle = null;
		WebElement webEle = null;
		if(subuiId != null){
			uiEle = UIElementFinder.findElementById(uimeta, uiId, subuiId);
		}
		else{
			uiEle = UIElementFinder.findElementById(uimeta, uiId);
		}
		
		if(eleId != null){
			webEle = getWebElement(pagemeta, widgetId, eleId);
		}
		
		doAdd(uiEle, webEle, uiId, subuiId, widgetId, uimeta, type);
	}


	private void deleteHandler() {
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		String uiId = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_UIID));
		String subuiId = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_SUBUIID));
		IPaEditorService ipaService = NCLocator.getInstance().lookup(IPaEditorService.class);
		String pageId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("_pageId");
		String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
		PageMeta pagemeta = ipaService.getOriPageMeta(pageId, sessionId);
		UIMeta uimeta = ipaService.getOriUIMeta(pageId, sessionId);
		
		LfwWidget widget = LfwRuntimeEnvironment.getWebContext().getPageMeta().getWidget("settings");
		Dataset ds = widget.getViewModels().getDataset("ds_middle");
		
		LfwWidget navWidget = LfwRuntimeEnvironment.getWebContext().getPageMeta().getWidget("data");
		Dataset navDs = navWidget.getViewModels().getDataset("ds_struct");
		
		
		String itemId = uiId;
		if(subuiId != null && !subuiId.equals("")){
			itemId += "." + subuiId;
			removeNavRow(subuiId, navDs);
			
		}
		else{
			UIElement uiEle = UIElementFinder.findElementById(uimeta, uiId);
			if(uiEle instanceof UILayout){
				Iterator<UILayoutPanel> it = ((UILayout)uiEle).getPanelList().iterator();
				while(it.hasNext()){
					UILayoutPanel panel = it.next();
					String cItemId = itemId + "." + panel.getId();
					removeRow(cItemId, ds);
					removeNavRow(panel.getId(), navDs);
					
				}
			}
		}
		
		removeRow(itemId, ds);
	}
	
	private void removeNavRow(String itemId, Dataset navDs) {
		RowData rd = navDs.getCurrentRowData();
		if(rd == null)
			return;
		Row[] rows = rd.getRows();
		if(rows == null || rows.length == 0)
			return;
		for(int i = 0; i < rows.length; i++){
			Row row = rows[i];
			int idIndex = navDs.nameToIndex("id");
			String value = (String) row.getValue(idIndex);
			if(value != null && value.equals(itemId)){
				navDs.removeRow(row);
			}
		}
		
	}
	
	private void removeRow(String itemId, Dataset ds){
		RowData rd = ds.getCurrentRowData();
		if(rd == null){
			return;
		}
		Row[] rows = rd.getRows();
		for (int i = 0; i < rows.length; i++) {
			Row row = rows[i];
			int idIndex = ds.nameToIndex(FIELD_CHILDID);
			String value = (String) row.getValue(idIndex);
			if(itemId != null && itemId.equals(value)){
				ds.removeRow(row);
			}
			ds.setSelectedIndex(-1);
		}
	}

	private String getParentId(UIElement uiEle, UIMeta uimeta){
		String prtId = null;
		UIElement parentEle = UIElementFinder.findParent(uimeta, uiEle);
		if(parentEle != null){
			prtId = getElementId(parentEle);
		}
		return prtId;
	}
	
//	@SuppressWarnings("restriction")
//	private void addChildDsInfo(String widgetId, UIElement uiEle, String dsState, String prtId) {
//		UIElement cele = null;
//		if(uiEle instanceof UIWidget){
//			cele = ((UIWidget)uiEle).getUimeta().getElement();
//		}
//		else
//			cele = ((UILayoutPanel) uiEle).getElement();
//			
//		if(cele != null){
//			if(cele instanceof UIGridLayout){
//				String ctype = getTypeByEle(cele);
//				setDsInfo(widgetId, ctype, cele, dsState, prtId);
//				prtId = getElementId(cele);
//				List<UILayoutPanel> gridRow = ((UIGridLayout) cele).getPanelList();
//				if(gridRow != null && gridRow.size() > 0){
//					for(UILayoutPanel rowPanel : gridRow){
//						UIGridRowLayout row = ((UIGridRowPanel)rowPanel).getRow();
//						String rtype = getTypeByEle(row);
//						setDsInfo(widgetId, rtype, row, dsState, prtId);
//						List<UILayoutPanel> panelList = row.getPanelList();
//						if(panelList != null && panelList.size() > 0){
//							String rowprtId = row.getId();
//							for(UILayoutPanel panel : panelList){
//								String pType = getTypeByEle(panel);
//								setDsInfo(widgetId, pType, panel, dsState, rowprtId);
//								String pprtId = getElementId(panel);
//								UIElement pEle = panel.getElement();
//								if(pEle != null){
//									addChildDsInfo(widgetId, panel, dsState, pprtId);
//								}
//							}
//						}
//					}
//				}
//			}
//			
//			else if(cele instanceof UILayout){
//				String ctype = getTypeByEle(cele);
//				setDsInfo(widgetId, ctype, cele, dsState, prtId);
//				//重新获取父id
//				prtId = getElementId(cele);
//				List<UILayoutPanel> panelList = ((UILayout) cele).getPanelList();
//				if(panelList != null && panelList.size() > 0){
//					for(UILayoutPanel panel: panelList){
//						String ptype = getTypeByEle(panel);
//						setDsInfo(widgetId, ptype, panel, dsState, prtId);
//						String pId = getElementId(panel);
//						if(panel != null)
//							addChildDsInfo(widgetId, panel, dsState, pId);
//					}
//				}
//			}
//			else if(cele instanceof UIComponent){
//				String ctype = null;
//				if(cele instanceof UITextField){
//					UITextField utf = (UITextField) cele;
//					ctype = PaConstantMap.web2ui.get(utf.getType());
//				}
//				else
//					ctype = getTypeByEle(cele);
//				String celeId = getElementId(cele);
//				
//				WebElement webEle = null;
//				IPaEditorService ipaService = NCLocator.getInstance().lookup(IPaEditorService.class);
//				String pageId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("_pageId");
//				String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
//				PageMeta pagemeta = ipaService.getOutPageMeta(pageId, sessionId);
//				if(ctype != null && ctype.equals(LfwPageContext.SOURCE_TYPE_FORMELE)){
//					webEle = getFormElement(pagemeta,(UIFormElement)cele);
//				}else{
//					webEle = getWebElement(pagemeta , widgetId, celeId);
//				}
//				
//				setDsInfo(widgetId, ctype, webEle, cele, dsState, prtId);
//			}
//		}
//	}
	
	public WebElement getFormElement(PageMeta pagemeta ,UIFormElement fe){
		FormComp formc = (FormComp)pagemeta.getWidget(fe.getWidgetId()).getViewComponents().getComponent(fe.getFormId());
		return formc.getElementById(fe.getId());
	}


//	/**
//	 * @param uimeta
//	 * @param ele
//	 * @return
//	 */
//	private List<Object> getBrotherEle(IUIMeta uimeta, Object ele, Object pEle) {
//		List<Object> bros = new ArrayList<Object>();
//		
//		String id = getElementId(ele);
//
//		if(pEle instanceof UILayout){
//			UILayout lpEle = (UILayout) pEle;
//			
//			if(lpEle instanceof UIGridLayout){
//				UIGridLayout gridRow = (UIGridLayout) lpEle;
//				List<UILayoutPanel> listRow = gridRow.getPanelList();
//				if(listRow != null && listRow.size() > 0){
//					for(UILayoutPanel rowPanel : listRow){
//						UIGridRowLayout row = ((UIGridRowPanel)rowPanel).getRow();
//						String rowId = row.getId();
//						if(rowId != null && !rowId.equals(id)){
//							bros.add(row);
//						}
//					}
//				}
//				
//			}
//			else{
//				List<UILayoutPanel> list = lpEle.getPanelList();
//				if(list != null && list.size() > 0){
//					for(UILayoutPanel panel : list){
//						String panelId = (String) panel.getAttribute(UIElement.ID);
//						if(panelId != null && !panelId.equals(id)){
//							bros.add(panel);
//						}
//					}
//				}
//			}
//			
//				
//		}
//		
//		else if(pEle instanceof UILayoutPanel){
//			UILayoutPanel lpEle = (UILayoutPanel) pEle;
//			UIElement uele = lpEle.getElement();
//			String uid = (String) uele.getAttribute(UIElement.ID);
//			if(uid != null && !uid.equals(id)){
//				bros.add(uele);
//			}
//		}
//		
//		else if(pEle instanceof GridComp){
//			GridComp grid = (GridComp) pEle;
//			List<IGridColumn> list = grid.getColumnList();
//			for(IGridColumn icolumn : list){
//				GridColumn column = (GridColumn) icolumn;
//				if(!column.getId().equals(id))
//					bros.add(column);
//			}
//		}
//		else if(pEle instanceof FormComp){
//			FormComp form = (FormComp) pEle;
//			List<FormElement> list = form.getElementList();
//			for(FormElement formEle : list){
//				if(!formEle.getId().equals(id))
//					bros.add(formEle);
//			}
//		}
//		
//		return bros;
//	}
//	
//	private List<WebElement> getWebChild(WebElement ele) {
//		List<WebElement> childEles = new ArrayList<WebElement>();
//		
//		if(ele instanceof GridComp){
//			List<IGridColumn> gridcolumns = ((GridComp) ele).getColumnList();
//			if(gridcolumns != null && gridcolumns.size() > 0){
//				for(IGridColumn column : gridcolumns){
//					childEles.add((GridColumn)column);
//				}
//			}
//		}
//		else if(ele instanceof FormComp){
//			List<FormElement> formEles = ((FormComp) ele).getElementList();
//			if(formEles != null && formEles.size() > 0){
//				for(FormElement formEle : formEles)
//					childEles.add(formEle);
//			}
//		}
//		return childEles;
//	}
	
	private void setDsInfo(String widgetId, String type, WebElement ele, UIElement uiEle, String dsState, String itemId, String parentId){
		LfwWidget widget = LfwRuntimeEnvironment.getWebContext().getPageMeta().getWidget("settings");
		Dataset ds = widget.getViewModels().getDataset("ds_middle");
		if(type == null){
			throw new LfwRuntimeException("获取类型为空");
		}
		
		if(type.equals(LfwPageContext.SOURCE_TYPE_FORMELE)){
			FormElement formEle = (FormElement) ele; 
			
			if(formEle != null && formEle.getEditorType().equals("Reference"))
				type = LfwPageContext.SOURCE_TYPE_FORMELE + "_ref";
		}
		BaseInfo pbi = InfoCategory.getInfo(type);
		IPropertyInfo[] ipi = pbi.getPropertyInfos();
		
		Row row = ds.getEmptyRow();
		setRowValue(ds, type, uiEle, ele, ipi, row, dsState, widgetId, itemId, parentId);
		ds.addRow(row);
		
		
//		
//		SuperVO pvo = getVOByType(type);
//		setVoValueByEle(ele, uiEle, pvo, ipi, widgetId, eleId);
//		
//		pvo = getVOByCondtion(pvo,widgetId, eleId);
		
//		int rowIndex = rowExist(ds, widgetId, eleId); 
//		Row row = null;
//		if(rowIndex == -1){
//			row = ds.getEmptyRow();
//			setRowValue(ds, type, pvo, eleId, ipi, row, dsState,widgetId, parentId);
//			ds.addRow(row);
////			rowIndex = ds.getRowCount() - 1;
////			ds.setRowSelectIndex(rowIndex);
//		}
//		else{
////			ds.setRowSelectIndex(rowIndex);
//			if(dsState.equals(PaConstant.DS_DEL)){
//				row = ds.getSelectedRow();
//				int stateIndex = ds.nameToIndex("ds_state");
//				row.setValue(stateIndex, dsState);
//			}
//			else if(dsState.equals(PaConstant.DS_UPDATE)){
//				row = ds.getSelectedRow();
//				int stateIndex = ds.nameToIndex("ds_state");
//				row.setValue(stateIndex, dsState);
//			}
//		}
//		ds.setRowSelectIndex(rowIndex);
	}
//	private void setDsInfo(String widgetId, String type, String itemId, Object ele, String dsState, String parentId){
//		this.setDsInfo(widgetId, type, itemId, ele, null, dsState, parentId);
//	}


	private void setNavTreeDs(String pId, String id, String oper) {
//		private void setCurrDsInfo(String id, String pId, String oper) {
		LfwWidget widget =  LfwRuntimeEnvironment.getWebContext().getPageMeta().getWidget("data");
		Dataset currDs = widget.getViewModels().getDataset("ds_struct");

		int idIndex = currDs.nameToIndex("id");
		int pIdIndex = currDs.nameToIndex("pid");

		if (oper.equals("delete")) {
			Row[] rows = currDs.getCurrentRowData().getRows();
			if (rows != null && rows.length > 0) {
				for (int i = 0; i < rows.length; i++) {
					Row r = rows[i];
					if(r.getValue(idIndex)!= null){
						if (id != null && r.getValue(idIndex).equals(id)){
							currDs.removeRow(r);
							break;
						}
					}
				}
			}
		} 
		else if (oper.equals("add")) {
			boolean isExist = false;
			Row preRow = currDs.getCurrentRowData().getSelectedRow();
			
			
			Row[] rows = currDs.getCurrentRowData().getRows();
			
			for(int i = 0; i < rows.length; i++){
				Row r = rows[i];
				String idValue = (String) r.getValue(idIndex);
				if(idValue != null &&idValue.equals(id)){
					isExist =  true;
					break;
				}
				
			}
			
			if(isExist)
				return;
			
			Row row = currDs.getEmptyRow();
			if(preRow == null){
				row.setValue(idIndex, id);
				row.setValue(pIdIndex, pId);
				currDs.addRow(row);
			}
			else{
				row.setValue(idIndex, id);
				row.setValue(pIdIndex, pId);
				int index = currDs.getCurrentRowData().getRowIndex(preRow);
				currDs.insertRow(index, row);
			}	
		}

//	}
		
	}

	//根据获取VO和属性信息向对应数据集中设置显示数据
	private void setRowValue(Dataset dss, String type, UIElement uiEle, WebElement ele, IPropertyInfo[] pi, Row row, String dsState, String widgetId, String itemId, String parentId) {
//		LfwWidget widget = LfwRuntimeEnvironment.getWebContext().getPageMeta().getWidget("settings");
//		FormComp form = (FormComp) widget.getViewComponents().getComponent("adhintform");
		Field[] fss = dss.getFieldSet().getFields();
		for(int i = 0; i < fss.length; i++){
			Field fdd = fss[i];
			String id = fdd.getId();
//			FormElement formEle = form.getElementById(id);
//			if(!formEle.isVisible() || formEle == null)
//				continue;
			Object value = getElementValue(ele, uiEle, pi, id);
			row.setValue(i, value);
		}
		if(ele == null && uiEle == null)
			return;
		String id = uiEle == null ? ele.getId() : uiEle.getId();
		
		int typeIndex = dss.nameToIndex("comptype");
		row.setValue(typeIndex, type);
		
		int idIndex = dss.nameToIndex("string_ext1");
		row.setValue(idIndex, id);
		
		int widgetIndex = dss.nameToIndex("string_ext2");
		row.setValue(widgetIndex, widgetId);
		
		int stateIndex = dss.nameToIndex("ds_state");
		row.setValue(stateIndex, dsState);
		
		int prtIndex = dss.nameToIndex("parentid");
		row.setValue(prtIndex, parentId);
		
		int itemIdIndex = dss.nameToIndex(FIELD_CHILDID);
		row.setValue(itemIdIndex, itemId);
	}
	
	private Object getElementValue(Object ele, UIElement uiEle, IPropertyInfo[] pi, String id) {
		
		Object newValue = null;
		
		for(int i = 0; i < pi.length; i++){
			IPropertyInfo pinfo = pi[i];
			
			if(pinfo.getDsField().equals(id)){
				Object value = null;
				if(pinfo.getId() != ""){
					try{
						value = getValue(ele, pinfo.getId());
					}
					catch (Exception e){
						if(uiEle != null){
							try{
								value = getValue(uiEle, pinfo.getId());
							}
							catch(Exception e1){
								LfwLogger.error("从UIElement:" + uiEle.getClass().getName() + "中获取:" + pinfo.getId() + "出错");
							}
						}
						else{
							LfwLogger.error("从WebElement:" + ele.getClass().getName() + "中获取:" + pinfo.getId() + "出错");
						}
					}
				}
				if(value instanceof Boolean)
					newValue = UFBoolean.valueOf((Boolean) value);
				else
					newValue = value;
				
				if(newValue != null)
					return newValue;
			}
			
		}
		return newValue;
	}

//	//根据ele中的值设置vo对应值
//	private void setVoValueByEle(Object ele, UIElement uiEle, SuperVO vo, IPropertyInfo[] pi, String widgetId,
//			String eleId) {
//		for(int i = 0; i < pi.length; i++){
//			IPropertyInfo pinfo = pi[i];
//			Object value = null;
//			if(pinfo.getId() != ""){
//				try {
//					value = getValue(ele, pinfo.getId());
//				} 
//				catch (Exception e) {
//					if(uiEle != null){
//						try {
//							value = getValue(uiEle, pinfo.getId());
//						} 
//						catch (Exception e1) {
//							LfwLogger.error("从UIElement:" + uiEle.getClass().getName() + "中获取:" + pinfo.getId() + "出错");
//						} 
//					}
//					else
//						LfwLogger.error("从WebElement:" + ele.getClass().getName() + "中获取:" + pinfo.getId() + "出错");
//				}
//			}
//			Object newValue = null;
//			
//			if(value instanceof Boolean)
//				newValue = UFBoolean.valueOf((Boolean) value);
//			else
//				newValue = value;
//			
//			vo.setAttributeValue(pinfo.getVoField(), newValue);
//		}
//		
//		//将ID设置为唯一属性
//		vo.setAttributeValue("id", eleId);
//		vo.setAttributeValue("widgetid",  widgetId);
////		vo.setAttributeValue("parentid", parentId);
//	}

//	//数据库中是否存在，如果存在将数据库中信息赋给vo
//	private SuperVO getVOByCondtion(SuperVO vo, String widgetId,
//			String eleId) {
//		/**
//		 * 从数据库中得到对应数据并赋值给vo
//		 */
//		//获取组件的父节点，即模板的pk
//		WebSession session = LfwRuntimeEnvironment.getWebContext().getWebSession();
//		String pk_template = session.getOriginalParameter("pk_template");
//		if(pk_template == null)
//			return vo;
//		IPaPersistService service = NCLocator.getInstance().lookup(IPaPersistService.class);
//		
//		String condition = "pk_parent = '" + pk_template + "' and id = '" + eleId +"' and widgetid = '" + widgetId + "'";
//		SuperVO newVO = null;
//		try {
//			newVO = service.getCompVOByClause(vo.getClass(), condition);
//		} catch (PaBusinessException e) {
//			LfwLogger.error(e.getMessage(), e);
////			throw new LfwRuntimeException("查询元素出错！" + e.getMessage());
//		}
//		if(newVO != null){
//			vo = newVO;
//		}
//		return vo;
//	}


//	//根据type获取对应的持久化vo的类
//	private SuperVO getVOByType(String type) {
//		SuperVO vo = null;
//		try {
//			Class<?> VOclazz = PaConstantMap.mappingTable.get(type);
//			vo = (SuperVO)VOclazz.newInstance();
//		} 
//		catch (Exception e) {
//			LfwLogger.error(e.getMessage(), e);
//		}
//		return vo;
//	}

	//根据ele获取其ID
	private String getElementId(Object ele) {
		String uiEleId = null;
		
		if(ele instanceof WebElement){
			uiEleId = ((WebElement) ele).getId();
		}
		if(ele instanceof UIElement){
			uiEleId = (String) ((UIElement)ele).getId();
		}
		return uiEleId;
	}

	/**
	 * 根据页面元素，返回其类型
	 */
	private String getTypeByEle(Object ele) {
		String temp = ele.getClass().getSimpleName();
		
		String type = PaConstantMap.web2ui.get(temp);
		return type;
	}

	/*
	 * 根据ele获取其父节点的元素
	 */
//	private Object getParentEle(IUIMeta uimeta, Object ele) {
//		Object pEle = null;
//		
//		UIMeta meta = (UIMeta) uimeta;
//		if(ele instanceof UIElement){
//			String uid = (String) ((UIElement) ele).getAttribute(UIElement.ID);
//			pEle = findParentEle((UILayout) meta.getElement(), uid);
//		}
//		if(ele instanceof WebElement){
//			String wid = ((WebElement) ele).getId();
//			pEle = findParentEle((UILayout) meta.getElement(), wid);
//			
//		}
//		return pEle;
//	}

//	/**
//	 * @param uimeta
//	 * @param ele
//	 * @return
//	 */
//	private List<Object> getChildEle(IUIMeta uimeta, Object ele) {
//	
//		List<Object> childEles = new ArrayList<Object>();
//		UIMeta meta = (UIMeta) uimeta;
//		if(ele instanceof UIElement){
//			String mode = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(WebConstant.WINDOW_MODE_KEY);
//			if(mode != null && (WebConstant.MODE_PERSONALIZATION).equals(mode)){
//				if(ele instanceof UIWidget){
//					childEles.add(ele);
//					return childEles;
//					
//				}
//			}
//		
//			String uid = (String) ((UIElement) ele).getAttribute(UIElement.ID);
//			
//			List<Object> uiEles = findChildEle((UILayout) meta.getElement(), uid);
//			if(uiEles != null && uiEles.size() > 0){
//				childEles.addAll(uiEles);
//			}
//		}
//		if(ele instanceof GridComp){
//			String wid = ((GridComp) ele).getId();
//			List<Object> webEles = findChildEle((UILayout) meta.getElement(), wid);
//			if(webEles != null && webEles.size() > 0){
//				childEles.addAll(webEles);
//			}
//		}
//		if(ele instanceof FormComp){
//			String fid = ((FormComp) ele).getId();
//			List<Object> webEles = findChildEle((UILayout) meta.getElement(), fid);
//			if(webEles != null && webEles.size() > 0){
//				childEles.addAll(webEles);
//			}
//		}
//		return childEles;
//	}
	
	/**
	 * 设置元素是否可见，根据具体类型设置，对FormElement元素属性进行特殊处理
	 * @param widget
	 * @param pi
	 * @param type
	 * @param uiEle
	 */
	private void setFormElementByProperty(LfwWidget widget, IPropertyInfo[] pi, String type){
		FormComp form = (FormComp) widget.getViewComponents().getComponent("adhintform");
		for(int i = 0; i < pi.length; i++){
			IPropertyInfo pinfo = pi[i];
			if(pinfo.isVisible()){
				String filedName = pinfo.getDsField();
				FormElement fe = form.getElementById(filedName);
				ComboData scd = null;
				if(pinfo instanceof ComboPropertyInfo){
					scd = widget.getViewModels().getComboData(pinfo.getDsField());
					scd.removeAllComboItems();
					String[] keys = ((ComboPropertyInfo) pinfo).getKeys();
					String[] values = ((ComboPropertyInfo) pinfo).getValues();
					
					for(int j = 0; j < keys.length; j++){
						CombItem item = new CombItem();
						item.setText(keys[j]);
						item.setValue(values[j]);
						scd.addCombItem(item);
					}
				}
				
				fe.setLabel(pinfo.getLabel());
				fe.setText(pinfo.getLabel());
				
				fe.setVisible(true);
			}
		}
	}

	//设置所有FormElement为不可见
	private void setFormElementInvisible() {
		LfwWidget widget = LfwRuntimeEnvironment.getWebContext().getPageMeta().getWidget("settings");
		FormComp form = (FormComp) widget.getViewComponents().getComponent("adhintform");
		for(FormElement fc: form.getElementList()){
			fc.setVisible(false);
		}
	}
	
	private void setFormElementVisible() {
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		String type = replaceNullString(ctx.getParameter(RaDynamicScriptListener.PARAM_TYPE));
		BaseInfo pbi = InfoCategory.getInfo(type);
		IPropertyInfo[] ipi = pbi.getPropertyInfos();
		
		LfwWidget widget = LfwRuntimeEnvironment.getWebContext().getPageMeta().getWidget("settings");
		setFormElementByProperty(widget, ipi, type);
	}

	//获取页面元素
	private WebElement getWebElement(PageMeta pagemeta, String widgetId, String eleId) {
		WebElement ele = null;
		if(widgetId != null){
			ele = pagemeta.getWidget(widgetId).getViewComponents().getComponent(eleId);
			if(ele == null){
				ele = pagemeta.getWidget(widgetId).getViewMenus().getMenuBar(eleId);
			}
		}
		else{
			ele = pagemeta.getViewMenus().getMenuBar(eleId);
		}
		return ele;
	}	
			
	private String replaceNullString(String arg){
		if(arg != null){
			if(arg.equals("null") || arg.equals(""))
				arg = null;
		}
		return arg;
	}


    private static String upperCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase();
    }
	public static final Object getValue(Object o , String fieldName) throws Exception{
		if(o instanceof UIElement){
			return ((UIElement) o).getAttribute(fieldName);
		}
		else{
			Method m = null;
			String upCaseFieldName = upperCase(fieldName.substring(0,1))+fieldName.substring(1);
			try {
				m = o.getClass().getMethod("get" + upCaseFieldName, null);
			} 
			catch (SecurityException e) {
				LfwLogger.error(e.getMessage(),e);
			} 
			catch (NoSuchMethodException e) {
				try {
					m = o.getClass().getMethod("is" + upCaseFieldName, null);
				} catch (SecurityException e1) {
					LfwLogger.error(e.getMessage(),e);
				} 
			}
			if(m != null){
				return m.invoke(o, null);
			}
		}
		return null;
	}

}
