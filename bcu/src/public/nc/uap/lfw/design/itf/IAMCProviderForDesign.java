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
 * UAPWEB����ӿ���
 * @author chouhl
 *
 */
public interface IAMCProviderForDesign {
	/**
	 * ����Ԫ��XML�ļ�
	 * @param amcServiceObj
	 * @return
	 */
	public AMCServiceObj operateWebElementXML(AMCServiceObj amcServiceObj);
	/**
	 * ����Ԫ��
	 * @param amcServiceObj
	 * @return
	 */
	public AMCServiceObj operateWebElement(AMCServiceObj amcServiceObj);
	/**
	 * ����VO
	 * @param amcServiceObj
	 * @return
	 */
	public AMCServiceObj operateVO(AMCServiceObj amcServiceObj);
	/**
	 * ����Node�ڵ����ļ�
	 * @param pm
	 * @param meta
	 * @param widget
	 * @param nodePath
	 */
	public void save(PageMeta pm, UIMeta meta, LfwWidget widget, String nodePath);
		
}
