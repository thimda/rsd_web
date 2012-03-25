package nc.uap.lfw.jsp.uimeta;


public class UIImageComp extends UIComponent {
	private static final long serialVersionUID = 1L;
	private static final String IMAGEHEIGHT = "image_height";
	private static final String IMAGEWIDTH = "image_width";
	public UIImageComp(){
		setImageWidth("100%");
		setImageHeight("100%");
	}
	
	private void setImageWidth(String width) {
		this.setAttribute(IMAGEWIDTH, width);
	}

	private void setImageHeight(String height) {
		this.setAttribute(IMAGEHEIGHT, height);
	}
	
	public String getImageWidth(){
		return (String) getAttribute(IMAGEWIDTH);
	}
	
	public String getImageHeight(){
		return (String) getAttribute(IMAGEHEIGHT);
	}
}
