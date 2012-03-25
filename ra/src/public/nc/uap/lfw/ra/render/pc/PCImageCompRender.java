package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.ImageComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIImageComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.render.UINormalComponentRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh 图片控件渲染器
 * @param <T>
 * @param <K>
 */
public class PCImageCompRender extends UINormalComponentRender<UIImageComp, ImageComp> {

	public PCImageCompRender(UIImageComp uiEle, ImageComp webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<? extends UIElement, ? extends WebElement> parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);
	}

	// 处理图片没有设高度宽度的情况
	public String generateBodyHtml() {
//
//		StringBuffer buf = new StringBuffer();
//		ImageComp comp = this.getWebElement();
//		if ("".equals(comp.getWidth())) {
//			buf.append("<div style=\"width:").append("100%").append(";height:").append("100%").append(";top:").append(comp.getTop() + "px").append(
//					";left:").append(comp.getLeft() + "px").append(";position:").append(comp.getPosition()).append(";overflow:auto");
//			buf.append("\" id=\"").append(getNewDivId()).append("\">\n");
//			buf.append(this.generalEditableHeadHtml());
//			buf.append(this.generalEditableTailHtml());
//			buf.append("</div>\n");
//			return buf.toString();
//		} else
		return super.generateBodyHtml();
	}

	// 处理图片没有设高度宽度的情况
	@Override
	public String generateBodyHtmlDynamic() {
//
//		StringBuffer buf = new StringBuffer();
//		ImageComp comp = this.getWebElement();
//		if ("".equals(comp.getWidth())) {
//			buf.append("var ").append(getDivId()).append(" = $ce('DIV');\n");
//			buf.append(getDivId()).append(".style.width = '100%';\n");
//			buf.append(getDivId()).append(".style.height = '100%';\n");
//			buf.append(getDivId()).append(".style.top = '").append(comp.getTop()).append("px';\n");
//			buf.append(getDivId()).append(".style.left = '").append(comp.getLeft()).append("px';\n");
//			buf.append(getDivId()).append(".style.position = '").append(comp.getPosition()).append("';\n");
//			buf.append(getDivId()).append(".style.overflow = 'auto';\n");
//			buf.append(getDivId()).append(".id = '" + getDivId() + "';\n");
//			return buf.toString();
//		} else {
		return super.generateBodyHtmlDynamic();
//		}
	}

	public String generateBodyScript() {

		StringBuffer buf = new StringBuffer();
		ImageComp image = this.getWebElement();
		String id = getVarId();
		buf.append("window.").append(id).append(" = new ImageComp(document.getElementById('").append(getDivId()).append("'),'");
		buf.append(image.getId()).append("','");
		buf.append(image.getRealImage1()).append("','0','0','100%','100%','");
		buf.append(image.getAlt()).append("',");
		if (image.getImage2() != null) {
			buf.append("'").append(image.getRealImage2()).append("'");
		} else {
			buf.append("null");
		}
		if (image.getImageInact() != null && !"".equals(image.getImageInact()))
			buf.append(",{inactiveImg:\"" + image.getImageInact() + "\"});\n");
		else
			buf.append(",null);\n");

		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + id + ");\n");
		return buf.toString();
	}

	public String generateBodyScriptDynamic() {

		StringBuffer buf = new StringBuffer();
		ImageComp image = this.getWebElement();
		String id = getVarId();
		buf.append("window.").append(id).append(" = new ImageComp(").append(getDivId()).append(",'");
		buf.append(image.getId()).append("','");
		buf.append(image.getRealImage1()).append("','0','0','100%','100%','");
		buf.append(image.getAlt()).append("',");
		if (image.getImage2() != null) {
			buf.append("'").append(image.getRealImage2()).append("'");
		} else {
			buf.append("null");
		}
		if (image.getImageInact() != null && !"".equals(image.getImageInact()))
			buf.append(",{inactiveImg:\"" + image.getImageInact() + "\"});\n");
		else
			buf.append(",null);\n");

		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + id + ");\n");
		return buf.toString();
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_IMAGECOMP;
	}

}
