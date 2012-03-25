/**
 * 
 */
package nc.uap.lfw.design.itf;

import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.uimodel.AMCServiceObj;
import nc.uap.lfw.jsp.uimeta.UIMeta;

/**
 * 
 * UAPWEB服务接口类
 * @author chouhl
 *
 */
public interface IAMCProviderForDesign {
	/**
	 * 操作元素XML文件
	 * @param amcServiceObj
	 * @return
	 */
	public AMCServiceObj operateWebElementXML(AMCServiceObj amcServiceObj);
	/**
	 * 操作元素
	 * @param amcServiceObj
	 * @return
	 */
	public AMCServiceObj operateWebElement(AMCServiceObj amcServiceObj);
	/**
	 * 操作VO
	 * @param amcServiceObj
	 * @return
	 */
	public AMCServiceObj operateVO(AMCServiceObj amcServiceObj);
	/**
	 * 保存Node节点下文件
	 * @param pm
	 * @param meta
	 * @param widget
	 * @param nodePath
	 */
	public void save(PageMeta pm, UIMeta meta, LfwWidget widget, String nodePath);
		
}
