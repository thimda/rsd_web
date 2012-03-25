package nc.uap.lfw.jsutil.jstools.vo;

import java.util.ArrayList;

public class ConfObject {
	private ArrayList<CompGroup> groupList;
	private boolean isScript = true;
	private String path = null;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ArrayList<CompGroup> getGroupList() {
		return groupList;
	}

	public void setGroupList(ArrayList<CompGroup> groupList) {
		this.groupList = groupList;
	}
	
	public void addCompGroup(CompGroup group)
	{
		if(groupList == null)
			groupList = new ArrayList<CompGroup>();
		groupList.add(group);
	}

	public boolean isScript() {
		return isScript;
	}

	public void setScript(boolean isScript) {
		this.isScript = isScript;
	}
}
