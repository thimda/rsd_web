package nc.uap.lfw.core.event.conf;

import java.util.List;
import java.util.Map;


public interface IListenerSupport {
	public void addListener(JsListenerConf listener);
	public void removeListener(String key);
	public Map<String, JsListenerConf> getListenerMap();
	public List<Class<? extends JsListenerConf>> getAcceptListeners();
}
