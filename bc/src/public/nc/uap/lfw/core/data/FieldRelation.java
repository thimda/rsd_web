package nc.uap.lfw.core.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * DataSet�ڲ���FieldRelation�Ĳ����ӿڶ���
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
	 * ȱʡ����
	 *
	 */
	public FieldRelation(){}
	
	/**
	 * ���ι���
	 * @param id
	 */
	public FieldRelation(String id){
		
		this.id = id;
	}
	
	/**
	 * ����Id
	 * @param id
	 */
	public void setId(String id){
		
		this.id = id;
	}
	
	/**
	 * ��ȡFieldRelation Id
	 * @return
	 */
	public String getId(){
		
		return this.id;
	}
	
	/**
	 * ��ȡ����DataSet������
	 * @param refDataSet
	 * @return
	 */
	public String getRefDataset(){
		
		return this.refDatasetid;
	}
	
	/**
	 * ����Ӧ��DataSet������
	 * @param refDataSet
	 */
	public void setRefDataset(String refDataSet){
		
		this.refDatasetid = refDataSet;
	}
	
	/**
	 * ��ȡ����DataSet������
	 * @param refDataSet
	 * @return
	 */
	public String getRefDatasetid(){
		
		return this.refDatasetid;
	}
	
	/**
	 * ����Ӧ��DataSet������
	 * @param refDataSet
	 */
	public void setRefDatasetid(String refDataSet){
		
		this.refDatasetid = refDataSet;
	}
	
	/**
	 * ��ȡFieldRelation�� matchFields����
	 * @return
	 */
	public MatchField[] getMatchFields(){
		
		return (MatchField[])matchFieldList.toArray(new MatchField[matchFieldList.size()]);
	}
	
	/**
	 * ����FieldRelation�� matchFields����
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
	 * ���ƥ������
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
