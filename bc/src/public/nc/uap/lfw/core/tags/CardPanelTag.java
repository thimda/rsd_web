package nc.uap.lfw.core.tags;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * ��Ƭ���������ǩ
 * @author guoweic
 * 2009-10-15
 */
public class CardPanelTag extends WebElementTag implements IContainerElementTag {
	
	// ��������ID
	private String id;
	
	// ����DIV��ID
	private String divId;

	// ��Tag����
	private CardLayoutTag parentTag = null;
	
	protected void doRender() throws JspException, IOException {
		parentTag = (CardLayoutTag) findAncestorWithClass(this, CardLayoutTag.class);
		if(parentTag == null)
			throw new LfwRuntimeException("this tag must be included in CardLayoutTag");
		parentTag.cardCount++;
		// ����Ĭ��ID
		if (id == null || id.equals("")) {
			id = String.valueOf(parentTag.cardCount);
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
		strBuf.append("<div id=\"" + divId + "\" style=\"width:100%;height:100%;position:absolute;\">\n");
		return strBuf.toString();
	}

	public String generateHeadScript() {
		// ��ȡ��¼��ǰ��Item��Index
		Integer itemIndex = (Integer) parentTag.getAttribute("itemIndex");
		if (itemIndex == null) {
			itemIndex = new Integer(0);
			parentTag.setAttribute("itemIndex", itemIndex);
		}
		StringBuffer buf = new StringBuffer();
		buf.append("window.$")
		   .append(parentTag.getId())
		   .append("_item")
		   .append(itemIndex)
		   .append(" = function(){\n");
		
		itemIndex++;
		parentTag.setAttribute("itemIndex", (itemIndex++));
		
		// �����еĽű��ݴ�����ʱ������
		StringBuffer dsScript = (StringBuffer) getJspContext().getAttribute(PageModelTag.DS_SCRIPT);
		getJspContext().setAttribute("$card_" + id + "$tmpScript", dsScript.toString());
		dsScript.delete(0, dsScript.length());
		
		return buf.toString();
	}

	public String generateTail() {
		return "</div>\n";
	}

	public String generateTailScript() {
		StringBuffer tmpBuf = new StringBuffer();
		StringBuffer dsScript = (StringBuffer) getJspContext().getAttribute(PageModelTag.DS_SCRIPT);
		String tmpScript = (String) getJspContext().getAttribute("$card_" + id + "$tmpScript");
		// ����ǵ�ǰ��ʾ��Ŀ
		if (parentTag.cardCount != 1) {
			// ��dsScript�е�����д��ҳ�棬���ָ�ԭ���Ľű�
			tmpBuf.append(dsScript.toString());
			dsScript.delete(0, dsScript.length());
			if(tmpScript != null)
				dsScript.append(tmpScript);
		} else {
			if (tmpScript != null)
				dsScript.insert(0, tmpScript);
		}
		getJspContext().removeAttribute("$card_" + id + "$tmpScript");
		tmpBuf.append("\n}\n");
		return tmpBuf.toString();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return null;
	}

}
