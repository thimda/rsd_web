package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.SelfDefEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * @author guoweic
 *
 */
public class SelfDefServerListener<SelfDefComp> extends AbstractServerListener {
	
	public SelfDefServerListener(LfwPageContext context, String widgetId) {
		super(context, widgetId);
	}
	
	public void onSelfDefEvent(SelfDefEvent<SelfDefComp> e){
		throw new LfwRuntimeException("onSelfDefEvent 方法没有被重写");
	};
	

}
