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
 * Ԥ���빤��
 * @author dengjt
 *
 */
public final class PreBuildUtil {
	/**
	 * ����������Ϣ����Ԥ����
	 * @param input
	 */
	public static void build(InputStream input) {
		if(input == null)
			return;
		try {
			//���н��н�����ÿ�д���һ��url
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
		//ȥ��ServiceDispatcherServlet
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
			LfwLogger.debug("Ԥ����url:" + url);
			// ����NCע��URL
			URL preUrl = new URL(url);
			URLConnection uc = preUrl.openConnection();
			// ����������������/ֵ�������������������Դ
			uc.setDoOutput(true);
			// ��ȡ���ӵ��ʵ�������
			HttpURLConnection hc = (HttpURLConnection) uc;
			// ��HTTP���󷽷�����ΪPOST��Ĭ�ϵ���GET��
			hc.setRequestMethod("GET");
			// �������
			OutputStream os = hc.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.flush();
			dos.close();
			
			// ��ȡNC��ƾ֤����֤���
			is = hc.getInputStream();
			LfwLogger.debug("Ԥ����url:" + url + "���");
		}
		catch(Throwable e){
			LfwLogger.error("Ԥ����url:" + url + "����", e);
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
