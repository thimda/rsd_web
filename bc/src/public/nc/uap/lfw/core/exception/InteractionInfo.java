package nc.uap.lfw.core.exception;

import java.io.Serializable;

/**
 * ������Ϣ������
 * @author gd 2010-4-20
 * @version NC6.0
 */
public abstract class InteractionInfo implements Serializable{
	private static final long serialVersionUID = -3112668953948934769L;
	// 3���Ի���
	public String THREE_BUTTONS_TYPE = "THREE_BUTTONS_DIALOG";
	// ȷ�϶Ի���
	public String OK_CANCEL_TYPE = "OKCANCEL_DIALOG";
	// ����Ի���
	public String INPUT_TYPE = "INPUT_DIALOG";
	// ��Ϣ�Ի���
	public String MESSAGE_TYPE = "MESSAGE_DIALOG";
	// ���������
	private String title;
	
	private String id;
	
	public InteractionInfo(String id, String title){
		this.title = title;
		this.id = id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getId() {
		return this.id;
	}
	
	// �Ի������
	public String getTitle()
	{
		return this.title;
	}
	
	// ������ʾ��Ϣ
	public abstract String getMsg();
	
	// ��������
	public abstract String getType();
}
