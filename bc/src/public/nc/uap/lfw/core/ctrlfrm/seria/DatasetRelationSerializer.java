package nc.uap.lfw.core.ctrlfrm.seria;

import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DatasetRelationSerializer extends BaseSerializer<DatasetRelation> implements IViewZone{

	@Override
	public void deSerialize(DatasetRelation rel, Document doc, Element parentNode) {
		if(rel.getFrom() == null){
			Element relationNode = doc.createElement("DatasetRelation");
			relationNode.setAttribute("id", rel.getId());
			if(isNotNullString(rel.getMasterDataset()))
				relationNode.setAttribute("masterDataset", rel.getMasterDataset());
			if(isNotNullString(rel.getMasterKeyField()))
				relationNode.setAttribute("masterKeyField", rel.getMasterKeyField());
			if(isNotNullString(rel.getDetailDataset()))
				relationNode.setAttribute("detailDataset", rel.getDetailDataset());
			if(isNotNullString(rel.getDetailForeignKey()))
				relationNode.setAttribute("detailForeignKey", rel.getDetailForeignKey());
			parentNode.appendChild(relationNode);
		}
	}

	@Override
	public void serialize(Digester digester) {
		String datasetRelationsClassName = DatasetRelations.class.getName();
		digester.addObjectCreate("Widget/Models/Datasets/DatasetRelations", datasetRelationsClassName);
		digester.addSetProperties("Widget/Models/Datasets/DatasetRelations");
		String dsRelationName = DatasetRelation.class.getName();
		digester.addObjectCreate("Widget/Models/Datasets/DatasetRelations/DatasetRelation", dsRelationName);
		digester.addSetProperties("Widget/Models/Datasets/DatasetRelations/DatasetRelation");
		digester.addSetNext("Widget/Models/Datasets/DatasetRelations/DatasetRelation", "addDsRelation", dsRelationName);
		digester.addSetNext("Widget/Models/Datasets/DatasetRelations", "setDsrelations", datasetRelationsClassName);
	}

}
