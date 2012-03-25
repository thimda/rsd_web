package nc.uap.lfw.core.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.base.ExtendAttributeSupport;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.WidgetUIContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.WidgetListener;
import nc.uap.lfw.core.uimodel.conf.Model;
import nc.uap.lfw.jsp.uimeta.UIElement;

public class LfwWidget extends WebComponent {
	private static final long serialVersionUID = -750733078068300535L;
	//片段对外输出的时间常量。
	public static final String UNIQUE_TS = ExtendAttributeSupport.DYN_ATTRIBUTE_KEY + "$UNIQUE_TS";
	//记录本片段最后修改时间的常量
	public static final String MODIFY_TS = ExtendAttributeSupport.DYN_ATTRIBUTE_KEY + "$MODIFY_TS";
	//记录本片段唯一标识的常量
	public static final String UNIQUE_ID = ExtendAttributeSupport.DYN_ATTRIBUTE_KEY + "$UNIQUE_ID";
	
	public static final String WIDGET_CAPTION = "WIDGET_CAPTION";
	
	public static final String BODYINFO = "$BODYINFO";
	
	public static final String POOLWIDGET = "$POOLWIDGE";
	
	private String editFormularClazz;
	// 视图模型
	private ViewModels viewModels = new ViewModels(this);
	// 视图控件
	private ViewComponents viewComponents = new ViewComponents(this);
//	private ViewContainers viewConinters = new ViewContainers(this);
	
	private ViewMenus viewMenus = new ViewMenus(this);

	private boolean dialog = false;
	
	private String caption;
	
	private String langDir;
		
	private String i18nName;
	
	private PageMeta pagemeta;
	
	private String refId;
	
	// 单据操作状态
//	private String operatorState = IOperatorState.INIT;
	
	// 单据业务状态
//	private String busiState = null;
	
	// 自定义状态
//	private String userState = null;
	
	/**
	 * controller类全路径
	 */
	private String controllerClazz = null;
	
	//输入描述
	private List<PluginDesc> pluginDescs;
	//输出描述
	private List<PlugoutDesc> plugoutDescs;
	
	private List<Model> models = null;
	
	private String foldPath;

	//内容构造类
	private String provider;
	
	public String getControllerClazz() {
		return controllerClazz;
	}

	public void setControllerClazz(String controllerClazz) {
		this.controllerClazz = controllerClazz;
	}
	
	public String getLangDir() {
		return langDir;
	}

	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}

	public String getEditFormularClazz() {
		return editFormularClazz;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		Dataset[] dss = this.getViewModels().getDatasets();
		for (int i = 0; i < dss.length; i++) {
			Dataset ds = dss[i];
			ds.setEnabled(enabled);
		}
		WebComponent[] components = this.getViewComponents().getComponents();
		for (int i = 0; i < components.length; i++) {
			WebComponent comp = components[i];
			comp.setEnabled(enabled);
		}
		
		MenubarComp[] mbs = this.getViewMenus().getMenuBars();
		if(mbs != null && mbs.length > 0){
			for (int i = 0; i < mbs.length; i++) {
				MenubarComp mb = (MenubarComp) mbs[i];
				List<MenuItem> items = mb.getMenuList();
				if(items != null){
					Iterator<MenuItem> it = items.iterator();
					while(it.hasNext()){
						MenuItem item = it.next();
						item.setEnabled(enabled);
					}
				}
			}
		}
	}

	public void setEditFormularClazz(String editFormularClazz) {
		this.editFormularClazz = editFormularClazz;
	}

	public void setViewModels(ViewModels viewModels) {
		viewModels.setWidget(this);
		this.viewModels = viewModels;
	}

	public ViewModels getViewModels() {
		return viewModels;
	}

	public void setViewComponents(ViewComponents viewComponents) {
		viewComponents.setWidget(this);
		this.viewComponents = viewComponents;
	}

	public ViewComponents getViewComponents() {
		return viewComponents;
	}

//	public ViewContainers getViewConinters() {
//		return viewConinters;
//	}
//
//	public void setViewConinters(ViewContainers viewConinters) {
//		viewConinters.setWidget(this);
//		this.viewConinters = viewConinters;
//	}
	
	public ViewMenus getViewMenus() {
		return viewMenus;
	}

	public void setViewMenus(ViewMenus viewMenus) {
		viewMenus.setWidget(this);
		this.viewMenus = viewMenus;
	}

//	public String getMasterDataset() {
//		return masterDataset;
//	}
//
//	public void setMasterDataset(String masterDataset) {
//		this.masterDataset = masterDataset;
//	}
//
//	public String getSlaveDatasets() {
//		return slaveDatasets;
//	}
//
//	public void setSlaveDatasets(String slaveDatasets) {
//		this.slaveDatasets = slaveDatasets;
//	}

	@Override
	public Object clone() {
		LfwWidget widget = (LfwWidget) super.clone();
		if (this.viewModels != null)
			widget.viewModels = (ViewModels) this.viewModels.clone();
		if (this.viewComponents != null)
			widget.viewComponents = (ViewComponents) this.viewComponents.clone();
		if (this.viewMenus != null)
			widget.viewMenus = (ViewMenus) this.viewMenus.clone();
//		if (this.viewConinters != null)
//			widget.viewConinters = (ViewContainers) this.viewConinters.clone();
		if (this.pluginDescs != null){
			widget.pluginDescs = new ArrayList<PluginDesc>();
			for (PluginDesc pluginDesc : this.pluginDescs){
				widget.pluginDescs.add((PluginDesc)pluginDesc.clone());
			}
		}
		if (this.plugoutDescs != null){
			widget.plugoutDescs = new ArrayList<PlugoutDesc>();
			for (PlugoutDesc plugoutDesc : this.plugoutDescs){
				widget.plugoutDescs.add((PlugoutDesc)plugoutDesc.clone());
			}
		}
		return widget;

	}

	@Override
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
//		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
////		list.add(WidgetListener.class);
//		//if(this.isDialog()){
//			list.add(DialogListener.class);
//		//}
//		return list;
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(WidgetListener.class);
		return list;
	}

	public void adjustForRuntime() {
		DsRelationAdjuster adj = new DsRelationAdjuster(this);
		adj.adjust();
	}

	public boolean isDialog() {
		return dialog;
	}

	public void setDialog(boolean isDialog) {
		this.dialog = isDialog;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
		notify(WIDGET_CAPTION, null);
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		i18nName = name;
	}
	
	@Override
	public BaseContext getContext() {
		WidgetUIContext ctx = new WidgetUIContext();
		ctx.setVisible(this.visible);
//		ctx.setCos(operatorState);
//		ctx.setCbs(busiState);
//		ctx.setCus(userState);
		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		WidgetUIContext cardCtx = (WidgetUIContext) ctx;
		setVisible(cardCtx.isVisible());
//		setOperatorState(cardCtx.getCos());
//		setBusiState(cardCtx.getCbs());
//		setUserState(cardCtx.getCus());
		setCtxChanged(false);
		super.setContext(ctx);
	}

	public PageMeta getPagemeta() {
		return pagemeta;
	}

	public void setPagemeta(PageMeta pagemeta) {
		this.pagemeta = pagemeta;
	}

	
	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public List<PluginDesc> getPluginDescs() {
		return pluginDescs;
	}

	public void setPluginDescs(List<PluginDesc> pluginDescs) {
		this.pluginDescs = pluginDescs;
	}
	
	public PluginDesc getPluginDesc(String id) {
		if (pluginDescs == null)
			return null;
		for (PluginDesc p : pluginDescs){
			if (id.equals(p.getId())){
				return p;
			}
		}
		return null;
	}
	
	public void addPluginDescs(PluginDesc pluginDesc){
		if (this.pluginDescs == null)
			this.pluginDescs = new ArrayList<PluginDesc>();
		this.pluginDescs.add(pluginDesc);
	}

	public List<PlugoutDesc> getPlugoutDescs() {
		return plugoutDescs;
	}

	public PlugoutDesc getPlugoutDesc(String id) {
		if (plugoutDescs == null)
			return null;
		for (PlugoutDesc p : plugoutDescs){
			if (id.equals(p.getId())){
				return p;
			}
		}
		return null;
	}
	
	public void setPlugoutDescs(List<PlugoutDesc> plugoutDescs) {
		this.plugoutDescs = plugoutDescs;
	}
	
	public void addPlugoutDescs(PlugoutDesc plugoutDesc){
		if (this.plugoutDescs == null)
			this.plugoutDescs = new ArrayList<PlugoutDesc>();
		this.plugoutDescs.add(plugoutDesc);
	}

	public void addModel(Model model){
		if(models == null){
			models = new ArrayList<Model>();
		}
		models.add(model);
	}
	
	public List<Model> getModels() {
		return models;
	}

	public void setModels(List<Model> models) {
		this.models = models;
	}

	public String getFoldPath() {
		return foldPath;
	}

	public void setFoldPath(String foldPath) {
		this.foldPath = foldPath;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String builder) {
		this.provider = builder;
	}
	
	private void notify(String type, Object obj){
		if(LifeCyclePhase.ajax.equals(getPhase())){			
			Map<String,Object> map = new HashMap<String,Object>();
			String widgetId = this.getId();
			map.put("widgetId", widgetId);
			map.put("type", type);
			this.getWidget().notifyChange(UIElement.UPDATE, map);
		}
	}
	
}
