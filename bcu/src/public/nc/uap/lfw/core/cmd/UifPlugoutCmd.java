package nc.uap.lfw.core.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.WindowContext;
import nc.uap.lfw.core.data.Parameter;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.WidgetRule;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.model.plug.IPlugoutType;
import nc.uap.lfw.core.model.plug.PlugoutTypeFactory;
import nc.uap.lfw.core.model.util.LfwSubmitRuleMergeUtil;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.PlugEventAdjuster;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.page.PlugoutDescItem;

public class UifPlugoutCmd extends UifCommand {
	private String plugoutId;
	private String widgetId;
	private String sourceWindowId = getLifeCycleContext().getWindowContext().getId();
	private Map<String, Object> paramMap;
	
	/**
	 * 同window内的plug调用
	 * 
	 */
	public UifPlugoutCmd(String widgetId, String plugoutId){
		this.widgetId = widgetId;
		this.plugoutId = plugoutId;
	}
	
	/**
	 * 跨window的plug调用
	 * 
	 * @param sourceWindowId
	 * @param widgetId
	 * @param plugoutId
	 */
	public UifPlugoutCmd(String sourceWindowId, String widgetId, String plugoutId){
		this.sourceWindowId = sourceWindowId;
		this.widgetId = widgetId;
		this.plugoutId = plugoutId;
	}
	
	/**
	 * 同window内的plug调用
	 * 
	 */
	public UifPlugoutCmd(String widgetId, String plugoutId, Map<String, Object> paramMap){
		this.widgetId = widgetId;
		this.plugoutId = plugoutId;
		this.paramMap = paramMap;
	}
	
	/**
	 * 跨window的plug调用
	 * 
	 * @param sourceWindowId
	 * @param widgetId
	 * @param plugoutId
	 */
	public UifPlugoutCmd(String sourceWindowId, String widgetId, String plugoutId,  Map<String, Object> paramMap){
		this.sourceWindowId = sourceWindowId;
		this.widgetId = widgetId;
		this.plugoutId = plugoutId;
		this.paramMap = paramMap;
	}
	
	@Override
	public void execute() {
		StringBuffer scriptBuf = new StringBuffer();
		genPlugoutScript(scriptBuf);
		getLifeCycleContext().getApplicationContext().addExecScript(scriptBuf.toString());
	}

	/**
	 * 
	 * 生成前台plugout脚本
	 * @param scriptBuf
	 */
	private void genPlugoutScript(StringBuffer scriptBuf) {
		List<Connector> connList = getConnector();
		if (connList.size() == 0)
			return;
		
		//设置提交规则
		WindowContext windowctx = getLifeCycleContext().getWindowContext();
		PlugoutDesc plugoutDesc = windowctx.getWindow().getWidget(widgetId).getPlugoutDesc(plugoutId);
		EventSubmitRule sr = new EventSubmitRule();
		
		//plugout对应提交规则
		WidgetRule sourceWr = new WidgetRule();				
		sourceWr.setId(widgetId);
		List<PlugoutDescItem> descDescList = plugoutDesc.getDescItemList();
		for (PlugoutDescItem item : descDescList){
			String type = item.getType();
//			type = type.split("\\.")[1];
			String source = item.getSource();
			IPlugoutType plugoutType = PlugoutTypeFactory.getPlugoutType(type);
			if (plugoutType != null)
				plugoutType.buildSourceWidgetRule(sourceWr, source);
		}
		sr.addWidgetRule(sourceWr);

		PlugEventAdjuster plugEventAdjuster = new PlugEventAdjuster();
		//plugin对应提交规则
		for (Connector conn : connList){
			String targetWindowId = conn.getTargetWindow();
			String targetWidgetId = conn.getTarget();
			String pluginId = conn.getPluginId();
			
			PageMeta pageMeta = null;
			//同window内的conn
			if (targetWindowId == null || targetWindowId.equals(this.sourceWindowId)){
				pageMeta = getLifeCycleContext().getWindowContext().getWindow();
				LfwWidget targetWidget = pageMeta.getWidget(targetWidgetId);
				EventSubmitRule targetsubmitRule = plugEventAdjuster.getTargetWidgetSubmitRule(targetWidget, pluginId);
				if (targetsubmitRule != null){
//					sr.addWidgetRule(targetsubmitRule.getWidgetRule(targetWidgetId));
					LfwSubmitRuleMergeUtil.mergeSubmitRule(sr, targetsubmitRule);
				}
			}
			//跨window的conn
			//TODO  目前跨window时，只支持 子window 到  父window的plug调用，即：targetWindow为sourceWindow的父
			else{
//				WindowContext targetWinCtx = getLifeCycleContext().getApplicationContext().getWindowContext(targetWindowId);
//				if (targetWinCtx == null)
//					continue;
//				pageMeta = targetWinCtx.getWindow();
				pageMeta = LfwRuntimeEnvironment.getWebContext().getCrossPageMeta(targetWindowId);
				if (pageMeta == null)
					continue;
				LfwWidget targetWidget = pageMeta.getWidget(targetWidgetId);
				EventSubmitRule targetsubmitRule = plugEventAdjuster.getTargetWidgetSubmitRule(targetWidget, pluginId);
				if (targetsubmitRule == null){
					targetsubmitRule = new EventSubmitRule();
					WidgetRule wr = new WidgetRule();
					wr.setId(targetWidgetId);
					targetsubmitRule.addWidgetRule(wr);
				}
				if (sr.getParentSubmitRule() == null)
					sr.setParentSubmitRule(targetsubmitRule);
				else
					LfwSubmitRuleMergeUtil.mergeSubmitRule(sr.getParentSubmitRule(), targetsubmitRule);
			}
		}
		
		//增加参数
		sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_SIGN,"1"));
		sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_SOURCE_WINDOW,sourceWindowId));
		sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_SOURCE,widgetId));
		sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_ID,plugoutId));
		sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_FROMSERVER,"1"));
		sr.addParam(new Parameter(LfwPageContext.WIDGET_ID,widgetId));

		//生成前台脚本
		scriptBuf.append(plugEventAdjuster.generateSubmitRule(sr));
//		
//		{"key":"value", "key":"value"}
//		key : "key"
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("{");
//		if (this.paramMap != null){
//			Iterator<String> it =  this.paramMap.keySet().iterator();
//			while (it.hasNext()){
//				String key = it.next();
//				Object value = this.paramMap.get(key);
//				paramBuff.append(key).append(":").append(value).append(",");
//			}
//		}
		
		windowctx.addPlug(widgetId + "_" + plugoutId, this.paramMap);
		
		String values = paramBuff.toString();
		if (values.endsWith(","))
			values = values.substring(0, values.length() - 1);
		values += "}";
		
//		paramBuff.append("}'");
		
		scriptBuf.append("triggerPlugout(submitRule," + values + ");");
	}

	/**
	 * 获取plug连接
	 * 
	 * @return
	 */
	private List<Connector> getConnector() {
		WindowContext windowctx = getLifeCycleContext().getWindowContext();
		List<Connector> connList = new ArrayList<Connector>();
		//取window内的conn
		Connector[] conns = windowctx.getWindow().getConnectors();
		for (int i = 0 ; i < conns.length; i ++){
			if (conns[i].getSource().equals(this.widgetId) && conns[i].getPlugoutId().equals(this.plugoutId)){
				connList.add(conns[i]); 
			}
		}
		//sourceWindowId 为null或者为当前window的id，只从当前window内取conn
//		if (this.sourceWindowId == null || this.sourceWindowId.equals(getLifeCycleContext().getWindowContext().getId()))
//			return connList;
		//取app上的conn
		List<Connector> appConnList = getLifeCycleContext().getApplicationContext().getApplication().getConnectorList();
		if (appConnList == null)
			return connList;
		for (Connector c : appConnList){
			if (c.getSourceWindow().equals(this.sourceWindowId) && c.getSource().equals(this.widgetId) && c.getPlugoutId().equals(this.plugoutId)){
				connList.add(c);
			}
		}
		return connList;
	}

}
