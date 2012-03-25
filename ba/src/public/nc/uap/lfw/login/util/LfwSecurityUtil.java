package nc.uap.lfw.login.util;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.servlet.http.HttpSession;
import nc.bs.logging.Logger;

/**
 * yer安全工具类
 * 
 * @author gd 2008-11-28
 * @version NC5.5
 * @since NC5.5
 * 
 */

public class LfwSecurityUtil {
	private static final ConcurrentMap<String, HttpSession> sessionMap = new ConcurrentHashMap<String, HttpSession>();

	public static void addSessionToQueue(HttpSession session) {
		if (session != null) {
			session.setAttribute("SESSION_EXPIRE_TIME", System
					.currentTimeMillis());
			sessionMap.put(session.getId(), session);
		}
	}

	public static void deleteSessionFromQueue(String id) {
		if (sessionMap.containsKey(id)) {
			if (Logger.isDebugEnabled())
				Logger.debug("===delete session from queue:" + id);
			sessionMap.remove(id);
		}
	}

	public static void clearExpiredSession() {
		Iterator<Entry<String, HttpSession>> it = sessionMap.entrySet()
				.iterator();
		if (Logger.isDebugEnabled())
			Logger.info("===before clear,session count=" + sessionMap.size());
		long nowTime = System.currentTimeMillis();
		while (it.hasNext()) {
			Entry<String, HttpSession> entry = it.next();
			HttpSession session = entry.getValue();
			try {
				Long savedTime = (Long) session
						.getAttribute("SESSION_EXPIRE_TIME");
				if (nowTime - savedTime.longValue() > 5000) {
					if (Logger.isDebugEnabled())
						Logger.info("===clear session for id:"
								+ session.getId());
					session.invalidate();
					it.remove();
				}
			} catch (IllegalStateException e) {
				it.remove();
				Logger.error(e);
			}
		}
		if (Logger.isInfoEnabled())
			Logger.info("after clear:" + sessionMap.size());
	}
}
