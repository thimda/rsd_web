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
public class PaRadioCompVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	private String pk_radio;
	private String pk_parent;
	private String pk_radiogroup;
	
	private String value;
	private String itext;
	private UFBoolean checked ;
	private String groups;
	
	/**
	 * widgetÖÐ°üº¬µÄ
	 */
	private String id;
	private UFBoolean visible;
	private UFBoolean enabled;
	private String editortype;
	private String width;
	private String height;
	private String i18nname;
	private String langdir;
	private String contextmenu;
	private String textalign;
	private int textwidth;
	private String positions;
	private UFBoolean focus;
	private String classname;
	private String itop;
	private String ileft;
	
	private String parentid;
	private String widgetid;
	
	private String imgsrc;
	
	@Override
	public String getPKFieldName() {
		return "pk_radio";
	}
	@Override
	public String getTableName() {
		return "pa_radio";
	}
	public String getPk_radio() {
		return pk_radio;
	}
	public void setPk_radio(String pk_radio) {
		this.pk_radio = pk_radio;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getPk_radiogroup() {
		return pk_radiogroup;
	}
	public void setPk_radiogroup(String pk_radiogroup) {
		this.pk_radiogroup = pk_radiogroup;
	}
	public String getItext() {
		return itext;
	}
	public void setItext(String itext) {
		this.itext = itext;
	}
	public UFBoolean getChecked() {
		return checked;
	}
	public void setChecked(UFBoolean checked) {
		this.checked = checked;
	}
	public String getGroups() {
		return groups;
	}
	public void setGroups(String groups) {
		this.groups = groups;
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
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
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
	public String getPositions() {
		return positions;
	}
	public void setPositions(String positions) {
		this.positions = positions;
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
	public String getImgsrc() {
		return imgsrc;
	}
	public void setImgsrc(String imgsrc) {
		this.imgsrc = imgsrc;
	}

}
