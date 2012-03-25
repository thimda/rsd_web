package nc.uap.lfw.login.itf;

import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.login.vo.LfwShortCutVO;
import nc.vo.sm.funcreg.FuncRegisterVO;

/**
 * ��ݷ�ʽ��ѯ����ӿ�
 * 
 * @author gd
 * @modify zhangxya
 * 
 */
public interface IShortCutQueryService {
	
	
	public LfwShortCutVO[] getShortcutVOs(String pk_corp, String pk_user) throws LfwBusinessException;
	/**
	 * ��ѯ��ݷ�ʽvos
	 * 
	 * @param pk_corp
	 * @param pk_user
	 * @param sysid
	 * @return
	 */
	public LfwShortCutVO[] getShortcutVOs(String pk_corp, String pk_user,
			int sysid) throws LfwBusinessException;

	/**
	 * ��ѯ��ݷ�ʽvos
	 * 
	 * @param pk_corp
	 * @param pk_user
	 * @param fun_code
	 * @param sysid
	 * @return
	 * @throws LfwBusinessException
	 */
	public LfwShortCutVO getShortcutVO(String pk_corp, String pk_user,
			String fun_code, int sysid) throws LfwBusinessException;

	/**
	 * ��ѯ��ݷ�ʽvos
	 * 
	 * @param whereClause
	 * @return
	 * @throws LfwBusinessException
	 */
	public LfwShortCutVO[] getShortcutVOs(String whereClause)
			throws LfwBusinessException;

	/**
	 * ��ѯ��ݷ�ʽvos
	 * 
	 * @param funCode
	 * @return
	 * @throws LfwBusinessException
	 */
	public FuncRegisterVO getFuncRegister(String funCode)
			throws LfwBusinessException;
}
