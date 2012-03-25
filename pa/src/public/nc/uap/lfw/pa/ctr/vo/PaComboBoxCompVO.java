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
public class PaComboBoxCompVO extends SuperVO{
	
	private static final long serialVersionUID = 1L;
	private String pk_combobox;
	private String pk_textcomp;
	private String pk_parent;
	
	private String refcombodata;
	private UFBoolean imageonlys;
	private UFBoolean selectonlys;
	private String datadivheight;
	private UFBoolean allowextendvalues;
	private UFBoolean showmarks;
	
	private String id;
	private UFBoolean visibles;
	private String editortype;
	private String itext;
	private String i18nname;
	private String contextmenu;
	private String itop;
	private String textalign;
	private int textwidth;
	private UFBoolean focuss;
	private String langdir;
	private String width;
	private String value;
	private String classname;
	private String height;
	private String ileft;
	private String positions;
	private UFBoolean enableds;
	
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_combobox";
	}
	@Override
	public String getTableName() {
		return "pa_combobox";
	}
	public String getPk_combobox() {
		return pk_combobox;
	}
	public void setPk_combobox(String pk_combobox) {
		this.pk_combobox = pk_combobox;
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
	public String getRefcombodata() {
		return refcombodata;
	}
	public void setRefcombodata(String refcombodata) {
		this.refcombodata = refcombodata;
	}
	public UFBoolean getImageonlys() {
		return imageonlys;
	}
	public void setImageonlys(UFBoolean imageonlys) {
		this.imageonlys = imageonlys;
	}
	public UFBoolean getSelectonlys() {
		return selectonlys;
	}
	public void setSelectonlys(UFBoolean selectonlys) {
		this.selectonlys = selectonlys;
	}

	public String getDatadivheight() {
		return datadivheight;
	}
	public void setDatadivheight(String datadivheight) {
		this.datadivheight = datadivheight;
	}
	public UFBoolean getAllowextendvalues() {
		return allowextendvalues;
	}
	public void setAllowextendvalues(UFBoolean allowextendvalues) {
		this.allowextendvalues = allowextendvalues;
	}
	public UFBoolean getShowmarks() {
		return showmarks;
	}
	public void setShowmarks(UFBoolean showmarks) {
		this.showmarks = showmarks;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public UFBoolean getVisibles() {
		return visibles;
	}
	public void setVisibles(UFBoolean visibles) {
		this.visibles = visibles;
	}
	public String getEditortype() {
		return editortype;
	}
	public void setEditortype(String editortype) {
		this.editortype = editortype;
	}
	public String getItext() {
		return itext;
	}
	public void setItext(String itext) {
		this.itext = itext;
	}
	public String getI18nname() {
		return i18nname;
	}
	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
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
	public UFBoolean getFocuss() {
		return focuss;
	}
	public void setFocuss(UFBoolean focuss) {
		this.focuss = focuss;
	}
	public String getLangdir() {
		return langdir;
	}
	public void setLangdir(String langdir) {
		this.langdir = langdir;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
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
	public UFBoolean getEnableds() {
		return enableds;
	}
	public void setEnableds(UFBoolean enableds) {
		this.enableds = enableds;
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
