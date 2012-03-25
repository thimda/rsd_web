/**
 * 
 */
package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;

/**
 * @author wupeng1
 * @version 6.0 2011-7-22
 * @since 1.6
 */
public class PaMenubarCompVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private String pk_menucomp;
	private String pk_parent;
	private String pk_pagemata;
	
	/**
	 * widget÷–µƒ Ù–‘
	 */
	private String id;
	private String contextmenu;
	
	private String classname;
	private String height;
	private String width;
	
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_menucomp";
	}
	@Override
	public String getTableName() {
		return "pa_menubar";
	}
	public String getPk_menucomp() {
		return pk_menucomp;
	}
	public void setPk_menucomp(String pk_menucomp) {
		this.pk_menucomp = pk_menucomp;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getPk_pagemata() {
		return pk_pagemata;
	}
	public void setPk_pagemata(String pk_pagemata) {
		this.pk_pagemata = pk_pagemata;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContextmenu() {
		return contextmenu;
	}
	public void setContextmenu(String contextmenu) {
		this.contextmenu = contextmenu;
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
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
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

	
}
