package nc.uap.lfw.core.comp.ctx;

/**
 * @author guoweic
 *
 */
public class FormElementContext extends BaseContext {

	private static final long serialVersionUID = -2141932721859736751L;

	private boolean enabled = true;
	
	private boolean visible = true;

	private boolean focus = false; 
	
	private String value = null; 
	
	//精度，仅FloadTextComp使用
	private String precision;
	
	// 仅ReferenceTextComp使用
	private String showValue = null;
	
	private String label = null;
	
	private String labelColor = null;
	
	private String sizeLimit = null;

	public String getSizeLimit() {
		return sizeLimit;
	}
	public void setSizeLimit(String sizeLimit) {
		this.sizeLimit = sizeLimit;
	}
	// 仅ComboBoxComp使用
	private int index;
	
	private boolean editable = true;
	// 仅ComboBoxComp使用
	private String comboDataId;
	
	public String getLabelColor() {
		return labelColor;
	}
	public void setLabelColor(String labelColor) {
		this.labelColor = labelColor;
	}
	
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isFocus() {
		return focus;
	}

	public void setFocus(boolean focus) {
		this.focus = focus;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getShowValue() {
		return showValue;
	}

	public void setShowValue(String showValue) {
		this.showValue = showValue;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getComboDataId() {
		return comboDataId;
	}

	public void setComboDataId(String comboDataId) {
		this.comboDataId = comboDataId;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
