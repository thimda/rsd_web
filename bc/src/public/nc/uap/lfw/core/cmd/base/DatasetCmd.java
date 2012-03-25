package nc.uap.lfw.core.cmd.base;

import java.util.Iterator;
import java.util.Map;

import nc.lfw.core.md.util.LfwMdUtil;
import nc.md.innerservice.MDQueryService;
import nc.md.model.IBusinessEntity;
import nc.md.model.MetaDataException;
import nc.uap.lfw.core.bm.ButtonStateManager;
import nc.uap.lfw.core.common.ExtAttrConstants;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.uif.listener.IBodyInfo;
import nc.uap.lfw.core.uif.listener.MultiBodyInfo;
import nc.uap.lfw.md.LfwTabcodeItf;
import nc.vo.pub.SuperVO;


public abstract class DatasetCmd extends UifCommand {
	
	private String loadPk = null;
	
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
	 * @return
	 */
	protected String postOrderPart(Dataset ds) {
		if(ds.getExtendAttribute("ORDER_PART") != null)
			return ds.getExtendAttribute("ORDER_PART").toString();
		else
			return null;
	}
	
	
	protected void postProcessChildRowSelect(Dataset ds) {
		if(ds.getCurrentRowCount() > 0)
			ds.setRowSelectIndex(0);
	}

	/**
	*	判断此Ds是否实现了nc.uap.lfw.md.LfwTabcodeItf业务接口,如果实现了这个接口，则需要将TabCode作为数据的区分
	*/
	protected void processTabCode(LfwWidget widget, Dataset detailDs, SuperVO vo) {
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

	
	public String getLoadPk() {
		return loadPk;
	}

	public void setLoadPk(String loadPk) {
		this.loadPk = loadPk;
	}
	
	
}
