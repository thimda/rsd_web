package nc.uap.lfw.core.tags;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * FLOW-V 布局标签
 * @author guoweic
 * 2009-10-15
 */
public class BorderLayoutTag extends WebElementTag implements
		IContainerElementTag {
	
	// Border 布局ID基础字符串
	protected static final String BORDER_ID_BASE = "border_layout_";
	
	// 布局ID
	private String id;
	
	// 生成DIV的ID
	private String divId;
	
	private String widget;
	
	// 用来标识BorderLayout中是否有左右
	protected static final String PARTS_LEFT = "PARTS_LEFT";
	protected static final String PARTS_RIGHT = "PARTS_RIGHT";
	protected static final String PARTS_CENTER = "PARTS_CENTER";
	protected static final String PARTS_TOP = "PARTS_TOP";
	protected static final String PARTS_BOTTOM = "PARTS_BOTTOM";

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
		
		if (getAttribute(PARTS_CENTER) == null)
			throw new LfwRuntimeException("center panel can not be null for BorderLayout");
		
		// 渲染子项
		StringBuffer buf = new StringBuffer();
		// 渲染 Top Div
		if (getAttribute(PARTS_TOP) != null) {
			buf.append(getAttribute(PARTS_TOP));
		} 
//		else {
//			buf.append("<div id=\"")
//				.append(divId)
//				.append("_top\" style=\"width:100%;height:0px;\"></div>\n");
//		}
		// 渲染 Middle Div
		buf.append("<div id=\"")
			.append(divId)
			.append("_middle\" style=\"width:100%;clear:both;overflow:hidden;\">\n");
		// 渲染 Left Div
		if (getAttribute(PARTS_LEFT) != null) {
			buf.append(getAttribute(PARTS_LEFT));
		} 
//		else {
//			buf.append("<div id=\"")
//				.append(divId)
//				.append("_left\" style=\"width:0px;height:100%;float:left;\"></div>\n");
//		}
		// 渲染 Center Div
		buf.append(getAttribute(PARTS_CENTER));
		if (getAttribute(PARTS_RIGHT) != null) {
			buf.append(getAttribute(PARTS_RIGHT));
		} 
//		else {
//			buf.append("<div id=\"")
//				.append(divId)
//				.append("_right\" style=\"width:0px;height:100%;float:right;\"></div>\n");
//		}
		// 结束 Middle Div
		buf.append("</div>\n");
		// 渲染 Bottom Div
		if (getAttribute(PARTS_BOTTOM) != null) {
			buf.append(getAttribute(PARTS_BOTTOM));
		} 
//		else {
//			buf.append("<div id=\"")
//				.append(divId)
//				.append("_bottom\" style=\"width:100%;height:0px;clear:both;\"></div>\n");
//		}
		out.write(buf.toString());
		
		out.write(generateTail());
		
	}

	public String generateHead() {
		StringBuffer head = new StringBuffer();
		if (id == null || id.equals("")||id.equalsIgnoreCase("null")) {
			id = (String)LfwRuntimeEnvironment.getWebContext().getAttribute("$borderLayoutId");
			if (id == null) {
				id = "0";
			} else {
				id = String.valueOf(Integer.parseInt(id) + 1);
			}
		}
		LfwRuntimeEnvironment.getWebContext().setAttribute("$borderLayoutId", id);
		divId = DIV_PRE + BORDER_ID_BASE + (widget == null || widget.equals("") ? "" : (widget + "_")) + id;
		
		head.append("<div id=\"")
			.append(divId)
			.append("\" style=\"width:100%;height:100%;overflow:hidden;\">\n");
		
		return head.toString();
	}

	public String generateHeadScript() {
		return null;
	}

	public String generateTail() {
		return "</div>\n";
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
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getDivId() {
		return divId;
	}

	@Override
	protected String getSourceType(WebElement ele) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getWidget() {
		return widget;
	}

	public void setWidget(String widget) {
		this.widget = widget;
	}

}
