package nc.uap.lfw.pa;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.ctrl.IController;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.design.itf.IPaEditorService;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIWidget;
import nc.uap.lfw.ra.util.ObjSelectedUtil;

public class PaStructDeleteContentMenuListener implements IController{
 

	public void onclick(MouseEvent<MenuItem> e) {
		
		IPaEditorService service = NCLocator.getInstance().lookup(IPaEditorService.class);
		String pageId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("_pageId");
		String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
		
//		waitForInit(pageId, sessionId);
		
		UIMeta uimeta = service.getOriUIMeta(pageId, sessionId);		
		PageMeta pageMeta = service.getOriPageMeta(pageId, sessionId);
		
		Dataset ds = LfwRuntimeEnvironment.getWebContext().getPageMeta().getWidget("data").getViewModels().getDataset("ds_struct");
		int idIndex = ds.nameToIndex("id");
		Row row = ds.getSelectedRow();
		String id = (String) row.getValue(idIndex);
		UIElement ele = UIElementFinder.findElementById(uimeta, id);	
		
		WebSession session = LfwRuntimeEnvironment.getWebContext().getWebSession();
		String mode = (String) session.getAttribute(WebConstant.WINDOW_MODE_KEY);
		
		if(mode != null && (WebConstant.MODE_PERSONALIZATION).equals(mode)){
			id = ObjSelectedUtil.toSelected(uimeta, pageMeta, ele);
			AppLifeCycleContext.current().getApplicationContext().addExecScript("deleteFromStruct('"+id+"')");
		}
		else{
			if(ele instanceof UIMeta || ele instanceof UIWidget)
				return;
			id = ObjSelectedUtil.toSelected(uimeta, pageMeta, ele);
			AppLifeCycleContext.current().getApplicationContext().addExecScript("deleteFromStruct('"+id+"')");
		}
	
	}
	
	private void waitForInit(String sessionId, String pageId) {
		int count = 0;
		PageMeta pagemeta = null;
		while(pagemeta == null && count < 5){
			IPaEditorService service = NCLocator.getInstance().lookup(IPaEditorService.class);
			pagemeta = service.getOriPageMeta(pageId, sessionId);
			if(pagemeta == null){
				count ++;
				try {
					Thread.sleep(1000);
				} 
				catch (InterruptedException e) {
					LfwLogger.error(e);
				}
			}
		}
	}	

}
