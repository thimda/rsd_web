package nc.uap.lfw.ra.render;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.IDataBinding;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.design.itf.IPaEditorService;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIFormElement;
import nc.uap.lfw.jsp.uimeta.UIMenubarComp;

/**
 * @author renxh 控件渲染器
 * @param <T>
 * @param <K>
 */
public abstract class UIComponentRender<T extends UIComponent, K extends WebComponent> extends UIRender<T, K> {

	public static final String BEFORE_SCRIPT = "beforeScript";
	public static final String AFTER_SCRIPT = "afterScript";
	public static final String BEFORE_LOGIC_SCRIPT = "beforeLogicScript";
	// 组件所用的外部css，此css根据组件的实现不同，不一定能被接受。
	private String styleClass = "";

	public UIComponentRender(T uiEle, K webEle) {
		super(uiEle, webEle);
	}

	/**
	 * 获得当前控件所绑定Dataset
	 * 
	 * @return
	 */
	protected Dataset getDataset() {
		WebComponent comp = this.getWebElement();
		if (!(comp instanceof IDataBinding))
			throw new LfwRuntimeException("the component is not type of IDataBinding:" + getId());
		Dataset ds = getDatasetById(((IDataBinding) comp).getDataset());
		if (ds == null)
			throw new LfwRuntimeException("can not find dataset by assigned id:" + ((IDataBinding) comp).getDataset());
		return ds;
	}

	/**
	 * 2011-8-2 下午07:21:28 renxh des：根据id获得绑定的Dataset
	 * 
	 * @param id
	 * @return
	 */
	protected Dataset getDatasetById(String id) {
		LfwWidget widget = getCurrWidget();
		return widget.getViewModels().getDataset(id);
	}

	/**
	 * 2011-8-2 下午07:21:44 renxh des：进行多语翻译,如果不能翻译,返回原defaultI18nName
	 * 
	 * @param i18nName
	 * @param fieldId
	 * @param defaultI18nName
	 * @param langDir
	 * @return
	 */
	protected String getFieldI18nName(String i18nName, String fieldId, String defaultI18nName, String langDir) {
		if (i18nName != null && !i18nName.equals("")) {
			if (i18nName.equals("$NULL$"))
				return "";
			return translate(i18nName, defaultI18nName == null ? i18nName : defaultI18nName, langDir);
		}
		Dataset ds = getDataset();
		if (ds == null)
			return defaultI18nName;

		if (fieldId != null) {
			int fldIndex = ds.getFieldSet().nameToIndex(fieldId);
			if (fldIndex == -1){
				LfwLogger.error("can not find the field:" + fieldId + ",dataset:" + ds.getId());
				return defaultI18nName;
			}
			Field field = ds.getFieldSet().getField(fldIndex);
			i18nName = field.getI18nName();
			String text = field.getText();
			String defaultValue = text == null ? i18nName : text;
			if (i18nName == null || i18nName.equals(""))
				return defaultI18nName == null ? defaultValue : defaultI18nName;
			else {
				return translate(i18nName, defaultI18nName == null ? defaultValue : defaultI18nName, langDir);
			}
		} else
			return defaultI18nName;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public void setBeforeScript(String beforeScript) {
		setAttribute(BEFORE_SCRIPT, beforeScript);
	}

	public void setAfterScript(String afterScript) {
		setAttribute(AFTER_SCRIPT, afterScript);
	}

	public void setBeforeLogicScript(String beforeLogicScript) {
		setAttribute(BEFORE_LOGIC_SCRIPT, beforeLogicScript);
	}

	public boolean isMenu(UIComponent comp) {
		if (comp instanceof UIMenubarComp) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除控件，删除原始的和当前的
	 * 
	 * @param widgetId
	 * @param compId
	 * @param isMenu
	 */
	public void removeComponent(String widgetId, String compId, boolean isMenu) {
		String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
		String pageId =  LfwRuntimeEnvironment.getWebContext().getWebSession().getPageId();
		IPaEditorService service = NCLocator.getInstance().lookup(IPaEditorService.class);
		PageMeta oriPageMeta = (PageMeta) service.getOriPageMeta(pageId,sessionId);
		PageMeta currPageMeta = LfwRuntimeEnvironment.getWebContext().getPageMeta();
		if (isMenu) {
			if (widgetId == null) {
				oriPageMeta.getViewMenus().removeMenuBar(compId);
			} else {
				LfwWidget widget = oriPageMeta.getWidget(widgetId);
				if (widget != null) {
					widget.getViewMenus().removeMenuBar(compId);
				}
			}
		} 
		else {
			LfwWidget widget = oriPageMeta.getWidget(widgetId);
			if (widget != null) {
				widget.getViewComponents().removeComponent(compId);
			}
			widget = currPageMeta.getWidget(widgetId);
			if (widget != null) {
				widget.getViewComponents().removeComponent(compId);
			}
		}
	}
	
//	/**
//	 * 添加控件，当进行页面添加控件时，将外部的Pagemeta中控件 克隆一份放到原始的和当前的pagemeta中
//	 * @param webComp
//	 * @param widgetId
//	 */
//	public void addWebComponent(WebComponent webComp,String widgetId){
//		IPaEditorService service = NCLocator.getInstance().lookup(IPaEditorService.class);
//		String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
//		String pageId =  LfwRuntimeEnvironment.getWebContext().getWebSession().getPageId();
//		if(isEditMode()){
//			PageMeta oriPageMeta = service.getOriPageMeta(pageId,sessionId);
//			this.addWebComponent(oriPageMeta, webComp, widgetId);
//		}
//		PageMeta currPageMeta = LfwRuntimeEnvironment.getWebContext().getPageMeta();
//		this.addWebComponent(currPageMeta, webComp, widgetId);
//	}
	
//	/**
//	 * 添加控件，当进行页面添加控件时，将外部的Pagemeta中控件 克隆一份放到原始的和当前的pagemeta中
//	 * @param webComp
//	 * @param widgetId
//	 */
//	private void addWebComponent(PageMeta pageMeta,WebComponent webComp,String widgetId){
//		if(webComp == null){
//			throw new LfwRuntimeException("参数webComp不能为null!");
//		}
//		if (webComp instanceof MenubarComp) {
//			if (widgetId == null) {
//				pageMeta.getViewMenus().addMenuBar((MenubarComp)webComp.clone());
//			} else {
//				LfwWidget widget = pageMeta.getWidget(widgetId);
//				if (widget != null) {
//					widget.getViewMenus().addMenuBar((MenubarComp)webComp.clone());
//				}
//			}
//		} else {
//			if (widgetId != null) {
//				LfwWidget widget = pageMeta.getWidget(widgetId);
//				if (widget != null) {
//					if(!(webComp instanceof FormElement))
//						widget.getViewComponents().addComponent((WebComponent)webComp.clone());
//				}
//			}
//			
//		}
//	}
	
	private FormElement getFormElement(UIFormElement fe ,LfwWidget widget){
		FormComp fc  = (FormComp)widget.getViewComponents().getComponent(fe.getFormId());
		FormElement e = fc.getElementById(fe.getId());
		return e;
	}

//	/**
//	 * 获得webComponent
//	 * 
//	 * @param uiEle
//	 * @param pageMeta
//	 * @return
//	 */
//	protected WebComponent getWebComponent(UIComponent uiEle,final PageMeta pageMeta){
//		IPaEditorService ipaService = NCLocator.getInstance().lookup(IPaEditorService.class);
//		String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
//		String pageId =  LfwRuntimeEnvironment.getWebContext().getWebSession().getPageId();
//		PageMeta outPageMeta = ipaService.getOutPageMeta(pageId,sessionId);
//		PageMeta pm = null;
//		if(outPageMeta != null){
//			pm = outPageMeta;
//		}else{
//			pm = pageMeta;
//		}
//		LfwWidget widget = pm.getWidget(uiEle.getWidgetId());
//		WebComponent webComp = null;
//		if(widget != null){
//			if(uiEle instanceof UIFormElement){
//				return this.getFormElement((UIFormElement)uiEle, widget);
//			}else{
//				webComp = (WebComponent)widget.getViewComponents().getComponent(uiEle.getId());
//				if(webComp == null){
//					webComp = (WebComponent)widget.getViewMenus().getMenuBar(uiEle.getId());
//				}
//			}
//			
//		}
//		if(webComp == null){
//			webComp = pageMeta.getViewMenus().getMenuBar(uiEle.getId());
//		}
//		if (webComp!=null && webComp.getWidget() == null)
//			webComp.setWidget(widget);
//		return webComp;
//	}

}
