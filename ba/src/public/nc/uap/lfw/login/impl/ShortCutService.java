package nc.uap.lfw.login.impl;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.login.itf.IShortCutService;
import nc.uap.lfw.login.vo.LfwShortCutVO;

/**
 * ��ݷ�ʽ���·���ʵ����
 * 
 * @author zhangxya
 * 
 */
public class ShortCutService implements IShortCutService {
	/**
	 * ���¿�ݷ�ʽvo
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
	 * �����ݷ�ʽvo
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
	 * ɾ����ݷ�ʽvo
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
