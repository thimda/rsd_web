package nc.uap.lfw.servletplus.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.servletplus.bind.RequestDataConvert;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 参数转换工具类
 * 
 * @author licza
 * 
 */
public class TransfUtils {

	/**
	 * 转换parameter参数
	 * 
	 * @param name
	 *            参数名称
	 * @param typeName
	 *            类型名称
	 * @param request
	 *            request对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object getValue(String name, String typeName,
			HttpServletRequest request) {
		// 为提高效率 直接用if/else判断
		// 数组 数组是无序的
		if (typeName.startsWith("[L")) {
			String[] parValues = request.getParameterValues(name);
			typeName = typeName.replace("[L", "").replace(";", "");
			if (typeName.equals("java.lang.String")) {
				return parValues;
			} else if (typeName.equals("java.lang.Integer")) {
				return RequestDataConvert.getIntParameters(request, name);
			} else if (typeName.equals("java.lang.Double")) {
				return RequestDataConvert.getDoubleParameters(request, name);
			} else if (typeName.equals("java.lang.Long")) {
				return RequestDataConvert.getLongParameters(request, name);
			} else if (typeName.equals("java.lang.Float")) {
				return RequestDataConvert.getFloatParameters(request, name);
			} else if (typeName.equals("java.lang.Boolean")) {
				return RequestDataConvert.getBooleanParameters(request, name);
			}
			// request.parameter无序 暂不支持非基础类型数组的转换
		} else {
			if (typeName.equals("java.lang.String")) {
				return RequestDataConvert.getStringParameter(request, name, "");
			} else if (typeName.equals("java.lang.Integer")) {
				return RequestDataConvert.getIntParameter(request, name, 0);
			} else if (typeName.equals("java.lang.Double")) {
				return RequestDataConvert.getDoubleParameter(request, name, 0D);
			} else if (typeName.equals("java.lang.Long")) {
				return RequestDataConvert.getLongParameter(request, name, 0L);
			} else if (typeName.equals("java.lang.Float")) {
				return RequestDataConvert.getFloatParameter(request, name, 0F);
			} else if (typeName.equals("java.lang.Boolean")) {
				return RequestDataConvert.getBooleanParameter(request, name,
						null);
			} else {
				Object bean = null;
				try {
					bean = Class.forName(typeName).newInstance();
				} catch (Exception e) {
					// Bean对象不存在 直接返回null
					return bean;
				}
				return TransfUtils
						.toBean(name, bean, request.getParameterMap());
			}
		}
		return null;
	}

	/**
	 * 把request对象中的参数转换为对象
	 * URL参数：Bean名称.beanField名称(如user.userid=aaa&user.password=bbb)
	 * bean中必须有对的field 并且有public的seter方法,否则copy的时候会消耗较长时间或者失败 bean可见性必须为public
	 * 
	 * @param beanName
	 *            bean的名称
	 * @param bean
	 *            bean实体
	 * @param paramMap
	 *            request对象中的paramterMap
	 * @return
	 */
	private static Object toBean(String name, Object bean,
			Map<String, String[]> paramMap) {
		Iterator<Map.Entry<String, String[]>> iterator = paramMap.entrySet()
				.iterator();
		Map<String, String> beanMap = new HashMap<String, String>();
		while (iterator.hasNext()) {
			Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) iterator
					.next();
			String key = entry.getKey().toString();
			if (key.startsWith(name + ".")) {
				String[] paramValue = entry.getValue();
				if (paramValue != null && paramValue.length > 0) {
					// 有多个记录  只取第一个
					beanMap.put(key.replace(name + ".", ""), paramValue[0]);
				}
			}
		}
		try {
			BeanUtils.copyProperties(bean, beanMap);
			return bean;
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);;
			return null;
		}
	}
}
