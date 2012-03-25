package nc.uap.lfw.core.exception;

public class LfwValidateException extends LfwRuntimeException {
	private static final long serialVersionUID = 5643508282282916234L;

	public LfwValidateException(String message, Throwable cause) {
		super(message, cause);
	}

	public LfwValidateException(String s) {
		super(s);
	}

	public LfwValidateException(Throwable cause) {
		super(cause);
	}
	
}
