package nc.uap.lfw.servletplus.core.impl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.servletplus.core.BaseActionInterface;

/**
 * BaseAction��һ��ʵ��
 * 
 * @author licza.
 * 
 */
public class BaseAction implements BaseActionInterface {
	public HttpServletRequest request;
	public HttpServletResponse response;
	public StringBuffer htmlBuffer = new StringBuffer();

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * ���HTML�ĵ�.
	 * 
	 * @param html
	 * @param docType
	 * @param charset
	 */
	private void print(Object html, String docType, String charset) {
		if (response.getContentType() == null) {
			response.setContentType(docType);
		}
		response.setCharacterEncoding(charset);
		response.setHeader("Pragma", "No-cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		try {
			PrintWriter out = response.getWriter();
			out.print(html);
			out.flush();
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(),e);
		}
	}

	/**
	 * ��ӡ�ĵ�
	 * 
	 * @param html
	 */
	public void print(Object html) {
		htmlBuffer.append(html);
	}

	/**
	 * ��ӡ�ĵ�����ҳ���ϻ���
	 */
	public void println(Object html) {
		htmlBuffer.append(html);
		htmlBuffer.append("<br/>\r\n");
	}

	/**
	 * ��ӡһ������
	 */
	public void println() {
		htmlBuffer.append("<br/>\r\n");
	}

	/**
	 * �������
	 */
	public void fush() {
		if (!response.isCommitted()) {
			print(htmlBuffer, "text/html", "UTF-8");
		}
	}

	/**
	 * ���request��response����
	 * 
	 * @param request
	 * @param response
	 */
	public void fill(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	/**
	 * request����ת
	 * 
	 * @param url
	 * @throws IOException 
	 * @throws ServletException 
	 * @throws PortalServiceException
	 */
	public void go(String url) throws ServletException, IOException {
		request.getRequestDispatcher(url).forward(request, response);
	}

	/**
	 * �ͻ���301��ת
	 * 
	 * @param url
	 * @throws IOException 
	 * @throws PortalServiceException
	 */
	public void gun(String url) throws IOException {
		response.sendRedirect(url);
	}
	/**
	 * ע����½
	 * 
	 * @throws IOException
	 */
	public void logout() throws IOException {
		request.getSession().invalidate();
		response.sendRedirect(request.getContextPath() +"/core/login.jsp?pageId=login");
	}
	/**
	 * ��ӡ��ʾ��Ϣ
	 * 
	 * @param str
	 */
	public void alert(String str) {
		print("<script>alert('" + str + "');</script>");
	}
	/**
	 * ִ��Js
	 * @param script
	 */
	public void addExecScript(String script){
		print("<script>" + script + "</script>");
	}
}
