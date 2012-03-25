package nc.uap.lfw.core.comp.ctx;

/**
 * @author guoweic
 *
 */
public class ContextMenuContext extends BaseContext {

	private static final long serialVersionUID = 1785496897575912208L;

	private String triggerId;
	
	private MenuItemContext[] childItemContexts;
	
	private ContextMenuContext[] childMenuContexts;

	public MenuItemContext[] getChildItemContexts() {
		return childItemContexts;
	}

	public void setChildItemContexts(MenuItemContext[] childItemContexts) {
		this.childItemContexts = childItemContexts;
	}

	public ContextMenuContext[] getChildMenuContexts() {
		return childMenuContexts;
	}

	public void setChildMenuContexts(ContextMenuContext[] childMenuContexts) {
		this.childMenuContexts = childMenuContexts;
	}

	public String getTriggerId() {
		return triggerId;
	}

	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}
	
}
