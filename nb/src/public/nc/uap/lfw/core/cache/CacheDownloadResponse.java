package nc.uap.lfw.core.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CacheDownloadResponse extends ClientCacheResponse {
	private static final long serialVersionUID = 6318149231909338278L;
	private boolean zip = false;
	private List<TableJsonCache> cacheList = new ArrayList<TableJsonCache>();
	public boolean isZip() {
		return zip;
	}
	public void setZip(boolean zip) {
		this.zip = zip;
	}
	
	public void addTableJsonCache(TableJsonCache cache) {
		cacheList.add(cache);
	}
	
	public String toResponse() {
		StringBuffer buf = new StringBuffer();
		buf.append("{\"zip\":\"")
		   .append(zip)
		   .append("\", \"content\":[");
		Iterator<TableJsonCache> it = cacheList.iterator();
		while(it.hasNext()){
			buf.append("\n");
			TableJsonCache cache = it.next();
			buf.append(cache.toResponse());
			if(it.hasNext())
				buf.append(",");
		}
		buf.append("]}");
		return buf.toString();
	}
}
