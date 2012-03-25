package nc.uap.lfw.servletplus.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * BaseAction接口
 * @author licza
 *
 */
public interface BaseActionInterface {
	/**
	 * 填充request及response参数
	 * @param request
	 * @param response
	 */
	public void fill(HttpServletRequest request,HttpServletResponse response);
	/**
	 * 输出数据
	 */
	public void fush();
}
