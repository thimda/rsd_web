package nc.uap.lfw.core.model.plug;

import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.event.conf.WidgetRule;
import nc.uap.lfw.core.page.PlugoutDescItem;

public interface IPlugoutType {
	/*数据集选中行(单行)*/
	public static final String TYPE_DATASET_SEL_ROW = "TYPE_DATASET_SEL_ROW";
	/*数据集所有选中行*/
	public static final String TYPE_DATASET_MUTL_SEL_ROW = "TYPE_DATASET_MUTL_SEL_ROW";
	/*数据集所有行*/
	public static final String TYPE_DATASET_ALL_ROW = "TYPE_DATASET_ALL_ROW";
	
	/*控件值*/
//	public static final String TYPE_COMPONENT_VALUE = "TYPE_COMPONENT_VALUE";
	
	public Object fetchContent(PlugoutDescItem item, ViewContext viewCtx);
	
	public void buildSourceWidgetRule(WidgetRule widgetRule, String source);
}
