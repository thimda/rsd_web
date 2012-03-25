package nc.uap.applet;

import nc.login.vo.LoginResponse;

public class TemplateWorkbench extends MockWorkbench{
	private static final long serialVersionUID = 6582494708941810648L;

	public TemplateWorkbench(LoginResponse response) {
		super(response);
	}
	
	protected String getFuncCode() {
		return "14100000";
	}

	protected void initUI() {
		super.initUI();
	}

}
