package nc.uap.lfw.login.authfield;

import java.io.Serializable;

/**
 * ������ϵͳ��֤��Ϣ�����ֶζ�����࣬��������field��չ�Ի�����ֵ��Ϣ
 * @author dengjt 2006-4-18
 */
public abstract class ExtAuthField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ��Ӧ�ı�ǩ��ʾֵ
	private String label;
	// �ؼ���������
	private String name;

	// �Ƿ����
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
