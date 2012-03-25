package nc.uap.lfw.crud.impl;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nc.bs.logging.Logger;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.page.LimitSQLBuilder;
import nc.jdbc.framework.page.SQLBuilderFactory;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.jdbc.framework.util.DBConsts;
import nc.jdbc.framework.util.DBUtil;
import nc.jdbc.framework.util.SQLHelper;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.crud.itf.ILfwQueryService;
import nc.uap.lfw.ncadapter.crud.LfwBeanPagedListProcessor;
import nc.uap.lfw.ncadapter.crud.PagedResult;
import nc.uap.lfw.util.LfwClassUtil;
import nc.vo.pub.SuperVO;

public class LfwQueryServiceImpl implements ILfwQueryService {

    private static Map<String, Object> cache = new ConcurrentHashMap<String, Object>();
	@SuppressWarnings("unchecked")
	public <T extends SuperVO>T[] queryVOs(T vo, PaginationInfo pg,
			Map<String, Object> extMap) throws LfwBusinessException {
		ResultSetProcessor rp = null;
		if(pg == null || pg.getPageSize() == -1)
			rp = new BeanListProcessor(vo.getClass());
		else{
			int index = pg.getPageIndex();
			int pageSize = pg.getPageSize();
			int skip = index * pageSize;
			rp = new LfwBeanPagedListProcessor(vo.getClass(), skip, pageSize);
		}
		PersistenceManager pm = null;
		try {
			pm = PersistenceManager.getInstance();
			Object list = (Object) pm.retrieve(vo, true, null, rp);
			if(list instanceof PagedResult){
				PagedResult pr = (PagedResult) list;
				pg.setRecordsCount(pr.getSize());
				return (T[]) pr.getResult().toArray((Object[]) Array.newInstance(vo.getClass(), 0));
			}
			return (T[])((List)list).toArray((Object[]) Array.newInstance(vo.getClass(), 0));
		} 
		catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e.getMessage());
		} 
		finally {
			if (pm != null)
				pm.release();
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public <T extends SuperVO>T[] queryVOs(T vo, PaginationInfo pg, String wherePart,
			Map<String, Object> extMap, String orderByPart) throws LfwBusinessException {
		ResultSetProcessor rp = new BeanListProcessor(vo.getClass());
		PersistenceManager pm = null;
		try {
			pm = PersistenceManager.getInstance();
			JdbcSession ses = pm.getJdbcSession();
			 // 得到插入字段类型的列表
	        Map types = getColmnTypes(vo.getTableName(), ses);
	        // 得到合法的字段列表
	        String sql = null;
	        SQLParameter param = null;
	        if(wherePart != null && !"".equals(wherePart)){
	        	sql = SQLHelper.getSelectSQL(vo.getTableName(), null, true, null);
	        	if(sql.indexOf(" WHERE ") != -1)
	        		sql += " AND (" + wherePart + ")";
	        	else 
	        		sql += " WHERE " + wherePart;
	        }
	        else{
	        	String names[] = getNotNullValidNames(vo, types);
	        	param = getSQLParam(vo, names);
	        	sql = SQLHelper.getSelectSQL(vo.getTableName(), names, true, null);
	        }
	        
	        //sql server不允许字句，在查询总条数时，不加入order
	        String oriSql = sql;
	        if(orderByPart != null && !"".equals(orderByPart)){
	        	if(!orderByPart.trim().startsWith("order ") && !orderByPart.trim().startsWith("ORDER "))
					sql += " ORDER BY ";
	        	sql += orderByPart;
	        }
	        if(pg == null || pg.getPageSize() == -1){
				Object list = ses.executeQuery(sql, param, rp);
				return (T[]) ((List)list).toArray((Object[]) Array.newInstance(vo.getClass(), 0));
			}
			else{
				int index = pg.getPageIndex();
				int pageSize = pg.getPageSize();
				LimitSQLBuilder builder = SQLBuilderFactory.getInstance().createLimitSQLBuilder(pm.getDBType());
				String countSql = "select count(1) as c from (" + oriSql + ") as a";
				sql = builder.build(sql, index + 1, pageSize);
				Map obj = (Map) ses.executeQuery(countSql, param, new MapProcessor());
				Object list = ses.executeQuery(sql, param, rp);
				pg.setRecordsCount((Integer)obj.get("c"));
				return (T[]) ((List)list).toArray((Object[]) Array.newInstance(vo.getClass(), 0));
			}
	        
		} 
		catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e.getMessage());
		} 
		finally {
			if (pm != null)
				pm.release();
		}
	}
	
    /**
     * @param vo
     * @param names
     * @return
     */
    private SQLParameter getSQLParam(SuperVO vo, String[] names) {
        if (names == null || names.length == 0) {
            return null;
        }
        SQLParameter parameter = new SQLParameter();
        for (int i = 0; i < names.length; i++) {
            parameter.addParam(vo.getAttributeValue(names[i]));
        }
        return parameter;
    }
	

    /**
     * @param vo
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
	private String[] getNotNullValidNames(SuperVO vo, Map type) {
        String names[] = vo.getAttributeNames();
        List nameList = new ArrayList();
        for (int i = 0; i < names.length; i++) {
            if (type.get(names[i].toUpperCase()) != null && vo.getAttributeValue(names[i]) != null) {
                nameList.add(names[i]);
            }
        }
        if (nameList.size() == 0)
            return null;
        return (String[]) nameList.toArray(new String[] {});
    }
	
	/**
     * 得到列的类型
     * 
     * @param table
     * @return
     */
    @SuppressWarnings("unchecked")
	private Map<String, Integer> getColmnTypes(String table, JdbcSession ses) {
        Map<String, Integer> result = (Map<String, Integer>) cache.get(table);
        if (result == null) {
            Map<String, Integer> typeMap = new HashMap<String, Integer>();
            ResultSet rsColumns = null;
            try {
                if (ses.getDbType() == DBConsts.SQLSERVER)
                    rsColumns = ses.getMetaData().getColumns(null, null, table.toUpperCase(), "%");
                else
                    rsColumns = ses.getMetaData().getColumns(null, getSchema(ses), table.toUpperCase(), "%");
                while (rsColumns.next()) {
                    String columnName = rsColumns.getString("COLUMN_NAME").toUpperCase();
                    int columnType = rsColumns.getShort("DATA_TYPE");
                    typeMap.put(columnName, columnType);
                }
                cache.put(table, typeMap);
                result = typeMap;
            } catch (SQLException e) {
                Logger.error("get table metadata error", e);
            } finally {
                DBUtil.closeRs(rsColumns);
            }
        }
        return result;
    }
    
    private String getSchema(JdbcSession ses) {
        String strSche = null;
        try {
            String schema = ses.getMetaData().getUserName();
            switch (ses.getDbType()) {
            case DBConsts.SQLSERVER:
                strSche = "dbo";
                break;
            case DBConsts.ORACLE:
            case DBConsts.DB2: {
                if (schema == null || schema.length() == 0)
                    throw new IllegalArgumentException("ORACLE数据库模式不允许为空");
                // ORACLE需将模式名大写
                strSche = schema.toUpperCase();
                break;
            }
            }
        } catch (Exception e) {
            LfwLogger.error(e.getMessage(), e);
        }
        return strSche;
    }
	
    public <T extends SuperVO>T[] queryVOs(String sql, Class<T> clazz,
			PaginationInfo pg, String orderBy, Map<String, Object> extMap) throws LfwBusinessException {
		ResultSetProcessor rp = null;
		PersistenceManager pm = null;
		try {
			pm = PersistenceManager.getInstance();
			JdbcSession ses = pm.getJdbcSession();
			if(!sql.toUpperCase().startsWith("SELECT")){
				SuperVO vo = LfwClassUtil.newInstance(clazz);
				String table = vo.getTableName();
				if(sql.indexOf(".") != -1){
					String prez = sql.substring(0, sql.indexOf("."));
					table += " " + prez;
				}
				sql = "SELECT * FROM " + table + " where " + sql;
			}
			String oriSql = sql;
			if(orderBy != null && !orderBy.equals("")){
				if(!orderBy.trim().startsWith("order ") && !orderBy.trim().startsWith("ORDER "))
					sql += " ORDER BY ";
				sql += orderBy;
			}
			rp = new BeanListProcessor(clazz);
			if(pg == null || pg.getPageSize() == -1){
				Object list = ses.executeQuery(oriSql, rp);
				return (T[]) ((List)list).toArray((Object[]) Array.newInstance(clazz, 0));
			}
			else{
				int index = pg.getPageIndex();
				int pageSize = pg.getPageSize();
				LimitSQLBuilder builder = SQLBuilderFactory.getInstance().createLimitSQLBuilder(pm.getDBType());
				String countSql = "select count(1) as c from (" + oriSql + ") as a";
				sql = builder.build(sql, index + 1, pageSize);
				Map obj = (Map) ses.executeQuery(countSql, new MapProcessor());
				Object list = ses.executeQuery(sql, rp);
				pg.setRecordsCount((Integer)obj.get("c"));
				return (T[]) ((List)list).toArray((Object[]) Array.newInstance(clazz, 0));
			}
			
		} 
		catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e.getMessage());
		} 
		finally {
			if (pm != null)
				pm.release();
		}
    }

	@SuppressWarnings({ "unchecked", "deprecation" })
	public <T extends SuperVO>T[] queryVOs(String sql, Class<T> clazz,
			PaginationInfo pg, Map<String, Object> extMap) throws LfwBusinessException {
		return queryVOs(sql, clazz, pg, null, extMap);
	}

	public Object queryVOs(String sql, ResultSetProcessor processor)
			throws LfwBusinessException {
		PersistenceManager pm = null;
		try {
			pm = PersistenceManager.getInstance();
			JdbcSession ses = pm.getJdbcSession();
			Object result = ses.executeQuery(sql, processor);
			return result;
		} 
		catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e.getMessage());
		} 
		finally {
			if (pm != null)
				pm.release();
		}
	}

}
