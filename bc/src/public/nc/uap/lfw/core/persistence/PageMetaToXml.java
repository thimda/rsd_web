package nc.uap.lfw.core.persistence;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.uimodel.WidgetConfig;
import nc.uap.lfw.core.util.AMCUtil;
import nc.vo.jcom.xml.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 将pageMeta持久化为xml
 * @author gd 2010-1-25
 * @version NC6.0
 * @since NC6.0
 */
public class PageMetaToXml {
	
	/**
	 * 将PageMeta持久化为xml文件
	 * @param filePath 文件全路径
	 * @param fileName 文件名
	 * @param projectPath 工程路径
	 * @param meta
	 */
	public static void toXml(String filePath, String fileName, String projectPath, PageMeta meta) {
	
		Document doc = getDocumentByPageMeta(meta);
		// 写出文件
		PersistenceUtil.toXmlFile(doc, filePath, fileName);
	}
	
	public static String toString(PageMeta meta){
		Document doc = getDocumentByPageMeta(meta);
		Writer wt = new StringWriter();
		
        XMLUtil.printDOMTree(wt, doc, 0, "UTF-8");
        String xmlStr = wt.toString();
		return xmlStr;
	}
	
	private static Document getDocumentByPageMeta(PageMeta meta){

		Document doc = XMLUtil.getNewDocument();
		Element rootNode = doc.createElement("PageMeta");
		//rootNode.setAttribute("masterWidget", meta.getMasterWidget());
		if(isNotNullString(meta.getId())){
			rootNode.setAttribute("id", meta.getId());
		}
		if(isNotNullString(meta.getCaption())){
			rootNode.setAttribute("caption", meta.getCaption());
		}
		if(isNotNullString(meta.getI18nName())){
			rootNode.setAttribute("i18nName", meta.getI18nName());
		}
		if(isNotNullString(meta.getControllerClazz())){
			rootNode.setAttribute("controllerClazz", meta.getControllerClazz());
		}
		rootNode.setAttribute("sourcePackage", meta.getSourcePackage());
		if(isNotNullString(meta.getWindowType())){
			rootNode.setAttribute("windowType", meta.getWindowType());
		}
		doc.appendChild(rootNode);
		
		Element processorNode = doc.createElement("Processor");
		rootNode.appendChild(processorNode);
		processorNode.appendChild(doc.createTextNode(meta.getProcessorClazz()));
		
		// <PageStates>
//		PageStates pageStates = meta.getPageStates();
//		if(pageStates != null){
//			PageState[] states = pageStates.getPageStates();
//			Element pageStatesNode = doc.createElement("PageStates");
//			rootNode.appendChild(pageStatesNode);
//			
//			String currentState = pageStates.getCurrentState();
//			if(currentState != null && !currentState.equals(""))
//				pageStatesNode.setAttribute("currentState", currentState);
//			
//			for (int i = 0; i < states.length; i++) 
//			{
//				PageState state = states[i];
//				Element pageStateNode = doc.createElement("PageState");
//				pageStatesNode.appendChild(pageStateNode);
//				Element keyNode = doc.createElement("Key");
//				keyNode.appendChild(doc.createTextNode(state.getKey()));
//				pageStateNode.appendChild(keyNode);
//				if(state.getName() != null && !"".equals(state.getName()))
//				{
//					Element nameNode = doc.createElement("Name");
//					pageStateNode.appendChild(nameNode);
//					nameNode.appendChild(doc.createTextNode(state.getName()));
//				}
//			}
//		}
		
		Element widgetsNode = doc.createElement("Widgets");
		rootNode.appendChild(widgetsNode);
		WidgetConfig[] widgetConfs = meta.getWidgetConfs();
		for (int i = 0; i < widgetConfs.length; i++) {
			WidgetConfig widget = widgetConfs[i];
			Element widgetNode = doc.createElement("Widget");
			widgetsNode.appendChild(widgetNode);
			widgetNode.setAttribute("id", widget.getId());
			if(isNotNullString(widget.getRefId()))
				widgetNode.setAttribute("refId", widget.getRefId());
			
			addPlugins(doc, widgetNode, widget);
			addPlugouts(doc, widgetNode, widget);
			
			addWidgetExtendAttributes(doc, widgetNode, widget);
		}
		
//		Map<String, Connector> connectorMap = meta.getConnectorMap();
//		if(connectorMap != null && !connectorMap.isEmpty())
//		{
//			Element connectorsNode = doc.createElement("Connectors");
//			rootNode.appendChild(connectorsNode);
//			
//			Iterator<Connector> connectorIt = connectorMap.values().iterator();
//			while(connectorIt.hasNext())
//			{
//				Connector con = connectorIt.next();
//				Element conNode = doc.createElement("Connector");
//				connectorsNode.appendChild(conNode);
//				conNode.setAttribute("id", con.getId());
//				conNode.setAttribute("source", con.getSource());
//				conNode.setAttribute("target", con.getTarget());
//				//TODO
//			}
//		}
		
		Map<String, ExtAttribute> extAttrs = meta.getExtendMap();
		if(extAttrs != null && !extAttrs.isEmpty()){
			Element attributesNode = doc.createElement("Attributes");
			rootNode.appendChild(attributesNode);
			
			Iterator<String> attrIt = extAttrs.keySet().iterator();
			while(attrIt.hasNext())
			{
				String attrKey = attrIt.next();
				ExtAttribute attr = extAttrs.get(attrKey);
				Element attributeNode = doc.createElement("Attribute");
				attributesNode.appendChild(attributeNode);
				Element keyNode = doc.createElement("Key");
				keyNode.appendChild(doc.createTextNode(attrKey));
				attributeNode.appendChild(keyNode);
				Element valueNode = doc.createElement("Value");
				if(attr.getValue() != null)
					valueNode.appendChild(doc.createTextNode(attr.getValue().toString()));
				attributeNode.appendChild(valueNode);
				Element descNode = doc.createElement("Desc");
				if(attr.getDesc() != null)
					descNode.appendChild(doc.createTextNode(attr.getDesc().toString()));
				attributeNode.appendChild(descNode);
			}
		}
		//Events
		AMCUtil.addEvents(doc, meta.getEventConfs(), rootNode);
		// 持久化<Menus>
		addMenus(doc, rootNode, meta);
		//持久化container
//		addContainers(doc, rootNode, meta);
		//持久化plug关联
		addPlugConnectors(doc, rootNode, meta);
		
		return doc;
	}
	
//	private static void addContainers(Document doc, Element rootNode, PageMeta pm) {
//		Element containersNodes = doc.createElement("Containers");
//		rootNode.appendChild(containersNodes);
//		// <TabComp>
//		WebComponent[] tabs = pm.getViewConinters().getContainerByType(TabLayout.class);
//		if(tabs != null)
//		{
//			for (int i = 0; i < tabs.length; i++) {
//				TabLayout tab = (TabLayout) tabs[i];
//				Element tabNode = doc.createElement("TabComp");
//				containersNodes.appendChild(tabNode);
//				tabNode.setAttribute("id", tab.getId());
//				List<TabItem> tabItemList = tab.getItemList();
//				if(tabItemList != null && tabItemList.size() > 0){
//					for (int j = 0; j < tabItemList.size(); j++) {
//						TabItem tabItem = (TabItem) tabItemList.get(j);
//						Element tabItemNode = doc.createElement("TabItem");
//						tabNode.appendChild(tabItemNode);
//						tabItemNode.setAttribute("id", tabItem.getId());
//						if(isNotNullString(tabItem.getI18nName()))
//							tabItemNode.setAttribute("i18nName", tabItem.getI18nName());
//						if(isNotNullString(tabItem.getText()))
//							tabItemNode.setAttribute("text", tabItem.getText());
////						tabItemNode.setAttribute("active", String.valueOf(tabItem.isActive()));
//					}
//				}
//				Map<String, JsListenerConf> jsListeners = tab.getListenerMap();
//				if(jsListeners != null && jsListeners.size() > 0)
//					PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), tabNode);
//			}
//		}
//		
//		WebComponent[] cards = pm.getViewConinters().getContainerByType(CardLayout.class);
//		if(cards != null)
//		{
//			for (int i = 0; i < cards.length; i++) {
//				CardLayout card = (CardLayout) cards[i];
//				Element cardNode = doc.createElement("CardLayout");
//				containersNodes.appendChild(cardNode);
//				cardNode.setAttribute("id", card.getId());
//				Map<String, JsListenerConf> jsListeners = card.getListenerMap();
//				if(jsListeners != null && jsListeners.size() > 0)
//					PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), cardNode);
//			}
//		}
//		
//		WebComponent[] panels = pm.getViewConinters().getContainerByType(PanelLayout.class);
//		if(panels != null)
//		{
//			for (int i = 0; i < panels.length; i++) {
//				PanelLayout panel = (PanelLayout) panels[i];
//				Element panelNode = doc.createElement("PanelLayout");
//				containersNodes.appendChild(panelNode);
//				panelNode.setAttribute("id", panel.getId());
//				Map<String, JsListenerConf> jsListeners = panel.getListenerMap();
//				if(jsListeners != null && jsListeners.size() > 0)
//					PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), panelNode);
//			}
//		}
//		
//		WebComponent[] outlookbars = pm.getViewConinters().getContainerByType(OutlookbarComp.class);
//		if(outlookbars != null)
//		{
//			for (int i = 0; i < outlookbars.length; i++) {
//				OutlookbarComp outlookbar = (OutlookbarComp) outlookbars[i];
//				Element outlookbarNode = doc.createElement("OutlookbarComp");
//				containersNodes.appendChild(outlookbarNode);
//				outlookbarNode.setAttribute("id", outlookbar.getId());
//				List<OutlookItem> outlookList = outlookbar.getItemList();
//				if(outlookList != null && outlookList.size() > 0){
//					for (int j = 0; j < outlookList.size(); j++) {
//						OutlookItem outlookItem = (OutlookItem) outlookList.get(j);
//						Element outlookItemNode = doc.createElement("OutlookItem");
//						outlookbarNode.appendChild(outlookItemNode);
//						outlookItemNode.setAttribute("id", outlookItem.getId());
//						if(isNotNullString(outlookItem.getI18nName()))
//							outlookItemNode.setAttribute("i18nName", outlookItem.getI18nName());
//						if(isNotNullString(outlookItem.getImage()))
//							outlookItemNode.setAttribute("image", outlookItem.getImage());
//					}
//				}
//				Map<String, JsListenerConf> jsListeners = outlookbar.getListenerMap();
//				if(jsListeners != null && jsListeners.size() > 0)
//					PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), outlookbarNode);
//			}
//		}
//		
//	}
	
	private static void addMenus(Document doc, Element rootNode, PageMeta meta)
	{
		MenubarComp[] menubars = meta.getViewMenus().getMenuBars();
		if(menubars.length > 0){
			Element menusNode = doc.createElement("Menus");
			rootNode.appendChild(menusNode);
			for (int i = 0; i < menubars.length; i++) 
			{
				MenubarComp menubar = menubars[i];
				Element menubarNode = doc.createElement("MenuBarComp");
				menusNode.appendChild(menubarNode);
				menubarNode.setAttribute("id", menubar.getId());
				
				Map<String, JsListenerConf> listenerMap = menubar.getListenerMap();
				if(listenerMap != null && !listenerMap.isEmpty())
				{
					JsListenerConf[] listeners = listenerMap.values().toArray(new JsListenerConf[0]);
					PersistenceUtil.addListeners(doc, listeners, menubarNode);
				}
				
				
				List<MenuItem> menuItems = menubar.getMenuList();
				if(menuItems != null && menuItems.size() > 0)
					processMenuItem(menuItems.toArray(new MenuItem[0]), menubarNode, doc);
			}
		}
	}
	
	private static void processMenuItem(MenuItem[] cItems, Element parentNode, Document doc)
	{
		for (int i = 0; i < cItems.length; i++) 
		{
			MenuItem menuItem = cItems[i];
			Element menuItemNode = doc.createElement("MenuItem");
			parentNode.appendChild(menuItemNode);
			menuItemNode.setAttribute("id", menuItem.getId());
			if(menuItem.getI18nName() != null && !menuItem.getI18nName().equals(""))
				menuItemNode.setAttribute("i18nName", menuItem.getI18nName());
			if(menuItem.getText() != null && !menuItem.getText().equals(""))
				menuItemNode.setAttribute("text", menuItem.getText());
//			if(menuItem.getOperatorStatusArray() != null && !menuItem.getOperatorStatusArray().equals(""))
//				menuItemNode.setAttribute("operatorStatusArray", menuItem.getOperatorStatusArray());
//			if(menuItem.getBusinessStatusArray() != null && !menuItem.getBusinessStatusArray().equals(""))
//				menuItemNode.setAttribute("businessStatusArray", menuItem.getBusinessStatusArray());
//			if(menuItem.getUserStatusArray() != null && !menuItem.getUserStatusArray().equals(""))
//				menuItemNode.setAttribute("userStatusArray", menuItem.getUserStatusArray());
//			//可见状态保存
//			if(menuItem.getOperatorVisibleStatusArray() != null && !menuItem.getOperatorVisibleStatusArray().equals(""))
//				menuItemNode.setAttribute("operatorVisibleStatusArray", menuItem.getOperatorVisibleStatusArray());
//			if(menuItem.getBusinessVisibleStatusArray() != null && !menuItem.getBusinessVisibleStatusArray().equals(""))
//				menuItemNode.setAttribute("businessVisibleStatusArray", menuItem.getBusinessVisibleStatusArray());
//			if(menuItem.getUserVisibleStatusArray() != null && !menuItem.getUserVisibleStatusArray().equals(""))
//				menuItemNode.setAttribute("userVisibleStatusArray", menuItem.getUserVisibleStatusArray());
			//
			if(menuItem.getImgIcon() != null && !menuItem.getImgIcon().equals(""))
				menuItemNode.setAttribute("imgIcon", menuItem.getImgIcon());
			if(menuItem.getImgIconOn() != null && !menuItem.getImgIconOn().equals(""))
				menuItemNode.setAttribute("imgIconOn", menuItem.getImgIconOn());
			if(menuItem.getImgIconDisable() != null && !menuItem.getImgIconDisable().equals(""))
				menuItemNode.setAttribute("imgIconDisable", menuItem.getImgIconDisable());
			if(menuItem.getLangDir() != null && !menuItem.getLangDir().equals(""))
				menuItemNode.setAttribute("langDir", menuItem.getLangDir());
			if(menuItem.getHotKey() != null && !menuItem.getHotKey().equals(""))
				menuItemNode.setAttribute("hotKey", menuItem.getHotKey());
			if(menuItem.getDisplayHotKey() != null && !menuItem.getDisplayHotKey().equals(""))
				menuItemNode.setAttribute("displayHotKey", menuItem.getDisplayHotKey());
			menuItemNode.setAttribute("modifiers", String.valueOf(menuItem.getModifiers()));
			Map<String, JsListenerConf> listenerMap = menuItem.getListenerMap();
			if(listenerMap != null && !listenerMap.isEmpty())
			{
				JsListenerConf[] listeners = listenerMap.values().toArray(new JsListenerConf[0]);
				PersistenceUtil.addListeners(doc, listeners, menuItemNode);
			}
			
			if(menuItem.getChildList() != null && menuItem.getChildList().size() > 0)
				processMenuItem(menuItem.getChildList().toArray(new MenuItem[0]), menuItemNode, doc);
		}
	}
	
	

	private static boolean isNotNullString(String param)
	{
		if(param != null && !param.equals(""))
			return true;
		else
			return false;
	}
	
	private static void addPlugConnectors(Document doc, Element rootNode, PageMeta meta){
		Map<String, Connector> connectorMap = meta.getConnectorMap();
		if (connectorMap.size() > 0){
			Element connNode = doc.createElement("Connectors");
			rootNode.appendChild(connNode);
			for (Iterator<String> i = connectorMap.keySet().iterator() ; i.hasNext(); ) {
				String id = i.next();
				Connector conn = connectorMap.get(id);
				Element c = doc.createElement("Connector");
				connNode.appendChild(c);
				c.setAttribute("id", conn.getId());
				c.setAttribute("pluginId", conn.getPluginId());
				c.setAttribute("plugoutId", conn.getPlugoutId());
				c.setAttribute("source", conn.getSource());
				c.setAttribute("target", conn.getTarget());
				Map<String, String> map =  conn.getMapping();
				if (map != null && map.size() > 0){
					Element maps = doc.createElement("Maps");
					c.appendChild(maps);
					for (Iterator<String> j = map.keySet().iterator() ; j.hasNext(); ){
						String outValue = j.next();
						String inValue = map.get(outValue);
						Element e = doc.createElement("Map");
						maps.appendChild(e);
						Element outValueEle = doc.createElement("outValue");
						outValueEle.appendChild(doc.createTextNode(outValue));
						e.appendChild(outValueEle);
						Element inValueEle = doc.createElement("inValue");
						inValueEle.appendChild(doc.createTextNode(inValue));
						e.appendChild(inValueEle);
						e.setAttribute("outValue", outValue);
						e.setAttribute("inValue", inValue);
					}
				}
			}	
		}
	}
	
	
	
	/**
	 * 输出信号槽
	 * @param doc
	 * @param rootNode
	 * @param con
	 */
	private static void addPlugins(Document doc, Element rootNode, WidgetConfig widget)
	{
//		List<PluginDesc> list = widget.getPluginDescs();
//		if (list == null) return;
//		Iterator<PluginDesc> it = list.iterator();
//		while(it.hasNext()){
//			PluginDesc plugin = it.next();
//			//TODO
//		}
		
	}
	
	/**
	 * 输出信号
	 * @param doc
	 * @param rootNode
	 * @param widget
	 */
	private static void addPlugouts(Document doc, Element rootNode, WidgetConfig widget)
	{
//		List<PlugoutDesc> list = widget.getPlugoutDescs();
//		if (list == null) return;
//		Iterator<PlugoutDesc> it = list.iterator();
//		while(it.hasNext()){
//			PlugoutDesc plugin = it.next();
//			//TODO
//		}
	}
	
	/**
	 * WidgetConfig扩展属性
	 * @param doc
	 * @param root
	 * @param config
	 */
	private static void addWidgetExtendAttributes(Document doc, Element root, WidgetConfig config){
		Map<String, ExtAttribute> extAttrs = config.getExtendMap();
		if(extAttrs != null && !extAttrs.isEmpty()){
			Element attributesNode = doc.createElement("Attributes");
			root.appendChild(attributesNode);
			
			String attrKey = null;
			Iterator<String> attrIt = extAttrs.keySet().iterator();
			while(attrIt.hasNext()){
				attrKey = attrIt.next();
				ExtAttribute attr = extAttrs.get(attrKey);
				
				Element attributeNode = doc.createElement("Attribute");
				attributesNode.appendChild(attributeNode);
				
				Element keyNode = doc.createElement("Key");
				keyNode.appendChild(doc.createTextNode(attrKey));
				attributeNode.appendChild(keyNode);
				
				Element valueNode = doc.createElement("Value");
				if(attr.getValue() != null)
					valueNode.appendChild(doc.createTextNode(attr.getValue().toString()));
				attributeNode.appendChild(valueNode);
				
				Element descNode = doc.createElement("Desc");
				if(attr.getDesc() != null)
					descNode.appendChild(doc.createTextNode(attr.getDesc().toString()));
				attributeNode.appendChild(descNode);
			}
		}
	}
	
}
