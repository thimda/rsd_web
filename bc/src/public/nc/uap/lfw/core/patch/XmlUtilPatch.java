package nc.uap.lfw.core.patch;

import javax.xml.parsers.DocumentBuilder;

import org.granite.lang.util.DOMs;
import org.w3c.dom.Document;
/**
 * �������Ҫ�������NC�ײ��ṩ��XMLUtil���������⡣���Ǹ����޸�֮���������Ҫɾ��
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
//            throw new RuntimeException("XML����������ʧ��!");
//        }
    }
	public static Document getNewDocument() {
		return getDocumentBuilder().newDocument();
	}
}
