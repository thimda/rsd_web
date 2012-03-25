/**
 * 
 */
package nc.uap.lfw.pa.vo;

import nc.uap.lfw.pa.PaConstant;

/**
 * @author wupeng1
 * @version 6.0 2011-7-20
 * @since 1.6
 */
public class PaBorderTrueVO extends PaLayoutPanelVO {
	private static final long serialVersionUID = 1L;

	private String pk_bordertrue;
	
	private String id;
	private String widgetid;
	private String classname;
	
	private String pk_parent;
	
	private String childid;
	private String childtype;
	private String parentid;
	

	@Override
	public String getPKFieldName() {
		return "pk_bordertrue";
	}

	@Override
	public String getTableName() {
		return "pa_panel" + PaConstant.PANEL_BORDERTRUE;
	}

	public String getPk_bordertrue() {
		return pk_bordertrue;
	}

	public void setPk_bordertrue(String pk_bordertrue) {
		this.pk_bordertrue = pk_bordertrue;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPk_parent() {
		return pk_parent;
	}

	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}

	public String getChildtype() {
		return childtype;
	}

	public void setChildtype(String childtype) {
		this.childtype = childtype;
	}

	public String getWidgetid() {
		return widgetid;
	}

	public void setWidgetid(String widgetid) {
		this.widgetid = widgetid;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getChildid() {
		return childid;
	}

	public void setChildid(String childid) {
		this.childid = childid;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}
}
