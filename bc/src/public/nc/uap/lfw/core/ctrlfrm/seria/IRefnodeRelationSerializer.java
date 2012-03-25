package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.List;

import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.refnode.MasterFieldInfo;
import nc.uap.lfw.core.refnode.RefNodeRelation;
import nc.uap.lfw.core.refnode.RefNodeRelations;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class IRefnodeRelationSerializer extends BaseSerializer<RefNodeRelation> implements IViewZone{

	@Override
	public void deSerialize(RefNodeRelation rel, Document doc, Element parentNode) {
		Element relationNode = doc.createElement("RefNodeRelation");
		relationNode.setAttribute("id", rel.getId());
		if(isNotNullString(rel.getDetailRefNode()))
			relationNode.setAttribute("detailRefNode", rel.getDetailRefNode());
		List<MasterFieldInfo> masterInfo = rel.getMasterFieldInfos();
		if(masterInfo != null && masterInfo.size() > 0){
			for (int j = 0; j < masterInfo.size(); j++) {
				MasterFieldInfo master = masterInfo.get(j);
				Element masterNode = doc.createElement("MasterFieldInfo");
				relationNode.appendChild(masterNode);
				if(isNotNullString(master.getFieldId()))
					masterNode.setAttribute("fieldId", master.getFieldId());
				if(isNotNullString(master.getDsId()))
					masterNode.setAttribute("dsId", master.getDsId());
				if(isNotNullString(master.getFilterSql()))
					masterNode.setAttribute("filterSql", master.getFilterSql());
				if(isNotNullString(master.getNullProcess()))
					masterNode.setAttribute("nullProcess", master.getNullProcess());
			}
		}
		parentNode.appendChild(relationNode);
	}

	@Override
	public void serialize(Digester digester) {
		String refNodeRelationsClassName = RefNodeRelations.class.getName();
		digester.addObjectCreate("Widget/Models/RefNodes/RefNodeRelations", refNodeRelationsClassName);
		digester.addSetProperties("Widget/Models/RefNodes/RefNodeRelations");
		String refnodeRelationName = RefNodeRelation.class.getName();
		digester.addObjectCreate("Widget/Models/RefNodes/RefNodeRelations/RefNodeRelation", refnodeRelationName);
		digester.addSetProperties("Widget/Models/RefNodes/RefNodeRelations/RefNodeRelation");
		digester.addSetNext("Widget/Models/RefNodes/RefNodeRelations/RefNodeRelation", "addRefNodeRelation", refnodeRelationName);
		
		String masterFieldInfoName = MasterFieldInfo.class.getName();
		digester.addObjectCreate("Widget/Models/RefNodes/RefNodeRelations/RefNodeRelation/MasterFieldInfo", masterFieldInfoName);
		digester.addSetProperties("Widget/Models/RefNodes/RefNodeRelations/RefNodeRelation/MasterFieldInfo");
		digester.addSetNext("Widget/Models/RefNodes/RefNodeRelations/RefNodeRelation/MasterFieldInfo", "addMasterFieldInfo", masterFieldInfoName);
		
		digester.addSetNext("Widget/Models/RefNodes/RefNodeRelations", "setRefnodeRelations", refNodeRelationsClassName);
	}

}
