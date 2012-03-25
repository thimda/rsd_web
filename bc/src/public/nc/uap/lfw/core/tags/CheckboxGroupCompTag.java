package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.comp.CheckboxGroupComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * @author guoweic
 *
 */
public class CheckboxGroupCompTag extends NormalComponentTag {

	public String generateBodyScript() {
		WebComponent component = this.getComponent();
		if(!(component instanceof CheckboxGroupComp))
		      throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "RadioCompTag-000000", null, new String[]{this.getId()})/*标签配置出错，{0}不是RadioComp类型！*/);
		
		CheckboxGroupComp cbg = (CheckboxGroupComp)component;
		String id = getVarShowId();
		String widgetId = this.getCurrWidget().getId();
		StringBuffer buf = new StringBuffer();
//		CheckboxGroupComp(parent, name, left, top, width, position, attrArr, className)
		buf.append("window.")
	       .append(id)
	       .append(" = new CheckboxGroupComp(")
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
		String attrArr = generateAttrArr(cbg);
		  if (null != attrArr)
			  buf.append(attrArr);
		  else
			  buf.append("null");
        buf.append(",")
           .append("null")
	       .append(");\n");
        
        buf.append("pageUI.getWidget('" + widgetId + "').addComponent(" + id + ");\n");
        
        // 加载子项
        String cbId = COMBO_PRE + widgetId + "_" + cbg.getComboDataId();
        buf.append(id)
           .append(".setComboData(")
 	       .append(cbId)
 	       .append(",")
 	       .append(cbg.getSepWidth())
 	       .append(");\n");
        
        if(cbg.getValue() != null){
        	buf.append(id)
        	  .append(".setValue('")
        	  .append(cbg.getValue())
        	  .append("');\n");
        }
        if(!cbg.isVisible()){
        	buf.append(id).append(".hideV();\n");
        }
        if(cbg.isReadOnly()){
        	buf.append(id).append(".setReadOnly(true);\n");
        }
		return buf.toString();
	}
	
	private String generateAttrArr(CheckboxGroupComp cbg) {
		StringBuffer buf = new StringBuffer();
		buf.append("{")
		   .append("'labelText':")
		   .append(cbg.getText() == null ? null : "'" + cbg.getText() + "'")
		   .append(",'labelAlign':")
		   .append(cbg.getTextAlign() == null ? null : "'" + cbg.getTextAlign() + "'")
		   .append(",'labelWidth':")
		   .append(cbg.getTextWidth())
		   .append(",'disabled':")
		   .append(!cbg.isEnabled())
		   .append(",'readOnly':")
		   .append(cbg.isReadOnly())
		   .append(",'tabIndex':")
		   .append(cbg.getTabIndex())
		   .append(",'changeLine':")
		   .append(cbg.isChangeLine())
		   .append("}");
			return buf.toString();
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_TEXT;
	}

}
