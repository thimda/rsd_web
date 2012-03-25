package nc.uap.lfw.file.helper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 上传场景
 * 
 * @author licza
 * 
 */
public abstract class Scene {
	protected Map<String, String> arg = new LinkedHashMap<String, String>();
	static final String BILLTYPE = "billtype";
	static final String BILLITEM = "billitem";
	static final String CATEGORY = "category";
	static final String ISCOVER = "iscover";
	static final String FILEMANAGER = "filemanager";
	static final String WIDGET = "widget";
	static final String DATASET = "dataset";
	static final String FILENAME = "filename";
	static final String FILEURL = "fileurl";
	static final String METHOD = "method";
	static final String MULTI = "multi";
	static final String SIZELIMIT = "sizeLimit";
	static final String AUTO = "auto";
	static final String FILEFILTER = "filefilter";
	static final String PARENTWIDGET = "parentwidget";
	static final String PARENTDATASET = "parentdataset";
	static final String UPLOADLISTENER = "uploadlistener";
	static final String COMPLETLISTENER = "completlistener";

	/**
	 * 设置自定义文件管理类
	 * 
	 * @param fileMananger
	 */
	public void setFileMananger(String fileMananger) {
		arg.put(FILEMANAGER, fileMananger);
	}
	/**
	 * 设置单据PK
	 * @param billtype 单据类型
	 * @param billitem 单据pk
	 */
	public void setBillItem(String billitem) {
		arg.put(BILLITEM, billitem);
	}
	/**
	 * 设置单据类型
	 * @param billtype
	 */
	public void setBillType(String billtype) {
		arg.put(BILLTYPE, billtype);
	}
	/**
	 * 覆盖旧文件
	 */
	public void coverOldFile() {
		arg.put(ISCOVER, Boolean.TRUE.toString());
	}

	/**
	 * 文件体积上限
	 * 
	 * @param size
	 */
	public void maxFileSize(Long size) {
		arg.put(SIZELIMIT, size.toString());
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (arg != null && !arg.isEmpty()) {
			Set<String> set = arg.keySet();
			for (String key : set) {
				sb.append("&");
				sb.append(key);
				sb.append("=");
				sb.append(arg.get(key));
			}
		}
		return sb.toString();
	}

}
