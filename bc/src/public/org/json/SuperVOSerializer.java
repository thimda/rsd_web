/**
 * 
 */
package org.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.logging.Logger;

import nc.vo.pub.CircularlyAccessibleValueObject;


/**
 * SuperVO,CircularlyAccessibleValueObject的serialize
 * @author yzy Created on 2006-03-23
 */
public class SuperVOSerializer extends AbstractSerializer {

	private static final long serialVersionUID = -2887586811615257196L;
	private static final String STATUS = "status";
	private static final String STATUS_SET_METHOD = "setStatus";
	private final static Logger log = Logger.getLogger(SuperVOSerializer.class
			.getName());
	private static Class[] _serializableClasses = new Class[] {};
	private static Class[] _JSONClasses = new Class[] {};

	public Class[] getSerializableClasses() {
		return _serializableClasses;
	}

	public Class[] getJSONClasses() {
		return _JSONClasses;
	}

	public boolean canSerialize(Class clazz, Class jsonClazz) {
		return (nc.vo.pub.CircularlyAccessibleValueObject.class
				.isAssignableFrom(clazz) || nc.vo.pub.SuperVO.class
				.isAssignableFrom(clazz));
	}

	protected static class BeanSerializerState {

		// in absence of getters and setters, these fields are
		// public to allow subclasses to access.
		// Circular reference detection
		public HashSet beanSet = new HashSet();
	}

	public ObjectMatch tryUnmarshall(SerializerState state, Class clazz,
			Object o) throws UnmarshallException {
		JSONObject jso = (JSONObject) o;
		int match = 0, mismatch = 0;
		String[] attributeNames = null;
		try {
			attributeNames = ((CircularlyAccessibleValueObject) clazz
					.newInstance()).getAttributeNames();
		} catch (Exception ie) {
			log.info("class " + clazz.getName() + " instance error:"
					+ ie.getMessage());
		}
		if (attributeNames != null && attributeNames.length > 0) {
			for (int i = 0; i < attributeNames.length; i++) {
				String prop = attributeNames[i];
				if (jso.has(prop) || jso.has("m_" + prop)) match++;
				else mismatch++;
			}
		}
		if (match == 0) throw new UnmarshallException("bean has no matches");
		ObjectMatch m = null;
		return m.max(new ObjectMatch(mismatch));
	}

	public Object unmarshall(SerializerState state, Class clazz, Object o)
			throws UnmarshallException {
		JSONObject jso = (JSONObject) o;
		Object instance = null;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			throw new UnmarshallException("can't instantiate bean "
					+ clazz.getName() + ": " + e.getMessage());
		}
		Object fieldVal;
		Field[] voFields = clazz.getDeclaredFields();
		if (voFields == null || voFields.length <= 0) {
			log.info("supervo " + clazz.getName()
					+ " fields is null or is empty");
		}
		for (int i = 0; i < voFields.length; i++) {
			String field = voFields[i].getName();
			if (jso.has(field) || (field.startsWith("m_") && jso.has(field = field.substring(2,field.length())))) {
				Class fieldType = voFields[i].getType();
				fieldVal = ser.unmarshall(state, fieldType, jso.get(field));
				((CircularlyAccessibleValueObject) instance).setAttributeValue(
						field, fieldVal);
			}
		}
		//处理CircularlyAccessibleValueObject 的 status
		if (jso.has(STATUS)) {
			fieldVal = ser.unmarshall(state, int.class, jso.get(STATUS));
			try {
				Method method = clazz.getMethod(STATUS_SET_METHOD,new Class[]{int.class});
				method.invoke(instance,new Object[]{fieldVal});
			} catch (NoSuchMethodException nfe) {
				log.info("no setStatus");
			} catch (Exception e) {
				log.info("execute setStatus error:" + e.getMessage());
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
		JSONObject val = new JSONObject();
		if (ser.getMarshallClassHints()) val.put("javaClass", o.getClass()
				.getName());
		String[] attributeNames = ((CircularlyAccessibleValueObject) o)
				.getAttributeNames();
		Object result = null;
		if (attributeNames != null && attributeNames.length > 0) {
			for (int i = 0; i < attributeNames.length; i++) {
				result = ((CircularlyAccessibleValueObject) o)
						.getAttributeValue(attributeNames[i]);
				try {
					if (result != null || ser.getMarshallNullAttributes()) val
							.put(attributeNames[i], ser.marshall(state, result));
				} catch (MarshallException e) {
					throw new MarshallException("bean "
							+ o.getClass().getName() + " " + e.getMessage());
				}
			}
		}
		beanState.beanSet.remove(identity);
//		// 处理公式列
//		FormulaProcess.getInstance().formulaProcess(val, state, o);
		return val;
	}
}
