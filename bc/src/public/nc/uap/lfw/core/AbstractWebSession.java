package nc.uap.lfw.core;

import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.servlet.IWebSessionEvent;
import nc.uap.lfw.core.servlet.IWebSessionListener;
import nc.uap.lfw.core.servlet.WebSessionEvent;

/**
 * 页面会话虚基类
 * @author dengjt
 *
 */
public abstract class AbstractWebSession implements WebSession {
	private static final long serialVersionUID = -4194778196164674386L;
	private Map<String, String> paramMap;
	private IWebSessionListener[] wsListeners;
	public AbstractWebSession(IWebSessionListener... wsListener) {
		this.wsListeners = wsListener;
	}
	public String getOriginalParameter(String key) {
		return paramMap == null ? null : paramMap.get(key);
	}
	public void addOriginalParameter(String key, String value) {
		if(paramMap == null){
			paramMap = new HashMap<String, String>();
		}
		paramMap.put(key, value);
	}
	public Map<String, String> getOriginalParamMap() {
		return paramMap;
	}
	
	public void created() {
		IWebSessionListener[] wsListeners = getWebSessionListeners();
		if(wsListeners != null){
			IWebSessionEvent event = new WebSessionEvent(this);
			for (int i = 0; i < wsListeners.length; i++) {
				try{
					wsListeners[i].sessionCreated(event);
				}
				catch(Exception e){
					LfwLogger.error(e);
				}
			}
		}
	}
	
	public void destroy() {
		IWebSessionListener[] wsListeners = getWebSessionListeners();
		if(wsListeners != null){
			IWebSessionEvent event = new WebSessionEvent(this);
			for (int i = 0; i < wsListeners.length; i++) {
				wsListeners[i].sessionDestroyed(event);
			}
		}
	}
	
	public IWebSessionListener[] getWebSessionListeners(){
		return wsListeners;
	}
}
