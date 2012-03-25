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
public class FlowhPanelTag extends WebElementTag implements IContainerElementTag {
	
	// ��������ID
	private String id;
	
	// ���
	private String width;

	// ����DIV��ID
	private String divId;

	// ��Tag����
	private FlowhLayoutTag parentTag = null;
	
	protected void doRender() throws JspException, IOException {
		parentTag = (FlowhLayoutTag) findAncestorWithClass(this, FlowhLayoutTag.class);
		if(parentTag == null)
			throw new LfwRuntimeException("this tag must be included in FlowhLayoutTag");
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
		if (width != null) {
			strBuf.append("<div id=\"" + divId + "\" haswidth=\"1\" style=\"width:" + width + ";height:100%;float:left;\">\n");
		} else {
			strBuf.append("<div id=\"" + divId + "\" haswidth=\"0\" style=\"height:100%;float:left;\">\n");
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
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
