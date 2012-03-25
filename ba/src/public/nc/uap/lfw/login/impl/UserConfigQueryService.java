package nc.uap.lfw.login.impl;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.login.itf.IUserConfigQueryService;
import nc.uap.lfw.login.vo.UserConfigVO;

/**
 * 用户个性配置查询服务类
 * 
 * @author gd
 * @modify zhangxya
 * 
 */
public class UserConfigQueryService implements IUserConfigQueryService {

	/**
	 * 查询用户配置vo
	 */
	public UserConfigVO getUserConfigVO(String userPk, int sysid)
			throws LfwBusinessException {
		if (userPk == null)
			return null;
		BaseDAO baseDAO = new BaseDAO();
		try {
			String sql = "select * from uw_userconfig where userid = ? and sysid =?";
			SQLParameter param = new SQLParameter();
			param.addParam(userPk);
			param.addParam(sysid);
			return (UserConfigVO) baseDAO.executeQuery(sql, param,
					new BeanProcessor(UserConfigVO.class));
		} catch (DAOException e) {
			throw new LfwBusinessException(e);
		}
	}

}
