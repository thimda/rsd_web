package nc.uap.lfw.jsp.uimeta;


/**
 * 
 */
public class UIDiv extends UILayout {
	private static final long serialVersionUID = 1L;
	public static final String STYLE = "style";

	public String getStyle() {
		return (String) getAttribute(STYLE);
	}

	public void setStyle(String style) {
		setAttribute(STYLE, style);
	}

}
