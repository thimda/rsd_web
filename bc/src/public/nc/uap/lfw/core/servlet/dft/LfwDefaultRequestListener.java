package nc.uap.lfw.core.servlet.dft;

import javax.servlet.http.HttpServletRequest;

import nc.uap.lfw.core.servlet.LfwRequestListener;

/**
 * ����������������󼶱����ĳ�ʼ��������
 * 
 * @author
 * 
 */
public class LfwDefaultRequestListener extends LfwRequestListener {

	/**
	 * �Ӵ洢�лָ���������
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
