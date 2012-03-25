package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.comp.RadioComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * Radio控件tag类
 * @author gd 2007-11-26
 *
 */
public class RadioCompTag extends NormalComponentTag{
	
	public String generateBodyScript() {
		WebComponent component = this.getComponent();
		if(!(component instanceof RadioComp))
		      throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "RadioCompTag-000000", null, new String[]{this.getId()})/*标签配置出错，{0}不是RadioComp类型！*/);
		
		RadioComp radio = (RadioComp)component;
		StringBuffer buf = new StringBuffer();
		//RadioComp(parent, name, left, top, group, value, text, checked, position, className)
		String radiocompId = getVarShowId();
		buf.append("window.")
	       .append(radiocompId)
	       .append(" = new RadioComp(")
		   .append("document.getElementById('" + getDivShowId())
        .append("'), '" + this.getId())
        .append("',")
        .append(getLeft())
        .append(",")
        .append(getTop())
        .append(",'")
        .append(radio.getGroup())
        .append("','")
        .append(radio.getValue())
        .append("','")
        .append(radio.getText())
        .append("',")
        .append(radio.isChecked())
        .append(",'")
        .append(getPosition())
        .append("','")
        .append("")
	    .append("');\n");
	
		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + radiocompId + ");\n"); 
		return buf.toString();
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return null;
	}
}
