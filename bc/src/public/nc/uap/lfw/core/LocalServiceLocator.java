package nc.uap.lfw.core;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.log.LfwLogger;

public final class LocalServiceLocator extends ServiceLocator{

	@Override
	public Object getService(String name) {
		try{
			return NCLocator.getInstance().lookup(name);
		}
		catch(Exception e){
			LfwLogger.error("查找服务时报错，服务" + name, e);
			throw new ServiceNotFoundException("查找服务时报错，服务名称" + name);
		}
	}

}
