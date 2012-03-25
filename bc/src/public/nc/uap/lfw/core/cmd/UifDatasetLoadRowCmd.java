package nc.uap.lfw.core.cmd;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import nc.uap.lfw.util.LfwClassUtil;
import nc.vo.pub.SuperVO;

/**
 * 数据集单条数据加载命令
 *
 */
public class UifDatasetLoadRowCmd extends UifCommand {

	private String datasetId;
	private String billId;
	
	/**
	 * 
	 * @param dsId 数据集ID
	 * @param billId  数据VO 的ID
	 */
	public UifDatasetLoadRowCmd(String dsId, String billId) {
		this.datasetId = dsId;
		this.billId = billId;
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

		
		//如果前台设置进主键参数，则表示加载此单据，需设置主键条件（前台传回来的）
		String pk = billId;
		if(pk != null){
			vo.setPrimaryKey(pk);
		}
		try {
			SuperVO result = queryVO(vo);
			if(result != null){
				new SuperVO2DatasetSerializer().serialize(new SuperVO[]{result}, ds, Row.STATE_NORMAL);
			}
			postProcessRowSelect(ds);
		} 
		catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwRuntimeException("查询对象出错," + e.getMessage() + ",ds id:" + ds.getId(), "查询过程出现错误");
		}
	}
	/**
	 * 真正执行查询，可在必要时重写此方法
	 * @param pinfo
	 * @param vo
	 * @param wherePart
	 * @return
	 * @throws LfwBusinessException
	 */
	protected SuperVO queryVO(SuperVO vo) throws LfwBusinessException {
		SuperVO[] vos = CRUDHelper.getCRUDService().queryVOs(vo, null, null);
		return (vos != null && vos.length == 1) ? vos[0] : null;
	}


	/**
	 * 设置查询后行选中，特殊界面不能设置默认选中需覆盖,默认不选中
	 * @param ds
	 */
	protected void postProcessRowSelect(Dataset ds) {
		if(ds.getCurrentRowCount() > 0){
			ds.setRowSelectIndex(0);
		}
		ds.setEnabled(false);
	}
}
