package nc.uap.lfw.impl;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.ses.IWebSessionService;
import nc.uap.lfw.core.ses.WebSessionVO;

public class WebSessionServiceImpl implements IWebSessionService {

	public void deleteSesObj(String pk_ses) throws LfwBusinessException {
		try{
			BaseDAO dao = new BaseDAO();
			String sql = "delete from uw_webses where pk_webses = ?";
			SQLParameter param = new SQLParameter();
			param.addParam(pk_ses);
			dao.executeUpdate(sql, param);
		}
		catch(DAOException e){
			LfwLogger.error(e);
			throw new LfwBusinessException(e.getMessage());
		}
	}

	public String persistSesObj(WebSessionVO sesVO) throws LfwBusinessException {
		try{
			BaseDAO dao = new BaseDAO();
			return dao.insertVO(sesVO);
		}
		catch(DAOException e){
			LfwLogger.error(e);
			throw new LfwBusinessException(e.getMessage());
		}
	}

	public void updateSesObj(WebSessionVO sesVO) throws LfwBusinessException {
		try{
			BaseDAO dao = new BaseDAO();
			dao.updateVO(sesVO);
		}
		catch(DAOException e){
			LfwLogger.error(e);
			throw new LfwBusinessException(e.getMessage());
		}
	}

}
