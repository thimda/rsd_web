package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.comp.CheckboxGroupComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
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
 * @author renxh
 * checkbox组渲染器
 * @param <T>
 * @param <K>
 */
public class PCCheckboxGroupCompRender extends UINormalComponentRender<UITextField, CheckboxGroupComp> {

	public PCCheckboxGroupCompRender(UITextField uiEle,CheckboxGroupComp webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<? extends UIElement, ? extends WebElement> parentPanel) {
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
		if (!(component instanceof CheckboxGroupComp))
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "RadioCompTag-000000", null,
					new String[] { this.getId() })/* 标签配置出错，{0}不是RadioComp类型！ */);

		CheckboxGroupComp cbg = (CheckboxGroupComp) component;
		String id = getVarId();
		String widgetId = this.getCurrWidget().getId();
		StringBuffer buf = new StringBuffer();
		// CheckboxGroupComp(parent, name, left, top, width, position, attrArr,
		// className)
		buf.append("window.").append(id).append(" = new CheckboxGroupComp(").append("document.getElementById('" + getDivId()).append(
				"'), '" + this.getId()).append("',0,0,'100%','relative',");
		String attrArr = generateAttrArr(cbg);
		if (null != attrArr)
			buf.append(attrArr);
		else
			buf.append("null");
		buf.append(",").append(uiComp.getClassName() == null ? null : "'" + uiComp.getClassName() + "'").append(");\n");

		buf.append("pageUI.getWidget('" + widgetId + "').addComponent(" + id + ");\n");

		// 加载子项
		String cbId = COMBO_PRE + widgetId + "_" + cbg.getComboDataId();
		buf.append(id).append(".setComboData(").append(cbId).append(",").append(cbg.getSepWidth()).append(");\n");

		if (cbg.getValue() != null) {
			buf.append(id).append(".setValue('").append(cbg.getValue()).append("');\n");
		}
		if (!cbg.isVisible()) {
			buf.append(id).append(".hideV();\n");
		}
		if (cbg.isReadOnly()) {
			buf.append(id).append(".setReadOnly(true);\n");
		}
		return buf.toString();
	}
	
	
//	@Override
//	public String generateBodyScriptDynamic() {
//		WebComponent component = this.getWebElement();
//		UIComponent uiComp = this.getUiElement();
//		if (!(component instanceof CheckboxGroupComp))
//			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "RadioCompTag-000000", null,
//					new String[] { this.getId() })/* 标签配置出错，{0}不是RadioComp类型！ */);
//
//		CheckboxGroupComp cbg = (CheckboxGroupComp) component;
//		String id = getVarId();
//		String widgetId = this.getCurrWidget().getId();
//		StringBuffer buf = new StringBuffer();
//		// CheckboxGroupComp(parent, name, left, top, width, position, attrArr,
//		// className)
//		buf.append("window.").append(id).append(" = new CheckboxGroupComp(").append(getDivId());
//		buf.append(", '" + this.getId()).append("',0,0,'100%','relative',");
//		
//		String attrArr = generateAttrArr(cbg);
//		if (null != attrArr){
//			buf.append(attrArr);
//		}else{
//			buf.append("null");
//		}
//		buf.append(",").append(uiComp.getClassName() == null ? null : "'" + uiComp.getClassName() + "'").append(");\n");
//
//		buf.append("pageUI.getWidget('" + widgetId + "').addComponent(" + id + ");\n");
//
//		// 加载子项
//		String cbId = COMBO_PRE + widgetId + "_" + cbg.getComboDataId();
//		buf.append(id).append(".setComboData(").append(cbId).append(",").append(cbg.getSepWidth()).append(");\n");
//
//		if (cbg.getValue() != null) {
//			buf.append(id).append(".setValue('").append(cbg.getValue()).append("');\n");
//		}
//		if (!cbg.isVisible()) {
//			buf.append(id).append(".hideV();\n");
//		}
//		if (cbg.isReadOnly()) {
//			buf.append(id).append(".setReadOnly(true);\n");
//		}
//		return buf.toString();
//	}

	private String generateAttrArr(CheckboxGroupComp cbg) {
		StringBuffer buf = new StringBuffer();
		buf.append("{").append("'labelText':").append(cbg.getText() == null ? null : "'" + cbg.getText() + "'").append(",'labelAlign':").append(
				cbg.getTextAlign() == null ? null : "'" + cbg.getTextAlign() + "'").append(",'labelWidth':").append(cbg.getTextWidth()).append(
				",'disabled':").append(!cbg.isEnabled()).append(",'readOnly':").append(cbg.isReadOnly()).append(",'tabIndex':").append(
				cbg.getTabIndex()).append(",'changeLine':").append(cbg.isChangeLine()).append("}");
		return buf.toString();
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_TEXT;
	}


	@Override
	protected String getRenderType(WebElement ele) {
		return EditorTypeConst.CHECKBOXGROUP;
	}
	
	@Override
	public String getType() {
		return "checkboxgrouptext";
	}

}
