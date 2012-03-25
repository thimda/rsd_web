package nc.uap.applet;

import java.awt.BorderLayout;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import javax.swing.JPanel;

import nc.bcmanage.vo.BusiCenterVO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.desktop.ui.WorkbenchEvent;
import nc.desktop.ui.WorkbenchGenerator;
import nc.desktop.ui.WorkbenchListener;
import nc.desktop.ui.console.NCConsoleFrame;
import nc.funcnode.bs.FuncletModel;
import nc.funcnode.bs.IOpenNodeRCService;
import nc.funcnode.ui.FuncletContext;
import nc.funcnode.ui.FuncletInitData;
import nc.funcnode.ui.FuncletWidgetContainer;
import nc.itf.uap.bbd.func.IFuncRegisterQueryService;
import nc.login.vo.AttachedProps;
import nc.login.vo.LoginResponse;
import nc.login.vo.NCSession;
import nc.uap.lfw.core.log.LfwLogger;
import nc.ui.ml.NCLangRes;
import nc.vo.org.GroupVO;
import nc.vo.pub.BusinessException;
import nc.vo.sm.funcreg.FuncRegisterVO;

public abstract class MockWorkbench extends JPanel implements WorkbenchListener{
	private static final long serialVersionUID = 6582494708941810648L;

	public MockWorkbench(LoginResponse response) {
		NCConsoleFrame.getInstance().redirectOutputStreamAsNeed();
		initWorkbenchEnvironment(response);
		initUI();
		NCConsoleFrame.getInstance().setDefaultLogLevel();
	}

	protected FuncRegisterVO getFuncRegisterVO() {
		IFuncRegisterQueryService qryService = NCLocator.getInstance().lookup(IFuncRegisterQueryService.class);
		FuncRegisterVO reg = null;
		try {
			String funcCode =  getFuncCode();
			reg = qryService.queryFunctionByCode(funcCode);
		} 
		catch (BusinessException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		return reg;
	}
	
	protected abstract String getFuncCode();
	
	protected void initUI() {
		this.setLayout(new BorderLayout());
		try {
			FuncRegisterVO reg = getFuncRegisterVO();
			
			FuncletModel funcletModel = getFuncModel(reg);
			
			FuncletContext context = new FuncletContext(reg);
			if(funcletModel != null){
				context.setFuncletModel(funcletModel);
			}
			
			Class<FuncletWidgetContainer> c = FuncletWidgetContainer.class;
			Constructor<FuncletWidgetContainer> cons = c.getConstructor(new Class[]{FuncRegisterVO.class});
			FuncletWidgetContainer fwContainer = cons.newInstance(reg);
			Method m = c.getDeclaredMethod("initUI", new Class[]{FuncletModel.class, FuncletInitData.class});
			m.setAccessible(true);
			m.invoke(fwContainer, new Object[]{funcletModel, null});
			
//			FuncletWidget widget = new FuncletWidget();
//			context.addPropertyChangListener(widget.fwPropertyChangeHandler);
//			IFunclet funclet = newFuncletInstance(reg.getClass_name());
//			String msg = funclet.preOpenCheck(reg);
//			if(msg != null){
//				throw new PreOpenCheckException(msg);
//			}
//			
//			FuncletWidget widget = new FuncletWidget();
//			widget.add((Component) funclet, BorderLayout.CENTER);
//			funclet.init(context);
//			funclet.initData(null);
//			widget.addFuncletToWidget(funclet);
//			return widget;
//			
//			FuncletContext ctx = new FuncletContext(reg);
//			ToftPanelAdaptor a = new ToftPanelAdaptor();
//			a.init(ctx);
			
			
			this.add(fwContainer, BorderLayout.CENTER);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			LfwLogger.error(e.getMessage(), e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LfwLogger.error(e.getMessage(), e);
		}
	}

	protected FuncletModel getFuncModel(FuncRegisterVO reg) throws Exception {
		FuncletModel funcletModel = null;
		AttachedProps response = fetchOpenNodeRCResponse(reg);
		
		funcletModel = response.getAttachedProp(FuncletModel.class);
		return funcletModel;
	}
	
//	private static IFunclet newFuncletInstance(String clsName) throws Exception{
//		IFunclet funclet = null;
//		try {
//			Class<?> cls = Class.forName(clsName);
//			if(IFunclet.class.isAssignableFrom(cls)){
//				funclet = (IFunclet) cls.newInstance();
//			}else if(JComponent.class.isAssignableFrom(cls)){
//				JComponent comp = (JComponent)cls.newInstance();
//				funclet = new FuncletWrapper(comp);
//			}else{
//				throw new Exception("Must implements "+IFunclet.class.getName()+" :"+clsName);
//			}
//		} catch (Exception e) {
//			Logger.error(e.getMessage(), e);
//			throw e;
//		}
//		return funclet;
//	}
	
	private AttachedProps fetchOpenNodeRCResponse(FuncRegisterVO frVO) throws Exception {
		AttachedProps request = getOpenNodeRCRequest(frVO);
		try {
			IOpenNodeRCService service = NCLocator.getInstance().lookup(IOpenNodeRCService.class);
			AttachedProps response = service.unitedOpenNodeRC(request);
			return response;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	private AttachedProps getOpenNodeRCRequest(FuncRegisterVO frVO) {
		WorkbenchEnvironment env = WorkbenchEnvironment.getInstance();
		String userId = env.getLoginUser().getPrimaryKey();
		NCSession session = env.getSession();
		GroupVO groupVO = env.getGroupVO();
		String dsName = "";
		BusiCenterVO bcVO = env.getLoginBusiCenter();
		if (bcVO != null) {
			dsName = bcVO.getDataSourceName();
		}
		AttachedProps request = new AttachedProps();
		request.putAttachProp(frVO);
		request.putAttachProp("dsName", dsName);
		request.putAttachProp("userID", userId);
		request.putAttachProp("sessionID", session.getSessionID());
		request.putAttachProp("pkGroup", groupVO == null ? "" : groupVO.getPk_group());
		request.putAttachProp(env.getUserType());
		//request.putAttachProp("busiActiveCodeArray", openParam.busiActiveCodes);
		return request;
	}

	static void initWorkbenchEnvironment(LoginResponse response) {
		Class<WorkbenchGenerator> c = WorkbenchGenerator.class;
		try {
			Method m = c.getDeclaredMethod("initWorkbenchEnvironment", new Class[]{LoginResponse.class});
			m.setAccessible(true);
			m.invoke(null, response);
		} 
		catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
		
		NCLangRes.getInstance().setCurrLanguage(NCLangRes.getInstance().getAllLanguages()[0]);
		//setCurrLanguage(NCLangRes.getInstance().getAllLanguages()[0]);
//		WorkbenchEnvironment environment = null;
//		try {
//			Logger.debug("start initialize workbench environment");
//			long start = System.currentTimeMillis();
//			environment = WorkbenchEnvironment.newInstance();
//			AttachedProps props = response.getAttachedProps();
//			//
//			environment.setAttachProps(props);
//			// 业务中心
//			BusiCenterVO bcVO = (BusiCenterVO) props.getAttachedProp(BusiCenterVO.class.getName());
//			environment.setLoginBusiCenter(bcVO);
//			// 用户
//			UserVO user = (UserVO) props.getAttachedProp(UserVO.class.getName());
//			environment.setLoginUser(user);
//			NCSession session = response.getNcSession();
//			int userType = session.getUserType();
//			environment.setUserType(userType);
//			environment.setSession(session);
//			UserExit.getInstance().setUserId(session.getUserID());
//			// System.setProperty("loginUserType", userType+"");
//
//			// 集团
//			GroupVO groupVO = (GroupVO) props.getAttachedProp(GroupVO.class.getName());
//			environment.setGroupVO(groupVO);
//			GroupVO[] powerGroups = (GroupVO[]) props.getAttachedProp(GroupVO.class.getName() + "powers");
//			environment.setPowerGroupVOs(powerGroups);
//			if (groupVO != null) {
//				UserExit.getInstance().setGroupId(groupVO.getPk_group());
//				UserExit.getInstance().setGroupNumber(groupVO.getGroupno());
//			} else {
//				UserExit.getInstance().setGroupId("0001");
//				UserExit.getInstance().setGroupNumber("0001");
//			}
//			// 权限
//
////			FuncRegisterVO[] powers = UserMenuPermissionHelper.getInstance().getUserPowerFuncNodeByType(userType, user.getPrimaryKey());
//			MenuItem[] powerItems = UserMenuPermissionHelper.getInstance().getUserPowerMenuItemByType(userType, user.getPrimaryKey());
//			environment.setUserPowers(powerItems);
//
//			//
//			long end = System.currentTimeMillis();
//			Logger.debug("end initialize workbench environment,cost " + (end - start) + "ms");
//		} catch (Throwable th) {
//			Logger.error(th.getMessage(), th);
//		} finally {
//			if(environment != null){
//				environment.setInited(true);
//			}
//		}
	}

	public void enterWorkbench(WorkbenchEvent event) {
		
	}

	public void logoutWorkbench(WorkbenchEvent event) {
		
	}

}
