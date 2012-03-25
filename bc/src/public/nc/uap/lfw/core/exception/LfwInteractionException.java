package nc.uap.lfw.core.exception;

/**
 * 交互异常,由异常产生的和前台交互
 */
public class LfwInteractionException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private InteractionInfo info = null;
	
	public LfwInteractionException(InteractionInfo info) {
		this.info = info;
	}

	public InteractionInfo getInfo() {
		return info;
	}

	public void setInfo(InteractionInfo info) {
		this.info = info;
	}
}
