package nc.uap.lfw.core.tags;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * 卡片布局子项标签
 * @author guoweic
 * 2009-10-15
 */
public class CardPanelTag extends WebElementTag implements IContainerElementTag {
	
	// 布局子项ID
	private String id;
	
	// 生成DIV的ID
	private String divId;

	// 父Tag对象
	private CardLayoutTag parentTag = null;
	
	protected void doRender() throws JspException, IOException {
		parentTag = (CardLayoutTag) findAncestorWithClass(this, CardLayoutTag.class);
		if(parentTag == null)
			throw new LfwRuntimeException("this tag must be included in CardLayoutTag");
		parentTag.cardCount++;
		// 设置默认ID
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
		
		// 直接将子Panel写出到页面
		getJspContext().getOut().write(writer.getBuffer().toString());

	}

	public String generateHead() {
		divId = parentTag.getDivId() + "_" + id;
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("<div id=\"" + divId + "\" style=\"width:100%;height:100%;position:absolute;\">\n");
		return strBuf.toString();
	}

	public String generateHeadScript() {
		// 获取记录当前子Item的Index
		Integer itemIndex = (Integer) parentTag.getAttribute("itemIndex");
		if (itemIndex == null) {
			itemIndex = Integer.valueOf(0);
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
		
		// 将已有的脚本暂存在临时变量中
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
		// 如果是当前显示项目
		if (parentTag.cardCount != 1) {
			// 将dsScript中的内容写入页面，并恢复原来的脚本
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
