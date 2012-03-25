package nc.uap.lfw.core.tags;

import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * ��̬����ʵ���ࡣ������Ҫ��Ӷ�̬���� jsp:attribute��tag��ֱ�Ӽ̳д�����
 * 
 * @author dengjt
 * 
 */
public class SimpleTagWithAttribute extends LfwTagSupport implements DynamicAttributes 
{
	private HashMap<String, Object> attributeMap = null;

	public void setDynamicAttribute(String uri, String name, Object value)
			throws JspException {
		if (attributeMap == null)
			attributeMap = new HashMap<String, Object>();
		attributeMap.put(name, value);
	}

	protected HashMap<String, Object> getAttributeMap() {
		return this.attributeMap;
	}

	protected Object getAttribute(String key) {
		if (getAttributeMap() == null)
			return null;
		return getAttributeMap().get(key);
	}

	protected void setAttribute(String key, Object value) {
		try {
			setDynamicAttribute(null, key, value);
		} 
		catch (JspException e) {
			throw new LfwRuntimeException(e);
		}
	}

}
