package nc.uap.lfw.core.exception;

/**
 * ����Ի��򽻻�������
 * @author gd 2010-5-6
 * @version NC6.0
 */
public class InputInteractionInfo extends InteractionInfo {
	private static final long serialVersionUID = 8870535241477897545L;
	private InputItem[] items;
	private String type;
	
	public InputInteractionInfo(String id, InputItem[] items, String title)
	{
		super(id, title);
		this.items = items;
		type = getType();
	}
	
	@Override
	public String getMsg() {
		return null;
	}
	
	public String getType()
	{
		return INPUT_TYPE;
	}
	
	public String getTitle() {
		String title = super.getTitle();
		if(title == null || title.equals(""))
			return "����Ի���";
		return title;
	}

	public InputItem[] getItems() {
		return items;
	}

	public void setItems(InputItem[] items) {
		this.items = items;
	}
}
