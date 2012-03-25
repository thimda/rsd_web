/**
 * 
 */
package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;

/**
 * @author wupeng1
 * @version 6.0 2011-7-25
 * @since 1.6
 */
public class PaFormGroupCompVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	private String pk_formgrpcomp;
	private String pk_parent;
	
	private String forms;
	
	private String id;
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_formgrpcomp";
	}

	@Override
	public String getTableName() {
		return "pa_formgrpcomp";
	}

	public String getPk_formgrpcomp() {
		return pk_formgrpcomp;
	}

	public void setPk_formgrpcomp(String pk_formgrpcomp) {
		this.pk_formgrpcomp = pk_formgrpcomp;
	}

	public String getPk_parent() {
		return pk_parent;
	}

	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}

	public String getForms() {
		return forms;
	}

	public void setForms(String forms) {
		this.forms = forms;
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
