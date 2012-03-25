package nc.uap.lfw.core;

import nc.uap.lfw.core.exception.LfwRuntimeException;
/**
 * 服务定位时抛出的异常
 * @author dengjt
 *
 */
public class ServiceNotFoundException extends LfwRuntimeException {
	private static final long serialVersionUID = 5415675274402405269L;
	public ServiceNotFoundException(String message, String hint, Throwable cause) {
		super(message, hint, cause);
	}

	public ServiceNotFoundException(String message, String hint) {
		super(message, hint);
	}

	public ServiceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceNotFoundException(String message) {
		super(message);
	}

	public ServiceNotFoundException(Throwable cause) {
		super(cause);
	}
}
