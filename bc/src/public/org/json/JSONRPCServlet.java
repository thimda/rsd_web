/*
 * JSON-RPC-Java - a JSON-RPC to Java Bridge with dynamic invocation
 *
 * $Id: JSONRPCServlet.java,v 1.20.2.2 2006/03/06 12:39:21 mclark Exp $
 *
 * Copyright Metaparadigm Pte. Ltd. 2004.
 * Michael Clark <michael@metaparadigm.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

/**
 * This servlet handles JSON-RPC requests over HTTP and hands them to
 * a JSONRPCBridge instance registered in the HttpSession.
 * </p>
 * By default, the JSONRPCServlet places an instance of the JSONRPCBridge
 * object is automatically in the HttpSession object registered under the
 * attribute "JSONRPCBridge".
 * <p />
 * The following can be added to your web.xml to export the servlet
 * under the URI &quot;<code>/JSON-RPC</code>&quot;
 * <p />
 * <pre>
 * &lt;servlet&gt;
 *   &lt;servlet-name&gt;com.metaparadigm.jsonrpc.JSONRPCServlet&lt;/servlet-name&gt;
 *   &lt;servlet-class&gt;com.metaparadigm.jsonrpc.JSONRPCServlet&lt;/servlet-class&gt;
 * &lt;/servlet&gt;
 * &lt;servlet-mapping&gt;
 *   &lt;servlet-name&gt;com.metaparadigm.jsonrpc.JSONRPCServlet&lt;/servlet-name&gt;
 *   &lt;url-pattern&gt;/JSON-RPC&lt;/url-pattern&gt;
 * &lt;/servlet-mapping&gt;
 * </pre>
 * <p />
 * You can disable the automatic creation of a JSONRPCBridge in the session
 * by placing the XML below into your web.xml inside the &lt;servlet&gt;
 * element. If you do this, you can add one to the session yourself. If it
 * is disabled, and you have not added one to the session, only the global
 * bridge will be available.
 * <p />
 * <pre>
 * &lt;init-param&gt;
 *   &lt;param-name&gt;auto-session-bridge&lt;/param-name&gt;
 *   &lt;param-value&gt;0&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * </pre>
 * To disable keepalives to workaround issues with certain web containers
 * and configurations of apache / connectors place the following XML into
 * your web.xml inside the &lt;servlet&gt; element.
 * <p />
 * <pre>
 * &lt;init-param&gt;
 *   &lt;param-name&gt;keepalive&lt;/param-name&gt;
 *   &lt;param-value&gt;0&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * </pre>
 */

public class JSONRPCServlet extends HttpServlet
{
    private final static Logger log =
	Logger.getLogger(JSONRPCServlet.class.getName());

    private final static int buf_size = 4096;

    private static boolean auto_session_bridge = true;
    private static boolean keepalive = true;

    public void init(ServletConfig config)
    {
	if("0".equals(config.getInitParameter("auto-session-bridge")))
	    auto_session_bridge = false;
	if("0".equals(config.getInitParameter("keepalive")))
	    keepalive = false;
	log.info("auto_session_bridge=" + auto_session_bridge +
		 ", keepalive=" + keepalive);
    }

    public void service(HttpServletRequest request,
			HttpServletResponse response)
	throws IOException, ClassCastException
    {
	// Find the JSONRPCBridge for this session or create one
	// if it doesn't exist
	HttpSession session = request.getSession();
	JSONRPCBridge json_bridge = null;
	json_bridge = (JSONRPCBridge) session.getAttribute("JSONRPCBridge");
	if(json_bridge == null) {
	    // Only create a new bridge if not disabled in config
	    if(!auto_session_bridge) {
	    	// Use the global bridge only, and don't set on session.
		json_bridge = JSONRPCBridge.getGlobalBridge();
		if (json_bridge.isDebug())
		    log.info("Using global bridge.");
	    } else {
	    	json_bridge = new JSONRPCBridge();
	    	session.setAttribute("JSONRPCBridge", json_bridge);
	    	if(json_bridge.isDebug())
		    log.info("Created a bridge for this session.");
	    }
	}

	// Encode using UTF-8, although We are actually ASCII clean as
	// all unicode data is JSON escaped using backslash u. This is
	// less data efficient for foreign character sets but it is
	// needed to support naughty browsers such as Konqueror and Safari
	// which do not honour the charset set in the response
	response.setContentType("text/plain;charset=utf-8");
	OutputStream out = response.getOutputStream();

	// Decode using the charset in the request if it exists otherwise
	// use UTF-8 as this is what all browser implementations use.
	// The JSON-RPC-Java JavaScript client is ASCII clean so it
	// although here we can correctly handle data from other clients
	// that do not escape non ASCII data
	String charset = request.getCharacterEncoding();
	if(charset == null) charset = "UTF-8";
	BufferedReader in = new BufferedReader
	    (new InputStreamReader(request.getInputStream(), charset));

	// Read the request
        CharArrayWriter data = new CharArrayWriter();
        char buf[] = new char[buf_size];
        int ret;
        while((ret = in.read(buf, 0, buf_size)) != -1)
            data.write(buf, 0, ret);
	if(json_bridge.isDebug())
	    log.fine("recieve: " + data.toString());

	// Process the request
	JSONObject json_req = null;
	JSONRPCResult json_res = null;
	try {
	    json_req = new JSONObject(data.toString());
	    json_res = json_bridge.call(new Object[] {request}, json_req);
	} catch (ParseException e) {
	    log.severe("can't parse call: " + data);
	    json_res = new JSONRPCResult
		(JSONRPCResult.CODE_ERR_PARSE, null,
		 JSONRPCResult.MSG_ERR_PARSE);
	}

	// Write the response
	if(json_bridge.isDebug())
	    log.fine("send: " + json_res.toString());
	byte[] bout = json_res.toString().getBytes("UTF-8");
	if(keepalive) {
	    response.setIntHeader("Content-Length", bout.length);
	}

	out.write(bout);
	out.flush();
	out.close();
    }
}
