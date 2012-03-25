package nc.uap.lfw.login.vo;

import nc.vo.pub.SuperVO;

/**
 * 用户个性信息配置表vo
 * @author zhangxya
 *
 */
public class UserConfigVO extends SuperVO {
	private static final long serialVersionUID = 1L;
	private String pk_userconfig;
	private String userid;
	private String themeid;
	private String languageid;
	private int sysid;
	
	public String getPk_userconfig() {
		return pk_userconfig;
	}
	public void setPk_userconfig(String pk_userconfig) {
		this.pk_userconfig = pk_userconfig;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getThemeid() {
		return themeid;
	}
	public void setThemeid(String themeid) {
		this.themeid = themeid;
	}
	public String getLanguageid() {
		return languageid;
	}
	public void setLanguageid(String languageid) {
		this.languageid = languageid;
	}
	public void setSysid(int sysid) {
		this.sysid = sysid;
	}
	
	public int getSysid() {
		return sysid;
	}
	
	public String getTableName(){
		return "uw_userconfig";
	}
	
	public String getPKFieldName() {
		return "pk_userconfig";
	}
	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
}	
