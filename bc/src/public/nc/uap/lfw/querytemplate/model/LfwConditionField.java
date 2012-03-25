package nc.uap.lfw.querytemplate.model;

import java.io.Serializable;

import nc.uap.lfw.core.common.DatasetConstant;
import nc.uap.lfw.core.data.Row;
import nc.ui.querytemplate.operator.IOperator;
import nc.vo.pub.guid.UUID;
import nc.vo.pub.guid.UUIDGenerator;

public class LfwConditionField implements Serializable{
	private static final long serialVersionUID = -1336106654692711442L;
	private String fieldId;
	private String label;
	private String dataType;
	private String field;
	private String editorType;
	private String editorInfo;
	private String defaultValue;
	private String pfieldId;
	private String editorInfo2;
	private String parentField;
	private String tableName;
	private String operatorValue;
	private String condValue;
	private String extendField;
	private IOperator[] operators;
	//字段类型(必须0，固定1，默认2，可选3)
	private int condType = 3;
	
	private int logicType = 0;
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getEditorInfo() {
		return editorInfo;
	}
	public void setEditorInfo(String editorInfo) {
		this.editorInfo = editorInfo;
	}
	public String getEditorInfo2() {
		return editorInfo2;
	}
	public void setEditorInfo2(String editorInfo2) {
		this.editorInfo2 = editorInfo2;
	}
	public String getEditorType() {
		return editorType;
	}
	public void setEditorType(String editorType) {
		this.editorType = editorType;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getParentField() {
		return parentField;
	}
	public void setParentField(String parentField) {
		this.parentField = parentField;
	}
	public String getPfieldId() {
		return pfieldId;
	}
	public void setPfieldId(String pfieldId) {
		this.pfieldId = pfieldId;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public Row translateToQueryRow() {
		UUID id = UUIDGenerator.getInstance().generateRandomBasedUUID();
		return translateToQueryRow(id.toString());
	}
	
	public Row translateToQueryRow(String key) {
//		<Field id="query_fieldId" field="fieldId" dataType="String" i18nName="字段Id" nullAble="false"/>
//		<Field id="query_label" field="label" dataType="String" i18nName="显示标签" nullAble="false"/>
//		<Field id="query_dataType" field="dataType" dataType="String" i18nName="数据类型" nullAble="false"/>
//		<Field id="query_field" field="field" dataType="String" i18nName="对应VO字段" nullAble="false"/>
//		<Field id="query_editorType" field="editorType" dataType="String" i18nName="编辑器类型" nullAble="false"/>
//		<Field id="query_editorInfo" field="editorInfo" dataType="String" i18nName="编辑器的说明信息" nullAble="false"/>
//		<Field id="query_defaultValue" field="defaultValue" dataType="String" i18nName="缺省值信息" nullAble="false"/>
//		<Field id="query_pfieldId" field="pfieldId" dataType="String" i18nName="父id"  nullAble="false"/>
//		<Field id="query_editorInfo2" field="editorInfo2" dataType="String" i18nName="编辑器额外信息" nullAble="false"/>
//		<Field id="parent_field" field="parentField" dataType="String" i18nName="parentField" nullAble="false"/>
//		<Field id="table_name" field="tableName" dataType="String" i18nName="tableName" nullAble="false"/>
		
		Row row = new Row(key, 11);
		row.setString(0, this.fieldId);
		row.setString(1, this.label);
		row.setString(2, this.dataType);
		row.setString(3, this.field);
		row.setString(4, this.editorType);
		row.setString(5, this.editorInfo);
		row.setString(6, this.defaultValue);
		row.setString(7, this.pfieldId);
		row.setString(8, this.editorInfo2);
		row.setString(9, this.parentField);
		row.setString(10, this.tableName);
		return row;
	}
	
	public Row translateToConditionRow() {
		UUID id = UUIDGenerator.getInstance().generateRandomBasedUUID();
		return translateToConditionRow(id.toString());
	}
	
	public Row translateToConditionRow(String key) {
//		<Field id="query_condition_id" field="id" dataType="String" label="标识" nullAble="false"/>
//		<Field id="query_condition_label" field="label" dataType="String" label="条件字段" nullAble="false"/>
//		<Field id="query_condition_condition" field="condition" dataType="String" label="条件类型" nullAble="false"/>
//		<Field id="query_condition_value" field="value" dataType="String" label="条件对应值" nullAble="false"/>
//		<Field id="query_condition_parentId" field="parentId" dataType="String" label="父节点标识" nullAble="false"/>
//		<Field id="query_condition_editorType" field="editorType" dataType="String" label="该条件对应的编辑器类型"/>
//		<Field id="query_condition_dataType" field="dataType" dataType="String" label="该条件对应的值的数据类型"/>
//		<Field id="query_condition_editorInfo" field="editorInfo" dataType="String" label="编辑器器类型的附加信息"/>
//		<Field id="query_condition_field" field="field"  datatType="String" label="对应的field字段"/>
//		<Field id="query_condition_type" field="fieldType"  datatType="String" label="字段类型(必须0，固定1，默认2，可选3)"/>
//		<Field id="query_condition_editorInfo2" field="editorInfo2"  datatType="String" label="编辑器类型的附加信息2"/>
//		<Field id="parent_field" field="parentField"  datatType="String" label="parentField"/>
//		<Field id="tableName" field="tableName"  datatType="String" label="tableName"/>
		
		Row row = new Row(key, 14);
		row.setString(0, this.fieldId);
		row.setString(1, this.label);
		row.setString(2, this.operatorValue);
		row.setString(3, this.condValue);
		row.setString(4, DatasetConstant.QUERY_TEMPLATE_DEFAULT_ROOTPARENTID);
		row.setString(5, this.editorType);
		row.setString(6, "String");
		row.setString(7, this.editorInfo);
		row.setString(8, this.field);
		row.setString(9, "" + this.condType);
		row.setString(10, this.editorInfo2);
//		row.setString(11, this.fieldId);
//		row.setString(12, this.fieldId);
		return row;
		
//		String id = cond.getFieldCode().replaceAll("\\.", "_");
//		row.setString(0, id);
//		row.setString(1, cond.getFieldName());
//		//cond.getOperaVO()
//		String operatorValue = cond.getOperaVO()[0].getOperaCode();
//		if(operatorValue.equals("<"))
//			operatorValue = "&lt;";
//		else if(operatorValue.equals("<="))
//			operatorValue = "&lt;=";
//		row.setString(2, operatorValue);
//		row.setString(3, cond.getValue());
//		row.setString(4, DatasetConstant.QUERY_TEMPLATE_DEFAULT_ROOTPARENTID);
//		
//		String editorType;
//		StringBuffer extInfo = new StringBuffer();
//		switch(cond.getDataType()){
//			case IQueryConstants.STRING:
//				editorType = EditorTypeConst.STRINGTEXT;
//				break;
//			case IQueryConstants.INTEGER:
//				editorType = EditorTypeConst.INTEGERTEXT;
//				break;
//			case IQueryConstants.DECIMAL:
//				editorType = EditorTypeConst.DECIMALTEXT;
//				break;
//			case IQueryConstants.DATE:
//				editorType = EditorTypeConst.DATETEXT;
//				break;
//			case IQueryConstants.BOOLEAN:
//				editorType = EditorTypeConst.CHECKBOX;
//				break;
//			case IQueryConstants.UFREF:
//				editorType = EditorTypeConst.REFERENCE;
//				String refCode = cond.getComboType();
//				Integer refType = LfwRefUtil.getRefType(refCode);
//				String refPath = "reference/";
//				switch(refType){
//					case IBusiType.GRID:
//						refPath += "refgrid.jsp";
//						break;
//					case IBusiType.GRIDTREE:
//						refPath += "refgridtree.jsp";
//						break;
//					case IBusiType.TREE:
//					default:
//						refPath += "reftree.jsp";
//				}
//				//TODO 需要重写
//				String dsId = "queryConditionDs" + id;
////				RefGenUtil util = new RefGenUtil(LfwRefUtil.getRefModel(refCode));
////				Dataset[] dss = util.getDataset(dsId);
//				AbstractRefModel model = LfwRefUtil.getRefModel(refCode);
//
//				extInfo.append("{id:'ref")
//					   .append(id)
//					   .append("',pageMeta:'")
//					   .append(dsId + "..." + refType + "...pageMeta")
//					   .append("',path:'")
//					   .append(refPath)
//					   .append("', readDs:'")
//					   .append(dsId)
//					   .append("', writeDs:'queryConditionDataset', readFields:['")
//					   .append(model.getRefCodeField().replaceAll("\\.", "_"))
//					   .append("'], writeFields:['")
//					   .append(id)
//					   .append("'], userObj:'")
//					   .append(refCode)
//					   .append("'}");	
//				break;
//				
//			case IQueryConstants.USERCOMBO:
//				editorType = EditorTypeConst.COMBODATA;
//				break;
//			default:
//				editorType = EditorTypeConst.STRINGTEXT;
//		}
//			
//			row.setString(5, editorType);
//			row.setString(6, "String");
//			row.setString(7, extInfo.toString());
//			row.setString(8, cond.getFieldCode());
//			String fieldType = "3";
//			if (cond.getIfMust().booleanValue())
//				fieldType = "0";
//			else if (cond.getIfImmobility().booleanValue())
//				fieldType = "1";
//			else if (cond.getIfDefault().booleanValue())
//				fieldType = "2";
////			else if (cond.getif)
////				fieldType = "3";
//			row.setString(9, fieldType);
	}
	
	public int getCondType() {
		return condType;
	}
	public void setCondType(int condType) {
		this.condType = condType;
	}
	public String getOperatorValue() {
		return operatorValue;
	}
	public void setOperatorValue(String condition) {
		this.operatorValue = condition;
	}
	public IOperator[] getOperators() {
		return operators;
	}
	
	public void setOperators(IOperator[] operators){
		this.operators = operators;
	}
	public String getExtendField() {
		return extendField;
	}
	public void setExtendField(String extendField) {
		this.extendField = extendField;
	}
	public int getLogicType() {
		return logicType;
	}
	public void setLogicType(int logicType) {
		this.logicType = logicType;
	}
}
