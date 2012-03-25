package nc.uap.lfw.core.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import nc.vo.cache.CacheManager;
import nc.vo.cache.ICache;


/**
 * 缓存监控工具类，提供了缓存监控所需的api
 * @author dengjt
 *
 */
public final class LfwCacheMonitor {
	
	public static Set<String> getExistCacheKeys() {
//		Set<String> set = LfwCacheManager.regionSet;
//		Set<String> rset = new HashSet<String>();
//		Iterator<String> it = set.iterator();
//		while(it.hasNext()){
//			String key = it.next();
//			if(key.startsWith(LfwCacheManager.SESSION_PRE))
//				continue;
//			rset.add(key);
//		}
//		return rset;
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, ILfwCache> getExistCacheMapByRegion(String key) {
		ICache cache = CacheManager.getInstance().getCache(key);
		return cache.toMap();
	}
	
	public static Map<String, ILfwCache> getExitFileCacheMap() {
		Map<String, ILfwCache> cache = LfwCacheManager.fileCacheMap;
		Map<String, ILfwCache> ncache = new HashMap<String, ILfwCache>();
		Iterator<Entry<String, ILfwCache>> it = cache.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, ILfwCache> entry = it.next();
			String key = entry.getKey();
			if(key.startsWith(LfwCacheManager.SESSION_PRE))
				continue;
			ncache.put(key, entry.getValue());
		}
		return ncache;
	}
}
