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
		//����path��Ϊ��,�� ctx/xx/xx.jsp��ʽ������ͳһ�ɷ�����Ӧҳ�档������Ϊ��ajax����
		String path = req.getPathInfo();
		ControlPlugin plugin = getControlPlugin(req, res, path);
		plugin.handle(req, res, path);
	}

	protected ControlPlugin getControlPlugin(HttpServletRequest req, HttpServletResponse res, String path) {
		return PresentPluginFactory.getControlPlugin(path);
	}
}
