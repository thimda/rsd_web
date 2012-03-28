package nc.uap.lfw.reference.app;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.common.ParamConstant;
import nc.uap.lfw.core.common.SessionConstant;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.event.AppRequestProcessor;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.EventHandlerConf;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.FocusListener;
import nc.uap.lfw.core.event.conf.KeyListener;
import nc.uap.lfw.core.event.conf.WidgetRule;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.IUIMeta;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.refnode.NCRefNode;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.jsp.uimeta.UIButton;
import nc.uap.lfw.jsp.uimeta.UIFlowhLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowhPanel;
import nc.uap.lfw.jsp.uimeta.UIFlowvLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIGridComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UITextField;
import nc.uap.lfw.jsp.uimeta.UITreeComp;
import nc.uap.lfw.jsp.uimeta.UIWidget;
import nc.uap.lfw.reference.base.BaseRefModel;
import nc.uap.lfw.reference.base.ILfwRefModel;
import nc.uap.lfw.reference.base.TreeGridRefModel;
import nc.uap.lfw.reference.base.TwoTreeGridRefModel;
import nc.uap.lfw.reference.base.TwoTreeRefModel;
import nc.uap.lfw.reference.util.LfwRefUtil;

/**
 * app应用中参照生成类
 * @author zhangxya
 *
 */
public class AppDefaultReferencePageModel extends PageModel {
	
	@Override
	protected IUIMeta createUIMeta(PageMeta pm) {
		WebContext ctx = getWebContext();
		String widgetId = ctx.getParameter("widgetId");
		if(widgetId == null || widgetId.equals(""))
			throw new LfwRuntimeException("未获取参照所属widget!");
		String refnodeId = ctx.getParameter("nodeId");
		PageMeta parentPm = ctx.getParentPageMeta();
		RefNode refnode = (RefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refnodeId);
		ILfwRefModel refModel = LfwRefUtil.getRefModel(refnode);
		int refType = LfwRefUtil.getRefType(refModel);
		UIMeta uiMeta = new UIMeta();
		// "表型参照"
		if(refType == ILfwRefModel.REFTYPE_GRID) {
			generateGridUIMeta(uiMeta, refnode);
		}
		else if(refType == ILfwRefModel.REFTYPE_TREE){
			generateTreeUIMeta(uiMeta, refnode); 
		}
		else if(refType == ILfwRefModel.REFTYPE_TREEGRID){
			generateGridTreeUI(uiMeta, refnode);
		}
		
		uiMeta.setReference(1);
		return uiMeta;
	}


	/**
	 * 产生树表型ui
	 * @param uiMeta
	 * @param refnode 
	 */
	private void generateGridTreeUI(UIMeta uiMeta, RefNode refnode) {
		UIWidget uiWidget = new UIWidget();
		uiWidget.setId("main");
		uiMeta.setElement(uiWidget);
		UIMeta uiMetaWidget = new UIMeta();
		uiMetaWidget.setFlowmode(false);
		uiMetaWidget.setId("main");
		uiWidget.setUimeta(uiMetaWidget);
		
		UIFlowvLayout flowvLayout = new UIFlowvLayout();
		flowvLayout.setId("flowvLayout");
		uiMetaWidget.setElement(flowvLayout);
		
		
		if(refnode instanceof NCRefNode){
			NCRefNode ncRefNode = (NCRefNode) refnode;
			if(ncRefNode.isOrgs()){
				UIFlowvPanel panelTop = new UIFlowvPanel();
				flowvLayout.addPanel(panelTop);
				
				panelTop.setHeight("30");
				UITextField locationField = new UITextField();
				locationField.setId("refcomp_org");
				locationField.setWidth("220");
				locationField.setWidgetId("main");
				panelTop.setElement(locationField);
			}
		}
		
		//center
		UIFlowvPanel centerFlowvPanel = new UIFlowvPanel();
		flowvLayout.addPanel(centerFlowvPanel);
				
		
		UIFlowhLayout flowhLayout = new UIFlowhLayout();
		flowhLayout.setId("flowhlayout");
		
		centerFlowvPanel.setElement(flowhLayout);
		
		UIFlowhPanel uiSplitterOne = new UIFlowhPanel();
		uiSplitterOne.setId("panel1");
		uiSplitterOne.setWidth("200");
		uiSplitterOne.setRightBorder("#");
		flowhLayout.addPanel(uiSplitterOne);
		
		UIFlowhPanel uiSplitterTwo = new UIFlowhPanel();
		uiSplitterTwo.setId("panel2");
		flowhLayout.addPanel(uiSplitterTwo);
		
		UITreeComp treeComp = new UITreeComp();
		treeComp.setId("reftree");
		treeComp.setWidgetId("main");
		uiSplitterOne.setElement(treeComp);
		
		UIGridComp uigridComp = new UIGridComp();
		uigridComp.setId("refgrid");
		uigridComp.setWidgetId("main");
		uiSplitterTwo.setElement(uigridComp);
		
		//bottom
		UIFlowvPanel borderPanelBottom = new UIFlowvPanel();
		borderPanelBottom.setId("bottomvp1");
		borderPanelBottom.setHeight("30");
		flowvLayout.addPanel(borderPanelBottom);

		UIFlowhLayout uiflowhLayout = new UIFlowhLayout();
		uiflowhLayout.setId("flowhLayout");
		borderPanelBottom.setElement(uiflowhLayout);
		
		UIFlowhPanel flowhPanel1 = new UIFlowhPanel();
		flowhPanel1.setAttribute("id", "flowhPanel1");
		uiflowhLayout.addPanel(flowhPanel1);
		
		if (refnode.isDialog()){
			UIFlowhPanel flowhPanel2 = new UIFlowhPanel();
			flowhPanel2.setAttribute("id", "flowhPanel2");
			uiflowhLayout.addPanel(flowhPanel2);
			flowhPanel2.setWidth("80");
			
			UIButton buttonOk = new UIButton();
			buttonOk.setId("okbt");
			buttonOk.setWidgetId("main");
			flowhPanel2.setElement(buttonOk); 
			
			UIFlowhPanel flowhPanel3 = new UIFlowhPanel();
			uiflowhLayout.addPanel(flowhPanel3);
			flowhPanel3.setAttribute("id", "flowhPanel3");
			flowhPanel3.setWidth("100");
			
			UIButton buttonCanCel = new UIButton();
			buttonCanCel.setId("cancelbt");
			buttonCanCel.setWidgetId("main");
			flowhPanel3.setElement(buttonCanCel);
		}
	}


	/**
	 * 生成表型参照UI
	 * @param uiMeta
	 * @param refnode 
	 */
	private void generateGridUIMeta(UIMeta uiMeta, RefNode refnode) {
		UIWidget uiWidget = new UIWidget();
		uiWidget.setId("main");
		uiMeta.setElement(uiWidget);
		UIMeta uiMetaWidget = new UIMeta();
		uiMetaWidget.setId("main");

		UIFlowvLayout flowvLayout = new UIFlowvLayout();
		flowvLayout.setId("borderLayout");
		uiWidget.setUimeta(uiMetaWidget);
		uiMetaWidget.setElement(flowvLayout);
		
		if(refnode instanceof NCRefNode){
			NCRefNode ncRefNode = (NCRefNode) refnode;
			if(ncRefNode.isOrgs()){
				UIFlowvPanel borderPanelTop = new UIFlowvPanel();
				borderPanelTop.setId("topFlowvp2");
				borderPanelTop.setHeight("30");
				flowvLayout.addPanel(borderPanelTop);
				
				UITextField orgField = new UITextField();
				orgField.setId("refcomp_org");
				orgField.setWidth("220");
				orgField.setWidgetId("main");
				borderPanelTop.setElement(orgField);
			}
		}
//		
		//center
		UIFlowvPanel borderPanel1 = new UIFlowvPanel();
		borderPanel1.setId("centerFlowvp1");
		
		flowvLayout.addPanel(borderPanel1);
		UIGridComp gridComp = new UIGridComp();
		gridComp.setId("refgrid");
		gridComp.setWidgetId("main");
		borderPanel1.setElement(gridComp);
		
		
		//bottom
		UIFlowvPanel borderPanelBottom = new UIFlowvPanel();
		borderPanelBottom.setId("bottomFlowvp3");
		borderPanelBottom.setHeight("60");
		flowvLayout.addPanel(borderPanelBottom);

		UIFlowhLayout uiflowhLayout = new UIFlowhLayout();
		uiflowhLayout.setId("flowhLayout");
		borderPanelBottom.setElement(uiflowhLayout);
		
		UIFlowhPanel flowhPanel1 = new UIFlowhPanel();
		flowhPanel1.setAttribute("id", "flowhPanel1");
		uiflowhLayout.addPanel(flowhPanel1);
		
		if (refnode.isDialog()){
			UIFlowhPanel flowhPanel2 = new UIFlowhPanel();
			flowhPanel2.setAttribute("id", "flowhPanel2");
			uiflowhLayout.addPanel(flowhPanel2);
			flowhPanel2.setWidth("80");
			
			UIButton buttonOk = new UIButton();
			buttonOk.setId("okbt");
			buttonOk.setWidgetId("main");
			flowhPanel2.setElement(buttonOk); 
			
			UIFlowhPanel flowhPanel3 = new UIFlowhPanel();
			uiflowhLayout.addPanel(flowhPanel3);
			flowhPanel3.setAttribute("id", "flowhPanel3");
			flowhPanel3.setWidth("100");
			
			UIButton buttonCanCel = new UIButton();
			buttonCanCel.setId("cancelbt");
			buttonCanCel.setWidgetId("main");
			flowhPanel3.setElement(buttonCanCel);
		}
	}


	/**
	 * 生成树形参照的uimeta
	 * @param uiMeta
	 * @param refnode 
	 */
	private void generateTreeUIMeta(UIMeta uiMeta, RefNode refnode) {
		UIWidget uiWidget = new UIWidget();
		uiWidget.setId("main");
		uiMeta.setElement(uiWidget);
		UIMeta uiMetaWidget = new UIMeta();
		uiMetaWidget.setId("main");
		uiMetaWidget.setFlowmode(false);
		UIFlowvLayout borderLayout = new UIFlowvLayout();
		borderLayout.setId("borderLayout");
		uiWidget.setUimeta(uiMetaWidget);
		uiMetaWidget.setElement(borderLayout);
		
		//top放置组织
		if(refnode instanceof NCRefNode){
			NCRefNode ncRefNode = (NCRefNode) refnode;
			if(ncRefNode.isOrgs()){
				UIFlowvPanel borderPanelTop = new UIFlowvPanel();
				borderPanelTop.setId("flowvp1");
				borderLayout.addPanel(borderPanelTop);
				
				borderPanelTop.setHeight("30");
				UITextField locationField = new UITextField();
				locationField.setId("refcomp_org");
				locationField.setWidth("220");
				locationField.setWidgetId("main");
				borderPanelTop.setElement(locationField);
			}
		}
		
		UIFlowvPanel borderPanel1 = new UIFlowvPanel();
		borderPanel1.setId("flowvp2");
		borderLayout.addPanel(borderPanel1);
		UITreeComp treeComp = new UITreeComp();
		treeComp.setId("reftree");
		treeComp.setWidgetId("main");
		borderPanel1.setElement(treeComp);
		borderPanel1.setLeftPadding("10");
		
		UIFlowvPanel borderPanelBottom = new UIFlowvPanel();
		borderPanelBottom.setId("flowvp3");
		borderPanelBottom.setHeight("30");
		borderLayout.addPanel(borderPanelBottom);
		
		UIFlowhLayout uiflowhLayout = new UIFlowhLayout();
		uiflowhLayout.setId("flowhLayout");
		borderPanelBottom.setElement(uiflowhLayout);
		
		UIFlowhPanel flowhPanel1 = new UIFlowhPanel();
		flowhPanel1.setAttribute("id", "flowhPanel1");
		uiflowhLayout.addPanel(flowhPanel1);
		
		if (refnode.isDialog()){
			UIFlowhPanel flowhPanel2 = new UIFlowhPanel();
			flowhPanel2.setAttribute("id", "flowhPanel2");
			uiflowhLayout.addPanel(flowhPanel2);
			flowhPanel2.setWidth("80");
			
			UIButton buttonOk = new UIButton();
			buttonOk.setId("okbt");
			buttonOk.setWidgetId("main");
			flowhPanel2.setElement(buttonOk); 
			
			UIFlowhPanel flowhPanel3 = new UIFlowhPanel();
			uiflowhLayout.addPanel(flowhPanel3);
			flowhPanel3.setAttribute("id", "flowhPanel3");
			flowhPanel3.setWidth("100");
			
			UIButton buttonCanCel = new UIButton();
			buttonCanCel.setId("cancelbt");
			buttonCanCel.setWidgetId("main");
			flowhPanel3.setElement(buttonCanCel);
		}
	}
	

	@Override
	public IUIMeta getUIMeta() {
		return super.getUIMeta();
	}

	public AppDefaultReferencePageModel() {
		super();
	}
	
	/**
	 * 初始化pageMeta
	 */
	protected PageMeta createPageMeta() {
		String pageMetaId = getPageModelId();
		WebContext ctx = getWebContext();
		String widgetId = ctx.getParameter("widgetId");
		if(widgetId == null || widgetId.equals(""))
			throw new LfwRuntimeException("未获取参照所属widget!");
		String refnodeId = ctx.getParameter("nodeId");
		PageMeta parentPm = ctx.getParentPageMeta();
		RefNode refnode = (RefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refnodeId);
		
		String pPageId = LfwRuntimeEnvironment.getWebContext().getParentPageId();
		String key = WebConstant.CACHE_PAGEMETA + pPageId + refnodeId + pageMetaId;
		PageMeta window = null;
		ILfwCache cache = LfwCacheManager.getStrongCache("reference_" + LfwRuntimeEnvironment.getRootPath(), LfwRuntimeEnvironment.getDatasource());
		if(LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_PRODUCTION)){
			window = (PageMeta) cache.get(key);
			if(window != null){
				Logger.info("从缓存中获取到pagemeta:" + pageMetaId);
				return (PageMeta)window.clone();
			}
		}
		
		window = createPageMeta(pageMetaId, refnode);
		window.setEditFormularClazz(null);	
		cache.put(key, window);
	

		return (PageMeta)window.clone();
	}
	
	/**
	 * 创建pageMeta
	 * @param pageMetaId
	 * @return
	 */
	private PageMeta createPageMeta(String pageMetaId, RefNode refnode)
	{
		//PageMeta meta = new PageMeta();
		PageMeta window = new PageMeta();
		window.setId(pageMetaId);
		LfwWidget widget = new LfwWidget();
		widget.setId("main");
		widget.setControllerClazz("nc.uap.lfw.reference.app.AppReferenceController");
		//取app的执行方式
		window.setProcessorClazz(AppRequestProcessor.class.getName());
		
		ILfwRefModel refModel = LfwRefUtil.getRefModel(refnode);
		
		AppLfwRefGenUtil util = new AppLfwRefGenUtil(refModel, refnode);
		
		//根据传送进来的dsId，产生对应的Dataset
		createViewModel4PageMeta(widget, util, refnode, refModel);
		//产生对应的控件
		createViewComponents4PageMeta(widget, refnode, util, refModel);
		//产生通用按钮事件
		createCommand4PageMeta(widget, refnode, util, refModel);
		window.addWidget(widget);
		
		// 创建页面事件
		createPageMetaListener(window);
		
		// 增加参照界面要使用的信息数据
		//PageData data = new PageData();
		//data.setId("referencePageData");
		//data.setClassName(ReferencePageDataHandler.class.getName());
		//meta.getViewModel().addPageData(data);
		return window; 
	}
	

	/**
	 * 为PageMeta创建ViewModel
	 * 
	 * @param widget
	 * @param dsId
	 * @param util
	 * @param refCode
	 * @param refModel 
	 */
	protected void createViewModel4PageMeta(LfwWidget widget, AppLfwRefGenUtil util, RefNode refnode, ILfwRefModel refModel)
	{
		Dataset[] dss = util.getDataset();
		
		for(int i = 0; i < dss.length; i ++){
			widget.getViewModels().addDataset((Dataset)dss[i]);
		}
		
		RefNode[] refNodes = util.getRefNodes();
		if(refNodes != null){
			for (int i = 0; i < refNodes.length; i++) {
				widget.getViewModels().addRefNode((RefNode)refNodes[i]);
			}
		}

		DatasetRelation[] rel = util.getRelation();
		if(rel != null){
			DatasetRelations rels = new DatasetRelations();
			for (int i = 0; i < rel.length; i++) {
				rels.addDsRelation(rel[i]);
			}
			widget.getViewModels().setDsrelations(rels);
		}
		
	}
	
	/**
	 * 为PageMeta创建控件
	 * 
	 * @param widget
	 * @param dsId
	 * @param refCode
	 * @param util
	 * @param refModel 
	 */
	protected void createViewComponents4PageMeta(LfwWidget widget, RefNode refnode, AppLfwRefGenUtil util, ILfwRefModel refModel)
	{
		WebComponent[] comps = util.getComponent();
		for(WebComponent comp : comps){
			widget.getViewComponents().addComponent(comp);
		}	
			
		ButtonComp okBtn = createButtonComp("okbt", "60", "left", "确定", null, null);
		ButtonComp cancelBt = createButtonComp("cancelbt", "60", "left", "取消", null, null);
		
		widget.getViewComponents().addComponent(okBtn);
		widget.getViewComponents().addComponent(cancelBt);
	
		EventSubmitRule submitRule = new EventSubmitRule();
		EventSubmitRule parentSubmitRule = new EventSubmitRule();
		
		WidgetRule pwr = new WidgetRule();
		pwr.setId(refnode.getWidget().getId());
		parentSubmitRule.addWidgetRule(pwr);
		
		DatasetRule parentDsRule = new DatasetRule();
		parentDsRule.setId(refnode.getWriteDs());
		parentDsRule.setType(DatasetRule.TYPE_CURRENT_LINE);
		parentSubmitRule.getWidgetRules().get(refnode.getWidget().getId()).addDsRule(parentDsRule);
		
		submitRule.setParentSubmitRule(parentSubmitRule);
		EventConf buttonOkEvent = new EventConf();
		buttonOkEvent.setJsEventClaszz("nc.uap.lfw.core.event.conf.MouseListener");
		buttonOkEvent.setOnserver(true);
		buttonOkEvent.setSubmitRule(submitRule);
		buttonOkEvent.setName("onclick");
		buttonOkEvent.setMethodName("refOkDelegator");
		if(refnode.getDataListener() != null)
			buttonOkEvent.setControllerClazz(refnode.getDataListener());
		okBtn.addEventConf(buttonOkEvent);
		
		
		
		EventConf buttonCancelEvent = new EventConf();
		buttonCancelEvent.setJsEventClaszz("nc.uap.lfw.core.event.conf.MouseListener");
		buttonCancelEvent.setOnserver(true);
		buttonCancelEvent.setSubmitRule(submitRule);
		buttonCancelEvent.setName("onclick");
		//buttonCancelEvent.setMethodName("refOk");
		
		buttonCancelEvent.setOnserver(false);
		buttonCancelEvent.setScript("parent.hideDialog();");
		
		if(refnode.getDataListener() != null)
			buttonOkEvent.setControllerClazz(refnode.getDataListener());
		cancelBt.addEventConf(buttonCancelEvent);
		
		//widget.getViewComponents().addCommand(CommandManager.getInstance().getCommand("common.reference.ReferenceOk"));
		//widget.getViewComponents().addCommand(CommandManager.getInstance().getCommand("common.reference.ReferenceCancel"));
	}
	
	/**
	 * 为参照创建高级操作区的按钮及Command
	 * 
	 * @param widget
	 * @param refCode
	 */
	private void createCommand4PageMeta(LfwWidget widget, RefNode refnode, AppLfwRefGenUtil util, ILfwRefModel refModel)
	{
		// 创建"刷新"按钮
//		createRefreshButton(widget);
		// 创建"刷新"按钮动作
		//createRefreshCommand(widget);
		
		TextComp locateComp = new TextComp();
		locateComp.setId("locatetext");
		locateComp.setText("定位");
		widget.getViewComponents().addComponent(locateComp);

		// 焦点移出后过滤
		FocusListener focusListener = new FocusListener();
		focusListener.setId("locatetextFocusListener");
		EventHandlerConf blurEvent = FocusListener.getOnBlurEvent();
		blurEvent.setOnserver(false);
		blurEvent.setScript("doFilter(pageUI.getWidget('main').getComponent('locatetext').getValue())");
		focusListener.addEventHandler(blurEvent);
		locateComp.addListener(focusListener);
		
		// 回车后过滤
		KeyListener keyListener = new KeyListener();
		keyListener.setId("locatetextKeyListener");
		EventHandlerConf enterEvent = KeyListener.getOnEnterEvent();
		enterEvent.setOnserver(false);
		enterEvent.setScript("doFilter(pageUI.getWidget('main').getComponent('locatetext').getValue())");
		keyListener.addEventHandler(enterEvent);
		locateComp.addListener(keyListener);
	}
	
	/**
	 * 创建当前页面的事件
	 * @param meta
	 */
	private void createPageMetaListener(PageMeta meta) {
		// 页面加载后事件
//		PageListener listener = new PageListener();
//		listener.setId("refPageListener");
//		EventHandlerConf event = PageListener.getAfterPageInitEvent();
//		event.setOnserver(false);
//		// 如果是在dialog中打开，则先进行数据加载
//		event.setScript("if($_PageShowType!='type_div'){doFilter('')}");
//		listener.addEventHandler(event);
//		meta.addListener(listener);
//		
//		
//		EventConf pageMetaEvent
		
	}
	
	/**
	 * 构造刷新按钮
	 * 
	 * @param widget
	 * @param pageType
	 */
	protected void createRefreshButton(LfwWidget widget)
	{
		ButtonComp refreshBtn = createButtonComp("refreshBtn", "50", "right", "刷新", "/lfw/frame/themes/" + "ncclassic" + "/images/reference/refresh.gif", "refbtn");
		widget.getViewComponents().addComponent(refreshBtn);
	}
	
	/**
	 * 创建按钮
	 * @param id
	 * @param width
	 * @param align
	 * @param text
	 * @param command
	 * @param refImg  所引用图片的路径
	 * @return
	 */
	protected ButtonComp createButtonComp(String id, String width, String align, String text, String refImg, String className)
	{
		ButtonComp btn = new ButtonComp(id);
//		btn.setWidth(width);
//		btn.setAlign(align);
		btn.setText(text);
		if(refImg != null && !refImg.equals(""))
			btn.setRefImg(refImg);
//		if(className != null && !className.equals(""))
//			btn.setClassName(className);
		return btn;
	}

	@Override
	protected void initPageMetaStruct() {
		super.initPageMetaStruct();
		
		WebContext ctx = getWebContext();
		String widgetId = ctx.getParameter("widgetId");
		ctx.getWebSession().setAttribute("widgetId", widgetId);
		if(widgetId == null || widgetId.equals(""))
			throw new LfwRuntimeException("未获取参照所属widget!");
		String refnodeId = ctx.getParameter("nodeId");
		ctx.getWebSession().setAttribute("refNodeId", refnodeId);
		ctx.getWebSession().setAttribute("owner", ctx.getParameter("owner"));
		
		getClientSession().setAttribute(ParamConstant.OTHER_PAGE_UNIQUE_ID, ctx.getParentPageUniqueId(), true);
		getClientSession().setAttribute(ParamConstant.OTHER_PAGE_ID, ctx.getParentPageId(), true);
		
		PageMeta parentPm = ctx.getParentPageMeta();
		RefNode refnode = (RefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refnodeId);
		ILfwRefModel refModel = LfwRefUtil.getRefModel(refnode);
		
		if(refModel instanceof BaseRefModel){
			BaseRefModel bm = (BaseRefModel) refModel;
			getClientSession().setAttribute(SessionConstant.REF_QUERY_SQL, bm.getRefSql(false));
			bm.setFilterValue("REPLACE");
			getClientSession().setAttribute(SessionConstant.REF_FILTER_SQL, bm.getRefSql(false));
			bm.setBlurValue("REPLACE");
			getClientSession().setAttribute(SessionConstant.REF_BLUR_SQL, bm.getRefSql(false));
			
			getClientSession().setAttribute(SessionConstant.REF_QUERY_SQL_ORDER, bm.getOrderSql());
			
			if(refModel instanceof TwoTreeRefModel){//二层树
				bm.setBlurValue("REPLACE");
				TwoTreeRefModel twotreeRefModel = (TwoTreeRefModel)refModel;
				getClientSession().setAttribute(SessionConstant.REF_QUERY_SQL_DIR, twotreeRefModel.getTopClassRefSql(false));
//				getClientSession().setAttribute(SessionConstant.REF_QUERY_SQL_DIR, twotreeRefModel.getClassRefSql(false));
////				// 根据选中的数节点，查询表数据的SQL语句
				String joinSql = " and ( " + twotreeRefModel.getDocJoinField() + " = 'REPLACE' )";
				getClientSession().setAttribute(SessionConstant.REF_QUERY_SQL_DIR_JOIN, joinSql);
				
				getClientSession().setAttribute(SessionConstant.REF_QUERY_SQL_DIR_ORDER, twotreeRefModel.getClassOrderSql());
			}
			else if(refModel instanceof TwoTreeGridRefModel){//二层树表
				// 加载树的数据SQL语句
				bm.setBlurValue("REPLACE");
				TwoTreeGridRefModel treeRefModel = (TwoTreeGridRefModel) refModel;
				getClientSession().setAttribute(SessionConstant.REF_QUERY_SQL_ROOT_SQL, treeRefModel.getTopClassRefSql());
				getClientSession().setAttribute(SessionConstant.REF_QUERY_SQL_DIR, treeRefModel.getClassRefSql(false));
//				// 根据选中的数节点，查询表数据的SQL语句
				String rootjoinSql = " and ( " + treeRefModel.getFirstDocJoinField() + " = 'REPLACE' )";
				getClientSession().setAttribute(SessionConstant.REF_QUERY_SQL_ROOT_JOIN, rootjoinSql);
				
				String joinSql = " and ( " + treeRefModel.getDocJoinField() + " = 'REPLACE' )";
				getClientSession().setAttribute(SessionConstant.REF_QUERY_SQL_DIR_JOIN, joinSql);
				getClientSession().setAttribute(SessionConstant.REF_QUERY_SQL_DIR_ORDER, treeRefModel.getClassOrderSql());
			}
			else if (refModel instanceof TreeGridRefModel) {  // 树表结构
				// 加载树的数据SQL语句
				bm.setBlurValue("REPLACE");
				TreeGridRefModel treeRefModel = (TreeGridRefModel) refModel;
				getClientSession().setAttribute(SessionConstant.REF_QUERY_SQL_DIR, treeRefModel.getClassRefSql(false));
				// 根据选中的数节点，查询表数据的SQL语句
				String joinSql = " and ( " + treeRefModel.getDocJoinField() + " = 'REPLACE' )";
				getClientSession().setAttribute(SessionConstant.REF_QUERY_SQL_DIR_JOIN, joinSql);
				getClientSession().setAttribute(SessionConstant.REF_QUERY_SQL_DIR_ORDER, treeRefModel.getClassOrderSql());
			}
		}
		
		PageMeta winConfig = new PageMeta();
		winConfig.setCaption(this.getPageMeta().getCaption());
		winConfig.setId(getPageModelId());
		AppLifeCycleContext.current().getApplicationContext().getApplication().addWindow(winConfig);
		
	}
	
}
