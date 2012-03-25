package nc.uap.lfw.ncadapter.cache;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheException;
/**
 * LFwøÚº‹NCª∫¥Ê  ≈‰
 * @author dengjt
 *
 */
public class LfwMapCache implements ILfwCache{
	private Map<Object, Object> map = new ConcurrentHashMap<Object, Object>();
	private String name;
	public LfwMapCache(String name){
		this.name = name;
	}
	public Object get(Object key) throws LfwCacheException {
		return map.get(key);
	}

	public Object put(Object key, Object value) throws LfwCacheException {
		return map.put(key, value);
	}

	public Object remove(Object key) throws LfwCacheException {
		return map.remove(key);
	}
	
	public Set<Object> getKeys() {
		return map.keySet();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
