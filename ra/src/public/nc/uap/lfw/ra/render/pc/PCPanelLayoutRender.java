package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIPanel;
import nc.uap.lfw.jsp.uimeta.UIPanelPanel;
import nc.uap.lfw.ra.render.UIPanelRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh Panel ≤ºæ÷‰÷»æ∆˜
 */
public class PCPanelLayoutRender extends UIPanelRender<UIPanel, WebElement> {
	private String title;
	public PCPanelLayoutRender(UIPanel uiEle, UIMeta uimeta, PageMeta pageMeta, UIRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		this.setPageMeta(pageMeta);
		UIPanel panelLayout = this.getUiElement();
		this.divId = DIV_PRE + id;
		this.varId = COMP_PRE + id;
		this.title = panelLayout.getTitle();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String generalHeadHtml() {
		StringBuffer head = new StringBuffer();
		String height = "height:100%;";
		if(isFlowMode())
			height = "";
//		String parentDivId = this.getDivId();
		head.append("<div id='").append(getNewDivId()).append("' style='" + height + "overflow:hidden;position:relative;'>\n");
		head.append(this.generalEditableHeadHtml());
		return head.toString();
	}

	public String generalHeadScript() {
		String parentDivId = this.getDivId();
		StringBuffer buf = new StringBuffer();
		String showId = this.getVarId();
		if(title == null)
			title = "";
		
		//(parent, name, left, top, width, height, position, title, attrArr, className) {
		buf.append("var ").append(showId).append(" = new PanelComp(").append("document.getElementById(\"").append(parentDivId).append("\"), \"")
				.append(id).append("\", '0px', '0px', '100%', '100%', 'relative', '").append(title).append("'");
		
		UIPanel panel = getUiElement();
		UIPanelPanel cp = (UIPanelPanel) panel.getPanelList().get(0);
		String topPadding = cp.getTopPadding();
		String bottomPadding = cp.getBottomPadding();
		buf.append(",{");
		buf.append("flowmode:").append(isFlowMode());
		if(isFlowMode() && topPadding != null && !topPadding.equals(""))
			buf.append(",topPadding:'").append(topPadding).append("'");
		if(isFlowMode() && bottomPadding != null && !bottomPadding.equals(""))
			buf.append(",bottomPadding:'").append(bottomPadding).append("'");
		buf.append("}");;

		buf.append(");\n");

//		buf.append(showId).append(".setDisplay(").append(display).append(");\n");
//
////		buf.append(showId).append(".setVisible(").append(visibility).append(");\n");
//
//		buf.append(showId).append(".setPadding('").append(paddingLeft).append("','").append(paddingTop).append("','").append(paddingRight).append(
//				"','").append(paddingBottom).append("');\n");

		if (this.getWidget() != null) {
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append("var ").append(widget).append(" = pageUI.getWidget('").append(this.getCurrWidget().getId()).append("');\n");
			buf.append(widget + ".addPanel(" + showId + ");\n");
		} else
			buf.append("pageUI.addPanel(" + showId + ");\n");

//		buf.append(showId).append(".flowmode=").append(isFlowMode()).append(";\n");
		return buf.toString();
	}

	public String generalTailHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalEditableTailHtml());
		buf.append("</div>\n");
		return buf.toString();
	}

	public String generalTailScript() {

		return "";
	}

	
	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_PANELLAYOUT;
	}

	@Override
	public String generalHeadHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		String newDivId = getNewDivId();
		buf.append("var ").append(newDivId).append(" = $ce('DIV');\n");
		buf.append(newDivId).append(".style.width = '100%';\n");
		if(!isFlowMode())
			buf.append(newDivId).append(".style.height = '100%';\n");
		buf.append(newDivId).append(".overflow = 'hidden';\n");
		buf.append(newDivId).append(".style.position = 'relative';\n");
		buf.append(newDivId).append(".id = '" + newDivId + "';\n");
		if (this.isEditMode()) {
			buf.append(this.generalEditableHeadHtmlDynamic());
			buf.append(newDivId).append(".appendChild(" + getDivId() + ");\n");
		}
		return buf.toString();
	}

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		StringBuffer buf = new StringBuffer();
		String showId = this.getVarId();
		if (this.getWidget() != null) {
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append("var ").append(widget).append(" = pageUI.getWidget('"+this.getCurrWidget().getId()+"');\n");
			buf.append("var "+showId+" = ").append(widget + ".getPanel('" + id + "');\n");
		} else{
			buf.append("var "+showId+" = ").append("pageUI.getPanel('" + id + "');\n");
		}
		buf.append(showId).append(".setTitle('"+title+"');\n");
		buf.append("layoutInitFunc();\n");
		this.addDynamicScript(buf.toString());
			
	}
}
