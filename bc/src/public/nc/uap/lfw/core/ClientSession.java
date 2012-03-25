package nc.uap.lfw.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * �ͻ��˻Ự��.��ͨ��������ͻ������ñ�Ҫ��Ϣ
 * ����  �ڷ���� PageModel.getClientSession().setAttribute("key1", "value1");
 *		�ڿͻ��� var value = getSessionAttribute("key1");
 * @author dengjt
 * @modified 2008-04-10 ���ճס���ԡ������Ե���˼�ǣ�����Ϊճס״̬�����ԣ���ȷ����ajax��������д��ص��������������ĳЩ��������Ҫ����ĳЩ��Ϣ��Ӧ���ṩ��һ�ּ�鷽ʽ��
 */
public final class ClientSession{
	//�ǡ�ճס������map
	private Map<String, Serializable> infoMap = null;
	//"ճס"����map
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
