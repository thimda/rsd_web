package nc.uap.lfw.login.itf;

import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.login.vo.UserConfigVO;

/**
 * 用户个性配置服务接口
 * @author gd
 * @modify zhangxya
 *
 */
public interface IUserConfigService {

	/**
	 * 更新用户个性配置服务
	 * 
	 * @param userConfig
	 * @throws LfwBusinessException
	 */
	public void updateUserConfig(UserConfigVO userConfig)
			throws LfwBusinessException;

	/**
	 * 插入用户个性配置服务
	 * 
	 * @param userConfig
	 * @throws LfwBusinessException
	 */
	public void addUserConfig(UserConfigVO userConfig)
			throws LfwBusinessException;

	/**
	 * 删除用户个性配置服务
	 * 
	 * @param pkuserconfig
	 * @throws LfwBusinessException
	 */
	public void deleteUserConfig(String pkuserconfig)
			throws LfwBusinessException;
}
