package nc.uap.lfw.crud.itf;

import java.util.Map;

import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.vo.pub.SuperVO;

/**
 * Lfw��ѯĬ��ʵ�֣�ֻ֧��VO
 * @author dengjt
 *
 */
public interface ILfwQueryService {
	/**
	 * ����VO�е�ֵ��������ѯ��ҳVO
	 * @param <T>
	 * @param vo
	 * @param pg
	 * @param extMap
	 * @return
	 * @throws LfwBusinessException
	 */
	public <T extends SuperVO>T[] queryVOs(T vo, PaginationInfo pg, Map<String, Object> extMap) throws LfwBusinessException;
	/**
	 * ����sql����ѯ
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
	 * ����sql����ѯ
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
	 * ����sql��ѯVO
	 * @param sql
	 * @param processor
	 * @return
	 * @throws LfwBusinessException
	 */
	public Object queryVOs(String sql, ResultSetProcessor processor) throws LfwBusinessException;
	/**
	 * ��ѯVO
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
