package nc.uap.lfw.reference.app;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebTreeNode;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.refnode.MasterFieldInfo;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.core.refnode.RefNodeRelation;
import nc.uap.lfw.reference.base.ILfwRefModel;
import nc.uap.lfw.reference.util.LfwRefUtil;

/**
 * 点击去定之后执行的命令
 * @author zhangxya
 *
 */
public class AppRefOkCmd extends UifCommand{

	private Dataset currentDs;
	public AppRefOkCmd(Dataset ds) {
		super();
		this.currentDs = ds;
	}
	@Override
	public void execute() {
		String widgetId = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("widgetId");
		String refNodeId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("refNodeId");
		//PageMeta parentPm = LfwRuntimeEnvironment.getWebContext().getParentPageMeta();
		String parentPageId = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("otherPageId");
		PageMeta parentPm = AppLifeCycleContext.current().getApplicationContext().getWindowContext(parentPageId).getWindow();
		
		RefNode rfnode = (RefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		Row[] currRows = null;
		if(rfnode.isMultiSel())
			currRows = currentDs.getAllSelectedRows();
		else{
			Row currRowOnly = currentDs.getSelectedRow();
			currRows = new Row[]{currRowOnly};
		}
//		if (currRows == null)
//			currRows = currentDs.getFocusRow();
		// 是否只选中叶子节点
		boolean selLeafOnly = rfnode.isSelLeafOnly();
		// 树
		WebComponent tree = AppLifeCycleContext.current().getViewContext().getView().getViewComponents().getComponent("reftree");
		
		if (currRows != null) {
			if (tree != null && selLeafOnly) {
				for (int i = 0; i < currRows.length; i++) {
					Row currRow = currRows[i];
					WebTreeNode node = ((TreeViewComp)tree).getTreeModel().getTreeNodeByRowId(currRow.getRowId());
					if (node.getChildNodeList() != null && node.getChildNodeList().size() > 0)  // 非叶子节点
						return;
				}
			}
		} 
		else {
			return;
		}
		
		/**
		 * 输入框的参照
		 */
		if (rfnode.getWriteDs() == null || "".equals(rfnode.getWriteDs())) {  // 未绑定dataset
			processTextOk(widgetId, parentPm, rfnode, currRows);
		}
		/**
		 * 数据集之上的参照
		 */
		else {
			processDatasetOk(widgetId, parentPm, rfnode, currRows);
		}
		
		
		//清空子参照数据
		clearRefRelationData(widgetId, parentPm, rfnode);
		
		 /*******************/
		 
		refClose();
	}

	protected void clearRefRelationData(String widgetId, PageMeta parentPm,
			RefNode rfnode) {
		if(parentPm.getWidget(widgetId).getViewModels().getRefNodeRelations() != null){
			String writeFieldStr = rfnode.getWriteFields();
			String[] writeFields = writeFieldStr.split(",");
			Map<String, RefNodeRelation>  refNodeRelMap = parentPm.getWidget(widgetId).getViewModels().getRefNodeRelations().getRefnodeRelations();
			for (Iterator<String> itwd = refNodeRelMap.keySet().iterator(); itwd.hasNext();) {
				 String id = itwd.next();
				 RefNodeRelation refNodeRel = refNodeRelMap.get(id);
				 List<MasterFieldInfo> masterFieldInfoList =  refNodeRel.getMasterFieldInfos();
				 for (int i = 0; i < masterFieldInfoList.size(); i++) {
					 MasterFieldInfo masterFieldInfo = masterFieldInfoList.get(i);
					 if(masterFieldInfo.getDsId() == null ||  masterFieldInfo.getFieldId() == null)
						 continue;
					 if(masterFieldInfo.getDsId().equals(rfnode.getWriteDs()) && masterFieldInfo.getFieldId().equals(writeFields[0])){
						 String detailRefNodeid = refNodeRel.getDetailRefNode();
						 RefNode relatedRefNode =  (RefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(detailRefNodeid);
						 if(relatedRefNode != null){
							 Dataset relatedWriteDs = parentPm.getWidget(widgetId).getViewModels().getDataset(relatedRefNode.getWriteDs());
							 String relatedwriteFieldStr = relatedRefNode.getWriteFields();
							 String[] relatedwriteFields = relatedwriteFieldStr.split(",");
							 Row relatedRow = relatedWriteDs.getSelectedRow();
							 if(relatedRow != null){
								 for (int j = 0; j < relatedwriteFields.length; j++) {
									relatedRow.setValue(relatedWriteDs.nameToIndex(relatedwriteFields[j]), "");
								 }
							 }
							 break;
						 }
					 }
				}
			 }
		}
	}

	/**
	 * 处理数据集之上的参照
	 * @param widgetId
	 * @param parentPm
	 * @param rfnode
	 * @param currRow
	 */
	protected void processDatasetOk(String widgetId, PageMeta parentPm,
			RefNode rfnode, Row[] currRows) {
		String readFieldStr = rfnode.getReadFields();
		String[] readFields = readFieldStr.split(",");
		
		String writeFieldStr = rfnode.getWriteFields();
		String[] writeFields = writeFieldStr.split(",");
		// 绑定了dataset
		Dataset wds = parentPm.getWidget(widgetId).getViewModels().getDataset(rfnode.getWriteDs());
		
//		Row row = wds.getSelectedRow();
		Row row =  wds.getSelectedRow();
		if(row == null)
			row = wds.getFocusRow();
		Map<String, String> valueMap = new HashMap<String, String>();
		for (int i = 0; i < writeFields.length; i++) {
			String value = "";
			for (int j = 0; j < currRows.length; j++) {
				Row currRow = currRows[j];
				if(currRow != null){
					int index = currentDs.nameToIndex(readFields[i]);
					if(index == -1)
						throw new LfwRuntimeException("没有找到列:" + readFields[i]);
					if(j != currRows.length -1)
						value += currRow.getValue(index) + ",";
					else
						value += currRow.getValue(index);
				}
			}
//			row.setValue(wds.nameToIndex(writeFields[i]), value);
			valueMap.put(writeFields[i], value);
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("type", "Dataset");
		paramMap.put("id", rfnode.getWriteDs());
		paramMap.put("writeFields", valueMap);
		
//		paramMap.put("type", "'Dataset'");
//		paramMap.put("id", "'" + rfnode.getWriteDs() + "'");
//		StringBuffer valueBuff = new StringBuffer();
//		valueBuff.append("{");
//		Iterator<String> it = valueMap.keySet().iterator();
//		while(it.hasNext()){
//			String filed = it.next();
//			String value = valueMap.get(filed);
//			valueBuff.append(filed).append(":'").append(value).append("',");
//		}
//		String values = valueBuff.toString();
//		if (values.endsWith(","))
//			values = values.substring(0, values.length() - 1);
//		values += "}";
//		paramMap.put("writeFields", values); 
		
		
		
		
		UifPlugoutCmd uifPluOutCmd = new UifPlugoutCmd("main","refOkPlugout",paramMap);
		uifPluOutCmd.execute();
	}

	/**
	 * 处理单输入框的参照
	 * @param widgetId
	 * @param parentPm
	 * @param rfnode
	 * @param currRow
	 */
	protected void processTextOk(String widgetId, PageMeta parentPm,
			RefNode rfnode, Row[] currRows) {
//		String readFieldStr = rfnode.getReadFields();
//		String[] readFields = readFieldStr.split(",");
		
//		String writeFieldStr = rfnode.getWriteFields();
//		String[] writeFields = writeFieldStr.split(",");
		String owner = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute("owner");
		//ReferenceComp refComp = (ReferenceComp) parentPm.getWidget(widgetId).getViewComponents().getComponent(owner);
		
		// 获取显示值和真实值
		ILfwRefModel rm = LfwRefUtil.getRefModel(rfnode);
		String rfStr = rfnode.getReadFields();
		String[] rfs = rfStr.split(",");
		String returnField = null;
		String pkField = rm.getRefPkField();
		String codeField = rm.getRefCodeField();
		String nameField = rm.getRefNameField();
		for (int i = 0; i < rfs.length; i++) {
			if(!rfs[i].equals(pkField)){
				if(rfs[i].equals(codeField) || rfs[i].equals(nameField)){
					returnField = rfs[i];
					break;
				}
			}
		}
		if(returnField == null)
			returnField = nameField;
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("type", "Text");
		paramMap.put("id", owner);
//		paramMap.put("type", "'Text'");
//		paramMap.put("id", "'" + owner + "'");
	
		if(currRows != null){
			String value = "";
			String showValue = "";
			for (int i = 0; i < currRows.length; i++) {
				Row currRow = currRows[i];
				if(currRow == null)
					continue;
				if(i != currRows.length - 1){
					value += (String) currRow.getValue(currentDs.getFieldSet().fieldToIndex(pkField)) + ",";
					showValue += (String) currRow.getValue(currentDs.getFieldSet().fieldToIndex(returnField)) + ",";
				}
				else{
					value += (String) currRow.getValue(currentDs.getFieldSet().fieldToIndex(pkField));
					showValue += (String) currRow.getValue(currentDs.getFieldSet().fieldToIndex(returnField));
				}
			}
			//refComp.setValue(value);
			//refComp.setShowValue(showValue);
			
			paramMap.put("key", value);
			paramMap.put("value", showValue);
//			StringBuffer valueBuff = new StringBuffer();
//			valueBuff.append("{");
//			valueBuff.append("key").append(":'").append(value).append("',");
//			valueBuff.append("value").append(":'").append(showValue).append("'");
//			valueBuff.append("}");
//			paramMap.put("writeFields", valueBuff.toString()); 
		}
		UifPlugoutCmd uifPluOutCmd = new UifPlugoutCmd("main","refOkPlugout",paramMap);
		uifPluOutCmd.execute();
		//AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
	}
	
	protected void refClose() {
		// 隐藏Div
//		getGlobalContext().addExecScript("parent.document.onclick();");
		//AppLifeCycleContext.current().getApplicationContext().addExecScript("if (parent.window.currentReferenceWithDivOpened) parent.window.currentReferenceWithDivOpened.hideRefDiv();");
		
		// 关闭Dialog
//		AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
		
		// 关闭Excel控件打开的参照对话框
		//getGlobalContext().addExecScript("if (parent.window.parentPage.excelComp) parent.window.parentPage.ExcelComp.closeRefDialog();");
		
	}

}

