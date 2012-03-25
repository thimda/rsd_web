package nc.uap.lfw.core.comp.ctx;

/**
 * @author guoweic
 *
 */
public class ProgressBarContext extends BaseContext {

	private static final long serialVersionUID = 3602431250873534673L;
	
	private String value;
	
	private boolean visible = true;
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
