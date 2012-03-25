package nc.uap.lfw.design.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bcmanage.bs.IBusiCenterManageService;
import nc.bcmanage.vo.BusiCenterVO;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.bbd.func.IFuncRegisterQueryService;
import nc.itf.uap.querytemplate.IQueryTemplate;
import nc.itf.uap.reporttemplate.IReportTemplateQry;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanMappingListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.uap.lfw.core.common.ExtAttrConstants;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.util.PageNameUtil;
import nc.uap.lfw.core.util.PageNodeUtil;
import nc.uap.lfw.design.itf.FuncNodeVO;
import nc.uap.lfw.design.itf.TemplateVO;
import nc.uap.lfw.design.itf.TypeNodeVO;
import nc.uap.lfw.design.noexport.IFuncRegisterService;
import nc.vo.bd.ref.RefInfoVO;
import nc.vo.org.orgmodel.OrgTypeVO;
import nc.vo.org.util.OrgTypeManager;
import nc.vo.pub.BusinessException;
import nc.vo.pub.bill.BillTempletHeadVO;
import nc.vo.pub.bill.BillTempletHeadVOMeta;
import nc.vo.pub.compiler.BusiclassVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pftemplate.SystemplateVO;
import nc.vo.pub.print.PrintTemplateVO;
import nc.vo.pub.query.QueryTempletTotalVO;
import nc.vo.pub.query.QueryTempletVO;
import nc.vo.pub.report.ReportTempletVO;
import nc.vo.sm.funcreg.ButtonRegVO;
import nc.vo.sm.funcreg.FunRegisterConst;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.sm.funcreg.ModuleVO;

public class FuncRegisterServiceImpl implements IFuncRegisterService {

	public String getBillType(String funcnode) throws LfwBusinessException{
		BaseDAO dao = new BaseDAO();
		String sql = "SELECT pk_billtypecode FROM bd_billtype WHERE nodecode = ?  or webnodecode = ?";
		SQLParameter para = new SQLParameter();
		para.addParam(funcnode);
		para.addParam(funcnode);
		String  billType = null;
		try {
			billType = (String) dao.executeQuery(sql, para, new ColumnProcessor());
		} catch (DAOException e) {
			Logger.error(e.getMessage(), e);
		}
		return  billType;
	}
	
	public List<FuncNodeVO> getFuncNodeVOs() throws LfwBusinessException{
		List<FuncNodeVO> list = new ArrayList<FuncNodeVO>();
		IFuncRegisterQueryService funcQryService = NCLocator.getInstance().lookup(IFuncRegisterQueryService.class);
		try {
			
			List<ModuleVO> modules = (List<ModuleVO>) new BaseDAO().retrieveAll(ModuleVO.class);
			if(modules != null){
				Iterator<ModuleVO> it = modules.iterator();
				while(it.hasNext()){
					FuncNodeVO vo = new FuncNodeVO();
					ModuleVO module = it.next();
					vo.setFuncCode(module.getModuleid());
					vo.setFuncName(module.getSystypename());
					vo.setPk_func(module.getModuleid());
					vo.setPk_parent(module.getParentcode());
					vo.setNodeType(FuncNodeVO.MODULE_FUNC_NODE);
					vo.setOwnmodule(module.getModuleid());
					list.add(vo);
				}
			}
			
			nc.vo.sm.funcreg.FuncRegisterVO[] vos = funcQryService.queryAllNCFunction(false);
			if(vos != null){
				for (int i = 0; i < vos.length; i++) {
//					if(vos[i].getFuntype()!= null && vos[i].getFuntype() == FuncNodeVO.EXECUTABLE_FUNC_NODE)
//						continue;
					FuncNodeVO vo = new FuncNodeVO();
					vo.setFuncCode(vos[i].getFuncode());
					vo.setFuncName(vos[i].getFun_name());
					vo.setPk_func(vos[i].getCfunid());
					String pid = vos[i].getParent_id();
					if(pid == null)
						pid = vos[i].getOwn_module();
					vo.setPk_parent(pid);
					if(vos[i].getFun_property() == null){
						vo.setNodeType(FuncNodeVO.INEXECUTABLE_FUNC_NODE);
						vo.setFunType(FuncNodeVO.INEXECUTABLE_FUNC_NODE);
					}
					else
					{
						vo.setNodeType(FuncNodeVO.EXECUTABLE_FUNC_NODE);
						vo.setFunType(FuncNodeVO.EXECUTABLE_FUNC_NODE);
						vo.setFunType(vos[i].getFuntype());
						vo.setFunurl(vos[i].getClass_name());
						vo.setOrgTypeCode(vos[i].getOrgtypecode());
						vo.setFunDesc(vos[i].getFun_desc());
					}
					vo.setOwnmodule(vos[i].getOwn_module());
					list.add(vo);
				}
			}

		} 
		catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e);
		}
		return list;
	}

	
	public BillTempletHeadVO[] findTemplet(String where) {
		BaseDAO dao = new BaseDAO();
		String strMainSql = "select pub_billtemplet.pk_billtemplet,pub_billtemplet.pk_billtypecode," +
				"pub_billtemplet.bill_templetname,pub_billtemplet.bill_templetcaption,pub_billtemplet.pk_corp," +
				"pub_billtemplet.model_type,pub_billtemplet.modulecode,pub_billtemplet.options,pub_billtemplet.shareflag,pub_billtemplet.nodecode,pub_billtemplet.resid ";

		strMainSql = strMainSql + "from pub_billtemplet where pub_billtemplet.pk_billtemplet <> '0'";
		if (where != null)
			strMainSql = strMainSql + " and " + where;
		strMainSql = strMainSql + " order by pub_billtemplet.pk_corp,pub_billtemplet.pk_billtypecode";
		List vHeadVO = null;
			try {
				vHeadVO = (List) dao.executeQuery(strMainSql, new BeanMappingListProcessor(BillTempletHeadVO.class, new BillTempletHeadVOMeta()));
			} catch (DAOException e) {
				LfwLogger.error(e.getMessage(), e);
			}
		    
			BillTempletHeadVO[] headVOs = new BillTempletHeadVO[vHeadVO.size()];
			vHeadVO.toArray(headVOs);
			return headVOs;

//		} catch (DbException e1) {
//			Logger.error(e1.getMessage(), e1);
//			throw new BillTemplateRuntimeException(e1.getMessage());
//		}
	}

	
	public List<TemplateVO> getTemplateVOs() throws LfwBusinessException{
		List<TemplateVO> voList = new ArrayList<TemplateVO>();
		//IBillTemplateQry qry = (IBillTemplateQry) NCLocator.getInstance().lookup(IBillTemplateQry.class.getName());
		try {
			//单据模板
			BillTempletHeadVO[] vos = findTemplet(null);
			if(vos != null){
				for (int i = 0; i < vos.length; i++) {
					BillTempletHeadVO vo = vos[i];
					TemplateVO tvo = new TemplateVO();
					tvo.setPk_template(vo.getPkPubBilltempletHead());
					tvo.setBilltypecode(vo.getPkBillTypeCode());
					tvo.setTemplatecaption(vo.getBillTempletCaption());
					if(vo.getModulecode() != null)
						tvo.setModulecode(vo.getModulecode());
					tvo.setTemplatetype(TemplateVO.TEMPLAGE_TYPE_BILL);
					tvo.setTemplatecode(vo.getPkBillTypeCode());
					voList.add(tvo);
				}
			}
			//打印模板
			PrintTemplateVO[] printtempvos = queryAll();
			if(printtempvos != null){
				for (int i = 0; i < printtempvos.length; i++) {
					PrintTemplateVO vo = printtempvos[i];
					TemplateVO tvo = new TemplateVO();
					tvo.setPk_template(vo.getPrimaryKey());
					tvo.setBilltypecode(vo.getVtemplatecode());
					tvo.setTemplatecaption(vo.getVtemplatename());
					tvo.setModulecode(vo.getVnodecode());
					tvo.setTemplatetype(TemplateVO.TEMPLAGE_TYPE_PRINT);
					tvo.setTemplatecode(vo.getVtemplatecode());
					voList.add(tvo);
				}
			}
			//查询模板
			IQueryTemplate queryTemplate = (IQueryTemplate) NCLocator.getInstance().lookup(IQueryTemplate.class.getName());
			QueryTempletTotalVO[] querytempvo = queryTemplate.queryQueryTempletTotalVOByWherePart(null);
			if(querytempvo != null){
				for (int i = 0; i < querytempvo.length; i++) {
					QueryTempletVO vo = querytempvo[i].getTempletVO();
					TemplateVO tvo = new TemplateVO();
					tvo.setPk_template(vo.getPrimaryKey());
					tvo.setBilltypecode(vo.getNodeCode());
					tvo.setTemplatecaption(vo.getModelName());
					tvo.setModulecode(vo.getModelCode());
					tvo.setTemplatetype(TemplateVO.TEMPLAGE_TYPE_QUERY);
					tvo.setTemplatecode(vo.getModelCode());
					voList.add(tvo);
				}
			}
			
			//报表模板
			IReportTemplateQry report = (IReportTemplateQry) NCLocator.getInstance().lookup(IReportTemplateQry.class.getName());
			ReportTempletVO[] reportvos = report.selectDefaultLeaf();
			if(reportvos != null){
				for (int i = 0; i < reportvos.length; i++) {
					ReportTempletVO vo = reportvos[i];
					TemplateVO tvo = new TemplateVO();
					tvo.setPk_template(vo.getPrimaryKey());
					tvo.setBilltypecode(vo.getNodeCode());
					tvo.setTemplatecaption(vo.getNodeName());
					tvo.setModulecode(vo.getParentCode());
					tvo.setTemplatetype(TemplateVO.TEMPLAGE_TYPE_REPORT);
					tvo.setTemplatecode(vo.getNodeCode());
					voList.add(tvo);
				}
			}
			
			
		} 
		catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e);
		}
		return voList;
	}

	public List<TemplateVO> getTemplateByFuncnode(String funcnode) throws LfwBusinessException{
		try {
			String sql = "select bill.bill_templetcaption as templatecaption, sys.nodekey, " +
					"bill.pk_billtemplet as pk_template, sys.tempstyle as templatetype from pub_systemplate sys, pub_billtemplet bill where sys.funnode = ? and sys.pk_corp = '@@@@' " +
					" and sys.templateid=bill.pk_billtemplet";
			BaseDAO dao = new BaseDAO();
			SQLParameter param = new SQLParameter();
			param.addParam(funcnode);
			List list = (List) dao.executeQuery(sql, param, new BeanListProcessor(TemplateVO.class));
			return list;
		} 
		catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e);
		}
	}
	
	public List<TemplateVO> getAllTemplateByFuncnode(String funcnode) throws LfwBusinessException{
		try {
			String sql = "select bill.pk_billtypecode as billtypecode, bill.bill_templetcaption as templatecaption, sys.nodekey, " +
					"bill.pk_billtemplet as pk_template, sys.tempstyle as templatetype from pub_systemplate sys, pub_billtemplet bill where sys.funnode = ? and sys.pk_corp = '@@@@' " +
					" and sys.templateid=bill.pk_billtemplet";
			BaseDAO dao = new BaseDAO();
			SQLParameter param = new SQLParameter();
			param.addParam(funcnode);
			List listTemp = (List) dao.executeQuery(sql, param, new BeanListProcessor(TemplateVO.class));
			
			String sql1 = "select bill.model_name AS templatecaption, sys.nodekey, bill.id as " +
			"pk_template, sys.tempstyle as templatetype from pub_systemplate sys, pub_query_templet " +
			"bill where sys.funnode = ? and sys.pk_corp = '@@@@'  and sys.templateid=bill.id";
	
			SQLParameter param1 = new SQLParameter();
			param1.addParam(funcnode);
			List listquery = (List) dao.executeQuery(sql1, param1, new BeanListProcessor(TemplateVO.class));
			
			for (int i = 0; i < listquery.size(); i++) {
				if(!listTemp.contains(listquery.get(i)))
					listTemp.add(listquery.get(i));
			}
			
			String sql2 = "select bill.vtemplatename AS templatecaption, sys.nodekey, bill.ctemplateid as pk_template, sys.tempstyle AS" +
					"	 templatetype from pub_systemplate sys, pub_print_template " +
					"	bill where sys.funnode = ? and sys.pk_corp = '@@@@'  and sys.templateid=bill.ctemplateid";
			SQLParameter param2 = new SQLParameter();
			param2.addParam(funcnode);
			List listprint = (List) dao.executeQuery(sql2, param2, new BeanListProcessor(TemplateVO.class));
			
			for (int i = 0; i < listprint.size(); i++) {
				if(!listTemp.contains(listprint.get(i)))
					listTemp.add(listprint.get(i));
			}
		
			return listTemp;
		} 
		catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e);
		}
	}
	
	public List<TemplateVO> getQueryTemplateByFuncnode(String funcnode) throws LfwBusinessException{
		try {
			String sql = "select bill.model_name AS templatecaption, sys.nodekey, bill.id as " +
					"pk_template, sys.tempstyle as templatetype from pub_systemplate sys, pub_query_templet " +
					"bill where sys.funnode = ? and sys.pk_corp = '@@@@'  and sys.templateid=bill.id";
			BaseDAO dao = new BaseDAO();
			SQLParameter param = new SQLParameter();
			param.addParam(funcnode);
			List list = (List) dao.executeQuery(sql, param, new BeanListProcessor(TemplateVO.class));
			return list;
		} 
		catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e);
		}
	}
	
	/**
	 * 取消nc发布
	 */
	public void delSysTemplateRelated(String funnode) throws LfwBusinessException{
		try {
			BaseDAO dao = new BaseDAO();
			String sql = "delete from pub_systemplate where funnode = ?";
			SQLParameter param = new SQLParameter();
			param.addParam(funnode);
			dao.executeUpdate(sql, param);
		}catch(DAOException e){
			Logger.error(e.getMessage(), e);
			throw new  LfwBusinessException(e);
		}
	}
	
	public void deleteFunNode(String funcnode) throws LfwBusinessException{
		nc.itf.uap.bbd.func.IFuncRegisterService funService = NCLocator.getInstance().lookup(nc.itf.uap.bbd.func.IFuncRegisterService.class);
		try {
			funService.deleteFunction(funcnode);
			IFuncRegisterQueryService qryService = NCLocator.getInstance().lookup(IFuncRegisterQueryService.class);
			FuncRegisterVO 	func = qryService.queryFunctionByCode(funcnode);
				if(func != null){
					BaseDAO dao = new BaseDAO();
					String sql = "delete from sm_butnregister WHERE parent_id =  ?";
					SQLParameter param = new SQLParameter();
					param.addParam(func.getPrimaryKey());
					dao.executeUpdate(sql, param);
				}
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
				throw new  LfwBusinessException(e);
		}
		//首先删除原来的注册
		delSysTemplateRelated(funcnode);
	}
	


	public void updateSysTemplate(FuncNodeVO funnode, TemplateVO[] templateVOs)
			throws LfwBusinessException {
		try {
			BaseDAO dao = new BaseDAO();
			//test code
//			funnode.setFunType(0);
//			funnode.setOrgTypeCode(IOrgConst.GLOBEORGTYPE);
//			funnode.setPk_parent(null);
//			funnode.setOwnmodule("TT01");
			if(funnode.getPk_func() != null){
				String sql = "delete from pub_systemplate where funnode = ?";
				SQLParameter param = new SQLParameter();
				param.addParam(funnode.getFuncCode());
				dao.executeUpdate(sql, param);
				
				
				IFuncRegisterQueryService qryService = NCLocator.getInstance().lookup(IFuncRegisterQueryService.class);
				FuncRegisterVO func = qryService.queryFunctionByCFunid(funnode.getPk_func());
				func.setFun_name(funnode.getFuncName());
				func.setFuncode(funnode.getFuncCode());
				func.setClass_name(funnode.getFunurl());
				func.setFun_property(FunRegisterConst.LFW_FUNC_NODE);
				func.setFuntype(funnode.getFunType());
				func.setOrgtypecode(funnode.getOrgTypeCode());
				func.setParent_id(funnode.getPk_parent());
				func.setFun_desc(funnode.getFunDesc());
				func.setFuntype(FuncNodeVO.EXECUTABLE_FUNC_NODE);
				func.setOwn_module(funnode.getOwnmodule());
				dao.updateVO(func);
				
			}
			else{
				FuncRegisterVO func = new FuncRegisterVO();
				func.setFun_name(funnode.getFuncName());
				func.setFuncode(funnode.getFuncCode());
				func.setClass_name(funnode.getFunurl());
				func.setFun_property(FunRegisterConst.LFW_FUNC_NODE);
				func.setFuntype(funnode.getFunType());
				func.setFuntype(FuncNodeVO.EXECUTABLE_FUNC_NODE);
				func.setOrgtypecode(funnode.getOrgTypeCode());
				func.setParent_id(funnode.getPk_parent());
				func.setOwn_module(funnode.getOwnmodule());
				func.setFun_desc(funnode.getFunDesc());
//				func.setForbid_flag(0);
//				func.setFun_code("");
//				func.setFun_level(0);
//				func.setGroup_flag(0);
//				func.setSystem_flag(0);
//				func.setSubsystem_id("");
				String pk = dao.insertVO(func);
				funnode.setPk_func(pk);
			}
			if(templateVOs.length == 0)
				return;
			
//			String userInfo = templateVOs[0].getOperatorinfo();
//			String userName = userInfo.split(":")[1];
//			String sqluser = "SELECT * FROM sm_user WHERE user_name = ?";
//			SQLParameter paramuser = new SQLParameter();
//			paramuser.addParam(userName);
//			UserVO uservo = (UserVO) dao.executeQuery(sqluser, paramuser, new BeanProcessor(UserVO.class));
			
			SystemplateVO[] vos = new SystemplateVO[templateVOs.length];
			for (int i = 0; i < templateVOs.length; i++) {
				vos[i] = new SystemplateVO();
				vos[i].setFunnode(funnode.getFuncCode());
				vos[i].setNodekey(templateVOs[i].getNodekey());
				vos[i].setPk_corp("@@@@");
				vos[i].setTemplateflag(UFBoolean.TRUE);
				vos[i].setTemplateid(templateVOs[i].getPk_template());
				vos[i].setTempstyle(templateVOs[i].getTemplatetype());
//				if(uservo != null){
//					vos[i].setOperator(uservo.getCuserid());
//					vos[i].setOperator_type(OrganizeUnitTypes.Operator_INT);
//				}
			}
			dao.insertVOArray(vos);
		} 
		catch (DAOException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new LfwBusinessException(e);
		}
	}
	
	public TypeNodeVO[] getAllMainOrgType() throws LfwBusinessException {
		OrgTypeVO[] types =  OrgTypeManager.getInstance().getAllMainOrgType();
		List list = new ArrayList();
		for (int i = 0; i < types.length; i++) {
			TypeNodeVO type = new TypeNodeVO();
			type.setPk_typenode(types[i].getPk_orgtype());
			type.setTypename(types[i].getName());
			list.add(type);
		}
		return (TypeNodeVO[])list.toArray(new TypeNodeVO[list.size()]);
	}
	
	public TypeNodeVO getOrgTypeById(String id) throws LfwBusinessException {
		OrgTypeVO typenode = OrgTypeManager.getInstance().getOrgTypeByID(id);
		TypeNodeVO type = new TypeNodeVO();
		type.setPk_typenode(typenode.getPk_orgtype());
		type.setTypename(typenode.getName());
		return type;
	}

	public String[] getAllRefNode() throws LfwBusinessException {
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Collection col = null;
		RefInfoVO[] vos = null;
		List list = new ArrayList();
		try {
			col = iUAPQueryBS.retrieveByClause(RefInfoVO.class,
					" 1=1 order by name ");
		} catch (BusinessException e) {
			Logger.error(e);
			throw new IllegalStateException("查询RefInfoVO出错," + e.getMessage());
		}
		if (col != null && col.size() > 0) {
			vos = new RefInfoVO[col.size()];
			col.toArray(vos);
		} 
		for (int i = 0; i < vos.length; i++) {
			list.add(vos[i].getName());
		}
		return (String[])list.toArray(new String[list.size()]);
	}

	public PrintTemplateVO[] queryAll() throws LfwBusinessException {
		 {
			BaseDAO dao = new BaseDAO();
	        SQLParameter params = new SQLParameter();
	        String sql = "";
	    	sql ="select ctemplateid, pk_corp, vnodecode, vtemplatecode, vtemplatename, itopmargin, ibotmargin, ileftmargin, irightmargin, vdefaultprinter, bdirector, ipageheight, ipagewidth, fpagination, igridcolor, bnormalcolor, iscale, ipagelocate, bdistotalpagenum, bdispagenum, ibreakposition, vleftnote, vmidnote, vrightnote, vfontname, ffontstyle, ifontsize from pub_print_template";
	    	PrintTemplateVO printTemplates[] = null;
	    	List result = null;
	    	try {
	    	    result = (List) dao.executeQuery(sql, new BeanMappingListProcessor(PrintTemplateVO.class, PrintVOMapModelFactory.getPrintRegionVOMapMeta()) );
	    	} catch (DAOException e) {
	    		Logger.error(e.getMessage(), e);
				throw new LfwBusinessException(e);
	    	}	
	    	if( result!=null ) {
	    	    printTemplates = (PrintTemplateVO[]) result.toArray(new PrintTemplateVO[result.size()]);
	    	}

	    	return printTemplates;
	    }
	}

	public String[][] getAllAccounts() throws LfwBusinessException {
		try{	
			IBusiCenterManageService busiCenter = NCLocator.getInstance().lookup(IBusiCenterManageService.class);
			BusiCenterVO[] accounts = busiCenter.getBusiCenterVOs();
			String[][] options;
			if (accounts != null && accounts.length > 1) {
				// 去掉系统管理帐套
				options = new String[accounts.length - 1][2];
				int j = 0;
				for (int i = 0; i < accounts.length; i++) {
					// 系统管理帐套是否固定为 0000
					if (accounts[i].getCode().equals("0000"))
						continue;
					options[j][0] = accounts[i].getCode();
					options[j][1] = accounts[i].getName();
					j++;
				}
			} 
			else
				options = new String[0][2];
			return options;
		}
		catch(Exception e){
			throw new LfwBusinessException(e);
		}
	}

	
	/**
	 * 将生成的MenuItem插入表中
	 */
	public void registerMenuItems(List<MenuItem> menuItems, String funnode) throws LfwBusinessException{
		try{
			IFuncRegisterQueryService qryService = NCLocator.getInstance().lookup(IFuncRegisterQueryService.class);
			FuncRegisterVO func = qryService.queryFunctionByCode(funnode);
			//首先删除原来的注册
			if(func != null){
				BaseDAO dao = new BaseDAO();
				String sql = "delete from sm_butnregister WHERE parent_id =  ?";
				SQLParameter param = new SQLParameter();
				param.addParam(func.getPrimaryKey());
				dao.executeUpdate(sql, param);
			}
			List<ButtonRegVO> buttonList = new ArrayList<ButtonRegVO>();
			for (int i = 0; i < menuItems.size(); i++) {
				MenuItem menuItem = menuItems.get(i);
				ButtonRegVO buttonVO = new ButtonRegVO();
				buttonVO.setBtncode(menuItem.getId());
				buttonVO.setBtnname(menuItem.getText());
				if(func != null)
					buttonVO.setParent_id(func.getPrimaryKey());
				buttonVO.setIsbuttonpower(new UFBoolean(false));
				buttonVO.setIsenable(new UFBoolean(true));
				buttonList.add(buttonVO);
			}
			BaseDAO dao = new BaseDAO();
			dao.insertVOArray(buttonList.toArray(new ButtonRegVO[0]));
		}
		catch(Exception e){
			throw new LfwBusinessException(e);
		}
	}
	
	public String[] registerBillAction(String billType, String[] actionTypes) throws LfwBusinessException {
		try{
			BaseDAO dao = new BaseDAO();
			//首先删除pk_billtype已经存在的定义，再插入
			String sql = "delete from pub_busiclass where pk_billtype = ? and actiontype = ?";
			SQLParameter param = new SQLParameter();
			param.addParam(billType);
			param.addParam(actionTypes[0]);
			dao.executeUpdate(sql, param);
			//插入
			List<BusiclassVO> busiList = new ArrayList<BusiclassVO>();
			//	List<BillactionVO> list = new ArrayList<BillactionVO>();
			for (int i = 0; i < actionTypes.length; i++) {
				BusiclassVO actionVO = new BusiclassVO();
				String actionType = actionTypes[i];
	//			String actionName = null;
	//			String actionStyle = null;
	//			String showHint = null;
	//			if(actionType.equals("SAVE")){
	//				actionName = "保存";
	//				actionStyle = "1";
	//			}
	//			else if(actionType.equals("WRITE")){
	//				actionName = "提交";
	//				actionStyle = "0";
	//			}
	//			else if(actionType.equals("APPROVE")){
	//				actionName = "审批";
	//				actionStyle = "2";	
	//			}
	//			
	//			else if(actionType.equals("UNAPPROVE")){
	//				actionName = "弃审";
	//				actionStyle = "3";	
	//			}
	//			
	//	
	//			else if(actionType.equals("DELETE")){
	//				actionName = "作废";
	//				actionStyle = "3";	
	//				showHint = "是否确认删除";
	//			}
	//			
				actionVO.setActiontype(actionType);
				actionVO.setPk_billtype(billType);
				actionVO.setClassname("N_" + billType + "_" + actionType);
				actionVO.setIsbefore(new UFBoolean(false));
				actionVO.setPk_businesstype("KHHH0000000000000001");
				
	//			actionVO.setActionnote(actionName);
	//			actionVO.setActionstyle(actionStyle);
	//			actionVO.setActionstyleremark(null);
	//			actionVO.setActiontype(actionType);
	//			actionVO.setConstrictflag(UFBoolean.FALSE);
	//			actionVO.setControlflag(UFBoolean.FALSE);
	//			actionVO.setFinishflag(UFBoolean.FALSE);
	//			//actionVO.setDirty(false);
	//			actionVO.setPk_billtype(billType);
	//			actionVO.setShowhint(showHint);
				busiList.add(actionVO);
			}
			
			dao.insertVOArray(busiList.toArray(new BusiclassVO[0]));
			return getActionClass(billType, actionTypes);
		}
		catch(Exception e){
			throw new LfwBusinessException(e);
		}
	}
	
	public String[] getActionClass(String billType, String[] actionTypes) throws BusinessException {
		String[] classSources = new String[actionTypes.length];
		for (int i = 0; i < actionTypes.length; i++) {
			String action = actionTypes[i];
			StringBuffer buf = new StringBuffer();
			BufferedReader reader = null;
			try {
				//this.getClass().getClassLoader().getResourceAsStream("nc/bs/pub/action/N_XXXX_" + action + ".java")
				String path = RuntimeEnv.getInstance().getNCHome() + "/resources/lfw/pf";
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(path + "/N_XXXX_" + action + ".java")));
				String line = reader.readLine();
				while(line != null){
					buf.append(line);
					buf.append("\r\n");
					line = reader.readLine();
				}
				String cs = buf.toString();
				cs = cs.replaceAll("XXXX", billType);
				classSources[i] = cs;
			} 
			catch (IOException e) {
				throw new LfwRuntimeException("获取脚本时出错，billType:" + billType + ",action:" + action);
			}
			finally{
				if(reader != null){
					try {
						reader.close();
					} 
					catch (IOException e) {
					}
				}
			}
		}
		return classSources;
	}

	public String getVersion() {
		// TODO Auto-generated method stub
		return ExtAttrConstants.VERSION60;
	}
	
	public Map<String, String>[] getPageNames(String[] projPaths){
		return PageNameUtil.getPageNames(projPaths);
	}
	
//	public PageFlow[][] getPageFlowDef(String[] projPaths){
//		return PageFlowRegister.getPageFlowDef(projPaths);
//	}
//	
//	public void pageFlowToXMl(String projectPath,PageFlow pageFlow){
//		 PageFlowUtil.toXml(projectPath, pageFlow);
//	}
	
//	public void refresh(String nodeDir){
//		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
//		PageNodeUtil.refresh(nodeDir);
//	}
	
	public void refresh(){
		PageNodeUtil.refresh();
	}
	
}
