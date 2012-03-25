package nc.uap.lfw.core.page;

import java.io.Serializable;

public class LoadMappingItem implements Cloneable, Serializable {

	private static final long serialVersionUID = -310090806460643615L;
	private String key;
	private String value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
