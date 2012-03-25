package nc.uap.lfw.core.uif.listener;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.vo.pub.AggregatedValueObject;

public class DefaultBillStateSet {

	private IBillStateService service;
	
	public AggregatedValueObject setBillBillState(AggregatedValueObject billVo)throws LfwBusinessException {
		return getService().setBillState(billVo);
	}
	
	public IBillStateService getService() throws LfwBusinessException {
		if (service == null) {
			try {
				service = (IBillStateService) NCLocator.getInstance().lookup(IBillStateService.class.getName());
			} catch (ComponentException e) {
				Logger.warn("can't find " + e.getComponentName(), e);
				throw new LfwBusinessException(e);
			}
		}
		return service;

	}
}
