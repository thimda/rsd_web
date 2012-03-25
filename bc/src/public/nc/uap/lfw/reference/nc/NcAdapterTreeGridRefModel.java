package nc.uap.lfw.reference.nc;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.uap.lfw.reference.base.RefResult;
import nc.uap.lfw.reference.base.TreeGridRefModel;
import nc.ui.bd.ref.AbstractRefGridTreeModel;

public class NcAdapterTreeGridRefModel extends TreeGridRefModel implements INcRefModel{
	private AbstractRefGridTreeModel ncModel;
	public AbstractRefGridTreeModel getNcModel() {
		return ncModel;
	}

	public void setNcModel(AbstractRefGridTreeModel ncModel) {
		this.ncModel = ncModel;
	}

	public String getClassRefCodeField() {
		return ncModel.getClassRefCodeField();
	}

	public String getClassRefNameField() {
		return ncModel.getClassRefNameField();
	}

	public String getClassChildField() {
		String childField =  ncModel.getChildField();
		if(childField == null)
			childField = ncModel.getClassChildField();
		return childField;
	}

	public String getClassFatherField() {
		String classFatherField =  ncModel.getFatherField();
		if(classFatherField == null)
			classFatherField = ncModel.getClassFatherField();
		return classFatherField;
	}

	public String getClassRootName() {
		return ncModel.getRootName();
	}

	public int getFieldIndex(String field) {
		return getModelFieldIndex(ncModel, field);
	}

	public String[] getHiddenFieldCodes() {
		return ncModel.getHiddenFieldCode();
	}

	public String[] getHiddenFieldNames() {
		return ncModel.getHiddenFieldCode();
	}

	public String getRefCodeField() {
		return ncModel.getRefCodeField();
	}

	public String getRefNameField() {
		return ncModel.getRefNameField();
	}

	public String getRefPkField() {
		return ncModel.getPkFieldCode();
	}

	public String[] getVisibleFieldCodes() {
		return ncModel.getFieldCode();
	}

	@Override
	public String[] getVisibleFieldNames() {
		return ncModel.getFieldName();
	}

	@Override
	public String getClassJoinField() {
		return ncModel.getClassJoinField();
	}

	@Override
	public String getDocJoinField() {
		return ncModel.getDocJoinField();
	}

	@Override
	public void setClassJoinValue(String keyValue) {
		ncModel.setClassJoinValue(keyValue);
	}

	
	public String[] getFilterFields() {
		return new String[]{getRefCodeField(), getRefNameField()};
	}

	public String getQuerySql() {
		return ncModel.getQuerySql();
	}

	protected String aggQuerySql() {
		StringBuffer buf = new StringBuffer();
		String sql = getQuerySql();
		buf.append(sql);
		String[] fields = getVisibleFieldCodes();
		if(sql.indexOf("where") == -1)
			buf.append(" where ");
		else
			buf.append(" and ");
		for (int i = 0; i < fields.length; i++) {
			buf.append(fields[i]);
			buf.append(" = '");
			buf.append(FILTERSQL_MASK);
			buf.append("'");
			if(i != fields.length - 1)
				buf.append(" and ");
		}
		return sql;
	}

	public String getFixedWherePart() {
		return ncModel.getOriginWherePart();
	}

	public String getGroupPart() {
		return ncModel.getGroupPart();
	}

	public String getOrderPart() {
		return ncModel.getOrderPart();
	}

	public String getTablesString() {
		return ncModel.getTableName();
	}

	public String getWherePart() {
		return ncModel.getWherePart();
	}

	public void setWherePart(String wherePart) {
		ncModel.setWherePart(wherePart);
	}

	public String getStrPatch() {
		return ncModel.getStrPatch();
	}

	@Override
	public String getClassRefPkField() {
		//首先取是否设置了树的递归child字段，取他作为树主键
		String classRefPkField = ncModel.getClassChildField();//return ncModel.getClassJoinField();
		//如果没有设置，取和grid关联的字段作为pk字段
		if(classRefPkField == null)
			classRefPkField = ncModel.getClassJoinField();
		return classRefPkField;
	}

	@Override
	public String getClassTableName() {
		return ncModel.getClassTableName();
	}

	@Override
	public String getClassJoinValue() {
		String value = ncModel.getClassJoinField();
		if(value == null)
			value = ncModel.getClassJoinValue();
		return value;
	}

	@Override
	public String getClassWherePart() {
		return ncModel.getClassWherePart();
	}

	@Override
	public void setClassWherePart(String wherePart) {
		ncModel.setClassWherePart(wherePart);
	}

	@Override
	protected List<List<Object>> getClassData(String sql) {
		RefResult rs = null;
		//此方法被重写，以此方法来加载数据
		try {
			//Method m = ncModel.getClass().getDeclaredMethod("getData", new Class[]{});
			//if(m != null){
			//	m.setAccessible(true);
				Vector data = ncModel.getClassData();
				rs = convertToList(data, getPageSize(), 0);
			//}
		} 
		catch (SecurityException e) {
			Logger.error(e.getMessage(), e);
		} 
//		catch (NoSuchMethodException e) {
//		}
		
		if(rs != null)
			return rs.getData();
		return super.getClassData(sql);
	}

	@Override
	public RefResult getRefData(int index) {
		RefResult rs = null;
		//此方法被重写，以此方法来加载数据
		try {
			//Method m = ncModel.getClass().getDeclaredMethod("getData", new Class[]{});
			//if(m != null){
			//	m.setAccessible(true);
				Vector data = ncModel.getData();
				if(ncModel.getFilterPks() != null && ncModel.getFilterPks().length > 0){
					ncModel.setSelectedData(data);
					ncModel.setFilterPks(ncModel.getFilterPks());
					data = ncModel.getSelectedData();
				}
				rs = convertToList(data, getPageSize(), index);
			//}
		} 
		catch (SecurityException e) {
			Logger.error(e.getMessage(), e);
		} 
//		catch (NoSuchMethodException e) {
//		}
		
		if(rs == null)
			rs = super.getRefData(index);
		
		//处理参照列枚举问题，考虑如何做成通用机制
		if (ncModel.getDispConvertor() != null) {
			Hashtable convert = ncModel.getDispConvertor();
			Enumeration e = convert.keys();
			while (e.hasMoreElements()) {
				String convertColumn = (String) e.nextElement();
				Hashtable conv = (Hashtable) convert.get(convertColumn);
				Integer Idx = getFieldIndex(convertColumn);
				if (Idx == null || Idx.intValue() < 0)
					continue;
				int idx = Idx.intValue();
				List<List<Object>> data = rs.getData();
				if (data != null) {
					int rows = data.size();
					for (int i = 0; i < rows; i++) {
						List<Object> vRow = data.get(i);
						if (vRow != null) {
							Object oldValue = vRow.get(idx);
							if (oldValue == null)
								continue;
							Object newValue = conv.get(oldValue.toString());
							if (newValue != null) {
								vRow.set(idx, newValue);
								// 如果需求，可以转换为原值，目前先保持翻译后的值
								// Object obj =
								// getRefValueVO(vRow.elementAt(idx),
								// newValue);
								// vRow.setElementAt(obj, idx);
							}
						}
					}
				}
			}
		}
		return rs;
	}
	
	private RefResult convertToList(Vector vec, int pageSize, int index){
		if(vec == null)
			return null;
		int start = pageSize * index;
		//左边的树没有分页，所以默认显示所有值
		int end = pageSize * (index + 1);
		List<List<Object>> list = new ArrayList<List<Object>>();
		int size = vec.size();
		for(int i = start; i < size; i ++){
			Vector vecData = (Vector) vec.get(i);
			List<Object> listData = new ArrayList<Object>();
			listData.addAll(vecData);
			list.add(listData);
		}
		
		RefResult r = new RefResult();
		r.setTotalCount(size);
		r.setData(list);
		return r;
	}

	@Override
	public String[] getClassFieldCodes() {
		return ncModel.getClassFieldCode();
	}
}
