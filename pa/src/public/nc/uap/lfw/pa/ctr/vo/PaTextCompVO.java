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
 * 参考TextComp.java类和WidgetToXml.java类
 */
public class PaTextCompVO extends SuperVO {
	private static final long serialVersionUID = 1L;
	
	private String pk_textcomp;
	
	private String pk_parent;
	private String value ;
	private UFBoolean readonly ;
	private String editortype ;
	
	private String maxvalue;
	private String minvalue;
	
	private String precisions;
	
	private String i18nname;
	private String langdir;
	private String itext;
	
	private UFBoolean focus ;
	private String textalign ;
	private Integer textwidth ;
	
	private UFBoolean showmark ;
	private String tip ;
	
	/**
	 * widget中所包含的属性
	 */
	private String id;
	private UFBoolean visible;
	private String contextmenu;
	private UFBoolean enabled;
	private String classname;
	private String width;
	private String itop;
	private String ileft;
	private String positions;
	private String height;
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_textcomp";
	}
	@Override
	public String getTableName() {
		return "pa_textcomp";
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
	public UFBoolean getReadonly() {
		return readonly;
	}
	public void setReadonly(UFBoolean readonly) {
		this.readonly = readonly;
	}
	public String getEditortype() {
		return editortype;
	}
	public void setEditortype(String editortype) {
		this.editortype = editortype;
	}
	public String getMaxvalue() {
		return maxvalue;
	}
	public void setMaxvalue(String maxvalue) {
		this.maxvalue = maxvalue;
	}
	public String getMinvalue() {
		return minvalue;
	}
	public void setMinvalue(String minvalue) {
		this.minvalue = minvalue;
	}
	public String getPrecisions() {
		return precisions;
	}
	public void setPrecisions(String precisions) {
		this.precisions = precisions;
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
	public String getItext() {
		return itext;
	}
	public void setItext(String itext) {
		this.itext = itext;
	}
	public UFBoolean getFocus() {
		return focus;
	}
	public void setFocus(UFBoolean focus) {
		this.focus = focus;
	}
	public String getTextalign() {
		return textalign;
	}
	public void setTextalign(String textalign) {
		this.textalign = textalign;
	}
	public Integer getTextwidth() {
		return textwidth;
	}
	public void setTextwidth(Integer textwidth) {
		this.textwidth = textwidth;
	}
	public UFBoolean getShowmark() {
		return showmark;
	}
	public void setShowmark(UFBoolean showmark) {
		this.showmark = showmark;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
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
	public String getContextmenu() {
		return contextmenu;
	}
	public void setContextmenu(String contextmenu) {
		this.contextmenu = contextmenu;
	}
	public UFBoolean getEnabled() {
		return enabled;
	}
	public void setEnabled(UFBoolean enabled) {
		this.enabled = enabled;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
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
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
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
