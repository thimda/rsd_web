package nc.uap.lfw.core.servlet.filter.compression;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import nc.bs.logging.Logger;

/**
 * ��Ӵ�Response��Ŀ����Ϊ��ͳ������������Ա��ڼ�¼��־
 * @author dengjt
 *
 */
public class CalHttpServletResponse implements HttpServletResponse {
	private HttpServletResponse delegator;
	private CalOutputStream delOutputStream;
	private CalPrintWriter delWriter;
	public CalHttpServletResponse(HttpServletResponse delegator){
		this.delegator = delegator;
	}
	public void addCookie(Cookie cookie) {
		delegator.addCookie(cookie);
	}

	public void addDateHeader(String arg0, long arg1) {
		delegator.addDateHeader(arg0, arg1);
	}

	public void addHeader(String arg0, String arg1) {
		delegator.addHeader(arg0, arg1);
	}

	public void addIntHeader(String arg0, int arg1) {
		delegator.addIntHeader(arg0, arg1);
	}

	public boolean containsHeader(String arg0) {
		return delegator.containsHeader(arg0);
	}

	public String encodeRedirectURL(String arg0) {
		return delegator.encodeRedirectURL(arg0);
	}

	public String encodeRedirectUrl(String arg0) {
		return delegator.encodeRedirectUrl(arg0);
	}

	public String encodeURL(String arg0) {
		return delegator.encodeURL(arg0);
	}

	public String encodeUrl(String arg0) {
		return delegator.encodeUrl(arg0);
	}

	public void sendError(int arg0) throws IOException {
		delegator.sendError(arg0);
	}

	public void sendError(int arg0, String arg1) throws IOException {
		delegator.sendError(arg0, arg1);
	}

	public void sendRedirect(String arg0) throws IOException {
		delegator.sendRedirect(arg0);
	}

	public void setDateHeader(String arg0, long arg1) {
		delegator.setDateHeader(arg0, arg1);
	}

	public void setHeader(String arg0, String arg1) {
		delegator.setHeader(arg0, arg1);
	}

	public void setIntHeader(String arg0, int arg1) {
		delegator.setIntHeader(arg0, arg1);
	}

	public void setStatus(int arg0) {
		delegator.setStatus(arg0);
	}

	public void setStatus(int arg0, String arg1) {
		delegator.setStatus(arg0, arg1);
	}

	public void flushBuffer() throws IOException {
		delegator.flushBuffer();
	}

	public int getBufferSize() {
		return delegator.getBufferSize();
	}

	public String getCharacterEncoding() {
		return delegator.getCharacterEncoding();
	}

	public String getContentType() {
		return delegator.getContentType();
	}

	public Locale getLocale() {
		return delegator.getLocale();
	}

	public ServletOutputStream getOutputStream() throws IOException {
		if(delOutputStream == null){
			try {
				delOutputStream = new CalOutputStream(delegator.getOutputStream());
			} 
			catch (IOException e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return delOutputStream;
	}

	public PrintWriter getWriter() throws IOException {
		if(delWriter == null){
			try {
				delWriter = new CalPrintWriter(delegator.getWriter());
			} 
			catch (IOException e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return delWriter;
	}

	public boolean isCommitted() {
		return delegator.isCommitted();
	}

	public void reset() {
		delegator.reset();
	}

	public void resetBuffer() {
		delegator.resetBuffer();
	}

	public void setBufferSize(int arg0) {
		delegator.setBufferSize(arg0);
	}

	public void setCharacterEncoding(String arg0) {
		delegator.setCharacterEncoding(arg0);
	}

	public void setContentLength(int arg0) {
		delegator.setContentLength(arg0);
	}

	public void setContentType(String arg0) {
		delegator.setContentType(arg0);
	}

	public void setLocale(Locale arg0) {
		delegator.setLocale(arg0);
	}
	
	public long getTotalCount() {
		if(delOutputStream != null)
			return delOutputStream.totalCount;
		else if(delWriter != null)
			return delWriter.totalCount;
		return 0;
	}

	
	private class CalOutputStream extends ServletOutputStream{
		private long totalCount = 0;
		private ServletOutputStream delegator;
		public CalOutputStream(ServletOutputStream delegator) {
			this.delegator = delegator;
		}


		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			delegator.write(b, off, len);
			totalCount += len;
		}

		@Override
		public void write(byte[] b) throws IOException {
			delegator.write(b);
			totalCount += b.length;
		}

		@Override
		public void write(int b) throws IOException {
			delegator.write(b);
			totalCount += 2;
		}
		
		
	}
	
	private class CalPrintWriter extends PrintWriter{
		private PrintWriter delegator;
		private long totalCount = 0;
		public CalPrintWriter(PrintWriter delegator) {
			super(delegator);
			this.delegator = delegator;
		}
		@Override
		public void write(char[] buf, int off, int len) {
			super.write(buf, off, len);
			totalCount += len * 2;
		}
		@Override
		public void write(int c) {
			super.write(c);
			totalCount += 2;
		}
		@Override
		public void write(String s, int off, int len) {
			super.write(s, off, len);
			totalCount += len;
		}
		@Override
		public void write(char[] buf) {
			super.write(buf);
			totalCount += buf.length;
			//LfwRuntimeEnvironment.setThreadCount(buf.length);
		}
		@Override
		public void write(String s) {
			super.write(s);
			totalCount += s.length();
		}
		
	}

	@Override
	public String getHeader(String key) {
		return delegator.getHeader(key);
	}
	@Override
	public Collection<String> getHeaderNames() {
		return delegator.getHeaderNames();
	}
	@Override
	public Collection<String> getHeaders(String key) {
		return delegator.getHeaders(key);
	}
	@Override
	public int getStatus() {
		return delegator.getStatus();
	}
}
