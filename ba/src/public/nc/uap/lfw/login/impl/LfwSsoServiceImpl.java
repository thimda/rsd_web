package nc.uap.lfw.login.impl;

import nc.bcmanage.bs.IBusiCenterManageService;
import nc.bcmanage.vo.BusiCenterVO;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.jdbc.framework.SQLParameter;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.itf.ILfwSsoService;
import nc.uap.lfw.login.vo.LfwSsoRegVO;
import nc.uap.lfw.login.vo.LfwTokenVO;
import nc.vo.pub.BusinessException;

public class LfwSsoServiceImpl implements ILfwSsoService {

	public String createToken(LfwTokenVO token) throws LfwBusinessException {
		BaseDAO baseDAO = new BaseDAO();
		try {
			return baseDAO.insertVO(token);
		} 
		catch (DAOException e) {
			LfwLogger.error("token±£´æÊ§°Ü", e);
			throw new LfwBusinessException(e);
		}
	}

	public void destoryToken(String sessionId) throws LfwBusinessException {
		BaseDAO baseDAO = new BaseDAO();
		try {
			baseDAO.deleteByClause(LfwTokenVO.class	, " userid IN (SELECT userid FROM uw_lfwtoken WHERE sessionid = '"+sessionId+"' ) ");
		} catch (DAOException e) {
			LfwLogger.error("tokenÏú»ÙÊ§°Ü", e);
			throw new LfwBusinessException(e);
		}
	}

	public void destoryAllToken() throws LfwBusinessException {
		IBusiCenterManageService ibcm = NCLocator.getInstance().lookup(IBusiCenterManageService.class);
		try {
			BusiCenterVO[] bcs = ibcm.getBusiCenterVOs();
			if(bcs != null && bcs.length > 0){
				for(BusiCenterVO bc : bcs){
					String dsName = bc.getDataSourceName();
					try {
						new BaseDAO(dsName).deleteByClause(LfwTokenVO.class	, " pk_lfwtoken != '' ");
					} catch (Exception e) {
						LfwLogger.error("tokenÏú»ÙÊ§°Ü!Êý¾ÝÔ´Ãû³Æ:" + dsName, e);
					}
				}
			}
		} catch (BusinessException e1) {
			LfwLogger.error(e1.getMessage(),e1);
		}
	}

	public String creatSsoInfo(LfwSsoRegVO vo) throws LfwBusinessException {
		BaseDAO baseDAO = new BaseDAO();
		try {
			baseDAO.insertVO(vo);
			return vo.getSsokey();
		} 
		catch (DAOException e) {
			LfwLogger.error("±£´æÊ§°Ü", e);
			throw new LfwBusinessException(e);
		}
	}

	public void destoryOverdueSsoInfo() throws LfwBusinessException {
		
	}

	public void destorySsoInfo(String key) throws LfwBusinessException {
		BaseDAO baseDAO = new BaseDAO();
		try {
			SQLParameter param = new SQLParameter();
			param.addParam(key);
			baseDAO.deleteByClause(LfwSsoRegVO.class	, " ssokey = ? " ,param );
		} catch (DAOException e) {
			LfwLogger.error("Ïú»ÙÊ§°Ü", e);
			throw new LfwBusinessException(e);
		}
	}
}
