package nc.uap.lfw.core.servlet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.comn.NetObjectInputStream;
import nc.bs.framework.server.ServerConfiguration;
import nc.uap.lfw.core.log.LfwLogger;
import nc.vo.pub.lang.UFDateTime;

/**
 * 日志文件下载
 * 
 * @author guoweic
 * 
 */
public class WatchLoggerServlet extends LfwServletBase {

	private static final long serialVersionUID = 1L;

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			getWatchLogger(request, response);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage());
			request.setAttribute("exception", e.getMessage());
		}
	}

	/**
	 * 获取日志文件
	 * 
	 * @param request
	 * @param response
	 */
	private void getWatchLogger(HttpServletRequest request,
			HttpServletResponse response) {
		ObjectOutputStream outPut = null;
		NetObjectInputStream inputStream = null;
		PrintWriter pw = null;
		try {
			String userid = request.getParameter("userid");
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ userid + "_server.log");

			String starttimeStr = request.getParameter("startTime");
			UFDateTime starttime = new UFDateTime(Long.parseLong(starttimeStr));
			String endtimeStr = request.getParameter("endTime");
			UFDateTime endtime = new UFDateTime(Long.parseLong(endtimeStr));

			Hashtable<String, Object> hm = new Hashtable<String, Object>();
			hm.put("servicename", "directlogquery");
			hm.put("methodname", "query");
			hm.put("parametertypes", new Class[] { String.class });
			// IClusterFinder icf = NCLocator.getInstance().lookup(
			// IClusterFinder.class);
			String masterName = ServerConfiguration.getServerConfiguration()
					.getServerName();// icf.getMasterName()
			// masterName = "record";
			
			String query = "select " + masterName + " from nclogs where ts>=\""
					+ starttime.toString() + "\"  and ts<=\""
					+ endtime.toString() + "\" and userid=\"" + userid + "\" ";
			hm.put("parameter", new Object[] { query });

			String serverip = "127.0.0.1";
			// NCAppletStub stub = NCAppletStub.getInstance();
			// String serverip = stub.getParameter("SERVER_IP");
			URL url = new URL("http://" + serverip + ":9999/remotecall");
			// if (!isConnect(url))
			// url = new URL("http://" + serverip + "/remotecall");
			LfwLogger.debug("request url=" + url);
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			outPut = new ObjectOutputStream(con.getOutputStream());
			outPut.writeObject(hm);
			inputStream = new NetObjectInputStream(con.getInputStream());
			// 读记录数。
			long lognum = inputStream.readLong();
			LfwLogger.debug("日志记录数 = " + lognum);
			pw = response.getWriter();
			while (true) {
				String s = (String) inputStream.readObject();
				if (s != null && !s.equals("0x99")) {
					pw.write(s + "\n");
				}
				else{
					break;
				}
			}
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		} finally {
			try {
				if (pw != null)
					pw.close();
			} catch (Exception e) {
				LfwLogger.error(e.getMessage(), e);
			}
			try {
				if (outPut != null)
					outPut.close();
			} catch (Exception e) {
				LfwLogger.error(e.getMessage(), e);
			}
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (Exception e) {
				LfwLogger.error(e.getMessage(), e);
			}
		}

	}

	@Override
	public String getRemoteCallMethod(HttpServletRequest req,
			HttpServletResponse res) {
		return WatchLoggerServlet.class.getName() + ".service";
	}

}
