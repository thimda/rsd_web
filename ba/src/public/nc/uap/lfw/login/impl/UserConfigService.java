package nc.uap.lfw.login.impl;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.login.itf.IUserConfigService;
import nc.uap.lfw.login.vo.UserConfigVO;

/**
 * �û��������÷���ʵ����
 * 
 * @author gd
 * @modify zhangxya
 * 
 */
public class UserConfigService implements IUserConfigService {

	/**
	 * ����WebMappingMeta�������һ��vo����
	 */
	public void addUserConfig(UserConfigVO userConfig)
			throws LfwBusinessException {
		BaseDAO dao = new BaseDAO();
		try {
			dao.insertVO(userConfig);
		} catch (DAOException e) {
			throw new LfwBusinessException(e);
		}
	}

	/**
	 * ����WebMappingMeta����ɾ��һ��VO���� (non-Javadoc)
	 */
	public void deleteUserConfig(String pkuserconfig)
			throws LfwBusinessException {
		BaseDAO dao = new BaseDAO();
		try {
			dao.deleteByPK(UserConfigVO.class, pkuserconfig);
			} catch (DAOException e) {
			throw new LfwBusinessException(e);
		}
	}

	/**
	 * ����WebMappingMeta����һ��VO����
	 */
	public void updateUserConfig(UserConfigVO userConfig)
			throws LfwBusinessException {
		BaseDAO baseDAO = new BaseDAO();
		try {
			baseDAO.updateVO(userConfig);
		} catch (DAOException e) {
			throw new LfwBusinessException(e);
		}
	}

}
