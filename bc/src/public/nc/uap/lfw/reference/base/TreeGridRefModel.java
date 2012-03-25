package nc.uap.lfw.reference.base;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.exception.LfwRuntimeException;


public abstract class TreeGridRefModel extends GridRefModel {
	private String classWherePart;
	public Integer getRefType(){
		return REFTYPE_TREEGRID;
	}
	
	/**
	 * ��ȡ�������ı����ֶ�
	 * @return
	 */
	public abstract String getClassRefCodeField();
	
	public String[] getClassFieldCodes(){
		return new String[]{getClassRefPkField(), getClassRefNameField(), getClassRefCodeField()};
	}
	/**
	 * ��ȡ�������������ֶ�
	 * @return
	 */
	public abstract String getClassRefNameField();
	
	//����Ӧ��refpk�ֶ�
	public abstract String getClassRefPkField();

	//���ͱ�����Ķ�Ӧ�����еĹ����ֶ�
	public abstract String getClassJoinField();
	
	//���ͱ�����Ķ�Ӧ�ı��еĹ����ֶ�
	public abstract String getDocJoinField();

	public abstract void setClassJoinValue(String keyValue) ;
	
	//�������--����ֱ�ӵĹ���ֵ
	public abstract String getClassJoinValue();
	
	/**
	 * ��ȡ�������ı������
	 * @return
	 */
	public String getClassCodingRule() {
		return null;
	}
	
	//����ǵݹ��������ĵݹ鸸�ֶ�
	public abstract String getClassFatherField();
	
	//��������
	public abstract String getClassChildField();
	
	/**
	 * ��ȡ�����������
	 * @return
	 */
	public abstract String getClassRootName();
	
	//����Ӧ�ı�
	public abstract String getClassTableName();
	
	@Override
	public int getPageSize() {
		return 20;
	}
	
	public List<List<Object>> getClassData() {
		return getClassData(getClassRefSql());
	}
	
	public String getClassWherePart(){
		return classWherePart;
	}
	
	public void setClassWherePart(String wherePart){
		classWherePart = wherePart;
	}
	
	public String getClassOrderPart(){
		return "";
	}
	
	public String getClassRefSql() {
		return getClassRefSql(true);
	}
	
	public String getClassRefSql(boolean withOrder) {
		return getRefSqlUtil().getClassRefSql(withOrder);
	}
	public String getClassOrderSql()
	{
		return getRefSqlUtil().getCLassOrderSql();
	}
	
	protected List<List<Object>> getClassData(String sql) {
		BaseDAO dao = new BaseDAO();
		try {
			RefResult result = (RefResult) dao.executeQuery(sql, new RefDataProcessor(0, 1000000));
			return result.data;
		} 
		catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwRuntimeException("��ѯ���ݳ�������," + e.getMessage());
		}
	}
}
