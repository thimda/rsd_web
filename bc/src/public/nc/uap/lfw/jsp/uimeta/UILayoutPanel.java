package nc.uap.lfw.jsp.uimeta;


public class UILayoutPanel extends UIElement {
	private static final long serialVersionUID = 1013723661597693848L;
	public static final String LEFTPADDING = "leftPadding";
	public static final String RIGHTPADDING = "rightPadding";
	public static final String LEFTBORDER = "leftBorder";
	public static final String RIGHTBORDER = "rightBorder";
	public static final String TOPBORDER = "topBorder";
	public static final String BOTTOMBORDER = "bottomBorder";
	public static final String BORDER = "border";
	public static final String TOPPADDING = "topPadding";
	public static final String BOTTOMPADDING = "bottomPadding";
	private UILayout layout;
	private UIElement element;
	public UIElement getElement() {
		return element;
	}
	
	public void setElement(UIElement element) {
		this.element = element;
		super.addElement(element);
	}

	@Override
	public void removeElement(UIElement ele) {
		super.removeElement(ele);
		this.element = null;
	}

	@Override
	public String getWidgetId() {
		String widgetId = (String) getAttribute(WIDGET_ID);
		if(widgetId == null && layout != null){
			return layout.getWidgetId();
		}
		return widgetId;
	}

//	@Override
//	public Object createSelf() {
//		return super.createSelf();
//	}
//
//	@Override
//	public void updateSelf() {
//		super.updateSelf();
//	}
//
//	@Override
//	public void destroySelf() {
//		super.destroySelf();
//	}
	
	@Override
	public UILayoutPanel doClone() {
		UILayoutPanel panel = (UILayoutPanel)super.doClone();
		if(this.element != null){
			panel.element = (UIElement) this.element.doClone();
		}
		return panel;
	}
	
	public String getLeftPadding() {
		return (String) getAttribute(LEFTPADDING);
	}
	
	public String getRightPadding() {
		return (String) getAttribute(RIGHTPADDING);
	}
	
	public void setLeftPadding(String leftPadding) {
		setAttribute(LEFTPADDING, leftPadding);
	}
	
	public void setRightPadding(String rightPadding) {
		setAttribute(RIGHTPADDING, rightPadding);
	}
	
	public void setLeftBorder(String leftPadding){
		setAttribute(LEFTBORDER, leftPadding);
	}
	
	public void setRightBorder(String rightPadding){
		setAttribute(RIGHTBORDER, rightPadding);
	}
	
	public void setTopBorder(String leftPadding){
		setAttribute(TOPBORDER, leftPadding);
	}
	
	public void setBottomBorder(String rightPadding){
		setAttribute(BOTTOMBORDER, rightPadding);
	}
	
	public String getLeftBorder() {
		return (String) getAttribute(LEFTBORDER);
	}
	public String getRightBorder() {
		return (String) getAttribute(RIGHTBORDER);
	}
	public String getBottomBorder() {
		return (String) getAttribute(BOTTOMBORDER);
	}
	public String getTopBorder() {
		return (String) getAttribute(TOPBORDER);
	}
	
	public void setBorder(String rightPadding){
		setAttribute(BORDER, rightPadding);
	}
	
	public String getBorder() {
		return (String) getAttribute(BORDER);
	}

	public String getBottomPadding() {
		return (String) getAttribute(BOTTOMPADDING);
	}
	
	public String getTopPadding() {
		return (String) getAttribute(TOPPADDING);
	}
	
	public void setBottomPadding(String bottomPadding) {
		setAttribute(BOTTOMPADDING, bottomPadding);
	}
	
	public void setTopPadding(String topPadding) {
		setAttribute(TOPPADDING, topPadding);
	}

	public UILayout getLayout() {
		return layout;
	}

	public void setLayout(UILayout layout) {
		this.layout = layout;
	}
}
