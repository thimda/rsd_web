package nc.uap.lfw.servletplus.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * BaseAction�ӿ�
 * @author licza
 *
 */
public interface BaseActionInterface {
	/**
	 * ���request��response����
	 * @param request
	 * @param response
	 */
	public void fill(HttpServletRequest request,HttpServletResponse response);
	/**
	 * �������
	 */
	public void fush();
}
