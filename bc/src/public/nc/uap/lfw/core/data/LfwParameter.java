package nc.uap.lfw.core.data;

import java.io.Serializable;

import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;

public class LfwParameter implements Cloneable, Serializable {
	private static final long serialVersionUID = -2848436767660730017L;
	/*参数名称*/
	private String name = "";
	/*参数描述*/
	private String desc = "";
	
	public LfwParameter(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}
	
	public LfwParameter(String name){
		this(name, null);
	}
	
	public LfwParameter() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
			LfwLogger.error(e);
			throw new LfwRuntimeException(e);
		}
	}
}
