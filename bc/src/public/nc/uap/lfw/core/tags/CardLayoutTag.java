package nc.uap.lfw.core.tags;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * ��Ƭ���ֱ�ǩ
 * @author guoweic
 * 2009-10-15
 */
public class CardLayoutTag extends WebElementTag implements
		IContainerElementTag {
	
	// ��Ƭ����ID�����ַ���
	protected static final String CARD_ID_BASE = "card_layout_";
	
	// ����ID
	private String id;

	// ��ʼʱ��ʾ��ҳ��˳��
	private int cardIndex = 0;
	
	// �������CardPanel�ļ���
	protected int cardCount = 0;
	
	// ����DIV��ID
	private String divId;

	protected void doRender() throws JspException, IOException {
		JspContext jspContext = getJspContext();
		JspWriter out = jspContext.getOut();
		out.write(generateHead());
		JspFragment body = getJspBody();
		if(body != null)
			body.invoke(out);
		out.write(generateTail());
		String script = generateTailScript();
		addToBodyScript(script);
		
	}

	public String generateHead() {
		String head = null;
		if (id == null)
			throw new LfwRuntimeException("card id can not be null for CardLayout");
//		{
//			cardId = (String)LfwRuntimeEnvironment.getWebContext().getAttribute("$cardId");
//			if (cardId == null) {
//				cardId = "0";
//			} else {
//				cardId = String.valueOf(Integer.parseInt(cardId) + 1);
//			}
//			LfwRuntimeEnvironment.getWebContext().setAttribute("$cardId", cardId);
//		}
		divId = DIV_PRE + CARD_ID_BASE + id;
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
		buf.append("var ")
		   .append(getVarShowId())
		   .append(" = new CardLayout(")
		   .append("\"")
		   .append(id)
		   .append("\",")
		   .append("document.getElementById(\"")
		   .append(divId)
		   .append("\"),")
		   .append(cardIndex)
		   .append(");\n");
		buf.append("addResizeEvent($ge(\"")
		   .append(divId)
		   .append("\"), cardResize);\n")
		   .append("cardResize.call($ge(\"")
		   .append(divId)
		   .append("\"));\n");
		
		if(this.getWidget() != null)
		{
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append(widget + ".addCard(" + getVarShowId() + ");\n");
		}
		else
			buf.append("pageUI.addCard(" + getVarShowId() + ");\n");
		return buf.toString();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCardCount() {
		return cardCount;
	}

	public void setCardCount(int cardCount) {
		this.cardCount = cardCount;
	}

	public int getCardIndex() {
		return cardIndex;
	}

	public void setCardIndex(int cardIndex) {
		this.cardIndex = cardIndex;
	}
	
	public String getDivId() {
		return divId;
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return null;
	}

}
