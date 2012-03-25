package nc.uap.lfw.core.tags;


import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.ClientSession;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.model.util.LfwMaskerUtil;
import nc.uap.lfw.core.page.IUIMeta;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.serializer.impl.LfwJsonSerializer;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.itf.IDynamicAttributes;
import nc.uap.lfw.ra.itf.IUIRender;
import nc.uap.lfw.ra.render.UIRender;
import nc.uap.lfw.util.JsURLEncoder;
import nc.uap.lfw.util.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;

import org.json.MarshallException;

/**
 * PageModel��ӦTag.��Tag����ʼ��PageModel,д��ҳ���ҪƬ��.����PageModel�����PageContext�Թ�
 * �����ؼ����á�
 * ע�⣺��������Lfw��ӦTag��������ڴ�Tag�ڣ���ȡ�ö�Ӧ��������
 * 
 * @author dengjt
 * 2007-1-25 
 */ 
public class PageBodyTag extends WebElementTag {
//	private String modelId = null; 
//	private String className = null;  
	private boolean showLoadingPage = false;
	private boolean simple = false;
	protected static final String DS_SCRIPT = "dsScript";
	protected static final String ALL_SCRIPT = "allScript";
	
	@SuppressWarnings("unchecked")
	protected void doRender() throws JspException, IOException {
		PageModel model = (PageModel) getJspContext().getAttribute("pageModel");
		try {
			long startTime = 0;
			if(Logger.isInfoEnabled())
				startTime = System.currentTimeMillis();
			
//			if(showLoadingPage){
//				getJspContext().getOut().write(addLoadingPage());
//				//getJspContext().getOut().flush();
//			}
			
			
			//��ô�ҳ���PageMeta
			PageMeta pageMeta = model.getPageMeta();
			//����Ǽ�ģʽ�������нű���Ⱦ
			if(simple)
				return;
			
			
			//���û�session������һ������buf��map����map����pageId����buf
//			synchronized(PageModelTag.class) {
//				if((bodyScriptMap = (ConcurrentMap<String, StringBuffer>) ((PageContext)this.getJspContext()).getSession().getAttribute("bodyScriptMap")) == null){
//					bodyScriptMap = new ConcurrentHashMap<String, StringBuffer>();
//					((PageContext)getJspContext()).getSession().setAttribute(BODY_SCRIPT_MAP, bodyScriptMap);
//				}
//			}
			//��Buf�е����ݣ����ܹ������γɽű��ļ�
			StringBuffer scriptBuf = new StringBuffer(4096);
			getPageContext().setAttribute(ALL_SCRIPT, scriptBuf);
			//����buf����ҳ����������
//			bodyScriptMap.put(model.getPageModelId(), scriptBuf);
			
			//�ؼ���ds���߼�ͳһ���ڴ˴������պϲ���scriptBuf�У�֮������ô��������Ϊtree�Ȼ��ڵ��ʱ����Ҫ������width��Ϣ����ֻ��
			//���пؼ�����Ⱦ��ɣ�������Ϣ������������������������Խ��������ݷ��������С�ע�⣺���л����Ŀؼ�������TabItem��CardLayout��
			//��������Ҫ�Լ��������ֻ�������
			StringBuffer dsScriptBuf = new StringBuffer();
//			getJspContext().setAttribute(DS_SCRIPT, dsScriptBuf);
			
			
			//ҳ���ʼ������,����js�߼���������
			scriptBuf.append("function pageBodyScript(){\n");
			
			//����masker��Ϣ
			LfwMaskerUtil.makeMaskerScript(scriptBuf);
			
			PageContext pageContext = (PageContext) getJspContext();
			Iterator it = pageContext.getRequest().getParameterMap().entrySet().iterator();
			
			scriptBuf.append(createPageUI());
			//�������������ֲ��ȫ���������Map
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
			
			prepareRender(model.getUIMeta(), model.getPageMeta(), dsScriptBuf);
			if(getJspBody() != null)
				getJspBody().invoke(getJspContext().getOut());

			//��dsScript�����ýű�����ҳ��ű���
			scriptBuf.append(dsScriptBuf.toString());
			
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
//			
//			String busiState = pageMeta.getBusiState();
//			if(busiState != null && !"".equals(busiState))
//				scriptBuf.append("pageUI.setBusinessState(" + busiState + ");\n");
//			scriptBuf.append("pageUI.updateButtons();\n");

			scriptBuf.append("if(typeof(externalInit) != 'undefined'){\n externalInit();\n}\n");
			scriptBuf.append("pageUI.$afterPageInit();\n");
			
			// ���ClientEnvironment
			
			scriptBuf.append("pageUI.$beforeInitData();\n")
					 .append("pageUI.$beforeActive();\n");
			
			
			scriptBuf.append("layoutInitFunc();\n");
			
			scriptBuf.append("window.renderDone = true;\n");
			scriptBuf.append("if(window.editMode){document.body.className='unselectable'}\n");
			scriptBuf.append("\n");
			scriptBuf.append("}\n");
			
			addDynamicScript(scriptBuf);
			
			
			if(Logger.isInfoEnabled()){
				long endTime = System.currentTimeMillis();
				Logger.info("��ʼ��PageMode����ʱ��:" + (endTime - startTime) + "ms");
			}
		} 
		catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
			if(model != null)
				model.destroy();
			if(e instanceof LfwRuntimeException)
				throw (LfwRuntimeException)e;
			throw new LfwRuntimeException(e);
		}
	}
	

	private void prepareRender(IUIMeta meta, PageMeta pagemeta, StringBuffer scriptBuf) throws IOException {
		String editModeStr = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(WebConstant.EDIT_MODE_KEY);
		boolean editMode = editModeStr == null ? false : editModeStr.equals(WebConstant.MODE_PERSONALIZATION);
		
		JspWriter out = getPageContext().getOut();
//		IUIRenderGroup group = RenderKit.getUIRenderGroup(RenderKit.RENDER_PC);
		IUIRender render = ControlFramework.getInstance().getUIRender((UIMeta) meta, (UIElement) meta, pagemeta, null, DriverPhase.PC);
		if(render != null){
			((IDynamicAttributes)render).setContextAttribute(UIRender.DS_SCRIPT, scriptBuf);
//			this.initWebContext((IDynamicAttributes)render, getJspContext());
			String html = render.createRenderHtml();
			out.write(html);
			
//			StringWriter script = new StringWriter();
			String script = render.createRenderScript();
			addToBodyScript(script);
		}
	}


	private void addDynamicScript(StringBuffer scriptBuf) throws IOException {
		if (!LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_PRODUCTION))
			System.out.println(scriptBuf.toString());
		String script = "<script>\n" + scriptBuf.toString() + "</script>";
		getJspContext().getOut().write(script);
	}

	private String createPageUI() {
		StringBuffer buf = new StringBuffer();
		PageMeta pm = getPageModel().getPageMeta();
		String caption = pm.getCaption();
		if(caption == null)
			caption = "";
		buf.append("window.pageUI = new PageUI('")
		   .append(pm.getId())
		   .append("','")
		   .append(pm.getCaption())
		   .append("');\n");

		buf.append("getApplication().addPageUI('")
		   .append(pm.getId())
		   .append("', window.pageUI);\n");
		
		String eventStr = addEventSupport(pm, null, "pageUI", null);
		buf.append(eventStr);
		return buf.toString();
	}

	
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
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "PageModelTag-000000")/*��ʼ���ͻ��˼�¼�����������:*/ + e.getMessage());
		}
	}
//	
//	public void addJsonSupport(StringBuffer scriptBuf, HttpServletRequest request, PageMeta pageMeta) {
//		//boolean isHeadAdded = false;
//		//��������ע������¶Ϊ�ͻ��˷��񣬿ͻ���ֻ����� getService("������")���ɻ�ö�Ӧ����
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
//		//ע�ṫʽ����
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
