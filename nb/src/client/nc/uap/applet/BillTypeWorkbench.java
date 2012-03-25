package nc.uap.applet;

import nc.login.vo.LoginResponse;

public class BillTypeWorkbench extends MockWorkbench {

	private static final long serialVersionUID = -1253668342682345648L;

	public BillTypeWorkbench(LoginResponse response) {
		super(response);
	}

	
	@Override
	protected String getFuncCode() {
		return "10222005";
	}

}
