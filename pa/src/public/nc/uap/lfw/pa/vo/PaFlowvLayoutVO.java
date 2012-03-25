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
public class PaFlowvLayoutVO extends PaLayoutVO {
	
	private static final long serialVersionUID = 1L;

	private String pk_flwvlayout;
	
	private String id;
	private Integer autofill;
	private String widgetid;
	private String classname;
	
	private String pk_parent;
	private String parentid;
	

	@Override
	public String getPKFieldName() {
		return "pk_flwvlayout";
	}

	@Override
	public String getTableName() {
		return "pa_layout" + PaConstant.LAYOUT_FLOWY;
	}

	public String getPk_flwvlayout() {
		return pk_flwvlayout;
	}

	public void setPk_flwvlayout(String pk_flwvlayout) {
		this.pk_flwvlayout = pk_flwvlayout;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPk_parent() {
		return pk_parent;
	}

	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}

	public Integer getAutofill() {
		return autofill;
	}

	public void setAutofill(Integer autofill) {
		this.autofill = autofill;
	}

	public String getWidgetid() {
		return widgetid;
	}

	public void setWidgetid(String widgetid) {
		this.widgetid = widgetid;
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

}
