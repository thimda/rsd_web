package nc.uap.lfw.login.vo;

import java.io.Serializable;

/**
 * 定义轻量级web节点vo
 * 
 * @author zhangxya
 * 
 */
public class WebFunRegisterVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String disp_code;
	private String fun_code;
	private String pk_user;
	private String pk_corp;
	private Integer frequency;
	private String label;
	private String linkURL;
	private String funPK;
	private String funProperty;
	private Integer icon = 0;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFun_code() {
		return fun_code;
	}

	public void setFun_code(String fun_code) {
		this.fun_code = fun_code;
	}

	public String getPk_user() {
		return pk_user;
	}

	public void setPk_user(String pk_user) {
		this.pk_user = pk_user;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLinkURL() {
		return linkURL;
	}

	public void setLinkURL(String linkURL) {
		this.linkURL = linkURL;
	}

	public String getFunPK() {
		return funPK;
	}

	public void setFunPK(String funPK) {
		this.funPK = funPK;
	}

	public String getFunProperty() {
		return funProperty;
	}

	public void setFunProperty(String funProperty) {
		this.funProperty = funProperty;
	}

	public Integer getIcon() {
		return icon;
	}

	public void setIcon(Integer icon) {
		this.icon = icon;
	}

	public String getDisp_code() {
		return disp_code;
	}

	public void setDisp_code(String disp_code) {
		this.disp_code = disp_code;
	}

}