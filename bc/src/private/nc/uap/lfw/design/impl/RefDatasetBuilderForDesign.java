package nc.uap.lfw.design.impl;

import java.util.Map;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.model.util.DefaultRefDatasetBuilder;

public class RefDatasetBuilderForDesign extends DefaultRefDatasetBuilder {

	String PROJECT_PATH_KEY = "PROJECT_PATH_KEY";
	/**
	 * 获取引用的公共ds
	 * @param dsId
	 * @param paramMap 
	 * @return
	 */
	protected Dataset getRefDataset(String dsId, Map<String, Object> paramMap)
	{
		return super.getRefDataset(dsId, paramMap);
	}
}
