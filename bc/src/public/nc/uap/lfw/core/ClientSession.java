package nc.uap.lfw.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * 客户端会话类.可通过此类向客户端设置必要信息
 * 例：  在服务端 PageModel.getClientSession().setAttribute("key1", "value1");
 *		在客户端 var value = getSessionAttribute("key1");
 * @author dengjt
 * @modified 2008-04-10 添加粘住特性。此特性的意思是，设置为粘住状态的属性，会确保在ajax请求过程中带回到服务器。这对于某些场景下需要带有某些信息的应用提供了一种简介方式。
 */
public final class ClientSession{
	//非“粘住”属性map
	private Map<String, Serializable> infoMap = null;
	//"粘住"属性map
	private Map<String, Serializable> stickInfoMap = null;
	public Serializable getAttribute(String key) {
		Serializable obj = null;
		if(infoMap != null)
			obj = infoMap.get(key);
		if(obj == null && stickInfoMap != null)
			obj = stickInfoMap.get(key);
		return obj;
	}
	
	public void setAttribute(String key, Serializable obj){
		setAttribute(key, obj, false);
	}
	
	public void setAttribute(String key, Serializable obj, boolean stick){
		if(stick){
			if(stickInfoMap == null)
				stickInfoMap = new HashMap<String, Serializable>();
			stickInfoMap.put(key, obj);
		}
		else{
			if(infoMap == null)
				infoMap = new HashMap<String, Serializable>();
			infoMap.put(key, obj);
		}
	}
	
	public Map<String, Serializable> getStickAttributeMap(){
		return stickInfoMap;
	}
	
	public Map<String, Serializable> getAttributeMap() {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		if(infoMap != null)
			map.putAll(infoMap);
		if(stickInfoMap != null)
			map.putAll(stickInfoMap);
		return map;
	}
}
