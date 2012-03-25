package nc.uap.lfw.login.util;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ModelServerConfig;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.login.itf.AbstractLfwIntegrateProvider;
import nc.uap.lfw.util.LfwClassUtil;

/**
 * �����ṩ����
 * 
 * @author licza
 * 
 * @param <K>
 */
public class LfwLoginFetcher {

	/** ���������IntegrationProvider��ʾ��Ϣ **/
	static final String err_collo = "system.properties��login.provider���ô���!";

	/** IntegrationProvider������ **/
	private static final String cfg_item = "login.provider";

	/**
	 * ��õ�½������
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
	 * ��õ�½��������
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
	 * ��õ�½������
	 * 
	 * @param clazz
	 * @return
	 */
	public AbstractLfwIntegrateProvider getInstance() {
		return (AbstractLfwIntegrateProvider) getGeneralInstance();
	}

	/**
	 * ���LFW������Ϣ
	 * 
	 * @return
	 */
	private static ModelServerConfig getLfwConfig() {
		return LfwRuntimeEnvironment.getModelServerConfig();
	}

	/**
	 * ��Ч��У��
	 * 
	 * @param o
	 */
	private static void verify(Object o) {
		if (!(o instanceof AbstractLfwIntegrateProvider))
			throw new IllegalArgumentException(err_collo);
	}

}
