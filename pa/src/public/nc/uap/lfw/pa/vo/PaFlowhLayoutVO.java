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
public class PaFlowhLayoutVO extends PaLayoutVO {
	private static final long serialVersionUID = 1L;

	private String pk_flwhlayout;
	
	private String id;
	private String widgetid;
	private String classname;
	
	private String pk_parent;
	private String parentid;
	
	@Override
	public String getPKFieldName() {
		return "pk_flwhlayout";
	}

	@Override
	public String getTableName() {
		return "pa_layout" + PaConstant.LAYOUT_FLOWX;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPk_flwhlayout() {
		return pk_flwhlayout;
	}

	public void setPk_flwhlayout(String pk_flwhlayout) {
		this.pk_flwhlayout = pk_flwhlayout;
	}

	public String getPk_parent() {
		return pk_parent;
	}

	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
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

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}
}
