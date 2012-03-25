package nc.uap.lfw.core.tags;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * FLOW-H 布局子项标签
 * @author guoweic
 * 2009-10-15
 */
public class BorderPanelTag extends WebElementTag implements IContainerElementTag {
	
	private static final String TOP = "top";
	private static final String BOTTOM = "bottom";
	private static final String LEFT = "left";
	private static final String RIGHT = "right";
	private static final String CENTER = "center";
	
	// 高度
	private String height;
	
	// 宽度
	private String width;
	
	// 位置
	private String position;

	// 生成DIV的ID
	private String divId;

	// 父Tag对象
	private BorderLayoutTag parentTag = null;
	
	protected void doRender() throws JspException, IOException {
		parentTag = (BorderLayoutTag) findAncestorWithClass(this, BorderLayoutTag.class);
		if(parentTag == null)
			throw new LfwRuntimeException("this tag must be included in BorderLayoutTag");
		
		StringWriter writer = new StringWriter();
		writer.write(generateHead());
		String script = generateHeadScript();
		addToBodyScript(script);
		JspFragment body = getJspBody();
		if(body != null)
			body.invoke(writer);
		writer.write(generateTail());
		script = generateTailScript();
		addToBodyScript(script);
		
		// Border Layout由于需要根据东西的Panel来决定中间的Panel所占列数，所以需要缓画
		if (position == null) {
			throw new LfwRuntimeException("attritute position must be asigned explicitedly");
		}
		if (position.equals(TOP)) {
			parentTag.setAttribute(BorderLayoutTag.PARTS_TOP, writer.getBuffer().toString());
		} else if (position.equals(BOTTOM)) {
			parentTag.setAttribute(BorderLayoutTag.PARTS_BOTTOM, writer.getBuffer().toString());
		} else if (position.equals(LEFT)) {
			parentTag.setAttribute(BorderLayoutTag.PARTS_LEFT, writer.getBuffer().toString());
		} else if (position.equals(RIGHT)) {
			parentTag.setAttribute(BorderLayoutTag.PARTS_RIGHT, writer.getBuffer().toString());
		} else if (position.equals(CENTER)) {
			parentTag.setAttribute(BorderLayoutTag.PARTS_CENTER, writer.getBuffer().toString());
		}

	}

	public String generateHead() {
		divId = parentTag.getDivId() + "_" + position;
		StringBuffer strBuf = new StringBuffer();
		if (position.equals(TOP) || position.equals(BOTTOM)) {
			if (height == null) {
				throw new LfwRuntimeException("attritute height must be asigned explicitedly in the top or bottom panel");
			} else {
				strBuf.append("<div id=\"" + divId + "\" style=\"height:" + height + ";width:100%;\">\n");
			}
		} else if (position.equals(LEFT) || position.equals(RIGHT)) {
			if (width == null) {
				throw new LfwRuntimeException("attritute width must be asigned explicitedly in the left or right panel");
			} else {
				if (position.equals(LEFT)) {
					strBuf.append("<div id=\"" + divId + "\" style=\"width:" + width + ";height:100%;float:left;\">\n");
				} else {
					strBuf.append("<div id=\"" + divId + "\" style=\"width:" + width + ";height:100%;float:right;\">\n");
				}
			}
		} else if (position.equals(CENTER)) {
			strBuf.append("<div id=\"" + divId + "\" style=\"height:100%;float:left;\">\n");
		}
		return strBuf.toString();
	}

	public String generateHeadScript() {
		return "";
	}

	public String generateTail() {
		return "</div>\n";
	}

	public String generateTailScript() {
		return "";
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = getFormatSize(height);
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = getFormatSize(width);
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return null;
	}

}
