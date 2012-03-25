package nc.uap.lfw.design.impl;

import nc.uap.lfw.core.model.util.DefaultWidgetBuilder;
import nc.uap.lfw.core.page.LfwWidget;

/***
 * 公共池中的widget
 * @author zhangxya
 *
 */
public class RefWidgetBuilderForDesign extends DefaultWidgetBuilder {

	String PROJECT_PATH_KEY = "PROJECT_PATH_KEY";
	/**
	 * 获取引用的公共ds
	 * @param dsId
	 * @param paramMap 
	 * @return
	 */
	protected LfwWidget getRefWidget(String widgetId){
		return super.fetchWidgetFromPool(widgetId);
	}
}
