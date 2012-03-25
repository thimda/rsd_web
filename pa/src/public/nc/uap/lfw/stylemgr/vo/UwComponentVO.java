package nc.uap.lfw.stylemgr.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

public class UwComponentVO extends SuperVO{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -1812962856606753864L;
	private String pk_component;
	private String componentname;
	private String componentcode;
	private UFBoolean isvisiable;
	private String componentdesc;
	private String pk_templatedetail;
	private String pk_template;
	private String dataset;
	
	private UFBoolean dr;
	private UFDateTime ts;
	
	@Override
	public String getPKFieldName() {
		return "pk_component";
	}
	@Override
	public String getTableName() {
		return "uw_component";
	}
	public String getPk_component() {
		return pk_component;
	}
	public void setPk_component(String pk_component) {
		this.pk_component = pk_component;
	}
	public String getComponentname() {
		return componentname;
	}
	public void setComponentname(String componentname) {
		this.componentname = componentname;
	}
	public String getComponentcode() {
		return componentcode;
	}
	public void setComponentcode(String componentcode) {
		this.componentcode = componentcode;
	}
	public UFBoolean getIsvisiable() {
		return isvisiable;
	}
	public void setIsvisiable(UFBoolean isvisiable) {
		this.isvisiable = isvisiable;
	}
	public String getComponentdesc() {
		return componentdesc;
	}
	public void setComponentdesc(String componentdesc) {
		this.componentdesc = componentdesc;
	}
	public String getPk_templatedetail() {
		return pk_templatedetail;
	}
	public void setPk_templatedetail(String pk_templatedetail) {
		this.pk_templatedetail = pk_templatedetail;
	}
	public String getPk_template() {
		return pk_template;
	}
	public void setPk_template(String pk_template) {
		this.pk_template = pk_template;
	}
	public UFBoolean getDr() {
		return dr;
	}
	public void setDr(UFBoolean dr) {
		this.dr = dr;
	}
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	public String getDataset() {
		return dataset;
	}
	public void setDataset(String dataset) {
		this.dataset = dataset;
	}
	
	
	

}
