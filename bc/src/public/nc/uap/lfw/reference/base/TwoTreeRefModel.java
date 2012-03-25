package nc.uap.lfw.reference.base;

import java.util.List;

public abstract class TwoTreeRefModel extends TreeRefModel{

	public Integer getRefType()
	{
		return REFTYPE_TWOTREE;
	}
	
	public List<List<Object>> getTopClassData() {
		return getClassData(getTopClassRefSql());
	}
	
	public abstract TreeRefModel getFirstLevelRefMode();
	
	public String getTopClassRefSql() {
		return getTopClassRefSql(true);
	}
	public String getTopClassRefSql(boolean order) {
		return getRefSqlUtil().getTopClassRefSql(order);
	}
	public String getClassOrderSql(){
		return getRefSqlUtil().getCLassOrderSql();
	}
	//得到树树间关联的父关联字段
	public abstract String getClassJoinField();
	
	////得到树树间关联的子关联字段
	public abstract String getDocJoinField();
}
