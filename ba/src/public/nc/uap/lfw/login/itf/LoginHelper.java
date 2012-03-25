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
 * 实现登录分步处理协议。将基于LFW的应用，在登录部分做了严格的步骤约定，使得各系统之间的易用性，单点等实现统一，简单。
 * 各业务系统需根据实际情况返回对应Handler
 * 
 * @author dengjt
 * 
 */
public abstract class LoginHelper<K extends LfwSessionBean> {
	/**
	 * 创建登录事件处理Handler
	 * 
	 * @return
	 */
	public abstract ILoginHandler<K> createLoginHandler();

	/**
	 * 创建集成事件处理Handler
	 * 
	 * @return
	 */
	public ILfwIntegrationHandler createIntegrationHandler() {
		throw new LfwRuntimeException("没有配置集成类");
	}

	public AuthenticationUserVO processLogin(AuthenticationUserVO userVO)
			throws LoginInterruptedException {

		ILoginHandler<K> handler = createLoginHandler();

		/**
		 * STEP 1. 校验用户，并返回用户对应的会话信息对象
		 */
		K ses = handler.doAuthenticate(userVO);

		if (ses == null)
			return userVO;

		/**
		 * STEP 2. 调用单点信息实现，将登录用户放置到单点中去
		 */
		ILoginSsoService<K> ssoService = handler.getLoginSsoService();
		ssoService.addSsoSign(ses, handler.getSysType());

		/**
		 * STEP 3. 调用单点登录实现，处理用户登录之后逻辑
		 */
		ssoService.afterLogin(ses);

		/**
		 * STEP 4. 实现cookie易用性辅助，根据实际情况，将cookie记录到客户端
		 */
		processCookie(userVO);
		
		done();
		/**
		 * add by licza:处理Masker信息
		 */
		if(handler instanceof IMaskerHandler){
			FormatDocVO fm = ((IMaskerHandler<K>)handler).getMaskerInfo(ses);
			LfwMaskerUtil.registerMasker(fm);
		}
		
		return userVO;
	}

	public void done() {
		//设置非登录页标识
		LfwRuntimeEnvironment.getWebContext().getRequest().getSession().setAttribute(WebContext.LOGIN_DID, "1");
	}

	/**
	 * 实现cookie易用性辅助，根据实际情况，将cookie记录到客户端
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
		 * STEP 1. 获取要校验的用户VO
		 */
		AuthenticationUserVO userVO = handler.getAuthenticateVO();
		if (userVO == null)
			return null;
		processLogin(userVO);
		return userVO;
	}
}
