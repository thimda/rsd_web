package nc.uap.lfw.core;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.RuntimeEnv;
import nc.uap.lfw.core.common.CookieConstant;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.lfw.util.CookieUtil;

/**
 * Web Frame ����ʱ������Ϣ��������Ҫ��չ
 * @author dengjt
 *  
 * @modified 2007-11-23 ����NC������Ϣ֧�֣��ṩInvocationInfoProxy�İ�װ������ӦNC����
 */
public final class LfwRuntimeEnvironment {
	// system.propoties�����õķ�������ַ
	private static String serverAddr = null;
	/**
	 * Lfw webӦ�ø�Ŀ¼·��
	 */
	private static String lfwPath;
	
	/**
	 * Lfw ������·��
	 */
	private static String lfwCtx;
	
	private static ServletContext lfwServletContext;
	
	private static LfwServerConfig lfwServerConfig = null;
	private static Map<String, ModelServerConfig> modelConfigMap = new HashMap<String, ModelServerConfig>();
	private static ThreadLocal<EnviromentObject> threadLocal = new ThreadLocal<EnviromentObject>(){
		protected EnviromentObject initialValue() {
	       return new EnviromentObject();
		}
	};
	
	// lfwȫ��Ӧ������
	
	// ��ģ��ȫ��Ӧ������
	public static ModelServerConfig getModelServerConfig() {
		WebContext ctx = getWebContext();
		if(ctx == null)
			return null;
		String ctxPath = ctx.getRequest().getContextPath();
		ModelServerConfig modelConfig = modelConfigMap.get(ctxPath);
		if(modelConfig == null){
			modelConfig = new ModelServerConfig(ctx.getRequest().getSession().getServletContext());
			modelConfigMap.put(ctxPath, modelConfig);
		}
		return modelConfig;
	}
	
	public static void setServerConfig(LfwServerConfig config){
		lfwServerConfig = config;
	}
	/**
	 * ��ȡwebӦ��ȫ������
	 * @return
	 */
	public static LfwServerConfig getServerConfig() {
		return lfwServerConfig;
	}
	
	public static void setWebContext(WebContext ctx)
	{
		threadLocal.get().webCtx = ctx;
	}
	
	public static WebContext getWebContext()
	{
		return threadLocal.get().webCtx;
	}
	
	public static void setRealPath(String realPath)
	{
		threadLocal.get().realPath = realPath;
	}
	
	public static void setLfwPath(String lfwRealPath)
	{
		lfwPath = lfwRealPath;
	}
	
	public static String getLfwPath()
	{
		return lfwPath;
	}
	
	public static String getLfwCtx() {
		return lfwCtx;
	}
	
	public static void setLfwCtx(String ctx){
		lfwCtx = ctx;
	}
	
	public static ServletContext getLfwServletContext() {
		return lfwServletContext;
	}
	
	public static void setLfwServletContext(ServletContext servletContext){
		lfwServletContext = servletContext;
	}
	
	public static String getRealPath()
	{
		return threadLocal.get().realPath;
	}
	
	public static void setRootPath(String rootPath)
	{
		threadLocal.get().rootPath = rootPath;
	}
	
	public static String getCorePath()
	{
		return threadLocal.get().corePath;
	}
	
	public static void setCorePath(String corePath)
	{
		threadLocal.get().corePath = corePath;
	}
	
	public static String getRootPath()
	{
		return threadLocal.get().rootPath;
	}
	
	public static void setMode(String debug){
		threadLocal.get().mode = debug;
	}
	
	public static String getMode()
	{
		if(threadLocal.get().mode == null){
			if(isDevelopMode())
				return WebConstant.MODE_DEBUG;
			return WebConstant.MODE_DEBUG;
//			return WebConstant.MODE_DEBUG;
		}
		return threadLocal.get().mode;
	}
	
	public static boolean isEditMode(){
		String editModeStr = (String) getWebContext().getWebSession().getAttribute(WebConstant.EDIT_MODE_KEY);
		boolean editMode = editModeStr == null ? false : editModeStr.equals(WebConstant.MODE_PERSONALIZATION);
		return editMode;
	}
	public static boolean isWindowEditorMode(){
		String editModeStr = (String) getWebContext().getWebSession().getAttribute(WebConstant.WINDOW_MODE_KEY);
		boolean editMode = editModeStr == null ? false : editModeStr.equals(WebConstant.MODE_PERSONALIZATION);
		return editMode;
	}
	public static boolean isDevelopMode() {
		return RuntimeEnv.getInstance().isDevelopMode();
	}

	public static void setFromServerName(String serverName) {
		threadLocal.get().serverName = serverName;
	}
	
	public static String getFromServerName() {
		return threadLocal.get().serverName;
	}
	
	public static Integer getSysId() {
		return getModelServerConfig().getSysId();
	}
	
	public static String getThemeId(){
//		 ���ȴ�session�л�ȡ�û��ĸ���������Ϣ
		String themeId = null;
		LfwSessionBean ses = getLfwSessionBean();
		if(ses != null)
			themeId = ses.getThemeId();
		if(themeId != null)
			return themeId;
		
		String sysId = "" + LfwRuntimeEnvironment.getSysId();
		// ���ȴ�cookie�л�ȡ�û����õ�����id
		themeId = CookieUtil.get(((HttpServletRequest) getWebContext().getRequest()).getCookies(), CookieConstant.THEME_CODE + sysId);
		if(themeId == null || themeId.length() == 0 || themeId.equals("null")){
			// ��ģ���ȡĬ�ϵ�����������Ϣ
			themeId = getModelServerConfig().getDefaultThemeId();
		}
		// ����ȡlfwĬ������
		if(themeId == null)
			themeId = getServerConfig().getDefaultThemeId();
		
		if(themeId == null)
			return "webclassic";
		return themeId;
	}
	
	public static LfwTheme getTheme() {
		return LfwThemeManager.getLfwTheme(getRootPath(), getThemeId());
	}
	
	public static ServletContext getServletContext() {
		return threadLocal.get().servletCtx;
	}
	
	public static void setServletContext(ServletContext ctx) {
		threadLocal.get().servletCtx = ctx;
	}
	
	public static void setLangDir(String langDir) {
		threadLocal.get().langDir = langDir;
	}
	public static String getLangDir() {
		String langDir = threadLocal.get().langDir;
		if(langDir == null){
			HttpServletRequest req = getWebContext().getRequest();
			String productCode = (String)req.getAttribute(WebConstant.MULTILANGE_PRODUCT_CODE);
			if(productCode == null || productCode.trim().equals(""))
				productCode = (String)req.getSession().getServletContext().getAttribute(WebConstant.MULTILANGE_PRODUCT_CODE);
			if(productCode == null || productCode.trim().equals(""))
				productCode = req.getContextPath();
			if(productCode.startsWith("/"))
				productCode = productCode.substring(1);
			langDir = productCode;
			threadLocal.get().langDir = productCode;
		}
		return langDir;
	}
	
	 /**
     * �ͻ��˵���������
     * @return
     */
    public static String getClientHost() {
        return InvocationInfoProxy.getInstance().getClientHost();
    }

	
    /**
     * �û�ָ��������Դ
     * 
     * @return
     */
    public static String getUserDataSource() {
    	return InvocationInfoProxy.getInstance().getUserDataSource();
    }

    /**
     * �����û�ָ��������Դ
     * @param ds
     */
    private static void setUserDataSource(String ds) {
    	InvocationInfoProxy.getInstance().setUserDataSource(ds);
    }

    /**
     * ��ȡ���Ա���
     * @return
     */
    public static String getLangCode() {

    	String langId = null;
    	// ���ȴ�session�л�ȡ�û��ĸ���������Ϣ
    	LfwSessionBean ses = LfwRuntimeEnvironment.getLfwSessionBean();
    	if(ses != null)
    		langId = ses.getLangId();
		if(langId != null && !"0".equalsIgnoreCase(langId))
			return langId;
		
		// ���ȴ�cookie�л�ȡ�û����õ�����id
		String sysId = "" + LfwRuntimeEnvironment.getSysId();
		langId = CookieUtil.get(((HttpServletRequest) getWebContext().getRequest()).getCookies(), CookieConstant.LANG_CODE + sysId);
		if(langId == null||"0".equalsIgnoreCase(langId)){
			// ��ģ���ȡĬ�ϵ�����������Ϣ
			langId = getModelServerConfig().getDefaultLangId();
		}
		// ����ȡlfwĬ������
		if(langId == null)
			langId = getServerConfig().getDefaultLangId();
		return langId;
    }
    
    /**
     * �������Ա���
     * 
     * @param langCode
     */
    public static void setLangCode(String langCode) {
    	InvocationInfoProxy.getInstance().setLangCode(langCode);
    }
    

	public static String getServerAddr() {
		return serverAddr;
	}

	public static void setServerAddr(String serverAddr) {
		LfwRuntimeEnvironment.serverAddr = serverAddr;
	}
	
	public static String getDatasource() {
		String dsName = threadLocal.get().datasource;
		return dsName;
	}
	
	public static boolean isCompressStream() {
		return threadLocal.get().compressStream;
	}
	
	public static void setCompressStream(boolean compress){
		threadLocal.get().compressStream = compress;
	}
	
	public static boolean isClientMode() {
		WebSession ses = getWebContext().getWebSession();
		if(ses != null)
			return ses.getAttribute(WebConstant.CLIENT_MODE) != null;
		return false;
	}
	
	
	public static void setDatasource(String ds) {
		threadLocal.get().datasource = ds;
		setUserDataSource(ds);
	}
	
	/**
	 * �Ƿ�LFW�����ڵ�
	 */
	public static boolean isFromLfw() {
		return threadLocal.get().isFromLfw;
	}
	
	public static void setFromLfw(boolean fromLfw){
		threadLocal.get().isFromLfw = fromLfw;
	}
	/**
	 * �Ƿ�DB�ڵ�
	 */
	public static boolean isFromDB() {
		return threadLocal.get().isFromDB;
	}
	
	public static void setFromDB(boolean isFromDB){
		threadLocal.get().isFromDB = isFromDB;
	}
	public static void reset() {
		InvocationInfoProxy.getInstance().reset();
		threadLocal.get().reset();
	}
    
    /**
     * ��ȡ�򿪽ڵ㷽ʽ
     */
    public static String getOpenNodeMode(){
    	return getServerConfig().getDefaultOpenNodeMode();
    }
    /**
     * ����Server����
     */
    public static String getConnectServerCycle(){
		return getServerConfig().getDefaultConnectServerCycle();
    }
    /**
     * ����&Ԥ��ˢ������
     */
    public static String getNoticeRefreshCycle(){
		return getServerConfig().getDefaultNoticeRefreshCycle();
    }

    public static boolean isLoadRunner() {
    	return LfwRuntimeEnvironment.getServerConfig().getLoadRunnerAdpater().equals("1");
    }
    /**
     * ��������ˢ������
     */
    public static String getJobRefreshCycle(){
		return getServerConfig().getDefaultJobRefreshCycle();
    }
    
    public static BrowserSniffer getBrowserInfo() {
		BrowserSniffer sniffer = threadLocal.get().browserSniffer;
		if(sniffer == null){
			sniffer = new BrowserSniffer(getWebContext().getRequest().getHeader("user-agent"));
			threadLocal.get().browserSniffer = sniffer;
		}
		return sniffer;
	}
    
    public static Object getPropertyValue(String key){
    	Map<String, Object> map = threadLocal.get().userObjects;
    	if(map == null)
    		return null;
    	return map.get(key);
    }
    
    public static void setProperty(String key, Object value){
    	Map<String, Object> map = threadLocal.get().userObjects;
    	if(map == null){
    		map = new HashMap<String, Object>();
    		threadLocal.get().userObjects = map;
    	}
    	if(value == null)
    		map.remove(key);
    	else
    		map.put(key, value);
    }
	
	public static LfwSessionBean getLfwSessionBean(){
		return threadLocal.get().loginBean;
	}
	public static void setLfwSessionBean(LfwSessionBean bean){
		threadLocal.get().loginBean = bean;
	}
}

/**
 * ����������װ�ࡣ�ڲ�ʹ���ҵ���Ƶ��������get set��������
 * @author dengjt
 *
 */
class EnviromentObject{
	protected WebContext webCtx;
	protected String realPath;
	protected String rootPath;
	protected String corePath;
	protected String mode = null;
	protected String serverName;
	protected ServletContext servletCtx;
	protected String langDir; 
	protected String themeId;
	protected String datasource;
	protected BrowserSniffer browserSniffer;
	protected Map<String, Object> userObjects;
	protected boolean compressStream = true;
	protected boolean isFromLfw = false;
	protected boolean isFromDB = false;
	protected LfwSessionBean loginBean;
	
	protected void reset() {
		webCtx = null;
		realPath = null;
		rootPath = null;
		corePath = null;
		mode = null;
		serverName = null;
		servletCtx = null;
		langDir = null; 
		themeId = null;
		datasource = null;
		browserSniffer = null;
		userObjects = null;
		compressStream = true;
		loginBean = null;
		isFromLfw = false;
		isFromDB = false;
	}
}
