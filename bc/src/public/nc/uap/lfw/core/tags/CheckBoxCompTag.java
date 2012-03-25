package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.CheckBoxComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.ml.NCLangRes4VoTransl;

public class CheckBoxCompTag extends NormalComponentTag {

	public String generateBodyScript() {
		WebComponent component = this.getComponent();
		if(!(component instanceof CheckBoxComp))
		      throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "CheckBoxCompTag-000000", null, new String[]{this.getId()})/*，{0}不是CheckBoxComp类型！*/);
		
		CheckBoxComp cb = (CheckBoxComp)component;
		StringBuffer buf = new StringBuffer();
		String checkboxId = getVarShowId();
		buf.append("window.")
	       .append(checkboxId)
	       .append(" = new CheckboxComp(")
		   .append("document.getElementById('" + getDivShowId())
        .append("'), '" + this.getId())
        .append("',")
        .append(getLeft())
        .append(",")
        .append(getTop())
        .append(",'")
        .append(getWidth())
        .append("','")
        .append(translate(cb.getI18nName(), cb.getText(), cb.getLangDir()))
        .append("',")
        .append(cb.isChecked())
        .append(",'")
        .append(getPosition())
        .append("','")
        .append("")
	    .append("');\n");
		
		String dataType = cb.getDataType();
			buf.append(checkboxId)
				.append(".setValuePair(");
		if(dataType.equals(StringDataTypeConst.BOOLEAN)|| dataType.equals(StringDataTypeConst.bOOLEAN))
			buf.append("[\"true\",\"false\"]");
		else if(dataType.equals(StringDataTypeConst.UFBOOLEAN))
			buf.append("['Y','N']");
		
			buf.append(");\n");
			
			buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + checkboxId + ");\n"); 
			
		return buf.toString();
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_TEXT;
	}


}
