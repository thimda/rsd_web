package nc.uap.lfw.core.uif.delegator;

import java.util.ArrayList;
import java.util.List;

import nc.bs.logging.Logger;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.lfw.core.md.util.LfwMdUtil;
import nc.md.innerservice.MDQueryService;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.uap.lfw.core.common.ExtAttrConstants;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.crud.ILfwCRUDService;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.exception.LfwValidateException;
import nc.uap.lfw.core.page.IBusinessState;
import nc.uap.lfw.core.page.IOperatorState;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.serializer.impl.Datasets2AggVOSerializer;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBillStatus;

/**
 * ����ʱ��������޸Ļ�����������
 * 
 * @author dingrf
 *
 */
public class UifSaveMultiDelegator extends AbstractUifDelegator {

	private static final String IFLOW_BIZ_ITF = "nc.itf.uap.pf.metadata.IFlowBizItf";
	private String widgetId;
	private String masterDsId;
	private String aggVoClazz;

	public UifSaveMultiDelegator(String widgetId, String masterDsId, String aggVoClazz) {
		this.widgetId = widgetId;
		this.masterDsId = masterDsId;
		this.aggVoClazz = aggVoClazz;
	}

	public void execute() {
		if (this.widgetId == null)
			throw new LfwRuntimeException("δָ��Ƭ��id!");
		LfwWidget widget = getGlobalContext().getPageMeta().getWidget(
				this.widgetId);
		if (widget == null)
			throw new LfwRuntimeException("Ƭ��Ϊ��,widgetId=" + widgetId + "!");
		if (this.masterDsId == null)
			throw new LfwRuntimeException("δָ�������ݼ�id!");
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if (masterDs == null)
			throw new LfwRuntimeException("���ݼ�Ϊ��,���ݼ�id=" + masterDsId + "!");

		if (this.aggVoClazz == null)
			throw new LfwRuntimeException("δָ���ۺ�vo����!");

		List<String> idList = new ArrayList<String>();
		idList.add(masterDsId);
		getGlobalContext().clearUndo(widgetId, idList.toArray(new String[0]));
		
		doValidate(masterDs);
		Datasets2AggVOSerializer ser = new Datasets2AggVOSerializer();
		AggregatedValueObject[] aggVos = ser.serialize(masterDs, aggVoClazz);
		try {
			setBillStatus(masterDs, aggVos);
			// ����ǰ���Ψһ��
			onBeforeVOSave(aggVos);
			//����Ƿ�����ֹ������쳣
			if(!checkBeforeVOSave(aggVos))
				return;
			onVoSave(aggVos);
		} 
		catch (Exception e) {
			dealWithException(e);
		}

		//�����ִ�е��߼�
		onAfterVOSave(widget, masterDs, ser, aggVos);
		
		// ���ò���״̬
//		if (masterDs.isControloperatorStatus())
//			getGlobalContext().getPageMeta().setOperatorState(
//					IOperatorState.SINGLESEL);
//		if (masterDs.isControlwidgetopeStatus())
//			getGlobalContext().getPageMeta().getWidget(widgetId)
//					.setOperatorState(IOperatorState.SINGLESEL);
//		getGlobalContext().getPageMeta().setBusiState(IBusinessState.FREE);
		masterDs.setEnabled(false);
	}

	/**
	 * �쳣�Ĵ�����
	 * @param e
	 */
	protected void dealWithException(Exception e) {
		Logger.error(e, e);
		throw new LfwRuntimeException(e.getMessage());
	}

	/**
	 * ����������, ��������ҳ������
	 * @param widget
	 * @param masterDs
	 * @param ser
	 * @param aggVos
	 */
	protected void onAfterVOSave(LfwWidget widget, Dataset masterDs, Datasets2AggVOSerializer ser,AggregatedValueObject[] aggVos) {
		for (int i = 0; i<aggVos.length; i++){
			ser.update(aggVos[i], masterDs, null);
		}
	}

	protected void setBillStatus(Dataset masterDs, AggregatedValueObject[] aggVos) {
		Object metaObj = masterDs
				.getExtendAttributeValue(ExtAttrConstants.DATASET_METAID);
		if (metaObj != null) {
			String metaId = metaObj.toString();
			try {
				IBusinessEntity entity = MDQueryService.lookupMDQueryService()
						.getBusinessEntityByFullName(metaId);
				String billStateColumn = LfwMdUtil.getMdItfAttr(entity,
						IFLOW_BIZ_ITF, IFlowBizItf.ATTRIBUTE_APPROVESTATUS);
				if (billStateColumn != null) {
					for (int i = 0; i < aggVos.length; i++){
						Integer state = (Integer) aggVos[i].getParentVO()
						.getAttributeValue(billStateColumn);
						if (state == null)
							aggVos[i].getParentVO().setAttributeValue(billStateColumn,
									IBillStatus.FREE);
					}
				}
			} catch (MetaDataException e) {
				Logger.error(e.getMessage(), e);
			}
		}
	}

	protected void doValidate(Dataset masterDs)
			throws LfwValidateException {
		IDataValidator validator = getValidator();
		validator.validate(masterDs, new LfwWidget());
	}

	protected void doValidateBodyNotNull(List<Dataset> detailDs)
			throws LfwValidateException {
		int size = detailDs.size();
		boolean hasBody = false;
		for (int i = 0; i < size; i++) {
			Dataset ds = detailDs.get(i);
			if(ds.getCurrentRowData() == null){
				hasBody = false;
				break;
			}
			if (ds.getCurrentRowCount() > 0) {
				hasBody = true;
				break;
			}
		}
		if (!hasBody) {
			throw new LfwValidateException("�������ݲ���Ϊ��");
		}
	}

	protected IDataValidator getValidator() {
		return new DefaultDataValidator();
	}

	protected ILfwCRUDService<SuperVO, AggregatedValueObject> getCrudService() {
		return CRUDHelper.getCRUDService();
	}

	/**
	 * ����ǰУ��
	 * @param aggVos
	 */
	protected void onBeforeVOSave(AggregatedValueObject[] aggVo) {
	}
	
	/***
	 * ����Ƿ�����ֹ������쳣�׳�
	 * @param aggVos
	 * @return
	 */
	protected boolean checkBeforeVOSave(AggregatedValueObject[] aggVos) throws Exception {
		return true;
	}
	
	protected void onVoSave(AggregatedValueObject[] aggVos) {
		try {
			getCrudService().saveBusinessVOs(aggVos);
		} catch (LfwBusinessException e) {
			Logger.error(e, e);
			throw new LfwRuntimeException(e.getMessage());
		}
	}
}
