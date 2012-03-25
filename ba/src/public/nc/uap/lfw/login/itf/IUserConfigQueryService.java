package nc.uap.lfw.login.itf;

import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.login.vo.UserConfigVO;

/**
 * 用户个性配置查询服务接口
 * 
 * @author gd
 * @modify zhangxya
 * 
 */
public interface IUserConfigQueryService {
	/**
	 * 查询用户个性配置服务
	 * 
	 * @param userPk
	 * @param sysid
	 * @return
	 * @throws LfwBusinessException
	 */
	public UserConfigVO getUserConfigVO(String userPk, int sysid)
			throws LfwBusinessException;

}
