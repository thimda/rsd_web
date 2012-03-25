package nc.uap.lfw.core.tags;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.WebElement;

/**
 * FLOW-V ���ֱ�ǩ
 * @author guoweic
 * 2009-10-15
 */
public class FlowvLayoutTag extends WebElementTag implements
		IContainerElementTag {
	
	// FLOW-V ����ID�����ַ���
	protected static final String FLOW_V_ID_BASE = "flowv_layout_";
	
	// ����ID
	private String id;
	
	// ����DIV��ID
	private String divId;
	
	private String widget;
	
	// û���ø߶������Ƿ��Զ����
	private boolean autoFill = true;
	
	// �������������Ϊ�����趨Ĭ��ID��
	protected int childCount = 0;

	protected void doRender() throws JspException, IOException {
		JspContext jspContext = getJspContext();
		JspWriter out = jspContext.getOut();
		out.write(generateHead());
		// ��Ⱦ����֮ǰ����ű�
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
		if (autoFill == false)  // �����Զ����
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
		if (autoFill) {  // �����Զ����
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
