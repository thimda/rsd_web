package nc.uap.lfw.core.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.execute.Executor;
import nc.bs.framework.server.ServerConfiguration;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.log.LfwLogger;

/**
 * 预编译工具
 * @author dengjt
 *
 */
public final class PreBuildUtil {
	/**
	 * 根据输入信息进行预编译
	 * @param input
	 */
	public static void build(InputStream input) {
		if(input == null)
			return;
		try {
			//按行进行解析，每行代表一个url
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			List<String> urlList = new ArrayList<String>();
			String line = br.readLine();
			while(line != null && !line.equals("") && !line.startsWith("//")){
				urlList.add(line);
				line = br.readLine();
			}
			
			if(urlList.size() > 0)
				build(urlList.toArray(new String[0]));
		} 
		catch (Exception e) {
			LfwLogger.error(e);
		}
	}
	
	private static void build(String[] urls) {
		String serverName = System.getProperty("nc.server.name");
		String urlPre = ServerConfiguration.getServerConfiguration().getServerEndpointURL(serverName);
		//去掉ServiceDispatcherServlet
		urlPre = urlPre.substring(0, urlPre.lastIndexOf("/"));
		for (int i = 0; i < urls.length; i++) {
			String url = urls[i];
			url = urlPre + LfwRuntimeEnvironment.getRootPath() + "/" + url;
			build(url);
		}
	}
	
	private static void build(String url){
		BuildThread bt = new BuildThread();
		bt.url = url;
		Executor exe = new Executor(bt);
		exe.run();
	}
}

class BuildThread implements Runnable{
	protected String url;
	public void run() {
		InputStream is = null;
		try{
			LfwLogger.debug("预编译url:" + url);
			// 构造NC注册URL
			URL preUrl = new URL(url);
			URLConnection uc = preUrl.openConnection();
			// 表明程序必须把名称/值对输出到服务器程序资源
			uc.setDoOutput(true);
			// 提取连接的适当的类型
			HttpURLConnection hc = (HttpURLConnection) uc;
			// 把HTTP请求方法设置为POST（默认的是GET）
			hc.setRequestMethod("GET");
			// 输出内容
			OutputStream os = hc.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.flush();
			dos.close();
			
			// 获取NC对凭证的验证结果
			is = hc.getInputStream();
			LfwLogger.debug("预编译url:" + url + "完成");
		}
		catch(Throwable e){
			LfwLogger.error("预编译url:" + url + "出错", e);
		}
		finally{
			if(is != null)
				try {
					is.close();
				} 
				catch (IOException e) {
					LfwLogger.error(e);
				}
		}
	}
}
