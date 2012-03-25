/**
 * 
 */
package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * @author wupeng1
 * @version 6.0 2011-7-22
 * @since 1.6
 */
public class PaComponentVO extends SuperVO {
	private static final long serialVersionUID = 1L;
	
	private String pk_componet;
	private UFBoolean visibles;
	private UFBoolean enableds;
	private String width;
	private String itop;
	private String ileft;
	private String height;
	private String positions;
	private String contextmenu;
	private String classname;
	
	private String pk_viewcomp;
	
	private String pk_parent;
	
	private String parentid;
	private String widgetid;
	
	
	@Override
	public String getPKFieldName() {
		return "pk_componet";
	}
	@Override
	public String getTableName() {
		return "pa_component";
	}
	public String getPk_componet() {
		return pk_componet;
	}
	public void setPk_componet(String pk_componet) {
		this.pk_componet = pk_componet;
	}
	public UFBoolean getVisibles() {
		return visibles;
	}
	public void setVisibles(UFBoolean visibles) {
		this.visibles = visibles;
	}
	public UFBoolean getEnableds() {
		return enableds;
	}
	public void setEnableds(UFBoolean enableds) {
		this.enableds = enableds;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getItop() {
		return itop;
	}
	public void setItop(String itop) {
		this.itop = itop;
	}
	public String getIleft() {
		return ileft;
	}
	public void setIleft(String ileft) {
		this.ileft = ileft;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getPositions() {
		return positions;
	}
	public void setPositions(String positions) {
		this.positions = positions;
	}
	public String getContextmenu() {
		return contextmenu;
	}
	public void setContextmenu(String contextmenu) {
		this.contextmenu = contextmenu;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getPk_viewcomp() {
		return pk_viewcomp;
	}
	public void setPk_viewcomp(String pk_viewcomp) {
		this.pk_viewcomp = pk_viewcomp;
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
	public String getWidgetid() {
		return widgetid;
	}
	public void setWidgetid(String widgetid) {
		this.widgetid = widgetid;
	}
	
}
