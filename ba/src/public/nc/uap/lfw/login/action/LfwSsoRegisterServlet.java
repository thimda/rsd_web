package nc.uap.lfw.login.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.servlet.LfwServletBase;
import nc.uap.lfw.login.authfield.ExtAuthField;
import nc.uap.lfw.login.itf.AbstractLfwIntegrateProvider;
import nc.uap.lfw.login.itf.ILfwIntegrationHandler;
import nc.uap.lfw.login.itf.ILfwSsoService;
import nc.uap.lfw.login.itf.LoginInterruptedException;
import nc.uap.lfw.login.listener.AuthenticationUserVO;
import nc.uap.lfw.login.util.LfwLoginFetcher;
import nc.uap.lfw.login.vo.LfwFunNodeVO;
import nc.uap.lfw.login.vo.LfwSsoRegVO;

import org.apache.commons.io.IOUtils;

/**
 * LFW单点信息注册Servlet
 * 
 * @author licza
 * 
 */
public class LfwSsoRegisterServlet extends LfwServletBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2037510067035600680L;

	static final int TYPE_AUTHFIELD = 0;
	static final int TYPE_AUTH = 1;
	static final int TYPE_REGISTER = 2;
	static final int TYPE_USER_FUNNODE = 3;
	static final int TYPE_ALLFUNNODE = 4;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		resp.setHeader("Pragma", "No-cache");
		resp.setDateHeader("Expires", 0);
		resp.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = resp.getWriter();
		try {
			int type = Integer.parseInt(req.getParameter("type"));
			String dsName = req.getParameter("dsname");
			//设置传入的默认数据源
			if(dsName != null)
				LfwRuntimeEnvironment.setDatasource(dsName);
			if (type == TYPE_AUTH)
				doAuth(req, pw);
			if (type == TYPE_REGISTER)
				doReg(req, pw);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e.getCause());
			pw.write(e.getMessage());
		} finally {
			pw.flush();
			IOUtils.closeQuietly(pw);
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		InputStream ins = req.getInputStream();
		OutputStream out = resp.getOutputStream();
		ObjectInputStream objInput = new ObjectInputStream(ins);
		ObjectOutputStream objOutput = new ObjectOutputStream(out);
		try {
			int type = objInput.readInt();
			String dsName = objInput.readUTF();
			if(dsName != null)
				LfwRuntimeEnvironment.setDatasource(dsName);
			if (type == TYPE_AUTHFIELD)
				getAuthFields(objInput, objOutput);
			if (type == TYPE_USER_FUNNODE)
				getUserNodes(objInput, objOutput);
			if (type == TYPE_ALLFUNNODE)
				getAllNodes(objInput, objOutput);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e.getCause());
			objOutput.writeObject(e);
		} finally {
			IOUtils.closeQuietly(objInput);
			IOUtils.closeQuietly(objOutput);
		}
	}

	/**
	 * 获得用户的功能节点
	 * 
	 * @param in
	 * @param out
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	void getUserNodes(ObjectInputStream in, ObjectOutputStream out)
			throws Exception {
		Map<String, String> param = (Map<String, String>)in.readObject();
		LfwFunNodeVO[] all = getLoginProvider().getFunNodes(param);
		out.writeObject(all);
	}

	/**
	 * 获得全部的功能节点
	 * 
	 * @param in
	 * @param out
	 * @throws Exception
	 */
	void getAllNodes(ObjectInputStream in, ObjectOutputStream out)
			throws Exception {
		LfwFunNodeVO[] all = getLoginProvider().getFunNodes();
		out.writeObject(all);
	}

	/**
	 * 获得验证字段
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	void getAuthFields(ObjectInputStream in, ObjectOutputStream out)
			throws Exception {
		ExtAuthField[] eafs = getLoginProvider().getAuthFields();
		out.writeObject(eafs);
	}

	/**
	 * 验证身份有效性
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws LoginInterruptedException
	 */

	void doAuth(HttpServletRequest req, Writer out) throws Exception {
		validate();
		Map<String, String> p = getParameterMap(req);
		try {
			ILfwIntegrationHandler handler = getLoginHandler();
			AuthenticationUserVO authVO = handler.getSsoAuthenticateVO(p);
			handler.verify(authVO);
			out.write("t:true");
		} catch (Exception e) {
			out.write("f:");
			out.write(e.getMessage());
		}
	}

	/**
	 * 获得Request参数Map
	 * 
	 * @param req
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String, String> getParameterMap(HttpServletRequest req) {
		HashMap<String, String> p = new HashMap<String, String>();
		Enumeration<String> e = req.getParameterNames();
		while (e.hasMoreElements()) {
			String key = e.nextElement();
			p.put(key, req.getParameter(key));
		}
		return p;
	}

	/**
	 * 注册登陆信息
	 * 
	 * @param in
	 * @param out
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	void doReg(HttpServletRequest req, Writer out) throws Exception {
		validate();
		HashMap p = getParameterMap(req);
		LfwSsoRegVO vo = new LfwSsoRegVO();
		vo.doSetRegmap(p);
		String key = UUID.randomUUID().toString();
		vo.setSsokey(key);
		try {
			getSsoService().creatSsoInfo(vo);
			out.write("t:");
			out.write(key);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
			out.write("f:");
			out.write(e.getMessage());
		}
	}

	/**
	 * 获得单点登陆服务
	 * 
	 * @return
	 */
	protected ILfwSsoService getSsoService() {
		return NCLocator.getInstance().lookup(ILfwSsoService.class);
	}

	/**
	 * 校验请求合法性
	 * 
	 * @throws IllegalAccessException
	 */
	protected void validate() throws IllegalAccessException {
		// WebContext ctx = LfwRuntimeEnvironment.getWebContext();
		// String host = ctx.getRequest().getRemoteHost();
		if (false)
			throw new IllegalAccessException();
	}

	/**
	 * 获得集成处理类
	 * 
	 * @return
	 */
	private ILfwIntegrationHandler getLoginHandler() {
		return getLoginProvider().getLoginHelper().createIntegrationHandler();
	}

	/**
	 * 获得登陆配置类
	 * 
	 * @return
	 */
	private AbstractLfwIntegrateProvider getLoginProvider() {
		return LfwLoginFetcher.getGeneralInstance();
	}
}
