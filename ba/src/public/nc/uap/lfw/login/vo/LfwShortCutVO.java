package nc.uap.lfw.login.vo;

import nc.vo.pub.SuperVO;
/**
 * ¿ì½Ý·½Ê½vo
 * @author zhangxya
 *
 */
public class LfwShortCutVO extends SuperVO {
	
	private static final long serialVersionUID = -8167159488107304800L;
	private String pk_shortcut;
	private String fun_code; 
	private String pk_user;
	private String pk_corp;
	private int sysid;
	private String name;
	private String ext1;
	private String ext2;
	private String ext3;
	
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
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
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
	
	public void setSysid(int sysid) {
		this.sysid = sysid;
	}
	
	public int getSysid() {
		return sysid;
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
	public String getPk_shortcut() {
		return pk_shortcut;
	}
	public void setPk_shortcut(String pk_shortcut) {
		this.pk_shortcut = pk_shortcut;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTableName(){
		return "uw_shortcut";
	}
	
	public String getPKFieldName() {
		return "pk_shortcut";
	}
	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
}
