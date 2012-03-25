package nc.uap.lfw.login.login;



/**
 * NC6.0登录默认实现
 *
 */
public class NCLoginHandler{
//implements ILoginHandler<NcSessionBean> ,IMaskerHandler<NcSessionBean>{
//
//	//当前登录片段
//	private LfwWidget currentWidget;
//	public AuthenticationUserVO getAuthenticateVO() throws LoginInterruptedException{
//		LfwWidget widget = getCurrentWidget();
//		
//		ComboBoxComp account = (ComboBoxComp) widget.getViewComponents().getComponent(NcSsoConstant.ACCOUNT_CODE);
//		String accountValue = null;
//		if(account == null){
//			accountValue = LfwRuntimeEnvironment.getModelServerConfig().getConfigValue("account");
//		}
//		else{
//			accountValue = account.getValue();
//		}
//		// 帐套编码
//		if (accountValue == null || accountValue.equals("")) {
//			throw new LoginInterruptedException("帐套不能为空");
//		}
//
//		TextComp userIdComp = (TextComp) widget.getViewComponents().getComponent(NcSsoConstant.USER_ID);
//		String userId = userIdComp.getValue();
//		if (userId == null || userId.equals("")) {
//			throw new LoginInterruptedException("用户名不能为空");
//		}
//
//		TextComp passComp = (TextComp) widget.getViewComponents().getComponent(NcSsoConstant.PASSWORD);
//		String passValue = passComp.getValue();
//		if (passValue == null || passValue.equals("")) {
//			throw new LoginInterruptedException("密码不能为空");
//		}
//
//		Encode.decode(passValue);
//		// 强行登陆标志
//		boolean forceLogin = false;
//		if (null != InteractionUtil.getConfirmDialogResult() && InteractionUtil.getConfirmDialogResult().equals(Boolean.TRUE))
//			forceLogin = true;
//		else if (null != InteractionUtil.getConfirmDialogResult() && InteractionUtil.getConfirmDialogResult().equals(Boolean.FALSE)) {
//			throw new LoginInterruptedException();
//		}
//
//		AuthenticationUserVO userVO = new AuthenticationUserVO();
//		userVO.setUserID(userId);
//		userVO.setPassword(passValue);
//		Map<String, String> extMap = new HashMap<String, String>();
//		extMap.put(NcSsoConstant.FORCE, forceLogin ? "true" : "false");
//		extMap.put(NcSsoConstant.ACCOUNT_CODE, accountValue);
//
//		
//		Encode.decode("akcjhcnjbjpbdbbm");
//		
//		//设置语言
//		String language = "simpchn";
//		extMap.put(NcSsoConstant.LANGUAGE, language);
//		extMap.put(NcSsoConstant.MAXWIN, "N");
//		userVO.setExtInfo(extMap);
//		return userVO;
//	}
//
//
//	public NcSessionBean doAuthenticate(AuthenticationUserVO userVO) throws LoginInterruptedException {
//		// 清理session队列中他人关闭浏览器后所留下的无效session
//		LfwSecurityUtil.clearExpiredSession();
//		// 用户登陆状态
//		LoginResponse response = null;
//		try {
//			response = doLogin(userVO);
//		} 
//		catch (BusinessException e1) {
//			LfwLogger.error(e1);
//			throw new LoginInterruptedException(e1.getMessage());
//		}
//		// 登陆成功
//		if (response.getLoginResult() == ILoginConstants.LOGIN_SUCCESS) {
//			NcSessionBean loginBean = new NcSessionBean();
//			loginBean.setSession(response.getNcSession());
//			loginBean.setThemeId(LfwRuntimeEnvironment.getThemeId());
//
//			UserVO user = getUser(loginBean.getPk_user());
//			// 校验密码
//			PasswordSecurityLevelVO pwdLevel = PasswordSecurityLevelFinder.getPWDLV(user);
//			IUserPasswordChecker upchecher = (IUserPasswordChecker) NCLocator.getInstance().lookup(IUserPasswordChecker.class.getName());
//			try {
//				//验证密码安全策略
//				upchecher.checkNewpassword(user.getPrimaryKey(), user.getUser_password(),
//						pwdLevel, user.getUser_type());
//			} 
//			catch (Exception be) {
//				LfwLogger.error(be.getMessage(), be);
//				if(pwdLevel != null && pwdLevel.getInvalidateLock())
//					throw new LoginInterruptedException("密码不符合安全策略,请登录NC系统修改!");
//				else if(pwdLevel != null && !pwdLevel.getInvalidateLock()){
//					InteractionUtil.showConfirmDialog("确认对话框", "密码不符合安全策略,继续登陆?");
//				}
//				//不做处理，继续执行
//				else{
//					
//				}
//			}
//			
//			try {
//				IUserConfigQueryService configQryService = (IUserConfigQueryService) NCLocator.getInstance().lookup(IUserConfigQueryService.class.getName());
//				//查找登陆系统ID
//				String sysid = "" + LfwRuntimeEnvironment.getSysId();
//				int sysId = Integer.valueOf(sysid);
//				//设置语言编码
//				UserConfigVO configVo = configQryService.getUserConfigVO(loginBean.getPk_user(), Integer.valueOf(sysId));
//				Map<String, String> extInfo = (Map<String, String>) userVO.getExtInfo();
//				String language = extInfo.get(NcSsoConstant.LANGUAGE);
//				if (configVo != null && configVo.getLanguageid() != null)
//					language = configVo.getLanguageid();
//				// 设置语言为简体中文
//				extInfo.put(NcSsoConstant.LANGUAGE, language);
//			} 
//			catch (LfwBusinessException e) {
//				LfwLogger.error(e.getMessage(), e);
//			}
//			return loginBean;
//		} 
//		else {
//			// 不成功返回失败码和失败信息
//			int loginResult = response.getLoginResult();
//			// 用户已在线,抛出交互异常
//			if (loginResult == ILoginConstants.USER_ALREADY_ONLINE) {
//				// 前台显示确认对话框
//				InteractionUtil.showConfirmDialog("确认对话框", "用户已经在线,确认要强制登录吗?");
//			} 
//			else {
//				String failureStr;
//				try {
//					failureStr = getResultMessage(loginResult, userVO.getUserID());
//				} 
//				catch (BusinessException e) {
//					LfwLogger.error(e);
//					failureStr = e.getMessage();
//				}
//				throw new LoginInterruptedException(failureStr);
//			}
//			return null;
//		}
//	}
//
//	public FormatDocVO getMaskerInfo(NcSessionBean loginBean){
//		FormatDocVO formatVO = null;
//		try {
//			UserVO user = getUser(loginBean.getPk_user());
//			// 先取用户个性化中心设置的格式
//			if (user != null) {
//				String pkUser = user.getPrimaryKey();
//				String pkGroup = user.getPk_group();
//				String pkFormat = LangSettingAccessor.getDefaultDataFormat(
//						pkUser, pkGroup);
//
//				if (pkFormat != null) {
//					IFormatQueryService service = getFormatQryService();
//					try {
//						formatVO = service.queryByPk(pkFormat);
//						if (formatVO != null) {
//							return formatVO;
//						}
//					} catch (BusinessException e) {
//						throw new LfwRuntimeException(e);
//					}
//				}
//			}
//
//			// 再取用户本身设置的格式
//			if (user != null && user.getFormat() != null) {
//				IFormatQueryService service = getFormatQryService();
//				try {
//					formatVO = service.queryByPk(user.getFormat());
//				} catch (BusinessException e) {
//					throw new LfwRuntimeException(e);
//				}
//			}
//			// 最后数据格式节点的默认格式
//			else
//				try {
//					formatVO = getDefaultMaskerInfo();
//				} catch (BusinessException e) {
//					throw new LfwRuntimeException(e);
//				}
//		} catch (Exception e) {
//			LfwLogger.error(e.getMessage(), e);
//			formatVO = null;
//		}
//		
//		return formatVO;
//	}
//	
//	/**
//	 * 获得格式化信息查询服务
//	 * @return
//	 */
//	private static IFormatQueryService getFormatQryService() {
//		return NCLocator.getInstance().lookup(IFormatQueryService.class);
//	}
//	/**
//	 * 获得系统设置的默认的格式化配置信息
//	 * 
//	 * @return
//	 * @throws BusinessException
//	 */
//	private static FormatDocVO getDefaultMaskerInfo() throws BusinessException {
//		try {
//			IFormatQueryService service = getFormatQryService();
//			return service.queryDefaultFormatVO();
//		} catch (Exception e) {
//			LfwLogger.error(e.getMessage(),e);
//			return null;
//		}
//	}
//	/**
//	 * 获得当前登陆的用户VO
//	 * 
//	 * @return
//	 */
//	private static UserVO getUser(String pkUser) {
//		if (pkUser != null) {
//			try {
//				return SFServiceFacility.getUserQueryService().getUser(pkUser);
//			} catch (BusinessException e) {
//				LfwLogger.error(e.getMessage(), e);
//			}
//		}
//		return null;
//	}
//	public static IAConfEntry getConfEntry(String userId) throws BusinessException{
//		IIdentitiVerifyService verifyServ = NCLocator.getInstance().lookup(IIdentitiVerifyService.class);
//		IAConfEntry entry = verifyServ.getIAModeVOByUser(userId);
//		return entry;
//	}
//
//	public static String getResultMessage(int intResult, String userId) throws BusinessException{
//		return translateMessage(intResult, getConfEntry(userId).getResultMsgHandlerClsName());
//	}
//	
//	public static String translateMessage(int intResult,String resultclass){
//		String resultstr = "some error occur,result NO. is " + intResult;
//		if (intResult < 200) {
//			resultstr = NCLangRes4VoTransl.getNCLangRes().getString("loginui", resultstr, "loginresult-" + intResult);
//		} else if (intResult >= 200) {
//			resultstr = getUserDefMSG(intResult,resultclass);
//		}
//		return resultstr;
//	}
//	
//	private static String getUserDefMSG(int intResult,String resultclass){
//		IResultMessageHandler handler = getClientHandler(resultclass);
//		if(handler==null){
//			return "undefined login result code "+intResult;
//		}else{
//			String message = handler.getResultMessage(intResult);
//			if(message==null||message.trim().length()==0)
//				message = "undefined login result code "+intResult+"###"+resultclass;
//			return message;
//		}
//	}
//	
//	private static IResultMessageHandler getClientHandler(String resultclass){
//		IResultMessageHandler handler = (IResultMessageHandler) LfwClassUtil.newInstance(resultclass);
//		return handler;
//	}
//
//	public ILoginSsoService<NcSessionBean> getLoginSsoService() {
//		return new DefaultNcSsoServiceImpl();
//	}
//
//	@SuppressWarnings("unchecked")
//	public Cookie[] getCookies(AuthenticationUserVO userVO) {
//		List<Cookie> list = new ArrayList<Cookie>();
//		String userId = userVO.getUserID();
//		Map<String, String> extMap = (Map<String, String>) userVO.getExtInfo();
//		String account = extMap.get(NcSsoConstant.ACCOUNT_CODE);
//		String language = extMap.get(NcSsoConstant.LANGUAGE);
//		String maxwin = extMap.get(NcSsoConstant.MAXWIN);
//	
//		String themeId = LfwRuntimeEnvironment.getLfwSessionBean().getThemeId();
//		String sysId = "" + LfwRuntimeEnvironment.getSysId();
//		// 保存Cookie
//		Cookie uc = new Cookie(CookieConstant.USER_CODE + sysId, userId);
//		uc.setPath("/");
//		uc.setMaxAge(CookieConstant.MAX_AGE);
//		list.add(uc);
//		Cookie ac = new Cookie(CookieConstant.ACCOUNT_CODE + sysId, account);
//		ac.setPath("/");
//		ac.setMaxAge(CookieConstant.MAX_AGE);
//		list.add(ac);
//		Cookie lc = new Cookie(CookieConstant.LANG_CODE + sysId, language);
//		lc.setPath("/");
//		lc.setMaxAge(CookieConstant.MAX_AGE);
//		list.add(lc);
//		Cookie mc = new Cookie(CookieConstant.MAX_WIN + sysId, maxwin);
//		mc.setPath("/");
//		mc.setMaxAge(CookieConstant.MAX_AGE);
//		list.add(mc);
//		Cookie tc = new Cookie(CookieConstant.THEME_CODE + sysId, themeId);
//		tc.setPath("/");
//		tc.setMaxAge(CookieConstant.MAX_AGE);
//		list.add(tc);
//		
//		return list.toArray(new Cookie[0]);
//	}
//
//	public String getSysType() {
//		return null;
//	}
//
//	public LfwWidget getCurrentWidget() {
//		if(currentWidget == null){
//			currentWidget = EventRequestContext.getLfwPageContext().getWidgetContext("main").getWidget();
//		}
//		return currentWidget;
//	}
//	
//	/**
//	 * 普通登录
//	 * 
//	 * @param req
//	 * @param res
//	 * @param bl
//	 * @return
//	 * @throws BusinessException
//	 */
//	@SuppressWarnings("unchecked")
//	private LoginResponse doLogin(AuthenticationUserVO userVO) throws BusinessException {
//		String userId = userVO.getUserID();
//		String passwd = userVO.getPassword();
//		Map<String, String> extInfo = (Map<String, String>) userVO.getExtInfo();
//		String accountCode = extInfo.get(NcSsoConstant.ACCOUNT_CODE);
//		
//		// 设置语言为简体中文
//		String language = extInfo.get(NcSsoConstant.LANGUAGE);
//		boolean force = extInfo.get(NcSsoConstant.FORCE).equals("true");
//
//		Logger.debug("客户端传入信息:userName:" + userId + ";language=" + language + " ;accountCode=" + accountCode);
//
//		LoginRequest request = new LoginRequest();
//		request.setUserCode(userId);
//		request.setUserPWD(passwd);
//		request.setBusiCenterCode(accountCode);
//		IBusiCenterManageService busiCenter = NCLocator.getInstance().lookup(IBusiCenterManageService.class);
//		BusiCenterVO[] accounts = busiCenter.getBusiCenterVOs();
//		for(BusiCenterVO b : accounts){
//			if(accountCode.equals(b.getCode())){
//				LfwRuntimeEnvironment.setDatasource(b.getDataSourceName());
//				break;
//			}
//		}
//		
//		LoginResponse response = doLoginImpl(request, force);
//		return response;
//	}
//
//	
//	/**
//	 * 登陆信息，返回Object数组
//	 * 
//	 * @param lsb
//	 * @return
//	 * @throws BusinessException
//	 */
//	private LoginResponse doLoginImpl(LoginRequest request, boolean forceLogin) throws BusinessException {
//		INCLoginService loginService = NCLocator.getInstance().lookup(INCLoginService.class);
//		
//		AuthenSubject subject = new AuthenSubject();
//		subject.setBusiCenterCode(request.getBusiCenterCode());
//		subject.setUserCode(request.getUserCode());
//		subject.setUserPWD(request.getUserPWD());
//		request.putAttachProp(AuthenSubject.class.getName(), subject);
//		//查找登陆系统ID
//		String sysId = "" + LfwRuntimeEnvironment.getSysId();
//		LoginVerifyBean verifyBean = new LoginVerifyBean(sysId);
//		verifyBean.setStaticPWDVerify(true);
//		//验证nc登陆
//		LoginResponse response = loginService.otherSystemLogin(request, verifyBean, forceLogin);
//		return response;
//	}
}

