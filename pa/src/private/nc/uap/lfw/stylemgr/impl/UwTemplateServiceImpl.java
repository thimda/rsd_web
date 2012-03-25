/**
 * 
 */
package nc.uap.lfw.stylemgr.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.pa.PaBusinessException;
import nc.uap.lfw.stylemgr.itf.IUwTemplateService;
import nc.uap.lfw.stylemgr.vo.UwIncrementVO;
import nc.uap.lfw.stylemgr.vo.UwTemplateVO;
import nc.uap.lfw.stylemgr.vo.UwViewVO;

/**
 * @author wupeng1
 * @version 6.0 2011-9-8
 * @since 1.6
 */
public class UwTemplateServiceImpl implements IUwTemplateService{

	@Override
	public void updateTemplateVO(UwTemplateVO vo) throws PaBusinessException {
		BaseDAO dao = new BaseDAO();
		try {
			dao.updateVO(vo);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e);
		}
	}

	@Override
	public UwTemplateVO getTemplateVOByPK(String pk_template)
			throws PaBusinessException {
		BaseDAO dao = new BaseDAO();
		try {
			return (UwTemplateVO) dao.retrieveByPK(UwTemplateVO.class, pk_template);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e);
		}
	}

	@Override
	public Collection<UwTemplateVO> getTemplateVOByCondition(String conditon)
			throws PaBusinessException {
		BaseDAO dao = new BaseDAO();
		
		try {
			return dao.retrieveByClause(UwTemplateVO.class, conditon);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e);
		}
	}
	@Override
	public UwTemplateVO getTemplateOrCreate(String appId, String winId, String busId, Map<String, String> paramMap)
			throws PaBusinessException {
		BaseDAO dao = new BaseDAO();
		
		String cond = null;
		
		String cond0 = appId == null ? "appid IS NULL " : " appid = '" + appId + " '";
		String cond1 = winId == null ? " and windowid IS NULL " : " and windowid = '" + winId + "' ";
		String cond2 = busId == null ? " and busiid IS NULL " : " and busiid = '" + busId + "' " ;
		
		String pk_prodef = null;
		String portId = null;
		String ext1 = null;
		String ext2 = null;
		String ext3 = null;
		String ext4 = null;
		String ext5 = null;
		
		if(paramMap == null)
			cond = cond0 + cond1 + cond2;
		
		else{
			pk_prodef = paramMap.get("pk_prodef");
			String cond3 = pk_prodef == null ? " and pk_prodef IS NULL " : " and pk_prodef = '" + pk_prodef + "' ";
			
			portId = paramMap.get("port_id");
			String cond4 = portId == null ? " and port_id IS NULL " : " and port_id = '" + portId + "' ";
			
			ext1 = paramMap.get("ext1");
			String cond5 = ext1 == null ? " and ext1 IS NULL " : " and ext1 = '" + ext1 + "' ";
			ext2 = paramMap.get("ext2");
			String cond6 = ext2 == null ? " and ext2 IS NULL " : " and ext2 = '" + ext2 + "' ";;
			ext3 = paramMap.get("ext3");
			String cond7 = ext3 == null ? " and ext3 IS NULL " : " and ext3 = '" + ext3 + "' ";;
			ext4 = paramMap.get("ext4");
			String cond8 = ext4 == null ? " and ext4 IS NULL " : " and ext4 = '" + ext4 + "' ";;
			ext5 = paramMap.get("ext5");
			String cond9 = ext5 == null ? " and ext5 IS NULL " : " and ext5 = '" + ext5 + "' ";;
		
			cond = cond0 + cond1 + cond2 + cond3 + cond4 + cond5 + cond6 + cond7 + cond8 + cond9;
		}
				
		UwTemplateVO vo = null;
		try {
			Collection<UwTemplateVO> templateVOs = dao.retrieveByClause(UwTemplateVO.class, cond);
		
			if(templateVOs != null){
				Iterator<UwTemplateVO> it = templateVOs.iterator();
				while(it.hasNext()){
					vo = it.next();
					if(vo != null)
						break;
				}
			}
			if(vo == null){
				
				vo = new UwTemplateVO();
				vo.setWindowid(winId);
				vo.setBusiid(busId);
				vo.setAppid(appId);
				
				if(paramMap != null && paramMap.size() > 0){
					if(pk_prodef != null)
						vo.setPk_prodef(pk_prodef);
					if(portId != null)
						vo.setPort_id(portId);
					if(ext1 != null)
						vo.setExt1(ext1);
					if(ext2 != null)
						vo.setExt2(ext2);
					if(ext3 != null)
						vo.setExt3(ext3);
					if(ext4 != null)
						vo.setExt4(ext4);
					if(ext5 != null)
						vo.setExt5(ext5);
				}
					
				dao.insertVO(vo);
			}
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e);
		}
		return vo;
	}

	@Override
	public UwViewVO getViewVO(String viewId, String pk_template)
			throws PaBusinessException {
		BaseDAO dao = new BaseDAO();
		UwViewVO vo = null;
		try {
			String condition = "pk_template = '" + pk_template + "' and viewid = '" + viewId + "'";
			Collection<UwViewVO> viewVOs =dao.retrieveByClause(UwViewVO.class, condition);
			if(viewVOs != null){
				Iterator<UwViewVO> it = viewVOs.iterator();
				while(it.hasNext()){
					vo = it.next();
					if(vo != null)
						break;
				}
			}
			
			if(vo != null)
				return vo;
			
			return null;
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e);
		}
	}

	@Override
	public void updateViewVO(UwViewVO vo) throws PaBusinessException {
		BaseDAO dao = new BaseDAO();
		try {
			dao.updateVO(vo);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e);
		}
		
	}

	@Override
	public UwViewVO getViewOrCreate(String pk_template, String viewId)
			throws PaBusinessException {
		BaseDAO dao = new BaseDAO();
		String condition = "pk_template = '" + pk_template + "' and viewid = '" + viewId + "'";
		UwViewVO vo = null;
		try{
			Collection<UwViewVO> viewVOs = dao.retrieveByClause(UwViewVO.class, condition);
			if(viewVOs != null){
				Iterator<UwViewVO> it = viewVOs.iterator();
				while(it.hasNext()){
					vo = it.next();
					if(vo != null)
						break;
				}
			}
			if(vo == null){
				vo = new UwViewVO();
				vo.setPk_template(pk_template);
				vo.setViewid(viewId);
				dao.insertVO(vo);
			}
		} catch( DAOException e){
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e);
		}
		
		return vo;
	}

	@Override
	public List<UwIncrementVO> getUwIncrementVOsByCondition(String condition)
			throws PaBusinessException {
		BaseDAO dao = new BaseDAO();
		List<UwIncrementVO> increVOs = new ArrayList<UwIncrementVO>();
		 try {
			Collection<UwIncrementVO> vos = dao.retrieveByClause(UwIncrementVO.class, condition);
			Iterator<UwIncrementVO> it = vos.iterator();
			while(it.hasNext()){
				increVOs.add(it.next());
			}
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e.getMessage(), e);
		}
		if(increVOs != null && increVOs.size() > 0)
			return increVOs;
		else
			return null;
	}

	@Override
	public UwTemplateVO getTemplateOrCreate(String pk_template)
			throws PaBusinessException {
		
		BaseDAO dao = new BaseDAO();
		UwTemplateVO vo = null;
		String condition = "pk_template = '" + pk_template + "'";
		try{
			Collection<UwTemplateVO> vos = dao.retrieveByClause(UwTemplateVO.class, condition);
			Iterator<UwTemplateVO> it = vos.iterator();
			if(vos != null){
				while(it.hasNext()){
					vo = it.next();
					if(vo != null)
						break;
				}
			}
			if(vo == null){
				vo = new UwTemplateVO();
				dao.insertVO(vo);
			}
			
		} catch(DAOException e){
			LfwLogger.error(e.getMessage(), e);
			
		}
		
		return vo;
	}

	@Override
	public List<UwViewVO> getViewVOsByCondition(String condition)throws PaBusinessException {
		BaseDAO dao = new BaseDAO();
		List<UwViewVO> viewVos = new ArrayList<UwViewVO>();
		 try {
			Collection<UwViewVO> vos = dao.retrieveByClause(UwViewVO.class, condition);
			Iterator<UwViewVO> it = vos.iterator();
			while(it.hasNext()){
				viewVos.add(it.next());
			}
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e.getMessage(), e);
		}
		if(viewVos != null && viewVos.size() > 0)
			return viewVos;
		else
			return null;
	}

	@Override
	public String getTemplatePkByBusiidAndAppId(String busiId, String appId)
			throws PaBusinessException {
		
		String condition = "busiid = '" + busiId + "' and appid = '" + appId + "'";
		BaseDAO dao = new BaseDAO();
		UwTemplateVO vo = null;
		try {
			Collection<UwTemplateVO> vos = dao.retrieveByClause(UwTemplateVO.class, condition);
			Iterator<UwTemplateVO> it = vos.iterator();
			if(vos != null){
				while(it.hasNext()){
					vo = it.next();
					if(vo != null)
						break;
				}
			}
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e.getMessage(), e);
		}
		
		if(vo == null)
			return null;
		else
			return vo.getPrimaryKey();
	}

	@Override
	public void deleteTemplateByBusiIdAndAppId(String busiId, String appId)
			throws PaBusinessException {
		BaseDAO dao = new BaseDAO();
		String pk_template = this.getTemplatePkByBusiidAndAppId(busiId, appId);
		try {
			dao.deleteByPK(UwTemplateVO.class, pk_template);
			dao.deleteByPK(UwViewVO.class, pk_template);
			dao.deleteByPK(UwIncrementVO.class, pk_template);
		} catch (DAOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new PaBusinessException(e.getMessage(), e);
		}
		
	}

}
