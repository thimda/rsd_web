package nc.uap.lfw.core.cache;

import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.ncadapter.cache.LfwFileCacheNcAdapter;
import nc.uap.lfw.ncadapter.cache.LfwMapCache;
import nc.vo.cache.CacheManager;
import nc.vo.cache.ICache;
import nc.vo.cache.config.CacheConfig;

/**
 * LFW Cache管理，封装这一层主要解决2方面问题：
 * 1. NC是因为NC提供的Cache默认不区分数据源
 * @author dengjt
 *
 */
public class LfwCacheManager {
	/**
	 * 文件缓存Map
	 */
	static Map<String, ILfwCache> fileCacheMap = new HashMap<String, ILfwCache>();
	/**
	 * 普通Cache（会话，文件缓存排除）的区域key缓存
	 */
//	static Set<String> regionSet = new HashSet<String>();
	private static final String DEFAULT_DS_NAME = "$NULL$";
	private static final String	CACHE_NAME = "LFW_NC_CACHE";
	private static final String FILE_CACHE_NAME = "FILE_CACHE_NAME";
	public static final String SESSION_PRE = "$S_";
	
	/**
	 * 获得软引用缓存
	 * @param name
	 * @param dsName
	 * @return
	 */
	public static ILfwCache getSoftCache(String name, String dsName){
		return getCache(name, dsName, true, true);
	}
	
	/**
	 * 获得缓存
	 * @param name
	 * @param dsName
	 * @return
	 */
	public static ILfwCache getStrongCache(String name, String dsName){
		return getCache(name, dsName, false, true);
	}
	
	/**
	 * 按照会话分配缓存，通过此方法获得的缓存，将确保随会话一起销毁
	 * @return
	 */
	public static ILfwCache getSessionCache() {
		String sesId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
		return getSessionCache(sesId);
	}
	
	
	/**
	 * 按照会话分配缓存，通过此方法获得的缓存，将确保随会话一起销毁
	 * @return
	 */
	public static ILfwCache getSessionCache(String sesId) {
		ILfwCache cache = getCache(SESSION_PRE + sesId, null, false, true);
		return cache;
	}
	
	/**
	 * 删除会话缓存
	 * @param sesId
	 */
	public static void removeSessionCache(String sesId) {
		removeCache(SESSION_PRE + sesId, null, false);
	}
	
	/**
	 * 按照会话分配文件缓存，通过此方法获得的缓存，将确保随会话一起销毁
	 * @return
	 */
	public static ILfwCache getSessionFileCache() {
		String sesId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
		return getSessionFileCache(sesId);
	}
	
	/**
	 * 按照会话分配文件缓存，通过此方法获得的缓存，将确保随会话一起销毁
	 * @return
	 */
	public static ILfwCache getSessionFileCache(String sesId) {
		ILfwCache cache = getFileCache(SESSION_PRE + sesId, sesId);
		return cache;
	}
	
	/**
	 * 删除会话文件缓存
	 * @param sesId
	 */
	public static void removeSessionFileCache(String sesId) {
		removeFileCache(sesId, sesId);
	}
	
	public static ILfwCache getFileCache(String name, String dsName){
		if(true)
			return null;
		if(dsName == null)
			dsName = DEFAULT_DS_NAME;
		String key = FILE_CACHE_NAME + name + dsName + "_file";
		ILfwCache cache = fileCacheMap.get(key);
		if(cache == null){
			LfwLogger.debug("获取数据源" + dsName + "的缓存:" + name + ",类型file!");
			CacheConfig config = new CacheConfig(key, false, -1, 10000, CacheConfig.CacheType.FILE, CacheConfig.MemoryCacheLevel.STRONG);
			ICache nccache = CacheManager.getInstance().getCache(config);
			cache = new LfwFileCacheNcAdapter(nccache);
			fileCacheMap.put(key, cache);
		}
		return cache;
	}
	
	public static void removeFileCache(String name, String dsName){
		if(true)
			return;
		if(dsName == null)
			dsName = DEFAULT_DS_NAME;
		String key = FILE_CACHE_NAME + name + dsName + "_file";
		ILfwCache cache = fileCacheMap.remove(key);
		if(cache != null){
			Object[] objs = cache.getKeys().toArray(new Object[0]);
			for (int i = 0; i < objs.length; i++) {
				cache.remove(objs[i]);
			}
		}
	}
	
	/**
	 * 清除缓存
	 * @param name
	 * @param dsName
	 * @param soft
	 */
	private static void removeCache(String name, String dsName, boolean soft) {
		String key = getCacheKeyName(dsName, soft);
		ICache cache = CacheManager.getInstance().getCache(key);
		if(cache == null)
			return;
		cache.remove(name);
//		regionSet.remove(key);
	}
	/**
	 * 返回已有或者创建新缓存对象
	 * @param name
	 * @param dsName
	 * @param soft
	 * @param create
	 * @return
	 */
	private static ILfwCache getCache(String name, String dsName, boolean soft, boolean create) {
		String key = getCacheKeyName(dsName, soft);
//		regionSet.add(key);
		CacheConfig config = new CacheConfig(key, false, -1, 10000, CacheConfig.CacheType.MEMORY, soft? CacheConfig.MemoryCacheLevel.SOFT : CacheConfig.MemoryCacheLevel.STRONG);
		ICache cache = CacheManager.getInstance().getCache(config);
		
		ILfwCache lfwcache = (ILfwCache) cache.get(name);
		if(lfwcache == null){
			lfwcache = new LfwMapCache(name);
			cache.put(name, lfwcache);
		}
		return lfwcache;
	}
	
	private static String getCacheKeyName(String dsName, boolean soft){
		if(dsName == null)
			dsName = DEFAULT_DS_NAME;
		String key = dsName + (soft? "_soft" : "_strong") + CACHE_NAME;
		return key;
	}
}
