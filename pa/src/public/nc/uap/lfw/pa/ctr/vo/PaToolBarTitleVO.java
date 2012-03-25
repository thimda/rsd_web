/**
 * 
 */
package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * @author wupeng1
 * @version 6.0 2011-7-25
 * @since 1.6
 */
public class PaToolBarTitleVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	
	private String pk_toolbartitle;
	private String pk_parent;
	private String pk_toolbarcomp;
	
	private String refimg1;
	private String refimg2;
	
	private UFBoolean refimg1changed;
	private String realrefimg1;
	private UFBoolean refimg2changed;
	private String realrefimg2;
	
	private String itext;
	private String i18nname;
	private String langdir;
	private String color;
	private UFBoolean bold;
	private String menuid;
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_toolbartitle";
	}
	@Override
	public String getTableName() {
		return "pa_toolbartitle";
	}
	public String getPk_toolbartitle() {
		return pk_toolbartitle;
	}
	public void setPk_toolbartitle(String pk_toolbartitle) {
		this.pk_toolbartitle = pk_toolbartitle;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	
	public String getPk_toolbarcomp() {
		return pk_toolbarcomp;
	}
	public void setPk_toolbarcomp(String pk_toolbarcomp) {
		this.pk_toolbarcomp = pk_toolbarcomp;
	}
	public String getRefimg1() {
		return refimg1;
	}
	public void setRefimg1(String refimg1) {
		this.refimg1 = refimg1;
	}
	public String getRefimg2() {
		return refimg2;
	}
	public void setRefimg2(String refimg2) {
		this.refimg2 = refimg2;
	}
	public UFBoolean getRefimg1changed() {
		return refimg1changed;
	}
	public void setRefimg1changed(UFBoolean refimg1changed) {
		this.refimg1changed = refimg1changed;
	}
	public String getRealrefimg1() {
		return realrefimg1;
	}
	public void setRealrefimg1(String realrefimg1) {
		this.realrefimg1 = realrefimg1;
	}
	public UFBoolean getRefimg2changed() {
		return refimg2changed;
	}
	public void setRefimg2changed(UFBoolean refimg2changed) {
		this.refimg2changed = refimg2changed;
	}
	public String getRealrefimg2() {
		return realrefimg2;
	}
	public void setRealrefimg2(String realrefimg2) {
		this.realrefimg2 = realrefimg2;
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
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public UFBoolean getBold() {
		return bold;
	}
	public void setBold(UFBoolean bold) {
		this.bold = bold;
	}
	public String getMenuid() {
		return menuid;
	}
	public void setMenuid(String menuid) {
		this.menuid = menuid;
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
