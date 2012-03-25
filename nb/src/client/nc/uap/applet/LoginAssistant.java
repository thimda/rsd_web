package nc.uap.applet;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JApplet;

import nc.bs.framework.common.NCLocator;
import nc.desktop.ui.Workbench;
import nc.desktop.ui.WorkbenchGenerator;
import nc.identityverify.bs.itf.IIdentitiVerifyService;
import nc.identityverify.itf.IClientHandler;
import nc.identityverify.vo.IAConfEntry;
import nc.identityverify.vo.IAConstant;
import nc.login.bs.INCLoginService;
import nc.login.identify.ui.ClientHandlerFactory;
import nc.login.vo.ILoginConstants;
import nc.login.vo.LoginRequest;
import nc.login.vo.LoginResponse;
import nc.sfbase.client.ClientToolKit;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;

public class LoginAssistant {
	
	protected LoginRequest request = null;
	private IAConfEntry entry = null;

	public LoginAssistant(LoginRequest request){
		super();
		this.request = request;		
	}

	public LoginResponse login() throws Exception {		
		validateRequest();
		LoginResponse response = loginImple(true);
		int resultCode = response.getLoginResult();
		if (resultCode == ILoginConstants.USER_ALREADY_ONLINE) {
			if (MessageDialog.showOkCancelDlg(ClientToolKit.getApplet(), NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000058")/*提示*/, 
					NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000059")/*该用户已在线，是否强制登录？*/) == MessageDialog.ID_OK) {
				response = loginImple(true);

			}
		}
		return response;
	}
	
	private void processClientHandler() throws Exception {
		IClientHandler handler = ClientHandlerFactory.createClientHandler(getConfEntry());
		Object obj = null;//handler.handle(LoginInfoFactory.createLoginInfo(request));
		request.putAttachProp(IAConstant.HANDLERRESULT, obj);
	}

	protected void validateRequest() throws Exception {
		String userCode = request.getUserCode();
		if (userCode == null || userCode.equals("")) {
			throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000231")/*您没有输入用户编码！*/);
		}
	}

	private LoginResponse loginImple(boolean isForceLogin) throws Exception {
		processClientHandler();	
		INCLoginService loginService = NCLocator.getInstance().lookup(INCLoginService.class);
		LoginResponse response = loginService.login(request, isForceLogin);
		return response;
	}



	public IAConfEntry getConfEntry() throws Exception{
		if(entry==null){
			IIdentitiVerifyService verifyServ = NCLocator.getInstance().lookup(IIdentitiVerifyService.class);
			entry = verifyServ.getIAModeVOByUser(request.getUserCode());
		}			
		return entry;
	}

	public String getResultMessage(int intResult) throws Exception {
		return "" + intResult;
		//return ResultMSGTranslator.translateMessage(intResult, getConfEntry().getResultmessagehandler());
	}

	public void showWorkbench(LoginResponse response) {
		Workbench workbench = WorkbenchGenerator.generatorNewWorkbench(response);
		JApplet applet = ClientToolKit.getApplet();
		Container con = applet.getContentPane();
		con.removeAll();
		con.setLayout(new BorderLayout());
		con.add(workbench,BorderLayout.CENTER);
		con.invalidate();
		con.validate();
		con.repaint();
	}

}
