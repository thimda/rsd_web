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
	 * ͨ��pk����ģ���Ӧ��vo
	 * @param pk_template
	 * @return
	 * @throws PaBusinessException
	 */
	public UwTemplateVO getTemplateVOByPK(String pk_template)throws PaBusinessException;
	/**
	 * ����ģ��vo
	 * @param vo
	 * @throws PaBusinessException
	 */
	public void updateTemplateVO(UwTemplateVO vo) throws PaBusinessException;
	/**
	 * ��������������ѯģ��vo
	 * @param condition
	 * @return
	 * @throws PaBusinessException
	 */
	public Collection<UwTemplateVO> getTemplateVOByCondition(String condition) throws PaBusinessException;
	/**
	 * ͨ��winId��busId��ѯ���ߴ���ģ��vo
	 * @param winId
	 * @param busId
	 * @return
	 * @throws PaBusinessException
	 */
	public UwTemplateVO getTemplateOrCreate(String appId, String winId, String busId, Map<String, String> paramMap) throws PaBusinessException;
	/**
	 * ͨ��ģ��pk��ѯ���ߴ���ģ��vo�����pkΪnull�򴴽�
	 * @param pk_template
	 * @return
	 * @throws PaBusinessException
	 */
	public UwTemplateVO getTemplateOrCreate(String pk_template) throws PaBusinessException;
	/**
	 * ͨ��viewId��ģ��pk���Ҷ�Ӧ��view
	 * @param viewId
	 * @param pk_template
	 * @return
	 * @throws PaBusinessException
	 */
	public UwViewVO getViewVO(String viewId, String pk_template) throws PaBusinessException;
	/**
	 * ����view��vo
	 * @param vo
	 * @throws PaBusinessException
	 */
	public void updateViewVO(UwViewVO vo) throws PaBusinessException;
	/**
	 * ͨ��ģ��pk��viewId��ѯ�����½�view��vo
	 * @param pk_template
	 * @param viewId
	 * @return
	 * @throws PaBusinessException
	 */
	public UwViewVO getViewOrCreate(String pk_template, String viewId) throws PaBusinessException;
	/**
	 * ͨ��condition��ѯ���Ի�ҳ���Ӧ�������仯
	 * @param condition
	 * @return
	 * @throws PaBusinessException
	 */
	public List<UwIncrementVO> getUwIncrementVOsByCondition(String condition) throws PaBusinessException;
	
	/**
	 * ͨ��condition��ѯview��vo����
	 * @param condition
	 * @return
	 * @throws PaBusinessException
	 */
	public List<UwViewVO> getViewVOsByCondition(String condition)throws PaBusinessException;
	
	/**
	 * ͨ��ҵ��ID��appId�õ�template��pk
	 * @param busiId
	 * @param appId
	 * @return
	 * @throws PaBusinessException
	 */
	public String getTemplatePkByBusiidAndAppId(String busiId, String appId) throws PaBusinessException;
	
	/**
	 * ɾ�����Ի�ģ�弰��������
	 */
	public void deleteTemplateByBusiIdAndAppId(String busiId, String appId) throws PaBusinessException;
}
