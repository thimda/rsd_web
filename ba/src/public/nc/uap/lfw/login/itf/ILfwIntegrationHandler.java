package nc.uap.lfw.login.itf;

import java.util.Map;

import nc.uap.lfw.login.listener.AuthenticationUserVO;

/**
 * Lfw集成
 * @author licza
 *
 */
public interface ILfwIntegrationHandler {
	/**
	 * 校验用户
	 * @param userVO
	 * @return
	 */
	public void verify(AuthenticationUserVO userVO) throws LoginInterruptedException;
	/**
	 * 获得单点登陆VO
	 * @return
	 * @throws LoginInterruptedException
	 */
	public AuthenticationUserVO getSsoAuthenticateVO(Map<String, String> param) throws LoginInterruptedException;

}
