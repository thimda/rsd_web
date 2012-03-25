package nc.uap.lfw.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.lfw.core.util.RpcHelper;

public class RpcControlPlugin implements ControlPlugin {

	@Override
	public void handle(HttpServletRequest req, HttpServletResponse res,
			String path) throws Exception {
		String result = RpcHelper.processJsonRequest(req, res);
		res.getOutputStream().print(result);
	}

}
