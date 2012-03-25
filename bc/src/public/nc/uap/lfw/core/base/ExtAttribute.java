package nc.uap.lfw.core.base;

import java.io.Serializable;

public class ExtAttribute implements Serializable, Cloneable {
	private static final long serialVersionUID = -7894011456596287487L;
	private String key;
	private Serializable value;
	private String desc;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Serializable getValue() {
		return value;
	}
	public void setValue(Serializable value) {
		this.value = value;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public Object clone() {
		try {
			return super.clone();
		} 
		catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
}
