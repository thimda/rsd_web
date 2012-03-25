package nc.uap.lfw.core.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.data.LfwParameter;
import nc.uap.lfw.core.data.Parameter;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.EventHandlerConf;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.ExcelRule;
import nc.uap.lfw.core.event.conf.FormRule;
import nc.uap.lfw.core.event.conf.GridRule;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.TreeRule;
import nc.uap.lfw.core.event.conf.WidgetRule;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.jcom.xml.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 持久化操作工具类
 * @author gd 2010-35
 * @version NC6.0
 */
public class PersistenceUtil {
	public static void addParameters(Document doc, LfwParameter[] params, Element parentNode)
	{
		if(params != null && params.length > 0)
		{
			Element paramsNode = doc.createElement("Params");
			parentNode.appendChild(paramsNode);
			for(int j = 0; j < params.length; j++)
			{
				LfwParameter eventParam = params[j];
				Element paramNode = doc.createElement("Param");
				paramsNode.appendChild(paramNode);
				Element nameNode = doc.createElement("Name");
				paramNode.appendChild(nameNode);
				
				nameNode.appendChild(doc.createTextNode(eventParam.getName()));
				
				if(eventParam instanceof Parameter){
					Parameter param = (Parameter) eventParam;
					Element valueNode = doc.createElement("Value");
					paramNode.appendChild(valueNode);
					valueNode.appendChild(doc.createTextNode(param.getValue()));
				}
				
				Element descNode = doc.createElement("Desc");
				paramNode.appendChild(descNode);
				String descValue = eventParam.getDesc() == null ? "":eventParam.getDesc();
				descNode.appendChild(doc.createCDATASection(descValue));
			}
		}	
	}
	
	
	public static void addExtendsParameters(Document doc, LfwParameter[] params, Element parentNode)
	{
		if(params != null && params.length > 0)
		{
			Element paramsNode = doc.createElement("ExtendParams");
			parentNode.appendChild(paramsNode);
			for(int j = 0; j < params.length; j++)
			{
				LfwParameter eventParam = params[j];
				Element paramNode = doc.createElement("Param");
				paramsNode.appendChild(paramNode);
				Element nameNode = doc.createElement("Name");
				paramNode.appendChild(nameNode);
				
				nameNode.appendChild(doc.createTextNode(eventParam.getName()));
				
				if(eventParam instanceof Parameter){
					Parameter param = (Parameter) eventParam;
					Element valueNode = doc.createElement("Value");
					paramNode.appendChild(valueNode);
					valueNode.appendChild(doc.createTextNode(param.getValue()));
				}
				
				Element descNode = doc.createElement("Desc");
				paramNode.appendChild(descNode);
				String descValue = eventParam.getDesc() == null ? "":eventParam.getDesc();
				descNode.appendChild(doc.createCDATASection(descValue));
			}
		}	
	}
	
	public static void addEvents(Document doc, EventHandlerConf[] events, Element parentNode)
	{
		if(events != null && events.length > 0)
		{
			Element eventsNode = doc.createElement("Events");
			parentNode.appendChild(eventsNode);
			
			for(int i = 0; i < events.length; i++)
			{
				EventHandlerConf event = events[i];
				Element eventNode = doc.createElement("Event");
				eventsNode.appendChild(eventNode);
				eventNode.setAttribute("name", event.getName());
				PersistenceUtil.addParameters(doc, event.getParamList().toArray(new LfwParameter[0]), eventNode);
				Element eventActionNode = doc.createElement("Action");
				eventNode.appendChild(eventActionNode);
				if(event.getScript() != null && !event.getScript().equals(""))
					eventActionNode.appendChild(doc.createCDATASection(event.getScript()));
			}
		}
	}
	
	
	//从其他途径的来的Listener
	public static void addListenersForm(Document doc, Map<String, JsListenerConf> originalListeners, Map<String, JsListenerConf> listeners, Element parentNode){
		List addListeners = new ArrayList();
		List modiListeners = new ArrayList();
		if(listeners != null && listeners.size() > 0){
			for (Iterator it = listeners.keySet().iterator(); it.hasNext();) {
				String listenerId = (String) it.next();
				JsListenerConf listener  = listeners.get(listenerId);
				JsListenerConf originalListener = originalListeners.get(listenerId);
				if(originalListener != null){
					//Listener都存在，检查是否是修改过的Listener
					modiListeners.add(originalListener);
					convertListener(originalListener, listener, doc);
					originalListeners.remove(listenerId);
				}else{
					//现在存在，以前不存在，是新增的Listener
					addListeners.add(listener);
				}
			}
		}
//		if((addListeners != null && addListeners.size() > 0)  || (modiListeners != null && modiListeners.size() > 0) || (originalListeners != null && originalListeners.size() > 0)){
//			parentNode = doc.createElement("MdDataset");
//			parentNode.setAttribute("id", ds.getId());
//		}
		//删除的Listener
		if(originalListeners != null && originalListeners.size() > 0){
			addDeleteListeners(doc, originalListeners.values().toArray(new JsListenerConf[0]), parentNode);
		}
		//增加的listener
		addListeners(doc, (JsListenerConf[])addListeners.toArray(new JsListenerConf[0]), parentNode);
	}
	
	/**
	 * 增加删除的Listener
	 * @param doc
	 * @param listeners
	 * @param parentNode
	 */
	public static void addDeleteListeners(Document doc, JsListenerConf[] listeners, Element parentNode)
	{
		if(listeners != null && listeners.length > 0)
		{
	
			for(int count = 0; count < listeners.length; count++)
			{
				JsListenerConf lis = listeners[count];
				Element listenerNode = doc.createElement(lis.getJsClazz());
				parentNode.appendChild(listenerNode);
				listenerNode.setAttribute("id", lis.getId());
			}
		}
	}
		
	public static Element convertListener(JsListenerConf origiListener, JsListenerConf listener, Document doc){
		Element node = null;
		if((origiListener.getServerClazz() != null && listener.getServerClazz() == null ) || (origiListener.getServerClazz() == null && listener.getServerClazz() != null)
				|| (origiListener.getServerClazz() !=  null && listener.getServerClazz() != null 
						&& !origiListener.getServerClazz().equals(listener.getServerClazz()))){
			node = doc.createElement("ModiListener");
			node.setAttribute("id", listener.getId());
			node.setAttribute("serverClazz", listener.getJsClazz());
		}
		if(origiListener.getEventHandlerMap() != listener.getEventHandlerMap()){
			node = doc.createElement("ModiListener");
			node.setAttribute("id", listener.getId());
			Map<String, EventHandlerConf> jsHandlersMap = listener.getEventHandlerMap();
			if(jsHandlersMap != null && !jsHandlersMap.isEmpty())
			{
				EventHandlerConf[]	events = jsHandlersMap.values().toArray(new EventHandlerConf[0]);
					if(events != null && events.length > 0){
					Element eventsNode = doc.createElement("Events");
					node.appendChild(eventsNode);
					for(int i = 0; i < events.length; i++)
					{
						EventHandlerConf event = events[i];
						if((event.getScript() != null && !event.getScript().equals("")) 
								|| event.isOnserver() == true 
								|| event.getExtendParamList().size() > 0)
						{	
							Element eventNode = doc.createElement("Event");
							eventsNode.appendChild(eventNode);
							eventNode.setAttribute("onserver", event.isOnserver() ? "true" : "false");
							eventNode.setAttribute("async", event.isAsync() ? "true" : "false");
							
							List<LfwParameter> extendParamList = event.getExtendParamList();
							if (extendParamList != null && extendParamList.size() > 0) {
								//Element extendParamNode = doc.createElement("ExtendParams");
//								for (LfwParameter param : extendParamList) {
//									Element paramNode = doc.createElement("Param");
//									paramNode.setAttribute("name", param.getName());
//									paramNode.setAttribute("desc", param.getDesc());
//									extendParamNode.appendChild(paramNode);
//								}
								//TODO
								addExtendsParameters(doc, extendParamList.toArray(new LfwParameter[0]), eventNode);
								//eventNode.appendChild(extendParamNode);
							}
							
							EventSubmitRule submitRule = event.getSubmitRule();
							if(submitRule != null)
							{
								Element submitRuleNode = doc.createElement("SubmitRule");
								eventNode.appendChild(submitRuleNode);
								// 父提交规则
								EventSubmitRule pSubmitRule = submitRule.getParentSubmitRule();
								if(pSubmitRule != null)
								{
									Element pSubmitRuleNode = doc.createElement("SubmitRule");
									submitRuleNode.appendChild(pSubmitRuleNode);
									Map<String, WidgetRule> pWidgetRuleMap = pSubmitRule.getWidgetRules();
									if(!pWidgetRuleMap.isEmpty())
										addSubmitContent(doc, pSubmitRuleNode, pWidgetRuleMap);
								}
								
								Map<String, WidgetRule> widgetRuleMap = submitRule.getWidgetRules();
								if(!widgetRuleMap.isEmpty())
									addSubmitContent(doc, submitRuleNode, widgetRuleMap);
								addParameters(doc, submitRule.getParams(), submitRuleNode);
							}
							
							eventNode.setAttribute("name", event.getName());
							
							addParameters(doc, event.getParamList().toArray(new LfwParameter[0]), eventNode);
							Element actionNode = doc.createElement("Action");
							eventNode.appendChild(actionNode);
							if(event.getScript() != null && !event.getScript().equals(""))
								actionNode.appendChild(doc.createCDATASection(event.getScript()));
						}
					}
				}	
			}
		}
		return node;
	}
	
	
	/**
	 * 持久化listener
	 * @param doc
	 * @param listeners
	 * @param parentNode
	 */
	public static void addListeners(Document doc, JsListenerConf[] listeners, Element parentNode)
	{
		if(listeners != null && listeners.length > 0)
		{
			Element listenersNode = doc.createElement("Listeners");
			parentNode.appendChild(listenersNode);
			for(int count = 0; count < listeners.length; count++)
			{
				JsListenerConf lis = listeners[count];
				if(lis.getFrom() != null && (lis.getConfType() == null || !lis.getConfType().equals(nc.uap.lfw.core.comp.WebElement.CONF_DEL)))
					continue;
				Element listenerNode = doc.createElement(lis.getJsClazz());
				listenersNode.appendChild(listenerNode);
				listenerNode.setAttribute("id", lis.getId());
				if(lis.getFrom() != null && !lis.getFrom().equals(""))
					listenerNode.setAttribute("from", lis.getFrom());
				if(lis.getServerClazz() != null && !lis.getServerClazz().equals("")){
					listenerNode.setAttribute("serverClazz", lis.getServerClazz());
				}
				if(lis.getConfType() != null && !lis.getConfType().equals("")){
					listenerNode.setAttribute("confType", lis.getConfType());
				}
				
				// 获取当前listener定义的handler
				Map<String, EventHandlerConf> jsHandlersMap = lis.getEventHandlerMap();
				if(jsHandlersMap != null && !jsHandlersMap.isEmpty())
				{
					EventHandlerConf[]	events = jsHandlersMap.values().toArray(new EventHandlerConf[0]);
						if(events != null && events.length > 0){
						Element eventsNode = doc.createElement("Events");
						listenerNode.appendChild(eventsNode);
						for(int i = 0; i < events.length; i++)
						{
							EventHandlerConf event = events[i];
							if((event.getScript() != null && !event.getScript().equals("")) 
									|| event.isOnserver() == true
									|| event.getExtendParamList().size() > 0)
							{	
								Element eventNode = doc.createElement("Event");
								eventsNode.appendChild(eventNode);
								eventNode.setAttribute("onserver", event.isOnserver() ? "true" : "false");
								eventNode.setAttribute("async", event.isAsync() ? "true" : "false");
								
								List<LfwParameter> extendParamList = event.getExtendParamList();
								if (extendParamList != null && extendParamList.size() > 0) {
									//Element extendParamNode = doc.createElement("ExtendParams");
//									for (LfwParameter param : extendParamList) {
//										Element paramNode = doc.createElement("Param");
//										paramNode.setAttribute("name", param.getName());
//										paramNode.setAttribute("desc", param.getDesc());
//										extendParamNode.appendChild(paramNode);
//									}
									//TODO
									addExtendsParameters(doc, extendParamList.toArray(new LfwParameter[0]), eventNode);
									
									//eventNode.appendChild(extendParamNode);
								}
								
								EventSubmitRule submitRule = event.getSubmitRule();
								if(submitRule != null)
								{
									Element submitRuleNode = doc.createElement("SubmitRule");
									submitRuleNode.setAttribute("cardSubmit", "" + submitRule.isCardSubmit());
									submitRuleNode.setAttribute("tabSubmit", "" + submitRule.isTabSubmit());
									submitRuleNode.setAttribute("panelSubmit", "" + submitRule.isPanelSubmit());
									eventNode.appendChild(submitRuleNode);
									// 父提交规则
									EventSubmitRule pSubmitRule = submitRule.getParentSubmitRule();
									if(pSubmitRule != null)
									{
										Element pSubmitRuleNode = doc.createElement("SubmitRule");
										pSubmitRuleNode.setAttribute("pagemeta", pSubmitRule.getPagemeta());
										pSubmitRuleNode.setAttribute("cardSubmit", "" + pSubmitRule.isCardSubmit());
										pSubmitRuleNode.setAttribute("tabSubmit", "" + pSubmitRule.isTabSubmit());
										pSubmitRuleNode.setAttribute("panelSubmit", "" + pSubmitRule.isPanelSubmit());
										submitRuleNode.appendChild(pSubmitRuleNode);
										Map<String, WidgetRule> pWidgetRuleMap = pSubmitRule.getWidgetRules();
										if(!pWidgetRuleMap.isEmpty())
											addSubmitContent(doc, pSubmitRuleNode, pWidgetRuleMap);
									}
									
									Map<String, WidgetRule> widgetRuleMap = submitRule.getWidgetRules();
									if(!widgetRuleMap.isEmpty())
										addSubmitContent(doc, submitRuleNode, widgetRuleMap);
									addParameters(doc, submitRule.getParams(), submitRuleNode);
								}
								
								eventNode.setAttribute("name", event.getName());
								
								addParameters(doc, event.getParamList().toArray(new LfwParameter[0]), eventNode);
								Element actionNode = doc.createElement("Action");
								eventNode.appendChild(actionNode);
								if(event.getScript() != null && !event.getScript().equals(""))
									actionNode.appendChild(doc.createCDATASection(event.getScript()));
							}
						}
					}	
				}
			}
		}
	
	}
	
	public static void addSubmitContent(Document doc, Element parentNode, Map<String, WidgetRule> widgetRule)
	{
		Iterator<String> widgetIt = widgetRule.keySet().iterator();
		while(widgetIt.hasNext())
		{
			String widgetId = widgetIt.next();
			WidgetRule wr = widgetRule.get(widgetId);
			Element widgetRuleNode = doc.createElement("Widget");
			parentNode.appendChild(widgetRuleNode);
			widgetRuleNode.setAttribute("id", wr.getId());
			widgetRuleNode.setAttribute("cardSubmit", "" + wr.isCardSubmit());
			widgetRuleNode.setAttribute("tabSubmit", "" + wr.isTabSubmit());
			widgetRuleNode.setAttribute("panelSubmit", "" + wr.isPanelSubmit());
			
			DatasetRule[] dsRules = wr.getDatasetRules();
			if(dsRules != null){
				for (int i = 0; i < dsRules.length; i++) {
					DatasetRule dr = dsRules[i];
					Element dsRuleNode = doc.createElement("Dataset");
					widgetRuleNode.appendChild(dsRuleNode);
					dsRuleNode.setAttribute("id", dr.getId());
					dsRuleNode.setAttribute("type", dr.getType());
				}
			}
			
			TreeRule[] treeRules = wr.getTreeRules();
			if(treeRules != null)
			{
				for (int i = 0; i < treeRules.length; i++) {
					TreeRule dr = treeRules[i];
					Element treeRuleNode = doc.createElement("Tree");
					widgetRuleNode.appendChild(treeRuleNode);
					treeRuleNode.setAttribute("id", dr.getId());
					treeRuleNode.setAttribute("type", dr.getType());
				}
			}
			
			GridRule[] gridRules = wr.getGridRules();
			if(gridRules != null)
			{
				for (int i = 0; i < gridRules.length; i++) {
					GridRule dr = gridRules[i];
					Element gridRuleNode = doc.createElement("Grid");
					widgetRuleNode.appendChild(gridRuleNode);
					gridRuleNode.setAttribute("id", dr.getId());
					gridRuleNode.setAttribute("type", dr.getType());
				}
			}
			
			FormRule[] formRules = wr.getFormRules();
			if(formRules != null){
				for (int i = 0; i < formRules.length; i++) {
					FormRule dr = formRules[i];
					Element formRuleNode = doc.createElement("Form");
					widgetRuleNode.appendChild(formRuleNode);
					formRuleNode.setAttribute("id", dr.getId());
					formRuleNode.setAttribute("type", dr.getType());
				}
			}
			
			ExcelRule[] excelRules = wr.getExcelRules();
			if(excelRules != null)
			{
				for (int i = 0; i < excelRules.length; i++) {
					ExcelRule dr = excelRules[i];
					Element excelRuleNode = doc.createElement("Excel");
					widgetRuleNode.appendChild(excelRuleNode);
					excelRuleNode.setAttribute("id", dr.getId());
					excelRuleNode.setAttribute("type", dr.getType());
				}
			}
		}
	}
	
	public static void toXmlFile(Document doc, String filePath, String fileName)
	{
		// 写出文件
		Writer wr = null;
	    try
	    {
	    	File dir = new File(filePath);
	    	if(!dir.exists())
	    		dir.mkdirs();
	    	File file = new File(filePath + File.separatorChar + fileName);
	    	if(!file.exists())
	    		file.createNewFile();
	    	wr = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
	    	XMLUtil.printDOMTree(wr, doc, 0, "UTF-8");
	    }
	    catch (IOException e)
		{
	    	Logger.error(e, e);
	    	throw new LfwRuntimeException(e);
		}
		finally
		{
			try
			{
				if(wr != null){
					wr.flush();
					wr.close();
				}
			} 
			catch (IOException e)
			{
				Logger.error(e, e);
			}
		}
	}
}

