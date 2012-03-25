package nc.uap.lfw.stylemgr.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

public class UwTemplateDetailVO extends SuperVO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6780827128041232711L;
	private String pk_templatedetail;
	private String templatedetailname;
	private String pk_parent;
	private String pk_template;
	private String comptype;
	private String templatedetaildesc;
	
	private UFBoolean dr;
	private UFDateTime ts;
	
	public String getPk_templatedetail() {
		return pk_templatedetail;
	}
	public void setPk_templatedetail(String pk_templatedetail) {
		this.pk_templatedetail = pk_templatedetail;
	}
	public String getTemplatedetailname() {
		return templatedetailname;
	}
	public void setTemplatedetailname(String templatedetailname) {
		this.templatedetailname = templatedetailname;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getPk_template() {
		return pk_template;
	}
	public void setPk_template(String pk_template) {
		this.pk_template = pk_template;
	}
	public String getComptype() {
		return comptype;
	}
	public void setComptype(String comptype) {
		this.comptype = comptype;
	}
	public String getTemplatedetaildesc() {
		return templatedetaildesc;
	}
	public void setTemplatedetaildesc(String templatedetaildesc) {
		this.templatedetaildesc = templatedetaildesc;
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
	@Override
	public String getPKFieldName() {
		return "pk_templatedetail";
	}
	@Override
	public String getTableName() {
		return "uw_templatedetail";
	}
	
	
	

}
