package nc.uap.lfw.core.event.deft;

import java.util.Iterator;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.lfw.core.md.util.LfwMdUtil;
import nc.md.innerservice.MDQueryService;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.uap.lfw.core.common.DatasetConstant;
import nc.uap.lfw.core.common.ExtAttrConstants;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowSet;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.ctx.WidgetContext;
import nc.uap.lfw.core.event.listener.DatasetServerListener;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import nc.uap.lfw.core.uif.listener.IBodyInfo;
import nc.uap.lfw.core.uif.listener.MultiBodyInfo;
import nc.uap.lfw.md.LfwTabcodeItf;
import nc.uap.lfw.util.LfwClassUtil;
import nc.vo.pub.SuperVO;

/**
 * 提供的数据集默认操作
 *
 */
public class DefaultDatasetServerListener extends DatasetServerListener {

	private static final String OPEN_BILL_ID = "openBillId";
	private String loadPk = null;
	public DefaultDatasetServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	} 

	/**
	 * 数据加载默认实现，支持主子查询，单数据查询及分页等。
	 */
	@Override
	public void onDataLoad(DataLoadEvent se) {
		Dataset ds = se.getSource();
		String clazz = ds.getVoMeta();
		if(clazz == null)
			return;
		SuperVO vo = (SuperVO) LfwClassUtil.newInstance(clazz);
		//根据参数设置VO条件
		String keys = ds.getReqParameters().getParameterValue(DatasetConstant.QUERY_PARAM_KEYS);
		if(keys != null && !keys.equals("")){
			String values = ds.getReqParameters().getParameterValue(DatasetConstant.QUERY_PARAM_VALUES);
			String[] keyStrs = keys.split(",");
			String[] valueStrs = values.split(",");
			for (int i = 0; i < keyStrs.length; i++) {
				vo.setAttributeValue(keyStrs[i], valueStrs[i]);
			}
		}
		
		//如果前台设置进主键参数，则表示加载此单据，需设置主键条件（前台传回来的）
		String pk = ds.getReqParameters().getParameterValue(OPEN_BILL_ID);
		if(pk != null){
			vo.setPrimaryKey(pk);
			loadPk = pk;
		}
		
		//进一步处理查询条件
		String wherePart = postProcessQueryVO(vo, ds);
		//处理order by 条件
		postOrderPart(ds);
		String orderPart = (String) ds.getExtendAttributeValue("ORDER_PART");
		try {
			PaginationInfo pinfo = ds.getCurrentRowSet().getPaginationInfo();
			SuperVO[] vos = null;
			if(orderPart == null || orderPart.equals(""))
				vos = queryVOs(pinfo, vo, wherePart);
			else
				vos = queryVOs(pinfo, vo, wherePart, orderPart);
			new SuperVO2DatasetSerializer().serialize(vos, ds, Row.STATE_NORMAL);
			postProcessRowSelect(ds);
		} 
		catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwRuntimeException("查询对象出错," + e.getMessage() + ",ds id:" + ds.getId(), "查询过程出现错误");
		}
		//setPageState(ds);
	}
 
	
	/**
	 * 真正执行查询，可在必要时重写此方法
	 * @param pinfo
	 * @param vo
	 * @param wherePart
	 * @return
	 * @throws LfwBusinessException
	 */
	protected SuperVO[] queryVOs(PaginationInfo pinfo, SuperVO vo, String wherePart, String orderPart) throws LfwBusinessException {
		SuperVO[] vos =  CRUDHelper.getCRUDService().queryVOs(vo, pinfo, wherePart, null, orderPart);
		return vos;
	}
	
	/**
	 * 真正执行查询，可在必要时重写此方法
	 * @param pinfo
	 * @param vo
	 * @param wherePart
	 * @return
	 * @throws LfwBusinessException
	 */
	protected SuperVO[] queryVOs(PaginationInfo pinfo, SuperVO vo, String wherePart) throws LfwBusinessException {
		return queryVOs(pinfo, vo, wherePart, null);
	}

	/**
	 * 设置查询后行选中，特殊界面不能设置默认选中需覆盖,默认不选中
	 * @param ds
	 */
	protected void postProcessRowSelect(Dataset ds) {
		if(loadPk != null && ds.getCurrentRowCount() > 0){
			ds.setRowSelectIndex(0);
		}
	}

	/**
	 * 进一步设置查询条件，可根据需要重写此方法。
	 * 支持在VO中设置条件及通过返回值设置条件双重方式。如 vo.setAttribute("name", "test"),或者返回 name='test'的条件子句。
	 * 本方法会同时取2种设置方式的合集
	 * @param vo
	 * @param ds
	 * @return
	 */
	protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
		return ds.getLastCondition();
	}
	
	/**
	 * 添加orderby
	 * 例如：order by ts 
	 * @return
	 */
	protected void postOrderPart(Dataset ds) {
		ds.setExtendAttribute("ORDER_PART", null);
	}


	/**
	 * 行选中方法，处理主子关系等通用逻辑
	 */
	@Override
	public void onAfterRowSelect(DatasetEvent e) {
		Dataset masterDs = (Dataset) e.getSource();
		//获取触发行
		Row masterSelecteRow = masterDs.getSelectedRow();
		if(masterSelecteRow == null)
			return;
		WidgetContext widgetCtx = getCurrentContext();
		LfwWidget widget = widgetCtx.getWidget();
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
					/**
					 * add by licza
					 * 子数据集控制页面状态
					 */
					setPageState(detailDs);
				} 
				catch (LfwBusinessException exp) {
					Logger.error(exp.getMessage(), exp);
					throw new LfwRuntimeException("查询对象出错," + exp.getMessage() + ",ds id:" + detailDs.getId(), "查询过程出现错误");


				}
			}
		}
		setPageState(masterDs);
	}

	protected void postProcessChildRowSelect(Dataset ds) {
		if(ds.getCurrentRowCount() > 0)
			ds.setRowSelectIndex(0);
	}

	/**
	*	判断此Ds是否实现了nc.uap.lfw.md.LfwTabcodeItf业务接口,如果实现了这个接口，则需要将TabCode作为数据的区分
	*/
	private void processTabCode(LfwWidget widget, Dataset detailDs, SuperVO vo) {
		Object datasetMetaId = detailDs.getExtendAttributeValue(ExtAttrConstants.DATASET_METAID);
		if(datasetMetaId != null){
			String metaId = datasetMetaId.toString();
			try {
				IBusinessEntity entity = MDQueryService.lookupMDQueryService().getBusinessEntityByFullName(metaId);
				boolean tabCodeItf = false;
				if(entity != null)
					tabCodeItf = entity.isImplementBizInterface(LfwTabcodeItf.class.getName());
				if(tabCodeItf){
					String tabcode = LfwMdUtil.getMdItfAttr(entity, LfwTabcodeItf.class.getName(), "tabcode");
					if(tabcode == null || tabcode.equals(""))
						throw new LfwRuntimeException("Dataset:" + detailDs.getId() + "实现了nc.uap.lfw.md.LfwTabcodeItf业务接口，但没有设置属性对照");
					IBodyInfo bodyInfo = (IBodyInfo) widget.getExtendAttributeValue(LfwWidget.BODYINFO);
					if(bodyInfo != null){
						if(bodyInfo instanceof MultiBodyInfo){
							MultiBodyInfo multiBodyInfo = (MultiBodyInfo) bodyInfo;
							Map<String, String> tabDsMap = multiBodyInfo.getItemDsMap();
							for (Iterator<String> itwd = tabDsMap.keySet().iterator(); itwd.hasNext();) {
								String tabId = (String) itwd.next();
								if(tabDsMap.get(tabId).equals(detailDs.getId())){
									vo.setAttributeValue(tabcode, tabId);
									break;
								}
							}
						}
					}
				}
			} 
			catch (MetaDataException e1) {
				LfwLogger.error(e1.getMessage(), e1);
			}
		}
	}

	
	/**
	 * 
	 * @param pinfo
	 * @param vo
	 * @param wherePart 
	 * @param isNewMaster 主表是否新增行
	 * @return
	 * @throws LfwBusinessException
	 */
	protected SuperVO[] queryChildVOs(PaginationInfo pinfo, SuperVO vo, String wherePart, boolean isNewMaster) throws LfwBusinessException {
		if(!isNewMaster){
			SuperVO[] vos = CRUDHelper.getCRUDService().queryVOs(vo, pinfo, wherePart, null, null);
			return vos;
		}
		return null;
	}
	
	
	protected SuperVO[] queryChildVOs(PaginationInfo pinfo, SuperVO vo, String wherePart, boolean isNewMaster, String orderPart) throws LfwBusinessException {
		if(!isNewMaster){
			SuperVO[] vos = CRUDHelper.getCRUDService().queryVOs(vo, pinfo, wherePart, null, orderPart);
			return vos;
		}
		return null;
	}
	
	protected String postProcessRowSelectVO(SuperVO vo, Dataset ds) {
		return ds.getLastCondition();
	}

	protected void setPageState(Dataset ds) {
		Row[] rows = ds.getSelectedRows();
//		if(ds.isControloperatorStatus()){
//			PageMeta pm = getGlobalContext().getPageMeta();
//			if(IOperatorState.ADD.equals(pm.getOperatorState()) || IOperatorState.EDIT.equals(pm.getOperatorState()))
//				return;
//			if(rows == null || rows.length == 0)
//				pm.setOperatorState(IOperatorState.INIT);
//			else if(rows.length == 1)
//				pm.setOperatorState(IOperatorState.SINGLESEL);
//			else
//				pm.setOperatorState(IOperatorState.MULTISEL);
//		}
		
//		if(ds.isControlwidgetopeStatus()){
//			LfwWidget widget = getCurrentContext().getWidget();
//			if(IOperatorState.ADD.equals(widget.getOperatorState()) || IOperatorState.EDIT.equals(widget.getOperatorState()))
//				return;
//			if(rows == null || rows.length == 0)
//				widget.setOperatorState(IOperatorState.INIT);
//			else if(rows.length == 1)
//				widget.setOperatorState(IOperatorState.SINGLESEL);
//			else
//				widget.setOperatorState(IOperatorState.MULTISEL);
//		}
		//对操作状态进行控制
		Object datasetMetaId =	ds.getExtendAttributeValue(ExtAttrConstants.DATASET_METAID);
		if(datasetMetaId != null){
			String metaId = datasetMetaId.toString();
		    try {
			    IBusinessEntity businessEntity = MDQueryService.lookupMDQueryService().getBusinessEntityByFullName(metaId);
			    if(!businessEntity.isImplementBizInterface(IFlowBizItf.class.getName()))
			    	return;
			    String billState = LfwMdUtil.getMdItfAttr(businessEntity, IFlowBizItf.class.getName(), IFlowBizItf.ATTRIBUTE_APPROVESTATUS);
			    if(ds.nameToIndex(billState) == -1)
			    	return;
			    //得到当前的单据状态
			    Integer billStateValue = (Integer)rows[0].getValue(ds.nameToIndex(billState));
			    PageMeta pm = getGlobalContext().getPageMeta();
//			    pm.setBusiState(billStateValue.toString());
			   } catch (MetaDataException e) {
			    // TODO Auto-generated catch block
			    LfwLogger.error(e.getMessage(), e);
			   }
		}
	}
}
