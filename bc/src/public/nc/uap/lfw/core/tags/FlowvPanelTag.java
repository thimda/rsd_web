package nc.uap.lfw.core.tags;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * FLOW-H ���������ǩ
 * @author guoweic
 * 2009-10-15
 */
public class FlowvPanelTag extends WebElementTag implements IContainerElementTag {
	
	// ��������ID
	private String id;
	
	// �߶�
	private String height;

	// ����DIV��ID
	private String divId;
	
	// ê������
	private String anchor;

	// ��Tag����
	private FlowvLayoutTag parentTag = null;
	
	protected void doRender() throws JspException, IOException {
		parentTag = (FlowvLayoutTag) findAncestorWithClass(this, FlowvLayoutTag.class);
		if(parentTag == null)
			throw new LfwRuntimeException("this tag must be included in FlowvLayoutTag");
		parentTag.childCount++;
		// ����Ĭ��ID
		if (id == null || id.equals("")) {
			id = String.valueOf(parentTag.childCount);
		}
		
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
		
		// ֱ�ӽ���Panelд����ҳ��
		getJspContext().getOut().write(writer.getBuffer().toString());

	}

	public String generateHead() {
		divId = parentTag.getDivId() + "_" + id;
		StringBuffer strBuf = new StringBuffer();
		if (height != null) {
			strBuf.append("<div id=\"" + divId + "\" hasheight=\"1\" style=\"height:" + height + ";width:100%;left:0px;position:relative;\" ");
		} else {
			strBuf.append("<div id=\"" + divId + "\" hasheight=\"0\" style=\"width:100%;left:0px;position:relative;\" ");
		}
		if (anchor != null)
			strBuf.append("anchor=\"")
			      .append(anchor)
			      .append("\"");
		strBuf.append(">\n");
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
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = getFormatSize(height);
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return null;
	}

	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

}
