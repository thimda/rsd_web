package nc.uap.lfw.core.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import nc.uap.lfw.core.comp.SelfDefComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * 自定义控件渲染Tag
 * 
 * @author guoweic
 *
 */
public class SelfDefCompTag extends NormalComponentTag {

	public String generateBody() {
		StringBuffer buf = new StringBuffer();
		WebComponent comp = getComponent();

		String parentDivId = getDivShowId();
		
		buf.append("<div id=\"")
		   .append(parentDivId)
		   .append("\" style=\"width:")
		   .append(getFormatString(getWidth()))
		   .append(";height:") 
		   .append(getFormatString(getHeight()))
		   .append(";top:")
		   .append(getFormatString(getTop()))
		   .append(";left:")
		   .append(getFormatString(getLeft()))
		   .append(";position:")
		   .append(getPosition())
		   .append(";\">\n</div>\n");
		return buf.toString();
	}

	public String generateBodyScript() {
		StringBuffer buf = new StringBuffer();
		SelfDefComp sdComp = (SelfDefComp) getComponent();
		
		String id = getVarShowId();
		buf.append("var ")
		   .append(id)
		   .append(" = new SelfDefComp(document.getElementById('")
		   .append(getDivShowId())
		   .append("'),'")
		   .append(sdComp.getId())
		   .append("','0','0','100%','100%','absolute',")
		   .append(sdComp.isVisible()) 
		   .append(",'")
		   .append("")
		   .append("');\n");
		
		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + id + ");\n");
		
		if(sdComp.isVisible() == false)
			buf.append(id + ".setVisible(false);\n");
		return buf.toString();
	}

	@Override
	protected void doRender() throws JspException, IOException {
		super.doRender();
		String id = getVarShowId();
		String script = id + ".oncreate();\n";
		addToBodyScript(script);
	}
	
	@Override
	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_SELF_DEF_COMP;
	}

}
