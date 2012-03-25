package nc.uap.lfw.login.itf;

import nc.uap.lfw.login.vo.LfwSessionBean;

/**
 * 用于将现有信息存放到公共可访问位置，一遍其它不同ctx的web应用可共享此信息，以达到单点登录的目的。 一般来说此位置通过cookie来实现。
 * 
 * @author dengjt
 */
public interface ILoginSsoService<T extends LfwSessionBean> {
	/**
	 * 将登录信息存放到共享域中
	 * 
	 * @param ssoInfo
	 *            ,登录信息
	 * @param systype
	 *            ,相互能单点登录的系统约定的系统类型
	 */
	public void addSsoSign(T ssoInfo, String systype);

	/**
	 * 回复登录信息
	 * 
	 * @param systype
	 *            ，根据系统类型回复登录信息
	 */
	public T restoreSsoSign(String systype);

	public void afterLogin(LfwSessionBean userVO);

}
