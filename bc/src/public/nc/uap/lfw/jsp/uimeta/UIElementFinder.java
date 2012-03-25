package nc.uap.lfw.jsp.uimeta;

import java.util.Iterator;

import nc.uap.lfw.core.comp.IContainerComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;


public final class UIElementFinder {

	/**
	 * 在被查找元素中找到对应的元素的父元素,被查找元素是布局或者控件
	 * @param targetEle 被查找元素
	 * @param searchEle 查找元素
	 * @return
	 */
	public static UIElement findParent(UIElement targetEle, UIElement searchEle) {
		if (targetEle == null || targetEle instanceof UIComponent)
			return null;
		if(searchEle instanceof UILayoutPanel)
			return findParent(targetEle, ((UILayoutPanel) searchEle).getLayout(), (UILayoutPanel) searchEle);
		
		//布局需按照panel查找
		if (targetEle instanceof UILayout) {
			Iterator<UILayoutPanel> it = ((UILayout)targetEle).getPanelList().iterator();
			while(it.hasNext()){
				UILayoutPanel panel = it.next();
				UIElement ele = findParent(panel, searchEle);
				if(ele != null)
					return ele;
			}
		} 
		else if (targetEle instanceof UILayoutPanel) {
			UIElement childEle = ((UILayoutPanel)targetEle).getElement();
			if(isEqual(childEle, searchEle))
				return targetEle;
			return findParent(childEle, searchEle);
		}
		return null;
	}
	
	/**
	 * 在被查找元素中找到对应的元素的父元素,被查找元素是布局Panel
	 * @param targetEle 被查找元素
	 * @param searchEle 查找元素
	 * @return
	 */
	public static UIElement findParent(UIElement targetEle, UILayout searchPEle, UILayoutPanel searchEle) {
		if (targetEle == null || targetEle instanceof UIComponent)
			return null;
		
		//布局需按照panel查找
		if (targetEle instanceof UILayout) {
			Iterator<UILayoutPanel> it = ((UILayout)targetEle).getPanelList().iterator();
			while(it.hasNext()){
				UILayoutPanel panel = it.next();
				if(isEqual(targetEle, panel, searchPEle, searchEle))
					return targetEle;
				UIElement ele = findParent(panel, searchPEle, searchEle);
				if(ele != null)
					return ele;
			}
		} 
		else if (targetEle instanceof UILayoutPanel) {
			UIElement childEle = ((UILayoutPanel)targetEle).getElement();
			return findParent(childEle, searchPEle, searchEle);
		}
		return null;
	}
	
	public static boolean isEqual(UIElement targetPEle, UIElement targetEle, UIElement searchPEle, UIElement searchEle){
		return isEqual(targetPEle, searchPEle) && isEqual(targetEle, searchEle);
	}
	
	public static boolean isEqual(UIElement targetEle, UIElement searchEle){
		if(targetEle == null || searchEle == null)
			return false;
		if(targetEle.getClass().equals(searchEle.getClass())){
			return targetEle.getId().equals(searchEle.getId());
		}
		return false;
	}
//	
//	private static boolean isEquals(UIElement ele1,UIElement ele2){
//		if(ele1 == null || ele2 == null) return false;
//		if(ele1 == ele2) return true;
//		if(ele1 instanceof UIMeta && ele2 instanceof UIMeta){
//			if(((UIMeta)ele1).getLayout() == null && ((UIMeta)ele2).getLayout() == null){ // 同为页面级的uimeta，parent为null，相同
//				return true;
//			}
//			else if(((UIMeta)ele1).getLayout() != null && ((UIMeta)ele2).getLayout() != null){
//				UIElement p1 = ((UIMeta)ele1).getLayout();
//				UIElement p2 = ((UIMeta)ele2).getLayout();
//				if(isEquals(p1, p2)){
//					return true;
//				}else{
//					return false;
//				}
//			}
//			else {
//				return false;
//			}
//		}
//		if(ele1.getAttribute(UIElement.ID) == null || ele2.getAttribute(UIElement.ID) == null) return false;
//		if(ele1.getAttribute(UIElement.ID).equals(ele2.getAttribute(UIElement.ID))){
//			if((ele1 instanceof UIWidget) && (ele2 instanceof UIWidget)){
//				return true;
//			}
//			String widgetId1 = (String) ele1.getAttribute(UIElement.WIDGET_ID);
//			String widgetId2 = (String) ele2.getAttribute(UIElement.WIDGET_ID);
//			if(widgetId1 != null && widgetId2!=null){
//				if(widgetId1.equals(widgetId2)){
//					return true;
//				}
//				else{
//					return false;
//				}
//			}
//			else{
//				if(widgetId1 == widgetId2){
//					return true;
//				}
//				else{
//					return false;
//				}
//			}
//		}
//		return false;
//	}
//
//	/**
//	 * 
//	 * @param targetEle
//	 * @return
//	 */
//	private static UIElement findParent(UIMeta targetEle, UIElement searchEle) {
//		if (targetEle == null)
//			return null;
//		UIElement uiEle = searchEle;
//		UIElement ele2 = targetEle.getElement();
//		if(isEquals(ele2,uiEle)){
//			if(targetEle.getLayout() != null){
//				return targetEle.getLayout();
//			}else{
//				return targetEle;
//			}
//		}
//
//		UIElement ele3 = findParent(ele2, uiEle);
//		if (ele3 != null)
//			return ele3;
//
//		Map<String, UIWidget> map = targetEle.getDialogMap();
//		Iterator<String> keys = map.keySet().iterator();
//		while (keys.hasNext()) {
//			String key = keys.next();
//			UIWidget widget = map.get(key);
//			if (isEquals(widget,uiEle)) {
//				return targetEle;
//			} else {
//				UIMeta uimeta = widget.getUimeta();
//				ele3 = findParent(uimeta, uiEle);
//				if (ele3 != null)
//					return ele3;
//			}
//		}
//
//		return null;
//	}
//	
//	/**
//	 * 从元素中找到自身
//	 * @param ele
//	 * @param searchEle
//	 * @return
//	 */
//	public static UIElement findSelf(UIMeta ele, UIElement searchEle) {
//		if (ele == null)
//			return null;
//		UIElement uiEle = searchEle;
//		if(isEquals(ele, uiEle)){
//			return ele;
//		}
//		UIElement ele2 = ele.getElement();
//		if(isEquals(ele2,uiEle)){
//			return ele2;
//		}
//
//		UIElement ele3 = findSelf(ele2, uiEle);
//		if (ele3 != null)
//			return ele3;
//
//		Map<String, UIWidget> map = ele.getDialogMap();
//		Iterator<String> keys = map.keySet().iterator();
//		while (keys.hasNext()) {
//			String key = keys.next();
//			UIWidget widget = map.get(key);
//			if (isEquals(widget,uiEle)) {
//				return widget;
//			} else {
//				UIMeta uimeta = widget.getUimeta();
//				ele3 = uiEle.findSelf(uimeta);
//				if (ele3 != null)
//					return ele3;
//			}
//		}
//
//		return null;
//	}
//	/**
//	 * 2011-9-27 下午08:39:26 renxh des：在ele中查找 uiEle的父节点
//	 * 
//	 * @param uiEle
//	 * @param ele
//	 * @return
//	 */
//	public UIElement findParent(UILayout ele, UIElement uiEle) {
//		if (ele == null)
//			return null;
//		if (ele instanceof UIWidget) {
//			UIWidget widget = (UIWidget) ele;
//			UIMeta uimeta = widget.getUimeta();
//			if (isEquals(uimeta,uiEle))
//				return ele;
//			return findParent(uimeta, uiEle);
//		}
//		else if(ele instanceof UIGridLayout){
//			List<UILayoutPanel> list = ((UIGridLayout)ele).getPanelList();
//			for(UILayoutPanel mockRow : list){
//				UIGridRowLayout row = ((UIGridRowPanel)mockRow).getRow(); 
//				if (isEquals(row, uiEle)){
//					return ele;
//				}else{
//					UIElement ele2 = findParent(row);
//					if (ele2 != null) {
//						return ele2;
//					}
//				}
//			}
//		} 
//		else {
//			List<UILayoutPanel> panelList = ele.getPanelList();
//			for (UILayoutPanel panel : panelList) {
//				if (isEquals(panel,uiEle)) {
//					return ele;
//				} else {
//					UIElement ele2 = findParent(panel, uiEle);
//					if (ele2 != null) {
//						return ele2;
//					}
//				}
//			}
//			if(ele instanceof UITabComp){
//				UITabComp tabComp = (UITabComp)ele;
//				UITabRightPanelPanel rightPanel = tabComp.getRightPanel();
//				if(rightPanel != null){
//					if (isEquals(rightPanel,uiEle)) {
//						return ele;
//					}else{
//						UIElement ele2 = findParent(rightPanel, uiEle);
//						return ele2;
//					}
//				}
//			}
//		}
//
//		return null;
//	}
//	
//	private static UIElement findSelf(UIElement ele, UIElement searchEle) {
//		if (ele == null)
//			return null;
//		UIElement uiEle = searchEle;
//		if(isEquals(ele, uiEle)){ // 与参数是否相等
//			return ele;
//		}
//		if (ele instanceof UIMeta) {
//			return findSelf((UIMeta) ele, uiEle);
//		} 
//		else if (ele instanceof UILayout) {
//			return findSelf((UILayout) ele, uiEle);
//		} 
//		else if (ele instanceof UILayoutPanel) {
//			return findSelf((UILayoutPanel) ele, uiEle);
//		}
//		return null;
//	}
//	
//	/**
//	 * 在其他uimeta中找到自己
//	 * @param ele
//	 * @return
//	 */
//	private static UIElement findSelf(UILayout ele, UIElement searchEle) {
//		if (ele == null)
//			return null;
//		UIElement uiEle = searchEle;
//		if (ele instanceof UIWidget) {
//			UIWidget widget = (UIWidget) ele;
//			UIMeta uimeta = widget.getUimeta();
//			if (isEquals(uimeta,uiEle))
//				return uimeta;
//			return findSelf(uimeta, uiEle);
//		}
//		else if(ele instanceof UIGridLayout){
//			List<UILayoutPanel> list = ((UIGridLayout)ele).getPanelList();
//			for(UILayoutPanel mockRow : list){
//				UIGridRowLayout row = ((UIGridRowPanel)mockRow).getRow(); 
//				if (isEquals(row,uiEle)){
//					return row;
//				}
//				else{
//					UIElement ele2 = findSelf(row, searchEle);
//					if (ele2 != null) {
//						return ele2;
//					}
//				}
//			}
//		} 
//		else {
//			List<UILayoutPanel> panelList = ele.getPanelList();
//			for (UILayoutPanel panel : panelList) {
//				if (isEquals(panel,uiEle)) {
//					return panel;
//				} 
//				else {
//					UIElement ele2 = findSelf(panel, uiEle);
//					if (ele2 != null) {
//						return ele2;
//					}
//				}
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * 
//	 * @param ele
//	 * @return
//	 */
//	private static UIElement findParent(UILayoutPanel ele, UIElement searchEle) {
//		UIElement uiEle = searchEle;
//		UIElement ele2 = ele.getElement();
//		if (isEquals(ele2,uiEle)) {
//			return ele;
//		} else if (ele2 instanceof UILayout) {
//			return findParent((UILayout) ele2, uiEle);
//		}
//		return null;
//	}
//	
//	/**
//	 * 在其他uimeta中找到自己
//	 * @param ele
//	 * @return
//	 */
//	private static UIElement findSelf(UILayoutPanel ele, UIElement searchEle) {
//		UIElement ele2 = ele.getElement();
//		if (isEquals(ele2, searchEle)) {
//			return ele2;
//		} else if (ele2 instanceof UILayout) {
//			return findSelf((UILayout) ele2, searchEle);
//		}
//		return null;
//	}
//	
//	private static String findWidget(UIMeta uimeta, UIElement searchEle){
//		if(searchEle instanceof UIWidget){
//			return (String)searchEle.getAttribute(UIElement.ID);
//		}
//		String widget = (String)searchEle.getAttribute(UIElement.WIDGET_ID);
//		if(widget != null ){
//			return widget;
//		}
//		UIElement parent = findParent(uimeta, searchEle);
//		if(parent != null){
//			widget = findWidget(uimeta, parent);
//		}
//		return widget;
//	}
//	

//	/*
//	 * 根据ID查找页面元素
//	 */
//	public UIElement findElementById(UILayout ele, String id) {
//		if (ele == null)
//			return null;
//		if ((ele.getAttribute(UIElement.ID) != null) && ele.getAttribute(UIElement.ID).equals(id))
//			return ele;
//		if (ele instanceof UIGridLayout) {
//			List<UILayoutPanel> list = ((UIGridLayout)ele).getPanelList();
//			for(UILayoutPanel mockRow : list){
//				UIGridRowLayout row = ((UIGridRowPanel)mockRow).getRow(); 
//				UIElement ele2 = findElementById(row, id);
//				if (ele2 != null) {
//					return ele2;
//				}
//			}
//		} else if (ele instanceof UIWidget) {
//			UIWidget wdgt = (UIWidget) ele;
//			return findElementById(wdgt.getUimeta(), id);
//		} else {
//			List<UILayoutPanel> list = ele.getPanelList();
//			if (list != null && list.size() > 0) {
//				for (UILayoutPanel panel : list) {
//					UIElement rs = findElementById(panel, id);
//					if (rs != null) {
//						return rs;
//					}
//				}
//			}
//
//		}
//		return null;
//	}
//

	/**
	 * 查找元素，本元素仅包含控件与布局
	 */
	public static UIElement findElementById(UIElement ele, String id) {
		UIElement uiEle = null;
		if (ele == null || id == null)
			return null;
		
		if(ele.getId() != null && ele.getId().equals(id))
			return ele;
		
		if(ele instanceof UILayoutPanel){
			uiEle = findElementById(((UILayoutPanel) ele).getElement(), id);
		}
		else if (ele instanceof UILayout) {
			Iterator<UILayoutPanel> it = ((UILayout)ele).getPanelList().iterator();
			while(it.hasNext()){
				UILayoutPanel panel = it.next();
				uiEle = findElementById(panel, id);
				if(uiEle != null)
					break;
			}
		}
		return uiEle;
	}
	
	public static WebElement findWebElementById(PageMeta pagemeta, String widgetId, String id){
		LfwWidget widget = pagemeta.getWidget(widgetId);
		if(widget == null)
			return null;
		return findWebElementById(widget, id);
	}
	
	public static WebElement findWebElementById(PageMeta pagemeta, String widgetId, String parentId, String id){
		LfwWidget widget = pagemeta.getWidget(widgetId);
		if(widget == null)
			return null;
		return findWebElementById(widget, parentId, id);
	}

	public static WebElement findWebElementById(LfwWidget widget, String id){
		WebComponent[] components = widget.getViewComponents().getComponents();
		for (int i = 0; i < components.length; i++) {
			if(components[i].getId().equals(id))
				return components[i];
		}
		return null;
	}
	
	public static WebElement findWebElementById(LfwWidget widget, String parentId, String id){
		WebElement ele = findWebElementById(widget, parentId);
		if(ele instanceof IContainerComp)
			return ((IContainerComp)ele).getElementById(id);
		return null;
	}
	
	public static UIElement findElementById(UIElement ele, String pid, String id) {
		if (ele == null || id == null)
			return null;
		UIElement uiEle = findElementById(ele, pid);
		if(!(uiEle instanceof UILayout))
			return null;
		Iterator<UILayoutPanel> it = ((UILayout)uiEle).getPanelList().iterator();
		while(it.hasNext()){
			UILayoutPanel panel = it.next();
			if(id.equals(panel.getId()))
				return panel;
		}
		return null;
	}
	
	/**
	 * 按照WidgetId， UIMetaID 查找UIMeta
	 * @param uiEle
	 * @param widgetId
	 * @param id
	 * @return
	 */
	public static UIMeta findUIMeta(UIElement uiEle, String widgetId, String id){
		UIElement ele = findElementById(uiEle, widgetId);
		if(ele instanceof UIWidget){
			UIMeta um = ((UIWidget)ele).getUimeta();
			if(um == null)
				return null;
			if(id.equals(um.getId()))
				return um;
		}
		return null;
	}
	
	/**
	 * 按照WidgetId， UIMetaID 查找UIMeta
	 * @param uiEle
	 * @param widgetId
	 * @param id
	 * @return
	 */
	public static UIMeta findUIMeta(UIElement uiEle, String widgetId){
		UIElement ele = findElementById(uiEle, widgetId);
		if(ele != null && ele instanceof UIWidget){
			return ((UIWidget)ele).getUimeta();
		}
		return null;
	}
	
	/**
	 * 按照WidgetId， UIMetaID 查找UIMeta
	 * @param uiEle
	 * @param widgetId
	 * @param id
	 * @return
	 */
	public static UIWidget findUIWidget(UIMeta uiEle, String widgetId){
		UIWidget um = uiEle.getDialog(widgetId);
		if(um != null)
			return um;
		UIElement ele = findElementById(uiEle, widgetId);
		if(ele != null && ele instanceof UIWidget){
			return (UIWidget) ele;
		}
		return null;
	}

//	private static UIElement findElementById(UIComponent ele, String id) {
//		if (ele == null)
//			return null;
//		if (ele.getId() != null && ele.getId().equals(id))
//			return ele;
//		return null;
//	}
//
//	private UIElement findElementById(UILayoutPanel ele, String id) {
//		if (ele == null)
//			return null;
//		if ((ele.getAttribute(UIElement.ID) != null) && ele.getAttribute(UIElement.ID).equals(id))
//			return ele;
//		return findElementById(ele.getElement(), id);
//	}
}
