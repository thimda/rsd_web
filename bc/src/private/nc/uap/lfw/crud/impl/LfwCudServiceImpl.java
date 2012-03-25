package nc.uap.lfw.crud.impl;

import java.util.Enumeration;
import java.util.Hashtable;

import nc.bs.logging.Logger;
import nc.bs.trade.business.HYSuperDMO;
import nc.jdbc.framework.JdbcPersistenceManager;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.crud.itf.ILfwCudService;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.trade.pub.IExAggVO;

public class LfwCudServiceImpl implements ILfwCudService {

	public AggregatedValueObject[] saveAggVos(AggregatedValueObject[] objs)
			throws LfwBusinessException {
		if(objs != null && objs.length > 0)
		{
			PersistenceManager pm = null;
			try {
				pm = PersistenceManager.getInstance();
				for(int i = 0; i < objs.length; i++)
				{
					SuperVO headVO = (SuperVO) objs[i].getParentVO();
					if(headVO.getStatus() == VOStatus.UPDATED)
						updateAggVO(objs[i], pm);
					else if(headVO.getStatus() == VOStatus.NEW)
						addAggVO(objs[i], pm);
					else
						saveChildren(objs[i], pm);
				}	
			} 
			catch (DbException e) {
				Logger.error(e, e);
				throw new LfwBusinessException(e.getMessage());
			}
			finally {
				if (pm != null)
					pm.release();
			}
		}
		return objs;
	}

	/**
	 * 新增
	 */
	public void addAggVO(AggregatedValueObject aggvo, PersistenceManager pm) throws LfwBusinessException {
		CircularlyAccessibleValueObject headVO = aggvo.getParentVO();
		try {
			String pk = headVO.getPrimaryKey();
			if(pk != null)
				pm.insertWithPK((SuperVO) headVO);
			else{
				pk = pm.insert((SuperVO) headVO);
				headVO.setPrimaryKey(pk);
			}
			headVO.setStatus(Row.STATE_NORMAL);
		} catch (Exception e) {
			Logger.error(e, e);
			throw new LfwBusinessException(e);
		}
		updateChildren(aggvo, pm);
	}
	
	/**
	 * 更新
	 */
	public void updateAggVO(AggregatedValueObject aggvo, PersistenceManager pm) throws LfwBusinessException {
		SuperVO headVO = (SuperVO) aggvo.getParentVO();
		try {
			pm.update(headVO);
			headVO.setStatus(Row.STATE_NORMAL);
		} catch (DbException e) {
			Logger.error(e, e);
			throw new LfwBusinessException(e.getMessage());
		}
		updateChildren(aggvo, pm);
	}
	
	/**
	 * 保存子表
	 * @param aggvo
	 * @param pm
	 * @throws LfwBusinessException
	 */
	public void saveChildren(AggregatedValueObject aggvo, PersistenceManager pm) throws LfwBusinessException {
		updateChildren(aggvo, pm);
	}
	
	private void updateChildren(AggregatedValueObject aggvo, PersistenceManager pm) throws LfwBusinessException {
		CircularlyAccessibleValueObject[] bodyVOs = null;
		if(aggvo instanceof IExAggVO)
			bodyVOs = ((IExAggVO)aggvo).getAllChildrenVO();
		else
			bodyVOs = aggvo.getChildrenVO();
		CircularlyAccessibleValueObject headVO = aggvo.getParentVO();
		
		try {
			String pk = (String) headVO.getPrimaryKey();
			if(bodyVOs != null && bodyVOs.length > 0)
			{
				for(CircularlyAccessibleValueObject vo : bodyVOs){
					SuperVO svo = (SuperVO) vo;
					svo.setAttributeValue(svo.getParentPKFieldName(), pk);
					if(vo.getStatus() == VOStatus.NEW){
						svo.setAttributeValue(svo.getParentPKFieldName(), pk);
						String cpk = pm.insert(svo);
						svo.setPrimaryKey(cpk);
					}
					else if(vo.getStatus() == VOStatus.UPDATED){
						pm.update(svo);
					}
					else if(vo.getStatus() == VOStatus.DELETED){
						pm.delete(svo);
					}
					else{
						continue;
					}
					svo.setStatus(Row.STATE_NORMAL);
				}
			}
		} catch (Exception e) {
			Logger.error(e, e);
			throw new LfwBusinessException(e);
		} 
	}

	public void deleteVo(AggregatedValueObject aggvo, boolean trueDel) throws LfwBusinessException {
        try {
        	HYSuperDMO dmo = new HYSuperDMO();
            SuperVO headVo = (SuperVO) aggvo.getParentVO();
            SuperVO[] items = null;
            if(aggvo instanceof IExAggVO)
            	items = (SuperVO[]) ((IExAggVO)aggvo).getAllChildrenVO();
            else
            	items = (SuperVO[]) aggvo.getChildrenVO();
            if (headVo != null) {
                //删除子表
                deleteItems(dmo, items, headVo.getPKFieldName(), headVo
                        .getPrimaryKey(), trueDel);

                if(trueDel){
                	dmo.delete(headVo);
                }
                
                //删除主表
                else{
	                dmo.updateDr(null, headVo.getTableName(), headVo.getPKFieldName(), headVo.getPrimaryKey());
                }
            } else {
                //删除子表
                deleteBDItems(dmo, items, trueDel);
            }
        }
        catch (Exception e) {
            Logger.error(e.getMessage(), e);
            throw new LfwBusinessException(e);
        }
	}
	
	private void deleteItems(HYSuperDMO dmo, SuperVO[] items, String mainField, String billPk, boolean trueDel) throws Exception 
	{
		if (items != null) 
        {
        	if(trueDel)
        	{
        		for(int i = 0; i < items.length; i++)
        			dmo.delete(items[i]);
        	}
        	else
        	{	
	            //key:tableName,value:Vector
	            Hashtable delDataHas = new Hashtable();
	            Hashtable delkey = new Hashtable();

	            for (int i = 0; i < items.length; i++) {
	                String tableName = items[i].getTableName();
	                if (!delDataHas.containsKey(tableName)) {
	                    delDataHas.put(tableName, tableName);
	                    if (items[i].getParentPKFieldName() != null)
	                        delkey.put(tableName, items[i].getParentPKFieldName());
	                    else
	                        delkey.put(tableName, mainField);
	                }
	            }

	            //更新处理
	            Enumeration en = delDataHas.elements();
	            while (en.hasMoreElements()) {
	                String tableName = (String) en.nextElement();
	                String keyField = (String) delkey.get(tableName);
	                dmo.updateDr(null, tableName, keyField, billPk);
	            }
        	}
        }
	}
	 
	private void deleteBDItems(HYSuperDMO dmo, SuperVO[] items, boolean trueDel) throws Exception 
	{
		if (items != null) 
		{
			if(trueDel)
			{
				for (int i = 0; i < items.length; i++) 
					dmo.delete(items[i]);
			}
			else
			{
				for (int i = 0; i < items.length; i++) 
			    {
					String tableName = items[i].getTableName();
			    	String mainField = items[i].getPKFieldName();
			        String billPk = items[i].getPrimaryKey();
			        dmo.updateDr(null, tableName, mainField, billPk);
			    }
			}
		}
	}

	public int executeUpdate(String sql, SQLParameter parameter)
			throws LfwBusinessException {
		PersistenceManager manager = null;
		int value;
		try {
			manager = JdbcPersistenceManager.getInstance();
			JdbcSession session = manager.getJdbcSession();
			value = session.executeUpdate(sql, parameter);

		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
		return value;
	}

	public int executeUpdate(String sql) throws LfwBusinessException {
		PersistenceManager manager = null;
		int value;
		try {
			manager = JdbcPersistenceManager.getInstance();
			JdbcSession session = manager.getJdbcSession();
			value = session.executeUpdate(sql);

		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e.getMessage());
		} 
		finally {
			if (manager != null)
				manager.release();
		}
		return value;
	}

	public void deleteVo(SuperVO vo) throws LfwBusinessException {
		PersistenceManager manager = null;
		try {
			manager = JdbcPersistenceManager.getInstance();
			manager.delete(vo);

		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e.getMessage());
		} 
		finally {
			if (manager != null)
				manager.release();
		}
	}

	
	public void deleteVos(SuperVO[] vos) throws LfwBusinessException{
		PersistenceManager manager = null;
		try {
			manager = JdbcPersistenceManager.getInstance();
			manager.delete(vos);

		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e.getMessage());
		} 
		finally {
			if (manager != null)
				manager.release();
		}
	}
}
