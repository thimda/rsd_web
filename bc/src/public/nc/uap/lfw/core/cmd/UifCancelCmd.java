package nc.uap.lfw.core.cmd;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.bm.ButtonStateManager;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;

/**
 * "取消"菜单默认逻辑处理
 * @author gd
 *
 */
public class UifCancelCmd extends UifCommand {

	private String masterDsId;
	public UifCancelCmd(String masterDsId){
		this.masterDsId = masterDsId;
	}
	 
	public void execute() {
		boolean pageUndo = false;
		boolean widgetUndo = false;
		
		LfwWidget widget = getLifeCycleContext().getViewContext().getView();
		if(widget == null)
			throw new LfwRuntimeException("片段为空!");
		
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
		for (int i = 0; i < idList.size(); i++) {
			getLifeCycleContext().getApplicationContext().addExecScript("pageUI.getWidget('" + widget.getId() + "').getDataset('" + idList.get(i) + "').undo();\n");
		}
		if (widgetUndo)
			getLifeCycleContext().getApplicationContext().addBeforeExecScript("pageUI.getWidget('" + widget.getId() + "').undo();\n");
		if (pageUndo)
			getLifeCycleContext().getApplicationContext().addBeforeExecScript("pageUI.undo();\n");
		
		masterDs.setEnabled(false);
		updateButtons();
	}

	protected void updateButtons() {
		ButtonStateManager.updateButtons();
	}
	
	

}
