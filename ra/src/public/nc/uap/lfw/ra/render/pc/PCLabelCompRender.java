package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.LabelComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UILabelComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.render.UINormalComponentRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh ±Í«©‰÷»æ∆˜
 * @param <T>
 * @param <K>
 */
public class PCLabelCompRender extends UINormalComponentRender<UILabelComp, LabelComp> {

	public PCLabelCompRender(UILabelComp uiEle,LabelComp webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<? extends UIElement, ? extends WebElement> parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);
	}
	
	@Override
	public String generateBodyHtml() {
		return super.generateBodyHtml();
	}
	
	@Override
	public String generateBodyHtmlDynamic() {
		return super.generateBodyHtmlDynamic();
	}
	
	@Override
	public String generateBodyScript() {
		StringBuffer buf = new StringBuffer();
		LabelComp label = this.getWebElement();
		UIComponent uiComp = this.getUiElement();
		String labelId = getVarId();
		String text = translate(label.getI18nName(), label.getText(), label.getLangDir());
		if (text == null)
			text = "";
		buf.append("var ").append(labelId).append(
				" = new LabelComp(document.getElementById('" + getDivId() + "'),'" + label.getId() + "','0','0','" + text + "','relative','"
						+ uiComp.getClassName() + "'");
		buf.append(", " + generateAttrArr(label, (UILabelComp)uiComp));
		buf.append(");\n");
		if (label.getColor() != null)
			buf.append(labelId + ".setColor('" + label.getColor() + "');\n");
		if (label.getInnerHTML() != null)
			buf.append(labelId + ".setInnerHTML('" + label.getInnerHTML() + "');\n");
		if (!label.isVisible())
			buf.append(labelId + ".hide();\n");
		else
			buf.append(labelId + ".show();\n");
		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + labelId + ");\n");
		return buf.toString();
	}
	
	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_LABEL;
	}

	private String generateAttrArr(LabelComp comp, UILabelComp uicomp) {
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		if (uicomp.getTextAlign() != null && uicomp.getTextAlign().trim().length() > 0) {
			buf.append("'textAlign':'").append(uicomp.getTextAlign()).append("'");
		}
		buf.append("}");
		return buf.toString();
	}
	
}
