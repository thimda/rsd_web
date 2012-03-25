package nc.uap.lfw.jsp.uimeta;

public class UILabelComp  extends UIComponent {

	private static final long serialVersionUID = 1L;
	private static final String LABELHEIGHT = "label_height";
	private static final String LABELWIDTH = "label_width";
	public static final String TEXTALIGN = "textAlign";
	public UILabelComp(){
		this.setLabelHeight("22");
		this.setLabelWidth("100");
	}
	
	public void setLabelHeight(String height) {
		this.setAttribute(HEIGHT, height);
	}
	
	public void setLabelWidth(String width) {
		this.setAttribute(WIDTH, width);
	}
	
	public String getLabelWidth() {
		return (String) getAttribute(LABELWIDTH);
	}
	
	public String getLabelHeight() {
		return (String) getAttribute(LABELHEIGHT);
	}
	
	public void setTextAlign(String textAlign){
		this.setAttribute(TEXTALIGN, textAlign);
	}
	
	public String getTextAlign(){
		return (String) getAttribute(TEXTALIGN);
	}
	
}
