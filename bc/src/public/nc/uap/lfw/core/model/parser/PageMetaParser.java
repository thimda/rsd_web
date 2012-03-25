package nc.uap.lfw.core.model.parser;

import java.io.IOException;
import java.io.InputStream;

import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.ViewMenus;
import nc.uap.lfw.core.uimodel.WidgetConfig;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;


/**
 * 将DatasetHandler提到pageMeta级别，通过pageMeta实现对viewModel,viewComponent,menu以及event等配置
 * 文件路径信息的获取。
 * 
 */
public class PageMetaParser {

	public static PageMeta parse(InputStream input) {
	
		Digester digester = new Digester();
		digester.setValidating(false);
		initRule(digester);
		
		try {
			PageMeta conf = (PageMeta)digester.parse(input);
			return conf;
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		} catch (SAXException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		}
		finally{
			try {
				if(input != null)
					input.close();
			} catch (IOException e) {
				LfwLogger.error(e.getMessage(), e);
			}
		}
	}
	private static void initRule(Digester digester) {
		digester.addObjectCreate("PageMeta", PageMeta.class.getName());
		digester.addSetProperties("PageMeta");
		AttributesParser.parseAttributes(digester, "PageMeta", PageMeta.class);
		digester.addCallMethod("PageMeta/Processor", "setProcessorClazz", 0);
		
//		String pageStatesClazz = PageStates.class.getName();
//		String pageStateClazz = PageState.class.getName();
//		digester.addObjectCreate("PageMeta/PageStates", pageStatesClazz);
//		digester.addSetProperties("PageMeta/PageStates");
//		digester.addSetNext("PageMeta/PageStates", "setPageStates", pageStatesClazz);
//		
//		digester.addObjectCreate("PageMeta/PageStates/PageState", pageStateClazz);
//		digester.addCallMethod("PageMeta/PageStates/PageState/Key", "setKey", 0);
//		digester.addCallMethod("PageMeta/PageStates/PageState/Name", "setName", 0);
//		digester.addSetNext("PageMeta/PageStates/PageState", "addPageState", pageStateClazz);
		
		String connectorClazz = Connector.class.getName();
		digester.addObjectCreate("PageMeta/Connectors/Connector", connectorClazz);
		digester.addSetProperties("PageMeta/Connectors/Connector");
		digester.addSetNext("PageMeta/Connectors/Connector", "addConnector");		
		digester.addCallMethod("PageMeta/Connectors/Connector/Maps/Map", "putMapping", 2);
		digester.addCallParam("PageMeta/Connectors/Connector/Maps/Map/outValue", 0);
		digester.addCallParam("PageMeta/Connectors/Connector/Maps/Map/inValue", 1);
		
	
//		String containerClazz = ViewContainers.class.getName();
//		digester.addObjectCreate("PageMeta/Containers", containerClazz);
//		digester.addSetProperties("PageMeta/Containers");
//		digester.addSetNext("PageMeta/Containers", "setViewConinters", containerClazz);
//		initContainerRules(digester);
		
		String widgetClazz = WidgetConfig.class.getName();
		digester.addObjectCreate("PageMeta/Widgets/Widget", widgetClazz);
		digester.addSetProperties("PageMeta/Widgets/Widget");
		digester.addSetNext("PageMeta/Widgets/Widget", "addWidgetConf");
		
		AttributesParser.parseAttributes(digester, "PageMeta/Widgets/Widget", WidgetConfig.class);
		
		String menuClazz = ViewMenus.class.getName();
		digester.addObjectCreate("PageMeta/Menus", menuClazz);
		digester.addSetProperties("PageMeta/Menus");
		digester.addSetNext("PageMeta/Menus", "setViewMenus", menuClazz);
		
		initMenuBarRules(digester);
		ListenersParser.parseListeners(digester, "PageMeta/Listeners", PageMeta.class);
		EventConfParser.parseEvents(digester, "PageMeta", PageMeta.class);
	}
	
//	private static void initContainerRules(Digester digester){
//		initTabRules(digester);
//		initCardLayoutRules(digester);
//		initPanelLayoutRules(digester);
//		initOutLookbarConRules(digester);
//	}
	
//	private static void initTabRules(Digester digester) {
//		String tabClassName = TabLayout.class.getName();
//		//解析TabComp
//		digester.addObjectCreate("PageMeta/Containers/TabComp", tabClassName);
//		digester.addSetProperties("PageMeta/Containers/TabComp");
//		digester.addSetNext("PageMeta/Containers/TabComp", "addContainer", tabClassName);
//
//		String tabItemClassName = TabItem.class.getName();
//		//解析TabComp组件
//		digester.addObjectCreate("PageMeta/Containers/TabComp/TabItem", tabItemClassName);
//		digester.addSetProperties("PageMeta/Containers/TabComp/TabItem");
//		digester.addSetNext("PageMeta/Containers/TabComp/TabItem", "addTabItem",
//				tabItemClassName);
//		ListenersParser.parseListeners(digester, "PageMeta/Containers/TabComp/Listeners", TabLayout.class);
//	}
	
//	private static void initOutLookbarConRules(Digester digester){
//		String outlookbarClassName = OutlookbarComp.class.getName();
//		//解析TabComp
//		digester.addObjectCreate("PageMeta/Containers/OutlookbarComp", outlookbarClassName);
//		digester.addSetProperties("PageMeta/Containers/OutlookbarComp");
//		digester.addSetNext("PageMeta/Containers/OutlookbarComp", "addContainer", outlookbarClassName);
//		
//		String outlookItemClassName = OutlookItem.class.getName();
//		//GridComp元素解析
//		digester.addObjectCreate("PageMeta/Containers/OutlookbarComp/OutlookItem",
//				outlookItemClassName);
//		digester.addSetProperties("PageMeta/Containers/OutlookbarComp/OutlookItem");
//		digester.addSetNext("PageMeta/Containers/OutlookbarComp/OutlookItem", "addOutlookItem",
//				outlookItemClassName);
//		
// 		ListenersParser.parseListeners(digester, "PageMeta/Containers/OutlookbarComp/Listeners", OutlookbarComp.class);
//	}
//	
//	private static void initCardLayoutRules(Digester digester) {
//		String cardClassName = CardLayout.class.getName();
//		//解析cardLayout
//		digester.addObjectCreate("PageMeta/Containers/CardLayout", cardClassName);
//		digester.addSetProperties("PageMeta/Containers/CardLayout");
//		digester.addSetNext("PageMeta/Containers/CardLayout", "addContainer", cardClassName);
// 		ListenersParser.parseListeners(digester, "PageMeta/Containers/CardLayout/Listeners", CardLayout.class);
//	}
//	
//	private static void initPanelLayoutRules(Digester digester) {
//		String panelClassName = PanelLayout.class.getName();
//		//解析cardLayout
//		digester.addObjectCreate("PageMeta/Containers/PanelLayout", panelClassName);
//		digester.addSetProperties("PageMeta/Containers/PanelLayout");
//		digester.addSetNext("PageMeta/Containers/PanelLayout", "addContainer", panelClassName);
// 		ListenersParser.parseListeners(digester, "PageMeta/Containers/PanelLayout/Listeners", PanelLayout.class);
//	}
//	
	
	private static void initMenuBarRules(Digester digester) {
		String menuBarClassName = MenubarComp.class.getName();
		//解析MenuBarComp
		digester.addObjectCreate("PageMeta/Menus/MenuBarComp", menuBarClassName);
		digester.addSetProperties("PageMeta/Menus/MenuBarComp");
		
		ListenersParser.parseListeners(digester, "PageMeta/Menus/MenuBarComp/Listeners", MenubarComp.class);
		
		String menuItemClassName = MenuItem.class.getName();
		
	
		String basePath = "PageMeta/Menus/MenuBarComp";
		for (int i = 0; i < 4; i++) {
			basePath += "/MenuItem";
			digester.addObjectCreate(basePath, menuItemClassName);
			digester.addSetProperties(basePath);
			ListenersParser.parseListeners(digester, basePath + "/Listeners", MenuItem.class);
			digester.addSetNext(basePath, "addMenuItem", menuItemClassName);
		}
		digester.addSetNext("PageMeta/Menus/MenuBarComp", "addMenuBar", menuBarClassName);
	}
}
