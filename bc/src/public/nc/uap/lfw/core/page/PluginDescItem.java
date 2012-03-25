package nc.uap.lfw.core.page;

import java.io.Serializable;

/**
 * ����ӿ�������
 * @author dengjt
 *
 */
public class PluginDescItem implements Cloneable, Serializable {
	private static final long serialVersionUID = 1564636173007514266L;
	public static final String TYPE_DS_LOAD = "TYPE_DS_LOAD";
	public static final String TYPE_IFRAME_REDIRECT = "TYPE_IFRAME_REDIRECT";
	// �û��Զ������
	public static final String TYPE_SELFDEF = "TYPE_SELFDEF";
	
	private String id;
	private String value;
	//�����������
	private String clazztype = "java.lang.String";
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getClazztype() {
		return clazztype;
	}
	
	public void setClazztype(String clazztype) {
		this.clazztype = clazztype;
	}
}
