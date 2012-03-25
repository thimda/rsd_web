package nc.uap.lfw.stylemgr.impl;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.pa.PaBusinessException;
import nc.uap.lfw.stylemgr.itf.IUwFuncnodeQry;
import nc.uap.lfw.stylemgr.vo.UwFuncNodeVO;

public class UwFuncnodeQryServiceImpl implements IUwFuncnodeQry{

	public UwFuncNodeVO getUwFuncnodeVOByPk(String funcnodePk)
			throws PaBusinessException {
		
		BaseDAO baseDAO = new BaseDAO();
		
		try {
			return (UwFuncNodeVO) baseDAO.retrieveByPK(UwFuncNodeVO.class, funcnodePk);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e.getMessage());
		}	
	}

}
