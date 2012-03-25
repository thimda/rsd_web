package nc.uap.lfw.core.ses;

import nc.uap.lfw.core.exception.LfwBusinessException;

/**
 * WebSession����־û��ӿ�
 * @author dengjt
 *
 */
public interface IWebSessionService {
	/**
	 * �־û��Ự����
	 */
	public String persistSesObj(WebSessionVO sesVO) throws LfwBusinessException;
	/**
	 * ���»Ự����
	 * @param sesVO
	 * @throws LfwBusinessException
	 */
	public void updateSesObj(WebSessionVO sesVO) throws LfwBusinessException;
	/**
	 * ɾ���Ự����
	 * @param sesId
	 * @throws LfwBusinessException
	 */
	public void deleteSesObj(String pk_ses) throws LfwBusinessException;
}
