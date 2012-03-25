package nc.uap.lfw.core.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.comn.NetObjectInputStream;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.MouseServerListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.lfw.pqm.PqmUtil;
import nc.vo.pub.lang.UFDateTime;

/**
 * 日志情况查询，点击“结束”后执行方法
 * 
 * @author guoweic
 *
 */
public class WatchLoggerEndListener extends MouseServerListener<ButtonComp> {

	public WatchLoggerEndListener(LfwPageContext context, String widgetId) {
		super(context, widgetId);
	}

	@Override
	public void onclick(MouseEvent<ButtonComp> e) {
		Date date = new Date();
		// 结束时间
		long endTime = date.getTime();
		
		WebSession session = LfwRuntimeEnvironment.getWebContext().getWebSession();
		// 开始时间
		long startTime = (Long) session.getAttribute("startTime");
		
		session.removeAttribute("startTime");
		
		LfwSessionBean  ses = LfwRuntimeEnvironment.getLfwSessionBean();
		String userid = ses.getUser_code();
		
//		String url = LfwRuntimeEnvironment.getCorePath() + "/watchlogger?startTime=" + startTime + "&endTime=" + endTime + "&userid=" + userid + "";

		File[] files = getLogFiles(startTime,endTime,userid);
		String logInfo = "";
		if (files.length ==0)
			logInfo = "无日志信息";
		else{
			PqmUtil pqm = new PqmUtil();
			try {
				logInfo = pqm.parseLog(files);
			} catch (Exception e1) {
				LfwLogger.error(e1.getMessage(), e1);
				logInfo = "获取日志信息时发生异常：\n"+e1.toString();
			}finally{
				for (int i = 0 ; i< files.length ; i++){
					files[i].delete();
				}
			}
		}
		Dataset ds = getGlobalContext().getPageMeta().getWidget("main").getViewModels().getDataset("temp");
		Row r = ds.getEmptyRow();
		r.setValue(ds.nameToIndex("logInfo"), logInfo);
		ds.clear();
		ds.setCurrentKey(Dataset.MASTER_KEY);
		ds.addRow(r);
		ds.setRowSelectIndex(0);
		ds.setEnabled(true);
		// 打开下载窗口
//		getGlobalContext().downloadFileInIframe(url);
//		getGlobalContext().openNewNormalWindow(url, "下载日志", "200", "100",false);

	}

	/**
	 * 取日志文件数组
	 * 
	 * @param startTime
	 * @param endTime
	 * @param userid
	 * @return
	 */
	private File[] getLogFiles(long startTime, long endTime, String userid) {
		ObjectOutputStream outPut = null;
		NetObjectInputStream inputStream = null;
		try {
			Hashtable<String, Object> hm = new Hashtable<String, Object>();
			hm.put("servicename", "directlogquery");
			hm.put("methodname", "query");
			hm.put("parametertypes", new Class[] { String.class });
//			String masterName = ServerConfiguration.getServerConfiguration()
//					.getServerName();
			String serverName = System.getProperty("nc.server.name");

			UFDateTime start = new UFDateTime(startTime);
			UFDateTime end = new UFDateTime(endTime);
			
			String query = "select " + serverName + "/m from nclogs where ts>=\""
					+ start.toString() + "\"  and ts<=\""
					+ end.toString() + "\" and userid=\"" + userid + "\" ";
			hm.put("parameter", new Object[] { query });
	
			String serverip = "127.0.0.1";
			String port = getPort();
			URL url = new URL("http://" + serverip + ":" + port + "/remotecall");
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
			
			String realPath =  LfwRuntimeEnvironment.getRealPath();
			String filePath = realPath + "/logTemp";			
			File logDir = new File(filePath);
			if (!logDir.exists())
				logDir.mkdirs();
			List<File> logFiles = new ArrayList<File>();
			int count = 1;
			while (true) {
				String s = (String) inputStream.readObject();
				if (s != null && !s.equals("0x99")) {
					String fileName = userid + "_" + startTime + "_" + count + ".log";
					File logFile = new File(filePath + "/" + fileName);
					OutputStream os = new FileOutputStream(logFile);
					os.write(s.getBytes());
					logFiles.add(logFile);
					count ++;
				}
				else{
					break;
				}
			}
			return logFiles.toArray(new File[0]);
		}
		catch (ConnectException ce){
			LfwLogger.error(ce.getMessage(), ce);
			throw new LfwRuntimeException("连接日志服务失败");
		}
		catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		} finally {
//			try {
//				if (pw != null)
//					pw.close();
//			} catch (Exception e) {
//				LfwLogger.error(e.getMessage(), e);
//			}
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

		return null;
	}
	
	/**
	 * 从NC_HOME/resources/monitor.properties中读取端口信息
	 * @return 
	 */
	private String getPort(){
		String port = "9999";
		String filePath = RuntimeEnv.getInstance().getNCHome() + "/resources";
		File  f = new File (filePath);
		if (!f.exists())
			return port;
		File file = new File(filePath + "/monitor.properties");
		InputStream input = null;
		if (file.exists()) {
			try {
				input = new FileInputStream(file);
				Properties prop = new Properties();
				prop.load(input);
				port =  prop.getProperty("MONITORSERVERPORT");
			} catch (Exception e) {
				LfwLogger.error(e.getMessage(), e);
			}
			finally{
				try {
					if(input != null)
						input.close();
				} catch (IOException e) {
					LfwLogger.error(e.getMessage(), e);
				}
			}
		}
		return port;
	}	
}
