package nc.uap.lfw.core.page;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.event.conf.EventHandlerConf;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.core.refnode.RefNodeRelations;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
/**
 * 该类如果信息完整后，其实来源于两个配置文件：
 * XxxViewModel.xml 和 XxxViewModelEvent.xml
 * @author zhouhai
 * @update lkp 20070402 增加clone支持
 *         lkp 20070403 修改该类结构，去掉对ViewModelDatasetConf的存储。
 *
 */
public class ViewModels implements /*IEventHandlerSupport,*/ Cloneable, Serializable {

	private static final long serialVersionUID = 3628459738668095893L;
	private DatasetRelations dsrelations;
	private RefNodeRelations refnodeRelations;
	private Map<String, Dataset> datasetMap = new HashMap<String, Dataset>();
	private Map<String, IRefNode> refNodeMap = new HashMap<String, IRefNode>(); 
	private Map<String,EventHandlerConf> hsEventHandler = new HashMap<String, EventHandlerConf>();
	private LfwWidget widget;
	//dengjt add 页面数据类配置
//	private Map<String, PageData> pageDataMap = new HashMap<String, PageData>();
	
	private Map<String, ComboData> comboDataMap = new HashMap<String, ComboData>();
	public ViewModels() {
	}
	
	public ViewModels(LfwWidget widget) {
		super();
		this.widget = widget;
	}

	public DatasetRelations getDsrelations() {
		return dsrelations;
	}

	public void setDsrelations(DatasetRelations dsrelations) {
		dsrelations.setWidget(this.widget);
		this.dsrelations = dsrelations;
	}
	
	public Dataset getDataset(String id)
	{
		return datasetMap.get(id);
	}
	
	public void addViewModelDataset(Dataset ds) {
		if (ds == null)
			return;
		datasetMap.put(ds.getId(), ds);
	}
	
	public Dataset[] getDatasets()
	{
		return datasetMap.values().toArray(new Dataset[0]);
	}
	
	/**
	 * @param className
	 * @param type
	 * @param obj
	 * @return
	 */
//	protected Object invoke(String className,String type,Object obj){
//		try{
//			Class c = Class.forName(className);
//			Object instance = c.newInstance();
//			Method m = c.getMethod("notifyChange", new Class[]{String.class, UIElement.class, Object.class});
//			return m.invoke(instance, new Object[]{type, this, obj});
//		}
//		//因此类在插件中用到，插件中无法包含日志工具。
//		catch(Throwable e){
//			throw new LfwRuntimeException(e.getMessage());
//		}
//	}

	
	public LifeCyclePhase getPhase() {
		return RequestLifeCycleContext.get().getPhase();
	}
	
//	public Object notifyChange(Object obj){
//		
//		if(LifeCyclePhase.ajax.equals(getPhase())){			
//			try{
//				String widgetId = this.getWidget().getId();
//				UIMeta uiMeta = (UIMeta) LfwRuntimeEnvironment.getWebContext().getUIMeta();
//				UIElement widget = null;//uiMeta.findChildById(widgetId);
//				Class c = Class.forName("nc.uap.lfw.ra.render.observer.UIWidgetObserver");
//				Object instance = c.newInstance();
//				Method m = c.getMethod("notifyMethod", new Class[]{String.class, UIElement.class, Object.class});
//				if(obj instanceof ComboData){
//					return m.invoke(instance, new Object[]{"notifyAddComboDataScript", widget, obj});
//				}else if(obj instanceof RefNode){
//					return m.invoke(instance, new Object[]{"notifyAddRefNode", widget, obj});
//				}
//				
//			}
//			//因此类在插件中用到，插件中无法包含日志工具。
//			catch(Throwable e){
//				throw new LfwRuntimeException(e.getMessage());
//			}
//		}
//		
//		return null;
//	}

	
	public Object clone()
	{
		try {
			ViewModels viewModel = (ViewModels)super.clone();
			if(this.dsrelations != null)
			  viewModel.dsrelations = (DatasetRelations)this.dsrelations.clone();
			viewModel.datasetMap = new HashMap<String, Dataset>();
			if(this.datasetMap != null)
			{
				Dataset ds;
				for(Iterator<Dataset> iter = this.datasetMap.values().iterator(); iter.hasNext();)
				{
					ds = (Dataset)iter.next().clone();
					viewModel.datasetMap.put(ds.getId(), ds);
				}
			}
				
			viewModel.hsEventHandler = new HashMap<String, EventHandlerConf>();
			EventHandlerConf handler;
			if(this.hsEventHandler != null)
			{
				for(Iterator<EventHandlerConf> iter = this.hsEventHandler.values().iterator(); iter.hasNext();)
				{
					handler = iter.next();
					viewModel.hsEventHandler.put(handler.getName(), (EventHandlerConf)handler.clone());
				}
			}
			
//			viewModel.pageDataMap = new HashMap<String, PageData>();
//			if(this.pageDataMap != null){
//				Iterator<PageData> it = this.pageDataMap.values().iterator();
//				while(it.hasNext()){
//					PageData pd = it.next();
//					viewModel.pageDataMap.put(pd.getId(), (PageData) pd.clone());
//				}
//			}
			
			viewModel.comboDataMap = new HashMap<String, ComboData>();
			if(this.comboDataMap != null)
			{
				for(Iterator<ComboData> iter = this.comboDataMap.values().iterator(); iter.hasNext();)
				{
					ComboData data = iter.next();
					viewModel.comboDataMap.put(data.getId(), (ComboData)data.clone());
				}
			}
			
			viewModel.refNodeMap = new HashMap<String, IRefNode>();
			if(this.refNodeMap != null)
			{
				for(Iterator<IRefNode> iter = this.refNodeMap.values().iterator(); iter.hasNext();)
				{
					IRefNode refNode = iter.next();
					viewModel.refNodeMap.put(refNode.getId(), (IRefNode) refNode.clone());
				}				
			}
			
			return viewModel;
		} catch (CloneNotSupportedException e) {
			throw new LfwRuntimeException(e.getMessage(), e);
		}
	}

	public void addDataset(Dataset ds)
	{
		ds.setWidget(this.widget);
		this.datasetMap.put(ds.getId(), ds);
	}


	public void merge(ViewModels viewModel) {
		this.datasetMap.putAll(viewModel.datasetMap);
		this.dsrelations.addDsRelations(viewModel.getDsrelations());
		
		this.comboDataMap.putAll(viewModel.comboDataMap);
		this.refNodeMap.putAll(viewModel.refNodeMap);
	}

	public RefNodeRelations getRefNodeRelations() {
		return refnodeRelations;
	}


	public void setRefnodeRelations(RefNodeRelations refnodeRelations) {
		this.refnodeRelations = refnodeRelations;
	}
	
	public ComboData getComboData(String id)
	{
		return this.comboDataMap.get(id);
	}
	
	public ComboData[] getComboDatas() {
		return this.comboDataMap.values().toArray(new ComboData[0]);
	}
	
	public void addComboData(ComboData comboData)
	{
		comboData.setWidget(this.widget);
		comboDataMap.put(comboData.getId(), comboData);
		this.notify("notifyAddComboDataScript", comboData);
	}
	
	public void addRefNode(IRefNode refNode)
	{
		refNode.setWidget(this.widget);
		refNodeMap.put(refNode.getId(), refNode);
		this.notify("notifyAddRefNode", refNode);
	}
	
	public IRefNode getRefNode(String id)
	{
		return refNodeMap.get(id);
	}
	
	public IRefNode[] getRefNodes()
	{
		return refNodeMap.values().toArray(new IRefNode[0]);
	}

	public void addDatasets(Map<String, Dataset> refDsMap) {
		this.datasetMap.putAll(refDsMap);
	}

	public void removeDataset(String id) {
		this.datasetMap.remove(id);
	}
	
	public void removeComboData(String id){
		this.comboDataMap.remove(id);
	}

	public void removeRefNode(String id) {
		this.refNodeMap.remove(id);
	}

	public LfwWidget getWidget() {
		return widget;
	}

	public void setWidget(LfwWidget widget) {
		this.widget = widget;
		IRefNode[] refnodes = getRefNodes();
		for (int i = 0; i < refnodes.length; i++) {
			refnodes[i].setWidget(widget);
		}
		
		Dataset[] dss = getDatasets();
		for (int i = 0; i < dss.length; i++) {
			dss[i].setWidget(widget);
		}
		
		ComboData[] combs = getComboDatas();
		for (int i = 0; i < combs.length; i++) {
			combs[i].setWidget(widget);
		}
	}
	
	private void notify(String type, Object obj){
		if(LifeCyclePhase.ajax.equals(getPhase())){			
			Map<String,Object> map = new HashMap<String,Object>();
			String widgetId = this.getWidget().getId();
			map.put("widgetId", widgetId);
			map.put("type", type);
			if (type.equals("notifyAddRefNode"))
				map.put("iRefNode", obj);
			else if (type.equals("notifyAddComboDataScript"))
				map.put("comboData", obj);
				
			this.getWidget().notifyChange(UIElement.UPDATE, map);
		}
	}
	
}
