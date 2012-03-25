package nc.uap.lfw.login.itf;

import java.util.Map;

import nc.uap.lfw.login.listener.AuthenticationUserVO;

/**
 * Lfw����
 * @author licza
 *
 */
public interface ILfwIntegrationHandler {
	/**
	 * У���û�
	 * @param userVO
	 * @return
	 */
	public void verify(AuthenticationUserVO userVO) throws LoginInterruptedException;
	/**
	 * ��õ����½VO
	 * @return
	 * @throws LoginInterruptedException
	 */
	public AuthenticationUserVO getSsoAuthenticateVO(Map<String, String> param) throws LoginInterruptedException;

}
