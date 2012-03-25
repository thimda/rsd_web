package nc.uap.lfw.design.itf;

import java.io.Serializable;

public class MdComponnetVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String ownmodule;
	private String displayname;
	private String namespace;
	private String displayId;
	private String ts;
	private String version;
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
	public String getOwnmodule() {
		return ownmodule;
	}
	public void setOwnmodule(String ownmodule) {
		this.ownmodule = ownmodule;
	}
	public String getDisplayname() {
		return displayname;
	}
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getDisplayId() {
		return displayId;
	}
	public void setDisplayId(String displayId) {
		this.displayId = displayId;
	}
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
}
