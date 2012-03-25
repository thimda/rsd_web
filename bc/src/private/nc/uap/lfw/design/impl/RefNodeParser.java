package nc.uap.lfw.design.impl;

import java.util.HashMap;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.model.parser.RefNodePoolParser;
import nc.uap.lfw.core.page.manager.PoolObjectUtil;
import nc.uap.lfw.core.refnode.IRefNode;

/**
 * ��ȡ�������е���������
 */
public class RefNodeParser 
{
	public static final String MAIN_PATH = "/web/pagemeta/public/";
	public static final String REF_PATH = "refnodes";
	
	/**
	 * ��ȡ�������е�����ds
	 * @param projectPath
	 * @return
	 */
	public static Map<String, IRefNode> getRefnodesFromPool(String projectPath) {
		String[] confFiles = ContextResourceUtilForDesign.getConfigFilePaths(projectPath + MAIN_PATH + REF_PATH);
		Map<String, IRefNode> refMap = new HashMap<String, IRefNode>();
		if(confFiles != null){
			for (int i = 0; i < confFiles.length; i++) {
				String filePath = confFiles[i];
				try{
					IRefNode refnode = RefNodePoolParser.parse(ContextResourceUtilForDesign.getResourceAsStream(filePath));
					if(refnode == null)
						continue;
					String refNodeId = PoolObjectUtil.parsePathId(filePath, projectPath + MAIN_PATH + REF_PATH);
					refnode.setId(refNodeId);
					refMap.put(refNodeId, refnode);
				}
				catch(Exception e){
					Logger.error("===PoolObjectManager��getDatasetFromPool����:��ʼ���ļ�" + filePath + "����", e);
				}
			}
		}
		return refMap;
	}
}
