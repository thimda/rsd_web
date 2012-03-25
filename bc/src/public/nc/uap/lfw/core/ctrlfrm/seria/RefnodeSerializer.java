package nc.uap.lfw.core.ctrlfrm.seria;

import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.NCRefNode;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.core.refnode.SelfDefRefNode;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RefnodeSerializer extends BaseSerializer<IRefNode> implements IViewZone{

	@Override
	public void deSerialize(IRefNode refnode, Document doc, Element parentNode) {
		if(refnode instanceof RefNode){
			RefNode cd = (RefNode)refnode;
			if(cd.getFrom() == null){
				if(cd instanceof NCRefNode){
					NCRefNode ncrefnode = (NCRefNode)cd;
					Element ncRefDataNode = doc.createElement("NCRefNode");
					parentNode.appendChild(ncRefDataNode);
					ncRefDataNode.setAttribute("id", ncrefnode.getId());
//					if(isNotNullString(ncrefnode.getCaption()))
//						ncRefDataNode.setAttribute("caption", ncrefnode.getCaption());
//					ncRefDataNode.setAttribute("relationId", ncrefnode.getRelationId());
					ncRefDataNode.setAttribute("pagemodel", ncrefnode.getPageModel());
					ncRefDataNode.setAttribute("pagemeta", ncrefnode.getPagemeta());
					ncRefDataNode.setAttribute("path", ncrefnode.getPath());
					ncRefDataNode.setAttribute("refcode", ncrefnode.getRefcode());
					ncRefDataNode.setAttribute("multiSel",String.valueOf(ncrefnode.isMultiSel()));
					ncRefDataNode.setAttribute("refresh",String.valueOf(ncrefnode.isRefresh()));
					ncRefDataNode.setAttribute("dialog",String.valueOf(ncrefnode.isDialog()));
					ncRefDataNode.setAttribute("allowInput",String.valueOf(ncrefnode.isAllowInput()));
					ncRefDataNode.setAttribute("usePower",String.valueOf(ncrefnode.isUsePower()));
					ncRefDataNode.setAttribute("orgs",String.valueOf(ncrefnode.isOrgs()));
					ncRefDataNode.setAttribute("selLeafOnly",String.valueOf(ncrefnode.isSelLeafOnly()));
					ncRefDataNode.setAttribute("readDs", cd.getReadDs() == null?"":ncrefnode.getReadDs());
					ncRefDataNode.setAttribute("writeDs", ncrefnode.getWriteDs() == null?"":ncrefnode.getWriteDs());
					ncRefDataNode.setAttribute("readFields", ncrefnode.getReadFields() == null?"":ncrefnode.getReadFields());
					ncRefDataNode.setAttribute("writeFields", ncrefnode.getWriteFields() == null?"":ncrefnode.getWriteFields());
//					ncRefDataNode.setAttribute("relationId", ncrefnode.getRelationId() == null?"":ncrefnode.getRelationId());
					ncRefDataNode.setAttribute("refId", ncrefnode.getRefId() == null?"":ncrefnode.getRefId());
					ncRefDataNode.setAttribute("text", ncrefnode.getText());
					ncRefDataNode.setAttribute("path", ncrefnode.getPath());
					ncRefDataNode.setAttribute("i18nName", ncrefnode.getI18nName());
					ncRefDataNode.setAttribute("text", ncrefnode.getText());
					ncRefDataNode.setAttribute("langDir", ncrefnode.getLangDir());
					ncRefDataNode.setAttribute("dataListener", ncrefnode.getDataListener());
					ncRefDataNode.setAttribute("refnodeDelegator", ncrefnode.getRefnodeDelegator());
					if(isNotNullString(ncrefnode.getPageModel()))
						ncRefDataNode.setAttribute("pageModel", ncrefnode.getPageModel());
					if(isNotNullString(ncrefnode.getRefModel()))
						ncRefDataNode.setAttribute("refModel", ncrefnode.getRefModel());
				}else {
					Element refNode = doc.createElement("RefNode");
					parentNode.appendChild(refNode);
					refNode.setAttribute("id", cd.getId());
//					if(isNotNullString(cd.getCaption()))
//						refNode.setAttribute("caption", cd.getCaption());
					refNode.setAttribute("dialog",String.valueOf(cd.isDialog()));
					refNode.setAttribute("refresh",String.valueOf(cd.isRefresh()));
					refNode.setAttribute("selLeafOnly",String.valueOf(cd.isSelLeafOnly()));
					refNode.setAttribute("readDs", cd.getReadDs() == null?"":cd.getReadDs());
					refNode.setAttribute("writeDs", cd.getWriteDs() == null?"":cd.getWriteDs());
					refNode.setAttribute("isRefresh",String.valueOf(cd.isRefresh()));
					refNode.setAttribute("isDialog",String.valueOf(cd.isDialog()));
					refNode.setAttribute("allowInput",String.valueOf(cd.isAllowInput()));
					refNode.setAttribute("readFields", cd.getReadFields() == null?"":cd.getReadFields());
					refNode.setAttribute("writeFields", cd.getWriteFields() == null?"":cd.getWriteFields());
					refNode.setAttribute("refId", cd.getRefId() == null?"":cd.getRefId());
					refNode.setAttribute("pagemodel", cd.getPageModel());
					refNode.setAttribute("pagemeta", cd.getPagemeta());
					refNode.setAttribute("path", cd.getPath());
					refNode.setAttribute("multiSel",String.valueOf(cd.isMultiSel()));
					refNode.setAttribute("i18nName", cd.getI18nName());
					refNode.setAttribute("text", cd.getText());
					refNode.setAttribute("refnodeDelegator", cd.getRefnodeDelegator());
					refNode.setAttribute("refModel", cd.getRefModel());
					refNode.setAttribute("langDir", cd.getLangDir());
					refNode.setAttribute("path", cd.getPath());
					refNode.setAttribute("dataListener", cd.getDataListener());
					if(isNotNullString(cd.getPageModel()))
						refNode.setAttribute("pageModel", cd.getPageModel());
				}
			}
		}
		else if(refnode instanceof SelfDefRefNode){
			SelfDefRefNode cd = (SelfDefRefNode)refnode;
			Element refNode = doc.createElement("SelfRefNode");
			parentNode.appendChild(refNode);
			refNode.setAttribute("id", cd.getId());
			refNode.setAttribute("dialog",String.valueOf(cd.isDialog()));
			refNode.setAttribute("refresh",String.valueOf(cd.isRefresh()));
			refNode.setAttribute("isRefresh",String.valueOf(cd.isRefresh()));
			refNode.setAttribute("isDialog",String.valueOf(cd.isDialog()));
			refNode.setAttribute("path", cd.getPath());
			refNode.setAttribute("i18nName", cd.getI18nName());
			refNode.setAttribute("text", cd.getText());
			refNode.setAttribute("langDir", cd.getLangDir());
			refNode.setAttribute("path", cd.getPath());
		}
	
	}

	@Override
	public void serialize(Digester digester) {
		String ncrefNodeClassName = NCRefNode.class.getName();
		digester.addObjectCreate("Widget/Models/RefNodes/NCRefNode",
				ncrefNodeClassName);
		digester.addSetProperties("Widget/Models/RefNodes/NCRefNode");
		digester.addSetNext("Widget/Models/RefNodes/NCRefNode",
				"addRefNode", ncrefNodeClassName);
		
		String refNodeClassName = RefNode.class.getName();
		digester.addObjectCreate("Widget/Models/RefNodes/RefNode",
				refNodeClassName);
		digester.addSetProperties("Widget/Models/RefNodes/RefNode");
		digester.addSetNext("Widget/Models/RefNodes/RefNode",
				"addRefNode", refNodeClassName);
		
		String selfRefNodeClassName = SelfDefRefNode.class.getName();
		digester.addObjectCreate("Widget/Models/RefNodes/SelfRefNode",
				selfRefNodeClassName);
		digester.addSetProperties("Widget/Models/RefNodes/SelfRefNode");
		digester.addSetNext("Widget/Models/RefNodes/SelfRefNode",
				"addRefNode", selfRefNodeClassName);
	}

}
