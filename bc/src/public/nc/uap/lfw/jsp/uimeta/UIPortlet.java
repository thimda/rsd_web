package nc.uap.lfw.jsp.uimeta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UIPortlet implements Serializable  {
	private static final long serialVersionUID = 1083409119485229582L;
	private Map<String, Serializable> attrMap = null;
	public static final String ID = "id";
	public void setAttribute(String key, Serializable value){
		getAttrMap().put(key, value);
	}
	public Serializable getAttribute(String key){
		return getAttrMap().get(key);
	}
	
	protected Map<String, Serializable> getAttrMap() {
		if(attrMap == null){
			attrMap = createAttrMap();
		}
		return attrMap;
	}
	protected Map<String, Serializable> createAttrMap(){
		return new HashMap<String, Serializable>();
	}
	
	public Object doClone(){
		try {
			UIPortlet ele = (UIPortlet)this.clone();
			if(this.attrMap != null){
				ele.attrMap = ele.createAttrMap();
				Iterator<String> keys = this.attrMap.keySet().iterator();
				while(keys.hasNext()){
					String key = keys.next();
					Serializable s = this.attrMap.get(key);
					ele.attrMap.put(key,s);
				}
			}
			
			return ele;
		} catch (CloneNotSupportedException e) {
			return this;
		}
	}
}
