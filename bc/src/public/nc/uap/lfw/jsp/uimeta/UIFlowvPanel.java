package nc.uap.lfw.jsp.uimeta;



public class UIFlowvPanel extends UILayoutPanel {
	private static final long serialVersionUID = -4186440303901937178L;
	public static final String HEIGHT = "height";
	public static final String ANCHOR = "anchor";

//	public static final String AUTO_FILL = "autoFill";

	public String getHeight() {
		return (String) getAttribute(HEIGHT);
	}

	public void setHeight(String height) {
		String oriHeight = getHeight();
		if(oriHeight == null || !oriHeight.equals(height)){
			setAttribute(HEIGHT, height);
			UpdatePair pair = new UpdatePair(HEIGHT, height);
			notifyChange(UPDATE, pair);
		}
		setAttribute(HEIGHT, height);
	}

	public String getAnchor() {
		return (String) getAttribute(ANCHOR);
	}

	public void setAnchor(String anchor) {
		setAttribute(ANCHOR, anchor);
	}
}
