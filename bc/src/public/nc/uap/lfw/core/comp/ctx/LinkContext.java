package nc.uap.lfw.core.comp.ctx;

/**
 * @author guoweic
 *
 */
public class LinkContext extends BaseContext {

	private static final long serialVersionUID = -6437664801040024084L;
	
	private boolean visible = true;
	private String text ;
	private boolean enabled = true;
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


}
