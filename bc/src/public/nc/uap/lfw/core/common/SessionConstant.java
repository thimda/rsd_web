package nc.uap.lfw.core.common;

/**
 * ���õ�Session���е�����Key
 * @author dengjt
 *
 */
public interface SessionConstant{
	public static final String EXCEPTION_KEY = "EXCEPTION_KEY";
	public static final String LAST_URL_KEY = "LAST_URL_KEY";
	public static final String LOGIN_SESSION_BEAN = "LOGIN_SESSION_BEAN";
	
	public static final String REF_QUERY_SQL = "ref_query_sql";//���������ݲ�ѯsql
	public static final String REF_QUERY_SQL_ORDER = "ref_query_sql_order";//���������ݲ�ѯsql
	public static final String REF_QUERY_SQL_DIR = "ref_query_sql_dir";//������գ��ڶ���������sql
	public static final String REF_QUERY_SQL_DIR_ORDER = "ref_query_sql_dir_order";//������գ��ڶ����� �����ֶ�	
	public static final String REF_FILTER_SQL = "ref_filter_sql";//���������ݹ���sql,where ((deptcode like '%REPLACE%' or deptname like '%REPLACE%' or pk_parent like '%REPLACE%') )
	public static final String REF_BLUR_SQL = "ref_blur_sql";//���������ݲ�ѯsql,where ((deptcode = 'REPLACE' or deptname = 'REPLACE') )
	public static final String REF_QUERY_SQL_DIR_JOIN = "ref_query_sql_dir_join";//������գ������sql��join��
	
	public static final String REF_QUERY_SQL_ROOT_SQL="ref_query_sql_root";//������գ��ڵ��һ���ѯsql
	public static final String REF_QUERY_SQL_ROOT_JOIN="ref_query_sql_root_join";//�����������ڵ�ڶ������ join ��
	
	
	//��TOKEN���
	public static final String ROOT_TOKEN_ID = "ROOT_TOKEN_ID";
	public static final String INTEGRATE_MODE = "IS_PORTAL";
	//����session�еĵ�¼ǰҳ���ַ
	public static final String CURRENT_URL = "CURRENT_URL";
}
