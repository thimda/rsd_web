package nc.uap.lfw.reference.base;

import java.util.List;

/**
 * web参照模型，接口参考NC 参照模式。
 *
 */
public interface ILfwRefModel {
	// 参照类型
	public static final Integer REFTYPE_GRID = 0;
	public static final Integer REFTYPE_TREE = 1;
	public static final Integer REFTYPE_TREEGRID = 2;
	public static final Integer REFTYPE_TWOTREE = 3;
	public static final Integer REFTYPE_TWOTREEGRID = 4;
	public static final Integer REFTYPE_TREEGRIDTEXT=5;

	public static final String FILTERSQL_MASK = "#R#";
	
	/**
	 * 获取参照显示列的字段的编码 
	 */
	public String[] getVisibleFieldCodes();
	
	/**
	 * 获取参照隐藏列的字段的编码
	 * @return
	 */
	public String[] getHiddenFieldCodes();
	

	/**
	 * 获取参照pk字段
	 * @return
	 */
	public String getRefPkField();

	/**
	 * 获取参照编码字段
	 * @return
	 */
	public String getRefCodeField();
	
	/**
	 * 获取参照名称字段
	 * @return
	 */
	public String getRefNameField();
	
	/**
	 * 获取参照数据(二维表结构)
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
	 * 获取参照类型
	 * @return
	 */
	public Integer getRefType();
	
	/**
	 * 获取字段列的下标
	 * @param field
	 * @return
	 */
	public int getFieldIndex(String field);

	public RefResult getFilterRefData(String value, int index);
	
	public int getPageSize();

	/**
	 * 根据某输入字段，过滤对应的pk及指定返回字段。如果过滤中包含多条，默认取第一条
	 * @param value
	 * @param returnField
	 * @return
	 */
	public String[] matchRefPk(String value, String returnField);
	
	/**
	 * 根据输入PK，过滤对应名称字段
	 * @param pk
	 * @return
	 */
	public String matchRefName(String pk);
	/**
	 * 获取读入字段的值
	 * @param value
	 * @param readFields
	 * @return
	 */
	public List<Object> getReadFieldValues(String value, String[] readFields);
	
}
