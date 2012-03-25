package nc.uap.lfw.core.serializer.impl;

import java.util.NoSuchElementException;

import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.util.LfwClassUtil;
import nc.vo.ml.NCLangRes4VoTransl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONSerializer;
import org.json.MarshallException;
import org.json.UnmarshallException;

/**
 * Json Object Serializer
 * @author dengjt
 *
 */

public class LfwJsonSerializer extends JSONSerializer {
	
	private static final long serialVersionUID = -3160947878333620366L;
	
	private static LfwJsonSerializer instance = new LfwJsonSerializer();
	
	private final String CONTEXT_PACKAGE = "nc.uap.lfw.core.comp.ctx.";
	
	public static LfwJsonSerializer getInstance() {
		return instance;
	}
	
	private LfwJsonSerializer(){
		try {
			registerDefaultSerializers();
		} 
		catch (Exception e) {
			LfwLogger.error("初始化json序列化器出错", e);
		}
	}
	
	public String toJsObject(Object o) {
		try {
			return toJSON(o);
		} 
		catch (MarshallException e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "LfwJsonSerializer-000000", null, new String[]{e.getMessage()})/*序列化js对象出现问题,{0}*/);
		}
	}
	
	public Object fromJsObject(String s) {
		if(s == null || s.equals(""))
			return null;
		try {
			return fromJSON(s);
		} catch (UnmarshallException e) {
			LfwLogger.error("反序列化出现问题，" + s, e);
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "LfwJsonSerializer-000001", null, new String[]{e.getMessage()})/*反序列化js对象出现问题,{0}*/);
		}
	}
	
	/**
	 * 获取映射类
	 */
	protected Class getClassFromUserDefine(Object o) {
		if (o == null)
			return null;
		if (o instanceof JSONObject) {
			try {
				String context_name = ((JSONObject) o).getString("c");
				String class_name = CONTEXT_PACKAGE + context_name;
				Class clazz = LfwClassUtil.forName(class_name);
				return clazz;
			} catch (NoSuchElementException e) {
			} catch (Exception e) {
				throw new LfwRuntimeException("class in hint not found");
			}
		}
		if (o instanceof JSONArray) {
			JSONArray arr = (JSONArray) o;
			if (arr.length() == 0)
				throw new LfwRuntimeException("no type for empty array");
			// return type of first element
			Class compClazz = getClassFromUserDefine(arr.get(0));
			try {
				if (compClazz.isArray())
					return Class.forName("[" + compClazz.getName());
				else
					return Class.forName("[L" + compClazz.getName() + ";");
			} catch (ClassNotFoundException e) {
				throw new LfwRuntimeException("problem getting array type");
			}
		}
		return o.getClass();
	}
}
