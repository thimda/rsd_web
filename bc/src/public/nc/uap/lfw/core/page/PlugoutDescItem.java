package nc.uap.lfw.core.page;

import java.io.Serializable;

/**
 * �����������ÿ������������е�һ��ֵ��֧�ֱ��ʽ��֧���Զ���
 * @author dengjt
 *
 */
public class PlugoutDescItem implements Serializable, Cloneable {
	private static final long serialVersionUID = -8798072449173452354L;
	public static final String TYPE_FOMULAR = "TYPE_FOMULAR";
	public static final String TYPE_DS_FIELD = "TYPE_DS_FIELD";
	public static final String TYPE_TEXT_VALUE = "TYPE_TEXT_VALUE";
	//���������
	private String name;
	//ȡ������
	private String type;
	//ȡ����Դ
	private String source;
	//ȡ�õ�ֵ��һ������£�ֻ�о�ֵ̬�ͱ��ʽ��Ҫ��¼��
	private String value;
	//����
	private String desc;
	//�����������
	private String clazztype = "java.lang.String";
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getClazztype() {
		return clazztype;
	}
	public void setClazztype(String clazztype) {
		this.clazztype = clazztype;
	}
}
