package nc.uap.lfw.core.comp.ctx;

public class ComboBoxContext extends BaseContext {
	private static final long serialVersionUID = 5851405804648688890L;
	
	private boolean enabled = true;
	private String value;
	private boolean visible = true;
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

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
