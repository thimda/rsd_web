package nc.uap.lfw.core.page;

import nc.uap.lfw.core.event.conf.EventHandlerConf;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.PageListener;
import nc.uap.lfw.core.event.deft.DefaultPageServerListener;
import nc.uap.lfw.core.model.IRuntimeAdjuster;

public class PageTypeRuntimeAdjuster implements IRuntimeAdjuster {

	@Override
	public void adjust(PageMeta pageMeta) {
		
		PlugEventAdjuster plugEventAdjuster = new PlugEventAdjuster(); 
		plugEventAdjuster.addPlugEventConf(pageMeta);
		
		JsListenerConf[] liss = pageMeta.getListenerMap().values().toArray(new JsListenerConf[0]);
		boolean findClose = false;
		for (int i = 0; i < liss.length; i++) {
			EventHandlerConf hconf = liss[i].getEventHandler(PageListener.ON_CLOSED);
			if(hconf != null && hconf.isOnserver()){
				findClose = true;
				break;
			}
		}
		
		if(!findClose){
			PageListener pl = new PageListener();
			pl.setId("sys_add_listener");
			pl.setServerClazz(DefaultPageServerListener.class.getName());
			EventHandlerConf ec = PageListener.getOnClosedEvent();
			ec.setOnserver(true);
			pl.addEventHandler(ec);
			pageMeta.addListener(pl);
		}
	}
	
	
}
