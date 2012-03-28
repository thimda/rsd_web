package nc.uap.lfw.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.log.LfwLogger;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * 后台url工具。提供对URL的一系列操作
 * @author dengjt
 *
 */
public class HttpUtil {

	public static final String FILE_ENCODING = "UTF-8";

	public static final String HTTP = "http";

	public static final String HTTPS = "https";

	public static final String HTTP_WITH_SLASH = "http://";

	public static final String HTTPS_WITH_SLASH = "https://";

	public static final int HTTP_PORT = 80;

	public static final int HTTPS_PORT = 443;

	public static final String PROXY_HOST = null;//GetterUtil.getString(PortalProperties.get("portal.proxy.host"));

	public static final int PROXY_PORT = 80;//GetterUtil.getInteger(PortalProperties.get("portal.proxy.port"));

	public static final String PROXY_USERNAME = null;//GetterUtil.getString(PortalProperties.get("portal.proxy.username"));
	
	public static final String PROXY_PASSWORD = null;//GetterUtil.getString(PortalProperties.get("portal.proxy.password"));
	
	public static final String PROXY_DOMAIN = null;//GetterUtil.getString(PortalProperties.get("portal.proxy.domain"));


	public static String decodeURL(String url) {
		if (url == null) {
			return null;
		}

		try {
			return URLDecoder.decode(url, FILE_ENCODING);
		}
		catch (UnsupportedEncodingException uee) {
			LfwLogger.error(uee.getMessage(), uee);;
			return StringPool.BLANK;
		}
	}

	public static String encodeURL(String url) {
		if (url == null) {
			return null;
		}

		try {
			return URLEncoder.encode(url, FILE_ENCODING);
		}
		catch (UnsupportedEncodingException uee) {
			LfwLogger.error(uee.getMessage(), uee);;

			return StringPool.BLANK;
		}
	}
	/**
	 * 返回请求网址及所有参数
	 * @param req
	 * @return
	 */
	public static String getCompleteURL(HttpServletRequest req) {
		StringBuffer completeURL = req.getRequestURL();

		if (completeURL == null) {
			completeURL = new StringBuffer();
		}

		if (req.getQueryString() != null) {
			completeURL.append(StringPool.QUESTION);
			completeURL.append(req.getQueryString());
		}

		return completeURL.toString();
	}

	public static String getProtocol(boolean secure) {
		if (!secure) {
			return HTTP;
		}
		else {
			return HTTPS;
		}
	}

	public static String getProtocol(HttpServletRequest req) {
		return getProtocol(req.isSecure());
	}

/*	public static String getProtocol(ActionRequest req) {
		return getProtocol(req.isSecure());
	}

	public static String getProtocol(RenderRequest req) {
		return getProtocol(req.isSecure());
	}*/

	public static String getRequestURL(HttpServletRequest req) {
		return req.getRequestURL().toString();
	}

	public static String parameterMapToString(Map parameterMap) {
		return parameterMapToString(parameterMap, true);
	}

	public static String parameterMapToString(
		Map parameterMap, boolean addQuestion) {

		StringBuffer sb = new StringBuffer();

		if (parameterMap.size() > 0) {
			if (addQuestion) {
				sb.append(StringPool.QUESTION);
			}

			Iterator itr = parameterMap.entrySet().iterator();

			while (itr.hasNext()) {
				Map.Entry entry = (Map.Entry)itr.next();

				String name = (String)entry.getKey();
				String[] values = (String[])entry.getValue();

				for (int i = 0; i < values.length; i++) {
					sb.append(name);
					sb.append(StringPool.EQUAL);
					sb.append(HttpUtil.encodeURL(values[i]));
					sb.append(StringPool.AMPERSAND);
				}
			}

			sb.deleteCharAt(sb.length() - 1);
		}

		return sb.toString();
	}

	public static String protocolize(String url, boolean secure) {
		if (secure) {
			if (url.startsWith(HTTP_WITH_SLASH)) {
				return StringUtil.replace(
					url, HTTP_WITH_SLASH, HTTPS_WITH_SLASH);
			}
		}
		else {
			if (url.startsWith(HTTPS_WITH_SLASH)) {
				return StringUtil.replace(
					url, HTTPS_WITH_SLASH, HTTP_WITH_SLASH);
			}
		}

		return url;
	}

	public static String protocolize(String url, HttpServletRequest req) {
		return protocolize(url, req.isSecure());
	}
//
//	public static String protocolize(String url, ActionRequest req) {
//		return protocolize(url, req.isSecure());
//	}
//
//	public static String protocolize(String url, RenderRequest req) {
//		return protocolize(url, req.isSecure());
//	}

	public static void submit(String location) throws IOException {
		submit(location, null);
	}

	public static void submit(String location, Cookie[] cookies)
		throws IOException {

		submit(location, cookies, false);
	}

	public static void submit(String location, boolean post)
		throws IOException {

		submit(location, null, post);
	}

	public static void submit(
			String location, Cookie[] cookies, boolean post)
		throws IOException {

		URLtoByteArray(location, cookies, post);
	}

	public static byte[] URLtoByteArray(String location)
		throws IOException {

		return URLtoByteArray(location, null);
	}

	public static byte[] URLtoByteArray(String location, Cookie[] cookies)
		throws IOException {

		return URLtoByteArray(location, cookies, false);
	}

	public static byte[] URLtoByteArray(String location, boolean post)
		throws IOException {
		return URLtoByteArray(location, null, post);
	}

	public static byte[] URLtoByteArray(
			String location, Cookie[] cookies, boolean post)
		throws IOException {

		byte[] byteArray = null;

		HttpMethod method = null;

		try {
			HttpClient client = new HttpClient();		
			if (location == null) {
				return byteArray;
			}
			else if (!location.startsWith(HTTP_WITH_SLASH) &&
					 !location.startsWith(HTTPS_WITH_SLASH)) {

				location = HTTP_WITH_SLASH + location;
			}

			if (Validator.isNotNull(PROXY_HOST) && PROXY_PORT > 0) {
				client.getHostConfiguration().setProxy(PROXY_HOST, PROXY_PORT);
			}

			HttpState state = new HttpState();
			//TODO 代理验证方式？
			if(Validator.isNotNull(PROXY_USERNAME))
			{
				AuthScope scope = new AuthScope(PROXY_HOST, PROXY_PORT, AuthScope.ANY_REALM);
				//如果domain不为空，则使用NTCreadential
				if(Validator.isNotNull(PROXY_DOMAIN))
				{
					state.setProxyCredentials(scope, new NTCredentials(PROXY_USERNAME, PROXY_PASSWORD, PROXY_HOST, PROXY_DOMAIN));
					
				}
				else
				{
					state.setProxyCredentials(scope, new UsernamePasswordCredentials(PROXY_USERNAME, PROXY_PASSWORD));
				}
				client.setState(state);
			}
			if (cookies != null && cookies.length > 0) {
				state.addCookies(cookies);
			}

			if (post) {
				method = new PostMethod(location);
			}
			else {
				method = new GetMethod(location);
			}

			method.setFollowRedirects(true);

			client.executeMethod(method);

			Header locationHeader = method.getResponseHeader("location");
			if (locationHeader != null) {
				return URLtoByteArray(locationHeader.getValue(), cookies, post);
			}

			InputStream is = method.getResponseBodyAsStream();

			if (is != null) {
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				byte[] bytes = new byte[512];
				int i = is.read(bytes, 0, 512);
				while(i != -1)
				{
					buffer.write(bytes, 0, i);
					i = is.read(bytes, 0, 512);
				}

				byteArray = buffer.toByteArray();

				is.close();
				buffer.close();
			}

			return byteArray;
		}
		finally {
			try {
				if (method != null) {
					method.releaseConnection();
				}
			}
			catch (Exception e) {
				Logger.error(e);
			}
		}
	}

	public static String URLtoString(String location)
		throws IOException {

		return URLtoString(location, null);
	}

	public static String URLtoString(String location, Cookie[] cookies)
		throws IOException {

		return URLtoString(location, cookies, false);
	}

	public static String URLtoString(String location, boolean post)
		throws IOException {

		return URLtoString(location, null, post);
	}

	public static String URLtoString(String location, Cookie[] cookies,
									 boolean post)
		throws IOException {

		return new String(URLtoByteArray(location, cookies, post));
	}

	public static String URLtoString(URL url) throws IOException {
		String xml = null;

		if (url != null) {
			URLConnection con = url.openConnection();

			con.setRequestProperty(
				"Content-Type", "application/x-www-form-urlencoded");

			con.setRequestProperty(
				"User-agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");

			InputStream is = con.getInputStream();

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			byte[] bytes = new byte[512];

			for (int i = is.read(bytes, 0, 512); i != -1;
					i = is.read(bytes, 0, 512)) {

				buffer.write(bytes, 0, i);
			}

			xml = new String(buffer.toByteArray(), "UTF-8");

			is.close();
			buffer.close();
		}

		return xml;
	}
	
	public static String fileToString(String fileName) throws IOException {
		String xml = null;

		if (fileName != null) {
			
			InputStream is = new FileInputStream(fileName);
			ByteArrayOutputStream buffer = null;
			try{
				buffer = new ByteArrayOutputStream();
				byte[] bytes = new byte[512];
				for (int i = is.read(bytes, 0, 512); i != -1;
						i = is.read(bytes, 0, 512)) {
	
					buffer.write(bytes, 0, i);
				}
				xml = new String(buffer.toByteArray(), "UTF-8");
			}	
			finally{
				is.close();
				buffer.close();
			}

		}

		return xml;
	}

}
