package nc.uap.lfw.login.listener;

/**
 * �������ڽ����û���֤ʱ����ʱVO,����ͨ����ͬ�ķ�ʽ��ȡ�û���ݺ����Ϣ�洢
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
