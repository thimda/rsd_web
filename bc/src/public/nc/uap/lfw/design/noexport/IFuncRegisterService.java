package nc.uap.lfw.design.noexport;

import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.design.itf.FuncNodeVO;
import nc.uap.lfw.design.itf.TemplateVO;
import nc.uap.lfw.design.itf.TypeNodeVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.print.PrintTemplateVO;

public interface IFuncRegisterService {
	public List<FuncNodeVO> getFuncNodeVOs() throws LfwBusinessException;
	
	public List<TemplateVO> getTemplateVOs() throws LfwBusinessException;
	
	public String getBillType(String funcnode) throws LfwBusinessException;
	
	public List<TemplateVO> getTemplateByFuncnode(String funcnode) throws LfwBusinessException;
	
	public List<TemplateVO> getAllTemplateByFuncnode(String funcnode) throws LfwBusinessException;
	public List<TemplateVO> getQueryTemplateByFuncnode(String funcnode) throws LfwBusinessException;
	
	public void updateSysTemplate(FuncNodeVO funnode, TemplateVO[] templateVOs) throws LfwBusinessException;
	
	public void delSysTemplateRelated(String funnode) throws LfwBusinessException;
	
	public void deleteFunNode(String funnode) throws LfwBusinessException;
	
	public TypeNodeVO[] getAllMainOrgType() throws LfwBusinessException;
	public TypeNodeVO getOrgTypeById(String id) throws LfwBusinessException;
	public String[] getAllRefNode() throws LfwBusinessException;
	
	public PrintTemplateVO[] queryAll() throws LfwBusinessException;
	
	public String[][] getAllAccounts() throws LfwBusinessException;
	
	public String[] registerBillAction(String billType, String[] actionTypes) throws LfwBusinessException;
	
	public void registerMenuItems(List<MenuItem> menuItems, String funnode) throws LfwBusinessException;
	
	public String getVersion();
	
	public Map<String, String>[] getPageNames(String[] projPaths);
	
//	public PageFlow[][] getPageFlowDef(String[] projPaths);
//	
//	public void pageFlowToXMl(String projectPath,PageFlow pageFlow);
	
	public void refresh();
}
