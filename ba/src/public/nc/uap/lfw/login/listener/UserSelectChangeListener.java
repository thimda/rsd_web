package nc.uap.lfw.login.listener;

import javax.servlet.http.Cookie;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.CookieConstant;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.event.TextEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.TextServerListener;

public class UserSelectChangeListener extends TextServerListener {

	public UserSelectChangeListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	@Override
	public void valueChanged(TextEvent e) {
		TextComp t = e.getSource();
		if(t.getId().equals("themeCombo")){
			String theme = t.getValue();
//			HttpSession ses = LfwRuntimeEnvironment.getWebContext().getRequest().getSession();
			//ses.setAttribute(WebConstant.SESSION_THEMEID_KEY, theme);
			Cookie lc = new Cookie(CookieConstant.THEME_CODE, theme);
			lc.setPath("/");
			lc.setMaxAge(CookieConstant.MAX_AGE);
			LfwRuntimeEnvironment.getWebContext().getResponse().addCookie(lc);
			getGlobalContext().refreshWindow();
		}
		else if(t.getId().equals("multiLanguageCombo")){
			String lang = t.getValue();
//			HttpSession ses = LfwRuntimeEnvironment.getWebContext().getRequest().getSession();
			//ses.setAttribute(WebConstant.SESSION_LANGUAGE_KEY, lang);
			Cookie lc = new Cookie(CookieConstant.LANG_CODE, lang);
			lc.setPath("/");
			lc.setMaxAge(CookieConstant.MAX_AGE);
			LfwRuntimeEnvironment.getWebContext().getResponse().addCookie(lc);
			getGlobalContext().refreshWindow();
		}
	}

}
