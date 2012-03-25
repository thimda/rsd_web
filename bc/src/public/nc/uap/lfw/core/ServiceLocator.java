package nc.uap.lfw.core;

import java.util.HashMap;
import java.util.Map;

/**
 * ������ң�ͨ����ͬ��ʵ�ֽ����NCLocator����������ͨ��getInstance��getRemoteInstance�ֱ��ȡ���ط������
 * ��Զ�̷�����ҡ�
 * @author dengjt
 *
 */
public abstract class ServiceLocator {
	/**
	 * ���ط���λ
	 */
	private static LocalServiceLocator localLocator = new LocalServiceLocator();
	private static Map<String, RemoteServiceLocator> remoteLocatorMap = new HashMap<String, RemoteServiceLocator>();
	
	public static ServiceLocator getInstance() {
		return localLocator;
	}
	
	public static ServiceLocator getRemoteInstance(String remoteIp) {
		RemoteServiceLocator locator = remoteLocatorMap.get(remoteIp);
		if(locator == null){
			locator = new RemoteServiceLocator();
			remoteLocatorMap.put(remoteIp, locator);
		}
		return locator;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getService(Class<T> clazz) {
		return (T) getService(clazz.getName());
	}
	
	public abstract Object getService(String name);
}
