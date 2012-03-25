package nc.uap.lfw.core.model.plug;

import java.util.HashMap;
import java.util.Map;

public class TranslatedRow {
	private Map<String, Object> objMap = new HashMap<String, Object>();
	public void setValue(String key, Object value){
		objMap.put(key, value);
	}
	
	public Object getValue(String key){
		return objMap.get(key);
	}
	
	public String[] getKeys(){
		return objMap.keySet().toArray(new String[0]);
	}
}
