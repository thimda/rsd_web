package nc.uap.lfw.ra.itf;


/**
 * @author renxh
 * 渲染器的动态属性接口
 *
 */
public interface IDynamicAttributes {



	/**
	 * 2011-7-28 下午08:03:02 renxh
	 * des：获得对象属性
	 * @param key
	 * @return
	 */
	public Object getAttribute(String key);

	/**
	 * 2011-7-28 下午08:02:01 renxh
	 * des：对象属性添加
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key, Object value);
	
	/**
	 * 2011-7-28 下午08:02:41 renxh
	 * des：对象属性删除
	 * @param key
	 * 
	 */
	public void removeAttribute(String key);
	
	/**
	 * 2011-7-26 下午02:57:01 renxh 
	 * des：设置上下文属性，全局	 * 
	 * @param key
	 * @param obj
	 */
	public void setContextAttribute(String key, Object obj);

	/**
	 * 2011-7-26 下午02:57:26 renxh des：
	 * 获得上下文属性，全局	 * 
	 * @param key
	 * @return
	 */
	public Object getContextAttribute(String key);

	/**
	 * 2011-7-26 下午02:57:39 renxh 
	 * des：删除上下文属性 全局	 * 
	 * @param key
	 */
	public void removeContextAttribute(String key) ;
}
