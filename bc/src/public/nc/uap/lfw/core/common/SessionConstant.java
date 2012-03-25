package nc.uap.lfw.core.common;

/**
 * 放置到Session域中的属性Key
 * @author dengjt
 *
 */
public interface SessionConstant{
	public static final String EXCEPTION_KEY = "EXCEPTION_KEY";
	public static final String LAST_URL_KEY = "LAST_URL_KEY";
	public static final String LOGIN_SESSION_BEAN = "LOGIN_SESSION_BEAN";
	
	public static final String REF_QUERY_SQL = "ref_query_sql";//参照主数据查询sql
	public static final String REF_QUERY_SQL_ORDER = "ref_query_sql_order";//参照主数据查询sql
	public static final String REF_QUERY_SQL_DIR = "ref_query_sql_dir";//树表参照，第二层树加载sql
	public static final String REF_QUERY_SQL_DIR_ORDER = "ref_query_sql_dir_order";//树表参照，第二层树 排序字段	
	public static final String REF_FILTER_SQL = "ref_filter_sql";//参照主数据过滤sql,where ((deptcode like '%REPLACE%' or deptname like '%REPLACE%' or pk_parent like '%REPLACE%') )
	public static final String REF_BLUR_SQL = "ref_blur_sql";//参照主数据查询sql,where ((deptcode = 'REPLACE' or deptname = 'REPLACE') )
	public static final String REF_QUERY_SQL_DIR_JOIN = "ref_query_sql_dir_join";//树表参照，表加载sql，join区
	
	public static final String REF_QUERY_SQL_ROOT_SQL="ref_query_sql_root";//树表参照，节点第一层查询sql
	public static final String REF_QUERY_SQL_ROOT_JOIN="ref_query_sql_root_join";//树树关联，节点第二层加载 join 区
	
	
	//主TOKEN编号
	public static final String ROOT_TOKEN_ID = "ROOT_TOKEN_ID";
	public static final String INTEGRATE_MODE = "IS_PORTAL";
	//放在session中的登录前页面地址
	public static final String CURRENT_URL = "CURRENT_URL";
}
