package nc.uap.lfw.core.serializer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.EventContextConstant;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.IFrameComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.ToolBarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.ContextMenuContext;
import nc.uap.lfw.core.comp.ctx.IFrameContext;
import nc.uap.lfw.core.comp.ctx.MenubarContext;
import nc.uap.lfw.core.comp.ctx.PageUIContext;
import nc.uap.lfw.core.comp.ctx.TabContext;
import nc.uap.lfw.core.comp.ctx.ToolbarContext;
import nc.uap.lfw.core.comp.ctx.WidgetUIContext;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ApplicationContext;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.ctx.WindowContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.IUIMeta;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.LifeCyclePhase;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.RequestLifeCycleContext;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.patch.XmlUtilPatch;
import nc.uap.lfw.core.serializer.impl.DomUtil;
import nc.uap.lfw.core.serializer.impl.LfwJsonSerializer;
import nc.uap.lfw.core.serializer.impl.Xml2DatasetSerializer;
import nc.uap.lfw.jsp.parser.UIMetaParserUtil;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import nc.uap.lfw.jsp.uimeta.UIWidget;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Xml2AppContextSerializer implements IXml2ObjectSerializer<AppLifeCycleContext> {
	private AppLifeCycleContext lifeCycleCtx;
	public AppLifeCycleContext serialize(String xml, Map<String, Object> paramMap) {
		LifeCyclePhase currentPhase = RequestLifeCycleContext.get().getPhase();
		try {
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			PageMeta pagemeta = LfwRuntimeEnvironment.getWebContext().getPageMeta();
			IUIMeta uimeta = LfwRuntimeEnvironment.getWebContext().getUIMeta();
			lifeCycleCtx = new AppLifeCycleContext();
			ApplicationContext appCtx = new ApplicationContext();
			lifeCycleCtx.setApplicationContext(appCtx);
			serialize(xml, pagemeta, uimeta);
			return lifeCycleCtx;
		} catch (Exception e) {
			throw new LfwRuntimeException(e);
		}finally{
			RequestLifeCycleContext.get().setPhase(currentPhase);
		}
	}
	
	private AppLifeCycleContext serialize(String xml, PageMeta pagemeta, IUIMeta uimeta){
		try {
			Document doc = XmlUtilPatch.getDocumentBuilder().parse(
					new InputSource(new StringReader(xml)));
			Element rootNode = (Element) doc.getFirstChild();
			Element ctxNode = (Element) DomUtil.getChildNode(rootNode, EventContextConstant.eventcontext);
			return serialize(rootNode, ctxNode, pagemeta, uimeta);
		}
		catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		} 
	}
	private AppLifeCycleContext serialize(Node rootNode, Node ctxNode, PageMeta pagemeta, IUIMeta uimeta) throws SAXException, IOException{
		ApplicationContext appCtx = lifeCycleCtx.getApplicationContext();
		String winId = ((Element)ctxNode).getAttribute("id");
		WindowContext winCtx = new WindowContext();
		winCtx.setId(winId);
		winCtx.setWindow(pagemeta);
		winCtx.setUiMeta(uimeta);
		appCtx.addWindowContext(winCtx);
		
		Element paramsNode = (Element) DomUtil.getChildNode(ctxNode,EventContextConstant.params);
		if (paramsNode != null) {
			Map<String, String> paramMap = parseParams(paramsNode);
			lifeCycleCtx.setParamMap(paramMap);
		}
		
		Element groupParamsNode = (Element) DomUtil.getChildNode(ctxNode,EventContextConstant.groupparams);
		if (groupParamsNode != null) {
			Node[] allParams = DomUtil.getChildNodes(groupParamsNode,
					EventContextConstant.params);
			if(allParams.length > 0){
				List<Map<String, String>> gplist = new ArrayList<Map<String, String>>();//lifeCycleCtx.getGroupParamMapList();
				lifeCycleCtx.setGroupParamMapList(gplist);
				for (int i = 0; i < allParams.length; i++) {
					Element paramsEle = (Element) allParams[i];
					Map<String, String> paramMap = parseParams(paramsEle);
					gplist.add(paramMap);
//					Element attEle = (Element) allParams[i];
//					Element keyEle = (Element) DomUtil.getChildNode(attEle,
//							EventContextConstant.key);
//					String key = keyEle.getTextContent();
//					Element valueEle = (Element) DomUtil.getChildNode(
//							attEle, EventContextConstant.value);
//					String value = valueEle.getTextContent();
//					paramMap.put(key, value);
				}
			}
		}
		
		String context = DomUtil.getChildNode(ctxNode, EventContextConstant.context)
				.getTextContent();
		PageUIContext ctx = (PageUIContext) LfwJsonSerializer.getInstance()
				.fromJsObject(context);
		pagemeta.setContext(ctx);

		Node[] widgets = DomUtil.getChildNodes(ctxNode, "widget");
		for (int i = 0; i < widgets.length; i++) {
			Element widgetEle = (Element) widgets[i];
			this.processwidget(winCtx, pagemeta, (UIMeta) uimeta, widgetEle);
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
		
		NodeList allList = ctxNode.getChildNodes();// rootNode.getElementsByTagName("tab");
		if (allList != null) {
			for (int i = 0; i < allList.getLength(); i++) {
				Element ele = (Element) allList.item(i);
				if (ele.getNodeName().equals("tab"))
					this.processTab(winCtx, pagemeta, ele);
			}
		}
		
		if(rootNode != null){
			Node pcontextNode = DomUtil.getChildNode(rootNode, EventContextConstant.parentcontext);
			if(pcontextNode != null){
				String pId = ((Element)pcontextNode.getFirstChild()).getAttribute("id");
				PageMeta parentPm = LfwRuntimeEnvironment.getWebContext().getCrossPageMeta(pId);
				IUIMeta parentUm = LfwRuntimeEnvironment.getWebContext().getCrossUm(pId);
				serialize(null, pcontextNode.getFirstChild(), parentPm, parentUm);
			}
		}
		
		return lifeCycleCtx;
	}

	private Map<String, String> parseParams(Element paramsNode) {
		Node[] allParams = DomUtil.getChildNodes(paramsNode,
				EventContextConstant.param);
		Map<String, String> paramMap = new HashMap<String, String>();
		for (int i = 0; i < allParams.length; i++) {
			Element attEle = (Element) allParams[i];
			Element keyEle = (Element) DomUtil.getChildNode(attEle,
					EventContextConstant.key);
			String key = keyEle.getTextContent();
			Element valueEle = (Element) DomUtil.getChildNode(
					attEle, EventContextConstant.value);
			String value = valueEle.getTextContent();
			paramMap.put(key, value);
		}
		return paramMap;
	}

	private void processTab(WindowContext  winCtx, PageMeta pagemeta, Element ele) {		
		String context = ele.getElementsByTagName(EventContextConstant.context).item(0).getTextContent();
		TabContext ctx = (TabContext) LfwJsonSerializer.getInstance().fromJsObject(context);
		UIMeta uimeta = (UIMeta)winCtx.getUiMeta();
		UITabComp tab = (UITabComp) UIElementFinder.findElementById(uimeta, ctx.getId());
//		UITabComp tab = (UITabComp) uimeta.findChildById(ctx.getId());
		tab.setCurrentItem(ctx.getCurrentIndex() + "");
//		;
//		TabLayout tab = new TabLayout();
//		tab.setContext(ctx);
//		
//		WebComponent ct = pagemeta.getViewConinters().getContainer(ctx.getId());
//		if(ct != null){
//			Map<String, JsListenerConf> listenerMap = ct.getListenerMap();
//			tab.setListenerMap(listenerMap);
//		}
//		pageCtx.addTab(tab);
	}
	protected void processwidget(WindowContext winCtx, PageMeta pagemeta, UIMeta um, Element widgetEle) throws SAXException, IOException{
		String widgetId = widgetEle.getAttribute("id");
		LfwWidget widget = pagemeta.getWidget(widgetId);
		if(widget == null){
			throw new LfwRuntimeException("根据ID没有找到对应的widget配置," + widgetId);
		}
		ViewContext vCtx = new ViewContext();
		vCtx.setId(widgetId);
		UIWidget uiWidget = (UIWidget) UIElementFinder.findElementById(um, widgetId);
		if(uiWidget != null)
			vCtx.setUIMeta(uiWidget.getUimeta());
		else{
			String appPath = ContextResourceUtil.getCurrentAppPath();
			String folderPath = pagemeta.getFoldPath();//(String) pagemeta.getExtendAttributeValue(PageMeta.FOLD_PATH);
			String refId = widget.getRefId();
			if(refId.startsWith("../")){
				folderPath = "pagemeta/public/widgetpool";
			}
			String path = appPath + folderPath + "/" + widgetId;
			UIMeta viewUIMeta = (UIMeta)LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("$TMP_UM_" + widgetId);
			if(viewUIMeta == null)
				viewUIMeta = new UIMetaParserUtil().parseUIMeta(path, widgetId);
			vCtx.setUIMeta(viewUIMeta);
		}
		vCtx.setView(widget);
		
		winCtx.addViewContext(vCtx);
		
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

			else if(nodeName.equals("dataset")){
				Xml2DatasetSerializer xml2datasetSerializer = new Xml2DatasetSerializer();
				Node dataEle = DomUtil.getChildNode(allEle, "data");
				if(dataEle != null){
					String dsStr = dataEle.getTextContent();
					if(dsStr != null && !dsStr.equals("")){
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("pagemeta", pagemeta);
						xml2datasetSerializer.serialize(dsStr, paramMap);
					}
				}
			}
			
			else if(nodeName.equals("combodata")){
			}
			else if(nodeName.equals("refnode")){
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
			}else if(nodeName.equals("tab")){
				String context = DomUtil.getChildNode(allEle, EventContextConstant.context).getTextContent();
				TabContext tabCtx = (TabContext) LfwJsonSerializer.getInstance().fromJsObject(context);
				UIMeta uimeta = vCtx.getUIMeta();
				UITabComp tab = (UITabComp) UIElementFinder.findElementById(uimeta, tabCtx.getId());
				tab.setCurrentItem(tabCtx.getCurrentIndex() + "");
			}
			else if(nodeName.equals("iframe")){
				String context = DomUtil.getChildNode(allEle, EventContextConstant.context).getTextContent();
				IFrameContext iFrameContext = (IFrameContext) LfwJsonSerializer.getInstance().fromJsObject(context);
				IFrameComp iFrameComp = (IFrameComp) widget.getViewComponents().getComponent(allEle.getAttribute("id"));
				iFrameComp.setContext(iFrameContext);
			}
			else{				
				ViewComponents viewComponent = widget.getViewComponents();
				WebComponent webcomp = (WebComponent)viewComponent.getComponent(allEle.getAttribute("id"));
				String context = DomUtil.getChildNode(allEle, EventContextConstant.context).getTextContent();
				BaseContext comp = (BaseContext) LfwJsonSerializer.getInstance().fromJsObject(context);				
				webcomp.setContext(comp);
			}
		}
	}
	

}
