package nc.uap.lfw.core.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;

import nc.uap.lfw.core.comp.WebComponent;

/**
 * 容器型控件Tag基类
 * @author dengjt
 *
 */
public abstract class ContainerComponentTag extends WebComponentTag implements IContainerElementTag{
	
	public void doTag() throws JspException, IOException {
		super.doTag();
	}
	
	protected void doRender() throws JspException, IOException {
		//logger.info("begin to render component:" + getId());
		WebComponent comp = getComponent();
		// 将控件实例设入pageContext中
		getJspContext().setAttribute(getId(), comp);
		
		JspWriter out = getJspContext().getOut();
		String headStr = generateHead();
		if(headStr != null)
			out.write(headStr);
		String script = generateHeadScript();
		addToBodyScript(script);
		
		JspFragment body = getJspBody();
		if(body != null)
			body.invoke(out);
		
		String tailStr = generateTail();
		if(tailStr != null)
			out.write(tailStr);
		script = generateTailScript();
		addToBodyScript(script);
		if(comp.getContextMenu() != null && !comp.getContextMenu().equals("")){
			script = addContextMenu(comp.getContextMenu(), COMP_PRE + getId());
			addToBodyScript(script);
		}
		
		// 添加控件事件绑定
		script = addEventSupport(comp, getCurrWidget() == null?null:getCurrWidget().getId(), getVarShowId(), null);
		addToBodyScript(script);
		
		//comp.setRendered(true);
		
		//logger.info("successfully render component:" + getId());
	}

	
	public String generateHeadScript() {
		return null;
	}
	
	public String generateTailScript() {
		return null;
	}
	
	public String generateHead() {
		WebComponent tab = getComponent();
		StringBuffer buf = new StringBuffer();
		buf.append("<div id=\"")
		   .append(DIV_PRE)
		   .append(getId())
		   .append("\" style=\"top:")
		   .append("0")
		   .append(";left:")
		   .append("0")
		   .append(";width:")
		   .append("100%")
		   .append(";height:")
		   .append("100%")
//		   .append(";padding-left:")
//		   .append(tab.getMarginLeft())
//		   .append(";padding-top:")
//		   .append(tab.getMarginTop())
//		   .append(";padding-right:")
//		   .append(tab.getMarginRight())
//		   .append(";padding-bottom:")
//		   .append(tab.getMarginBottom())
		   .append(";overflow:hidden;\">\n");
		return buf.toString();
	}

	public String generateTail() {
		return "</div>";
	}

}
