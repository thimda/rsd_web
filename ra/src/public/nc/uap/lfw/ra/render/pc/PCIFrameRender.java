package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.IFrameComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIIFrame;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.render.UINormalComponentRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh IFrameäÖÈ¾Æ÷
 * @param <T>
 * @param <K>
 */
public class PCIFrameRender extends UINormalComponentRender<UIIFrame, IFrameComp> {

	public PCIFrameRender(UIIFrame uiEle, IFrameComp webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<? extends UIElement, ? extends WebElement> parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);
	}

	@Override
	public String generateBodyHtml() {
		StringBuffer buf = new StringBuffer();
		UIComponent comp = this.getUiElement();
		String width = comp.getWidth();
		if (width == null)
			buf.append("<div style=\"");
		else
			buf.append("<div style=\"width:").append(width.indexOf("%") != -1 ? width : width + "px;");
		String height = comp.getHeight();
		if (height != null)
			buf.append(";height:").append(height.indexOf("%") != -1 ? height : height + "px;");
		buf.append(";top:").append(comp.getTop() + "px");
		buf.append(";left:").append(comp.getLeft() + "px");
		buf.append(";position:").append(comp.getPosition());
		buf.append(";overflow:hidden");
//		if(this.align.equals(UIComponent.ALIGN_RIGHT))
//			buf.append(";float:right;");
		buf.append("\" id=\"").append(getDivId()).append("\">\n");
		buf.append("</div>\n");
		return buf.toString();
	}

	@Override
	public String generateBodyHtmlDynamic() {

		return super.generateBodyHtmlDynamic();
	}

	public String generateBodyScript() {

		StringBuffer buf = new StringBuffer();
		IFrameComp comp = this.getWebElement();

		if (comp.getId() == null) {
			throw new LfwRuntimeException("id can not be null for iframe");
		}
//		String width = (comp.getWidth() == null || "".equals(comp.getWidth())) ? "100%" : comp.getWidth();
//		String height = (comp.getHeight() == null || "".equals(comp.getHeight())) ? "100%" : comp.getHeight();
		String border = (comp.getBorder() == null || "".equals(comp.getBorder())) ? "0px" : comp.getBorder();
		String frameBorder = (comp.getFrameBorder() == null || "".equals(comp.getFrameBorder())) ? "0" : comp.getFrameBorder();
		String scrolling = (comp.getScrolling() == null || "".equals(comp.getScrolling())) ? "auto" : comp.getScrolling();

		String id = getVarId();

		buf.append("var ").append(id);
		buf.append(" = new IFrameComp(document.getElementById(\"").append(getDivId()).append("\"),\"");
		buf.append(comp.getId()).append("\",\"");
		buf.append(comp.getId()).append("\",\"");
		buf.append(comp.getSrc() == null ? "" : comp.getSrc()).append("\",\"100%\",\"100%\",\"");
		buf.append(border).append("\",\"");
		buf.append(frameBorder).append("\",\"");
		buf.append(scrolling).append("\");\n");

		if (null != this.getCurrWidget()) {
			buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + id + ");\n");
		}

		return buf.toString();
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_IFRAME;
	}

}
