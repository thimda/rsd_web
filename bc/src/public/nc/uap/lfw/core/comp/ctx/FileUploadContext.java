package nc.uap.lfw.core.comp.ctx;

/**
 * @author guoweic
 *
 */
public class FileUploadContext extends BaseContext {
	
	private static final long serialVersionUID = 2158792887987811222L;
	
	private boolean enabled = true;
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
