package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.comp.EditorComp;
import nc.uap.lfw.core.comp.WebElement;

/**
 * ±à¼­Æ÷¿Ø¼þTag
 * @author dengjt
 *
 */
public class EditorCompTag extends NormalComponentTag {

	public String generateBodyScript() {
		StringBuffer buf = new StringBuffer();
		EditorComp editor = (EditorComp) getComponent();
		//function EditorComp(parent, name, left, top, width, height, position, hideBarIndices, hideImageIndices, className)
		buf.append("window.")
		   .append(COMP_PRE + editor.getId())
		   .append(" = new EditorComp(document.getElementById('" + getDivShowId() + "'),'")
		   .append(editor.getId())
		   .append("0,0,'100%','100%','relative',[")
		   .append(editor.getHideBarIndices())
		   .append("],[")
		   .append(editor.getHideImageIndices())
		   .append("],null);\n");
		return buf.toString();
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return null;
	}

}
