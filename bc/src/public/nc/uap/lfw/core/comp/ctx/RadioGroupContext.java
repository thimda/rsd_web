package nc.uap.lfw.core.comp.ctx;

/**
 * @author guoweic
 *
 */
public class RadioGroupContext extends BaseContext {

	private static final long serialVersionUID = -626657722405988798L;
	
	private boolean enabled = true;
	
	private String comboDataId;
	
	private int index;
	
	private String value;

	private boolean readOnly;

//	private RadioContext[] radioContexts;
	
	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

//	public RadioContext[] getRadioContexts() {
//		return radioContexts;
//	}
//
//	public void setRadioContexts(RadioContext[] radioContexts) {
//		this.radioContexts = radioContexts;
//	}

}
