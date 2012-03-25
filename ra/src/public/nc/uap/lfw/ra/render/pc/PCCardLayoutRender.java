package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UICardLayout;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UpdatePair;
import nc.uap.lfw.ra.render.UILayoutRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh 卡片布局渲染器
 * @param <T>
 * @param <K>
 */
public class PCCardLayoutRender extends UILayoutRender<UICardLayout, WebElement> {

	// 卡片布局ID基础字符串
	protected static final String CARD_ID_BASE = "card_layout_";

	public PCCardLayoutRender(UICardLayout uiEle, UIMeta uimeta, PageMeta pageMeta, UIRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		this.setPageMeta(pageMeta);
		
		divId = DIV_PRE + CARD_ID_BASE + id;
		if (getCurrWidget() == null)
			varId = COMP_PRE + getId();
		else
			varId = COMP_PRE + getCurrWidget().getId() + "_" + getId();
		this.setCardIndex(Integer.parseInt(uiEle.getCurrentItem()));

	}

	// 初始时显示的页的顺序
	private int cardIndex = 0;

	// 用来完成CardPanel的计数
	private int cardCount = 0;

	public String generalHeadHtml() {
		return super.generalHeadHtml();
	}

	public String generalHeadScript() {
		return toResize("$ge(\""+getDivId()+"\")", "cardResize");
	}
	

	public String generalTailHtml() {
		return super.generalTailHtml();
	}

	public String generalTailScript() {

		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getVarId()).append(" = new CardLayout(").append("\"").append(getId()).append("\",");
		buf.append("document.getElementById(\"").append(getDivId()).append("\"),").append(cardIndex).append(");\n");

		if (this.getWidget() != null) {
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append("var ").append(widget).append(" = pageUI.getWidget('").append(this.getCurrWidget().getId()).append("');\n");
			buf.append(widget + ".addCard(" + getVarId() + ");\n");
		} else
			buf.append("pageUI.addCard(" + getVarId() + ");\n");
		return buf.toString();
	}

	public int getCardIndex() {
		return cardIndex;
	}

	public void setCardIndex(int cardIndex) {
		this.cardIndex = cardIndex;
	}

	public int getCardCount() {
		return cardCount;
	}

	public void setCardCount(int cardCount) {
		this.cardCount = cardCount;
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_CARDLAYOUT;
	}
	
	
	

	@Override
	public String generalHeadHtmlDynamic() {
		return super.generalHeadHtmlDynamic();
	}

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		StringBuffer buf = new StringBuffer();		
		if (this.getWidget() != null) {
			buf.append("var currForm = pageUI.getWidget('").append(this.widget).append("');\n");
			buf.append("var ").append(getVarId()).append(" = currForm.getCard('"+getId()+"');\n");
		} else{
			buf.append("var ").append(getVarId()).append(" = pageUI.getCard('" + getId() + "');\n");
		}
		UpdatePair pair = (UpdatePair) obj;
		if(pair == null)
			return;
		
		if(pair.getKey() != null && pair.getKey().equals(UICardLayout.CURRENT_ITEM)){
			buf.append("if("+getVarId()+"){\n");
			buf.append(getVarId()).append(".setPage("+pair.getValue()+");\n");
			buf.append("};\n");		
		}
		
		buf.append("layoutInitFunc();\n");		
		addBeforeExeScript(buf.toString());
	}
}
