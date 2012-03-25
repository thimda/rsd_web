package nc.uap.lfw.stylemgr.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

public class UwTemplateVO extends SuperVO{


	private static final long serialVersionUID = -7088590627320880221L;
	private String pk_template;
	private String templatename;
	private UFBoolean isactive;
	private String windowid;
	private byte[] pagemeta;
	private byte[] uimeta;
	private String appid;
	private Integer priority;
	private String pk_group;
	private String pk_funcnode;
	private String action;
	private String busiid;
	
	private String pk_prodef;
	private String port_id;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;
	
	private UFBoolean dr;
	private UFDateTime ts;
	
	@Override
	public String getPKFieldName() {
		return "pk_template";
	}
	@Override
	public String getTableName() {
		return "uw_template";
	}
	
	public String getPk_template() {
		return pk_template;
	}
	public void setPk_template(String pk_template) {
		this.pk_template = pk_template;
	}
	public String getTemplatename() {
		return templatename;
	}
	public void setTemplatename(String templatename) {
		this.templatename = templatename;
	}

	public UFBoolean getIsactive() {
		return isactive;
	}
	public void setIsactive(UFBoolean isactive) {
		this.isactive = isactive;
	}
	public String getWindowid() {
		return windowid;
	}
	public void setWindowid(String windowid) {
		this.windowid = windowid;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getPk_group() {
		return pk_group;
	}
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}
	public String getPk_funcnode() {
		return pk_funcnode;
	}
	public void setPk_funcnode(String pk_funcnode) {
		this.pk_funcnode = pk_funcnode;
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
	public byte[] getPagemeta() {
		return pagemeta;
	}
	public void setPagemeta(byte[] pagemeta) {
		this.pagemeta = pagemeta;
	}
	
	public String doGetPageMetaStr() {
		return new String(this.pagemeta);
	}
	
	public void doSetPageMetaStr(String pagemeta){
		this.pagemeta = pagemeta.getBytes();
	}

	public byte[] getUimeta() {
		return uimeta;
	}
	public void setUimeta(byte[] uimeta) {
		this.uimeta = uimeta;
	}
	public String doGetUIMetaStr() {
		return new String(this.uimeta);
	}
	
	public void doSetUIMetaStr(String uimeta){
		this.uimeta = uimeta.getBytes();
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getBusiid() {
		return busiid;
	}
	public void setBusiid(String busiid) {
		this.busiid = busiid;
	}
	public String getPk_prodef() {
		return pk_prodef;
	}
	public void setPk_prodef(String pk_prodef) {
		this.pk_prodef = pk_prodef;
	}
	public String getPort_id() {
		return port_id;
	}
	public void setPort_id(String port_id) {
		this.port_id = port_id;
	}
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	public String getExt2() {
		return ext2;
	}
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	public String getExt3() {
		return ext3;
	}
	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}
	public String getExt4() {
		return ext4;
	}
	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}
	public String getExt5() {
		return ext5;
	}
	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}
}
