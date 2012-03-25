package nc.uap.lfw.core.page;

import java.io.Serializable;
/**
 * ������������壬һ����������ɰ������������
 * @author dengjt
 *
 */
public class PlugoutEmitItem implements Serializable, Cloneable {
	private static final long serialVersionUID = 2291351982569466595L;
	public static final String TYPE_DS_ROW_SELECT = "TYPE_DS_ROW_SELECT";
	public static final String TYPE_BUTTON_CLICK = "BUTTON_CLICK";
	public static final String TYPE_TEXT_VALUE_CHANGE = "TYPE_TEXT_VALUE_CHANGE";
	//����������
	private String id;
	//������Դ
	private String source;
	//��������
	private String type;
	//����
	private String desc;
	public String getId() {
		return id;
	}
	public void setId(String name) {
		this.id = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
