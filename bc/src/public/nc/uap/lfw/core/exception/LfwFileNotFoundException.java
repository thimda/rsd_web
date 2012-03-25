package nc.uap.lfw.core.exception;
/**
 * 用于配置文件找不到的exception
 * @author dengjt
 *
 */
public class LfwFileNotFoundException extends LfwRuntimeException {

	private static final long serialVersionUID = -7423994232359308526L;

	public LfwFileNotFoundException(String message, String hint, Throwable cause) {
		super(message, hint, cause);
	}

	public LfwFileNotFoundException(String message, String hint) {
		super(message, hint);
	}

	public LfwFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public LfwFileNotFoundException(String message) {
		super(message);
	}

	public LfwFileNotFoundException(Throwable cause) {
		super(cause);
	}

}
