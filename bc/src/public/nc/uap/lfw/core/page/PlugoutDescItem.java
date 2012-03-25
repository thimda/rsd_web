package nc.uap.lfw.core.page;

import java.io.Serializable;

/**
 * 输出项描述，每个项描述输出中的一个值。支持表达式，支持自定义
 * @author dengjt
 *
 */
public class PlugoutDescItem implements Serializable, Cloneable {
	private static final long serialVersionUID = -8798072449173452354L;
	public static final String TYPE_FOMULAR = "TYPE_FOMULAR";
	public static final String TYPE_DS_FIELD = "TYPE_DS_FIELD";
	public static final String TYPE_TEXT_VALUE = "TYPE_TEXT_VALUE";
	//输出键名称
	private String name;
	//取数类型
	private String type;
	//取数来源
	private String source;
	//取得的值，一般情况下，只有静态值和表达式需要记录。
	private String value;
	//描述
	private String desc;
	//输出对象类型
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
