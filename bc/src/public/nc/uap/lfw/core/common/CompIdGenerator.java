package nc.uap.lfw.core.common;



/**
 * 生成ID的工具类
 *
 */
public class CompIdGenerator {
	
	public static String generateComboCompId(String dsId, String fieldId)
	{
		return "combo_" + dsId + "_" + fieldId.replaceAll("\\.", "_");
	}
	
	public static String generateRefCompId(String dsId, String fieldId)
	{
		return "refnode_" + dsId + "_" + fieldId.replaceAll("\\.", "_");
	}

}
