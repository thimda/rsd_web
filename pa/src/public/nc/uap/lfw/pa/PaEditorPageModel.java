/**
 * 
 */
package nc.uap.lfw.pa;

import java.util.List;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.comp.IFrameComp;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIGridLayout;
import nc.uap.lfw.jsp.uimeta.UIGridRowLayout;
import nc.uap.lfw.jsp.uimeta.UIGridRowPanel;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIShutter;
import nc.uap.lfw.jsp.uimeta.UIShutterItem;
import nc.uap.lfw.jsp.uimeta.UIWidget;

/**
 * @author wupeng1
 * @version 6.0 2011-8-2
 * @since 1.6
 */
public class PaEditorPageModel extends PageModel {
	
	 //自定义页面的页面pageid
	 public static final String SELFDEF_PAGEID_KEY = "widgetPageId";
	 //自定义页面的页面widgetId
	 public static final String SELFDEF_WIDGETID_KEY = "widgetViewId";
	 protected void initPageMetaStruct() {
		 
		
		WebSession session = LfwRuntimeEnvironment.getWebContext().getWebSession();
		
		String isEclipse = session.getOriginalParameter("eclipse");	//eclipse是标记从设计工具中访问
		String fromWhere = session.getOriginalParameter("from");	//from是标记从个性化设计中访问
	
		String pageId = null;
		String url1 = null; 
		
		//获取设计器中的编辑区域IFrame
		IFrameComp iframe_tmp = (IFrameComp) this.getPageMeta().getWidget("editor").getViewComponents().getComponent("iframe_tmp");
		
		//从设计器中打开页面
		if(isEclipse != null && isEclipse.equals("1")){
			UIMeta um = (UIMeta) this.getUIMeta();
			UILayoutPanel ele = (UILayoutPanel) getUIElement(um, "ffvp01");
			UIElement menuEle = getUIElement(um, "menu_set");
			ele.removeElement(menuEle);		//隐藏保存按钮
			ele.setAttribute("height", "1");
			
			String pagemetaPath = session.getOriginalParameter("pagemetaPath");
			LfwCacheManager.getSessionCache().put("pagemetaPath", pagemetaPath);
			String projectName = session.getOriginalParameter("projectName");
			String viewId = session.getOriginalParameter("viewId");
			pageId = session.getOriginalParameter("windowId");
			session.setAttribute("_pageId", pageId);
			UIShutter shutLayout = (UIShutter) getUIElement(um, "outlookbar1");
			if(viewId != null){
				UIShutterItem currItem = (UIShutterItem) getUIElement(um, "item_curr");
				shutLayout.removePanel(currItem);
				url1 = "/"+projectName+"/core/uimeta.ra?pageId=" + pageId + "&widgetId="+ viewId + "&emode=1&model=nc.uap.lfw.core.model.RaSelfDefinePageModel&otherPageUniqueId="+LfwRuntimeEnvironment.getWebContext().getPageUniqueId();
			}
			else{
				UIShutterItem ctrlItem = (UIShutterItem) getUIElement(um, "item_ctrl");
				UIShutterItem bindItem = (UIShutterItem) getUIElement(um, "item_bind");
				UIShutterItem entityItem = (UIShutterItem) getUIElement(um, "item_entity");
				shutLayout.removePanel(ctrlItem);
				shutLayout.removePanel(bindItem);
				shutLayout.removePanel(entityItem);
				session.setAttribute(WebConstant.WINDOW_MODE_KEY, WebConstant.MODE_PERSONALIZATION);
				url1 = "/"+projectName+"/core/uimeta.ra?pageId=" + pageId + "&wmodel=1&emode=1&model=nc.uap.lfw.core.model.PageModel&otherPageUniqueId="+LfwRuntimeEnvironment.getWebContext().getPageUniqueId();
			}
		}
		else if(fromWhere != null && fromWhere.equals("1")){	
			String appId = session.getOriginalParameter("appId");
			String viewId = session.getOriginalParameter("viewId");
			String winId = LfwRuntimeEnvironment.getWebContext().getRequest().getParameterMap().get("winId")[1];
			
			if(winId != null){
				session.setAttribute("_pageId", winId);
				UIMeta um = (UIMeta)this.getUIMeta();
				UIShutter shutLayout = (UIShutter) getUIElement(um, "outlookbar1");
				if(viewId != null){
					UIShutterItem currItem = (UIShutterItem) getUIElement(um, "item_curr");
					shutLayout.removePanel(currItem);
					url1 = LfwRuntimeEnvironment.getRootPath() + "/core/uimeta.ra?pageId=" + winId + "&widgetId="+ viewId + "&emode=1&model=nc.uap.lfw.core.model.RaSelfDefinePageModel&otherPageUniqueId="+LfwRuntimeEnvironment.getWebContext().getPageUniqueId();
				}
				else{
//					UIElement ctrlItem = getUIElement(um, "item_ctrl");
//					UIElement bindItem = getUIElement(um, "item_bind");
//					UIElement entityItem = getUIElement(um, "item_entity");
//					shutLayout.removeElement(ctrlItem);
//					shutLayout.removeElement(bindItem);
//					shutLayout.removeElement(entityItem);
					
					String pk_template = session.getOriginalParameter("pk_template");
					if(("mockapp").equals(appId)){
						url1 = LfwRuntimeEnvironment.getRootPath() + "/core/uimeta.ra?pageId=" + winId + "&pk_templateDB=" + pk_template + "&model=nc.uap.lfw.core.model.PageModel&emode=1&otherPageUniqueId="+LfwRuntimeEnvironment.getWebContext().getPageUniqueId();
					}
					else
						url1 = LfwRuntimeEnvironment.getRootPath() + "/core/uimeta.ra?pageId=" + winId + "&pk_templateDB=" + pk_template + "&model=nc.uap.lfw.core.model.PageModel&emode=1&otherPageUniqueId="+LfwRuntimeEnvironment.getWebContext().getPageUniqueId();
//					url1 = LfwRuntimeEnvironment.getRootPath() + "/core/uimeta.ra?pageId=" + winId + "&appId = " + appId + "&model=nc.uap.lfw.core.model.PageModel&emode=1&otherPageUniqueId="+LfwRuntimeEnvironment.getWebContext().getPageUniqueId();
				}
			}
			
		}
		iframe_tmp.setSrc(url1);
	}

	private UIElement getUIElement(UIMeta uimeta, String id) {
		UIElement ele = null;
		UIElement uiEle = uimeta.getElement();
		if(uiEle != null && uiEle instanceof UILayout){
			ele = findUIElementById((UILayout) uiEle, id);
		}
		return ele;
	}
	/**
	 * @param layout
	 * @param id
	 * @return
	 */
	private UIElement findUIElementById(UILayout layout, String id) {
		if(layout.getId() != null && layout.getId().equals(id)){
			return layout;
		}
		if(layout instanceof UIGridLayout){
			List<UILayoutPanel> listRow = ((UIGridLayout)layout).getPanelList();
			if(listRow != null && listRow.size() > 0){
				for(UILayoutPanel rowPanel : listRow){
					UIGridRowLayout row = ((UIGridRowPanel)rowPanel).getRow();
					UIElement findEle = findUIElementById(row, id);
					if(findEle != null)
						return findEle;
				}
			}
		}
		else if(layout instanceof UIWidget){
			UIWidget wdg = (UIWidget) layout;
			UIElement ele1 = wdg.getUimeta().getElement();
			UIElement findEle = findUIElementById((UILayout) ele1, id);
			return findEle;
		}
		else{
			List<UILayoutPanel> list = layout.getPanelList();
			if(list != null && list.size() > 0){
				for(UILayoutPanel panel : list){
					String panelId = (String) panel.getAttribute(UIElement.ID);
					if(panelId != null && panelId.equals(id)){
						return panel;
					}
					UIElement uiEle = panel.getElement();
					if(uiEle instanceof UILayout){
						UIElement find = findUIElementById((UILayout) uiEle, id); 
						if(find != null){
							return find;
						}
					}
					if(uiEle instanceof UIComponent){
						UIComponent comp = (UIComponent) uiEle;
						if(comp.getId() != null && comp.getId().equals(id))
							return comp;
					}
				}
			}
		}
		return null;
	}

}
