package nc.uap.lfw.core.ctrlfrm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.ctrlfrm.plugin.ControlPluginConfig;
import nc.uap.lfw.core.ctrlfrm.plugin.ControlPluginImpl;
import nc.uap.lfw.core.ctrlfrm.plugin.IControlPlugin;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIDialog;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIWidget;
import nc.uap.lfw.ra.itf.IUIRender;
import nc.uap.lfw.ra.itf.IUIRenderGroup;
import nc.uap.lfw.util.LfwClassUtil;

public class ControlFramework {
	public static final String TYPE_APPCORE = "appcore";
	public static final String TYPE_APPCOREEDIT = "appcoreedit";
	public static final String TYPE_PAGECORE = "pagecore";
	public static final String TYPE_PAGECOREEDIT = "pagecoreedit";
	private List<ControlPluginConfig> ctrlPluginConfigList = new ArrayList<ControlPluginConfig>();
	private List<IControlPlugin> ctrlPluginList;
	private Map<String, IControlPlugin> clazzControlMap = null;
	private Map<String, IControlPlugin> uiclazzControlMap = null;
	private Map<String, IControlPlugin> typeControlMap = null;
	private Set<IControlPlugin> viewZoneSet = null;
	private Set<IControlPlugin> windowZoneSet = null;
	private Map<String, IControlPlugin> fullNameControlMap = null;
//	private Map<String, IControlPlugin> cssFullNameControlMap = null;
	private static ControlFramework instance = new ControlFramework();
	private static IUIRenderGroup renderGroup = (IUIRenderGroup) LfwClassUtil.newInstance("nc.uap.lfw.ra.group.PCUIRenderGroup");;
	private Map<String, String> compressedJsMap = null;
	private Map<String, String> compressedCssMap = null;
	private ControlFramework() {
		clazzControlMap = new HashMap<String, IControlPlugin>();
		uiclazzControlMap = new HashMap<String, IControlPlugin>();
		typeControlMap = new HashMap<String, IControlPlugin>();
		viewZoneSet = new HashSet<IControlPlugin>();
		windowZoneSet = new HashSet<IControlPlugin>();
		fullNameControlMap = new HashMap<String, IControlPlugin>();
		ctrlPluginList = new ArrayList<IControlPlugin>();
		compressedJsMap = new HashMap<String, String>();
		compressedCssMap = new HashMap<String, String>();
	}
	
	public IControlPlugin getControlPluginByType(String type){
		return typeControlMap.get(type);
	}
	public IControlPlugin getControlPluginByClass(Class<?> c){
		if(ComboData.class.isAssignableFrom(c))
			c = ComboData.class;
		return clazzControlMap.get(c.getName());
	}
	
	public IControlPlugin getControlPluginByUIClass(Class<?> clazz){
		Class<?> c = clazz;
		if(clazz.equals(UIDialog.class))
			c = UIWidget.class;
		return uiclazzControlMap.get(c.getName());
	}
	
	public IControlPlugin getControlPluginByFullName(String fullName){
		return fullNameControlMap.get(fullName);
	}
	
	public static ControlFramework getInstance() {
		return instance;
	}
	
	public IControlPlugin[] getViewControlPlugins() {
		return null;
	}
	
//	public void addCompressedJs(String key, String js){
//		compressedJsMap.put(key, js);
//	}
//	
//	public String getCompressedJs(String key){
//		return compressedJsMap.get(key);
//	}
//	
//	public void addCompressedCss(String key, String css){
//		compressedJsMap.put(key, css);
//	}
//	
//	public String getCompressedCss(String key){
//		return compressedCssMap.get(key);
//	}
	
	public IControlPlugin[] getViewZonePlugins() {
		return viewZoneSet.toArray(new IControlPlugin[0]);
	}
	public void addControlPluginConfig(ControlPluginConfig config) {
		ctrlPluginConfigList.add(config);
		ControlPluginImpl cpi = new ControlPluginImpl(config);
		ctrlPluginList.add(cpi);
		String javaClazz = config.getJavaclazz();
		if(javaClazz != null && !javaClazz.equals(""))
			clazzControlMap.put(config.getJavaclazz(), cpi);
		
		String uiJavaClazz = config.getUijavaclazz();
		if(uiJavaClazz != null && !uiJavaClazz.equals(""))
			uiclazzControlMap.put(uiJavaClazz, cpi);
		
		String id = config.getId();
		typeControlMap.put(id, cpi);
		
		String zoneStr = config.getZone();
		if(zoneStr != null && !zoneStr.equals("")){
			String[] zones = zoneStr.split(",");
			for (int i = 0; i < zones.length; i++) {
				String zone = zones[i];
				if(zone.equals(ControlPluginConfig.ZONE_VIEW)){
					viewZoneSet.add(cpi);
				}
				else if(zone.equals(ControlPluginConfig.ZONE_WINDOW)){
					windowZoneSet.add(cpi);
				}
			}
		}
		String fullNameStr = config.getFullname();
		if(fullNameStr != null && !fullNameStr.equals("")){
			String[] fullNames = fullNameStr.split(",");
			fullNameControlMap.put(fullNames[0], cpi);
		}
//		String cssFullName = config.getCssfullname();
	}
	
	public IControlPlugin[] getAllControlPlugins() {
		return ctrlPluginList.toArray(new IControlPlugin[0]);
	}
	
	public IUIRender getUIRender(UIMeta uimeta, UIElement uiEle, PageMeta pageMeta, IUIRender parentRender, DriverPhase dp){
		if(uiEle == null)
			return null;
		Class<?> c = null;
		if(uiEle instanceof UIDialog)
			c = UIWidget.class;
		else
			c = uiEle.getClass();
		IControlPlugin cplugin = getControlPluginByUIClass(c);
		return cplugin.getUIRender(uimeta, uiEle, pageMeta, parentRender, dp);
	}

	public static IUIRender getContextMenuUIRender(UIMeta uimeta, UIElement uiEle,ContextMenuComp webEle, PageMeta pageMeta, IUIRender parentRender, DriverPhase pc) {
		IUIRender render =  renderGroup.getContextMenuUIRender(uimeta, uiEle, webEle, pageMeta, parentRender);
		render.setControlPlugin(getInstance().getControlPluginByType("contextmenu"));
		return render;
	}

	public IUIRender getUIRender(UIMeta uiMeta, UIElement uiEle, WebComponent comp, PageMeta pageMeta, IUIRender parentRender, DriverPhase pc) {
		IControlPlugin cplugin = getControlPluginByClass(comp.getClass());
		return cplugin.getUIRender(uiMeta, uiEle, comp, pageMeta, parentRender, pc);
	}
}
