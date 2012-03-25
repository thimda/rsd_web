package nc.uap.lfw.core.tags;

public class MenubarGroupItemTag extends ContainerElementTag {

	private String state = null;
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String addMenuBar(String menubarId) {
		StringBuffer sb = new StringBuffer();
		MenubarGroupTag tab = (MenubarGroupTag) findAncestorWithClass(this, MenubarGroupTag.class);
		String groupId = COMP_PRE + tab.getId();
		sb.append(groupId)
		  .append(".addItem('")
		  .append(getState())
		  .append("', ")
		  .append(menubarId)
		  .append(");\n");
		return sb.toString();
	}
}
