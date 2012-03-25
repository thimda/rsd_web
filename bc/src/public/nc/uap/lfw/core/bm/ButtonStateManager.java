package nc.uap.lfw.core.bm;

import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.util.LfwClassUtil;

public final class ButtonStateManager {
	public static void updateButtons() {
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		ViewContext viewCtx = ctx.getApplicationContext().getCurrentWindowContext().getCurrentViewContext();
		WebComponent[] mbs = viewCtx.getView().getViewComponents().getComponentByType(MenubarComp.class);
		if(mbs == null || mbs.length == 0){
			mbs = viewCtx.getView().getViewMenus().getMenuBars();
		}
		
		if(mbs != null && mbs.length > 0){
			for (int i = 0; i < mbs.length; i++) {
				MenubarComp mb = (MenubarComp) mbs[i];
				List<MenuItem> items = mb.getMenuList();
				if(items != null){
					Iterator<MenuItem> it = items.iterator();
					while(it.hasNext()){
						MenuItem item = it.next();
						String stateMgrStr = item.getStateManager();
						if(stateMgrStr != null && !stateMgrStr.equals("")){
							IStateManager stateMgr = (IStateManager) LfwClassUtil.newInstance(stateMgrStr);
							IStateManager.State enabled = stateMgr.getState(item, viewCtx.getView());
							if(enabled.equals(IStateManager.State.ENABLED))
								item.setEnabled(true);
							else if(enabled.equals(IStateManager.State.DISABLED))
								item.setEnabled(false);
							else if(enabled.equals(IStateManager.State.HIDDEN))
								item.setVisible(false);
							else if(enabled.equals(IStateManager.State.VISIBLE))
								item.setVisible(true);
						}
					}
				}
			}
		}
	}
}
