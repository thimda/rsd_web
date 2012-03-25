package nc.uap.lfw.core.servlet.dft;

import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.servlet.IWebSessionEvent;
import nc.uap.lfw.core.servlet.IWebSessionListener;

/**
 * Ä¬ÈÏWebSession ¼àÌý
 * @author dengjt
 *
 */
public class LfwDefaultWebSessionListener implements IWebSessionListener{
	private static final long serialVersionUID = 1505801017808554358L;

	@Override
	public void sessionCreated(IWebSessionEvent sesEvent) {
		WebSession ws = sesEvent.getWebSession();
		if(isAppSession(ws)){
			String appId = (String) ws.getAttribute(WebContext.APP_ID);
			LfwLogger.info("App session created, id is " + appId);
		}
		else{
			LfwLogger.info("Page session created, id is " + ws.getPageId());
		}
	}

	@Override
	public void sessionDestroyed(IWebSessionEvent sesEvent) {
		WebSession ws = sesEvent.getWebSession();
		if(isAppSession(ws)){
			String appId = (String) ws.getAttribute(WebContext.APP_ID);
			LfwLogger.info("App session destroyed, id is " + appId);
		}
		else{
			LfwLogger.info("Page session destroyed, id is " + ws.getPageId());
		}
	}
	
	private boolean isAppSession(WebSession ws) {
		Boolean appSes = (Boolean) ws.getAttribute(WebContext.APP_SES);
		return appSes == null ? false : appSes.booleanValue();
	}
	
}
