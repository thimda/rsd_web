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
 * 边框标签
 * @author guoweic
 * 2009-10-16
 */
public class BorderTag extends WebElementTag implements
		IContainerElementTag {
	
	// 边框基础字符串
	protected static final String BORDER_ID_BASE = "border_";

	// 布局ID
	private String id;
	
	// 生成DIV的ID
	private String divId;
	
	private String widget;
	
	private String color = LfwRuntimeEnvironment.getTheme().getThemeElement(LfwTheme.LFW_BORDER_COLOR);
	private String leftColor = null;
	private String rightColor = null;
	private String topColor = null;
	private String bottomColor = null;

	private int width = 0;
	private int leftWidth = -1;
	private int rightWidth = -1;
	private int topWidth = -1;
	private int bottomWidth = -1;
	
	private boolean showLeft = true;
	private boolean showRight = true;
	private boolean showTop = true;
	private boolean showBottom = true;
	
	// div的class属性
	private String className = null;

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

	public String generateHead() {
		StringBuffer head = new StringBuffer();
		if (id == null || id.equals("")) {
			id = (String)LfwRuntimeEnvironment.getWebContext().getAttribute("$borderId");
			if (id == null) {
				id = "0";
			} else {
				id = String.valueOf(Integer.parseInt(id) + 1);
			}
		}
		LfwRuntimeEnvironment.getWebContext().setAttribute("$borderId", id);
		divId = DIV_PRE + BORDER_ID_BASE + (widget == null || widget.equals("") ? "" : (widget + "_")) + id;
		
		head.append("<div id=\"")
			.append(divId)
			.append("\" style=\"width:100%;height:100%;overflow:hidden;\">\n");
		
		head.append("<div id=\"")
			.append(divId)
			.append("_top\" style=\"overflow:hidden;width:100%;");
		if (showTop && (topWidth > 0)) {
			head.append("height:")
				.append(topWidth)
				.append("px;")
				.append("background:")
				.append(topColor == null ? color : topColor)
				.append(";\"></div>\n");
		} else if (showTop && width > 0) {
			head.append("height:")
				.append(width)
				.append("px;")
				.append("background:")
				.append(topColor == null ? color : topColor)
				.append(";\"></div>\n");
		} else {
			head.append("height:")
			.append(0)
			.append("px;")
			.append("\"></div>\n");
		}
		
		head.append("<div id=\"")
			.append(divId)
			.append("_middle\" style=\"width:100%;clear:both;overflow:hidden;\">\n");
			
		head.append("<div id=\"")
			.append(divId)
			.append("_left\" style=\"float:left;height:100%;");
		if (showLeft && (leftWidth > 0)) {
			head.append("width:")
				.append(leftWidth)
				.append("px;")
				.append("background:")
				.append(leftColor == null ? color : leftColor)
				.append(";\"></div>\n");
		} else if (showLeft && width > 0) {
			head.append("width:")
				.append(width)
				.append("px;")
				.append("background:")
				.append(leftColor == null ? color : leftColor)
				.append(";\"></div>\n");
		} else {
			head.append("width:")
			.append(0)
			.append("px;")
			.append("\"></div>\n");
		}
		
		head.append("<div id=\"")
			.append(divId)
			.append("_center\" style=\"float:left;height:100%;\">\n");
		
		head.append("<div id=\"")
			.append(divId)
			.append("_inner\" style=\"width:100%;height:100%;overflow:auto;\"");
		if (className != null && !className.equals("")) {
			head.append(" class=\"")
				.append(className)
				.append("\" >\n");
		} else {
			head.append(">\n");
		}
		
		return head.toString();
	}

	public String generateHeadScript() {
		return null;
	}

	public String generateTail() {
		StringBuffer tail = new StringBuffer();
		
		tail.append("</div>\n")
			.append("</div>\n");
		
		tail.append("<div id=\"")
			.append(divId)
			.append("_right\" style=\"float:right;height:100%;");
		if (showRight && (rightWidth > 0)) {
			tail.append("width:")
				.append(rightWidth)
				.append("px;")
				.append("background:")
				.append(rightColor == null ? color : rightColor)
				.append(";\"></div>\n");
		} else if (showRight && width > 0) {
			tail.append("width:")
				.append(width)
				.append("px;")
				.append("background:")
				.append(rightColor == null ? color : rightColor)
				.append(";\"></div>\n");
		} else {
			tail.append("width:")
			.append(0)
			.append("px;")
			.append("\"></div>\n");
		}
		
		tail.append("</div>\n");
	
		tail.append("<div id=\"")
			.append(divId)
			.append("_bottom\" style=\"overflow:hidden;width:100%;");
		if (showBottom && (bottomWidth > 0)) {
			tail.append("height:")
				.append(bottomWidth)
				.append("px;")
				.append("background:")
				.append(bottomColor == null ? color : bottomColor)
				.append(";\"></div>\n");
		} else if (showBottom && width > 0) {
			tail.append("height:")
				.append(width)
				.append("px;")
				.append("background:")
				.append(bottomColor == null ? color : bottomColor)
				.append(";\"></div>\n");
		} else {
			tail.append("height:")
			.append(0)
			.append("px;")
			.append("\"></div>\n");
		}
		tail.append("</div>\n");
		
		
		return tail.toString();
	}

	public String generateTailScript() {
		StringBuffer buf = new StringBuffer();
		buf.append("addResizeEvent($ge(\"")
		   .append(divId)
		   .append("\"), borderResize);\n")
		   .append("borderResize.call($ge(\"")
		   .append(divId)
		   .append("\"));\n");
		return buf.toString();
	}

	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setColor(String color) {
		this.color = color;
	}

	public void setLeftColor(String leftColor) {
		this.leftColor = leftColor;
	}

	public void setRightColor(String rightColor) {
		this.rightColor = rightColor;
	}

	public void setBottomColor(String bottomColor) {
		this.bottomColor = bottomColor;
	}

	public void setTopColor(String topColor) {
		this.topColor = topColor;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setLeftWidth(int leftWidth) {
		this.leftWidth = leftWidth;
	}

	public void setRightWidth(int rightWidth) {
		this.rightWidth = rightWidth;
	}

	public void setTopWidth(int topWidth) {
		this.topWidth = topWidth;
	}

	public void setBottomWidth(int bottomWidth) {
		this.bottomWidth = bottomWidth;
	}

	public void setShowLeft(boolean showLeft) {
		this.showLeft = showLeft;
	}

	public void setShowRight(boolean showRight) {
		this.showRight = showRight;
	}

	public void setShowTop(boolean showTop) {
		this.showTop = showTop;
	}

	public void setShowBottom(boolean showBottom) {
		this.showBottom = showBottom;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return null;
	}

	public String getWidget() {
		return widget;
	}

	public void setWidget(String widget) {
		this.widget = widget;
	}

}
