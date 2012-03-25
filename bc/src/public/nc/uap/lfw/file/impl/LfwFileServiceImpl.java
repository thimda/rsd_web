package nc.uap.lfw.file.impl;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;

import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.file.itf.ILfwFileService;
import nc.uap.lfw.file.vo.LfwFileVO;

public class LfwFileServiceImpl implements ILfwFileService {

	public String add(LfwFileVO vo) throws LfwBusinessException {
		BaseDAO dao = new BaseDAO();
		try {
			return dao.insertVO(vo);
		} catch (DAOException e) {

			LfwLogger.error("文件保存错误!文件PK:" + vo.getPk_lfwfile(), e);
			throw new LfwBusinessException(e);
		}
	}

	public void delete(LfwFileVO vo) throws LfwBusinessException {
		BaseDAO dao = new BaseDAO();
		try {
			dao.deleteVO(vo);
		} catch (DAOException e) {
			LfwLogger.error("文件删除错误!文件PK:" + vo.getPk_lfwfile(), e);
			throw new LfwBusinessException(e);
		}
	}

	public void edit(LfwFileVO vo) throws LfwBusinessException {
		BaseDAO dao = new BaseDAO();
		try {
			dao.updateVO(vo);
		} catch (DAOException e) {

			LfwLogger.error("文件修改错误!文件PK:" + vo.getPk_lfwfile(), e);
			throw new LfwBusinessException(e);
		}
	}

	public void updataVos(LfwFileVO[] vos) throws LfwBusinessException {
		BaseDAO dao = new BaseDAO();
		try {
			dao.updateVOArray(vos);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			LfwLogger.error(e.getMessage(), e);
			throw new LfwBusinessException(e);
		}

	}
}
