package nc.uap.lfw.util;

import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import nc.bs.ml.NCLangResOnserver;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.CookieConstant;

/**
 * 多语言资源读取工具和格式工具
 */
public class LanguageUtil {

	public static final String DEFAULT_ENCODING = "UTF-8";

	public static final String LFW_LANGUAGE_PRODUCT_CODE = "lfw";

	private static final ArrayList<Locale> locales = new ArrayList<Locale>();
	
	static{
		locales.add(Locale.CHINESE);
		locales.add(Locale.ENGLISH);
		locales.add(Locale.TRADITIONAL_CHINESE);
	}
	/**
	 * 使用NC的多语种翻译，不带参数的直接翻译
	 * @param key
	 * @return
	 * @throws LanguageException
	 */
	public static String get(String key) {
		return NCLangResOnserver.getInstance().getStrByID(
				LfwRuntimeEnvironment.getLangDir(), key);
	} 
	
	/**
	 * 
	 * 实现指定产品编码的多语翻译
	 * @param productCode
	 * @param key
	 * @return
	 * @throws LanguageException
	 */
	public static String getByProductCode(String productCode, String key){
		
		return NCLangResOnserver.getInstance().getStrByID(productCode, key);
	}
	
	public static String getWithDefaultByProductCode(String productCode, String def, String key){
		if(key == null)
			return def;
		return NCLangResOnserver.getInstance().getString(productCode, def, key);
	}

	/**
	 * 使用NC的多语种翻译，带参数的翻译
	 * @param key
	 * @param args
	 * @return
	 * @throws LanguageException
	 */
	public static String get(String key, String[] args) {
		return NCLangResOnserver.getInstance().getString(
				LFW_LANGUAGE_PRODUCT_CODE, null, key, null, args);
	}
	/**
	 * 使用NC的多语种翻译，针对一个参数情况暴露的接口，便于jsp端使用
	 * @param key
	 * @param param
	 * @return
	 * @throws LanguageException
	 */
	public static String get(String key, String param)
	{
		return get(key, new String[]{param});
	}

	public static String getTwoString(String key1, String key2)
	{
		String value1 = get(key1);
		return get(key2, value1);
	}

	public static String getWithDefault(String key, String def)
	{
		return NCLangResOnserver.getInstance().getString(LFW_LANGUAGE_PRODUCT_CODE, def, key);
	}
	

	
	public static ArrayList<Locale> getAvailableLocales()
	{
		return locales;
	}
	
	/**
	 * 多语言工具构造函数
	 */
	private LanguageUtil() {

	}
	/**
	 * 由Cookie得到languageId
	 * @param req
	 * @return
	 */
	public static String getLangIdByCookie(HttpServletRequest req)
	{
		String sysId = "" + LfwRuntimeEnvironment.getSysId();
		Cookie[] cks = req.getCookies();
		if(cks != null)
		{
			for (int i = 0; i < cks.length; i++) 
			{
				if(cks[i].getName().equals(CookieConstant.LANG_CODE + sysId))
					return cks[i].getValue();
			}
		}
		return LocaleUtil.getDefaultLangId();
	}
}
