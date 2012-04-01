package nc.uap.lfw.core.ctx;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
 * Ӧ�ü���������
 * @author dengjt
 *
 */
public class ApplicationContext {
	/**
	 * �Ի�������
	 */
	public static final String TYPE_DIALOG = "TYPE_DIALOG";
	/**
	 * �������������
	 */
	public static final String TYPE_WINDOW = "TYPE_WINDOW";
	
	public static final String OUTER_WIN_WIDTH = "1200";
	
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
	 * ɾ��ĳ��λ�õĽű�
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
	 * ���ִ�нű�
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
	
	

	public Map<String, Object> getPlug(String key) {
		return getPlugMap().get(key);
	}
	
	public void addPlug(String key, Map<String, Object> value) {
		getPlugMap().put(key, value);
	}
	
	private Map<String, Map<String, Object>> getPlugMap() {
		Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) getAppSession().getAttribute("PLUGMAP");
		if(map == null){
			map = new HashMap<String, Map<String, Object>>();
			getAppSession().setAttribute("PLUGMAP", (Serializable) map);
		}
		return map;
	}
	/**
	 * ��һ���´���
	 * @param url
	 * @param title
	 * @param width
	 * @param height
	 */
	public void navgateTo(String winId, String title, String width, String height) {
		navgateTo(winId, title, width, height, null, null);
	}
	
	
	/**
	 * ��һ���´���
	 * @param url
	 * @param title
	 * @param width
	 * @param height
	 */
	public void navgateTo(String winId, String title, String width, String height, Map<String, String> paramMap) {
		navgateTo(winId, title, width, height, paramMap, null);
	}
	
	/**
	 * ��һ���´���
	 * @param url
	 * @param title
	 * @param width
	 * @param height
	 */
	public void navgateTo(String winId, String title, String width, String height, Map<String, String> paramMap, String type) {
		PageMeta win = getApplication().getWindowConf(winId);
		if(win == null)
			throw new LfwRuntimeException("��ǰAPP��û�ж�Ӧ��window:" + winId);
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
	 * ����url��һ���´���
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
	 * �ر�һ������
	 */
	public void closeWindow(){
		this.addAfterExecScript("window.close();");
	}
	
	/*
	 * �ر���Dialog�򿪵�Window
	 */
	public void closeWinDialog(){
		this.addAfterExecScript("parent.hideDialog();");
	}
	
	
	/**
	 * ��һ���´���
	 * @param url
	 * @param title
	 * @param width
	 * @param height
	 */
	public void popOuterWindow(String url, String title, String width, String height) {
		this.addExecScript("openWindowInCenter('" + url + "', '" + title + "', '" + height + "', '" + width + "', true);\n");
	}
	
	/**
	 * ȫ����һ���´���
	 * @param url
	 * @param title
	 * @param width
	 * @param height
	 */
	public void popOuterWindow(String url, String title) {
		popOuterWindow(url, title, OUTER_WIN_WIDTH, "100%");
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
	 * ˢ��ҳ��
	 */
	public void refreshWindow() {
		this.addExecScript("window.location.href = window.location.href");
	}
	
	/**
	 * ��תҳ��
	 * @param url
	 */
	public void sendRedirect(String url) {
		this.addExecScript("window.location.href='" + url + "'\n");
	}
	
	/**
	 * ��ʷ����
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
	 * ��ȡ��ͬView�������ġ�������ֻ�ṩϵͳ����
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
	 * ��ȡ��ͬView�������ġ�������ֻ�ṩϵͳ����
	 * @param id
	 * @return
	 */
	public WindowContext[] getWindowContexts(){
		return windowCtxs == null ? null : windowCtxs.toArray(new WindowContext[0]);
	}
}
