package nc.uap.lfw.compatible;

import java.util.Map;

/**
 * ��֤�û��Ƿ�Ϊ�Ϸ��û�
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
//			throw new LfwRuntimeException("�û���¼����:" + username + "," + e.getMessage());
//		}
//		
//		if(response.getLoginResult() != ILoginConstants.LOGIN_SUCCESS){
//			throw new LfwRuntimeException("�û���¼����:" + response.getLoginResult());
//		}
//			
//		NCSession session = response.getNcSession();
//		NcSessionBean ncSessionBean = new NcSessionBean();
//		ncSessionBean.setSession(session);
//		LfwRuntimeEnvironment.setLfwSessionBean(ncSessionBean);
	}
}
