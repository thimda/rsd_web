package nc.uap.lfw.core.refnode;

import java.io.Serializable;

/**
 * 主字段信息描述对象，用于主子过滤
 * 
 * @author guoweic
 *
 */
public class MasterFieldInfo implements Cloneable,Serializable{

	private static final long serialVersionUID = -6887020530820000165L;
	
	// 对应Sql为 "XXX = null"
	public static final String NULL = "null";
	// 对应Sql为 "XXX = ''"
	public static final String EMPTY = "empty";
	// 对应Sql为 "(XXX = null or XXX = '')"
	public static final String BOTH = "both";
	// 不使用该过滤Sql
	public static final String IGNORE = "ignore";
	
	private String dsId;
	
	private String fieldId;
	
	/**
	 * 过滤Sql语句
	 * 形如 pk_corp='?',运行时将以masterKeyField的值替代它 
	 * 如果被触发参照设置了RefModelHandler,则此参数将传给对应的Handler进行处理，所以只需写Handler本身
	 * 可辨识的信息即可
	 */
	private String filterSql;
	
	/**
	 * 值为null时的处理
	 */
	private String nullProcess;

	public String getFilterSql() {
		return filterSql;
	}

	public void setFilterSql(String filterSql) {
		this.filterSql = filterSql;
	}

	public String getNullProcess() {
		return nullProcess;
	}

	public void setNullProcess(String nullProcess) {
		this.nullProcess = nullProcess;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getDsId() {
		return dsId;
	}

	public void setDsId(String dsId) {
		this.dsId = dsId;
	}
	
	

}
