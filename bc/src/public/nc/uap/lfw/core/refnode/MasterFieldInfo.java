package nc.uap.lfw.core.refnode;

import java.io.Serializable;

/**
 * ���ֶ���Ϣ���������������ӹ���
 * 
 * @author guoweic
 *
 */
public class MasterFieldInfo implements Cloneable,Serializable{

	private static final long serialVersionUID = -6887020530820000165L;
	
	// ��ӦSqlΪ "XXX = null"
	public static final String NULL = "null";
	// ��ӦSqlΪ "XXX = ''"
	public static final String EMPTY = "empty";
	// ��ӦSqlΪ "(XXX = null or XXX = '')"
	public static final String BOTH = "both";
	// ��ʹ�øù���Sql
	public static final String IGNORE = "ignore";
	
	private String dsId;
	
	private String fieldId;
	
	/**
	 * ����Sql���
	 * ���� pk_corp='?',����ʱ����masterKeyField��ֵ����� 
	 * �������������������RefModelHandler,��˲�����������Ӧ��Handler���д�������ֻ��дHandler����
	 * �ɱ�ʶ����Ϣ����
	 */
	private String filterSql;
	
	/**
	 * ֵΪnullʱ�Ĵ���
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
