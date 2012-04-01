package nc.uap.lfw.core.cmd;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.cmd.base.DatasetCmd;
import nc.uap.lfw.core.common.DatasetConstant;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.data.Row;
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
public class UifDatasetLoadCmd extends DatasetCmd {
	public static final String OPEN_BILL_ID = "openBillId";
	private String datasetId;
	
	public UifDatasetLoadCmd(String dsId) {
		this.datasetId = dsId;
	}
	
	/**
	 * 数据加载默认实现，支持主子查询，单数据查询及分页等。
	 */
	public void execute() {
		LfwWidget widget = getLifeCycleContext().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset(datasetId);
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
			setLoadPk(pk);
		}
		
		//进一步处理查询条件
		String wherePart = postProcessQueryVO(vo, ds);
		//处理order by 条件
		String orderPart = postOrderPart(ds);
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
		ds.setEnabled(false);
		updateButtons();
	}
 

}
