package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.FocusEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;


/**
 * ¾Û½¹½Úµãlistener
 * @author zhangxya
 *
 */
public class FocusServerListener extends AbstractServerListener {

	public FocusServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}
	
	public void onFocus(FocusEvent e){};
	public void onBlur(FocusEvent e){};
	
}