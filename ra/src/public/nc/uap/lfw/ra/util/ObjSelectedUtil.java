package nc.uap.lfw.ra.util;

import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIGridLayout;
import nc.uap.lfw.jsp.uimeta.UIGridPanel;
import nc.uap.lfw.jsp.uimeta.UIGridRowLayout;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIPanelPanel;
import nc.uap.lfw.ra.itf.IUIRender;
import nc.uap.lfw.ra.itf.IUIRenderGroup;
import nc.uap.lfw.ra.kit.RenderKit;

public class ObjSelectedUtil {
	
	public static String toSelected(UIMeta uimeta,PageMeta pageMeta,UIElement ele){
		IUIRenderGroup group = RenderKit.getUIRenderGroup(RenderKit.RENDER_PC);
		String renderDiv = "";
		if(ele instanceof UILayoutPanel){
			if(ele instanceof UIGridPanel){
				UIGridPanel cell = (UIGridPanel)ele;
				UIGridRowLayout row = cell.getParent();
				UIGridLayout table = row.getParent();
				IUIRender t = ControlFramework.getInstance().getUIRender(uimeta, table, pageMeta, null, DriverPhase.PC);
				IUIRender r = ControlFramework.getInstance().getUIRender(uimeta, row, pageMeta, t, DriverPhase.PC);
				IUIRender c = ControlFramework.getInstance().getUIRender(uimeta, cell, pageMeta, r, DriverPhase.PC);
				renderDiv = c.getDivId();
			}
			else if(ele instanceof UIPanelPanel){
				UIElement parent = UIElementFinder.findParent(uimeta, ele);
				renderDiv = parent.getId() + "_content";
			}else{
				UIElement parent = UIElementFinder.findParent(uimeta, ele);
				IUIRender parentRender = ControlFramework.getInstance().getUIRender(uimeta, parent, pageMeta, null, DriverPhase.PC);
				IUIRender render = ControlFramework.getInstance().getUIRender(uimeta, ele, pageMeta, parentRender, DriverPhase.PC);
				renderDiv = render.getDivId();
			}
			
		}
		else{
			if(ele instanceof UIGridRowLayout){
				UIElement parent = UIElementFinder.findParent(uimeta, ele);
				IUIRender parentRender = ControlFramework.getInstance().getUIRender(uimeta, parent, pageMeta, null, DriverPhase.PC);
				IUIRender render = ControlFramework.getInstance().getUIRender(uimeta, ele, pageMeta, parentRender, DriverPhase.PC);
				renderDiv = render.getDivId();
			}
			else{
				IUIRender render = ControlFramework.getInstance().getUIRender(uimeta, ele, pageMeta, null, DriverPhase.PC);
				renderDiv = render.getDivId();
			}
		}
		return renderDiv;
	}

}
