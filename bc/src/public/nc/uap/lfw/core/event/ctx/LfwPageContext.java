package nc.uap.lfw.core.event.ctx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.ClientSession;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.common.ParamConstant;
import nc.uap.lfw.core.event.AbstractServerEvent;
import nc.uap.lfw.core.page.PageMeta;

public class LfwPageContext {
	public static final String LISTENER_CLAZZ = "listener_clazz";
	public static final String EVENT_NAME = "event_name";
	public static final String WIDGET_ID = "widget_id";
	public static final String SOURCE_ID = "source_id";
	public static final String LISTENER_ID = "listener_id";
	public static final String SOURCE_TYPE = "source_type";
	
	public static final String SOURCE_TYPE_MENUBAR = "menubar";
	public static final String SOURCE_TYPE_MENUBAR_MENUITEM = "menubar_menuitem";
	public static final String SOURCE_TYPE_CONTEXTMENU_MENUITEM = "contextmenu_menuitem";
	public static final String SOURCE_TYPE_TOOLBAR_BUTTON = "toolbar_button";
	public static final String SOURCE_TYPE_TREE = "tree";
	public static final String SOURCE_TYPE_GRID = "grid";
	public static final String SOURCE_TYPE_GRID_HEADER = "grid_header";
	public static final String SOURCE_TYPE_EXCEL = "excel";
	public static final String SOURCE_TYPE_PAGEMETA = "pagemeta";
	public static final String PARENT_SOURCE_ID = "parent_source_id";
	public static final String SOURCE_TYPE_TEXT = "text";
	public static final String SOURCE_TYPE_DATASET = "dataset";
	public static final String SOURCE_TYPE_BUTTON = "button";
	public static final String SOURCE_TYPE_CHART = "chart";
	public static final String SOURCE_TYPE_LINKCOMP = "link";
	public static final String SOURCE_TYPE_IMAGECOMP = "image";
	public static final String SOURCE_TYPE_PROGRESS_BAR = "progress_bar";
	public static final String SOURCE_TYPE_FILEUPLOAD = "file_upload";
	public static final String SOURCE_TYPE_SELF_DEF_COMP = "self_def_comp";
	public static final String SOURCE_TYPE_TAG = "tag";
	public static final String SOURCE_TYPE_OUTLOOKBAR = "outlookbar";
	public static final String SOURCE_TYPE_OUTLOOKBAR_ITEM = "outlookbar_item";
	public static final String SOURCE_TYPE_WIDGT = "widget";
	public static final String SOURCE_TYPE_UIMETA = "uimeta";
	public static final String SOURCE_TYPE_FORMCOMP = "formcomp";
	public static final String SOURCE_TYPE_FORMELE = "form_element";
	public static final String SOURCE_TYPE_FORMGROUP = "formgroup";
	public static final String SOURCE_TYPE_IFRAME = "iframe";
	public static final String SOURCE_TYPE_LABEL = "label";
	public static final String SOURCE_TYPE_SilverlightWidget = "silverlightwidget";
	public static final String SOURCE_TYPE_TEXTAREA = "textarea";
	
//	布局定义类型
//	public static final String SOURCE_TYPE_BORDERLAYOUT = "borderlayout";// border布局
//	public static final String SOURCE_TYPE_BORDERPANEL = "borderpanel"; // border布局中的panel
	public static final String SOURCE_TYPE_BORDER = "border";//边框布局
	public static final String SOURCE_TYPE_BORDERTRUE = "bordertrue";
	public static final String SOURCE_TYPE_CARDLAYOUT = "cardlayout";
	public static final String SOURCE_TYPE_CARDPANEL = "cardpanel";
	public static final String SOURCE_TYPE_FLOWHLAYOUT = "flowhlayout";
	public static final String SOURCE_TYPE_FLOWHPANEL = "flowhpanel";
	public static final String SOURCE_TYPE_FLOWVLAYOUT = "flowvlayout";
	public static final String SOURCE_TYPE_FLOWVPANEL = "flowvpanel";
	public static final String SOURCE_TYPE_GRIDLAYOUT = "gridlayout";
	public static final String SOURCE_TYPE_GRIDROWPANEL = "gridrowpanel";
	public static final String SOURCE_TYPE_GRIDROW = "gridrow";
	public static final String SOURCE_TYPE_GRIDPANEL = "gridpanel";
	public static final String SOURCE_TYPE_PANELLAYOUT = "panellayout";
	public static final String SOURCE_TYPE_PANELPANEL = "panelpanel";
	public static final String SOURCE_TYPE_CANVASLAYOUT = "canvaslayout";
	public static final String SOURCE_TYPE_CANVASPANEL = "canvaspanel";
	public static final String SOURCE_TYPE_PANEL = "panel";
	public static final String SOURCE_TYPE_SPLITERLAYOUT = "spliterlayout";
	public static final String SOURCE_TYPE_SPLITERONEPANEL = "spliteronepanel";
	public static final String SOURCE_TYPE_SPLITERTWOPANLE = "splitertwopanel";
	public static final String SOURCE_TYPE_MENU_GROUP_ITEM = "menu_group_item";
	public static final String SOURCE_TYPE_MENU_GROUP = "menu_group";
	public static final String SOURCE_TYPE_TABITEM = "tabitem";
	public static final String SOURCE_TYPE_TABSPACE = "tabspace";
	public static final String SOURCE_TYPE_HTMLCONTENT = "htmlcontent";
	public static final String SOURCE_FORMELEMENT = "form_element";
	
	
	private Map<String, String> paramMap = new HashMap<String, String>();
	private PageMeta pageMeta;
//	private Map<String, TabLayout> tabMap = new HashMap<String, TabLayout>();
//	private Map<String, CardLayout> cardMap = new HashMap<String, CardLayout>();
//	private Map<String, PanelLayout> panelMap = new HashMap<String, PanelLayout>();
	private Map<String, WidgetContext> widgetMap = new HashMap<String, WidgetContext>();
	private List<String> execScriptList;
	
	private LfwPageContext parentGlobalContext;
	private AbstractServerEvent<?> currentEvent;
	private boolean parent = false;
	private List<String> beforeExecScriptList;
	private ClientSession clientSession = new ClientSession();
	private Map<String, Object> objMap = null;
	
	public void setParameter(String key, String value){
		paramMap.put(key, value);
	}
	
	public String getParameter(String key){
		return paramMap.get(key);
	}
	
	public Map<String, String> getParamMap() {
		return paramMap;
	}
	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}

	public PageMeta getPageMeta() {
		return pageMeta;
	}

	public void setPageMeta(PageMeta pagemeta) {
		this.pageMeta = pagemeta;
	}

//	public TabLayout[] getTabs(){
//		return tabMap.values().toArray(new TabLayout[0]);
//	}
//	
//	public TabLayout getTab(String id){
//		return tabMap.get(id);
//	}
//	
//	public void addTab(TabLayout tab){
//		tabMap.put(tab.getId(), tab);
//	}
//	
//	public CardLayout[] getCards() {
//		return cardMap.values().toArray(new CardLayout[0]);
//	}
//	
//	public CardLayout getCard(String id){
//		return cardMap.get(id);
//	}
//	
//	public void addCard(CardLayout card){
//		cardMap.put(card.getId(), card);
//	}
//	
//	public PanelLayout[] getPanels() {
//		return panelMap.values().toArray(new PanelLayout[0]);
//	}
//	
//	public PanelLayout getPanel(String id){
//		return panelMap.get(id);
//	}
//	
//	public void addPanel(PanelLayout panel){
//		panelMap.put(panel.getId(), panel);
//	}
	
	public WidgetContext[] getWidgetContexts() {
		return widgetMap.values().toArray(new WidgetContext[0]);
	}
	
	public WidgetContext getWidgetContext(String id) {
		return widgetMap.get(id);
	}
	
	public void addWidgetContext(WidgetContext ctx){
		widgetMap.put(ctx.getId(), ctx);
	}
	
	public WebSession getWebSession() {
		return LfwRuntimeEnvironment.getWebContext().getWebSession();
	}
	
	public WebContext getWebContext() {
		return LfwRuntimeEnvironment.getWebContext();
	}

	public List<String> getExecScript() {
		return execScriptList;
	}

	/**
	 * 添加执行脚本
	 * @param execScript
	 */
	public int addExecScript(String execScript) {
		if(this.execScriptList == null){
			this.execScriptList = new ArrayList<String>();
		}
		this.execScriptList.add(execScript);
		return this.execScriptList.size() - 1;
	}
	
	/**
	 * 删除某个位置的脚本
	 * @param index
	 */
	public void removeExecScript(int index) {
		if(this.execScriptList == null)
			return;
		this.execScriptList.remove(index);
	}

	public void setParentGlobalContext(LfwPageContext pcontext) {
		this.parentGlobalContext = pcontext;
		pcontext.parent = true;
	}

	public LfwPageContext getParentGlobalContext() {
		return parentGlobalContext;
	}
//	
//	public String[] getEventParamsKeys() {
//		return eventParamMap.keySet().toArray(new String[0]);
//	}
//	
//	public String getEventParamValue(String key) {
//		return eventParamMap.get(key);
//	}
//	
//	public void addEventParam(String key, String value){
//		eventParamMap.put(key, value);
//	}

	public AbstractServerEvent<?> getCurrentEvent() {
		return currentEvent;
	}

	public void setCurrentEvent(AbstractServerEvent<?> currentEvent) {
		this.currentEvent = currentEvent;
	}
	
	public void showModalDialog(String url, String title, String width, String height, String id, boolean refresh, boolean clearState) {
		String pageUniqueId = LfwRuntimeEnvironment.getWebContext().getPageUniqueId();
		if(url.indexOf("?") != -1)
			url += "&";
		else
			url += "?";
		url += ParamConstant.OTHER_PAGE_UNIQUE_ID + "=" + pageUniqueId;
		//showDialog(pageUrl, title, width, height, id, refresh, clearState) {
		this.addExecScript("showDialog(\"" + url + "\", \"" + title + "\", " + width + "," + height + ", \"" + id + "\", " + refresh + "," + clearState + ");");
	}
	
	public void hideCurrentDialog() {
		if(parent){
			this.addExecScript("parent.hideDialog();");
		}
		else{
			this.addExecScript("window.hideDialog();");
		}
	}
	
	/**
	 * 关闭一个窗口
	 */
	public void closeWindow(){
		this.addExecScript("window.close();");
	}
	
	public void undo(String widgetId, String[] dssIds){
		for (int i = 0; i < dssIds.length; i++) {
			this.addExecScript("pageUI.getWidget('" + widgetId + "').getDataset('" + dssIds[i] + "').undo();\n");
		}
		this.addBeforeExecScript("pageUI.getWidget('" + widgetId + "').undo();\n");
		this.addBeforeExecScript("pageUI.undo();\n");
	}
	
	/**
	 * 以下三个函数根据dataset的是否控制页面操作状态，是否控制widget操作状态分别undo
	 */
	public void datasetUndo(String widgetId, String[] dssIds){
		for (int i = 0; i < dssIds.length; i++) {
			this.addExecScript("pageUI.getWidget('" + widgetId + "').getDataset('" + dssIds[i] + "').undo();\n");
		}
	}
	
	public void widgetUndo(String widgetId){
		this.addBeforeExecScript("pageUI.getWidget('" + widgetId + "').undo();\n");
	}
	
	public void pageUndo(){
		this.addBeforeExecScript("pageUI.undo();\n");
	}
	
	
	
	public void recordUndo(String widgetId, String[] dssIds){
		this.addBeforeExecScript("pageUI.recordUndo();\n");
		this.addBeforeExecScript("pageUI.getWidget('" + widgetId + "').recordUndo();\n");
		for (int i = 0; i < dssIds.length; i++) {
			this.addBeforeExecScript("pageUI.getWidget('" + widgetId + "').getDataset('" + dssIds[i] + "').recordUndo();\n");
		}
	}
	
	/**
	 * 以下三个函数根据dataset的是否控制页面操作状态，是否控制widget操作状态分别undo
	 */
	public void pageRecordUndo(){
		this.addBeforeExecScript("pageUI.recordUndo();\n");
	}
	
	public void widgetRecordUndo(String widgetId){
		this.addBeforeExecScript("pageUI.getWidget('" + widgetId + "').recordUndo();\n");
	}
	
	public void datasetRecordUndo(String widgetId, String[] dssIds){
		for (int i = 0; i < dssIds.length; i++) {
			this.addBeforeExecScript("pageUI.getWidget('" + widgetId + "').getDataset('" + dssIds[i] + "').recordUndo();\n");
		}
	}
	
	public void clearUndo(String widgetId, String[] dssIds){
		for (int i = 0; i < dssIds.length; i++) {
			this.addExecScript("pageUI.getWidget('" + widgetId + "').getDataset('" + dssIds[i] + "').clearUndo();\n");
		}
	}
	
	/**
	 * 打开一个新窗口
	 * @param url
	 * @param title
	 * @param width
	 * @param height
	 */
	public void openNewWindow(String url, String title, String width, String height) {
		String pageUniqueId = LfwRuntimeEnvironment.getWebContext().getPageUniqueId();
		if(url.indexOf("?") != -1)
			url += "&";
		else
			url += "?";
		url += ParamConstant.OTHER_PAGE_UNIQUE_ID + "=" + pageUniqueId;
//		this.addExecScript("window.open('" + url + "', '" + title + "', 'height=" + height + ",width=" + width + "', true);\n");
		this.addExecScript("openWindowInCenter('" + url + "', '" + title + "', " + height + ", " + width + ", true);\n");
	}
	
	/**
	 * 打开一个新窗口(非模态)
	 * @param url
	 * @param title
	 * @param width
	 * @param height
	 * @param height
	 */
	public void openNewNormalWindow(String url, String title, String width, String height,boolean closeParent) {
		String pageUniqueId = LfwRuntimeEnvironment.getWebContext().getPageUniqueId();
		if(url.indexOf("?") != -1)
			url += "&";
		else
			url += "?";
		url += ParamConstant.OTHER_PAGE_UNIQUE_ID + "=" + pageUniqueId;
		this.addExecScript("openNormalWindowInCenter('" + url + "', '" + title + "', " + height + ", " + width + ", " + closeParent + ");\n");
	}
	
	/**
	 * 打开一个最大化窗口
	 * @param url
	 * @param title
	 * @param closeParent 是否关闭父窗口
	 */
	public void openMaxWindow(String url, String title, boolean closeParent) {
		this.addExecScript("showMaxWin('" + url + "', '" + title + "', " + closeParent + ")");
//		this.addExecScript("window.open('" + url + "', '" + title + "', 'resizable=no,fullscreen=1', true);\n");
//		if (closeParent) {
//			/**
//			 * 关闭父窗口
//			 * Firefox下默认不支持（必须修改Firefox配置内容，地址栏输入 about:config ，修改 dom.allow_scripts_to_close_windows 属性为 true ）
//			 */
//			this.addExecScript("window.close();");
//		}
	}
	
	public void sendRedirect(String url) {
		this.addExecScript("window.location.href='" + url + "'\n");
	}
	
	public void historyBack() {
		String referer = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(WebSession.REFERER);
		if(referer != null)
			sendRedirect(referer);
		else
			this.addExecScript("window.history.go(-1);");
	}
	
	public void refreshWindow() {
		this.addExecScript("window.location.href = window.location.href");
	}

	public ClientSession getClientSession() {
		return clientSession;
	}

	public void setClientSession(ClientSession clientSession) {
		this.clientSession = clientSession;
	}

	public void setClientAttribute(String key, String value) {
		clientSession.setAttribute(key, value);
	}
	

	public List<String> getBeforeExecScript() {
		return beforeExecScriptList;
	}
	
	public boolean isParent() {
		return parent;
	}

	/**
	 * 添加临时数据到页面上下文中
	 * @param key
	 * @param value
	 */
	public void addUserObject(String key, Object value){
		if(objMap == null){
			objMap = new HashMap<String, Object>();
		}
		objMap.put(key, value);
	}
	
	public Object getUserObject(String key){
		if(objMap == null)
			return null;
		return objMap.get(key);
	}

	public void addBeforeExecScript(String beforeExecScript) {
		if(this.beforeExecScriptList == null){
			this.beforeExecScriptList = new ArrayList<String>();
		}
		this.beforeExecScriptList.add(beforeExecScript);
	}
	/**
	 * 在Iframe中下载文件
	 * @param url
	 */
	public void downloadFileInIframe(String url){
		StringBuffer sb = new StringBuffer();
		sb.append("if (window.sys_DownFileFrame == null) {");
		sb.append("var frm = $ce('iframe');");
		sb.append("frm.frameborder = 0;");
		sb.append("frm.vspace = 0;");
		sb.append("frm.hspace = 0;");
		sb.append("frm.style.width = '1px';");
		sb.append("frm.style.heigh = 0;");
		sb.append("window.sys_DownFileFrame = frm;");
		sb.append("document.body.appendChild(window.sys_DownFileFrame);");
		sb.append("}");
		sb.append("window.sys_DownFileFrame.src='"+url+"';");	
		addExecScript(sb.toString());
	}
	/**
	 * 下载文件
	 * @param url
	 */
	public void downloadFile(String url){
		addExecScript("window.open('"+url+"')");
	}

	public void viewFileInIframe(String url) {
		StringBuffer sb = new StringBuffer();
		sb.append("if (window.sys_ViewFileFrame == null) {");
		sb.append("var frm = $ce('iframe');");
		sb.append("frm.frameborder = 0;");
		sb.append("frm.vspace = 0;");
		sb.append("frm.hspace = 0;");
		sb.append("frm.style.display = \"none\";");
		sb.append("window.sys_ViewFileFrame = frm;");
		sb.append("document.body.appendChild(window.sys_ViewFileFrame);");
		sb.append("}");
		sb.append("window.sys_ViewFileFrame.contentWindow.open('" + url + "','_blank','width=800,height=600,menubar=no,toolbar=no,location=no,directories=no,status=no, scrollbars=yes,resizable=yes');");
		addExecScript(sb.toString());
	}
}
