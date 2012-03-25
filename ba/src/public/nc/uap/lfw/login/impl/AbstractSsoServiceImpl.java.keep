package nc.uap.lfw.login.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.SessionConstant;
import nc.uap.lfw.login.itf.ILoginSsoService;
import nc.uap.lfw.login.vo.LfwSessionBean;

/**
 * 默认实现，通过cookie实现跨ctx的登录信息共享
 * 
 * @author dengjt
 * 
 */
public abstract class AbstractSsoServiceImpl<T extends LfwSessionBean>
		implements ILoginSsoService<T> {


	public void addSsoSign(T ssoInfo, String sysid) {
		HttpSession httpSession = LfwRuntimeEnvironment.getWebContext()
				.getRequest().getSession();
		httpSession.setAttribute(SessionConstant.LOGIN_SESSION_BEAN, ssoInfo);
		LfwRuntimeEnvironment.setLfwSessionBean(ssoInfo);
		LfwRuntimeEnvironment.setDatasource(ssoInfo.getDatasource());

		Map<String, String> infoMap = getInfoMap(ssoInfo);
		if (infoMap != null && infoMap.size() > 0) {
			HttpServletResponse res = LfwRuntimeEnvironment.getWebContext()
					.getResponse();
			Iterator<Entry<String, String>> it = infoMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
				String key = entry.getKey();
				// 区分不同系统的cookie
				key = "L_" + sysid + "_" + key;
				Cookie c = new Cookie(key, entry.getValue());
				// 会话级cookie
				c.setMaxAge(-1);
				c.setPath("/");
				res.addCookie(c);
			}
		}
	}

	protected abstract Map<String, String> getInfoMap(T ssoInfo);

	public T restoreSsoSign(String sysid) {
		HttpServletRequest req = LfwRuntimeEnvironment.getWebContext()
				.getRequest();
		String key = "L_" + sysid + "_";
		Cookie[] cks = req.getCookies();
		if (cks != null && cks.length > 0) {
			Map<String, String> info = new HashMap<String, String>();
			for (int i = 0; i < cks.length; i++) {
				Cookie ck = cks[i];
				String name = ck.getName();
				if (name.startsWith(key)) {
					name = name.substring(key.length());
					info.put(name, ck.getValue());
				}
			}

			if (info.size() == 0)
				return null;
			T ses = restoreByInfoMap(info);
			if (ses != null) {
				HttpSession httpSession = LfwRuntimeEnvironment.getWebContext()
						.getRequest().getSession();
				httpSession.setAttribute(SessionConstant.LOGIN_SESSION_BEAN,
						ses);
				LfwRuntimeEnvironment.setLfwSessionBean(ses);
			}
			return ses;
		} else
			return null;
	}

	protected abstract T restoreByInfoMap(Map<String, String> info);

	

}
