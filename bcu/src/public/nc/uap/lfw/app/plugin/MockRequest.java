package nc.uap.lfw.app.plugin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
/**
 * HttpRequest °ü×°
 * @author dengjt
 *
 */
public class MockRequest extends HttpServletRequestWrapper {
	private HttpServletRequest request;
	private Map<String, String> paramMap = new HashMap<String, String>();
	public MockRequest(HttpServletRequest request){
		super(request);
		this.request = request;
	}
//	@Override
//	public String getAuthType() {
//		return request.getAuthType();
//	}
//
//	@Override
//	public String getContextPath() {
//		return request.getContextPath();
//	}
//
//	@Override
//	public Cookie[] getCookies() {
//		return request.getCookies();
//	}
//
//	@Override
//	public long getDateHeader(String key) {
//		return request.getDateHeader(key);
//	}
//
//	@Override
//	public String getHeader(String key) {
//		return request.getHeader(key);
//	}
//
//	@Override
//	public Enumeration getHeaderNames() {
//		return request.getHeaderNames();
//	}
//
//	@Override
//	public Enumeration getHeaders(String key) {
//		return request.getHeaders(key);
//	}
//
//	@Override
//	public int getIntHeader(String key) {
//		return request.getIntHeader(key);
//	}
//
//	@Override
//	public String getMethod() {
//		return request.getMethod();
//	}
//
//	@Override
//	public String getPathInfo() {
//		return request.getPathInfo();
//	}
//
//	@Override
//	public String getPathTranslated() {
//		return request.getPathTranslated();
//	}
//
//	@Override
//	public String getQueryString() {
//		return request.getQueryString();
//	}
//
//	@Override
//	public String getRemoteUser() {
//		return request.getRemoteUser();
//	}
//
//	@Override
//	public String getRequestURI() {
//		return request.getRequestURI();
//	}
//
//	@Override
//	public StringBuffer getRequestURL() {
//		return request.getRequestURL();
//	}
//
//	@Override
//	public String getRequestedSessionId() {
//		return request.getRequestedSessionId();
//	}
//
//	@Override
//	public String getServletPath() {
//		return request.getServletPath();
//	}
//
//	@Override
//	public HttpSession getSession() {
//		return request.getSession();
//	}
//
//	@Override
//	public HttpSession getSession(boolean create) {
//		return request.getSession(create);
//	}
//
//	@Override
//	public Principal getUserPrincipal() {
//		return request.getUserPrincipal();
//	}
//
//	@Override
//	public boolean isRequestedSessionIdFromCookie() {
//		return request.isRequestedSessionIdFromCookie();
//	}
//
//	@Override
//	public boolean isRequestedSessionIdFromURL() {
//		return request.isRequestedSessionIdFromURL();
//	}
//
//	@Override
//	public boolean isRequestedSessionIdFromUrl() {
//		return request.isRequestedSessionIdFromUrl();
//	}
//
//	@Override
//	public boolean isRequestedSessionIdValid() {
//		return request.isRequestedSessionIdValid();
//	}
//
//	@Override
//	public boolean isUserInRole(String userid) {
//		return request.isUserInRole(userid);
//	}
//
//	@Override
//	public Object getAttribute(String key) {
//		return request.getAttribute(key);
//	}
//
//	@Override
//	public Enumeration getAttributeNames() {
//		return request.getAttributeNames();
//	}
//
//	@Override
//	public String getCharacterEncoding() {
//		return request.getCharacterEncoding();
//	}
//
//	@Override
//	public int getContentLength() {
//		return request.getContentLength();
//	}
//
//	@Override
//	public String getContentType() {
//		return request.getContentType();
//	}
//
//	@Override
//	public ServletInputStream getInputStream() throws IOException {
//		return request.getInputStream();
//	}
//
//	@Override
//	public String getLocalAddr() {
//		return request.getLocalAddr();
//	}
//
//	@Override
//	public String getLocalName() {
//		return request.getLocalName();
//	}
//
//	@Override
//	public int getLocalPort() {
//		return request.getLocalPort();
//	}
//
//	@Override
//	public Locale getLocale() {
//		return request.getLocale();
//	}
//
//	@Override
//	public Enumeration getLocales() {
//		return request.getLocales();
//	}

	@Override
	public String getParameter(String key) {
		String value = paramMap.get(key);
		return value;
//		if(value != null)
//			return value;
//		return request.getParameter(key);
	}
//
//	@Override
//	public Map getParameterMap() {
//		return request.getParameterMap();
//	}
//
//	@Override
//	public Enumeration getParameterNames() {
//		return request.getParameterNames();
//	}
//
//	@Override
//	public String[] getParameterValues(String key) {
//		return request.getParameterValues(key);
//	}
//
//	@Override
//	public String getProtocol() {
//		return request.getProtocol();
//	}
//
//	@Override
//	public BufferedReader getReader() throws IOException {
//		return request.getReader();
//	}
//
//	@Override
//	public String getRealPath(String key) {
//		return request.getRealPath(key);
//	}
//
//	@Override
//	public String getRemoteAddr() {
//		return request.getRemoteAddr();
//	}
//
//	@Override
//	public String getRemoteHost() {
//		return request.getRemoteHost();
//	}
//
//	@Override
//	public int getRemotePort() {
//		return request.getRemotePort();
//	}
//
//	@Override
//	public RequestDispatcher getRequestDispatcher(String url) {
//		return request.getRequestDispatcher(url);
//	}
//
//	@Override
//	public String getScheme() {
//		return request.getScheme();
//	}
//
//	@Override
//	public String getServerName() {
//		return request.getServerName();
//	}
//
//	@Override
//	public int getServerPort() {
//		return request.getServerPort();
//	}
//
//	@Override
//	public boolean isSecure() {
//		return request.isSecure();
//	}
//
//	@Override
//	public void removeAttribute(String key) {
//		request.removeAttribute(key);
//	}
//
//	@Override
//	public void setAttribute(String key, Object value) {
//		request.setAttribute(key, value);
//	}
//
//	@Override
//	public void setCharacterEncoding(String charset)
//			throws UnsupportedEncodingException {
//		request.setCharacterEncoding(charset);
//	}
	public void addParameter(String key, String value) {
		paramMap.put(key, value);
	}

}
