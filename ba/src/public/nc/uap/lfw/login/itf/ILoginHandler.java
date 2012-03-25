package nc.uap.lfw.login.itf;


import javax.servlet.http.Cookie;

import nc.uap.lfw.login.listener.AuthenticationUserVO;
import nc.uap.lfw.login.vo.LfwSessionBean;

/**
 * 登录逻辑提供着，供登录协议调用
 * @author dengjt
 *
 */
public interface ILoginHandler <K extends LfwSessionBean>{
	/**
	 * 获取要校验的用户VO,根据具体业务校验需要，将必要信息放置到此VO中
	 */
	public AuthenticationUserVO getAuthenticateVO() throws LoginInterruptedException;
	
	
	/**
	 * 校验用户，并返回用户对应的会话信息对象
	 * @throws LoginInterruptedException 
	 */
	public K doAuthenticate(AuthenticationUserVO userVO) throws LoginInterruptedException;
	
	/**
	 * 获取单点信息实现
	 */
	public ILoginSsoService<K> getLoginSsoService();
	
	
	/**
	 * 实现cookie易用性辅助，根据实际情况，将cookie记录到客户端
	 */
	public Cookie[] getCookies(AuthenticationUserVO userVO);
	
	/**
	 * 获取系统类型。此系统类型可自行约定返回
	 * @return
	 */
	public String getSysType();
	
}
