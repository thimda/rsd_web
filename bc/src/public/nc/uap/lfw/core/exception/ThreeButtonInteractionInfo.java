package nc.uap.lfw.core.exception;

public class ThreeButtonInteractionInfo extends InteractionInfo {
	private static final long serialVersionUID = 618892671908214445L;
	private String msg;
	private String[] btnTexts;
	public ThreeButtonInteractionInfo(String id, String msg, String title) {
		super(id, title);
		this.msg = msg;
	}

	public String getTitle() {
		String title = super.getTitle();
		if(title == null || title.equals(""))
			return "¶Ô»°¿ò";
		return title;
	}

	public String getType() {
		return THREE_BUTTONS_TYPE;
	}

	public String getMsg() {
		return msg;
	}

	public String[] getBtnTexts() {
		return btnTexts;
	}

	public void setBtnTexts(String[] btnTexts) {
		this.btnTexts = btnTexts;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
