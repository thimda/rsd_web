package nc.uap.lfw.core.cache;

import java.util.List;
import java.util.Set;

import nc.bs.dbcache.exception.DBCacheException;
import nc.bs.dbcache.intf.IDBCacheBS;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.vo.dbcache.config.TableInfo;
import nc.vo.dbcache.query.Column;
import nc.vo.dbcache.query.QueryClause;
import nc.vo.dbcache.query.Table;


public class FullInitCacheLoader extends AbstractCacheLoader {

	public FullInitCacheLoader(String oldVersion) {
		super(oldVersion);
	}

	public Set<String> getNeedDeletePKs(TableInfo tableInfo) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<String> getNeedLoadPKs(TableInfo tableInfo) {
		Table t = new Table(tableInfo.getName());
		QueryClause clause = new QueryClause(t);
		clause.addColumn(new Column(t, tableInfo.getPrimaryKey()));
		// 解析filterSQL并接到获取PK的SQL上
		addFilterSQL(tableInfo, clause);
		String sql = clause.getSQL();
		// ToDo:
		Logger.debug("远程获取数据主键的SQL：" + sql);
		String pkName = tableInfo.getPrimaryKey();
		List<String> result = null;
		try {
			IDBCacheBS dbcachebs = NCLocator.getInstance().lookup(
					IDBCacheBS.class);
			result = (List<String>) dbcachebs.runSQLQuery(sql,
					new ColumnListProcessor(pkName));
		} catch (Exception e) {
			String msg = "远程获取数据主键错误, sql： " + sql;
			Throwable tr  = e.getCause();
			while(tr != null&& tr.getCause() != null){
				tr = tr.getCause();
			}
			if(tr!= null){
				msg = msg + "\n" + tr.getMessage();
			}
			Logger.error(msg);
			throw new DBCacheException(msg,e);
		}
		return result;
	}

}
