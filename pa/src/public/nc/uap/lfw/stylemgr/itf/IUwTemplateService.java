/**
 * 
 */
package nc.uap.lfw.stylemgr.itf;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.pa.PaBusinessException;
import nc.uap.lfw.stylemgr.vo.UwIncrementVO;
import nc.uap.lfw.stylemgr.vo.UwTemplateVO;
import nc.uap.lfw.stylemgr.vo.UwViewVO;

/**
 * @author wupeng1
 * @version 6.0 2011-9-8
 * @since 1.6
 */
public interface IUwTemplateService {

	/**
	 * 通过pk查找模板对应的vo
	 * @param pk_template
	 * @return
	 * @throws PaBusinessException
	 */
	public UwTemplateVO getTemplateVOByPK(String pk_template)throws PaBusinessException;
	/**
	 * 更新模板vo
	 * @param vo
	 * @throws PaBusinessException
	 */
	public void updateTemplateVO(UwTemplateVO vo) throws PaBusinessException;
	/**
	 * 根据所给条件查询模板vo
	 * @param condition
	 * @return
	 * @throws PaBusinessException
	 */
	public Collection<UwTemplateVO> getTemplateVOByCondition(String condition) throws PaBusinessException;
	/**
	 * 通过winId和busId查询或者创建模板vo
	 * @param winId
	 * @param busId
	 * @return
	 * @throws PaBusinessException
	 */
	public UwTemplateVO getTemplateOrCreate(String appId, String winId, String busId, Map<String, String> paramMap) throws PaBusinessException;
	/**
	 * 通过模板pk查询或者创建模板vo，如果pk为null则创建
	 * @param pk_template
	 * @return
	 * @throws PaBusinessException
	 */
	public UwTemplateVO getTemplateOrCreate(String pk_template) throws PaBusinessException;
	/**
	 * 通过viewId和模板pk查找对应的view
	 * @param viewId
	 * @param pk_template
	 * @return
	 * @throws PaBusinessException
	 */
	public UwViewVO getViewVO(String viewId, String pk_template) throws PaBusinessException;
	/**
	 * 更新view的vo
	 * @param vo
	 * @throws PaBusinessException
	 */
	public void updateViewVO(UwViewVO vo) throws PaBusinessException;
	/**
	 * 通过模板pk和viewId查询或者新建view的vo
	 * @param pk_template
	 * @param viewId
	 * @return
	 * @throws PaBusinessException
	 */
	public UwViewVO getViewOrCreate(String pk_template, String viewId) throws PaBusinessException;
	/**
	 * 通过condition查询个性化页面对应的增量变化
	 * @param condition
	 * @return
	 * @throws PaBusinessException
	 */
	public List<UwIncrementVO> getUwIncrementVOsByCondition(String condition) throws PaBusinessException;
	
	/**
	 * 通过condition查询view的vo集合
	 * @param condition
	 * @return
	 * @throws PaBusinessException
	 */
	public List<UwViewVO> getViewVOsByCondition(String condition)throws PaBusinessException;
	
	/**
	 * 通过业务ID和appId得到template的pk
	 * @param busiId
	 * @param appId
	 * @return
	 * @throws PaBusinessException
	 */
	public String getTemplatePkByBusiidAndAppId(String busiId, String appId) throws PaBusinessException;
	
	/**
	 * 删除个性化模板及其子属性
	 */
	public void deleteTemplateByBusiIdAndAppId(String busiId, String appId) throws PaBusinessException;
}
