package nc.uap.lfw.core.serializer.impl;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import nc.uap.lfw.core.common.EventContextConstant;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.IDetachable;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.IRefDataset;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.ctx.WidgetContext;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.patch.XmlUtilPatch;
import nc.uap.lfw.core.serializer.IObject2XmlSerializer;
import nc.uap.lfw.core.tags.DynamicCompUtil;
import nc.uap.lfw.util.JsURLEncoder;
import nc.vo.jcom.xml.XMLUtil;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EventContext2XmlSerializer implements	IObject2XmlSerializer<LfwPageContext> {

	public String[] serialize(LfwPageContext obj) {
		Document doc = XmlUtilPatch.getNewDocument();
		Element rootNode = doSerialize(obj, doc);
		doc.appendChild(rootNode);
		
		StringWriter writer = new StringWriter();
		XMLUtil.printDOMTree(writer, doc, 0, "UTF-8");
		String result = writer.toString();
		result = JsURLEncoder.encode(result, "UTF-8");
		return new String[]{result};
	}
	
	private Element doSerialize(LfwPageContext pageCtx, Document doc){
		PageMeta meta = pageCtx.getPageMeta();
		Element rootNode = doc.createElement(EventContextConstant.eventcontext);
		//doc.appendChild(rootNode);
		
		if(pageCtx.getClientSession().getAttributeMap().size() > 0){
			Element attrNodes = doc.createElement(EventContextConstant.attributes);
			rootNode.appendChild(attrNodes);
			Iterator<Entry<String, Serializable>> it = pageCtx.getClientSession().getAttributeMap().entrySet().iterator();
			while(it.hasNext()){
				Entry<String, Serializable> entry = it.next();
				Element attrNode = doc.createElement(EventContextConstant.attribute);
				Element keyNode = doc.createElement(EventContextConstant.key);
				keyNode.setTextContent(entry.getKey());
				Element valueNode = doc.createElement(EventContextConstant.value);
				String valueStr = entry.getValue() == null? "" : entry.getValue().toString();
				valueNode.appendChild(doc.createCDATASection(valueStr));
				attrNode.appendChild(keyNode);
				attrNode.appendChild(valueNode);
				
				attrNodes.appendChild(attrNode);
			}
		}
		
//		if(pageCtx.getBeforeExecScript() != null){
//			Element execNode = doc.createElement("beforeExec");
//			rootNode.appendChild(execNode);
//			List<String> sList = pageCtx.getBeforeExecScript();
//			StringBuffer buf = new StringBuffer();
//			for (int i = 0; i < sList.size(); i++) {
//				String script = sList.get(i);
//				buf.append(script)
//				  .append("\n");
//			}
//			execNode.appendChild(doc.createTextNode(buf.toString()));
//		}
//		
//		if(pageCtx.getExecScript() != null){
//			Element execNode = doc.createElement("exec");
//			rootNode.appendChild(execNode);
//			List<String> sList = pageCtx.getExecScript();
//			StringBuffer buf = new StringBuffer();
//			for (int i = 0; i < sList.size(); i++) {
//				String script = sList.get(i);
//				buf.append(script)
//				  .append("\n");
//			}
//			execNode.appendChild(doc.createTextNode(buf.toString()));
//		}
		
		if(pageCtx.getCurrentEvent() != null && pageCtx.getCurrentEvent().isStop()){
			Element resultNode = doc.createElement("result");
			rootNode.appendChild(resultNode);
			resultNode.setTextContent("false");
		}
		Element pageNode = doc.createElement("pagemeta");
		rootNode.appendChild(pageNode);
		if(meta.isCtxChanged()){
			Element pagemetaContext = doc.createElement(EventContextConstant.context);
			pagemetaContext.appendChild(doc.createTextNode(LfwJsonSerializer.getInstance().toJsObject(meta.getContext())));
			pageNode.appendChild(pagemetaContext);
		}
		
		
//		CardLayout[] pcards = pageCtx.getCards();
//		if(pcards != null && pcards.length > 0)
//		{
//			for(int i = 0; i < pcards.length; i++)
//			{
//				if(pcards[i].isCtxChanged()){
//					CardLayout cardComp = pcards[i];
//					Element cardEle = doc.createElement("card");
//					cardEle.setAttribute("id", cardComp.getId());
//					rootNode.appendChild(cardEle);
//					Element cardCtx = doc.createElement(EventContextConstant.context);
//					cardEle.appendChild(cardCtx);
//					cardCtx.appendChild(doc.createTextNode(LfwJsonSerializer.getInstance().toJsObject(cardComp.getContext())));
//				}
//			}
//		}
//		
//		TabLayout[] ptabs = pageCtx.getTabs();
//		if(ptabs != null && ptabs.length > 0)
//		{
//			for(int i = 0; i < ptabs.length; i++)
//			{
//				if(ptabs[i].isCtxChanged()){
//					TabLayout tabComp = ptabs[i];
//					Element tabEle = doc.createElement("tab");
//					tabEle.setAttribute("id", tabComp.getId());
//					rootNode.appendChild(tabEle);
//					Element tabCtx = doc.createElement(EventContextConstant.context);
//					tabEle.appendChild(tabCtx);
//					tabCtx.appendChild(doc.createTextNode(LfwJsonSerializer.getInstance().toJsObject(tabComp.getContext())));
//				}
//			}
//		}
//		
//		PanelLayout[] pPanels = pageCtx.getPanels();
//		if(pPanels != null && pPanels.length > 0)
//		{
//			for(int i = 0; i < pPanels.length; i++)
//			{
//				if(pPanels[i].isCtxChanged()){
//					PanelLayout panelComp = pPanels[i];
//					Element panelEle = doc.createElement("panel");
//					panelEle.setAttribute("id", panelComp.getId());
//					rootNode.appendChild(panelEle);
//					Element panelCtx = doc.createElement(EventContextConstant.context);
//					panelEle.appendChild(panelCtx);
//					panelCtx.appendChild(doc.createTextNode(LfwJsonSerializer.getInstance().toJsObject(panelComp.getContext())));
//				}
//			}
//		}
		
		MenubarComp[] menubars = pageCtx.getPageMeta().getViewMenus().getMenuBars();
		if (menubars != null && menubars.length > 0) {
			for (int i = 0; i < menubars.length; i++) {
				MenubarComp menubar = menubars[i];
				if (menubar.isCtxChanged()) {
					Element menubarEle = doc.createElement("menubar");
					menubarEle.setAttribute("id", menubar.getId());
					rootNode.appendChild(menubarEle);
					Element menubarCtx = doc.createElement(EventContextConstant.context);
					menubarEle.appendChild(menubarCtx);
					menubarCtx.appendChild(doc.createCDATASection(LfwJsonSerializer.getInstance().toJsObject(menubar.getContext())));
				}
			}
		}
		
	
		LfwWidget[] widgets = meta.getWidgets();
		for (int j = 0; j < widgets.length; j++) 
		{
			LfwWidget widget = (LfwWidget) widgets[j];
			Element widgetNode = doc.createElement("widget");
			pageNode.appendChild(widgetNode);
			widgetNode.setAttribute("id", widget.getId());
			
			if(widget.isCtxChanged()){
				Element widgetContext = doc.createElement(EventContextConstant.context);
				widgetContext.appendChild(doc.createTextNode(LfwJsonSerializer.getInstance().toJsObject(widget.getContext())));
				widgetNode.appendChild(widgetContext);
			}
			
			WidgetContext widgetCtx = pageCtx.getWidgetContext(widget.getId());
			if(widgetCtx != null){
//				CardLayout[] cards = widgetCtx.getCards();
//				if(cards != null && cards.length > 0)
//				{
//					for(int i = 0; i < cards.length; i++)
//					{
//						if(cards[i].isCtxChanged()){
//							CardLayout cardComp = cards[i];
//							Element cardEle = doc.createElement("card");
//							cardEle.setAttribute("id", cardComp.getId());
//							widgetNode.appendChild(cardEle);
//							Element cardCtx = doc.createElement(EventContextConstant.context);
//							cardEle.appendChild(cardCtx);
//							cardCtx.appendChild(doc.createTextNode(LfwJsonSerializer.getInstance().toJsObject(cardComp.getContext())));
//						}
//					}
//				}
//				
//				TabLayout[] tabs = widgetCtx.getTabs();
//				if(tabs != null && tabs.length > 0)
//				{
//					for(int i = 0; i < tabs.length; i++)
//					{
//						if(tabs[i].isCtxChanged()){
//							TabLayout tabComp = tabs[i];
//							Element tabEle = doc.createElement("tab");
//							tabEle.setAttribute("id", tabComp.getId());
//							widgetNode.appendChild(tabEle);
//							Element tabCtx = doc.createElement(EventContextConstant.context);
//							tabEle.appendChild(tabCtx);
//							tabCtx.appendChild(doc.createTextNode(LfwJsonSerializer.getInstance().toJsObject(tabComp.getContext())));
//						}
//					}
//				}
//				
//				PanelLayout[] panels = widgetCtx.getPanels();
//				if(panels != null && panels.length > 0)
//				{
//					for(int i = 0; i < panels.length; i++)
//					{
//						if(panels[i].isCtxChanged()){
//							PanelLayout panelComp = panels[i];
//							Element panelEle = doc.createElement("panel");
//							panelEle.setAttribute("id", panelComp.getId());
//							widgetNode.appendChild(panelEle);
//							Element panelCtx = doc.createElement(EventContextConstant.context);
//							panelEle.appendChild(panelCtx);
//							panelCtx.appendChild(doc.createTextNode(LfwJsonSerializer.getInstance().toJsObject(panelComp.getContext())));
//						}
//					}
//				}
			}
			addComponents(doc, widgetNode, widget);
			addModels(doc, widgetNode, widget, pageCtx);
		}
		
		if(pageCtx.getBeforeExecScript() != null){
			Element execNode = doc.createElement("beforeExec");
			rootNode.appendChild(execNode);
			List<String> sList = pageCtx.getBeforeExecScript();
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < sList.size(); i++) {
				String script = sList.get(i);
				buf.append(script)
				  .append("\n");
			}
			execNode.appendChild(doc.createCDATASection(buf.toString()));
		}
		
		if(pageCtx.getExecScript() != null){
			Element execNode = doc.createElement("exec");
			rootNode.appendChild(execNode);
			List<String> sList = pageCtx.getExecScript();
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < sList.size(); i++) {
				String script = sList.get(i);
				buf.append(script)
				  .append("\n");
			}
			execNode.appendChild(doc.createCDATASection(buf.toString()));
		}
		
		if(pageCtx.getParentGlobalContext() != null){
			Element pctx = doc.createElement(EventContextConstant.parentcontext);
			pctx.appendChild(doSerialize(pageCtx.getParentGlobalContext(), doc));
			rootNode.appendChild(pctx);
		}
		
		return rootNode;
	}

	private void addModels(Document doc, Element widgetNode, LfwWidget widget, LfwPageContext pageCtx) {
		Dataset[] dss = widget.getViewModels().getDatasets();
		DatasetRelations dsRels = widget.getViewModels().getDsrelations();
		if(dsRels != null){
			List<Dataset> dsList = new ArrayList<Dataset>();
			for (int i = 0; i < dss.length; i++) {
				if(dss[i] instanceof IRefDataset)
					continue;
				sortDss(dss[i], dsRels, widget, dsList);
			}
			dss = dsList.toArray(new Dataset[0]);
		}
		for (int i = 0; i < dss.length; i++) {
			Dataset ds = dss[i];
			//取metadata信息
			WidgetContext wc = pageCtx.getWidgetContext(ds.getWidget().getId());
			DynamicCompUtil dcu = new DynamicCompUtil(pageCtx, wc);
			dcu.generateDsMetaDataScript(ds);
			
			if(ds.isCtxChanged()){
				Element dsNode = doc.createElement("dataset");
				dsNode.setAttribute("id", ds.getId());
				widgetNode.appendChild(dsNode);
				
				Element dsData = doc.createElement("data");
				dsNode.appendChild(dsData);
				String dsXml = new SingleDataset2XmlSerializer().serialize(ds)[0];
				CDATASection dsDataText = doc.createCDATASection(dsXml);
				dsData.appendChild(dsDataText);
			}
			ds.detach();
		}
	}

	/**
	 * 使客户端按照先子后主的逻辑进行
	 * @param dss
	 * @param widget
	 * @param dsList 
	 * @return
	 */
	private void sortDss(Dataset ds, DatasetRelations dsRels, LfwWidget widget, List<Dataset> dsList) {
		if(dsList.contains(ds))
			return;
		
		DatasetRelation[] masterRels = dsRels.getDsRelations(ds.getId());
		//先加入子或者独立Ds
		if(masterRels != null && masterRels.length > 0){
			for (int i = 0; i < masterRels.length; i++) {
				//获取子对应的外键值，并设置到VO条件中
				DatasetRelation dr = masterRels[i];
				Dataset detailDs = widget.getViewModels().getDataset(dr.getDetailDataset());
				sortDss(detailDs, dsRels, widget, dsList);
			}
		}
		dsList.add(ds);
	}

	private void addComponents(Document doc, Element rootNode, LfwWidget widget){
		// <TreeviewComp>
		WebComponent[] comps = widget.getViewComponents().getComponents();
		if(comps != null)
		{
			for (int i = 0; i < comps.length; i++) 
			{
				WebComponent comp = comps[i];
				if (comp.isCtxChanged()) {
					Element node = doc.createElement("comp");
					rootNode.appendChild(node);
					
					node.setAttribute("id", comp.getId());
					BaseContext ctx = comp.getContext();
					if(ctx != null){
						Element ctxEle = doc.createElement(EventContextConstant.context);
						ctxEle.appendChild(doc.createCDATASection(LfwJsonSerializer.getInstance().toJsObject(ctx)));
						node.appendChild(ctxEle);
					}
				}
				
				if(comp instanceof IDetachable)
					((IDetachable)comp).detach();
			}
		}
		ContextMenuComp[] menus = widget.getViewMenus().getContextMenus();
		if (menus != null && menus.length > 0) {
			for (int i = 0; i < menus.length; i++) {
				ContextMenuComp menu = menus[i];
				if (menu.isCtxChanged()) {
					Element contextMenuEle = doc.createElement("contextmenu");
					contextMenuEle.setAttribute("id", menu.getId());
					rootNode.appendChild(contextMenuEle);
					Element menuCtx = doc.createElement(EventContextConstant.context);
					contextMenuEle.appendChild(menuCtx);
					menuCtx.appendChild(doc.createCDATASection(LfwJsonSerializer.getInstance().toJsObject(menu.getContext())));
				}
			}
		}
		MenubarComp[] menubars = widget.getViewMenus().getMenuBars();
		if (menubars != null && menubars.length > 0) {
			for (int i = 0; i < menubars.length; i++) {
				MenubarComp menubar = menubars[i];
				if (menubar.isCtxChanged()) {
					Element menubarEle = doc.createElement("menubar");
					menubarEle.setAttribute("id", menubar.getId());
					rootNode.appendChild(menubarEle);
					Element menubarCtx = doc.createElement(EventContextConstant.context);
					menubarEle.appendChild(menubarCtx);
					menubarCtx.appendChild(doc.createCDATASection(LfwJsonSerializer.getInstance().toJsObject(menubar.getContext())));
				}
			}
		}
	}

}

