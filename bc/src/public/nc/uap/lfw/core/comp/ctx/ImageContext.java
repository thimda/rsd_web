package nc.uap.lfw.core.comp.ctx;

/**
 * @author guoweic
 *
 */
public class ImageContext extends BaseContext {

	private static final long serialVersionUID = -1854903654383503482L;
	private boolean enabled = true;
	private String image1;
	private String image2;
	public String getImage1() {
		return image1;
	}
	public void setImage1(String image1) {
		this.image1 = image1;
	}
	public String getImage2() {
		return image2;
	}
	public void setImage2(String image2) {
		this.image2 = image2;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
