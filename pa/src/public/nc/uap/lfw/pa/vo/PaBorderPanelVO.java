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
public class PaBorderPanelVO extends PaLayoutPanelVO{
	

	private static final long serialVersionUID = 1L;

	private String pk_bdpanel;
	
	private String id;
	private String widgetid;
	private String itop;
	private String center;
	private String bottom;
	private String ileft;
	private String iright;
	private String positions;
	private String height;
	private String width;
	private String classname;
	
	private String pk_parent;
	
	
	private String childid;
	private String childtype;
	
	private String parentid;
	

	@Override
	public String getPKFieldName() {
		return "pk_bdpanel";
	}

	@Override
	public String getTableName() {
		return "pa_panel" + PaConstant.PANEL_BORDER;
	}

	public String getPk_bdpanel() {
		return pk_bdpanel;
	}

	public void setPk_bdpanel(String pk_bdpanel) {
		this.pk_bdpanel = pk_bdpanel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItop() {
		return itop;
	}

	public void setItop(String itop) {
		this.itop = itop;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getBottom() {
		return bottom;
	}

	public void setBottom(String bottom) {
		this.bottom = bottom;
	}

	public String getIleft() {
		return ileft;
	}

	public void setIleft(String ileft) {
		this.ileft = ileft;
	}

	public String getIright() {
		return iright;
	}

	public void setIright(String iright) {
		this.iright = iright;
	}

	public String getPositions() {
		return positions;
	}

	public void setPositions(String positions) {
		this.positions = positions;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
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
