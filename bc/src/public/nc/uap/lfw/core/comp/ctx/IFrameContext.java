package nc.uap.lfw.core.comp.ctx;

public class IFrameContext extends BaseContext {
	
	private static final long serialVersionUID = -4969704094058306617L;
	
	private String src;
	
	private boolean visible = true;

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
