package nc.uap.lfw.login.itf;

import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.login.vo.LfwSsoRegVO;
import nc.uap.lfw.login.vo.LfwTokenVO;

/**
 * 单点登陆服务类
 * 
 * @author guoweic
 * 
 */
public interface ILfwSsoService {
	/**
	 * 创建登陆信息
	 * 
	 * @param token
	 * @return
	 * @throws LfwBusinessException
	 */
	public String createToken(LfwTokenVO token) throws LfwBusinessException;

	/**
	 * 删除登陆信息
	 * 
	 * @param sessionId
	 * @throws LfwBusinessException
	 */
	public void destoryToken(String sessionId) throws LfwBusinessException;

	/**
	 * 清空登陆信息
	 * 
	 * @throws LfwBusinessException
	 */
	public void destoryAllToken() throws LfwBusinessException;

	/**
	 * 销毁一个登陆信息
	 * 
	 * @param key
	 * @throws LfwBusinessException
	 */
	public void destorySsoInfo(String key) throws LfwBusinessException;

	/**
	 * 清理过期的登陆信息
	 * 
	 * @throws LfwBusinessException
	 */
	public void destoryOverdueSsoInfo() throws LfwBusinessException;

	/**
	 * 增加一个登陆信息
	 * 
	 * @param vo
	 * @return ssokey
	 * @throws LfwBusinessException
	 */
	public String creatSsoInfo(LfwSsoRegVO vo) throws LfwBusinessException;
}
