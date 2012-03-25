package nc.uap.lfw.core.exception;


/**
 * ȷ�ϡ�ȡ���Ի��򽻻�������
 * @author gd 2010-4-20
 * @version NC6.0
 */
public class OkCancelInteractionInfo extends InteractionInfo {
	private static final long serialVersionUID = 6096002023859452108L;
	// �Ի�����ʾ���
	private String msg;
	private String okText;
	private String cancelText;
	
	public OkCancelInteractionInfo(String dialogId, String title, String msg)
	{
		super(dialogId, title);
		this.msg = msg;
	}
	
	public OkCancelInteractionInfo(String dialogId, String msg, String title, String btnText)
	{
		super(dialogId, title);
		this.msg = msg;
		this.okText = btnText;
	}
	
	public OkCancelInteractionInfo(String dialogId, String msg, String title, String okText, String cancelText)
	{
		super(dialogId, title);
		this.msg = msg;
		this.okText = okText;
		this.cancelText = cancelText;
	}
	
	
	
	public String getTitle() {
		String title = super.getTitle();
		if(title == null || title.equals(""))
			return "ȷ�϶Ի���";
		return title;
	}

	public String getType() {
		return OK_CANCEL_TYPE;
	}

	public String getMsg() {
		return msg;
	}

	public String getOkText() {
		return okText;
	}

	public void setOkText(String okText) {
		this.okText = okText;
	}

	public String getCancelText() {
		return cancelText;
	}

	public void setCancelText(String cancelText) {
		this.cancelText = cancelText;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


}
