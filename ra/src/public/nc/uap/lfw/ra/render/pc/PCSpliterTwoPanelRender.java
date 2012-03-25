package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UISplitterTwo;
import nc.uap.lfw.ra.render.UILayoutRender;

/**
 * @author renxh
 * ∑÷∏Ó√Ê∞Â”“±ﬂµƒpanel
 */
public class PCSpliterTwoPanelRender extends PCSpliterOnePanelRender {

	public PCSpliterTwoPanelRender(UISplitterTwo uiEle, UIMeta uimeta, PageMeta pageMeta, UILayoutRender<? extends UILayout, ? extends WebElement> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
	}

	protected String getDivIndex() {
		return "2";
	}

	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_SPLITERTWOPANLE;
	}
}
