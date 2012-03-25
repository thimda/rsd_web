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
 * 元素基类，所有控件,dataset或者子元素都派生自此类
 * TODO 需要将事件描述外置于单独的描述类中
 * @author dengjt
 *
 */
public abstract class WebElement extends ExtendAttributeSupport implements IListenerSupport,IEventSupport,Serializable,Cloneable, ILifeCycleSupport{

	public static final String CONF_ADD = "add";
	public static final String CONF_REF = "ref";
	public static final String CONF_DEL = "del";
	
	private static final long serialVersionUID = -8330041983034564169L;
	
	//组件唯一标识
	private String id;
	//标识是否被渲染过
	private boolean rendered = false;
	
	//用于处于附加配置环境的文件，比如针对NC模板，由配置文件补充配置时的类型。如果是ADD，表示配置元素为新加。如果为DEL，表示删除原模板中对应的元素。如果是REF，表示是通过此配置更改原有配置。
	private String confType = CONF_ADD;
	//用于处于附加配置环境的文件，指明某些对位置敏感的元素具体插入的位置。-1标识追加到尾部。从0开始
	private int confPos = -1;
	
	private Map<String, JsListenerConf> listenerMap = new HashMap<String, JsListenerConf>();
	
	private List<Class<? extends JsListenerConf>> acceptListenerList = null;
	
	private List<JsEventDesc> acceptEventList = null;
	
	private List<EventConf> eventConfList = null;
	
	// context是否改变
	private boolean ctxChanged = false;
	// 已改变的context属性列表
	private List<String> ctxChangedProperties = new ArrayList<String>();

	private String from;
	
	private String sourcePackage;
	//真实路径
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
	 * 将传入的参数合并至当前控件。
	 * 要求传入的参数是当前参数的子类或相同
	 * 对传入参数不为空的值，一律以传入参数值为准
	 * @param ele
	 */
	public void mergeProperties(WebElement ele){
		if(ele == null)
			return;
		if(!ele.getClass().isAssignableFrom(this.getClass())){
			throw new LfwRuntimeException("指定的元素并非来自同一继承体系");
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
			throw new LfwRuntimeException("此Listener不是当前组件的可接受类型," + listener.getClass());
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
			throw new  LfwPluginException("ID不能为空");
	}
	
	public List<String> getCtxChangedProperties() {
		return ctxChangedProperties;
	}

	public void setCtxChangedProperties(List<String> ctxChangedProperties) {
		this.ctxChangedProperties = ctxChangedProperties;
	}
	
	/**
	 * 增加已改变的context属性
	 * @param propertyName
	 */
	public void addCtxChangedProperty(String propertyName) {
		this.ctxChangedProperties.add(propertyName);
	}
	
	/**
	 * 清空已改变的context属性列表
	 */
	public void clearCtxChangedProperties() {
		this.ctxChangedProperties.clear();
	}
	
	/**
	 * 查看context的某个属性是否发生变化
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
//			//调用UIElementObServer
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
//				//因此类在插件中用到，插件中无法包含日志工具。
//				catch(Throwable e){
//				}
//			}
		}
	}
	
	public void notifyChange(String type, Object obj){
		if(LifeCyclePhase.ajax.equals(getPhase())){
			String className = "nc.uap.lfw.ra.render.observer.UIElementObserver";
			this.invoke(className, type, obj);
//			//调用UIElementObServer
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
//				//因此类在插件中用到，插件中无法包含日志工具。
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
		// 因此类在插件中用到，插件中无法包含日志工具。
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
