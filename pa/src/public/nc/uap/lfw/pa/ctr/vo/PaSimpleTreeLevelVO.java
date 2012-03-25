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
public class PaSimpleTreeLevelVO extends SuperVO {
	private static final long serialVersionUID = 1L;
	
	private String pk_simpletree;
	private String pk_parent;
	private String pk_treeview;
	
	private String id;
	private String dataset;
	private String masterKeyField;
	private String labelFields;
	private String labelDelims;
	private String detailKeyParameter;
	private String contextMenu;
	
	private String pk_treelevel;
	private String parentid;
	private String widgetid;

	@Override
	public String getPKFieldName() {
		return "pk_simpletree";
	}

	@Override
	public String getTableName() {
		return "pa_simpletree";
	}

	public String getPk_simpletree() {
		return pk_simpletree;
	}

	public void setPk_simpletree(String pk_simpletree) {
		this.pk_simpletree = pk_simpletree;
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

	public String getMasterKeyField() {
		return masterKeyField;
	}

	public void setMasterKeyField(String masterKeyField) {
		this.masterKeyField = masterKeyField;
	}

	public String getLabelFields() {
		return labelFields;
	}

	public void setLabelFields(String labelFields) {
		this.labelFields = labelFields;
	}

	public String getLabelDelims() {
		return labelDelims;
	}

	public void setLabelDelims(String labelDelims) {
		this.labelDelims = labelDelims;
	}

	public String getDetailKeyParameter() {
		return detailKeyParameter;
	}

	public void setDetailKeyParameter(String detailKeyParameter) {
		this.detailKeyParameter = detailKeyParameter;
	}

	public String getContextMenu() {
		return contextMenu;
	}

	public void setContextMenu(String contextMenu) {
		this.contextMenu = contextMenu;
	}

	public String getPk_treelevel() {
		return pk_treelevel;
	}

	public void setPk_treelevel(String pk_treelevel) {
		this.pk_treelevel = pk_treelevel;
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
