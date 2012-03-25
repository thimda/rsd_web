package nc.uap.lfw.login.authfield;

/**
 * 参照类型外部程序验证字段
 * @author dengjt 2006-4-18
 */
public class RefExtAuthField extends ExtAuthField {

	private static final long serialVersionUID = 7815021123587668193L;
	private String defaultValue;
	private String defaultCaption;
	
	private String pageMeta;
	private String path;
	private String readDs;
	private String readFields;
	private String writeFields;
	private String datasource;
	/**参照模型类**/
	private String refmodel;
	
	/**数据加载类**/
	private String dsloaderclass;
	
	
	public RefExtAuthField() {
		super();
	}

	public RefExtAuthField(String label, String name, boolean required) {
		super(label, name, required);
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefaultCaption() {
		return defaultCaption;
	}

	public void setDefaultCaption(String defaultCaption) {
		this.defaultCaption = defaultCaption;
	}
	
	public String getPageMeta() {
		return pageMeta;
	}

	public void setPageMeta(String pageMeta) {
		this.pageMeta = pageMeta;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getReadDs() {
		return readDs;
	}

	public void setReadDs(String readDs) {
		this.readDs = readDs;
	}

	public String getReadFields() {
		return readFields;
	}

	public void setReadFields(String readFields) {
		this.readFields = readFields;
	}

	public int getType() {
		
		return ExtAuthFiledTypeConst.TYPE_REF;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getWriteFields() {
		return writeFields;
	}

	public void setWriteFields(String writeFields) {
		this.writeFields = writeFields;
	}

	public String getRefmodel() {
		return refmodel;
	}

	public void setRefmodel(String refmodel) {
		this.refmodel = refmodel;
	}

	public String getDsloaderclass() {
		return dsloaderclass;
	}

	public void setDsloaderclass(String dsloaderclass) {
		this.dsloaderclass = dsloaderclass;
	}
	
	
}
