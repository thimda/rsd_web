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
public class PaCheckBoxCompVO extends SuperVO{

	private static final long serialVersionUID = 1L;
	private String pk_checkbox;
	
	private String value;
	private String i18nname;
	private UFBoolean checkeds;
	private String datatype;
	private String pk_parent;
	private String parentid;
	private String widgetid;
	
	/**
	 * widget中包含的属性
	 */
	private String id;
	private UFBoolean visibles;
	private String editortype;
	private String textalign;
	private int textwidth;
	private UFBoolean focuss;
	private String contextmenu;
	private String itext;
	private String langdir;
	private String ileft;
	private String classname;
	private String positions;
	private String width;
	private UFBoolean enableds;
	//显示图片路径
	private String imgsrc;
	@Override
	public String getPKFieldName() {
		return "pk_checkbox";
	}
	@Override
	public String getTableName() {
		return "pa_checkbox";
	}
	public String getPk_checkbox() {
		return pk_checkbox;
	}
	public void setPk_checkbox(String pk_checkbox) {
		this.pk_checkbox = pk_checkbox;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getI18nname() {
		return i18nname;
	}
	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
	}
	public UFBoolean getCheckeds() {
		return checkeds;
	}
	public void setCheckeds(UFBoolean checkeds) {
		this.checkeds = checkeds;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
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
	public String getContextmenu() {
		return contextmenu;
	}
	public void setContextmenu(String contextmenu) {
		this.contextmenu = contextmenu;
	}
	public String getItext() {
		return itext;
	}
	public void setItext(String itext) {
		this.itext = itext;
	}
	public String getLangdir() {
		return langdir;
	}
	public void setLangdir(String langdir) {
		this.langdir = langdir;
	}
	public String getIleft() {
		return ileft;
	}
	public void setIleft(String ileft) {
		this.ileft = ileft;
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
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
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
	public String getImgsrc() {
		return imgsrc;
	}
	public void setImgsrc(String imgsrc) {
		this.imgsrc = imgsrc;
	}
	
}
