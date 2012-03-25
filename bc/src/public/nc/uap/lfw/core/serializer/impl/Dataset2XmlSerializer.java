package nc.uap.lfw.core.serializer.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.logging.Logger;
import nc.bs.pub.formulaparse.FormulaParse;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.uap.lfw.core.common.EventContextConstant;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.EmptyRow;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldRelation;
import nc.uap.lfw.core.data.FieldRelations;
import nc.uap.lfw.core.data.MatchField;
import nc.uap.lfw.core.data.Parameter;
import nc.uap.lfw.core.data.ParameterSet;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowData;
import nc.uap.lfw.core.data.RowSet;
import nc.uap.lfw.core.data.WhereField;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.util.ICodeRuleDsUtil;
import nc.uap.lfw.core.serializer.IObject2XmlSerializer;
import nc.uap.lfw.util.JsURLDecoder;
import nc.uap.lfw.util.JsURLEncoder;
import nc.uap.lfw.util.LfwClassUtil;
import nc.vo.pub.SuperVO;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFLiteralDate;
/**
 * ���ݼ����ͻ���Xml���л�������ʽ��FieldRelation������׶α�����,�˽�����ÿ�ζ�������ʵ��
 *
 */
public class Dataset2XmlSerializer implements IObject2XmlSerializer<Dataset[]> {
	private static int BUFFER_SIZE = 8 * 1024;
	private FormulaParse fp = new FormulaParse();
	
	/**
	 * ���л����dataset
	 */
	public String[] serialize(Dataset[] dss) {
		if(dss != null && dss.length > 0){
			String[] rss = new String[dss.length];
			for (int i = 0; i < dss.length; i++) {
				Dataset ds = dss[i];
				//������У�����������������ṹ�����Ϊ��PK�������˸����ڱ�ҳ��Ự��ֻ����һ��
				if(ds.getExtendAttribute(Dataset.CODE_LEVEL_CLAZZ) != null){
					ICodeRuleDsUtil dsUtil = (ICodeRuleDsUtil) LfwClassUtil.newInstance((String) ds.getExtendAttributeValue(Dataset.CODE_LEVEL_CLAZZ));
					String codeRule = (String) ds.getExtendAttributeValue(Dataset.CODE_LEVEL_RULE);
					String codeField = (String) ds.getExtendAttributeValue(Dataset.CODE_LEVEL_CODEFIELD);
					String codePk = (String) ds.getExtendAttributeValue(Dataset.CODE_LEVEL_PK);
					String codePPk = (String) ds.getExtendAttributeValue(Dataset.CODE_LEVEL_PPK);
					int codeIndex = ds.getFieldSet().nameToIndex(codeField);
					int pkIndex = ds.getFieldSet().nameToIndex(codePk);
					int ppkIndex = ds.getFieldSet().nameToIndex(codePPk);
					dsUtil.buildCodeRuleDataset(ds, codeRule, codeIndex, pkIndex, ppkIndex);
				}
				
				//�����ֶι���
				processFieldRelation(ds);
				processFormular(ds);
				StringBuffer xmlSb = new StringBuffer(BUFFER_SIZE);
				xmlSb.append("<dataset id=\""  + ds.getId() + "\" focusIndex=\"" + ds.getFocusIndex() + "\" currentkey=\"" + (ds.getCurrentKey() == null ? EventContextConstant.NULL : ds.getCurrentKey()) + "\" editable=\"" + ds.isEnabled() + "\" randomRowIndex=\"" + ds.getRandomRowIndex() + "\">\n");
				xmlSb.append("    <" + EventContextConstant.res_parameters + ">\n");
				ParameterSet resParamSet = ds.getResParameters();
				int count = resParamSet.size();
				for (int j = 0; j < count; j++) {
					Parameter param = resParamSet.getParameter(j);
					String name = param.getName();
					String value = param.getValue();
					xmlSb.append("      <" + EventContextConstant.parameter + " name=\"" + name + "\">");
						xmlSb.append(value == null? "" : value)
						     .append("</" + EventContextConstant.parameter + ">\n");
				}
				xmlSb.append("    </" + EventContextConstant.res_parameters + ">\n");
				xmlSb.append("    <rowsets>\n");
				RowSet[] rowsets = ds.getRowSets();
				for (int j = 0; j < rowsets.length; j++) {
					RowSet rowSet = rowsets[j];
					int pagecount = rowSet.getPaginationInfo().getPageCount();
					int pagesize = rowSet.getPaginationInfo().getPageSize();
					int recordscount = rowSet.getPaginationInfo().getRecordsCount();
					int pageindex = rowSet.getPaginationInfo().getPageIndex();
					xmlSb.append("    <rowset pagecount=\"")
					     .append(pagecount)
					     .append("\" pagesize=\"")
					     .append(pagesize)
					     .append("\" recordcount=\"")
					     .append(recordscount)
					     .append("\" pageindex=\"")
					     .append(pageindex)
					     .append("\" keyvalue=\"")
					     .append(rowSet.getKeyvalue())
					     .append("\"");
					if(rowSet.getOldKeyValue() != null){
						xmlSb.append(" oldkeyvalue=\"")
						     .append(rowSet.getOldKeyValue())
						     .append("\"");
					}
					    xmlSb.append(">\n");
					
					RowData[] rds = rowSet.getRowDatas();
					for (int k = 0; k < rds.length; k++) {
						RowData rdata = rds[k];
						if(rdata.isRowDataChanged()){
							xmlSb.append("    <" + EventContextConstant.records + " pageindex=\"" + rdata.getPageindex() + "\" changed=\"" + rdata.isRowDataSelfChanged() + "\">\n");
							Integer[] indices = rdata.getSelectIndices();
							if(indices != null && indices.length > 0){
								xmlSb.append("<selected>");
								for (int l = 0; l < indices.length; l++) {
									xmlSb.append(indices[l]);
									if(l != indices.length - 1){
										xmlSb.append(",");
									}
								}
								xmlSb.append("</selected>\n");
							}
							processRecord(xmlSb, rdata);
							xmlSb.append("</" + EventContextConstant.records + ">\n");
						}
					}
					xmlSb.append("    </rowset>\n");
				}
				xmlSb.append("    </rowsets>\n");
				xmlSb.append("</dataset>\n");
				rss[i] = xmlSb.toString();
			}
			return rss;
		}
		return null;
	}

	private void processFormular(Dataset ds) {
		RowData rd = ds.getCurrentRowData();
		if(rd == null)
			return;
		int rcount = rd.getRowCount();
		List<String> executedFpList = new ArrayList<String>();
		int fieldCount = ds.getFieldSet().getFieldCount();
		for(int i = 0; i < fieldCount; i ++){
			try{
				Field field = ds.getFieldSet().getField(i);
				List<String> fieldFormulars = new ArrayList<String>();
				String formular = field.getEditFormular();
				if(formular != null)
					fieldFormulars.add(formular);
				
				formular = field.getLoadFormular();
				if(formular != null)
					fieldFormulars.add(formular);
				
				Iterator<String> fit = fieldFormulars.iterator();
				while(fit.hasNext()){
					formular = fit.next();
					String exp = null;
					if(formular != null)
						exp = JsURLDecoder.decode(formular,"UTF-8");
					//���û�м��ع�ʽ��������һ���ֶ�
					if(exp == null)
						continue;
					
					if(executedFpList.contains(exp))
						continue;
					executedFpList.add(exp);
					String[] expArr = exp.split(";");
						
					fp.setExpressArray(expArr);
					VarryVO[] varryVOs = fp.getVarryArray();
					if(varryVOs == null || varryVOs.length == 0)
						continue;
					String[] formularNames = new String[varryVOs.length];
					
					Map<String,Integer> indexMap = getIndexMap(ds);
					for(int j = 0; j < varryVOs.length; j ++){
						String[] keys = varryVOs[j].getVarry();
						if(keys != null){
							for(String key : keys){
								List<Object> valueList = new ArrayList<Object>();
								for(int k = 0; k < rcount; k ++){
									Row r = rd.getRow(k);
//									
									Object value = r.getValue(indexMap.get(key));
									Field f = ds.getFieldSet().getField(key);
									if(f != null && value != null){
//										//�����Double���ͣ�����һ��ת��
										if(StringDataTypeConst.UFDOUBLE.equals(f.getDataType()) || StringDataTypeConst.DOUBLE.equals(f.getDataType()) || StringDataTypeConst.Decimal.equals(f.getDataType())){
											if(!(value instanceof UFDouble))
												value = new UFDouble(value.toString());
										}
										else if(StringDataTypeConst.INTEGER.equals(f.getDataType())){
											if(!(value instanceof Integer))
												value = new Integer((String)value);
										}
									}
									valueList.add( value );
								}
								fp.addVariable(key, valueList);
							}
							formularNames[j] = varryVOs[j].getFormulaName();
						}
					}
					
					Object[][] result = fp.getValueOArray();
					if(result != null){
						for (int l = 0; l < formularNames.length; l++) {
							int index = ds.nameToIndex(formularNames[l]);
							if(index == -1){
								LfwLogger.error("can not find column:" + formularNames[l] + ", ds id:" + ds.getId());
								continue;
							}
							for (int k = 0; k < rcount; k++) {
								Row r = rd.getRow(k);
								r.setValue(index, result[l][k]);
							}
						}
					}
				}
			}
			catch(Throwable e){
				Logger.error(e.getMessage(), e);
			}
		}
	}
	
	
	
	/**
	 * ͨ��ds����Ӧfield
	 * @param ds
	 * @return
	 */
	private Map<String,Integer> getIndexMap(Dataset ds) {
		Map<String,Integer> indexMap = new HashMap<String,Integer>();
		int count = ds.getFieldSet().getFieldCount();
		for(int i=0;i < count; i++){
			Field field = ds.getFieldSet().getField(i);
			String key = field.getId();
			indexMap.put(key,i);
		}
		return indexMap;
	}

	/**
	 * �ֶι���������
	 * @param ds
	 */
	private void processFieldRelation(Dataset ds) {
		FieldRelations frs = ds.getFieldRelations();
		if(frs != null){
			FieldRelation[] rels = frs.getFieldRelations();
			dealFieldRelation(ds, rels);
		}

	}

	/**
	 * �ֶι�������
	 * @param ds
	 * @param rels
	 */
	public void dealFieldRelation(Dataset ds, FieldRelation[] rels) {
		if(rels != null && rels.length != 0) {
			for (int i = 0; i < rels.length; i++) {
				FieldRelation rel = rels[i];
				try {
					if(!needDoRel(ds, rel))
						continue;
					String sql = aggregateSql(ds, rel);
					List<Object[]> result = null;
					if(sql != null){
						result = (List<Object[]>) CRUDHelper.getCRUDService().query(sql, new ArrayListProcessor());
					}
					else{
						result = new ArrayList<Object[]>();
					}
					//����������Ϊ�գ�������һ�У�ʹ�����ÿ��߼��ɼ�������
					if(result.size() == 0)
						result.add(null);
					Iterator<Object[]> it = result.iterator();
					
					WhereField whereField = rel.getWhereField();
					//����ֶ�
					String key = ds.getFieldSet().getField(whereField.getValue()).getId();
					//�����ֶ�
					MatchField[] mfs = rel.getMatchFields();
					int[] mfIndices = new int[mfs.length];
					for (int j = 0; j < mfIndices.length; j++) {
						mfIndices[j] = ds.nameToIndex(mfs[j].getWriteField());
					}
					
					//��¼�Ƿ�������ı�ʶ���Ա��ڴ����ѡ����
					Row[] rows = ds.getCurrentRowData().getRows();
					boolean[][] notFirst = new boolean[rows.length][];
					for (int j = 0; j < notFirst.length; j++) {
						notFirst[j] = new boolean[mfIndices.length];
						
					}
					
					while(it.hasNext()){
						
						Object[] values = it.next();
						int keyIndex = ds.nameToIndex(key);
						for (int j = 0; j < rows.length; j++) {
							if(rows[j] instanceof EmptyRow)
								continue;
							String keyValue = (String) rows[j].getValue(keyIndex);
							if(keyValue != null && values != null){
								//������ֵ�ָ�
								String[] keyValues = keyValue.split(",");
								for (int m = 0; m < keyValues.length; m++) {
									//�ҳ�ƥ�������ֵ������Ӧ��ֵ�ֶ�д��
									if(values[values.length - 1] != null && values[values.length - 1].toString().trim().equals(keyValues[m].trim())) {
										for (int k = 0; k < mfIndices.length; k++) {
											Object value = values[k];
											int mfIndex = mfIndices[k];
											Object oldValue = rows[j].getValue(mfIndex);
											
											//ԭ����ֵ��Ϊ�գ���2�������һ���ǲ���ֵ���������Ӧ��ȥ������һ������һѭ�����õģ��������Ӧ��ƴ�� ","
											if(oldValue != null){
												if(notFirst[j][k]){
													//SuperVO vo = (SuperVO) cavos[j].getCavo();
													if(!mfs[k].getReadField().equals(whereField.getKey())){
														value = oldValue.toString() + "," + value;
														rows[j].setValue(mfIndex, value);
													}
												}
												else{
													notFirst[j][k] = true;
													rows[j].setValue(mfIndex, value);
												}
											}
											else{
												notFirst[j][k] = true;
												rows[j].setValue(mfIndex, value);
											}
										}
										break;
									}
								}
							}
							//�������ÿ�
							else{
								for (int k = 0; k < mfIndices.length; k++) {
									Object value = null;
									int mfIndex = mfIndices[k];
									rows[j].setValue(mfIndex, value);
								}
							}
						}
					}
					if(rel.getChildRelationList() != null){
						dealFieldRelation(ds, rel.getChildRelationList().toArray(new FieldRelation[0]));
					}
				} 
				catch (Exception e) {
					Logger.error(e.getMessage(), e);
				}
			}
			
		}
	}
	
	protected boolean needDoRel(Dataset ds, FieldRelation rel) {
		if(ds.getCurrentRowData() == null)
			return false;
//		RowSet[] rss = ds.getRowSets();
//		for (int i = 0; i < rss.length; i++) {
//			RowSet rs = rss[i];
//			if(rs.isRowSetChanged()){
//				RowData[] rd
//			}
//		}
		return true;
	}

	private String aggregateSql(Dataset ds, FieldRelation rel) {
		if(ds.getCurrentKey() == null)
			return null;
		Dataset refDs = ds.getWidget().getViewModels().getDataset(rel.getRefDataset());
		if(refDs == null){
			Logger.error("ref ds can not be fount:" + rel.getRefDataset());
			return null;
		}
		if(rel.getWhereField() == null)
			return null;
		MatchField[] mfs = rel.getMatchFields();
		WhereField where = rel.getWhereField();
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		for (int i = 0; i < mfs.length; i++) {
			Field f = refDs.getFieldSet().getField(mfs[i].getReadField());
			if(f == null)
				throw new LfwRuntimeException("can not find field in ds:" + rel.getRefDataset() + ",field:" + mfs[i].getReadField() + ", in fieldrelation process");
			sql.append("a.")
				.append(f.getField());
			if(i != mfs.length - 1)
				sql.append(",");
			}
		sql.append(",a.")
			.append(refDs.getFieldSet().getField(where.getKey()).getField());
		sql.append(" from ");
		sql.append(getRefTableName(refDs));
		sql.append(" a where a.")
		.append(refDs.getFieldSet().getField(where.getKey()).getField())
		.append(" in (");
  
		RowData rowSet = ds.getCurrentRowSet().getCurrentRowData();
		Set<String> inSet = new HashSet<String>();
		if (rowSet != null) {
			Row[] rows = rowSet.getRows();
			int whereIndex = ds.nameToIndex(where.getValue());
			if(whereIndex == -1)
				return null;
			for (int i = 0; i < rows.length; i++) {
				if(rows[i] instanceof EmptyRow)
					continue;
				Object wValue = rows[i].getValue(whereIndex);
				String wValueStr = "";
				if(wValue != null){
					wValueStr = wValue.toString();
					String[] strs = wValueStr.split(",");
					for (int j = 0; j < strs.length; j++) {
						inSet.add(strs[j]);
					}
				}
			}
		}

		if (inSet.size() == 0)
			return null;

		Iterator<String> it = inSet.iterator();
		while (it.hasNext()) {
			String value = it.next();
			sql.append("'");
			sql.append(value);
			sql.append("'");
			if (it.hasNext())
				sql.append(",");
		}
		sql.append(")");
		return sql.toString();
	 }

	private String getRefTableName(Dataset refDs) {
		String superClazz = refDs.getVoMeta();
		SuperVO vo = (SuperVO) LfwClassUtil.newInstance(superClazz);
		return vo.getTableName();
	}


	/**
	 * ����״̬ת������Ӧ�ؼ�¼Ӧ���ʽ��ע�⣬��Ϊ�����ɾ����¼���᷵��ajaxӦ������
	 * �˴�������ɾ��״̬��
	 * 
	 * @param xmlSb
	 * @param pm 
	 * @param rows
	 * @param state
	 */
	protected void processRecord(StringBuffer xmlSb, RowData data) {

		StringBuffer rowCountsb = null;
		int size = data.getRowCount();
		for (int i = 0; i < size; i++) {
			Row row = data.getRow(i);
			int fieldCount = row.size();
			rowCountsb = new StringBuffer();
			
			boolean isEmptyRow = (row instanceof EmptyRow);
			String recordSign = "<" + EventContextConstant.record + "";
			String recordESign = "</" + EventContextConstant.record + ">";
			if(isEmptyRow){
				recordSign = "<" + EventContextConstant.erecord;
				recordESign = "</" + EventContextConstant.erecord + ">";
			}
			xmlSb.append(recordSign + " id=\"" + row.getRowId() + "\" ");
			if(!isNull(row.getParentId()))
				xmlSb.append(" parentid=\"" + row.getParentId() + ">");
			else
				xmlSb.append(">");
			
			if(!isEmptyRow){
				String[] startend = getStartEndByState(row.getState());
				xmlSb.append(startend[0]);
				
				Object value = null;
				for (int j = 0; j < fieldCount; j++) {
					value = row.getValue(j);
					if (value != null){
						//��������ʱ������ dingrf
						if (row.getContent()[j] instanceof UFDate){
							value = ((UFDate)row.getContent()[j]).getMillis();
						}
						else if (row.getContent()[j] instanceof UFDateTime){
							value = ((UFDateTime)row.getContent()[j]).getMillis();
						}
						else if (row.getContent()[j] instanceof UFLiteralDate){
							value = ((UFLiteralDate)row.getContent()[j]).getMillis();
						}
						rowCountsb.append(JsURLEncoder.encode(value.toString(), "UTF-8"));
					}
					else
						rowCountsb.append(EventContextConstant.NULL);
					if (j < fieldCount - 1)
						rowCountsb.append(",");
				}
				xmlSb.append(rowCountsb);
				xmlSb.append(startend[1]);
				
				//dingrf��¼�����з����޸ĵ�������
				String changedSign = "<" + EventContextConstant.changed + ">";
				String changedESign = "</" + EventContextConstant.changed + ">";
				xmlSb.append(changedSign);
				for (int k = 0; k < row.getChangedIndices().size(); k++){
					xmlSb.append(row.getChangedIndices().get(k));
					if (k < row.getChangedIndices().size() - 1)
						xmlSb.append(",");
				}
				xmlSb.append(changedESign);
				
			}
			xmlSb.append(recordESign + "\n");
		}
		
		try{
			Method m = RowData.class.getDeclaredMethod("getDeleteRows", new Class[]{});
			m.setAccessible(true);
			Row[] delRows = (Row[]) m.invoke(data, new Object[]{});
			if(delRows != null && delRows.length > 0){
				for (int i = 0; i < delRows.length; i++) {
					xmlSb.append("<" + EventContextConstant.drecord + " id=\"" + delRows[i].getRowId() + "\"/>");
				}
			}
		}
		catch(Exception e){
			LfwLogger.error(e);
		}
	}
	
	private String[] getStartEndByState(int state) {
		String strStateStart, strStateEnd;
		if (state == Row.STATE_ADD) {
			strStateStart = "<add>";
			strStateEnd = "</add>";
		} 
		else if (state == Row.STATE_UPDATE) {
			strStateStart = "<upd>";
			strStateEnd = "</upd>";
		}
		else if(state == Row.STATE_DELETED)
		{
			strStateStart = "<del>";
			strStateEnd = "</del>";
		} 
		//��ɾ��
		else if (state == Row.STATE_FALSE_DELETED){
			strStateStart = "<fdel>";
			strStateEnd = "</fdel>";
		}
		else {
			strStateStart = "<nrm>";
			strStateEnd = "</nrm>";
		}
		return new String[]{strStateStart, strStateEnd};
	}
	
	private static boolean isNull(String s) {
		if (s == null || s.trim().equals(""))
			return true;
		else
			return false;
	}	
}

