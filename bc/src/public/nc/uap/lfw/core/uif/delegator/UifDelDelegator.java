package nc.uap.lfw.core.uif.delegator;

import java.util.ArrayList;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.crud.ILfwCRUDService;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.IOperatorState;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.serializer.impl.Datasets2AggVOSerializer;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

public class UifDelDelegator extends AbstractUifDelegator{
	private String widgetId;
	private String masterDsId;
	private String aggVoClazz;
	
	public UifDelDelegator(String widgetId, String masterDsId, String aggVoClazz){
		this.widgetId = widgetId;
		this.masterDsId = masterDsId;
		this.aggVoClazz = aggVoClazz;
	}
	
	public void execute() {
		if(this.widgetId == null)
			throw new LfwRuntimeException("未指定片段id!");
		LfwWidget widget = getGlobalContext().getPageMeta().getWidget(this.widgetId);
		if(widget == null)
			throw new LfwRuntimeException("片段为空,widgetId=" + widgetId + "!");
		
		if(this.masterDsId == null)
			throw new LfwRuntimeException("未指定数据集id!");
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if(masterDs == null)
			throw new LfwRuntimeException("数据集为空,数据集id=" + masterDsId + "!");
		
		// 未指定aggVo,则从ds配置的元数据中获得
//		if(aggVoClazz == null)
//		{
//			if(masterDs.getVoMeta() != null && !masterDs.getVoMeta().equals(""))
//			{	
//				try {
//					IBusinessEntity entity = (IBusinessEntity) MDQueryService.lookupMDQueryService().getBeanByFullClassName(masterDs.getVoMeta());
//					AggregatedValueObject aggVo = (AggregatedValueObject) entity.getBeanStyle().newInstance(null);
//					aggVoClazz = aggVo.getClass().getName();
//				} catch (MetaDataException e) {
//					Logger.error(e, e);
//					throw new LfwRuntimeException(e);
//				}
//				
//	//			ArrayList<LfwExAggVO> vos = new ArrayList<LfwExAggVO>();
//	//			Datasets2AggVOSerializer ser = new Datasets2AggVOSerializer();
//				//AggregatedValueObject aggVo = ser.serialize(masterDs, detailDss, aggVoClazz);
//			}
//		}
		
		// 要删除的所有主表行
		Row[] delRows = masterDs.getSelectedRows();
		if(delRows != null && delRows.length > 0){
			ArrayList<Dataset> detailDss = null;
			DatasetRelation[] rels = null;
			if (widget.getViewModels().getDsrelations() != null) {
				rels = widget.getViewModels().getDsrelations().getDsRelations(masterDsId);
			}
			if (rels != null) {
				// 所有子表
				detailDss = new ArrayList<Dataset>();
				for (int i = 0; i < rels.length; i++) {
					int index = masterDs.getFieldSet().nameToIndex(
							rels[i].getMasterKeyField());
					Dataset detailDs = widget.getViewModels().getDataset(
							rels[i].getDetailDataset());
					detailDs.setExtendAttribute("parent_index", index);
					if (detailDs != null) {
						detailDss.add(detailDs);
					}
				}
			}
			
			ArrayList<AggregatedValueObject> vos = new ArrayList<AggregatedValueObject>();
			// 持久化 
			Datasets2AggVOSerializer ser = new Datasets2AggVOSerializer();
			vos.add(ser.serialize(masterDs, detailDss == null ? null: detailDss.toArray(new Dataset[0]), aggVoClazz));
			//getCrudService().deleteVo(vos.get(0), true);
			onDeleteVO(vos, true);
					
			// 删除主表数据
			for (int i = 0; i < delRows.length; i++) {
				masterDs.removeRow(masterDs.getRowIndex(delRows[i]));
			}
			
			if(detailDss != null){
				int size = detailDss.size();
				if(size > 0){
					for (int i = 0; i < size; i++) {
						Dataset detailDs = detailDss.get(i);
						// 删除子表数据
						for (int j = 0; j < delRows.length; j++) {
							Integer index = (Integer) detailDs.getExtendAttributeValue("parent_index");
							// 移除子表对应的数据片段
							if(delRows[j].getValue(index) != null)
								detailDs.removeRowSet((String) delRows[j].getValue(index));
						}
					}
				}
			}
			
		}
		
		// 设置操作状态
//		if(masterDs.isControloperatorStatus())
//			getGlobalContext().getPageMeta().setOperatorState(IOperatorState.INIT);
//		if(masterDs.isControlwidgetopeStatus())
//			getGlobalContext().getPageMeta().getWidget(widgetId).setOperatorState(IOperatorState.INIT);

	}
	
	protected void onDeleteVO(ArrayList<AggregatedValueObject> vos,boolean trueDel){
		try {
			getCrudService().deleteVo(vos.get(0), trueDel);
		} catch (LfwBusinessException e) {
			Logger.error(e, e);
			throw new LfwRuntimeException(e);
		}
	}
	
	protected ILfwCRUDService<SuperVO, AggregatedValueObject> getCrudService() {
		return CRUDHelper.getCRUDService();
	}
}
