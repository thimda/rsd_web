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
	//�õ�����������ĸ������ֶ�
	public abstract String getClassJoinField();
	
	////�õ�������������ӹ����ֶ�
	public abstract String getDocJoinField();
}
