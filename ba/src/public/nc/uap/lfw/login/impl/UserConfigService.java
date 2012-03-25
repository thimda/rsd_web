package nc.uap.lfw.login.impl;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.login.itf.IUserConfigService;
import nc.uap.lfw.login.vo.UserConfigVO;

/**
 * 用户个性配置服务实现类
 * 
 * @author gd
 * @modify zhangxya
 * 
 */
public class UserConfigService implements IUserConfigService {

	/**
	 * 根据WebMappingMeta对象插入一个vo对象
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
	 * 根据WebMappingMeta对象删除一个VO对象 (non-Javadoc)
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
	 * 根据WebMappingMeta更新一个VO对象
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
