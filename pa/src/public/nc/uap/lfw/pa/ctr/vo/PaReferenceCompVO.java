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
public class PaReferenceCompVO extends SuperVO{
	
	private static final long serialVersionUID = 1L;
	private String pk_refcomp;
	private String pk_textcomp;
	private String pk_parent;
	
	private String refcode;
	private String showvalue ;
	
	/**
	 * widget中包含的属性
	 */
	private String id;
	private UFBoolean visible;
	private UFBoolean enabled;
	private String editortype;
	private String width;
	private String itext;
	private String ileft;
	private String itop;
	private String i18nname;
	private String langdir;
	private String contextmenu;
	private String textalign;
	private int textwidth;
	private UFBoolean focus;
	private String classname;
	private String height;
	private String tip;
	private String positions;
	
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_refcomp";
	}
	@Override
	public String getTableName() {
		return "pa_refcomp";
	}
	public String getPk_refcomp() {
		return pk_refcomp;
	}
	public void setPk_refcomp(String pk_refcomp) {
		this.pk_refcomp = pk_refcomp;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getRefcode() {
		return refcode;
	}
	public void setRefcode(String refcode) {
		this.refcode = refcode;
	}
	public String getPk_textcomp() {
		return pk_textcomp;
	}
	public void setPk_textcomp(String pk_textcomp) {
		this.pk_textcomp = pk_textcomp;
	}
	public String getShowvalue() {
		return showvalue;
	}
	public void setShowvalue(String showvalue) {
		this.showvalue = showvalue;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getEditortype() {
		return editortype;
	}
	public void setEditortype(String editortype) {
		this.editortype = editortype;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getItext() {
		return itext;
	}
	public void setItext(String itext) {
		this.itext = itext;
	}
	public String getIleft() {
		return ileft;
	}
	public void setIleft(String ileft) {
		this.ileft = ileft;
	}
	public String getItop() {
		return itop;
	}
	public void setItop(String itop) {
		this.itop = itop;
	}
	public String getI18nname() {
		return i18nname;
	}
	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
	}
	public String getLangdir() {
		return langdir;
	}
	public void setLangdir(String langdir) {
		this.langdir = langdir;
	}
	public String getContextmenu() {
		return contextmenu;
	}
	public void setContextmenu(String contextmenu) {
		this.contextmenu = contextmenu;
	}
	public String getTextalign() {
		return textalign;
	}
	public void setTextalign(String textalign) {
		this.textalign = textalign;
	}
	public int getTextwidth() {
		return textwidth;
	}
	public void setTextwidth(int textwidth) {
		this.textwidth = textwidth;
	}
	public UFBoolean getFocus() {
		return focus;
	}
	public void setFocus(UFBoolean focus) {
		this.focus = focus;
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
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getPositions() {
		return positions;
	}
	public void setPositions(String positions) {
		this.positions = positions;
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
