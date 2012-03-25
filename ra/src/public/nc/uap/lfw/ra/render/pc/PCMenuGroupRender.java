package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIMenuGroup;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.render.UILayoutRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh ²Ëµ¥×é¿Ø¼þ äÖÈ¾Æ÷
 */
public class PCMenuGroupRender extends UILayoutRender<UIMenuGroup, WebElement> {

	public PCMenuGroupRender(UIMenuGroup uiEle, UIMeta uimeta, PageMeta pageMeta, UIRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		divId = DIV_PRE + id;
		varId = COMP_PRE + id;
	}

	public String generalHeadHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append("<div id=\"").append(this.getNewDivId());
		buf.append("\" style=\"width:100%;height:30px");
		buf.append(";top:0px;left:0px;position:relative;\">\n");
		buf.append(this.generalEditableHeadHtml());
		return buf.toString();
	}

	public String generalHeadScript() {

		StringBuffer buf = new StringBuffer();
		buf.append("window." + varId + " = new MenuBarGroup('").append(getId()).append("');\n");
		buf.append("pageUI.addMenubarGroup(window." + varId + ");\n");
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
		return LfwPageContext.SOURCE_TYPE_MENU_GROUP;
	}
	
	

	@Override
	public String generalHeadHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getNewDivId()).append(" = $ce('DIV');\n");
		buf.append(getNewDivId()).append(".style.width = '100%';\n");
		buf.append(getNewDivId()).append(".style.height = '30px';\n");
		buf.append(getNewDivId()).append(".style.top = '0px';\n");
		buf.append(getNewDivId()).append(".style.position = 'relative';\n");
		buf.append(getNewDivId()).append(".id = '"+getNewDivId()+"';\n");
		if(this.isEditMode()){
			buf.append(this.generalEditableHeadHtmlDynamic());
			buf.append(getNewDivId()).append(".appendChild("+getDivId()+");\n");
		}
		
		return buf.toString();
	}
}
