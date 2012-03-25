package nc.uap.applet;

import java.awt.BorderLayout;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JPanel;

import nc.login.vo.LoginRequest;
import nc.login.vo.LoginResponse;
import nc.uap.lfw.core.log.LfwLogger;
import nc.vo.ml.Language;

public class MockUI extends JPanel {
	private static final long serialVersionUID = -8565884682941318095L;
//	
	public MockUI() {
		super();
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		initUI();
	}
	public void initUI(){
		removeAll();
		LoginResponse response = mockEnviroment();
		JPanel panel = getMockPanel(response);
		add(panel,BorderLayout.CENTER);
	}
	
	private LoginResponse mockEnviroment() {
		LoginRequest request = getLoginRequest();
		LoginAssistant loginAssi = new LoginAssistant(request);
		try {
			LoginResponse response = loginAssi.login();
			return response;
		} 
		catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
		return null;
	}
	
	private LoginRequest getLoginRequest(){
		LoginRequest request = new LoginRequest();
		String BC = System.getProperty("BC");
		String user = System.getProperty("U");
		String PWD = System.getProperty("P");
		request.setBusiCenterCode(BC);
		request.setUserCode(user);
		request.setUserPWD(PWD);
		
		LfwLogger.debug("USER:" + user + ",PASSWORD:" + PWD + ",BUSICENTER:" + BC);
		//System.out.println("USER:" + user + ",PASSWORD:" + PWD + ",BUSICENTER:" + BC);
		request.setLangCode(Language.SIMPLE_CHINESE_CODE);
		return request;
	}
	
	
	private JPanel getMockPanel(LoginResponse response) {
		String workbenchClazz = System.getProperty("workbench_clazz");
		if(workbenchClazz == null)
			throw new IllegalArgumentException("没有指定打开的界面类");
		try {
			Class c = Class.forName(workbenchClazz);
			Constructor<MockWorkbench> construct =  c.getConstructor(new Class[]{LoginResponse.class});
			MockWorkbench workbench = (MockWorkbench)construct.newInstance(response);
			return workbench;
		} 
		catch (ClassNotFoundException e) {
			LfwLogger.error(e.getMessage(), e);
		} 
		catch (InstantiationException e) {
			LfwLogger.error(e.getMessage(), e);
		} 
		catch (IllegalAccessException e) {
			LfwLogger.error(e.getMessage(), e);
		} catch (SecurityException e) {
			LfwLogger.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			LfwLogger.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			LfwLogger.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		return null;
	}
}