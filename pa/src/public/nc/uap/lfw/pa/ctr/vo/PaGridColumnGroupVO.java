/**
 * 
 */
package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;


/**
 * @author wupeng1
 * @version 6.0 2011-8-5
 * @since 1.6
 */
public class PaGridColumnGroupVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	
	private String pk_gridcolumngroup;
	private String id;
	private String i18nname;
	private UFBoolean visibles;
	private String itext;
	private String pk_gridcomp;
	private String pk_parent;
	
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_gridcolumngroup";
	}
	@Override
	public String getTableName() {
		return "pa_gridcolumngroup";
	}
	
	public String getPk_gridcolumngroup() {
		return pk_gridcolumngroup;
	}
	public void setPk_gridcolumngroup(String pk_gridcolumngroup) {
		this.pk_gridcolumngroup = pk_gridcolumngroup;
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
	public String getPk_gridcomp() {
		return pk_gridcomp;
	}
	public void setPk_gridcomp(String pk_gridcomp) {
		this.pk_gridcomp = pk_gridcomp;
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
	
	
}
