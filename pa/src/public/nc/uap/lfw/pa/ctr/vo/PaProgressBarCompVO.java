/**
 * 
 */
package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * @author wupeng1
 * @version 6.0 2011-8-5
 * @since 1.6
 */
public class PaProgressBarCompVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private String pk_probarcomp;
	private String id;
	private String classname;
	private String value;
	private String positions;
	private String conftype;
	private String contextmenu;
	private String itop;
	private String height;
	private String width;
	private UFBoolean enabled;
	private UFBoolean visible;
	private String valuealign;
	private String ileft;
	
	private String pk_parent;
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_probarcomp";
	}
	@Override
	public String getTableName() {
		return "pa_probarcomp";
	}
	public String getPk_probarcomp() {
		return pk_probarcomp;
	}
	public void setPk_probarcomp(String pk_probarcomp) {
		this.pk_probarcomp = pk_probarcomp;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getPositions() {
		return positions;
	}
	public void setPositions(String positions) {
		this.positions = positions;
	}
	public String getConftype() {
		return conftype;
	}
	public void setConftype(String conftype) {
		this.conftype = conftype;
	}
	public String getContextmenu() {
		return contextmenu;
	}
	public void setContextmenu(String contextmenu) {
		this.contextmenu = contextmenu;
	}
	public String getItop() {
		return itop;
	}
	public void setItop(String itop) {
		this.itop = itop;
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
	public UFBoolean getEnabled() {
		return enabled;
	}
	public void setEnabled(UFBoolean enabled) {
		this.enabled = enabled;
	}
	public UFBoolean getVisible() {
		return visible;
	}
	public void setVisible(UFBoolean visible) {
		this.visible = visible;
	}
	public String getValuealign() {
		return valuealign;
	}
	public void setValuealign(String valuealign) {
		this.valuealign = valuealign;
	}
	public String getIleft() {
		return ileft;
	}
	public void setIleft(String ileft) {
		this.ileft = ileft;
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
