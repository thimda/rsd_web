package nc.uap.lfw.core.comp.ctx;

import java.io.Serializable;

public class BaseContext implements Serializable{

	private static final long serialVersionUID = 3691828575638870293L;
	
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
