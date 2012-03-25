package nc.uap.lfw.core.common;

/**
 *
 *  �漰��Dataset��һЩ������Ϣ��
 *
 * create on 2007-3-14 ����11:06:43
 *
 * @author lkp 
 *
 */

public final class DatasetConstant {

	public static String PREFIX_SYSTEM_QUERY_PARAM = "$$";
	/**
	 * ����ǰ̨���̨���͵���ͨ��ѯ��ͨ����ѯģ������Ĳ�ѯ��ʽ����Key��Value����ͨ�������������ʽ
	 * ���͡�����������keys/values�Ĳ������ƣ����Ӧ��ֵ���Զ��ŷָ�Ĳ�ѯField id�Լ���Ӧ����ֵ��
	 */	
	public static String QUERY_PARAM_KEYS = "query_param_keys"; 
	
	public static String QUERY_PARAM_VALUES =  "query_param_values";
	
	public static String QUERY_PARAM_PAGEINDEX = "query_param_pageindex";
	
	public static String QUERY_PARAM_KEYVALUE = "query_param_keyvalue";
	
	/*��ѯ�������˲���ֻ��ֵ��������ֱ�����õ�Ds��CurrentKey��*/
	public static String QUERY_KEYVALUE = "query_keyvalue";
	
	public static String QUERY_PARAM_VALUES_LIST =  "query_param_values_list";
	/*���ĸ��������Ĳ�ѯ�򱣴������*/
	public static String QUERY_FROM_MASTER_DS_ID = "query_from_master_ds_id";
	/*��ѯģ������Ĭ��ROOT ID*/
	public static String QUERY_TEMPLATE_DEFAULT_ROOTPARENTID = "queryTemplateSpecialParentId";
	
	/*����������*/
	public static String ROW_INSERTED_INDEX = "insertedIndex";
	 
	/*��ǰ̨���̨���Ͳ�ѯ����ʱ��ͨ���ò���ȷ���Ƿ����Բ�ѯģ�棬������Բ�ѯģ������Ӹò�������ֵ�����塣
	 * ��������Բ�ѯģ�棬��������øò�����
	 * */
	public static String FROM_QUERY_TEMPLATE = PREFIX_SYSTEM_QUERY_PARAM + "from_query_model";
	
	/*ͨ���˲����洢ͨ����ѯģ�����ɵĲ�ѯ����*/
	public static String QUERY_TEMPLATE_CONDITION = PREFIX_SYSTEM_QUERY_PARAM + "query_template_condition";
	
	public static String QUERY_TEMPLATE_LOGICCONDITION = PREFIX_SYSTEM_QUERY_PARAM + "query_template_logiccondition";
	
	/*�˲����洢��ƴװ��ѯ����*/
	public static String NORMAL_QUERY_CONDITiON = PREFIX_SYSTEM_QUERY_PARAM + "normal_query_condition";
	
	
	/*ͨ���ò������ƣ��ڲ�ѯģ���handler����ʱ��������ѯDataset��id�ŵ���ѯģ��������νṹ��response-parameter�С�*/
	public static String QUERY_TEMPLATE_TARGET_DSID = PREFIX_SYSTEM_QUERY_PARAM + "query_template_target_dsid";
	/*ͨ���˲������ƣ��ڲ�ѯģ���handler����ʱ��������ѯdataset���ڵ�pageId�ŵ���ѯģ��������νṹ��response-parameter�С�*/
	public static String QUERY_TEMPLATE_TARGET_PAGEID = PREFIX_SYSTEM_QUERY_PARAM + "query_template_target_pageId";
		
	// ����˵ĵ���id
	public static String APPROVE_BILLID = "approveid";
	//���ݵ��������� 0=�Լ��ĵ��ݣ�1=�����ĵ���
	public static String APPROVE_TYPE = "approve_type";
	
	
	public static final String REF_RETURN_CODE = "REF_RETURN_CODE";
	public static final String REF_MODEL = "refMode";
}

