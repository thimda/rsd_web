package nc.uap.lfw.core.servlet.dft;

import java.util.Iterator;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.itf.ILfwSsoService;

/**
 * session销毁时调用,当session销毁时要在NC系统中注销用户
 * 
 * @author gd 2008-11-29
 * 
 */
public class LfwDefaultSessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent reqEvent) {
	}

	public void sessionDestroyed(HttpSessionEvent reqEvent) {
		
		HttpSession session = reqEvent.getSession();
		String sesid = session.getId();
		//删除单点登录凭证
		ILfwSsoService ssoService = NCLocator.getInstance().lookup(ILfwSsoService.class);
		try {
			ssoService.destoryToken(sesid);
		} 
		catch (Exception e) {
			LfwLogger.error("===LfwDefaultSessionListener===销毁单点登陆凭据失败！SessionID:"+sesid, e);
		}
		
		try{
			//消除因意外关闭导致的没有正常销毁的，属于本会话的内存占用
			destroyAllWebSession(session);
		}
		catch (Exception e) {
			LfwLogger.error(e);
		}
		
		//消除会话缓存
		try{
			LfwCacheManager.removeSessionCache(sesid);
		}
		catch(Exception e){
			LfwLogger.error(e);
		}
	}
	
	/**
	 * 清除当前页面的所有会话,延迟10秒执行
	 */
	@SuppressWarnings("unchecked")
	private void destroyAllWebSession(HttpSession session) {
		ILfwCache cache = LfwCacheManager.getSessionCache(session.getId());
		Iterator em = cache.getKeys().iterator();
		while (em.hasNext()) {
			String key = em.next().toString();
			if (key.startsWith(WebContext.WEB_SESSION_PRE)) {
				WebSession ws = (WebSession) cache.get(key);
				if (ws == null)
					continue;
				ws.destroy();
				em.remove();
				//cache.remove(key);
			}
		}
	}

}

