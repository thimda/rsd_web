package nc.uap.lfw.compatible;

import java.util.Collection;

import nc.bs.dbcache.intf.IDBCacheBS;
import nc.bs.framework.common.NCLocator;
import nc.vo.dbcache.dbinfo.DBInfo;
import nc.vo.dbcache.load.LoadRequest;
import nc.vo.dbcache.load.LoadResponse;
import nc.vo.pub.BusinessException;

public final class NCCacheUtil {
	public static LoadResponse loadData(LoadRequest request) throws BusinessException{
		return NCLocator.getInstance().lookup(IDBCacheBS.class).LoadData(request);
	}

	public static DBInfo getServerDBInfo(Collection<String> needLoads) throws BusinessException{
		IDBCacheBS dbCacheBs = NCLocator.getInstance().lookup(IDBCacheBS.class);
		return dbCacheBs.getServerDBInfo(needLoads);
	}
}
