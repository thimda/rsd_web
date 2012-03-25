package nc.uap.lfw.login.itf;

import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.login.vo.LfwShortCutVO;

/**
 * 快捷方式更新服务接口
 * 
 * @author gd
 * @modify zhangxya
 * 
 */
public interface IShortCutService {
	/**
	 * 更新快捷方式vo
	 * 
	 * @param shortCutVo
	 * @throws LfwBusinessException
	 */
	public void updateShortCut(LfwShortCutVO shortCutVo)
			throws LfwBusinessException;

	/**
	 * 插入快捷方式vo
	 * 
	 * @param shortCutVo
	 * @throws LfwBusinessException
	 */
	public void insertShortCut(LfwShortCutVO shortCutVo)
			throws LfwBusinessException;
	
	/**
	 * 删除快捷方式vo
	 * 
	 * @param shortCutVo
	 * @throws LfwBusinessException
	 */
	public void deleteShortCut(LfwShortCutVO shortCutVo)
			throws LfwBusinessException;
}
