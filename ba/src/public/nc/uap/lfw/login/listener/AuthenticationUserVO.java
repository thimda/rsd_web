package nc.uap.lfw.login.listener;

/**
 * 该类是在进行用户认证时的临时VO,用于通过不同的方式获取用户身份后的信息存储
 * 
 */
public class AuthenticationUserVO {
	private String userID;
	private String password; 
	private Object extInfo;	
	public AuthenticationUserVO(){}
	public AuthenticationUserVO(String user, String pwd){
		this.userID = user;
		this.password = pwd;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public Object getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(Object extInfo) {
		this.extInfo = extInfo;
	}
}
