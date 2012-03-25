package nc.uap.lfw.core.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.uimodel.Application;

public final class ApplicationNodeUtil {
	
	private static final String APPLICATION = "_APPLICATION";
	
	private static Map<String, String> dirPathMap = new HashMap<String, String>();
	
	public static void refresh(String nodeDir) {
		String ctxPath = LfwRuntimeEnvironment.getRootPath();
		refresh(nodeDir, ctxPath);
	}
	
	/**
	 * 仅供开发工具调用
	 */
	public static void refresh() {
		Iterator<Entry<String, String>> eit = dirPathMap.entrySet().iterator();
		while(eit.hasNext()){
			Entry<String, String> entry = eit.next();
			refresh(entry.getKey(), entry.getValue());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void refreshApplication(String ctxPath, Application appConf){
		Iterator<Entry<String, String>> eit = dirPathMap.entrySet().iterator();
		while(eit.hasNext()){
			Entry<String, String> entry = eit.next();
			if(entry.getValue().equals(ctxPath)){
				refresh(entry.getKey(), entry.getValue());
				break;
			}
		}
		ILfwCache cache = LfwCacheManager.getStrongCache(WebConstant.LFW_CORE_CACHE, null);
		String cacheKey = ctxPath + WebConstant.CACHE_APPLICATIONNODES;
		Map<String, String> nodeIdMap = (Map<String, String>) cache.get(cacheKey);
		Map<String, Application> nodeMetaMap = (Map<String, Application>) cache.get(cacheKey + APPLICATION);
		if(nodeIdMap == null){
			nodeIdMap = new HashMap<String, String>();
			cache.put(cacheKey, nodeIdMap);
			
			nodeMetaMap = new HashMap<String, Application>();
			cache.put(cacheKey + APPLICATION, nodeMetaMap);
		}
		nodeIdMap.put(appConf.getId(), appConf.getRealPath());
		nodeMetaMap.put(appConf.getId(), appConf);
	}
	
	@SuppressWarnings("unchecked")
	private static void refresh(String nodeDir, String ctxPath){
		dirPathMap.put(nodeDir, ctxPath);
		ILfwCache cache = LfwCacheManager.getStrongCache(WebConstant.LFW_CORE_CACHE, null);
		String cacheKey = ctxPath + WebConstant.CACHE_APPLICATIONNODES;
		Map<String, String> nodeIdMap = (Map<String, String>) cache.get(cacheKey);
		Map<String, Application> nodeMetaMap = (Map<String, Application>) cache.get(cacheKey + APPLICATION);
		if(nodeIdMap == null){
			nodeIdMap = new HashMap<String, String>();
			cache.put(cacheKey, nodeIdMap);
			
			nodeMetaMap = new HashMap<String, Application>();
			cache.put(cacheKey + APPLICATION, nodeMetaMap);
		}
		
		//读取服务器下当前Application的应用节点
		File f = new File(nodeDir);
		File[] fs = f.listFiles();
		nodeIdMap.clear();
		if(fs != null){
			for (int i = 0; i < fs.length; i++) {
				File file = fs[i];
				if(file.isDirectory()){
					scanDir(null, f.getAbsolutePath(), file, nodeIdMap, nodeMetaMap);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> getCacheMap(String ctxPath){
		ILfwCache cache = LfwCacheManager.getStrongCache(WebConstant.LFW_CORE_CACHE, null);
		String cacheKey = ctxPath + WebConstant.CACHE_APPLICATIONNODES;
		Map<String, String> pageNodeMap = (Map<String, String>) cache.get(cacheKey);
		return pageNodeMap;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Application> getCacheMetaMap(String ctxPath){
		ILfwCache cache = LfwCacheManager.getStrongCache(WebConstant.LFW_CORE_CACHE, null);
		String cacheKey = ctxPath + WebConstant.CACHE_APPLICATIONNODES;
		Map<String, Application> pageNodeMap = (Map<String, Application>) cache.get(cacheKey + APPLICATION);
		return pageNodeMap;
	}
	
	public static String getApplicationNodeDir(String appId){
		String ctxPath = LfwRuntimeEnvironment.getRootPath();
		return getCacheMap(ctxPath).get(appId);
	}
	
	/**
	 * 递归扫描目录下的所有节点
	 * @param prefix
	 * @param basePath
	 * @param dir
	 * @param nodeIdDirMap
	 * @param nodeMetaMap
	 */
	private static void scanDir(String prefix, String basePath, File dir, Map<String, String> nodeIdDirMap, Map<String, Application> nodeMetaMap) {
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			File f = fs[i];
			if(f.isFile()){
				if(f.getName().endsWith(".app")){
					try{
						String id = dir.getName();
						if(prefix != null)
							id = prefix + "." + id;
						prefix = id;
						String absPath = dir.getAbsolutePath();
						absPath = absPath.replaceAll("\\\\", "/");
						
//						IPageMetaBuidlerForDesign builder = NCLocator.getInstance().lookup(IPageMetaBuidlerForDesign.class);
						String realPath = absPath.substring(basePath.length() + 1);
//						Map<String, Object> paramMap = new HashMap<String, Object>();
//						paramMap.put("FILE_PATH", absPath);
//						paramMap.put(WebConstant.PAGE_ID_KEY, relPath);
//						PageMetaConfig pmConfig = builder.buildPageMeta(paramMap);
						nodeIdDirMap.put(id, realPath);
//						nodeMetaMap.put(id, pmConfig);
						LfwLogger.info(LfwLogger.LOGGER_LFW_SYS, "找到节点'" + id + "',目录'" + realPath + "'");
					}
					catch(Throwable e){
						LfwLogger.error(e);
					}
				}
			}
			else{
				scanDir(prefix, basePath, f, nodeIdDirMap, nodeMetaMap);
			}
		}
	}
}
