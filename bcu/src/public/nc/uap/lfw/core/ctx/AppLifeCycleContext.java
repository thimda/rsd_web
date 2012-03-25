package nc.uap.lfw.core.ctx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppLifeCycleContext {
	public static final String EVENT_LEVEL = "el";
	public static final String EVENT_LEVEL_APP = "0";
	public static final String EVENT_LEVEL_WIN = "1";
	public static final String EVENT_LEVEL_VIEW = "2";
	public static final String EVENT_LEVEL_SCRIPT = "3";
	public static final String METHOD_NAME = "m_n";
	public static final String PLUGOUT_SIGN = "plug";
	public static final String PLUGOUT_ID = "plugid";
	public static final String PLUGOUT_SOURCE = "plugsource";
	public static final String PLUGOUT_SOURCE_WINDOW = "plugsourcewindow";
	public static final String PLUGOUT_FROMSERVER = "fromServer";
	public static final String PLUGOUT_PARAMS = "plugparams";
	private static ThreadLocal<AppLifeCycleContext> threadLocal = new ThreadLocal<AppLifeCycleContext>();
	private Map<String, String> paramMap;
	private ApplicationContext appCtx;
	
	private List<Map<String, String>> groupParamMap;
//	private WindowContext winCtx;
	public AppLifeCycleContext(){
	}
	
	public static AppLifeCycleContext current() {
		return threadLocal.get();
	}
	
	public static void current(AppLifeCycleContext current) {
		threadLocal.set(current);
	}
	
	public String getParameter(String key){
		if(paramMap == null)
			paramMap = new HashMap<String, String>();
		return paramMap.get(key);
	}
	
	public void setParameter(String key, String value){
		if(paramMap == null)
			paramMap = new HashMap<String, String>();
		paramMap.put(key, value);
	}
	
	public static void reset() {
		threadLocal.remove();
	}
	public WindowContext getWindowContext() {
		return appCtx.getCurrentWindowContext();
	}
	public ViewContext getViewContext() {
		return getWindowContext().getCurrentViewContext();
	}
//
//	public void setWindowContext(WindowContext winCtx) {
//		this.winCtx = winCtx;
//	}

	public void setApplicationContext(ApplicationContext appCtx) {
		this.appCtx = appCtx;
	}
	
	public ApplicationContext getApplicationContext(){
		return appCtx;
	}
	
//	private void setParam(String key, String value){
//		paramMap.put(key, value);
//	}
//	
//	private String getParam(String key){
//		return paramMap.get(key);
//	}
	
	public Map<String, String> getParamMap() {
		return paramMap;
	}
	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}

	public List<Map<String, String>> getGroupParamMapList() {
		return groupParamMap;
	}

	public void setGroupParamMapList(List<Map<String, String>> groupParamMap) {
		this.groupParamMap = groupParamMap;
	}
}