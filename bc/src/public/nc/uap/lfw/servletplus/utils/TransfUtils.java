package nc.uap.lfw.servletplus.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.servletplus.bind.RequestDataConvert;

import org.apache.commons.beanutils.BeanUtils;

/**
 * ����ת��������
 * 
 * @author licza
 * 
 */
public class TransfUtils {

	/**
	 * ת��parameter����
	 * 
	 * @param name
	 *            ��������
	 * @param typeName
	 *            ��������
	 * @param request
	 *            request����
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object getValue(String name, String typeName,
			HttpServletRequest request) {
		// Ϊ���Ч�� ֱ����if/else�ж�
		// ���� �����������
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
			// request.parameter���� �ݲ�֧�ַǻ������������ת��
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
					// Bean���󲻴��� ֱ�ӷ���null
					return bean;
				}
				return TransfUtils
						.toBean(name, bean, request.getParameterMap());
			}
		}
		return null;
	}

	/**
	 * ��request�����еĲ���ת��Ϊ����
	 * URL������Bean����.beanField����(��user.userid=aaa&user.password=bbb)
	 * bean�б����жԵ�field ������public��seter����,����copy��ʱ������Ľϳ�ʱ�����ʧ�� bean�ɼ��Ա���Ϊpublic
	 * 
	 * @param beanName
	 *            bean������
	 * @param bean
	 *            beanʵ��
	 * @param paramMap
	 *            request�����е�paramterMap
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
					// �ж����¼  ֻȡ��һ��
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
