package nc.uap.lfw.core.servlet;

import java.io.Serializable;

import nc.uap.lfw.core.WebSession;

/**
 * Web Session����Ự�¼�
 * @author dengjt
 *
 */
public interface IWebSessionEvent extends Serializable{
	public WebSession getWebSession();
}
