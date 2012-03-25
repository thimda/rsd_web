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
public class PaTreeLevelVO extends SuperVO{
	
	private static final long serialVersionUID = 1L;

	private String pk_treelevel;
	
	private String dataset;
	private String labelfields;
	private String labeldelims;
	private String masterkeyfield;
	private String detailkeyparameter;
	private String pk_parent;
	private String pk_child;
	private String contextmenu;
	private String id;
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_treelevel";
	}
	@Override
	public String getTableName() {
		return "pa_treelevel";
	}
	public String getPk_treelevel() {
		return pk_treelevel;
	}
	public void setPk_treelevel(String pk_treelevel) {
		this.pk_treelevel = pk_treelevel;
	}
	public String getDataset() {
		return dataset;
	}
	public void setDataset(String dataset) {
		this.dataset = dataset;
	}
	public String getLabelfields() {
		return labelfields;
	}
	public void setLabelfields(String labelfields) {
		this.labelfields = labelfields;
	}
	public String getLabeldelims() {
		return labeldelims;
	}
	public void setLabeldelims(String labeldelims) {
		this.labeldelims = labeldelims;
	}
	public String getMasterkeyfield() {
		return masterkeyfield;
	}
	public void setMasterkeyfield(String masterkeyfield) {
		this.masterkeyfield = masterkeyfield;
	}
	public String getDetailkeyparameter() {
		return detailkeyparameter;
	}
	public void setDetailkeyparameter(String detailkeyparameter) {
		this.detailkeyparameter = detailkeyparameter;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getPk_child() {
		return pk_child;
	}
	public void setPk_child(String pk_child) {
		this.pk_child = pk_child;
	}
	public String getContextmenu() {
		return contextmenu;
	}
	public void setContextmenu(String contextmenu) {
		this.contextmenu = contextmenu;
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
