package nc.uap.lfw.conf.persist;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.plugin.IControlPlugin;
import nc.uap.lfw.core.ctrlfrm.seria.IControlSerializer;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PluginDescItem;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.page.PlugoutDescItem;
import nc.uap.lfw.core.page.PlugoutEmitItem;
import nc.uap.lfw.core.page.manager.PoolObjectManager;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.RefNodeRelation;
import nc.uap.lfw.core.refnode.RefNodeRelations;
import nc.uap.lfw.core.uimodel.conf.Model;
import nc.uap.lfw.core.util.AMCUtil;
import nc.vo.jcom.xml.XMLUtil;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 将widget输入为xml文件
 * @author gd 2010-1-22
 * @version NC6.0
 * @since NC6.0
 */
public class WidgetToXml {
	
	public static final String DYN_ATTRIBUTE_KEY = "DYN";
	
	/**
	 * 修改View引用ID（RefID）
	 * @param filePath
	 * @param fileName
	 * @param projectPath
	 * @param refId
	 */
	public static void toXml(String filePath, String fileName, String projectPath, String refId){
		Digester d = new Digester();
		d.addObjectCreate("Widget", LfwWidget.class);
		d.addSetProperties("Widget");
		try {
			LfwWidget widget = (LfwWidget) d.parse(new File(filePath + "/" + fileName));
			widget.setRefId(refId);
			WidgetToXml.toXml(filePath, fileName, projectPath, widget);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 将LfwWidget持久化为xml文件
	 * @param filePath 文件全路径
	 * @param fileName 文件名
	 * @param projectPath 工程路径
	 * @param widget
	 */
	public static void toXml(String filePath, String fileName, String projectPath, LfwWidget widget) {
		Document doc = getDocumentByWidget(widget);
		
		// 写出文件
		PersistenceUtil.toXmlFile(doc, filePath, fileName);
		
		if(widget.getExtendAttribute(LfwWidget.POOLWIDGET) != null){
			PoolObjectManager.refreshWidgetPool(LfwRuntimeEnvironment.getRootPath(), widget);
		}
		
	}
	
	public static String toString(LfwWidget widget){
		Document doc = getDocumentByWidget(widget);
		Writer wt = new StringWriter();
		XMLUtil.printDOMTree(wt, doc, 0, "UTF-8");
		String xmlStr = wt.toString();
		return xmlStr;
	}
	private static Document getDocumentByWidget(LfwWidget widget){
		Document doc = XMLUtil.getNewDocument();
		Element rootNode = doc.createElement("Widget");
		doc.appendChild(rootNode);
		if(widget.getId() != null && !widget.getId().equals(""))	
			rootNode.setAttribute("id", widget.getId());
		if(widget.getFrom() != null && !widget.getFrom().equals(""))	
			rootNode.setAttribute("from", widget.getFrom());
		rootNode.setAttribute("dialog", String.valueOf(widget.isDialog()));
		if(isNotNullString(widget.getI18nName()))
			rootNode.setAttribute("i18nName", widget.getI18nName());
		if(isNotNullString(widget.getRefId())){
			rootNode.setAttribute("refId", widget.getRefId());
		}
		if(isNotNullString(widget.getControllerClazz())){
			rootNode.setAttribute("controllerClazz", widget.getControllerClazz());
		}
		if(isNotNullString(widget.getSourcePackage())){
			rootNode.setAttribute("sourcePackage", widget.getSourcePackage());
		}
		
		if(isNotNullString(widget.getProvider())){
			rootNode.setAttribute("provider", widget.getProvider());
		}
		
		Map<String, ExtAttribute> extAttrs = widget.getExtendMap();
		if(extAttrs != null && !extAttrs.isEmpty()){
			Element attributesNode = doc.createElement("Attributes");
			rootNode.appendChild(attributesNode);
			
			Iterator<String> attrIt = extAttrs.keySet().iterator();
			while(attrIt.hasNext())
			{
				String attrKey = attrIt.next();
				ExtAttribute attr = extAttrs.get(attrKey);
				if(attr.getValue() != null){
					if(!attr.getKey().toString().startsWith(DYN_ATTRIBUTE_KEY) && ((attr.getValue() instanceof String) || (attr.getValue() instanceof Integer))){
						Element attributeNode = doc.createElement("Attribute");
						attributesNode.appendChild(attributeNode);
						Element keyNode = doc.createElement("Key");
						keyNode.appendChild(doc.createTextNode(attrKey));
						attributeNode.appendChild(keyNode);
						Element valueNode = doc.createElement("Value");
						if(attr.getValue() != null)
							valueNode.appendChild(doc.createTextNode(attr.getValue().toString()));
						attributeNode.appendChild(valueNode);
					}
				}
			}
		}
		//引用元数据描述
		addModelsGroups(doc, rootNode, widget);
		
		// listener持久化
		Map<String, JsListenerConf> lisMap = widget.getListenerMap();
		if(lisMap != null && !lisMap.isEmpty())
			PersistenceUtil.addListeners(doc, lisMap.values().toArray(new JsListenerConf[0]), rootNode);
		
		//增加plugin plugout 描述
		addPlugDesc(doc, rootNode, widget);
		
		//Events
		AMCUtil.addEvents(doc, widget.getEventConfs(), rootNode);
		
		addModels(doc, rootNode, widget);
	
		//addComboDatas(doc, rootNode, widget);
		
		addComponents(doc, rootNode, widget);
		
//		addContainers(doc, rootNode, widget);
		
		addMenus(doc, rootNode, widget);
		
		// 写出文件
		return doc;
		
//		PersistenceUtil.toXmlFile(doc, filePath, fileName);
		
//		if(widget.getExtendAttribute(LfwWidget.POOLWIDGET) != null){
//			PoolObjectManager.refreshWidgetPool(LfwRuntimeEnvironment.getRootPath(), widget);
//		}
	}
	
	/**
	 * 增加plugin plugout 描述
	 * @param doc
	 * @param rootNode
	 * @param widget
	 */
	private static void addPlugDesc(Document doc, Element rootNode, LfwWidget widget) {
//		Element plugoutNodes = doc.createElement("PlugoutDescs");
//		rootNode.appendChild(plugoutNodes);
//		
//		ControlFramework cfrm = ControlFramework.getInstance();
//		//plugout
//		List<PlugoutDesc> plugoutDescs =  widget.getPlugoutDescs();
//		if (plugoutDescs != null){
//			for (PlugoutDesc plugoutDesc : plugoutDescs){
//				IControlPlugin ctrlPlugin = cfrm.getControlPluginByClass(PlugoutDesc.class);
//				IControlSerializer<PlugoutDesc> ctrlSerializer = ctrlPlugin.getControlSerializer();
//				ctrlSerializer.deSerialize(plugoutDesc, doc, plugoutNodes);
//			}
//		}
//		//plugin
//		Element pluginNodes = doc.createElement("PluginDescs");
//		rootNode.appendChild(pluginNodes);
//		List<PluginDesc> pluginDescs =  widget.getPluginDescs();
//		if (pluginDescs != null){
//			for (PluginDesc pluginDesc : pluginDescs){
//				IControlPlugin ctrlPlugin = cfrm.getControlPluginByClass(PlugoutDesc.class);
//				IControlSerializer<PluginDesc> ctrlSerializer = ctrlPlugin.getControlSerializer();
//				ctrlSerializer.deSerialize(pluginDesc, doc, pluginNodes);
//			}
//		}
		
		Element plugoutNodes = doc.createElement("PlugoutDescs");
		rootNode.appendChild(plugoutNodes);
		//plugout
		List<PlugoutDesc> plugoutDescs =  widget.getPlugoutDescs();
		if (plugoutDescs != null){
			for (PlugoutDesc plugoutDesc : plugoutDescs){
				Element plugoutDescNodes = doc.createElement("PlugoutDesc");
				plugoutNodes.appendChild(plugoutDescNodes);
				if (isNotNullString(plugoutDesc.getId()))
					plugoutDescNodes.setAttribute("id", plugoutDesc.getId());
				
				List<PlugoutDescItem> plugoutDescItems =  plugoutDesc.getDescItemList();
				if (plugoutDescItems != null){
					for (PlugoutDescItem descItem : plugoutDescItems){
						Element plugoutDescItemNodes = doc.createElement("PlugoutDescItem");
						plugoutDescNodes.appendChild(plugoutDescItemNodes);
						if (isNotNullString(descItem.getName()))
							plugoutDescItemNodes.setAttribute("name", descItem.getName());
						if (isNotNullString(descItem.getType()))
							plugoutDescItemNodes.setAttribute("type", descItem.getType());
						if (isNotNullString(descItem.getSource()))
							plugoutDescItemNodes.setAttribute("source", descItem.getSource());
						if (isNotNullString(descItem.getValue()))
							plugoutDescItemNodes.setAttribute("value", descItem.getValue());
						if (isNotNullString(descItem.getDesc()))
							plugoutDescItemNodes.setAttribute("desc", descItem.getDesc());
						if (isNotNullString(descItem.getClazztype()))
							plugoutDescItemNodes.setAttribute("clazztype", descItem.getClazztype());
					}
				}
				List<PlugoutEmitItem> plugoutEmitItems =  plugoutDesc.getEmitList();
				if (plugoutEmitItems != null){
					for (PlugoutEmitItem emitItem : plugoutEmitItems){
						Element plugoutEmitItemNodes = doc.createElement("PlugoutEmitItem");
						plugoutDescNodes.appendChild(plugoutEmitItemNodes);
						if (isNotNullString(emitItem.getId()))
							plugoutEmitItemNodes.setAttribute("id", emitItem.getId());
						if (isNotNullString(emitItem.getSource()))
							plugoutEmitItemNodes.setAttribute("source", emitItem.getSource());
						if (isNotNullString(emitItem.getType()))
							plugoutEmitItemNodes.setAttribute("type", emitItem.getType());
						if (isNotNullString(emitItem.getDesc()))
							plugoutEmitItemNodes.setAttribute("desc", emitItem.getDesc());
					}
				}
			}
		}
		//plugin
		Element pluginNodes = doc.createElement("PluginDescs");
		rootNode.appendChild(pluginNodes);
		List<PluginDesc> pluginDescs =  widget.getPluginDescs();
		if (pluginDescs != null){
			for (PluginDesc pluginDesc : pluginDescs){
				Element pluginDescNodes = doc.createElement("PluginDesc");
				pluginNodes.appendChild(pluginDescNodes);
				if (isNotNullString(pluginDesc.getId()))
					pluginDescNodes.setAttribute("id", pluginDesc.getId());
				
				List<PluginDescItem> pluginDescItems =  pluginDesc.getDescItemList();
				if (pluginDescItems != null){
					for (PluginDescItem descItem : pluginDescItems){
						Element pluginDescItemNodes = doc.createElement("PluginDescItem");
						pluginDescNodes.appendChild(pluginDescItemNodes);
						if (isNotNullString(descItem.getId()))
							pluginDescItemNodes.setAttribute("id", descItem.getId());
						if (isNotNullString(descItem.getValue()))
							pluginDescItemNodes.setAttribute("value", descItem.getValue());
						if (isNotNullString(descItem.getClazztype()))
							pluginDescItemNodes.setAttribute("clazztype", descItem.getClazztype());
					}
				}
			}
		}
		
	}
	

	
	/**
	 * 增加menus
	 * @param doc
	 * @param rootNode
	 * @param widget
	 */
	private static void addMenus(Document doc, Element rootNode, LfwWidget widget) {
		Element menusNodes = doc.createElement("Menus");
		rootNode.appendChild(menusNodes);
		
		ControlFramework cfrm = ControlFramework.getInstance();
		//menubarcomp
		MenubarComp[] menubars = widget.getViewMenus().getMenuBars();
		if(menubars.length > 0){
			for (int i = 0; i < menubars.length; i++) {
				IControlPlugin ctrlPlugin = cfrm.getControlPluginByClass(MenubarComp.class);
				IControlSerializer<MenubarComp> ctrlSerializer = ctrlPlugin.getControlSerializer();
				ctrlSerializer.deSerialize(menubars[i], doc, menusNodes);
			}
		}
		// <ContextMenuComp>
		ContextMenuComp[] contextMenus = widget.getViewMenus().getContextMenus();
		if(contextMenus != null && contextMenus.length > 0)
		{
			int size = contextMenus.length;
			for (int i = 0; i < size; i++) {
				IControlPlugin ctrlPlugin = cfrm.getControlPluginByClass(ContextMenuComp.class);
				IControlSerializer<ContextMenuComp> ctrlSerializer = ctrlPlugin.getControlSerializer();
				ctrlSerializer.deSerialize(contextMenus[i], doc, menusNodes);
			}
		}
		
		
		
	}

//	/**
//	 * 增加容器类组件
//	 * @param doc
//	 * @param rootNode
//	 * @param widget
//	 */
//	private static void addContainers(Document doc, Element rootNode, LfwWidget widget) {
//		Element containersNodes = doc.createElement("Containers");
//		rootNode.appendChild(containersNodes);
//		ControlFramework cfrm = ControlFramework.getInstance();
//		// <TabComp>
//		WebComponent[] containers = widget.getViewConinters().getContainers();
//		if(containers != null){
//			for (int i = 0; i < containers.length; i++) {
//				IControlPlugin ctrlPlugin = cfrm.getControlPluginByClass(containers[i].getClass());
//				IControlSerializer<WebComponent> ctrlSerializer = ctrlPlugin.getControlSerializer();
//				ctrlSerializer.deSerialize(containers[i], doc, containersNodes);
//			}
//		}
//	}
	
	private static void addComponents(Document doc, Element rootNode, LfwWidget widget) {
		// <Components>
		Element compsNodes = doc.createElement("Components");
		rootNode.appendChild(compsNodes);
		ControlFramework cfrm = ControlFramework.getInstance();
		WebComponent[] comps = widget.getViewComponents().getComponents();
		if(comps != null){
			for (int i = 0; i < comps.length; i++) {
				IControlPlugin ctrlPlugin = cfrm.getControlPluginByClass(comps[i].getClass());
				IControlSerializer<WebComponent> ctrlSerializer = ctrlPlugin.getControlSerializer();
				ctrlSerializer.deSerialize(comps[i], doc, compsNodes);
			}
		}
	}

	private static void addComboDatas(Document doc, Element rootNode, LfwWidget widget) {
		// <ComboDatas>
		ComboData[] cds = widget.getViewModels().getComboDatas();
		ControlFramework cfrm = ControlFramework.getInstance();
		if(cds != null && cds.length > 0){
			Element comboDatasNode = doc.createElement("ComboDatas");
			rootNode.appendChild(comboDatasNode);
			for (int i = 0; i < cds.length; i++) {
				IControlPlugin ctrlPlugin = cfrm.getControlPluginByClass(cds[i].getClass());
				IControlSerializer<ComboData> ctrlSerializer = ctrlPlugin.getControlSerializer();
				ctrlSerializer.deSerialize(cds[i], doc, comboDatasNode);
			}
		}
	}

	/**
	 * 添加参照节点
	 * @param doc
	 * @param rootNode
	 * @param widget
	 */
	private static void addRefNodes(Document doc, Element rootNode, LfwWidget widget) {
		IRefNode[] refnodes = widget.getViewModels().getRefNodes();
		if(refnodes != null && refnodes.length > 0){
			ControlFramework cfrm = ControlFramework.getInstance();
			Element refDatasNode = doc.createElement("RefNodes");
			rootNode.appendChild(refDatasNode);
			
			//增加RefnodeRelation
			RefNodeRelations refNodeRels = widget.getViewModels().getRefNodeRelations();
			if(refNodeRels != null && refNodeRels.getRefnodeRelations().size() > 0){
				Element refnodeRelNode = doc.createElement("RefNodeRelations");
				refDatasNode.appendChild(refnodeRelNode);
				
				Map<String,RefNodeRelation> rels = refNodeRels.getRefnodeRelations();
				if(rels != null && rels.size() > 0){
					// 创建<DatasetRelation>
					RefNodeRelation[] refnodeRels = rels.values().toArray(new RefNodeRelation[0]);
					for(int i = 0; i < refnodeRels.length; i++){
						IControlPlugin ctrlPlugin = cfrm.getControlPluginByClass(RefNodeRelation.class);
						IControlSerializer<RefNodeRelation> ctrlSerializer = ctrlPlugin.getControlSerializer();
						ctrlSerializer.deSerialize(refnodeRels[i], doc, refnodeRelNode);
					}
				}
			}
			for (IRefNode refnode : refnodes) {
				IControlPlugin ctrlPlugin = cfrm.getControlPluginByClass(IRefNode.class);
				IControlSerializer<IRefNode> ctrlSerializer = ctrlPlugin.getControlSerializer();
				ctrlSerializer.deSerialize(refnode, doc, refDatasNode);
			}
		}
	}
	

	private static void addModels(Document doc, Element rootNode, LfwWidget widget)
	{
		//<Models>
		Element modelsNode = doc.createElement("Models");
		rootNode.appendChild(modelsNode);
		addDatasets(doc, modelsNode, widget);
		
		addComboDatas(doc, modelsNode, widget);
		addRefNodes(doc, modelsNode, widget);
	}

	private static void addDatasets(Document doc, Element modelsNode, LfwWidget widget) {
		// 创建<Datasets>
		Element datasetsNode = doc.createElement("Datasets");
		modelsNode.appendChild(datasetsNode);
		
		ControlFramework cfrm = ControlFramework.getInstance();
		// 创建<DatasetRelations> 
		DatasetRelations dsRels = widget.getViewModels().getDsrelations();
		if(dsRels != null){
			Element datasetRelNode = doc.createElement("DatasetRelations");
			datasetsNode.appendChild(datasetRelNode);
			DatasetRelation[] rels = dsRels.getDsRelations();
			if(rels.length > 0){
				// 创建<DatasetRelation>
				for(int i = 0; i < rels.length; i++){
					IControlPlugin ctrlPlugin = cfrm.getControlPluginByClass(DatasetRelation.class);
					IControlSerializer<DatasetRelation> ctrlSerializer = ctrlPlugin.getControlSerializer();
					ctrlSerializer.deSerialize(rels[i], doc, datasetRelNode);
				}
			}
		}
		
		// 创建<Dataset>
		Dataset[] dss = widget.getViewModels().getDatasets();
		if(dss != null && dss.length > 0){
			for (int i = 0; i < dss.length; i++) {
				IControlPlugin ctrlPlugin = cfrm.getControlPluginByClass(Dataset.class);
				IControlSerializer<Dataset> ctrlSerializer = ctrlPlugin.getControlSerializer();
				ctrlSerializer.deSerialize(dss[i], doc, datasetsNode);
			}
		}
	}


	public static void addModelsGroups(Document doc, Element parentNode, LfwWidget widget){
		List<Model> models = widget.getModels();
		if(models != null && models.size() > 0){
			Element modelsNode = doc.createElement(Model.TagName + "Groups");
			parentNode.appendChild(modelsNode);
			for(Model model : models){
				Element modelNode = AMCUtil.getElementFromClass(doc, model);
				modelsNode.appendChild(modelNode);
			}
		}
	}
	
	private static boolean isNotNullString(String param)
	{
		if(param != null && !param.equals(""))
			return true;
		else
			return false;
	}
	
}

