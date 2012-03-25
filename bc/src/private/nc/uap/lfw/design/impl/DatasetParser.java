package nc.uap.lfw.design.impl;

import java.util.HashMap;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.model.parser.DatasetPoolParser;
import nc.uap.lfw.core.page.manager.PoolObjectUtil;

/**
 * 获取公共池中的所有ds
 * @author gd 2010-1-7
 * @version NC6.0
 * @since NC6.0
 */
public class DatasetParser 
{
	public static final String MAIN_PATH = "/web/pagemeta/public/";
	public static final String DS_PATH = "dspool";
	
	/**
	 * 获取公共池中的所有ds
	 * @param projectPath
	 * @return
	 */
	public static Map<String, Dataset> getDatasetsFromPool(String projectPath) {
		String[] confFiles = ContextResourceUtilForDesign.getConfigFilePaths(projectPath + MAIN_PATH + DS_PATH);
		HashMap<String, Dataset> dss = new HashMap<String, Dataset>(); 
		if(confFiles != null){
			for (int i = 0; i < confFiles.length; i++) {
				String filePath = confFiles[i];
				try{
					Dataset ds = DatasetPoolParser.parse(ContextResourceUtilForDesign.getResourceAsStream(filePath));
					String dsId = PoolObjectUtil.parsePathId(filePath, projectPath + MAIN_PATH + DS_PATH);
					ds.setId(dsId);
					dss.put(dsId, ds);
				}
				catch(Exception e){
					Logger.error("===PoolObjectManager类getDatasetFromPool方法:初始化文件" + filePath + "出错", e);
				}
			}
		}
		return dss;
	}
}
