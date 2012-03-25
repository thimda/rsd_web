package nc.uap.lfw.core.exception;
/**
 * Web开发框架基础异常类.此类将异常信息分成hint和message.其中hint将是对用户直接可见的友好提示信息.
 * message 和stacktrace是对客户端调试有用的信息.
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
