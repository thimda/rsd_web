package nc.uap.lfw.reference.action;

import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebTreeNode;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.refnode.RefNode;

public class RefMultiGridOkDelegator extends RefOkDelegator {
	
	@Override
	protected void processDatasetOk(String widgetId, PageMeta parentPm,
			RefNode rfnode, Row[] currRows) {
		// TODO Auto-generated method stub
		String writeFieldStr = rfnode.getWriteFields();
		String[] writeFields = writeFieldStr.split(",");
		// 绑定了dataset
		Dataset wds = parentPm.getWidget(widgetId).getViewModels().getDataset(rfnode.getWriteDs());
		
//		Row row = wds.getSelectedRow();
		Row row = wds.getFocusRow();
		if (row == null)
			row = wds.getSelectedRow();
		//for (int i = 0; i < writeFields.length; i++) {
			String valuePKs = "";
			String valueNames = "";
			for (int j = 0; j < currRows.length; j++) {
				Row currRow = currRows[j];
				if(currRow != null){
					int pkIndex = currentDs.nameToIndex("pk");
					int nameIndex = currentDs.nameToIndex("name");
					if(pkIndex == -1)
						throw new LfwRuntimeException("没有找到列:" + "pk!");
					if(j != currRows.length -1){
						valuePKs += currRow.getValue(pkIndex) + ",";
						valueNames += currRow.getValue(nameIndex) + ",";
					}
					else{
						valuePKs += currRow.getValue(pkIndex);
						valueNames += currRow.getValue(nameIndex);
					}
				}
			}
			row.setValue(wds.nameToIndex(writeFields[0]), valuePKs);
			row.setValue(wds.nameToIndex(writeFields[1]), valueNames);
		//}
		
	}


	private Dataset currentDs;

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		String widgetId = (String) getGlobalContext().getWebSession().getAttribute("widgetId");
		String refNodeId = (String) getGlobalContext().getWebSession().getAttribute("refNodeId");
		PageMeta parentPm = getGlobalContext().getWebContext().getParentPageMeta();
		RefNode rfnode = (RefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		Row[] currRows = null;
		if(rfnode.isMultiSel())
			currRows = currentDs.getCurrentRowData().getRows();
		else{
			Row currRowOnly = currentDs.getSelectedRow();
			currRows = new Row[]{currRowOnly};
		}
		// 是否只选中叶子节点
		boolean selLeafOnly = rfnode.isSelLeafOnly();
		// 树
		WebComponent tree = getGlobalContext().getWebContext().getPageMeta().getWidget("main").getViewComponents().getComponent("reftree");
		
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

	
	public RefMultiGridOkDelegator(Dataset ds) {
		super(ds);
		this.currentDs = ds;
		// TODO Auto-generated constructor stub
	}

}
