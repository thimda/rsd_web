package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIPanel;
import nc.uap.lfw.ra.render.UIPanelRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh
 *  panel布局 渲染器 没用到
 * @param <T>
 * @param <K>
 */
public class PCPanelRender extends UIPanelRender<UIPanel, WebElement> {
	public PCPanelRender(UIPanel uiEle, UIMeta uimeta, PageMeta pageMeta, UIRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
	}

	public String generalHeadHtml() {

		return "";
	}

	public String generalHeadScript() {

		return "";
	}

	public String generalTailHtml() {

		return "";
	}

	public String generalTailScript() {

		return "";
	}
	
	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_PANEL;
	}

	@Override
	public String generalHeadHtmlDynamic() {
		return "";
	}

}
