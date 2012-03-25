package nc.uap.lfw.core.model.parser;


import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.plugin.IControlPlugin;
import nc.uap.lfw.core.ctrlfrm.seria.IControlSerializer;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PluginDescItem;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.page.PlugoutDescItem;
import nc.uap.lfw.core.page.PlugoutEmitItem;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.page.ViewMenus;
import nc.uap.lfw.core.page.ViewModels;

import org.apache.commons.digester.Digester;

/**
 * 配置文件解析规则
 *
 */
public class ParserRules {
	
	/**
	 * widget.wd 解析规则
	 * @param digester
	 */
	public static void initWidgetRules(Digester digester){
		digester.addObjectCreate("Widget", LfwWidget.class.getName());
		digester.addSetProperties("Widget");
		AttributesParser.parseAttributes(digester, "Widget", LfwWidget.class);
	
		String modelClazz = ViewModels.class.getName();
		digester.addObjectCreate("Widget/Models", modelClazz);
		digester.addSetProperties("Widget/Models");
		digester.addSetNext("Widget/Models", "setViewModels", modelClazz);
		
		String componentClazz = ViewComponents.class.getName();
		digester.addObjectCreate("Widget/Components", componentClazz);
		digester.addSetProperties("Widget/Components");
		digester.addSetNext("Widget/Components", "setViewComponents", componentClazz);
		
//		String containerClazz = ViewContainers.class.getName();
//		digester.addObjectCreate("Widget/Containers", containerClazz);
//		digester.addSetProperties("Widget/Containers");
//		digester.addSetNext("Widget/Containers", "setViewConinters", containerClazz);
		
		String menuClazz = ViewMenus.class.getName();
		digester.addObjectCreate("Widget/Menus", menuClazz);
		digester.addSetProperties("Widget/Menus");
		digester.addSetNext("Widget/Menus", "setViewMenus", menuClazz);
		
		
		ListenersParser.parseListeners(digester, "Widget/Listeners", LfwWidget.class);
		
		EventConfParser.parseEvents(digester, "Widget", LfwWidget.class);
		
		ControlFramework cfrm = ControlFramework.getInstance();
		IControlPlugin[] ctrlPlugins = cfrm.getViewZonePlugins();
		for (int i = 0; i < ctrlPlugins.length; i++) {
			IControlPlugin ctrlPlugin = ctrlPlugins[i];
			IControlSerializer<?> s = ctrlPlugin.getControlSerializer();
			if(s != null){
				s.serialize(digester);
			}
		}
		initPlugRules(digester);
	}
	
	private static void initPlugRules(Digester digester) {
		//plugin
		digester.addObjectCreate("Widget/PluginDescs/PluginDesc", PluginDesc.class.getName());
		digester.addSetProperties("Widget/PluginDescs/PluginDesc");
		digester.addSetNext("Widget/PluginDescs/PluginDesc", "addPluginDescs", PluginDesc.class.getName());
		
		digester.addObjectCreate("Widget/PluginDescs/PluginDesc/PluginDescItem", PluginDescItem.class.getName());
		digester.addSetProperties("Widget/PluginDescs/PluginDesc/PluginDescItem");
		digester.addSetNext("Widget/PluginDescs/PluginDesc/PluginDescItem", "addDescItem", PluginDescItem.class.getName());
		
		// 解析提交规则
//		digester.addObjectCreate("Widget/PluginDescs/PluginDesc/SubmitRule", EventSubmitRule.class.getName());
//		digester.addSetProperties("Widget/PluginDescs/PluginDesc/SubmitRule");
//		digester.addSetNext("Widget/PluginDescs/PluginDesc/SubmitRule", "setSubmitRule", EventSubmitRule.class.getName());
//
//		String paramClassName = Parameter.class.getName();
//		digester.addObjectCreate("Widget/PluginDescs/PluginDesc/SubmitRule/Params/Param", paramClassName);
//		digester.addCallMethod("Widget/PluginDescs/PluginDesc/SubmitRule/Params/Param/Name", "setName", 0);
//		digester.addCallMethod("Widget/PluginDescs/PluginDesc/SubmitRule/Params/Param/Value", "setValue", 0);
//		digester.addSetNext("Widget/PluginDescs/PluginDesc/SubmitRule/Params/Param", "addParam", paramClassName);
//		
//		String widgetEventClazz = WidgetRule.class.getName();
//		digester.addObjectCreate("Widget/PluginDescs/PluginDesc/SubmitRule/Widget", widgetEventClazz);
//		digester.addSetProperties("Widget/PluginDescs/PluginDesc/SubmitRule/Widget");
//		digester.addSetNext("Widget/PluginDescs/PluginDesc/SubmitRule/Widget", "addWidgetRule", widgetEventClazz);
//		
//		String datasetEventClazz = DatasetRule.class.getName();
//		digester.addObjectCreate("Widget/PluginDescs/PluginDesc/SubmitRule/Widget/Dataset", datasetEventClazz);
//		digester.addSetProperties("Widget/PluginDescs/PluginDesc/SubmitRule/Widget/Dataset");
//		digester.addSetNext("Widget/PluginDescs/PluginDesc/SubmitRule/Widget/Dataset", "addDsRule", datasetEventClazz);
//		
//		String treeClazz = TreeRule.class.getName();
//		digester.addObjectCreate("Widget/PluginDescs/PluginDesc/SubmitRule/Widget/Tree", treeClazz);
//		digester.addSetProperties("Widget/PluginDescs/PluginDesc/SubmitRule/Widget/Tree");
//		digester.addSetNext("Widget/PluginDescs/PluginDesc/SubmitRule/Widget/Tree", "addTreeRule", treeClazz);
//		
//		String gridClazz = GridRule.class.getName();
//		digester.addObjectCreate("Widget/PluginDescs/PluginDesc/SubmitRule/Widget/Grid", gridClazz);
//		digester.addSetProperties("Widget/PluginDescs/PluginDesc/SubmitRule/Widget/Grid");
//		digester.addSetNext("Widget/PluginDescs/PluginDesc/SubmitRule/Widget/Grid", "addGridRule", gridClazz);
//		
//		String formClazz = FormRule.class.getName();
//		digester.addObjectCreate("Widget/PluginDescs/PluginDesc/SubmitRule/Widget/Form", formClazz);
//		digester.addSetProperties("Widget/PluginDescs/PluginDesc/SubmitRule/Widget/Form");
//		digester.addSetNext("Widget/PluginDescs/PluginDesc/SubmitRule/Widget/Form", "addFormRule", formClazz);
//		
//		String excelClazz = ExcelRule.class.getName();
//		digester.addObjectCreate("Widget/PluginDescs/PluginDesc/SubmitRule/Widget/Excel", excelClazz);
//		digester.addSetProperties("Widget/PluginDescs/PluginDesc/SubmitRule/Widget/Excel");
//		digester.addSetNext("Widget/PluginDescs/PluginDesc/SubmitRule/Widget/Excel", "addExcelRule", excelClazz);
		
		
		//plugout
		digester.addObjectCreate("Widget/PlugoutDescs/PlugoutDesc", PlugoutDesc.class.getName());
		digester.addSetProperties("Widget/PlugoutDescs/PlugoutDesc");
		digester.addSetNext("Widget/PlugoutDescs/PlugoutDesc", "addPlugoutDescs", PlugoutDesc.class.getName());
		
		digester.addObjectCreate("Widget/PlugoutDescs/PlugoutDesc/PlugoutDescItem", PlugoutDescItem.class.getName());
		digester.addSetProperties("Widget/PlugoutDescs/PlugoutDesc/PlugoutDescItem");
		digester.addSetNext("Widget/PlugoutDescs/PlugoutDesc/PlugoutDescItem", "addDescItem", PlugoutDescItem.class.getName());
		
		digester.addObjectCreate("Widget/PlugoutDescs/PlugoutDesc/PlugoutEmitItem", PlugoutEmitItem.class.getName());
		digester.addSetProperties("Widget/PlugoutDescs/PlugoutDesc/PlugoutEmitItem");
		digester.addSetNext("Widget/PlugoutDescs/PlugoutDesc/PlugoutEmitItem", "addEmitItem", PlugoutEmitItem.class.getName());
	}


}
