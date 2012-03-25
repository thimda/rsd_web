package nc.uap.lfw.core.exception;

/**
 * 提示信息对话框交互描述类
 * 
 * @author guoweic
 *
 */
public class MessageInteractionInfo extends InteractionInfo {

	private static final long serialVersionUID = 7575316025769621950L;

	private String msg = null;
	
	private boolean okReturn;
	
	private String btnText;

	public MessageInteractionInfo(String id, String msg, boolean okReturn) {
		super(id, null);
		this.msg = msg;
		this.okReturn = okReturn;
	}
	
	public MessageInteractionInfo(String id, String msg, String title, String btnText, boolean okReturn) {
		super(id, title);
		this.msg = msg;
		this.okReturn = okReturn;
		this.btnText = btnText;
	}

	public String getType() {
		return MESSAGE_TYPE;
	}
	
	public String getTitle() {
		String title = super.getTitle();
		if(title == null || title.equals(""))
			return "提示对话框";
		return title;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isOkReturn() {
		return okReturn;
	}

	public void setOkReturn(boolean okReturn) {
		this.okReturn = okReturn;
	}

	public String getBtnText() {
		return btnText;
	}

	public void setBtnText(String btnText) {
		this.btnText = btnText;
	}

}
