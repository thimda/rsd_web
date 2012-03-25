package nc.uap.lfw.login.util;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ModelServerConfig;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.itf.AbstractLfwIntegrateProvider;
import nc.uap.lfw.util.LfwClassUtil;

/**
 * 集成提供者类
 * 
 * @author licza
 * 
 * @param <K>
 */
public class LfwLoginFetcher {

	/** 错误的配置IntegrationProvider提示信息 **/
	static final String err_collo = "system.properties中login.provider配置错误!";

	/** IntegrationProvider配置项 **/
	private static final String cfg_item = "login.provider";

	/**
	 * 获得登陆配置类
	 * 
	 * @return
	 */
	public static AbstractLfwIntegrateProvider getGeneralInstance() {
		try {
			Object o = LfwClassUtil.newInstance(getProviderClass());
			verify(o);
			return (AbstractLfwIntegrateProvider) o;
		} catch (Exception ex) {
			LfwLogger.error(ex.getMessage(), ex.getCause());
			throw new LfwRuntimeException(ex.getMessage());
		}
	}

	/**
	 * 获得登陆配置类名
	 * 
	 * @return
	 */
	private static String getProviderClass() {
		String providerClazz = getLfwConfig().getConfigValue(cfg_item);
		if (providerClazz == null || providerClazz.length() == 0)
			providerClazz = "nc.uap.lfw.sso.NcSsoProvider";
		return providerClazz;
	}

	/**
	 * 获得登陆配置类
	 * 
	 * @param clazz
	 * @return
	 */
	public AbstractLfwIntegrateProvider getInstance() {
		return (AbstractLfwIntegrateProvider) getGeneralInstance();
	}

	/**
	 * 获得LFW配置信息
	 * 
	 * @return
	 */
	private static ModelServerConfig getLfwConfig() {
		return LfwRuntimeEnvironment.getModelServerConfig();
	}

	/**
	 * 有效性校验
	 * 
	 * @param o
	 */
	private static void verify(Object o) {
		if (!(o instanceof AbstractLfwIntegrateProvider))
			throw new IllegalArgumentException(err_collo);
	}

}
