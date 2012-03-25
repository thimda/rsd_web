package nc.uap.lfw.core.common;

/**
 *
 *  涉及到Dataset的一些常量信息。
 *
 * create on 2007-3-14 上午11:06:43
 *
 * @author lkp 
 *
 */

public final class DatasetConstant {

	public static String PREFIX_SYSTEM_QUERY_PARAM = "$$";
	/**
	 * 对于前台向后台发送的普通查询（通过查询模版以外的查询方式），Key和Value都是通过特殊参数的形式
	 * 传送。下面两个是keys/values的参数名称，其对应的值是以逗号分割的查询Field id以及对应的数值。
	 */	
	public static String QUERY_PARAM_KEYS = "query_param_keys"; 
	
	public static String QUERY_PARAM_VALUES =  "query_param_values";
	
	public static String QUERY_PARAM_PAGEINDEX = "query_param_pageindex";
	
	public static String QUERY_PARAM_KEYVALUE = "query_param_keyvalue";
	
	/*查询参数，此参数只带值，并不会直接设置到Ds的CurrentKey中*/
	public static String QUERY_KEYVALUE = "query_keyvalue";
	
	public static String QUERY_PARAM_VALUES_LIST =  "query_param_values_list";
	/*由哪个主表触发的查询或保存操作。*/
	public static String QUERY_FROM_MASTER_DS_ID = "query_from_master_ds_id";
	/*查询模板条件默认ROOT ID*/
	public static String QUERY_TEMPLATE_DEFAULT_ROOTPARENTID = "queryTemplateSpecialParentId";
	
	/*新增行索引*/
	public static String ROW_INSERTED_INDEX = "insertedIndex";
	 
	/*在前台向后台发送查询请求时，通过该参数确定是否来自查询模版，如果来自查询模版则添加该参数，其值无意义。
	 * 如果不来自查询模版，不因该设置该参数。
	 * */
	public static String FROM_QUERY_TEMPLATE = PREFIX_SYSTEM_QUERY_PARAM + "from_query_model";
	
	/*通过此参数存储通过查询模版生成的查询条件*/
	public static String QUERY_TEMPLATE_CONDITION = PREFIX_SYSTEM_QUERY_PARAM + "query_template_condition";
	
	public static String QUERY_TEMPLATE_LOGICCONDITION = PREFIX_SYSTEM_QUERY_PARAM + "query_template_logiccondition";
	
	/*此参数存储自拼装查询条件*/
	public static String NORMAL_QUERY_CONDITiON = PREFIX_SYSTEM_QUERY_PARAM + "normal_query_condition";
	
	
	/*通过该参数名称，在查询模版的handler处理时，将被查询Dataset的id放到查询模版左边树形结构的response-parameter中。*/
	public static String QUERY_TEMPLATE_TARGET_DSID = PREFIX_SYSTEM_QUERY_PARAM + "query_template_target_dsid";
	/*通过此参数名称，在查询模版的handler处理时，将被查询dataset所在的pageId放到查询模版左边树形结构的response-parameter中。*/
	public static String QUERY_TEMPLATE_TARGET_PAGEID = PREFIX_SYSTEM_QUERY_PARAM + "query_template_target_pageId";
		
	// 待审核的单据id
	public static String APPROVE_BILLID = "approveid";
	//单据的审批类型 0=自己的单据，1=审批的单据
	public static String APPROVE_TYPE = "approve_type";
	
	
	public static final String REF_RETURN_CODE = "REF_RETURN_CODE";
	public static final String REF_MODEL = "refMode";
}

