package nc.uap.lfw.core.cache;

import java.io.Serializable;

public class TableJsonCache implements Serializable {
	private static final long serialVersionUID = 1490813707680390758L;
	private String tableName;
	private String version;
	private String pkField;
	private String scheme;
	private TableJsonContent content;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getPkField() {
		return pkField;
	}
	public void setPkField(String pkField) {
		this.pkField = pkField;
	}
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public TableJsonContent getContent() {
		return content;
	}
	public void setContent(TableJsonContent content) {
		this.content = content;
	}
	public Object toResponse() {
//		key:¡±T1¡±,ver:¡±12345¡±,pk:¡±p_id¡±,scheme:¡±create table ¡­.¡±, meta
		StringBuffer buf = new StringBuffer();
		buf.append("{\"key\":\"")
		   .append(tableName)
		   .append("\",\"ver\":\"")
		   .append(version)
		   .append("\",\"pk\":\"")
		   .append(pkField)
		   .append("\",\"scheme\":\"")
		   .append(scheme == null ? "" : scheme)
		   .append("\"");
			if(content != null){
				buf.append(",");
				buf.append(content.toResponse());
			}
		  buf.append("}");
		return buf.toString();
	}
}
