package nc.uap.lfw.core.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nc.bs.dbcache.exception.DBCacheException;
import nc.bs.logging.Logger;
import nc.uap.lfw.compatible.NCCacheUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.vo.dbcache.config.ColumnInfo;
import nc.vo.dbcache.config.TableInfo;
import nc.vo.dbcache.dbinfo.DBInfo;
import nc.vo.dbcache.load.LoadRequest;
import nc.vo.dbcache.load.LoadResponse;
import nc.vo.dbcache.query.Column;
import nc.vo.dbcache.query.Criteria;
import nc.vo.dbcache.query.InCriteria;
import nc.vo.dbcache.query.MatchCriteria;
import nc.vo.dbcache.query.QueryClause;
import nc.vo.dbcache.query.Table;
import nc.vo.dbcache.utils.FilterSqlAnalyzer;
import nc.vo.pub.BusinessException;

public abstract class AbstractCacheLoader implements IClientCacheLoader {
	protected String oldDataTableVersion;
	public AbstractCacheLoader(String oldVersion) {
		super();
		this.oldDataTableVersion = oldVersion;
	}


	public TableJsonContent loadData(TableInfo tableInfo, boolean force, String oldVersion, String newVersion, DBInfo dbInfo) {
		String tableName = tableInfo.getName().toLowerCase();
		// 如果不是强制下载，检查版本，如果未发生变化，退出。
		if (!force) {
			if (oldVersion != null && !oldVersion.equals("-1") && oldVersion.equals(newVersion)) {
				// 版本相同，无需下载
				String message = tableName + "表的版本未发生变化，无需刷新！";
				Logger.debug(message);
			}
		}
		
		TableJsonContent json = null;
		try {
			Set<String> delPks = getNeedDeletePKs(tableInfo);
			List<String> pks = getNeedLoadPKs(tableInfo);
			if (delPks != null && delPks.size() > 0) {
				if(json == null)
					json = new TableJsonContent();
				processDelPks(json, delPks);
			}
			if (pks != null && pks.size() > 0) {
				if(json == null)
					json = new TableJsonContent();
				loadPkDatas(json, tableInfo, pks, dbInfo);
			}
		} 
		catch (Exception e) {
			String msg = "出现异常!下载表" + tableInfo.getName() + "数据失败！";
			Logger.error(msg, e);
			throw new DBCacheException(e);
		} 
		
		return json;
	}


	protected void processDelPks(TableJsonContent json, Set<String> delPks) {
		if(delPks != null && delPks.size() > 0){
			DeleteData del = new DeleteData();
			del.setDelPks(delPks.toArray(new String[0]));
			json.setDelete(del);
		}
	}
	
	protected void loadPkDatas(TableJsonContent json, TableInfo tableInfo, List<String> pks, DBInfo dbInfo) throws DBCacheException {
		String msg = "该表一共需要下载" + pks.size() + "条记录";
		int length = pks.size();
		if(length == 0)
			return;
		int pageSize = (length+999)/1000;
		Logger.debug(msg);
		Logger.debug("该页需远程获得记录：" + pks.size() + "条");
		TableData data = new TableData();
		List<Object[]> listData = new ArrayList<Object[]>();
		for(int i = 0; i < pageSize; i++){
			int start = i * 1000;
			int end = length;
			if(i < pageSize-1)
				end = (i+1) * 1000;

			List<String> aPage = pks.subList(start, end);
			LoadRequest request = getRequest(tableInfo, aPage, dbInfo);
			try {
				LoadResponse response = NCCacheUtil.loadData(request);
				if(i == 0){
					TableMeta meta = new TableMeta();
					meta.setMetas((String[]) ((List)response.get(LoadResponse.ROW_NAMES)).toArray(new String[0]));
					json.setMeta(meta);
				}
				List<Object[]> list = (List<Object[]>) response.get(LoadResponse.PAGE_DATA);
				listData.addAll(list);
			} 
			catch (BusinessException e) {
				String errMsg = "远程获取数据出错！";
				Logger.error(errMsg, e);
				throw new DBCacheException(e);
			}
		}
		data.setData(listData);
		json.setData(data);
	}
	
	protected LoadRequest getRequest(TableInfo tableInfo, List<String> pks,
			DBInfo dbInfo) {
		String tn = tableInfo.getName();
		String pk = tableInfo.getPrimaryKey();
		List<String> columns = new ArrayList<String>();
		List<ColumnInfo> cis = tableInfo.getColumns();
		for (ColumnInfo ci : cis) {
			columns.add(ci.getName());
		}
		
		LoadRequest request = new LoadRequest();
		request.setPrimaryKey(pk);
		request.setColumns(columns);
		request.setPageSize(100000);
		request.setTablename(tn);
		Table table = new Table(tableInfo.getName());
		
		
		QueryClause clause = new QueryClause(table);
		clause.addColumns(table, columns.toArray(new String[0]));
		
		Column col = new Column(table, pk);
		InCriteria inc = new InCriteria(col, pks);
		clause.addCriteria(inc);
		request.setLoadClause(clause);
		request.setCountClause(clause);
		
		return request;
	}
	
	
	/**
	 * 如果按集团过滤，则加入集团过滤条件，如果配置了filterSQL变量,解析该变量，然后加入过滤条件
	 * 
	 * @param clause
	 * @return
	 */
	protected QueryClause addFilterSQL(TableInfo tableInfo, QueryClause clause) {
		if (tableInfo.getGroupColumn() != null) {
			String groupName = tableInfo.getGroupColumn();
			Table table = new Table(tableInfo.getName());
			LfwSessionBean ses = (LfwSessionBean) LfwRuntimeEnvironment.getLfwSessionBean();
			String groupId = "~";
			if(ses != null){
				groupId = ses.getPk_unit();
			}
			clause.addCriteria(new MatchCriteria(table, groupName,
					Criteria.EQUALS, groupId));
		}
		String filterSql = tableInfo.getFilterSql();
		if (filterSql != null && filterSql.trim().length() > 0) {
			try {
				clause = FilterSqlAnalyzer.getInstance().addFilter(clause, filterSql);
				clause = FilterSqlAnalyzer.getInstance().addProviderFilter(clause, tableInfo.getName());
			} 
			catch (DBCacheException e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return clause;
	}

	protected String addFilterSQL(TableInfo tableInfo, String sql) {
		if (sql == null) {
			return null;
		}
		if (tableInfo.getGroupColumn() != null) {
			String groupName = tableInfo.getGroupColumn();
			String pkUnit = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();
			if (sql.toLowerCase().indexOf(" where ") > 0) {
				sql = sql + " AND " + groupName + "='" + pkUnit + "'";
			} else {
				sql = sql + " WHERE " + groupName + "='" + pkUnit
						+ "'";
			}
		}
		String filterSql = tableInfo.getFilterSql();
		if (filterSql != null && filterSql.trim().length() > 0) {
			sql = FilterSqlAnalyzer.getInstance().addFilter(sql, filterSql);
		}
		return sql;
	}
}
