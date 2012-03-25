/**
 * 
 */
package nc.uap.lfw.pa.itf;

import java.util.Collection;

import nc.uap.lfw.pa.PaBusinessException;
import nc.vo.pub.SuperVO;

/**
 * @author wupeng1
 * @version 6.0 2011-8-16
 * @since 1.6
 */
public interface IPaPersistService {

//	public void persitButtonComp(PaButtonCompVO btnVO) throws PaBusinessException;
//	public boolean persitLabelComp(PaLabelCompVO lbVO);
//	
	public SuperVO getCompVOByTypeAndID(String type, String id, String widgetid, String pk_template) throws PaBusinessException;
	public SuperVO getCompVOByClause(Class<?> clazz, String clause) throws PaBusinessException;
	public void persitCompVO(SuperVO vo) throws PaBusinessException;
	public void updateCompVO(SuperVO vo) throws PaBusinessException;
	
	public Collection<SuperVO> getCompVOsByClause(Class<?> clazz, String clause) throws PaBusinessException;
	
	public void deleteVOByClause(Class<?> clazz, String clause) throws PaBusinessException;
	
//	public void savePaForDB();
}
