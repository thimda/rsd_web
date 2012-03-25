package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UICanvas;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UpdatePair;
import nc.uap.lfw.ra.render.UILayoutRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh Panel ≤ºæ÷‰÷»æ∆˜
 */
public class PCCanvasLayoutRender extends UILayoutRender<UICanvas, WebElement> {
	private String title;
	public PCCanvasLayoutRender(UICanvas uiEle, UIMeta uimeta, PageMeta pageMeta, UIRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		this.setPageMeta(pageMeta);
//		UICanvas panelLayout = this.getUiElement();
//		this.divId = DIV_PRE + id;
//		this.varId = COMP_PRE + id;
		//this.title = panelLayout.getTitle();
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
		
		UICanvas panel = getUiElement();
		//(parent, name, left, top, width, height, position, title, attrArr, className) {
		buf.append("var ").append(showId).append(" = new CanvasComp(").append("document.getElementById(\"").append(parentDivId).append("\"), \"")
				.append(id).append("\", '0px', '0px', '100%', '100%', 'relative',");
		
		buf.append("{");
		String title = panel.getTitle();
		if(title != null && !title.equals(""))
			buf.append("title:'").append(title).append("'");
		buf.append("}");
		buf.append(",'").append(panel.getClassName()).append("');\n");
//		UICanvasPanel cp = (UICanvasPanel) panel.getPanelList().get(0);

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
		return LfwPageContext.SOURCE_TYPE_CANVASLAYOUT;
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
		if(obj instanceof UpdatePair){
			UpdatePair up = (UpdatePair) obj;
			if(up.getKey().equals(UIElement.CLASSNAME)){
				String className = (String) up.getValue();
				StringBuffer buf = new StringBuffer();
				String showId = this.getVarId();
				if (this.getWidget() != null) {
					String widget = WIDGET_PRE + this.getCurrWidget().getId();
					buf.append("var ").append(widget).append(" = pageUI.getWidget('"+this.getCurrWidget().getId()+"');\n");
					buf.append("var "+showId+" = ").append(widget + ".getPanel('" + id + "');\n");
				} else{
					buf.append("var "+showId+" = ").append("pageUI.getPanel('" + id + "');\n");
				}
				buf.append(showId).append(".changeClass('" + className + "');\n");
				this.addDynamicScript(buf.toString());
			}
		}
			
	}
}
