package nc.uap.lfw.core.tags;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.LfwTheme;
import nc.uap.lfw.core.comp.WebElement;

/**
 * @author guoweic
 *
 */
public class PanelTag extends WebElementTag implements IContainerElementTag {

	private String id;

	private String left;
	private String top;
	private String width;
	private String height;
	private String position;
	private boolean scroll = false;
	private String className;
	
	private boolean display = true;
	private boolean visibility = true;
	private boolean transparent = false;
	
	private String backgroundColor;

	
	private String paddingLeft = "0";
	private String paddingTop = "0";
	private String paddingRight = "0";
	private String paddingBottom = "0";
	
	// 是否为圆角
	private boolean roundRect = false;
	// 圆角半径
	private String radius = "5";
	
	// 是否有边框
	private boolean withBorder = false;
	// 边框宽度
	private String borderWidth = "1";
	// 边框颜色
	private String borderColor = LfwRuntimeEnvironment.getTheme().getThemeElement(LfwTheme.LFW_BORDER_COLOR);
	
	protected void doRender() throws JspException, IOException {
		JspContext jspContext = getJspContext();
		JspWriter out = jspContext.getOut();
		out.write(generateHead());
		// 渲染子项之前加入脚本
		String script = generateTailScript();
		addToBodyScript(script);
		JspFragment body = getJspBody();
		if(body != null)
			body.invoke(out);
		out.write(generateTail());
	}

	protected String getSourceType(WebElement ele) {
		return null;
	}

	public String generateHead() {
		StringBuffer head = new StringBuffer();
		String parentDivId = getDivShowId();
		String contentDivId = parentDivId + "_content";
		
		head.append("<div id=\"")
			.append(parentDivId)
			.append("\" style=\"width:100%;height:100%;overflow:hidden;\"")
			.append(">")
			.append("<div id=\"")
			.append(contentDivId)
			.append("\" style=\"width:100%;height:100%;overflow:")
			.append(scroll ? "auto" : "hidden")
			.append(";\"")
			.append(">\n");
		
		return head.toString();
	}

	public String generateHeadScript() {
		return null;
	}

	public String generateTail() {
		return "</div></div>\n";
	}

	public String generateTailScript() {
		String parentDivId = getDivShowId();
		String contentDivId = parentDivId + "_content";
		StringBuffer buf = new StringBuffer();
		String showId = COMP_PRE + id;
		buf.append("var ")
			.append(showId)
			.append(" = new PanelComp(")
			.append("document.getElementById(\"")
			.append(parentDivId)
			.append("\"), \"")
			.append(id)
			.append("\", \"")
			.append(left == null ? "0px" : left)
			.append("\", \"")
			.append(top == null ? "0px" : top)
			.append("\", \"")
			.append(width == null ? "100%" : width)
			.append("\", \"")
			.append(height == null ? "100%" : height)
			.append("\", \"")
			.append(position == null ? "relative" : position)
			.append("\", ")
			.append(scroll)
			.append(", ")
			.append(className == null ? null : "\"" + className + "\"")
			.append(", ")
			.append(transparent)
			.append(", \"")
			.append(backgroundColor == null ? "" : backgroundColor)
			.append("\"");
			
		if (roundRect || withBorder) {
			buf.append(",{")
			   .append("isRoundRect:")
			   .append(roundRect)
			   .append(",radius:")
			   .append(radius)
			   .append(",withBorder:")
			   .append(withBorder)
			   .append(",borderWidth:")
			   .append(borderWidth)
			   .append(",borderColor:'")
			   .append(borderColor)
			   .append("'}");
		}
			
		buf.append(");\n");
		
		
		
		buf.append(showId)
			.append(".setContent(document.getElementById(\"")
			.append(contentDivId)
			.append("\"));\n");

		buf.append(showId)
			.append(".setDisplay(")
			.append(display)
			.append(");\n");
		
		buf.append(showId)
			.append(".setVisible(")
			.append(visibility)
			.append(");\n");
		
		buf.append(showId)
			.append(".setPadding('")
			.append(paddingLeft)
			.append("','")
			.append(paddingTop)
			.append("','")
			.append(paddingRight)
			.append("','")
			.append(paddingBottom)
			.append("');\n");

		if (this.getWidget() != null) {
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append(widget + ".addPanel(" + showId + ");\n");
		}
		else
			buf.append("pageUI.addPanel(" + showId + ");\n");
		
		return buf.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public boolean isScroll() {
		return scroll;
	}

	public void setScroll(boolean scroll) {
		this.scroll = scroll;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public boolean isVisibility() {
		return visibility;
	}

	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public void setPaddingLeft(String paddingLeft) {
		this.paddingLeft = paddingLeft;
	}

	public void setPaddingTop(String paddingTop) {
		this.paddingTop = paddingTop;
	}

	public void setPaddingRight(String paddingRight) {
		this.paddingRight = paddingRight;
	}

	public void setPaddingBottom(String paddingBottom) {
		this.paddingBottom = paddingBottom;
	}

	@Override
	protected String getDivShowId() {
		return super.getDivShowId();
	}

	public void setRoundRect(boolean roundRect) {
		this.roundRect = roundRect;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public void setWithBorder(boolean withBorder) {
		this.withBorder = withBorder;
	}

	public void setBorderWidth(String borderWidth) {
		this.borderWidth = borderWidth;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}
}
