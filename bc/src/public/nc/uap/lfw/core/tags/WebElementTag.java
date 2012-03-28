package nc.uap.lfw.core.tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.ToolBarItem;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.WidgetElement;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.LfwParameter;
import nc.uap.lfw.core.data.Parameter;
import nc.uap.lfw.core.event.conf.DatasetListener;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.EventHandlerConf;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.FormRule;
import nc.uap.lfw.core.event.conf.GridRule;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.TreeRule;
import nc.uap.lfw.core.event.conf.WidgetRule;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.page.ViewMenus;
import nc.uap.lfw.util.LanguageUtil;
import nc.uap.lfw.util.LfwClassUtil;

/**
 * ҳ��Ԫ����ȾTag���ࡣ�˻����ṩ�����пؼ��ĳ��÷���������addToBodyScript,addEventSupport��
 * @author dengjt
 *
 */
public abstract class WebElementTag extends SimpleTagWithAttribute {
	private static final String DIV_INDEX = "$DIV_INDEX";
	//ռλDIVǰ׺����ֹ��DIV id�Ϳؼ�ʵ��DIV��ͻ
	public static final String DIV_PRE = "$d_";
	public static final String COMP_PRE = "$c_";
	public static final String TAB_PRE = "$tab_";
	public static final String DS_PRE = "$ds_";
	public static final String DS_RELATION_PRE = "$dsr_";
	public static final String COMMAND_PRE = "$cm_";
	public static final String COMBO_PRE = "$cb_";
	public static final String SERVICE_PRE = "$s_";
	public static final String CS_PRE = "$cs_";
	public static final String RF_RELATION_PRE = "$rfr_";
	public static final String RF_PRE = "$rf_";
	public static final String TL_PRE = "$tl_";
	public static final String SLOT_PRE = "$sl_";
	public static final String WIDGET_PRE = "$wd_";
	public static final String LISTENER_PRE = "$ls_";
	
	protected static final String BODY_SCRIPT_MAP = "bodyScriptMap";
	// �������widget id
	private String widget = null;
	// ���id
	private String id = null;
	// ���ǰ̨id
	private String varShowId = null;
	private String divShowId = null;
	private LfwWidget currWidget = null;
	
	/**
	 * Tag��������
	 */
	public void doTag() throws JspException, IOException {
		try{
			doRender();
		}
		catch(Exception e){
			if(e instanceof LfwRuntimeException)
				throw (LfwRuntimeException)e;
			else if(e instanceof JspException){
				JspException jspe = (JspException) e;
				if(jspe.getRootCause() != null && jspe.getRootCause() instanceof LfwRuntimeException)
					throw (LfwRuntimeException)jspe.getRootCause();
			}
			else{
				LfwLogger.error(e.getMessage(), e);
				throw new LfwRuntimeException("��Ⱦ���ʱ���ִ���," + e.getMessage(), e);
			}
		}
	}

	/**
	 * ��������ɽű���ӵ���ű�����buf�С�
	 */
	protected void addToBodyScript(String script) {
//		String pageId = LfwRuntimeEnvironment.getWebContext().getPageId();
		if (script != null && !script.equals("")) {
			//�ӻỰmap�л��buf��������ж��ҳ��ͬʱ������������Ҫ����pageId
//			StringBuffer scriptBuf = (StringBuffer) ((ConcurrentMap<String, StringBuffer>)((PageContext)this.getJspContext()).getSession()
//					.getAttribute(BODY_SCRIPT_MAP)).get(pageId);
			StringBuffer scriptBuf = (StringBuffer) getPageContext().getAttribute(PageModelTag.ALL_SCRIPT);
			scriptBuf.append("\n");
			scriptBuf.append(script);
		}
	}
	
	/**
	 * body������Ⱦ.��������д
	 * @throws JspException
	 * @throws IOException
	 */
	protected abstract void doRender()  throws JspException, IOException;
	
	protected String getVarShowId(){
		if(varShowId == null){
			if(getCurrWidget() == null)
				varShowId = COMP_PRE + getId();
			else	
				varShowId = COMP_PRE + getCurrWidget().getId() + "_" + getId();
		}
		return varShowId;
	}

	protected String getVarShowId(String id) {
		String varShowId = "";
		if(getCurrWidget() == null)
			varShowId = COMP_PRE + id;
		else	
			varShowId = COMP_PRE + getCurrWidget().getId() + "_" + id;
		return varShowId;
	}
	
	protected String getDivShowId(){
		if(divShowId == null){
			Integer index = (Integer) getJspContext().getAttribute(DIV_INDEX);
			if(index == null){
				index = Integer.valueOf(0);
			}
			divShowId = DIV_PRE + getId() + index;
			getJspContext().setAttribute(DIV_INDEX, (index + 1));
//			if(getCurrWidget() == null)
//			else	
//				divShowId = DIV_PRE + getCurrWidget().getId() + "_" + getId();
		}
		return divShowId;
	}
	
	protected LfwWidget getCurrWidget() {
		if(currWidget != null)
			return currWidget;
		if(getWidget() != null){
			currWidget = getPageModel().getPageMeta().getWidget(getWidget());
		}
		else
		{
			LfwWidget[] widgets = getPageModel().getPageMeta().getWidgets();
			int doubleId = 0;
			LfwWidget ownerWidget = null;
			if(widgets != null && widgets.length > 0)
			{
				for (int i = 0; i < widgets.length; i++) 
				{
					ViewComponents views = widgets[i].getViewComponents();
					ViewMenus contextMenus = widgets[i].getViewMenus();
					
					if(views.getComponent(getId()) != null){
						doubleId ++;
					}
					
					if(contextMenus.getContextMenu(getId()) != null)
						doubleId ++;

					if(contextMenus.getMenuBar(getId()) != null)
						doubleId ++;
					
					if(doubleId >= 2)
						throw new LfwRuntimeException("���id���������id�ظ�,id=" + getId());
					else if(doubleId == 1 && ownerWidget == null)
						ownerWidget = widgets[i];
				}
				
//				ViewMenus menus = getPageModel().getPageMeta().getViewMenus();
//				if(menus.getMenuBar(this.id) != null){
//					doubleId ++;
//				}
				
				if(doubleId == 0)
				{
					//throw new LfwRuntimeException("���idΪ" + getId() + "������������κ�widget,���ô���!");
					return null;
				}
			}
			currWidget = ownerWidget;
		}
		return currWidget;
	}
	
	/**
	 * ����ӦԪ������js�¼�.�¼��������ݵ�ǰTag���¼�ӳ��map���
	 * @param element
	 * @param showId �ؼ��ĵ�ǰ��ȾID
	 */
	protected String addEventSupport(WebElement element, String widgetId, String showId, String parentId)
	{
		StringBuffer buf = new StringBuffer("");

		// ��ÿؼ���¶���¼�map
		Map<String, JsListenerConf> listenerMap = element.getListenerMap();
		List<JsListenerConf> listeners = new ArrayList<JsListenerConf>();
		listeners.addAll(listenerMap.values());
		Collection<JsListenerConf> eventConfListeners = Controller2Event(element.getEventConfs(), element);
		if (eventConfListeners != null)
			listeners.addAll(eventConfListeners);

		if (listeners == null || listeners.isEmpty())
			return "";
		for (JsListenerConf listener : listeners) {
			String listenerId = null;
			if (widgetId != null)
				listenerId = LISTENER_PRE + widgetId + "_" + listener.getId();
			else
				listenerId = LISTENER_PRE + listener.getId();
			String listenerStr = generateListener(listener, listenerId, widgetId, element, parentId);
			buf.append(listenerStr);
			buf.append(showId).append(".addListener(").append(listenerId).append(");\n");
		}

		return buf.toString();
	}
	/**
	 * ��Controllerת��Ϊjs����������
	 * 
	 * @param events
	 * @return
	 */
	private Collection<JsListenerConf> Controller2Event(EventConf[] events, WebElement element) {
		if (events != null && events.length > 0) {
			Map<String, JsListenerConf> jsListeners = new HashMap<String, JsListenerConf>();
			for (EventConf event : events) {
				if (event.getJsEventClaszz() == null)
					continue;
				JsListenerConf jslc = jsListeners.get(event.getJsEventClaszz());
				if (jslc == null)
					jslc = (JsListenerConf) LfwClassUtil.newInstance(event.getJsEventClaszz());
				if (jslc == null)
					continue;

				String ctrl = event.getControllerClazz();
				String parentCtrl = null;
				String el = AppLifeCycleContext.EVENT_LEVEL_APP;
				EventHandlerConf ent = jslc.getEventTemplate(event.getName());
				if (element instanceof WidgetElement) {
					WidgetElement we = (WidgetElement) element;
					parentCtrl = getPageModel().getPageMeta().getWidget(we.getWidget().getId()).getControllerClazz();
					el = AppLifeCycleContext.EVENT_LEVEL_VIEW ;

				}
				if (element instanceof PageMeta) {
					PageMeta we = (PageMeta) element;
					parentCtrl = we.getControllerClazz();
					el = AppLifeCycleContext.EVENT_LEVEL_WIN ;
				}
				 
				
				/**
				 * ���ȼ� �Զ���ControllerClazz > widget����ControllerClazz
				 */
				if (ctrl == null || ctrl.isEmpty()) {
					ctrl = parentCtrl;
				}
				
				ent.setSubmitRule(event.getSubmitRule());
				ent.setOnserver(event.isOnserver());
				ent.setScript(event.getScript());
				ent.setName(event.getName());
				ent.setExtendAttribute(EventHandlerConf.SUBMIT_PRE + AppLifeCycleContext.METHOD_NAME, event.getMethodName());
				ent.setExtendAttribute(EventHandlerConf.SUBMIT_PRE + LfwPageContext.SOURCE_ID, element.getId());
				ent.setExtendAttribute(EventHandlerConf.SUBMIT_PRE + "clc", ctrl);
				ent.setExtendAttribute(EventHandlerConf.SUBMIT_PRE + AppLifeCycleContext.EVENT_LEVEL, el);
				
				jslc.setId(event.getControllerClazz().replace(".", "_"));

				jslc.setServerClazz(ctrl);
				jslc.addEventHandler(ent);
				jsListeners.put(event.getJsEventClaszz(), jslc);
			}
			return jsListeners.values();
		}

		return null;
	}
	protected String generateListener(JsListenerConf listener, String listenerShowId, String widgetId, WebElement ele, String parentId) {
		StringBuffer buf = new StringBuffer();
		buf.append("var ")
		   .append(listenerShowId)
		   .append(" = new ")
		   .append(listener.getJsClazz())
		   .append("();\n");
		if(ele instanceof MenuItem){
			buf.append(getVarShowId());
//			StringBuffer newBuf = new StringBuffer();
			MenuItem currEle = (MenuItem) ele;
			currEle = currEle.getParentItem();
			StringBuffer bf = new StringBuffer();
			while(currEle != null){
				String str = "";
				str += ".getMenu('" + currEle.getId() + "')";
				currEle = currEle.getParentItem();
				bf.insert(0, str);
			}
			buf.append(bf.toString());
			buf.append(".getMenu('")
			   .append(ele.getId())
			   .append("')");
		}
		else if(ele instanceof ToolBarItem){
			//����д��Ϊ�˿��ظ�ʹ��
			buf.append("pageUI.getWidget('")
			   .append(widgetId)
			   .append("').getComponent('")
			   .append(getId())
			   .append("')");
			buf.append(".getButton('")
			   .append(ele.getId())
			   .append("')");
		}
		else if(ele instanceof PageMeta){
			buf.append("pageUI");
		}
		else if(ele instanceof Dataset){
			String dsId = getDatasetVarShowId(ele.getId(), widgetId);
			buf.append(dsId);
		}
		//������widget������£��������ʵ��Dialog
		else if(ele instanceof LfwWidget){
			LfwWidget widget = (LfwWidget) ele;
			if(widget.isDialog())
				buf.append("window.pageUI.getDialog('").append(widgetId).append("')");
		}
		else
			buf.append(getVarShowId());
		
		buf.append(".$TEMP_")
		   .append(listener.getId())
		   .append(" = ")
		   .append(listenerShowId)
		   .append(";\n");
		
		if(!(ele instanceof PageMeta)){
			buf.append(listenerShowId)
			   .append(".")
			   .append(LfwPageContext.SOURCE_ID)
			   .append(" = '")
			   .append(ele.getId())
			   .append("';\n");
		}
		buf.append(listenerShowId)
		   .append(".")
		   .append(LfwPageContext.LISTENER_ID)
		   .append(" = '")
		   .append(listener.getId())
		   .append("';\n");
		
		if(widgetId != null){
			buf.append(listenerShowId)
			   .append(".")
			   .append(LfwPageContext.WIDGET_ID)
			   .append(" = '")
			   .append(widgetId)
			   .append("';\n");
		}
		
		buf.append(listenerShowId)
		   .append(".")
		   .append(LfwPageContext.SOURCE_TYPE)
		   .append(" = '")
		   .append(getSourceType(ele))
		   .append("';\n");
		
		if(parentId != null){
			buf.append(listenerShowId)
			   .append(".")
			   .append(LfwPageContext.PARENT_SOURCE_ID)
			   .append(" = '")
			   .append(parentId)
			   .append("';\n");
		}
		
		Map<String, EventHandlerConf> eventMap = listener.getEventHandlerMap();
    	Iterator<EventHandlerConf> eit = eventMap.values().iterator();
    	while(eit.hasNext())
    	{
    		EventHandlerConf jsEvent = eit.next();
    		
    		buf.append(listenerShowId)
    		   .append(".")
    		   .append(jsEvent.getName())
    		   .append(" = function(");
    		StringBuffer params = new StringBuffer();
    		int pSize = jsEvent.getParamList().size();
    		if(jsEvent.getParamList() != null && pSize > 0)
    		{
    			for(int i = 0; i < pSize; i++)
    			{
    				params.append(jsEvent.getParamList().get(i).getName());
    				if(i != pSize - 1)
    					params.append(",");
    			}
    		}
    		buf.append(params.toString() + "){\n");
    		
    		if (jsEvent.getExtendParam(EventHandlerConf.PARAM_DATASET_FIELD_ID) != null) {
				if ("onBeforeDataChange".equals(jsEvent.getName())) { // Dataset��onBeforeDataChange
					generateBeforeDataChangeEventHeadScript(buf, jsEvent, ele, widgetId);
				} else if ("onAfterDataChange".equals(jsEvent.getName())) { // Dataset��onAfterDataChange�¼�
					generateAfterDataChangeEventHeadScript(buf, jsEvent, ele, widgetId);
				}
			}
			if(listener.getServerClazz() != null && jsEvent.isOnserver()){
				buf.append(generateServerProxy(widgetId, listenerShowId, ele, listener, jsEvent, parentId));
			} else if (jsEvent.getScript() != null && !jsEvent.getScript().equals("")) {
				buf.append(jsEvent.getScript());
			}
    		
    		buf.append("\n};\n");
    		
    		generateSubmitRule(listener, listenerShowId, buf, jsEvent, widgetId);
    	}
    	return buf.toString();
	}
	
	/**
	 * ����Dataset��onAfterDataChange�¼���JS�ж����
	 * @param buf
	 * @param jsEvent
	 * @param ele 
	 * @param widgetId 
	 */
	private void generateAfterDataChangeEventHeadScript(StringBuffer buf, EventHandlerConf jsEvent, WebElement ele, String widgetId) {
		LfwParameter fieldParam = jsEvent.getExtendParam(EventHandlerConf.PARAM_DATASET_FIELD_ID);
		String fields = fieldParam.getDesc();
//		buf.append(getVarShowId(ele.getId()))
		buf.append(getDatasetVarShowId(ele.getId(), widgetId))
		   .append(".afterDataChangeAcceptFields = \"")
		   .append(fields)
		   .append("\".split(\",\");\n");
	}
	
	/**
	 * ����Dataset��onBeforeDataChange�¼���JS�ж����
	 * @param buf
	 * @param jsEvent
	 * @param ele 
	 * @param widgetId 
	 */
	private void generateBeforeDataChangeEventHeadScript(StringBuffer buf, EventHandlerConf jsEvent, WebElement ele, String widgetId) {
		LfwParameter fieldParam = jsEvent.getExtendParam(EventHandlerConf.PARAM_DATASET_FIELD_ID);
		String fields = fieldParam.getDesc();
//		buf.append(getVarShowId(ele.getId()))
		buf.append(getDatasetVarShowId(ele.getId(), widgetId))
		   .append(".beforeDataChangeAcceptFields = \"")
		   .append(fields)
		   .append("\".split(\",\");\n");
	}
	
	protected void generateSubmitRule(JsListenerConf listener,
			String listenerShowId, StringBuffer buf, EventHandlerConf jsEvent, String widgetId) {
		// �ύ����
		EventSubmitRule submitRule = jsEvent.getSubmitRule();
		if(widgetId != null){
			if(submitRule == null){
				submitRule = new EventSubmitRule();
			}
			if(submitRule.getWidgetRule(widgetId) == null){
				WidgetRule wr = new WidgetRule();
				wr.setId(widgetId);
				submitRule.addWidgetRule(wr);
			}
		}
		if(submitRule != null)
		{
			String ruleId = "sr_" + jsEvent.getName() + "_" + listener.getId();
			buf.append("var ").append(ruleId).append(" = new SubmitRule();\n");
			if(submitRule.getParamMap() != null && submitRule.getParamMap().size() > 0){
				Iterator<Entry<String, Parameter>> it = submitRule.getParamMap().entrySet().iterator();
				while(it.hasNext()){
					Entry<String, Parameter> entry = it.next();
					buf.append(ruleId)
					   .append(".addParam('")
					   .append(entry.getKey())
					   .append("', '")
					   .append(entry.getValue().getValue())
					   .append("');\n");
				}
			}
			if(submitRule.isCardSubmit()){
				buf.append(ruleId).append(".setCardSubmit(true);\n");
			}
			if(submitRule.isTabSubmit()){
				buf.append(ruleId).append(".setTabSubmit(true);\n");
			}
			if(submitRule.isPanelSubmit()){
				buf.append(ruleId).append(".setPanelSubmit(true);\n");
			}
			
			String jsScript = createJsSubmitRule(submitRule, ruleId);
			buf.append(jsScript);
			
			EventSubmitRule pSubmitRule = submitRule.getParentSubmitRule();
			if(pSubmitRule != null)
			{
				String pRuleId = ruleId + "_parent";
				buf.append("var " + pRuleId + " = new SubmitRule();\n");
				String pJsScript = createJsSubmitRule(pSubmitRule, pRuleId);
				buf.append(pJsScript);
				buf.append(ruleId + ".setParentSubmitRule(" + pRuleId + ");\n");
			}
			
			buf.append(listenerShowId)
			.append(".")
			.append(jsEvent.getName())
			.append(".submitRule = (")
			.append(ruleId)
			.append(");\n");
		}
		
		
	}

	protected String generateServerProxy(String widgetId, String listenerShowId, WebElement ele, JsListenerConf listener, EventHandlerConf jsEvent, String parentId) {
		StringBuffer buf = new StringBuffer();
		String eventName = jsEvent.getName();
//		if(jsEvent.getParamList() != null && jsEvent.getParamList().size() > 0){
//			eventName = jsEvent.getParamList().get(0).getName();
//		}
		buf.append("if(window.editMode) return;");
		buf.append("var proxy = new ServerProxy(this.$TEMP_" + listener.getId() + ",'" + eventName + "'," + jsEvent.isAsync() + ");\n");
		buf.append("if(typeof beforeCallServer != 'undefined')\n")
		   .append("beforeCallServer(proxy, '" + listener.getId() + "', '" + eventName + "','" + ele.getId() + "');\n");
		addProxyParam(buf, eventName);
//		if(!(ele instanceof PageMeta)){
//			buf.append("proxy.addParam('")
//			   .append(LfwPageContext.SOURCE_ID)
//			   .append("', '")
//			   .append(ele.getId())
//			   .append("');\n");
//		}
//		buf.append("proxy.addParam('")
//		   .append(LfwPageContext.LISTENER_ID)
//		   .append("','")
//		   .append(listener.getId())
//		   .append("');\n");
//		
//		if(widgetId != null){
//			buf.append("proxy.addParam('")
//			   .append(LfwPageContext.WIDGET_ID)
//			   .append("','")
//			   .append(widgetId)
//			   .append("');\n");
//		}
//		
//		buf.append("proxy.addParam('")
//		   .append(LfwPageContext.EVENT_NAME)
//		   .append("','")
//		   .append(jsEvent.getName())
//		   .append("');\n");
//		
//		buf.append("proxy.addParam('")
//		   .append(LfwPageContext.SOURCE_TYPE)
//		   .append("','")
//		   .append(getSourceType(ele))
//		   .append("');\n");
//		
//		if(parentId != null){
//			buf.append("proxy.addParam('")
//			   .append(LfwPageContext.PARENT_SOURCE_ID)
//			   .append("', '")
//			   .append(parentId)
//			   .append("');\n");
//		}
		
		if(jsEvent.getExtendMap().size() > 0){
			String[] keys = jsEvent.getExtendMap().keySet().toArray(new String[0]);
			for (int i = 0; i < keys.length; i++) {
				if(keys[i].startsWith(EventHandlerConf.SUBMIT_PRE)){
					buf.append("proxy.addParam('")
					   .append(keys[i].substring(EventHandlerConf.SUBMIT_PRE.length()))
					   .append("', '");
					Object value = jsEvent.getExtendAttributeValue(keys[i]);
					if(value == null)
						value = "";
					buf.append(value.toString())
					   .append("');\n");
				}
			}
		}
		
		String extendStr = getExtendProxyStr(ele, listener, jsEvent);
		if(extendStr != null)
			buf.append(extendStr);
		
		// ��ʾ������ʾ��
		buf.append("showDefaultLoadingBar();\n");
		
		buf.append("return proxy.execute();\n");
		return buf.toString();
	}

	/**
	 * ����ǰ̨Event���ύ��������̨
	 * @param buf JS�����ַ���
	 * @param eventName �¼�����
	 */
	protected void addProxyParam(StringBuffer buf, String eventName) {
		if (DatasetListener.ON_AFTER_DATA_CHANGE.equals(eventName)) {
			buf.append("if(dataChangeEvent.isColSingle == null || dataChangeEvent.isColSingle == false){\n");
			buf.append("proxy.addParam('cellRowIndex', dataChangeEvent.cellRowIndex);\n");
			buf.append("proxy.addParam('cellColIndex', dataChangeEvent.cellColIndex);\n");
			buf.append("proxy.addParam('newValue', dataChangeEvent.currentValue);\n");
			buf.append("proxy.addParam('oldValue', dataChangeEvent.oldValue);\n");
			buf.append("proxy.addParam('isColSingle', 'false');\n");
			buf.append("}else{\n");
			buf.append("proxy.addParam('cellRowIndices', dataChangeEvent.cellRowIndices);\n");
			buf.append("proxy.addParam('cellColIndex', dataChangeEvent.cellColIndex);\n");
			buf.append("proxy.addParam('newValues', dataChangeEvent.currentValues);\n");
			buf.append("proxy.addParam('oldValues', dataChangeEvent.oldValues);\n");
			buf.append("proxy.addParam('isColSingle', 'true');\n");
			buf.append("}\n");
		}
	}

	private String createJsSubmitRule(EventSubmitRule submitRule, String ruleId) {
		StringBuffer sb = new StringBuffer();
		Map<String, WidgetRule> widgetRuleMap = submitRule.getWidgetRules();
		if(widgetRuleMap != null && !widgetRuleMap.isEmpty())
		{	
			Iterator<Entry<String, WidgetRule>> it = widgetRuleMap.entrySet().iterator();
			while(it.hasNext())
			{
				Entry<String, WidgetRule> entry = it.next();
				WidgetRule widgetRule = entry.getValue();
				String wstr = getWidgetRule(widgetRule);
				sb.append(wstr);
				
				String widgetId = widgetRule.getId();
				sb.append(ruleId).append(".addWidgetRule('").append(widgetId).append("', wdr_").append(widgetId).append(");\n");
			}
		}
		return sb.toString();
	}
	
	private String getWidgetRule(WidgetRule widgetRule){
		StringBuffer buf = new StringBuffer();
		String wid = "wdr_" + widgetRule.getId();
		buf.append("var ").append(wid).append(" = new WidgetRule('").append(widgetRule.getId()).append("');\n");
		if(widgetRule.isCardSubmit()){
			buf.append(wid).append(".setCardSubmit(true);\n");
		}
		if(widgetRule.isTabSubmit()){
			buf.append(wid).append(".setTabSubmit(true);\n");
		}
		if(widgetRule.isPanelSubmit()){
			buf.append(wid).append(".setPanelSubmit(true);\n");
		}
		
		DatasetRule[] dsRules = widgetRule.getDatasetRules();
		if(dsRules != null){
			for (int i = 0; i < dsRules.length; i++) {
				DatasetRule dsRule = dsRules[i];
				String id = "dsr_" + dsRule.getId();
				buf.append("var ").append(id).append(" = new DatasetRule('").append(dsRule.getId()).append("','").append(dsRule.getType()).append("');\n");
				buf.append(wid).append(".addDsRule('").append(dsRule.getId()).append("',").append(id).append(");\n");
			}
		}
		
		TreeRule[] treeRules = widgetRule.getTreeRules();
		if(treeRules != null){
			for (int i = 0; i < treeRules.length; i++) {
				TreeRule treeRule = treeRules[i];
				String id = "treer_" + treeRule.getId();
				buf.append("var ").append(id).append(" = new TreeRule('").append(treeRule.getId()).append("','").append(treeRule.getType()).append("');\n");
				buf.append(wid).append(".addTreeRule('").append(treeRule.getId()).append("',").append(id).append(");\n");
			}
		}
		
		GridRule[] gridRules = widgetRule.getGridRules();
		if(gridRules != null){
			for (int i = 0; i < gridRules.length; i++) {
				GridRule gridRule = gridRules[i];
				String id = "gridr_" + gridRule.getId();
				buf.append("var ").append(id).append(" = new GridRule('").append(gridRule.getId()).append("','").append(gridRule.getType()).append("');\n");
				buf.append(wid).append(".addGridRule('").append(gridRule.getId() + "',").append(id).append(");\n");
			}
		}
		
		FormRule[] formRules = widgetRule.getFormRules();
		if(formRules != null){
			for (int i = 0; i < formRules.length; i++) {
				FormRule formRule = formRules[i];
				String id = "formr_" + formRule.getId();
				buf.append("var ").append(id).append(" = new FormRule('").append(formRule.getId()).append("','").append(formRule.getType()).append("');\n");
				buf.append(wid).append(".addFormRule('").append(formRule.getId() + "',").append(id).append(");\n");
			}
		}
		return buf.toString();
	}

	protected String getExtendProxyStr(WebElement ele, JsListenerConf listener, EventHandlerConf jsEvent) {
		return null;
	}

	protected abstract String getSourceType(WebElement ele);

	/**
	 * ����Menu��Tag��ǩ
	 * @param menuId
	 */
	protected void createMenuTag(String menuId) {
		ContextMenuComp ctxMenu = getCurrWidget().getViewMenus().getContextMenu(menuId);
		if(!ctxMenu.isRendered()){
			ContextMenuCompTag tag = new ContextMenuCompTag();
			tag.setJspContext(getJspContext());
			tag.setId(ctxMenu.getId());
			try {
				tag.doTag();
			} 
			catch (Exception e) {
				Logger.error(e.getMessage(), e);
				throw new LfwRuntimeException("����ContextMenu����");
			}
		}
	}
	
	/**
	 * ����Ҽ��˵�
	 * @param menuId
	 * @param id
	 * @return
	 */
	protected String addContextMenu(String menuId, String id) {
		createMenuTag(menuId);
		String menuShowId = COMP_PRE + getCurrWidget().getId() + "_" + menuId;
		StringBuffer buf = new StringBuffer();
		buf.append(id)
    	   .append(".setContextMenu(")
    	   .append(menuShowId)
    	   .append(");\n");
		// ���ContextMenu�ϵ�ContextMenu��������
		buf.append(menuShowId)
 	       .append(".addZIndex();\n");
		
	    return buf.toString();
	}
	
	
	protected PageModel getPageModel()
	{
		return (PageModel) getJspContext().getAttribute("pageModel");
	}
	
	
	/**
	 * ���ж��﷭��,������ܷ���,����ԭi18nName
	 * @param i18nName
	 * @return
	 */
	protected String translate(String i18nName, String defaultI18nName, String langDir){
		if(i18nName == null && defaultI18nName == null)
			return "";
//			return defaultI18nName;
//		return i18nName;
		if(langDir == null)
			langDir = LfwRuntimeEnvironment.getLangDir();
		return LanguageUtil.getWithDefaultByProductCode(langDir, defaultI18nName, i18nName);
	}
	
	/**
	 * ���ش���λ�ĸ߶Ȼ���
	 * @param size: �߶Ȼ���
	 * @return
	 */
	public String getFormatSize(String size) {
		if (size == null)
			return "0px";
		else if (size.indexOf("%") != -1 || size.indexOf("px") != -1)
			return size;
		else
			return size + "px";
	}
	
	/**
	 * ��ȡds����ʵid
	 * @param dsId
	 * @param widgetId
	 * @return
	 */
	protected String getDatasetVarShowId(String dsId, String widgetId)
	{
		return DS_PRE + widgetId + "_" + dsId;
	}
	
	public String getWidget() {
		return widget;
	}

	public void setWidget(String widget) {
		this.widget = widget;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
