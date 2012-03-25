package nc.uap.lfw.core.comp.ctx;

/**
 * ¸´ÔÓÎÄ±¾±à¼­Æ÷µÄContext
 * 
 * @author guoweic
 */
public class EditorContext extends BaseContext {

	private static final long serialVersionUID = 4598356864698097304L;

	private String value;
	private boolean readOnly;
	private boolean enabled = true;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

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

}
