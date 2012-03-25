package nc.uap.lfw.core.serializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ApplicationContext;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.ctx.WindowContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.IRefDataset;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.patch.XmlUtilPatch;
import nc.uap.lfw.core.serializer.impl.LfwJsonSerializer;
import nc.uap.lfw.core.serializer.impl.SingleDataset2XmlSerializer;
import nc.uap.lfw.util.JsURLEncoder;
import nc.vo.jcom.xml.XMLUtil;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class AppContext2XmlSerializer implements IObject2XmlSerializer<AppLifeCycleContext> {

	public String[] serialize(AppLifeCycleContext lifeCycleCtx) {
		Document doc = XmlUtilPatch.getNewDocument();
		Element rootNode = doSerialize(lifeCycleCtx, doc);
		doc.appendChild(rootNode);
		
		StringWriter writer = new StringWriter();
		XMLUtil.printDOMTree(writer, doc, 0, "UTF-8");
		String result = writer.toString();
		
		result = result.replaceAll("<!\\[CDATA\\[", "<CD>");
		result = result.replaceAll("\\]\\]>", "</CD>");
		//result = JsURLEncoder.encode(result, "UTF-8");
		return new String[]{result};
	}
	
	private Element doSerialize(AppLifeCycleContext ctx, Document doc){
		ApplicationContext appCtx = ctx.getApplicationContext();
//		PageMeta meta = LfwRuntimeEnvironment.getWebContext().getPageMeta();
		Element rootNode = doc.createElement(EventContextConstant.eventcontext);
		
		if(appCtx.getClientSession().getAttributeMap().size() > 0){
			Element attrNodes = doc.createElement(EventContextConstant.attributes);
			rootNode.appendChild(attrNodes);
			Iterator<Entry<String, Serializable>> it = appCtx.getClientSession().getAttributeMap().entrySet().iterator();
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
		
		if(appCtx.getBeforeExecScript() != null){
			Element execNode = doc.createElement("beforeExec");
			rootNode.appendChild(execNode);
			List<String> sList = appCtx.getBeforeExecScript();
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < sList.size(); i++) {
				String script = sList.get(i);
				buf.append(script)
				  .append("\n");
			}
			execNode.appendChild(doc.createCDATASection(buf.toString()));
		}
		
		if(appCtx.getExecScript() != null){
			Element execNode = doc.createElement("exec");
			rootNode.appendChild(execNode);
			List<String> sList = appCtx.getExecScript();
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < sList.size(); i++) {
				String script = sList.get(i);
				buf.append(script)
				  .append("\n");
			}
			execNode.appendChild(doc.createCDATASection(buf.toString()));
		}
		
		if(appCtx.getAfterExecScript() != null){
			Element execNode = doc.createElement("afterExec");
			rootNode.appendChild(execNode);
			List<String> sList = appCtx.getAfterExecScript();
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < sList.size(); i++) {
				String script = sList.get(i);
				buf.append(script)
				  .append("\n");
			}
			execNode.appendChild(doc.createCDATASection(buf.toString()));
		}
		
		if(appCtx.getCurrentEvent() != null && appCtx.getCurrentEvent().isStop()){
			Element resultNode = doc.createElement("result");
			rootNode.appendChild(resultNode);
			resultNode.setTextContent("false");
		}
		serializeWindow(appCtx.getCurrentWindowContext(), doc, rootNode);
//			
//		}
//		Element pageNode = doc.createElement("pagemeta");
//		rootNode.appendChild(pageNode);
//		if(meta.isCtxChanged()){
//			Element pagemetaContext = doc.createElement(EventContextConstant.context);
//			pagemetaContext.appendChild(doc.createTextNode(LfwJsonSerializer.getInstance().toJsObject(meta.getContext())));
//			pageNode.appendChild(pagemetaContext);
//		}
//		
//		ViewContext viewCtx = ctx.getViewContext();
//		if(viewCtx != null){
//			LfwWidget widget = viewCtx.getView();
//			Element widgetNode = doc.createElement("widget");
//			pageNode.appendChild(widgetNode);
//			widgetNode.setAttribute("id", widget.getId());
//			
//			if(widget.isCtxChanged()){
//				Element widgetContext = doc.createElement(EventContextConstant.context);
//				widgetContext.appendChild(doc.createTextNode(LfwJsonSerializer.getInstance().toJsObject(widget.getContext())));
//				widgetNode.appendChild(widgetContext);
//			}
//			
//			addComponents(doc, widgetNode, widget);
//			addModels(doc, widgetNode, widget, viewCtx);
//		}
//		
		WindowContext[] ctxs = appCtx.getWindowContexts();
		if(ctxs != null && ctxs.length > 0){
			WindowContext currCtx = appCtx.getCurrentWindowContext();
			for (int i = 0; i < ctxs.length; i++) {
				if(ctxs[i].equals(appCtx.getCurrentWindowContext()))
					continue;
				Element pctx = doc.createElement(EventContextConstant.parentcontext);
				pctx.appendChild(doSerialize(ctxs[i], doc));
				rootNode.appendChild(pctx);
			}
		}
		
		return rootNode;
	}

	private Node doSerialize(WindowContext windowContext, Document doc) {
		Element rootNode = doc.createElement(EventContextConstant.eventcontext);
		serializeWindow(windowContext, doc, rootNode);
		return rootNode;
		
	}

	private void serializeWindow(WindowContext windowContext, Document doc,
			Element rootNode) {
		Element pageNode = doc.createElement("pagemeta");
		rootNode.appendChild(pageNode);
		PageMeta meta = windowContext.getWindow();
		if(meta.isCtxChanged()){
			Element pagemetaContext = doc.createElement(EventContextConstant.context);
			pagemetaContext.appendChild(doc.createTextNode(LfwJsonSerializer.getInstance().toJsObject(meta.getContext())));
			pageNode.appendChild(pagemetaContext);
		}
		
		LfwWidget[] widgets = meta.getWidgets();
//		ViewContext[] viewCtxs = windowContext.getViewContexts();
		if(widgets != null){
			for (int i = 0; i < widgets.length; i++) {
//				ViewContext viewCtx = viewCtxs[i];
				LfwWidget widget = widgets[i];
				Element widgetNode = doc.createElement("widget");
				pageNode.appendChild(widgetNode);
				widgetNode.setAttribute("id", widget.getId());
				
				if(widget.isCtxChanged()){
					Element widgetContext = doc.createElement(EventContextConstant.context);
					widgetContext.appendChild(doc.createTextNode(LfwJsonSerializer.getInstance().toJsObject(widget.getContext())));
					widgetNode.appendChild(widgetContext);
				}
				addComponents(doc, widgetNode, widget);
				addModels(doc, widgetNode, widget, null);
			}
		}
	}

	private void addModels(Document doc, Element widgetNode, LfwWidget widget, ViewContext viewCtx) {
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
			if(ds.isCtxChanged()){
				Element dsNode = doc.createElement("dataset");
				dsNode.setAttribute("id", ds.getId());
				widgetNode.appendChild(dsNode);
				
				Element dsData = doc.createElement("data");
				dsNode.appendChild(dsData);
				String dsXml = new SingleDataset2XmlSerializer().serialize(ds)[0];
				CDATASection dsDataText = doc.createCDATASection(dsXml);
				dsData.appendChild(dsDataText);
				ds.setCtxChanged(false);
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
		if(comps != null){
			for (int i = 0; i < comps.length; i++) {
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
					comp.setCtxChanged(false);
				}
				if(comp instanceof IDetachable){
					((IDetachable)comp).detach();
				}
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
					menu.setCtxChanged(false);
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
					menubar.setCtxChanged(false);
				}
			}
		}
	}

}
