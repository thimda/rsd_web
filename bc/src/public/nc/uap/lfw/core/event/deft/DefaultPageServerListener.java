package nc.uap.lfw.core.event.deft;

import nc.uap.lfw.core.event.PageEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.PageServerListener;

public class DefaultPageServerListener extends PageServerListener {

	public DefaultPageServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}
	
	@Override
	public void afterPageInit(PageEvent e) {
		super.afterPageInit(e);
	}

	@Override
	public void onClosed(PageEvent e) {
		super.onClosed(e);
	}

	@Override
	public void onClosing(PageEvent e) {
		
	}

	@Override
	public void beforeActive(PageEvent e) {
		
	}
}
