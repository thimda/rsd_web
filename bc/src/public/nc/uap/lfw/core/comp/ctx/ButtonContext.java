package nc.uap.lfw.core.comp.ctx;

public class ButtonContext extends BaseContext {
	private static final long serialVersionUID = -2914956126049091351L;
	private boolean enabled = true;
	private String text;
	private boolean visible = true;
	
	private String width;
	
	private String height;
	
	private String refImg;
	
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
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getRefImg() {
		return refImg;
	}
	public void setRefImg(String refImg) {
		this.refImg = refImg;
	}
	
}
