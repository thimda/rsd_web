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
 * �ṩ�����ݼ�Ĭ�ϲ���
 *
 */
public class UifDatasetAfterSelectCmd extends DatasetCmd {
	private String datasetId;
	public UifDatasetAfterSelectCmd(String dsId) {
		this.datasetId = dsId;
	}  

	/**
	 * ��ѡ�з������������ӹ�ϵ��ͨ���߼�
	 */
	public void execute() {
		LfwWidget widget = getLifeCycleContext().getViewContext().getView();
		Dataset masterDs = widget.getViewModels().getDataset(datasetId);
		//��ȡ������
		Row masterSelecteRow = masterDs.getSelectedRow();
		if(masterSelecteRow == null)
			return;
		//��ȡ���ݼ����ӹ�ϵ���������ӹ�ϵ���������ݼ�����
		DatasetRelations dsRels = widget.getViewModels().getDsrelations();
		if(dsRels != null)
		{
			DatasetRelation[] masterRels = dsRels.getDsRelations(masterDs.getId());
			for (int i = 0; i < masterRels.length; i++) {
				//��ȡ�Ӷ�Ӧ�����ֵ�������õ�VO������
				DatasetRelation dr = masterRels[i];
				Dataset detailDs = widget.getViewModels().getDataset(dr.getDetailDataset());
				String masterKey = dr.getMasterKeyField();
				String detailFk = dr.getDetailForeignKey();
				String keyValue = (String) masterSelecteRow.getValue(masterDs.getFieldSet().nameToIndex(masterKey));
				
				//��ʶ�Ƿ���������
				boolean isNewMaster = false;
				if(keyValue == null){
					isNewMaster = true;
					keyValue = masterSelecteRow.getRowId();
				}
				//�����ӱ�ǰ���ݿ飨�����Ӧֵ)
				detailDs.setCurrentKey(keyValue);
				RowSet rowset = detailDs.getRowSet(keyValue, true);
				//Ĭ�ϻ�ȡ��һҳ
				PaginationInfo pinfo = rowset.getPaginationInfo();
				pinfo.setPageIndex(0);
				
				String clazz = detailDs.getVoMeta();
				SuperVO vo = (SuperVO) LfwClassUtil.newInstance(clazz);
				
				if(!isNewMaster)
					vo.setAttributeValue(detailFk, keyValue);
				
				//��һ�����в�ѯ��������
				String wherePart = postProcessRowSelectVO(vo, detailDs);
				
				//����Ԫ����Tabcode����֧�ֵ���������
				processTabCode(widget, detailDs, vo);
				//������ds��orderby
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
					throw new LfwRuntimeException("��ѯ�������," + exp.getMessage() + ",ds id:" + detailDs.getId(), "��ѯ���̳��ִ���");


				}
			}
		}
		updateButtons();
	}

	
}
