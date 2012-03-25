package nc.uap.lfw.jsp.uimeta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UIBorder extends UILayout {
	private static final long serialVersionUID = 3305526108711586464L;
	
	public static final String COLOR = "color";
	public static final String SHOWLEFT = "showLeft";
	public static final String SHOWRIGHT = "showRight";
	public static final String SHOWTOP = "showTop";
	public static final String SHOWBOTTOM = "showBottom";
	public static final String LEFTCOLOR = "leftColor";
	public static final String RIGHTCOLOR = "rightColor";
	public static final String TOPCOLOR = "topColor";
	public static final String BOTTOMCOLOR = "bottomColor";
	public static final String WIDTH = "width";
	public static final String LEFTWIDTH = "leftWidth";
	public static final String RIGHTWIDTH = "rightWidth";
	public static final String TOPWIDTH = "topWidth";
	public static final String BOTTOMWIDTH = "bottomWidth";
	public static final String CLASSNAME = "className";
	public static final String ROUNDBORDER = "roundBorder";
	
	public void setId(String id) {
		setAttribute(ID, id);
	}
	
	public String getId(){
		return (String) getAttribute(ID);
	}
	
	
	public void setWidgetId(String widgetId) {
		setAttribute(WIDGET_ID, widgetId);
	}
	
	public String getWidgetId(){
		return (String) getAttribute(WIDGET_ID);
	}
	
	public String getWidth() {
		return (String) getAttribute(WIDTH);
	}

	public void setWidth(String width) {
		setAttribute(WIDTH, width);
	}

	public String getLeftWidth() {
		return (String) getAttribute(LEFTWIDTH);
	}

	public void setLeftWidth(String leftWidth) {
		setAttribute(LEFTWIDTH, leftWidth);
	}

	public String getRightWidth() {
		return (String) getAttribute(RIGHTWIDTH);
	}

	public void setRightWidth(String rightWidth) {
		setAttribute(RIGHTWIDTH, rightWidth);
	}

	public String getTopWidth() {
		return (String) getAttribute(TOPWIDTH);
	}

	public void setTopWidth(String topWidth) {
		setAttribute(TOPWIDTH, topWidth);
	}

	public String getBottomWidth() {
		return (String) getAttribute(BOTTOMWIDTH);
	}

	public void setBottomWidth(String bottomWidth) {
		setAttribute(BOTTOMWIDTH, bottomWidth);
	}

	public String getLeftColor() {
		return (String) getAttribute(LEFTCOLOR);
	}

	public void setLeftColor(String leftColor) {
		setAttribute(LEFTCOLOR, leftColor);
	}

	public String getRightColor() {
		return (String) getAttribute(RIGHTCOLOR);
	}

	public void setRightColor(String rightColor) {
		setAttribute(RIGHTCOLOR, rightColor);
	}

	public String getTopColor() {
		return (String) getAttribute(TOPCOLOR);
	}

	public void setTopColor(String topColor) {
		setAttribute(TOPCOLOR, topColor);
	}

	public String getBottomColor() {
		return (String) getAttribute(BOTTOMCOLOR);
	}

	public void setBottomColor(String bottomColor) {
		setAttribute(BOTTOMCOLOR, bottomColor);
	}

	public String getClassName() {
		return (String) getAttribute(CLASSNAME);
	}

	public void setClassName(String className) {
		setAttribute(CLASSNAME, className);
	}
	
	public void setShowLeft(int showLeft) {
		setAttribute(SHOWLEFT, showLeft);
	}

	public void setShowRight(int showRight) {
		setAttribute(SHOWRIGHT, showRight);
	}

	public void setShowTop(int showTop) {
		setAttribute(SHOWTOP, showTop);
	}

	public void setShowBottom(int showBottom) {
		setAttribute(SHOWBOTTOM, showBottom);
	}
	
	public void setColor(String color) {
		setAttribute(COLOR, color);
	}
	
	
	public int getShowLeft() {
		return (Integer)getAttribute(SHOWLEFT);
	}

	public int getShowRight() {
		return (Integer)getAttribute(SHOWRIGHT);
	}

	public int getShowTop() {
		return (Integer)getAttribute(SHOWTOP);
	}

	public int getShowBottom() {
		return (Integer)getAttribute(SHOWBOTTOM);
	}
	
	public String getColor() {
		return (String)getAttribute(COLOR);
	}
	
	public int getRoundBorder() {
		Integer r = (Integer) getAttribute(ROUNDBORDER);
		return r == null ? 1 : r.intValue();
	}
	
	public void setRoundBorder(int round){
		setAttribute(ROUNDBORDER, round);
	}
	
		
	protected Map<String, Serializable> createAttrMap() {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put(SHOWLEFT, 0);
		map.put(SHOWRIGHT, 0);
		map.put(SHOWTOP, 0); 
		map.put(SHOWBOTTOM, 0); 
		return map;
	}
}
