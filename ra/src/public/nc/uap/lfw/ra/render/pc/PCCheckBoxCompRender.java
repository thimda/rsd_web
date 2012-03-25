package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.CheckBoxComp;
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
 * checkbox控件渲染器
 * @param <T>
 * @param <K>
 */
public class PCCheckBoxCompRender extends UINormalComponentRender<UITextField, CheckBoxComp> {

	public PCCheckBoxCompRender(UITextField uiEle,CheckBoxComp webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<? extends UIElement, ? extends WebElement> parentPanel) {
		super(uiEle,webEle, uimeta, pageMeta, parentPanel);
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
		if (!(component instanceof CheckBoxComp))
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "CheckBoxCompTag-000000", null,
					new String[] { this.getId() })/* ，{0}不是CheckBoxComp类型！ */);

		CheckBoxComp cb = (CheckBoxComp) component;
		StringBuffer buf = new StringBuffer();
		// function CheckboxComp(parent, name, left, top, width, text, checked,
		// position, attrArr)
		// window.$c_test = new CheckboxComp(document.getElementById('$d_test'),
		// 'test',0,0,100%,'haha',false,'relative','');
		String checkboxId = getVarId();
		buf.append("window.").append(checkboxId).append(" = new CheckboxComp(").append("document.getElementById('" + getDivId()).append(
				"'), '" + this.getId()).append("',0,0,'100%','").append(translate(cb.getI18nName(), cb.getText(), cb.getLangDir())).append("',").append(cb.isChecked()).append(",'relative','").append(uiComp.getClassName()).append("'");

		String attrArr = generateAttrArr(cb, uiComp);
		buf.append(",").append(attrArr).append(");\n");
		
		String dataType = cb.getDataType();
		buf.append(checkboxId).append(".setValuePair(");
		if (dataType.equals(StringDataTypeConst.BOOLEAN) || dataType.equals(StringDataTypeConst.bOOLEAN))
			buf.append("[\"true\",\"false\"]");
		else if (dataType.equals(StringDataTypeConst.UFBOOLEAN))
			buf.append("['Y','N']");

		buf.append(");\n");

		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + checkboxId + ");\n");

		return buf.toString();
	}
	
//	@Override
//	public String generateBodyScriptDynamic() {
//		WebComponent component = this.getWebElement();
//		UIComponent uiComp = this.getUiElement();
//		if (!(component instanceof CheckBoxComp))
//			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "CheckBoxCompTag-000000", null,
//					new String[] { this.getId() })/* ，{0}不是CheckBoxComp类型！ */);
//
//		CheckBoxComp cb = (CheckBoxComp) component;
//		StringBuffer buf = new StringBuffer();
//		String checkboxId = getVarId();
//		buf.append("window.").append(checkboxId).append(" = new CheckboxComp(").append(getDivId());
//		buf.append(", '" + this.getId()).append("',0,0,'100%','");
//		buf.append(translate(cb.getI18nName(), cb.getText(), cb.getLangDir())).append("',");
//		buf.append(cb.isChecked()).append(",'relative','");
//		buf.append(uiComp.getClassName()).append("'");
//
//		String attrArr = generateAttrArr(cb, uiComp);
//		buf.append(",").append(attrArr).append(");\n");
//		
//		String dataType = cb.getDataType();
//		buf.append(checkboxId).append(".setValuePair(");
//		if (dataType.equals(StringDataTypeConst.BOOLEAN) || dataType.equals(StringDataTypeConst.bOOLEAN))
//			buf.append("[\"true\",\"false\"]");
//		else if (dataType.equals(StringDataTypeConst.UFBOOLEAN))
//			buf.append("['Y','N']");
//
//		buf.append(");\n");
//
//		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + checkboxId + ");\n");
//
//		return buf.toString();
//	}

	protected String getSourceType(IEventSupport ele) {

		return LfwPageContext.SOURCE_TYPE_TEXT;
	}

	@Override
	protected String getRenderType(WebElement ele) {
		return EditorTypeConst.CHECKBOX;
	}
	
	@Override
	public String getType() {
		return "checkboxtext";
	}
	
	private String generateAttrArr(CheckBoxComp comp, UIComponent uiComp) {
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		if(comp != null){
			
		}
		if(uiComp != null){
			if(uiComp.getAttribute(UITextField.IMG_SRC) != null){
				buf.append("'imgsrc':'").append(uiComp.getAttribute(UITextField.IMG_SRC)).append("'");
			}
		}
		buf.append("}");
		return buf.toString();
	}

	@Override
	public void notifyAddChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyAddChild(uiMeta, pageMeta, obj);
	}

	@Override
	public void notifyDestroy(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyDestroy(uiMeta, pageMeta, obj);
	}

	@Override
	public void notifyRemoveChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyRemoveChild(uiMeta, pageMeta, obj);
	}

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyUpdate(uiMeta, pageMeta, obj);
	}
}
