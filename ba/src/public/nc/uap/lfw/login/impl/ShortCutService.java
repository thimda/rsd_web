package nc.uap.lfw.login.impl;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.login.itf.IShortCutService;
import nc.uap.lfw.login.vo.LfwShortCutVO;

/**
 * 快捷方式更新服务实现类
 * 
 * @author zhangxya
 * 
 */
public class ShortCutService implements IShortCutService {
	/**
	 * 更新快捷方式vo
	 */
	public void updateShortCut(LfwShortCutVO shortCutVo)
			throws LfwBusinessException {
		BaseDAO baseDAO = new BaseDAO();
		try {
			baseDAO.updateVO(shortCutVo);
		} catch (DAOException e) {
			throw new LfwBusinessException(e);
		}
	}

	/**
	 * 插入快捷方式vo
	 */
	public void insertShortCut(LfwShortCutVO shortCutVo)
			throws LfwBusinessException {
		BaseDAO baseDAO = new BaseDAO();
		try {
			baseDAO.insertVO(shortCutVo);
		} catch (DAOException e) {
			throw new LfwBusinessException(e);
		}
	}
	
	/**
	 * 删除快捷方式vo
	 * @param shortCutVo
	 * @throws LfwBusinessException
	 */
	public void deleteShortCut(LfwShortCutVO shortCutVo)
			throws LfwBusinessException {
		BaseDAO baseDAO = new BaseDAO();
		try {
			baseDAO.deleteVO(shortCutVo);
		} catch (DAOException e) {
			throw new LfwBusinessException(e);
		}
	}

}
