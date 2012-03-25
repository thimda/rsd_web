package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.event.PageEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

public abstract class PageServerListener extends AbstractServerListener {

	public PageServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}
	
	public void afterPageInit(PageEvent e){
		//正常情况，设置非login页面标识
		LfwRuntimeEnvironment.getWebContext().getRequest().getSession().setAttribute(WebContext.LOGIN_DID, "1");
	}
	public void beforeActive(PageEvent e){}
	public void onClosing(PageEvent e){}
	public void onClosed(PageEvent e){
		LfwRuntimeEnvironment.getWebContext().destroyWebSession();
	}
	
}
