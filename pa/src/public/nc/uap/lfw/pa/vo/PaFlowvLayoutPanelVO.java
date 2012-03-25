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
public class PaFlowvLayoutPanelVO extends PaLayoutPanelVO {
	private static final long serialVersionUID = 1L;

	
	private String pk_flwvpanel;
	
	private String pk_parent;
	
	
	private String id;
	private String widgetid;
	private String classname;
	
	private String height;
	
	private String allfill;
	
	private String anchor;
	
	private String childid;
	private String childtype;
	private String parentid;
	
	@Override
	public String getPKFieldName() {
		return "pk_flwvpanel";
	}

	@Override
	public String getTableName() {
		return "pa_panel" + PaConstant.PANEL_FLOWY;
	}

	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getPk_flwvpanel() {
		return pk_flwvpanel;
	}

	public void setPk_flwvpanel(String pk_flwvpanel) {
		this.pk_flwvpanel = pk_flwvpanel;
	}

	public String getPk_parent() {
		return pk_parent;
	}

	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getAllfill() {
		return allfill;
	}

	public void setAllfill(String allfill) {
		this.allfill = allfill;
	}

	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
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
