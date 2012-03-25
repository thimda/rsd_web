/**
 * 
 */
package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * @author wupeng1
 * @version 6.0 2011-7-27
 * @since 1.6
 */
public class PaToolBarItemVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	
	private String pk_toolbaritem;
	private String pk_parent;
	
	private String type;
	private String refimg;
	private UFBoolean refimgchanged;
	private String realrefimg;
	private String itext;
	private String i18nname;
	private String langdir;
	private String tip;
	private String tipi18nname;
	private String align;
	private UFBoolean withsep;
	private int modifiers;
	private String hotkey ;
	private String displayhotkey;
	private String parentid;
	private String widgetid;
	/**
	 * widget中包含的属性
	 */
	private String id;
	private String width;
	
	
	@Override
	public String getPKFieldName() {
		return "pk_toolbaritem";
	}
	@Override
	public String getTableName() {
		return "pa_toolbaritem";
	}
	
	public String getPk_toolbaritem() {
		return pk_toolbaritem;
	}
	public void setPk_toolbaritem(String pk_toolbaritem) {
		this.pk_toolbaritem = pk_toolbaritem;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRefimg() {
		return refimg;
	}
	public void setRefimg(String refimg) {
		this.refimg = refimg;
	}
	public UFBoolean getRefimgchanged() {
		return refimgchanged;
	}
	public void setRefimgchanged(UFBoolean refimgchanged) {
		this.refimgchanged = refimgchanged;
	}
	public String getRealrefimg() {
		return realrefimg;
	}
	public void setRealrefimg(String realrefimg) {
		this.realrefimg = realrefimg;
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
	public String getLangdir() {
		return langdir;
	}
	public void setLangdir(String langdir) {
		this.langdir = langdir;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getTipi18nname() {
		return tipi18nname;
	}
	public void setTipi18nname(String tipi18nname) {
		this.tipi18nname = tipi18nname;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public UFBoolean getWithsep() {
		return withsep;
	}
	public void setWithsep(UFBoolean withsep) {
		this.withsep = withsep;
	}
	public int getModifiers() {
		return modifiers;
	}
	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
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
