package nc.uap.lfw.file.itf;

import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.file.vo.LfwFileVO;

/**
 * LFW文件操作接口
 * 
 * @author licza
 * 
 */
public interface ILfwFileService {
	String add(LfwFileVO vo) throws LfwBusinessException;

	void delete(LfwFileVO vo) throws LfwBusinessException;

	void edit(LfwFileVO vo) throws LfwBusinessException;
	
	void updataVos(LfwFileVO[] vos) throws LfwBusinessException;
	
}
