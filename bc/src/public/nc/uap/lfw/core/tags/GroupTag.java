package nc.uap.lfw.core.tags;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;

import nc.uap.lfw.core.comp.WebElement;
/**
 * Fieldset Tag
 * @author dengjt
 *
 */
public class GroupTag extends WebElementTag implements IContainerElementTag{
	private String text;
	private String i18nName;
	private String langDir;
	private String marginTop;
	private String marginBottom;
	private String marginLeft;
	private String marginRight;
	public String generateHead() {
		StringBuffer buf = new StringBuffer();
		buf.append("<fieldset style=\"padding:0px;border:1px dashed gray;height:100%;top:0px;left:0px;position:relative;");
		
		if (null != marginTop && !"".equals(marginTop)) {
			buf.append("margin-top:")
			   .append(-1 == marginTop.indexOf("px") ? marginTop + "px;" : marginTop);
		}
		if (null != marginBottom && !"".equals(marginBottom)) {
			buf.append("margin-bottom:")
			   .append(-1 == marginBottom.indexOf("px") ? marginBottom + "px;" : marginBottom);
		}
		if (null != marginLeft && !"".equals(marginLeft)) {
			buf.append("margin-left:")
			   .append(-1 == marginLeft.indexOf("px") ? marginLeft + "px;" : marginLeft);
		}
		if (null != marginRight && !"".equals(marginRight)) {
			buf.append("margin-right:")
			   .append(-1 == marginRight.indexOf("px") ? marginRight + "px;" : marginRight);
		}
		
		buf.append("\">\n")
		   .append("<legend><span style='font-weight:bold;color:#000000;background:#FFFFFF;padding-left:3px;padding-right:3px'>")
		   .append(translate(i18nName, text, langDir))
		   .append("</span></legend>\n");
		return buf.toString();
	}
	public String generateTail() {
		return "</fieldset>\n";
	}

	public String generateHeadScript() {
		return null;
	}


	public String generateTailScript() {
		return null;
	}
	@Override
	protected String getSourceType(WebElement ele) {
		return null;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getI18nName() {
		return i18nName;
	}
	public void setI18nName(String name) {
		i18nName = name;
	}
	public String getLangDir() {
		return langDir;
	}
	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}
	@Override
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
	public String getMarginTop() {
		return marginTop;
	}
	public void setMarginTop(String marginTop) {
		this.marginTop = marginTop;
	}
	public String getMarginBottom() {
		return marginBottom;
	}
	public void setMarginBottom(String marginBottom) {
		this.marginBottom = marginBottom;
	}
	public String getMarginLeft() {
		return marginLeft;
	}
	public void setMarginLeft(String marginLeft) {
		this.marginLeft = marginLeft;
	}
	public String getMarginRight() {
		return marginRight;
	}
	public void setMarginRight(String marginRight) {
		this.marginRight = marginRight;
	}

}
