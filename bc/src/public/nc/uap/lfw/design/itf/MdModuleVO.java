package nc.uap.lfw.design.itf;

import java.io.Serializable;

public class MdModuleVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String displayname;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayname() {
		return displayname;
	}
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

}
