package nc.uap.lfw.design.itf;

import java.util.Map;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.exception.LfwBusinessException;


/**
 * 根据类名生成代码
 * @author zhangxya
 *
 */
public interface IGeneratorCode {
	
	public String generatorVO(String fullPath, String tableName, String primaryKey, Dataset ds) throws LfwBusinessException;
	
	public String generatorCode(String fullPath, String extendClass,  Map<String, Object> param) throws LfwBusinessException;

	public String generateRefNodeClass(String refType, String modelClass, String tableName,
			String refPk, String refCode, String refName, String visibleFields,
			String pfield, String childfield) throws LfwBusinessException;
		
}
