package nc.uap.lfw.ncadapter.common;

import nc.md.model.type.IType;
import nc.md.model.type.impl.EnumType;
import nc.uap.lfw.core.common.StringDataTypeConst;

/**
 * Web Frame数据类型到元数据类型转换器
 * @author dengjt
 *
 */
public class LfwTypeToITypeTranslator {
	/**
	 * 将IType翻译成String类型
	 * @param type
	 * @return
	 */
	public static String translateITypeToString(IType type){		
		switch(type.getTypeType()){
			case IType.TYPE_Boolean:
				return StringDataTypeConst.BOOLEAN;
			case IType.TYPE_UFBoolean:
				return StringDataTypeConst.UFBOOLEAN;
			case IType.TYPE_BOOL:
				return StringDataTypeConst.bOOLEAN;
			case IType.TYPE_BYTE:
				return StringDataTypeConst.BYTE;
			case IType.TYPE_CHAR:
				return StringDataTypeConst.CHAR;
			case IType.TYPE_INT:
				return StringDataTypeConst.INT;
			case IType.TYPE_Integer:
				return StringDataTypeConst.INTEGER;
			case IType.ENUM:
				EnumType tempType = (EnumType)type;
				return translateITypeToString(tempType.getReturnType());
			case IType.TYPE_UFDouble:
				return StringDataTypeConst.UFDOUBLE;
			case IType.TYPE_DOUBLE:
				return StringDataTypeConst.DOUBLE;
			case IType.TYPE_Double:
				return StringDataTypeConst.dOUBLE;
			//TODO UFMoney类型暂时对应到UFDouble
			case IType.TYPE_UFMoney:
				return StringDataTypeConst.UFDOUBLE;
			case IType.TYPE_UFDateTime:
				return StringDataTypeConst.UFDATETIME; 
			case IType.TYPE_UFDate:
				return StringDataTypeConst.UFDATE;
			case IType.TYPE_UFDate_BEGIN:
				return StringDataTypeConst.UFDate_begin;
			case IType.TYPE_UFDate_END:
				return StringDataTypeConst.UFDate_end;
			case IType.ENTITY:
				return StringDataTypeConst.ENTITY;
			case IType.TYPE_CUSTOM:
				return StringDataTypeConst.CUSTOM;
			case IType.TYPE_MEMO:
				return StringDataTypeConst.MEMO;
			case IType.TYPE_UFDATE_LITERAL:
				return StringDataTypeConst.UFLiteralDate;
			case IType.TYPE_BIGDECIMAL:
				return StringDataTypeConst.BIGDECIMAL;
			case IType.TYPE_IMAGE:
				return StringDataTypeConst.IMAGE;
			default: 
				return StringDataTypeConst.STRING;
		}
		
	}
	
}
