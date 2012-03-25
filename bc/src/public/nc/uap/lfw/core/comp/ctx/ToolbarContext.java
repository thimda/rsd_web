package nc.uap.lfw.core.comp.ctx;

/**
 * @author guoweic
 *
 */
public class ToolbarContext extends BaseContext {

	private static final long serialVersionUID = -2177394655748128033L;
	
	private ButtonContext[] buttonItemContexts;
	
	private String[] delIds;

	public ButtonContext[] getButtonItemContexts() {
		return buttonItemContexts;
	}

	public void setButtonItemContexts(ButtonContext[] buttonItemContexts) {
		this.buttonItemContexts = buttonItemContexts;
	}

	public String[] getDelIds() {
		return delIds;
	}

	public void setDelIds(String[] delIds) {
		this.delIds = delIds;
	}

}
