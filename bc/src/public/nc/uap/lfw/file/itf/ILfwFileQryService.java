package nc.uap.lfw.file.itf;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.file.vo.LfwFileVO;
/**
 * LFW�ļ���ѯ�ӿ�
 * 
 * @author licza
 * 
 */
public interface ILfwFileQryService {
	/**
	 * ����ļ�VO
	 * 
	 * @param filePK �ļ�����
	 * @return
	 * @throws LfwBusinessException
	 */
	LfwFileVO getFile(String filePK) throws LfwBusinessException;
	/**
	 * ����ļ�VO
	 * 
	 * @param billtype ҵ������
	 * @param billitem ҵ���־(PK)
	 * @return
	 * @throws LfwBusinessException
	 */
	LfwFileVO[] getFile(String billtype, String billitem) throws LfwBusinessException;
	/**
	 * ����sql��ѯ�ļ�
	 * @param sql
	 * @return
	 * @throws LfwBusinessException
	 */
	LfwFileVO[] getFileByFillTypeAndFillItem(String fillTpe, String fillItem) throws LfwBusinessException;
}
