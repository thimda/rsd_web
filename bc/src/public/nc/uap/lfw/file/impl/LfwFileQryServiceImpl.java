package nc.uap.lfw.file.impl;

import java.util.ArrayList;
import java.util.List;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.file.itf.ILfwFileQryService;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.vo.pub.SuperVO;
import org.apache.commons.lang.StringUtils;

public class LfwFileQryServiceImpl implements ILfwFileQryService {

	public LfwFileVO getFile(String filePK) throws LfwBusinessException {
		BaseDAO dao = new BaseDAO();
		try {
			return (LfwFileVO) dao.retrieveByPK(LfwFileVO.class, filePK);
		} catch (DAOException e) {

			LfwLogger.error("文件查询错误!文件PK:" + filePK, e);
			throw new LfwBusinessException(e);
		}

	}

	public LfwFileVO[] getFile(String billtype, String billitem)
			throws LfwBusinessException {
		BaseDAO dao = new BaseDAO();
		try {
			StringBuffer sb = new StringBuffer();
			
			LfwFileVO vo = new LfwFileVO();
			if (StringUtils.isNotBlank(billitem)) {
				sb.append(" pk_billitem = '"+billitem+"' order by ts desc ");
			}
			
			List<? extends SuperVO> l = (List<? extends SuperVO>)dao.retrieveByClause(LfwFileVO.class, sb.toString());
			return l.isEmpty() ? null : ((LfwFileVO[]) l
					.toArray(new LfwFileVO[0]));
		} catch (DAOException e) {
			LfwLogger.error("文件查询错误! ", e);
			throw new LfwBusinessException(e);
		}

	}

	public LfwFileVO[] getFileByFillTypeAndFillItem(String fillTpe,
			String fillItem) throws LfwBusinessException {
		BaseDAO dao = new BaseDAO();
		LfwFileVO[] vos = null;
		String sql = "select * from uw_lfwfile where pk_billtype = ? and pk_billitem = ?";
		SQLParameter param = new SQLParameter();
		param.addParam(fillTpe);
		param.addParam(fillItem);
		try {
			List list = (List) dao.executeQuery(sql, param,
					new BeanListProcessor(LfwFileVO.class));
			vos = (LfwFileVO[]) list.toArray(new LfwFileVO[0]);
		} catch (DAOException e1) {
			LfwLogger.error(e1.getMessage(), e1);
			throw new LfwBusinessException(e1.getMessage());
		}
		return vos;
	}

	public LfwFileVO[] getFileByCondition(String sql)
			throws LfwBusinessException {
		return null;
	}

}
