package nc.uap.lfw.core.exception;

public class LfwPluginException extends RuntimeException {

	private static final long serialVersionUID = 6461253948274704817L;
	private String hint;

	public LfwPluginException(String message, Throwable cause) {
		super(message, cause);
	}

	public LfwPluginException(String message, String hint, Throwable cause) {
		super(message, cause);
		this.hint = hint;
	}
	
	public LfwPluginException(String message) {
		super(message);
	}
	
	public LfwPluginException(String message, String hint){
		super(message);
		this.hint = hint;
	}

	public LfwPluginException(Throwable cause) {
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
