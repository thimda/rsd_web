package nc.uap.lfw.core.serializer;

import java.util.Map;

public interface IXml2ObjectSerializer<T>{
	public T serialize(String xml, Map<String, Object> paramMap);
}
