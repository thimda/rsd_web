package nc.uap.lfw.core.cache;

import java.util.List;
import java.util.Set;

import nc.vo.dbcache.config.TableInfo;
import nc.vo.dbcache.dbinfo.DBInfo;


public interface IClientCacheLoader {
	public TableJsonContent loadData(TableInfo tableInfo, boolean force, String oldVersion, String newVersion, DBInfo dbInfo);
	public List<String> getNeedLoadPKs(TableInfo tableInfo);
	public Set<String> getNeedDeletePKs(TableInfo tableInfo);
}
