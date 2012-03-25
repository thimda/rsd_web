package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.KeyEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;


public class KeyServerListener extends AbstractServerListener {

	public KeyServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}
	public void onKeyDown(KeyEvent e){};
	public void onKeyUp(KeyEvent e){};
	public void onEnter(KeyEvent e){};
}
