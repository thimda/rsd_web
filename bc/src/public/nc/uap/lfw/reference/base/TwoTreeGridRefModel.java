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
	
	//第二级树对应的外键，对应于level.setDetailParameter();
	public abstract String getDetailParameter();
	
	/**
	 * 和一级树关联子的字段
	 * @return
	 */
	public String getFirstDocJoinField(){
		return null;
	}
	
	/**
	 * 返回第一级树table
	 * @return
	 */
	public String getFirstTableString(){
		return null;
	}
	
	/**
	 * 和一级树关联父中的字段
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
