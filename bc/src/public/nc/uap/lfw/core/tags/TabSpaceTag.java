package nc.uap.lfw.core.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;

import nc.uap.lfw.core.comp.WebElement;

/**
 * Tab控件的左右空间填充部分
 * @author dengjt
 */
public class TabSpaceTag extends WebElementTag{
	
	protected static final String CHILD_SCRIPT = "$CHILD_SCRIPT";
//	private String id;
	private String width;
	private String position = "right";
	@Override
	protected void doRender() throws JspException, IOException {
		StringBuffer buf = new StringBuffer();
		buf.append("<div id=\"")
		   .append(DIV_PRE)
		   .append(getId())
		   .append("\" style=\"width:")
		   .append(width)
		   .append("px;height:100%;overflow:hidden;\">\n");
		JspWriter out = getJspContext().getOut();
		out.write(buf.toString());
		
		JspFragment body = getJspBody();
		if(body != null)
			body.invoke(out);
		
		out.write("</div>\n");
		
		String script = generateTailScript();
		addToBodyScript(script);
	}
	
	public String generateTailScript() {
//		TabCompTag parent = (TabCompTag) findAncestorWithClass(this, TabCompTag.class);
//		StringBuffer buf = new StringBuffer();
//		if(parent.getWidget() == null)
//			buf.append(COMP_PRE + parent.getId());
//		else
//			buf.append(COMP_PRE + parent.getWidget() + "_" + parent.getId());
//		   if("right".equals(position))
//			   buf.append(".rightBarSpace");
//		   else
//			   buf.append(".leftBarSpace");
//		   buf.append(".appendChild($ge('")
//		   	  .append(DIV_PRE)
//		   	  .append(getId())
//		   	  .append("'));\n");
//		return buf.toString();
		return null;
	}
	
	public String getId() {
//		TabCompTag parent = (TabCompTag) findAncestorWithClass(this, TabCompTag.class);
//		String pId = parent.getId();
//		return pId + "_space_div";
		return null;
	}
//	public void setId(String id) {
//		this.id = id;
//	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return null;
	}
}
