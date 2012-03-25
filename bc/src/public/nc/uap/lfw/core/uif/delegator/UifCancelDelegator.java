package nc.uap.lfw.core.uif.delegator;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;

/**
 * "�༭"�˵��߼�����
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
			throw new LfwRuntimeException("δָ��Ƭ��id!");
		LfwWidget widget = getGlobalContext().getPageMeta().getWidget(this.widgetId);
		if(widget == null)
			throw new LfwRuntimeException("Ƭ��Ϊ��,widgetId=" + widgetId + "!");
		
		if(this.masterDsId == null)
			throw new LfwRuntimeException("δָ�������ݼ�id!");
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if(masterDs == null)
			throw new LfwRuntimeException("���ݼ�Ϊ��,���ݼ�id=" + masterDsId + "!");
		
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
//				// ���ò���״̬
//				getGlobalContext().getPageMeta().setOperatorState(IOperatorState.INIT);
//			else{
//				//ѡ�����������У���undoʱ��
//				if(row.getState() == Row.STATE_ADD){
//					// ���ò���״̬
//					getGlobalContext().getPageMeta().setOperatorState(IOperatorState.INIT);
//				}
//				// ���ò���״̬
//				else{
//					getGlobalContext().getPageMeta().setOperatorState(IOperatorState.SINGLESEL);
//				}
//			}
//		}
//		if(masterDs.isControlwidgetopeStatus()){
//			if(masterDs.getSelectedRow() == null)
//				// ���ò���״̬
//				getGlobalContext().getPageMeta().getWidget(widgetId).setOperatorState(IOperatorState.INIT);
//			else
//				// ���ò���״̬
//				getGlobalContext().getPageMeta().getWidget(widgetId).setOperatorState(IOperatorState.SINGLESEL);
//		}
	}
	
	

}
