package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.comp.IFrameComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * iframe ±Í«©
 * @author guoweic
 *
 */
public class IFrameTag extends NormalComponentTag {

	public String generateBody() {
//		StringBuffer buf = new StringBuffer();
//		IFrameComp comp = (IFrameComp) getComponent();
//		buf.append("<div id=\"")
//		   .append(DIV_PRE)
//		   .append(comp.getId())
//		   .append("\" ")
//		   .append("style=\"width:100%;height:100%;position:absolute;\">");
//		return buf.toString();
		return super.generateBody();
	}

	public String generateBodyScript() {
		StringBuffer buf = new StringBuffer();
		IFrameComp comp = (IFrameComp) getComponent();
		
		if (comp.getId() == null) {
			throw new LfwRuntimeException("id can not be null for iframe");
		}
		String width = "100%";//(comp.getWidth() == null || "".equals(comp.getWidth())) ? "100%" : comp.getWidth();
		String height = "100%";//(comp.getHeight() == null || "".equals(comp.getHeight())) ? "100%" : comp.getHeight();
		String border = (comp.getBorder() == null || "".equals(comp.getBorder())) ? "0px" : comp.getBorder();
		String frameBorder = (comp.getFrameBorder() == null || "".equals(comp.getFrameBorder())) ? "0" : comp.getFrameBorder();
		String scrolling = (comp.getScrolling() == null || "".equals(comp.getScrolling())) ? "auto" : comp.getScrolling();
		
		String id = getVarShowId();
		
		buf.append("var ")
		   .append(id)
		   .append(" = new IFrameComp(document.getElementById(\"")
		   .append(getDivShowId())
		   .append("\"),\"")
		   .append(comp.getId())
		   .append("\",\"")
		   .append(comp.getId())
		   .append("\",\"")
		   .append(comp.getSrc() == null? "" : comp.getSrc())
		   .append("\",\"")
		   .append(width)
		   .append("\",\"")
		   .append(height)
		   .append("\",\"")
		   .append(border)
		   .append("\",\"")
		   .append(frameBorder)
		   .append("\",\"")
		   .append(scrolling)
		   .append("\");\n");
		
		if (null != this.getCurrWidget()){
			buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + id + ");\n");
		}
		
		return buf.toString();
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return null;
	}
}
	
