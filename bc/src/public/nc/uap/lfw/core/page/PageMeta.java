package nc.uap.lfw.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.PageUIContext;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.PageListener;
import nc.uap.lfw.core.exception.LfwParseException;
import nc.uap.lfw.core.uimodel.WidgetConfig;
/**
 * 页面元数据信息。此类是5个配置文件的对应体。对外只暴露components和model，以及pagemeta本身信息
 * @author dengjt
 *
 */
public class PageMeta extends WebElement implements Cloneable, Serializable{
	public static final String PROP_SHOWCARDFIRST = "PROP_SHOWCARDFIRST";
	public static final String MODIFY_TS = "$MODIFY_TS";
//	public static final String FOLD_PATH = ExtendAttributeSupport.DYN_ATTRIBUTE_KEY + "_FOLDPATH";
	public static final String WIN_TYPE_APP = "App";
	public static final String WIN_TYPE_PAGE = "Page";
	private static final long serialVersionUID = 2275080997090171371L;
	
	//查询模板标识
	public static final String $QUERYTEMPLATE = "$QueryTemplate";
	
	//对应Processor类名
	private String processorClazz;
	
	private String caption = null;
	
	private String i18nName = null;
	
	private String editFormularClazz = "nc.uap.lfw.core.model.formular.DefaultEditFormularService";
	
	private Map<String, LfwWidget> widgetMap = new HashMap<String, LfwWidget>();
	
	private Map<String, Connector> connectorMap = new HashMap<String, Connector>();
	
	// 菜单
	private ViewMenus viewMenus = new ViewMenus(this);
	
	private String etag;
	
	// 节点中存放图片的路径
	private String nodeImagePath = "";
	/**
	 * controller类全路径
	 */
	private String controllerClazz = null;
	
	private String windowType;
	
	private String foldPath;

	public static final String TagName = "PageMeta";
	//片段引用列表
	private List<WidgetConfig> widgetList = new ArrayList<WidgetConfig>();
	
	private Boolean hasChanged = false;	
	
	public String getControllerClazz() {
		return controllerClazz;
	}

	public void setControllerClazz(String controllerClazz) {
		this.controllerClazz = controllerClazz;
	}

	public PageMeta() {
	}

	
	public String getProcessorClazz() {
		return processorClazz;
	}


	public void setProcessorClazz(String processorClazz) {
		this.processorClazz = processorClazz;
	}

	public Object clone(){
		PageMeta meta = (PageMeta) super.clone(); 
		meta.widgetMap = new HashMap<String, LfwWidget>();
		Iterator<LfwWidget> widgetIt = this.widgetMap.values().iterator();
		while(widgetIt.hasNext()){
			meta.addWidget((LfwWidget) widgetIt.next().clone());
		}
		
		meta.connectorMap = new HashMap<String, Connector>();
		Iterator<Connector> connectorIt = this.connectorMap.values().iterator();
		while(connectorIt.hasNext()){
			meta.addConnector((Connector) connectorIt.next().clone());
		}
		
		if(this.viewMenus != null)
			meta.viewMenus = (ViewMenus)this.viewMenus.clone();
		
//		meta.pageStates = (PageStates) this.pageStates.clone();
		return meta;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

//	public Map<String, String> getServiceMap() {
//		return serviceMap;
//	}
//
//	public void setServiceMap(Map<String, String> serviceMap) {
//		this.serviceMap = serviceMap;
//	}
//	
//	public void addService(PageService pageService) {
//		this.serviceMap.put(pageService.getName(), pageService.getClassName());
//	}
	
	public LfwWidget[] getWidgets() {
		return this.widgetMap.values().toArray(new LfwWidget[0]);
	}
	
	public String[] getWidgetIds() {
		return this.widgetMap.keySet().toArray(new String[0]);
	}
	
	public LfwWidget getWidget(String id) {
		return widgetMap.get(id);
	}
	
	public Map<String, Connector> getConnectorMap() {
		return connectorMap;
	}
	
	public Connector[] getConnectors() {
		return   (Connector[]) connectorMap.values().toArray(new Connector[0]);
	}
	
	public void addConnector(Connector connector){
		this.connectorMap.put(connector.getId(), connector);
	}
	
	public void addWidget(LfwWidget widget){
		if(widgetMap.get(widget.getId()) != null)
			throw new LfwParseException("widget 重复,id:" + widget.getId());
		widget.setPagemeta(this);
		widgetMap.put(widget.getId(), widget);
	}
	
	public void removeWidget(String id) {
		if (widgetMap.containsKey(id))
			widgetMap.remove(id);
	}


	public String getEditFormularClazz() {
		return editFormularClazz;
	}


	public void setEditFormularClazz(String editFormularClazz) {
		this.editFormularClazz = editFormularClazz;
	}

	public String getI18nName() {
		return i18nName;
	}


	public void setI18nName(String name) {
		i18nName = name;
	}


//	public String getMasterWidget() {
//		return masterWidget;
//	}
//
//
//	public void setMasterWidget(String masterWidget) {
//		this.masterWidget = masterWidget;
//	}


	@Override
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(PageListener.class);
		return list;
	}


	public ViewMenus getViewMenus() {
		return viewMenus;
	}


	public void setViewMenus(ViewMenus viewMenus) {
		this.viewMenus = viewMenus;
	}
	
	
	public void adjustForRuntime() {
//		PlugAdjuster adjuster = new PlugAdjuster();
//		adjuster.setPagemeta(this);
//		adjuster.adjust();
		LfwWidget[] ws = getWidgets();
		for (int i = 0; i < ws.length; i++) {
			LfwWidget wd = ws[i];
			wd.adjustForRuntime();
		}
	}


//	public String getOperatorState() {
//		return operatorState;
//	}
//
//
//	public void setOperatorState(String os) {
//		if((this.operatorState == null && os != null) || (this.operatorState != null && !this.operatorState.equals(os))){
//			this.operatorState = os;
//			setCtxChanged(true);
//		}
//	}


	@Override
	public BaseContext getContext() {
		PageUIContext ctx = new PageUIContext();
		ctx.setHasChanged(this.hasChanged);
		return ctx;
	}
	
	/**
	 * 获取页面是否有输入
	 * @return
	 */
	public boolean getHasChanged(){
//		return ((PageUIContext)getContext()).getHasChanged();
		return hasChanged;
	}
	/**
	 * 设置页面是否有输入
	 * @param hasChanged
	 */
	public void setHasChanged(Boolean hasChanged){
//		if(hasChanged != getHasChanged()){
//			((PageUIContext)getContext()).setHasChanged(hasChanged);
//			setCtxChanged(true);
//		}
		if(hasChanged != this.hasChanged){
			this.hasChanged = hasChanged;
			setCtxChanged(true);
		}
	}

	@Override
	public void setContext(BaseContext ctx) {
		super.setContext(ctx);
		PageUIContext pctx = (PageUIContext) ctx;
//		this.setUserState(pctx.getCurrentUserState());
		setHasChanged(pctx.getHasChanged());
		setCtxChanged(false);
	}
//
//	public String getUserState() {
//		return userState;
//	}
//	
//	public void setUserState(String userState) {
//		if(userState == null)
//			return;
//		if(this.userState == null || !this.userState.equals(userState)){
//			this.userState = userState;
//			setCtxChanged(true);
//		}
//	}
	
	/**
	 * 返回标识此PageMeta唯一性的值
	 * @return
	 */
	public String getEtag() {
		return etag;
	}


	public void setEtag(String etag) {
		this.etag = etag;
	}
	
	public String getNodeImagePath() {
		return nodeImagePath;
	}


	public void setNodeImagePath(String nodeImagePath) {
		this.nodeImagePath = nodeImagePath;
	}

	public String getWindowType() {
		return windowType;
	}

	public void setWindowType(String windowType) {
		this.windowType = windowType;
	}

	public String getFoldPath() {
		return foldPath;
	}

	public void setFoldPath(String foldPath) {
		this.foldPath = foldPath;
	}
	
	public void addWidgetConf(WidgetConfig widgetConf) {
		widgetList.add(widgetConf);
	}

//	public List<LfwWidget> getWidgetConfList() {
//		return widgetList;
//	}
	
	public WidgetConfig[] getWidgetConfs(){
		return widgetList.toArray(new WidgetConfig[0]);
	}
	
	public void removeWidgetConf(WidgetConfig wconf){
		widgetList.remove(wconf);
	}
	
	public WidgetConfig getWidgetConf(String id){
		Iterator<WidgetConfig> it = widgetList.iterator();
		while(it.hasNext()){
			WidgetConfig w = it.next();
			if(w.getId().equals(id))
				return w;
		}
		return null;
	}
}
