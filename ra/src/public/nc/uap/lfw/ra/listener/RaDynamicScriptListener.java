package nc.uap.lfw.ra.listener;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.lfw.core.md.util.LfwMdUtil;
import nc.md.IMDQueryFacade;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.md.model.type.IRefType;
import nc.md.model.type.IType;
import nc.uap.lfw.core.InteractionUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.MDComboDataConf;
import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.core.common.CompIdGenerator;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.WidgetElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldRelation;
import nc.uap.lfw.core.data.MatchField;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.WhereField;
import nc.uap.lfw.core.event.ScriptEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.ScriptServerListener;
import nc.uap.lfw.core.exception.ComboInputItem;
import nc.uap.lfw.core.exception.InputItem;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.util.DefaultUIMetaBuilder;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.LifeCyclePhase;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.RequestLifeCycleContext;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.NCRefNode;
import nc.uap.lfw.core.uimodel.WidgetConfig;
import nc.uap.lfw.design.itf.IPaEditorService;
import nc.uap.lfw.jsp.parser.UIMetaParserUtil;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIFormElement;
import nc.uap.lfw.jsp.uimeta.UIGridLayout;
import nc.uap.lfw.jsp.uimeta.UIGridPanel;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIShutter;
import nc.uap.lfw.jsp.uimeta.UIShutterItem;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import nc.uap.lfw.jsp.uimeta.UITabItem;
import nc.uap.lfw.jsp.uimeta.UITextField;
import nc.uap.lfw.jsp.uimeta.UIWidget;
import nc.uap.lfw.ncadapter.billtemplate.gen.RefNodeGenerator;
import nc.uap.lfw.ncadapter.billtemplate.ref.LfwNCRefUtil;
import nc.uap.lfw.ra.params.UpdateParameter;
import nc.uap.lfw.ra.util.UIElementFactory;
import nc.uap.lfw.reference.app.AppLfwRefGenUtil;
import nc.uap.lfw.reference.base.ILfwRefModel;
import nc.uap.lfw.reference.nc.NcAdapterGridRefModel;
import nc.uap.lfw.reference.nc.NcAdapterTreeGridRefModel;
import nc.uap.lfw.reference.nc.NcAdapterTreeRefModel;
import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.bd.ref.IRefConst;

import org.apache.commons.lang.StringUtils;

public class RaDynamicScriptListener extends ScriptServerListener {

	private static final String ITEMID = "itemid";
	private static final String PRTID = "parentid";
	private static final String COMPTYPE = "comptype";
	private static final String ATTR = "attr";
	private static final String NEWVALUE = "newvalue";
	private static final String OLDVALUE = "oldvalue";
	private static final String ATTRTYPE = "attrtype";
	public static final String PARAM_CURRENT_DROP_DS_ID = "currentDropDsId";
	public static final String PARAM_CURRENT_DROP_OBJ_TYPE2 = "currentDropObjType2";
	public static final String PARAM_CURRENT_DROP_OBJ_TYPE = "currentDropObjType";
	public static final String PARAM_CURRENT_DROP_OBJ = "currentDropObj";
	public static final String PARAM_SUBELEID = "subeleid";
	public static final String PARAM_TYPE = "type";
	public static final String PARAM_SUBUIID = "subuiid";
	public static final String PARAM_UIID = "uiid";
	public static final String PARAM_ELEID = "eleid";
	public static final String PARAM_WIDGETID = "widgetid";
	public static final String PARAM_OPER = "oper";
	public static final String PARAM_ROWINDEX = "rowindex";
	public static final String PARAM_COLINDEX = "colindex";
	public static final String INIT = "init";
	public static final String ADD = "add";
	public static final String DELETE = "delete";
	public static final String PARAM_COMPID = "compid";
	
	public RaDynamicScriptListener(LfwPageContext pageCtx, String widgetId) {
		super(pageCtx, widgetId);
	}

	@Override
	public void handlerEvent(ScriptEvent event) {
		// 获取从前台传过来的数据属性
		LfwPageContext lpc = getGlobalContext();
		String oper = replaceNullString(lpc.getParameter("oper"));
		if (oper.equals("delete")) {
			PageMeta pageMeta = LfwRuntimeEnvironment.getWebContext().getPageMeta();
			UIMeta uiMeta = (UIMeta) LfwRuntimeEnvironment.getWebContext().getUIMeta();
			this.delete(uiMeta, pageMeta, lpc);
		} 
		else if (oper.equals("add")) {
			PageMeta pageMeta = LfwRuntimeEnvironment.getWebContext().getPageMeta();
			UIMeta uiMeta = (UIMeta) LfwRuntimeEnvironment.getWebContext().getUIMeta();
			this.add(uiMeta, pageMeta, lpc);
		} 
		else if (oper.equals("update")) {
			PageMeta pageMeta = LfwRuntimeEnvironment.getWebContext().getPageMeta();
			UIMeta uiMeta = (UIMeta) LfwRuntimeEnvironment.getWebContext().getUIMeta();
			this.update(uiMeta, pageMeta, lpc);
		}
		else if("updateid".equals(oper)){
			PageMeta pageMeta = LfwRuntimeEnvironment.getWebContext().getPageMeta();
			UIMeta uiMeta = (UIMeta) LfwRuntimeEnvironment.getWebContext().getUIMeta();
			this.updateId(uiMeta, pageMeta, lpc);
		}
	}
	/**
	 * 更新ID操作
	 */
	private void updateId(UIMeta uiMeta, PageMeta pageMeta, LfwPageContext lpc){
//		//从UI里面获得原始控件
//		String oldCompId = lpc.getParameter(OLDVALUE);
//		String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
//		IPaEditorService editorService = NCLocator.getInstance().lookup(IPaEditorService.class);
//		
//		UIMeta outUm = editorService.getOutUIMeta(pageMeta.getId(), sessionId);
//		
//		PageMeta outPm = editorService.getOutPageMeta(pageMeta.getId(), sessionId);
//		
//		String currentDropObjType = lpc.getParameter("type");
//		String currentDropObjType2 = null;
//		//暂不支持Form
//		String currentDropDsId = null;
//		String widget = lpc.getParameter("viewId");
//		String newCompId = lpc.getParameter("newValue");
//		UIElement ue = outUm.findChildById(oldCompId);
//		//创建一个新的child
//		UIElement child = getChild(pageMeta, newCompId, currentDropObjType, currentDropObjType2, currentDropDsId, widget);
//		
//		WebComponent wc = (WebComponent)outPm.getWidget(widget).getViewComponents().getComponent(oldCompId).clone();
//		
//		wc.setId(newCompId);
//		pageMeta.getWidget(widget).getViewComponents().addComponent(wc);
//		//加入ori
//		editorService.getOriPageMeta(pageMeta.getId(), sessionId).getWidget(widget).getViewComponents().addComponent((WebComponent)wc.clone());
//
//		editorService.getOutPageMeta(pageMeta.getId(), sessionId).getWidget(widget).getViewComponents().addComponent((WebComponent)wc.clone());
//
//		UIElement uiEle;
//		if(ue != null){
//			UIElement oriPanel = ue.findParent(outUm);
//			if(oriPanel != null){
//				String oriPanelId = (String)oriPanel.getAttribute("id");
//				uiEle = ((UIMeta)LfwRuntimeEnvironment.getWebContext().getUIMeta()).findChildById(oriPanelId);
//				if (child != null) {
//					if (uiEle instanceof UIGridPanel) { // 如果当前选中元素为表格的TD
//						addToGridPanel((UIGridPanel) uiEle, widget, child);
//					} else {
//						this.addElement(uiEle, child);
//					}
//				} else {
//					throw new LfwRuntimeException("获得UIElement失败 ：" + currentDropObjType);
//				}
//			}
//			
//		}
//		
//		getGlobalContext().addExecScript("triggerChangerId('" + oldCompId + "','" + newCompId + "');");
	}
	/**
	 * 控件或者布局属性修改参数处理
	 * 
	 * @param lpc
	 * @return
	 */
	private UpdateParameter getUpdateParameter(LfwPageContext lpc) {
		// 修改属性从前台传递的参数
//		String compId = replaceNullString(lpc.getParameter(ITEMID));
		String prtId = replaceNullString(lpc.getParameter(PRTID));
		String compType = replaceNullString(lpc.getParameter(COMPTYPE));
		String attr = replaceNullString(lpc.getParameter(ATTR));
		String attrType = replaceNullString(lpc.getParameter(ATTRTYPE));
		String oldValue = replaceNullString(lpc.getParameter(OLDVALUE));
		String newValue = replaceNullString(lpc.getParameter(NEWVALUE));
		String viewId = replaceNullString(lpc.getParameter(PARAM_WIDGETID));
		String compId = replaceNullString(lpc.getParameter(PARAM_COMPID));
		
//		proxy.addParam("compid", obj.compid); // 前台组件ID
//		proxy.addParam("prtid", obj.prtid); //父节点ID
//		proxy.addParam("viewid", obj.viewid);
//		proxy.addParam("comptype", obj.type); // 前台组件类型
//		proxy.addParam("attr", obj.attr); // 前台属性名
//		proxy.addParam("attrtype", obj.attrtype); // 属性类型
//		proxy.addParam("oldvalue", obj.oldvalue); // 修改前的值
//		proxy.addParam("newvalue", obj.newvalue); // 修改后的值
		
		UpdateParameter param = new UpdateParameter();
		param.setCompId(compId);
		param.setPrtId(prtId);
		param.setCompType(compType);
		param.setAttr(attr);
		param.setAttrType(attrType);
		param.setOldValue(oldValue);
		param.setNewValue(newValue);
		param.setViewId(viewId);
		return param;
	}

	/**
	 * 执行删除元素的操作
	 * 
	 * @param uiMeta
	 * @param pageMeta
	 * @param lpc
	 */
	private void delete(UIMeta uiMeta, PageMeta pageMeta, LfwPageContext lpc) {
		String widgetId = replaceNullString(lpc.getParameter(PARAM_WIDGETID));
		String eleId = replaceNullString(lpc.getParameter(PARAM_ELEID));
		String uiId = replaceNullString(lpc.getParameter(PARAM_UIID));
		String type = replaceNullString(lpc.getParameter(PARAM_TYPE));
		String subEleId = replaceNullString(lpc.getParameter(PARAM_SUBELEID));
		String subuiId = replaceNullString(lpc.getParameter(PARAM_SUBUIID));
		Object ele = null;
		Object ele2 = null;

		UIElement uiEle = null;//getUIElement((UIMeta) uiMeta, ele);
		if(subuiId != null && !subuiId.equals("")){
			uiEle = UIElementFinder.findElementById(uiMeta, uiId, subuiId);
		}
		else{
			uiEle = UIElementFinder.findElementById(uiMeta, uiId);
		}
		UIElement parent = UIElementFinder.findParent((UIMeta) uiMeta, uiEle);
		
		this.removeUIElement(parent, uiEle);
		
		ParamObject param = new ParamObject();
		param.subuiId = uiEle.getId();
		param.uiId = parent.getId();
		param.type = type;
		param.widgetId = widgetId;
		callServer(param, "delete");
		
//		if(parent instanceof UILayout){
//			List<UILayoutPanel> panelList = ((UILayout) parent).getPanelList();
//			if(panelList == null || panelList.size() == 0){
//				ParamObject prtParam = new ParamObject();
//				prtParam.uiId = parent.getId();
//				prtParam.widgetId = widgetId;
//				callServer(prtParam, "delete");
//			}
//				
//		}
		
//		if(uiEle instanceof UIComponent){
//			ParamObject param1 = new ParamObject();
//			
//			this.removeWebElement(pageMeta, uiEle);
//			
//			param1.eleId = uiEle.getId();
//			param1.type = type;
//			param1.widgetId = widgetId;
//			callServer(param1, "delete");
//			
//		}
//		this.removeUIElement(parent, uiEle);
		
		
	}

	
	private void removeWebElement(PageMeta pm, UIElement uiEle){
		LfwWidget widget = pm.getWidget(uiEle.getWidgetId());
		widget.getViewComponents().removeComponent(uiEle.getId());
	}


	/**
	 * @param uiMeta
	 * @param pageMeta
	 * @param lpc
	 */
	private void add(UIMeta uiMeta, PageMeta pageMeta, LfwPageContext lpc) {
		String widgetId = replaceNullString(lpc.getParameter(PARAM_WIDGETID));
		String eleId = replaceNullString(lpc.getParameter(PARAM_ELEID));
		String uiId = replaceNullString(lpc.getParameter(PARAM_UIID));
		String subuiId = replaceNullString(lpc.getParameter(PARAM_SUBUIID));
		String type = replaceNullString(lpc.getParameter(PARAM_TYPE));
		String subEleId = replaceNullString(lpc.getParameter(PARAM_SUBELEID));
		String currentDropObj = replaceNullString(lpc.getParameter(PARAM_CURRENT_DROP_OBJ));
		String currentDropObjType = replaceNullString(lpc.getParameter(PARAM_CURRENT_DROP_OBJ_TYPE));
		String currentDropObjType2 = replaceNullString(lpc.getParameter(PARAM_CURRENT_DROP_OBJ_TYPE2));
		String currentDropDsId = replaceNullString(lpc.getParameter(PARAM_CURRENT_DROP_DS_ID));
		String colIndex = replaceNullString(lpc.getParameter(RaDynamicScriptListener.PARAM_COLINDEX));
		String rowIndex = replaceNullString(lpc.getParameter(RaDynamicScriptListener.PARAM_ROWINDEX));
		
		UIElement uiEle = null;
		//表格数据
		if(rowIndex != null){
			uiEle = getGridElement(uiMeta, uiId, rowIndex, colIndex);
		}
		else{
			
			if(subuiId != null){
				uiEle = UIElementFinder.findElementById(uiMeta, uiId, subuiId);
			}
			else{
				uiEle = UIElementFinder.findElementById(uiMeta, uiId);
			}
		}
		
		ParamObject param = null;
		//getUIElement((UIMeta) uiMeta, ele);// 当前选中的元素
		if(uiEle instanceof UILayoutPanel){
			if("isPanel".equals(currentDropObjType2)){
//				String preId = this.getEleId(uiEle);
//				uiEle = null;//uiEle.findParent(uiMeta);
				UILayout layout = ((UILayoutPanel)uiEle).getLayout();
				param = addPanelToUILayout(pageMeta, uiMeta, layout, uiEle, widgetId, type, currentDropObjType2);
			}
			else
				param = addToUILayoutPanel(pageMeta, uiMeta, currentDropObj, currentDropObjType, currentDropObjType2, currentDropDsId, (UILayoutPanel) uiEle, widgetId);
		} 
		else if (uiEle instanceof UILayout) {
			param = addPanelToUILayout(pageMeta, uiMeta, uiEle, null, widgetId, type, currentDropObjType2);
		}
		
		if(param != null)
			callServer(param, ADD);
	}
	
	private UIElement getGridElement(UIMeta uimeta, String uiId,
			String rowIndex, String colIndex) {
		UIGridLayout grid = (UIGridLayout) UIElementFinder.findElementById(uimeta, uiId);
		UIGridPanel panel = (UIGridPanel) grid.getGridCell(Integer.valueOf(rowIndex), Integer.valueOf(colIndex));
		return panel;
	}

	private void callServer(ParamObject paramObj, String ope) {
		StringBuffer buf = new StringBuffer();
		buf.append("{widgetid:'").append(paramObj.widgetId).append("',eleid:'").append(paramObj.eleId).append("',subeleid:'")
		.append(paramObj.subEleId).append("',uiid:'").append(paramObj.uiId).append("',subuiid:'").append(paramObj.subuiId).append("',type:'").append(paramObj.type).append("'}");
		String script = "callParent(" + buf.toString() + ", '" + ope + "')";
		getGlobalContext().addExecScript(script);
	}
	
	
	private boolean isPanel(String type){
		return "flowvpanel".equals(type) || "flowhpanel".equals(type);
	}

	/**
	 * 向UILayout中添加panel元素
	 * @param pageMeta
	 * @param currentDropObj
	 * @param currentDropObjType
	 * @param currentDropObjType2
	 * @param currentDropDsId
	 * @param uiEle
	 * @param widget
	 */
	private ParamObject addPanelToUILayout(PageMeta pageMeta, UIMeta uiMeta, UIElement uiEle, UIElement subuiELe, 
			String widget, String type, String currentDropObjType2) {
		
		if(currentDropObjType2 == null )
			return null;
		if("isLayout".equals(currentDropObjType2)){
			if(type == null){
				throw new LfwRuntimeException("添加类型为空");
			}
			if(type.equals(LfwPageContext.SOURCE_TYPE_FLOWHLAYOUT))
				type = LfwPageContext.SOURCE_TYPE_FLOWHPANEL;
			else if(type.equals(LfwPageContext.SOURCE_TYPE_FLOWVLAYOUT))
				type = LfwPageContext.SOURCE_TYPE_FLOWVPANEL;
			else if(type.equals(LfwPageContext.SOURCE_TYPE_CARDLAYOUT))
				type = LfwPageContext.SOURCE_TYPE_CARDPANEL;
			else if(type.equals(LfwPageContext.SOURCE_TYPE_BORDER))
				type = LfwPageContext.SOURCE_TYPE_BORDERTRUE;
			else if(type.equals(LfwPageContext.SOURCE_TYPE_TAG))
				type = LfwPageContext.SOURCE_TYPE_TABITEM;
			else if(type.equals(LfwPageContext.SOURCE_TYPE_OUTLOOKBAR))
				type = LfwPageContext.SOURCE_TYPE_OUTLOOKBAR_ITEM;
		}
		
		if(type != null){
			int count = UIElementFactory.showInputDialog("确认");
			UIElementFactory uf = new UIElementFactory();
			for(int i=0;i<count;i++){
				UIElement child = uf.createUIElement(type, widget);
				String t = randomT(4);
				if(child instanceof UITabItem){
					int lenth = ((UITabComp)uiEle).getPanelList().size();
					int index = lenth + 1;
					child.setAttribute("text", "页签" + index);
				}
				if(child instanceof UIShutterItem){
					int lenth = ((UIShutter)uiEle).getPanelList().size();
					int index = lenth + 1;
					child.setAttribute("text", "item" + index);
				}
				child.setAttribute(UIElement.ID, type+i+t);
				if(child != null){
					this.addUIElement(uiEle, child);
				}else{
					throw new LfwRuntimeException("创建panel失败");
				}
					
			}
		}

		
		ParamObject paramObj = new ParamObject();
		paramObj.widgetId = widget;
		paramObj.uiId = uiEle.getId();
//		paramObj.subuiId = child.getId();
//		if(child instanceof UIComponent)
//			paramObj.eleId = child.getId();
		paramObj.type = type;
		return paramObj;
	}

	/**
	 * 向UILayoutPanel中添加元素
	 * @param pageMeta
	 * @param currentDropObj
	 * @param currentDropObjType
	 * @param currentDropObjType2
	 * @param currentDropDsId
	 * @param uiEle
	 * @param widget
	 */
	private ParamObject addToUILayoutPanel(PageMeta pageMeta, UIMeta uimeta, String currentDropObj, String currentDropObjType, String currentDropObjType2, String currentDropDsId, UILayoutPanel uiEle, String widget) {
		if(uiEle.getElement() != null){
			if(checkPanelElement(uiEle))
				this.removeUIElement(uiEle, uiEle.getElement());
			else
				return null;
		}
		
		UIElement child = getChild(pageMeta, currentDropObj, currentDropObjType, currentDropObjType2, currentDropDsId, widget);
		this.addUIElement(uiEle, child);
		ParamObject paramObj = new ParamObject();
		paramObj.widgetId = widget;
		paramObj.uiId = child.getId();
		if(child instanceof UIComponent)
			paramObj.eleId = child.getId();
		
		if(child instanceof UIFormElement)
			paramObj.type = LfwPageContext.SOURCE_FORMELEMENT;
		else
			paramObj.type = currentDropObjType == null ? currentDropObj : currentDropObjType;
		
		return paramObj;
	}

	/**
	 * @param uiEle
	 *            检查panel中是否有元素，如果有则进行提示
	 */
	private boolean checkPanelElement(UILayoutPanel uiEle) {
		boolean flag = false;
		if (uiEle.getElement() != null) {
			flag = InteractionUtil.showConfirmDialog("确认", "容器中已包含元素，是否替换?");
		}
		return flag;
	}
	
	/**
	 * 删除元素
	 * @param parent
	 * @param child
	 */
	private void removeUIElement(UIElement parent, UIElement child){
		if(parent == null || child == null) 
			return ;
		if(parent instanceof UILayout){
			((UILayout)parent).removePanel((UILayoutPanel) child);
		}
		else{
			((UILayoutPanel)parent).removeElement(child);
		}
		
	}
	
	/**
	 * 添加元素
	 * @param parent
	 * @param child
	 */
	private void addUIElement(UIElement parent,UIElement child){
		if(parent == null || child == null) 
			return ;
		if(parent instanceof UILayout){
			((UILayout)parent).addPanel((UILayoutPanel) child);
		}
		else if(parent instanceof UILayoutPanel){
			((UILayoutPanel)parent).setElement(child);
		}
	}



	/**
	 * @param pageMeta
	 * @param currentDropObjId
	 *            uiElement id
	 * @param currentDropObjType
	 *            类型
	 * @param currentDropObjType2
	 *            小类型， 对 textfield
	 * @param currentDropDsId
	 *            数据集的id
	 * @param widget
	 * @return
	 */
	private UIElement getChild(PageMeta pageMeta, String currentDropObjId, String currentDropObjType, String currentDropObjType2, String currentDropDsId, String widget) {
		if (currentDropObjType != null && !currentDropObjType.equals("")) {
			UIElement child = null;
			if (currentDropObjType.equals(LfwPageContext.SOURCE_TYPE_WIDGT)) {// widget的特殊处理
				UIWidget uiWidget = new UIWidget();
				uiWidget.setId(currentDropObjId);
				String pageId = LfwRuntimeEnvironment.getWebContext().getPageId();
				uiWidget.setUimeta(this.createUIMeta(pageMeta, pageId, currentDropObjId));
				child = uiWidget;
			} 
			else if (currentDropObjType.equals("DATASET")) { // 对传过来是数据集的进行处理
				// 通过dataset的id查找对应的form
				child = dataset2Form(pageMeta, currentDropObjId, currentDropDsId, widget);
			} 
			else if (currentDropObjType.equals(LfwPageContext.SOURCE_TYPE_TEXT)) {
				if (currentDropObjId != null && !currentDropObjId.equals("")) {
					UIElementFactory uf = new UIElementFactory();
					UITextField text = (UITextField) uf.createUIElement(currentDropObjType, widget);
					text.setId(currentDropObjId);
					text.setType(currentDropObjType);
					child = text;
				} 
				else {
					// 创建未定义的 text 控件
					child = dealTextField(pageMeta, currentDropObjType, currentDropObjType2, widget);
				}

			}
			else if(LfwPageContext.SOURCE_TYPE_FORMELE.equals(currentDropObjType)){
				FormElement newFrmEle = null;
				String formId = null;
				WebComponent[] wcs = pageMeta.getWidget(widget).getViewComponents().getComponents();
				if(wcs != null && wcs.length > 0){
					for(WebComponent wc : wcs){
						if(wc instanceof FormComp){
							FormComp fc = (FormComp) wc;
							FormElement el = fc.getElementById(currentDropObjId);
							if(el != null){
								newFrmEle = (FormElement)el;
								formId = fc.getId();
							}
						}
					}
				}
				if(newFrmEle != null){
//					UIFormElement newUIFormEle = new UIFormElement();
//					newUIFormEle.setId(newFrmEle.getId());
//					newUIFormEle.setFormId(formId);
//					newUIFormEle.setWidgetId(widget);
//					newUIFormEle.setElementType(newFrmEle.getEditorType());
					UIFormElement newUIFormEle = new UIElementFactory().createFormElement(newFrmEle.getId(), formId, widget, newFrmEle.getEditorType());
					child = newUIFormEle;
				}
			} 
			else {
				UIElementFactory uf = new UIElementFactory();
				child = uf.createUIElement(currentDropObjType, widget);
				if (child != null) {
					if (currentDropObjId != null && !currentDropObjId.equals("")) {
						child.setAttribute(UIElement.ID, currentDropObjId);
						LfwWidget currWidget = pageMeta.getWidget(widget);
						WebComponent comp = null;
						comp = currWidget.getViewComponents().getComponent(currentDropObjId);
						if(comp == null)
							comp =currWidget.getViewMenus().getMenuBar(currentDropObjId);
//						if(comp == null){
//							IPaEditorService ipaService = NCLocator.getInstance().lookup(IPaEditorService.class);
//							String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
//							String pageId = LfwRuntimeEnvironment.getWebContext().getWebSession().getPageId();
////							PageMeta outPageMeta = ipaService.getOutPageMeta(pageId, sessionId);
////							comp = outPageMeta.getWidget(widget).getViewComponents().getComponent(currentDropObjId);
//							addComponent(pageMeta, widget, comp);
//						}	
					} 
					else {// 没有Id的 认为是未定义的控件，此处需要创建webElement
						String t = randomT(4);
						child.setAttribute(UIElement.ID, currentDropObjType + t);
						WebComponent webComp = uf.createWebComponent(child);
						addComponent(pageMeta, widget, webComp);
						initOtherPageMeta(widget, webComp);
					}
				}
			}
			return child;
		} 
		else {
			String t = randomT(4);
			UIElementFactory uf = new UIElementFactory();
			UIElement child = uf.createUIElement(currentDropObjId, widget);
			if (child == null)
				throw new LfwRuntimeException("创建UIElement失败 !");
			child.setAttribute(UIElement.ID, currentDropObjId + t);
			return child;
		}
	}

//	/**
//	 * 
//	 * 表格的td中添加元素的处理
//	 * 
//	 * @param gridPanel
//	 * @param widget
//	 * @param child
//	 */
//	private void addToGridPanel(UIGridPanel gridPanel, String widget, UIElement child) {
////		if (gridPanel.getCellType() == null) {
//			this.addUIElement(gridPanel, child);
////		} else {
////			// if (!(child instanceof UIFormElement)) {
////			// throw new LfwRuntimeException("自由表单中只能放form元素");
////			// }
////			if (gridPanel.getCellType().equals("0")) {
////				UIFormElement uife = (UIFormElement) child;
////				UIGridPanel cell1 = gridPanel.getNextCell();
////				UIGridPanel cell2 = cell1.getNextCell();
////
////				UIElementFactory uf = new UIElementFactory();
////				UILabelComp uilc = (UILabelComp) uf.createUIElement(LfwPageContext.SOURCE_TYPE_LABEL, widget);
////				uilc.setId(uife.getId() + "_label");
////
////				this.addElement(cell2, child);
////				this.addElement(gridPanel, uilc);
////
////			} else if (gridPanel.getCellType().equals("1")) {
////				UIFormElement uife = (UIFormElement) child;
////				UIGridPanel cell0 = gridPanel.getPreCell();
////				UIGridPanel cell2 = gridPanel.getNextCell();
////				this.addElement(cell2, child);
////
////				UIElementFactory uf = new UIElementFactory();
////				UILabelComp uilc = (UILabelComp) uf.createUIElement(LfwPageContext.SOURCE_TYPE_LABEL, widget);
////				uilc.setId(uife.getId() + "_label");
////				this.addElement(cell0, uilc);
////
////			} else if (gridPanel.getCellType().equals("2")) {
////				UIFormElement uife = (UIFormElement) child;
////				UIGridPanel cell1 = gridPanel.getPreCell();
////				UIGridPanel cell0 = cell1.getPreCell();
////				this.addElement(gridPanel, child);
////
////				UIElementFactory uf = new UIElementFactory();
////				UILabelComp uilc = (UILabelComp) uf.createUIElement(LfwPageContext.SOURCE_TYPE_LABEL, widget);
////				uilc.setId(uife.getId() + "_label");
////				this.addElement(cell0, uilc);
////
////			}
////		}
//	}

	/**
	 * 处理控件
	 * 
	 * @param pageMeta
	 * @param currentDropObjType
	 * @param currentDropObjType2
	 * @param widget
	 * @return
	 */
	private UIElement dealTextField(PageMeta pageMeta, String currentDropObjType, String currentDropObjType2, String widget) {
		UIElement child;
		UIElementFactory uf = new UIElementFactory();
		UITextField text = (UITextField) uf.createUIElement(currentDropObjType, widget);
		String t = randomT(4);
		text.setId(currentDropObjType + t);
		text.setType(currentDropObjType2);
		WebComponent webComp = uf.createWebComponent(text);
		addComponent(pageMeta, widget, webComp);
		initOtherPageMeta(widget, webComp);
		child = text;
		return child;
	}

	/**
	 * 添加webcomponent时，需要添加到 原始的和外部的 pagemeta中
	 * 
	 * @param widget
	 * @param webComp
	 */
	private void initOtherPageMeta(String widget, WebComponent webComp) {
		String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
		String pageId = LfwRuntimeEnvironment.getWebContext().getWebSession().getPageId();
		IPaEditorService ipaService = NCLocator.getInstance().lookup(IPaEditorService.class);
		PageMeta outPageMeta = ipaService.getOutPageMeta(pageId, sessionId);
//		PageMeta oriPageMeta = ipaService.getOriPageMeta(pageId, sessionId);
		addComponent(outPageMeta, widget, (WebComponent) webComp.clone());
//		addComponent(oriPageMeta, widget, (WebComponent) webComp.clone());
	}

	/**
	 * 将webcomponent添加到pagemeta中
	 * 
	 * @param pageMeta
	 * @param widget
	 * @param webComp
	 */
	private void addComponent(PageMeta pageMeta, String widget, WebComponent webComp) {
		LfwWidget lfwWidget = pageMeta.getWidget(widget);
		if (lfwWidget != null) {
			lfwWidget.getViewComponents().addComponent(webComp);
		}
	}

	public static String randomT(int length) {
		String t = String.valueOf(System.currentTimeMillis());
		return t.substring(t.length() - length);
	}

	/**
	 * 
	 * 自由表单，dataset获取formElement
	 * 
	 * @param pageMeta
	 * @param fieldKey
	 *            数据集的字段的id
	 * @param datasetId
	 *            数据集id
	 * @param widget
	 * @return
	 */
	private UIElement dataset2Form(PageMeta pageMeta, String fieldKey, String datasetId, String widgetId) {
		String formId = datasetId + "_newFrm";
		FormComp form = (FormComp) UIElementFinder.findWebElementById(pageMeta, widgetId, formId);
		if(form == null){
			FormComp newFrm = new FormComp();
			newFrm.setId(formId);
			newFrm.setDataset(datasetId);
			
			pageMeta.getWidget(widgetId).getViewComponents().addComponent(newFrm);
			form = newFrm;
		}
		// 根据dataset中的一行，查找FormComp中的FormElement
		LfwWidget widget = pageMeta.getWidget(widgetId);
		Dataset ds = widget.getViewModels().getDataset(datasetId);
		Field field = ds.getFieldSet().getField(fieldKey);

		WidgetElement[] allElements = setField2FormElement(widget, ds, field);
		if(allElements != null && allElements.length > 0){
			FormElement formEle = (FormElement) allElements[0];
			// 根据生成的formEle字段，判断是form中是存在此FormElement
			FormElement formElement = form.getElementById(formEle.getId());
			
			if(formElement == null){
				form.addElement(formEle);
				//如果生成了参照，将参照加入的pagemeta中
				if(allElements.length >= 3){
					IRefNode refNode = null;
					if(allElements[1] instanceof IRefNode){
						refNode = (IRefNode) allElements[1];
					}
					if(refNode != null){
						pageMeta.getWidget(widgetId).getViewModels().addRefNode(refNode);
					}
					MDComboDataConf combo = null;
					if(allElements[2] instanceof MDComboDataConf){
						combo = (MDComboDataConf) allElements[2];
					}
					if(combo != null){
						pageMeta.getWidget(widgetId).getViewModels().addComboData(combo);
					}
				}
			}
			UIFormElement uiFormEle = new UIElementFactory().createFormElement(formEle.getId(), form.getId(), widgetId, formEle.getEditorType());
			return uiFormEle;
		}
		return null;
	}

	private WidgetElement[]  setField2FormElement(LfwWidget widget, Dataset ds, Field field) {
		FormElement frmEle = new FormElement();
		//如果是Field有引用的sourceId，需要生成额外的Field字段
		Dataset entityDs = getGlobalContext().getParentGlobalContext().getPageMeta().getWidget("data").getViewModels().getDataset("entityds");
		Row row = entityDs.getSelectedRow();
		String parentExtSourceAttr = null;
		Field parentField = null;
		NCRefNode refNode = null;
		String editorType = null;
		//是下拉框
		MDComboDataConf combo = null;
		if(field.getExtSource() != null && field.getExtSource().equals(Field.SOURCE_ENUM)){
			editorType = EditorTypeConst.COMBODATA;
			String comboDataFlag = CompIdGenerator.generateComboCompId(ds.getId(),field.getId());
			frmEle.setRefComboData(comboDataFlag);
			combo = (MDComboDataConf) widget.getViewModels().getComboData(comboDataFlag);
			if(combo == null){
				combo = new MDComboDataConf();
				combo.setId(comboDataFlag);
				combo.setFullclassName(field.getExtSourceAttr());
				combo.setCaption(field.getText());
			}
		}
		//如果本身引用的是参照字段
		else if(field.getExtSource() != null && field.getExtSource().equals(Field.SOURCE_MD)){
			editorType = EditorTypeConst.REFERENCE;
			String refKey = field.getId();
			
			String extAttr = field.getExtSourceAttr();
			IMDQueryFacade qry = MDBaseQueryFacade.getInstance();
			IBusinessEntity bean = null;
			try {
				bean = (IBusinessEntity) qry.getBeanByFullName(extAttr);
			} catch (MetaDataException e) {
				LfwLogger.error(e.getMessage(), e);
			}
			String showName = LfwMdUtil.getMdItfAttr(bean, "nc.vo.bd.meta.IBDObject", "name");
			String refNodeId = CompIdGenerator.generateRefCompId(ds.getId(), refKey + "_" + showName);
			IRefNode gerefNode = widget.getViewModels().getRefNode(refNodeId);
			if(gerefNode != null){
				frmEle.setRefNode(refNodeId);
			}
		}
		if(editorType == null)
			editorType = EditorTypeConst.getEditorTypeByString(field.getDataType());
		
		if(editorType.equals(EditorTypeConst.STRINGTEXT)){
			editorType = this.showInputDialog(editorType);
		}
		
		frmEle.setEditorType(editorType);
		if(row != null){
			int pIdIndex = entityDs.nameToIndex("pid");
			String parentId = (String) row.getValue(pIdIndex);
			if(parentId != null){
				parentField = ds.getFieldSet().getField(parentId);
				if(parentField != null){
					//生成扩展字段
					int idIndex = entityDs.nameToIndex("id");
					int nameIndex = entityDs.nameToIndex("name");
					String fieldId = (String) row.getValue(idIndex);
					String name = (String) row.getValue(nameIndex);
					Field extField = ds.getFieldSet().getField(parentId + "_" + fieldId);
					if(extField == null){
						extField = new Field();
						extField.setId(parentId + "_" + fieldId);
						extField.setField(null);
						extField.setDefaultValue(null);
						extField.setText(name);
						extField.setI18nName(name);
						extField.setDataType(StringDataTypeConst.STRING);
						ds.getFieldSet().addField(extField);
						//生成FieldRelation
						FieldRelation fieldRelation = new FieldRelation();
						fieldRelation.setId(parentId + "_" + fieldId + "_" + "rel");
						if(parentField != null){
							parentExtSourceAttr = parentField.getExtSourceAttr();
							String refDsId = "$refds_" + parentExtSourceAttr.replaceAll("\\.", "_");
							fieldRelation.setRefDataset(refDsId);
							MatchField mf = new MatchField();
							mf.setReadField(fieldId);
							mf.setWriteField(extField.getId());
							fieldRelation.addMatchField(mf);
							WhereField whereField = new WhereField();
							whereField.setKey(parentId);
							whereField.setValue(parentId);
							fieldRelation.setWhereField(whereField);
							ds.getFieldRelations().addFieldRelation(fieldRelation);
							ds.getFieldSet().addField(extField);
							
						}
					}
					field = extField;
					//如果是参照，需要生成参照refnode
					refNode = generateRefNode(ds, field, parentField, editorType);
				}
			}
		}
		frmEle.setId(field.getId());
		frmEle.setField(field.getId());
		frmEle.setText(field.getText());
		frmEle.setI18nName(field.getI18nName());

		
		// datatype
		frmEle.setDataType(field.getDataType());
		frmEle.setPrecision(field.getPrecision());
//		frmEle.setWidth("120");

		frmEle.setNullAble(field.isNullAble());
		
		if(refNode != null)
			frmEle.setRefNode(refNode.getId());
		return new WidgetElement[]{frmEle, refNode, combo};
	}

	/**
	 * 如果需要生成参照，生成参照自从
	 * @param ds
	 * @param field
	 * @param parentField
	 * @param editorType
	 * @return
	 */
	private NCRefNode generateRefNode(Dataset ds, Field field,
			 Field parentField, String editorType) {
		NCRefNode refNode = null;
		if(editorType != null && editorType.equals(EditorTypeConst.REFERENCE)){
			if(parentField != null){
				if(ds instanceof MdDataset){
					String objMeta = ((MdDataset)ds).getObjMeta();
					IMDQueryFacade qry = MDBaseQueryFacade.getInstance();
					try {
						IBean bean = qry.getBeanByFullName(objMeta);
						IAttribute attr = bean.getAttributeByName(parentField.getId());
						if(attr.getDataType().getTypeType() == IType.REF || attr.getDataType().getTypeType() == IType.ENTITY){
							//IBusinessEntity targetBean = ((IBusinessEntity)attr.getAssociation().getEndBean());
							IRefType ref = (IRefType) attr.getDataType();
							String refCode = ref.getRefType().getDefaultRefModelName();
							if(refCode != null){
								AbstractRefModel model = LfwNCRefUtil.getRefModel(refCode);
								if(model != null){
									int type = LfwNCRefUtil.getRefType(model);
									ILfwRefModel refModel = null;
									if(type == IRefConst.GRIDTREE){
										NcAdapterTreeGridRefModel tgrm = new NcAdapterTreeGridRefModel();
										tgrm.setNcModel((AbstractRefGridTreeModel) model);
										refModel = tgrm;
									}
									
									else if(type == IRefConst.TREE){
										NcAdapterTreeRefModel tgrm = new NcAdapterTreeRefModel();
										tgrm.setNcModel((AbstractRefTreeModel) model);
										refModel = tgrm;
									}
									
									else if(type == IRefConst.GRID){
										NcAdapterGridRefModel tgrm = new NcAdapterGridRefModel();
										tgrm.setNcModel(model);
										refModel = tgrm;
									}
									
									AppLfwRefGenUtil gen = new AppLfwRefGenUtil(refModel, null);
									String[] refEles = gen.getRefElements();
									
									
									String writeFields = attr.getName() + "," + field.getId();
									String readFields = refEles[0] + "," + refEles[2];
										
									refNode = new RefNodeGenerator().createRefNode((MdDataset)ds, false, attr.getName(), null, refCode, readFields, writeFields, false, null, null);
									//NCRefNode refNode = null;
									refNode.setId(refNode.getId() + "_" + field.getId());
									refNode.setFrom(null);
								}
							}
						}
					} catch (MetaDataException e) {
						// TODO Auto-generated catch block
						LfwLogger.error(e.getMessage(), e);
					}
				}
			}
		}
		return refNode;
	}
	
	
	private String showInputDialog(String editorType) {
		StaticComboData comboData = new StaticComboData();
		
		
		CombItem item1 = new CombItem(EditorTypeConst.STRINGTEXT, "单行文本");
		CombItem item2 = new CombItem(EditorTypeConst.TEXTAREA, "文本域");
		CombItem item3 = new CombItem(EditorTypeConst.RICHEDITOR, "富文本");
		CombItem item4 = new CombItem(EditorTypeConst.PWDTEXT, "密码");
		CombItem item5 = new CombItem(EditorTypeConst.SELFDEF, "自定义");
		CombItem item6 = new CombItem(EditorTypeConst.REFERENCE, "参照");
		comboData.addCombItem(item1);
		comboData.addCombItem(item2);
		comboData.addCombItem(item3);
		comboData.addCombItem(item4);
		comboData.addCombItem(item5);
		comboData.addCombItem(item6);
		ComboInputItem editor = new ComboInputItem("editorType", "类型", true);
		editor.setValue(EditorTypeConst.STRINGTEXT);
		editor.setComboData(comboData);
		
		InteractionUtil.showInputDialog("确认", new InputItem[] { editor });
		Map<String, String> rs = InteractionUtil.getInputDialogResult();
		
		if (rs != null && !rs.get("editorType").equals("null")) {
			return	rs.get("editorType");
		}
		return editorType;
	}

	
//	/**
//	 * 根据字段的id获取对应的
//	 * 
//	 * @param form
//	 * @param currentDropObj
//	 * @return
//	 */
//	private FormElement getFormElementByFieldId(FormComp form, String currentDropObj) {
//
//		List<FormElement> formEles = form.getElementList();
//		if (formEles != null && formEles.size() > 0) {
//			for (int i = 0; i < formEles.size(); i++) {
//				FormElement formEle = formEles.get(i);
//				if (formEle.getId().equals(currentDropObj)) {
//					return formEle;
//				}
//			}
//		}
//		return null;
//	}

	/**
	 * 页面属性更改时，执行此方法，对页面响应的属性做调整
	 * 
	 * @param uiMeta
	 * @param pageMeta
	 * @param lpc
	 */
	private void update(UIMeta uiMeta, PageMeta pageMeta, LfwPageContext lpc) {
		UpdateParameter param = getUpdateParameter(lpc);

		this.doUpdate(uiMeta, pageMeta, param);
//		String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
//		String pageId = LfwRuntimeEnvironment.getWebContext().getWebSession().getPageId();
//		IPaEditorService ipaService = NCLocator.getInstance().lookup(IPaEditorService.class);
//		PageMeta outPageMeta = ipaService.getOutPageMeta(pageId, sessionId);
//		UIMeta outUIMeta = ipaService.getOutUIMeta(pageId, sessionId);
//
//		PageMeta oriPageMeta = ipaService.getOriPageMeta(pageId, sessionId);
//		UIMeta oriUIMeta = ipaService.getOriUIMeta(pageId, sessionId);
//
//		LifeCyclePhase phase = getPhase();
//		try{
//			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
//	
//			this.doUpdate(outUIMeta, outPageMeta, param);
//			this.doUpdate(oriUIMeta, oriPageMeta, param);
//		}
//		finally{
//			RequestLifeCycleContext.get().setPhase(phase);
//		}

	}

	public LifeCyclePhase getPhase() {
		return RequestLifeCycleContext.get().getPhase();
	}

	/**
	 * 页面属性更改时，执行此方法，对页面响应的属性做调整
	 * 
	 * @param uiMeta
	 * @param pageMeta
	 * @param param
	 */
	private void doUpdate(UIMeta uiMeta, PageMeta pageMeta, UpdateParameter param) {
		String compId = param.getCompId();
		String viewId = param.getViewId();
//		String prtId = param.getPrtId();
		UIElement uiEle = null;
		WebElement webEle = null;
		if(compId.indexOf(".") != -1){
			String[] ids = compId.split("\\.");
			uiEle = UIElementFinder.findElementById(uiMeta, ids[0], ids[1]);
			webEle = UIElementFinder.findWebElementById(pageMeta, viewId, ids[0], ids[1]);
		}
		else{
			uiEle = UIElementFinder.findElementById(uiMeta, compId);
			webEle = UIElementFinder.findWebElementById(pageMeta, viewId, compId);
		}
	
		if (uiEle == null && webEle == null)
			return;
		
		if (webEle != null) {
			try {
				setContext(webEle, param);
			} 
			catch (Exception e) {
				try {
					setContext(uiEle, param);
				} 
				catch (Exception e1) {
					throw new LfwRuntimeException(e1.getMessage(), e1);
				}
			}
		} 
		else{
			try {
				setContext(uiEle, param);
			} 
			catch (Exception e1) {
				throw new LfwRuntimeException(e1.getMessage(), e1);
			}
		}
	}

//	private WebElement findFormOrGridElement(PageMeta pageMeta, String viewId,
//			String compId, String prtId, String compType) {
//		WebElement web = null;
//		if(compType.equals(LfwPageContext.SOURCE_TYPE_FORMELE)){
//			FormComp form = (FormComp) pageMeta.getWidget(viewId).getViewComponents().getComponent(prtId);
//			if(form != null)
//				web = form.getElementById(compId);
//		}
//		else{
//			GridComp grid = (GridComp) pageMeta.getWidget(viewId).getViewComponents().getComponent(prtId);
//			if(grid != null)
//				web = (WebElement) grid.getColumnById(compId);
//		}
//		return web;
//	}

//	/**
//	 * 根据Id在uimeta中查找对应的ui元素
//	 * 
//	 * @param uimeta
//	 * @param itemId
//	 * @return
//	 */
//	private UIElement findUIElement(IUIMeta uimeta, String itemId) {
//		UIElement ele = null;
//		if (uimeta instanceof UIMeta) {
//			UIMeta ui = (UIMeta) uimeta;
//			if(ui.getId() != null && ui.getId().equals(itemId))
//				return ui;
//			else{
//				UIElement uiEle = ui.getElement();
//				if (uiEle != null && uiEle instanceof UILayout) {
//					ele = findUIElementById((UILayout) uiEle, itemId);
//				}
//			}
//		}
//		return ele;
//	}
//
//	// 获取页面元素
//	private WebElement findWebElement(PageMeta pagemeta, String widgetId, String eleId) {
//		WebElement ele = null;
//		if (widgetId != null) {
//			ele = pagemeta.getWidget(widgetId).getViewComponents().getComponent(eleId);
//			if (ele == null) {
//				ele = pagemeta.getWidget(widgetId).getViewMenus().getMenuBar(eleId);
//			}
//			if (ele == null) {
//				
//			}
//		} else {
//			ele = pagemeta.getViewMenus().getMenuBar(eleId);
//		}
//		return ele;
//	}
//
//	// 获取页面元素
//	private WebElement findWebElement(PageMeta pagemeta, String widgetId, UIComponent uiEle) {
//		if (widgetId != null) {
//			if (uiEle instanceof UIFormElement) {
//				UIFormElement fe = (UIFormElement) uiEle;
//				FormComp formComp = (FormComp) pagemeta.getWidget(widgetId).getViewComponents().getComponent(fe.getFormId());
//				return formComp.getElementById(fe.getId());
//			} else if (uiEle instanceof UIMenubarComp) {
//				return pagemeta.getWidget(widgetId).getViewMenus().getMenuBar(uiEle.getId());
//			} else {
//				return pagemeta.getWidget(widgetId).getViewComponents().getComponent(uiEle.getId());
//			}
//		}
//		return null;
//	}

	private void setContext(Object uiEle, UpdateParameter param) throws Exception {
		if(param == null || param.getAttr() == null) return;
		String upCaseAttr = StringUtils.upperCase(param.getAttr().substring(0, 1)) + param.getAttr().substring(1);
		String newValue = param.getNewValue();
		String oldValue = param.getOldValue();
		Method m = null;
		if (param.getAttrType().equals(StringDataTypeConst.bOOLEAN)) {
			m = uiEle.getClass().getMethod("set" + upCaseAttr, boolean.class);
			boolean value = false;
			if(oldValue != null)
				value = oldValue.equals("Y") ? true : false;
			if(newValue != null)
				value = newValue.equals("Y") ? true : false;
			m.invoke(uiEle, value);
		}
		else if(param.getAttrType().equals(StringDataTypeConst.BOOLEAN)){
			m = uiEle.getClass().getMethod("set" + upCaseAttr, Boolean.class);
			Boolean value = false;
			if(oldValue != null)
				value = oldValue.equals("Y") ? true : false;
			if(newValue != null)
				value = newValue.equals("Y") ? true : false;
			m.invoke(uiEle, value);
		} else if (param.getAttrType().equals(StringDataTypeConst.INT)) {
			m = uiEle.getClass().getMethod("set" + upCaseAttr, int.class);
			int value = 0;
			if(oldValue != null)
				value = Integer.valueOf(oldValue);
			if(newValue != null)
				value = Integer.valueOf(newValue);
			m.invoke(uiEle, value);
		} else if (param.getAttrType().equals(StringDataTypeConst.INTEGER)) {
			m = uiEle.getClass().getMethod("set" + upCaseAttr, Integer.class);
			Integer value = null ;
			if(oldValue != null)
				value = Integer.valueOf(param.getOldValue());
			if(newValue != null)
				value = Integer.valueOf(newValue);
			m.invoke(uiEle, value);
		} else {
			m = uiEle.getClass().getMethod("set" + upCaseAttr, param.getAttrType().getClass());
			m.invoke(uiEle, newValue);
		}
//		if (uiEle instanceof UIElement) 
//			((UIElement) uiEle).updateSelf();
	}

	/**
	 * 创建uimeta
	 * 
	 * @param pm
	 * @param pageId
	 * @param widgetId
	 * @return
	 */
	private UIMeta createUIMeta(PageMeta pm, String pageId, String widgetId) {
		WidgetConfig wconf = pm.getWidgetConf(widgetId);
		UIMeta meta = null;
		LifeCyclePhase oriPhase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		if(wconf.getRefId().startsWith("..")){
			UIMetaParserUtil parserUtil = new UIMetaParserUtil();
			parserUtil.setPagemeta(pm);
			meta = parserUtil.parseUIMeta((String)null, wconf.getId());
		}
		else{
			
			DefaultUIMetaBuilder builder = new DefaultUIMetaBuilder();
			meta = builder.buildUIMeta(pm, pageId, widgetId);
		}
		RequestLifeCycleContext.get().setPhase(oriPhase);
		return meta;
	}

	private String replaceNullString(String arg) {
		if (arg != null) {
			if (arg.equals("null") || arg.equals(""))
				arg = null;
		}
		return arg;
	}

	private UIWidget findWidget(UIMeta uimeta, String widgetId) {
		UIElement ele = uimeta.getElement();
		return this.findWidget(ele, widgetId);
	}

	private UIWidget findWidget(UILayout layout, String widgetId) {
		if (layout instanceof UIGridLayout) {
//			UIGridLayout gridLayout = (UIGridLayout) layout;
//			List<UIGridRowLayout> rows = gridLayout.getGridRow();
//			for (UIGridRowLayout row : rows) {
//				UIWidget widget = this.findWidget(row, widgetId);
//				if (widget != null) {
//					return widget;
//				}
//			}
		} else {
			List<UILayoutPanel> panels = layout.getPanelList();
			if (panels != null && panels.size() > 0) {
				for (UILayoutPanel panel : panels) {
					UIWidget widget = this.findWidget(panel, widgetId);
					if (widget != null) {
						return widget;
					}
				}
			}
		}

		return null;
	}

	private UIWidget findWidget(UILayoutPanel panel, String widgetId) {
		UIElement element = panel.getElement();
		if (element != null) {
			return this.findWidget(element, widgetId);
		} else {
			return null;
		}
	}

	private UIWidget findWidget(UIElement ele, String widgetId) {
		if (ele instanceof UIWidget) {
			if (((UIWidget) ele).getId().equals(widgetId)) {
				return (UIWidget) ele;
			} else {
				UIMeta uimeta = ((UIWidget) ele).getUimeta();
				return this.findWidget(uimeta, widgetId);
			}
		} else if (ele instanceof UIMeta) {
			UIMeta uimeta = (UIMeta) ele;
			return this.findWidget(uimeta, widgetId);
		} else if (ele instanceof UILayout) {
			return this.findWidget((UILayout) ele, widgetId);
		} else if (ele instanceof UILayoutPanel) {
			return this.findWidget((UILayoutPanel) ele, widgetId);
		}
		return null;
	}

//	// 获取页面元素
//	private Object getPageElement(PageMeta pagemeta, UIMeta uimeta, String widgetId, String eleId, String uiId, String type) {
//		Object ele = null;
//		if (uiId != null) {
//			if(type.equals("uimeta"))
//				ele = UIElementFinder.findUIMeta(uimeta, widgetId, uiId);
//			else{
//				ele = UIElementFinder.findElementById(uimeta, uiId);
//			}
//
//		}
////		if (ele == null) {
////			if (widgetId != null) {
////				ele = pagemeta.getWidget(widgetId).getViewComponents().getComponent(eleId);
////				if (ele == null) {
////					ele = pagemeta.getWidget(widgetId).getViewMenus().getMenuBar(eleId);
////				}
////			} 
////			else {
////				ele = pagemeta.getViewMenus().getMenuBar(eleId);
////			}
////		}
//		return ele;
//	}
//
//	/**
//	 * @param uimeta
//	 * @param ele
//	 * @return
//	 */
//	private UIElement getUIElement(UIMeta uimeta, Object ele) {
//
//		String id = getEleId(ele);
//
//		if (ele instanceof UIElement) {
//			return (UIElement) ele;
//		}
//		if (ele instanceof WebElement) {
//			UILayout layout = (UILayout) uimeta.getElement();
//			return findUIElementById(layout, id);
//
//		}
//		return null;
//	}

//	// 根据ele获取其ID
//	private String getEleId(Object ele) {
//		String uiEleId = null;
//		if (ele instanceof WebElement) {
//			uiEleId = ((WebElement) ele).getId();
//		}
//		if (ele instanceof UIElement) {
//			uiEleId = (String) ((UIElement) ele).getAttribute(UIElement.ID);
//		}
//		return uiEleId;
//	}
//
//	/**
//	 * @param layout
//	 * @param id
//	 * @return
//	 */
//	private UIElement findUIElementById(UILayout layout, String id) {
//		//return layout.findElementById(layout, id);
//		return null;
//	}
//	private void setWinCurrDsInfo(UIElement parent, UIElement child, String oper) {
//		if (child == null || parent == null)
//			return;
//		
//		if(parent instanceof UIMeta && child instanceof UIWidget){
//			if(((UIMeta) parent).getParent() == null)
//				this.setCurrDsInfo(((UIWidget) child).getId(),((UIMeta) parent).getId(), oper);
//		}
//		else{
//			if(child instanceof UIWidget){
//				UIElement ele = ((UIWidget) child).getUimeta();
//				if(ele != null){
//					this.setCurrDsInfo(((UIWidget)child).getId(),(String)parent.getAttribute(UIElement.ID), oper);
//				}
//			}
//			else if(child instanceof UILayout){
//				if(child instanceof UIGridLayout){
////					List<UIGridRowLayout> list = ((UIGridLayout) child).getGridRow();
////					if(list != null){
////						for(UIGridRowLayout row : list){
////							this.setWinCurrDsInfo(child, row, oper);
////						}
////					}
//				}
//				else{
//					List<UILayoutPanel> list = ((UILayout) child).getPanelList();
//					if(list != null){
//						for (UILayoutPanel panel : list) {
//							this.setWinCurrDsInfo(child, panel, oper);
//						}
//					}
//				}
//			}
//			else if(child instanceof UILayoutPanel){
//				UILayoutPanel panel = (UILayoutPanel) child;
//				UIElement ele = panel.getElement();
//				if(ele != null){
//					this.setWinCurrDsInfo(panel, ele, oper);
//				}
//				
//			}
////			
////			if(parent instanceof UIMeta){
////				if(((UIMeta) parent).getParent() == null){
////					this.setCurrDsInfo((String) child.getAttribute(UIElement.ID), null, oper);
////				}
////				else{
////					String prtId = (String)((UIMeta) parent).getParent().getAttribute(UIElement.ID);
////					if(prtId != null){
////						this.setCurrDsInfo((String) child.getAttribute(UIElement.ID), prtId, oper);
////					}
////				}
////				
////			}
////			
////			else
//				this.setCurrDsInfo((String) child.getAttribute(UIElement.ID), (String) parent.getAttribute(UIElement.ID), oper);
//			
//		}
		
//	}

//	private void setCurrDsInfo(UIElement parent, UIElement child, String oper) {
//		if (child == null || parent == null)
//			return;
//		//this.setCurrDsInfo((String) child.getAttribute(UIElement.ID), (String) parent.getAttribute(UIElement.ID), oper);
//		if (child instanceof UIMeta)
//			return;
//		if (child instanceof UIWidget) {
//			UIElement ele2 = ((UIWidget) child).getUimeta();
//			if (ele2 != null)
//				this.setCurrDsInfo(child, ele2, oper);
//		} 
//		else if (child instanceof UILayout) {
//			if (child instanceof UIGridLayout) {
////				List<UIGridRowLayout> list = ((UIGridLayout) child).getGridRow();
////				if (list != null) {
////					for (UIGridRowLayout row : list) {
////						this.setCurrDsInfo(child, row, oper);
////					}
////				}
//
//			} 
//			else {
//				List<UILayoutPanel> list = ((UILayout) child).getPanelList();
//				if (list != null) {
//					for (UILayoutPanel panel : list) {
//						this.setCurrDsInfo(child, panel, oper);
//					}
//				}
//			}
//		} 
//		else if (child instanceof UILayoutPanel) {
//			UILayoutPanel panel = (UILayoutPanel) child;
//			UIElement ele2 = panel.getElement();
//			if (ele2 != null)
//				this.setCurrDsInfo(panel, ele2, oper);
//		}
//		
////		
////		if(parent instanceof UIMeta){
////			if(((UIMeta) parent).getParent() == null){
////				this.setCurrDsInfo((String) child.getAttribute(UIElement.ID), null, oper);
////			}
////			else{
////				String prtId = (String)((UIMeta) parent).getParent().getAttribute(UIElement.ID);
////				if(prtId != null){
////					this.setCurrDsInfo((String) child.getAttribute(UIElement.ID), prtId, oper);
////				}
////			}
////			
////		}
////		else
////		this.setCurrDsInfo((String) child.getAttribute(UIElement.ID), (String) parent.getAttribute(UIElement.ID), oper);
//	}

//	private void setCurrDsInfo(String id, String pId, String oper) {
//		LfwWidget widget = getGlobalContext().getParentGlobalContext().getPageMeta().getWidget("data");
//		Dataset currDs = widget.getViewModels().getDataset("ds_struct");
//
//		int idIndex = currDs.nameToIndex("id");
//		int pIdIndex = currDs.nameToIndex("pid");
//
//		if (oper.equals("delete")) {
//			Row[] rows = currDs.getCurrentRowData().getRows();
//			if (rows != null && rows.length > 0) {
//				for (int i = 0; i < rows.length; i++) {
//					Row r = rows[i];
//					if(r.getValue(idIndex)!= null){
//						if (id != null && r.getValue(idIndex).equals(id)){
//							currDs.removeRow(r);
//							break;
//						}
//					}
//				}
//			}
//		} 
//		else if (oper.equals("add")) {
//			Row preRow = currDs.getCurrentRowData().getSelectedRow();
//			Row row = currDs.getEmptyRow();
//			if(preRow == null){
//				row.setValue(idIndex, id);
//				row.setValue(pIdIndex, pId);
//				currDs.addRow(row);
//			}
//			else{
//				row.setValue(idIndex, id);
//				row.setValue(pIdIndex, pId);
//				int index = currDs.getCurrentRowData().getRowIndex(preRow);
//				currDs.insertRow(index, row);
//			}	
//		}
//
//	}
	
	class ParamObject{
		protected String widgetId;
		protected String eleId;
		protected String uiId;
		protected String subuiId;
		protected String type;
		protected String subEleId;
	}

}
