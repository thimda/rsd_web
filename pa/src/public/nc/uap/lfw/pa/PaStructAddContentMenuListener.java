package nc.uap.lfw.pa;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.ctrl.IController;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.design.itf.IPaEditorService;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.util.ObjSelectedUtil;

public class PaStructAddContentMenuListener implements IController{


	public void onclick(MouseEvent<MenuItem> e) {
		
		IPaEditorService service = NCLocator.getInstance().lookup(IPaEditorService.class);
		String pageId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("_pageId");
		String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
		
		
		UIMeta uimeta = service.getOriUIMeta(pageId, sessionId);		
		PageMeta pageMeta = service.getOriPageMeta(pageId, sessionId);
		
		Dataset ds = LfwRuntimeEnvironment.getWebContext().getPageMeta().getWidget("data").getViewModels().getDataset("ds_struct");
		int idIndex = ds.nameToIndex("id");
		Row row = ds.getSelectedRow();
		String id = (String) row.getValue(idIndex);
		UIElement ele = UIElementFinder.findElementById(uimeta, id);		
		id = ObjSelectedUtil.toSelected(uimeta, pageMeta, ele);
		if(ele instanceof UILayoutPanel){
			AppLifeCycleContext.current().getApplicationContext().addExecScript("var obj = {id : '" + id + "', currentDropObjType2 : 'isPanel'}; \n");
		}
		else{
			AppLifeCycleContext.current().getApplicationContext().addExecScript("var obj = {id : '" + id + "', currentDropObjType2 : 'isLayout'}; \n");
		}
		
		AppLifeCycleContext.current().getApplicationContext().addExecScript("addFromStruct(obj); \n");
	}

}
