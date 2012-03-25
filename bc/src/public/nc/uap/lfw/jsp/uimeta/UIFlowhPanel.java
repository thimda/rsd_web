package nc.uap.lfw.jsp.uimeta;



public class UIFlowhPanel extends UILayoutPanel {
	private static final long serialVersionUID = 3063729814443707258L;
	public static final String WIDTH = "width";

	public void setWidth(String width) {
		String oriWidth = getWidth();
		if(oriWidth == null || !oriWidth.equals(width)){
			setAttribute(WIDTH, width);
			UpdatePair pair = new UpdatePair(WIDTH, width);
			notifyChange(UPDATE, pair);
		}
	}

	public String getWidth() {
		return (String) getAttribute(WIDTH);
	}
}
