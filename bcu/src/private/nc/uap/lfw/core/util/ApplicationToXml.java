package nc.uap.lfw.core.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.uimodel.Application;
import nc.vo.jcom.xml.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author chouhl
 *
 */
public class ApplicationToXml {

	public static void toXml(String filePath, String fileName, String projectPath, Application appConf){
		Document doc = XMLUtil.getNewDocument();
		
		Element rootNode = AMCUtil.getElementFromClass(doc, appConf);
		doc.appendChild(rootNode);
		
		rootNode.setAttribute("sourcePackage", appConf.getSourcePackage());
		
		Element windowsNode = doc.createElement(PageMeta.TagName + "s");
		List<PageMeta> windowList = appConf.getWindowList();
		if(windowList != null && windowList.size() > 0){
			for(PageMeta window : windowList){
				Element windowNode = doc.createElement(PageMeta.TagName);
				windowNode.setAttribute("id", window.getId());
				windowNode.setAttribute("caption", window.getCaption());
				windowsNode.appendChild(windowNode);
			}
		}
		rootNode.appendChild(windowsNode);
		//Events
		AMCUtil.addEvents(doc, appConf.getEventConfs(), rootNode);
		//持久化plug关联
		addPlugConnectors(doc, rootNode, appConf);
		
		// 写出文件
		PersistenceUtil.toXmlFile(doc, filePath, fileName);
	}
	
	private static void addPlugConnectors(Document doc, Element rootNode, Application app){
		List<Connector> connectorList = app.getConnectorList();
		if (connectorList.size() > 0){
			Element connNode = doc.createElement("Connectors");
			rootNode.appendChild(connNode);
			for (Connector conn : connectorList) {
//				String id = i.next();
//				Connector conn = connectorMap.get(id);
				Element c = doc.createElement("Connector");
				connNode.appendChild(c);
//				c.setAttribute("id", conn.getId());
				c.setAttribute("sourceWindow", conn.getSourceWindow());
				c.setAttribute("source", conn.getSource());
				c.setAttribute("targetWindow", conn.getTargetWindow());
				c.setAttribute("target", conn.getTarget());
				c.setAttribute("pluginId", conn.getPluginId());
				c.setAttribute("plugoutId", conn.getPlugoutId());
				Map<String, String> map =  conn.getMapping();
				if (map != null && map.size() > 0){
					Element maps = doc.createElement("Maps");
					c.appendChild(maps);
					for (Iterator<String> j = map.keySet().iterator() ; j.hasNext(); ){
						Element m = doc.createElement("Map");
						maps.appendChild(m);
						String outValue = j.next();
						String inValue = map.get(outValue);
						Element out = doc.createElement("outValue");
						m.appendChild(out);
//						out.set.setNodeValue(outValue);
						out.appendChild(doc.createTextNode(outValue));
						Element in = doc.createElement("inValue");
						m.appendChild(in);
//						out.setNodeValue(inValue);
						in.appendChild(doc.createTextNode(inValue));
//						Element e = doc.createElement("Map");
//						maps.appendChild(e);
//						e.setAttribute("outValue", outValue);
//						e.setAttribute("inValue", inValue);
					}
				}
			}	
		}
	}
	
}
