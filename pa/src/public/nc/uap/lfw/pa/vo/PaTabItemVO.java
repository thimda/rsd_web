/**
 * 
 */
package nc.uap.lfw.pa.vo;

import nc.uap.lfw.pa.PaConstant;

/**
 * @author wupeng1
 * @version 6.0 2011-7-20
 * @since 1.6
 */
public class PaTabItemVO extends PaLayoutPanelVO {
	
	private static final long serialVersionUID = 1L;

	private String pk_tabitem;
	
	private String id;
	private String widgetid;
	private String classname;
	private String itext;
	private String i18nname;
	private Integer state;
	private String showcloseicon;
	
	private String active;
	private boolean visibles;
	private boolean disabled;
	
	private String pk_parent;

	private String child;
	private String childtype;
	private String parentid;
	

	@Override
	public String getPKFieldName() {
		return "pk_tabitem";
	}
	@Override
	public String getTableName() {
		return "pa_panel" + PaConstant.PANEL_TABITEM;
	}

	public String getPk_tabitem() {
		return pk_tabitem;
	}

	public void setPk_tabitem(String pk_tabitem) {
		this.pk_tabitem = pk_tabitem;
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

	public String getItext() {
		return itext;
	}

	public void setItext(String itext) {
		this.itext = itext;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getI18nname() {
		return i18nname;
	}

	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getChild() {
		return child;
	}
	public void setChild(String child) {
		this.child = child;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}

	public boolean isVisibles() {
		return visibles;
	}
	public void setVisibles(boolean visibles) {
		this.visibles = visibles;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public String getShowcloseicon() {
		return showcloseicon;
	}
	public void setShowcloseicon(String showcloseicon) {
		this.showcloseicon = showcloseicon;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}

	
}
