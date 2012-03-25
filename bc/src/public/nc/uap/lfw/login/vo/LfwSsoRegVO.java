package nc.uap.lfw.login.vo;

import java.util.HashMap;

import org.apache.commons.lang.SerializationUtils;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

/**
 * LFW单点登陆注册信息
 * 
 * @author licza
 * 
 */
public class LfwSsoRegVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3691346000982663522L;
	private String pk_ssoreg;
	/**
	 * Key
	 */
	private String ssokey;
	private byte[] reginfo;
	/**
	 * 注册时间
	 */
	private UFDateTime regtime;
	
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;
	public java.lang.Integer getDr() {
		return dr;
	}

	public void setDr(java.lang.Integer dr) {
		this.dr = dr;
	}

	public nc.vo.pub.lang.UFDateTime getTs() {
		return ts;
	}

	public void setTs(nc.vo.pub.lang.UFDateTime ts) {
		this.ts = ts;
	}

	public String getPk_ssoreg() {
		return pk_ssoreg;
	}

	public void setPk_ssoreg(String pk_ssoreg) {
		this.pk_ssoreg = pk_ssoreg;
	}

	public byte[] getReginfo() {
		return reginfo;
	}

	public void setReginfo(byte[] reginfo) {
		this.reginfo = reginfo;
	}

	public String getSsokey() {
		return ssokey;
	}

	public void setSsokey(String ssokey) {
		this.ssokey = ssokey;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String> doGetRegmap() {
		return (HashMap<String, String>) SerializationUtils
				.deserialize(getReginfo());
	}

	public void doSetRegmap(HashMap<String, String> regmap) {
		this.reginfo = SerializationUtils.serialize(regmap);
	}

	public UFDateTime getRegtime() {
		return regtime;
	}

	public void setRegtime(UFDateTime regtime) {
		this.regtime = regtime;
	}

	@Override
	public String getPKFieldName() {
		return "pk_ssoreg";
	}

	@Override
	public String getTableName() {
		return "uw_ssoreg";
	}
	
}
