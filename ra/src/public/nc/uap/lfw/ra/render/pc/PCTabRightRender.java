package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import nc.uap.lfw.jsp.uimeta.UITabRightPanel;
import nc.uap.lfw.ra.render.UILayoutPanelRender;
import nc.uap.lfw.ra.render.UILayoutRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh ab控件的左右空间填充部分
 */
@SuppressWarnings("unchecked")
public class PCTabRightRender extends UILayoutPanelRender<UITabRightPanel, WebElement> {
	protected static final String CHILD_SCRIPT = "$CHILD_SCRIPT";
	private String width;
	private String position = "right";

	public PCTabRightRender(UITabRightPanel uiEle, UIMeta uimeta, PageMeta pageMeta, UILayoutRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		UIRender pRender = (UIRender) this.getParentRender();
		this.id = pRender.getId() + "_rightspace";
		this.divId = DIV_PRE + getId();
		UITabRightPanel panel = this.getUiElement();
		this.width = panel.getWidth();
	}


	public String generalHeadHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append("<div id=\"").append(getNewDivId()).append("\" style=\"min-width:50px;height:100%;overflow:hidden;\">\n");
		buf.append(this.generalEditableHeadHtml());
		return buf.toString();
	}

	public String generalHeadScript() {
		return "";
	}

	public String generalTailHtml() {
		return super.generalTailHtml();
	}

	public String generalTailScript() {
		UILayoutRender<?, ?> parent = (UILayoutRender<?, ?>) this.getParentRender();
		UITabComp tabComp = (UITabComp) parent.getUiElement();
		String tabId = tabComp.getId();
		StringBuffer buf = new StringBuffer();
		if(tabComp.getWidgetId() != null)
			buf.append("var tab = pageUI.getWidget('").append(tabComp.getWidgetId()).append("').getTab('").append(tabId).append("');\n");
		else
			buf.append("var tab = pageUI.getTab('").append(tabId).append("');\n");
		buf.append("tab.rightBarSpace");
		buf.append(".appendChild($ge('").append(getNewDivId()).append("'));\n");
		return buf.toString();
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_TABSPACE;
	}

	@Override
	public String generalHeadHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getNewDivId()).append(" = $ce('DIV');\n");
		buf.append(getNewDivId()).append(".style.width = '" + width + "px';\n");
		buf.append(getNewDivId()).append(".style.height = '100%';\n");
		buf.append(getNewDivId()).append(".overflow = 'hidden';\n");
		buf.append(getNewDivId()).append(".id = '" + getNewDivId() + "';\n");
		if (this.isEditMode()) {
			buf.append(this.generalEditableHeadHtmlDynamic());
			buf.append(getNewDivId()).append(".appendChild(" + getDivId() + ");\n");
		}
		return buf.toString();
	}
}
