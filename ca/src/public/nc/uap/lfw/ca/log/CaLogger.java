package nc.uap.lfw.ca.log;

import nc.bs.logging.Log;

public final class CaLogger {
	private static final String LOG_NAME = "lfwca";
	private static Log log = Log.getInstance(LOG_NAME);
	public static void info(String msg){
		//log.info(msg);
	}
	
	public static void debug(String msg){
		log.debug(msg);
	}
	
	public static void error(String msg, Throwable t){
		log.error(msg, t);
	}
	
	public static void error(String msg){
		log.error(msg);
	}
	
	public static void error(Throwable e){
		log.error(e.getMessage(), e);
	}

	public static boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	public static boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}

	public static void warn(String string) {
		log.warn(string);
	}

	public static boolean isWarnEnabled() {
		return log.isWarnEnabled();
	}
}
