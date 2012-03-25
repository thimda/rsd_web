package nc.uap.lfw.design.itf;

import java.io.Serializable;

public class MdClassVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String componentid;
	private String classtype;
	private String displayname;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getComponentid() {
		return componentid;
	}
	public void setComponentid(String componentid) {
		this.componentid = componentid;
	}
	public String getClasstype() {
		return classtype;
	}
	public void setClasstype(String classtype) {
		this.classtype = classtype;
	}
	public String getDisplayname() {
		return displayname;
	}
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccessorclassname() {
		return accessorclassname;
	}
	public void setAccessorclassname(String accessorclassname) {
		this.accessorclassname = accessorclassname;
	}
	private String name;
	private String accessorclassname;
	
	
}
