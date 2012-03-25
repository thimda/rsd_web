package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.text.ComboBoxComp;
import nc.uap.lfw.core.event.ctx.LfwPageContext;


/**
 * 下拉控件的标签类
 * 
 */
public class ComboCompTag extends NormalComponentTag {
	
	
	public String generateBody() {
		return super.generateBody();
	}

	public String generateBodyScript() {
		//parent, name, left, top, width, position, selectOnly, attrArr, className
		StringBuffer buf = new StringBuffer();
		ComboBoxComp comboComp = (ComboBoxComp) getComponent();
		String comboId = getVarShowId();
		buf.append("var ")
		   .append(comboId);
		if (comboComp.isShowMark())
			buf.append(" = new TextMarkComp(");
		else
			buf.append(" = new ComboComp(");
		buf.append("document.getElementById('")
		   .append(getDivShowId())
		   .append("'),'")
		   .append(comboComp.getId())
		   .append("','0','0','")
		   .append(getWidth())
		   .append("','")
		   .append(getPosition())
		   .append("',")
		   .append(comboComp.isSelectOnly())
		   .append(",{'disabled':")
		   .append(comboComp.isEnabled()? "false" : "true")
		   .append(",'readOnly':")
		   .append(comboComp.isReadOnly() ? "true" : "false");
		
		if (comboComp.getDataDivHeight() != null && !"".equals(comboComp.getDataDivHeight()))
			buf.append(",'dataDivHeight':'" + comboComp.getDataDivHeight() + "'");
		if (comboComp.isAllowExtendValue() == true)
			buf.append(",'allowExtendValue':true");
		
		if (null != comboComp.getText() && !"".equals(comboComp.getText())) {  // 有标签属性
			buf.append(",'labelText':'")
			  // .append(comboComp.getText())
			   .append(translate(comboComp.getI18nName(), comboComp.getText(), comboComp.getLangDir()))
			   .append("','labelAlign':'")
			   .append(comboComp.getTextAlign())
			   .append("','labelWidth':")
			   .append(comboComp.getTextWidth());
			
		if (comboComp.getValue() != null && !"".equals(comboComp.getValue())) {
			if (buf.length() > 1)
				buf.append(",");
			buf.append("'value':'")
			   .append(comboComp.getValue())
			   .append("'");
		}
	}
	   
	   buf.append("}");
	   
	   buf.append(",'")
	      .append("")
	      .append("');\n");
		
		// 隐藏下拉框
		if(comboComp.isVisible() == false) {
			buf.append(getVarShowId())
			   .append(".hideV();\n");
		}
		
		buf.append(addRefItemScript(comboComp));
		
		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + comboId + ");\n");
		return buf.toString();
	}
	
	
	private String addRefItemScript(ComboBoxComp combo){
		
		StringBuffer buf = new StringBuffer();
		if (!combo.isShowMark()) {
			buf.append(getVarShowId())
			   .append(".setShowImgOnly(")
			   .append(combo.isImageOnly() ? "true" : "false")
			   .append(");\n");
			String cbId = COMBO_PRE + getCurrWidget().getId() + "_" + combo.getRefComboData();
			buf.append(getVarShowId())
			   .append(".setComboData(")
			   .append(cbId)
			   .append(");\n");
			
			if (combo.getValue() != null && !"".equals(combo.getValue())) {
				buf.append(getVarShowId())
				   .append(".setValue('")
				   .append(combo.getValue())
				   .append("');\n");
			}
	
			if (combo.isReadOnly() == true)
				buf.append(getVarShowId())
			       .append(".setReadOnly(true);\n");
			
			if (combo.isEnabled() == false)
				buf.append(getVarShowId())
			       .append(".setActive(false);\n");
			
			if (combo.isVisible() == false)
				buf.append(getVarShowId())
			       .append(".hideV();\n");
		}
		return buf.toString();
	}
	

	@Override
	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_TEXT;
	}
}
