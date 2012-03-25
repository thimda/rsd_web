package nc.uap.lfw.core;

import java.lang.reflect.Field;

import nc.uap.lfw.core.log.LfwLogger;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanGenerator;

public class LfwBeanUtil<T> {
	private static final String UIID_FIELD = "$UIID_FIELD";
	
	/**
	 * 产生包装对象
	 * @param c
	 * @return
	 */
	public T generateUIObject(Class<T> c) {
		return addExtendAttribute(UIID_FIELD, String.class, c);
	}
	
	/**
	 * 对已有对象复制包装
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T wrapUIObject(T obj){
		T newObj = generateUIObject((Class<T>) obj.getClass());
		BeanCopier copier = BeanCopier.create(obj.getClass(), newObj.getClass(), false);
		copier.copy(obj, newObj, null);
		return newObj;
	}
	
	/**
	 * 设置UI标识
	 * @param obj
	 * @param uiid
	 */
	public void setUIID(Object obj, String uiid){
		setExtendAttributeValue(UIID_FIELD, uiid, obj);
	}
	
	/**
	 * 获取UI标识
	 * @param obj
	 * @param uiid
	 * @return
	 */
	public String getUIID(Object obj){
		return (String) getExtendAttributeValue(UIID_FIELD, obj);
	}
	
	@SuppressWarnings("unchecked")
	public T addExtendAttribute(String name, Class<?> c, Class<T> superC) {
		BeanGenerator gen = new BeanGenerator();
		gen.addProperty(name, c);
		gen.setSuperclass(superC);
		return (T) gen.create();
	}
	
	public Object getExtendAttributeValue(String name, Object vo){
		Field f;
		try {
			f = vo.getClass().getDeclaredField("$cglib_prop_" + name);
			if(f == null)
				return null;
			f.setAccessible(true);
			return f.get(vo);
		} 
		catch (SecurityException e) {
			LfwLogger.error(e.getMessage(), e);
		} 
		catch (NoSuchFieldException e) {
			LfwLogger.info("没有找到对应的字段UI标识，是新增行");
		} 
		catch (IllegalArgumentException e) {
			LfwLogger.error(e.getMessage(), e);
		} 
		catch (IllegalAccessException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public void setExtendAttributeValue(String name, Object value, Object vo) {
		Field f;
		try {
			f = vo.getClass().getDeclaredField("$cglib_prop_" + name);
			f.setAccessible(true);
			f.set(vo, value);
		} 
		catch (SecurityException e) {
			LfwLogger.error(e.getMessage(), e);
		} 
		catch (NoSuchFieldException e) {
			LfwLogger.error(e.getMessage(), e);
		} 
		catch (IllegalArgumentException e) {
			LfwLogger.error(e.getMessage(), e);
		} 
		catch (IllegalAccessException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		
	}
}
