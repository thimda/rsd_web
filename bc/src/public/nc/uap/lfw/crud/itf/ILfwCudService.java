package nc.uap.lfw.crud.itf;

import nc.jdbc.framework.SQLParameter;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

public interface ILfwCudService {
	public AggregatedValueObject[] saveAggVos(AggregatedValueObject[] objs) throws LfwBusinessException;

	public void deleteVo(AggregatedValueObject aggvo, boolean trueDel) throws LfwBusinessException;
	
	public void deleteVo(SuperVO vo) throws LfwBusinessException;
	
	
	public void deleteVos(SuperVO[] vos) throws LfwBusinessException;

	public int executeUpdate(String sql, SQLParameter parameter) throws LfwBusinessException;

	public int executeUpdate(String sql) throws LfwBusinessException;
}
