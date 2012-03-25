package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIBorder;
import nc.uap.lfw.jsp.uimeta.UIBorderTrue;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.render.UILayoutPanelRender;
import nc.uap.lfw.ra.render.UILayoutRender;

@SuppressWarnings("unchecked")
public class PCBorderTrueRender extends UILayoutPanelRender<UIBorderTrue, WebElement> {

	public PCBorderTrueRender(UIBorderTrue uiEle, UIMeta uimeta, PageMeta pageMeta, UILayoutRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		this.id = "inner";
		this.divId = parentRender.getDivId() + "_inner";
		this.className = ((PCBorderRender) parentRender).getClassName();
		this.widget = parentRender.getWidget();

	}

	private String className = null;

	public String generalHeadHtml() {

		UIBorderTrue panelPanel = getUiElement();
		boolean roundBorder = ((UIBorder)panelPanel.getLayout()).getRoundBorder() == 0 ? true : false;;
		StringBuffer buf = new StringBuffer();
		if(roundBorder){
			className = "roundcorner";
		}
		buf.append("<div id='" + getNewDivId() + "' style=\"height:99%;overflow:visible;\"");
		if (className != null && !className.equals("")) {
			buf.append(" class=\"").append(className).append("\" >\n");
		} 
		else {
			buf.append(">\n");
		}
		buf.append(this.generalEditableHeadHtml());
		return buf.toString();
	}
	
	@Override
	public String generalHeadHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getNewDivId()).append(" = $ce('DIV');\n");
		buf.append(getNewDivId()).append(".style.width='100%';\n");
		buf.append(getNewDivId()).append(".style.height='100%';\n");
		buf.append(getNewDivId()).append(".style.overflow='hidden';\n");
		buf.append(getNewDivId()).append(".id='" + getNewDivId() + "';\n");
		if(this.isEditMode()){
			buf.append(this.generalEditableHeadHtmlDynamic());
			buf.append(getNewDivId()).append(".appendChild("+getDivId()+");\n");
		}
		return buf.toString();
	}

	public String generalTailHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalEditableTailHtml());
		buf.append("</div>\n");
		return buf.toString();
	}

	public String generalHeadScript() {
		return "";
	}

	public String generalTailScript() {
		return "";
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_BORDERTRUE;
	}
}
