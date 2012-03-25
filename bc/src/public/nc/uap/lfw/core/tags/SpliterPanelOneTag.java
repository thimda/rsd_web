package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.tags.ContainerElementTag;

/**
 * ∑÷∏Ó√Ê∞Â1
 * @author dengjt, guoweic
 *
 */
public class SpliterPanelOneTag extends ContainerElementTag{

	public String generateHead() {
		StringBuffer buf = new StringBuffer();
		SpliterTag parent = (SpliterTag) getParent();
		buf.append("<div id=\"")
//		   .append(DIV_PRE)
		   .append(parent.getDivShowId())
		   .append("_div_")
		   .append(getDivIndex())
		   .append("\" style=\"width:100%;height:100%;\">");
		return buf.toString();
	}

	public String generateTail() {
		return "</div>";
	}

	public String generateHeadScript() {
		SpliterTag parent = (SpliterTag) getParent();
		StringBuffer buf = new StringBuffer();
//		buf.append(COMP_PRE)
		buf.append(parent.getVarShowId())
		   .append(".getDiv")
		   .append(getDivIndex())
		   .append("().add(document.getElementById(\"")
//		   .append(DIV_PRE)
		   .append(parent.getDivShowId())
		   .append("_div_")
		   .append(getDivIndex())
		   .append("\"));\n");
		return buf.toString();
	}
	
	protected String getDivIndex()
	{
		return "1";
	}

}
