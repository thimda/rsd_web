package nc.uap.lfw.core.model.plug;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslatedRows {
	private Map<String, List<Object>> objMap = new HashMap<String, List<Object>>();
	public void setValue(String key, List<Object> value){
		objMap.put(key, value);
	}
	
	public List<Object> getValue(String key){
		return objMap.get(key);
	}
	public String[] getKeys(){
		return objMap.keySet().toArray(new String[0]);
	}
}
