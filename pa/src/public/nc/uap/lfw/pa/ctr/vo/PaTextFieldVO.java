/**
 * 
 */
package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;

/**
 * @author wupeng1
 * @version 6.0 2011-7-25
 * @since 1.6
 */
public class PaTextFieldVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	private String pk_textfield;
	private String pk_parent;
	
	private String type;
	private String refcode;
	private String id;
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_textfield";
	}
	@Override
	public String getTableName() {
		return "pa_textfield";
	}
	public String getPk_textfield() {
		return pk_textfield;
	}
	public void setPk_textfield(String pk_textfield) {
		this.pk_textfield = pk_textfield;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRefcode() {
		return refcode;
	}
	public void setRefcode(String refcode) {
		this.refcode = refcode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getWidgetid() {
		return widgetid;
	}
	public void setWidgetid(String widgetid) {
		this.widgetid = widgetid;
	}
	
}
