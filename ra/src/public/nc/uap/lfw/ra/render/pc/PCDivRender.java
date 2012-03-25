package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIDiv;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.render.UILayoutRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh
 *  panel布局 渲染器 没用到
 * @param <T>
 * @param <K>
 */
public class PCDivRender extends UILayoutRender<UIDiv, WebElement> {
	public PCDivRender(UIDiv uiEle, UIMeta uimeta, PageMeta pageMeta, UIRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
	}

	public String generalHeadHtml() {
		UIDiv div = getUiElement();
		StringBuffer buf = new StringBuffer();
		buf.append("<div id=\"" + getDivId() + "\" style=\"" + div.getStyle() + "\">\n");
		return buf.toString();
	}

	public String generalHeadScript() {
		return "";
	}

	public String generalTailHtml() {
		return "</div>";
	}

	public String generalTailScript() {

		return "";
	}
	
	protected String getSourceType(IEventSupport ele) {
		return "";
	}

//	@Override
//	public String generalHeadHtmlDynamic() {
//		StringBuffer buf = new StringBuffer();
//		String newDivId = getDivId();
//		buf.append("var ").append(newDivId).append(" = $ce('DIV');\n");
//		buf.append(newDivId).append(".style.width = '100%';\n");
//		if(!isFlowMode())
//			buf.append(newDivId).append(".style.height = '100%';\n");
//		else
//			buf.append(newDivId).append(".flowmode=" + isFlowMode() + ";\n");
////		buf.append(newDivId).append(".style.overlflow = 'hidden';\n");
//		buf.append(newDivId).append(".id = '"+newDivId+"';\n");		
//		if (this.isEditMode()) {
//			buf.append(this.generalEditableHeadHtmlDynamic());
//			buf.append(newDivId).append(".appendChild(").append(getDivId()).append(");\n");
//		}
//		return buf.toString();
//	}

}
