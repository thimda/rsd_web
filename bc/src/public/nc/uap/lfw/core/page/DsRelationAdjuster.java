package nc.uap.lfw.core.page;

import java.util.Iterator;
import java.util.List;

import nc.md.innerservice.MDQueryService;
import nc.md.model.IAssociation;
import nc.md.model.IBusinessEntity;
import nc.uap.lfw.core.common.ExtAttrConstants;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.log.LfwLogger;

public final class DsRelationAdjuster {
	private LfwWidget widget;
	public DsRelationAdjuster(LfwWidget widget){
		this.widget = widget;
	}
	
	public void adjust() {
		DatasetRelations dsRelations = widget.getViewModels().getDsrelations();
		if(dsRelations != null){
			DatasetRelation[] rels = dsRelations.getDsRelations();
			if(rels != null){
				for (int i = 0; i < rels.length; i++) {
					DatasetRelation rel = rels[i];
					String masterDsId = rel.getMasterDataset();
					String slaveDsId = rel.getDetailDataset();
					Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
					Dataset slaveDs = widget.getViewModels().getDataset(slaveDsId);
					if(slaveDs == null){
						LfwLogger.error("设置了不正确的rel id:" + rel.getId());
						return;
					}
					if((slaveDs.getExtendAttributeValue(ExtAttrConstants.PARENT_FIELD)) == null && (masterDs instanceof MdDataset) && (slaveDs instanceof MdDataset)){
						try{
							IBusinessEntity entity = MDQueryService.lookupMDQueryService().getBusinessEntityByFullName(((MdDataset)masterDs).getObjMeta());
							IBusinessEntity childEntity = MDQueryService.lookupMDQueryService().getBusinessEntityByFullName(((MdDataset)slaveDs).getObjMeta());
							List<IAssociation> list = entity.getAssociations();
							if(list != null){
								Iterator<IAssociation> assIt = list.iterator();
								while(assIt.hasNext()){
									IAssociation ass = assIt.next();
									if(ass.getEndElement().getAssElement().getID().equals(childEntity.getID())){
											String parentField = ass.getStartAttribute().getName();
											slaveDs.setExtendAttribute(ExtAttrConstants.PARENT_FIELD, parentField);
											break;
										}
								}
							}
						}
						catch(Exception e){
							LfwLogger.error(e.getMessage(), e);
						}
					}
				}
			}
		}
	}
}
