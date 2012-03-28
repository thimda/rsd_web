package nc.uap.lfw.reference.base;

import java.util.StringTokenizer;

import nc.ui.bd.ref.IRefConst;

public class RefSqlUtil {
	private ILfwRefModel model;
	public RefSqlUtil(ILfwRefModel model){
		this.model = model;
	}

	/**
	 *���������ݼ���ѯsql 
	 */
	public String getRefSql() {
		return getSpecialSql();		
	}
	
	/**
	 * ���ظ��ݵ�һ������ѯ�����ݼ�sql
	 * @return
	 */
	public String getFirstRefSql(){
		return getFirstLevelSpecialSql(true);
	}
	/**
	 * ���������ݼ���ѯsql
	 * @param withorder
	 * @return
	 */
	public String getRefSql(boolean withorder) {
		return getSpecialSql(withorder);
	}
	/**
	 * ��������sql
	 * @return
	 */
	public String getRefOrderSql()
	{
		StringBuffer buffer = new StringBuffer("");
		String order = model.getOrderPart();
		// ����ORDER�Ӿ�
		if (order != null && order.trim().length() != 0) {
			buffer.append("order by ").append(filterColumn(order));
		}
		return buffer.toString();
	}
	/**
	 *�����������ݹ�����������sql 
	 */
	public String getCLassOrderSql()
	{
		StringBuffer buffer = new StringBuffer("");
		String order = "";
		if(model instanceof TreeGridRefModel){
			TreeGridRefModel treegridmodel = (TreeGridRefModel) model;
			order = treegridmodel.getClassOrderPart();
		} 
		else if(model instanceof TwoTreeRefModel)
		{
			TwoTreeRefModel treemodeol = (TwoTreeRefModel)model;
			TreeRefModel firstRefMode = treemodeol.getFirstLevelRefMode();
			if(null != firstRefMode)
			{
				order =  firstRefMode.getOrderPart();
			}
		}		
		// ����ORDER�Ӿ�
		if (order != null && order.trim().length() != 0) {
			buffer.append("order by ").append(filterColumn(order));
		}
		return buffer.toString();
	}
	/**
	 *���ص�һ������ѯsql 
	 */
	public String getTopClassRefSql(boolean withorder) {
		if(model instanceof TwoTreeRefModel){
			TwoTreeRefModel twoRefModel = (TwoTreeRefModel) model;
			TreeRefModel firstRefMode = twoRefModel.getFirstLevelRefMode();
			String strTableName = firstRefMode.getTablesString();
			if(strTableName == null)
				return "";
			String strPatch = firstRefMode.getStrPatch();
			String[] strFieldCode = firstRefMode.getVisibleFieldCodes();
			String[] hiddenFields = firstRefMode.getHiddenFieldCodes();
			//String strWherePart = model.getWherePart();
			String strWherePart = firstRefMode.getWherePart();
			String group = firstRefMode.getGroupPart();
			String order = firstRefMode.getOrderPart();
			if (strTableName == null || strTableName.trim().length() == 0)
				return null;
			//strTableName = changeBaseTable(strTableName);
			String basSQL = buildBaseSql(strPatch, strFieldCode, hiddenFields, strTableName, strWherePart);
			StringBuffer buffer = new StringBuffer(basSQL);

			if (firstRefMode.getQuerySql() != null) {
				addQueryCondition(firstRefMode, buffer);
			}
			// ��or�ĳ�union
			addSpecialBlurCriteria(buffer, basSQL);
			if (group != null) {
				buffer.append(" group by ").append(filterColumn(group));
			}
			if(withorder)
			{
				// ����ORDER�Ӿ�
				if (order != null && order.trim().length() != 0) {
					buffer.append("order by ").append(filterColumn(order));
				}
			}
			return buffer.toString();
		}
		else{
			TwoTreeGridRefModel twoRefModel = (TwoTreeGridRefModel) model;
			TreeRefModel firstRefMode = twoRefModel.getFirstLevelRefMode();
			String strTableName = firstRefMode.getTablesString();
			if(strTableName == null)
				return "";
			String strPatch = firstRefMode.getStrPatch();
			String[] strFieldCode = firstRefMode.getVisibleFieldCodes();
			String[] hiddenFields = firstRefMode.getHiddenFieldCodes();
//			String strWherePart = twoRefModel.getWherePart();
			String strWherePart = firstRefMode.getWherePart();
			String group = firstRefMode.getGroupPart();
			String order = firstRefMode.getOrderPart();
			if (strTableName == null || strTableName.trim().length() == 0)
				return null;
			//strTableName = changeBaseTable(strTableName);
			String basSQL = buildBaseSql(strPatch, strFieldCode, hiddenFields, strTableName, strWherePart);
			StringBuffer buffer = new StringBuffer(basSQL);

			if (firstRefMode.getQuerySql() != null) {
				addQueryCondition(firstRefMode, buffer);
			}
			// ��or�ĳ�union
			addSpecialBlurCriteria(buffer, basSQL);
			if (group != null) {
				buffer.append(" group by ").append(filterColumn(group));
			}
			if(withorder)
			{
				// ����ORDER�Ӿ�
				if (order != null && order.trim().length() != 0) {
					buffer.append("order by ").append(filterColumn(order));
				}
			}
			return buffer.toString();
		}
	}

	/**
	 *��������������ѯsql 
	 */
	public String getClassRefSql(boolean withOrder) {
		TreeGridRefModel treeModel = (TreeGridRefModel) model;
		String strTableName = treeModel.getClassTableName();
		String[] strFieldCode = null;
		if(treeModel instanceof TwoTreeGridRefModel){
			strFieldCode = new String[]{treeModel.getClassRefPkField(), treeModel.getClassRefCodeField(), treeModel.getClassRefNameField(), ((TwoTreeGridRefModel) treeModel).getDetailParameter()};
		}else {
			strFieldCode =	new String[]{treeModel.getClassRefPkField(), treeModel.getClassRefCodeField(), treeModel.getClassRefNameField(), treeModel.getClassJoinField()};
		}
		String strWherePart = treeModel.getClassWherePart();
		String strOrderField = treeModel.getClassOrderPart();
		if (strTableName == null)
			return null; 
		int iSelectFieldCount = strFieldCode == null ? 0 : strFieldCode.length;
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
//		if (getStrClassSqlPatch() != null) {
//			sb.append(getStrClassSqlPatch()).append(" ");
//		}
		
		boolean first = true; 
		for (int i = 0; i < iSelectFieldCount; i++) {
			if(strFieldCode[i] == null || strFieldCode[i].equals(""))
				continue;
			if(strFieldCode[i].indexOf(".") != -1)
				continue;
			if(!first)
				sb.append(",");
			sb.append(strFieldCode[i]);
			first = false;
		}
		sb.append(" from ").append(strTableName);
		// ����WHERE�Ӿ�
		if (strWherePart != null && strWherePart.trim().length() != 0) {
			sb.append(" where (").append(strWherePart).append(" )");
		} else
			sb.append(" where 11=11 ");
//		String joinfield =  treeModel.getClassJoinField();
//		
//		if(treeModel.getWherePart() != null && !"".equals(treeModel.getWherePart())){
//			sb.append(" and " + treeModel.getWherePart());
//		}
		// ����������Ȩ��
//		if (isClassDataPower()) {
//			String classDataPowerSql = getClassDataPowerSql(true);
//			if (classDataPowerSql != null) {
//				sb.append(" and ").append(classDataPowerSql);
//			}
//		}

//		if(model instanceof NcAdapterTreeRefModel){
//			AbstractRefModel ncModel = ((INcRefModel) model).getNcModel();
//			//���Ϲ����ֶ�
//			String[] filterPks = ncModel.getFilterPks();
//			if(filterPks != null){
//				String pkField = ncModel.getPkFieldCode();
//				sb.append(" and " +  pkField + " in (");
//				for (int i = 0; i < filterPks.length; i++) {
//					if(i != filterPks.length - 1){
//						sb.append("'");
//						sb.append(filterPks[i]);
//						sb.append("'");
//						sb.append(",");
//					}
//					else{
//						sb.append("'");
//						sb.append(filterPks[i]);
//						sb.append("'");
//						sb.append(")");
//					}
//				}
//			}
//		}
		
		if(withOrder){
			// ����ORDER�Ӿ�
			if (strOrderField != null && strOrderField.trim().length() != 0) {
				sb.append(" order by ").append(strOrderField);
			}
		}
		return sb.toString();
	}
	/**
	 * ���������ݲ�ѯsql
	 * @return ���������������ݲ�ѯsql
	 */
	public String getSpecialSql()
	{
		return getSpecialSql(true);
	}
	/**
	 * ���������ݲ�ѯsql
	 * @param withorder �Ƿ�����������
	 * @return
	 */
	public String getSpecialSql(boolean withorder) {
		String strTableName = model.getTablesString();
		if(strTableName == null)
			return "";
		String strPatch = model.getStrPatch();
		String[] strFieldCode = model.getVisibleFieldCodes();
		String[] hiddenFields = model.getHiddenFieldCodes();
		String strWherePart = model.getWherePart();
		String group = model.getGroupPart();
		String order = model.getOrderPart();
		if (strTableName == null || strTableName.trim().length() == 0)
			return null;
		//strTableName = changeBaseTable(strTableName);
		String basSQL = buildBaseSql(strPatch, strFieldCode, hiddenFields, strTableName, strWherePart);
		StringBuffer buffer = new StringBuffer(basSQL);

		if (model.getQuerySql() != null) {
			addQueryCondition(model, buffer);
		}
		// ��or�ĳ�union
		addSpecialBlurCriteria(buffer, basSQL);
		
		if(model instanceof TreeGridRefModel){
			TreeGridRefModel tgModel = (TreeGridRefModel) model;
			// �������---���ǲ�����WherePart
			if (tgModel.getClassJoinValue() != null
					&& !tgModel.getClassJoinValue().equals(IRefConst.QUERY)) {
					buffer.append(" and ( " + tgModel.getDocJoinField() + " = '"
							+ tgModel.getClassJoinValue() + "' ) ");
			}
			// ��ѯʱҪ���Ϸ��������Ȩ��
//			addClassAreaCondition(buffer);
		}
		
//		if(model instanceof NcAdapterTreeRefModel){
//			AbstractRefModel ncModel = ((INcRefModel) model).getNcModel();
//			//���Ϲ����ֶ�
//			String[] filterPks = ncModel.getFilterPks();
//			if(filterPks != null){
//				String pkField = ncModel.getPkFieldCode();
//				buffer.append(" and " +  pkField + " in (");
//				for (int i = 0; i < filterPks.length; i++) {
//					if(i != filterPks.length - 1){
//						buffer.append("'");
//						buffer.append(filterPks[i]);
//						buffer.append("'");
//						buffer.append(",");
//					}
//					else{
//						buffer.append("'");
//						buffer.append(filterPks[i]);
//						buffer.append("'");
//						buffer.append(")");
//					}
//				}
//			}
//		}
		
		if (group != null) {
			buffer.append(" group by ").append(filterColumn(group));
		}
		if(withorder)
		{
//			 ����ORDER�Ӿ�
			if (order != null && order.trim().length() != 0) {
				buffer.append("order by ").append(filterColumn(order));
			}
		}
		return buffer.toString();
	}
	
//	// ������������������������������Ȩ��
//	private void addClassAreaCondition(StringBuffer sqlBuffer) {
//		TreeGridRefModel tgModel = (TreeGridRefModel) model;
//		String sql = getClassSql(new String[] { tgModel.getClassJoinField() },
//				tgModel.getClassTableName(), tgModel.getClassWherePart(), null);
//		if (sql != null) {
//			sqlBuffer.append(" and " + tgModel.getDocJoinField() + " in (" + sql + ")");
//		}
//	}
	
	public String filterColumn(String column) {
		return column.substring(column.indexOf(".") + 1, column.length());
	}
	
	public String changeBaseTable(String table) {

		if (table == null || table.indexOf("join") < 0)
			return table;
		StringTokenizer token = new StringTokenizer(table);
		String firstTable = "";
		String secondTable = "";
		String lastElement = "";
		String joinStr = "";
		String onStr = "";
		boolean isJoin = false;
		boolean isOn = false;
		int index = 0;
		while (token.hasMoreTokens()) {
			String element = token.nextToken();
			if (lastElement.equalsIgnoreCase("join")) {
				secondTable = element;
				isJoin = false;
			}
			if (element.equalsIgnoreCase("on")) {
				isOn = true;
			}
			if (isJoin) {
				joinStr += new StringBuffer().append(" ").append(element)
						.append(" ").toString();
			}
			if (isOn) {
				onStr += new StringBuffer().append(" ").append(element).append(
						" ").toString();
			}
			if (index == 0) {
				firstTable = new StringBuffer().append(element).append(" ")
						.toString();
				isJoin = true;
			}

			lastElement = element;
			index++;
		}

		return secondTable + joinStr + firstTable + onStr;
	}
	
	/**
	 * ƴ�Ӹ��ݵ�һ��������֮��Ĺ�����ѯ�ӵ�Sql
	 * @param withorder
	 * @return
	 */
	public String getFirstLevelSpecialSql(boolean withorder) {
		String strTableName = null;
		if(model instanceof TwoTreeGridRefModel)
			strTableName = ((TwoTreeGridRefModel)model).getFirstTableString();
		if(strTableName == null)
			return "";
		String strPatch = model.getStrPatch();
		String[] strFieldCode = model.getVisibleFieldCodes();
		String[] hiddenFields = model.getHiddenFieldCodes();
		String strWherePart = model.getWherePart();
		String group = model.getGroupPart();
		String order = model.getOrderPart();
		if (strTableName == null || strTableName.trim().length() == 0)
			return null;
		//strTableName = changeBaseTable(strTableName);
		String basSQL = buildBaseSql(strPatch, strFieldCode, hiddenFields, strTableName, strWherePart);
		StringBuffer buffer = new StringBuffer(basSQL);

		if (model.getQuerySql() != null) {
			addQueryCondition(model, buffer);
		}
		// ��or�ĳ�union
		addSpecialBlurCriteria(buffer, basSQL);
		
		if(model instanceof TwoTreeGridRefModel){
			TwoTreeGridRefModel tgModel = (TwoTreeGridRefModel) model;
			// �������---���ǲ�����WherePart
			if (tgModel.getClassJoinValue() != null
					&& !tgModel.getFirstDocJoinField().equals(IRefConst.QUERY)) {
					buffer.append(" and ( " + tgModel.getFirstDocJoinField() + " = '"
							+ tgModel.getClassJoinValue() + "' ) ");
			}
			// ��ѯʱҪ���Ϸ��������Ȩ��
//			addClassAreaCondition(buffer);
		}
		
//		if(model instanceof NcAdapterTreeRefModel){
//			AbstractRefModel ncModel = ((INcRefModel) model).getNcModel();
//			//���Ϲ����ֶ�
//			String[] filterPks = ncModel.getFilterPks();
//			if(filterPks != null){
//				String pkField = ncModel.getPkFieldCode();
//				buffer.append(" and " +  pkField + " in (");
//				for (int i = 0; i < filterPks.length; i++) {
//					if(i != filterPks.length - 1){
//						buffer.append("'");
//						buffer.append(filterPks[i]);
//						buffer.append("'");
//						buffer.append(",");
//					}
//					else{
//						buffer.append("'");
//						buffer.append(filterPks[i]);
//						buffer.append("'");
//						buffer.append(")");
//					}
//				}
//			}
//		}
		
		if (group != null) {
			buffer.append(" group by ").append(filterColumn(group));
		}
		if(withorder)
		{
//			 ����ORDER�Ӿ�
			if (order != null && order.trim().length() != 0) {
				buffer.append("order by ").append(filterColumn(order));
			}
		}
		return buffer.toString();
	}
	
	
	/**
	 * ������� SQL
	 */
	public String buildBaseSql(String patch, String[] columns,
			String[] hiddenColumns, String tableName, String whereCondition) {
		StringBuffer whereClause = new StringBuffer();
		StringBuffer sql = new StringBuffer("select ").append(patch)
				.append(" ");
		int columnCount = columns == null ? 0 : columns.length;
		addQueryColumn(columnCount, sql, columns, hiddenColumns);
		// ����FROM�Ӿ�
		sql.append(" from ").append(tableName);
		// ����WHERE�Ӿ�
		if (whereCondition != null && whereCondition.trim().length() != 0) {
			whereClause.append(" where (").append(whereCondition).append(" )");
		} else
			whereClause.append(" where 11=11 ");

//		appendAddWherePartCondition(whereClause);

		
//		addDataPowerCondition(model.getTablesString(), whereClause);
//		addSealCondition(whereClause);
//		addEnvWherePart(whereClause);
		sql.append(" ").append(whereClause.toString());

		return sql.toString();
	}
	
//	private void addEnvWherePart(StringBuffer whereClause) {
//		// setpk ,������������
//		if (isPKMatch() && !isMatchPkWithWherePart()) {
//			return;
//		}
//		String wherePart = getEnvWherePart();
//		if (wherePart != null) {
//
//			whereClause.append(" and (").append(wherePart).append(") ");
//
//		}
//
//	}
	
//	
//	public void addSealCondition(StringBuffer whereClause) {
//		// ������ݲ���ʾ�����Where����
//		if (!isSealedDataShow()) {
//
//			String wherePart = getSealedWherePart();
//			if (wherePart != null) {
//				whereClause.append(" and (").append(wherePart).append(") ");
//
//			}
//		}
//	}
	
//	protected void addDataPowerCondition(String tableName,
//			StringBuffer whereClause) {
//		if (isUseDataPower()) {
//
//			String powerSql = getDataPowerSubSql(tableName, model.getRefNodeName(), getResourceID());
//			
//			if (powerSql!=null){
//				whereClause.append(" and (").append(powerSql).append(")");
//			}
//
//		}
//	}
	
//	public String getDataPowerSubSql(String strTableName,
//			String strTableShowName,String resourceID) {
//		String tableName = strTableName;
//		if (strTableName != null) {
//			tableName = strTableName.trim();
//		}
//		String powerSql = modelHandler.getDataPowerSubSql(tableName,
//				strTableShowName, this,resourceID);
//		return powerSql;
//	}
	
//	private void appendAddWherePartCondition(StringBuffer whereClause) {
//
//		if (getAddWherePart() == null) {
//			return;
//		}
//
//		if (isPKMatch() && !isMatchPkWithWherePart()) {
//
//			return;
//
//		}
//		whereClause.append(" ").append(getAddWherePart());
//
//	}
	
	/**
	 * ���������
	 * 
	 * @param iSelectFieldCount
	 * @param strSql
	 * @param strFieldCode
	 * @param hiddenFields
	 */
	public void addQueryColumn(int iSelectFieldCount, StringBuffer strSql,
			String[] strFieldCode, String[] hiddenFields) {
		
		//int nameFieldIndex = model.getFieldIndex(model.getRefNameField());

		for (int i = 0; i < iSelectFieldCount; i++) {
			if(strFieldCode[i] == null || strFieldCode[i].equals(""))
				continue;
//			if (isMutilLangNameRef() && i == nameFieldIndex) {
//				strSql.append(getLangNameColume());
//			} 
//			else {
				strSql.append(strFieldCode[i]);
//			}

			if (i < iSelectFieldCount - 1)
				strSql.append(",");
		}
		// ���������ֶ�
		if (hiddenFields != null && hiddenFields.length > 0) {
			for (int k = 0; k < hiddenFields.length; k++) {
				if (hiddenFields[k] != null
						&& hiddenFields[k].trim().length() > 0) {
					strSql.append(",");
					strSql.append(hiddenFields[k]);
				}
			}
		}
	}
	
	protected void addQueryCondition(ILfwRefModel model, StringBuffer sqlBuffer) {
		sqlBuffer.append(" and (").append(model.getRefPkField()).append(" in (")
				.append(model.getQuerySql()).append("))").toString();
	}
	
	private void addSpecialBlurCriteria(StringBuffer buffer, String basSQL) {
		String blurValue = null;
//		if(blurValue == null)
//			return;
		if (blurValue != null/* && isIncludeBlurPart()*/) {
			if (blurValue.indexOf('*') != -1 || blurValue.indexOf('%') != -1 || blurValue.indexOf('?') != -1) {
				String[] blurfields = model.getFilterFields();
				buffer.append(" and (");
				buffer.append("( ").append(blurfields[0]).append(" like '")
						.append(
								blurValue.replace('*', '%').replace('?',
										'_')
										+ "' )");
				for (int j = 1; j < blurfields.length; j++) {
					buffer.append(" or ( ").append(blurfields[j]).append(
							" like '").append(
							blurValue.replace('*', '%').replace('?', '_')
									+ "' )");
				}
				buffer.append(")");
			} 
//			else {
//				if (isMnecodeInput() && getMnecodes() != null) {
//					buffer.append(" and ( ").append(getRefCodeField()).append(
//							" = '").append(blurValue).append("' ");
//					for (int i = 0; i < getMnecodes().length; i++) {
//						buffer.append(" union ");
//						buffer.append(basSQL);
//						buffer.append(" and ").append(getMnecodes()[i]).append(
//								"='").append(blurValue).append("' ");
//					}
//				}
//			}
		}
	}
}
