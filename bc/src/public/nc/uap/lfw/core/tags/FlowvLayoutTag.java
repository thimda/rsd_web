package nc.uap.lfw.core.tags;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.WebElement;

/**
 * FLOW-V 布局标签
 * @author guoweic
 * 2009-10-15
 */
public class FlowvLayoutTag extends WebElementTag implements
		IContainerElementTag {
	
	// FLOW-V 布局ID基础字符串
	protected static final String FLOW_V_ID_BASE = "flowv_layout_";
	
	// 布局ID
	private String id;
	
	// 生成DIV的ID
	private String divId;
	
	private String widget;
	
	// 没设置高度子项是否自动填充
	private boolean autoFill = true;
	
	// 子项个数（用来为子项设定默认ID）
	protected int childCount = 0;

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
		String head = null;
		if (id == null || "null".equals(id)) {
			id = (String)LfwRuntimeEnvironment.getWebContext().getAttribute("$flowvId");
			if (id == null) {
				id = "0";
			} else {
				id = String.valueOf(Integer.parseInt(id) + 1);
			}
			LfwRuntimeEnvironment.getWebContext().setAttribute("$flowvId", id);
		}
//		divId = getDivShowId();//DIV_PRE + FLOW_V_ID_BASE + id;
		divId = DIV_PRE + (widget == null || widget.equals("") ? "" : (widget + "_")) + id;
		if (autoFill == false)  // 子项自动填充
			head = "<div id=\"" + divId + "\" style=\"width:100%;height:100%;overflow-y:auto;overflow-x:hidden;position:relative;\">\n";
		else
			head = "<div id=\"" + divId + "\" style=\"width:100%;height:100%;overflow:hidden;position:relative;\">\n";
		return head;
	}

	public String generateHeadScript() {
		return null;
	}

	public String generateTail() {
		return "</div>\n";
	}
	
	public String generateTailScript() {
		StringBuffer buf = new StringBuffer();
		if (autoFill) {  // 子项自动填充
			buf.append("addResizeEvent($ge(\"")
			   .append(divId)
			   .append("\"), flowvResize);\n")
			   .append("flowvResize.call($ge(\"")
			   .append(divId)
			   .append("\"));\n");
		}
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
		return null;
	}

	public boolean isAutoFill() {
		return autoFill;
	}

	public void setAutoFill(boolean autoFill) {
		this.autoFill = autoFill;
	}

	public String getWidget() {
		return widget;
	}

	public void setWidget(String widget) {
		this.widget = widget;
	}

}
