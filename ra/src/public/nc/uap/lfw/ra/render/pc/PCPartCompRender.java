package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.IWebPartContentFetcher;
import nc.uap.lfw.core.comp.WebPartComp;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIPartComp;
import nc.uap.lfw.ra.render.UILayoutPanelRender;
import nc.uap.lfw.ra.render.UINormalComponentRender;
import nc.uap.lfw.util.LfwClassUtil;

/**
 * @author renxh html内容渲染的布局
 */
public class PCPartCompRender extends UINormalComponentRender<UIPartComp, WebPartComp> {

	public PCPartCompRender(UIPartComp uiEle, WebPartComp webEle, UIMeta uimeta, PageMeta pageMeta, UILayoutPanelRender<?, ?> parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);
	}

	@Override
	public String generateBodyHtml() {
		StringBuffer buf = new StringBuffer();
		String content = fetcherContent(this.getWebElement());
		buf.append("<div style='width:100%;height:100%;overflow:hidden' id='" + getDivId() + "'>"+content+"</div>\n");
		return buf.toString();
	}

	@Override
	public String generateBodyHtmlDynamic() {
		String content = fetcherContent(this.getWebElement());
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getDivId()).append(" = $ce('DIV');\n");
		buf.append(getDivId()).append(".style.height = '100%';\n");
		buf.append(getDivId()).append(".style.width = '100%';\n");
		buf.append(getDivId()).append(".style.overflow = 'hidden';\n");
		buf.append(getDivId()).append(".id = '" + getDivId() + "';\n");
		buf.append(getDivId()).append(".innerHTML = '" + content + "';\n");
		return buf.toString();

	}

	@Override
	public String generateBodyScript() {
		String parentDivId = this.getDivId();
		StringBuffer buf = new StringBuffer();
		UIComponent uiComp = this.getUiElement();
		WebPartComp webEle = this.getWebElement();
		String showId = this.getVarId();
		buf.append("var ").append(showId).append(" = new HtmlContentComp(");
		
		buf.append("document.getElementById(\"").append(parentDivId).append("\"), \"");
		buf.append(id).append("\", \"0\", \"0\", \"100%\", \"100%\", \"relative\", \"");
		buf.append(uiComp.getClassName() + "\"");
		buf.append(");\n");
//		String content = fetcherContent(webEle);
//		buf.append(showId).append(".setContent('").append(content).append("');\n");
		buf.append("pageUI.getWidget('" + getWidget() + "').addComponent(" + showId + ");\n");
		
		String fetchscript = this.fetcherBodyScript(this.getWebElement());
		if (fetchscript != null)
			buf.append(fetchscript);
		return buf.toString();
	}

	/**
	 * 加载内容
	 * 
	 * @param webEle
	 * @return
	 */
	private String fetcherContent(WebPartComp webEle) {
		IWebPartContentFetcher fetcher = (IWebPartContentFetcher) LfwClassUtil.newInstance(webEle.getContentFetcher());
		PageMeta pm = this.getPageMeta();
		LfwWidget widget = pm.getWidget(this.widget);
		UIMeta um = this.getUiMeta();
//		IUIRenderGroup group =  this.getGroup();
		String content = fetcher.fetchHtml(um,pm, widget);
		
		return content;
	}
	private String fetcherBodyScript(WebPartComp webEle) {
		IWebPartContentFetcher fetcher = (IWebPartContentFetcher) LfwClassUtil.newInstance(webEle.getContentFetcher());
		PageMeta pm = this.getPageMeta();
		LfwWidget widget = pm.getWidget(this.widget);
		UIMeta um = this.getUiMeta();
//		IUIRenderGroup group =  this.getGroup();
		String content = fetcher.fetchBodyScript(um,pm, widget);
		
		return content;
	}

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyUpdate(uiMeta, pageMeta, obj);
	}

	@Override
	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_HTMLCONTENT;
	}

}
