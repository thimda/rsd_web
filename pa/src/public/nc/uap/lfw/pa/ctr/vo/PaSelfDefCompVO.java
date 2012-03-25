/**
 * 
 */
package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * @author wupeng1
 * @version 6.0 2011-7-25
 * @since 1.6
 */
public class PaSelfDefCompVO extends SuperVO {


	private static final long serialVersionUID = 1L;
	private String pk_selfdefcomp;
	private UFBoolean visible;
	private String pk_parent;
	
	
	/**
	 * widget中包含的属性
	 */
	private String id;
	private String width;
	private String height;
	private String itop;
	private String ileft;
	private String positions;
	private String className;
	
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_selfdefcomp";
	}
	@Override
	public String getTableName() {
		return "pa_selfdefcomp";
	}
	public String getPk_selfdefcomp() {
		return pk_selfdefcomp;
	}
	public void setPk_selfdefcomp(String pk_selfdefcomp) {
		this.pk_selfdefcomp = pk_selfdefcomp;
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
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
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
	public String getPositions() {
		return positions;
	}
	public void setPositions(String positions) {
		this.positions = positions;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public UFBoolean getVisible() {
		return visible;
	}
	public void setVisible(UFBoolean visible) {
		this.visible = visible;
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
