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
	 * 根据VO类型及VO内容查询对应VO
	 * @param c
	 * @return
	 * @throws LfwBusinessException 
	 */
	public <M extends T>M[] queryVOs(M c, PaginationInfo pg, Map<String, Object> extMap) throws LfwBusinessException;
	
	public <M extends T>M[] queryVOs(String sql, Class<M> clazz, PaginationInfo pg, Map<String, Object> extMap) throws LfwBusinessException;
	
	public <M extends T>M[] queryVOs(String sql, Class<M> clazz, PaginationInfo pg, String orderBy, Map<String, Object> extMap) throws LfwBusinessException;
	
	/**
	 * 根据VO类型及查询条件查询对应VO
	 * @param c
	 * @param pg
	 * @param wherePart
	 * @param extMap
	 * @return
	 * @throws LfwBusinessException
	 */
	public <M extends T>M[] queryVOs(M c, PaginationInfo pg, String wherePart, Map<String, Object> extMap, String orderByPart) throws LfwBusinessException;
	
	/**
	 * 根据VO类型及查询条件查询对应VO
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
	 * 删除
	 * @param obj
	 * @param trueDel true:数据库删除,false:逻辑删除
	 * @throws LfwBusinessException
	 */
	public void deleteVo(K obj, boolean trueDel) throws LfwBusinessException;
	
	
	/**
	 * 删除多个VOs
	 * @throws LfwBusinessException
	 */
	public void deleteVos(SuperVO[] vos) throws LfwBusinessException;

	/**
	 * 根据指定SQL 执行有参数的数据库更新操作
	 * 
	 * @param sql
	 *            更新的sql
	 * @param parameter
	 *            更新参数
	 * @return
	 * @throws DAOException
	 *             更新发生错误抛出DAOException
	 */
	public int executeUpdate(String sql, SQLParameter parameter) throws LfwBusinessException;

	/**
	 * 根据指定SQL 执行无参数的数据库更新操作
	 * 
	 * @param sql
	 *            更新的sql
	 * @return
	 * @throws DAOException
	 *             更新发生错误抛出DAOException
	 */
	public int executeUpdate(String sql) throws LfwBusinessException;
	public void deleteVo(SuperVO vo) throws LfwBusinessException;
}
