package nc.uap.lfw.login.itf;

import java.util.HashMap;

import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.login.vo.LfwTokenVO;

/**
 * �����½��ѯ������
 * 
 * @author guoweic
 * 
 */
public interface ILfwSsoQryService {

	/**
	 * ����token��õ�½���
	 * 
	 * @param tokenId
	 * @return
	 * @throws LfwBusinessException
	 */
	public LfwTokenVO getTokenByID(String tokenID) throws LfwBusinessException;
	
	/**
	 * ���������Ļ�õ�½���
	 * @param context
	 * @return
	 * @throws LfwBusinessException
	 */
	public Integer getOnlineUserCount(String context) throws LfwBusinessException;
	
	/**
	 * ���ݵ�½key��õ�½��Ϣ
	 * @param key
	 * @return
	 * @throws LfwBusinessException
	 */
	public HashMap getUserBySsoKey(String key)throws LfwBusinessException;
}
