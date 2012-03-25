package nc.uap.lfw.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.exception.LfwRuntimeException;

public class ViewComponents implements Cloneable, Serializable{
	
	private static final long serialVersionUID = 6273467687662465677L;
	private Map<String, WebComponent> compMap;
	private LfwWidget widget;
	
	public void setCompMap(Map<String, WebComponent> compMap) {
		this.compMap = compMap;
	}
	
	public ViewComponents()
	{
		compMap = new HashMap<String, WebComponent>();
	}
	
	public ViewComponents(LfwWidget widget) {
		this();
		this.widget = widget;
	}
	
	public WebComponent getComponent(String id) {
		return compMap.get(id);
	}
	
	public void removeComponent(String id) {
		compMap.remove(id);
	}
	
	/**
	 * 获取指定Class类型的控件
	 * @param c Class
	 * @return
	 */
	public WebComponent[] getComponentByType(Class<? extends WebComponent> c) {
		Iterator<WebComponent> it = compMap.values().iterator();
		List<WebComponent> list = new ArrayList<WebComponent>();
		while(it.hasNext()){
			WebComponent comp = it.next();
			if(c.isAssignableFrom(comp.getClass())){
				list.add(comp);
			}
		}
		return list.toArray(new WebComponent[list.size()]);
	}
	
	public Map<String, WebComponent> getComponentsMap(){
		return compMap;
	}
	
	public WebComponent[] getComponents()
	{
		return compMap.values().toArray(new WebComponent[0]);
	}
	
	public void addComponent(WebComponent component)
	{
		component.setWidget(this.widget);
		compMap.put(component.getId(), component);
	}
	
//	public ComboData getComboDataById(String id)
//	{
//		return this.comboDataMap.get(id);
//	}
//	
//	public void addComboData(ComboData comboData)
//	{
//		comboDataMap.put(comboData.getId(), comboData);
//	}
//
//	public Map<String, ComboData> getComboDataMap() {
//		return comboDataMap;
//	}
	
//	public void addRefNode(RefNode refNode)
//	{
//		refNodeMap.put(refNode.getId(), refNode);
//	}
//	
//	public RefNode getRefNodeById(String id)
//	{
//		return refNodeMap.get(id);
//	}
//	
//	public RefNode[] getRefNodes()
//	{
//		return refNodeMap.values().toArray(new RefNode[0]);
//	}
//	
//	public Map<String, RefNode> getRefNodeMap()
//	{
//		return refNodeMap;
//	}
	
	public void merge(ViewComponents viewComps)
	{
		compMap.putAll(viewComps.getComponentsMap());
//		comboDataMap.putAll(viewComps.getComboDataMap());
//		refNodeMap.putAll(viewComps.getRefNodeMap());
	}
	/**
	 * lkp add 20070402实现对clone的支持。
	 */
	public Object clone()
	{
		try {
			ViewComponents components = (ViewComponents)super.clone();
			//components.comboDataMap = new HashMap<String, ComboData>();
			//components.refNodeMap = new HashMap<String, RefNode>();
			components.compMap = new HashMap<String, WebComponent>();
			
			//ComboData data = null;
			WebComponent component = null;
			//RefNode refNode = null;
			
//			if(this.comboDataMap != null)
//			{
//				for(Iterator<ComboData> iter = this.comboDataMap.values().iterator(); iter.hasNext();)
//				{
//					data = iter.next();
//					components.comboDataMap.put(data.getId(), (ComboData)data.clone());
//				}
//			}
			
			if(this.compMap != null)
			{
				for(Iterator<WebComponent> iter = this.compMap.values().iterator(); iter.hasNext();)
				{
					component = iter.next();
					components.compMap.put(component.getId(), (WebComponent)component.clone());
				}
			}
			
//			if(this.refNodeMap != null)
//			{
//				for(Iterator<RefNode> iter = this.refNodeMap.values().iterator(); iter.hasNext();)
//				{
//					refNode = iter.next();
//					components.refNodeMap.put(refNode.getId(), (RefNode)refNode.clone());
//				}				
//			}

			
			return components;
		} 
		catch (CloneNotSupportedException e) {
			throw new LfwRuntimeException(e.getMessage(), e);
		}
	}

	public void setWidget(LfwWidget lfwWidget) {
		for(Iterator<WebComponent> iter = this.compMap.values().iterator(); iter.hasNext();){
			WebComponent comp = iter.next();
			comp.setWidget(lfwWidget);
		}
	}

	public LfwWidget getWidget() {
		return widget;
	}
	
}
