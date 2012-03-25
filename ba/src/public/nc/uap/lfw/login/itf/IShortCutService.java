package nc.uap.lfw.login.itf;

import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.login.vo.LfwShortCutVO;

/**
 * ��ݷ�ʽ���·���ӿ�
 * 
 * @author gd
 * @modify zhangxya
 * 
 */
public interface IShortCutService {
	/**
	 * ���¿�ݷ�ʽvo
	 * 
	 * @param shortCutVo
	 * @throws LfwBusinessException
	 */
	public void updateShortCut(LfwShortCutVO shortCutVo)
			throws LfwBusinessException;

	/**
	 * �����ݷ�ʽvo
	 * 
	 * @param shortCutVo
	 * @throws LfwBusinessException
	 */
	public void insertShortCut(LfwShortCutVO shortCutVo)
			throws LfwBusinessException;
	
	/**
	 * ɾ����ݷ�ʽvo
	 * 
	 * @param shortCutVo
	 * @throws LfwBusinessException
	 */
	public void deleteShortCut(LfwShortCutVO shortCutVo)
			throws LfwBusinessException;
}
