package nc.uap.lfw.ra.itf;


/**
 * @author renxh
 * ��Ⱦ���Ķ�̬���Խӿ�
 *
 */
public interface IDynamicAttributes {



	/**
	 * 2011-7-28 ����08:03:02 renxh
	 * des����ö�������
	 * @param key
	 * @return
	 */
	public Object getAttribute(String key);

	/**
	 * 2011-7-28 ����08:02:01 renxh
	 * des�������������
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key, Object value);
	
	/**
	 * 2011-7-28 ����08:02:41 renxh
	 * des����������ɾ��
	 * @param key
	 * 
	 */
	public void removeAttribute(String key);
	
	/**
	 * 2011-7-26 ����02:57:01 renxh 
	 * des���������������ԣ�ȫ��	 * 
	 * @param key
	 * @param obj
	 */
	public void setContextAttribute(String key, Object obj);

	/**
	 * 2011-7-26 ����02:57:26 renxh des��
	 * ������������ԣ�ȫ��	 * 
	 * @param key
	 * @return
	 */
	public Object getContextAttribute(String key);

	/**
	 * 2011-7-26 ����02:57:39 renxh 
	 * des��ɾ������������ ȫ��	 * 
	 * @param key
	 */
	public void removeContextAttribute(String key) ;
}
