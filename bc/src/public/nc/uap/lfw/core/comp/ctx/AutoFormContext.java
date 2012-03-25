package nc.uap.lfw.core.comp.ctx;

/**
 * @author guoweic
 *
 */
public class AutoFormContext extends BaseContext {

	private static final long serialVersionUID = -6454586881363017556L;
	
	private boolean enabled = true;
	private boolean readOnly = false;
	private FormElementContext[] elementContexts;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public FormElementContext[] getElementContexts() {
		return elementContexts;
	}

	public void setElementContexts(FormElementContext[] elementContexts) {
		this.elementContexts = elementContexts;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

}
