package nc.uap.lfw.core.cmd;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.crud.CRUDHelper;
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
public class UifDatasetWhereCmd extends UifCommand {

	private String datasetId;
	private FromWhereSQL whereSql;
	public UifDatasetWhereCmd(String dsId, FromWhereSQL whereSql) {
		this.datasetId = dsId;
		this.whereSql = whereSql;
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
		
		//处理order by 条件
		String orderPart = postOrderPart(vo, ds, whereSql);
		try {
			PaginationInfo pinfo = ds.getCurrentRowSet().getPaginationInfo();
			SuperVO[] vos = null;
			String sql = getQuerySql(vo, ds, whereSql);
			
			String wherePart = postWherePart(vo, ds, whereSql);
			if((sql != null && !sql.equals(""))){
				 if(wherePart != null && !wherePart.equals("")){
					// if(sql.indexOf(" where ") != -1)
						 sql += " and (" + wherePart + ")";
//					 else
//						 sql += " where (" + wherePart + ")";
				 }
			}
			if(orderPart == null || orderPart.equals(""))
				vos = queryVOs(pinfo, vo, sql);
			else
				vos = queryVOs(pinfo, vo, sql, orderPart);
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

	protected SuperVO[] queryVOs(PaginationInfo pinfo, SuperVO vo, String sql) throws LfwBusinessException{
		return CRUDHelper.getCRUDService().queryVOs(vo, pinfo, sql, null, null);
	}

	protected SuperVO[] queryVOs(PaginationInfo pinfo, SuperVO vo, String sql,
			String orderPart) throws LfwBusinessException{
		return CRUDHelper.getCRUDService().queryVOs(vo, pinfo, sql, null, orderPart);
	}

	protected String postWherePart(SuperVO vo, Dataset ds, FromWhereSQL whereSql) {
		return null;
	}

	protected String getQuerySql(SuperVO vo, Dataset ds, FromWhereSQL whereSql) {
		return whereSql.getWhere();
	}

	protected void postProcessRowSelect(Dataset ds) {
		
	}

	protected String postOrderPart(SuperVO vo, Dataset ds, FromWhereSQL whereSql) {
		return null;
	}

 

}
