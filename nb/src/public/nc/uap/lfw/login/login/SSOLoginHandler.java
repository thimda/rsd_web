package nc.uap.lfw.login.login;


/**
 * 默认SSO登陆实现
 * 
 * @author guoweic
 * 
 */
public class SSOLoginHandler extends NCLoginHandler {


//	@Override
//	public AuthenticationUserVO getAuthenticateVO()	throws LoginInterruptedException {
//		try {
//			String serverIP=LfwSsoUtil.getServerIP();
//			String tokenID=LfwSsoUtil.getTokenID();
//			String ds = LfwSsoUtil.getDsId();
//			LfwTokenVO token = LfwSsoUtil.getRmtToken(serverIP, tokenID,ds);
//			return LfwSsoUtil.getAuthUserVO(token);
//		} catch (Exception e) {
//			LfwLogger.error("===DefaultLfwSsoServiceImpl===转换单点登陆信息失败", e);
//			throw new LfwRuntimeException("转换单点登陆信息失败");
//		}
//	}
	
}
