package nc.uap.lfw.core.ctx;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.page.IUIMeta;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.LifeCyclePhase;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.RequestLifeCycleContext;
import nc.uap.lfw.jsp.parser.UIMetaParserUtil;
import nc.uap.lfw.jsp.uimeta.UIDialog;
import nc.uap.lfw.jsp.uimeta.UIMeta;

public class WindowContext{
	private PageMeta pageMeta;
	private IUIMeta uiMeta;
	private String id;
	private List<ViewContext> viewCtxs;
	private ViewContext currentViewCtx;
	
	public WindowContext(){
	}
	public PageMeta getWindow() {
		return pageMeta;
	}
	public IUIMeta getUiMeta() {
		return uiMeta;
	}
	public void setUiMeta(IUIMeta uiMeta) {
		this.uiMeta = uiMeta;
	}
	public void setWindow(PageMeta pageMeta) {
		this.pageMeta = pageMeta;
	}
	public void reset() {
		
	}
	
	public ViewContext getCurrentViewContext() {
		if(currentViewCtx == null){
			if(viewCtxs != null && viewCtxs.size() > 0){
				currentViewCtx = viewCtxs.get(0);
			}
		}
		return currentViewCtx;
	}
	
	public void setCurrentViewContext(ViewContext ctx) {
		currentViewCtx = ctx;
	}
	
	public void addViewContext(ViewContext ctx) {
		if(viewCtxs == null)
			viewCtxs = new ArrayList<ViewContext>();
		viewCtxs.add(ctx);
	}
	
	/**
	 * 获取不同View的上下文。本方法只提供系统调用
	 * @param id
	 * @return
	 */
	public ViewContext getViewContext(String id){
		if(viewCtxs == null)
			return null;
		Iterator<ViewContext> it = viewCtxs.iterator();
		while(it.hasNext()){
			ViewContext ctx = it.next();
			if(ctx.getId().equals(id))
				return ctx;
		}
		return null;
	}
	
	public ViewContext[] getViewContexts(){
		return viewCtxs == null ? null : viewCtxs.toArray(new ViewContext[0]);
	}
	
	public void addAppAttribute(String key, Serializable value){
		getWindowSession().setAttribute(key, value);
	}
	
	public void removeAppAttribute(String key){
		getWindowSession().removeAttribute(key);
	}
	
	public Object getAppAttribute(String key){
		return getWindowSession().getAttribute(key);
	}
	
	public Object getAppAttributeAndRemove(String key){
		return getWindowSession().removeAttribute(key);
	}
	
	private WebSession getWindowSession() {
		return LfwRuntimeEnvironment.getWebContext().getWebSession();
	}
	/**
	 * 增加Dialog事件监听
	 * @param event
	 */
	public void addDialogEventHandler(String viewId,String event){
		UIMeta um = (UIMeta) getUiMeta();
		UIDialog dialog = (UIDialog) um.getDialog(viewId);
		if(dialog == null)
			return;
		dialog.addEvent(event);
	}
	/**
	 * 删除Dialog事件监听
	 * @param viewId
	 * @param event
	 */
	public void removeDialogEventHandler(String viewId,String event){
		UIMeta um = (UIMeta) getUiMeta();
		UIDialog dialog = (UIDialog) um.getDialog(viewId);
		if(dialog == null)
			return;
		dialog.removeEvent(event);
	}
	 
	public void popView(String viewId, String width, String height, String title, boolean isPopClose){
		
		UIMeta um = (UIMeta) getUiMeta();
		UIDialog dialog = (UIDialog) um.getDialog(viewId);
		if(dialog == null){
			PageMeta pageMeta = getWindow();
			
			LfwWidget widget = getWindow().getWidget(viewId);
			String refId = widget.getRefId();
			String folderPath =(String) pageMeta.getFoldPath();
			if(refId.startsWith("../")){
				folderPath = "pagemeta/public/widgetpool";
			}
			
			dialog = createDialog(folderPath, widget, viewId, isPopClose);
			dialog.setWidth(width);
			dialog.setHeight(height);
			dialog.setTitle(title);
			 
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
//			UIMeta oum = (UIMeta) LfwRuntimeEnvironment.getWebContext().getOriginalUIMeta();
//			um.setDialog(viewId, dialog);
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.ajax);
			um.setDialog(viewId, dialog);
		}
		dialog.setVisible(true);	
		
	}
	
	
	public void popView(String viewId, String width, String height, String title){
		this.popView(viewId, width, height, title, true);
	}
	 
	/**
	 * @deprecated  use closeView instead
	 * @param viewId
	 */
	public void closeViewDialog(String viewId){
		closeView(viewId);
	}
	
	public void closeView(String viewId){
		UIMeta um = (UIMeta) getUiMeta();
		UIDialog dialog = (UIDialog) um.getDialog(viewId);
		if(dialog == null)
			return;
		dialog.setVisible(false);
	}
	
	private UIDialog createDialog(String folderPath, LfwWidget widget, String viewId, boolean isPopClose) {
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		try{
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			UIMeta um = (UIMeta)LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("$TMP_UM_" + viewId);
			if(um == null){
				String appPath = ContextResourceUtil.getCurrentAppPath();
				String path = appPath + folderPath + "/" + viewId;
				um = new UIMetaParserUtil().parseUIMeta(path, viewId);
			}
			UIDialog uiWidget = new UIDialog();
			uiWidget.setAttribute("isPopClose", isPopClose);
			uiWidget.setId(viewId);
			uiWidget.setUimeta(um);
			return uiWidget;
		}
		finally{
			RequestLifeCycleContext.get().setPhase(phase);
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Map<String, Object> getPlug(String key) {
		return getPlugMap().get(key);
	}
	
	public void addPlug(String key, Map<String, Object> value) {
		getPlugMap().put(key, value);
	}
	
	private Map<String, Map<String, Object>> getPlugMap() {
		Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) getWindowSession().getAttribute("PLUGMAP");
		if(map == null){
			map = new HashMap<String, Map<String, Object>>();
			getWindowSession().setAttribute("PLUGMAP", (Serializable) map);
		}
		return map;
	}
}
