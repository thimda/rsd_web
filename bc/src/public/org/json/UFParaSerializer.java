/**
 * 
 */
package org.json;

import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;

/**
 * UFDate,UFTime,UFBoolean,UFDateTime,UFDouble的Serializer处理
 * @author yzy
 */
public class UFParaSerializer extends AbstractSerializer {

	private static Class[] _serializableClasses = new Class[] { UFDate.class,
			UFTime.class, UFDateTime.class, UFDouble.class, UFBoolean.class };
	private static Class[] _JSONClasses = new Class[] {};

	/*
	 * (non-Javadoc)
	 * @see com.metaparadigm.jsonrpc.Serializer#getSerializableClasses()
	 */
	public Class[] getSerializableClasses() {
		return _serializableClasses;
	}

	/*
	 * (non-Javadoc)
	 * @see com.metaparadigm.jsonrpc.Serializer#getJSONClasses()
	 */
	public Class[] getJSONClasses() {
		return _JSONClasses;
	}

	public boolean canSerialize(Class clazz, Class jsonClazz) {
			return (nc.vo.pub.lang.UFDate.class.equals(clazz) || nc.vo.pub.lang.UFTime.class.equals(clazz) || nc.vo.pub.lang.UFDateTime.class.equals(clazz) ||
					nc.vo.pub.lang.UFBoolean.class.equals(clazz) || nc.vo.pub.lang.UFDouble.class.equals(clazz));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.metaparadigm.jsonrpc.Serializer#tryUnmarshall(com.metaparadigm.jsonrpc.SerializerState,
	 *      java.lang.Class, java.lang.Object)
	 */
	public ObjectMatch tryUnmarshall(SerializerState state, Class clazz,
			Object json) throws UnmarshallException {
		if (nc.vo.pub.lang.UFDate.class.equals(clazz) || nc.vo.pub.lang.UFTime.class.equals(clazz) || nc.vo.pub.lang.UFDateTime.class.equals(clazz) ||
				nc.vo.pub.lang.UFBoolean.class.equals(clazz) || nc.vo.pub.lang.UFDouble.class.equals(clazz)) {
			return ObjectMatch.OKAY;
		} else {
			return ObjectMatch.NULL;
		}
	}

	public Object toUFPara(Class clazz, Object jso)
			throws UnmarshallException {

		//lkp update 20060814 增加对空串的判断，否则将报异常。
		String str = null;
		if(jso instanceof String)
		{
			str = (String)jso;
			if(str.trim().length() == 0)
				str = null;
		}
		
		if (nc.vo.pub.lang.UFDate.class.equals(clazz)) {
			if (jso instanceof String) 
			{
				if(str == null)
				   return null;
				else
				   return new UFDate(str);
			}
			else
				return null;
		} else if (nc.vo.pub.lang.UFTime.class.equals(clazz)) {
			if (jso instanceof String) 
				if(str == null)
					return null;
				else
					return new UFTime(str);
			else 
				return null;
		} else if (nc.vo.pub.lang.UFDateTime.class.equals(clazz)) {
			if (jso instanceof String)
				if(str == null)
					return null;
				else
					return new UFDateTime((String) jso);
			else
				return null;
		} else if (nc.vo.pub.lang.UFBoolean.class.equals(clazz)) {
			if (jso instanceof String) {
				if(str == null)
					return null;
				else
					return new UFBoolean(str);
			} else if(jso instanceof Boolean) {
				return new UFBoolean(((Boolean)jso).booleanValue());
			} else {
				return null;
			}
		} else if (nc.vo.pub.lang.UFDouble.class.equals(clazz)) {
			if (jso instanceof String) {
				if(str == null)
					return null;
				else 
					return new UFDouble(str);
			} else {
				return new UFDouble(((Number) jso).doubleValue());
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.metaparadigm.jsonrpc.Serializer#unmarshall(com.metaparadigm.jsonrpc.SerializerState,
	 *      java.lang.Class, java.lang.Object)
	 */
	public Object unmarshall(SerializerState state, Class clazz, Object json)
			throws UnmarshallException {
		return toUFPara(clazz,json);
	}

	/*
	 * (non-Javadoc)
	 * @see com.metaparadigm.jsonrpc.Serializer#marshall(com.metaparadigm.jsonrpc.SerializerState,
	 *      java.lang.Object)
	 */
	public Object marshall(SerializerState state, Object o)
			throws MarshallException {
		if (o != null) return o.toString();
		return null;
	}
}
