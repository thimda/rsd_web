package nc.uap.lfw.core.serializer.impl;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.common.ExtAttrConstants;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.util.LfwClassUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IExAggVO;

public class Datasets2AggVOSerializer {
	/**
	 * 将数据集组装成聚合VO
	 * @param masterDs
	 * @param detailDss
	 * @param aggVoClazz
	 * @return
	 */
	public AggregatedValueObject serialize(Dataset masterDs, Dataset[] detailDss, String aggVoClazz) 
	{
		if(masterDs != null)
		{
			AggregatedValueObject aggVo = (AggregatedValueObject) LfwClassUtil.newInstance(aggVoClazz);
			Dataset2SuperVOSerializer ser = new Dataset2SuperVOSerializer();
			SuperVO[] pvos = ser.serialize(masterDs, masterDs.getSelectedRow());
			if(pvos == null || pvos.length == 0)
				return null;
			aggVo.setParentVO(pvos[0]);
			if(aggVo instanceof IExAggVO){
				if(detailDss != null && detailDss.length > 0)
				{
					for(int i = 0; i < detailDss.length; i++)
					{
						Dataset detailDs = detailDss[i];
						SuperVO[] vos = ser.serialize(detailDs);
						String tableId = null;
						Object tabcode = detailDs.getExtendAttributeValue(ExtAttrConstants.TAB_CODE);
						if(tabcode != null)
							tableId = tabcode.toString();
						else{
							Object parentField = detailDs.getExtendAttributeValue(ExtAttrConstants.PARENT_FIELD);
							if(parentField != null)
								tableId = parentField.toString();
						}
						if(tableId == null)
							tableId = detailDs.getId();
						((IExAggVO)aggVo).setTableVO(tableId, vos);
					}
				}
				return aggVo;
			}
			else{
				if(detailDss != null && detailDss.length > 0)
				{
					Dataset detailDs = detailDss[0];
					SuperVO[] vos = ser.serialize(detailDs);
					aggVo.setChildrenVO(vos);
				}
				return aggVo;
			}
		}
		return null;
	}
	
	
	/**
	 * 根据masterDs、masterDS的选中行得到序列化后的aggvo
	 * @param masterDs
	 * @param detailDss
	 * @param aggVoClazz
	 * @return
	 */
	
	public AggregatedValueObject serialize(Dataset masterDs, Row masterRow, Dataset[] detailDss, String aggVoClazz) 
	{
		if(masterDs != null)
		{
			AggregatedValueObject aggVo = (AggregatedValueObject) LfwClassUtil.newInstance(aggVoClazz);
			Dataset2SuperVOSerializer ser = new Dataset2SuperVOSerializer();
			SuperVO[] pvos = ser.serialize(masterDs, masterRow);
			aggVo.setParentVO(pvos[0]);
			if(aggVo instanceof IExAggVO){
				if(detailDss != null && detailDss.length > 0)
				{
					for(int i = 0; i < detailDss.length; i++)
					{
						Dataset detailDs = detailDss[i];
						SuperVO[] vos = ser.serialize(detailDs);
						String tableId = null;
						Object tabcode = detailDs.getExtendAttributeValue(ExtAttrConstants.TAB_CODE);
						if(tabcode != null)
							tableId = tabcode.toString();
						else{
							Object parentField = detailDs.getExtendAttributeValue(ExtAttrConstants.PARENT_FIELD);
							if(parentField != null)
								tableId = parentField.toString();
						}
						if(tableId == null)
							tableId = detailDs.getId();
						((IExAggVO)aggVo).setTableVO(tableId, vos);
					}
				}
				return aggVo;
			}
			else{
				if(detailDss != null && detailDss.length > 0)
				{
					Dataset detailDs = detailDss[0];
					SuperVO[] vos = ser.serialize(detailDs);
					aggVo.setChildrenVO(vos);
				}
				return aggVo;
			}
		}
		return null;
	}
	
	
	/**
	 * 将数据集组装成聚合VO
	 * 
	 * @param masterDs
	 * @param aggVoClazz
	 * @return
	 */
	public AggregatedValueObject[] serialize(Dataset masterDs,String aggVoClazz) 
	{
		if(masterDs != null)
		{
			List<AggregatedValueObject> aggVos = new ArrayList<AggregatedValueObject>();
			Dataset2SuperVOSerializer ser = new Dataset2SuperVOSerializer();
			SuperVO[] pvos = ser.serialize(masterDs, masterDs.getChangedRows());
			for (int i = 0; i< pvos.length; i++){
				AggregatedValueObject aggVo = (AggregatedValueObject) LfwClassUtil.newInstance(aggVoClazz);				
				aggVo.setParentVO(pvos[i]);
				aggVos.add(aggVo);
			}
			return aggVos.toArray(new AggregatedValueObject[0]);
		}
		return null;
	}
	
	/**
	 * 用aggVo中的数据更新ds中的数据
	 * @param aggVO
	 * @param masterDs
	 * @param detailDss
	 */
	public void update(AggregatedValueObject aggVO, Dataset masterDs, Dataset[] detailDss) {
		SuperVO2DatasetSerializer s = new SuperVO2DatasetSerializer();
		s.update(new SuperVO[]{(SuperVO) aggVO.getParentVO()}, masterDs);
		if(detailDss != null && detailDss.length > 0){
			if(aggVO instanceof IExAggVO){
				String[] codes = ((IExAggVO)aggVO).getTableCodes();
				for (int i = 0; i < codes.length; i++) {
					SuperVO[] vos = (SuperVO[]) ((IExAggVO)aggVO).getTableVO(codes[i]);
					s.update(vos, getDataset(detailDss, codes[i]));
				}
			}
			else{
				SuperVO[] vos = (SuperVO[]) aggVO.getChildrenVO();
				s.update(vos, detailDss[0]);
			}
		}
	}
	private Dataset getDataset(Dataset[] dss, String id){
		for (int i = 0; i < dss.length; i++) {
			String tableId = null;
			Dataset detailDs = dss[i];
			Object tabcode = detailDs.getExtendAttributeValue(ExtAttrConstants.TAB_CODE);
			if(tabcode != null)
				tableId = tabcode.toString();
			else{
				Object parentField = detailDs.getExtendAttributeValue(ExtAttrConstants.PARENT_FIELD);
				if(parentField != null)
					tableId = parentField.toString();
			}if(tableId == null)
				tableId = detailDs.getId();
			if(tableId.equals(id))
				return dss[i];
		}
		return null;
	}
}
