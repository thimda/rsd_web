package nc.uap.lfw.ncadapter.crud;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.uap.lfw.core.crud.ILfwCRUDService;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.crud.itf.ILfwCudService;
import nc.uap.lfw.crud.itf.ILfwQueryService;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

/**
 * NC单据持久化的默认实现 
 */
public class NCCrudServiceImpl implements ILfwCRUDService<SuperVO, AggregatedValueObject> {
 
	public AggregatedValueObject saveBusinessVO(AggregatedValueObject obj) throws LfwBusinessException{
		this.saveBusinessVOs(new AggregatedValueObject[]{obj});
		return obj;	
	}

	public AggregatedValueObject[] saveBusinessVOs(AggregatedValueObject[] objs) throws LfwBusinessException{
		return NCLocator.getInstance().lookup(ILfwCudService.class).saveAggVos(objs);
	}
	

	public <M extends SuperVO>M[] queryVOs(M vo, PaginationInfo pg,
			Map<String, Object> extMap) throws LfwBusinessException {
		return (M[]) NCLocator.getInstance().lookup(ILfwQueryService.class).queryVOs(vo, pg, extMap);
	}

	public <M extends SuperVO>M[] queryVOs(String sql, Class<M> clazz,
			PaginationInfo pg, Map<String, Object> extMap) throws LfwBusinessException {
		return (M[]) NCLocator.getInstance().lookup(ILfwQueryService.class).queryVOs(sql, clazz, pg, extMap);
	}
	
	public <M extends SuperVO> M[] queryVOs(String sql, Class<M> clazz,
			PaginationInfo pg, String orderBy, Map<String, Object> extMap)
			throws LfwBusinessException {
		return (M[]) NCLocator.getInstance().lookup(ILfwQueryService.class).queryVOs(sql, clazz, pg, orderBy, extMap);
	}
	
	public void deleteVo(AggregatedValueObject aggvo, boolean trueDel) throws LfwBusinessException {
		NCLocator.getInstance().lookup(ILfwCudService.class).deleteVo(aggvo, trueDel);
	}
	
	public Object query(String sql, ResultSetProcessor processor)
			throws LfwBusinessException {
		return NCLocator.getInstance().lookup(ILfwQueryService.class).queryVOs(sql, processor);
	}

	public int executeUpdate(String sql, SQLParameter parameter)
			throws LfwBusinessException {
		return NCLocator.getInstance().lookup(ILfwCudService.class).executeUpdate(sql, parameter);
	}

	public int executeUpdate(String sql) throws LfwBusinessException {
		return NCLocator.getInstance().lookup(ILfwCudService.class).executeUpdate(sql);
	}

	public <M extends SuperVO>M[] queryVOs(M c, PaginationInfo pg, String wherePart, Map<String, Object> extMap, String orderByPart) throws LfwBusinessException {
		return (M[]) NCLocator.getInstance().lookup(ILfwQueryService.class).queryVOs(c, pg, wherePart, extMap, orderByPart);
	}
	
	public <M extends SuperVO>M[] queryVOs(M c, PaginationInfo pg, String wherePart, Map<String, Object> extMap) throws LfwBusinessException {
		return (M[]) NCLocator.getInstance().lookup(ILfwQueryService.class).queryVOs(c, pg, wherePart, extMap, null);
	}

	public void deleteVo(SuperVO vo) throws LfwBusinessException {
		NCLocator.getInstance().lookup(ILfwCudService.class).deleteVo(vo);
	}
	
	 public void deleteVos(SuperVO[] vos) throws LfwBusinessException{
		 NCLocator.getInstance().lookup(ILfwCudService.class).deleteVos(vos);
	 }

}
