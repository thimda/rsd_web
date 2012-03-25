package nc.uap.lfw.reference.action;

import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.MouseServerListener;
import nc.uap.lfw.core.uif.delegator.IUifDeletator;

public class RefCancelButtonMouseListener extends MouseServerListener {
	public RefCancelButtonMouseListener(LfwPageContext context, String widgetId) {
		super(context, widgetId);
	}

	@Override
	public void onclick(MouseEvent e) {
		IUifDeletator delegator = new RefCancelDelegator();
		delegator.setGlobalContext(getGlobalContext());
		delegator.execute();
	}
}
