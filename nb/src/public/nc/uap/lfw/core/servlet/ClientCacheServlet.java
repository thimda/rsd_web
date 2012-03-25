package nc.uap.lfw.core.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bcmanage.bs.IBusiCenterManageService;
import nc.bcmanage.vo.BusiCenterVO;
import nc.bs.dbcache.exception.DBCacheException;
import nc.bs.dbcache.intf.ICacheVersionBS;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.execute.Executor;
import nc.bs.logging.Logger;
import nc.uap.lfw.compatible.NCCacheUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cache.CacheDownloadResponse;
import nc.uap.lfw.core.cache.FullInitCacheLoader;
import nc.uap.lfw.core.cache.IClientCacheLoader;
import nc.uap.lfw.core.cache.TableJsonCache;
import nc.uap.lfw.core.cache.TableJsonContent;
import nc.uap.lfw.core.cache.UpdateCacheLoader;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.serializer.impl.LfwJsonSerializer;
import nc.vo.dbcache.config.CacheStrategy;
import nc.vo.dbcache.config.ConfigUtils;
import nc.vo.dbcache.config.DBCacheConfig;
import nc.vo.dbcache.config.SysSql;
import nc.vo.dbcache.config.TableInfo;
import nc.vo.dbcache.dbinfo.DBInfo;
import nc.vo.dbcache.utils.DatabaseUtils;
import nc.vo.dbcache.utils.FilterSqlAnalyzer;
import nc.vo.pub.BusinessException;

import org.apache.commons.lang.StringUtils;

/**
 * 客户端缓存。
 * @author ybo
 * @version 6.0 2011-6-16
 * @since 1.6
 */
public class ClientCacheServlet extends LfwServletBase {
	/**
	 * 序列id
	 */
	private static final long serialVersionUID = 2173363872477751885L;
	
	/**
	 * 客户端缓存配置信息
	 */
	private DBCacheConfig dbCacheConfig;
	
	/**
	 * 数据库信息
	 */
	private DBInfo dbinfo = null;
	
	/**
	 * 放入ServletContext中的缓存版本key,由于加入多数据源,则只设置第一个数据源
	 * @deprecated
	 */
	public static final String CACHE_VERSION_MAP = "CACHE_VERSION_MAP";
	
	/**
	 * 放入ServletContext中的json缓存版本key,由于加入多数据源,则只设置第一个数据源
	 * @deprecated
	 */
	public static final String CACHE_VERSION_MAP_JSON = "CACHE_VERSION_MAP_JSON";
	
	/**
	 * 多数据数据源的版本map
	 */
	private Map<String,Map<String,String>> versionMap = null;
	
	/**
	 * 数据源名列表
	 */
	private String[] datasources = null;
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try{
			// 客户端缓存的配置文件（包含表结构等信息）
			String filePath = getServletContext().getRealPath("/") + "WEB-INF/conf/cacheconfig.xml";
			File f = new File(filePath);
			if(f.exists()){
				LfwLogger.debug("加载缓存文件:" + filePath);
				dbCacheConfig = ConfigUtils.loadCacheConfig(f);
				// 设置客户端缓存启用标志
				getServletContext().setAttribute("CLIENT_CACHE", "1");
				versionMap = new HashMap<String,Map<String,String>>();
				String[] datasources = getDataSources();
				for(String ds : datasources){
					versionMap.put(ds, new HashMap<String,String>());
				}
				//加载版本信息
				startLoadVersion();
				
	            List<String> vts = dbCacheConfig.getVarTranslaters();
	            FilterSqlAnalyzer.getInstance().registTranslaterSilently(vts);
	            
	            List<String> plugins = dbCacheConfig.getFilterPlugins();
	            FilterSqlAnalyzer.getInstance().registPluginsSilently(plugins);
			}
			else{
				LfwLogger.debug("缓存文件:" + filePath + " 不存在");
			}
		}
		catch(Throwable e){
			LfwLogger.error(e.getMessage(), e);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		//获取请求数据源
		String datasource = req.getParameter("ds");
		if(StringUtils.isEmpty(datasource)){
			datasource = LfwRuntimeEnvironment.getDatasource();
		}
		if(datasource == null){
			datasource = getDataSources()[0];
		}
		InvocationInfoProxy.getInstance().setUserDataSource(datasource);
		
		fetchTableDefines();
		
		String type = req.getParameter("type");
		if(type == null)
			type = "fullinit";
		if(type.equals("fullinit")){
			doFullInit(req, resp);
		}
		else if(type.equals("update")){
			doUpdate(req, resp);
		}
	}

	private void doUpdate(HttpServletRequest req, HttpServletResponse resp) {
		
		List<TableInfo> infos = dbCacheConfig.getTableInfos();
		if (infos == null || infos.size() < 1) {
			return;
		}
		try {
			//获取请求的版本
			String versionObj = req.getParameter("tableVersion");
			if(versionObj == null || versionObj.equals(""))
				return;
			String datasource = InvocationInfoProxy.getInstance().getUserDataSource();
			String[] versionPairs = versionObj.split(";");
			Map<String, String> oldVMap = new HashMap<String, String>();
			for (int i = 0; i < versionPairs.length; i++) {
				String[] pair = versionPairs[i].split(",");
				oldVMap.put(pair[0], pair[1]);
			}
			CacheDownloadResponse cacheResp = new CacheDownloadResponse();
			//获取数据源对应的版本信息
			Map<String,String> dsVersions = getVersionMap().get(datasource);
			for (int i = 0; i < infos.size(); i++) {
				TableInfo tableInfo = infos.get(i);
				String version = oldVMap.get(tableInfo.getName());
				if(version == null)
					continue;
				IClientCacheLoader loader = new UpdateCacheLoader(version);
				try {
					TableJsonCache tableCache = new TableJsonCache();
					tableCache.setVersion(dsVersions.get(tableInfo.getName()));
					tableCache.setPkField(tableInfo.getPrimaryKey());
					tableCache.setTableName(tableInfo.getName());
					TableJsonContent jsonContent = loader.loadData(tableInfo, true, version, tableCache.getVersion(), dbinfo);
					tableCache.setContent(jsonContent);
					cacheResp.addTableJsonCache(tableCache);
				} 
				catch (DBCacheException e) {
					throw e;
				}
			}
			resp.getWriter().write(cacheResp.toResponse());
		} 
		catch (Exception exp) {
			Logger.error(exp.getMessage(), exp);
		}
	}

	/**
	 * 获取全部需缓存的表结构和表数据
	 * @param req
	 * @param resp
	 */
	private void doFullInit(HttpServletRequest req, HttpServletResponse resp) {
		
		SysSql headSql = dbCacheConfig.getHeadSql();
		if (headSql != null && headSql.getSql() != null && headSql.getSql().trim().length() > 0) {
		}
		List<TableInfo> infos = dbCacheConfig.getTableInfos();
		if (infos == null || infos.size() < 1) {
			return;
		}

		try {
			
			CacheDownloadResponse cacheResp = new CacheDownloadResponse();
			//获取数据源对应的版本信息
			String datasource = InvocationInfoProxy.getInstance().getUserDataSource();
			Map<String,String> dsVersions = getVersionMap().get(datasource);
			for (int i = 0; i < infos.size(); i++) {
				TableInfo tableInfo = infos.get(i);
				if(LfwRuntimeEnvironment.isClientMode()){
					TableInfo copy = new TableInfo();
					copy.setColumns(tableInfo.getColumns());
					copy.setDataTable(tableInfo.getDataTable());
					copy.setName(tableInfo.getName());
					copy.setPrimaryKey(tableInfo.getPrimaryKey());
					copy.setStrategy(CacheStrategy.FULL);
					tableInfo = copy;
				}
				IClientCacheLoader loader = new FullInitCacheLoader("-1");
				try {
					TableJsonCache tableCache = new TableJsonCache();
					tableCache.setVersion(dsVersions.get(tableInfo.getName()));
					tableCache.setPkField(tableInfo.getPrimaryKey());
					String ddlSql = dbinfo.getTableDDL(tableInfo, DatabaseUtils.HSQL);
					tableCache.setScheme(ddlSql);
					tableCache.setTableName(tableInfo.getName());
					TableJsonContent jsonContent = loader.loadData(tableInfo, true, "-1", tableCache.getVersion(), dbinfo);
					tableCache.setContent(jsonContent);
					cacheResp.addTableJsonCache(tableCache);
				} 
				catch (DBCacheException e) {
					throw e;
				}
			}
			resp.getWriter().write(cacheResp.toResponse());
		} 
		catch (Exception exp) {
			Logger.error(exp.getMessage(), exp);
		}
	}

	
	/**
	 * 获取建表信息
	 * 
	 * @param tablenames
	 * @return
	 */
	public void fetchTableDefines() {
//		IDBCacheBS dbCacheBs = NCLocator.getInstance().lookup(IDBCacheBS.class);
		Collection<String> needLoads = new HashSet<String>();
		if (dbCacheConfig != null) {
			List<TableInfo> tis = dbCacheConfig.getTableInfos();
			if (tis != null && tis.size() > 0) {
				for (TableInfo ti : tis) {
					if (ti.isCommonDocTable()) {
						needLoads.add(ti.getDataTable());
					} else {
						needLoads.add(ti.getName());
					}
				}
			}
		}
		
		if(needLoads.size() > 0){
			DBInfo temp = null;
			try {
				temp = NCCacheUtil.getServerDBInfo(needLoads);
			} 
			catch (BusinessException e) {
				Logger.error("load server database schema infomation error!", e);
			}
			if (dbinfo != null && temp != null) {
				for (String key : temp.getDbinfoMap().keySet()) {
					dbinfo.addTableDefine(key, temp.getDbinfoMap().get(key));
				}
			} 
			else if (dbinfo == null) {
				dbinfo = temp;
			}
		}
	}
	
	/**
	 * 启动版本加载线程
	 */
	private void startLoadVersion() {
		getDataSources();
		if(datasources == null || datasources.length == 0)
			return;
		VersionThread t = new VersionThread();
		Executor tr = new Executor(t);
		tr.start();
	}
	
	/**
	 * 缓存版本处理线程。
	 * @version 6.0 2011-6-16
	 * @since 1.6
	 */
	private class VersionThread implements Runnable{
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			while(true){
				Set<String> errDs = new HashSet<String>();
				for(String ds:datasources){
					Map<String,String> dsVersions = getVersionMap().get(ds);
					try{
						//设置数据源
						InvocationInfoProxy.getInstance().setUserDataSource(ds);
						List<TableInfo> infos = dbCacheConfig.getTableInfos();
						int size = infos.size();
						String[] names = new String[size];
						for (int i = 0; i < size; i++) {
							names[i] = infos.get(i).getName();
						}
						ICacheVersionBS cacheBs = NCLocator.getInstance().lookup(ICacheVersionBS.class);
						String[] versions = cacheBs.getVersions(names);
						synchronized (dsVersions) {
							dsVersions.clear();
							for (int i = 0; i < versions.length; i++) {
								dsVersions.put(names[i], versions[i]);
							}
						}
					}
					catch(nc.vo.pub.BusinessRuntimeException e){
						errDs.add(ds);
						LfwLogger.error(e.getMessage(), e);
					} catch(Throwable e){
						LfwLogger.error(e.getMessage(), e);
					} 
				}
				if(!errDs.isEmpty()){
					List<String> tmpArr = new ArrayList<String>();
					tmpArr.addAll(Arrays.asList(datasources));
					Iterator<String> it = errDs.iterator();
					while(it.hasNext())
						tmpArr.remove(it.next());
					datasources = tmpArr.toArray(new String[0]);
				}
				Map<String,String> dsVersions = getVersionMap().get(datasources[0]);
				getServletContext().setAttribute(CACHE_VERSION_MAP, dsVersions);
				String jsString = LfwJsonSerializer.getInstance().toJsObject(dsVersions);
				getServletContext().setAttribute(CACHE_VERSION_MAP_JSON, jsString);
				
				//10秒
				try {
					Thread.sleep(10000);
				} 
				catch (InterruptedException e) {
					LfwLogger.error(e.getMessage(), e);
				}
			}
		}

	}

	/* (non-Javadoc)
	 * @see nc.uap.lfw.core.servlet.LfwServletBase#getRemoteCallMethod(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public String getRemoteCallMethod(HttpServletRequest req,
			HttpServletResponse res) {
		return null;
	}
	
	/**
	 * 取得多数据源的版本Map
	 * @return 多数据源的版本Map
	 */
	public Map<String,Map<String,String>> getVersionMap() {
		return versionMap;
	}
	
	/**
	 * 取得所有数据源名
	 * @return 数据源名
	 */
	public String[] getDataSources(){
		if(datasources != null)
			return datasources;
		IBusiCenterManageService ics = NCLocator.getInstance().lookup(IBusiCenterManageService.class);
		try {
			BusiCenterVO[] bvos = ics.getBusiCenterVOs();
			if(bvos != null){
				Set<String> dsSet = new HashSet<String>();
				for (int i = 0; i < bvos.length; i++) {
					String ds = bvos[i].getDataSourceName();
					if(ds != null && !ds.equals(""))
						dsSet.add(ds);
				}
				datasources = dsSet.toArray(new String[0]);
			}
			else
				datasources = new String[0];
		} 
		catch (BusinessException e) {
			LfwLogger.error(e);
			datasources = new String[0];
		}
		return datasources;
	}
}
