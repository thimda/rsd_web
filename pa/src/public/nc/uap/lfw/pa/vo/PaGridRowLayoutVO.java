package nc.uap.lfw.pa.vo;

import nc.uap.lfw.pa.PaConstant;
import nc.vo.pub.SuperVO;

public class PaGridRowLayoutVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;

	private String pk_gridrowlayout;
	
	private String id;
	private String widgetid;
	private String classname;
	
	private String rowheight;
	
	private String pk_parent;
	private String parentid;
	
	@Override
	public String getPKFieldName() {
		return "pk_gridrowlayout";
	}
	@Override
	public String getTableName() {
		return "pa_layout" + PaConstant.LAYOUT_GRIDROW;
	}
	
	public String getPk_gridrowlayout() {
		return pk_gridrowlayout;
	}
	public void setPk_gridrowlayout(String pk_gridrowlayout) {
		this.pk_gridrowlayout = pk_gridrowlayout;
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
	public String getRowheight() {
		return rowheight;
	}
	public void setRowheight(String rowheight) {
		this.rowheight = rowheight;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
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
