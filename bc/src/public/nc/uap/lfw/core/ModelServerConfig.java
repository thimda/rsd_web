package nc.uap.lfw.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;

import nc.uap.lfw.core.log.LfwLogger;

/**
 * 针对各模块配置文件的配置类
 * @author dengjt
 *
 */
public class ModelServerConfig implements Serializable {
	private static final long serialVersionUID = -851156071995482872L;
	private static final String DEFAULT_THEME_ID = "DEFAULT_THEME_ID";
	private static final String DEFAULT_LANG_ID = "DEFAULT_LANG_ID";
	private static final String DEFAULT_OPEN_NODE_MODE = "DEFAULT_OPEN_NODE_MODE";
	private static final String DEFAULT_CONNECT_SERVER_CYCLE = "DEFAULT_CONNECT_SERVER_CYCLE"; 
//	private static final String DEFAULT_NOTICE_REFRESH_CYCLE = "DEFAULT_NOTICE_REFRESH_CYCLE";
//	private static final String DEFAULT_JOB_REFRESH_CYCLE = "DEFAULT_JOB_REFRESH_CYCLE";
//	private static final String DEFAULT_TEMP_FILE_PATH = "DEFAULT_TEMP_FILE_PATH";
	//当前产品对应编号,一般在授权控制及其它系统区分的公共区域中使用
	private static final String SYS_ID = "SYS_ID";
	private static final String MAX_WIN = "MAX_WIN";
	// 窗口打开方式,0:普通打开 1:打开最大化
	private static final String DEFAULT_WINDOW_OPENMODE = "DEFAULT_WINDOW_OPENMODE";
	private static final String WEB_SESSION_IMPL = "WEB_SESSION_IMPL";
	private static final String WEB_SESSION_LISTENER = "WEB_SESSION_LISTENER";
	private static final String MAIN_URL = "MAIN_URL";
	
	private Map<String, String> keyValuePair = new HashMap<String, String>();
	public ModelServerConfig() {
		
	}
	
	public ModelServerConfig(ServletContext ctx) {
		InputStream is = null;
		try {
			String priPath = "/WEB-INF/conf/system.properties";
			loadProperties(ctx, priPath);
			Set pathSet = ctx.getResourcePaths("/WEB-INF/conf");
			Iterator it = pathSet.iterator();
			while(it.hasNext()){
				String path = (String) it.next();
				LfwLogger.debug("加载配置文件:" + path);
				if(path.equals("/WEB-INF/conf/system.properties"))
					continue;
				if(!path.endsWith("system.properties"))
					continue;
				loadProperties(ctx, path);
			}
		} 
		catch (Exception e) {
			LfwLogger.error(e);
		}
		finally{
			try {
				if(is != null)
					is.close();
			} catch (IOException e) {
				LfwLogger.error(e);
			}
		}
	}

	private void loadProperties(ServletContext ctx, String path) {
		InputStream is = null;
		try {
			is = ctx.getResourceAsStream(path);
			if(is != null){
				Properties props = new Properties();
				props.load(is);
				propToMap(keyValuePair, props);
			}
		} 
		catch (Exception e) {
			LfwLogger.error(e);
		}
		finally{
			try {
				if(is != null)
					is.close();
			} catch (IOException e) {
				LfwLogger.error(e);
			}
		}
	}
	
	
	private void propToMap(Map<String, String> keyValuePair, Properties props) {
		Enumeration<?> em = props.propertyNames();
		while(em.hasMoreElements()){
			String key = (String) em.nextElement();
			String newValue = props.getProperty(key);
			if(newValue == null || newValue.equals(""))
				continue;
			String value = keyValuePair.get(key);
			if(value != null && !value.equals("")){
				value += ",";
			}
			else
				value = "";
			value += newValue;
			keyValuePair.put(key, value);
		}
	}

	public String getConfigValue(String key){
		return keyValuePair.get(key);
	}
	
	public String getDefaultThemeId() {
		return keyValuePair.get(DEFAULT_THEME_ID);
	}
	
	public String getDefaultLangId() {
		String defaultLangId = keyValuePair.get(DEFAULT_LANG_ID);
		return defaultLangId == null ? "simpchn" : defaultLangId;
	}
	
	public String getDefaultOpenNodeMode() {
		String defaultOpenNodeMode = keyValuePair.get(DEFAULT_OPEN_NODE_MODE);
		return defaultOpenNodeMode == null ? "0" : defaultOpenNodeMode;
	}
	
	public String getDefaultConnectServerCycle() {
		return keyValuePair.get(DEFAULT_CONNECT_SERVER_CYCLE) == null ? "3" : keyValuePair.get(DEFAULT_CONNECT_SERVER_CYCLE);
	}
	
	public String getDefaultWindowOpenMode() {
		return keyValuePair.get(DEFAULT_WINDOW_OPENMODE) == null ? "0" : keyValuePair.get(DEFAULT_WINDOW_OPENMODE);
	}
	
	public String getMainUrl() {
		return keyValuePair.get(MAIN_URL);
	}
	
	public Integer getSysId() {
		String sysId =  keyValuePair.get(SYS_ID) == null ? "-1" : keyValuePair.get(SYS_ID);
		return Integer.valueOf(sysId);
	}
	
	public boolean isMaxWin() {
		String maxWin = keyValuePair.get(MAX_WIN);
		return maxWin == null || Boolean.valueOf(maxWin);
	}
	
	public String getWebSessionImpl(){
		String webSessionImpl = keyValuePair.get(WEB_SESSION_IMPL);
		return webSessionImpl;
	}
	
	public String[] getWebSessionListeners() {
		String listeners = keyValuePair.get(WEB_SESSION_LISTENER);
		if(listeners == null || listeners.equals(""))
			return null;
		return listeners.split(",");
	}
}
