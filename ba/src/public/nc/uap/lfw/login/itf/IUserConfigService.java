package nc.uap.lfw.login.itf;

import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.login.vo.UserConfigVO;

/**
 * �û��������÷���ӿ�
 * @author gd
 * @modify zhangxya
 *
 */
public interface IUserConfigService {

	/**
	 * �����û��������÷���
	 * 
	 * @param userConfig
	 * @throws LfwBusinessException
	 */
	public void updateUserConfig(UserConfigVO userConfig)
			throws LfwBusinessException;

	/**
	 * �����û��������÷���
	 * 
	 * @param userConfig
	 * @throws LfwBusinessException
	 */
	public void addUserConfig(UserConfigVO userConfig)
			throws LfwBusinessException;

	/**
	 * ɾ���û��������÷���
	 * 
	 * @param pkuserconfig
	 * @throws LfwBusinessException
	 */
	public void deleteUserConfig(String pkuserconfig)
			throws LfwBusinessException;
}
