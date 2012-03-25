package nc.uap.lfw.core.exception;

import java.io.Serializable;

/**
 * 交互对话框输入条目,支持字符输入框、整型输入框、下拉输入框
 * @author gd 
 * @version 6.0
 */
public abstract class InputItem implements Serializable{
	private static final long serialVersionUID = -7417613034328850119L;
	public String STRING_TYPE = "string";
	public String INT_TYPE = "int";
	public String COMBO_TYPE = "combo";
	public String RADIO_TYPE = "radio";
	private String labelText;
	private String inputId;
//	private String inputType;
	private boolean required;
	private Object value;
	public InputItem(String inputId, String labelText, boolean required)
	{
		this.inputId = inputId;
		this.labelText = labelText;
		this.required = required;
//		this.inputType = getInputType();
	}
	
	public String getLabelText() {
		return labelText;
	}
	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}
	public String getInputId() {
		return inputId;
	}
	public void setInputId(String inputId) {
		this.inputId = inputId;
	}
	
	public abstract String getInputType();
	
//	public void setInputType(String inputType) {
//		this.inputType = inputType;
//	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
}
