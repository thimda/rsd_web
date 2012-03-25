/**
 * 
 */
package nc.uap.lfw.pa.vo;

import nc.uap.lfw.pa.PaConstant;
import nc.vo.pub.SuperVO;

/**
 * @author wupeng1
 * @version 6.0 2011-9-5
 * @since 1.6
 */
public class PaGridLayoutVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	
	private String pk_gridlayout;
	private String pk_parent;
	private String id;
	private String widgetid;
	private Integer rowcounts;
	private Integer colcount;
	private String border;
	private String classname;
	private String parentid;
	@Override
	public String getPKFieldName() {
		return "pk_gridlayout";
	}
	@Override
	public String getTableName() {
		return "pa_layout" + PaConstant.LAYOUT_GRID;
	}
	public String getPk_gridlayout() {
		return pk_gridlayout;
	}
	public void setPk_gridlayout(String pk_gridlayout) {
		this.pk_gridlayout = pk_gridlayout;
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
	public Integer getColcount() {
		return colcount;
	}
	public void setColcount(Integer colcount) {
		this.colcount = colcount;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public Integer getRowcounts() {
		return rowcounts;
	}
	public void setRowcounts(Integer rowcounts) {
		this.rowcounts = rowcounts;
	}
	public String getBorder() {
		return border;
	}
	public void setBorder(String border) {
		this.border = border;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}

	
}
