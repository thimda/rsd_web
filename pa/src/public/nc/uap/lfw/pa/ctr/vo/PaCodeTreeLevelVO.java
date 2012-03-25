/**
 * 
 */
package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;

/**
 * @author wupeng1
 * @version 6.0 2011-8-8
 * @since 1.6
 */
public class PaCodeTreeLevelVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	
	private String pk_codetree;
	private String pk_parent;
	private String pk_treeview;
	
	private String codefield;
	private String coderule;
	
	/**
	 * widget÷–µƒ Ù–‘
	 */
	private String id;
	private String dataset;
	private String masterkeyfield;
	private String labelfields;
	private String labeldelims;
	private String detailkeyparameter;
	private String contextmenu;
	
	private String parentid;
	private String widgetid;
	@Override
	public String getPKFieldName() {
		return "pk_codetree";
	}
	@Override
	public String getTableName() {
		return "pa_codetree";
	}
	public String getPk_codetree() {
		return pk_codetree;
	}
	public void setPk_codetree(String pk_codetree) {
		this.pk_codetree = pk_codetree;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getPk_treeview() {
		return pk_treeview;
	}
	public void setPk_treeview(String pk_treeview) {
		this.pk_treeview = pk_treeview;
	}
	public String getCodefield() {
		return codefield;
	}
	public void setCodefield(String codefield) {
		this.codefield = codefield;
	}
	public String getCoderule() {
		return coderule;
	}
	public void setCoderule(String coderule) {
		this.coderule = coderule;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDataset() {
		return dataset;
	}
	public void setDataset(String dataset) {
		this.dataset = dataset;
	}
	public String getMasterkeyfield() {
		return masterkeyfield;
	}
	public void setMasterkeyfield(String masterkeyfield) {
		this.masterkeyfield = masterkeyfield;
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
	public String getDetailkeyparameter() {
		return detailkeyparameter;
	}
	public void setDetailkeyparameter(String detailkeyparameter) {
		this.detailkeyparameter = detailkeyparameter;
	}
	public String getContextmenu() {
		return contextmenu;
	}
	public void setContextmenu(String contextmenu) {
		this.contextmenu = contextmenu;
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
