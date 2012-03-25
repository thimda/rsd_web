package nc.uap.lfw.reference.nc;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.uap.lfw.reference.base.GridRefModel;
import nc.uap.lfw.reference.base.RefResult;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.RefValueVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.formulaset.FormulaParseFather;

public class NcAdapterGridRefModel extends GridRefModel implements INcRefModel{
	 
	private AbstractRefModel ncModel;
	
	private FormulaParseFather parse;
	
	public NcAdapterGridRefModel() {
	}
	
	public AbstractRefModel getNcModel() {
		return ncModel;
	}
	
	public void setNcModel(AbstractRefModel ncModel) {
		this.ncModel = ncModel;
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
	

	@Override
	public String[] getHiddenFieldNames() {
		return ncModel.getHiddenFieldCode();
	}

	public String[] getVisibleFieldCodes() {
		return ncModel.getFieldCode();
	}

	@Override
	public String[] getVisibleFieldNames() {
		return ncModel.getFieldName();
	}

	public int getFieldIndex(String field) {
//		return ncModel.getFieldIndex(field);
		return getModelFieldIndex(ncModel, field);
	}

	public String[] getFilterFields() {
		List<String> list = new ArrayList<String>();
		String name = getRefNameField();
		if(name != null)
			list.add(name);
		String code = getRefCodeField();
		if(code != null)
			list.add(code);
		return list.toArray(new String[0]);
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

	public String getQuerySql() {
		return ncModel.getQuerySql();
	}

	public String getStrPatch() {
		return ncModel.getStrPatch();
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
				if(data == null)
					return null;
				if(ncModel.getFilterPks() != null && ncModel.getFilterPks().length > 0){
					ncModel.setSelectedData(data);
					ncModel.setFilterPks(ncModel.getFilterPks());
					data = ncModel.getSelectedData();
				}
				String[][] formulas = ncModel.getFormulas();
				if (data != null && data.size() > 0) {
					if(formulas != null && formulas.length > 0){
						String[][] showFormulas = getShowFieldFormulas(formulas);
						if (showFormulas == null) {
							return null;
						}
						Object[][] datas = getValues_formulas(data, showFormulas);
						String[] fieldCodes = ncModel.getShowFields();
						for (int i = 0; i < fieldCodes.length; i++) {
							
						}
						if (datas == null || datas.length != showFormulas.length) {
							return null;
						}
						Vector v = new Vector();
						String[] fieldNames = new String[datas.length];
						for (int i = 0; i < datas.length; i++) {
							v.add(datas[i]);
							fieldNames[i] = (String) showFormulas[i][0];
						}
						setValuesByFieldName(data, v, fieldNames);
					}
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
	
	
	/**
	 * 对nc参照单独处理一下blurValue
	 */
	public void setBlurValue(String value) {
		List<String> list = new ArrayList<String>();
		String code = getRefCodeField();
		if(code != null)
			list.add(code);
		String name = getRefNameField();
		if(name != null)
			list.add(name);
		String[] fields = list.toArray(new String[0]);
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
		//特殊处理
		ncModel.addWherePart(buf.toString());
	}
	
	/**
	 * 针对nc参照做额外的设置
	 */
	public void setFilterValue(String value) {
		super.setFilterValue(value);
		ncModel.addWherePart( " and " +  super.generateFilterSql(value));
	}
	
	private String[][] getShowFieldFormulas(String[][] formulas) {
		List<String[]> list = new ArrayList<String[]>();
		for (int i = 0; i < formulas.length; i++) {
			String formulaField = formulas[i][0];
			if (isShowField(formulaField)) {
				list.add(formulas[i]);
			}
		}
		String[][] showFormulas = null;
		if (list.size() > 0) {
			showFormulas = new String[list.size()][];
			list.toArray(showFormulas);
		}
		return showFormulas;
	}
	
	
	public boolean isShowField(String fieldName) {
		boolean isShowField = false;
		int[] showColumns = ncModel.getShownColumns();
		int index = getFieldIndex(fieldName);
		for (int i = 0; i < showColumns.length; i++) {
			if (showColumns[i] == index) {
				isShowField = true;
				break;
			}
		}
		return isShowField;
	}

	public String[] getShowFields() {
		String[] showFields = new String[ncModel.getShownColumns().length];
		for (int i = 0; i < showFields.length; i++) {
			showFields[i] = ncModel.getFieldCode()[ncModel.getShownColumns()[i]];
		}
		return showFields;
	}
	
	private RefValueVO getRefValueVO(Object originValue, Object newValue) {
		RefValueVO valueVO = new RefValueVO();
		valueVO.setOriginValue(originValue);
		valueVO.setNewValue(newValue);
		return valueVO;
	}
	
	private void setValuesByFieldName(Vector vecData, Vector newVecData,
			String[] fieldNames) {
		int col = -1;
		for (int i = 0; i < fieldNames.length; i++) {

			col = getFieldIndex(fieldNames[i]);
			Object[] values = (Object[]) newVecData.get(i);
			if (values == null) {
				continue;
			}
			for (int j = 0; j < values.length; j++) {
				Vector vRecord = (Vector) vecData.get(j);
				if (vRecord == null || vRecord.size() == 0 || col < 0
						|| col >= vRecord.size()) {
					continue;
				}
				Object obj = getRefValueVO(vRecord.elementAt(col), values[j]);
				vRecord.setElementAt(obj, col);

			}
		}
	}

	
	private Object[][] getValues_formulas(Vector vecData,
			String[][] formulas) {
		// 要翻译的列

		Object[][] datas = null;
		int flen = formulas.length;
		if (vecData != null && vecData.size() > 0 && flen > 0) {

			int recordAccout = vecData.size(); // 行记录数。
			datas = new Object[flen][recordAccout];
			ArrayList list = new ArrayList();
			String formulaField = null;
			String strFormula = null;
			for (int i = 0; i < flen; i++) {
				list.clear();
				formulaField = formulas[i][0];
				strFormula = formulas[i][1];

				for (int j = 0; j < recordAccout; j++) {
					Vector record = (Vector) vecData.get(j);
					list.add(getFormulaValue(record, formulaField));
				}
				datas[i] = getFormulaValues(formulaField, strFormula, list);
			}
		}

		return datas;

	}
	
	private String getFormulaValue(Vector record, String formulaField) {
		int fieldIndex = getFieldIndex(formulaField);
		if (fieldIndex == -1) {
			return null;
		}
		Object obj = record.get(fieldIndex);

		String value = null;

		if (obj instanceof RefValueVO && obj != null) {

			Object originValue = ((RefValueVO) obj).getOriginValue();

			value = originValue == null ? null : originValue.toString();

		} else {
			if (obj != null) {
				value = obj.toString();
			}
		}
		return value;
	}

	
	
	
	private FormulaParseFather getParse() {
		if (parse == null) {
			parse = new nc.ui.pub.formulaparse.FormulaParse();
		}
		return parse;
	}

	
	private Object[] getFormulaValues(String fieldName, String formulas,
			List givenvalues) {
		FormulaParseFather parse = getParse();
		// String express = formula;
		String[] expresses = StringUtil.toArray(formulas, ";");
		parse.setExpressArray(expresses);
		parse.addVariable(fieldName, givenvalues);
		Object[][] values = parse.getValueOArray();
		return values[values.length - 1];
	}
	
	private RefResult convertToList(Vector vec, int pageSize, int index){
		if(vec == null)
			return null;
		int start = pageSize * index;
		int end = pageSize * (index + 1);
		List<List<Object>> list = new ArrayList<List<Object>>();
		int size = vec.size();
		for(int i = start; i < end && i < size; i ++){
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
