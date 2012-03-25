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
			LfwLogger.error("���ҷ���ʱ��������" + name, e);
			throw new ServiceNotFoundException("���ҷ���ʱ������������" + name);
		}
	}

}
