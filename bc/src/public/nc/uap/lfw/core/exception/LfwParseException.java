package nc.uap.lfw.core.exception;

public class LfwParseException extends LfwRuntimeException {
	private static final long serialVersionUID = 7933322024363835575L;
	public LfwParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public LfwParseException(String message, String hint, Throwable cause) {
		super(message, hint, cause);
	}

	public LfwParseException(String message, String hint) {
		super(message, hint);
	}

	public LfwParseException(String message) {
		super(message);
	}

	public LfwParseException(Throwable cause) {
		super(cause);
	}

}
