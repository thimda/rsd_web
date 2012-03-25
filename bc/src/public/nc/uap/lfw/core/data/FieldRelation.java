package nc.uap.lfw.core.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * DataSet内部的FieldRelation的操作接口定义
 * 
 */

public class FieldRelation implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String id ; 
	private String refDatasetid;
	private List<MatchField> matchFieldList = new ArrayList<MatchField>();
	private WhereField whereField;
	private FieldRelation parentRelation;
	private List<FieldRelation> childRelationList = null;
	public List<FieldRelation> getChildRelationList() {
		return childRelationList;
	}

	public void setChildRelationList(List<FieldRelation> childRelationList) {
		this.childRelationList = childRelationList;
	}

	/**
	 * 缺省构造
	 *
	 */
	public FieldRelation(){}
	
	/**
	 * 带参构造
	 * @param id
	 */
	public FieldRelation(String id){
		
		this.id = id;
	}
	
	/**
	 * 设置Id
	 * @param id
	 */
	public void setId(String id){
		
		this.id = id;
	}
	
	/**
	 * 获取FieldRelation Id
	 * @return
	 */
	public String getId(){
		
		return this.id;
	}
	
	/**
	 * 获取引用DataSet的名称
	 * @param refDataSet
	 * @return
	 */
	public String getRefDataset(){
		
		return this.refDatasetid;
	}
	
	/**
	 * 设置应用DataSet的名称
	 * @param refDataSet
	 */
	public void setRefDataset(String refDataSet){
		
		this.refDatasetid = refDataSet;
	}
	
	/**
	 * 获取引用DataSet的名称
	 * @param refDataSet
	 * @return
	 */
	public String getRefDatasetid(){
		
		return this.refDatasetid;
	}
	
	/**
	 * 设置应用DataSet的名称
	 * @param refDataSet
	 */
	public void setRefDatasetid(String refDataSet){
		
		this.refDatasetid = refDataSet;
	}
	
	/**
	 * 获取FieldRelation的 matchFields属性
	 * @return
	 */
	public MatchField[] getMatchFields(){
		
		return (MatchField[])matchFieldList.toArray(new MatchField[matchFieldList.size()]);
	}
	
	/**
	 * 设置FieldRelation的 matchFields属性
	 * @param matchFields
	 */
	public void setMatchFields(MatchField[] matchFields){
		
		if(matchFields == null)
			return ;
		matchFieldList.clear();
		for(int i = 0;i < matchFields.length; i++)
			matchFieldList.add(matchFields[i]);
	}
	
	/**
	 * 添加匹配条件
	 * @param field
	 */
	public void addMatchField(MatchField field){
		
		if(field == null)
			return ;
		matchFieldList.add(field);
	}


	public FieldRelation getParentRelation() {
		return parentRelation;
	}

	public void setParentRelation(FieldRelation parentRelation) {
		this.parentRelation = parentRelation;
	}
	
	public void addChildRelation(FieldRelation rel) {
		if(this.childRelationList == null)
			this.childRelationList = new ArrayList<FieldRelation>();
		this.childRelationList.add(rel);
	}

	public WhereField getWhereField() {
		return whereField;
	}

	public void setWhereField(WhereField whereField) {
		this.whereField = whereField;
	}
}
