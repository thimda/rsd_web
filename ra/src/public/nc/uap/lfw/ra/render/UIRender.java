package nc.uap.lfw.ra.render;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.ToolBarItem;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.WidgetElement;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
import nc.uap.lfw.core.ctrlfrm.plugin.IControlPlugin;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.LfwParameter;
import nc.uap.lfw.core.data.Parameter;
import nc.uap.lfw.core.event.IEventSupport;
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
import nc.uap.lfw.core.model.AppTypeUtil;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.page.ViewMenus;
import nc.uap.lfw.core.processor.EventRequestContext;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIWidget;
import nc.uap.lfw.ra.itf.IDynamicAttributes;
import nc.uap.lfw.ra.itf.IUIRender;
import nc.uap.lfw.util.LanguageUtil;
import nc.uap.lfw.util.LfwClassUtil;

/**
 * @author renxh ��Ⱦ�������
 * @param <T>
 * @param <K>
 */
public abstract class UIRender<T extends UIElement, K extends WebElement> implements IUIRender, IDynamicAttributes {

	public static final String DIV_INDEX = "$DIV_INDEX";
	// ռλDIVǰ׺����ֹ��DIV id�Ϳؼ�ʵ��DIV��ͻ
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

	public static final String PLACEINDIV_KEY = "placeInDiv_key";
	public static final String PLACEINDIV_VALUE = "placeInDiv_value";
	public static final String PLACEINDIV_SCRIPT = "placeInDiv_script";

	public static final String DS_SCRIPT = "dsScript";
	public static final String ALL_SCRIPT = "allScript";

	protected String id; // ���֡��������ؼ���Id
	protected String divId; // ռλdiv��id
	protected String varId; // ����js����id
	protected String widget; // widget��Ӧ��Id
//	protected IUIRenderGroup group = null; // ��Ӧ����Ⱦ��
	private T uiElement; //
	private K webElement; // 
	private IUIRender parentRender; // ����Ⱦ��

	private PageMeta pageMeta; // ҳ��Ԫ����

	private UIMeta uiMeta; // ҳ��Ԫ����

	private HashMap<String, Object> attributeMap = null; // ��Ⱦ��������

	// ģʽ״̬
//	private boolean editMode = false; // true Ϊ�༭̬ false Ϊ�Ǳ༭̬

	protected static final String MIN_HEIGHT = "23px";
	
	private IControlPlugin controlPlugin;
	/**
	 * ���캯��
	 * 
	 * @param uiEle
	 * @param webEle
	 */
	public UIRender(T uiEle, K webEle) {
		uiElement = uiEle;
		webElement = webEle;
	}

	public T getUiElement() {
		return uiElement;
	}

	public K getWebElement() {
		return webElement;

	}

	public String getDivId() {
		return divId;
	}

	public void setDivId(String divId) {
		this.divId = divId;
	}

	public UIMeta getUiMeta() {
		return uiMeta;
	}
	
	private UIMeta getTargetUiMeta() {
		if(this.widget != null && (!this.widget.equals("") && !this.widget.equals("pagemeta")) && uiMeta != null){
			UIWidget uiw = (UIWidget) UIElementFinder.findElementById(uiMeta, this.widget);
			if(uiw != null)
				return uiw.getUimeta();
			return null;
		}
		return uiMeta;
	}

	public void setUiMeta(UIMeta uiMeta) {
		this.uiMeta = uiMeta;
	}

	/**
	 * 2011-7-29 ����04:47:48 renxh des��varIdΪ�ÿؼ�js�Ķ���ı�����
	 * 
	 * @return
	 */
	public String getVarId() {
		if (varId == null) {
			if (getCurrWidget() == null)
				varId = COMP_PRE + getId();
			else
				varId = COMP_PRE + getCurrWidget().getId() + "_" + getId();
		}
		return varId;
	}

	public void setVarId(String varId) {
		this.varId = varId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
//
//	public IUIRenderGroup getGroup() {
//		return group;
//	}
//
//	public void setGroup(IUIRenderGroup group) {
//		this.group = group;
//	}

	public PageMeta getPageMeta() {
		return pageMeta;
	}

	public void setPageMeta(PageMeta pageMeta) {
		this.pageMeta = pageMeta;
	}

	public String getWidget() {
		return widget;
	}

	public void setWidget(String widget) {
		this.widget = widget;
	}

	public IUIRender getParentRender() {
		return parentRender;
	}

	public void setParentRender(IUIRender parentRender) {
		this.parentRender = parentRender;
	}

//	public void setEditMode(boolean editMode) {
//		this.editMode = editMode;
//	}

	public Object getAttribute(String key) {
		if (attributeMap != null && attributeMap.containsKey(key))
			return attributeMap.get(key);
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.uap.lfw.ra.itf.IDynamicAttributes#setAttribute(java.lang.String,
	 * java.lang.Object)
	 */
	public void setAttribute(String key, Object value) {
		if (attributeMap == null)
			attributeMap = new HashMap<String, Object>();
		attributeMap.put(key, value);
	}

	public void removeAttribute(String key) {
		if (attributeMap == null)
			return;
		if (attributeMap.containsKey(key))
			attributeMap.remove(key);
	}

	/**
	 * ���ж��﷭��,������ܷ���,����ԭi18nName
	 * 
	 * @param i18nName
	 * @return
	 */
	public String translate(String i18nName, String defaultI18nName, String langDir) {
		if (i18nName == null && defaultI18nName == null)
			return "";
		if (langDir == null)
			langDir = LfwRuntimeEnvironment.getLangDir();
		return LanguageUtil.getWithDefaultByProductCode(langDir, defaultI18nName, i18nName);
	}

	/**
	 * 2011-7-26 ����02:56:44 renxh des������key���Ψһ��id, ��idΪ��ʱ�������ô˷������Id
	 * 
	 * @param key
	 * @return
	 */
	public String getUniqueId(String key) {
		String id = (String) LfwRuntimeEnvironment.getWebContext().getAttribute(key);
		if (id == null) {
			id = "0";
		} else {
			id = String.valueOf(Integer.parseInt(id) + 1);
		}
		LfwRuntimeEnvironment.getWebContext().setAttribute(key, id);
		return id;
	}

	/**
	 * 2011-7-26 ����02:57:01 renxh des���������������ԣ�ȫ��
	 * 
	 * @param key
	 * @param obj
	 */
	public void setContextAttribute(String key, Object obj) {
		LfwRuntimeEnvironment.getWebContext().setAttribute(key, obj);
	}

	/**
	 * 2011-7-26 ����02:57:26 renxh des��������������ԣ�ȫ��
	 * 
	 * @param key
	 * @return
	 */
	public Object getContextAttribute(String key) {
		return LfwRuntimeEnvironment.getWebContext().getAttribute(key);
	}

	/**
	 * 2011-7-26 ����02:57:39 renxh des��ɾ������������ ȫ��
	 * 
	 * @param key
	 */
	public void removeContextAttribute(String key) {
		LfwRuntimeEnvironment.getWebContext().removeAttribute(key);
	}

	/**
	 * 2011-7-29 ����04:50:56 renxh des������¼�֧��
	 * 
	 * @param element
	 *            webԪ��
	 * @param widgetId
	 *            Ƭ��Id
	 * @param showId
	 *            js������id
	 * @param parentId
	 *            ��Id
	 * @return
	 */
	protected String addEventSupport(IEventSupport element, String widgetId, String showId, String parentId) {
		StringBuffer buf = new StringBuffer("");

		// ��ÿؼ���¶���¼�map
//		Map<String, JsListenerConf> listenerMap = element.getListenerMap();
		List<JsListenerConf> listeners = new ArrayList<JsListenerConf>();
		// EventConf event = new EventConf();
		// event.setOnserver(true);
		// event.setMethodName("func111");
		// event.setName("onclick");
		// event.setControllerClazz("app.NEWAPP");
		// event.setJsEventClaszz("nc.uap.lfw.core.event.conf.MouseListener");
//		listeners.addAll(listenerMap.values());
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
	private Collection<JsListenerConf> Controller2Event(EventConf[] events, IEventSupport element) {
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
					//Modify by licza 12/13 11 support popView event  
					WidgetElement we = (WidgetElement) element;
					if(we.getWidget() != null){
						String trueId = null;
						if(element instanceof LfwWidget){
							trueId = we.getId();
						}else{
							trueId = we.getWidget().getId();
						}
						parentCtrl = getPageMeta().getWidget(trueId).getControllerClazz();
						//Modify End
						
						el = AppLifeCycleContext.EVENT_LEVEL_VIEW ;
					}
					else{
						if(getPageMeta().getWidget(we.getId()) != null)
							parentCtrl = getPageMeta().getWidget(we.getId()).getControllerClazz();
					}
				}
				if (element instanceof PageMeta) {
					PageMeta we = (PageMeta) element;
					parentCtrl = we.getControllerClazz();
					el = AppLifeCycleContext.EVENT_LEVEL_WIN ;
				}
				if (element instanceof UIElement){
					UIElement ue = (UIElement) element;
					String widgetId = ue.getWidgetId();
					parentCtrl = getPageMeta().getWidget(widgetId).getControllerClazz(); 
					el = AppLifeCycleContext.EVENT_LEVEL_VIEW;
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
				//dingrf add 111118
				if (ent.getParamList().size() == 0 && event.getParamList().size() > 0) 
					ent.setParamList(event.getParamList());
				ent.setName(event.getName());
				ent.setExtendAttribute(EventHandlerConf.SUBMIT_PRE + AppLifeCycleContext.METHOD_NAME, event.getMethodName());
				ent.setExtendAttribute(EventHandlerConf.SUBMIT_PRE + LfwPageContext.SOURCE_ID, element.getId());
				ent.setExtendAttribute(EventHandlerConf.SUBMIT_PRE + "clc", ctrl);
				ent.setExtendAttribute(EventHandlerConf.SUBMIT_PRE + AppLifeCycleContext.EVENT_LEVEL, el);
				
				//dingrf 20111121 event.getControllerClazz() Ϊ��ʱ��ʹͬһ��ele�Ķ���¼�ͬ��
//				jslc.setId(event.getControllerClazz().replace(".", "_"));
				jslc.setId(event.getMethodName());

				jslc.setServerClazz(ctrl);
				jslc.addEventHandler(ent);
				
				//dingrf 20111121   event.getJsEventClaszz()   ����plugin plugout�¼��������¼�ʱ��keyֵ��ͬ���ᶪ�¼�
//				jsListeners.put(event.getJsEventClaszz(), jslc);
				jsListeners.put(event.getMethodName() + "_" + event.getJsEventClaszz(), jslc);
			}
			return jsListeners.values();
		}

		return null;
	}

	/**
	 * 2011-7-29 ����04:51:25 renxh des�����ɼ�����
	 * 
	 * @param listener
	 *            ����
	 * @param listenerShowId
	 *            ������Ӧ�ı���id
	 * @param widgetId
	 *            Ƭ��Id
	 * @param ele
	 *            webԪ��
	 * @param parentId
	 *            ��id
	 * @return
	 */
	protected String generateListener(JsListenerConf listener, String listenerShowId, String widgetId, IEventSupport ele, String parentId) {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(listenerShowId).append(" = new ").append(listener.getJsClazz()).append("();\n");
		if (ele instanceof MenuItem) {
			buf.append(getVarId());
			// StringBuffer newBuf = new StringBuffer();
			MenuItem currEle = (MenuItem) ele;
			currEle = currEle.getParentItem();
			StringBuffer bf = new StringBuffer();
			while (currEle != null) {
				String str = "";
				str += ".getMenu('" + currEle.getId() + "')";
				currEle = currEle.getParentItem();
				bf.insert(0, str);
			}
			buf.append(bf.toString());
			buf.append(".getMenu('").append(ele.getId()).append("')");
		} else if (ele instanceof ToolBarItem) {
			// ����д��Ϊ�˿��ظ�ʹ��
			buf.append("pageUI.getWidget('").append(widgetId).append("').getComponent('").append(getId()).append("')");
			buf.append(".getButton('").append(ele.getId()).append("')");
		} else if (ele instanceof PageMeta) {
			buf.append("pageUI");
		} else if (ele instanceof Dataset) {
			String dsId = getDatasetVarShowId(ele.getId(), widgetId);
			buf.append(dsId);
		} else if (ele instanceof LfwWidget) {
			LfwWidget widget = (LfwWidget) ele;
			//Modify by licza 12/13 11 support popView event
			if (AppTypeUtil.getApplicationType().equals(AppTypeUtil.APP_TYPE) || widget.isDialog())
			//Modify end
				buf.append("window.pageUI.getWidget('").append(widgetId).append("')");
		} else
			buf.append(getVarId());

		buf.append(".$TEMP_").append(listener.getId()).append(" = ").append(listenerShowId).append(";\n");

		if (!(ele instanceof PageMeta)) {
			buf.append(listenerShowId).append(".").append(LfwPageContext.SOURCE_ID).append(" = '").append(ele.getId()).append("';\n");
		}
		buf.append(listenerShowId).append(".").append(LfwPageContext.LISTENER_ID).append(" = '").append(listener.getId()).append("';\n");

		if (widgetId != null) {
			buf.append(listenerShowId).append(".").append(LfwPageContext.WIDGET_ID).append(" = '").append(widgetId).append("';\n");
		}

		buf.append(listenerShowId).append(".").append(LfwPageContext.SOURCE_TYPE).append(" = '").append(getSourceType(ele)).append("';\n");

		if (parentId != null) {
			buf.append(listenerShowId).append(".").append(LfwPageContext.PARENT_SOURCE_ID).append(" = '").append(parentId).append("';\n");
		}

		Map<String, EventHandlerConf> eventMap = listener.getEventHandlerMap();
		Iterator<EventHandlerConf> eit = eventMap.values().iterator();
		while (eit.hasNext()) {
			EventHandlerConf jsEvent = eit.next();

			buf.append(listenerShowId).append(".").append(jsEvent.getName()).append(" = function(");
			StringBuffer params = new StringBuffer();
			int pSize = jsEvent.getParamList().size();
			if (jsEvent.getParamList() != null && pSize > 0) {
				for (int i = 0; i < pSize; i++) {
					params.append(jsEvent.getParamList().get(i).getName());
					if (i != pSize - 1)
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
			if (listener.getServerClazz() != null && jsEvent.isOnserver()) {
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
	 * 2011-7-29 ����04:51:43 renxh des�� �����ύ����
	 * 
	 * @param listener
	 * @param listenerShowId
	 * @param buf
	 * @param jsEvent
	 * @param widgetId
	 */
	protected void generateSubmitRule(JsListenerConf listener, String listenerShowId, StringBuffer buf, EventHandlerConf jsEvent, String widgetId) {
		// �ύ����
		EventSubmitRule submitRule = jsEvent.getSubmitRule();
		if (widgetId != null) {
			if (submitRule == null) {
				submitRule = new EventSubmitRule();
			}
			if (submitRule.getWidgetRule(widgetId) == null) {
				WidgetRule wr = new WidgetRule();
				wr.setId(widgetId);
				submitRule.addWidgetRule(wr);
			}
		}
		if (submitRule != null) {
			String ruleId = "sr_" + jsEvent.getName() + "_" + listener.getId();
			buf.append("var ").append(ruleId).append(" = new SubmitRule();\n");
			if (submitRule.getParamMap() != null && submitRule.getParamMap().size() > 0) {
				Iterator<Entry<String, Parameter>> it = submitRule.getParamMap().entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, Parameter> entry = it.next();
					buf.append(ruleId).append(".addParam('").append(entry.getKey()).append("', '").append(entry.getValue().getValue()).append("');\n");
				}
			}
			if (submitRule.isCardSubmit()) {
				buf.append(ruleId).append(".setCardSubmit(true);\n");
			}
			if (submitRule.isTabSubmit()) {
				buf.append(ruleId).append(".setTabSubmit(true);\n");
			}
			if (submitRule.isPanelSubmit()) {
				buf.append(ruleId).append(".setPanelSubmit(true);\n");
			}

			String jsScript = createJsSubmitRule(submitRule, ruleId);
			buf.append(jsScript);

			EventSubmitRule pSubmitRule = submitRule.getParentSubmitRule();
			if (pSubmitRule != null) {
				String pRuleId = ruleId + "_parent";
				buf.append("var " + pRuleId + " = new SubmitRule();\n");
				String pJsScript = createJsSubmitRule(pSubmitRule, pRuleId);
				buf.append(pJsScript);
				buf.append(ruleId + ".setParentSubmitRule(" + pRuleId + ");\n");
			}

			buf.append(listenerShowId).append(".").append(jsEvent.getName()).append(".submitRule = (").append(ruleId).append(");\n");
		}

	}

	/**
	 * 2011-7-29 ����04:51:58 renxh des������js���ύ����
	 * 
	 * @param submitRule
	 * @param ruleId
	 * @return
	 */
	private String createJsSubmitRule(EventSubmitRule submitRule, String ruleId) {
		StringBuffer sb = new StringBuffer();
		Map<String, WidgetRule> widgetRuleMap = submitRule.getWidgetRules();
		if (widgetRuleMap != null && !widgetRuleMap.isEmpty()) {
			Iterator<Entry<String, WidgetRule>> it = widgetRuleMap.entrySet().iterator();
			while (it.hasNext()) {
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

	/**
	 * 2011-7-29 ����04:52:10 renxh �õ�Ƭ�εĹ���
	 * 
	 * @param widgetRule
	 * @return
	 */
	private String getWidgetRule(WidgetRule widgetRule) {
		StringBuffer buf = new StringBuffer();
		String wid = "wdr_" + widgetRule.getId();
		buf.append("var ").append(wid).append(" = new WidgetRule('").append(widgetRule.getId()).append("');\n");
		if (widgetRule.isCardSubmit()) {
			buf.append(wid).append(".setCardSubmit(true);\n");
		}
		if (widgetRule.isTabSubmit()) {
			buf.append(wid).append(".setTabSubmit(true);\n");
		}
		if (widgetRule.isPanelSubmit()) {
			buf.append(wid).append(".setPanelSubmit(true);\n");
		}

		DatasetRule[] dsRules = widgetRule.getDatasetRules();
		if (dsRules != null) {
			for (int i = 0; i < dsRules.length; i++) {
				DatasetRule dsRule = dsRules[i];
				String id = "dsr_" + dsRule.getId();
				buf.append("var ").append(id).append(" = new DatasetRule('").append(dsRule.getId()).append("','").append(dsRule.getType()).append("');\n");
				buf.append(wid).append(".addDsRule('").append(dsRule.getId()).append("',").append(id).append(");\n");
			}
		}

		TreeRule[] treeRules = widgetRule.getTreeRules();
		if (treeRules != null) {
			for (int i = 0; i < treeRules.length; i++) {
				TreeRule treeRule = treeRules[i];
				String id = "treer_" + treeRule.getId();
				buf.append("var ").append(id).append(" = new TreeRule('").append(treeRule.getId()).append("','").append(treeRule.getType()).append("');\n");
				buf.append(wid).append(".addTreeRule('").append(treeRule.getId()).append("',").append(id).append(");\n");
			}
		}

		GridRule[] gridRules = widgetRule.getGridRules();
		if (gridRules != null) {
			for (int i = 0; i < gridRules.length; i++) {
				GridRule gridRule = gridRules[i];
				String id = "gridr_" + gridRule.getId();
				buf.append("var ").append(id).append(" = new GridRule('").append(gridRule.getId()).append("','").append(gridRule.getType()).append("');\n");
				buf.append(wid).append(".addGridRule('").append(gridRule.getId() + "',").append(id).append(");\n");
			}
		}

		FormRule[] formRules = widgetRule.getFormRules();
		if (formRules != null) {
			for (int i = 0; i < formRules.length; i++) {
				FormRule formRule = formRules[i];
				String id = "formr_" + formRule.getId();
				buf.append("var ").append(id).append(" = new FormRule('").append(formRule.getId()).append("','").append(formRule.getType()).append("');\n");
				buf.append(wid).append(".addFormRule('").append(formRule.getId() + "',").append(id).append(");\n");
			}
		}
		return buf.toString();
	}

	/**
	 * 2011-7-29 ����04:52:39 renxh des����Ⱦ�������
	 * 
	 * @param widgetId
	 * @param listenerShowId
	 * @param ele
	 * @param listener
	 * @param jsEvent
	 * @param parentId
	 * @return
	 */
	protected String generateServerProxy(String widgetId, String listenerShowId, IEventSupport ele, JsListenerConf listener, EventHandlerConf jsEvent, String parentId) {
		StringBuffer buf = new StringBuffer();
		String eventName = jsEvent.getName();
		buf.append("if(window.editMode) return;");
		buf.append("var proxy = new ServerProxy(this.$TEMP_" + listener.getId() + ",'" + eventName + "'," + jsEvent.isAsync() + ");\n");
		buf.append("if(typeof beforeCallServer != 'undefined')\n").append("beforeCallServer(proxy, '" + listener.getId() + "', '" + eventName + "','" + ele.getId() + "');\n");
		addProxyParam(buf, eventName);

		if (jsEvent.getExtendMap().size() > 0) {
			String[] keys = jsEvent.getExtendMap().keySet().toArray(new String[0]);
			for (int i = 0; i < keys.length; i++) {
				if (keys[i].startsWith(EventHandlerConf.SUBMIT_PRE)) {
					buf.append("proxy.addParam('").append(keys[i].substring(EventHandlerConf.SUBMIT_PRE.length())).append("', '");
					Object value = jsEvent.getExtendAttributeValue(keys[i]);
					if (value == null)
						value = "";
					buf.append(value.toString()).append("');\n");
				}
			}
		}

		String extendStr = getExtendProxyStr(ele, listener, jsEvent);
		if (extendStr != null)
			buf.append(extendStr);

		// ��ʾ������ʾ��
//		buf.append("showDefaultLoadingBar();\n");
//
//		buf.append("return proxy.execute();\n");
		buf.append("ServerProxy.wrapProxy(proxy);\n");
		return buf.toString();
	}

	/**
	 * 2011-7-29 ����04:53:08 renxh des�������չ�ô����ַ���
	 * 
	 * @param ele
	 * @param listener
	 * @param jsEvent
	 * @return
	 */
	protected String getExtendProxyStr(IEventSupport ele, JsListenerConf listener, EventHandlerConf jsEvent) {
		return null;
	}

	/**
	 * ����ǰ̨Event���ύ��������̨
	 * 
	 * @param buf
	 *            JS�����ַ���
	 * @param eventName
	 *            �¼�����
	 */
	protected void addProxyParam(StringBuffer buf, String eventName) {
		if (DatasetListener.ON_AFTER_DATA_CHANGE.equals(eventName)) {
			buf.append("proxy.addParam('cellRowIndex', dataChangeEvent.cellRowIndex);\n");
			buf.append("proxy.addParam('cellColIndex', dataChangeEvent.cellColIndex);\n");
			buf.append("proxy.addParam('newValue', dataChangeEvent.currentValue);\n");
			buf.append("proxy.addParam('oldValue', dataChangeEvent.oldValue);\n");
		}
	}

	protected void addAppParam(StringBuffer buf, String eventName, EventHandlerConf jsEvent) {
		// buf.append("proxy.addParam('cellRowIndex', dataChangeEvent.cellRowIndex);\n");
		// buf.append("proxy.addParam('cellColIndex', dataChangeEvent.cellColIndex);\n");
		// buf.append("proxy.addParam('newValue', dataChangeEvent.currentValue);\n");

	}

	/**
	 * ����Dataset��onAfterDataChange�¼���JS�ж����
	 * 
	 * @param buf
	 * @param jsEvent
	 * @param ele
	 * @param widgetId
	 */
	private void generateAfterDataChangeEventHeadScript(StringBuffer buf, EventHandlerConf jsEvent, IEventSupport ele, String widgetId) {
		LfwParameter fieldParam = jsEvent.getExtendParam(EventHandlerConf.PARAM_DATASET_FIELD_ID);
		String fields = fieldParam.getDesc();
		buf.append(getDatasetVarShowId(ele.getId(), widgetId)).append(".afterDataChangeAcceptFields = \"").append(fields).append("\".split(\",\");\n");
	}

	/**
	 * ����Dataset��onBeforeDataChange�¼���JS�ж����
	 * 
	 * @param buf
	 * @param jsEvent
	 * @param ele
	 * @param widgetId
	 */
	private void generateBeforeDataChangeEventHeadScript(StringBuffer buf, EventHandlerConf jsEvent, IEventSupport ele, String widgetId) {
		LfwParameter fieldParam = jsEvent.getExtendParam(EventHandlerConf.PARAM_DATASET_FIELD_ID);
		String fields = fieldParam.getDesc();
		buf.append(getDatasetVarShowId(ele.getId(), widgetId));
		buf.append(".beforeDataChangeAcceptFields = \"");
		buf.append(fields).append("\".split(\",\");\n");
	}

	/**
	 * 2011-8-16 ����03:33:55 renxh des���������ݼ���js����
	 * 
	 * @param dsId
	 * @param widgetId
	 * @return
	 */
	protected String getDatasetVarShowId(String dsId, String widgetId) {
		return DS_PRE + widgetId + "_" + dsId;
	}

	/**
	 * ���ش���λ�ĸ߶Ȼ���
	 * 
	 * @param size
	 *            : �߶Ȼ���
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
	 * ����Ҽ��˵�
	 * 
	 * @param menuId
	 * @param id
	 * @return
	 */
	protected String addContextMenu(String menuId, String id) {
		if (this.isEditMode())
			return "";
		String menuShowId = COMP_PRE + getWidget() + "_" + menuId;
		StringBuffer buf = new StringBuffer();
		buf.append(createMenuRender(menuId));
		buf.append(id).append(".setContextMenu(").append(menuShowId).append(");\n");
		// ���ContextMenu�ϵ�ContextMenu��������
		buf.append(menuShowId).append(".addZIndex();\n");

		return wrapByRequired("contextmenu", buf.toString());
	}

	/**
	 * 2011-7-29 ����04:53:32 renxh des�� ��õ�ǰ��Ƭ��
	 * 
	 * @return
	 */
	public LfwWidget getCurrWidget() {
		if (this.getPageMeta() == null)
			return null;
		if (this.getWidget() == null) {
			LfwWidget[] widgets = getPageMeta().getWidgets();
			int doubleId = 0;
			LfwWidget ownerWidget = null;
			if (widgets != null && widgets.length > 0) {
				for (int i = 0; i < widgets.length; i++) {
					ViewComponents views = widgets[i].getViewComponents();
					ViewMenus contextMenus = widgets[i].getViewMenus();

					if (views.getComponent(getId()) != null) {
						doubleId++;
					}

					if (contextMenus.getContextMenu(getId()) != null)
						doubleId++;

					if (contextMenus.getMenuBar(getId()) != null)
						doubleId++;

					if (doubleId >= 2)
						throw new LfwRuntimeException("���id���������id�ظ�,id=" + getId());
					else if (doubleId == 1 && ownerWidget == null)
						ownerWidget = widgets[i];
				}

				if (doubleId == 0) {
					return null;
				}
			}
			this.setWidget(ownerWidget.getId());
			return ownerWidget;
		} else {
			return this.pageMeta.getWidget(this.getWidget());
		}

	}

	/**
	 * ����Menu����Ⱦ��
	 * 
	 * @param menuId
	 */
	protected String createMenuRender(String menuId) {
		LfwWidget currWidget = getCurrWidget();
		if (currWidget == null) {
			throw new LfwRuntimeException("�޷���õ�ǰ�� widget");
		}
		ContextMenuComp ctxMenu = currWidget.getViewMenus().getContextMenu(menuId);
		if (!ctxMenu.isRendered()) {
			UIContextMenuCompRender render = (UIContextMenuCompRender) ControlFramework.getContextMenuUIRender(getUiMeta(), null, ctxMenu, this.getPageMeta(), this.getParentRender(), DriverPhase.PC);
			return render.createRenderScript();
		}
		return "";
	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see nc.uap.lfw.ra.itf.IUIRender#renderHtml(java.io.Writer)
//	 */
//	public void renderHtml(Writer writer) throws IOException {
//		writer.write(createRenderHtml());
//
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see nc.uap.lfw.ra.itf.IUIRender#renderScript(java.io.Writer)
//	 */
//	public void renderScript(Writer writer) throws IOException {
//		writer.write(createRenderScript());
//	}

	/**
	 * 2011-8-4 ����03:51:38 renxh des���ж��Ƿ�Ϊ�ɱ༭״̬
	 * 
	 * @return
	 */
	public boolean isEditMode() {
		return LfwRuntimeEnvironment.isEditMode();
	}

	/**
	 * 2011-8-4 ����03:52:07 renxh des������µ�divId����Ϊ�Ա༭̬ʱ����Ҫ��������div�ı�id
	 * 
	 * @return
	 */
	public String getNewDivId() {
		if (isEditMode()) {
			return this.getDivId() + "_raw";
		}
		return this.getDivId();
	}

	/**
	 * 2011-8-2 ����07:02:06 renxh des���༭̬ʱ��������div
	 * 
	 * @return
	 */
	public String generalEditableHeadHtml() {
		if (isEditMode()) {
			StringBuffer buf = new StringBuffer("");
			buf.append("<div ");
			buf.append(" id='").append(getDivId()).append("'");
			buf.append(" style='position:relative;height:100%;");
			boolean flowmode = isFlowMode();
			if(flowmode){
				buf.append("min-height:" + MIN_HEIGHT + ";");
			}
			buf.append("'");
			if(flowmode){
				buf.append(" flowmode='true'");
			}
			buf.append(">");
			return buf.toString();
		}
		return "";
	}

	/**
	 * 2011-8-2 ����07:02:06 renxh des���༭̬ʱ��������div
	 * 
	 * @return
	 */
	public String generalEditableHeadHtmlDynamic() {
		if (isEditMode()) {

			StringBuffer buf = new StringBuffer("");
			buf.append("var ").append(getDivId()).append(" = $ce('DIV');\n");
			buf.append(getDivId()).append(".style.position = 'relative';\n");
			buf.append(getDivId()).append(".style.height = '100%';\n");
//			buf.append(getDivId()).append(".style.minHeight = '20px';\n");
			buf.append(getDivId()).append(".id='" + getDivId() + "';\n");
			if(isFlowMode()){
				buf.append(getDivId()).append(".style.minHeight = '" + MIN_HEIGHT + "';\n");
				buf.append(getDivId()).append(".flowmode = true;\n");
			}
			return buf.toString();
		}
		return "";
	}

	/**
	 * 2011-8-4 ����06:51:34 renxh des����Ⱦ�༭̬βdiv
	 * 
	 * @return
	 */
	public String generalEditableTailHtml() {
		if (isEditMode()) {
			String tail = "</div>\n";
			return tail;
		}
		return "";
	}

	/**
	 * 2011-8-4 ����06:52:05 renxh des����Ⱦ�༭̬ͷ �ű�
	 * 
	 * @return
	 */
	public String generalEditableHeadScript() {
		if(!isFlowMode()){
			if (this.isEditMode() && getDivId() != null) {
				return toResize("$ge(\"" + getDivId() + "\")", "editableDivResize");
			}
		}
		return "";
	}

	public String generalEditableHeadScriptDynamic() {
		if(!isFlowMode()){
			if (this.isEditMode() && getDivId() != null) {
				return toResizeDynamic(getDivId(), "editableDivResize");
			}
		}
		return "";
	}

	/**
	 * ���÷��� ���� ���²��ֵĽű�
	 * @param obj
	 * @param func
	 * @return 
	 */
	protected String toResize(String obj, String func) {
		StringBuffer buf = new StringBuffer();
		
		buf.append("\nif(" + func + "){\n");
//		buf.append("debugger;\n");
		buf.append("addAndCallResizeEvent(" + obj + ", " + func + ");\n");
		buf.append("}else{");
//		buf.append("debugger;");
		buf.append("setTimeout('try{addAndCallResizeEvent(" + obj + "," + func + ")}catch(e){}',500);\n");
		buf.append("};\n");
		return buf.toString();
	}

	/**
	 * ���÷��� ���� ���²��ֵĽű�
	 * @param obj
	 * @param func
	 * @return
	 */
	protected String toResizeDynamic(String obj, String func) {
		StringBuffer buf = new StringBuffer();
		
		buf.append("\nif(" + func + "){\n");
//		buf.append("debugger;\n");
		buf.append("addResizeEvent(" + obj + ", "+func+");\n");
		buf.append(func + ".call(" + obj + ");\n");
		buf.append("}else{\n");
//		buf.append("debugger;");
		buf.append("setTimeout('try{addResizeEvent(" + obj + ", " + func + ")}catch(e){}',500);\n");
		buf.append("};\n");
		return buf.toString();
	}

	/**
	 * 2011-8-4 ����06:52:23 renxh des����Ⱦ�༭̬ β �ű�
	 * 
	 * @return
	 */
	public String generalEditableTailScript() {
		if (isEditMode()) {
			if(this.getWidget() != null && LfwRuntimeEnvironment.isWindowEditorMode()){
				return "";
			}
			String widgetId = this.getWidget() == null ? "" : this.getWidget();
			String uiid = null;
			String subuiid = null;
			String eleid = null;
			String subeleid = null;
			if(uiElement instanceof UILayoutPanel){
				if(uiElement != null){
					UILayoutPanel panel = (UILayoutPanel) uiElement;
					UILayout parent = panel.getLayout();
					if(parent != null)
						uiid = parent.getId();
					subuiid = uiElement.getId();
				}
				if(webElement != null){
					WebElement parentEle = ((UIRender)parentRender).webElement;
					eleid = parentEle.getId();
					subeleid = webElement.getId();
				}
			}
			else{
				uiid = uiElement == null ? "" : (String) uiElement.getAttribute(UIElement.ID);
				eleid = webElement == null ? "" : webElement.getId();
			}
			
			String type = this.getRenderType(webElement);
			if (type == null)
				type = "";
			StringBuffer buf = new StringBuffer();
			if (getDivId() == null) {
				LfwLogger.error("div id is null!" + this.getClass().getName());
			} else {
				buf.append(this.addEditableListener(getDivId(), widgetId, uiid, subuiid, eleid, subeleid, type));
			}

			return buf.toString();
		}
		return "";
	}

	/**
	 * 2011-8-4 ����06:52:23 renxh des����Ⱦ�༭̬ β �ű�
	 * 
	 * @return
	 */
	public String generalEditableTailScriptDynamic() {
		return generalEditableTailScript();
	}

	/**
	 * 2011-9-14 ����10:18:37 renxh des��Ϊ������ӿɱ༭����ʽ������Ժ��ɿɱ༭��ʽ
	 * 
	 * @param divId
	 * @param widgetId
	 * @param uiId
	 * @param eleId
	 * @param type
	 * @return
	 */
	protected String addEditableListener(String divId, String widgetId, String uiId, String subuiId, String eleId, String subEleId, String type) {
		StringBuffer buf = new StringBuffer();
		buf.append("var params = {");
		buf.append("widgetid:'").append(widgetId).append("'");
		buf.append(",uiid:'").append(uiId).append("'");
		buf.append(",subuiid:'").append(subuiId).append("'");
		buf.append(",eleid:'").append(eleId).append("'");
		buf.append(",subeleid:'").append(subEleId).append("'");
		buf.append(",type:'").append(type).append("'");
		buf.append("};\n");
		buf.append("new EditableListener(document.getElementById('" + getDivId() + "'),params,'layout');\n");
		buf.append(addDragableListener(getDivId()));
		return buf.toString();
	}
	
	protected String addDragableListener(String divId){
		StringBuffer buf = new StringBuffer();
		buf.append("new DragListener(document.getElementById('" + divId + "'));");
		return buf.toString();
	}

	/**
	 * add renxh
	 * 
	 * @param buf
	 *            ��ִ���궯̬�޸��Ժ���Ҫִ�д˷����������²��֣�����漰�����ݼ��Ľű�������Ҳ��������ִ��
	 *            ���ڶ�̬���۴����ݼ�����Ҫִ�д˷�������ֻ��panel�л���� �˷��������нű���ĩβִ��
	 */
	protected void dynamicScriptTail(StringBuffer buf) {
		// ����Ĵ����Ժ�ó����
		StringBuffer buft = (StringBuffer) this.getContextAttribute(DS_SCRIPT);
		if (buft != null) {
			String script = buft.toString();
			buft.delete(0, buft.length());
			buf.append(script);
		}

		buf.append("\nlayoutInitFunc();\n");
		buf.append("window.isRenderDone = true;\n");
	}
	
	/**
	 * ��Ҷ������Ӷ�̬�ű�
	 * @param script
	 */
	public void addDynamicScript(String script){
		if(script == null || script.equals(""))
			return;
		if(AppTypeUtil.getApplicationType().equals(AppTypeUtil.APP_TYPE)){
			AppLifeCycleContext.current().getApplicationContext().addExecScript(script);
		}
		else{
			LfwPageContext ctx = EventRequestContext.getLfwPageContext();
			ctx.addExecScript(script);
		}
	}
	/**
	 * ��setContext֮ǰִ�еĽű�
	 * @param script
	 */
	public void addBeforeExeScript(String script){
		if(script == null || script.equals(""))
			return;
		if(AppTypeUtil.getApplicationType().equals(AppTypeUtil.APP_TYPE)){
			AppLifeCycleContext.current().getApplicationContext().addBeforeExecScript(script);
		}
		else{
			LfwPageContext ctx = EventRequestContext.getLfwPageContext();
			ctx.addBeforeExecScript(script);
		}
	}
	/**
	 * 2011-8-16 ����03:42:56 renxh des�����webԪ�ص�Ԫ������
	 * 
	 * @param ele
	 * @return
	 */
	protected abstract String getSourceType(IEventSupport ele);

	/**
	 * ������Ⱦ�����ͣ�Ĭ��ΪsourceType����
	 * 
	 * @param ele
	 * @return
	 */
	protected String getRenderType(WebElement ele) {
		return this.getSourceType(ele);
	}

//	/**
//	 * 2011-8-2 ����08:01:50 renxh des����Ⱦ html
//	 * 
//	 * @return
//	 */
//	public abstract String createRenderHtml();
//
//	/**
//	 * 2011-8-2 ����08:02:06 renxh des����Ⱦscript
//	 * 
//	 * @return
//	 */
//	public abstract String createRenderScript();

	/**
	 * 2011-8-2 ����08:02:16 renxh des��ͷ����html����
	 * 
	 * @return
	 */
	public abstract String generalHeadHtml();

	/**
	 * 2011-8-2 ����08:02:28 renxh des��β����html����
	 * 
	 * @return
	 */
	public abstract String generalTailHtml();

	/**
	 * 2011-8-2 ����08:02:38 renxh des��ͷ���Ľű�
	 * 
	 * @return
	 */
	public abstract String generalHeadScript();

	/**
	 * 2011-8-2 ����08:02:51 renxh des��β���Ľű�
	 * 
	 * @return
	 */
	public abstract String generalTailScript();

	protected boolean isFlowMode() {
		UIMeta um = getTargetUiMeta();
		if(um != null){
			Boolean flowMode = um.getFlowmode();
			return flowMode.booleanValue();
		}
		return false;
	}

	
	/**
	 * �������js library
	 * @param id
	 * @param func
	 * @return
	 */
	protected String wrapByRequired(String id, String func){
		if(id == null || id.equals(""))
			return func;
		StringBuffer buf = new StringBuffer();
		buf.append("require('").append(id).append("', function() {").append(func).append("});\n");
		return buf.toString();
	}

	
	public String getType(){
		return getControlPlugin().getId();
	}

	@Override
	public IControlPlugin getControlPlugin() {
		return controlPlugin;
	}

	@Override
	public void setControlPlugin(IControlPlugin controlPlugin) {
		this.controlPlugin = controlPlugin;		
	}
}
