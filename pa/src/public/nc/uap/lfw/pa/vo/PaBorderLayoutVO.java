/**
 * 
 */
package nc.uap.lfw.pa.vo;

import nc.uap.lfw.pa.PaConstant;
import nc.vo.pub.SuperVO;

/**
 * @author wupeng1
 * @version 6.0 2011-7-20
 * @since 1.6
 */
public class PaBorderLayoutVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private String pk_bdlayout;
	
	private String id;
	private String widgetid;
	
	private String classname;
	
	private String pk_parent;
	private String parentid;
	

	@Override
	public String getPKFieldName() {
		return "pk_bdlayout";
	}

	@Override
	public String getTableName() {
		return "pa_layout" + PaConstant.LAYOUT_BORDERL;
	}

	public String getPk_bdlayout() {
		return pk_bdlayout;
	}

	public void setPk_bdlayout(String pk_bdlayout) {
		this.pk_bdlayout = pk_bdlayout;
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
