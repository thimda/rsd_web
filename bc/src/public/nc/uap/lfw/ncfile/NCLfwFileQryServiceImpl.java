package nc.uap.lfw.ncfile;

import java.io.IOException;

import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.file.vo.LfwFileVO;
import sun.misc.BASE64Decoder;

public class NCLfwFileQryServiceImpl implements INCLfwFileQryService {

	public LfwFileVO getFile(String filePK) throws LfwBusinessException {
		LfwFileVO fileVo = new LfwFileVO();
		byte[] bytes = null;
		try {
			bytes = new BASE64Decoder().decodeBuffer(filePK);
			fileVo.setFilename(new String(bytes,"gbk"));
		} 
		catch (IOException e1) {
			LfwLogger.error(e1.getMessage(), e1);
		}
		return fileVo;
	}

	public LfwFileVO[] getFile(String billtype, String billitem)
			throws LfwBusinessException {
		return null;
	}

	public LfwFileVO[] getFileByFillTypeAndFillItem(String fillTpe,
			String fillItem) throws LfwBusinessException {
		return null;
	}

}
