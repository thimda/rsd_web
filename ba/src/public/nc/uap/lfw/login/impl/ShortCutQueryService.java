package nc.uap.lfw.login.impl;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.login.itf.IShortCutQueryService;
import nc.uap.lfw.login.vo.LfwShortCutVO;
import nc.vo.sm.funcreg.FuncRegisterVO;

/**
 * 快捷方式查询实现类
 * 
 * @author gd
 * @modify zhangxya
 * 
 */
public class ShortCutQueryService implements IShortCutQueryService {
	
	public LfwShortCutVO[] getShortcutVOs(String pk_corp, String pk_user) throws LfwBusinessException{
		BaseDAO baseDAO = new BaseDAO();
		String sql = "select * from uw_shortcut where pk_corp = ? and pk_user = ?  ORDER BY frequency DESC";
		SQLParameter param = new SQLParameter();
		param.addParam(pk_corp);
		param.addParam(pk_user);
		try {
			List<LfwShortCutVO> list = (List<LfwShortCutVO>) baseDAO
					.executeQuery(sql, param, new BeanListProcessor(
							LfwShortCutVO.class));
			return list.toArray(new LfwShortCutVO[0]);
		} catch (DAOException e) {
			throw new LfwBusinessException(e);
		}
	}
	
	/**
	 * 查询快捷方式vo
	 */
	public LfwShortCutVO[] getShortcutVOs(String pk_corp, String pk_user,
			int sysid) throws LfwBusinessException {
		BaseDAO baseDAO = new BaseDAO();
		String sql = "select * from uw_shortcut where pk_corp = ? and pk_user = ? and sysid =? ";
		//ORDER BY frequency DESC
		SQLParameter param = new SQLParameter();
		param.addParam(pk_corp);
		param.addParam(pk_user);
		param.addParam(sysid);
		try {
			List<LfwShortCutVO> list = (List<LfwShortCutVO>) baseDAO
					.executeQuery(sql, param, new BeanListProcessor(
							LfwShortCutVO.class));
			return list.toArray(new LfwShortCutVO[0]);
		} catch (DAOException e) {
			throw new LfwBusinessException(e);
		}
	}

	/**
	 * 查询快捷方式vo
	 */
	public LfwShortCutVO[] getShortcutVOs(String whereClause)
			throws LfwBusinessException {
		if (whereClause == null || whereClause.trim().equals(""))
			whereClause = "1=1";
		BaseDAO baseDAO = new BaseDAO();
		String sql = "select * from uw_shortcut where " + whereClause
				+ " ORDER BY frequency DESC";
		try {
			List<LfwShortCutVO> list = (List<LfwShortCutVO>) baseDAO
					.executeQuery(sql,
							new BeanListProcessor(LfwShortCutVO.class));
			return list.toArray(new LfwShortCutVO[0]);
		} catch (DAOException e) {
			throw new LfwBusinessException(e);
		}
	}

	/**
	 * 查询快捷方式vo
	 */
	public LfwShortCutVO getShortcutVO(String pk_corp, String pk_user,
			String fun_code, int sysid) throws LfwBusinessException {
		BaseDAO baseDAO = new BaseDAO();
		String sql = "select * from uw_shortcut where pk_corp = ? and pk_user = ? and fun_code= ?  and sysid =? ORDER BY frequency DESC";
		SQLParameter param = new SQLParameter();
		param.addParam(pk_corp);
		param.addParam(pk_user);
		param.addParam(fun_code);
		param.addParam(sysid);
		try {
			List<LfwShortCutVO> list = (List<LfwShortCutVO>) baseDAO
					.executeQuery(sql, param, new BeanListProcessor(
							LfwShortCutVO.class));
			if (list.size() > 0)
				return list.get(0);
			return null;
		} catch (DAOException e) {
			throw new LfwBusinessException(e);
		}
	}

	/**
	 * 查询快捷方式vo
	 */
	public FuncRegisterVO getFuncRegister(String funCode)
			throws LfwBusinessException {
//		BaseDAO baseDAO = new BaseDAO();
//		String sql = "select * from sm_funcregister where fun_code = ?";
//		SQLParameter param = new SQLParameter();
//		param.addParam(funCode);
//		try {
//			List<FunctionRegistryVO> list = (List<FunctionRegistryVO>) baseDAO
//					.executeQuery(sql, param, new BeanListProcessor(
//							FunctionRegistryVO.class));
//			if (list.size() > 0)
//				return list.get(0);
//			return null;
//		} catch (DAOException e) {
//			throw new LfwBusinessException(e);
//		}
		return null;
	}
}
