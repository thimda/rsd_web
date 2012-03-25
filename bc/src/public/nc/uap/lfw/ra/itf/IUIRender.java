package nc.uap.lfw.ra.itf;

import java.io.IOException;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.plugin.IControlPlugin;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;


/**
 * @author renxh
 * ��Ⱦ�ӿ�
 *
 */
public interface IUIRender{
	
	/**
	 * 2011-8-2 ����08:01:50 renxh des����Ⱦ html
	 * 
	 * @return
	 */
	public String createRenderHtml();

	/**
	 * 2011-8-2 ����08:02:06 renxh des����Ⱦscript
	 * 
	 * @return
	 */
	public String createRenderScript();
	
	/**
	 * ��̬����htmlռλ��
	 * @return
	 */
	public String createRenderHtmlDynamic();
	
	/**
	 * 2011-7-29 ����04:42:34 renxh
	 * des�� ��Ⱦjs����
	 * @param writer
	 * @throws IOException
	 */
	public String createRenderScriptDynamic();
	
	public String getType();

	public String getDivId();
	
	public String getNewDivId();

	public void setDivId(String divId);
	
	public IControlPlugin getControlPlugin();

	public void setControlPlugin(IControlPlugin controlPlugin);
	
	/**
	 * 2011-10-8 ����09:06:03 renxh des��ɾ��UIMeta�е�Ԫ��ʱ��ִ��ɾ���ű�����,removeΪɾ����Ԫ��
	 */

	public void notifyRemoveChild(UIMeta uiMeta, PageMeta pageMeta, Object obj);

	/**
	 * 2011-10-8 ����09:06:40 renxh des�����UIMetaʱ��ִ�д˲��� // �����Ԫ��
	 */
	public void notifyAddChild(UIMeta uiMeta, PageMeta pageMeta, Object obj);

	/**
	 * ��������
	 * 
	 * @param uiMeta
	 * @param pageMeta
	 */
	public void notifyDestroy(UIMeta uiMeta, PageMeta pageMeta, Object obj);

	/**
	 * 2011-10-8 ����09:07:30 renxh des������UIMeta�е�Ԫ��ʱ��ִ�д˲������������������
	 */
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj);
	
	public IUIRender getParentRender();
	
	public String getId();
	
	public <T extends UIElement> T getUiElement();

	public <K extends WebElement> K getWebElement();

}
