package nc.uap.lfw.jsp.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.data.Parameter;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.ExcelRule;
import nc.uap.lfw.core.event.conf.FormRule;
import nc.uap.lfw.core.event.conf.GridRule;
import nc.uap.lfw.core.event.conf.TreeRule;
import nc.uap.lfw.core.event.conf.WidgetRule;
import nc.uap.lfw.core.exception.LfwParseException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.IWidgetUIProvider;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.LifeCyclePhase;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.RequestLifeCycleContext;
import nc.uap.lfw.core.patch.XmlUtilPatch;
import nc.uap.lfw.core.uimodel.WidgetConfig;
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
import nc.uap.lfw.jsp.uimeta.UIDialog;
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
import nc.uap.lfw.jsp.uimeta.UISilverlightWidget;
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
import nc.uap.lfw.pa.PaBusinessException;
import nc.uap.lfw.stylemgr.itf.IUwTemplateService;
import nc.uap.lfw.stylemgr.vo.UwViewVO;
import nc.uap.lfw.util.LfwClassUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class UIMetaParserUtil implements UIConstant {
//	private static final String POSITION = "position";
	private static final String ID = "id";
	private static final String STATE = "state";
	private static final String WIDGET_ID = "widgetId";
	private UIMeta meta = null;
	private PageMeta pagemeta;
	public PageMeta getPagemeta() {
		return pagemeta;
	}

	public void setPagemeta(PageMeta pagemeta) {
		this.pagemeta = pagemeta;
	}

	public UIMeta parseUIMeta(String folderPath, String widgetId) {
		Document doc = null;
		FileInputStream input = null;
		Element rootNode = null;
		try {

			meta = new UIMeta();
			meta.setFolderPath(folderPath);
			String path = null;
			if (widgetId != null && pagemeta != null) {
				WidgetConfig wConf = pagemeta.getWidgetConf(widgetId);
				String refId = wConf.getRefId();
				//public
				if(refId.startsWith("..")){
					folderPath = ContextResourceUtil.getCurrentAppPath() + "/pagemeta/public/widgetpool/";
					meta.setFolderPath(folderPath);
					String trueId = refId.substring(3);
					path = folderPath + trueId + "/uimeta.um";
					input = new FileInputStream(path);
				}
				else{
					path = folderPath + "/uimeta.um";
					input = new FileInputStream(path);
				}
				doc = XmlUtilPatch.getDocumentBuilder().parse(input);
				rootNode = (Element) doc.getFirstChild();
				
				LfwWidget widget = pagemeta.getWidget(widgetId);
				// 得到uimeta的动态产生类
//				String geneClass = getAttributeValue(rootNode, UIMeta.GENERATECLASS);
//				if (geneClass != null) {
//					IUIMetaGenerator generateClass = (IUIMetaGenerator) LfwClassUtil.newInstance(geneClass);
//					boolean tabBodyValue = true;
//					String tabBody = getAttributeValue(rootNode, UIMeta.TABBODY);
//					if (tabBody != null && "false".equals(tabBody)) {
//						tabBodyValue = false;
//					}
//					meta = generateClass.getDefaultUIMeta(widget, tabBodyValue);
//					return meta;
//				}
				
				String uiprovider = getAttributeValue(rootNode, UIMeta.UIPROVIDER);
				if(uiprovider != null && !uiprovider.equals("")){
					IWidgetUIProvider uip = (IWidgetUIProvider) LfwClassUtil.newInstance(uiprovider);
					meta = uip.getDefaultUIMeta(widget);
					meta.setUiprovider(uiprovider);
				}
				
			}
			else{
				path = folderPath + "/uimeta.um";
				input = new FileInputStream(path);
				doc = XmlUtilPatch.getDocumentBuilder().parse(input);
				rootNode = (Element) doc.getFirstChild();
			}

			String id = getAttributeValue(rootNode, UIMeta.ID);
			meta.setId(id);
		
			
			String isJquery = getAttributeValue(rootNode, UIMeta.ISJQUERY);
			if (isJquery != null && isJquery.equals("1"))
				meta.setJquery(Integer.valueOf(1));
			else
				meta.setJquery(Integer.valueOf(0));
			
			String flowmode = getAttributeValue(rootNode, UIMeta.FLOWMODE);
			if (flowmode != null && !flowmode.equals(""))
				meta.setFlowmode(Boolean.valueOf(flowmode));

			String includejs = getAttributeValue(rootNode, UIMeta.INCLUDEJS);
			if (includejs != null)
				meta.setIncludejs(includejs);
			String includecss = getAttributeValue(rootNode, UIMeta.INCLUDECSS);
			if (includecss != null)
				meta.setIncludecss(includecss);

			NodeList ndList = rootNode.getChildNodes();
			Node node = null;
			for (int i = 0; i < ndList.getLength(); i++) {
				node = ndList.item(i);
				if (node instanceof Text)
					continue;
				break;
			}
			LifeCyclePhase oriPhase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			UIElement layout = parseLayout(node,null);
			RequestLifeCycleContext.get().setPhase(oriPhase);
			if (layout == null) {
				layout = parseWidget(node);
				if (layout == null)
					layout = parseComponent(node);
			}
			if(layout != null)
				meta.setElement(layout);
			meta.getDialogMap().putAll(parseDialog(ndList));
			
			if (widgetId != null){
				meta.setId(widgetId + "_um");
				meta.adjustUI(widgetId);
			}
			return meta;
		} catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwParseException(e.getMessage());
		} finally {
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					LfwLogger.error(e.getMessage(), e);
				}
		}
	}

	public UIMeta parseUIMeta(InputStream input, String widgetId, String pk_template){
		Document doc = null;
		Element rootNode = null;
		try {
			meta = new UIMeta();
			if (widgetId != null && pagemeta != null) {
				//public
				doc = XmlUtilPatch.getDocumentBuilder().parse(input);
				rootNode = (Element) doc.getFirstChild();
				
				LfwWidget widget = pagemeta.getWidget(widgetId);
				// 得到uimeta的动态产生类
//				String geneClass = getAttributeValue(rootNode, UIMeta.GENERATECLASS);
//				if (geneClass != null) {
//					IUIMetaGenerator generateClass = (IUIMetaGenerator) LfwClassUtil.newInstance(geneClass);
//					boolean tabBodyValue = true;
//					String tabBody = getAttributeValue(rootNode, UIMeta.TABBODY);
//					if (tabBody != null && "false".equals(tabBody)) {
//						tabBodyValue = false;
//					}
//					meta = generateClass.getDefaultUIMeta(widget, tabBodyValue);
//					return meta;
//				}
				
				String uiprovider = getAttributeValue(rootNode, UIMeta.UIPROVIDER);
				if(uiprovider != null && !uiprovider.equals("")){
					IWidgetUIProvider uip = (IWidgetUIProvider) LfwClassUtil.newInstance(uiprovider);
					meta = uip.getDefaultUIMeta(widget);
					meta.setUiprovider(uiprovider);
				}
				
			}
			else{
				doc = XmlUtilPatch.getDocumentBuilder().parse(input);
				rootNode = (Element) doc.getFirstChild();
			}

			String id = getAttributeValue(rootNode, UIMeta.ID);
			meta.setId(id);
		
			
			String isJquery = getAttributeValue(rootNode, UIMeta.ISJQUERY);
			if (isJquery != null && isJquery.equals("1"))
				meta.setJquery(Integer.valueOf(1));
			else
				meta.setJquery(Integer.valueOf(0));
			
			String flowmode = getAttributeValue(rootNode, UIMeta.FLOWMODE);
			if (flowmode != null && !flowmode.equals(""))
				meta.setFlowmode(Boolean.valueOf(flowmode));
			
			String includejs = getAttributeValue(rootNode, UIMeta.INCLUDEJS);
			if (includejs != null)
				meta.setIncludejs(includejs);
			String includecss = getAttributeValue(rootNode, UIMeta.INCLUDECSS);
			if (includecss != null)
				meta.setIncludecss(includecss);

			NodeList ndList = rootNode.getChildNodes();
			Node node = null;
			for (int i = 0; i < ndList.getLength(); i++) {
				node = ndList.item(i);
				if (node instanceof Text)
					continue;
				break;
			}
			LifeCyclePhase oriPhase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			UIElement layout = parseLayout(node, pk_template);
			RequestLifeCycleContext.get().setPhase(oriPhase);
			if (layout == null) {
				if(pk_template != null)
					layout = parseWidget(node, pk_template);
				if (layout == null)
					layout = parseComponent(node);
			}
			if(layout != null)
				meta.setElement(layout);
			meta.getDialogMap().putAll(parseDialog(ndList, pk_template));
			
			if (widgetId != null){
				meta.setId(widgetId + "_um");
				meta.adjustUI(widgetId);
			}
			return meta;
		} catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwParseException(e.getMessage());
		} finally {
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					LfwLogger.error(e.getMessage(), e);
				}
		}
	}







	private Map<String, UIWidget> parseDialog(NodeList ndList) {
		Map<String, UIWidget> map = new HashMap<String, UIWidget>();
		for (int i = 0; i < ndList.getLength(); i++) {
			Node nd = ndList.item(i);
			if (nd instanceof Text)
				continue;
			if (nd.getNodeName().equals("Dialog")) {
				UIWidget w = (UIWidget) parseDialog(nd);
				map.put(w.getId(), w);
			}
		}
		return map;
	}
	
	private Map<String, UIWidget> parseDialog(NodeList ndList, String pk_template) {
		Map<String, UIWidget> map = new HashMap<String, UIWidget>();
		for (int i = 0; i < ndList.getLength(); i++) {
			Node nd = ndList.item(i);
			if (nd instanceof Text)
				continue;
			if (nd.getNodeName().equals("Dialog")) {
				UIWidget w = (UIWidget) parseDialog(nd, pk_template);
				map.put(w.getId(), w);
			}
		}
		return map;
	}


	private UILayout parseLayout(Node node, String pk_template) {
		String nodeName = node.getNodeName();
		UILayout layout = null;
		if (nodeName.equals(FLOWV_LAYOUT)) {
			layout = new UIFlowvLayout();
			String id = getAttributeValue(node, UIFlowvLayout.ID);
			layout.setAttribute(UIFlowvLayout.ID, id);
			String widgetId = getAttributeValue(node, UIFlowvLayout.WIDGET_ID);
			layout.setAttribute(UIFlowvLayout.WIDGET_ID, widgetId);
		} 
		else if (nodeName.equals(PANEL_LAYOUT)) {
			layout = new UIPanel();
			String id = getAttributeValue(node, ID);
			layout.setAttribute(ID, id);
			String className = getAttributeValue(node, UIPanel.CLASSNAME);
			layout.setAttribute(UIPanel.CLASSNAME, className);
			String title = getAttributeValue(node, UIPanel.TITLE);
			layout.setAttribute(UIPanel.TITLE, title);
		} 
		else if (nodeName.equals(CANVAS_LAYOUT)) {
			layout = new UICanvas();
			String className = getAttributeValue(node, UICanvas.CLASSNAME);
			if(className != null && !className.equals(""))
				layout.setAttribute(UICanvas.CLASSNAME, className);
			
			String title = getAttributeValue(node, UICanvas.TITLE);
			if(title != null)
				layout.setAttribute(UICanvas.TITLE, title);
		}
		else if (nodeName.equals(FLOWH_LAYOUT)) {
			layout = new UIFlowhLayout();
			String isAuto = getAttributeValue(node, UIFlowhLayout.AUTO_FILL);
			if (isAuto != null && isAuto.equals("true"))
				layout.setAttribute(UIFlowhLayout.AUTO_FILL, UIConstant.TRUE);
			else
				layout.setAttribute(UIFlowhLayout.AUTO_FILL, UIConstant.FALSE);
		} 
		else if (nodeName.equals(CARD_LAYOUT)) {
			layout = new UICardLayout();
			String id = getAttributeValue(node, ID);
			String widgetId = getAttributeValue(node, WIDGET_ID);
			((UICardLayout) layout).setWidgetId(widgetId);
			((UICardLayout) layout).setId(id);
			String currentItem = getAttributeValue(node, UICardLayout.CURRENT_ITEM);
			if (currentItem != null) {
//				String showValue = "cardpanel1";
//				if (currentItem.equals("1"))
//					showValue = "cardpanel2";
//				((UICardLayout) layout).setCurrentItem(showValue);
				((UICardLayout) layout).setCurrentItem(currentItem);
			}else{
				((UICardLayout) layout).setCurrentItem("0");
			}
		} else if (nodeName.equals(SPLIT_LAYOUT)) {
			UISplitter spliter = new UISplitter();
			String id = getAttributeValue(node, ID);
			spliter.setId(id);
			String widgetId = getAttributeValue(node, WIDGET_ID);
			spliter.setWidgetId(widgetId);

			String orient = getAttributeValue(node, UISplitter.ORIENTATION);
			if (orient.equals("h"))
				spliter.setOrientation(Integer.valueOf(1));
			else
				spliter.setOrientation(Integer.valueOf(0));

			String boundMode = getAttributeValue(node, UISplitter.BOUNDMODE);
			if (boundMode != null && !boundMode.equals(""))
				spliter.setBoundMode(Integer.valueOf(boundMode));
			String inverseValue = getAttributeValue(node, UISplitter.INVERSE);
			if (inverseValue != null && inverseValue.equals("true")){
				spliter.setInverse(UIConstant.TRUE);
			}
			else if(inverseValue != null && inverseValue.equals("false"))
				spliter.setInverse(UIConstant.FALSE);	
			String divideSize = getAttributeValue(node, UISplitter.DIVIDE_SIZE);
			if (divideSize != null)
				spliter.setDivideSize(divideSize);
//			String spliterWidth = getAttributeValue(node, UISplitter.SPLITERWIDTH);
//			if (spliterWidth != null)
//				spliter.setSpliterWidth(spliterWidth);
//			else
//				spliter.setSpliterWidth("4");
//			String hideBar = getAttributeValue(node, UISplitter.HIDEBAR);
//			if (hideBar != null && hideBar.equals("true"))
//				spliter.setHideBar(Integer.valueOf(1));
//			else
//				spliter.setHideBar(Integer.valueOf(0));

			String oneTouch = getAttributeValue(node, UISplitter.ONETOUCH);
			if (oneTouch != null && oneTouch.equals("true")) {
				spliter.setOneTouch(UIConstant.TRUE);
			} 
			else
				spliter.setOneTouch(UIConstant.FALSE);
			layout = spliter;
		} else if (nodeName.equals(MENUGROUP_LAYOUT)) {
			layout = new UIMenuGroup();
			String id = getAttributeValue(node, ID);
			layout.setId(id);
		} else if (nodeName.equals(BORDER)) {
			layout = new UIBorder();
			String width = getAttributeValue(node, UIBorder.WIDTH);
			layout.setAttribute("width", width);
			String leftWidth = getAttributeValue(node, UIBorder.LEFTWIDTH);
			layout.setAttribute("leftWidth", leftWidth);
			String rightWidth = getAttributeValue(node, UIBorder.RIGHTWIDTH);
			layout.setAttribute("rightWidth", rightWidth);
			String topWidth = getAttributeValue(node, UIBorder.TOPWIDTH);
			layout.setAttribute("topWidth", topWidth);
			String bottomWidth = getAttributeValue(node, UIBorder.BOTTOMWIDTH);
			layout.setAttribute("bottomWidth", bottomWidth);
			// widgetId
			String widgetId = getAttributeValue(node, UIBorder.WIDGET_ID);
			layout.setAttribute(UIBorder.WIDGET_ID, widgetId);

			String showLeft = getAttributeValue(node, UIBorder.SHOWLEFT);
			if (showLeft == null)
				((UIBorder) layout).setShowLeft(0);
			else {
				((UIBorder) layout).setShowLeft(showLeft.equals("true") ? 0 : 1);
			}
			String showRight = getAttributeValue(node, UIBorder.SHOWRIGHT);
			if (showRight == null)
				((UIBorder) layout).setShowRight(0);
			else {
				((UIBorder) layout).setShowRight(showRight.equals("true") ? 0 : 1);
			}

			String showTop = getAttributeValue(node, UIBorder.SHOWTOP);
			if (showTop == null)
				((UIBorder) layout).setShowTop(0);
			else {
				((UIBorder) layout).setShowTop(showTop.equals("true") ? 0 : 1);
			}

			String showBottom = getAttributeValue(node, UIBorder.SHOWBOTTOM);
			if (showBottom == null)
				((UIBorder) layout).setShowBottom(0);
			else {
				((UIBorder) layout).setShowBottom(showBottom.equals("true") ? 0 : 1);
			}

			String id = getAttributeValue(node, UIBorder.ID);
			layout.setAttribute(UIBorder.ID, id);

			String color = getAttributeValue(node, UIBorder.COLOR);
			layout.setAttribute(UIBorder.COLOR, color);

			String leftcolor = getAttributeValue(node, UIBorder.LEFTCOLOR);
			layout.setAttribute(UIBorder.LEFTCOLOR, leftcolor);

			String rightcolor = getAttributeValue(node, UIBorder.RIGHTCOLOR);
			layout.setAttribute(UIBorder.RIGHTCOLOR, rightcolor);

			String topcolor = getAttributeValue(node, UIBorder.TOPCOLOR);
			layout.setAttribute(UIBorder.TOPCOLOR, topcolor);

			String bottomcolor = getAttributeValue(node, UIBorder.BOTTOMCOLOR);
			layout.setAttribute(UIBorder.BOTTOMCOLOR, bottomcolor);

			String className = getAttributeValue(node, UIBorder.CLASSNAME);
			layout.setAttribute(UIBorder.CLASSNAME, className);
			
			String roundBorder = getAttributeValue(node, UIBorder.ROUNDBORDER);
			if(roundBorder != null && !roundBorder.equals("")){
				((UIBorder)layout).setRoundBorder(roundBorder.equals("true") ? 0 : 1);
			}
			else
				((UIBorder)layout).setRoundBorder(1);

		}
		else if (nodeName.equals(TAB_LAYOUT)) {
			layout = new UITabComp();
			String id = getAttributeValue(node, ID);
			((UITabComp) layout).setId(id);

			String widgetId = getAttributeValue(node, WIDGET_ID);
			((UITabComp) layout).setWidgetId(widgetId);

			String currentIndex = getAttributeValue(node, UITabComp.CURRENT_ITEM);
			if (currentIndex != null)
				((UITabComp) layout).setCurrentItem(currentIndex);
			
			String tabType = getAttributeValue(node, UITabComp.TAB_TYPE);
			if (tabType != null)
				((UITabComp) layout).setTabType(tabType);

			String oneTabHide = getAttributeValue(node, "oneTabHide");
			if (oneTabHide != null && !oneTabHide.equals("")) {
				((UITabComp) layout).setOneTabHide(oneTabHide.equals("true") ? 1 : 0);
			}
		} else if (nodeName.equals(SHUTTER_LAYOUT)) {
			layout = new UIShutter();
			String id = getAttributeValue(node, ID);
			((UIShutter) layout).setId(id);

			String widgetId = getAttributeValue(node, WIDGET_ID);
			((UIShutter) layout).setWidgetId(widgetId);

			String className = getAttributeValue(node, UIShutter.CLASSNAME);
			((UIShutter) layout).setClassName(className);

			String currentItem = getAttributeValue(node, "currentItem");
			if (currentItem != null && !currentItem.equals("")) {
				((UIShutter) layout).setCurrentItem(currentItem);
			}
		} else if (nodeName.equals(GRID_LAYOUT)) {
			layout = new UIGridLayout();
			String id = getAttributeValue(node, ID);
			((UIGridLayout) layout).setId(id);

			String widgetId = getAttributeValue(node, WIDGET_ID);
			((UIGridLayout) layout).setWidgetId(widgetId);

			// String width = getAttributeValue(node, UIGridLayout.WIDTH);
			// if(width != null)
			// ((UIGridLayout)layout).setWidth(width);
			// String height = getAttributeValue(node, UIGridLayout.HEIGHT);
			// if(height != null)
			// ((UIGridLayout)layout).setHeight(height);
			String border = getAttributeValue(node, UIGridLayout.BORDER);
			if (border != null) {
				((UIGridLayout) layout).setBorder(border);
			}
			String colCount = getAttributeValue(node, UIGridLayout.COLCOUNT);
			if (colCount != null) {
				((UIGridLayout) layout).setColcount(Integer.valueOf(colCount));
			}

			String rowCount = getAttributeValue(node, UIGridLayout.ROWCOUNT);
			if (rowCount != null) {
				((UIGridLayout) layout).setRowcount(Integer.valueOf(rowCount));
			}
		} else
			return null;
		// modify by renxh
		String id = getAttributeValue(node, ID);
		if (id != null)
			layout.setAttribute(ID, id);
		String widgetId = getAttributeValue(node, WIDGET_ID);
		if (widgetId != null)
			layout.setAttribute(WIDGET_ID, widgetId);

		if (nodeName.equals(GRID_LAYOUT)) {
			NodeList list = node.getChildNodes();
			int size = list.getLength();
			for (int i = 0; i < size; i++) {
				Node child = list.item(i);
				if (child.getNodeType() == Node.TEXT_NODE)
					continue;
				if(pk_template != null)
					parseGirdRowLayout(child, (UIGridLayout) layout, pk_template);
				else 
					parseGirdRowLayout(child, (UIGridLayout) layout, null);
			}
			return layout;
		} else {
			NodeList list = node.getChildNodes();
			int size = list.getLength();
			for (int i = 0; i < size; i++) {
				Node child = list.item(i);
				if (child.getNodeType() == Node.TEXT_NODE)
					continue;
				if(pk_template != null)
					parseLayoutPanel(child, layout, pk_template);
				else
					parseLayoutPanel(child, layout, null);
			}
			return layout;
		}

	}

	/**
	 * 2011-9-8 下午06:29:02 renxh des：grid布局，行对应的UI
	 * 
	 * @param node
	 * @param parent
	 */
	public void parseGirdRowLayout(Node node, UIGridLayout parent, String pk_template) {
		String nodeName = node.getNodeName();
		if (nodeName.equals(GRID_ROW)) {
			UIGridRowLayout layout = new UIGridRowLayout();
			String id = getAttributeValue(node, ID);
			layout.setId(id);

			String widgetId = getAttributeValue(node, WIDGET_ID);
			layout.setAttribute(WIDGET_ID, widgetId);

			String rowHeight = getAttributeValue(node, UIGridRowLayout.ROWHEIGHT);
			layout.setRowHeight(rowHeight);
			NodeList list = node.getChildNodes();
			int size = list.getLength();
			for (int i = 0; i < size; i++) {
				Node child = list.item(i);
				if (child.getNodeType() == Node.TEXT_NODE)
					continue;
				if(pk_template != null)
					parseLayoutPanel(child, layout, pk_template);
				else
					parseLayoutPanel(child, layout, null);
			}
			parent.addGridRow(layout);
		} else {
			LfwLogger.error("错误的节点结构!");
		}
	}

	private void parseLayoutPanel(Node panelNode, UILayout parent, String pk_template) {
		UILayoutPanel panel = null;
		String nodeName = panelNode.getNodeName();
		if(EVENTS.equals(nodeName)){
			parseEvents(panelNode, parent);
			EventConf[] events = meta.getEventConfs();
			List<EventConf> list = new ArrayList<EventConf>();
			if(events != null){
				for(EventConf event : events){
					list.add(event);
				}
			}
			events = parent.getEventConfs();
			if(events != null){
				for(EventConf event : events){
					list.add(event);
				}
			}
			meta.removeAllEventConf();
			for(EventConf event : list){
				meta.addEventConf(event);
			}
			return;
		}
		
		if (nodeName.equals(PANEL_PANEL)) {
			UIPanelPanel panelLayout = new UIPanelPanel();
			panel = panelLayout;
		}
		
		if (nodeName.equals(CANVAS_PANEL)) {
			UICanvasPanel panelLayout = new UICanvasPanel();
			panel = panelLayout;
		}
		else if (nodeName.equals(FLOWV_PANEL)) {
			UIFlowvPanel flowvLayout = new UIFlowvPanel();
			String height = getAttributeValue(panelNode, "height");
			if (height != null)
				flowvLayout.setHeight(height);
			String anchor = getAttributeValue(panelNode, "anchor");
			if (anchor != null)
				flowvLayout.setAnchor(anchor);
			panel = flowvLayout;
		}else if (nodeName.equals(FLOWH_PANEL)) {
			UIFlowhPanel flowhLayout = new UIFlowhPanel();
			String width = getAttributeValue(panelNode, "width");
			if (width != null)
				flowhLayout.setWidth(width);
			panel = flowhLayout;
		}else if (nodeName.equals(CARD_PANEL)) {
			UICardPanel cardLayout = new UICardPanel();
			cardLayout.setId(getAttributeValue(panelNode, ID));
			panel = cardLayout;
		}else if (nodeName.equals(SPLIT_ONE)) {
			UISplitterOne one = new UISplitterOne();
			panel = one;
		}else if (nodeName.equals(SPLIT_TWO)) {
			UISplitterTwo two = new UISplitterTwo();
			panel = two;
		}else if (nodeName.equals(BORDER_TRUE)) {
			UIBorderTrue border = new UIBorderTrue();
			panel = border;
		}else if (nodeName.equals(TAB_RIGHT)) {
			UITabRightPanel item = new UITabRightPanel();
			String width = getAttributeValue(panelNode, UITabRightPanel.WIDTH);
			if(width != null && !width.equals(""))
				item.setWidth(width);
			panel = item;
		}else if((TAB_ITEMS).equals(nodeName)){
			NodeList nl = panelNode.getChildNodes();
			if(nl != null && nl.getLength() > 0){
				for(int i=0;i<nl.getLength();i++){
					if(TAB_ITEM.equals(nl.item(i).getNodeName())){
						if(pk_template != null)
							parseLayoutPanel(nl.item(i), parent, pk_template);
						else
							parseLayoutPanel(nl.item(i), parent, null);
					}
				}
			}
			return;
		}else if (nodeName.equals(TAB_ITEM)) {
			UITabItem item = new UITabItem();
			String id = getAttributeValue(panelNode, UITabItem.ID);
			item.setId(id);

			String text = getAttributeValue(panelNode, UITabItem.TEXT);
			item.setText(text);

			String i18nName = getAttributeValue(panelNode, UITabItem.I18NNAME);
			item.setI18nName(i18nName);
			
			String showCloseIcon = getAttributeValue(panelNode, UITabItem.SHOWCLOSEICON);
			item.setShowCloseIcon(showCloseIcon);

			String active = getAttributeValue(panelNode, UITabItem.ACTIVE);
			item.setActive(active);
			
			String state = getAttributeValue(panelNode, UITabItem.STATE);
			if (state != null && !state.equals("")) {
				item.setState(Integer.valueOf(state));
			}
			panel = item;
		}else if((SHUTTER_ITEMS).equals(nodeName)){
			NodeList nl = panelNode.getChildNodes();
			if(nl != null && nl.getLength() > 0){
				for(int i=0;i<nl.getLength();i++){
					if(SHUTTER_ITEM.equals(nl.item(i).getNodeName())){
						if(pk_template != null)
							parseLayoutPanel(nl.item(i), parent, pk_template);
						else
							parseLayoutPanel(nl.item(i), parent, null);
					}
				}
			}
			return;
		}else if (nodeName.equals(SHUTTER_ITEM)) {
			UIShutterItem item = new UIShutterItem();
			String id = getAttributeValue(panelNode, UIShutterItem.ID);
			item.setId(id);

			String text = getAttributeValue(panelNode, UIShutterItem.TEXT);
			item.setText(text);

			String i18nName = getAttributeValue(panelNode, UIShutterItem.I18NNAME);
			item.setI18nName(i18nName);

			panel = item;
		}else if (nodeName.equals(MENUGROUP_ITEM)) {
			UIMenuGroupItem groupItem = new UIMenuGroupItem();
			String state = getAttributeValue(panelNode, STATE);
			if (state != null)
				groupItem.setState(Integer.valueOf(state));
			panel = groupItem;
		}else if (nodeName.equals(GRID_PANEL)) {
			UIGridPanel gridPanel = new UIGridPanel();
			String rowSpan = getAttributeValue(panelNode, UIGridPanel.ROWSPAN);
			if (rowSpan != null) {
				gridPanel.setRowSpan(rowSpan);
			}

			String colSpan = getAttributeValue(panelNode, UIGridPanel.COLSPAN);
			if (colSpan != null)
				gridPanel.setColSpan(colSpan);

			String colWidth = getAttributeValue(panelNode, UIGridPanel.COLWIDTH);
			if (colWidth != null)
				gridPanel.setColWidth(colWidth);

			String colHeight = getAttributeValue(panelNode, UIGridPanel.COLHEIGHT);
			if (colHeight != null)
				gridPanel.setColHeight(colHeight);
			String id = getAttributeValue(panelNode, UIGridPanel.ID);
			if (id != null)
				gridPanel.setId(id);

			panel = gridPanel;
		}
		String id = getAttributeValue(panelNode, ID);
		if (id != null)
			panel.setAttribute(UILayoutPanel.ID, id);
		// modify by chouhl: set widgetId to attribute
		String widgetId = getAttributeValue(panelNode, WIDGET_ID);
		if (widgetId != null) {
			panel.setAttribute(WIDGET_ID, widgetId);
		}
		
		String leftPadding = getAttributeValue(panelNode, UILayoutPanel.LEFTPADDING);
		if(leftPadding != null && !leftPadding.equals(""))
			panel.setLeftPadding(leftPadding);
		String rightPadding = getAttributeValue(panelNode, UILayoutPanel.RIGHTPADDING);
		if(rightPadding != null && !rightPadding.equals(""))
			panel.setRightPadding(rightPadding);
		
		String topPadding = getAttributeValue(panelNode, UILayoutPanel.TOPPADDING);
		if(topPadding != null && !topPadding.equals(""))
			panel.setTopPadding(topPadding);
		String bottomPadding = getAttributeValue(panelNode, UILayoutPanel.BOTTOMPADDING);
		if(bottomPadding != null && !bottomPadding.equals(""))
			panel.setBottomPadding(rightPadding);
		
		String leftBorder = getAttributeValue(panelNode, UILayoutPanel.LEFTBORDER);
		if(leftBorder != null && !leftBorder.equals(""))
			panel.setLeftBorder(leftBorder);
		
		String rightBorder = getAttributeValue(panelNode, UILayoutPanel.RIGHTBORDER);
		if(rightBorder != null && !rightBorder.equals(""))
			panel.setRightBorder(rightBorder);
		
		String topBorder = getAttributeValue(panelNode, UILayoutPanel.TOPBORDER);
		if(topBorder != null && !topBorder.equals(""))
			panel.setTopBorder(topBorder);
		
		String bottomBorder = getAttributeValue(panelNode, UILayoutPanel.BOTTOMBORDER);
		if(bottomBorder != null && !bottomBorder.equals(""))
			panel.setBottomBorder(bottomBorder);
		
		String border = getAttributeValue(panelNode, UILayoutPanel.BORDER);
		if(border != null && !border.equals(""))
			panel.setBorder(border);

		NodeList list = panelNode.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node.getNodeType() == Node.TEXT_NODE)
				continue;
			if(pk_template != null)
				panel.setElement(parseLayoutOrComp(node, pk_template));
			else
				panel.setElement(parseLayoutOrComp(node, null));
		}
		parent.addPanel(panel);
	}

	private UIElement parseLayoutOrComp(Node node, String pk_template) {
		UIElement ele;
		if (node.getNodeName().endsWith("Layout") || node.getNodeName().equals(SPLIT_LAYOUT) || node.getNodeName().equals(MENUGROUP_LAYOUT) || node.getNodeName().equals(BORDER))
			ele = parseLayout(node, pk_template);
		else if (node.getNodeName().equals(UIWIDGET))
			if(pk_template != null)
				ele = parseWidget(node, pk_template);
			else
				ele = parseWidget(node);
		else
			ele = parseComponent(node);

		return ele;
	}

	
	private UIElement parseWidget(Node node) {
		if (!node.getNodeName().equals(UIWIDGET) && !(node.getNodeName().equals(UIDIALOG)))
			return null;
		String id = getAttributeValue(node, "id");
		UIMetaParserUtil util = new UIMetaParserUtil();
		util.setPagemeta(pagemeta);
		UIMeta childMeta = null;
		
		String folderPath = meta.getFolderPath() + "/" + id;
		childMeta = util.parseUIMeta(folderPath, id);
		
		UIWidget widget = new UIWidget();
		widget.setId(id);
		widget.setUimeta(childMeta);
		return widget;
	}
	
	private UIElement parseWidget(Node node, String pk_template) {
		if (!node.getNodeName().equals(UIWIDGET) && !(node.getNodeName().equals(UIDIALOG)))
			return null;
		String id = getAttributeValue(node, "id");
		UIMetaParserUtil util = new UIMetaParserUtil();
		util.setPagemeta(pagemeta);
		UIMeta childMeta = null;
		
		if(pk_template != null){
			LfwRuntimeEnvironment.setFromDB(true);
		}
		if(LfwRuntimeEnvironment.isFromDB()){
			IUwTemplateService service = NCLocator.getInstance().lookup(IUwTemplateService.class);
			try {
				UwViewVO vo = service.getViewVO(id, pk_template);
				if(vo == null)
					return null;
				InputStream input = getInput(vo.doGetUIMetaStr());
				childMeta = util.parseUIMeta(input, id, pk_template);
			} catch (PaBusinessException e) {
				LfwLogger.error(e.getMessage(), e);
				throw new LfwRuntimeException(e.getMessage(), e);
			}
		}
		UIWidget widget = new UIWidget();
		widget.setId(id);
		widget.setUimeta(childMeta);
		return widget;
	}
	
	private UIElement parseDialog(Node node) {
		if (!node.getNodeName().equals(UIWIDGET) && !(node.getNodeName().equals(UIDIALOG)))
			return null;
		String id = getAttributeValue(node, "id");
		UIMetaParserUtil util = new UIMetaParserUtil();
		util.setPagemeta(pagemeta);
		
		UIMeta childMeta = null;
		
		String folderPath = meta.getFolderPath() + "/" + id;
		childMeta = util.parseUIMeta(folderPath, id);
		
		UIDialog widget = new UIDialog();
		widget.setId(id);
		widget.setUimeta(childMeta);
		
		String width = getAttributeValue(node, "width");
		String height = getAttributeValue(node, "height");
		if(width != null && !width.equals(""))
			widget.setWidth(width);
		if(height != null && !height.equals(""))
			widget.setHeight(height);
		return widget;
	}
	private UIWidget parseDialog(Node node, String pk_template) {
		if (!node.getNodeName().equals(UIWIDGET) && !(node.getNodeName().equals(UIDIALOG)))
			return null;
		String id = getAttributeValue(node, "id");
		UIMetaParserUtil util = new UIMetaParserUtil();
		util.setPagemeta(pagemeta);
		UIMeta childMeta = null;
		
		if(pk_template != null){
			LfwRuntimeEnvironment.setFromDB(true);
		}
		if(LfwRuntimeEnvironment.isFromDB()){
			IUwTemplateService service = NCLocator.getInstance().lookup(IUwTemplateService.class);
			try {
				UwViewVO vo = service.getViewVO(id, pk_template);
				if(vo == null)
					return null;
				InputStream input = getInput(vo.doGetUIMetaStr());
				childMeta = util.parseUIMeta(input, id, pk_template);
			} catch (PaBusinessException e) {
				LfwLogger.error(e.getMessage(), e);
				throw new LfwRuntimeException(e.getMessage(), e);
			}
		}
		UIDialog widget = new UIDialog();
		widget.setId(id);
		widget.setUimeta(childMeta);
		
		String width = getAttributeValue(node, "width");
		String height = getAttributeValue(node, "height");
		if(width != null && !width.equals(""))
			widget.setWidth(width);
		if(height != null && !height.equals(""))
			widget.setHeight(height);
		return widget;
	}

	private UIComponent parseComponent(Node node) {
		UIComponent comp = null;
		if (node.getNodeName().equals(UIGRID)) {
			comp = new UIGridComp();
		} else if (node.getNodeName().equals(UIEXCEL)) {
			comp = new UIExcelComp();
		} else if (node.getNodeName().equals(UITREE)) {
			comp = new UITreeComp();
		}

		else if (node.getNodeName().equals(UIFORM)) {
			comp = new UIFormComp();
			((UIFormComp)comp).setLabelPosition(getAttributeValue(node, UIFormComp.LABEL_POSITION));
		}
		else if (node.getNodeName().equals(SILVERLIGHTWIDGET)) {
			comp = new UISilverlightWidget();
		}
		else if (node.getNodeName().equals(UIFORMGROUP)) {
			comp = new UIFormGroupComp();
			String forms = getAttributeValue(node, "formList");
			((UIFormGroupComp) comp).setForms(forms);
		}

		else if (node.getNodeName().equals(UIMENUBAR)) {
			comp = new UIMenubarComp();
		}

		else if (node.getNodeName().equals(UIIFRAME)) {
			comp = new UIIFrame();
		}else if(node.getNodeName().equals(UIHTMLCONTENT)){
			comp = new UIPartComp();
		}else if (node.getNodeName().equals(UITEXT)) {
			comp = new UITextField();
			String type = getAttributeValue(node, "type");
			((UITextField) comp).setType(type);
			((UITextField) comp).setImgsrc(getAttributeValue(node, UITextField.IMG_SRC));
		} else if (node.getNodeName().equals(UIBUTTON)) {
			comp = new UIButton();
		} else if (node.getNodeName().equals(UICHARTCOMP))
			comp = new UIChartComp();
		else if (node.getNodeName().equals(UILINKCOMP)) {
			comp = new UILinkComp();
		} else if (node.getNodeName().equals(UITOOLBAR)) {
			comp = new UIToolBar();
		} else if (node.getNodeName().equals(UILABEL)) {
			UILabelComp lc = new UILabelComp();
			lc.setTextAlign(getAttributeValue(node, UILabelComp.TEXTALIGN));
			comp = lc;
		} else if (node.getNodeName().equals(UIIMAGE)) {
			comp = new UIImageComp();
		} else if (node.getNodeName().equals(UISELFDEFCOMP)) {
			comp = new UISelfDefComp();
		} else if (node.getNodeName().equals(UIFORMELEMENT)) {
			UIFormElement fe = new UIFormElement();
			fe.setFormId(getAttributeValue(node, UIFormElement.FORM_ID));
			
			String eleWidth = getAttributeValue(node, UIFormElement.ELEWIDTH);
			if(eleWidth != null && !eleWidth.equals(""))
				fe.setEleWidth(eleWidth);
			comp = fe;
		}
		
		if (comp == null)
			return null;
		String id = getAttributeValue(node, ID);
		comp.setId(id);
//		comp.setId(id);

		String widgetId = getAttributeValue(node, WIDGET_ID);
		comp.setWidgetId(widgetId);
		
		String width = getAttributeValue(node, "width");
		if(width != null && !width.equals(""))
			comp.setWidth(width);

		String height = getAttributeValue(node, "height");
		if(height != null && !height.equals(""))
			comp.setHeight(height);

		String align = getAttributeValue(node, "align");
		if(align != null && !align.equals(""))
			comp.setAlign(align);
		
		String left = getAttributeValue(node, "left");
		if(left != null && !left.equals(""))
			comp.setLeft(left);
		
		String top = getAttributeValue(node, "top");
		if(top != null && !top.equals(""))
			comp.setTop(top);
		
		String position = getAttributeValue(node, "position");
		if(position != null && !position.equals(""))
			comp.setPosition(position);
		
		return comp;
	}

	// private void parseTabItem(Node node, UITabComp comp) {
	// NodeList list = node.getChildNodes();
	// int size = list.getLength();
	// for (int i = 0; i < size; i++) {
	// Node child = list.item(i);
	// if(child.getNodeType() == Node.TEXT_NODE)
	// continue;
	// if(child.getNodeName().equals(TAB_ITEM)){
	// UITabItem item = new UITabItem();
	// String id = getAttributeValue(node, ID);
	// item.setId(id);
	// comp.addPanel(item);
	//				
	// int csize = child.getChildNodes().getLength();
	// for (int j = 0; j < csize; j++) {
	// Node cc = child.getChildNodes().item(j);
	// if(cc.getNodeType() == Node.TEXT_NODE)
	// continue;
	// item.setElement(parseLayoutOrComp(cc));
	// }
	// }
	// }
	// }

	private static final String EVENT_TAG_EVENTS = "Events";
	private static final String EVENT_TAG_EVENT = "Event";
	private static final String EVENT_PROP_NAME = "name";
	private static final String EVENT_PROP_METHOD_NAME = "methodName";
	private static final String EVENT_PROP_CONTROLLER_CLAZZ = "controllerClazz";
	private static final String EVENT_PROP_JS_EVENT_CLAZZ = "jsEventClaszz";
	private static final String EVENT_PROP_ON_SERVER = "onserver";
	private static final String EVENT_PROP_ASYNC = "async";
	
	private static final String EVENT_TAG_SUBMIT_RULE = "SubmitRule";
	private static final String EVENT_TAG_PARAMS = "Params";
	private static final String EVENT_TAG_ACTION = "Action";
	private static final String SUBMIT_RULE_PROP_CARD_SUBMIT = "cardSubmit";
	private static final String SUBMIT_RULE_PROP_TAB_SUBMIT = "tabSubmit";
	private static final String SUBMIT_RULE_PROP_PANEL_SUBMIT = "panelSubmit";
	private static final String SUBMIT_RULE_PROP_PAGEMETA = "pagemeta";
	
	/**
	 * Event DOM解析
	 * @param node
	 * @param element
	 */
	public void parseEvents(Node node, UIElement element){
		if(node == null || element == null){
			return;
		}
		if(EVENT_TAG_EVENTS.equals(node.getNodeName())){
			NodeList nl = node.getChildNodes();
			if(nl != null && nl.getLength() > 0){
//				List<EventConf> list = new ArrayList<EventConf>();
				element.removeAllEventConf();
				for(int i=0; i<nl.getLength(); i++){
					if(EVENT_TAG_EVENT.equals(nl.item(i).getNodeName())){
						EventConf event = new EventConf();
						event.setAsync(Boolean.valueOf(getAttributeValue(nl.item(i), EVENT_PROP_ASYNC)));
						event.setOnserver(Boolean.valueOf(getAttributeValue(nl.item(i), EVENT_PROP_ON_SERVER)));
						event.setName(getAttributeValue(nl.item(i), EVENT_PROP_NAME));
						event.setMethodName(getAttributeValue(nl.item(i), EVENT_PROP_METHOD_NAME));
						event.setControllerClazz(getAttributeValue(nl.item(i), EVENT_PROP_CONTROLLER_CLAZZ));
						event.setJsEventClaszz(getAttributeValue(nl.item(i), EVENT_PROP_JS_EVENT_CLAZZ));
						NodeList nl1 = nl.item(i).getChildNodes();
						if(nl1 != null && nl1.getLength() > 0){
							for(int j=0; j<nl1.getLength(); j++){
								if(EVENT_TAG_SUBMIT_RULE.equals(nl1.item(j).getNodeName())){
									event.setSubmitRule(parseSubmitRule(nl1.item(j)));
								}else if(EVENT_TAG_PARAMS.equals(nl1.item(j).getNodeName())){
									NodeList params = nl1.item(j).getChildNodes();
									if(params != null && params.getLength() > 0){
										Map<String, Parameter> map = parseParameter(params);
										Iterator<String> keys = map.keySet().iterator();
										while(keys.hasNext()){
											event.addParam(map.get(keys.next()));
										}
									}
								}else if(EVENT_TAG_ACTION.equals(nl1.item(j).getNodeName())){
									event.setScript(nl1.item(j).getTextContent().trim());
								}
							}
						}
//						list.add(event);
						element.addEventConf(event);
					}
				}
//				element.setAttribute(UIElement.CONTROLLER_EVENT, list.toArray(new EventConf[0]));
			}
		}
	}
	
	private EventSubmitRule parseSubmitRule(Node node){
		EventSubmitRule rule = null;
		if(node != null && EVENT_TAG_SUBMIT_RULE.equals(node.getNodeName())){
			rule = new EventSubmitRule();
			rule.setPagemeta(getAttributeValue(node, SUBMIT_RULE_PROP_PAGEMETA));
			rule.setCardSubmit(Boolean.valueOf((getAttributeValue(node, SUBMIT_RULE_PROP_CARD_SUBMIT))));
			rule.setTabSubmit(Boolean.valueOf((getAttributeValue(node, SUBMIT_RULE_PROP_TAB_SUBMIT))));
			rule.setPanelSubmit(Boolean.valueOf((getAttributeValue(node, SUBMIT_RULE_PROP_PANEL_SUBMIT))));
			NodeList nl = node.getChildNodes();
			if(nl != null && nl.getLength() > 0){
				rule.setWidgetRules(parseWidgetRule(nl));
				rule.setParams(parseParameter(nl));
				for(int i=0; i<nl.getLength(); i++){
					if(EVENT_TAG_SUBMIT_RULE.equals(nl.item(i).getNodeName())){
						rule.setParentSubmitRule(parseSubmitRule(nl.item(i)));
					}
				}
			}
		}
		return rule;
	}
	
	private static final String SUBMIT_RULE_TAG_WIDGET = "Widget";
	private static final String WIDGET_RULE_PROP_ID = "id";
	private static final String WIDGET_RULE_PROP_CARD_SUBMIT = "cardSubmit";
	private static final String WIDGET_RULE_PROP_TAB_SUBMIT = "tabSubmit";
	private static final String WIDGET_RULE_PROP_PANEL_SUBMIT = "panelSubmit";
	
	private Map<String, WidgetRule> parseWidgetRule(NodeList nl){
		Map<String, WidgetRule> widgetRule = new HashMap<String, WidgetRule>();
		for(int i=0; i<nl.getLength(); i++){
			if(SUBMIT_RULE_TAG_WIDGET.equals(nl.item(i).getNodeName())){
				WidgetRule wr = new WidgetRule();
				wr.setId(getAttributeValue(nl.item(i), WIDGET_RULE_PROP_ID));
				wr.setCardSubmit(Boolean.valueOf((getAttributeValue(nl.item(i), WIDGET_RULE_PROP_CARD_SUBMIT))));
				wr.setTabSubmit(Boolean.valueOf((getAttributeValue(nl.item(i), WIDGET_RULE_PROP_TAB_SUBMIT))));
				wr.setPanelSubmit(Boolean.valueOf((getAttributeValue(nl.item(i), WIDGET_RULE_PROP_PANEL_SUBMIT))));
				NodeList children = nl.item(i).getChildNodes();
				if(children != null && children.getLength() > 0){
					Map<String, Object> rules = parseWidgetChildRule(children);
					Iterator<String> keys = rules.keySet().iterator();
					String key = null;
					while(keys.hasNext()){
						key = keys.next();
						if(key.startsWith(WIDGET_RULE_TAG_DATASET)){
							wr.addDsRule((DatasetRule)rules.get(key));
						}else if(key.startsWith(WIDGET_RULE_TAG_TREE)){
							wr.addTreeRule((TreeRule)rules.get(key));
						}else if(key.startsWith(WIDGET_RULE_TAG_GRID)){
							wr.addGridRule((GridRule)rules.get(key));
						}else if(key.startsWith(WIDGET_RULE_TAG_FORM)){
							wr.addFormRule((FormRule)rules.get(key));
						}else if(key.startsWith(WIDGET_RULE_TAG_EXCEL)){
							wr.addExcelRule((ExcelRule)rules.get(key));
						}
					}
				}
				widgetRule.put(wr.getId(), wr);
			}
		}
		return widgetRule;
	}
	
	private static final String WIDGET_RULE_TAG_DATASET = "Dataset";
	private static final String WIDGET_RULE_TAG_TREE = "Tree";
	private static final String WIDGET_RULE_TAG_GRID = "Grid";
	private static final String WIDGET_RULE_TAG_FORM = "Form";
	private static final String WIDGET_RULE_TAG_EXCEL = "Excel";
	private static final String OBJECT_RULE_PROP_ID = "id";
	private static final String OBJECT_RULE_PROP_TYPE = "type";
	
	private Map<String, Object> parseWidgetChildRule(NodeList nl){
		Map<String, Object> map = new HashMap<String, Object>();
		for(int i=0; i<nl.getLength(); i++){
			if(WIDGET_RULE_TAG_DATASET.equals(nl.item(i).getNodeName())){
				DatasetRule dr = new DatasetRule();
				dr.setId(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_ID));
				dr.setType(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_TYPE));
				map.put(WIDGET_RULE_TAG_DATASET + "_" + dr.getId(), dr);
			}else if(WIDGET_RULE_TAG_TREE.equals(nl.item(i).getNodeName())){
				TreeRule tr = new TreeRule();
				tr.setId(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_ID));
				tr.setType(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_TYPE));
				map.put(WIDGET_RULE_TAG_TREE + "_" + tr.getId(), tr);
			}else if(WIDGET_RULE_TAG_GRID.equals(nl.item(i).getNodeName())){
				GridRule gr = new GridRule();
				gr.setId(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_ID));
				gr.setType(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_TYPE));
				map.put(WIDGET_RULE_TAG_GRID + "_" + gr.getId(), gr);
			}else if(WIDGET_RULE_TAG_FORM.equals(nl.item(i).getNodeName())){
				FormRule fr = new FormRule();
				fr.setId(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_ID));
				fr.setType(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_TYPE));
				map.put(WIDGET_RULE_TAG_FORM + "_" + fr.getId(), fr);
			}else if(WIDGET_RULE_TAG_EXCEL.equals(nl.item(i).getNodeName())){
				ExcelRule er = new ExcelRule();
				er.setId(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_ID));
				er.setType(getAttributeValue(nl.item(i), OBJECT_RULE_PROP_TYPE));
				map.put(WIDGET_RULE_TAG_EXCEL + "_" + er.getId(), er);
			}
		}
		return map;
	}
	
	private static final String PARAMS_TAG_PARAM = "Param";
	private static final String PARAM_TAG_NAME = "Name";
	private static final String PARAM_TAG_VALUE = "Value";
	private static final String PARAM_TAG_DESC = "Desc";
	
	private Map<String, Parameter> parseParameter(NodeList nl){
		Map<String, Parameter> map = new HashMap<String, Parameter>();
		for(int i=0; i<nl.getLength(); i++){
			if(PARAMS_TAG_PARAM.equals(nl.item(i).getNodeName())){
				NodeList param = nl.item(i).getChildNodes();
				if(param != null && param.getLength() > 0){
					Parameter lfwParam = new Parameter();
					for(int k=0; k<param.getLength(); k++){
						if(PARAM_TAG_NAME.equals(param.item(k).getNodeName())){
							lfwParam.setName(param.item(k).getTextContent());
						}else if(PARAM_TAG_VALUE.equals(param.item(k).getNodeName())){
							lfwParam.setValue(param.item(k).getTextContent());
						}else if(PARAM_TAG_DESC.equals(param.item(k).getNodeName())){
							lfwParam.setDesc(param.item(k).getTextContent().trim());
						}
					}
					map.put(lfwParam.getName() + "_" + i, lfwParam);
				}
			}
		}
		return map;
	}
	
	private String getAttributeValue(Node node, String key) {
		Node attr = node.getAttributes().getNamedItem(key);
		if (attr == null)
			return null;
		return attr.getNodeValue();
	}

	//将String流的转换成input流
	private java.io.InputStream getInput(String metaStr) {
		java.io.InputStream input = null;
		try {
			input = new java.io.ByteArrayInputStream(metaStr.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}
		return input;
	}

}
