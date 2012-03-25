package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.comp.RadioGroupComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UITextField;
import nc.uap.lfw.ra.render.UINormalComponentRender;
import nc.uap.lfw.ra.render.UIRender;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * @author renxh Radio集合组件渲染器
 * 
 */
public class PCRadioGroupCompRender extends UINormalComponentRender<UITextField, RadioGroupComp> {

	public PCRadioGroupCompRender(UITextField uiEle,RadioGroupComp webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<? extends UIElement, ? extends WebElement> parentPanel) {
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
		WebComponent component = this.getWebElement();
		UIComponent uiComp = this.getUiElement();
		if (!(component instanceof RadioGroupComp))
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "RadioCompTag-000000", null,
					new String[] { this.getId() })/* 标签配置出错，{0}不是RadioComp类型！ */);

		RadioGroupComp rg = (RadioGroupComp) component;
		String id = getVarId();
		String widgetId = this.getCurrWidget().getId();
		StringBuffer buf = new StringBuffer();
		// RadioGroupComp(parent, name, left, top, width, position, attrArr,
		// className)
		buf.append("window.").append(id).append(" = new RadioGroupComp(");
		buf.append("document.getElementById('" + getDivId());
		buf.append("'), '" + this.getId());
		buf.append("',0,0,'100%','relative',");
		String attrArr = generateAttrArr(rg);
		if (null != attrArr)
			buf.append(attrArr);
		else
			buf.append("null");
		
		String className = uiComp.getClassName();
		buf.append(",").append(className == null ? null : "'" + className + "'").append(");\n");

		buf.append("pageUI.getWidget('" + widgetId + "').addComponent(" + id + ");\n");

		// 加载子项
		String cbId = COMBO_PRE + widgetId + "_" + rg.getComboDataId();
		buf.append(id).append(".setComboData(").append(cbId).append(",").append(rg.getSepWidth()).append(");\n");

		if (rg.getValue() != null) {
			buf.append(id).append(".setValue('").append(rg.getValue()).append("');\n");
		}
		if (!rg.isVisible()) {
			buf.append(id).append(".hideV();\n");
		}
		if (rg.isReadOnly()) {
			buf.append(id).append(".setReadOnly(true);\n");
		}
		return buf.toString();
	}

	private String generateAttrArr(RadioGroupComp rg) {
		StringBuffer buf = new StringBuffer();
		buf.append("{").append("'labelText':").append(rg.getText() == null ? null : "'" + rg.getText() + "'");
		buf.append(",'labelAlign':").append(rg.getTextAlign() == null ? null : "'" + rg.getTextAlign() + "'");
		buf.append(",'labelWidth':").append(rg.getTextWidth());
		buf.append(",'disabled':").append(!rg.isEnabled());
		buf.append(",'readOnly':").append(rg.isReadOnly());
		buf.append(",'tabIndex':").append(rg.getTabIndex());
		buf.append(",'changeLine':").append(rg.isChangeLine()).append("}");
		return buf.toString();
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_TEXT;
	}
	
	@Override
	protected String getRenderType(WebElement ele) {
		return EditorTypeConst.RADIOGROUP;
	}
	public String getType(){
		return "radiogrouptext";
	}
}
