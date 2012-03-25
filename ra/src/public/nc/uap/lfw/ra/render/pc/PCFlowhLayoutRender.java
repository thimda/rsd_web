package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIConstant;
import nc.uap.lfw.jsp.uimeta.UIFlowhLayout;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.render.UILayoutRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh
 * 横向布局渲染器
 * @param <T>
 * @param <K>
 */
public class PCFlowhLayoutRender extends UILayoutRender<UIFlowhLayout, WebElement> {

	private static final long serialVersionUID = 5889314278625193695L;
	public PCFlowhLayoutRender(UIFlowhLayout uiEle,UIMeta uimeta, PageMeta pageMeta,UIRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
//		divId = DIV_PRE + id;
	}

	public String generalHeadHtml() {
		UIFlowhLayout flowh = getUiElement();
		boolean autoFill = flowh.getAutoFill().equals(UIConstant.TRUE);
		StringBuffer buf = new StringBuffer();
		String height = "height:100%;";
		boolean isFlowmode = isFlowMode();
		if(isFlowmode && !autoFill)
			height = "";
		buf.append("<div id=\"" + getNewDivId() + "\" style=\"" + height + "\" flowmode=\"" + isFlowmode + "\">\n");
		buf.append(this.generalEditableHeadHtml());
		return buf.toString();
	}

	public String generalHeadScript() {
		return toResizeDynamic("$ge(\""+getDivId()+"\")", "flowhResize");
	}

	public String generalTailHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append("<div style='clear:both'></div>");
		buf.append("</div>\n");
		buf.append(this.generalEditableTailHtml());
		return buf.toString();
	}

	public String generalTailScript() {

		return "";
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_FLOWHLAYOUT;
	}

	
	/**
	 * 2011-8-2 下午07:02:06 renxh des：编辑态时，需加入此div
	 * 
	 * @return
	 */
	public String generalEditableHeadHtml() {
		return super.generalEditableHeadHtml();
	}
	/**
	 * 2011-8-2 下午07:02:06 renxh des：编辑态时，需加入此div
	 * 
	 * @return
	 */
	public String generalEditableHeadHtmlDynamic() {
		return super.generalEditableHeadHtmlDynamic();
	}
	@Override
	public String generalHeadHtmlDynamic() {
		return super.generalHeadHtmlDynamic();
	}

	@Override
	public void notifyAddChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyAddChild(uiMeta, pageMeta, obj);
	}

	@Override
	public void notifyDestroy(UIMeta uimeta, PageMeta pagemeta, Object obj) {
		super.notifyDestroy(uimeta, pagemeta, obj);
	}

	@Override
	public void notifyRemoveChild(UIMeta uimeta, PageMeta pagemeta, Object obj) {
		super.notifyRemoveChild(uimeta, pagemeta, obj);
	}

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyUpdate(uiMeta, pageMeta, obj);
	}
}
