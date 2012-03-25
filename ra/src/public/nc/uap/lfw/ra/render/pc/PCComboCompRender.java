package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.text.ComboBoxComp;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UITextField;
import nc.uap.lfw.ra.render.UINormalComponentRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh
 * 组合框渲染器
 * @param <T>
 * @param <K>
 */
public class PCComboCompRender extends UINormalComponentRender<UITextField, ComboBoxComp> {

	public PCComboCompRender(UITextField uiEle,ComboBoxComp webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<? extends UIElement, ? extends WebElement> parentPanel) {
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
		ComboBoxComp comboComp = this.getWebElement();
		UIComponent uiComp = this.getUiElement();
		String comboId = getVarId();
		buf.append("var ").append(comboId);
		if (comboComp.isShowMark())
			buf.append(" = new TextMarkComp(");
		else
			buf.append(" = new ComboComp(");
		buf.append("document.getElementById('").append(getDivId()).append("'),'").append(comboComp.getId()).append("','0','0','100%','relative',")
				.append(comboComp.isSelectOnly()).append(",{'disabled':").append(comboComp.isEnabled() ? "false" : "true").append(",'readOnly':")
				.append(comboComp.isReadOnly() ? "true" : "false");

		if (comboComp.getDataDivHeight() != null && !"".equals(comboComp.getDataDivHeight()))
			buf.append(",'dataDivHeight':'" + comboComp.getDataDivHeight() + "'");
		if (comboComp.isAllowExtendValue() == true)
			buf.append(",'allowExtendValue':true");

		if (null != comboComp.getText() && !"".equals(comboComp.getText())) { // 有标签属性
			buf.append(",'labelText':'").append(comboComp.getText()).append("','labelAlign':'").append(comboComp.getTextAlign()).append(
					"','labelWidth':").append(comboComp.getTextWidth());

			if (comboComp.getValue() != null && !"".equals(comboComp.getValue())) {
				if (buf.length() > 1)
					buf.append(",");
				buf.append("'value':'").append(comboComp.getValue()).append("'");
			}
		}

		buf.append("}");

		buf.append(",'").append(uiComp.getClassName() == null ? "" : uiComp.getClassName()).append("');\n");

		// 隐藏下拉框
		if (comboComp.isVisible() == false) {
			buf.append(getVarId()).append(".hideV();\n");
		}

		buf.append(addRefItemScript(comboComp));

		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + comboId + ");\n");
		return buf.toString();
	}
	
//	@Override
//	public String generateBodyScriptDynamic() {
//		StringBuffer buf = new StringBuffer();
//		ComboBoxComp comboComp = this.getWebElement();
//		UIComponent uiComp = this.getUiElement();
//		String comboId = getVarId();
//		buf.append("var ").append(comboId);
//		if (comboComp.isShowMark()){
//			buf.append(" = new TextMarkComp(");
//		}else{
//			buf.append(" = new ComboComp(");
//		}
//		buf.append(getDivId()).append(",'").append(comboComp.getId()).append("','0','0','100%','relative',");
//		buf.append(comboComp.isSelectOnly()).append(",{'disabled':");
//		buf.append(comboComp.isEnabled() ? "false" : "true");
//		buf.append(",'readOnly':").append(comboComp.isReadOnly() ? "true" : "false");
//
//		if (comboComp.getDataDivHeight() != null && !"".equals(comboComp.getDataDivHeight()))
//			buf.append(",'dataDivHeight':'" + comboComp.getDataDivHeight() + "'");
//		if (comboComp.isAllowExtendValue() == true){
//			buf.append(",'allowExtendValue':true");
//		}
//
//		if (null != comboComp.getText() && !"".equals(comboComp.getText())) { // 有标签属性
//			buf.append(",'labelText':'").append(comboComp.getText());
//			buf.append("','labelAlign':'").append(comboComp.getTextAlign());
//			buf.append("','labelWidth':").append(comboComp.getTextWidth());
//
//			if (comboComp.getValue() != null && !"".equals(comboComp.getValue())) {
//				if (buf.length() > 1){
//					buf.append(",");
//				}
//				buf.append("'value':'").append(comboComp.getValue()).append("'");
//			}
//		}
//
//		buf.append("}");
//
//		buf.append(",'").append(uiComp.getClassName() == null ? "" : uiComp.getClassName()).append("');\n");
//
//		// 隐藏下拉框
//		if (comboComp.isVisible() == false) {
//			buf.append(getVarId()).append(".hideV();\n");
//		}
//
//		buf.append(addRefItemScript(comboComp));
//
//		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + comboId + ");\n");
//		return buf.toString();
//	}

	/**
	 * 2011-8-2 下午08:12:33 renxh
	 * des：添加引用条目的脚本
	 * @param combo
	 * @return
	 */
	private String addRefItemScript(ComboBoxComp combo) {

		StringBuffer buf = new StringBuffer();
		if (!combo.isShowMark()) {
			buf.append(getVarId()).append(".setShowImgOnly(").append(combo.isImageOnly() ? "true" : "false").append(");\n");
			if(combo.getRefComboData() != null){
				String cbId = COMBO_PRE + getCurrWidget().getId() + "_" + combo.getRefComboData();
				buf.append(getVarId()).append(".setComboData(").append(cbId).append(");\n");
			}
			
			if (combo.getValue() != null && !"".equals(combo.getValue())) {
				buf.append(getVarId()).append(".setValue('").append(combo.getValue()).append("');\n");
			}

			if (combo.isReadOnly() == true)
				buf.append(getVarId()).append(".setReadOnly(true);\n");

			if (combo.isEnabled() == false)
				buf.append(getVarId()).append(".setActive(false);\n");

			if (combo.isVisible() == false)
				buf.append(getVarId()).append(".hideV();\n");
		}
		return buf.toString();
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_TEXT;
	}
	
	@Override
	protected String getRenderType(WebElement ele) {
		return EditorTypeConst.COMBODATA;
	}

	public String getType(){
		return "combotext";
	}
}
