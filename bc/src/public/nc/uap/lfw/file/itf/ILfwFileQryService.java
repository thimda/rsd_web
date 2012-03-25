package nc.uap.lfw.file.itf;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.file.vo.LfwFileVO;
/**
 * LFW文件查询接口
 * 
 * @author licza
 * 
 */
public interface ILfwFileQryService {
	/**
	 * 获得文件VO
	 * 
	 * @param filePK 文件主键
	 * @return
	 * @throws LfwBusinessException
	 */
	LfwFileVO getFile(String filePK) throws LfwBusinessException;
	/**
	 * 获得文件VO
	 * 
	 * @param billtype 业务类型
	 * @param billitem 业务标志(PK)
	 * @return
	 * @throws LfwBusinessException
	 */
	LfwFileVO[] getFile(String billtype, String billitem) throws LfwBusinessException;
	/**
	 * 根据sql查询文件
	 * @param sql
	 * @return
	 * @throws LfwBusinessException
	 */
	LfwFileVO[] getFileByFillTypeAndFillItem(String fillTpe, String fillItem) throws LfwBusinessException;
}
