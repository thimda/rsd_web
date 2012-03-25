package nc.uap.lfw.core.crud;

import java.util.Map;

import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.vo.pub.SuperVO;

public interface ILfwCRUDService<T, K> {
	public K saveBusinessVO(K obj) throws LfwBusinessException;
	public K[] saveBusinessVOs(K[] objs) throws LfwBusinessException;
	
	/**
	 * ����VO���ͼ�VO���ݲ�ѯ��ӦVO
	 * @param c
	 * @return
	 * @throws LfwBusinessException 
	 */
	public <M extends T>M[] queryVOs(M c, PaginationInfo pg, Map<String, Object> extMap) throws LfwBusinessException;
	
	public <M extends T>M[] queryVOs(String sql, Class<M> clazz, PaginationInfo pg, Map<String, Object> extMap) throws LfwBusinessException;
	
	public <M extends T>M[] queryVOs(String sql, Class<M> clazz, PaginationInfo pg, String orderBy, Map<String, Object> extMap) throws LfwBusinessException;
	
	/**
	 * ����VO���ͼ���ѯ������ѯ��ӦVO
	 * @param c
	 * @param pg
	 * @param wherePart
	 * @param extMap
	 * @return
	 * @throws LfwBusinessException
	 */
	public <M extends T>M[] queryVOs(M c, PaginationInfo pg, String wherePart, Map<String, Object> extMap, String orderByPart) throws LfwBusinessException;
	
	/**
	 * ����VO���ͼ���ѯ������ѯ��ӦVO
	 * @param c
	 * @param pg
	 * @param wherePart
	 * @param extMap
	 * @return
	 * @throws LfwBusinessException
	 */
	public <M extends T>M[] queryVOs(M c, PaginationInfo pg, String wherePart, Map<String, Object> extMap) throws LfwBusinessException;


	public Object query(String sql, ResultSetProcessor processor) throws LfwBusinessException;
	/**
	 * ɾ��
	 * @param obj
	 * @param trueDel true:���ݿ�ɾ��,false:�߼�ɾ��
	 * @throws LfwBusinessException
	 */
	public void deleteVo(K obj, boolean trueDel) throws LfwBusinessException;
	
	
	/**
	 * ɾ�����VOs
	 * @throws LfwBusinessException
	 */
	public void deleteVos(SuperVO[] vos) throws LfwBusinessException;

	/**
	 * ����ָ��SQL ִ���в��������ݿ���²���
	 * 
	 * @param sql
	 *            ���µ�sql
	 * @param parameter
	 *            ���²���
	 * @return
	 * @throws DAOException
	 *             ���·��������׳�DAOException
	 */
	public int executeUpdate(String sql, SQLParameter parameter) throws LfwBusinessException;

	/**
	 * ����ָ��SQL ִ���޲��������ݿ���²���
	 * 
	 * @param sql
	 *            ���µ�sql
	 * @return
	 * @throws DAOException
	 *             ���·��������׳�DAOException
	 */
	public int executeUpdate(String sql) throws LfwBusinessException;
	public void deleteVo(SuperVO vo) throws LfwBusinessException;
}
