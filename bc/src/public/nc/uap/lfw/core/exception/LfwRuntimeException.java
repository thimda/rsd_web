package nc.uap.lfw.core.exception;
/**
 * Web������ܻ����쳣��.���ཫ�쳣��Ϣ�ֳ�hint��message.����hint���Ƕ��û�ֱ�ӿɼ����Ѻ���ʾ��Ϣ.
 * message ��stacktrace�ǶԿͻ��˵������õ���Ϣ.
 * @author dengjt
 * 2007-1-25
 */
public class LfwRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 6461253948274704817L;
	private String hint;

	public LfwRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public LfwRuntimeException(String message, String hint, Throwable cause) {
		super(message, cause);
		this.hint = hint;
	}
	
	public LfwRuntimeException(String message) {
		super(message);
	}
	
	public LfwRuntimeException(String message, String hint){
		super(message);
		this.hint = hint;
	}

	public LfwRuntimeException(Throwable cause) {
		super(cause);
	}

	public String getHint() {
		if(hint == null)
			hint = this.getMessage();
		if(hint == null){
			if(this.getCause() != null)
				hint = this.getCause().getMessage();
		}
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}
	
}
