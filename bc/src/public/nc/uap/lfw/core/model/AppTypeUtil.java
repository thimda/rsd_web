package nc.uap.lfw.core.model;

import nc.uap.lfw.core.ctx.AppLifeCycleContext;

public final class AppTypeUtil {
	public static final String APP_TYPE = "App";
	public static final String PAGE_TYPE = "Page";
	/**
	 * ���ؽڵ���֯���ͣ���ǰ��Ϊ2�֣�һ����ҳ��Ϊ��λ��һ����Ӧ��Ϊ��λ��������ֻ��������Ⱦ��
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
