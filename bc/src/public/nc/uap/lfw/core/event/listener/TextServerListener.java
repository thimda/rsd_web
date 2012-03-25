package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.TextEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * @author guoweic
 *
 */
public abstract class TextServerListener extends AbstractServerListener {

	public TextServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public void onselect(TextEvent e) {};
	
	public void valueChanged(TextEvent e){};
}
