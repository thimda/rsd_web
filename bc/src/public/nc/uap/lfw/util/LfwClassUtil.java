package nc.uap.lfw.util;

import java.lang.reflect.Constructor;

import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;

/**
 * �򵥵�LFw���ʼ������.�˹��߲�׽�������쳣���ܳ�LfwRuntime�쳣��
 * @author dengjt
 *
 */
public class LfwClassUtil {
	public static Class<?> forName(String className, ClassLoader loader) {
		try {
			return Class.forName(className, true, loader);
		} 
		catch (ClassNotFoundException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException("���������," + e.getMessage());
		}
	}
	
	
	public static Class<?> forName(String className) {
		try {
			return Class.forName(className);
		} 
		catch (ClassNotFoundException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException("û�м��ص���:" + className);
		}
	}
	
	public static Object newInstance(String className) {
		if(className == null)
			throw new LfwRuntimeException("classname ����Ϊ��");
		return newInstance(forName(className));
	}
	
	public static Object newInstance(String className, ClassLoader loader) {
		return newInstance(forName(className, loader));
	}
	
	public static Object newInstance(String className, Class[] params, Object[] values){
		return newInstance(forName(className), params, values);
	}
	
	public static Object newInstance(String className, Class[] params, Object[] values, ClassLoader loader){
		return newInstance(forName(className, loader), params, values);
	}
	
	public static <T>T newInstance(Class<T> c, Class[] params, Object[] values){
		try {
			Constructor<T> cs = c.getConstructor(params);
			return cs.newInstance(values);
		}
		//catch all, include nullpointer
		catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException("���������," + e.getMessage(), e);
		} 
	}
	
	public static <T>T newInstance(Class<T> c) {
		try {
			return c.newInstance();
		}
		//catch all, include nullpointer
		catch (Exception e) { 
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException("������" + c.getName() + "����," + e.getMessage(), e);
		} 
	}
}
