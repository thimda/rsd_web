package nc.uap.lfw.reference.base;

import java.util.List;

/**
 * web����ģ�ͣ��ӿڲο�NC ����ģʽ��
 *
 */
public interface ILfwRefModel {
	// ��������
	public static final Integer REFTYPE_GRID = 0;
	public static final Integer REFTYPE_TREE = 1;
	public static final Integer REFTYPE_TREEGRID = 2;
	public static final Integer REFTYPE_TWOTREE = 3;
	public static final Integer REFTYPE_TWOTREEGRID = 4;
	public static final Integer REFTYPE_TREEGRIDTEXT=5;

	public static final String FILTERSQL_MASK = "#R#";
	
	/**
	 * ��ȡ������ʾ�е��ֶεı��� 
	 */
	public String[] getVisibleFieldCodes();
	
	/**
	 * ��ȡ���������е��ֶεı���
	 * @return
	 */
	public String[] getHiddenFieldCodes();
	

	/**
	 * ��ȡ����pk�ֶ�
	 * @return
	 */
	public String getRefPkField();

	/**
	 * ��ȡ���ձ����ֶ�
	 * @return
	 */
	public String getRefCodeField();
	
	/**
	 * ��ȡ���������ֶ�
	 * @return
	 */
	public String getRefNameField();
	
	/**
	 * ��ȡ��������(��ά��ṹ)
	 * @return
	 */
	public RefResult getRefData(int index);
	
	public String getFixedWherePart();
	public String getWherePart();
	public void addWherePart(String wherePart);
	public void setWherePart(String wherePart);
	public String getTablesString();
	public String getOrderPart();
	public String getGroupPart();
	public String[] getFilterFields();
	public String getQuerySql();
	public String getStrPatch();
	
	/**
	 * ��ȡ��������
	 * @return
	 */
	public Integer getRefType();
	
	/**
	 * ��ȡ�ֶ��е��±�
	 * @param field
	 * @return
	 */
	public int getFieldIndex(String field);

	public RefResult getFilterRefData(String value, int index);
	
	public int getPageSize();

	/**
	 * ����ĳ�����ֶΣ����˶�Ӧ��pk��ָ�������ֶΡ���������а���������Ĭ��ȡ��һ��
	 * @param value
	 * @param returnField
	 * @return
	 */
	public String[] matchRefPk(String value, String returnField);
	
	/**
	 * ��������PK�����˶�Ӧ�����ֶ�
	 * @param pk
	 * @return
	 */
	public String matchRefName(String pk);
	/**
	 * ��ȡ�����ֶε�ֵ
	 * @param value
	 * @param readFields
	 * @return
	 */
	public List<Object> getReadFieldValues(String value, String[] readFields);
	
}
