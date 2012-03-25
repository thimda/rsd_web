package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Map;

import nc.uap.lfw.core.comp.CodeTreeLevel;
import nc.uap.lfw.core.comp.RecursiveTreeLevel;
import nc.uap.lfw.core.comp.SimpleTreeLevel;
import nc.uap.lfw.core.comp.TreeLevel;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TreeSerializer extends BaseSerializer<TreeViewComp> implements IViewZone{

	@Override
	public void deSerialize(TreeViewComp tree, Document doc, Element parentNode) {
		if(tree.getFrom() == null){
			Element treeNode = doc.createElement("TreeViewComp");
			parentNode.appendChild(treeNode);
			treeNode.setAttribute("id", tree.getId());
			if(isNotNullString(tree.getCaption()))
				treeNode.setAttribute("caption", tree.getCaption());
//			if(isNotNullString(tree.getWidth()))
//				treeNode.setAttribute("width", tree.getWidth());
//			if(isNotNullString(tree.getHeight()))
//				treeNode.setAttribute("height", tree.getHeight());
			if(isNotNullString(tree.getText()))
				treeNode.setAttribute("text", tree.getText());
//			if(isNotNullString(tree.getTop()))
//				treeNode.setAttribute("top", tree.getTop());
//			if(isNotNullString(tree.getLeft()))
//				treeNode.setAttribute("left", tree.getLeft());
			if(isNotNullString(tree.getContextMenu()))
				treeNode.setAttribute("contextMenu", tree.getContextMenu());
			if(isNotNullString(tree.getLangDir()))
				treeNode.setAttribute("langDir", tree.getLangDir());
			if(isNotNullString(tree.getI18nName()))
				treeNode.setAttribute("i18nName", tree.getI18nName());
//			if(isNotNullString(tree.getPosition()))
//				treeNode.setAttribute("position", tree.getPosition());
			treeNode.setAttribute("withCheckBox", String.valueOf(tree.isWithCheckBox()));
			treeNode.setAttribute("withRoot", String.valueOf(tree.isWithRoot()));
			treeNode.setAttribute("rootOpen", String.valueOf(tree.isRootOpen()));
			treeNode.setAttribute("visible", String.valueOf(tree.isVisible()));
			treeNode.setAttribute("enabled", String.valueOf(tree.isEnabled()));
			treeNode.setAttribute("dragEnable", String.valueOf(tree.isDragEnable()));
			treeNode.setAttribute("checkBoxModel", String.valueOf(tree.getCheckBoxModel()));
//			if(isNotNullString(tree.getClassName()))
//				treeNode.setAttribute("className", tree.getClassName());
			TreeLevel level = tree.getTopLevel();
			if(level != null){
				Element levelNode = null;
				if(level instanceof CodeTreeLevel)
				{
					levelNode = doc.createElement("CodeTreeLevel");
					treeNode.appendChild(levelNode);
					if(isNotNullString(((CodeTreeLevel) level).getCodeField()))
						levelNode.setAttribute("codeField", ((CodeTreeLevel) level).getCodeField());
					if(isNotNullString(((CodeTreeLevel) level).getCodeRule()))
						levelNode.setAttribute("codeRule", ((CodeTreeLevel) level).getCodeRule());
				}
				else if(level instanceof RecursiveTreeLevel)
				{
					levelNode = doc.createElement("RecursiveTreeLevel");
					treeNode.appendChild(levelNode);
					levelNode.setAttribute("recursiveKeyField", ((RecursiveTreeLevel) level).getRecursiveKeyField());
					levelNode.setAttribute("recursivePKeyField", ((RecursiveTreeLevel) level).getRecursivePKeyField());
				}
				
				else if(level instanceof SimpleTreeLevel)
				{
					levelNode = doc.createElement("SimpleTreeLevel");
					treeNode.appendChild(levelNode);
				}
				if(isNotNullString(level.getId()))	
					levelNode.setAttribute("id", level.getId());
				if(isNotNullString(level.getDataset()))
					levelNode.setAttribute("dataset", level.getDataset());
				if(isNotNullString(level.getMasterKeyField()))
					levelNode.setAttribute("masterKeyField", level.getMasterKeyField());
				if(isNotNullString(level.getLabelFields()))
					levelNode.setAttribute("labelFields", level.getLabelFields());
				if(isNotNullString(level.getLabelDelims()))
					levelNode.setAttribute("labelDelims", level.getLabelDelims());
				if(isNotNullString(level.getContextMenu()))
					levelNode.setAttribute("contextMenu", level.getContextMenu());
				
				Element pNode = levelNode;
				while(level.getChildTreeLevel() != null)
				{
					level = level.getChildTreeLevel();
					Element childLevelNode = null;
					 if(level instanceof CodeTreeLevel)
					{
						childLevelNode = doc.createElement("CodeTreeLevel");
						pNode.appendChild(childLevelNode);
						childLevelNode.setAttribute("codeField", ((CodeTreeLevel) level).getCodeField());
						childLevelNode.setAttribute("codeRule", ((CodeTreeLevel) level).getCodeRule());
					}
					 else if(level instanceof RecursiveTreeLevel)
					{
						childLevelNode = doc.createElement("RecursiveTreeLevel");
						pNode.appendChild(childLevelNode);
						childLevelNode.setAttribute("recursiveKeyField", ((RecursiveTreeLevel) level).getRecursiveKeyField());
						childLevelNode.setAttribute("recursivePKeyField", ((RecursiveTreeLevel) level).getRecursivePKeyField());
					}
					
					else if(level instanceof SimpleTreeLevel)
					{
						childLevelNode = doc.createElement("SimpleTreeLevel");
						pNode.appendChild(childLevelNode);
					}
					if(isNotNullString(level.getId()))	
						childLevelNode.setAttribute("id", level.getId());
					if(isNotNullString(level.getDataset()))
						childLevelNode.setAttribute("dataset", level.getDataset());
					if(isNotNullString(level.getMasterKeyField()))
						childLevelNode.setAttribute("masterKeyField", level.getMasterKeyField());
					if(isNotNullString(level.getLabelFields()))
						childLevelNode.setAttribute("labelFields", level.getLabelFields());
					if(isNotNullString(level.getLabelDelims()))
						childLevelNode.setAttribute("labelDelims", level.getLabelDelims());
					if(isNotNullString(level.getDetailKeyParameter()))
						childLevelNode.setAttribute("detailKeyParameter", level.getDetailKeyParameter());
					if(isNotNullString(level.getContextMenu()))
						childLevelNode.setAttribute("contextMenu", level.getContextMenu());
					pNode = childLevelNode;
				}
			}
			Map<String, JsListenerConf> jsListeners = tree.getListenerMap();
			if(jsListeners != null && jsListeners.size() > 0)
				PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), treeNode);
			
			//Events
			AMCUtil.addEvents(doc, tree.getEventConfs(), treeNode);
		}
	}

	@Override
	public void serialize(Digester digester) {
		String treeViewClassName = TreeViewComp.class.getName();
		digester.addObjectCreate("Widget/Components/TreeViewComp", treeViewClassName);
		digester.addSetProperties("Widget/Components/TreeViewComp");
		digester.addSetNext("Widget/Components/TreeViewComp", "addComponent",
				treeViewClassName);
		//TODO:多层level的支持，如果有未知的N层怎么解析？现在暂时认为最多有三层
		String simpleTreeLevelClassName = SimpleTreeLevel.class.getName();
		String recursiveTreeLevelClassName = RecursiveTreeLevel.class.getName();
		String codeTreeLevelClassName = CodeTreeLevel.class.getName();
		
		// 简单-递归
		digester.addObjectCreate("Widget/Components/TreeViewComp/SimpleTreeLevel", simpleTreeLevelClassName);
		digester.addSetProperties("Widget/Components/TreeViewComp/SimpleTreeLevel");
		digester.addObjectCreate("Widget/Components/TreeViewComp/SimpleTreeLevel/RecursiveTreeLevel", recursiveTreeLevelClassName);
		digester.addSetProperties("Widget/Components/TreeViewComp/SimpleTreeLevel/RecursiveTreeLevel");
		digester.addSetNext("Widget/Components/TreeViewComp/SimpleTreeLevel/RecursiveTreeLevel", "setChildTreeLevel");
		
		// 简单-简单
		digester.addObjectCreate("Widget/Components/TreeViewComp/SimpleTreeLevel/SimpleTreeLevel", simpleTreeLevelClassName);
		digester.addSetProperties("Widget/Components/TreeViewComp/SimpleTreeLevel/SimpleTreeLevel");
		digester.addSetNext("Widget/Components/TreeViewComp/SimpleTreeLevel/SimpleTreeLevel", "setChildTreeLevel");
		
		// 简单-编码
		digester.addObjectCreate("Widget/Components/TreeViewComp/SimpleTreeLevel/CodeTreeLevel", codeTreeLevelClassName);
		digester.addSetProperties("Widget/Components/TreeViewComp/SimpleTreeLevel/CodeTreeLevel");
		digester.addSetNext("Widget/Components/TreeViewComp/SimpleTreeLevel/CodeTreeLevel", "setChildTreeLevel");
		
		// 递归
		digester.addObjectCreate("Widget/Components/TreeViewComp/RecursiveTreeLevel", recursiveTreeLevelClassName);
		digester.addSetProperties("Widget/Components/TreeViewComp/RecursiveTreeLevel");
		// 递归-简单
		digester.addObjectCreate("Widget/Components/TreeViewComp/RecursiveTreeLevel/SimpleTreeLevel", simpleTreeLevelClassName);
		digester.addSetProperties("Widget/Components/TreeViewComp/RecursiveTreeLevel/SimpleTreeLevel");
		digester.addSetNext("Widget/Components/TreeViewComp/RecursiveTreeLevel/SimpleTreeLevel", "setChildTreeLevel");
		// 递归-简单-简单
		digester.addObjectCreate("Widget/Components/TreeViewComp/RecursiveTreeLevel/SimpleTreeLevel/SimpleTreeLevel", simpleTreeLevelClassName);
		digester.addSetProperties("Widget/Components/TreeViewComp/RecursiveTreeLevel/SimpleTreeLevel/SimpleTreeLevel");
		digester.addSetNext("Widget/Components/TreeViewComp/RecursiveTreeLevel/SimpleTreeLevel/SimpleTreeLevel", "setChildTreeLevel");
		
		// 递归-递归
		digester.addObjectCreate("Widget/Components/TreeViewComp/RecursiveTreeLevel/RecursiveTreeLevel", recursiveTreeLevelClassName);
		digester.addSetProperties("Widget/Components/TreeViewComp/RecursiveTreeLevel/RecursiveTreeLevel");
		digester.addSetNext("Widget/Components/TreeViewComp/RecursiveTreeLevel/RecursiveTreeLevel", "setChildTreeLevel");
		// 递归-递归-递归
		digester.addObjectCreate("Widget/Components/TreeViewComp/RecursiveTreeLevel/RecursiveTreeLevel/RecursiveTreeLevel", recursiveTreeLevelClassName);
		digester.addSetProperties("Widget/Components/TreeViewComp/RecursiveTreeLevel/RecursiveTreeLevel/RecursiveTreeLevel");
		digester.addSetNext("Widget/Components/TreeViewComp/RecursiveTreeLevel/RecursiveTreeLevel/RecursiveTreeLevel", "setChildTreeLevel");
		// 递归-递归-简单
		digester.addObjectCreate("Widget/Components/TreeViewComp/RecursiveTreeLevel/RecursiveTreeLevel/SimpleTreeLevel", simpleTreeLevelClassName);
		digester.addSetProperties("Widget/Components/TreeViewComp/RecursiveTreeLevel/RecursiveTreeLevel/SimpleTreeLevel");
		digester.addSetNext("Widget/Components/TreeViewComp/RecursiveTreeLevel/RecursiveTreeLevel/SimpleTreeLevel", "setChildTreeLevel");
		
		// 递归-编码
		digester.addObjectCreate("Widget/Components/TreeViewComp/RecursiveTreeLevel/CodeTreeLevel", codeTreeLevelClassName);
		digester.addSetProperties("Widget/Components/TreeViewComp/RecursiveTreeLevel/CodeTreeLevel");
		digester.addSetNext("Widget/Components/TreeViewComp/RecursiveTreeLevel/CodeTreeLevel", "setChildTreeLevel");
		
		digester.addObjectCreate("Widget/Components/TreeViewComp/CodeTreeLevel", codeTreeLevelClassName);
		digester.addSetProperties("Widget/Components/TreeViewComp/CodeTreeLevel");
		digester.addObjectCreate("Widget/Components/TreeViewComp/CodeTreeLevel/SimpleTreeLevel", simpleTreeLevelClassName);
		digester.addSetProperties("Widget/Components/TreeViewComp/CodeTreeLevel/SimpleTreeLevel");
		digester.addSetNext("Widget/Components/TreeViewComp/CodeTreeLevel/SimpleTreeLevel", "setChildTreeLevel");
		digester.addObjectCreate("Widget/Components/TreeViewComp/CodeTreeLevel/RecursiveTreeLevel", recursiveTreeLevelClassName);
		digester.addSetProperties("Widget/Components/TreeViewComp/CodeTreeLevel/RecursiveTreeLevel");
		digester.addSetNext("Widget/Components/TreeViewComp/CodeTreeLevel/RecursiveTreeLevel", "setChildTreeLevel");
		digester.addObjectCreate("Widget/Components/TreeViewComp/CodeTreeLevel/CodeTreeLevel", codeTreeLevelClassName);
		digester.addSetProperties("Widget/Components/TreeViewComp/CodeTreeLevel/CodeTreeLevel");
		digester.addSetNext("Widget/Components/TreeViewComp/CodeTreeLevel/CodeTreeLevel", "setChildTreeLevel");
		
//		digester.addObjectCreate("Widget/Components/TreeViewComp/TreeLevel/TreeLevel",
//				simpleTreeLevelClassName);
//		digester
//				.addSetProperties("Widget/Components/TreeViewComp/TreeLevel/TreeLevel");
//		digester.addSetNext("Widget/Components/TreeViewComp/TreeLevel/TreeLevel",
//				"setChildTreeLevel", simpleTreeLevelClassName);
//
//		digester.addObjectCreate(
//				"Widget/Components/TreeViewComp/TreeLevel/TreeLevel/TreeLevel",
//				simpleTreeLevelClassName);
//		digester
//				.addSetProperties("Widget/Components/TreeViewComp/TreeLevel/TreeLevel/TreeLevel");
//		digester.addSetNext(
//				"Widget/Components/TreeViewComp/TreeLevel/TreeLevel/TreeLevel",
//				"setChildTreeLevel", simpleTreeLevelClassName);
		
		digester.addSetNext("Widget/Components/TreeViewComp/SimpleTreeLevel", "setTopLevel");
		digester.addSetNext("Widget/Components/TreeViewComp/RecursiveTreeLevel", "setTopLevel");
		digester.addSetNext("Widget/Components/TreeViewComp/CodeTreeLevel", "setTopLevel");
		ListenersParser.parseListeners(digester, "Widget/Components/TreeViewComp/Listeners", TreeViewComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/TreeViewComp", TreeViewComp.class);
	}

}
