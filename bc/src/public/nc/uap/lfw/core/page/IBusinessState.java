package nc.uap.lfw.core.page;

/**
 * 单据的业务状态
 * @author gd 2010-5-24
 * @version NC6.0
 */
public interface IBusinessState {
	//审批不通过
	public static final String NOPASS = "0";
	//审批通过
	public static final String CHECKPASS = "1";
	//审批进行中
	public static final String CHECKGOING = "2";
	//提交态
	public static final String COMMIT = "3";
	//自由态
	public static final String FREE = "-1";
	
	//作废态
	public static final String DELETE = "4";
	//冲销态
	public static final String CX = "5";
	//终止(结算）态
	public static final String ENDED = "6";
	//冻结状态
	public static final String FREEZE = "7";

	public static final String ALL = "30";
}
