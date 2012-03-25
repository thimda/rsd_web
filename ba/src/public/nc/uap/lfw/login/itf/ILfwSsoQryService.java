package nc.uap.lfw.login.itf;

import java.util.HashMap;

import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.login.vo.LfwTokenVO;

/**
 * 单点登陆查询服务类
 * 
 * @author guoweic
 * 
 */
public interface ILfwSsoQryService {

	/**
	 * 根据token获得登陆标记
	 * 
	 * @param tokenId
	 * @return
	 * @throws LfwBusinessException
	 */
	public LfwTokenVO getTokenByID(String tokenID) throws LfwBusinessException;
	
	/**
	 * 根据上下文获得登陆标记
	 * @param context
	 * @return
	 * @throws LfwBusinessException
	 */
	public Integer getOnlineUserCount(String context) throws LfwBusinessException;
	
	/**
	 * 根据登陆key获得登陆信息
	 * @param key
	 * @return
	 * @throws LfwBusinessException
	 */
	public HashMap getUserBySsoKey(String key)throws LfwBusinessException;
}
