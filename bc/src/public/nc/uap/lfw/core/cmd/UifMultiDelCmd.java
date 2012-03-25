package nc.uap.lfw.core.cmd;

import nc.bs.logging.Logger;
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

public class UifMultiDelCmd extends UifCommand{
	
	private String masterDsId;

	public UifMultiDelCmd(String masterDsId, String aggVoClazz){
		this.masterDsId = masterDsId;
	}
	

	@Override
	public void execute() {
		LfwWidget widget = getLifeCycleContext().getViewContext().getView();
		if(widget == null)
			throw new LfwRuntimeException("片段为空!");
		
		if(this.masterDsId == null)
			throw new LfwRuntimeException("未指定数据集id!");
		Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
		if(masterDs == null)
			throw new LfwRuntimeException("数据集为空,数据集id=" + masterDsId + "!");
		//要删除的所有行
		Row[] delRows = masterDs.getAllSelectedRows();
		if(delRows != null && delRows.length > 0){
			Dataset2SuperVOSerializer ser = new Dataset2SuperVOSerializer();
			SuperVO[] superVOs = ser.serialize(masterDs, delRows);
			onDeleteVO(superVOs);
			// 删除主表数据
			for (int i = 0; i < delRows.length; i++) {
				masterDs.removeRowInAllRowSets(delRows[i]);
			}
		}
		
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
