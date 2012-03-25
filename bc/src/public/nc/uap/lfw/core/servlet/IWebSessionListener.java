package nc.uap.lfw.core.servlet;

import java.io.Serializable;

/**
 * WebSession����
 * @author dengjt
 *
 */
public interface IWebSessionListener extends Serializable{
	public void sessionCreated(IWebSessionEvent sesEvent);
	public void sessionDestroyed(IWebSessionEvent sesEvent);
}
