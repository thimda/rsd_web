package nc.uap.lfw.core.event;

import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ApplicationContext;
import nc.uap.lfw.core.model.IRuntimeAdjuster;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.PlugEventAdjuster;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.SelfDefRefNode;

/**
 * 插件运行时调整类
 *
 */
public final class AppTypeRuntimeAdjuster implements IRuntimeAdjuster{
	public void adjust(PageMeta pageMeta) {
		PlugEventAdjuster plugEventAdjuster = new PlugEventAdjuster();
		ApplicationContext appCtx = AppLifeCycleContext.current().getApplicationContext();
		plugEventAdjuster.addAppPlugEventConf(appCtx, pageMeta);
		plugEventAdjuster.addPlugEventConf(pageMeta);
		adjustRefNode(pageMeta);
	}
	
	private void adjustRefNode(PageMeta pageMeta) {
		LfwWidget[] widgets = pageMeta.getWidgets();
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		String appId = ctx.getApplicationContext().getAppId();
		for (int i = 0; i < widgets.length; i++) {
			IRefNode[] refnodes = widgets[i].getViewModels().getRefNodes();
			for (int j = 0; j < refnodes.length; j++) {
				refnodes[j].setWindowType(PageMeta.WIN_TYPE_APP);
				if(refnodes[j] instanceof SelfDefRefNode){
					SelfDefRefNode selfRef = (SelfDefRefNode) refnodes[j];
					String path = selfRef.getPath();
					path = "app/" + appId + "/" + path;
					selfRef.setPath(path);
				}
			}
		}
	}

}
