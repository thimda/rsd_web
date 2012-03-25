package nc.uap.lfw.core;

import java.io.Serializable;
import java.util.Map;

import nc.uap.lfw.core.servlet.IWebSessionListener;

/**
 * ҳ�漶�Ự�ӿڣ�ÿ��ҳ��ͨ����ͬ��UniqueId��Ӧһ���Ự���˻Ự����ҳ��رջ��߸�ҳ��ر�ʱ����ע��
 * ���ڿ��滻�ԣ�Ҫ��Ự�е����Ա����ǿ����л���
 * @author dengjt
 *
 */
public interface WebSession extends Serializable {

	public static final String REFERER = "referer";

	/**
	 * ��ȡҳ��ỰID
	 * @return
	 */
	public String getWebSessionId();
	
	/**
	 * ��ȡҳ��ID
	 * @return
	 */
	public String getPageId();

	/**
	 * ��ȡҳ��Ự������
	 * @param key
	 * @return
	 */
	public Serializable getAttribute(String key);

	/**
	 * ����ҳ��ỰID
	 * @param sesId
	 */
	public void setWebSessionId(String sesId);
	
	public void setPageId(String pageId);

	/**
	 * ɾ��ҳ��Ự������
	 * @param key
	 */
	public Serializable removeAttribute(String key);

	/**
	 * ����ҳ��Ự�����ԡ����Ա����ǿ����л���
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key, Serializable value);

	/**
	 * ҳ��Ự������ʱ����
	 */
	public void destroy();
	
	public void created();
//	/**
//	 * ˢ������
//	 */
//	public void flush();

	public String getOriginalParameter(String key);
	
	public void addOriginalParameter(String key, String value);
	
	public Map<String, String> getOriginalParamMap();
	
	public IWebSessionListener[] getWebSessionListeners();
}
