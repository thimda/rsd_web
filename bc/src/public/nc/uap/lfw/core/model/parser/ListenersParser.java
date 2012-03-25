package nc.uap.lfw.core.model.parser;

import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Parameter;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.EventHandlerConf;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.FormRule;
import nc.uap.lfw.core.event.conf.GridRule;
import nc.uap.lfw.core.event.conf.ExcelRule;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.TreeRule;
import nc.uap.lfw.core.event.conf.WidgetRule;
import nc.uap.lfw.util.LfwClassUtil;

import org.apache.commons.digester.Digester;

public class ListenersParser {
	public static void parseListeners(Digester digester, String base, Class<? extends WebElement> eleClazz) {
		WebElement ele = LfwClassUtil.newInstance(eleClazz);
		List<Class<? extends JsListenerConf>> list = ele.getAcceptListeners();
		if(list == null)
			return;
		Iterator<Class<? extends JsListenerConf>> it = list.iterator();
		while(it.hasNext()){
			Class<? extends JsListenerConf> c = it.next();
			String path = base + "/" + c.getSimpleName();
			String clazz = c.getName();
			digester.addObjectCreate(path, clazz);
			digester.addSetProperties(path);
			digester.addSetNext(path, "addListener", clazz);
			
			String path1 = path + "/Events/Event";
			clazz = EventHandlerConf.class.getName();
			digester.addObjectCreate(path1, clazz);
			digester.addCallMethod(path1 + "/Action", "setScript", 0);
			digester.addSetProperties(path1);
			digester.addSetNext(path1, "addEventHandler", clazz);
			String paramClassName = Parameter.class.getName();
			digester.addObjectCreate(path1 + "/Params/Param", paramClassName);
			digester.addCallMethod(path1 + "/Params/Param/Name", "setName", 0);
			digester.addCallMethod(path1 + "/Params/Param/Value", "setValue", 0);
			digester.addSetNext(path1 + "/Params/Param", "addParam", paramClassName);
	
			//exntedsParam
			String extendsParamClassName = Parameter.class.getName();
			digester.addObjectCreate(path1 + "/ExtendParams/Param", extendsParamClassName);
			digester.addCallMethod(path1 + "/ExtendParams/Param/Name", "setName", 0);
			digester.addCallMethod(path1 + "/ExtendParams/Param/Desc", "setDesc", 0);
			digester.addSetNext(path1 + "/ExtendParams/Param", "addExtendParam", extendsParamClassName);
			
			// 解析提交规则
			String path3 = path + "/Events/Event/SubmitRule";
			clazz = EventSubmitRule.class.getName();
			digester.addObjectCreate(path3, clazz);
			digester.addSetProperties(path3);
			digester.addSetNext(path3, "setSubmitRule", clazz);
			parseContent(digester, path3);
			String path4 = path + "/Events/Event/SubmitRule/SubmitRule";
			digester.addObjectCreate(path4, clazz);
			digester.addSetProperties(path4);
			digester.addSetNext(path4, "setParentSubmitRule", clazz);
			parseContent(digester, path4);
			
//			String path2 = path + "/Events/RefEvent";
//			clazz = RefEventHandlerConf.class.getName();
//			digester.addObjectCreate(path2, clazz);
//			digester.addSetProperties(path2);
//			digester.addSetNext(path2, "addEventHandler", clazz);
//			String cmdParamClassName = CmdParameter.class.getName();
//			digester.addObjectCreate(path2 + "/CmdParams/CmdParam", cmdParamClassName);
//			digester.addCallMethod(path2 + "/CmdParams/CmdParam/Name", "setName", 0);
//			digester.addCallMethod(path2 + "/CmdParams/CmdParam/Value", "setValue", 0);
//			digester.addSetNext(path2 + "/CmdParams/CmdParam", "addCmdParam", cmdParamClassName);
			
		}
	}
	
	private static void parseContent(Digester digester, String submitRulePath)
	{
		String paramClassName = Parameter.class.getName();
		digester.addObjectCreate(submitRulePath + "/Params/Param", paramClassName);
		digester.addCallMethod(submitRulePath + "/Params/Param/Name", "setName", 0);
		digester.addCallMethod(submitRulePath + "/Params/Param/Value", "setValue", 0);
		digester.addSetNext(submitRulePath + "/Params/Param", "addParam", paramClassName);
		
		String path4 = submitRulePath + "/Widget";
		String widgetEventClazz = WidgetRule.class.getName();
		digester.addObjectCreate(path4, widgetEventClazz);
		digester.addSetProperties(path4);
		digester.addSetNext(path4, "addWidgetRule", widgetEventClazz);
		
		String path5 = submitRulePath + "/Widget/Dataset";
		String datasetEventClazz = DatasetRule.class.getName();
		digester.addObjectCreate(path5, datasetEventClazz);
		digester.addSetProperties(path5);
		digester.addSetNext(path5, "addDsRule", datasetEventClazz);
		
		String path6 = submitRulePath + "/Widget/Tree";
		String treeClazz = TreeRule.class.getName();
		digester.addObjectCreate(path6, treeClazz);
		digester.addSetProperties(path6);
		digester.addSetNext(path6, "addTreeRule", treeClazz);
		
		String path7 = submitRulePath + "/Widget/Grid";
		String gridClazz = GridRule.class.getName();
		digester.addObjectCreate(path7, gridClazz);
		digester.addSetProperties(path7);
		digester.addSetNext(path7, "addGridRule", gridClazz);
		
		String path8 = submitRulePath + "/Widget/Form";
		String formClazz = FormRule.class.getName();
		digester.addObjectCreate(path8, formClazz);
		digester.addSetProperties(path8);
		digester.addSetNext(path8, "addFormRule", formClazz);
		
		String path9 = submitRulePath + "/Widget/Excel";
		String excelClazz = ExcelRule.class.getName();
		digester.addObjectCreate(path9, excelClazz);
		digester.addSetProperties(path9);
		digester.addSetNext(path9, "addExcelRule", excelClazz);
		
		
	}
}

