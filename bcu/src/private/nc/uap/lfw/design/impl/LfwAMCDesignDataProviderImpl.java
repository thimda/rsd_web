/**
 * 
 */
package nc.uap.lfw.design.impl;

import java.util.Properties;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.uimodel.AMCServiceObj;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.design.itf.IAMCProviderForDesign;
import nc.uap.lfw.design.itf.ILfwAMCDesignDataProvider;

/**
 * 
 * UAPWEB服务接口实现类
 * @author chouhl
 *
 */
public class LfwAMCDesignDataProviderImpl implements ILfwAMCDesignDataProvider {
	
	private static <T> T getInterfaceService(Class<T> clazz){
		Properties props = new  Properties();
		props.setProperty(NCLocator.SERVICEDISPATCH_URL, "http://127.0.0.1:80/ServiceDispatcherServlet");
		return NCLocator.getInstance(props).lookup(clazz);
	}
	
	public void createApplication(String appId, String appName){
		final String key = "user.dir";
		final String fileName = "application.app";
		final String folder = "\\web\\html\\applications\\";
		if(appId != null && appId.trim().length() > 0){
			if(appName == null || appName.trim().length() == 0){
				appName = appId;
			}
			String filePath = System.getProperty(key) + folder + appId;
			Application app = new Application();
			app.setId(appId);
			app.setCaption(appName);
			AMCServiceObj amcServiceObj = new AMCServiceObj();
			amcServiceObj.setFilePath(filePath);
			amcServiceObj.setFileName(fileName);
			amcServiceObj.setCurrentProjPath(System.getProperty(key));
			amcServiceObj.setAppConf(app);
			getInterfaceService(IAMCProviderForDesign.class).operateWebElementXML(amcServiceObj);
		}
	}
	
	public AMCServiceObj operateWebElementXML(AMCServiceObj amcServiceObj){
		return getInterfaceService(IAMCProviderForDesign.class).operateWebElementXML(amcServiceObj);
	}
	
	public AMCServiceObj operateWebElement(AMCServiceObj amcServiceObj){
		return getInterfaceService(IAMCProviderForDesign.class).operateWebElement(amcServiceObj);
	}
	
	public AMCServiceObj operateVO(AMCServiceObj amcServiceObj){
		return getInterfaceService(IAMCProviderForDesign.class).operateVO(amcServiceObj);
	}
	
}
