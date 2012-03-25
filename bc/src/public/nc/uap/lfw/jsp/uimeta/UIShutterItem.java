package nc.uap.lfw.jsp.uimeta;
public class UIShutterItem extends UILayoutPanel {
	private static final long serialVersionUID = 1498487520155539622L;
	public static final String TEXT = "text";
	public static final String I18NNAME = "i18nName";
	
	public void setId(String id) {
		setAttribute(ID, id);
	}

	public String getId() {
		return (String) getAttribute(ID);
	}

	public String getText() {
		return (String) getAttribute(TEXT);
	}

	public void setText(String text) {
		setAttribute(TEXT, text);
	}

	public String getI18nName() {
		return (String) getAttribute(I18NNAME);
	}

	public void setI18nName(String name) {
		setAttribute(I18NNAME, name);
	}
	

}
