package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class DialogServerListener extends AbstractServerListener {

	public DialogServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public void onOk(DialogEvent e){};
	public void onCancel(DialogEvent e){};
	public void beforeShow(DialogEvent e){};
	public void beforeClose(DialogEvent e){};
	public void afterClose(DialogEvent e){};
	public void onclose(DialogEvent e){};
	
}
