package nc.uap.lfw.login.impl;

import java.util.HashMap;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.itf.ILfwSsoQryService;
import nc.uap.lfw.login.vo.LfwSsoRegVO;
import nc.uap.lfw.login.vo.LfwTokenVO;

public class LfwSsoQryServiceImpl implements ILfwSsoQryService {

	public LfwTokenVO getTokenByID(String tokenID) throws LfwBusinessException {
		try {
			String sql = "select * from UW_LFWTOKEN where tokenid =? ";
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(tokenID);
			List<LfwTokenVO> list = (List<LfwTokenVO>) new BaseDAO().executeQuery(sql, parameter, new BeanListProcessor(LfwTokenVO.class));
			if (list != null && !list.isEmpty())
				return list.get(0);

		} catch (DAOException e) {
			LfwLogger.error("===LfwSsoQryServiceImpl===≤È—Ø ß∞‹   ", e);
		}
		return new LfwTokenVO();
	}

	public Integer getOnlineUserCount(String context)
			throws LfwBusinessException {
		try {
			String sql = "select  count(distinct userpk) from uw_lfwtoken where ext1 = ?";
			SQLParameter param = new SQLParameter();
			param.addParam(context);
			return (Integer) new BaseDAO().executeQuery(sql,param,new ColumnProcessor(1));
		} catch (DAOException e) {
			LfwLogger.error("===LfwSsoQryServiceImpl===≤È—Ø ß∞‹  ", e);
		}
		return null;
	}

	public HashMap getUserBySsoKey(String key) throws LfwBusinessException {
		try {
			SQLParameter param = new SQLParameter();
			param.addParam(key);
			List<LfwSsoRegVO> list = (List<LfwSsoRegVO>)new BaseDAO().retrieveByClause(LfwSsoRegVO.class, " ssokey = ?  " ,param);
			return (list != null && list.size() > 0) ? list.get(0).doGetRegmap() : new HashMap();
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
			throw new LfwBusinessException(e.getMessage());
		}
	}
}
