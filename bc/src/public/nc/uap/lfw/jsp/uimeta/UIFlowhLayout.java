package nc.uap.lfw.jsp.uimeta;



public class UIFlowhLayout extends UILayout {
	private static final long serialVersionUID = 2772229643528375039L;
	public static final String AUTO_FILL = "autoFill";

	public UIFlowhLayout(){
		setAutoFill(UIConstant.FALSE);
	}
	
	public Integer getAutoFill() {
		return (Integer) getAttribute(AUTO_FILL);
	}

	public void setAutoFill(Integer autoFill) {
		setAttribute(AUTO_FILL, autoFill);
	}
}
