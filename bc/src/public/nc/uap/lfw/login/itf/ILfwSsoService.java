package nc.uap.lfw.login.itf;

import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.login.vo.LfwSsoRegVO;
import nc.uap.lfw.login.vo.LfwTokenVO;

/**
 * �����½������
 * 
 * @author guoweic
 * 
 */
public interface ILfwSsoService {
	/**
	 * ������½��Ϣ
	 * 
	 * @param token
	 * @return
	 * @throws LfwBusinessException
	 */
	public String createToken(LfwTokenVO token) throws LfwBusinessException;

	/**
	 * ɾ����½��Ϣ
	 * 
	 * @param sessionId
	 * @throws LfwBusinessException
	 */
	public void destoryToken(String sessionId) throws LfwBusinessException;

	/**
	 * ��յ�½��Ϣ
	 * 
	 * @throws LfwBusinessException
	 */
	public void destoryAllToken() throws LfwBusinessException;

	/**
	 * ����һ����½��Ϣ
	 * 
	 * @param key
	 * @throws LfwBusinessException
	 */
	public void destorySsoInfo(String key) throws LfwBusinessException;

	/**
	 * ������ڵĵ�½��Ϣ
	 * 
	 * @throws LfwBusinessException
	 */
	public void destoryOverdueSsoInfo() throws LfwBusinessException;

	/**
	 * ����һ����½��Ϣ
	 * 
	 * @param vo
	 * @return ssokey
	 * @throws LfwBusinessException
	 */
	public String creatSsoInfo(LfwSsoRegVO vo) throws LfwBusinessException;
}
