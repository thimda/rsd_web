package nc.uap.lfw.core.uif.listener;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.bs.logging.Logger;
import nc.itf.uif.pub.IUifService;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

public class BillStateServiceImpl implements IBillStateService{

	private IUifService service = null;
	
	public AggregatedValueObject setBillState(AggregatedValueObject billVo) throws LfwBusinessException {
		try {
			SuperVO headVO = (SuperVO) billVo.getParentVO();
			BaseDAO dao = new BaseDAO();
			dao.updateVO(headVO);
			return getService().setBillTs(billVo);
		} catch (Exception ex) {
			LfwLogger.error(ex.getMessage(), ex);
			throw new LfwBusinessException(ex);
																															
		}
	}
	
	public IUifService getService() throws UifException {
		if (service == null) {
			try {
				service = (IUifService) NCLocator.getInstance().lookup(
						IUifService.class.getName());
			} catch (ComponentException e) {
				Logger.warn("can't find " + e.getComponentName(), e);
				throw new UifException();
			}
		}
		return service;

	}

}
