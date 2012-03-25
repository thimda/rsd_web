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
public class PaMenuItemVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	
	private String pk_menuitem;
	private String pk_parent;
	
	private String itext;
	
	private String i18nname;
	private String operatorstatusarray;
	private String businessstatusarray;
	
	private String operatorvisiblestatusarray;
	private String businessvisiblestatusarray;
	
	private String extendstatusarray;
	private String tip;
	private String tipi18nname;
	private UFBoolean selected;
	private UFBoolean checkboxgroup;
	private String tag;
	private String hotkey;
	private String displayhotkey;
	private Integer modifiers;
	private String imgicon;
	private String imgiconon;
	private String imgicondisable;
	private UFBoolean imgiconchanged;
	private String realimgicon;
	private UFBoolean imgicononchanged;
	private String realimgiconon;
	private UFBoolean imgicondisablechanged;
	private String realimgicondisable;
	private String langdir;
	private UFBoolean visible;
	private UFBoolean sep;
	private String id;
	
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_menuitem";
	}

	@Override
	public String getTableName() {
		return "pa_menuitem";
	}

	public String getPk_menuitem() {
		return pk_menuitem;
	}

	public void setPk_menuitem(String pk_menuitem) {
		this.pk_menuitem = pk_menuitem;
	}

	public String getPk_parent() {
		return pk_parent;
	}

	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
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

	public String getOperatorstatusarray() {
		return operatorstatusarray;
	}

	public void setOperatorstatusarray(String operatorstatusarray) {
		this.operatorstatusarray = operatorstatusarray;
	}

	public String getBusinessstatusarray() {
		return businessstatusarray;
	}

	public void setBusinessstatusarray(String businessstatusarray) {
		this.businessstatusarray = businessstatusarray;
	}

	public String getOperatorvisiblestatusarray() {
		return operatorvisiblestatusarray;
	}

	public void setOperatorvisiblestatusarray(String operatorvisiblestatusarray) {
		this.operatorvisiblestatusarray = operatorvisiblestatusarray;
	}

	public String getBusinessvisiblestatusarray() {
		return businessvisiblestatusarray;
	}

	public void setBusinessvisiblestatusarray(String businessvisiblestatusarray) {
		this.businessvisiblestatusarray = businessvisiblestatusarray;
	}

	public String getExtendstatusarray() {
		return extendstatusarray;
	}

	public void setExtendstatusarray(String extendstatusarray) {
		this.extendstatusarray = extendstatusarray;
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

	public UFBoolean getSelected() {
		return selected;
	}

	public void setSelected(UFBoolean selected) {
		this.selected = selected;
	}

	public UFBoolean getCheckboxgroup() {
		return checkboxgroup;
	}

	public void setCheckboxgroup(UFBoolean checkboxgroup) {
		this.checkboxgroup = checkboxgroup;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
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

	public Integer getModifiers() {
		return modifiers;
	}

	public void setModifiers(Integer modifiers) {
		this.modifiers = modifiers;
	}

	public String getImgicon() {
		return imgicon;
	}

	public void setImgicon(String imgicon) {
		this.imgicon = imgicon;
	}

	public String getImgiconon() {
		return imgiconon;
	}

	public void setImgiconon(String imgiconon) {
		this.imgiconon = imgiconon;
	}

	public String getImgicondisable() {
		return imgicondisable;
	}

	public void setImgicondisable(String imgicondisable) {
		this.imgicondisable = imgicondisable;
	}

	public UFBoolean getImgiconchanged() {
		return imgiconchanged;
	}

	public void setImgiconchanged(UFBoolean imgiconchanged) {
		this.imgiconchanged = imgiconchanged;
	}

	public String getRealimgicon() {
		return realimgicon;
	}

	public void setRealimgicon(String realimgicon) {
		this.realimgicon = realimgicon;
	}

	public UFBoolean getImgicononchanged() {
		return imgicononchanged;
	}

	public void setImgicononchanged(UFBoolean imgicononchanged) {
		this.imgicononchanged = imgicononchanged;
	}

	public String getRealimgiconon() {
		return realimgiconon;
	}

	public void setRealimgiconon(String realimgiconon) {
		this.realimgiconon = realimgiconon;
	}

	public UFBoolean getImgicondisablechanged() {
		return imgicondisablechanged;
	}

	public void setImgicondisablechanged(UFBoolean imgicondisablechanged) {
		this.imgicondisablechanged = imgicondisablechanged;
	}

	public String getRealimgicondisable() {
		return realimgicondisable;
	}

	public void setRealimgicondisable(String realimgicondisable) {
		this.realimgicondisable = realimgicondisable;
	}

	public String getLangdir() {
		return langdir;
	}

	public void setLangdir(String langdir) {
		this.langdir = langdir;
	}

	public UFBoolean getVisible() {
		return visible;
	}

	public void setVisible(UFBoolean visible) {
		this.visible = visible;
	}

	public UFBoolean getSep() {
		return sep;
	}

	public void setSep(UFBoolean sep) {
		this.sep = sep;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
