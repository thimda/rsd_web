package nc.uap.lfw.core.serializer.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.EventContextConstant;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.ToolBarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.ContextMenuContext;
import nc.uap.lfw.core.comp.ctx.MenubarContext;
import nc.uap.lfw.core.comp.ctx.PageUIContext;
import nc.uap.lfw.core.comp.ctx.ToolbarContext;
import nc.uap.lfw.core.comp.ctx.WidgetUIContext;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.ctx.WidgetContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.patch.XmlUtilPatch;
import nc.uap.lfw.core.serializer.IXml2ObjectSerializer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Xml2EventContextSerializer implements
		IXml2ObjectSerializer<LfwPageContext> {

	public LfwPageContext serialize(String xml, Map<String, Object> paramMap) {
		PageMeta pagemeta = LfwRuntimeEnvironment.getWebContext().getPageMeta();
		LfwPageContext ctx = serialize(xml, pagemeta);
		return ctx;
	}
	
	private LfwPageContext serialize(String xml, PageMeta pagemeta){
		try {
			Document doc = XmlUtilPatch.getDocumentBuilder().parse(
					new InputSource(new StringReader(xml)));
			Element rootNode = (Element) doc.getFirstChild();
			Element ctxNode = (Element) DomUtil.getChildNode(rootNode, EventContextConstant.eventcontext);
			return serialize(rootNode, ctxNode, pagemeta);
		}
		catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		} 
	}
	private LfwPageContext serialize(Node rootNode, Node ctxNode, PageMeta pagemeta) throws SAXException, IOException{
		LfwPageContext pageCtx = new LfwPageContext();
		pageCtx.setPageMeta(pagemeta);
		Element paramsNode = (Element) DomUtil.getChildNode(ctxNode,EventContextConstant.params);
		if (paramsNode != null) {
			Node[] allParams = DomUtil.getChildNodes(paramsNode,
					EventContextConstant.param);
			for (int i = 0; i < allParams.length; i++) {
				Element attEle = (Element) allParams[i];
				Element keyEle = (Element) DomUtil.getChildNode(attEle,
						EventContextConstant.key);
				String key = keyEle.getTextContent();
				Element valueEle = (Element) DomUtil.getChildNode(
						attEle, EventContextConstant.value);
				String value = valueEle.getTextContent();
				pageCtx.setParameter(key, value);
			}
		}
		
		
		Element attrNode = (Element) DomUtil.getChildNode(ctxNode, EventContextConstant.attributes);
		if (attrNode != null) {
			Node[] allAttrs = DomUtil.getChildNodes(attrNode, EventContextConstant.attribute);
			for (int i = 0; i < allAttrs.length; i++) {
				Element attEle = (Element) allAttrs[i];
				Element keyEle = (Element) DomUtil.getChildNode(attEle, EventContextConstant.key);
				String key = keyEle.getTextContent();
				Element valueEle = (Element) DomUtil.getChildNode(attEle, EventContextConstant.value);
				String value = valueEle.getTextContent();
				pageCtx.setClientAttribute(key, value);
			}
		}
		
		
//			Element eventNode = (Element) DomUtil.getChildNode(rootNode, "event");
//			if(eventNode != null){
//				paramsNode = (Element) DomUtil.getChildNode(eventNode, EventContextConstant.params);
//				if (paramsNode != null) {
//					Node[] allParams = DomUtil.getChildNodes(paramsNode, EventContextConstant.param);
//					for (int i = 0; i < allParams.length; i++) {
//						Element attEle = (Element) allParams[i];
//						Element keyEle = (Element) DomUtil.getChildNode(attEle,
//								EventContextConstant.key);
//						String key = keyEle.getTextContent();
//						Element valueEle = (Element) DomUtil.getChildNode(attEle,
//								EventContextConstant.value);
//						String value = valueEle.getTextContent();
//						pageCtx.addEventParam(key, value);
//					}
//				}
//			}
		
		String context = DomUtil.getChildNode(ctxNode, EventContextConstant.context)
				.getTextContent();
		PageUIContext ctx = (PageUIContext) LfwJsonSerializer.getInstance()
				.fromJsObject(context);
		pagemeta.setContext(ctx);

		Node[] widgets = DomUtil.getChildNodes(ctxNode, "widget");
		for (int i = 0; i < widgets.length; i++) {
			Element widgetEle = (Element) widgets[i];
			this.processwidget(pageCtx, pagemeta, widgetEle);
		}

		Node[] menubars = DomUtil.getChildNodes(ctxNode, "menubar");
		for (int i = 0; i < menubars.length; i++) {
			Element menubarEle = (Element) menubars[i];
			context = DomUtil.getChildNode(menubarEle, EventContextConstant.context).getTextContent();
			MenubarContext menubarContext = (MenubarContext) LfwJsonSerializer.getInstance().fromJsObject(context);
			MenubarComp menubar = pagemeta.getViewMenus().getMenuBar(menubarEle.getAttribute("id"));
			if(menubar == null){
				for (int j = 0; j < widgets.length; j++) {
					Element widgetEle = (Element) widgets[j];
					LfwWidget widget = pagemeta.getWidget(widgetEle.getAttribute("id"));
					menubar = widget.getViewMenus().getMenuBar(menubarEle.getAttribute("id"));
					if(menubar != null)
						break;
				}
			}
			menubar.setContext(menubarContext);
		}
		
//		NodeList allList = ctxNode.getChildNodes();// rootNode.getElementsByTagName("tab");
//		if (allList != null) {
//			for (int i = 0; i < allList.getLength(); i++) {
//				Element ele = (Element) allList.item(i);
//				if (ele.getNodeName().equals("tab"))
//					this.processTab(pageCtx, pagemeta, ele);
//				else if (ele.getNodeName().equals("card"))
//					this.processCardLayout(pageCtx, pagemeta, ele);
//				else if (ele.getNodeName().equals("panel"))
//					this.processPanelLayout(pageCtx, pagemeta, ele);
//			}
//		}
		
		if(rootNode != null){
			Node pcontextNode = DomUtil.getChildNode(rootNode, EventContextConstant.parentcontext);
			if(pcontextNode != null){
				PageMeta parentPm = LfwRuntimeEnvironment.getWebContext().getParentPageMeta();
	//				String pcontent = JsURLDecoder.decode(pcontextNode.getTextContent(), "UTF-8");
	//				//前台编码2次，后台也要做相应的反编码2次
	//				pcontent = JsURLDecoder.decode(pcontent, "UTF-8");
				
				LfwPageContext pcontext = serialize(null, pcontextNode.getFirstChild(), parentPm);
				pageCtx.setParentGlobalContext(pcontext);
			}
		}
		
		return pageCtx;
	}
	
//	private void processCardLayout(LfwPageContext pageCtx, PageMeta pagemeta,
//			Element ele) {
//		String context = ele.getElementsByTagName(EventContextConstant.context).item(0).getTextContent();
//		CardContext ctx = (CardContext) LfwJsonSerializer.getInstance().fromJsObject(context);
//		WebComponent component =  pagemeta.getViewConinters().getContainer(ctx.getId());
//		
//		CardLayout card = new CardLayout();
//		card.setContext(ctx);
//		if(component != null){
//			Map<String, JsListenerConf> listenerMap = component.getListenerMap();
//			card.setListenerMap(listenerMap);
//		}
//		pageCtx.addCard(card);
//		
//	}
	
//	private void processPanelLayout(LfwPageContext pageCtx, PageMeta pagemeta,
//			Element ele) {
//		String context = ele.getElementsByTagName(EventContextConstant.context).item(0).getTextContent();
//		PanelContext ctx = (PanelContext) LfwJsonSerializer.getInstance().fromJsObject(context);
//		WebComponent component =  pagemeta.getViewConinters().getContainer(ctx.getId());
//		PanelLayout panel = new PanelLayout();
//		panel.setContext(ctx);
//		if(component != null){
//			Map<String, JsListenerConf> listenerMap =  component.getListenerMap();
//			panel.setListenerMap(listenerMap);
//		}
//		pageCtx.addPanel(panel);
//		
//	}

//	private void processTab(LfwPageContext pageCtx, PageMeta pagemeta, Element ele) {		
//		String context = ele.getElementsByTagName(EventContextConstant.context).item(0).getTextContent();
//		TabContext ctx = (TabContext) LfwJsonSerializer.getInstance().fromJsObject(context);
//		
//		TabLayout tab = new TabLayout();
//		tab.setContext(ctx);
//		
//		WebComponent ct = pagemeta.getViewConinters().getContainer(ctx.getId());
//		if(ct != null){
//			Map<String, JsListenerConf> listenerMap = ct.getListenerMap();
//			tab.setListenerMap(listenerMap);
//		}
//		pageCtx.addTab(tab);
//	}


	protected void processwidget(LfwPageContext pageCtx, PageMeta pagemeta, Element widgetEle) throws SAXException, IOException{
		String widgetId = widgetEle.getAttribute("id");
		LfwWidget widget = pagemeta.getWidget(widgetId);
		if(widget == null){
			throw new LfwRuntimeException("根据ID没有找到对应的widget配置," + widgetId);
		}
		WidgetContext wCtx = new WidgetContext();
		wCtx.setId(widgetId);
		wCtx.setWidget(widget);
		pageCtx.addWidgetContext(wCtx);
		
		String init = widgetEle.getAttribute("init");
		if(init != null && init.equals("false")){
			return;
		}
		
		NodeList allList = widgetEle.getChildNodes();
		int size = allList.getLength();
		for (int i = 0; i < size; i++) {
			Node node = (Node) allList.item(i);
			if(node instanceof Text)
				continue;
			Element allEle = (Element) node;
			String nodeName = allEle.getNodeName();
			if(nodeName.equals(EventContextConstant.context)){
				 WidgetUIContext newWidget = (WidgetUIContext) LfwJsonSerializer.getInstance().fromJsObject(allEle.getTextContent());
				 widget.setContext(newWidget);
			}
//			else if(nodeName.equals("tab")){
//				String context = DomUtil.getChildNode(allEle, EventContextConstant.context).getTextContent();
//				TabContext tabCtx = (TabContext) LfwJsonSerializer.getInstance().fromJsObject(context);
//				WebComponent container = widget.getViewConinters().getContainer(tabCtx.getId());
//				Map<String, JsListenerConf> listenerMap = null;
//				if (null != container)
//					listenerMap = container.getListenerMap();
//				TabLayout tab = new TabLayout();
//				tab.setContext(tabCtx);
//				if (null != listenerMap)
//					tab.setListenerMap(listenerMap);
//				wCtx.addTab(tab);
//			}
//			else if(nodeName.equals("card")){
//				String context = DomUtil.getChildNode(allEle, EventContextConstant.context).getTextContent();
//				CardContext ctx = (CardContext) LfwJsonSerializer.getInstance().fromJsObject(context);
//				WebComponent container = widget.getViewConinters().getContainer(ctx.getId());
//				Map<String, JsListenerConf> listenerMap = null;
//				if (null != container)
//					listenerMap = container.getListenerMap();
//				CardLayout card = new CardLayout();
//				card.setContext(ctx);
//				if (null != listenerMap)
//					card.setListenerMap(listenerMap);
//				wCtx.addCard(card);
//			}
//			else if(nodeName.equals("panel")){
//				String context = DomUtil.getChildNode(allEle, EventContextConstant.context).getTextContent();
//				PanelContext ctx = (PanelContext) LfwJsonSerializer.getInstance().fromJsObject(context);
//				WebComponent container = widget.getViewConinters().getContainer(ctx.getId());
//				Map<String, JsListenerConf> listenerMap = null;
//				if (null != container)
//					listenerMap = container.getListenerMap();
//				PanelLayout panel = new PanelLayout();
//				panel.setContext(ctx);
//				if (null != listenerMap)
//					panel.setListenerMap(listenerMap);
//				wCtx.addPanel(panel);
//			}
			else if(nodeName.equals("dataset")){
				Xml2DatasetSerializer xml2datasetSerializer = new Xml2DatasetSerializer();
//				ViewModels viewModels = widget.getViewModels();
				//Dataset ds = (Dataset)viewModels.getDataset(allEle.getAttribute("id"));
				Node dataEle = DomUtil.getChildNode(allEle, "data");
				if(dataEle != null){
					String dsStr = dataEle.getTextContent();
					if(dsStr != null && !dsStr.equals("")){
						//Document doc = XMLUtil.getDocumentBuilder().parse(new InputSource(new StringReader(dsStr)));
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("pagemeta", pagemeta);
						xml2datasetSerializer.serialize(dsStr, paramMap);
					}
				}
			}
			else if(nodeName.equals("combodata")){
//				ComboData combodata = widget.getViewModels().getComboData(nodeName);
				//widget.getViewModels().addComboData(combodata);
			}
			else if(nodeName.equals("refnode")){
//				RefNode refnode = widget.getViewModels().getRefNode(nodeName);
//				String context = DomUtil.getChildNode(allEle, EventContextConstant.context).getTextContent();
//				WebElement comp = (WebElement) LfwJsonSerializer.getInstance().fromJsObject(context);
				//refnode.mergeProperties(comp);
				//widget.getViewModels().addRefNode(refnode);
			}
			else if(nodeName.equals("toolbar")){
				String context = DomUtil.getChildNode(allEle, EventContextConstant.context).getTextContent();
				ToolbarContext toolbarContext = (ToolbarContext) LfwJsonSerializer.getInstance().fromJsObject(context);
				ToolBarComp toolbar = (ToolBarComp) widget.getViewComponents().getComponent(allEle.getAttribute("id"));
				toolbar.setContext(toolbarContext);
			}
			else if(nodeName.equals("menubar")){
				String context = DomUtil.getChildNode(allEle, EventContextConstant.context).getTextContent();
				MenubarContext menubarContext = (MenubarContext) LfwJsonSerializer.getInstance().fromJsObject(context);
				MenubarComp menubar = widget.getViewMenus().getMenuBar(allEle.getAttribute("id"));
				menubar.setContext(menubarContext);
			}
			else if(nodeName.equals("ctxmenu")){
				String context = DomUtil.getChildNode(allEle, EventContextConstant.context).getTextContent();
				ContextMenuContext ctxMenuContext = (ContextMenuContext) LfwJsonSerializer.getInstance().fromJsObject(context);
				ContextMenuComp ctxmenu = widget.getViewMenus().getContextMenu(allEle.getAttribute("id"));
				ctxmenu.setContext(ctxMenuContext);
			}
			else{
				ViewComponents viewComponent = widget.getViewComponents();
				WebComponent webcomp = (WebComponent)viewComponent.getComponent(allEle.getAttribute("id"));
				String context = DomUtil.getChildNode(allEle, EventContextConstant.context).getTextContent();
				try{
					BaseContext comp = (BaseContext) LfwJsonSerializer.getInstance().fromJsObject(context);
					if(comp != null)
						webcomp.setContext(comp);
				}
				catch(Throwable e){
					LfwLogger.error(e);
				}
			}
		}
	}
	
}
