package nc.uap.lfw.stylemgr.itf;

import nc.uap.lfw.pa.PaBusinessException;
import nc.uap.lfw.stylemgr.vo.UwFuncNodeVO;
/**
 * 用于查询节点的接口
 * @author wupeng1
 * @version 6.0 2011-7-18
 * @since 1.6
 */
public interface IUwFuncnodeQry {
	/**
	 * 根据PK查询UwFuncNodeVO
	 * @param funcnodePk
	 * @return
	 * @throws PaBusinessException
	 */
	public UwFuncNodeVO getUwFuncnodeVOByPk(String funcnodePk) throws PaBusinessException;

}
