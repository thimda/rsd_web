package nc.uap.lfw.pa.vo;

import nc.uap.lfw.pa.PaConstant;
import nc.vo.pub.SuperVO;

public class PaGridPanelVO extends SuperVO{
	
	private static final long serialVersionUID = 1L;
	private String pk_gridpanel;
	private String pk_parent;
	
	private String id;
	private String classname;
	
	private String rowspan;
	private String colspan;
	private String colwidth;
	private String colheight;

	private String widgetid;
	private String childid;
	private String childtype;
	
	private String parentid;
	
	@Override
	public String getPKFieldName() {
		return "pk_gridpanel";
	}

	@Override
	public String getTableName() {
		return "pa_panel" + PaConstant.PANEL_GRID;
	}

	public String getPk_gridpanel() {
		return pk_gridpanel;
	}

	public void setPk_gridpanel(String pk_gridpanel) {
		this.pk_gridpanel = pk_gridpanel;
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

	public String getColwidth() {
		return colwidth;
	}

	public void setColwidth(String colwidth) {
		this.colwidth = colwidth;
	}

	public String getColheight() {
		return colheight;
	}

	public void setColheight(String colheight) {
		this.colheight = colheight;
	}

	public String getChildid() {
		return childid;
	}

	public void setChildid(String childid) {
		this.childid = childid;
	}

	public String getChildtype() {
		return childtype;
	}

	public void setChildtype(String childtype) {
		this.childtype = childtype;
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

	public String getRowspan() {
		return rowspan;
	}

	public void setRowspan(String rowspan) {
		this.rowspan = rowspan;
	}

	public String getColspan() {
		return colspan;
	}

	public void setColspan(String colspan) {
		this.colspan = colspan;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

}
