package nc.uap.lfw.jsp.uimeta;

public class UICardPanel extends UILayoutPanel {
	private static final long serialVersionUID = 4116930870463526713L;
	public String getId() {
		return (String) getAttribute(ID);
	}
	public void setId(String id) {
		setAttribute(ID, id);
	}
}
