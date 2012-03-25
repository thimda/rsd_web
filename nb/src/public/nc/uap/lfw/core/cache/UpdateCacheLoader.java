package nc.uap.lfw.core.cache;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nc.bs.dbcache.exception.DBCacheException;
import nc.bs.dbcache.intf.IDBCacheBS;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.dbcache.config.TableInfo;
import nc.vo.dbcache.query.MatchCriteria;
import nc.vo.dbcache.query.QueryClause;
import nc.vo.dbcache.query.Table;

public class UpdateCacheLoader extends AbstractCacheLoader {
	private Object[] updatedDataPKs = null;
	public UpdateCacheLoader(String oldDataTableVersion) {
		super(oldDataTableVersion);
	}

	@SuppressWarnings("unchecked")
	public Set<String> getNeedDeletePKs(TableInfo tableInfo) {
		if (updatedDataPKs == null) {
			updatedDataPKs = getUpdatedDataPKs(tableInfo, this.oldDataTableVersion);
		}
		if (updatedDataPKs != null && updatedDataPKs.length > 1) {
			Set<String> needDeleteDataPKs = new HashSet<String>();
			// 后台删除的和更新过的数据，在前台都要删除。
			// 后台更新过的数据，在前台删除后，要重新下载。
			Collection<String> udp0 = (Collection<String>) updatedDataPKs[0];
			Collection<String> udp1 = (Collection<String>) updatedDataPKs[1];
			needDeleteDataPKs.addAll(udp0);
			needDeleteDataPKs.addAll(udp1);
			return needDeleteDataPKs;
		}
		return null;
	}
	
	public List<String> getNeedLoadPKs(TableInfo tableInfo) {
		if (updatedDataPKs == null) {
			updatedDataPKs = getUpdatedDataPKs(tableInfo, this.oldDataTableVersion);
		}
		if (updatedDataPKs != null && updatedDataPKs.length > 1) {
			List<String> needReloadDataPKs = (List<String>) updatedDataPKs[0];
			return needReloadDataPKs;
		}
		return null;
	}

	private Object[] getUpdatedDataPKs(TableInfo tableInfo, String oldVersion) {
		String sql1 = getNeedReloadPKsSql(tableInfo, tableInfo.getName(), oldVersion);
		Logger.debug("fetch need reload pks sql:" + sql1);

		String pkName = tableInfo.getPrimaryKey();
		String sql2 = getNeedDeletePKsSql(tableInfo.getName(), oldVersion);
		Logger.debug("fetch need delete pks sql:" + sql2);

		String[] sqls = new String[] { sql1, sql2 };
		ResultSetProcessor[] processors = new ResultSetProcessor[] {
				new ColumnListProcessor(pkName),
				new ColumnListProcessor("pk_value") };
		Object[] result = null;
		try {
			result = NCLocator.getInstance().lookup(IDBCacheBS.class)
					.runSQLQuery(sqls, processors);
		} catch (Exception be) {
			String msg = "远程获取变化数据主键错误, sql： " + sql1 + ";\n" + sql2;
			Logger.error(msg);
			throw new DBCacheException(msg, be);
		}
		return result;
	}

	private String getNeedReloadPKsSql(TableInfo tableInfo, String tname, String version) {
		Table table = new Table(tname);
		QueryClause clause = new QueryClause(table);
		clause.addColumn(table, tableInfo.getPrimaryKey());
		clause.addCriteria(new MatchCriteria(table, "ts",
				MatchCriteria.GREATEREQUAL, version));
		clause = addFilterSQL(tableInfo, clause);
		return clause.getSQL();
	}
	

	// 得到后台已经删除的数据的主键集合
	private String getNeedDeletePKsSql(String tableName, String oldTs) {
		StringBuffer sqlBuilder = new StringBuffer();
		sqlBuilder.append("SELECT pk_value FROM bd_del_log where tablename='")
				.append(tableName).append("' AND ").append(" ts>='").append(
						oldTs).append("'");
		return sqlBuilder.toString();

	}

}
