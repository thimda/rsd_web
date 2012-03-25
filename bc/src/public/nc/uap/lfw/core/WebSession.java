package nc.uap.lfw.core;

import java.io.Serializable;
import java.util.Map;

import nc.uap.lfw.core.servlet.IWebSessionListener;

/**
 * 页面级会话接口，每个页面通过不同的UniqueId对应一个会话。此会话会在页面关闭或者父页面关闭时，被注销
 * 由于可替换性，要求会话中的属性必须是可序列化的
 * @author dengjt
 *
 */
public interface WebSession extends Serializable {

	public static final String REFERER = "referer";

	/**
	 * 获取页面会话ID
	 * @return
	 */
	public String getWebSessionId();
	
	/**
	 * 获取页面ID
	 * @return
	 */
	public String getPageId();

	/**
	 * 获取页面会话中属性
	 * @param key
	 * @return
	 */
	public Serializable getAttribute(String key);

	/**
	 * 设置页面会话ID
	 * @param sesId
	 */
	public void setWebSessionId(String sesId);
	
	public void setPageId(String pageId);

	/**
	 * 删除页面会话中属性
	 * @param key
	 */
	public Serializable removeAttribute(String key);

	/**
	 * 设置页面会话中属性。属性必须是可序列化的
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key, Serializable value);

	/**
	 * 页面会话被销毁时调用
	 */
	public void destroy();
	
	public void created();
//	/**
//	 * 刷新内容
//	 */
//	public void flush();

	public String getOriginalParameter(String key);
	
	public void addOriginalParameter(String key, String value);
	
	public Map<String, String> getOriginalParamMap();
	
	public IWebSessionListener[] getWebSessionListeners();
}
