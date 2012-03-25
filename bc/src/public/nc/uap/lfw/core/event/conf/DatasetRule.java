package nc.uap.lfw.core.event.conf;

import java.io.Serializable;

/**
 * Dataset�ύ����
 * 
 * @author guoweic
 * 
 */
public class DatasetRule implements Cloneable, Serializable {

	private static final long serialVersionUID = -4616047733374430734L;
	
	// ��ǰ��
	public static final String TYPE_CURRENT_LINE = "ds_current_line";
	// ��ǰҳ
	public static final String TYPE_CURRENT_PAGE = "ds_current_page";
	// ��ǰҳ������
	public static final String TYPE_CURRENT_KEY = "ds_current_key";
	// ������
	public static final String TYPE_ALL_LINE = "ds_all_line";
	// ����ѡ����
	public static final String TYPE_ALL_SEL_LINE = "ds_all_sel_line";
	// Dataset��ID
	private String id;
	// �ύ����
	private String type = TYPE_CURRENT_LINE;

	public Object clone(){
		DatasetRule dsRule = new DatasetRule();
		dsRule.setId(id);
		dsRule.setType(type);
		return dsRule;
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
