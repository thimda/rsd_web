/**
 * 
 */
package nc.uap.lfw.pa.impl;

import java.util.Collection;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.pa.PaBusinessException;
import nc.uap.lfw.pa.PaConstantMap;
import nc.uap.lfw.pa.itf.IPaPersistService;
import nc.vo.pub.SuperVO;

/**
 * @author wupeng1
 * @version 6.0 2011-8-16
 * @since 1.6
 */
public class PaPersistServiceImpl implements IPaPersistService {


	@Override
	public SuperVO getCompVOByClause(Class<?> clazz, String clause) throws PaBusinessException {
		Collection<SuperVO> svo = null;
		BaseDAO dao = new BaseDAO();
		try {
			svo = dao.retrieveByClause(clazz, clause);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e);
		}
		if(svo == null || svo.size() == 0){
			return null;
		}
		else
			return svo.iterator().next();
	}
	
	@Override
	public void persitCompVO(SuperVO vo) throws PaBusinessException{
		BaseDAO dao = new BaseDAO();
		try {
			dao.insertVO(vo);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e);
		}
	}

	@Override
	public SuperVO getCompVOByTypeAndID(String type, String id, String widgetid, String pk_template)
			throws PaBusinessException {
		Class<?> clazz = PaConstantMap.mappingTable.get(type);
		SuperVO vo = null;
		
		try {
			vo = (SuperVO) clazz.newInstance();
		} catch (InstantiationException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e);
		} catch (IllegalAccessException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e);
		}
		String uniqueId = id + widgetid;
		String condition = "pk_parent = '" + pk_template + "' and id = '" + uniqueId +"'";
		
		return this.getCompVOByClause(vo.getClass(), condition);
	}

	@Override
	public Collection<SuperVO> getCompVOsByClause(Class<?> clazz, String clause)
			throws PaBusinessException {
		Collection<SuperVO> covos = null;
		BaseDAO dao = new BaseDAO();
		try {
			covos = dao.retrieveByClause(clazz, clause);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e);
		}
		return covos;
	}

	/* (non-Javadoc)
	 * @see nc.uap.lfw.pa.itf.IPaPersistService#updateCompVO(nc.vo.pub.SuperVO)
	 */
	@Override
	public void updateCompVO(SuperVO vo) throws PaBusinessException {
		BaseDAO dao = new BaseDAO();
		try {
			dao.updateVO(vo);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e);
		}
		
	}

	/* (non-Javadoc)
	 * @see nc.uap.lfw.pa.itf.IPaPersistService#deleteVOByClause(java.lang.Class, java.lang.String)
	 */
	@Override
	public void deleteVOByClause(Class<?> clazz, String clause)
			throws PaBusinessException {
		BaseDAO dao = new BaseDAO();
		try {
			dao.deleteByClause(clazz, clause);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e);
		}
	}
}
