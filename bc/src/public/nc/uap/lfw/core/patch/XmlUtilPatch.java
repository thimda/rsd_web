package nc.uap.lfw.core.patch;

import javax.xml.parsers.DocumentBuilder;

import org.granite.lang.util.DOMs;
import org.w3c.dom.Document;
/**
 * 这个类主要用来解决NC底层提供的XMLUtil的性能问题。在那个类修改之后，这个类需要删除
 * @author dengjt
 *
 */
public final class XmlUtilPatch {
    public static DocumentBuilder getDocumentBuilder() {
    	return DOMs.getDocumentBuilder(false);
//		DocumentBuilderFactory dbf = new DocumentBuilderFactoryImpl();
//        dbf.setValidating(false);
//        dbf.setNamespaceAware(false);
//        try {
//        	return dbf.newDocumentBuilder();
//        } 
//        catch (ParserConfigurationException e) {
//            throw new RuntimeException("XML解析器构造失败!");
//        }
    }
	public static Document getNewDocument() {
		return getDocumentBuilder().newDocument();
	}
}
