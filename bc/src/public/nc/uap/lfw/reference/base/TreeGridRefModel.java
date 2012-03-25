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
	 * 获取分类树的编码字段
	 * @return
	 */
	public abstract String getClassRefCodeField();
	
	public String[] getClassFieldCodes(){
		return new String[]{getClassRefPkField(), getClassRefNameField(), getClassRefCodeField()};
	}
	/**
	 * 获取分类树的名称字段
	 * @return
	 */
	public abstract String getClassRefNameField();
	
	//树对应的refpk字段
	public abstract String getClassRefPkField();

	//树和表关联的对应的树中的关联字段
	public abstract String getClassJoinField();
	
	//树和表关联的对应的表中的关联字段
	public abstract String getDocJoinField();

	public abstract void setClassJoinValue(String keyValue) ;
	
	//处理关联--树表直接的关联值
	public abstract String getClassJoinValue();
	
	/**
	 * 获取编码树的编码规则
	 * @return
	 */
	public String getClassCodingRule() {
		return null;
	}
	
	//如果是递归树，树的递归父字段
	public abstract String getClassFatherField();
	
	//树的主键
	public abstract String getClassChildField();
	
	/**
	 * 获取树根结点名称
	 * @return
	 */
	public abstract String getClassRootName();
	
	//树对应的表
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
			throw new LfwRuntimeException("查询数据出现问题," + e.getMessage());
		}
	}
}
