package nc.uap.lfw.reference.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.crud.ILfwCRUDService;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

public abstract class BaseRefModel implements ILfwRefModel {
	
	// 数据模型 字段名称-字段索引 映射集合
	private Map<String, Integer> htCodeIndex = null;
	
	private String wherePart;

	
	public RefResult getFilterRefData(String value, int index) {
		setFilterValue(value);
		RefResult data = getRefData(index);
		setWherePart("");
		return data;
	}
	
	public String getStrPatch() {
		return "";
	}

	public void setFilterValue(String value) {
		String buf = generateFilterSql(value);
		addWherePart(buf);
	}

	protected String generateFilterSql(String value) {
		String[] fields = getFilterFields();
		if(fields == null)
			fields = getVisibleFieldCodes();
		StringBuffer buf = new StringBuffer();
		buf.append("(");
		for (int i = 0; i < fields.length; i++) {
			String field = cutOffAs(fields[i]);
			buf.append(field);
			buf.append(" like '%");
			buf.append(value);
			buf.append("%'");
			if(i != fields.length - 1)
				buf.append(" or ");
		}
		buf.append(")");
		return buf.toString();
	}
	
	protected String cutOffAs(String oriField) {
		int index = oriField.indexOf(" as ");
		if(index != -1)
			oriField = oriField.substring(0, index);
		return oriField;
	}

	public void setBlurValue(String value) {
		String code = getRefCodeField();
		String name = getRefNameField();
		String[] fields = new String[]{code, name};
		StringBuffer buf = new StringBuffer();
		buf.append("(");
		for (int i = 0; i < fields.length; i++) {
			buf.append(cutOffAs(fields[i]));
			buf.append(" = '");
			buf.append(value);
			buf.append("'");
			if(i != fields.length - 1)
				buf.append(" or ");
		}
		buf.append(")");
		setWherePart(buf.toString());
	}
	
	public RefResult getRefData(int index) {
		return getRefData(getRefSql(), index);
	}
	public String getRefSql(boolean withorder) {
		return getRefSqlUtil().getRefSql(withorder);
	}	
	public String getRefSql() {
		return getRefSqlUtil().getRefSql();
	}
	
	public String getFirstRefSql() {
		return getRefSqlUtil().getFirstRefSql();
	}
	
	/**
	 * 返回根据第一级树查询子数据集得到的结果
	 * @param index
	 * @return
	 */
	public RefResult getFirstRefData(int index) {
		return getRefData(getFirstRefSql(), index);
	}
	
	public String getOrderSql()
	{
		return getRefSqlUtil().getRefOrderSql();
	}
	
	protected RefResult getRefData(String sql, int index) {
		BaseDAO dao = new BaseDAO();
		try {
			//暂时不用分页
			int begin = -1;
			int end = -1;
			if(index != -1 && getPageSize() != -1){
				begin = index * getPageSize();
				end = (index + 1) * getPageSize();
			}
			RefResult result = (RefResult) dao.executeQuery(sql, new RefDataProcessor(begin, end));
			return result;
//			RefResult result = (RefResult) dao.executeQuery(sql, new RefDataProcessor(0, 1000000));
//			return result;
		} 
		catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwRuntimeException("查询数据出现问题," + e.getMessage());
		}
		
	
	}
	
	protected RefSqlUtil getRefSqlUtil() {
		RefSqlUtil util = new RefSqlUtil(this);
		return util;
	}
	
	protected ILfwCRUDService<SuperVO, AggregatedValueObject> getCrudService() {
		return CRUDHelper.getCRUDService();
	}
	
	public String[] matchRefPk(String value, String returnField){
		setBlurValue(value);
		RefResult data = getRefData(0);
		setWherePart("");
		if(data == null || data.getData().size() == 0)
			return null;
		List<Object> dataRow = data.getData().get(0);
		int nameIndex = getFieldIndex(returnField);
		int pkIndex = getFieldIndex(getRefPkField());
		return new String[]{(String) dataRow.get(pkIndex), (String) dataRow.get(nameIndex)};
	}
	

	public String matchRefName(String pk) {
		String pkField = getRefPkField();
		String wherePart = pkField + " = '" + pk + "'";
		addWherePart(wherePart);
		RefResult data = getRefData(0);
		setWherePart("");
		if(data.getData().size() == 0)
			return null;
		List<Object> dataRow = data.getData().get(0);
		int nameIndex = getFieldIndex(getRefNameField());
		return (String) dataRow.get(nameIndex);
	}
	
	/**
	 * 获取读入字段的值
	 * @param value
	 * @param readFields
	 * @return
	 */
	public List<Object> getReadFieldValues(String value, String[] readFields) {
		setBlurValue(value);
		RefResult data = getRefData(0);
		setWherePart("");
		if(data == null || data.getData().size() == 0)
			return null;
		List<Object> dataRow = data.getData().get(0);
		List<Object> result = new ArrayList<Object>();
		for (int i = 0, n = readFields.length; i < n; i++) {
			result.add(dataRow.get(getFieldIndex(readFields[i])));
		}
		return result;
	}
	
	/**
	 * 获取数据模型字段索引
	 * @param ncModel 数据模型
	 * @param field 字段名
	 * @return
	 */
	public int getModelFieldIndex(AbstractRefModel ncModel, String field) {
		if (field == null || field.trim().length() == 0
				|| getHtCodeIndex(ncModel) == null || getHtCodeIndex(ncModel).size() == 0)
			return -1;
		Object o = getHtCodeIndex(ncModel).get(field.trim());
		if (o == null) {
			// 加入动态列
			int index = htCodeIndex.size();
			if (ncModel.isDynamicCol() && ncModel.getDynamicFieldNames() != null) {
				for (int i = 0; i < ncModel.getDynamicFieldNames().length; i++) {
					htCodeIndex.put(ncModel.getDynamicFieldNames()[i].trim(),
							new Integer(index + i));
				}
			}
			o = getHtCodeIndex(ncModel).get(field.trim());
		}
		return (o == null) ? -1 : ((Integer) o).intValue();
	}
	
	/**
	 * 获取数据模型 字段名称-字段索引 映射集合
	 * @param ncModel 数据模型
	 * @return
	 */
	private Map<String, Integer> getHtCodeIndex(AbstractRefModel ncModel) {
		if (htCodeIndex == null || htCodeIndex.size() == 0) {
			htCodeIndex = new HashMap<String, Integer>();
			if (ncModel.getFieldCode() != null)
				for (int i = 0; i < ncModel.getFieldCode().length; i++) {
					String key = ncModel.getFieldCode()[i].trim();
					if (-1 != key.indexOf(" "))
						key = key.substring(key.indexOf(" "));
					htCodeIndex.put(key, new Integer(i));
				}
			if (ncModel.getHiddenFieldCode() != null) {
				int index = 0;
				if (ncModel.getFieldCode() != null) {
					index = ncModel.getFieldCode().length;
				}
				for (int i = 0; i < ncModel.getHiddenFieldCode().length; i++) {
					if (ncModel.getHiddenFieldCode()[i] != null) {
						String key = ncModel.getHiddenFieldCode()[i].trim();
						if (-1 != key.indexOf(" "))
							key = key.substring(key.lastIndexOf(" ") + 1);
						htCodeIndex.put(key, new Integer(index + i));
					} else {
						LfwLogger.debug("Waring: The RefModel has some errors.");
					}
				}
			}
		}
		return htCodeIndex;
	}

	public void addWherePart(String wherePart) {
		String oriPart = getWherePart();
		if(oriPart != null && !oriPart.equals("")){
			wherePart = oriPart + " and (" + wherePart + ")";
		}
		setWherePart(wherePart);
	}
	
	public String getWherePart() {
		return wherePart;
	}

	public void setWherePart(String wherePart) {
		this.wherePart = wherePart;
	}
	
	public int getFieldIndex(String field) {
		String[] vfs = getVisibleFieldCodes();
		int i = 0;
		for (; i < vfs.length; i++) {
			if(vfs[i].equals(field))
				return i;
		}
		
		String[] hfs = getHiddenFieldCodes();
		if(hfs != null){
			for (int j = 0; j < hfs.length; j++) {
				if(hfs[j].equals(field))
					return i + j;
			}
		}
		return -1;
	}

}
