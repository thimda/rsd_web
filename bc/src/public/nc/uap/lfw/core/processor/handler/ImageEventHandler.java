package nc.uap.lfw.core.processor.handler;

import nc.uap.lfw.core.comp.ImageComp;
import nc.uap.lfw.core.event.AbstractServerEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.processor.AbstractEventHandler;

/**
 * @author guoweic
 *
 */
public class ImageEventHandler extends AbstractEventHandler<ImageComp> {

	public ImageEventHandler(LfwPageContext pageCtx) {
		super(pageCtx);
	}

	protected ImageComp getSource() {
		String sourceId = getPageCtx().getParameter(LfwPageContext.SOURCE_ID);
		ImageComp img = (ImageComp) getPageCtx().getPageMeta().getWidget(getWidgetId()).getViewComponents().getComponent(sourceId);
		return img;
	}
	
	protected AbstractServerEvent getServerEvent(String eventName, ImageComp img) {
		JsListenerConf listener = getListenerConf();
		if(listener instanceof MouseListener){
			AbstractServerEvent event = new MouseEvent(img);
			return event;
		}
		else
			throw new LfwRuntimeException("not implemented");
	}

}
