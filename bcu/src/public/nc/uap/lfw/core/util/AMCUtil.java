/**
 * 
 */
package nc.uap.lfw.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.RuntimeEnv;
import nc.uap.lfw.ca.jdt.core.dom.AST;
import nc.uap.lfw.ca.jdt.core.dom.ASTNode;
import nc.uap.lfw.ca.jdt.core.dom.ASTParser1;
import nc.uap.lfw.ca.jdt.core.dom.AbstractTypeDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.Block;
import nc.uap.lfw.ca.jdt.core.dom.BodyDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.CompilationUnit;
import nc.uap.lfw.ca.jdt.core.dom.IExtendedModifier;
import nc.uap.lfw.ca.jdt.core.dom.ImportDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.MethodDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.PrimitiveType;
import nc.uap.lfw.ca.jdt.core.dom.SingleVariableDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.Type;
import nc.uap.lfw.ca.jdt.core.dom.TypeDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.Modifier.ModifierKeyword;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.LfwParameter;
import nc.uap.lfw.core.data.Parameter;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.ExcelRule;
import nc.uap.lfw.core.event.conf.FormRule;
import nc.uap.lfw.core.event.conf.GridRule;
import nc.uap.lfw.core.event.conf.TreeRule;
import nc.uap.lfw.core.event.conf.WidgetRule;
import nc.uap.lfw.core.exception.LfwParseException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.parser.ApplicationParser;
import nc.uap.lfw.core.model.parser.PageMetaParser;
import nc.uap.lfw.core.model.parser.WidgetParser;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.page.ViewMenus;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.core.uimodel.AMCServiceObj;
import nc.uap.lfw.core.uimodel.AMCWebElement;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.core.uimodel.conf.Model;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIMeta;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author chouhl
 *
 */
public class AMCUtil {
	
	@SuppressWarnings("unchecked")
	public static Map<String, String>[] getAMCNames(String[] projPaths, String path, String prefix, String tagName) {
		Map<String, String>[] maps = new HashMap[projPaths.length];
		for (int i = 0; i < projPaths.length; i++) {
			String basePath = projPaths[i] + path;
			File dir = new File(basePath);
			File[] fs = dir.listFiles();
			if(fs != null){
				maps[i] = new HashMap<String, String>();
				for (int j = 0; j < fs.length; j++) {
					File file = fs[j];
					if(file.isDirectory()){
						scanDir(prefix, file, maps[i], tagName);
					}
				}
			}
		}
		return maps;
	}
	
	private static void scanDir(String prefix, File dir, Map<String, String> nodeIdDirMap, String tagName) {
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			if(fs[i].isFile() && fs[i].getName().endsWith(prefix)){
				try{
					Digester d = new Digester();
					d.addObjectCreate(tagName, AMCWebElement.class);
					d.addSetProperties(tagName);
					AMCWebElement app = (AMCWebElement) d.parse(fs[i]);
					String name = app.getCaption();
					nodeIdDirMap.put(dir.getName(), name);
				}catch(Exception e){
					LfwLogger.error(e.getMessage(), e);
				}
			}else if(fs[i].isDirectory()){
				scanDir(prefix, fs[i], nodeIdDirMap, tagName);
			}
		}
	}
	
	public static Map<String, String> getExistComponentIds(String projPath, String path, String prefix, String tagName){
		Map<String, String> map = null;
		String basePath = projPath + path;
		File dir = new File(basePath);
		File[] fs = dir.listFiles();
		if(fs != null){
			map = new HashMap<String, String>();
			for (int j = 0; j < fs.length; j++) {
				File file = fs[j];
				if(file.isDirectory()){
					scanModelDir(prefix, dir.getAbsolutePath(), file, map, tagName);
				}
			}
		}
		return map;
	}
	
	private static void scanModelDir(String prefix, String basePath, File dir, Map<String, String> nodeIdDirMap, String tagName) {
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			if(fs[i].isFile() && fs[i].getName().endsWith(prefix)){
				try{
					Digester d = new Digester();
					d.addObjectCreate(tagName, Model.class);
					d.addSetProperties(tagName);
					Model model = (Model) d.parse(fs[i]);
					nodeIdDirMap.put(model.getRefId(), model.getId());
				}catch(Exception e){
					LfwLogger.error(e.getMessage(), e);
				}
			}else if(fs[i].isDirectory()){
				scanModelDir(prefix, basePath, fs[i], nodeIdDirMap, tagName);
			}
		}
	}
	
	public static Application getApplication(String filePath, String fileName){
		File file = new File(filePath + File.separatorChar + fileName);
		return ApplicationParser.parse(file);
	}
	
	public static List<PageMeta> getAppWindowList(String filePath, String fileName){
		File file = new File(filePath + File.separatorChar + fileName);
		Application appConf = ApplicationParser.parse(file);
		return appConf.getWindowList();
	}
	
	public static PageMeta getWindow(String filePath, String fileName){
		File file = new File(filePath + File.separator + fileName);
		InputStream input = null;
		try{
			input = new FileInputStream(file);
			return PageMetaParser.parse(input);
		}
		catch(Exception e){
			LfwLogger.error(e);
			throw new LfwParseException(e);
		}
		finally{
			try {
				input.close();
			} 
			catch (IOException e) {
				LfwLogger.error(e);
			}
		}
	}
	
	public static LfwWidget getView(String filePath, String fileName){
		File file = new File(filePath + File.separator + fileName);
		
		InputStream input = null;
		try{
			input = new FileInputStream(file);
			return WidgetParser.parse(input);
		}
		catch(Exception e){
			LfwLogger.error(e);
			throw new LfwParseException(e);
		}
		finally{
			try {
				input.close();
			} 
			catch (IOException e) {
				LfwLogger.error(e);
			}
		}
	}
	
	public static Element getElementFromClass(Document doc, Object obj){
		final String webElementTypeName = "nc.uap.lfw.core.comp.WebElement";
		Element rootNode = null;
		if(obj != null){
			Class<?> clazz = obj.getClass();
			try {
				rootNode = doc.createElement(clazz.getField("TagName").get(obj).toString());
			} catch (Exception e1) {
				rootNode = doc.createElement(clazz.getSimpleName());
			} 
			do{
				try {
					getElementFromClass(rootNode, clazz, obj);
				} catch (Exception e) {
					LfwLogger.error(e.getMessage(), e);
				}
				clazz = clazz.getSuperclass();
			}while(clazz != null && !webElementTypeName.equals(clazz.getName()));
		}
		return rootNode;
	}
	
	private static void getElementFromClass(Element ele, Class<?> clazz, Object obj) throws Exception{
		final String stringTypeName = "java.lang.String";
		String value = null;
		Field[] fields = clazz.getDeclaredFields();
		if(fields != null && fields.length > 0){
			for(Field field : fields){
				if(stringTypeName.equals(field.getType().getName())){
					field.setAccessible(true);
					value = (String)field.get(obj);
					if(value != null && value.trim().length() > 0){
						ele.setAttribute(field.getName(), value.trim());
					}
				}
			}
		}
	}
	
	private static final String EVENT_TAG_EVENTS = "Events";
	private static final String EVENT_TAG_EVENT = "Event";
	private static final String EVENT_PROP_NAME = "name";
	private static final String EVENT_PROP_METHOD_NAME = "methodName";
	private static final String EVENT_PROP_CONTROLLER_CLAZZ = "controllerClazz";
	private static final String EVENT_PROP_JS_EVENT_CLAZZ = "jsEventClaszz";
	private static final String EVENT_PROP_ON_SERVER = "onserver";
	private static final String EVENT_PROP_ASYNC = "async";
	
	private static final String EVENT_TAG_SUBMIT_RULE = "SubmitRule";
	private static final String EVENT_TAG_PARAMS = "Params";
	private static final String EVENT_TAG_ACTION = "Action";
	private static final String SUBMIT_RULE_PROP_CARD_SUBMIT = "cardSubmit";
	private static final String SUBMIT_RULE_PROP_TAB_SUBMIT = "tabSubmit";
	private static final String SUBMIT_RULE_PROP_PANEL_SUBMIT = "panelSubmit";
	private static final String SUBMIT_RULE_PROP_PAGEMETA = "pagemeta";
	
	public static void addEvents(Document doc, EventConf[] eventConfs, Element parentNode){
		if(eventConfs != null && eventConfs.length > 0){
			Element eventsNode = doc.createElement(EVENT_TAG_EVENTS);
			parentNode.appendChild(eventsNode);
			for(EventConf eventConf : eventConfs){
				if(eventConf.getEventStatus() == EventConf.DEL_STATUS){
					continue;
				}
				Element eventNode = doc.createElement(EVENT_TAG_EVENT);
				eventsNode.appendChild(eventNode);
				if(eventConf.getName() != null && eventConf.getName().trim().length() > 0){
					eventNode.setAttribute(EVENT_PROP_NAME, eventConf.getName().trim());
				}
				if(eventConf.getMethodName() != null && eventConf.getMethodName().trim().length() > 0){
					eventNode.setAttribute(EVENT_PROP_METHOD_NAME, eventConf.getMethodName().trim());
				}
				if(eventConf.getControllerClazz() != null && eventConf.getControllerClazz().trim().length() > 0){
					eventNode.setAttribute(EVENT_PROP_CONTROLLER_CLAZZ, eventConf.getControllerClazz().trim());
				}
				if(eventConf.getJsEventClaszz() != null && eventConf.getJsEventClaszz().trim().length() > 0){
					eventNode.setAttribute(EVENT_PROP_JS_EVENT_CLAZZ, eventConf.getJsEventClaszz().trim());
				}
				eventNode.setAttribute(EVENT_PROP_ON_SERVER, String.valueOf(eventConf.isOnserver()));
				eventNode.setAttribute(EVENT_PROP_ASYNC, String.valueOf(eventConf.isAsync()));
				
				List<LfwParameter> extendParamList = eventConf.getExtendParamList();
				if(extendParamList != null && extendParamList.size() > 0){
					PersistenceUtil.addExtendsParameters(doc, extendParamList.toArray(new LfwParameter[0]), eventNode);
				}
				
				EventSubmitRule submitRule = eventConf.getSubmitRule();
				if(submitRule != null){
					Element submitRuleNode = doc.createElement(EVENT_TAG_SUBMIT_RULE);
					submitRuleNode.setAttribute(SUBMIT_RULE_PROP_CARD_SUBMIT, String.valueOf(submitRule.isCardSubmit()));
					submitRuleNode.setAttribute(SUBMIT_RULE_PROP_TAB_SUBMIT, String.valueOf(submitRule.isTabSubmit()));
					submitRuleNode.setAttribute(SUBMIT_RULE_PROP_PANEL_SUBMIT, String.valueOf(submitRule.isPanelSubmit()));
					eventNode.appendChild(submitRuleNode);
					// 父提交规则
					EventSubmitRule pSubmitRule = submitRule.getParentSubmitRule();
					if(pSubmitRule != null){
						Element pSubmitRuleNode = doc.createElement(EVENT_TAG_SUBMIT_RULE);
						pSubmitRuleNode.setAttribute(SUBMIT_RULE_PROP_PAGEMETA, pSubmitRule.getPagemeta());
						pSubmitRuleNode.setAttribute(SUBMIT_RULE_PROP_CARD_SUBMIT, String.valueOf(pSubmitRule.isCardSubmit()));
						pSubmitRuleNode.setAttribute(SUBMIT_RULE_PROP_TAB_SUBMIT, String.valueOf(pSubmitRule.isTabSubmit()));
						pSubmitRuleNode.setAttribute(SUBMIT_RULE_PROP_PANEL_SUBMIT, String.valueOf(pSubmitRule.isPanelSubmit()));
						submitRuleNode.appendChild(pSubmitRuleNode);
						Map<String, WidgetRule> pWidgetRuleMap = pSubmitRule.getWidgetRules();
						if(!pWidgetRuleMap.isEmpty()){
							PersistenceUtil.addSubmitContent(doc, pSubmitRuleNode, pWidgetRuleMap);
						}
					}
					
					Map<String, WidgetRule> widgetRuleMap = submitRule.getWidgetRules();
					if(!widgetRuleMap.isEmpty()){
						PersistenceUtil.addSubmitContent(doc, submitRuleNode, widgetRuleMap);
					}
					PersistenceUtil.addParameters(doc, submitRule.getParams(), submitRuleNode);
				}
				
				PersistenceUtil.addParameters(doc, eventConf.getParamList().toArray(new LfwParameter[0]), eventNode);
				Element actionNode = doc.createElement(EVENT_TAG_ACTION);
				eventNode.appendChild(actionNode);
				if(eventConf.getScript() != null && eventConf.getScript().trim().length() > 0){
					actionNode.appendChild(doc.createCDATASection(eventConf.getScript().trim()));
				}
			}
		}
	}
	
	/**
	 * Event DOM解析
	 * @param node
	 * @param element
	 */
	public static void parseEvents(Node node, UIElement element){
		if(node == null || element == null){
			return;
		}
		if(EVENT_TAG_EVENTS.equals(node.getNodeName())){
			NodeList nl = node.getChildNodes();
			if(nl != null && nl.getLength() > 0){
				List<EventConf> list = new ArrayList<EventConf>();
				for(int i=0; i<nl.getLength(); i++){
					if(EVENT_TAG_EVENT.equals(nl.item(i).getNodeName())){
						EventConf event = new EventConf();
						event.setAsync(Boolean.valueOf(getAttributeValue(nl.item(i), EVENT_PROP_ASYNC)));
						event.setOnserver(Boolean.valueOf(getAttributeValue(nl.item(i), EVENT_PROP_ON_SERVER)));
						event.setName(getAttributeValue(nl.item(i), EVENT_PROP_NAME));
						event.setMethodName(getAttributeValue(nl.item(i), EVENT_PROP_METHOD_NAME));
						event.setControllerClazz(getAttributeValue(nl.item(i), EVENT_PROP_CONTROLLER_CLAZZ));
						event.setJsEventClaszz(getAttributeValue(nl.item(i), EVENT_PROP_JS_EVENT_CLAZZ));
						NodeList nl1 = nl.item(i).getChildNodes();
						if(nl1 != null && nl1.getLength() > 0){
							for(int j=0; j<nl1.getLength(); j++){
								if(EVENT_TAG_SUBMIT_RULE.equals(nl1.item(j).getNodeName())){
									event.setSubmitRule(parseSubmitRule(nl1.item(j)));
								}else if(EVENT_TAG_PARAMS.equals(nl1.item(j).getNodeName())){
									NodeList params = nl1.item(j).getChildNodes();
									if(params != null && params.getLength() > 0){
										Map<String, Parameter> map = parseParameter(params);
										Iterator<String> keys = map.keySet().iterator();
										while(keys.hasNext()){
											event.addParam(map.get(keys.next()));
										}
									}
								}else if(EVENT_TAG_ACTION.equals(nl1.item(j).getNodeName())){
									event.setScript(nl1.item(j).getTextContent().trim());
								}
							}
						}
						list.add(event);
					}
				}
				element.removeAllEventConf();
				for(EventConf event : list){
					element.addEventConf(event);
				}
			}
		}
	}
	
	private static EventSubmitRule parseSubmitRule(Node node){
		EventSubmitRule rule = null;
		if(node != null && EVENT_TAG_SUBMIT_RULE.equals(node.getNodeName())){
			rule = new EventSubmitRule();
			rule.setPagemeta(getAttributeValue(node, SUBMIT_RULE_PROP_PAGEMETA));
			rule.setCardSubmit(Boolean.valueOf((getAttributeValue(node, SUBMIT_RULE_PROP_CARD_SUBMIT))));
			rule.setTabSubmit(Boolean.valueOf((getAttributeValue(node, SUBMIT_RULE_PROP_TAB_SUBMIT))));
			rule.setPanelSubmit(Boolean.valueOf((getAttributeValue(node, SUBMIT_RULE_PROP_PANEL_SUBMIT))));
			NodeList nl = node.getChildNodes();
			if(nl != null && nl.getLength() > 0){
				rule.setWidgetRules(parseWidgetRule(nl));
				rule.setParams(parseParameter(nl));
				for(int i=0; i<nl.getLength(); i++){
					if(EVENT_TAG_SUBMIT_RULE.equals(nl.item(i).getNodeName())){
						rule.setParentSubmitRule(parseSubmitRule(nl.item(i)));
					}
				}
			}
		}
		return rule;
	}
	
	private static final String SUBMIT_RULE_TAG_WIDGET = "Widget";
	private static final String WIDGET_RULE_PROP_ID = "id";
	private static final String WIDGET_RULE_PROP_CARD_SUBMIT = "cardSubmit";
	private static final String WIDGET_RULE_PROP_TAB_SUBMIT = "tabSubmit";
	private static final String WIDGET_RULE_PROP_PANEL_SUBMIT = "panelSubmit";
	
	private static Map<String, WidgetRule> parseWidgetRule(NodeList nl){
		Map<String, WidgetRule> widgetRule = new HashMap<String, WidgetRule>();
		for(int i=0; i<nl.getLength(); i++){
			if(SUBMIT_RULE_TAG_WIDGET.equals(nl.item(i))){
				WidgetRule wr = new WidgetRule();
				wr.setId(getAttributeValue(nl.item(i), WIDGET_RULE_PROP_ID));
				wr.setCardSubmit(Boolean.valueOf((getAttributeValue(nl.item(i), WIDGET_RULE_PROP_CARD_SUBMIT))));
				wr.setTabSubmit(Boolean.valueOf((getAttributeValue(nl.item(i), WIDGET_RULE_PROP_TAB_SUBMIT))));
				wr.setPanelSubmit(Boolean.valueOf((getAttributeValue(nl.item(i), WIDGET_RULE_PROP_PANEL_SUBMIT))));
				NodeList children = nl.item(i).getChildNodes();
				if(children != null && children.getLength() > 0){
					Map<String, Object> rules = parseWidgetChildRule(children);
					Iterator<String> keys = rules.keySet().iterator();
					String key = null;
					while(keys.hasNext()){
						key = keys.next();
						if(key.startsWith(WIDGET_RULE_TAG_DATASET)){
							wr.addDsRule((DatasetRule)rules.get(key));
						}else if(key.startsWith(WIDGET_RULE_TAG_TREE)){
							wr.addTreeRule((TreeRule)rules.get(key));
						}else if(key.startsWith(WIDGET_RULE_TAG_GRID)){
							wr.addGridRule((GridRule)rules.get(key));
						}else if(key.startsWith(WIDGET_RULE_TAG_FORM)){
							wr.addFormRule((FormRule)rules.get(key));
						}else if(key.startsWith(WIDGET_RULE_TAG_EXCEL)){
							wr.addExcelRule((ExcelRule)rules.get(key));
						}
					}
				}
			}
		}
		return widgetRule;
	}
	
	private static final String WIDGET_RULE_TAG_DATASET = "Dataset";
	private static final String WIDGET_RULE_TAG_TREE = "Tree";
	private static final String WIDGET_RULE_TAG_GRID = "Grid";
	private static final String WIDGET_RULE_TAG_FORM = "Form";
	private static final String WIDGET_RULE_TAG_EXCEL = "Excel";
	private static final String OBJECT_RULE_PROP_ID = "id";
	private static final String OBJECT_RULE_PROP_TYPE = "type";
	
	private static Map<String, Object> parseWidgetChildRule(NodeList nl){
		Map<String, Object> map = new HashMap<String, Object>();
		for(int i=0; i<nl.getLength(); i++){
			if(WIDGET_RULE_TAG_DATASET.equals(nl.item(i).getNodeName())){
				DatasetRule dr = new DatasetRule();
				dr.setId(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_ID));
				dr.setType(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_TYPE));
				map.put(WIDGET_RULE_TAG_DATASET + "_" + dr.getId(), dr);
			}else if(WIDGET_RULE_TAG_TREE.equals(nl.item(i).getNodeName())){
				TreeRule tr = new TreeRule();
				tr.setId(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_ID));
				tr.setType(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_TYPE));
				map.put(WIDGET_RULE_TAG_TREE + "_" + tr.getId(), tr);
			}else if(WIDGET_RULE_TAG_GRID.equals(nl.item(i).getNodeName())){
				GridRule gr = new GridRule();
				gr.setId(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_ID));
				gr.setType(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_TYPE));
				map.put(WIDGET_RULE_TAG_GRID + "_" + gr.getId(), gr);
			}else if(WIDGET_RULE_TAG_FORM.equals(nl.item(i).getNodeName())){
				FormRule fr = new FormRule();
				fr.setId(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_ID));
				fr.setType(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_TYPE));
				map.put(WIDGET_RULE_TAG_FORM + "_" + fr.getId(), fr);
			}else if(WIDGET_RULE_TAG_EXCEL.equals(nl.item(i).getNodeName())){
				ExcelRule er = new ExcelRule();
				er.setId(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_ID));
				er.setType(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_TYPE));
				map.put(WIDGET_RULE_TAG_EXCEL + "_" + er.getId(), er);
			}
		}
		return map;
	}
	
	private static final String PARAMS_TAG_PARAM = "Param";
	private static final String PARAM_TAG_NAME = "Name";
	private static final String PARAM_TAG_VALUE = "Value";
	private static final String PARAM_TAG_DESC = "Desc";
	
	private static Map<String, Parameter> parseParameter(NodeList nl){
		Map<String, Parameter> map = new HashMap<String, Parameter>();
		for(int i=0; i<nl.getLength(); i++){
			if(PARAMS_TAG_PARAM.equals(nl.item(i).getNodeName())){
				NodeList param = nl.item(i).getChildNodes();
				if(param != null && param.getLength() > 0){
					Parameter lfwParam = new Parameter();
					for(int k=0; k<param.getLength(); k++){
						if(PARAM_TAG_NAME.equals(param.item(k).getNodeName())){
							lfwParam.setName(param.item(k).getTextContent());
						}else if(PARAM_TAG_VALUE.equals(param.item(k).getNodeName())){
							lfwParam.setValue(param.item(k).getTextContent());
						}else if(PARAM_TAG_DESC.equals(param.item(k).getNodeName())){
							lfwParam.setDesc(param.item(k).getTextContent().trim());
						}
					}
					map.put(lfwParam.getName() + "_" + i, lfwParam);
				}
			}
		}
		return map;
	}
	
	private static String getAttributeValue(Node node, String key) {
		Node attr = node.getAttributes().getNamedItem(key);
		if (attr == null)
			return null;
		return attr.getNodeValue();
	}
	
	/**
	 * 类模板文件路径
	 */
	private static final String TemplatePath = RuntimeEnv.getInstance().getNCHome() + "/resources/lfw/ctrl/";
	/**
	 * 类模板文件名称
	 */
	private static final String CLASS_TEMPLATE_NAME_ControllerTemplate = "ControllerTemplate.java";
	/**
	 * 类模板文件名称
	 */
	private static final String CLASS_TEMPLATE_NAME_SysWindowController = "SysWindowController.java";
	/**
	 * 类模板文件名称
	 */
	private final static String CLASS_TEMPLATE_NAME_MethodTemplate = "MethodTemplate.java";
	
	/**
	 * 获取模板类中的模版方法
	 * @return
	 */
	public static Map<String, MethodDeclaration> getMethodTemplate(){
		Map<String, MethodDeclaration> mt = new HashMap<String, MethodDeclaration>();
		CompilationUnit unit = getASTUnit(TemplatePath + CLASS_TEMPLATE_NAME_MethodTemplate);
		TypeDeclaration td = getFirstPublicClass(unit, TemplatePath + CLASS_TEMPLATE_NAME_MethodTemplate);
		MethodDeclaration[] mds = td.getMethods();
		if(mds != null && mds.length > 0){
			for(MethodDeclaration md : mds){
				mt.put(md.getName().getFullyQualifiedName(), md);
			}
		}
		return mt;
	}
	
	/**
	 * 获取模板类中的所有导入类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, ImportDeclaration> getImportTemplate(){
		Map<String, ImportDeclaration> imports = new HashMap<String, ImportDeclaration>();
		List<ImportDeclaration> ids = null;
		CompilationUnit unit = getASTUnit(TemplatePath + CLASS_TEMPLATE_NAME_MethodTemplate);
		ids = unit.imports();
		if(ids != null && ids.size() > 0){
			for(ImportDeclaration importDecl : ids){
				imports.put(importDecl.getName().getFullyQualifiedName(), importDecl);
			}
		}
		return imports;
	}
	
	/**
	 * 创建AST编译单元对象
	 * @param javaFilePath
	 * @return
	 */
	private static CompilationUnit getASTUnit(String javaFilePath){
		CompilationUnit unit = null;
		File tempFile = new File(javaFilePath);
		LfwLogger.error("类文件路径：" + tempFile.getAbsolutePath());
		if(tempFile.exists() && tempFile.isFile()){
			try {
				String content = readFileToString(tempFile);
				char[] ch = new char[content.length()];
				content.getChars(0, content.length(), ch, 0);
				//获取模板java类AST对象
				ASTParser1 astParser = ASTParser1.newParser();
				astParser.setSource(ch);
				unit = (CompilationUnit)astParser.createAST(null);
			}catch(Exception e){
				LfwLogger.error(e);
				throw new LfwRuntimeException("创建AST编译对象失败", e);
			}
		}else{
			LfwLogger.error("类文件" + tempFile.getAbsolutePath() + "不存在");
			throw new LfwRuntimeException("类文件" + tempFile.getAbsolutePath() + "不存在");
		}
		return unit;
	}
	
	/**
	 * 获取类文件中第一个public类
	 * @param unit
	 * @return
	 */
	@SuppressWarnings({"unchecked" })
	private static TypeDeclaration getFirstPublicClass(CompilationUnit unit, String classFilePath){
		TypeDeclaration td = null;
		List<AbstractTypeDeclaration> atds = unit.types();
		if(atds != null && atds.size() > 0){
			jump:for(AbstractTypeDeclaration atd : atds){
				if(atd instanceof TypeDeclaration){
					td = (TypeDeclaration)atd;
					List<IExtendedModifier> iems = td.modifiers();
					if(iems != null && iems.size() > 0){
						for(IExtendedModifier iem : iems){
							if(iem.isModifier() && iem.toString().equals(ModifierKeyword.PUBLIC_KEYWORD.toString())){
								break jump;
							}
						}
					}
					td = null;
				}
			}
		}
		if(td == null){
			LfwLogger.error(classFilePath + "中不存在public类");
			throw new LfwRuntimeException(classFilePath + "中不存在public类");
		}
		return td;
	}
	
	/**
	 * 替换方法中的标识为方法真实内容
	 * @param classContent
	 * @param mds
	 */
	private static void replaceMethodBody(StringBuffer classContent, MethodDeclaration[] mds){
		if(mds != null && mds.length > 0){
			for(MethodDeclaration md : mds){
				if(md.getMs() != null){
					int start = classContent.indexOf(md.getMs()) - 1;
					int end = start + md.getMs().length() + 3;
					classContent.replace(start, end, md.getMethodBody().trim());
				}
			}
		}
	}
	
	/**
	 * 创建AST方法对象，方法体为空
	 * @param ast
	 * @param methodName
	 * @param paramList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static MethodDeclaration createNewMethod(AST ast,String methodName, List<LfwParameter> paramList){
		//新建AST方法对象
		MethodDeclaration newMethod = ast.newMethodDeclaration();
		//设置方法访问权限为public
		List<IExtendedModifier> ems = newMethod.modifiers();
		if(ems == null){
			ems = new ArrayList<IExtendedModifier>();
		}
		ems.add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
		//返回值
		Type returnType = ast.newPrimitiveType(PrimitiveType.VOID);
		newMethod.setReturnType2(returnType);
		//methodName
		newMethod.setName(ast.newSimpleName(methodName));
		//params
		if(paramList != null && paramList.size() > 0){
			List<SingleVariableDeclaration> params = newMethod.parameters();
			if(params == null){
				params = new ArrayList<SingleVariableDeclaration>();
			}
			for(LfwParameter param : paramList){
				SingleVariableDeclaration svd = ast.newSingleVariableDeclaration();
				svd.setName(ast.newSimpleName(param.getName()));
				svd.setType(ast.newSimpleType(ast.newName(param.getDesc().substring(param.getDesc().lastIndexOf(".")+1))));
				params.add(svd);
			}
		}
		//body
		Block body = ast.newBlock();
		newMethod.setBody(body);
		return newMethod;
	}
	/**
	 * 获取Controller类
	 * @param operateWebElementXMLType
	 * @param packageName
	 * @param importNames
	 * @param publicClassName
	 * @param parentClassName
	 * @param interfaceNames
	 * @param methodList
	 * @return
	 */
	public static String getControllerClazz(int operateWebElementXMLType, String packageName, String publicClassName){
		String templateClassName = CLASS_TEMPLATE_NAME_ControllerTemplate;
		switch(operateWebElementXMLType){
			case AMCServiceObj.CreateWindowXml:
				templateClassName = CLASS_TEMPLATE_NAME_SysWindowController;
				break;
			default:
				break;
		}
		CompilationUnit unit = getASTUnit(TemplatePath + templateClassName);
		AST ast = unit.getAST();
		//修改包名
		if(packageName == null || packageName.trim().length() == 0){
			unit.getPackage().delete();
		}else{
			unit.getPackage().setName(ast.newName(packageName.trim()));
		}
		/**
		 * 只修改public类
		 */
		TypeDeclaration td = getFirstPublicClass(unit, TemplatePath + templateClassName);
		//修改类名
		if(publicClassName != null && publicClassName.trim().length() > 0){
			td.setName(ast.newSimpleName(publicClassName));
		}
		//修改构造方法名
		MethodDeclaration[] mds = td.getMethods();
		for(MethodDeclaration md : mds){
			if(md.isConstructor()){
				md.setName(ast.newSimpleName(publicClassName));
			}
		}
		StringBuffer classContent = new StringBuffer(unit.toString());
		replaceMethodBody(classContent, mds);
		return classContent.toString();
	}
	
	/**
	 * 操作事件方法
	 * @param amcServiceObj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String operateMethod(String classFilePath, List<EventConf> eventList){
		CompilationUnit unit = getASTUnit(classFilePath);
		AST ast = unit.getAST();
		//获取类文件所有导入类
		List<ImportDeclaration> imports = unit.imports();
		if(imports == null){
			imports = new ArrayList<ImportDeclaration>();
		}
		Map<String, ImportDeclaration> importsMap = new HashMap<String, ImportDeclaration>();
		for(ImportDeclaration id : imports){
			importsMap.put(id.getName().getFullyQualifiedName(), id);
		}
		//获取类文件public类
		TypeDeclaration td = getFirstPublicClass(unit, classFilePath);
		List<BodyDeclaration> bodys = td.bodyDeclarations();
		if(bodys == null){
			bodys = new ArrayList<BodyDeclaration>();
		}
		if(eventList != null && eventList.size() > 0){
			for(EventConf event : eventList){
				switch(event.getEventStatus()){
					case EventConf.ADD_STATUS:
						MethodDeclaration newMethod = createNewMethod(ast, event.getMethodName(), event.getParamList());
						//增加导入类
						List<LfwParameter> paramList = event.getParamList();
						if(paramList != null && paramList.size() > 0){
							for(LfwParameter param : paramList){
								ImportDeclaration id = ast.newImportDeclaration();
								id.setName(ast.newName(param.getDesc()));
								importsMap.put(id.getName().getFullyQualifiedName(), id);
							}
						}
						//重新设置body
						Block body = ast.newBlock();
						Map<String, MethodDeclaration> mt = getMethodTemplate();
						MethodDeclaration md = mt.get(event.getName());
						if(md != null){
							body = (Block)ASTNode.copySubtree(body.getAST(), md.getBody());
							importsMap.putAll(getImportTemplate());
							newMethod.setMethodBody(md.getMethodBody());
							newMethod.setMs(md.getMs());
						}
						newMethod.setBody(body);
						bodys.add(newMethod);
						break;
					case EventConf.DEL_STATUS:
						for(int i=0; i<bodys.size(); i++){
							if(bodys.get(i) instanceof MethodDeclaration){
								if(((MethodDeclaration)bodys.get(i)).getName().getFullyQualifiedName().equals(event.getMethodName())){
									bodys.remove(i);
									break;
								}
							}
						}
						break;
					default:
						break;
				}
			}
			//重新设置类文件导入类
			imports.clear();
			Iterator<String> keys = importsMap.keySet().iterator();
			while(keys.hasNext()){
				ImportDeclaration id = ast.newImportDeclaration();
				id = (ImportDeclaration)ASTNode.copySubtree(id.getAST(), importsMap.get(keys.next()));
				imports.add(id);
			}
		}
		StringBuffer classContent = new StringBuffer(unit.toString());
		replaceMethodBody(classContent, td.getMethods());
		return classContent.toString();
	}
	
	/**
	 * 合并ViewUIMeta事件
	 * @param meta
	 * @param oriUIMeta
	 */
	public static void mergeViewUIMetaEvents(UIMeta meta, UIMeta oriMeta, Map<String, UIElement> map){
		if(meta != null && oriMeta != null){
			EventConf[] events = meta.getEventConfs();
			if(events != null){
				oriMeta.removeAllEventConf();
				for(EventConf event : events){
					oriMeta.addEventConf(event);
				}
			}
			if(map != null){
				UIElement element = null;
				UIElement oriElement = null;
				Iterator<String> keys = map.keySet().iterator();
				while(keys.hasNext()){
					element = UIElementFinder.findElementById(meta, keys.next());
					if(element != null){
						oriElement = UIElementFinder.findElementById(oriMeta, (String)element.getAttribute(UIElement.ID));
						if(oriElement != null){
							events = element.getEventConfs();
							if(events != null){
								oriElement.removeAllEventConf();
								for(EventConf event : events){
									oriElement.addEventConf(event);
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 合并View所有事件(包括View包含的控件)
	 * @param widget
	 * @param oriWidget
	 */
	public static void mergeViewEvents(LfwWidget widget, LfwWidget oriWidget){
		if(widget != null && oriWidget != null){
			
			ViewComponents oriVComp = oriWidget.getViewComponents();
			ViewComponents vcomp = widget.getViewComponents();
			WebComponent[] comps = vcomp.getComponents();
			if(comps != null && comps.length > 0){
				for(WebComponent comp : comps){
					WebComponent oriComp = oriVComp.getComponent(comp.getId());
					if(oriComp != null){
						oriComp.removeAllEventConf();
						oriComp.setEventConfList(comp.getEventConfList());
					}else{
						oriVComp.addComponent(comp);
					}
				}
			}
			
//			ViewContainers oriVCon = oriWidget.getViewConinters();
//			ViewContainers vcon = widget.getViewConinters();
//			WebComponent[] cons = vcon.getContainers();
//			if(cons != null && cons.length > 0){
//				for(WebComponent con : cons){
//					WebComponent oriCon = oriVCon.getContainer(con.getId());
//					if(oriCon != null){
//						oriCon.removeAllEventConf();
//						oriCon.setEventConfList(con.getEventConfList());
//					}else{
//						oriVCon.addContainer(con);
//					}
//				}
//			}
			
			ViewMenus oriVM = oriWidget.getViewMenus();
			ViewMenus vm = widget.getViewMenus();
			
			MenubarComp[] mcs = vm.getMenuBars();
			if(mcs != null && mcs.length > 0){
				for(MenubarComp mc : mcs){
					MenubarComp oriMC = oriVM.getMenuBar(mc.getId());
					if(oriMC != null){
						oriMC.removeAllEventConf();
						oriMC.setEventConfList(mc.getEventConfList());
						mergeChildrenMenuItemEvents(oriMC.getMenuList(), mc.getMenuList());
					}else{
						oriVM.addMenuBar(mc);
					}
				}
			}
			
			ContextMenuComp[] cmcs = vm.getContextMenus();
			if(cmcs != null && cmcs.length > 0){
				for(ContextMenuComp cmc : cmcs){
					ContextMenuComp oriCMC = oriVM.getContextMenu(cmc.getId());
					if(oriCMC != null){
						oriCMC.removeAllEventConf();
						oriCMC.setEventConfList(cmc.getEventConfList());
						mergeChildrenMenuItemEvents(oriCMC.getItemList(), cmc.getItemList());
					}else{
						oriVM.addContextMenu(cmc);
					}
				}
			}
			
			ViewModels oriVms = oriWidget.getViewModels();
			ViewModels vms = widget.getViewModels();
			
			ComboData[] cds = vms.getComboDatas();
			if(cds != null && cds.length > 0){
				for(ComboData cd : cds){
					ComboData oriCD = oriVms.getComboData(cd.getId());
					if(oriCD != null){
						oriCD.removeAllEventConf();
						oriCD.setEventConfList(cd.getEventConfList());
					}else{
						oriVms.addComboData(cd);
					}
				}
			}
			
			Dataset[] dss = vms.getDatasets();
			if(dss != null && dss.length > 0){
				for(Dataset ds : dss){
					Dataset oriDS = oriVms.getDataset(ds.getId());
					if(oriDS != null){
						oriDS.removeAllEventConf();
						oriDS.setEventConfList(ds.getEventConfList());
					}else{
						oriVms.addDataset(ds);
					}
				}
			}
			
			IRefNode[] rns = vms.getRefNodes();
			if(rns != null && rns.length > 0){
				for(IRefNode rn : rns){
					IRefNode oriRN = oriVms.getRefNode(rn.getId());
					if(oriRN != null){
						if(oriRN instanceof RefNode){
							((RefNode)oriRN).removeAllEventConf();
							((RefNode)oriRN).setEventConfList(((RefNode)rn).getEventConfList());
						}
					}
					else{
						oriVms.addRefNode(rn);
					}
				}
			}
		}
	}
	/**
	 * 合并菜单子节点事件
	 * @param oriChildren
	 * @param children
	 */
	private static void mergeChildrenMenuItemEvents(List<MenuItem> oriChildren, List<MenuItem> children){
		Map<String, MenuItem> oriItemMap = null;
		if(oriChildren != null && oriChildren.size() > 0){
			oriItemMap = new HashMap<String, MenuItem>();
			for(MenuItem item : oriChildren){
				oriItemMap.put(item.getId(), item);
			}
			for(MenuItem item : children){
				MenuItem oriItem = oriItemMap.get(item.getId());
				if(oriItem != null){
					oriItem.removeAllEventConf();
					oriItem.setEventConfList(item.getEventConfList());
					if(oriItem.getChildList() != null && oriItem.getChildList().size() > 0){
						mergeChildrenMenuItemEvents(oriItem.getChildList(), item.getChildList());
					}else{
						oriItem.setChildList(item.getChildList());
					}
				}else{
					oriChildren.add(item);
				}
			}
		}
	}
	
	/**
	 * 获取View对象事件集合（包括View中各个控件）
	 * @param viewConf
	 * @return
	 */
	public static EventConf[] getAllEvents(LfwWidget viewConf){
		//事件数组集合
		EventConf[] eventConfs = null;
		//Component
		ViewComponents vc = viewConf.getViewComponents();
		//Model
		ViewModels vm = viewConf.getViewModels();
		//菜单
		ViewMenus vmenu = viewConf.getViewMenus();
		//事件List集合
		List<EventConf> ecList= new ArrayList<EventConf>();
		//View事件集合
		EventConf[] events = viewConf.getEventConfs();
		if(events != null && events.length > 0){
			for(EventConf event : events){
				ecList.add(event);
			}
		}
		//Web元素集合
		WebElement[] wcs = null;
		//控件元素集合
		wcs = vc.getComponents();
		//控件元素事件集合
		if(wcs != null && wcs.length > 0){
			for(WebElement wc : wcs){
				EventConf[] ecs = wc.getEventConfs();
				if(ecs != null && ecs.length > 0){
					for(EventConf ec : ecs){
						ecList.add(ec);
					}
				}
			}
		}
		//下拉数据集 集合
		wcs = vm.getComboDatas();
		//下拉数据集事件集合
		if(wcs != null && wcs.length > 0){
			for(WebElement wc : wcs){
				EventConf[] ecs = wc.getEventConfs();
				if(ecs != null && ecs.length > 0){
					for(EventConf ec : ecs){
						ecList.add(ec);
					}
				}
			}
		}
		//数据集 集合
		wcs = vm.getDatasets();
		//数据集事件集合
		if(wcs != null && wcs.length > 0){
			for(WebElement wc : wcs){
				EventConf[] ecs = wc.getEventConfs();
				if(ecs != null && ecs.length > 0){
					for(EventConf ec : ecs){
						ecList.add(ec);
					}
				}
			}
		}
		//参照集合
		IRefNode[] refnodes = vm.getRefNodes();
		//参照事件集合
		if(refnodes != null && refnodes.length > 0){
			for(IRefNode rf : refnodes){
				WebElement wc = (WebElement) rf;
				EventConf[] ecs = wc.getEventConfs();
				if(ecs != null && ecs.length > 0){
					for(EventConf ec : ecs){
						ecList.add(ec);
					}
				}
			}
		}
		//菜单集合
		MenubarComp[] mcs = vmenu.getMenuBars();
		//菜单项集合
		wcs = null;
		if(mcs != null && mcs.length > 0){
			List<WebElement> weList = new ArrayList<WebElement>();
			for(MenubarComp comp : mcs){
				if(comp.getMenuList() != null){
					List<MenuItem> miList = comp.getMenuList();
					for(MenuItem mi : miList){
						weList.addAll(getMenuItems(mi));
					}
				}
			}
			wcs = weList.toArray(new WebElement[0]);
		}
		//菜单项事件集合
		if(wcs != null && wcs.length > 0){
			for(WebElement wc : wcs){
				EventConf[] ecs = wc.getEventConfs();
				if(ecs != null && ecs.length > 0){
					for(EventConf ec : ecs){
						ecList.add(ec);
					}
				}
			}
		}
		//右键菜单集合
		ContextMenuComp[] cmcs = vmenu.getContextMenus();
		//右键菜单项集合
		wcs = null;
		if(cmcs != null && cmcs.length > 0){
			List<WebElement> weList = new ArrayList<WebElement>();
			for(ContextMenuComp comp : cmcs){
				if(comp.getItemList() != null){
					List<MenuItem> miList = comp.getItemList();
					for(MenuItem mi : miList){
						weList.addAll(getMenuItems(mi));
					}
				}
			}
			wcs = weList.toArray(new WebElement[0]);
		}
		//右键菜单项事件集合
		if(wcs != null && wcs.length > 0){
			for(WebElement wc : wcs){
				EventConf[] ecs = wc.getEventConfs();
				if(ecs != null && ecs.length > 0){
					for(EventConf ec : ecs){
						ecList.add(ec);
					}
				}
			}
		}
		//View包含所有事件集合
		eventConfs = ecList.toArray(new EventConf[0]);
		
		return eventConfs;
	}
	
	/**
	 * 移除View对象事件（包括View中各个控件）
	 * @param viewConf
	 * @return
	 */
	public static void removeEvent(LfwWidget viewConf, String eventName, String methodName){
		//Component
		ViewComponents vc = viewConf.getViewComponents();
		//Model
		ViewModels vm = viewConf.getViewModels();
		//菜单
		ViewMenus vmenu = viewConf.getViewMenus();

		viewConf.removeEventConf(eventName, methodName);
		
		//Web元素集合
		WebElement[] wcs = null;
		//控件元素集合
		wcs = vc.getComponents();
		//控件元素事件集合
		if(wcs != null && wcs.length > 0){
			for(WebElement wc : wcs){
				wc.removeEventConf(eventName, methodName);
			}
		}
		//下拉数据集 集合
		wcs = vm.getComboDatas();
		//下拉数据集事件集合
		if(wcs != null && wcs.length > 0){
			for(WebElement wc : wcs){
				wc.removeEventConf(eventName, methodName);
			}
		}
		//数据集 集合
		wcs = vm.getDatasets();
		//数据集事件集合
		if(wcs != null && wcs.length > 0){
			for(WebElement wc : wcs){
				wc.removeEventConf(eventName, methodName);
			}
		}
		//参照集合
		IRefNode[] refnodes = vm.getRefNodes();
		//参照事件集合
		if(refnodes != null && refnodes.length > 0){
			for(WebElement wc : wcs){
				wc.removeEventConf(eventName, methodName);
			}
		}
		//菜单集合
		MenubarComp[] mcs = vmenu.getMenuBars();
		//菜单项集合
		if(mcs != null && mcs.length > 0){
			List<WebElement> weList = new ArrayList<WebElement>();
			for(MenubarComp comp : mcs){
				if(comp.getMenuList() != null){
					List<MenuItem> miList = comp.getMenuList();
					for(MenuItem mi : miList){
						weList.addAll(getMenuItems(mi));
					}
				}
			}
			wcs = weList.toArray(new WebElement[0]);
		}
		//菜单项事件集合
		if(wcs != null && wcs.length > 0){
			for(WebElement wc : wcs){
				wc.removeEventConf(eventName, methodName);
			}
		}
		//右键菜单集合
		ContextMenuComp[] cmcs = vmenu.getContextMenus();
		//右键菜单项集合
		if(cmcs != null && cmcs.length > 0){
			List<WebElement> weList = new ArrayList<WebElement>();
			for(ContextMenuComp comp : cmcs){
				if(comp.getItemList() != null){
					List<MenuItem> miList = comp.getItemList();
					for(MenuItem mi : miList){
						weList.addAll(getMenuItems(mi));
					}
				}
			}
			wcs = weList.toArray(new WebElement[0]);
		}
		//右键菜单项事件集合
		if(wcs != null && wcs.length > 0){
			for(WebElement wc : wcs){
				wc.removeEventConf(eventName, methodName);
			}
		}
	}
	
	private static List<MenuItem> getMenuItems(MenuItem mi){
		List<MenuItem> miList = new ArrayList<MenuItem>();
		if(mi != null){
			miList.add(mi);
			List<MenuItem> childList = mi.getChildList();
			if(childList != null && childList.size() > 0){
				for(MenuItem item : childList){
					miList.add(item);
					miList.addAll(getMenuItems(item));
				}
			}
		}
		return miList;
	}
	/**
	 * 读取文件内容
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readFileToString(File file) throws IOException{
		if(file != null && file.exists() && file.isFile()){
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
			char[] chuf = new char[1024];
			StringBuffer content = new StringBuffer();
			int len = 0;
			while((len = isr.read(chuf))!=-1){
				content.append(String.copyValueOf(chuf, 0, len));
			}
			isr.close();
			return content.toString();
		}
		return null;
	}
	
	public static void main(String[] args) throws IOException{
	
	}
	
}
