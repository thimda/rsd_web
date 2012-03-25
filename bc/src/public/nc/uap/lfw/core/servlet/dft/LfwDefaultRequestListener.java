package nc.uap.lfw.core.servlet.dft;

import javax.servlet.http.HttpServletRequest;

import nc.uap.lfw.core.servlet.LfwRequestListener;

/**
 * 请求监听，进行请求级变量的初始化或设置
 * 
 * @author
 * 
 */
public class LfwDefaultRequestListener extends LfwRequestListener {

	/**
	 * 从存储中恢复环境变量
	 */
	protected void restoreEnviroment(HttpServletRequest req) {
		super.restoreEnviroment(req);
	}

	protected void beginRequest(HttpServletRequest request) {
//		String compressStream = CookieUtil.get(request.getCookies(),
//				"compressStream");
//		if (compressStream != null && compressStream.equals("0")) {
//			LfwRuntimeEnvironment.setCompressStream(false);
//		}
		super.beginRequest(request);
	}

}
