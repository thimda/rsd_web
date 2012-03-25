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
public class PaExcelColumnGroupVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	
	private String pk_excelcolumngrp;
	private String pk_parent;
	private String pk_excelcomp;
	
	private String id;
	private String i18nname;
	private UFBoolean visibles;
	private String itext;
	
	private String parentid;
	private String widgetid;
	
	
	@Override
	public String getPKFieldName() {
		return "pk_excelcolumngrp";
	}
	@Override
	public String getTableName() {
		return "pa_excelcolumngrp";
	}
	public String getPk_excelcolumngrp() {
		return pk_excelcolumngrp;
	}
	public void setPk_excelcolumngrp(String pk_excelcolumngrp) {
		this.pk_excelcolumngrp = pk_excelcolumngrp;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getPk_excelcomp() {
		return pk_excelcomp;
	}
	public void setPk_excelcomp(String pk_excelcomp) {
		this.pk_excelcomp = pk_excelcomp;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getI18nname() {
		return i18nname;
	}
	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
	}
	public UFBoolean getVisibles() {
		return visibles;
	}
	public void setVisibles(UFBoolean visibles) {
		this.visibles = visibles;
	}
	public String getItext() {
		return itext;
	}
	public void setItext(String itext) {
		this.itext = itext;
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
