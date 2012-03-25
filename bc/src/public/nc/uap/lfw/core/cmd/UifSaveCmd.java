package nc.uap.lfw.core.cmd;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.bs.logging.Logger;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.lfw.core.md.util.LfwMdUtil;
import nc.md.innerservice.MDQueryService;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.common.ExtAttrConstants;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.crud.ILfwCRUDService;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.exception.LfwValidateException;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.uap.lfw.core.serializer.impl.Datasets2AggVOSerializer;
import nc.uap.lfw.core.uif.delegator.DefaultDataValidator;
import nc.uap.lfw.core.uif.delegator.IDataValidator;
import nc.uap.lfw.core.vo.LfwExAggVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.trade.pub.IExAggVO;
/**
 * 通用保存命令
 * 
 */
public class UifSaveCmd extends UifCommand {
	private static final String IFLOW_BIZ_ITF = "nc.itf.uap.pf.metadata.IFlowBizItf";
	private String masterDsId;
	private String[] detailDsIds;
	private String aggVoClazz;
	private boolean bodyNotNull;
	private List<String> notNullBodyList;
	public List<String> getNotNullBodyList() {
		return notNullBodyList;
	}
	public void setNotNullBodyList(List<String> notNullBodyList) {
		this.notNullBodyList = notNullBodyList;
	}
	public UifSaveCmd(String masterDsId, String[] detailDsIds, String aggVoClazz, boolean bodyNotNull) {
		this.masterDsId = masterDsId;
		this.detailDsIds = detailDsIds;
		this.aggVoClazz = aggVoClazz;
		this.bodyNotNull = bodyNotNull;
	}
	public UifSaveCmd(String masterDsId2, String[] detailDsIds2, String aggVoClazz2) {
		this(masterDsId2, detailDsIds2, aggVoClazz2, true);
	}
	public void execute() {
		ViewContext viewCtx = getLifeCycleContext().getViewContext();
		LfwWidget widget = viewCtx.getView();
		if (widget == null)
			throw new LfwRuntimeException("片段为空!");
		if (this.masterDsId == null)
			throw new LfwRuntimeException("未指定主数据集id!");
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if (masterDs == null)
			throw new LfwRuntimeException("数据集为空,数据集id=" + masterDsId + "!");
		if (this.aggVoClazz == null)
			aggVoClazz = LfwExAggVO.class.getName();
		List<String> idList = new ArrayList<String>();
		idList.add(masterDsId);
		if (detailDsIds != null && detailDsIds.length > 0)
			idList.addAll(Arrays.asList(detailDsIds));
		ArrayList<Dataset> detailDs = new ArrayList<Dataset>();
		if (detailDsIds != null && detailDsIds.length > 0) {
			for (int i = 0; i < detailDsIds.length; i++) {
				Dataset ds = widget.getViewModels().getDataset(detailDsIds[i]);
				if (ds != null)
					detailDs.add(ds);
			}
		}
		doValidate(masterDs, detailDs);
		Datasets2AggVOSerializer ser = new Datasets2AggVOSerializer();
		Dataset[] detailDss = detailDs.toArray(new Dataset[0]);
		AggregatedValueObject aggVo = ser.serialize(masterDs, detailDss, aggVoClazz);
		if(aggVo == null)
			return;
		fillCachedDeletedVO(aggVo, detailDss);
		try {
			setBillStatus(masterDs, aggVo);
			// 保存前检查唯一性
			onBeforeVOSave(aggVo);
			// 检查是否有阻止保存的异常
			if (!checkBeforeVOSave(aggVo))
				return;
			onVoSave(aggVo);
		} catch (Exception e) {
			dealWithException(e);
		}
		// 保存后执行的逻辑
		onAfterVOSave(widget, masterDs, ser, detailDss, aggVo);
		onAfterSave(masterDs, detailDss);
	}
	/**
	 * 保存完成之后调用逻辑
	 * 
	 * @param masterDs
	 * @param detailDss
	 */
	protected void onAfterSave(Dataset masterDs, Dataset[] detailDss) {
		masterDs.setEnabled(false);
		for (int i = 0; i < detailDss.length; i++) {
			detailDss[i].setEnabled(false);
		}
		updateButtons();
	}
	/**
	 * 异常的处理类
	 * 
	 * @param e
	 */
	protected void dealWithException(Exception e) {
		Logger.error(e, e);
		throw new LfwRuntimeException(e.getMessage());
	}
	/**
	 * 保存后操作后, 重新设置页面数据
	 * 
	 * @param widget
	 * @param masterDs
	 * @param ser
	 * @param detailDss
	 * @param aggVo
	 */
	protected void onAfterVOSave(LfwWidget widget, Dataset masterDs, Datasets2AggVOSerializer ser, Dataset[] detailDss, AggregatedValueObject aggVo) {
		if (detailDss != null && detailDss.length > 0) {
			DatasetRelation dr = widget.getViewModels().getDsrelations().getDsRelation(masterDsId, detailDsIds[0]);
			String newKeyValue = (String) aggVo.getParentVO().getAttributeValue(dr.getMasterKeyField());
			for (int i = 0; i < detailDss.length; i++) {
				if (newKeyValue != null && !newKeyValue.equals(detailDss[i].getCurrentKey()))
					detailDss[i].replaceKeyValue(detailDss[i].getCurrentKey(), newKeyValue);
			}
		}
		ser.update(aggVo, masterDs, detailDss);
	}
	// 如果有记录的删除的子表行，则将子表的vo标志为delete状态
	protected void fillCachedDeletedVO(AggregatedValueObject aggVo, Dataset[] detailDss) {
		SuperVO masterVO = (SuperVO) aggVo.getParentVO();
		if (LfwCacheManager.getSessionCache() == null)
			return;
		if (masterVO.getPrimaryKey() == null)
			return;
		// 删除放入缓存中的数据
		List<SuperVO> delBodyVoList = (List<SuperVO>) LfwCacheManager.getSessionCache().get(masterVO.getPrimaryKey());
		if (delBodyVoList == null || delBodyVoList.size() == 0)
			return;
		for (Dataset dataset : detailDss) {
			String delRowForeignKey = masterVO.getPrimaryKey() + "_" + dataset.getId();
			// 子ds中删除的行
			List<Row> listDelRow = (List<Row>) LfwCacheManager.getSessionCache().get(delRowForeignKey);
			if (listDelRow == null || listDelRow.size() == 0)
				continue;
			Dataset2SuperVOSerializer ser = new Dataset2SuperVOSerializer();
			// 将删除的数据序列化
			CircularlyAccessibleValueObject[] superVOs = ser.serialize(dataset, listDelRow.toArray(new Row[0]));
			if (aggVo instanceof IExAggVO) {
				String tableId = null;
				Object tabcode = dataset.getExtendAttributeValue(ExtAttrConstants.TAB_CODE);
				if (tabcode != null)
					tableId = tabcode.toString();
				else {
					Object parentField = dataset.getExtendAttributeValue(ExtAttrConstants.PARENT_FIELD);
					if (parentField != null)
						tableId = parentField.toString();
				}
				if (tableId == null)
					tableId = dataset.getId();
				CircularlyAccessibleValueObject[] vos = ((IExAggVO) aggVo).getTableVO(tableId);
				List<CircularlyAccessibleValueObject> vosList = new ArrayList<CircularlyAccessibleValueObject>();
				for (CircularlyAccessibleValueObject vo : vos) {
					vosList.add(vo);
				}
				vosList.addAll(Arrays.asList(superVOs));
				// 重新设置子表vo的数据
				((IExAggVO) aggVo).setTableVO(tableId, (CircularlyAccessibleValueObject[]) vosList.toArray(new SuperVO[0]));
			} else {
				CircularlyAccessibleValueObject[] vos = aggVo.getChildrenVO();
				List<CircularlyAccessibleValueObject> vosList = new ArrayList<CircularlyAccessibleValueObject>();
				for (int i = 0; i < vos.length; i++) {
					vosList.add(vos[i]);
				}
				vosList.addAll(Arrays.asList(superVOs));
				aggVo.setChildrenVO((CircularlyAccessibleValueObject[]) vosList.toArray(new CircularlyAccessibleValueObject[0]));
			}
			LfwCacheManager.getSessionCache().remove(delRowForeignKey);
		}
		CircularlyAccessibleValueObject[] bodyVOs = null;
		if (aggVo instanceof IExAggVO)
			bodyVOs = ((IExAggVO) aggVo).getAllChildrenVO();
		else
			bodyVOs = aggVo.getChildrenVO();
		for (int i = 0; i < bodyVOs.length; i++) {
			SuperVO bodyVo = (SuperVO) bodyVOs[i];
			for (int j = 0; j < delBodyVoList.size(); j++) {
				SuperVO bodyVoChild = (SuperVO) delBodyVoList.get(j);
				if (bodyVo.getPrimaryKey() != null && bodyVoChild.getPrimaryKey() != null && bodyVo.getPrimaryKey().equals(bodyVoChild.getPrimaryKey())) {
					bodyVo.setStatus(VOStatus.DELETED);
					break;
				}
			}
		}
		aggVo.setChildrenVO(bodyVOs);
		LfwCacheManager.getSessionCache().remove(masterVO.getPrimaryKey());
	}
	protected void setBillStatus(Dataset masterDs, AggregatedValueObject aggVo) {
		Object metaObj = masterDs.getExtendAttributeValue(ExtAttrConstants.DATASET_METAID);
		if (metaObj != null) {
			String metaId = metaObj.toString();
			try {
				IBusinessEntity entity = MDQueryService.lookupMDQueryService().getBusinessEntityByFullName(metaId);
				String billStateColumn = LfwMdUtil.getMdItfAttr(entity, IFLOW_BIZ_ITF, IFlowBizItf.ATTRIBUTE_APPROVESTATUS);
				if (billStateColumn != null) {
					Integer state = (Integer) aggVo.getParentVO().getAttributeValue(billStateColumn);
					if (state == null)
						aggVo.getParentVO().setAttributeValue(billStateColumn, IBillStatus.FREE);
				}
			} catch (MetaDataException e) {
				Logger.error(e.getMessage(), e);
			}
		}
	}
	protected void doValidate(Dataset masterDs, List<Dataset> detailDs) throws LfwValidateException {
		IDataValidator validator = getValidator();
		validator.validate(masterDs, new LfwWidget());
		if (detailDs != null) {
			int size = detailDs.size();
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					Dataset ds = detailDs.get(i);
					validator.validate(ds, new LfwWidget());
					if (notNullBodyList != null && notNullBodyList.contains(ds.getId())) {
						doSingleValidateBodyNotNull(ds);
					}
				}
				if (bodyNotNull) {
					doValidateBodyNotNull(detailDs);
				}
			}
		}
	}
	/**
	 * 检查单个dataset是否存在数据
	 * 
	 * @param detailDs
	 * @throws LfwValidateException
	 */
	protected void doSingleValidateBodyNotNull(Dataset detailDs) throws LfwValidateException {
		boolean hasBody = false;
		if (detailDs.getCurrentRowData() == null) {
			hasBody = false;
		}
		if (detailDs.getCurrentRowCount() > 0) {
			hasBody = true;
		}
		if (!hasBody) {
			throw new LfwValidateException("表体数据不能为空");
		}
	}
	protected void doValidateBodyNotNull(List<Dataset> detailDs) throws LfwValidateException {
		int size = detailDs.size();
		for (int i = 0; i < size; i++) {
			Dataset ds = detailDs.get(i);
			//此表体数据不能为空
			if(ds.isNotNullBody()){
				if (ds.getCurrentRowData() == null) {
					throw new LfwValidateException(ds.getCaption() + "的表体数据不能为空");
				}
			}
		}
	}
	protected IDataValidator getValidator() {
		return new DefaultDataValidator();
	}
	protected ILfwCRUDService<SuperVO, AggregatedValueObject> getCrudService() {
		return CRUDHelper.getCRUDService();
	}
	/**
	 * 保存前校验
	 * 
	 * @param aggVo
	 */
	protected void onBeforeVOSave(AggregatedValueObject aggVo) {}
	/***
	 * 检查是否有阻止保存的异常抛出
	 * 
	 * @param aggVo
	 * @return
	 */
	protected boolean checkBeforeVOSave(AggregatedValueObject aggVo) throws Exception {
		return true;
	}
	protected void onVoSave(AggregatedValueObject aggVo) {
		try {
			getCrudService().saveBusinessVO(aggVo);
		} catch (LfwBusinessException e) {
			Logger.error(e, e);
			throw new LfwRuntimeException(e.getMessage());
		}
	}
}
