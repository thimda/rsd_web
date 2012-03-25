package nc.uap.lfw.core.tags;



/**
 * 分割面板Tag
 * @author dengjt, guoweic
 *
 */
public class SpliterTag extends ContainerElementTag {
	
	private String divideSize = "0.5";
	private String orientation = "v";
	private String boundMode = "%";
	private boolean oneTouch = false;
	private int spliterWidth = 4;
	private boolean inverse = false;
	private String id;
	// 是否隐藏拖动条
	private boolean hideBar = true;
	
	// 隐藏方向（true-向左/向上；false-向右/向下）
	private boolean hideDirection = true;
	
	public String generateHead() {
		StringBuffer buf = new StringBuffer();
		buf.append("<div id=\"")
//		   .append(DIV_PRE)
		   .append(getDivShowId())
		   .append("\" style=\"width:100%;height:100%;overflow:hidden;\">");
		return buf.toString();
	}

	public String generateHeadScript() {
		//SpliterComp(parent, name, top, left, width, height, position, prop, orientation, resizable, attrAry, className)
		StringBuffer buf = new StringBuffer();
		buf.append("window.")
//		   .append(COMP_PRE)
		   .append(getVarShowId())
		   .append(" = new SpliterComp(document.getElementById(\"")
//		   .append(DIV_PRE)
		   .append(getDivShowId())
		   .append("\"), \"")
//		   .append(getDivShowId())
		   .append(id)
		   .append("\", 0, 0, \"100%\", \"100%\", \"relative\", \"")
		   .append(divideSize)
		   .append("\", \"")
		   .append(orientation)
		   .append("\", ")
		   .append(oneTouch)
		   .append(", ")
		   .append("{spliterWidth:")
		   .append(spliterWidth)
		   .append(", boundMode:'")
		   .append(boundMode)
		   .append("', isInverse:")
		   .append(inverse);
		   // 反方向隐藏
		   if(!isHideDirection())
			   buf.append(", hideDirection:false");
		   // 不隐藏bar
		   if(!isHideBar())
			   buf.append(", hideBar:false");
		   buf.append("},null);\n");
//		StringBuffer scriptBuf = (StringBuffer) getJspContext().getAttribute(PageModelTag.DS_SCRIPT);
//		scriptBuf.append(COMP_PRE)
//				 .append(getId())
//				 .append(".fixeProp();\n");
		return buf.toString();
	}

	public String generateTail() {
		return "</div>";
	}

	public String generateTailScript() {
//		StringBuffer buf = (StringBuffer) getJspContext().getAttribute(PageModelTag.DS_SCRIPT);
//		buf.append(COMP_PRE)
//		   .append(getId())
//		   .append(".fixeProp();\n");
//		buf.append("$c_spliter1.fixeProp();\r\n" + 
//				"\r\n" + 
//				"\r\n" + 
//				"$c_spliter2.fixeProp();");
		
		
		return null;//buf.toString();
	}

	public String getBoundMode() {
		return boundMode;
	}

	public void setBoundMode(String boundMode) {
		this.boundMode = boundMode;
	}

	public String getDivideSize() {
		return divideSize;
	}

	public void setDivideSize(String divideSize) {
		this.divideSize = divideSize;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public boolean isOneTouch() {
		return oneTouch;
	}

	public void setOneTouch(boolean resizable) {
		this.oneTouch = resizable;
	}

	public int getSpliterWidth() {
		return spliterWidth;
	}

	public void setSpliterWidth(int spliterWidth) {
		this.spliterWidth = spliterWidth;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isInverse() {
		return inverse;
	}

	public void setInverse(boolean inverse) {
		this.inverse = inverse;
	}

	public boolean isHideBar() {
		return hideBar;
	}

	public void setHideBar(boolean hideBar) {
		this.hideBar = hideBar;
	}
	
	public boolean isHideDirection() {
		return hideDirection;
	}

	public void setHideDirection(boolean hideDirection) {
		this.hideDirection = hideDirection;
	}

}
