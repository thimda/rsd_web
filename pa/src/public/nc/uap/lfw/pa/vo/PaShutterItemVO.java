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
public class PaShutterItemVO extends PaLayoutPanelVO {
	
	private static final long serialVersionUID = 1L;

	private String pk_shutteritem;

	private String id;
	private String widgetid;
	private String classname;
	private String itext;
	private String i18nname;

	private String pk_parent;
	
	private String childid;
	private String childtype;
	private String parentid;
	

	@Override
	public String getPKFieldName() {
		return "pk_shutteritem";
	}

	@Override
	public String getTableName() {
		return "pa_panel" + PaConstant.PANEL_SHUTTERITEM;
	}

	public String getPk_shutteritem() {
		return pk_shutteritem;
	}

	public void setPk_shutteritem(String pk_shutteritem) {
		this.pk_shutteritem = pk_shutteritem;
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

	public String getI18nname() {
		return i18nname;
	}

	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
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
