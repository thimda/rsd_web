/**
 * 
 */
package nc.uap.lfw.design.itf;

import nc.uap.lfw.core.uimodel.AMCServiceObj;

/**
 * 
 * UAPWEB����ӿ���
 * @author chouhl
 *
 */
public interface ILfwAMCDesignDataProvider {
	
	public void createApplication(String appId, String appName);
	
	public AMCServiceObj operateWebElementXML(AMCServiceObj amcServiceObj);
	
	public AMCServiceObj operateWebElement(AMCServiceObj amcServiceObj);
	
	public AMCServiceObj operateVO(AMCServiceObj amcServiceObj);

}
