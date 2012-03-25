package nc.uap.lfw.ra.render.observer;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.WidgetElement;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
import nc.uap.lfw.core.ctrlfrm.plugin.IControlPlugin;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIDialog;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIWidget;
import nc.uap.lfw.ra.itf.IUIRender;
import nc.uap.lfw.render.observer.IUIElementObserver;

public final class UIElementObserver implements IUIElementObserver {

	@Override
	public void notifyChange(String type, Object ele, Object userObject) {
		PageMeta pageMeta = LfwRuntimeEnvironment.getWebContext().getPageMeta();
		UIMeta uiMeta = (UIMeta) LfwRuntimeEnvironment.getWebContext().getUIMeta();
		if (type.equals(UIElement.DELETE)) {
			this.notifyRemoveChild(uiMeta, ele, pageMeta, userObject);
		} else if (type.equals(UIElement.ADD)) {
			this.notifyAddChild(uiMeta, ele, pageMeta, userObject);
		} else if (type.equals(UIElement.UPDATE)) {			
			this.notifyUpdate(uiMeta, ele, pageMeta, userObject);
		} else if (type.equals(UIElement.DESTROY)) {
			this.notifyDestroy(uiMeta, ele, pageMeta, userObject);
		} else {
			throw new LfwRuntimeException("未实现的类型：" + type);
		}
	}
	

	/**
	 * 删除子元素
	 *   UIElement
	 * @param uimeta
	 * @param pageMeta
	 * @param userObject
	 */
	public final void notifyRemoveChild(UIMeta uimeta, Object ele, PageMeta pageMeta, Object userObject) {
		IUIRender render = getRender(uimeta, ele, pageMeta);
		render.notifyRemoveChild(uimeta, pageMeta, userObject);
	}
	
	private IUIRender getRender(UIMeta uimeta, Object ele, PageMeta pm){
		Class<? extends Object> c = null;
		if(ele instanceof UIDialog)
			c = UIWidget.class;
		else
			c = ele.getClass();
		IUIRender render = null;
		if (ele instanceof UIElement){
			UIElement uiElement = (UIElement)ele; 	
			IControlPlugin plugin = ControlFramework.getInstance().getControlPluginByUIClass(c);
			render = plugin.getUIRender(uimeta, uiElement, pm, null, DriverPhase.PC);
		}
		else if (ele instanceof WebElement){
			WebElement webElement = (WebElement)ele;
			IControlPlugin plugin = ControlFramework.getInstance().getControlPluginByClass(c);
			String id = webElement.getId();
			UIMeta um = uimeta;
			UIElement uiEle = null;
			if (webElement instanceof LfwWidget){
				uiEle = UIElementFinder.findUIWidget(um, id);
			}
			else if(webElement instanceof WidgetElement){
				LfwWidget widget = ((WidgetElement)webElement).getWidget();
				if(widget != null)
					um = UIElementFinder.findUIMeta(uimeta, widget.getId());
				uiEle = UIElementFinder.findElementById(um, id);
			}
//			UIElement uiEle = null;
//			if(um != null && um.getId().equals(id + "_um")){
//				uiEle = UIElementFinder.findElementById(um, id + "_um");
//			}
//			else{
//				uiEle = UIElementFinder.findElementById(um, id);
//			}
			render = plugin.getUIRender(uimeta, uiEle, pm, null, DriverPhase.PC);
		}
		return render;
	}

	/**
	 * 销毁自身
	 * 
	 * @param uimeta
	 * @param pageMeta
	 * @param userObject
	 */
	public final void notifyDestroy(UIMeta uimeta, Object ele, PageMeta pageMeta, Object userObject) {
		IUIRender render = getRender(uimeta, ele, pageMeta);
		render.notifyDestroy(uimeta, pageMeta, userObject);
//		this.invoke("notifyDestroy", uimeta, pageMeta, userObject);
	}

	/**
	 * 添加子元素
	 * 
	 * @param uimeta
	 * @param pageMeta
	 * @param userObject
	 */
	public final void notifyAddChild(UIMeta uimeta, Object ele, PageMeta pageMeta, Object userObject) {
		IUIRender render = getRender(uimeta, ele, pageMeta);
		render.notifyAddChild(uimeta, pageMeta, userObject);
//		this.invoke("notifyAdd", uimeta, pageMeta, userObject);
	}

	/**
	 * 更新自身的元素
	 * 
	 * @param uimeta
	 * @param pageMeta
	 * @param userObject
	 */
	public final void notifyUpdate(UIMeta uimeta, Object ele, PageMeta pageMeta, Object userObject) {
		IUIRender render = getRender(uimeta, ele, pageMeta);
		render.notifyUpdate(uimeta, pageMeta, userObject);
	}

//
//	/**
//	 * 2011-10-8 上午09:25:41 renxh des：
//	 * 
//	 * @param className
//	 *            类名称
//	 * @param methodName
//	 *            方法名
//	 * @param paramTypes0
//	 *            构造器参数类型
//	 * @param params0
//	 *            构造器参数
//	 * @param paramTypes1
//	 *            方法参数类型
//	 * @param params1
//	 *            方法参数
//	 */
//	public Object invoke(String className, String methodName, Class<?>[] paramTypes0, Object[] params0, Class<?>[] paramTypes1, Object[] params1) {
//		try {
//			Class<?> c = Class.forName(className);
//			IUIRender obj = (IUIRender) c.getConstructor(paramTypes0).newInstance(params0);
//			obj.setControlPlugin(ControlFramework.getInstance().getControlPluginByUIClass(paramTypes0[0]));
//			Method m = c.getMethod(methodName, paramTypes1);
//			return m.invoke(obj, params1);
//		} catch (Throwable e) {
//			LfwLogger.error(e.getMessage());
//			throw new LfwRuntimeException(e.getMessage());
//		}
//	}

//	/**
//	 * 2011-10-9 上午09:27:47 renxh des：实例化一个对象
//	 * 
//	 * @param className
//	 * @param paramTypes0
//	 * @param params0
//	 * @return
//	 */
//	public Object newInstance(String className, Class<?>[] paramTypes0, Object[] params0) {
//		try {
//			Class<?> c = Class.forName(className);
//			Object obj = c.getConstructor(paramTypes0).newInstance(params0);
//			return obj;
//		} 
//		catch (Throwable e) {
//			LfwLogger.error(e.getMessage());
//			throw new LfwRuntimeException(e);
//		}
//	}
//
//	protected void checkParameters(UIMeta uimeta, PageMeta pageMeta, Object userObject) {
//		if (pageMeta == null) {
//			throw new LfwRuntimeException("paramter pagemeta can't be null!");
//		}
//	}
}
