package nc.uap.lfw.core.model;

import nc.uap.lfw.core.ctx.AppLifeCycleContext;

public final class AppTypeUtil {
	public static final String APP_TYPE = "App";
	public static final String PAGE_TYPE = "Page";
	/**
	 * 返回节点组织类型，当前分为2种，一种以页面为单位，一种以应用为单位。本方法只能用于渲染期
	 * @return
	 */
	public static String getApplicationType(){
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		boolean isApp = (ctx != null);
		if(isApp){
			return APP_TYPE;
		}
		else
			return PAGE_TYPE;
	}
}
