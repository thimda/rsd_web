package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.LinkComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UILinkComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.render.UINormalComponentRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh 链接渲染器
 * @param <T>
 * @param <K>
 */
public class PCLinkCompRender extends UINormalComponentRender<UILinkComp, LinkComp> {

	public PCLinkCompRender(UILinkComp uiEle, LinkComp webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<? extends UIElement, ? extends WebElement> parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);

	}

	/**
	 * 统一渲染控件占位符
	 */
	@Override
	public String generateBodyHtml() {

		return super.generateBodyHtml();
	}

	@Override
	public String generateBodyHtmlDynamic() {

		return super.generateBodyHtmlDynamic();
	}

	@Override
	public String generateBodyScript() {

		StringBuffer buf = new StringBuffer();
		LinkComp link = this.getWebElement();
		String linkId = getVarId();
		buf.append("window.").append(linkId).append(" = new LinkComp(document.getElementById('" + getDivId() + "'),'");
		buf.append(link.getId()).append("','0','0',");
		if (link.getHref() == null) {
			buf.append(link.getHref());
		} else {
			buf.append("'" + link.getHref() + "'");
		}
		buf.append(",'").append(link.getI18nName()).append("',");
		buf.append(link.isHasImg()).append(",'");
		// .append(getRealImgPath(link.getImage()))
		buf.append(link.getRealImage()).append("','").append(link.getTarget()).append("','relative', null);\n");

		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + linkId + ");\n");

		return buf.toString();
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_LINKCOMP;
	}

}
