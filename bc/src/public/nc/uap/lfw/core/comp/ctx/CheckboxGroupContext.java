package nc.uap.lfw.core.comp.ctx;

/**
 * @author guoweic
 *
 */
public class CheckboxGroupContext extends BaseContext {

	private static final long serialVersionUID = -8777657997067214960L;
	
	private boolean enabled = true;
	
	private String comboDataId;
	
	private String value;
	
	private boolean visible = true;

	private boolean readOnly;

	private CheckBoxContext[] checkboxContexts;

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getComboDataId() {
		return comboDataId;
	}

	public void setComboDataId(String comboDataId) {
		this.comboDataId = comboDataId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public CheckBoxContext[] getCheckboxContexts() {
		return checkboxContexts;
	}

	public void setCheckboxContexts(CheckBoxContext[] checkboxContexts) {
		this.checkboxContexts = checkboxContexts;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

}
