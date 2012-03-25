package nc.uap.lfw.reference.base;

import java.util.List;

public abstract class TwoTreeGridRefModel extends TreeGridRefModel{
	public Integer getRefType(){
		return REFTYPE_TWOTREEGRID;
	}
	
	public List<List<Object>> getTopClassData() {
		return getClassData(getTopClassRefSql());
	}
	
	public String getTopClassRefSql() {
		return getRefSqlUtil().getTopClassRefSql(true);
	}
	
	//�ڶ�������Ӧ���������Ӧ��level.setDetailParameter();
	public abstract String getDetailParameter();
	
	/**
	 * ��һ���������ӵ��ֶ�
	 * @return
	 */
	public String getFirstDocJoinField(){
		return null;
	}
	
	/**
	 * ���ص�һ����table
	 * @return
	 */
	public String getFirstTableString(){
		return null;
	}
	
	/**
	 * ��һ�����������е��ֶ�
	 * @return
	 */
	public String getFirstClassJoinField(){
		return null;
	}
	
	public abstract TreeRefModel getFirstLevelRefMode();
	
	public boolean isFirstLevelRelation(){
		return false;
	}

}
