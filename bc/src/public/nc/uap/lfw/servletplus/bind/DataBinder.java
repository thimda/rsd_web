package nc.uap.lfw.servletplus.bind;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;

import nc.uap.lfw.servletplus.annotation.Param;
import nc.uap.lfw.servletplus.constant.Keys;
import nc.uap.lfw.servletplus.utils.TransfUtils;
/**
 * ��request�����ݽ��а�
 * @author licza
 *
 */
public class DataBinder {
	/**
	 * ��request�л��Annotationָ���ķ�����������
	 * @param request
	 * @param cls
	 * @param ans
	 * @return
	 */
	public static Object[] bind(final HttpServletRequest request,final Class<?>[] cls,
			final Annotation[][] ans) {
		Object[] param = new Object[cls.length];
		for (int i = 0; i < cls.length; i++) {
			final Param paramAnn= getParamAnn(ans[i]);
			final String paramName=paramAnn.name();
			final String paramScope=paramAnn.scope();
			if (paramAnn == null) {
				return null;
			}
			if(paramScope!=null&&paramScope.equals(Keys.SESSION)){
				param[i] = request.getSession().getAttribute(paramName);
			}else{
				param[i] = TransfUtils.getValue(paramName, cls[i].getName(),
						request);
			}
		}
		return param;
	}

	/**
	 * ���Param Annotation
	 * 
	 * @param annotation
	 * @return
	 */
	public static Param getParamAnn(Annotation[] annotation) {
		if (annotation.length > 0) {
			for (Annotation ann : annotation) {
				if (ann instanceof Param) {
					Param param = (Param) ann;
					return param;
				}
			}
		}
		return null;
	}
}
