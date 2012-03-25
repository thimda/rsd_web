package nc.uap.lfw.reference.base;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.exception.LfwRuntimeException;

public abstract class TreeRefModel extends BaseRefModel {
	
	private int pageSize = -1;
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * ��ȡ�������ı������
	 * @return
	 */
	public String getCodingRule() {
		return null;
	}
	
	public Integer getRefType()
	{
		return REFTYPE_TREE;
	}
	
	public abstract String getFatherField();
	
	public abstract String getChildField();
	
	/**
	 * ��ȡ�����������
	 * @return
	 */
	public abstract String getRootName();
	
	public int getPageSize() {
		if(pageSize == -1)
			return 20;
		else
			return pageSize;
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
