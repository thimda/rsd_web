package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.comp.RadioGroupComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * @author guoweic
 *
 */
public class RadioGroupCompTag extends NormalComponentTag {


	public String generateBodyScript() {
		WebComponent component = this.getComponent();
		if(!(component instanceof RadioGroupComp))
		      throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "RadioCompTag-000000", null, new String[]{this.getId()})/*标签配置出错，{0}不是RadioComp类型！*/);
		
		RadioGroupComp rg = (RadioGroupComp)component;
		String id = getVarShowId();
		String widgetId = this.getCurrWidget().getId();
		StringBuffer buf = new StringBuffer();
//		RadioGroupComp(parent, name, left, top, width, position, attrArr, className)
		buf.append("window.")
	       .append(id)
	       .append(" = new RadioGroupComp(")
		   .append("document.getElementById('" + getDivShowId())
           .append("'), '" + this.getId())
           .append("',")
           .append(getLeft())
           .append(",")
           .append(getTop())
           .append(",'")
           .append(getWidth())
           .append("','")
           .append(getPosition())
           .append("',");
		String attrArr = generateAttrArr(rg);
		  if (null != attrArr)
			  buf.append(attrArr);
		  else
			  buf.append("null");
        buf.append(",")
           .append("null")
	       .append(");\n");
        
        buf.append("pageUI.getWidget('" + widgetId + "').addComponent(" + id + ");\n");
        
        // 加载子项
        String cbId = COMBO_PRE + widgetId + "_" + rg.getComboDataId();
        buf.append(id)
           .append(".setComboData(")
 	       .append(cbId)
 	       .append(",")
 	       .append(rg.getSepWidth())
 	       .append(");\n");
        
        if(rg.getValue() != null){
        	buf.append(id)
        	  .append(".setValue('")
        	  .append(rg.getValue())
        	  .append("');\n");
        }
        if(!rg.isVisible()){
        	buf.append(id).append(".hideV();\n");
        }
        if(rg.isReadOnly()){
        	buf.append(id).append(".setReadOnly(true);\n");
        }
		return buf.toString();
	}
	
	private String generateAttrArr(RadioGroupComp rg) {
		StringBuffer buf = new StringBuffer();
		buf.append("{")
		   .append("'labelText':")
		   .append(rg.getText() == null ? null : "'" + rg.getText() + "'")
		   .append(",'labelAlign':")
		   .append(rg.getTextAlign() == null ? null : "'" + rg.getTextAlign() + "'")
		   .append(",'labelWidth':")
		   .append(rg.getTextWidth())
		   .append(",'disabled':")
		   .append(!rg.isEnabled())
		   .append(",'readOnly':")
		   .append(rg.isReadOnly())
		   .append(",'tabIndex':")
		   .append(rg.getTabIndex())
		   .append(",'changeLine':")
		   .append(rg.isChangeLine())
		   .append("}");
			return buf.toString();
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_TEXT;
	}
	
}
