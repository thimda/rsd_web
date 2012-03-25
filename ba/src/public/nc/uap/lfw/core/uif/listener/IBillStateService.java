package nc.uap.lfw.core.uif.listener;

import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.vo.pub.AggregatedValueObject;

public interface IBillStateService {

	public AggregatedValueObject setBillState(AggregatedValueObject billVo) throws LfwBusinessException;
}
