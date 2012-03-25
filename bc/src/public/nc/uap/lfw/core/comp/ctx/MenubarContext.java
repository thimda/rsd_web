package nc.uap.lfw.core.comp.ctx;

/**
 * @author guoweic
 *
 */
public class MenubarContext extends BaseContext {

	private static final long serialVersionUID = -1831288692426049090L;
	
	private MenuItemContext[] childItemContexts;

	public MenuItemContext[] getChildItemContexts() {
		return childItemContexts;
	}

	public void setChildItemContexts(MenuItemContext[] childItemContexts) {
		this.childItemContexts = childItemContexts;
	}

}
