/**
 * 
 */
package nc.uap.lfw.pa.vo;

import nc.uap.lfw.pa.PaConstant;

/**
 * @author wupeng1
 * @version 6.0 2011-7-19
 * @since 1.6
 */
public class PaTabRightPanelPanelVO extends PaLayoutPanelVO{

	private static final long serialVersionUID = 1L;
	private String pk_tabrtpanel;
	private String pk_parent;
	
	private String id;
	private String widgetid;
	private String classname;
	private String width;

	private String childid;
	private String childtype;
	private String parentid;
	
	@Override
	public String getPKFieldName() {
		return "pk_tabrtpanel";
	}

	@Override
	public String getTableName() {
		return "pa_panel" + PaConstant.PANEL_TABRIGHT;
	}

	public String getPk_tabrtpanel() {
		return pk_tabrtpanel;
	}

	public void setPk_tabrtpanel(String pk_tabrtpanel) {
		this.pk_tabrtpanel = pk_tabrtpanel;
	}

	public String getPk_parent() {
		return pk_parent;
	}

	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
