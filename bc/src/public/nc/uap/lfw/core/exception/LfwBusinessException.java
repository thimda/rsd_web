package nc.uap.lfw.core.exception;

/**
 * lfw����ҵ���߼������׳��쳣
 * @author dengjt
 */
public class LfwBusinessException extends Exception {
	private static final long serialVersionUID = -7701050274700646799L;

	public LfwBusinessException() {
		super();
	}

	public LfwBusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public LfwBusinessException(String s) {
		super(s);
	}

	public LfwBusinessException(Throwable cause) {
		super(cause);
	}


}
