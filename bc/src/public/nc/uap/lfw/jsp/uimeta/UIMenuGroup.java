package nc.uap.lfw.jsp.uimeta;


public class UIMenuGroup extends UILayout {
	private static final long serialVersionUID = -2462390247359218359L;
	public String getId(){
		return (String) getAttribute(ID);
	}
	
	public void setId(String value){
		setAttribute(ID, value);
	}
}
