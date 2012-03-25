package nc.uap.lfw.core.ses;

import nc.uap.lfw.core.exception.LfwBusinessException;

/**
 * WebSession缓存持久化接口
 * @author dengjt
 *
 */
public interface IWebSessionService {
	/**
	 * 持久化会话内容
	 */
	public String persistSesObj(WebSessionVO sesVO) throws LfwBusinessException;
	/**
	 * 更新会话内容
	 * @param sesVO
	 * @throws LfwBusinessException
	 */
	public void updateSesObj(WebSessionVO sesVO) throws LfwBusinessException;
	/**
	 * 删除会话内容
	 * @param sesId
	 * @throws LfwBusinessException
	 */
	public void deleteSesObj(String pk_ses) throws LfwBusinessException;
}
