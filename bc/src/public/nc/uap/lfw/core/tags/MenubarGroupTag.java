package nc.uap.lfw.core.tags;


public class MenubarGroupTag extends ContainerElementTag {

	@Override
	public String generateHead() {
		StringBuffer buf = new StringBuffer();
		buf.append("<div id=\"")
		   .append(DIV_PRE)
		   .append(getId())
		   .append("\" style=\"width:100%;height:30px")
		   .append(";top:0px;left:0px;position:relative;\">");
		return buf.toString();
	}

	@Override
	public String generateHeadScript() {
		// MenuBarGroup(parent, name, left, top, width, height, position, className)
		StringBuffer buf = new StringBuffer();
		String id = COMP_PRE + getId();
		buf.append("window." + id + " = new MenuBarGroup('")
		   .append(getId())
		   .append("');\n");
		buf.append("pageUI.addMenubarGroup(window." + id + ");\n");
		return buf.toString();
	}

	@Override
	public String generateTail() {
		return "</div>";
	}
	
}
