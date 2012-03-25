/**
 * 
 */
package nc.uap.lfw.pa;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.PageModelUtil;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.design.itf.IPaEditorService;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIGridLayout;
import nc.uap.lfw.jsp.uimeta.UIGridRowLayout;
import nc.uap.lfw.jsp.uimeta.UIGridRowPanel;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIWidget;
import nc.uap.lfw.ra.util.ObjSelectedUtil;

/**
 * @author wupeng1
 * @version 6.0 2011-8-29
 * @since 1.6
 */
public class PaStructDsListener {
	public void onDataLoad(DataLoadEvent e) {
//		PageMeta pagemeta = null;
		UIMeta uimeta = null;
		
		String pk_template = LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("pk_template");
		if(pk_template != null)
			LfwRuntimeEnvironment.setFromDB(true);
		if(LfwRuntimeEnvironment.isFromDB()){
			if(pk_template != null){
//				pagemeta = PageModelUtil.getPageMeta(pk_template);
				uimeta = PageModelUtil.getUIMeta(pk_template);
			}
		}
		else{
			IPaEditorService ipaService = NCLocator.getInstance().lookup(IPaEditorService.class);
			String pageId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("_pageId");
			String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
			waitForInit(pageId, sessionId);
//			pagemeta = ipaService.getOriPageMeta(pageId, sessionId);
			uimeta = ipaService.getOriUIMeta(pageId, sessionId);
		}
		
		Dataset ds = e.getSource();
		
		WebSession session = LfwRuntimeEnvironment.getWebContext().getWebSession();
		String mode = (String) session.getAttribute(WebConstant.WINDOW_MODE_KEY);
		
		if(uimeta != null ){
			if(mode != null && (WebConstant.MODE_PERSONALIZATION).equals(mode)){
				setWinRowValue(uimeta, ds, null);
			}
			else{
				if(uimeta.getElement()!= null)
					setRowValue(uimeta, ds, null);
			}
			
		}
	}
	
	private void setWinRowValue(UIElement ele, Dataset ds, String pid) {
		
		String eleId = (String) ele.getAttribute(UIElement.ID);
		if(eleId != null){
			setRowInfo(ds, eleId, pid);
		}
		
		if(ele instanceof UILayout){
			if(ele instanceof UIGridLayout){
				List<UILayoutPanel> gridRows = ((UIGridLayout) ele).getPanelList();
				if(gridRows != null && gridRows.size() > 0){
					for(UILayoutPanel gridRow : gridRows){
						UIGridRowLayout row = ((UIGridRowPanel)gridRow).getRow();
						setWinRowValue(row, ds, eleId);
					}
				}
			}
			else{
				List<UILayoutPanel> panels = ((UILayout) ele).getPanelList();
				if(panels != null && panels.size() > 0){
					for(UILayoutPanel panel : panels){
						if(panel != null)
							setWinRowValue(panel, ds, eleId);
					}
				}
			}
		}
		if(ele instanceof UILayoutPanel){
			UIElement pEle = ((UILayoutPanel) ele).getElement();
			if(pEle != null && !(pEle instanceof UIComponent))
				setWinRowValue(pEle, ds, eleId);
		}
		if(ele instanceof UIWidget){
			setRowInfo(ds, ((UIWidget) ele).getId(), eleId);
//			UIMeta uimeta = ((UIWidget) ele).getUimeta();
//			if(uimeta != null){
//				UIElement uiEle = uimeta.getElement();
//				if(uiEle != null)
//					setWinRowValue(uiEle, ds, eleId);
//			}
		}
	}

	public static void setRowValue(UIElement ele, Dataset ds, String pid){
		String eleId = (String) ele.getAttribute(UIElement.ID);
		if(eleId != null && !(ele instanceof UIWidget)){
			setRowInfo(ds, eleId, pid);
		}
		
		if(ele instanceof UILayout){
			if(ele instanceof UIGridLayout){
				List<UILayoutPanel> gridRows = ((UIGridLayout) ele).getPanelList();
				if(gridRows != null && gridRows.size() > 0){
					for(UILayoutPanel gridRow : gridRows){
						UIGridRowLayout row = ((UIGridRowPanel)gridRow).getRow();
						setRowValue(row, ds, eleId);
					}
				}
			}
			if(ele instanceof UIWidget){
				UIMeta uimeta = ((UIWidget) ele).getUimeta();
//				setRowInfo(ds, ele.getId(), eleId);
				setRowValue(uimeta, ds, eleId);
			}
			else{
				List<UILayoutPanel> panels = ((UILayout) ele).getPanelList();
				if(panels != null && panels.size() > 0){
					for(UILayoutPanel panel : panels){
						if(panel != null)
							setRowValue(panel, ds, eleId);
					}
				}
			}
		}
		if(ele instanceof UILayoutPanel){
			UIElement pEle = ((UILayoutPanel) ele).getElement();
			if(pEle != null)
				setRowValue(pEle, ds, eleId);
		}
		
//		if(ele instanceof UIWidget)
//			setRowInfo(ds, ((UIWidget) ele).getId(), eleId);
//		if(ele instanceof UIWidget){
//			UIMeta uimeta = ((UIWidget) ele).getUimeta();
////			setRowInfo(ds, ele.getId(), eleId);
//			setRowValue(uimeta, ds, eleId);
////			if(uimeta != null){
////				UIElement uiEle = uimeta.getElement();
////				if(uiEle != null)
////					setRowValue(uiEle, ds, eleId);
////			}
//		}
	}

	private static void setRowInfo(Dataset ds, String eleId, String pid) {
		Row row = ds.getEmptyRow();
		int idIndex = ds.nameToIndex("id");
		int pIdIndex = ds.nameToIndex("pid");
		row.setValue(idIndex, eleId);
		row.setValue(pIdIndex, pid);
		ds.addRow(row);
	}


	public void onAfterRowSelect(DatasetEvent e) {
		
		PageMeta pagemeta = null;
		UIMeta uimeta = null;
		String pk_template = LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("pk_template");
		if(pk_template != null)
			LfwRuntimeEnvironment.setFromDB(true);
		if(LfwRuntimeEnvironment.isFromDB()){
			if(pk_template != null){
				pagemeta = PageModelUtil.getPageMeta(pk_template);
				uimeta = PageModelUtil.getUIMeta(pk_template);
			}
		}
		else{
			IPaEditorService ipaService = NCLocator.getInstance().lookup(IPaEditorService.class);
			String pageId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("_pageId");
			String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
			pagemeta = ipaService.getOriPageMeta(pageId, sessionId);
			uimeta = ipaService.getOriUIMeta(pageId, sessionId);
		}
		
		Dataset ds = e.getSource();
		int idIndex = ds.nameToIndex("id");
		Row row = ds.getSelectedRow();
		if(row == null)
			return;
		String id = (String) row.getValue(idIndex);
		
		UIElement ele = UIElementFinder.findElementById(uimeta, id);	
		if(ele != null){
			id = ObjSelectedUtil.toSelected(uimeta, pagemeta, ele);
			AppLifeCycleContext.current().getApplicationContext().addExecScript("onLoadStruct('"+id+"')");
		}
		
	}

	private void waitForInit(String pageId, String sessionId) {
		int count = 0;
		PageMeta pagemeta = null;
		while(pagemeta == null && count < 10){
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
