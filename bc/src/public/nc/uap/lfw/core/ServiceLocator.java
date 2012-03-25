package nc.uap.lfw.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务查找，通过不同的实现解除对NCLocator的依赖。可通过getInstance与getRemoteInstance分别获取本地服务查找
 * 与远程服务查找。
 * @author dengjt
 *
 */
public abstract class ServiceLocator {
	/**
	 * 本地服务定位
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
