package nc.uap.lfw.file.helper;

/**
 * 使用前台回调脚本
 * 
 * @author licza
 * 
 */
public class UseCallBackMehtod extends Scene {
	/**
	 * 调用前台脚本方法
	 * 
	 * @param method
	 *            方法名称
	 */
	public UseCallBackMehtod(String method) {
		arg.put(METHOD, method);
	}

}
