package nc.uap.lfw.design.impl;

import nc.uap.lfw.core.model.util.DefaultWidgetBuilder;
import nc.uap.lfw.core.page.LfwWidget;

/***
 * �������е�widget
 * @author zhangxya
 *
 */
public class RefWidgetBuilderForDesign extends DefaultWidgetBuilder {

	String PROJECT_PATH_KEY = "PROJECT_PATH_KEY";
	/**
	 * ��ȡ���õĹ���ds
	 * @param dsId
	 * @param paramMap 
	 * @return
	 */
	protected LfwWidget getRefWidget(String widgetId){
		return super.fetchWidgetFromPool(widgetId);
	}
}
