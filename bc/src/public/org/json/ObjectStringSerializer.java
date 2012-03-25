package org.json;
/**
 * 用于解决一些vo中的类型是object的问题
 * @author dengjt
 *
 */
public class ObjectStringSerializer extends AbstractSerializer {

	private static final long serialVersionUID = 1L;

	public Class[] getJSONClasses() {
		return new Class[]{String.class};
	}

	public Class[] getSerializableClasses() {
		return new Class[]{Object.class};
	}

	public Object marshall(SerializerState state, Object o)
			throws MarshallException {
		return o.toString();
	}

	public ObjectMatch tryUnmarshall(SerializerState state, Class clazz,
			Object json) throws UnmarshallException {
		if(clazz.equals(Object.class) && json.equals(String.class))
			return ObjectMatch.OKAY;
		return ObjectMatch.NULL;
	}

	public Object unmarshall(SerializerState state, Class clazz, Object json)
			throws UnmarshallException {
		return json;
	}

}
