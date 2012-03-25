package nc.uap.lfw.compatible;

import java.util.Map;

/**
 * 验证用户是否为合法用户
 * @author zhangxya
 *
 */
public final class NCLoginUtil {
	public static void doNCLogin(Map<String, String> userInfoMap) {
//		boolean forceLogin = true;
//		LoginRequest request = new LoginRequest();
//		String accountCode  = userInfoMap.get(ExtAttrConstants.ACCOUNTCODE);
//		String username = userInfoMap.get(ExtAttrConstants.USERNAME);
//		String password = userInfoMap.get(ExtAttrConstants.PASSWORD);
//		request.setBusiCenterCode(accountCode);
//		request.setUserCode(username); 
//		request.setUserPWD(password);
//		INCLoginService loginService = NCLocator.getInstance().lookup(INCLoginService.class);
//		LoginResponse response = null; 
//		LoginVerifyBean verifyBean = new LoginVerifyBean("8");
//		verifyBean.setStaticPWDVerify(true);
//		try {
//			response = loginService.otherSystemLogin(request, verifyBean, forceLogin);
//		} catch (BusinessException e) {
//			throw new LfwRuntimeException("用户登录报错:" + username + "," + e.getMessage());
//		}
//		
//		if(response.getLoginResult() != ILoginConstants.LOGIN_SUCCESS){
//			throw new LfwRuntimeException("用户登录报错:" + response.getLoginResult());
//		}
//			
//		NCSession session = response.getNcSession();
//		NcSessionBean ncSessionBean = new NcSessionBean();
//		ncSessionBean.setSession(session);
//		LfwRuntimeEnvironment.setLfwSessionBean(ncSessionBean);
	}
}
