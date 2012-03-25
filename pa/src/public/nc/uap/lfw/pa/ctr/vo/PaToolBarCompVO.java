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
 * �ο�ToolBarComp.java���WidgetToXml.java��
 */
public class PaToolBarCompVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	
	private String pk_toolbarcomp;
	private String pk_parent;
	
	private String pk_toolbartitle;
	
	private UFBoolean transparent;
	
	/**
	 * widget�а���������
	 */
	private String id;
	private String width;
	private String height;
	private String itop;
	private String ileft;
	private UFBoolean visible;
	private UFBoolean enabled;
	private String contextmenu;
	private String classname;
	private String positions;
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_toolbarcomp";
	}

	@Override
	public String getTableName() {
		return "pa_toolbarcomp";
	}

	public String getPk_toolbarcomp() {
		return pk_toolbarcomp;
	}

	public void setPk_toolbarcomp(String pk_toolbarcomp) {
		this.pk_toolbarcomp = pk_toolbarcomp;
	}

	public String getPk_parent() {
		return pk_parent;
	}

	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}

	public String getPk_toolbartitle() {
		return pk_toolbartitle;
	}

	public void setPk_toolbartitle(String pk_toolbartitle) {
		this.pk_toolbartitle = pk_toolbartitle;
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

	public UFBoolean getTransparent() {
		return transparent;
	}

	public void setTransparent(UFBoolean transparent) {
		this.transparent = transparent;
	}

	public UFBoolean getVisible() {
		return visible;
	}

	public void setVisible(UFBoolean visible) {
		this.visible = visible;
	}

	public UFBoolean getEnabled() {
		return enabled;
	}

	public void setEnabled(UFBoolean enabled) {
		this.enabled = enabled;
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
