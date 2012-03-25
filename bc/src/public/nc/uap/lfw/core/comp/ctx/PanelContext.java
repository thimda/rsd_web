package nc.uap.lfw.core.comp.ctx;

/**
 * @author guoweic
 *
 */
public class PanelContext extends BaseContext {
	
	private static final long serialVersionUID = -4599872229377059812L;
	
	private boolean display = true;
	private boolean visible = true;
	
	public boolean isDisplay() {
		return display;
	}
	public void setDisplay(boolean display) {
		this.display = display;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
