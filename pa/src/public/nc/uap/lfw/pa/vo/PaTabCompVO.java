/**
 * 
 */
package nc.uap.lfw.pa.vo;

import nc.uap.lfw.pa.PaConstant;

/**
 * @author wupeng1
 * @version 6.0 2011-7-20
 * @since 1.6
 */
public class PaTabCompVO extends PaLayoutVO {
	
	private static final long serialVersionUID = 1L;
	private String pk_tabcomp;
	private String pk_parent;
	
	private String id;
	private String widgetid;
	private String classname;
	private String currentitem;
	private Integer onetabhide;
	
	private String pk_tabrtpanel;
	private String parentid;
	private String tabtype;
	
	@Override
	public String getPKFieldName() {
		return "pk_tabcomp";
	}

	@Override
	public String getTableName() {
		return "pa_layout" + PaConstant.LAYOUT_TAB;
	}

	public String getPk_tabcomp() {
		return pk_tabcomp;
	}

	public void setPk_tabcomp(String pk_tabcomp) {
		this.pk_tabcomp = pk_tabcomp;
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

	public String getWidgetid() {
		return widgetid;
	}

	public void setWidgetid(String widgetid) {
		this.widgetid = widgetid;
	}

	public String getCurrentitem() {
		return currentitem;
	}

	public void setCurrentitem(String currentitem) {
		this.currentitem = currentitem;
	}

	public Integer getOnetabhide() {
		return onetabhide;
	}

	public void setOnetabhide(Integer onetabhide) {
		this.onetabhide = onetabhide;
	}

	public String getPk_tabrtpanel() {
		return pk_tabrtpanel;
	}

	public void setPk_tabrtpanel(String pk_tabrtpanel) {
		this.pk_tabrtpanel = pk_tabrtpanel;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getTabtype() {
		return tabtype;
	}

	public void setTabtype(String tabtype) {
		this.tabtype = tabtype;
	}

}
