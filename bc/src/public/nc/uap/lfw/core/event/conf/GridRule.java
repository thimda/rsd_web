package nc.uap.lfw.core.event.conf;

import java.io.Serializable;

/**
 * Grid提交类型
 * 
 * @author guoweic
 *
 */
public class GridRule implements Cloneable, Serializable {

	private static final long serialVersionUID = -4616047733374430734L;

	// 当前选中行
	public static final String TYPE_CURRENT_ROW = "grid_current_row";
	// 当前所有行
	public static final String TYPE_ALL_ROW = "grid_all_row";
	
	// Grid的ID
	private String id;
	// 提交类型
	private String type = TYPE_CURRENT_ROW;

	public Object clone(){
		GridRule gridRule = new GridRule();
		gridRule.setId(id);
		gridRule.setType(type);
		return gridRule;
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
