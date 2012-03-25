package nc.uap.lfw.core.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class ExtendAttributeSupport implements Serializable{

	private static final long serialVersionUID = 8969126498901877638L;
	public static final String DYN_ATTRIBUTE_KEY = "DYN";
	private Map<String, ExtAttribute> extMap;
	
	public Map<String, ExtAttribute> getExtendMap() {
		if(extMap == null)
			extMap = new HashMap<String, ExtAttribute>();
		return extMap;
	}
	
	public void setExtendAttribute(String key, Serializable value){
		setExtendAttribute(key, value, null);
	}
	
	public void setExtendAttribute(String key, Serializable value, String desc){
		ExtAttribute attr = new ExtAttribute();
		attr.setKey(key);
		attr.setValue(value);
		attr.setDesc(desc);
		getExtendMap().put(key, attr);
	}
	
	public ExtAttribute removeExtendAttribute(String key){
		return getExtendMap().remove(key);
	}
	
	/**
	 * 此方法不提倡用，为Digester解析方便
	 * @param attr
	 */
	public void addAttribute(ExtAttribute attr){
		getExtendMap().put(attr.getKey(), attr);
	}
	
	public ExtAttribute getExtendAttribute(String key) {
		return extMap == null ? null : extMap.get(key);
	}
	
	public Serializable getExtendAttributeValue(String key){
		ExtAttribute attr = extMap == null ? null : extMap.get(key);
		if(attr == null)
			return null;
		return attr.getValue();
	}
	
	public Object clone()
	{
		ExtendAttributeSupport ea;
		try {
			ea = (ExtendAttributeSupport) super.clone();
			if(this.extMap != null)
			{
				ea.extMap = new HashMap<String, ExtAttribute>();
				Iterator<ExtAttribute> it = this.extMap.values().iterator();
				while(it.hasNext())
				{
					ea.addAttribute((ExtAttribute) it.next().clone());
				}
			}
			return ea;
		} 
		catch (CloneNotSupportedException e) {
			return null;
		}
		
	}
}
