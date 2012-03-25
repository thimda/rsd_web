package nc.uap.lfw.jsp.uimeta;


public class UIGridRowPanel extends UILayoutPanel {
	private static final long serialVersionUID = 2047434545903659116L;
	private UIGridRowLayout row;
	
	public UIGridRowPanel(UIGridRowLayout row){
		setRow(row);
	}
	
	public UIGridRowLayout getRow() {
		return (UIGridRowLayout) getElement();
	}
	
	public void setRow(UIGridRowLayout row) {
		setElement(row);
	}
}
