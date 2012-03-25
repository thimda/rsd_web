package nc.uap.lfw.core.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.PageMeta;

import org.apache.commons.digester.Digester;

public final class PageNameUtil {
	
	public static Map<String, String>[] getPageNames(String[] projPaths) {
		Map<String, String>[] maps = new HashMap[projPaths.length];
		for (int i = 0; i < projPaths.length; i++) {
			String basePath = projPaths[i] + "/web/html/nodes";
			File dir = new File(basePath);
			File[] fs = dir.listFiles();
			if(fs != null){
				maps[i] = new HashMap<String, String>();
				for (int j = 0; j < fs.length; j++) {
					File file = fs[j];
					if(file.isDirectory()){
						scanDir(null, dir.getAbsolutePath(), file, maps[i]);
					}
				}
			}
		}
		return maps;
	}
	
	
	private static void scanDir(String prefix, String basePath, File dir, Map<String, String> nodeIdDirMap) {
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			File f = fs[i];
			if(f.isFile()){
				if(f.getName().endsWith(".pm")){
					String id = dir.getName();
					if(prefix != null)
						id = prefix + "." + id;
					prefix = id;
//					String absPath = dir.getAbsolutePath();
//					String relPath = absPath.substring(basePath.length() + 1);
//					nodeIdDirMap.put(id, relPath);
					try{
						Digester d = new Digester();
						d.addObjectCreate("PageMeta", PageMeta.class);
						d.addSetProperties("PageMeta");
						PageMeta pm = (PageMeta) d.parse(f);
						String name = pm.getCaption();
						nodeIdDirMap.put(id, name);
					}
					catch(Exception e){
						LfwLogger.error(e.getMessage(), e);
					}
					//LfwLogger.info(LfwLogger.LOGGER_LFW_SYS, "找到节点'" + id + "',目录'" + relPath + "'");
				}
			}
			else{
				scanDir(prefix, basePath, f, nodeIdDirMap);
			}
		}
	}
}
