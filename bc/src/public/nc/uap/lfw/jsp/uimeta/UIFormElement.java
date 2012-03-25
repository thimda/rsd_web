package nc.uap.lfw.jsp.uimeta;

public class UIFormElement extends UIComponent {

	private static final long serialVersionUID = 1L;
	
	public static final String FORM_ID = "form_id";
	private static final String TYPE = "type";
	
	public static final String ELEWIDTH = "eleWidth";
	
	public UIFormElement(){
		setWidth("200");
		setHeight("23");
		setEleWidth("120");
	}
	
	public void setEleWidth(String width){
		this.setAttribute(ELEWIDTH, width);
		UpdatePair pair = new UpdatePair(ELEWIDTH, width);
		notifyChange(UPDATE, pair);
	}
	
	public String getEleWidth(){
		return (String) this.getAttribute(ELEWIDTH);
	}
	
	public void setFormId(String formId){
		this.setAttribute(FORM_ID, formId);
	}
	
	public String getFormId(){
		return (String)this.getAttribute(FORM_ID);
	}
	
	public void setElementType(String elementType){
		this.setAttribute(TYPE, elementType);
	}
	
	public String getElementType(){
		return (String)this.getAttribute(TYPE);
	}
	
}
