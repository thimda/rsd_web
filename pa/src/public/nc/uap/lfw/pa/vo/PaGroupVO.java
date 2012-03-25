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
public class PaGroupVO extends PaLayoutVO{
	
	private static final long serialVersionUID = 1L;
	private String pk_group;
	private String pk_parent;
	
	private String id;
	private String widgetid;
	private String classname;
	
	private String itext;
	private String i18nname;
	private Integer opens;
	private String margintop;
	private String marginbottom;
	private String marginleft;
	private String marginright;
	
	private String parentid;
	
	@Override
	public String getPKFieldName() {
		return "pk_group";
	}
	@Override
	public String getTableName() {
		return "pa_layout" + PaConstant.LAYOUT_GROUP;
	}
	public String getPk_group() {
		return pk_group;
	}
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
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

	public String getItext() {
		return itext;
	}
	public void setItext(String itext) {
		this.itext = itext;
	}
	public String getMargintop() {
		return margintop;
	}
	public void setMargintop(String margintop) {
		this.margintop = margintop;
	}
	public String getMarginbottom() {
		return marginbottom;
	}
	public void setMarginbottom(String marginbottom) {
		this.marginbottom = marginbottom;
	}
	public String getMarginleft() {
		return marginleft;
	}
	public void setMarginleft(String marginleft) {
		this.marginleft = marginleft;
	}
	public String getMarginright() {
		return marginright;
	}
	public void setMarginright(String marginright) {
		this.marginright = marginright;
	}
	public String getWidgetid() {
		return widgetid;
	}
	public void setWidgetid(String widgetid) {
		this.widgetid = widgetid;
	}
	public Integer getOpens() {
		return opens;
	}
	public void setOpens(Integer opens) {
		this.opens = opens;
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
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}

}
