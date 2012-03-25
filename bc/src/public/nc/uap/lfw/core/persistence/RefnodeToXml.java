package nc.uap.lfw.core.persistence;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.page.config.RefNodeConf;
import nc.uap.lfw.core.page.manager.PoolObjectManager;
import nc.uap.lfw.core.refnode.BaseRefNode;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.SelfDefRefNode;
import nc.vo.jcom.xml.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 将一个refnode保存为xml
 * @author zhangxya
 *
 */

public class RefnodeToXml {
	public static void toXml(String filePath, String fileName, IRefNode node) 
	{
		Document doc = XMLUtil.getNewDocument();
		Element rootNode = null;
		rootNode = doc.createElement("RefNode");
		if(node instanceof RefNodeConf){
			RefNodeConf refnode = (RefNodeConf)node;
			rootNode.setAttribute("readDs", refnode.getReadDs() == null?"":refnode.getReadDs());
			rootNode.setAttribute("pageModel", refnode.getPageModel());
			rootNode.setAttribute("refModel", refnode.getRefModel());
			rootNode.setAttribute("pagemeta", refnode.getPagemeta());
			rootNode.setAttribute("path", refnode.getPath());
			rootNode.setAttribute("multiSel",String.valueOf(refnode.isMultiSel()));
			rootNode.setAttribute("i18nName", refnode.getI18nName());
			rootNode.setAttribute("text", refnode.getText());
			rootNode.setAttribute("langDir", refnode.getLangDir());
		}else if(node instanceof SelfDefRefNode){
			SelfDefRefNode refnode = (SelfDefRefNode)node;
			rootNode.setAttribute("path", refnode.getPath());
			rootNode.setAttribute("i18nName", refnode.getI18nName());
			rootNode.setAttribute("text", refnode.getText());
			rootNode.setAttribute("langDir", refnode.getLangDir());
		}else if(node instanceof BaseRefNode){
			BaseRefNode refnode = (BaseRefNode)node;
			rootNode.setAttribute("path", refnode.getPath());
			rootNode.setAttribute("i18nName", refnode.getI18nName());
			rootNode.setAttribute("text", refnode.getText());
			rootNode.setAttribute("langDir", refnode.getLangDir());
		}
		
		doc.appendChild(rootNode);
			// 写出文件
		PersistenceUtil.toXmlFile(doc, filePath, fileName);
		// 刷新缓存,同步最新数据
		PoolObjectManager.refreshRefNodePool(LfwRuntimeEnvironment.getRootPath(), node);
	}
	
	public static void deletePoolRefNode(IRefNode refnode){
		PoolObjectManager.removeRefNodeFromPool(LfwRuntimeEnvironment.getRootPath(), refnode);
	}
	
}
