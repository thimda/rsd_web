package nc.uap.lfw.core.util;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.log.LfwLogger;

import org.json.JSONObject;
import org.json.JSONRPCBridge;
import org.json.JSONRPCResult;


/**
 * Json帮助类，提供Json响应（copy from json servlet），以及服务注册支持
 * @author dengjt
 *
 */
public final class JsonHelper {
	public static void processJsonRequest(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String pageId = request.getParameter("pageId");
		HttpSession session = request.getSession();
//		WebSession ws = LfwRuntimeEnvironment.getWebContext().getWebSession();
		JSONRPCBridge json_bridge = (JSONRPCBridge) session.getAttribute(pageId + "_JSONRPCBridge");
		if(json_bridge == null) {
		   json_bridge = new JSONRPCBridge();
		   session.setAttribute(pageId + "_JSONRPCBridge", json_bridge);
		   LfwLogger.info("Created a bridge for this pageId:" + pageId);
		}

		OutputStream out = response.getOutputStream();

		String charset = request.getCharacterEncoding();
		if(charset == null) 
			charset = "UTF-8";
		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), charset));

		int bufSize = 4096;
		// Read the request
	    CharArrayWriter data = new CharArrayWriter();
	    char buf[] = new char[bufSize];
	    int ret;
	    while((ret = in.read(buf, 0, bufSize)) != -1)
	    	data.write(buf, 0, ret);
		LfwLogger.info("recieve: " + data.toString());

		JSONObject jsonReq = null;
		JSONRPCResult jsonRes = null;
		try {
		    jsonReq = new JSONObject(data.toString());
		    jsonRes = json_bridge.call(new Object[] {request}, jsonReq);
		} 
		catch (ParseException e) {
		    LfwLogger.error(e);
		    jsonRes = new JSONRPCResult(JSONRPCResult.CODE_ERR_PARSE, null, JSONRPCResult.MSG_ERR_PARSE);
		}

		// Write the response
		LfwLogger.info("send: " + jsonRes.toString());
		byte[] bout = jsonRes.toString().getBytes("UTF-8");
		response.setIntHeader("Content-Length", bout.length);

		out.write(bout);
		out.flush();
		out.close();
	}
	
	public static void registerService(HttpServletRequest request, String pageId, Map<String, Object> map) {
		if(map == null || map.size() == 0)
			return;
		HttpSession ws = request.getSession();
//		WebSession ws = LfwRuntimeEnvironment.getWebContext().getWebSession();
		JSONRPCBridge json_bridge = (JSONRPCBridge) ws.getAttribute(pageId + "_JSONRPCBridge");
		if(json_bridge == null) {
			json_bridge = new JSONRPCBridge();
			ws.setAttribute(pageId + "_JSONRPCBridge", json_bridge);
			LfwLogger.info("Created a bridge for this pageId:" + pageId);
		}
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Object> entry = it.next();
			try {
				Object value = entry.getValue();
				if(value instanceof String){
					String className = (String) value;
					Class c = Class.forName(className);
					//是接口，则认为是nc服务
					if(c.isInterface())
						json_bridge.registerObject(entry.getKey(), NCLocator.getInstance().lookup(className));
					else
						json_bridge.registerObject(entry.getKey(), c.newInstance());
				}
				else{
					json_bridge.registerObject(entry.getKey(), value);
				}
			} catch (Exception e) {
				LfwLogger.error(e);
			}
		}
	}
}
