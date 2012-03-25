package nc.uap.lfw.core.comp.ctx;

/**
 * @author guoweic
 *
 */
public class CheckBoxContext extends TextContext {
	private static final long serialVersionUID = 5622338595247715970L;
//	private boolean enabled = true;
	private boolean checked = false;
//	public boolean isEnabled() {
//		return enabled;
//	}
//	public void setEnabled(boolean enabled) {
//		this.enabled = enabled;
//	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
