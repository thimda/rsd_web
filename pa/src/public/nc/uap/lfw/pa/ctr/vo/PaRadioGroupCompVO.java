/**
 * 
 */
package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * @author wupeng1
 * @version 6.0 2011-8-8
 * @since 1.6
 */
public class PaRadioGroupCompVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	
	private String pk_radiogroup;
	private String pk_textcomp;
	private String pk_parent;
	
	private String value;
	private String combodataid;
	private int indexs;
	private int tabindex;
	private int sepwidth;
	private UFBoolean changeline;
	private UFBoolean readonly;
	
	private String id;
	private UFBoolean visible;
	private String editortype;
	private UFBoolean enabled;
	private String itext;
	private int textwidth;
	private String i18nname;
	private String langdir;
	private String textalign;
	private String width;
	private String height;
	private UFBoolean focus;
	private String itop;
	private String ileft;
	private String contextmenu;
	private String classname;
	private String positions;
	
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_radiogroup";
	}
	@Override
	public String getTableName() {
		return "pa_radiogroup";
	}
	public String getPk_radiogroup() {
		return pk_radiogroup;
	}
	public void setPk_radiogroup(String pk_radiogroup) {
		this.pk_radiogroup = pk_radiogroup;
	}
	public String getPk_textcomp() {
		return pk_textcomp;
	}
	public void setPk_textcomp(String pk_textcomp) {
		this.pk_textcomp = pk_textcomp;
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
	public String getCombodataid() {
		return combodataid;
	}
	public void setCombodataid(String combodataid) {
		this.combodataid = combodataid;
	}
	public int getIndexs() {
		return indexs;
	}
	public void setIndexs(int indexs) {
		this.indexs = indexs;
	}
	public int getTabindex() {
		return tabindex;
	}
	public void setTabindex(int tabindex) {
		this.tabindex = tabindex;
	}
	public int getSepwidth() {
		return sepwidth;
	}
	public void setSepwidth(int sepwidth) {
		this.sepwidth = sepwidth;
	}
	public UFBoolean getChangeline() {
		return changeline;
	}
	public void setChangeline(UFBoolean changeline) {
		this.changeline = changeline;
	}
	public UFBoolean getReadonly() {
		return readonly;
	}
	public void setReadonly(UFBoolean readonly) {
		this.readonly = readonly;
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
	public String getEditortype() {
		return editortype;
	}
	public void setEditortype(String editortype) {
		this.editortype = editortype;
	}
	public UFBoolean getEnabled() {
		return enabled;
	}
	public void setEnabled(UFBoolean enabled) {
		this.enabled = enabled;
	}
	public String getItext() {
		return itext;
	}
	public void setItext(String itext) {
		this.itext = itext;
	}
	public int getTextwidth() {
		return textwidth;
	}
	public void setTextwidth(int textwidth) {
		this.textwidth = textwidth;
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
	public String getTextalign() {
		return textalign;
	}
	public void setTextalign(String textalign) {
		this.textalign = textalign;
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
	public UFBoolean getFocus() {
		return focus;
	}
	public void setFocus(UFBoolean focus) {
		this.focus = focus;
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
