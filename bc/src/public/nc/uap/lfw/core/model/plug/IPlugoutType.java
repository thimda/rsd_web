package nc.uap.lfw.core.model.plug;

import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.event.conf.WidgetRule;
import nc.uap.lfw.core.page.PlugoutDescItem;

public interface IPlugoutType {
	/*���ݼ�ѡ����(����)*/
	public static final String TYPE_DATASET_SEL_ROW = "TYPE_DATASET_SEL_ROW";
	/*���ݼ�����ѡ����*/
	public static final String TYPE_DATASET_MUTL_SEL_ROW = "TYPE_DATASET_MUTL_SEL_ROW";
	/*���ݼ�������*/
	public static final String TYPE_DATASET_ALL_ROW = "TYPE_DATASET_ALL_ROW";
	
	/*�ؼ�ֵ*/
//	public static final String TYPE_COMPONENT_VALUE = "TYPE_COMPONENT_VALUE";
	
	public Object fetchContent(PlugoutDescItem item, ViewContext viewCtx);
	
	public void buildSourceWidgetRule(WidgetRule widgetRule, String source);
}
