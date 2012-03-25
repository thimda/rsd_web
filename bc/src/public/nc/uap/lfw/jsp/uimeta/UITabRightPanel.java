package nc.uap.lfw.jsp.uimeta;


/**
 * 默认为tabcomp右侧设置的panel
 * 
 * @author zhangxya
 * 
 */
public class UITabRightPanel extends UILayoutPanel {
	private static final long serialVersionUID = 1L;

	public static final String WIDTH = "width";

	public String getWidth() {
		return (String) getAttribute(WIDTH);
	}

	public void setWidth(String width) {
		setAttribute(WIDTH, width);
	}


}
