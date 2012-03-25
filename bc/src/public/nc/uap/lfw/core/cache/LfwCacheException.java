package nc.uap.lfw.core.cache;

import nc.uap.lfw.core.exception.LfwRuntimeException;

public class LfwCacheException extends LfwRuntimeException {
	private static final long serialVersionUID = -1903321482523350058L;
	public LfwCacheException(String message, String hint, Throwable cause) {
		super(message, hint, cause);
	}
	public LfwCacheException(String message, Throwable cause) {
		super(message, cause);
	}
	public LfwCacheException(String message) {
		super(message);
	}
	public LfwCacheException(Throwable cause) {
		super(cause);
	}
	public LfwCacheException(String message, String hint) {
		super(message, hint);
	}

}
