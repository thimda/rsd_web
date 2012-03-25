package nc.uap.lfw.login.itf;

import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.login.vo.UserConfigVO;

/**
 * �û��������ò�ѯ����ӿ�
 * 
 * @author gd
 * @modify zhangxya
 * 
 */
public interface IUserConfigQueryService {
	/**
	 * ��ѯ�û��������÷���
	 * 
	 * @param userPk
	 * @param sysid
	 * @return
	 * @throws LfwBusinessException
	 */
	public UserConfigVO getUserConfigVO(String userPk, int sysid)
			throws LfwBusinessException;

}
