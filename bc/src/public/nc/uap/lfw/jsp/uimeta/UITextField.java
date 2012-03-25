package nc.uap.lfw.jsp.uimeta;


public class UITextField extends UIComponent {
	private static final long serialVersionUID = 1656422141563952123L;
	public static final String TYPE = "type";
//	public static final String REFCODE="refcode";
	public static final String IMG_SRC = "imgsrc";
	
	public UITextField() {
		setWidth("120");
		setHeight("22");
	}
	public void setType(String type){
		setAttribute(TYPE, type);
	}
	
	public String getType(){
		return (String) getAttribute(TYPE);
	}
	
//	public String getRefcode() {
//		return (String) getAttribute(REFCODE);
//	}
//	public void setRefcode(String refcode) {
//		setAttribute(REFCODE, refcode);
//	}
	
	public void setImgsrc(String imgsrc){
		setAttribute(IMG_SRC, imgsrc);
	}
	
	public String getImgsrc(){
		return (String) getAttribute(IMG_SRC);
	}
	
}
