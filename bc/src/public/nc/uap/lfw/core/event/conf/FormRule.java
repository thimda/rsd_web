package nc.uap.lfw.core.event.conf;

import java.io.Serializable;

/**
 * Form�ύ����
 * 
 * @author guoweic
 *
 */
public class FormRule implements Cloneable, Serializable {

	private static final long serialVersionUID = -4616047733374430734L;
	public static final String NO_CHILD = "no_child";
	// ��ǰ������
	public static final String ALL_CHILD = "all_child";
	
	// Form��ID
	private String id;
	// �ύ����
	private String type = NO_CHILD;

	public Object clone(){
		FormRule formRule = new FormRule();
		formRule.setId(id);
		formRule.setType(type);
		return formRule;
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
