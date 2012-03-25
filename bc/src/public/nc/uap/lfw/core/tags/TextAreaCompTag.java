package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.comp.TextAreaComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 文本域控件tag
 * @author gd 2007-11-26
 */
public class TextAreaCompTag extends NormalComponentTag{

	public String generateBodyScript() {
		WebComponent component = this.getComponent();
		if(!(component instanceof TextAreaComp))
		      throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "TextAreaCompTag-000000", null, new String[]{this.getId()})/*标签配置出错，{0}不是TextAreaComp类型！*/);
		
		TextAreaComp textArea = (TextAreaComp)component;
		StringBuffer buf = new StringBuffer();
		//TextAreaComp(parent, name, left, top, rows, cols, position, readOnly, value, width, height)
		String id = getVarShowId();
		buf.append("window.")
		       .append(id);
		if (textArea.isShowMark())
			buf.append(" = new TextMarkComp(");
		else
		    buf.append(" = new TextAreaComp(");
		buf.append("document.getElementById('" + getDivShowId())
	           .append("'), '" + this.getId())
	           .append("',")
	           .append(getLeft())
	           .append(",")
	           .append(getTop())
	           .append(",")
	           .append(textArea.getRows())
	           .append(",")
	           .append(textArea.getCols())
	           .append(",'")
	           .append(getPosition())
	           .append("',")
	           .append(textArea.isReadOnly())
	           .append(",'")
	           .append("")
	           .append("','")
	           .append(getWidth())
	           .append("','")
	           .append(getHeight())
	           .append("',")
	           .append(textArea.getTip() == null ? "null" : "'" + textArea.getTip() + "'")
	           .append(",'")
	           .append("")
		       .append("');\n");
		
		String value=textArea.getValue();
		value=value.replaceAll("\r\n", "\n");
		value=value.replaceAll("\\n", "\r\n");
		value=value.replaceAll("\r\n", "\\\\\\r\\\\\\n");
		buf.append(id+".setValue('"+value+"');\n");
		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + id + ");\n");
		
		if (textArea.isEnabled() == false)
			buf.append(id)
		       .append(".setActive(false);\n");
		buf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + id + ");\n");
		return buf.toString();
	}

	@Override
	protected String getSourceType(WebElement ele) {
		// TODO Auto-generated method stub
		return null;
	}

}
