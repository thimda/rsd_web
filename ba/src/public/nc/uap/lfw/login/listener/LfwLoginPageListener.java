package nc.uap.lfw.login.listener;

import javax.servlet.http.HttpSession;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.event.PageEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.PageServerListener;

/**
 * ��¼ҳ�����Listener
 * 
 * @author guoweic
 *
 */
public class LfwLoginPageListener extends PageServerListener {

	public LfwLoginPageListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}
	
	public void afterPageInit(PageEvent e) {
		LfwRuntimeEnvironment.getWebContext().getRequest().getSession().setAttribute(WebContext.LOGIN_DID, null);
		// �����Ƶ�������b
		getGlobalContext().addExecScript("focusAccountCombo();\nloadInRealPage();\n");
	}
	
	@Override
	public void onClosed(PageEvent e) {
		HttpSession session = LfwRuntimeEnvironment.getWebContext().getRequest().getSession();
		if(session.getAttribute(WebContext.LOGIN_DID) == null)
			session.setAttribute(WebContext.LOGIN_DESTROY, "1");
		LfwRuntimeEnvironment.getWebContext().destroyWebSession();
	}
	
}
