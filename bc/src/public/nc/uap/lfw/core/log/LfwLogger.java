package nc.uap.lfw.core.log;

import nc.bs.logging.Logger;

public class LfwLogger {
	public static final String LOGGER_LFW_SYS = "lfw_sys";
	public static final String LOGGER_LFW = "lfw";
	public static final String LOGGER_PORTAL = "portal";
	public static void error(String type, String msg, Throwable t){
		Logger.init(type);
		Logger.error(msg, t);
		Logger.reset();
	}
	
	public static void error(String type, String msg){
		error(type, msg, null);
	}
	
	public static void debug(String type, String msg){
		Logger.init(type);
		Logger.debug(msg);
		Logger.reset();
	}
	
	public static void info(String type, String msg){
		Logger.init(type);
//		if(RuntimeEnv.getInstance().isDevelopMode()){
//			System.out.println(msg);
//		}
		Logger.info(msg);
		Logger.reset();
	}
	
	public static void info(String msg){
		Logger.info(msg);
	}
	
	public static void debug(String msg){
		Logger.debug(msg);
	}
	
	public static void error(String msg, Throwable t){
		Logger.error(msg, t);
	}
	
	public static void error(String msg){
		Logger.error(msg);
	}
	
	public static void error(Throwable e){
		Logger.error(e.getMessage(), e);
	}

	public static boolean isDebugEnabled() {
		return Logger.isDebugEnabled();
	}

	public static boolean isInfoEnabled() {
		return Logger.isInfoEnabled();
	}

	public static void warn(String string) {
		Logger.warn(string);
	}

	public static boolean isWarnEnabled() {
		return Logger.isWarnEnabled();
	}
}
