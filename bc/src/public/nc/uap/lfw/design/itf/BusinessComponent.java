package nc.uap.lfw.design.itf;

import java.io.Serializable;

public class BusinessComponent implements Serializable{

	private static final long serialVersionUID = 1L;

	private String name;
	private String dispname;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDispname() {
		return dispname;
	}
	public void setDispname(String dispname) {
		this.dispname = dispname;
	}
}
