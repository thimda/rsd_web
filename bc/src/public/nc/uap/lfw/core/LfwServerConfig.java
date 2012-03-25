package nc.uap.lfw.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import javax.servlet.ServletContext;

import nc.bs.framework.common.RuntimeEnv;
import nc.uap.lfw.core.log.LfwLogger;
/**
 * Lfw web应用配置
 * @author dengjt
 *
 */
public class LfwServerConfig implements Serializable {
	private static final long serialVersionUID = -851156071995482872L;
	private static final String DEFAULT_THEME_ID = "DEFAULT_THEME_ID";
	private static final String DEFAULT_LANG_ID = "DEFAULT_LANG_ID";
	private static final String DEFAULT_OPEN_NODE_MODE = "DEFAULT_OPEN_NODE_MODE";
	private static final String DEFAULT_CONNECT_SERVER_CYCLE = "DEFAULT_CONNECT_SERVER_CYCLE"; 
	private static final String DEFAULT_NOTICE_REFRESH_CYCLE = "DEFAULT_NOTICE_REFRESH_CYCLE";
	private static final String DEFAULT_JOB_REFRESH_CYCLE = "DEFAULT_JOB_REFRESH_CYCLE";
	private static final String DEFAULT_TEMP_FILE_PATH = "DEFAULT_TEMP_FILE_PATH";
	// 窗口打开方式,0:普通打开 1:打开最大化
	private static final String DEFAULT_WINDOW_OPENMODE = "DEFAULT_WINDOW_OPENMODE";
	private static final String LOAD_RUNNER_ADAPTER = "LOAD_RUNNER_ADAPTER";
	
	private Properties props = null;
	public LfwServerConfig(ServletContext ctx) {
		props = new Properties();
//		String confPath = RuntimeEnv.getInstance().getNCHome();
//		confPath += "/hotwebs/lfw/WEB-INF/conf/";
//		FileInputStream is = null;
		InputStream is = null;
		try {
			is = ctx.getResourceAsStream("/WEB-INF/conf/system.properties");
			props.load(is);
		} 
		catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
		finally{
			try {
				if(is != null)
					is.close();
			} catch (IOException e) {
				LfwLogger.error(e.getMessage(), e);
			}
		}
	}
	
	public String getDefaultThemeId() {
		return props.getProperty(DEFAULT_THEME_ID) == null ? "mediclassic" : props.getProperty(DEFAULT_THEME_ID);
	}
	
	public String getDefaultLangId() {
		return props.getProperty(DEFAULT_LANG_ID) == null ? "simpchn" : props.getProperty(DEFAULT_LANG_ID);
	}
	
	public String getDefaultOpenNodeMode() {
		return props.getProperty(DEFAULT_OPEN_NODE_MODE) == null ? "0" : props.getProperty(DEFAULT_OPEN_NODE_MODE);
	}
	
	public String getDefaultConnectServerCycle() {
		return props.getProperty(DEFAULT_CONNECT_SERVER_CYCLE) == null ? "3" : props.getProperty(DEFAULT_CONNECT_SERVER_CYCLE);
	}
	
	public String getDefaultWindowOpenMode() {
		return props.getProperty(DEFAULT_WINDOW_OPENMODE) == null ? "0" : props.getProperty(DEFAULT_WINDOW_OPENMODE);
	}
	
	//公告和预警
	public String getDefaultNoticeRefreshCycle() {
		return props.getProperty(DEFAULT_NOTICE_REFRESH_CYCLE) == null ? "10" : props.getProperty(DEFAULT_NOTICE_REFRESH_CYCLE);
	}
	public String getDefaultJobRefreshCycle() {
		return props.getProperty(DEFAULT_JOB_REFRESH_CYCLE) == null ? "10" : props.getProperty(DEFAULT_JOB_REFRESH_CYCLE);
	}
	
	public String getDefaultTempFilePath() {
		String tempPath = props.getProperty(DEFAULT_TEMP_FILE_PATH);
		if(tempPath != null)
			return tempPath;
		else{
			String ncHome = RuntimeEnv.getInstance().getNCHome();
			return ncHome + "/lfwtempfiles";
		}
	}
	
	public String getLoadRunnerAdpater(){
		return props.getProperty(LOAD_RUNNER_ADAPTER) == null ? "0" : props.getProperty(LOAD_RUNNER_ADAPTER);
	}
	
	public String get(String key){
		return props.getProperty(key);
	}
}
