package nc.uap.lfw.core.comp;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.base.ExtendAttributeSupport;
import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.event.EventUtil;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.IListenerSupport;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.exception.LfwPluginException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.ILifeCycleSupport;
import nc.uap.lfw.core.page.RequestLifeCycleContext;
import nc.uap.lfw.core.page.LifeCyclePhase;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.render.observer.IUIElementObserver;
import nc.uap.lfw.util.LfwClassUtil;

/**
 * Ԫ�ػ��࣬���пؼ�,dataset������Ԫ�ض������Դ���
 * TODO ��Ҫ���¼����������ڵ�������������
 * @author dengjt
 *
 */
public abstract class WebElement extends ExtendAttributeSupport implements IListenerSupport,IEventSupport,Serializable,Cloneable, ILifeCycleSupport{

	public static final String CONF_ADD = "add";
	public static final String CONF_REF = "ref";
	public static final String CONF_DEL = "del";
	
	private static final long serialVersionUID = -8330041983034564169L;
	
	//���Ψһ��ʶ
	private String id;
	//��ʶ�Ƿ���Ⱦ��
	private boolean rendered = false;
	
	//���ڴ��ڸ������û������ļ����������NCģ�壬�������ļ���������ʱ�����͡������ADD����ʾ����Ԫ��Ϊ�¼ӡ����ΪDEL����ʾɾ��ԭģ���ж�Ӧ��Ԫ�ء������REF����ʾ��ͨ�������ø���ԭ�����á�
	private String confType = CONF_ADD;
	//���ڴ��ڸ������û������ļ���ָ��ĳЩ��λ�����е�Ԫ�ؾ�������λ�á�-1��ʶ׷�ӵ�β������0��ʼ
	private int confPos = -1;
	
	private Map<String, JsListenerConf> listenerMap = new HashMap<String, JsListenerConf>();
	
	private List<Class<? extends JsListenerConf>> acceptListenerList = null;
	
	private List<JsEventDesc> acceptEventList = null;
	
	private List<EventConf> eventConfList = null;
	
	// context�Ƿ�ı�
	private boolean ctxChanged = false;
	// �Ѹı��context�����б�
	private List<String> ctxChangedProperties = new ArrayList<String>();

	private String from;
	
	private String sourcePackage;
	//��ʵ·��
	private String realPath;
	
	public WebElement()
	{
	}
	
	public WebElement(String id)
	{
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

	public boolean isRendered() {
		return rendered;
	}
	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}
	
	
	public Object clone()
	{
		WebElement ele = (WebElement) super.clone();
		if(this.listenerMap != null)
		{
			ele.listenerMap = new HashMap<String, JsListenerConf>();
			Iterator<JsListenerConf> it = this.listenerMap.values().iterator();
			while(it.hasNext())
			{
				ele.addListener((JsListenerConf) it.next().clone());
			}
		}
		ele.ctxChanged = false;
		ele.ctxChangedProperties = new ArrayList<String>();
		//Events
		EventConf[] eventConfs = this.getEventConfs();
		if(eventConfs != null && eventConfs.length > 0){
			ele.eventConfList = new ArrayList<EventConf>();
			for(EventConf eventConf : eventConfs){
				ele.addEventConf(eventConf);
			}
		}
		return ele;
	}

	public String getConfType() {
		return confType;
	}

	public void setConfType(String confType) {
		this.confType = confType;
	}
	
	/**
	 * ������Ĳ����ϲ�����ǰ�ؼ���
	 * Ҫ����Ĳ����ǵ�ǰ�������������ͬ
	 * �Դ��������Ϊ�յ�ֵ��һ���Դ������ֵΪ׼
	 * @param ele
	 */
	public void mergeProperties(WebElement ele){
		if(ele == null)
			return;
		if(!ele.getClass().isAssignableFrom(this.getClass())){
			throw new LfwRuntimeException("ָ����Ԫ�ز�������ͬһ�̳���ϵ");
		}
		
		int confPos = ele.getConfPos();
		if(confPos != -1)
			this.confPos = confPos;
		String confType = ele.getConfType();
		if(confType != null)
			this.confType = ele.getConfType();
	}

	public int getConfPos() {
		return confPos;
	}

	public void setConfPos(int confPos) {
		this.confPos = confPos;
	}
	
	public void addListener(JsListenerConf listener) {
		boolean accept = acceptListener(listener);
		if(!accept)
			throw new LfwRuntimeException("��Listener���ǵ�ǰ����Ŀɽ�������," + listener.getClass());
		listenerMap.put(listener.getId(), listener);
	}


	public Map<String, JsListenerConf> getListenerMap() {
		return listenerMap;
	}

	public void removeListener(String key) {
		listenerMap.remove(key);
	}
	
	protected boolean acceptListener(JsListenerConf listener) {
		List<Class<? extends JsListenerConf>> acceptList = getAcceptListeners();
		if(acceptList == null)
			return false;
		Iterator<Class<? extends JsListenerConf>> it = acceptList.iterator();
		while(it.hasNext()){
			Class<? extends JsListenerConf> c = it.next();
			if(c.isAssignableFrom(listener.getClass())){
				return true;
			}
		}
		return false;
	}
	public List<Class<? extends JsListenerConf>> createAcceptListeners(){
		return null;
	}


	public List<Class<? extends JsListenerConf>> getAcceptListeners() {
		if(acceptListenerList == null)
			acceptListenerList = createAcceptListeners();
		return acceptListenerList;
	}

	public void setListenerMap(Map<String, JsListenerConf> listenerMap) {
		this.listenerMap = listenerMap;
	}
	
	public boolean isCtxChanged() {
		return ctxChanged ;
	}

	public void setCtxChanged(boolean changed) {
		this.ctxChanged = changed;
		if (!changed)
			this.clearCtxChangedProperties();
	}
	
	
	public BaseContext getContext() {
		return null;
	}
	
	public void setContext(BaseContext ctx) {
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
	
	public void validate(){
		if(this.getId() == null || this.getId().equals(""))
			throw new  LfwPluginException("ID����Ϊ��");
	}
	
	public List<String> getCtxChangedProperties() {
		return ctxChangedProperties;
	}

	public void setCtxChangedProperties(List<String> ctxChangedProperties) {
		this.ctxChangedProperties = ctxChangedProperties;
	}
	
	/**
	 * �����Ѹı��context����
	 * @param propertyName
	 */
	public void addCtxChangedProperty(String propertyName) {
		this.ctxChangedProperties.add(propertyName);
	}
	
	/**
	 * ����Ѹı��context�����б�
	 */
	public void clearCtxChangedProperties() {
		this.ctxChangedProperties.clear();
	}
	
	/**
	 * �鿴context��ĳ�������Ƿ����仯
	 * @param propertyName
	 * @return
	 */
	public boolean checkCtxPropertyChanged(String propertyName) {
		if (this.ctxChangedProperties.indexOf(propertyName) != -1)
			return true;
		return false;
	}
	
	public void notifyChange(){
		notifyChange(null);
	}
	
	public void notifyChange(String type){
		if(LifeCyclePhase.ajax.equals(getPhase())){
			this.notifyChange(type, null);
//			//����UIElementObServer
//			if (type.equals(UIElement.DELETE) || type.equals(UIElement.ADD) 
//					|| type.equals(UIElement.UPDATE) || type.equals(UIElement.DESTROY)){
//				this.notifyChange(type, null);
//			}
//			else{
//				try{
//					Class c = Class.forName("nc.uap.lfw.core.model.util.ElementObserver");
//					Method m = c.getMethod("notifyChange", new Class[]{String.class, WebElement.class});
//					m.invoke(null, new Object[]{type, this});
//				}
//				//������ڲ�����õ���������޷�������־���ߡ�
//				catch(Throwable e){
//				}
//			}
		}
	}
	
	public void notifyChange(String type, Object obj){
		if(LifeCyclePhase.ajax.equals(getPhase())){
			String className = "nc.uap.lfw.ra.render.observer.UIElementObserver";
			this.invoke(className, type, obj);
//			//����UIElementObServer
//			if (type.equals(UIElement.DELETE) || type.equals(UIElement.ADD) 
//					|| type.equals(UIElement.UPDATE) || type.equals(UIElement.DESTROY)){
//				String className = "nc.uap.lfw.ra.render.observer.UIElementObserver";
//				this.invoke(className, type, obj);
//			}
//			else{
//				try{
//					Class c = Class.forName("nc.uap.lfw.core.model.util.ElementObserver");
//					Method m = c.getMethod("notifyChange", new Class[]{String.class, WebElement.class, Object.class});
//					m.invoke(null, new Object[]{type, this, obj});
//				}
//				//������ڲ�����õ���������޷�������־���ߡ�
//				catch(Throwable e){
//				}
//			}
		}
	}

	/**
	 * @param className
	 * @param type
	 * @param obj
	 * @return
	 */
	private void invoke(String className, String type, Object obj) {
		try {
			IUIElementObserver observer = (IUIElementObserver) LfwClassUtil.newInstance(className);
			observer.notifyChange(type, this, obj);
		}
		// ������ڲ�����õ���������޷�������־���ߡ�
		catch (Throwable e) {
			throw new LfwRuntimeException(e.getMessage());
		}
	}
	
	@Override
	public LifeCyclePhase getPhase() {
		return RequestLifeCycleContext.get().getPhase();
	}

	@Override
	public void addEventConf(EventConf event) {
		if(eventConfList == null)
			eventConfList = new ArrayList<EventConf>();
		eventConfList.add(event);
	}
	
	public void removeEventConf(EventConf event){
		if(eventConfList != null)
			eventConfList.remove(event);
	}
	
	public void removeAllEventConf(){
		if(eventConfList != null){
			eventConfList.clear();
		}
	}

	public List<EventConf> getEventConfList() {
		return eventConfList;
	}

	public void setEventConfList(List<EventConf> eventConfList) {
		this.eventConfList = eventConfList;
	}

	@Override
	public EventConf[] getEventConfs() {
		return eventConfList == null ? null : eventConfList.toArray(new EventConf[0]);
	}

	@Override
	public void removeEventConf(String eventName, String method) {
		if(eventConfList != null){
			Iterator<EventConf> it = eventConfList.iterator();
			while(it.hasNext()){
				EventConf event = it.next();
				if(event.getName().equals(eventName) && event.getMethodName().equals(method)){
					it.remove();
					break;
				}
			}
		}
	}
	
	public EventConf getEventConf(String eventName, String methodName){
		EventConf event = null;
		if(eventConfList != null){
			Iterator<EventConf> it = eventConfList.iterator();
			while(it.hasNext()){
				event = it.next();
				if(event.getName().equals(eventName) && event.getMethodName().equals(methodName)){
					break;
				}
				event = null;
			}
		}
		return event;
	}

	@Override
	public List<JsEventDesc> getAcceptEventDescs() {
		if(acceptEventList == null)
			acceptEventList = createAcceptEventDescs();
		return acceptEventList;
	}

	protected List<JsEventDesc> createAcceptEventDescs() {
		return EventUtil.createAcceptEventDescs(this);
	}

	public String getSourcePackage() {
		return sourcePackage;
	}

	public void setSourcePackage(String sourcePackage) {
		this.sourcePackage = sourcePackage;
	}

	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}
	
}
