/**
 * 
 */
package nc.uap.lfw.core.util;

import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.uimodel.conf.Model;
import nc.vo.jcom.xml.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author chouhl
 *
 */
public class ModelToXml {

	public static void toXml(String filePath, String fileName, String projectPath, Model model){
		Document doc = XMLUtil.getNewDocument();
		Element rootNode = AMCUtil.getElementFromClass(doc, model);
		doc.appendChild(rootNode);
		// Ð´³öÎÄ¼þ
		PersistenceUtil.toXmlFile(doc, filePath, fileName);
	}
	
}
