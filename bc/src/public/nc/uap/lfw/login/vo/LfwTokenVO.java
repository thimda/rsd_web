package nc.uap.lfw.login.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

/**
 * LFW登陆信息
 * 
 * @author guoweic
 * 
 */
public class LfwTokenVO extends SuperVO {

	private static final long serialVersionUID = -3719154195467933690L;
	/**
	 * session编号
	 */
	private String sessionid;
	/**
	 * 用户编码
	 */
	private String userpk;
	private String pk_lfwtoken;
	private String pkcrop;
	private String username;
	/** 帐套 **/
	private String accountcode;
	/** 数据源 **/
	private String datasource;
	/** 皮肤 **/
	private String themeid;
	private String usertype;
	/** 登陆时间 **/
	private UFDateTime logindate;
	/** 用户名称 **/
	private String userid;
	/** 登陆IP **/
	private String loginip;
	/** 系统ID **/
	private String systemid;
	/** 密码 **/
	private String passwd;
	/** token编号(UUID) **/
	private String tokenid;

	/** ---扩展项--- **/
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;
	private String ext6;
	private String ext7;
	private String ext8;
	private String ext9;
	private String ext10;

	/** ---扩展项--- **/

	@Override
	public String getPKFieldName() {
		return "pk_lfwtoken";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "uw_lfwtoken";
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getUserpk() {
		return userpk;
	}

	public void setUserpk(String userpk) {
		this.userpk = userpk;
	}

	public String getPk_lfwtoken() {
		return pk_lfwtoken;
	}

	public void setPk_lfwtoken(String pk_lfwtoken) {
		this.pk_lfwtoken = pk_lfwtoken;
	}

	public String getPkcrop() {
		return pkcrop;
	}

	public void setPkcrop(String pkcrop) {
		this.pkcrop = pkcrop;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getThemeid() {
		return themeid;
	}

	public void setThemeid(String themeid) {
		this.themeid = themeid;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getLoginip() {
		return loginip;
	}

	public void setLoginip(String loginip) {
		this.loginip = loginip;
	}

	public String getExt0() {
		return ext10;
	}

	public void setExt0(String ext10) {
		this.ext10 = ext10;
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

	public String getExt6() {
		return ext6;
	}

	public void setExt6(String ext6) {
		this.ext6 = ext6;
	}

	public String getExt7() {
		return ext7;
	}

	public void setExt7(String ext7) {
		this.ext7 = ext7;
	}

	public String getExt8() {
		return ext8;
	}

	public void setExt8(String ext8) {
		this.ext8 = ext8;
	}

	public String getExt9() {
		return ext9;
	}

	public void setExt9(String ext9) {
		this.ext9 = ext9;
	}

	public String getSystemid() {
		return systemid;
	}

	public void setSystemid(String systemid) {
		this.systemid = systemid;
	}

	public UFDateTime getLogindate() {
		return logindate;
	}

	public void setLogindate(UFDateTime logindate) {
		this.logindate = logindate;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getTokenid() {
		return tokenid;
	}

	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
	}

	public String getAccountcode() {
		return accountcode;
	}

	public void setAccountcode(String accountcode) {
		this.accountcode = accountcode;
	}
 
}
