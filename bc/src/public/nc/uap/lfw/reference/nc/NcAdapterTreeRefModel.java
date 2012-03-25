package nc.uap.lfw.reference.nc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.uap.lfw.reference.base.RefResult;
import nc.uap.lfw.reference.base.TreeRefModel;
import nc.ui.bd.ref.AbstractRefTreeModel;
 
public class NcAdapterTreeRefModel extends TreeRefModel implements INcRefModel{
 
	private AbstractRefTreeModel ncModel;
	
	@Override
	public String getChildField() {
		return ncModel.getChildField();
	}

	@Override
	public String getFatherField() {
		return ncModel.getFatherField();
	}

	public String[] getHiddenFieldCodes() {
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

	public AbstractRefTreeModel getNcModel() {
		return ncModel;
	}

	public void setNcModel(AbstractRefTreeModel ncModel) {
		this.ncModel = ncModel;
	}

	@Override
	public String getRootName() {
		return ncModel.getRootName();
	}

	public int getFieldIndex(String field) {
//		return ncModel.getFieldIndex(field);
		return getModelFieldIndex(ncModel, field);
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
	public RefResult getRefData(int index) {
		//此方法被重写，以此方法来加载数据
		try {
			//Method m = ncModel.getClass().getDeclaredMethod("getData", new Class[]{});
			//if(m != null){
				Vector data = ncModel.getData();
				if(ncModel.getFilterPks() != null && ncModel.getFilterPks().length > 0){
					ncModel.setSelectedData(data);
					ncModel.setFilterPks(ncModel.getFilterPks());
					data = ncModel.getSelectedData();
				}
				if(data != null)
					return convertToList(data, getPageSize(), index);
			//}
		} 
		catch (SecurityException e) {
			Logger.error(e.getMessage(), e);
		} 
//		catch (NoSuchMethodException e) {
//			Logger.error(e.getMessage(), e);
//		}
		return super.getRefData(index);
	}
	
	private RefResult convertToList(Vector vec, int pageSize, int index){
		if(vec == null)
			return null;
		int start = pageSize * index;
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
}
