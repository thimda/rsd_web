package nc.uap.lfw.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.core.servlet.IWebSessionListener;

public class DbWebSession extends AbstractWebSession{
	private static final long serialVersionUID = 8733097358391655209L;
	private String pageId;
	private String sesId;
	private Map<String, Serializable> objMap = null;

	public DbWebSession(IWebSessionListener ... wsListener) {
		super(wsListener);
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void created() {
		super.created();
	}
	
	public Serializable getAttribute(String key) {
		return objMap == null ? null : objMap.get(key);
	}

	public String getWebSessionId() {
		return sesId;
	}

	public Serializable removeAttribute(String key) {
		if(objMap == null)
			return null;
		return objMap.remove(key);
	}

	public void setAttribute(String key, Serializable value) {
		if(objMap == null)
			objMap = new HashMap<String, Serializable>();
		objMap.put(key, value);
	}

	public void setWebSessionId(String sesId) {
		this.sesId = sesId;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getSesId() {
		return sesId;
	}

	public void setSesId(String sesId) {
		this.sesId = sesId;
	}
}
