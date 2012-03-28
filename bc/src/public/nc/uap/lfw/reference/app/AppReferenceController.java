package nc.uap.lfw.reference.app;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.common.DatasetConstant;
import nc.uap.lfw.core.comp.ReferenceComp;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.data.Parameter;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowSet;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.GridRowEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.TextEvent;
import nc.uap.lfw.core.event.TreeNodeEvent;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.refnode.NCRefNode;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.core.serializer.impl.List2DatasetSerializer;
import nc.uap.lfw.reference.ReferenceConstant;
import nc.uap.lfw.reference.base.BaseRefModel;
import nc.uap.lfw.reference.base.ILfwRefModel;
import nc.uap.lfw.reference.base.RefResult;
import nc.uap.lfw.reference.base.TreeGridRefModel;
import nc.uap.lfw.reference.base.TwoTreeGridRefModel;
import nc.uap.lfw.reference.base.TwoTreeRefModel;
import nc.uap.lfw.reference.nc.NcAdapterTreeGridRefModel;
import nc.uap.lfw.reference.util.LfwRefUtil;
import nc.uap.lfw.util.JsURLDecoder;
import nc.uap.lfw.util.LfwClassUtil;
import nc.ui.bd.ref.AbstractRefModel;

public class AppReferenceController {
	
	private static final String RELATION_WHERE_SQL = "relationWhereSql";
	public void onDataLoad(DataLoadEvent e) {
		String widgetId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("widgetId");
		String refNodeId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("refNodeId");
		PageMeta parentPm = LfwRuntimeEnvironment.getWebContext().getParentPageMeta();
		RefNode rfnode = (RefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		ILfwRefModel refModel = LfwRefUtil.getRefModel(rfnode);
		Dataset ds = (Dataset) e.getSource();
		String filterValue = ds.getReqParameters().getParameterValue("filterValue");
		if(filterValue!=null){
			filterValue = JsURLDecoder.decode(filterValue, "UTF-8");
		}

		if(ds.getId().equals(ReferenceConstant.MASTER_DS_ID)){
			ViewContext widgetCtx = AppLifeCycleContext.current().getViewContext();
			LfwWidget widget = widgetCtx.getView();
			List<List<Object>> v = null;
			PaginationInfo pInfo = ds.getCurrentRowSet().getPaginationInfo();
			
			// 根据参照关联关系产生的查询条件语句
			Parameter parameter = ds.getReqParameter(RELATION_WHERE_SQL);
			if (null != parameter) {
				String relationWhereSql = parameter.getValue();
				if (relationWhereSql != null && !"".equals(relationWhereSql))
					refModel.addWherePart(relationWhereSql);
			}
			
			processSelfWherePart(ds, rfnode, filterValue, refModel);
			
			//将上次查询条件加入到where条件中
//			String wherePart = refModel.getWherePart();
//			if(ds.getLastCondition() != null){
//				if(wherePart == null)
//					wherePart = ds.getLastCondition();
//				else
//					wherePart += " and " + ds.getLastCondition();
//				refModel.setWherePart(wherePart);
//			}
			
			if (filterValue == null || filterValue.equals("")) {
				DatasetRelations dsRels = widget.getViewModels().getDsrelations();
				if (dsRels != null) {
					String parentDsId = dsRels.getMasterDsByDetailDs(ds.getId());
					if (parentDsId != null) {  // 为子DS
						Dataset parentDs = widget.getViewModels().getDataset(parentDsId);
						Row row = parentDs.getSelectedRow();
						if (row != null) {  // 主DS有选中行
							DatasetRelation dr = dsRels.getDsRelation(parentDsId, ds.getId());
							String masterKey = dr.getMasterKeyField();
							String keyValue = (String) row.getValue(parentDs.getFieldSet().nameToIndex(masterKey));
							
							if(keyValue == null)
								keyValue = row.getRowId();
							ds.setCurrentKey(keyValue);
							RowSet rowset = ds.getRowSet(keyValue, true);
							PaginationInfo pinfo = rowset.getPaginationInfo();
							pinfo.setPageIndex(0);
							
							if(refModel instanceof TreeGridRefModel){
								TreeGridRefModel treeRefModel = (TreeGridRefModel) refModel;
								treeRefModel.setClassJoinValue(keyValue);
								RefResult rr = treeRefModel.getRefData(-1);
								pInfo.setRecordsCount(rr.getTotalCount());
								v = rr.getData();
								new List2DatasetSerializer().serialize(keyValue, pinfo, v, ds);
								return;
							}
							else if(refModel instanceof TwoTreeRefModel){
								TwoTreeRefModel treeRefModel = (TwoTreeRefModel) refModel;
								//treeRefModel.setClassJoinValue(keyValue);
								String where = treeRefModel.getDocJoinField() + "= '" + keyValue + "'";
								treeRefModel.setWherePart(where);
								RefResult rr = treeRefModel.getRefData(-1);
								pInfo.setRecordsCount(rr.getTotalCount());
								v = rr.getData();
								new List2DatasetSerializer().serialize(keyValue, pinfo, v, ds);
								return;
							}
						}
					}
				}
				
				int pageIndex = pInfo.getPageIndex();
				RefResult rd = refModel.getRefData(pageIndex);
				if(rd != null){
					pInfo.setRecordsCount(rd.getTotalCount());
					v = rd.getData();
				}
				//new List2DatasetSerializer().serialize(ds.getCurrentKey(), pInfo, v, ds);
			}
			else{
				RefResult rd = refModel.getFilterRefData(filterValue, pInfo.getPageIndex());
				pInfo.setRecordsCount(rd.getTotalCount());
				v = rd.getData();
			}
			new List2DatasetSerializer().serialize(ds.getCurrentKey(), pInfo, v, ds);
		}
		//处理参照是2级树的情况
		else if(ds.getId().equals(ReferenceConstant.MASTER_DS_ID + "_tree_1")){
			if(refModel instanceof TwoTreeGridRefModel){
				TwoTreeGridRefModel tgModel = (TwoTreeGridRefModel) refModel;
				processSelfWherePart(ds, rfnode, filterValue, tgModel);
				List<List<Object>> v = tgModel.getTopClassData();
				new List2DatasetSerializer().serialize(ds.getCurrentKey(), null, v, ds);
			}
			if(refModel instanceof TwoTreeRefModel){
				TwoTreeRefModel tgModel = (TwoTreeRefModel) refModel;
				processSelfWherePart(ds, rfnode, filterValue, tgModel);
				List<List<Object>> v = tgModel.getTopClassData();
				new List2DatasetSerializer().serialize(ds.getCurrentKey(), null, v, ds);
			}
		}
		else if(ds.getId().equals("rightGridDs")){
			Dataset wds = parentPm.getWidget(widgetId).getViewModels().getDataset(rfnode.getWriteDs());
			Row row = wds.getFocusRow();
			if (row == null)
				row = wds.getSelectedRow();
			String writeFieldStr = rfnode.getWriteFields();
			String[] writeFields = writeFieldStr.split(",");
			String valuePK = (String) row.getValue(wds.nameToIndex(writeFields[0]));
			if( valuePK == null)
				return;
			String[] valuePKs = valuePK.split(",");
			String valueName = (String) row.getValue(wds.nameToIndex(writeFields[1]));
			String[] valueNames = valueName.split(",");
			for (int i = 0; i < valuePKs.length; i++) {
				String valPk = valuePKs[i];
				Row newRow = ds.getEmptyRow();
				ds.addRow(newRow);
				newRow.setValue(0, valPk);
				String valName = valueNames[i];
				newRow.setValue(1, valName);
			}
		}
		else {
			TreeGridRefModel tgModel = (TreeGridRefModel) refModel;
			//根据参数设置VO条件
			String keys = ds.getReqParameters().getParameterValue(DatasetConstant.QUERY_PARAM_KEYS);
			if(keys != null && !keys.equals("")){
				String values = ds.getReqParameters().getParameterValue(DatasetConstant.QUERY_PARAM_VALUES);
				String wherePart = (keys + " = '" + values + "'");
				tgModel.setClassWherePart(wherePart);
			}
			
			processSelfWherePart(ds, rfnode, filterValue, refModel);
			
			List<List<Object>> v = tgModel.getClassData();
			new List2DatasetSerializer().serialize(ds.getCurrentKey(), null, v, ds);
		}
	}
	
	
	
	protected void processSelfWherePart(Dataset ds, RefNode rfnode,
			String filterValue, ILfwRefModel refModel) {
	
	}
	
	
	
	public void onAfterRowSelect(DatasetEvent se) {
		Dataset masterDs = (Dataset) se.getSource();
		Row masterSelecteRow = masterDs.getSelectedRow();
		ViewContext widgetCtx =  AppLifeCycleContext.current().getWindowContext().getCurrentViewContext();
		LfwWidget widget = widgetCtx.getView();
		DatasetRelations dsRels = widget.getViewModels().getDsrelations();
		if(dsRels != null)
		{
			DatasetRelation[] masterRels = dsRels.getDsRelations(masterDs.getId());
			for (int i = 0; i < masterRels.length; i++) {
				DatasetRelation dr = masterRels[i];
				Dataset detailDs = widget.getViewModels().getDataset(dr.getDetailDataset());
				String masterKey = dr.getMasterKeyField();
				//String detailFk = dr.getDetailForeignKey();
				if (masterSelecteRow != null) {  // 有选中行，查询detailDs内容
					String keyValue = (String) masterSelecteRow.getValue(masterDs.getFieldSet().nameToIndex(masterKey));
					
					if(keyValue == null)
						keyValue = masterSelecteRow.getRowId();
					detailDs.setCurrentKey(keyValue);
					RowSet rowset = detailDs.getRowSet(keyValue, true);
					PaginationInfo pinfo = rowset.getPaginationInfo();
					pinfo.setPageIndex(0);
					
					String widgetId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("widgetId");
					String refNodeId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("refNodeId");
					PageMeta parentPm = LfwRuntimeEnvironment.getWebContext().getParentPageMeta();
					RefNode rfnode = (RefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
					BaseRefModel baseRefModel = (BaseRefModel) LfwRefUtil.getRefModel(rfnode);
					String pk_org = null;
					if(rfnode instanceof NCRefNode){
						boolean orgs = ((NCRefNode)rfnode).isOrgs();
						if(orgs){
							ReferenceComp reftext = (ReferenceComp) widget.getViewComponents().getComponent("refcomp_org");
							if(reftext != null){
								pk_org = reftext.getValue(); 
								//根据参数设置VO条件
								
							}
						}
					}
					if(baseRefModel instanceof TreeGridRefModel){
						TreeGridRefModel refModel = (TreeGridRefModel)baseRefModel;
						if(pk_org != null && !pk_org.equals("")){
							if(baseRefModel instanceof NcAdapterTreeGridRefModel){
								AbstractRefModel ncModel = ((NcAdapterTreeGridRefModel)baseRefModel).getNcModel();
								ncModel.setPk_org(pk_org);
							}
						}
						processTreeSelWherePart(masterDs, rfnode, refModel);
						//如果是通过第一级树与masterDs建立的关系
						if(dr.getId().equals("master_slave_rel1")){
							refModel.setClassJoinValue(keyValue);
							//设置关联字段，为了有分页信息时处理，点”下一页“等处理条件
							//detailDs.setLastCondition(refModel.getDocJoinField() + " = '" + keyValue + "'");
							RefResult rr = refModel.getFirstRefData(0);
							List<List<Object>> v = rr.getData();
							pinfo.setRecordsCount(rr.getTotalCount());
							new List2DatasetSerializer().serialize(keyValue, pinfo, v, detailDs);
						}
						else{
							refModel.setClassJoinValue(keyValue);
							//设置关联字段，为了有分页信息时处理，点”下一页“等处理条件
							//detailDs.setLastCondition(refModel.getDocJoinField() + " = '" + keyValue + "'");
							RefResult rr = refModel.getRefData(0);
							List<List<Object>> v = rr.getData();
							pinfo.setRecordsCount(rr.getTotalCount());
							new List2DatasetSerializer().serialize(keyValue, pinfo, v, detailDs);
						}
					}
					else if(baseRefModel instanceof TwoTreeRefModel){
						TwoTreeRefModel refModel = (TwoTreeRefModel)baseRefModel;
						processTreeSelWherePart(masterDs, rfnode, refModel);
						String joinField = refModel.getDocJoinField();
						refModel.setWherePart(joinField + " = '" + keyValue + "'");
						RefResult rr = refModel.getRefData(-1);
						List<List<Object>> v = rr.getData();
						new List2DatasetSerializer().serialize(keyValue, pinfo, v, detailDs);
					}
				} else {  // 没有选中行，detailDs置空
					new List2DatasetSerializer().serialize(null, null, new ArrayList<List<Object>>(), detailDs);
				}
			}
		}	
	}
	
	public void onRowDbClick(GridRowEvent e) {
		LfwWidget widget = AppLifeCycleContext.current().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset(ReferenceConstant.MASTER_DS_ID);
		String refNodeId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("refNodeId");
		String widgetId = (String)  LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("widgetId");
		PageMeta parentPm = LfwRuntimeEnvironment.getWebContext().getParentPageMeta();
		RefNode refnode = (RefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		UifCommand refOkCmd = null;
		if(refnode != null && refnode.getRefnodeDelegator() != null && !refnode.getRefnodeDelegator().equals("")){
			refOkCmd = (UifCommand) LfwClassUtil.newInstance(refnode.getRefnodeDelegator(), new Class[]{Dataset.class}, new Object[]{ds});
		}	
		else
			refOkCmd = new AppRefOkCmd(ds);
		refOkCmd.execute();
	}
	
	
	public void refOk(){
		
	}
	
	/**
	 * 点确定的时候执行方法
	 * @param e
	 */
	public void refOkDelegator(MouseEvent e) {
		LfwWidget widget = AppLifeCycleContext.current().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset(ReferenceConstant.MASTER_DS_ID);
		String refNodeId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("refNodeId");
		String widgetId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("widgetId");
		String parentPageId = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("otherPageId");
		PageMeta parentPm = AppLifeCycleContext.current().getApplicationContext().getWindowContext(parentPageId).getWindow();
		
		RefNode refnode = (RefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		UifCommand refOkCmd = null;
		if(refnode != null && refnode.getRefnodeDelegator() != null && !refnode.getRefnodeDelegator().equals("")){
			refOkCmd = (UifCommand) LfwClassUtil.newInstance(refnode.getRefnodeDelegator(), new Class[]{Dataset.class}, new Object[]{ds});
		}	
		else
			refOkCmd = new AppRefOkCmd(ds);
		refOkCmd.execute();
	}
	
	public void onTreeNodedbclick(TreeNodeEvent e) {
		Dataset ds = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset(ReferenceConstant.MASTER_DS_ID);
		String refNodeId = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("refNodeId");
		String widgetId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("widgetId");
		PageMeta parentPm = LfwRuntimeEnvironment.getWebContext().getParentPageMeta();
		RefNode refnode = (RefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		UifCommand refOkCmd = null;
		if(refnode != null && refnode.getRefnodeDelegator() != null && !refnode.getRefnodeDelegator().equals("")){
			refOkCmd = (UifCommand) LfwClassUtil.newInstance(refnode.getRefnodeDelegator(), new Class[]{Dataset.class}, new Object[]{ds});
		}	
		else
			refOkCmd = new AppRefOkCmd(ds);
		refOkCmd.execute();
	}
	
	public void orgValueChanged(TextEvent e) {
		ReferenceComp text = (ReferenceComp) e.getSource();
		String pk_org = text.getValue();
		String widgetId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("widgetId");
		String refNodeId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("refNodeId");
		PageMeta parentPm = LfwRuntimeEnvironment.getWebContext().getParentPageMeta();
		RefNode rfnode = (RefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		ILfwRefModel refModel = LfwRefUtil.getRefModel(rfnode);
		Dataset ds = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset("masterDs_tree");
		if(ds.getCurrentKey() == null || "".equals(ds.getCurrentKey()))
			ds.setCurrentKey(Dataset.MASTER_KEY);
		TreeGridRefModel tgModel = (TreeGridRefModel) refModel;
		//根据参数设置VO条件
		if(pk_org != null && !pk_org.equals("")){
			AbstractRefModel ncModel = ((NcAdapterTreeGridRefModel)refModel).getNcModel();
			ncModel.setPk_org(pk_org);
		}
		List<List<Object>> v = tgModel.getClassData();
		new List2DatasetSerializer().serialize(ds.getCurrentKey(), null, v, ds);
	}

	protected void processTreeSelWherePart(Dataset ds, RefNode rfnode, ILfwRefModel refModel) {
		
	}
}
