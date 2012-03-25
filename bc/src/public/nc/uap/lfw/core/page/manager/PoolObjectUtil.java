package nc.uap.lfw.core.page.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nc.bs.framework.common.RuntimeEnv;
import nc.uap.lfw.core.ContextResourceUtil;

/**
 * 缓存池解析帮助类
 * @author gd 2010-1-4
 * @version NC6.0
 * @since NC6.0
 */
public class PoolObjectUtil {
	
	public static String getAbsPath(String ctx, String relativePath)
	{
		String absPath = RuntimeEnv.getInstance().getNCHome() + "/hotwebs" + ctx + "/" + relativePath;
		return absPath;
	}
	
	/**
	 * 获得配置文件路径
	 * @param dirPath
	 * @param type
	 * @return
	 */
	public static String[] getConfigFilePaths(String dirPath){
		Set<String> pathSet = ContextResourceUtil.getResourcePaths(dirPath);
		if(pathSet == null)
			return null;
		Iterator<String> it = pathSet.iterator();
		List<String> list = new ArrayList<String>();
		while(it.hasNext()){
			String path = it.next();
			//只能根据"."来判断是否文件
			if(path.indexOf(".") != -1){
				list.add(path);
				continue;
			}
			getConfigFiles(path, list);
		}
		return list.toArray(new String[0]);
	}
	
	private static void getConfigFiles(String dir, List<String> list){
		Set<String> pathSet = ContextResourceUtil.getResourcePaths(dir);
		Iterator<String> it = pathSet.iterator();
		while(it.hasNext()){
			String path = it.next();
			// 非pagemeta配置需要以.xml结尾,且去掉类似a.xml.keep 文件
			if(path.indexOf(".xml") != -1 && path.indexOf(".xml.") == -1)
				list.add(path);
			// pagemeta配置需要以pagemeta.xml结尾
			else if(path.indexOf("pagemeta.xml") != -1)
				list.add(path);
			else if(path.indexOf("widget.wd") != -1)
				list.add(path);
			else if(path.indexOf(".") == -1) {
				getConfigFiles(path, list);
			}
		}
	}

	public static String parsePathId(String filePath, String ctxPath){
		String path = filePath.replaceAll("\\\\", "/");
		path = path.substring(ctxPath.length() + 1);
		//sub ".xml"
		path = path.substring(0, path.length() - 4);
		path = path.replaceAll("/", ".");
		return path;
	}
	
	public static String parseWidgetId(String filePath, String ctxPath){
		String path = filePath.replaceAll("\\\\", "/");
		path = path.substring(ctxPath.length() + 1);
		//sub ".xml"
		path = path.substring(0, path.length() - "widget.wd".length() - 1);
		path = path.replaceAll("/", ".");
		return path;
	}
}
