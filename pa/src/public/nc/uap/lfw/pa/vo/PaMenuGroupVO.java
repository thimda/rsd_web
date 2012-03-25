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
public class PaMenuGroupVO extends PaLayoutVO{
	
	private static final long serialVersionUID = 1L;
	private String pk_menugroup;
	private String pk_parent;
	
	private String id;	
	private String classname;
	private String widgetid;
	private String parentid;
	
	@Override
	public String getPKFieldName() {
		return "pk_menugroup";
	}
	@Override
	public String getTableName() {
		return "pa_layout" + PaConstant.LAYOUT_MENUGROUP;
	}
	public String getPk_menugroup() {
		return pk_menugroup;
	}
	public void setPk_menugroup(String pk_menugroup) {
		this.pk_menugroup = pk_menugroup;
	}

	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
