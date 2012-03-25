package nc.uap.lfw.core.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 渲染器类型常量。主要用于客户端grid控件
 * @author dengjt
 *
 */
public final class RenderTypeConst 
{
	private RenderTypeConst(){}
	
	public static final String BooleanRender = "BooleanRender";
	public static final String DefaultRender = "DefaultRender";
	public static final String IntegerRender = "IntegerRender";
	public static final String DecimalRender = "DecimalRender";
	public static final String ComboRender = "ComboRender";
	public static final String ImageRender = "ImageRender";
	public static final String DateRender = "DateRender";
	public static final String DateTimeRender = "DateTimeRender";
	
	
	//下拉框类型的暂时没法从数据类型直接得到,需要根据编辑类型得到渲染类型
	private static final Map<String, String> dt2rtMap = new HashMap<String, String>();
	static{
		dt2rtMap.put(StringDataTypeConst.BIGDECIMAL, RenderTypeConst.DecimalRender);
		dt2rtMap.put(StringDataTypeConst.bOOLEAN, RenderTypeConst.BooleanRender);
		dt2rtMap.put(StringDataTypeConst.BOOLEAN, RenderTypeConst.BooleanRender);
		dt2rtMap.put(StringDataTypeConst.bYTE, RenderTypeConst.DefaultRender);
		dt2rtMap.put(StringDataTypeConst.BYTE, RenderTypeConst.DefaultRender);
		dt2rtMap.put(StringDataTypeConst.CHAR, RenderTypeConst.DefaultRender);
		dt2rtMap.put(StringDataTypeConst.CHARACTER, RenderTypeConst.DefaultRender);
//		dt2rtMap.put(StringDataTypeConst.DATE, RenderTypeConst.DefaultRender);
		dt2rtMap.put(StringDataTypeConst.DATE, RenderTypeConst.DateRender);
		dt2rtMap.put(StringDataTypeConst.Decimal, RenderTypeConst.DecimalRender);
		dt2rtMap.put(StringDataTypeConst.dOUBLE, RenderTypeConst.DecimalRender);
		dt2rtMap.put(StringDataTypeConst.DOUBLE, RenderTypeConst.DecimalRender);
		dt2rtMap.put(StringDataTypeConst.fLOATE, RenderTypeConst.DecimalRender);
		dt2rtMap.put(StringDataTypeConst.FLOATE, RenderTypeConst.DecimalRender);
		dt2rtMap.put(StringDataTypeConst.INT, RenderTypeConst.IntegerRender);
		dt2rtMap.put(StringDataTypeConst.INTEGER, RenderTypeConst.IntegerRender);
		dt2rtMap.put(StringDataTypeConst.lONG, RenderTypeConst.IntegerRender);
		dt2rtMap.put(StringDataTypeConst.LONG, RenderTypeConst.IntegerRender);
		dt2rtMap.put(StringDataTypeConst.STRING, RenderTypeConst.DefaultRender);
		dt2rtMap.put(StringDataTypeConst.UFBOOLEAN, RenderTypeConst.BooleanRender);
//		dt2rtMap.put(StringDataTypeConst.UFDATE, RenderTypeConst.DefaultRender);
		dt2rtMap.put(StringDataTypeConst.UFDATE, RenderTypeConst.DateRender);
		dt2rtMap.put(StringDataTypeConst.UFDate_begin, RenderTypeConst.DateRender);
		dt2rtMap.put(StringDataTypeConst.UFDate_end, RenderTypeConst.DateRender);
		dt2rtMap.put(StringDataTypeConst.UFDATETIME, RenderTypeConst.DateTimeRender);
		dt2rtMap.put(StringDataTypeConst.UFLiteralDate, RenderTypeConst.DateRender);
		dt2rtMap.put(StringDataTypeConst.UFDOUBLE, RenderTypeConst.DecimalRender);
		dt2rtMap.put(StringDataTypeConst.UFNUMBERFORMAT, RenderTypeConst.IntegerRender);
		dt2rtMap.put(StringDataTypeConst.UFTIME, RenderTypeConst.DefaultRender);
		dt2rtMap.put(StringDataTypeConst.CUSTOM, RenderTypeConst.DefaultRender);
		dt2rtMap.put(StringDataTypeConst.MEMO, RenderTypeConst.DefaultRender);
		dt2rtMap.put(StringDataTypeConst.IMAGE, RenderTypeConst.ImageRender);
		
	}
	
	public static String getRenderTypeByString(String strType){
		return dt2rtMap.get(strType);
	}
}
