package nc.uap.lfw.design.itf;

import java.io.Serializable;

public class TemplateVO implements Serializable {

	public String getOperatorinfo() {
		return operatorinfo;
	}
	public void setOperatorinfo(String operatorinfo) {
		this.operatorinfo = operatorinfo;
	}
	private static final long serialVersionUID = 2558771354295367658L;
//	
//	public static final int billTemplate = 0;
//
//	public static final int queryTemplate = 1;
//
//	public static final int reportTemplate = 2;
//
//	public static final int printTemplate = 3;
//
//	public static final int cardTemplate = 4;
	
	/**
	 * 单据模板
	 */
	public final static int TEMPLAGE_TYPE_BILL = 0;
	/**
	 * 查询模板
	 */
	public final static int TEMPLAGE_TYPE_QUERY = 1;
	/**
	 * 报表模板
	 */
	public final static int TEMPLAGE_TYPE_REPORT = 2;
	/**
	 * 打印模板
	 */
	public final static int TEMPLAGE_TYPE_PRINT = 3;
	private String pk_template;
	private String billtypecode;
	private String templatecaption;
	private String nodekey;
	private String modulecode;
	private Integer templatetype;
	private String operatorinfo;
	private String templatecode;
	
	public String getTemplatecode() {
		return templatecode;
	}
	public void setTemplatecode(String templatecode) {
		this.templatecode = templatecode;
	}
	public Integer getTemplatetype() {
		return templatetype;
	}
	public void setTemplatetype(Integer templatetype) {
		this.templatetype = templatetype;
	}
	public String getModulecode() {
		return modulecode;
	}
	public void setModulecode(String modulecode) {
		this.modulecode = modulecode;
	}
	public String getPk_template() {
		return pk_template;
	}
	public void setPk_template(String pk_template) {
		this.pk_template = pk_template;
	}
	public String getBilltypecode() {
		return billtypecode;
	}
	public void setBilltypecode(String billtypecode) {
		this.billtypecode = billtypecode;
	}
	public String getTemplatecaption() {
		return templatecaption;
	}
	public void setTemplatecaption(String templatecaption) {
		this.templatecaption = templatecaption;
	}
	public String getNodekey() {
		return nodekey;
	}
	public void setNodekey(String nodekey) {
		this.nodekey = nodekey;
	}

}
