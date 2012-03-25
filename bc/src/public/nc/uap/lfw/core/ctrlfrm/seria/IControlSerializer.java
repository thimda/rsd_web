package nc.uap.lfw.core.ctrlfrm.seria;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 控件序列化成Xml接口
 * @author dengjt
 *
 */
public interface IControlSerializer<T> {
	public void serialize(Digester digester);
	public void deSerialize(T t, Document doc, Element parentNode);
}
