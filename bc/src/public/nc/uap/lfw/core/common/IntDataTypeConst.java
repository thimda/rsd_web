package nc.uap.lfw.core.common;

/**
 * 本系统用到的数据类型的数字编码。
 *
 * create on 2007-2-6 下午03:59:30
 *
 * @author lkp 
 *
 */

public class IntDataTypeConst 
{
	//阻止实例化
	private IntDataTypeConst() {}
	//数据类型
	
	
//	public final static int STRING = 0; //字符
//	public final static int INTEGER = 1; //整数
//	public final static int DECIMAL = 2; //小数
//	public final static int DATE = 3; //日期
//	public final static int BOOLEAN = 4; //逻辑
//	public final static int UFREF = 5; //参照
//	//public final static intCOMBO = 6;//下拉
//	public final static int USERCOMBO = 6; //下拉
//	public final static int USERDEF = 7; //其它
//	public final static int TIME = 8; //时间
	
	public static final int int_TYPE = 16;
	public static final int String_TYPE = 0;
	public static final int double_TYPE = 23;
	public static final int float_TYPE = 6;
	public static final int byte_TYPE = 5;
	public static final int boolean_TYPE = 4;
	public static final int Date_TYPE = 3;
	public static final int BigDecimal_TYPE = 7;
	public static final int long_TYPE = 8;
	public static final int char_TYPE = 9;
	
	public static final int UFBoolean_TYPE	= 10;
	public static final int UFDouble_TYPE = 11;
	public static final int UFDateTime_TYPE = 12;
	public static final int UFLiteralDate = 28;
	
	//UFDate_begin
	public static final int UFDate_begin = 29;
	//UFDate_end 
	public static final int UFDate_end = 30;
	
	public static final int UFDate_TYPE = 13;
	public static final int UFTime_TYPE = 14;
	public static final int UFNumberFormat_TYPE = 15;
	
	public static final int Integer_TYPE = 1;
	public static final int Double_TYPE = 17;
	public static final int Float_TYPE = 18;
	public static final int Byte_TYPE = 19;
	public static final int Boolean_TYPE = 20;
	public static final int Long_TYPE = 21;
	public static final int Character_TYPE = 22;
	
	public static final int Decimal_TYPE = 2;
	
	public static final int Entity_TYPE = 24;
	public static final int Image = 55;
	
}

