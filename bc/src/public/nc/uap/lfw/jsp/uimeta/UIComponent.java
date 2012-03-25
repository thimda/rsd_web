package nc.uap.lfw.jsp.uimeta;

import nc.uap.lfw.core.exception.LfwRuntimeException;

public class UIComponent extends UIElement {
	private static final long serialVersionUID = 8431336052516488514L;
	public static final String ALIGN = "align";
	public static final String ALIGN_LEFT = "left";
	public static final String ALIGN_RIGHT = "right";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String TOP = "top";
	public static final String LEFT = "left";
	public static final String POSITION = "position";
	public UIComponent(String width, String height){
		this("0", "0", width, height);
	}
	
	public UIComponent(String left, String top, String width, String height){
		setTop(top);
		setLeft(left);
		setWidth(width);
		setHeight(height);
		setPosition("relative");
	}
	
	public UIComponent(){
		this("100%", "100%");
	}
	
	public void setHeight(String height){
		setAttribute(HEIGHT, height);
		UpdatePair pair = new UpdatePair(HEIGHT, height);
		notifyChange(UPDATE, pair);
	}
	
	public String getHeight(){
		return (String) getAttribute(HEIGHT);
	}
	
	public void setWidth(String width){
		setAttribute(WIDTH, width);
		UpdatePair pair = new UpdatePair(WIDTH, width);
		notifyChange(UPDATE, pair);
	}
	
	public String getWidth(){
		return (String) getAttribute(WIDTH);
	}
	
	
	public void setTop(String top){
		setAttribute(TOP, top);
	}
	
	public String getTop(){
		return (String) getAttribute(TOP);
	}
	
	public void setLeft(String left){
		setAttribute(LEFT, left);
	}
	
	public String getLeft(){
		return (String) getAttribute(LEFT);
	}
	
	
	public void setPosition(String position){
		setAttribute(POSITION, position);
	}
	
	public String getPosition(){
		return (String) getAttribute(POSITION);
	}
	
	public String getAlign() {
		return (String) getAttribute(ALIGN);
	}

	public void setAlign(String align) {
		setAttribute(ALIGN, align);
		UpdatePair pair = new UpdatePair(ALIGN, align);
		notifyChange(UPDATE, pair);
	}

	@Override
	public void addElement(UIElement ele) {
		throw new LfwRuntimeException("错误的方法调用，不能添加子元素！");
	}

	@Override
	public void removeElement(UIElement ele) {
		throw new LfwRuntimeException("错误的方法调用，不能删除子元素！");
	}

	@Override
	public UIComponent doClone() {
		UIComponent comp = (UIComponent)super.doClone();		
		return comp;
	}
	
}
