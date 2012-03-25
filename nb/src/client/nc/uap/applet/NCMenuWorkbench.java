package nc.uap.applet;

import nc.login.vo.LoginResponse;

public class NCMenuWorkbench extends MockWorkbench{
	private static final long serialVersionUID = 6582494708941810648L;

	public NCMenuWorkbench(LoginResponse response) {
		super(response);
	}
	
	protected String getFuncCode() {
		return "1410MENURE";
	}

	protected void initUI() {
		super.initUI();
	}

}
