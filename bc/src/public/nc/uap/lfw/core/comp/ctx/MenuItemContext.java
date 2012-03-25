package nc.uap.lfw.core.comp.ctx;

/**
 * @author guoweic
 *
 */
public class MenuItemContext extends BaseContext {

	private static final long serialVersionUID = 5710586136377319769L;

	private Boolean enabled = null;
	private Boolean visible = null;
	private String text;
	private String refImg;
	private MenuItemContext[] childItemContexts;
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public MenuItemContext[] getChildItemContexts() {
		return childItemContexts;
	}
	public void setChildItemContexts(MenuItemContext[] childItemContexts) {
		this.childItemContexts = childItemContexts;
	}
	public String getRefImg() {
		return refImg;
	}
	public void setRefImg(String refImg) {
		this.refImg = refImg;
	}
}
