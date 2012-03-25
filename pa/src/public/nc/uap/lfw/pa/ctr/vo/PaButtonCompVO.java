/**
 * 
 */
package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * 参考ButtonComp.java类和WidgetToXml.java类
 * @author wupeng1
 * @version 6.0 2011-7-22
 * @since 1.6
 * 
 * 修改根据实际的添加字段
 */
public class PaButtonCompVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * widget中包含的有：align, enabled, height, id, left, position, text, top, visible, width
	 */
	private String pk_button;
	
	private String id;
	private String ileft;
	private String positions;
	private UFBoolean visibles;
	private String width;
	private String height;
	private String itop;
	private UFBoolean enableds;
	
	private String tip;
	private String i18nname;
	
	private String refimg;
	
	private UFBoolean refimgchangeds;
	private String realrefimg;
	
	private String align;
	private String langdir;
	private String itext;
	private String tipi18nname;
	private String hotkey;
	private String displayhotkey;
	
	private String classname;	//UI中的属性
	
	private String pk_parent;
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_button";
	}
	@Override
	public String getTableName() {
		return "pa_buttoncomp";
	}
	public String getPk_button() {
		return pk_button;
	}
	public void setPk_button(String pk_button) {
		this.pk_button = pk_button;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public UFBoolean getVisibles() {
		return visibles;
	}
	public void setVisibles(UFBoolean visibles) {
		this.visibles = visibles;
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
	public String getItop() {
		return itop;
	}
	public void setItop(String itop) {
		this.itop = itop;
	}
	public UFBoolean getEnableds() {
		return enableds;
	}
	public void setEnableds(UFBoolean enableds) {
		this.enableds = enableds;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getI18nname() {
		return i18nname;
	}
	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
	}
	public String getRefimg() {
		return refimg;
	}
	public void setRefimg(String refimg) {
		this.refimg = refimg;
	}
	public UFBoolean getRefimgchangeds() {
		return refimgchangeds;
	}
	public void setRefimgchangeds(UFBoolean refimgchangeds) {
		this.refimgchangeds = refimgchangeds;
	}
	public String getRealrefimg() {
		return realrefimg;
	}
	public void setRealrefimg(String realrefimg) {
		this.realrefimg = realrefimg;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
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
	public String getTipi18nname() {
		return tipi18nname;
	}
	public void setTipi18nname(String tipi18nname) {
		this.tipi18nname = tipi18nname;
	}
	public String getHotkey() {
		return hotkey;
	}
	public void setHotkey(String hotkey) {
		this.hotkey = hotkey;
	}
	public String getDisplayhotkey() {
		return displayhotkey;
	}
	public void setDisplayhotkey(String displayhotkey) {
		this.displayhotkey = displayhotkey;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
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


	
}
