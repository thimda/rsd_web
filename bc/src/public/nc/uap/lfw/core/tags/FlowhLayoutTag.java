package nc.uap.lfw.core.tags;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.WebElement;

/**
 * FLOW-H ���ֱ�ǩ
 * @author guoweic
 * 2009-10-15
 */
public class FlowhLayoutTag extends WebElementTag implements
		IContainerElementTag {
	
	// FLOW-H ����ID�����ַ���
	protected static final String FLOW_H_ID_BASE = "flowh_layout_";
	
	// ����ID
	private String id;
	
	// ����DIV��ID
	private String divId;
	
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
		if (id == null) {
			id = (String)LfwRuntimeEnvironment.getWebContext().getAttribute("$flowhId");
			if (id == null) {
				id = "0";
			} else {
				id = String.valueOf(Integer.parseInt(id) + 1);
			}
			LfwRuntimeEnvironment.getWebContext().setAttribute("$flowhId", id);
//			throw new LfwRuntimeException("flowh id can not be null for FlowhLayout");
		}
		divId = getDivShowId();//DIV_PRE + FLOW_H_ID_BASE + id;
		head = "<div id=\"" + divId + "\" style=\"width:100%;height:100%;overflow:hidden;\">\n";
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
		buf.append("addResizeEvent($ge(\"")
		   .append(divId)
		   .append("\"), flowhResize);\n")
		   .append("flowhResize.call($ge(\"")
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
		return null;
	}

}
