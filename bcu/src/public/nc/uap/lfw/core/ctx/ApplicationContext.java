package nc.uap.lfw.core.ctx;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.uap.lfw.core.ClientSession;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.common.ParamConstant;
import nc.uap.lfw.core.event.AbstractServerEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.uimodel.Application;

/**
 * 应用级别上下文
 * @author dengjt
 *
 */
public class ApplicationContext {
	/**
	 * 对话框类型
	 */
	public static final String TYPE_DIALOG = "TYPE_DIALOG";
	/**
	 * 浏览器窗口类型
	 */
	public static final String TYPE_WINDOW = "TYPE_WINDOW";
	private List<String> execScriptList;
	
	private List<String> beforeExecScriptList;
	
	private List<String> afterExecScriptList;
	
	private AbstractServerEvent<?> currentEvent;
	
	private ClientSession clientSession = new ClientSession();

	private List<WindowContext> windowCtxs;

	private WindowContext currentWindowCtx;
	
	public void reset() {
		
	}

	public Application getApplication() {
		return (Application) getAppSession().getAttribute(WebContext.APP_CONF);
	}
	
	public List<String> getExecScript() {
		return execScriptList;
	}

	
	/**
	 * 删除某个位置的脚本
	 * @param index
	 */
	public void removeExecScript(int index) {
		if(this.execScriptList == null)
			return;
		this.execScriptList.remove(index);
	}

	public void addBeforeExecScript(String beforeExecScript) {
		if(this.beforeExecScriptList == null){
			this.beforeExecScriptList = new ArrayList<String>();
		}
		this.beforeExecScriptList.add(beforeExecScript);
	}
	
	public void addAfterExecScript(String beforeExecScript) {
		if(this.afterExecScriptList == null){
			this.afterExecScriptList = new ArrayList<String>();
		}
		this.afterExecScriptList.add(beforeExecScript);
	}

	/**
	 * 添加执行脚本
	 * @param execScript
	 */
	public int addExecScript(String execScript) {
		if(this.execScriptList == null){
			this.execScriptList = new ArrayList<String>();
		}
		this.execScriptList.add(execScript);
		return this.execScriptList.size() - 1;
	}
	
	public List<String> getBeforeExecScript() {
		return beforeExecScriptList;
	}
	
	public List<String> getAfterExecScript() {
		return afterExecScriptList;
	}
	
	public AbstractServerEvent<?> getCurrentEvent() {
		return currentEvent;
	}

	public void setCurrentEvent(AbstractServerEvent<?> currentEvent) {
		this.currentEvent = currentEvent;
	}
	

	public ClientSession getClientSession() {
		return clientSession;
	}

	public void setClientSession(ClientSession clientSession) {
		this.clientSession = clientSession;
	}

	public void setClientAttribute(String key, String value) {
		clientSession.setAttribute(key, value);
	}
	
	public String getAppId() {
		return (String) LfwRuntimeEnvironment.getWebContext().getAppSession().getAttribute(WebContext.APP_ID);
	}
	
	public void addAppAttribute(String key, Serializable value){
		getAppSession().setAttribute(key, value);
	}
	
	public Object getAppAttribute(String key){
		return getAppSession().getAttribute(key);
	}
	
	private WebSession getAppSession() {
		return LfwRuntimeEnvironment.getWebContext().getAppSession();
	}
	
	/**
	 * 打开一个新窗口
	 * @param url
	 * @param title
	 * @param width
	 * @param height
	 */
	public void navgateTo(String winId, String title, String width, String height) {
		navgateTo(winId, title, width, height, null, null);
	}
	
	
	/**
	 * 打开一个新窗口
	 * @param url
	 * @param title
	 * @param width
	 * @param height
	 */
	public void navgateTo(String winId, String title, String width, String height, Map<String, String> paramMap) {
		navgateTo(winId, title, width, height, paramMap, null);
	}
	
	/**
	 * 打开一个新窗口
	 * @param url
	 * @param title
	 * @param width
	 * @param height
	 */
	public void navgateTo(String winId, String title, String width, String height, Map<String, String> paramMap, String type) {
		PageMeta win = getApplication().getWindowConf(winId);
		if(win == null)
			throw new LfwRuntimeException("当前APP中没有对应的window:" + winId);
		String url = LfwRuntimeEnvironment.getRootPath() + "/app/" + getAppId() + "/" + winId + "?";
		String appUniqueId = LfwRuntimeEnvironment.getWebContext().getAppUniqueId();
		url += ParamConstant.APP_UNIQUE_ID + "=" + appUniqueId;
		
		if(paramMap != null){
			Iterator<Entry<String, String>> entryIt = paramMap.entrySet().iterator();
			while(entryIt.hasNext()){
				Entry<String, String> entry = entryIt.next();
				url += ("&" + entry.getKey() + "=" + entry.getValue());
			}
		}
		if(TYPE_WINDOW.equals(type)){
			this.addExecScript("openWindowInCenter('" + url + "', '" + title + "', " + height + ", " + width + ", true);\n");
		}else{
			this.addExecScript("showDialog(\"" + url + "\", \"" + title + "\", " + width + "," + height + ", \"" + winId + "\",   true  ,  true );");
		}
	}

	/**
	 * 根据url打开一个新窗口
	 * 
	 * @param url
	 * @param title
	 * @param width
	 * @param height
	 * @param id
	 * @param refresh
	 * @param clearState
	 */
	public void showModalDialog(String url, String title, String width, String height, String id, boolean refresh, boolean clearState, String type) {
		String appUniqueId = LfwRuntimeEnvironment.getWebContext().getAppUniqueId();
		if(url.indexOf("?") != -1)
			url += "&";
		else
			url += "?";
		url += ParamConstant.APP_UNIQUE_ID + "=" + appUniqueId;
//		this.addExecScript("showDialog(\"" + url + "\", \"" + title + "\", " + width + "," + height + ", \"" + id + "\", " + refresh + "," + clearState + ");");
		if(TYPE_WINDOW.equals(type)){
			this.addExecScript("openWindowInCenter('" + url + "', '" + title + "', " + height + ", " + width + ");\n");
		}else{
			this.addExecScript("showDialog(\"" + url + "\", \"" + title + "\", " + width + "," + height + ", \"" + id + "\"," + refresh + "," + clearState + ");");
		}
	}

	/**
	 * 关闭一个窗口
	 */
	public void closeWindow(){
		this.addAfterExecScript("window.close();");
	}
	
	/*
	 * 关闭以Dialog打开的Window
	 */
	public void closeWinDialog(){
		this.addAfterExecScript("parent.hideDialog();");
	}
	
	
	/**
	 * 打开一个新窗口
	 * @param url
	 * @param title
	 * @param width
	 * @param height
	 */
	public void popOuterWindow(String url, String title, String width, String height) {
		this.addExecScript("openWindowInCenter('" + url + "', '" + title + "', " + height + ", " + width + ", true);\n");
	}
	
	public WindowContext getCurrentWindowContext() {
		if(currentWindowCtx == null){
			if(windowCtxs != null && windowCtxs.size() > 0){
				currentWindowCtx = windowCtxs.get(0);
			}
		}
		return currentWindowCtx;
	}
	
	public void setCurrentWindowContext(WindowContext ctx) {
		currentWindowCtx = ctx;
	}
	
	/**
	 * 刷新页面
	 */
	public void refreshWindow() {
		this.addExecScript("window.location.href = window.location.href");
	}
	
	/**
	 * 跳转页面
	 * @param url
	 */
	public void sendRedirect(String url) {
		this.addExecScript("window.location.href='" + url + "'\n");
	}
	
	/**
	 * 历史回退
	 */
	public void historyBack() {
		String referer = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(WebSession.REFERER);
		if(referer != null)
			sendRedirect(referer);
		else
			this.addExecScript("window.history.go(-1);");
	}
	
	public void addWindowContext(WindowContext ctx) {
		if(windowCtxs == null)
			windowCtxs = new ArrayList<WindowContext>();
		windowCtxs.add(ctx);
	}
	
	/**
	 * 获取不同View的上下文。本方法只提供系统调用
	 * @param id
	 * @return
	 */
	public WindowContext getWindowContext(String id){
		if(windowCtxs == null)
			return null;
		Iterator<WindowContext> it = windowCtxs.iterator();
		while(it.hasNext()){
			WindowContext ctx = it.next();
			if(ctx.getId().equals(id))
				return ctx;
		}
		return null;
	}
	
	/**
	 * 获取不同View的上下文。本方法只提供系统调用
	 * @param id
	 * @return
	 */
	public WindowContext[] getWindowContexts(){
		return windowCtxs == null ? null : windowCtxs.toArray(new WindowContext[0]);
	}
}
