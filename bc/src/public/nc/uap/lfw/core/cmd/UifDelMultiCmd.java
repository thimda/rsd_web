package nc.uap.lfw.core.cmd;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.crud.ILfwCRUDService;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

/**
 * 删除单个子表的多条数据
 * @author zhangxya
 *
 */
public class UifDelMultiCmd extends UifCommand{
	private String masterDsId;
	
	public UifDelMultiCmd(String masterDsId){
		this.masterDsId = masterDsId;
	}
	
	
	public void execute() {
		AppInteractionUtil.showConfirmDialog("确认对话框", "确认删除吗?");
		if (AppInteractionUtil.getConfirmDialogResult().equals(Boolean.FALSE))
			return;
		LfwWidget widget = getLifeCycleContext().getViewContext().getView();
		if(widget == null)
			throw new LfwRuntimeException("片段为空!");
		if(this.masterDsId == null)
			throw new LfwRuntimeException("未指定数据集id!");
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if(masterDs == null)
			throw new LfwRuntimeException("数据集为空,数据集id=" + masterDsId + "!");
		// 要删除的所有主表行
		Row[] delRows = masterDs.getSelectedRows();
		if(delRows != null && delRows.length > 0){
			// 持久化 
			Dataset2SuperVOSerializer ser = new Dataset2SuperVOSerializer();
			SuperVO[] superVOs = ser.serialize(masterDs, delRows);
			onDeleteVO(superVOs);
			for (int j = 0; j < delRows.length; j++) {
				masterDs.removeRow(masterDs.getRowIndex(delRows[j]));
			}
		
		}
		
//		// 设置操作状态
//		if(masterDs.isControloperatorStatus())
//			getLifeCycleContext().getWindowContext().getWindow().setOperatorState(IOperatorState.INIT);
//		if(masterDs.isControlwidgetopeStatus())
//			getLifeCycleContext().getViewContext().getView().setOperatorState(IOperatorState.INIT);
		updateButtons();
	}
	
	protected void onDeleteVO(SuperVO[] vos){
		try {
			getCrudService().deleteVos(vos);
		} catch (LfwBusinessException e) {
			Logger.error(e, e);
			throw new LfwRuntimeException(e);
		}
	}
	
	protected ILfwCRUDService<SuperVO, AggregatedValueObject> getCrudService() {
		return CRUDHelper.getCRUDService();
	}
}


