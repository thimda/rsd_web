package nc.uap.lfw.core.servlet;

import nc.uap.lfw.core.AbstractWebSession;
import nc.uap.lfw.core.WebSession;

/**
 * WebSession会话事件
 * @author dengjt
 *
 */
public class WebSessionEvent implements IWebSessionEvent {
	private static final long serialVersionUID = -7614831985850849442L;
	private WebSession webSession;
	public WebSessionEvent(AbstractWebSession ws) {
		this.webSession = ws;
	}
	public WebSession getWebSession() {
		return webSession;
	}
	public void setWebSession(WebSession webSession) {
		this.webSession = webSession;
	}
}
