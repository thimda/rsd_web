package nc.uap.lfw.design.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.util.AMCUtil;
import nc.uap.lfw.jsp.uimeta.UIBorder;
import nc.uap.lfw.jsp.uimeta.UIBorderTrue;
import nc.uap.lfw.jsp.uimeta.UIButton;
import nc.uap.lfw.jsp.uimeta.UICanvas;
import nc.uap.lfw.jsp.uimeta.UICanvasPanel;
import nc.uap.lfw.jsp.uimeta.UICardLayout;
import nc.uap.lfw.jsp.uimeta.UICardPanel;
import nc.uap.lfw.jsp.uimeta.UIChartComp;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIConstant;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIExcelComp;
import nc.uap.lfw.jsp.uimeta.UIFlowhLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowhPanel;
import nc.uap.lfw.jsp.uimeta.UIFlowvLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIFormComp;
import nc.uap.lfw.jsp.uimeta.UIFormElement;
import nc.uap.lfw.jsp.uimeta.UIFormGroupComp;
import nc.uap.lfw.jsp.uimeta.UIGridComp;
import nc.uap.lfw.jsp.uimeta.UIGridLayout;
import nc.uap.lfw.jsp.uimeta.UIGridPanel;
import nc.uap.lfw.jsp.uimeta.UIGridRowLayout;
import nc.uap.lfw.jsp.uimeta.UIGridRowPanel;
import nc.uap.lfw.jsp.uimeta.UIIFrame;
import nc.uap.lfw.jsp.uimeta.UIImageComp;
import nc.uap.lfw.jsp.uimeta.UILabelComp;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UILinkComp;
import nc.uap.lfw.jsp.uimeta.UIMenuGroup;
import nc.uap.lfw.jsp.uimeta.UIMenuGroupItem;
import nc.uap.lfw.jsp.uimeta.UIMenubarComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIPanel;
import nc.uap.lfw.jsp.uimeta.UIPanelPanel;
import nc.uap.lfw.jsp.uimeta.UIPartComp;
import nc.uap.lfw.jsp.uimeta.UISelfDefComp;
import nc.uap.lfw.jsp.uimeta.UIShutter;
import nc.uap.lfw.jsp.uimeta.UIShutterItem;
import nc.uap.lfw.jsp.uimeta.UISplitter;
import nc.uap.lfw.jsp.uimeta.UISplitterOne;
import nc.uap.lfw.jsp.uimeta.UISplitterTwo;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import nc.uap.lfw.jsp.uimeta.UITabItem;
import nc.uap.lfw.jsp.uimeta.UITabRightPanel;
import nc.uap.lfw.jsp.uimeta.UITextField;
import nc.uap.lfw.jsp.uimeta.UIToolBar;
import nc.uap.lfw.jsp.uimeta.UITreeComp;
import nc.uap.lfw.jsp.uimeta.UIWidget;
import nc.vo.jcom.xml.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
public class UIMetaToXml {
	public static void toXml(UIMeta meta, String folderPath) {
		String filePath = folderPath + "/uimeta.um";
		// 写出文件
		Writer wr = null;
		File file = null;
		boolean create = false;
	    try
	    {
	    	file = new File(filePath);
	    	if(!file.exists()){
	    		create = true;
	    		file.createNewFile();
	    	}
	    	StringWriter sw = new StringWriter();
	    	toXml(meta, sw, folderPath);
	    	wr = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
	    	wr.write(sw.toString());
	    }
	    catch (IOException e){
	    	if(create && file != null)
	    		file.delete();
	    	Logger.error(e.getMessage(), e);
		}
		finally
		{
			try
			{
				if(wr != null){
					wr.flush();
					wr.close();
				}
			} 
			catch (IOException e)
			{
				Logger.error(e, e);
			}
		}
	}
	public static void toXml(UIMeta meta, Writer writer, String folderPath){
		Document doc = getDocumentByUIMeta(meta);
		XMLUtil.printDOMTree(writer, doc, 0, "UTF-8");
	}
	public static String toString(UIMeta meta){
		Document doc = getDocumentByUIMeta(meta);
		Writer wt = new StringWriter();
		
        XMLUtil.printDOMTree(wt, doc, 0, "UTF-8");
        String xmlStr = wt.toString();
        return xmlStr;
	}
	private static Document getDocumentByUIMeta(UIMeta meta){
		Document doc = XMLUtil.getNewDocument();
		UIElement element = meta.getElement();
		Element node = doc.createElement("UIMeta");
		String id = (String)meta.getAttribute(UIMeta.ID);
		node.setAttribute(UIMeta.ID, id);
		Integer isJquery = (Integer) meta.getAttribute(UIMeta.ISJQUERY);
		if(isJquery != null)
			node.setAttribute(UIMeta.ISJQUERY, String.valueOf(isJquery));
		
		String uiprovider = meta.getUiprovider();
		if(uiprovider != null && !uiprovider.equals("")){
			node.setAttribute("uiprovider", uiprovider);
		}

		//是否流式布局
		boolean isFlowMode = (Boolean)meta.getAttribute(UIMeta.FLOWMODE);
		node.setAttribute(UIMeta.FLOWMODE, String.valueOf(isFlowMode));
		doc.appendChild(node);
		
		if(meta.getAttribute("includejs") != null){
			String includejs = meta.getAttribute("includejs").toString();
			if(includejs != null)
				node.setAttribute(UIMeta.INCLUDEJS, includejs);
		}
		if(meta.getAttribute("includecss") != null){
			String includecss = meta.getAttribute("includecss").toString();
			if(includecss != null)
				node.setAttribute(UIMeta.INCLUDECSS, includecss);
		}
		
		//判断uimeta的动态生成uimeta类是否为空,不为空保存此类
		if(meta.getAttribute(UIMeta.GENERATECLASS) != null){
			String generateClass = (String) meta.getAttribute(UIMeta.GENERATECLASS);
			if(generateClass != null && !generateClass.equals(""))
				node.setAttribute(UIMeta.GENERATECLASS, generateClass);
			Boolean tabBody = (Boolean) meta.getAttribute(UIMeta.TABBODY);
			if(tabBody != null && !"".equals(tabBody)){
				node.setAttribute(UIMeta.TABBODY, tabBody.toString());
			}
		}

		if(element != null)
			createLayoutOrComponent(element, doc, node);
		
		if(meta.getDialogMap() != null)
			createDialog(meta.getDialogMap(), doc, node);
		//Element rootNode = doc.createElement("Widget");
		//doc.appendChild(rootNode);
		return doc;
	}

	private static void createDialog(Map<String, UIWidget> dialogMap,  Document doc, Node parent) {
		Iterator<UIWidget> wit = dialogMap.values().iterator();
		while(wit.hasNext()){
			UIWidget w = wit.next();
			String widgetName = UIConstant.UIDIALOG;
			Element widget = doc.createElement(widgetName);
			widget.setAttribute("id", w.getId());
			parent.appendChild(widget);
		}
	}
	private static void createLayout(UILayout layout, Document doc, Node parent) {
		Element node = null;
		if(layout instanceof UICardLayout){
			String layoutName = UIConstant.CARD_LAYOUT;
			String id = ((UICardLayout)layout).getId();
			node = doc.createElement(layoutName);
			if(id != null)
				node.setAttribute("id", id);
			if(((UICardLayout)layout).getAttribute(UICardLayout.CURRENT_ITEM) != null){
				String currentItem =  ((UICardLayout)layout).getAttribute(UICardLayout.CURRENT_ITEM).toString();
				if(currentItem != null){
//					String value = "0";
//					if(currentItem.equals("1"))
//						value = "1";
					node.setAttribute(UICardLayout.CURRENT_ITEM, currentItem);
				}
			}
			String widgetId = ((UICardLayout)layout).getWidgetId();
			if(widgetId != null)
				node.setAttribute("widgetId", widgetId);
		}
		else if(layout instanceof UIFlowhLayout){
			String layoutName = UIConstant.FLOWH_LAYOUT;
			node = doc.createElement(layoutName);
			String id = String.valueOf(layout.getAttribute(UIFlowhLayout.ID));
			node.setAttribute(UIFlowhLayout.ID, id);
			Integer autoFill = (Integer) layout.getAttribute(UIFlowhLayout.AUTO_FILL);
			node.setAttribute(UIFlowhLayout.AUTO_FILL, autoFill == null ? "false" : "" + UIConstant.TRUE.equals(autoFill));
			if(layout.getAttribute(UIFlowhLayout.WIDGET_ID) != null){
				String widgetId = String.valueOf(layout.getAttribute(UIFlowhLayout.WIDGET_ID));
				node.setAttribute(UIFlowhLayout.WIDGET_ID, widgetId);
			}
		}
		else if(layout instanceof UIFlowvLayout){
			String layoutName = UIConstant.FLOWV_LAYOUT;
			node = doc.createElement(layoutName);
			String id = String.valueOf(layout.getAttribute(UIFlowvLayout.ID));
			node.setAttribute(UIFlowvLayout.ID, id);
			if(layout.getAttribute(UIFlowvLayout.WIDGET_ID) != null){
				String widgetId = String.valueOf(layout.getAttribute(UIFlowvLayout.WIDGET_ID));
				node.setAttribute(UIFlowvLayout.WIDGET_ID, widgetId);
			}
		}
		else if(layout instanceof UICanvas){
			String canvasName = UIConstant.CANVAS_LAYOUT;
			node = doc.createElement(canvasName);
			UICanvas canvas = (UICanvas) layout;
			String id = String.valueOf(canvas.getId());
			node.setAttribute(UIElement.ID, id);
			
			String widgetId = canvas.getWidgetId();
			if(widgetId != null){
				node.setAttribute(UIElement.WIDGET_ID, widgetId);
			}
			String className = canvas.getClassName();
			if(className != null){
				node.setAttribute(UICanvas.CLASSNAME, className);
			}
		}
		else if(layout instanceof UIPanel){
			String layoutName = UIConstant.PANEL_LAYOUT;
			node = doc.createElement(layoutName);
			UIPanel panel = (UIPanel) layout; 
			String id = (String) panel.getAttribute("id");
			node.setAttribute("id", id);
			
			String title = (String) panel.getAttribute(UIPanel.TITLE);
			if(title != null && !title.equals(""))
				node.setAttribute(UIPanel.TITLE, String.valueOf(title));
			
			String className = (String) panel.getAttribute(UIPanel.CLASSNAME);
			if(className != null && !className.equals(""))
				node.setAttribute(UIPanel.CLASSNAME, className);
				
			if(panel.getAttribute(UIPanel.WIDGET_ID) != null){
				String widgetId = String.valueOf(panel.getAttribute(UIPanel.WIDGET_ID));
				node.setAttribute(UIPanel.WIDGET_ID, widgetId);
			}
		}
		else if(layout instanceof UIBorder){
			UIBorder border = (UIBorder) layout;
			String layoutName = UIConstant.BORDER;
			node = doc.createElement(layoutName);
			if(border.getId() != null)
				node.setAttribute(UIBorder.ID, border.getId());
			if(border.getWidth() != null)
				node.setAttribute("width", border.getWidth());
			if(border.getLeftWidth() != null)
				node.setAttribute("leftWidth", border.getLeftWidth());
			if(border.getRightWidth() != null)
				node.setAttribute("rightWidth", border.getRightWidth());
			if(border.getTopWidth() != null)
				node.setAttribute("topWidth", border.getTopWidth());
			if(border.getBottomWidth() != null)
				node.setAttribute("bottomWidth", border.getBottomWidth());
			if(border.getColor() != null)
				node.setAttribute(UIBorder.COLOR, border.getColor());
			if(border.getLeftColor() != null)
				node.setAttribute(UIBorder.LEFTCOLOR, border.getLeftColor());
			if(border.getRightColor() != null)
				node.setAttribute(UIBorder.RIGHTCOLOR, border.getRightColor());
			if(border.getTopColor() != null)
				node.setAttribute(UIBorder.TOPCOLOR, border.getTopColor());
			if(border.getBottomColor() != null)
				node.setAttribute(UIBorder.BOTTOMCOLOR, border.getBottomColor());
			if(border.getClassName() != null)
				node.setAttribute(UIBorder.CLASSNAME, border.getClassName());
			if(border.getWidgetId() != null)
				node.setAttribute(UIBorder.WIDGET_ID, border.getWidgetId());
			Integer showleft = ((UIBorder)layout).getShowLeft();
			node.setAttribute(UIBorder.SHOWLEFT, (showleft == null || showleft.intValue() == 0) ? "true" : "false");
			Integer showright = ((UIBorder)layout).getShowRight();
			node.setAttribute(UIBorder.SHOWRIGHT, (showright == null || showright.intValue() == 0) ? "true" : "false");
			Integer showtop = ((UIBorder)layout).getShowTop();
			node.setAttribute(UIBorder.SHOWTOP, (showtop == null || showtop.intValue() == 0) ? "true" : "false");
			Integer showbottom = ((UIBorder)layout).getShowBottom();
			node.setAttribute(UIBorder.SHOWBOTTOM, (showbottom == null || showbottom.intValue() == 0) ? "true" : "false");
		}
		
		else if(layout instanceof UITabComp){
			String layoutName = UIConstant.TAB_LAYOUT;
			String id = ((UITabComp)layout).getId();
			node = doc.createElement(layoutName);
			if(id != null)
				node.setAttribute("id", id);
			
			String widgetId = ((UITabComp)layout).getWidgetId();
			if(widgetId != null)
				node.setAttribute("widgetId", widgetId);
			
			String currentIndex = ((UITabComp)layout).getCurrentItem();
			if(currentIndex != null)
				node.setAttribute(UITabComp.CURRENT_ITEM, currentIndex);
			String tabType = ((UITabComp)layout).getTabType();
			if(tabType != null)
				node.setAttribute(UITabComp.TAB_TYPE, tabType);
			Integer oneTabHide = ((UITabComp)layout).getOneTabHide();
			node.setAttribute("oneTabHide", (oneTabHide == null || oneTabHide.intValue() == 0) ? "false" : "true");
			//增加TabItems节点
			Element tabItems = doc.createElement(UIConstant.TAB_ITEMS);
			node.appendChild(tabItems);
			//Events
//			AMCUtil.addEvents(doc, (EventConf[])layout.getAttribute(UIElement.CONTROLLER_EVENT), node);
			AMCUtil.addEvents(doc, layout.getEventConfs(), node);
		}
		else if(layout instanceof UIShutter){
			String layoutName = UIConstant.SHUTTER_LAYOUT;
			String id = ((UIShutter)layout).getId();
			node = doc.createElement(layoutName);
			if(id != null)
				node.setAttribute("id", id);
			
			String widgetId = ((UIShutter)layout).getWidgetId();
			if(widgetId != null)
				node.setAttribute("widgetId", widgetId);
			String className = ((UIShutter)layout).getClassName();
			if(className != null)
				node.setAttribute(UIShutter.CLASSNAME, className);
			String currentItem = ((UIShutter)layout).getCurrentItem();
			if(currentItem != null)
				node.setAttribute("currentItem", currentItem);
			//增加ShutterItems节点
			Element shutterItems = doc.createElement(UIConstant.SHUTTER_ITEMS);
			node.appendChild(shutterItems);
			//Events
			AMCUtil.addEvents(doc, layout.getEventConfs(), node);
		}
		else if(layout instanceof UISplitter){
			UISplitter uiSplitter = (UISplitter) layout;
			String layoutName = UIConstant.SPLIT_LAYOUT;
			node = doc.createElement(layoutName);
			String id = (uiSplitter).getId();
			if(id != null)
				node.setAttribute("id", id);
			String divideSize = uiSplitter.getDivideSize();
			if(divideSize != null){
				node.setAttribute("divideSize", divideSize);
			}
						
			String widgetId = ((UISplitter)layout).getWidgetId();
			if(widgetId != null)
				node.setAttribute("widgetId", widgetId);
			
			Integer boundMode = ((UISplitter)layout).getBoundMode();
			if(boundMode != null){
				node.setAttribute(UISplitter.BOUNDMODE, String.valueOf(boundMode));
			}
			
			Integer inverseValue = ((UISplitter)layout).getInverse();
			if(inverseValue != null)
				node.setAttribute(UISplitter.INVERSE, String.valueOf(inverseValue));
			
//			String spliterWidth = uiSplitter.getSpliterWidth();
//			if(spliterWidth != null)
//				node.setAttribute(UISplitter.SPLITERWIDTH, spliterWidth);
//			
//			Integer hideBar = uiSplitter.getHideBar();
//			boolean hidebarValue = false;
//			if(hideBar != null && hideBar == 1)
//				hidebarValue = true;
//			node.setAttribute(UISplitter.HIDEBAR, String.valueOf(hidebarValue));
			
			Integer oneTouch = (Integer) uiSplitter.getAttribute(UISplitter.ONETOUCH);
			boolean oneTouchValue = false;
			if(oneTouch != null && oneTouch == 1)
				oneTouchValue = true;
			node.setAttribute(UISplitter.ONETOUCH, String.valueOf(oneTouchValue));
			
			Integer ori = uiSplitter.getOrientation();
			if(ori != null){
				String orientation = "h";
				if(ori.intValue() == 0)
					orientation = "v";
				node.setAttribute("orientation", orientation);
			}
		}
		else if(layout instanceof UIMenuGroup){
			String layoutName = UIConstant.MENUGROUP_LAYOUT;
			String id = ((UIMenuGroup)layout).getId();
			node = doc.createElement(layoutName);
			if(id != null)
				node.setAttribute("id", id);
		}
		else if(layout instanceof UIGridLayout){
			UIGridLayout gridLayout = (UIGridLayout) layout;
			String layoutName = UIConstant.GRID_LAYOUT;
			node = doc.createElement(layoutName);
			
			if(gridLayout.getId() != null){
				node.setAttribute("id", gridLayout.getId());
			}
			if(gridLayout.getWidgetId() != null){
				node.setAttribute(UIGridLayout.WIDGET_ID, gridLayout.getWidgetId());
			}
			if(gridLayout.getBorder() != null){
				node.setAttribute(UIGridLayout.BORDER, gridLayout.getBorder());
			}
//			if(gridLayout.getWidth() != null){
//				node.setAttribute(UIGridLayout.WIDTH, gridLayout.getWidth());
//			}
//			if(gridLayout.getHeight() != null){
//				node.setAttribute(UIGridLayout.HEIGHT, gridLayout.getHeight());
//			}
//			
			Integer colCount = gridLayout.getColcount();
			if(colCount != null && colCount.intValue() != -1){
				node.setAttribute(UIGridLayout.COLCOUNT, colCount.toString());
			}
			
			Integer rowCount = gridLayout.getRowcount();
			if(rowCount != null && rowCount.intValue() != -1){
				node.setAttribute(UIGridLayout.ROWCOUNT, rowCount.toString());
			}
		}
		else if(layout instanceof UIGridRowLayout){
			UIGridRowLayout gridRow = (UIGridRowLayout) layout;
			String layoutName = UIConstant.GRID_ROW;
			node = doc.createElement(layoutName);
			
			if(gridRow.getId() != null){
				node.setAttribute("id", gridRow.getId());
			}
			
			String widgetId = (String) gridRow.getAttribute(UIGridRowLayout.WIDGET_ID);
			if(widgetId != null){
				node.setAttribute(UIGridRowLayout.WIDGET_ID, widgetId);
			}
			if(gridRow.getRowHeight() != null){
				node.setAttribute(UIGridRowLayout.ROWHEIGHT, gridRow.getRowHeight());
			}
			Integer colCount = gridRow.getColcount();
			if(colCount != null && colCount.intValue() != -1){
				node.setAttribute(UIGridRowLayout.COLCOUNT, colCount.toString());
			}
		}
		
		
		if(node == null)
			return;
		parent.appendChild(node);
		
		List<UILayoutPanel> list = layout.getPanelList();
		Iterator<UILayoutPanel> it = list.iterator();
		while(it.hasNext()){
			UILayoutPanel uipanel = it.next();
			if(!(uipanel instanceof UITabRightPanel)){
				createLayoutPanel(uipanel, doc, node);
			}
		}
		if(layout instanceof UITabComp){
			UITabComp uiTab = (UITabComp) layout;
			UITabRightPanel rightPanel = uiTab.getRightPanel();
			if(rightPanel != null &&rightPanel.getElement() != null)
				createLayoutPanel(rightPanel, doc, node);
		}
		if(layout instanceof UIGridLayout){
			UIGridLayout gridLayout = (UIGridLayout) layout;
			List<UILayoutPanel> rowList = gridLayout.getPanelList();
			if(rowList != null && rowList.size() > 0){
				for(int i = 0; i< rowList.size(); i++){
					UIGridRowPanel rowPanel = (UIGridRowPanel) rowList.get(i);
					if(rowPanel != null)
						createLayout(rowPanel.getRow(), doc, node);
				}
			}
		}
	}

	private static void createLayoutPanel(UILayoutPanel panel, Document doc,  Element node) {
		String panelName = null;
		Element child = null;
		if(panel instanceof UICardPanel){
			panelName = UIConstant.CARD_PANEL;
			UICardPanel cardPanel = (UICardPanel) panel;
			child = doc.createElement(panelName);
			child.setAttribute("id", cardPanel.getId());
		}
		else if(panel instanceof UICanvasPanel){
			panelName = UIConstant.CANVAS_PANEL;
			UICanvasPanel canvasPanel = (UICanvasPanel) panel;
			child = doc.createElement(panelName);
			child.setAttribute("id", canvasPanel.getId());
		}
		else if(panel instanceof UIFlowhPanel){
			panelName = UIConstant.FLOWH_PANEL;
			UIFlowhPanel flowhPanel = (UIFlowhPanel) panel;
			child = doc.createElement(panelName);
			
			if(flowhPanel.getWidth() != null && !flowhPanel.getWidth().equals(""))
				child.setAttribute("width", flowhPanel.getWidth().toString());
		}
		
		else if(panel instanceof UIFlowvPanel){
			panelName = UIConstant.FLOWV_PANEL;
			UIFlowvPanel flowvPanel = (UIFlowvPanel) panel;
			child = doc.createElement(panelName);
			child.setAttribute("id", (String)flowvPanel.getAttribute("id"));
			if(flowvPanel.getHeight() != null && !flowvPanel.getHeight().equals(""))
				child.setAttribute("height", flowvPanel.getHeight().toString());
			if(flowvPanel.getAnchor() != null && !flowvPanel.getAnchor().equals(""))
				child.setAttribute("anchor", flowvPanel.getAnchor().toString());
		}
		else if(panel instanceof UIPanelPanel){
			panelName = UIConstant.PANEL_PANEL;
			child = doc.createElement(panelName);

		}
		else if(panel instanceof UITabItem){
			panelName = UIConstant.TAB_ITEM;
			UITabItem item = (UITabItem) panel;
			child = doc.createElement(panelName);
			child.setAttribute("id", item.getId());
			child.setAttribute("text", item.getText());
			child.setAttribute("i18nName", item.getI18nName());
			child.setAttribute("showCloseIcon", item.getShowCloseIcon());
			child.setAttribute("active", item.getActive());
			Integer state = item.getState();
			if(state != null && !(state.intValue() == -1))
				child.setAttribute("state", state.toString());
			NodeList nl = node.getElementsByTagName(UIConstant.TAB_ITEMS);
			if(nl != null && nl.getLength() > 0){
				node = (Element)nl.item(0);
			}
		}
		else if(panel instanceof UIShutterItem){
			panelName = UIConstant.SHUTTER_ITEM;
			UIShutterItem item = (UIShutterItem) panel;
			child = doc.createElement(panelName);
			child.setAttribute("id", item.getId());
			child.setAttribute("text", item.getText());
			child.setAttribute("i18nName", item.getI18nName());
			NodeList nl = node.getElementsByTagName(UIConstant.SHUTTER_ITEMS);
			if(nl != null && nl.getLength() > 0){
				node = (Element)nl.item(0);
			}
		}
		
		else if(panel instanceof UITabRightPanel){
			panelName = UIConstant.TAB_RIGHT;
			UITabRightPanel item = (UITabRightPanel) panel;
			child = doc.createElement(panelName);
			if(item.getWidth() != null)
				child.setAttribute("width", item.getWidth());
		}
		else if(panel instanceof UISplitterTwo){
			panelName = UIConstant.SPLIT_TWO;
			//UISplitterOne one = (UISplitterOne) panel;
			child = doc.createElement(panelName);
		}
		else if(panel instanceof UISplitterOne){
			panelName = UIConstant.SPLIT_ONE;
			//UISplitterOne one = (UISplitterOne) panel;
			child = doc.createElement(panelName);
		}
		else if(panel instanceof UIMenuGroupItem){
			UIMenuGroupItem groupItem = (UIMenuGroupItem) panel;
			panelName = UIConstant.MENUGROUP_ITEM;
			//UISplitterOne one = (UISplitterOne) panel;
			child = doc.createElement(panelName);
			Integer state = groupItem.getState();
			if(state != null && !(state.intValue() == -1))
				child.setAttribute("state", state.toString());
		}
		else if(panel instanceof UIGridRowPanel){
			panelName = UIConstant.GRID_ROW_PANEL;
			child = doc.createElement(panelName);
		}
		else if(panel instanceof UIBorderTrue){
			panelName = UIConstant.BORDER_TRUE;
			child = doc.createElement(panelName);
		}
		else if(panel instanceof UIGridPanel){
			UIGridPanel gridPanel = (UIGridPanel) panel;
			panelName = UIConstant.GRID_PANEL;
			child = doc.createElement(panelName);
			
			
			
			String colHeight = gridPanel.getColHeight();
			if(colHeight != null){
				child.setAttribute(UIGridPanel.COLHEIGHT, colHeight);
			}
			
			String colWidth = gridPanel.getColWidth();
			if(colWidth != null){
				child.setAttribute(UIGridPanel.COLWIDTH, colWidth);
			}
			
			String colSpan = gridPanel.getColSpan();
			if(colSpan != null && !colSpan.equals("")){
				child.setAttribute(UIGridPanel.COLSPAN, colSpan.toString());
			}
			
			String rowSpan = gridPanel.getRowSpan();
			if(rowSpan != null && !rowSpan.equals("")){
				child.setAttribute(UIGridPanel.ROWSPAN, rowSpan.toString());
			}
			
		}
		if(panel.getAttribute(UIElement.ID) != null){
			child.setAttribute(UIElement.ID, (String)panel.getAttribute(UIElement.ID));
		}
		
		String id = String.valueOf(panel.getAttribute(UIFlowvLayout.ID));
		child.setAttribute(UIFlowvLayout.ID, id);
		if(panel.getAttribute(UIFlowvLayout.WIDGET_ID) != null){
			String widgetId = String.valueOf(panel.getAttribute(UIFlowvLayout.WIDGET_ID));
			child.setAttribute(UIFlowvLayout.WIDGET_ID, widgetId);
		}
		
		String topPadding = panel.getTopPadding();
		if(topPadding != null)
			child.setAttribute(UILayoutPanel.TOPPADDING, topPadding);
		
		String bottomPadding = panel.getBottomPadding();
		if(bottomPadding != null)
			child.setAttribute(UILayoutPanel.BOTTOMPADDING, bottomPadding);
		
		String leftPadding = panel.getLeftPadding();
		if(leftPadding != null)
			child.setAttribute(UILayoutPanel.LEFTPADDING, leftPadding);
		
		String rightPadding = panel.getRightPadding();
		if(rightPadding != null)
			child.setAttribute(UILayoutPanel.RIGHTPADDING, rightPadding);
		
		String leftBorder = panel.getLeftBorder();
		if(leftBorder != null)
			child.setAttribute(UILayoutPanel.LEFTBORDER, leftBorder);
		
		String rightBorder = panel.getRightBorder();
		if(rightBorder != null)
			child.setAttribute(UILayoutPanel.RIGHTBORDER, rightBorder);
		
		String topBorder = panel.getTopBorder();
		if(topBorder != null)
			child.setAttribute(UILayoutPanel.TOPBORDER, topBorder);
		
		String bottomBorder = panel.getBottomBorder();
		if(bottomBorder != null)
			child.setAttribute(UILayoutPanel.BOTTOMBORDER, bottomBorder);
		
		String border = panel.getBorder();
		if(border != null)
			child.setAttribute(UILayoutPanel.BORDER, border);
		
		
		node.appendChild(child);
		UIElement ele = panel.getElement();
		if(ele != null)
			createLayoutOrComponent(ele, doc, child);
	}

	private static void createLayoutOrComponent(UIElement ele, Document doc, Element child) {
		if(ele instanceof UIWidget){
			createWidget((UIWidget) ele, doc, child);
		}
		else if(ele instanceof UILayout)
			createLayout((UILayout) ele, doc, child);
		else
			createComponent((UIComponent) ele, doc, child);
	}

	private static void createWidget(UIWidget ele, Document doc, Element parent) {
		Element widget = null;
		String widgetName = UIConstant.UIWIDGET;
		widget = doc.createElement(widgetName);
		widget.setAttribute("id", ele.getId());
		parent.appendChild(widget);
	}

	private static void createComponent(UIComponent ele, Document doc,
			Element parent) {
		Element comp = null;
		String compName = null;
		if(ele instanceof UIGridComp){
			compName = UIConstant.UIGRID;
		}
		if(ele instanceof UIExcelComp){
			compName = UIConstant.UIEXCEL;
		}
		else if(ele instanceof UITreeComp){
			compName = UIConstant.UITREE;
		}
		else if(ele instanceof UIFormComp){
			compName = UIConstant.UIFORM;
		}
		else if(ele instanceof UIFormGroupComp){
			compName = UIConstant.UIFORMGROUP;
		}
		else if(ele instanceof UIMenubarComp)
			compName = UIConstant.UIMENUBAR;
		else if(ele instanceof UIButton)
			compName = UIConstant.UIBUTTON;
		else if(ele instanceof UIChartComp)
			compName = UIConstant.UICHARTCOMP;
		else if(ele instanceof UISelfDefComp)
			compName = UIConstant.UISELFDEFCOMP;
		else if(ele instanceof UILinkComp)
			compName = UIConstant.UILINKCOMP;
		else if(ele instanceof UIToolBar)
			compName = UIConstant.UITOOLBAR;
		else if(ele instanceof UILabelComp)
			compName = UIConstant.UILABEL;
		else if(ele instanceof UITextField)
			compName = UIConstant.UITEXT;
		else if(ele instanceof UIImageComp)
			compName = UIConstant.UIIMAGE;
		else if(ele instanceof UIIFrame)
			compName = UIConstant.UIIFRAME;
		else if(ele instanceof UIFormElement)
			compName = UIConstant.UIFORMELEMENT;
		else if(ele instanceof UIPartComp)
			compName = UIConstant.UIHTMLCONTENT;
		
		comp = doc.createElement(compName);
		comp.setAttribute("id", ele.getId());
		
		if(ele.getAlign() != null)
			comp.setAttribute("align", ele.getAlign());
		if(ele.getHeight() != null)
			comp.setAttribute("height", ele.getHeight());
		if(ele.getWidth() != null)
			comp.setAttribute("width", ele.getWidth());
		if(ele.getLeft() != null)
			comp.setAttribute("left", ele.getLeft());
		if(ele.getTop() != null)
			comp.setAttribute("top", ele.getTop());
		if(ele.getPosition() != null)
			comp.setAttribute("position", ele.getPosition());
		if(ele.getWidgetId() != null)
			comp.setAttribute("widgetId", ele.getWidgetId());
		
		if(ele instanceof UITextField){
			String type = ((UITextField)ele).getType();
			comp.setAttribute("type", type);
			String imgSrc = ((UITextField)ele).getImgsrc();
			comp.setAttribute(UITextField.IMG_SRC, imgSrc);
		} else if (ele instanceof UIFormGroupComp) {
			String forms = ((UIFormGroupComp)ele).getForms();
			comp.setAttribute("formList", forms);
		} else if (ele instanceof UIFormElement){
			String formId = ((UIFormElement) ele).getFormId();
			comp.setAttribute(UIFormElement.FORM_ID, formId);
			String eleWidth = ((UIFormElement) ele).getEleWidth();
			comp.setAttribute(UIFormElement.ELEWIDTH, eleWidth);
		} else if(ele instanceof UIFormComp){
			String labelPosition = ((UIFormComp)ele).getLabelPosition();
			comp.setAttribute(UIConstant.LABEL_POSITION, labelPosition);
		} else if(ele instanceof UILabelComp){
			String textAlign = ((UILabelComp)ele).getTextAlign();
			comp.setAttribute(UILabelComp.TEXTALIGN, textAlign != null? textAlign : "left");
		}
		
		parent.appendChild(comp);
	}
}
