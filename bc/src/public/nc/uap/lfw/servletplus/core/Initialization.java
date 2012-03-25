package nc.uap.lfw.servletplus.core;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.constant.Keys;
import nc.uap.lfw.servletplus.utils.ClassScan;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 初始化Servlet工具集
 * 
 * @author licza
 * 
 */
public class Initialization {
	private String urlPrefix;
	/**
	 * 有servlet Action的包
	 */
	private String actionFolder;
	/**
	 * 存放URL地址表
	 */
	private Map<String, Method> urlMap = new ConcurrentHashMap<String, Method>();;

	public String getActionFolder() {
		return actionFolder;
	}

	public void setActionFolder(String actionFolder) {
		this.actionFolder = actionFolder;
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	/**
	 * 注册URL
	 */
	public void registerUrl() {
		urlMap.clear();
		String[] folders = StringUtils.split(getActionFolder(), ",");
		if (ArrayUtils.isEmpty(folders)) {
			LfwLogger.debug("====CoreServlet.Initialization==== ActionFolder is null." );
			return;
		}
		for (String folder : folders) {
			LfwLogger.debug("====CoreServlet.Initialization==== ActionFolder:" + folder);
			Set<Class<?>> clazzs = ClassScan.getClasses(folder);
			if (clazzs == null || clazzs.isEmpty()){
				LfwLogger.info("====CoreServlet.Initialization==== ClassScan.getClasses class is null ;folder :" + folder );
				continue;
			}
			for (Class<?> clazz : clazzs) {
				if (LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DEBUG)) {
					LfwLogger.info("====CoreServlet.Initialization====   registerClassUrl(dynamic) start .class :" + clazz.getName() );
					registerClassUrl(ClassScan.dynamicClass(clazz));
					LfwLogger.info("====CoreServlet.Initialization====   registerClassUrl(dynamic) end .class :" + clazz.getName() );
				} else {
					LfwLogger.info("====CoreServlet.Initialization====   registerClassUrl(static) start .class :" + clazz.getName() );
					registerClassUrl(clazz);
					LfwLogger.info("====CoreServlet.Initialization====   registerClassUrl(static) start .class :" + clazz.getName() );
				}
			}
		}
	}

	public Map<String, Method> getUrlMap() {
		return urlMap;
	}

	public void setUrlMap(Map<String, Method> urlMap) {
		this.urlMap = urlMap;
	}

	/**
	 * 注册单个servlet下的Action
	 */
	public void registerClassUrl(final Class<?> clazz) {
		if (clazz == null){
			LfwLogger.info("====Initialization#registerClassUrl=== =: class is null"  );
			return;
		}
		Servlet servletAnn = clazz.getAnnotation(Servlet.class);
		if (servletAnn == null) {
			LfwLogger.info("====Initialization#registerClassUrl=== : Servlet annotation is null"  );
			return;
		}
		String servletUrl = servletAnn.path();
		Method[] methods = clazz.getDeclaredMethods();
		if (ArrayUtils.isEmpty(methods)) {
			LfwLogger.info("====Initialization#registerClassUrl=== : Servlet DeclaredMethods is null"  );
			return;
		}
		for (Method method : methods) {
			boolean isAction = method.isAnnotationPresent(Action.class);
			if (isAction) {
				Action action = method.getAnnotation(Action.class);
				String url = getUrlPrefix() + (action.url().equals("") ? (servletUrl + "/" + method.getName()): servletUrl + action.url());
				//if (StringUtils.isEmpty(action.method())) {
					LfwLogger.debug("====Initialization#registerClassUrl=== url :  " + url +" is reg ;all method");
//				} else if (action.method().equals(Keys.POST))  {
//					url = Keys.POST + ":" + url;
//					LfwLogger.debug("====Initialization#registerClassUrl=== url :  " + url +" is reg ;method is " + method.getName());
//				}else if (action.method().equals(Keys.GET))  {
//					url = Keys.GET + ":" + url;
//					LfwLogger.debug("====Initialization#registerClassUrl=== url :  " + url +" is reg ;method is " + method.getName());
//				}
				urlMap.put(url, method);
			}
		}
	}
}
