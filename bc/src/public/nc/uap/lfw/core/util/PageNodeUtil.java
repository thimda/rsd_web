package nc.uap.lfw.core.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.design.noexport.IPageMetaBuidlerForDesign;

public final class PageNodeUtil {
	private static final String PAGEMETA = "_PAGEMETA";
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
	public static void refreshAMC(String ctxPath, PageMeta pmConf){
		Iterator<Entry<String, String>> eit = dirPathMap.entrySet().iterator();
		while(eit.hasNext()){
			Entry<String, String> entry = eit.next();
			if(entry.getValue().equals(ctxPath)){
				refresh(entry.getKey(), entry.getValue());
				break;
			}
		}
		ILfwCache cache = LfwCacheManager.getStrongCache(WebConstant.LFW_CORE_CACHE, null);
		String cacheKey = ctxPath + WebConstant.CACHE_PAGENODES;
		Map<String, String> nodeIdMap = (Map<String, String>) cache.get(cacheKey);
		Map<String, PageMeta> nodeMetaMap = (Map<String, PageMeta>) cache.get(cacheKey + PAGEMETA);
		if(nodeIdMap == null){
			nodeIdMap = new HashMap<String, String>();
			cache.put(cacheKey, nodeIdMap);
			
			nodeMetaMap = new HashMap<String, PageMeta>();
			cache.put(cacheKey + PAGEMETA, nodeMetaMap);
		}
		nodeIdMap.put(pmConf.getId(), pmConf.getRealPath());
		nodeMetaMap.put(pmConf.getId(), pmConf);
	}
	
	@SuppressWarnings("unchecked")
	private static void refresh(String nodeDir, String ctxPath){
		dirPathMap.put(nodeDir, ctxPath);
		ILfwCache cache = LfwCacheManager.getStrongCache(WebConstant.LFW_CORE_CACHE, null);
		String cacheKey = ctxPath + WebConstant.CACHE_PAGENODES;
		Map<String, String> nodeIdMap = (Map<String, String>) cache.get(cacheKey);
		Map<String, PageMeta> nodeMetaMap = (Map<String, PageMeta>) cache.get(cacheKey + PAGEMETA);
		if(nodeIdMap == null){
			nodeIdMap = new HashMap<String, String>();
			cache.put(cacheKey, nodeIdMap);
			
			nodeMetaMap = new HashMap<String, PageMeta>();
			cache.put(cacheKey + PAGEMETA, nodeMetaMap);
		}
		
		//读取服务器下当前APP的应用节点
//		String nodeDir = appPath + "/html/nodes";
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
		String cacheKey = ctxPath + WebConstant.CACHE_PAGENODES;
		Map<String, String> pageNodeMap = (Map<String, String>) cache.get(cacheKey);
		return pageNodeMap;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, PageMeta> getCacheMetaMap(String ctxPath){
		ILfwCache cache = LfwCacheManager.getStrongCache(WebConstant.LFW_CORE_CACHE, null);
		String cacheKey = ctxPath + WebConstant.CACHE_PAGENODES;
		Map<String, PageMeta> pageNodeMap = (Map<String, PageMeta>) cache.get(cacheKey + PAGEMETA);
		return pageNodeMap;
	}
	
	public static String getPageNodeDir(String pageId){
		String ctxPath = LfwRuntimeEnvironment.getRootPath();
		if("login".equals(pageId)){
			while(getCacheMap(ctxPath) == null){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					LfwLogger.error(e.getMessage(), e);
				}
			}
		}
		return getCacheMap(ctxPath).get(pageId);
	}
	
	/**
	 * 递归扫描目录下的所有节点
	 * @param prefix
	 * @param basePath
	 * @param dir
	 * @param nodeIdDirMap
	 * @param nodeMetaMap
	 */
	private static void scanDir(String prefix, String basePath, File dir, Map<String, String> nodeIdDirMap, Map<String, PageMeta> nodeMetaMap) {
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			File f = fs[i];
			if(f.isFile()){
				if(f.getName().endsWith(".pm")){
					try{
						String id = dir.getName();
						if(prefix != null)
							id = prefix + "." + id;
						prefix = id;
						String absPath = dir.getAbsolutePath();
						absPath = absPath.replaceAll("\\\\", "/");
						
						IPageMetaBuidlerForDesign builder = NCLocator.getInstance().lookup(IPageMetaBuidlerForDesign.class);
						String realPath = absPath.substring(basePath.length() + 1);
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("FILE_PATH", absPath);
						paramMap.put(WebConstant.PAGE_ID_KEY, realPath);
						PageMeta pmConfig = builder.buildPageMeta(paramMap);
						nodeIdDirMap.put(id, realPath);
						nodeMetaMap.put(id, pmConfig);
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
