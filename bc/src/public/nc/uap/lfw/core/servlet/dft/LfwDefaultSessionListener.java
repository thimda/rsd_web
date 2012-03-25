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
 * session����ʱ����,��session����ʱҪ��NCϵͳ��ע���û�
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
		//ɾ�������¼ƾ֤
		ILfwSsoService ssoService = NCLocator.getInstance().lookup(ILfwSsoService.class);
		try {
			ssoService.destoryToken(sesid);
		} 
		catch (Exception e) {
			LfwLogger.error("===LfwDefaultSessionListener===���ٵ����½ƾ��ʧ�ܣ�SessionID:"+sesid, e);
		}
		
		try{
			//����������رյ��µ�û���������ٵģ����ڱ��Ự���ڴ�ռ��
			destroyAllWebSession(session);
		}
		catch (Exception e) {
			LfwLogger.error(e);
		}
		
		//�����Ự����
		try{
			LfwCacheManager.removeSessionCache(sesid);
		}
		catch(Exception e){
			LfwLogger.error(e);
		}
	}
	
	/**
	 * �����ǰҳ������лỰ,�ӳ�10��ִ��
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

