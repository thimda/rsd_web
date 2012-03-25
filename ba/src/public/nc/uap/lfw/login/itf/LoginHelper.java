package nc.uap.lfw.login.itf;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.model.util.LfwMaskerUtil;
import nc.uap.lfw.login.listener.AuthenticationUserVO;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.vo.bd.format.FormatDocVO;

/**
 * ʵ�ֵ�¼�ֲ�����Э�顣������LFW��Ӧ�ã��ڵ�¼���������ϸ�Ĳ���Լ����ʹ�ø�ϵͳ֮��������ԣ������ʵ��ͳһ���򵥡�
 * ��ҵ��ϵͳ�����ʵ��������ض�ӦHandler
 * 
 * @author dengjt
 * 
 */
public abstract class LoginHelper<K extends LfwSessionBean> {
	/**
	 * ������¼�¼�����Handler
	 * 
	 * @return
	 */
	public abstract ILoginHandler<K> createLoginHandler();

	/**
	 * ���������¼�����Handler
	 * 
	 * @return
	 */
	public ILfwIntegrationHandler createIntegrationHandler() {
		throw new LfwRuntimeException("û�����ü�����");
	}

	public AuthenticationUserVO processLogin(AuthenticationUserVO userVO)
			throws LoginInterruptedException {

		ILoginHandler<K> handler = createLoginHandler();

		/**
		 * STEP 1. У���û����������û���Ӧ�ĻỰ��Ϣ����
		 */
		K ses = handler.doAuthenticate(userVO);

		if (ses == null)
			return userVO;

		/**
		 * STEP 2. ���õ�����Ϣʵ�֣�����¼�û����õ�������ȥ
		 */
		ILoginSsoService<K> ssoService = handler.getLoginSsoService();
		ssoService.addSsoSign(ses, handler.getSysType());

		/**
		 * STEP 3. ���õ����¼ʵ�֣������û���¼֮���߼�
		 */
		ssoService.afterLogin(ses);

		/**
		 * STEP 4. ʵ��cookie�����Ը���������ʵ���������cookie��¼���ͻ���
		 */
		processCookie(userVO);
		
		done();
		/**
		 * add by licza:����Masker��Ϣ
		 */
		if(handler instanceof IMaskerHandler){
			FormatDocVO fm = ((IMaskerHandler<K>)handler).getMaskerInfo(ses);
			LfwMaskerUtil.registerMasker(fm);
		}
		
		return userVO;
	}

	public void done() {
		//���÷ǵ�¼ҳ��ʶ
		LfwRuntimeEnvironment.getWebContext().getRequest().getSession().setAttribute(WebContext.LOGIN_DID, "1");
	}

	/**
	 * ʵ��cookie�����Ը���������ʵ���������cookie��¼���ͻ���
	 * @param handler
	 * @param userVO
	 */
	public void processCookie( AuthenticationUserVO userVO){
		Cookie[] cks = createLoginHandler().getCookies(userVO);
		if (cks != null) {
			HttpServletResponse res = LfwRuntimeEnvironment.getWebContext().getResponse();
			if (res != null) {
				for (int i = 0; i < cks.length; i++) {
					res.addCookie(cks[i]);
				}
			}
		}
	}
	
	
	public AuthenticationUserVO processLogin() throws LoginInterruptedException {

		ILoginHandler<K> handler = createLoginHandler();
		/**
		 * STEP 1. ��ȡҪУ����û�VO
		 */
		AuthenticationUserVO userVO = handler.getAuthenticateVO();
		if (userVO == null)
			return null;
		processLogin(userVO);
		return userVO;
	}
}
