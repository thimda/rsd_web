package nc.uap.lfw.core.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.serializer.impl.LfwJsonSerializer;

import org.json.JSONObject;
import org.json.JSONRPCResult;

/**
 * Json帮助类，提供Json响应（copy from json servlet），以及服务注册支持
 * 
 * @author dengjt
 * 
 */
public final class RpcHelper {
	public static String processJsonRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String pageId = request.getParameter("pageId");
		// HttpSession session = request.getSession();
		// WebSession ws =
		// LfwRuntimeEnvironment.getWebContext().getWebSession();
		// JSONRPCBridge json_bridge = (JSONRPCBridge)
		// session.getAttribute(pageId + "_JSONRPCBridge");
		// if(json_bridge == null) {
		// json_bridge = new JSONRPCBridge();
		// session.setAttribute(pageId + "_JSONRPCBridge", json_bridge);
		// LfwLogger.info("Created a bridge for this pageId:" + pageId);
		// }

		OutputStream out = response.getOutputStream();

		String charset = request.getCharacterEncoding();
		if (charset == null)
			charset = "UTF-8";

		String data = request.getParameter("rpcdata");

		JSONObject jsonReq = null;
		JSONRPCResult jsonRes = null;
		try {
			jsonReq = new JSONObject(data);
			return call(jsonReq);
		} catch (ParseException e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException("远程调用出错," + e.getMessage());
		}

	}

	private static String call(JSONObject jsonReq) {
		// Get method name, arguments and request id
		String className = (String) jsonReq.get("rpcname");
		String methodName = jsonReq.getString("method");
		List<String> paramList = new ArrayList<String>();
		for(int i = 0; i < 10; i ++){
			try{
				String params = jsonReq.getString("params" + i);
				paramList.add(params);
			}
			catch(NoSuchElementException e){
				break;
			}
		}
		int paramCount = paramList.size();
		Object[] paramObjs = new Object[paramCount];
		for (int i = 0; i < paramCount; i++) {
			paramObjs[i] = LfwJsonSerializer.getInstance().fromJsObject(paramList.get(i));
		}
		Class[] paramTypes = new Class[paramCount];
		for (int i = 0; i < paramTypes.length; i++) {
			paramTypes[i] = paramObjs[i].getClass();
		}
		Object instance = NCLocator.getInstance().lookup(className);
		try {
			Method method = instance.getClass().getMethod(methodName, paramTypes);
			Object obj = method.invoke(instance, paramObjs);
			if(obj != null)
				return LfwJsonSerializer.getInstance().toJsObject(obj);
			return null;
		} 
		catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}
	}

}
