package nc.uap.lfw.core.exception;

/**
 * �����쳣,���쳣�����ĺ�ǰ̨����
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
