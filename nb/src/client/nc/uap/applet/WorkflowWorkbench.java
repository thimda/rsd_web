package nc.uap.applet;

import nc.login.vo.LoginResponse;

public class WorkflowWorkbench extends MockWorkbench{
	private static final long serialVersionUID = 6582494708941810648L;

	public WorkflowWorkbench(LoginResponse response) {
		super(response);
	}
	
	protected String getFuncCode() {
		return "101605";
	}

	protected void initUI() {
		super.initUI();
	}

}
