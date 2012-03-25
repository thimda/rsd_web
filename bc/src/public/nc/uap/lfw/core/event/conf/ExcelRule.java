package nc.uap.lfw.core.event.conf;

import java.io.Serializable;

/**
 * Gxcel�ύ����
 * 
 * @author guoweic
 *
 */
public class ExcelRule implements Cloneable, Serializable {

	private static final long serialVersionUID = -4616047733374430734L;

	// ��ǰѡ����
	public static final String TYPE_CURRENT_ROW = "excel_current_row";
	// ��ǰ������
	public static final String TYPE_ALL_ROW = "excel_all_row";
	
	// Gxcel��ID
	private String id;
	// �ύ����
	private String type = TYPE_CURRENT_ROW;

	public Object clone(){
		ExcelRule excelRule = new ExcelRule();
		excelRule.setId(id);
		excelRule.setType(type);
		return excelRule;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
