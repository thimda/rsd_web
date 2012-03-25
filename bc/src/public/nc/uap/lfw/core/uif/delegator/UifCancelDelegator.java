package nc.uap.lfw.core.uif.delegator;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;

/**
 * "编辑"菜单逻辑处理
 * @author gd
 *
 */
public class UifCancelDelegator extends DefaultUifDelegator {

	private String widgetId;
	private String masterDsId;
	public UifCancelDelegator(String widgetId, String masterDsId){
		this.widgetId = widgetId;
		this.masterDsId = masterDsId;
	}
	
	public void execute() {
		boolean pageUndo = false;
		boolean widgetUndo = false;
		if(this.widgetId == null)
			throw new LfwRuntimeException("未指定片段id!");
		LfwWidget widget = getGlobalContext().getPageMeta().getWidget(this.widgetId);
		if(widget == null)
			throw new LfwRuntimeException("片段为空,widgetId=" + widgetId + "!");
		
		if(this.masterDsId == null)
			throw new LfwRuntimeException("未指定主数据集id!");
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if(masterDs == null)
			throw new LfwRuntimeException("数据集为空,数据集id=" + masterDsId + "!");
		
		List<String> idList = new ArrayList<String>();
		idList.add(masterDsId);
//		if (masterDs.isControloperatorStatus())
//			pageUndo = true;
		if (masterDs.isControlwidgetopeStatus())
			widgetUndo = true;
		
		if(widget.getViewModels().getDsrelations()!=null){
			DatasetRelation[] rels =  widget.getViewModels().getDsrelations().getDsRelations(masterDsId);
			if(rels != null){
				for (int i = 0; i < rels.length; i++) {
					String detailDsId = rels[i].getDetailDataset();
					Dataset detailDs = widget.getViewModels().getDataset(detailDsId);
					detailDs.setEnabled(false);
					idList.add(detailDsId);
//					if (detailDs.isControloperatorStatus())
//						pageUndo = true;
					if (detailDs.isControlwidgetopeStatus())
						widgetUndo = true;
				}
			}
		}
		getGlobalContext().datasetUndo(this.widgetId, idList.toArray(new String[0]));
		if (widgetUndo)
			getGlobalContext().widgetUndo(this.widgetId);
		if (pageUndo)
			getGlobalContext().pageUndo();
		
		masterDs.setEnabled(false);
		setPageState(masterDs);
	}

	protected void setPageState(Dataset masterDs) {
//		if(masterDs.isControloperatorStatus()){
//			Row row = masterDs.getSelectedRow();
//			if(row == null)
//				// 设置操作状态
//				getGlobalContext().getPageMeta().setOperatorState(IOperatorState.INIT);
//			else{
//				//选中行是新增行，在undo时会
//				if(row.getState() == Row.STATE_ADD){
//					// 设置操作状态
//					getGlobalContext().getPageMeta().setOperatorState(IOperatorState.INIT);
//				}
//				// 设置操作状态
//				else{
//					getGlobalContext().getPageMeta().setOperatorState(IOperatorState.SINGLESEL);
//				}
//			}
//		}
//		if(masterDs.isControlwidgetopeStatus()){
//			if(masterDs.getSelectedRow() == null)
//				// 设置操作状态
//				getGlobalContext().getPageMeta().getWidget(widgetId).setOperatorState(IOperatorState.INIT);
//			else
//				// 设置操作状态
//				getGlobalContext().getPageMeta().getWidget(widgetId).setOperatorState(IOperatorState.SINGLESEL);
//		}
	}
	
	

}
