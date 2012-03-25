package nc.uap.lfw.core.uif.delegator;

import java.util.Properties;

import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;

/**
 * �����ѯpagemodel
 * @author zhangxya
 *
 */
public class UifQueryPageModel extends PageModel {

	@Override
	protected void initPageMetaStruct() {
//		PageService pageService = new PageService();
//		pageService.setName("favorite_service");
//		pageService.setClassName("nc.uap.lfw.ncadapter.qrytemplate.LfwQueryTemplateServiceViaJson");
//		getPageMeta().addService(pageService);
		
		Properties props = this.getProps(); 
		if(props == null)
			return;
		String queryOkClass = props.getProperty("QUERY_OK_LISTENER");
		if(queryOkClass == null || "".equals(queryOkClass))
			return;
		PageMeta pageMeta = getPageMeta();
		LfwWidget mainWidget = pageMeta.getWidget("template");
		ButtonComp okButton = (ButtonComp) mainWidget.getViewComponents().getComponent("okBt");
		//ȷ����ť��listener
		MouseListener mouseListener = (MouseListener) okButton.getListenerMap().get("okListener");
		mouseListener.setServerClazz(queryOkClass);
	}
}
