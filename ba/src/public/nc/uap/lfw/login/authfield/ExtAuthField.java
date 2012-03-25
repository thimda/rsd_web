package nc.uap.lfw.login.authfield;

import java.io.Serializable;

/**
 * 第三方系统验证信息附加字段定义基类，各种类型field扩展以获得相关值信息
 * @author dengjt 2006-4-18
 */
public abstract class ExtAuthField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 对应的标签显示值
	private String label;
	// 控件属性名称
	private String name;

	// 是否必须
	private boolean required = true;
	private String regStr = null;
	private Object userObject;

	public ExtAuthField() {
	}

	public ExtAuthField(String label, String name, boolean required) {
		setLabel(label);
		setName(name);
		setRequired(required);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public abstract int getType() ;

	public String getRegStr() {
		return regStr;
	}

	public void setRegStr(String regStr) {
		this.regStr = regStr;
	}

	/**
	 * @return Returns the userObject.
	 */
	public Object getUserObject() {
		return userObject;
	}

	/**
	 * @param userObject The userObject to set.
	 */
	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}
}
