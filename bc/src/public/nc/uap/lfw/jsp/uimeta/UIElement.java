package nc.uap.lfw.jsp.uimeta;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.base.ExtendAttributeSupport;
import nc.uap.lfw.core.event.EventUtil;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.ILifeCycleSupport;
import nc.uap.lfw.core.page.LifeCyclePhase;
import nc.uap.lfw.core.page.RequestLifeCycleContext;
import nc.uap.lfw.render.observer.IUIElementObserver;
import nc.uap.lfw.util.LfwClassUtil;
public class UIElement extends ExtendAttributeSupport implements IEventSupport, Serializable, ILifeCycleSupport, Cloneable {
	private static final long serialVersionUID = 1083409119485229582L;
	private Map<String, Serializable> attrMap = null;
	public static final String ID = "id";
	public static final String WIDGET_ID = "widgetId";
	public static final String CLASSNAME = "className";
	public static final String DELETE = "delete";
	public static final String ADD = "add";
	public static final String UPDATE = "update";
	public static final String CREATE = "create";
	public static final String DESTROY = "destroy";
	public static final String VISIBLE = "visible";
	private List<JsEventDesc> acceptEventList = null;
	private List<EventConf> eventConfList = null;
	public void setId(String id) {
		String oriId = getId();
		if(oriId == null || !oriId.equals(id)){
			setAttribute(ID, id);
			UpdatePair pair = new UpdatePair(ID, id);
			pair.setOldValue(oriId);
			notifyChange(UPDATE, pair);
		}
	}
	public String getId() {
		return (String) getAttribute(ID);
	}
	public String getWidgetId() {
		return (String) getAttribute(WIDGET_ID);
	}
	public void setWidgetId(String widgetId) {
		setAttribute(WIDGET_ID, widgetId);
	}
	public void setVisible(boolean visibility) {
		boolean oriVisible = getVisible();
		if(oriVisible != visibility){
			setAttribute(VISIBLE, visibility);
			notifyChange(UPDATE, VISIBLE);
		}
	}
	public boolean getVisible() {
		return getAttribute(VISIBLE) == null ? true : (Boolean) getAttribute(VISIBLE);
	}
	public String getClassName() {
		return (String) getAttribute(CLASSNAME);
	}
	public void setClassName(String className) {
		String oriCn = getClassName();
		if(!className.equals(oriCn)){
			setAttribute(CLASSNAME, className);
			UpdatePair up = new UpdatePair(CLASSNAME, className);
			notifyChange(UPDATE, up);
		}
	}
	public void setAttribute(String key, Serializable value) {
		getAttrMap().put(key, value);
	}
	public Serializable getAttribute(String key) {
		return getAttrMap().get(key);
	}
	protected Map<String, Serializable> getAttrMap() {
		if (attrMap == null) {
			attrMap = createAttrMap();
		}
		return attrMap;
	}
	protected Map<String, Serializable> createAttrMap() {
		return new HashMap<String, Serializable>();
	}
	@Override public LifeCyclePhase getPhase() {
		return RequestLifeCycleContext.get().getPhase();
	}
	/**
	 * �ı�ʱ����
	 * 
	 * @return
	 */
	protected void notifyChange() {
		notifyChange(null);
	}
	public void notifyChange(String type) {
		this.notifyChange(type, null);
	}
	public void notifyChange(String type, Object obj) {
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			String className = "nc.uap.lfw.ra.render.observer.UIElementObserver";
			this.invoke(className, type, obj);
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
	/**
	 * @param ele
	 *            �����Ԫ��
	 */
	protected void addElement(UIElement ele) {
		if (ele != null && LifeCyclePhase.ajax.equals(getPhase())) {
			this.notifyChange(ADD, ele);
		}
	}
	/**
	 * @param ele
	 *            ɾ����Ԫ��
	 */
	protected void removeElement(UIElement ele) {
		if (ele != null && LifeCyclePhase.ajax.equals(getPhase())) {
			this.notifyChange(DELETE, ele);
		}
	}
	/**
	 * �������������
	 */
	public void updateSelf() {
		this.notifyChange(UPDATE);
	}
	public Object doClone() {
		UIElement ele = (UIElement) this.clone();
		if (this.attrMap != null) {
			ele.attrMap = ele.createAttrMap();
			Iterator<String> keys = this.attrMap.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				Serializable s = this.attrMap.get(key);
				ele.attrMap.put(key, s);
			}
		}
		return ele;
	}
	@Override public void addEventConf(EventConf event) {
		if (eventConfList == null)
			eventConfList = new ArrayList<EventConf>();
		eventConfList.add(event);
	}
	@Override public List<JsEventDesc> getAcceptEventDescs() {
		if (acceptEventList == null)
			acceptEventList = createAcceptEventDescs();
		return acceptEventList;
	}
	protected List<JsEventDesc> createAcceptEventDescs() {
		return EventUtil.createAcceptEventDescs(this);
	}
	@Override public EventConf[] getEventConfs() {
		return eventConfList == null ? null : eventConfList.toArray(new EventConf[0]);
	}
	@Override public void removeEventConf(String eventName, String method) {
		if (eventConfList != null) {
			Iterator<EventConf> it = eventConfList.iterator();
			while (it.hasNext()) {
				EventConf event = it.next();
				if (event.getName().equals(eventName) && event.getMethodName().equals(method)) {
					it.remove();
					break;
				}
			}
		}
	}
	public void removeAllEventConf() {
		if (eventConfList != null) {
			eventConfList.clear();
		}
	}
	
	/**
	 * ���ҵ�ǰԪ���µ���Ԫ�ء�ע�⣺������Ԫ�ر����Ƕ���Ԫ�أ����粼�ֻ��߿ؼ�������Panel��μ�2�������ķ���
	 * @param id ����Ԫ��ID
	 * @return
	 */
	public UIElement findChildById(String id){
		return UIElementFinder.findElementById(this, id);
	}
	
	/**
	 * ���ҵ�ǰԪ���µ���Ԫ��
	 * @param pid ��Ԫ�ص�����Ԫ��ID������ UIFlowvLayout->pid    UIFlowvPanel->id
	 * @param id ��Ԫ��ID
	 * @return
	 */
	public UIElement findChildById(String pid, String id){
		return UIElementFinder.findElementById(this, pid, id);
	}
}
