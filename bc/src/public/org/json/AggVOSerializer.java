/**
 * 
 */
package org.json;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import nc.uap.lfw.core.log.LfwLogger;
import nc.vo.pub.ExtendedAggregatedValueObject;
import nc.vo.trade.pub.IExAggVO;


/**
 * 聚合VO（1：1和1：N的聚合VO）处理
 * @author yzy
 */
public class AggVOSerializer extends AbstractSerializer {


	private static final long serialVersionUID = 3358689555031508636L;
	private static HashMap beanCache = new HashMap();
	private static Class[] _serializableClasses = new Class[] {};
	private static Class[] _JSONClasses = new Class[] {};
	public final static String AGGVO_TYPE_KEY = "voType";
	private final static int SIMPLE_AGGVO = 1;
	private final static int MUTI_AGGVO = 2;

	public Class[] getSerializableClasses() {
		return _serializableClasses;
	}

	public Class[] getJSONClasses() {
		return _JSONClasses;
	}

	public boolean canSerialize(Class clazz, Class jsonClazz) {
		return (nc.vo.pub.ExtendedAggregatedValueObject.class.isAssignableFrom(clazz)
				|| nc.vo.trade.pub.IExAggVO.class.isAssignableFrom(clazz) 
				|| nc.vo.pub.AggregatedValueObject.class.isAssignableFrom(clazz));
	}

	protected static class BeanData {

		// in absence of getters and setters, these fields are
		// public to allow subclasses to access.
		public BeanInfo beanInfo;
		public HashMap readableProps;
		public HashMap writableProps;
	}

	protected static class BeanSerializerState {

		// in absence of getters and setters, these fields are
		// public to allow subclasses to access.
		// Circular reference detection
		public HashSet beanSet = new HashSet();
	}

	public static BeanData analyzeBean(Class clazz)
			throws IntrospectionException {
		LfwLogger.info("analyzing " + clazz.getName());
		BeanData bd = new BeanData();
		bd.beanInfo = Introspector.getBeanInfo(clazz, Object.class);
		PropertyDescriptor props[] = bd.beanInfo.getPropertyDescriptors();
		bd.readableProps = new HashMap();
		bd.writableProps = new HashMap();
		for (int i = 0; i < props.length; i++) {
			if (props[i].getWriteMethod() != null) {
				bd.writableProps.put(props[i].getName(), props[i]
						.getWriteMethod());
			}
			if (props[i].getReadMethod() != null) {
				bd.readableProps.put(props[i].getName(), props[i]
						.getReadMethod());
			}
		}
		return bd;
	}

	public static BeanData getBeanData(Class clazz)
			throws IntrospectionException {
		BeanData bd;
		synchronized (beanCache) {
			bd = (BeanData) beanCache.get(clazz);
			if (bd == null) {
				bd = analyzeBean(clazz);
				beanCache.put(clazz, bd);
			}
		}
		return bd;
	}

	public ObjectMatch tryUnmarshall(SerializerState state, Class clazz,
			Object o) throws UnmarshallException {
		JSONObject jso = (JSONObject) o;
		BeanData bd = null;
		try {
			bd = getBeanData(clazz);
		} catch (IntrospectionException e) {
			throw new UnmarshallException(clazz.getName() + " is not a bean");
		}
		int match = 0, mismatch = 0;
		Iterator i = bd.writableProps.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry ent = (Map.Entry) i.next();
			String prop = (String) ent.getKey();
			Method method = (Method) ent.getValue();
			if (jso.has(prop)) match++;
			else mismatch++;
		}
		if (match == 0) throw new UnmarshallException("bean has no matches");
		ObjectMatch m = null, tmp = null;
		i = jso.keys();
		while (i.hasNext()) {
			String field = (String) i.next();
			Method setMethod = (Method) bd.writableProps.get(field);
			if (setMethod != null) {
				try {
					Class param[] = setMethod.getParameterTypes();
					if (param.length != 1) throw new UnmarshallException(
							"bean " + clazz.getName() + " method "
									+ setMethod.getName()
									+ " does not have one arg");
					tmp = ser.tryUnmarshall(state, param[0], jso.get(field));
					if (m == null) m = tmp;
					else m = m.max(tmp);
				} catch (UnmarshallException e) {
					throw new UnmarshallException("bean " + clazz.getName()
							+ " " + e.getMessage());
				}
			} else {
				mismatch++;
			}
		}
		return m.max(new ObjectMatch(mismatch));
	}

	public Object unmarshall(SerializerState state, Class clazz, Object o)
			throws UnmarshallException {
		JSONObject jso = (JSONObject) o;
		BeanData bd = null;
		try {
			bd = getBeanData(clazz);
		} catch (IntrospectionException e) {
			throw new UnmarshallException(clazz.getName() + " is not a bean");
		}
		if (ser.isDebug()) LfwLogger.info("instantiating " + clazz.getName());
		Object instance = null;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			throw new UnmarshallException("can't instantiate bean "
					+ clazz.getName() + ": " + e.getMessage());
		}
		Object invokeArgs[] = new Object[1];
		Object fieldVal;
		Iterator i = jso.keys();
		while (i.hasNext()) {
			String field = (String) i.next();
			Method setMethod = (Method) bd.writableProps.get(field);
			if (setMethod != null) {
				try {
					Class param[] = setMethod.getParameterTypes();
					fieldVal = ser.unmarshall(state, param[0], jso.get(field));
				} catch (UnmarshallException e) {
					throw new UnmarshallException("bean " + clazz.getName()
							+ " " + e.getMessage());
				}
				if (ser.isDebug()) LfwLogger.info("invoking " + setMethod.getName()
						+ "(" + fieldVal + ")");
				invokeArgs[0] = fieldVal;
				try {
					setMethod.invoke(instance, invokeArgs);
				} catch (Throwable e) {
					if (e instanceof InvocationTargetException) e = ((InvocationTargetException) e)
							.getTargetException();
					throw new UnmarshallException("bean " + clazz.getName()
							+ "can't invoke " + setMethod.getName() + ": "
							+ e.getMessage());
				}
			}
		}
		return instance;
	}

	public Object marshall(SerializerState state, Object o)
			throws MarshallException {
		BeanSerializerState beanState;
		try {
			beanState = (BeanSerializerState) state
					.get(BeanSerializerState.class);
		} catch (Exception e) {
			throw new MarshallException("bean serializer internal error");
		}
		Integer identity = new Integer(System.identityHashCode(o));
		if (beanState.beanSet.contains(identity)) throw new MarshallException(
				"circular reference");
		beanState.beanSet.add(identity);
		BeanData bd = null;
		try {
			bd = getBeanData(o.getClass());
		} catch (IntrospectionException e) {
			throw new MarshallException(o.getClass().getName()
					+ " is not a bean");
		}
		JSONObject val = new JSONObject();
		if (ser.getMarshallClassHints()) val.put("javaClass", o.getClass()
				.getName());
		if (o instanceof ExtendedAggregatedValueObject || o instanceof IExAggVO) {
			val.put(AGGVO_TYPE_KEY, new Integer(MUTI_AGGVO));
		} else {
			val.put(AGGVO_TYPE_KEY, new Integer(SIMPLE_AGGVO));
		}
		Iterator i = bd.readableProps.entrySet().iterator();
		Object args[] = new Object[0];
		Object result = null;
		while (i.hasNext()) {
			Map.Entry ent = (Map.Entry) i.next();
			String prop = (String) ent.getKey();
			Method getMethod = (Method) ent.getValue();
			if (ser.isDebug()) LfwLogger.info("invoking " + getMethod.getName()
					+ "()");
			try {
				result = getMethod.invoke(o, args);
			} catch (Throwable e) {
				
				if (e instanceof InvocationTargetException) 
					e = ((InvocationTargetException) e).getTargetException();
				
//lkp update20060814 将异常进行了屏蔽，如果在序列化过程中调用方法时抛出异常，则将结果设置为null.
//这主要是因为在单表体的聚合VO进行序列化时表头为null,有些方法不能被调用。
//				throw new MarshallException("bean " + o.getClass().getName()
//						+ " can't invoke " + getMethod.getName() + ": "
//						+ e.getMessage());
				result = null;
			}
			try {
				if (result != null || ser.getMarshallNullAttributes()) val.put(
						prop, ser.marshall(state, result));
			} catch (MarshallException e) {
				throw new MarshallException("bean " + o.getClass().getName()
						+ " " + e.getMessage());
			}
		}
		beanState.beanSet.remove(identity);
		return val;
	}
}
