package nc.uap.lfw.core;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Web Framework core controller, all request dispatched here.
 * if the request is Ajax request, the Request Handler will be called
 * else directly dispatch to the page 
 * @author dengjt
 * 2007-1-10
 * 
 * @modified by dengjt 2007-11-08  add json support 
 */
public class LfwCoreController{

	//private IModuleObjectPool moduleObjectPool;
	public void handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//对于path不为空,即 ctx/xx/xx.jsp形式的请求，统一派发到相应页面。否则认为是ajax请求
		String path = req.getPathInfo();
		ControlPlugin plugin = getControlPlugin(req, res, path);
		plugin.handle(req, res, path);
	}

	protected ControlPlugin getControlPlugin(HttpServletRequest req, HttpServletResponse res, String path) {
		return PresentPluginFactory.getControlPlugin(path);
	}
}
