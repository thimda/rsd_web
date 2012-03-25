package nc.uap.lfw.crud.itf;

import java.util.Map;

import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.vo.pub.SuperVO;

/**
 * Lfw查询默认实现，只支持VO
 * @author dengjt
 *
 */
public interface ILfwQueryService {
	/**
	 * 根据VO中的值条件，查询分页VO
	 * @param <T>
	 * @param vo
	 * @param pg
	 * @param extMap
	 * @return
	 * @throws LfwBusinessException
	 */
	public <T extends SuperVO>T[] queryVOs(T vo, PaginationInfo pg, Map<String, Object> extMap) throws LfwBusinessException;
	/**
	 * 根据sql，查询
	 * @param <T>
	 * @param sql
	 * @param clazz
	 * @param pg
	 * @param extMap
	 * @return
	 * @throws LfwBusinessException
	 */
	public <T extends SuperVO>T[] queryVOs(String sql, Class<T> clazz, PaginationInfo pg, Map<String, Object> extMap) throws LfwBusinessException;
	
	/**
	 * 根据sql，查询
	 * @param <T>
	 * @param sql
	 * @param clazz
	 * @param pg
	 * @param extMap
	 * @return
	 * @throws LfwBusinessException
	 */
	public <T extends SuperVO>T[] queryVOs(String sql, Class<T> clazz, PaginationInfo pg, String orderBy, Map<String, Object> extMap) throws LfwBusinessException;
	
	/**
	 * 根据sql查询VO
	 * @param sql
	 * @param processor
	 * @return
	 * @throws LfwBusinessException
	 */
	public Object queryVOs(String sql, ResultSetProcessor processor) throws LfwBusinessException;
	/**
	 * 查询VO
	 * @param <T>
	 * @param vo
	 * @param pg
	 * @param wherePart
	 * @param extMap
	 * @param orderByPart
	 * @return
	 * @throws LfwBusinessException
	 */
	public <T extends SuperVO>T[] queryVOs(T vo, PaginationInfo pg, String wherePart, Map<String, Object> extMap, String orderByPart) throws LfwBusinessException;
}
