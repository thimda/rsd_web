package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UISplitterOne;
import nc.uap.lfw.ra.render.UILayoutPanelRender;
import nc.uap.lfw.ra.render.UILayoutRender;

/**
 * @author renxh
 * ·Ö¸îÃæ°å×ó±ßµÄpanel
 */
public class PCSpliterOnePanelRender extends UILayoutPanelRender<UISplitterOne, WebElement> {

	public PCSpliterOnePanelRender(UISplitterOne uiEle, UIMeta uimeta, PageMeta pageMeta, UILayoutRender<? extends UILayout, ? extends WebElement> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		this.divId = parentRender.getDivId() + "_div_" + getDivIndex();

	}

	public String generalHeadHtml() {

		StringBuffer buf = new StringBuffer();
		buf.append("<div id=\"").append(getNewDivId()).append("\" style=\"width:100%;height:100%;\">\n");
		buf.append(this.generalEditableHeadHtml());
		return buf.toString();
	}

	public String generalHeadScript() {
		UILayoutRender<?, ?> parent = (UILayoutRender<?, ?>)this.getParentRender();
		StringBuffer buf = new StringBuffer();
		buf.append(parent.getVarId()).append(".getDiv").append(getDivIndex());
		buf.append("().add(document.getElementById(\"").append(getNewDivId()).append("\"));\n");
		return buf.toString();
	}

	public String generalTailHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalEditableTailHtml());
		buf.append("</div>\n");
		return buf.toString();
	}

	public String generalTailScript() {

		return super.generalTailScript();
	}

	protected String getDivIndex() {
		return "1";
	}

	public String generalEditableHeadScript() {
		return super.generalEditableHeadScript();
	}

	public String generalEditableTailScript() {
		return super.generalEditableTailScript();
	}
	
	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_SPLITERONEPANEL;
	}

	@Override
	public String generalHeadHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getNewDivId()).append(" = $ce('DIV');\n");
		buf.append(getNewDivId()).append(".style.width = '100%';\n");
		buf.append(getNewDivId()).append(".style.height = '100%';\n");
		buf.append(getNewDivId()).append(".id = '" + getNewDivId() + "';\n");
		
		if (this.isEditMode()) {
			buf.append(this.generalEditableHeadHtmlDynamic());
			buf.append(getNewDivId()).append(".appendChild(" + getDivId() + ");\n");
		}
		
		
		return buf.toString();
	}

	@Override
	public String createRenderHtmlDynamic() {
		return super.createRenderHtmlDynamic();
	}

	@Override
	public String createRenderScriptDynamic() {
		return super.createRenderScriptDynamic();
	}
	
	@Override
	public void notifyAddChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyAddChild(uiMeta, pageMeta, obj);
	}
}
