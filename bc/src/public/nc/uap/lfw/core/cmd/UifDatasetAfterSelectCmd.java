package nc.uap.lfw.core.cmd;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.cmd.base.DatasetCmd;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowSet;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import nc.uap.lfw.util.LfwClassUtil;
import nc.vo.pub.SuperVO;

/**
 * 提供的数据集默认操作
 *
 */
public class UifDatasetAfterSelectCmd extends DatasetCmd {
	private String datasetId;
	public UifDatasetAfterSelectCmd(String dsId) {
		this.datasetId = dsId;
	}  

	/**
	 * 行选中方法，处理主子关系等通用逻辑
	 */
	public void execute() {
		LfwWidget widget = getLifeCycleContext().getViewContext().getView();
		Dataset masterDs = widget.getViewModels().getDataset(datasetId);
		//获取触发行
		Row masterSelecteRow = masterDs.getSelectedRow();
		if(masterSelecteRow == null)
			return;
		//获取数据集主子关系，根据主子关系进行子数据集加载
		DatasetRelations dsRels = widget.getViewModels().getDsrelations();
		if(dsRels != null)
		{
			DatasetRelation[] masterRels = dsRels.getDsRelations(masterDs.getId());
			for (int i = 0; i < masterRels.length; i++) {
				//获取子对应的外键值，并设置到VO条件中
				DatasetRelation dr = masterRels[i];
				Dataset detailDs = widget.getViewModels().getDataset(dr.getDetailDataset());
				String masterKey = dr.getMasterKeyField();
				String detailFk = dr.getDetailForeignKey();
				String keyValue = (String) masterSelecteRow.getValue(masterDs.getFieldSet().nameToIndex(masterKey));
				
				//标识是否是新主行
				boolean isNewMaster = false;
				if(keyValue == null){
					isNewMaster = true;
					keyValue = masterSelecteRow.getRowId();
				}
				//设置子表当前数据块（外键对应值)
				detailDs.setCurrentKey(keyValue);
				RowSet rowset = detailDs.getRowSet(keyValue, true);
				//默认获取第一页
				PaginationInfo pinfo = rowset.getPaginationInfo();
				pinfo.setPageIndex(0);
				
				String clazz = detailDs.getVoMeta();
				SuperVO vo = (SuperVO) LfwClassUtil.newInstance(clazz);
				
				if(!isNewMaster)
					vo.setAttributeValue(detailFk, keyValue);
				
				//进一步进行查询条件处理
				String wherePart = postProcessRowSelectVO(vo, detailDs);
				
				//进行元数据Tabcode特性支持的条件处理
				processTabCode(widget, detailDs, vo);
				//处理子ds的orderby
				postOrderPart(detailDs);
				String orderPart = (String) detailDs.getExtendAttributeValue("ORDER_PART");
				try {
					SuperVO[] vos = queryChildVOs(pinfo, vo, wherePart, isNewMaster, orderPart);
					pinfo.setRecordsCount(pinfo.getRecordsCount());
					new SuperVO2DatasetSerializer().serialize(vos, detailDs, Row.STATE_NORMAL);
					postProcessChildRowSelect(detailDs);
//					setPageState(detailDs);
					detailDs.setEnabled(false);
				} 
				catch (LfwBusinessException exp) {
					Logger.error(exp.getMessage(), exp);
					throw new LfwRuntimeException("查询对象出错," + exp.getMessage() + ",ds id:" + detailDs.getId(), "查询过程出现错误");


				}
			}
		}
		updateButtons();
	}

	
}
