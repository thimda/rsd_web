package nc.uap.lfw.core.tags;


import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.ClientSession;
import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.model.util.LfwMaskerUtil;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.serializer.impl.LfwJsonSerializer;
import nc.uap.lfw.util.JsURLEncoder;
import nc.uap.lfw.util.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;

import org.json.MarshallException;

/**
 * PageModel对应Tag.此Tag将初始化PageModel,写入页面必要片段.并将PageModel存放在PageContext以供
 * 后续控件调用。
 * 注意：所有其它Lfw对应Tag必须包含在此Tag内，以取得对应的上下文
 * 
 * @author dengjt
 * 2007-1-25 
 */ 
public class PageModelTag extends WebElementTag {
	private String modelId = null; 
//	private String className = null;  
	private boolean showLoadingPage = false;
	private boolean simple = false;
	protected static final String DS_SCRIPT = "dsScript";
	protected static final String ALL_SCRIPT = "allScript";
	
	@SuppressWarnings("unchecked")
	protected void doRender() throws JspException, IOException {
		
//		ConcurrentMap<String, StringBuffer> bodyScriptMap = null;
		HttpServletRequest request = (HttpServletRequest) ((PageContext)getJspContext()).getRequest();
		PageModel model = (PageModel) request.getAttribute("pageModel");
		modelId = model.getPageModelId();
		getJspContext().setAttribute("pageModel", model);
		try {
			long startTime = 0;
			if(Logger.isInfoEnabled())
				startTime = System.currentTimeMillis();
			
//			if(showLoadingPage){
//				getJspContext().getOut().write(addLoadingPage());
//				//getJspContext().getOut().flush();
//			}
			
			
			//获得此页面的PageMeta
			PageMeta pageMeta = model.getPageMeta();
			//如果是简单模式，不进行脚本渲染
			if(simple)
				return;
			
			
			//在用户session中设置一个放置buf的map，此map根据pageId区分buf
//			synchronized(PageModelTag.class) {
//				if((bodyScriptMap = (ConcurrentMap<String, StringBuffer>) ((PageContext)this.getJspContext()).getSession().getAttribute("bodyScriptMap")) == null){
//					bodyScriptMap = new ConcurrentHashMap<String, StringBuffer>();
//					((PageContext)getJspContext()).getSession().setAttribute(BODY_SCRIPT_MAP, bodyScriptMap);
//				}
//			}
			//此Buf中的内容，将能够独立形成脚本文件
			StringBuffer scriptBuf = new StringBuffer(4096);
			getPageContext().setAttribute(ALL_SCRIPT, scriptBuf);
			//将此buf放在页面上下文中
//			bodyScriptMap.put(model.getPageModelId(), scriptBuf);
			
			//控件绑定ds的逻辑统一放在此处。最终合并到scriptBuf中，之所以这么做，是因为tree等画节点的时候需要完整的width信息。而只有
			//所有控件都渲染完成，这种信息才能由浏览器补充完整。所以将设置数据放在最后进行。注意：所有缓画的控件，例如TabItem和CardLayout等
			//都可能需要自己处理这种缓画策略
			StringBuffer dsScriptBuf = new StringBuffer();
			getJspContext().setAttribute(DS_SCRIPT, dsScriptBuf);
			
			
			//页面初始化函数,所有js逻辑包含其中
			scriptBuf.append("function pageBodyScript(){\n");
			
			//加入masker信息
			LfwMaskerUtil.makeMaskerScript(scriptBuf);
			
			PageContext pageContext = (PageContext) getJspContext();
			Iterator it = pageContext.getRequest().getParameterMap().entrySet().iterator();
			
			scriptBuf.append(createPageUI());
			//将所有请求参数植入全局请求参数Map
			scriptBuf.append("window.$paramsMap = new HashMap();\n");
			
			while(it.hasNext())
			{
				Entry entry = (Entry) it.next();
				scriptBuf.append("setParameter(\"")
				         .append(entry.getKey())
				         .append("\",\"");
				         String str = StringUtil.convertToCorrectEncoding(((String[])entry.getValue())[0]); 
		         scriptBuf.append(JsURLEncoder.encode(str, "UTF-8")) 
				         .append("\");\n");
			}
			
			//render client attribute
			renderClientSessionAttribute(model, scriptBuf);
			
			if(pageMeta.getCaption() != null)
				scriptBuf.append("document.title = \"")
						 .append(pageMeta.getCaption())
						 .append("\";\n");
			
			Map<String, ExtAttribute> attrMap = pageMeta.getExtendMap();
			if(attrMap != null && !attrMap.isEmpty())
			{
				Iterator<String> attrIt = attrMap.keySet().iterator();
				while(attrIt.hasNext())
				{
					String attrName = attrIt.next();
					scriptBuf.append("pageUI.addAttribute('" + attrName + "', '" + attrMap.get(attrName) + "');\n");
				}
			}
			
			if(getJspBody() != null)
				getJspBody().invoke(getJspContext().getOut());
			
			//将没有加载的空Widget进行渲染。
			// 注释掉 renxh 原因，在多渲染引擎render进行渲染时，这里的空widget会影响渲染结果 2011-10-24
			/*LfwWidget[] widgets = pageMeta.getWidgets();
			for (int i = 0; i < widgets.length; i++) {
				LfwWidget wd = widgets[i];
				if(!wd.isRendered()){
					WidgetTag wt = new WidgetTag();
					wt.setRenderDiv(true);
					wt.setId(wd.getId());
					wt.setJspContext(this.getJspContext());
					wt.doRender();
				}
			}*/
			
			

			//将dsScript的设置脚本加入页面脚本中
			scriptBuf.append(getJspContext().getAttribute(DS_SCRIPT).toString());
			
//			String connStr = renderConnector();
//			if(connStr != null)
//				scriptBuf.append(connStr);
			
			// lfw框架一般在afterPageInit方法中初始化billUI,如果希望在初始化UI之前做些事情写此方法即可
            //scriptBuf.append("pageUI.beforeBillUIInit();\n");
			
//			PageStates pageStates = pageMeta.getPageStates();
//			if(pageStates != null)
//			{
//				String currentState = pageStates.getCurrentState();
//				if(currentState != null && !currentState.equals(""))
//				{
//					scriptBuf.append("pageUI.setPageState('" + currentState + "');\n");
//				}
//			}
//			scriptBuf.append("pageUI.setOperateState(" + pageMeta.getOperatorState() + ");\n");
			
//			String busiState = pageMeta.getBusiState();
//			if(busiState != null && !"".equals(busiState))
//				scriptBuf.append("pageUI.setBusinessState(" + busiState + ");\n");
			scriptBuf.append("pageUI.updateButtons();\n");

			scriptBuf.append("if(typeof(externalInit) != 'undefined'){\n externalInit();\n}\n");
			scriptBuf.append("pageUI.$afterPageInit();\n");
			
			//add json support
//			if(!LfwRuntimeEnvironment.isClientMode())
//				addJsonSupport(scriptBuf, request, pageMeta);
			
			// 添加ClientEnvironment
			
			scriptBuf.append("pageUI.$beforeInitData();\n")
					 .append("pageUI.$beforeActive();\n");
			
			
			scriptBuf.append("layoutInitFunc();\n");
			
			scriptBuf.append("window.renderDone = true;\n");
			
			scriptBuf.append("}\n");
			
			addDynamicScript(scriptBuf);
			
			
			if(Logger.isInfoEnabled()){
				long endTime = System.currentTimeMillis();
				Logger.info("初始化PageMode:\"" + modelId + "\"所用时间:" + (endTime - startTime) + "ms");
			}
			
		} 
		catch (Exception e) {
			//释放掉错误的脚本
//			if(bodyScriptMap != null)
//				bodyScriptMap.remove(modelId);
			LfwLogger.error(e.getMessage(), e);
			if(model != null)
				model.destroy();
			if(e instanceof LfwRuntimeException)
				throw (LfwRuntimeException)e;
			throw new LfwRuntimeException(e);
		}
	}
	

	private void addDynamicScript(StringBuffer scriptBuf) throws IOException {
//		if (!LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_PRODUCTION))
//			System.out.println(scriptBuf.toString());
		String script = "<script>\n" + scriptBuf.toString() + "</script>";
		getJspContext().getOut().write(script);
	}


//	private String renderConnector() {
//		StringBuffer buf = new StringBuffer();
//		PageMeta pm = getPageModel().getPageMeta();
//		Iterator<Connector> it = pm.getConnectorMap().values().iterator();
//		while(it.hasNext()){
//			Connector conn = it.next();
//			buf.append("var ")
//			   .append(conn.getId())
//			   .append(" = ")
//			   .append("new Connector('")
//			   .append(conn.getId())
//			   .append("','")
//			   .append(conn.getSource())
//			   .append("','")
//			   .append(conn.getTarget())
//			   .append("','")
//			   .append(conn.getPlugoutId())
//			   .append("','")
//			   .append(conn.getPluginId())
//			   .append("');\n");
//			buf.append("pageUI.addConnector(")
//			   .append(conn.getId())
//			   .append(");\n");
//		}
//		return buf.toString();
//	}

//	private String createWidget() {
//		PageMeta pm = getPageModel().getPageMeta();
//		LfwWidget[] widgets = pm.getWidgetMap().values().toArray(new LfwWidget[0]);
//		StringBuffer buf = new StringBuffer();
//		if(widgets != null && widgets.length > 0)
//		{
//			for (int i = 0; i < widgets.length; i++) {
//				buf.append("pageUI.addWidget(new Widget('" + widgets[i].getId() + "'));\n");
//			}
//		}
//		return buf.toString();
//	}

	private String createPageUI() {
		StringBuffer buf = new StringBuffer();
		PageMeta pm = getPageModel().getPageMeta();
		String caption = pm.getCaption();
		if(caption == null)
			caption = "";
		buf.append("window.pageUI = new PageUI('")
		   .append(pm.getCaption())
		   .append("');\n");
//		if(pm.getMasterWidget() != null){
//			buf.append("pageUI.setMasterWidget('")
//			   .append(pm.getMasterWidget())
//			   .append("');\n");
//		}
		
		String eventStr = addEventSupport(pm, null, "pageUI", null);
		buf.append(eventStr);
		return buf.toString();
	}

	/**
	 * 客户端运行信息环境
	 * @param scriptBuf
	 */
//	private void addClientEnviromentToClientSession(PageModel model) {
//		//String pkCorp = (LfwRuntimeEnvironment.getPkCorp() == null ? "" : LfwRuntimeEnvironment.getPkCorp());
//		String accountCode = LfwRuntimeEnvironment.getAccountCode() == null ? "" : LfwRuntimeEnvironment.getAccountCode();
//		String pkUser = LfwRuntimeEnvironment.getPkUser() == null ? "" : LfwRuntimeEnvironment.getPkUser();
//		String userCode = LfwRuntimeEnvironment.getUserCode() == null ? "" : LfwRuntimeEnvironment.getUserCode();
//		String connectServerCycle = LfwRuntimeEnvironment.getConnectServerCycle();
//		String openNodeMode = LfwRuntimeEnvironment.getOpenNodeMode();
//		String noticeRefreshCycle =LfwRuntimeEnvironment.getNoticeRefreshCycle();
//		String jobRefreshCycle = LfwRuntimeEnvironment.getJobRefreshCycle();
//		String theme = LfwRuntimeEnvironment.getThemeId();
//		
//		ClientSession session = model.getClientSession();
//		//session.setAttribute("pkCorp", pkCorp);
//		session.setAttribute("accountCode", accountCode);
//		session.setAttribute("pkUser", pkUser);
//		session.setAttribute("userCode", userCode);
//		
//		session.setAttribute("connectServerCycle", connectServerCycle);
//		session.setAttribute("openNodeMode", openNodeMode);
//		session.setAttribute("noticeRefreshCycle", noticeRefreshCycle);
//		session.setAttribute("jobRefreshCycle", jobRefreshCycle);
//		session.setAttribute("theme", theme);
//		
//	}
//
//	public String getModelId() {
//		return modelId;
//	}
//	
//	public void setModelId(String modelId) {
//		this.modelId = modelId;
//	}
//	
//	public String getClassName() {
//		return className;
//	}
//	
//	public void setClassName(String className) {
//		this.className = className;
//	}
	
	private void renderClientSessionAttribute(PageModel model, StringBuffer scriptBuf) {
		try {
			ClientSession cs = model.getClientSession();
			if(cs != null) {
				Map<String, Serializable> map = cs.getAttributeMap();
				if(map != null && map.size() > 0) {
					LfwJsonSerializer jsonSerialzer = LfwJsonSerializer.getInstance();
					String jsonStr = jsonSerialzer.toJSON(map);
					scriptBuf.append(CS_PRE)
					         .append("clientSession = ")
					         .append(jsonStr)
					         .append(";\n");
				}
				Map<String, Serializable> stickMap = cs.getStickAttributeMap();
				if(stickMap != null && stickMap.size() > 0){
					Iterator<Entry<String, Serializable>> it = stickMap.entrySet().iterator();
					scriptBuf.append(CS_PRE)
							 .append("clientStickKeys = '");
					while(it.hasNext()){
						Entry<String, Serializable> entry = it.next();
						if(entry.getValue() == null){
							Logger.warn("client session value is null, key is:" + entry.getKey());
							continue;
						}
						scriptBuf.append(entry.getKey())
								 .append("=")
								 .append(JsURLEncoder.encode(entry.getValue().toString(), "UTF-8"));
						if(it.hasNext())
							scriptBuf.append("&");
					}
					scriptBuf.append("';\n");
				}
			}
		} 
		catch (MarshallException e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "PageModelTag-000000")/*初始化客户端记录对象出现问题:*/ + e.getMessage());
		}
	}
//	
//	public void addJsonSupport(StringBuffer scriptBuf, HttpServletRequest request, PageMeta pageMeta) {
//		//boolean isHeadAdded = false;
//		//将服务按照注册名暴露为客户端服务，客户端只需调用 getService("服务名")即可获得对应服务
//		Map<String, Object> serviceMap = new HashMap<String, Object>();
//		if(pageMeta.getServiceMap().size() > 0){
//			Iterator<Entry<String, String>> it = pageMeta.getServiceMap().entrySet().iterator();
//			while(it.hasNext()){
//				Entry<String, String> entry = it.next();
//				serviceMap.put(entry.getKey(), LfwClassUtil.newInstance(entry.getValue()));
//			}
//		}
//		
//		String clazz = pageMeta.getEditFormularClazz();
//		//注册公式服务
//		if(clazz != null && !clazz.equals(""))
//			serviceMap.put("formular_service", LfwClassUtil.newInstance(clazz));
//		
//		if(serviceMap.size() > 0){
//			addJsonHead(scriptBuf);
//			JsonHelper.registerService(request, modelId, serviceMap);
//		}
//	}
//	
//	private void addJsonHead(StringBuffer scriptBuf) {
//		scriptBuf.append("$jsonrpc = new JSONRpcClient(window.corePath + \"/json?pageId=")
//			.append(modelId)
//			.append("\");\n");
//		scriptBuf.append("log(\"rpc service was successfully initialized\");\n");
//	}
	
	
	
//	private String addLoadingPage() {
//		StringBuffer buf = new StringBuffer();
//		buf.append("<table id=\"$lfw_loadingPage\" width=\"100%\" height=\"100%\" border=\"0\" style=\"background:#FFFFFF;position:absolute;z-index:10000\">\n<tr>\n<td align=\"center\"><img src=\"/lfw/frame/themes/ncclassic/images/common/loading.gif\"/></td></tr></table>");
//		return buf.toString();
//	}
//
//	protected DatasetRelations getDatasetRelations()
//	{
//		return getPageModel().getDsRelations();
//	}

//
//	@Override
//	protected Map<String, String[]> getEventParamMap() {
//		return eventParamMap;
//	}


	public boolean isShowLoadingPage() {
		return showLoadingPage;
	}


	public void setShowLoadingPage(boolean showLoadingPage) {
		this.showLoadingPage = showLoadingPage;
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_PAGEMETA;
	}


	public boolean isSimple() {
		return simple;
	}


	public void setSimple(boolean simple) {
		this.simple = simple;
	}

}
