package nc.uap.lfw.core.exception;

import java.io.Serializable;

/**
 * 交互信息描述类
 * @author gd 2010-4-20
 * @version NC6.0
 */
public abstract class InteractionInfo implements Serializable{
	private static final long serialVersionUID = -3112668953948934769L;
	// 3键对话框
	public String THREE_BUTTONS_TYPE = "THREE_BUTTONS_DIALOG";
	// 确认对话框
	public String OK_CANCEL_TYPE = "OKCANCEL_DIALOG";
	// 输入对话框
	public String INPUT_TYPE = "INPUT_DIALOG";
	// 信息对话框
	public String MESSAGE_TYPE = "MESSAGE_DIALOG";
	// 交互框标题
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
	
	// 对话框标题
	public String getTitle()
	{
		return this.title;
	}
	
	// 交互提示信息
	public abstract String getMsg();
	
	// 交互类型
	public abstract String getType();
}
