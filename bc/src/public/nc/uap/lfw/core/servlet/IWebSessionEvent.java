package nc.uap.lfw.core.servlet;

import java.io.Serializable;

import nc.uap.lfw.core.WebSession;

/**
 * Web Session级别会话事件
 * @author dengjt
 *
 */
public interface IWebSessionEvent extends Serializable{
	public WebSession getWebSession();
}
