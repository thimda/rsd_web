package nc.uap.lfw.core.cmd.base;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.exception.LfwValidateException;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.vo.LfwExAggVO;

public abstract class AbstractWidgetController<T extends WebElement> {
	// public void onAdd(MouseEvent<T> mouseEvent){
	// UifAddAction action=new
	// UifAddAction(getMasterDsId(),getPageState(),null,null);
	// action.execute();
	// }
	// public void onEdit(MouseEvent mouseEvent){
	// UifEditAction action = new UifEditAction(getMasterDsId(),getPageState());
	// action.execute();
	// }
	// public void onSave(MouseEvent mouseEvent){
	// UifSaveAction action = new
	// UifSaveAction(getMasterDsId(),getDetailDsIds(),getAggVoClazz(),bodyNotNull());
	// action.execute();
	// }
	// public void onCancel(MouseEvent mouseEvent){
	// UifCancelAction action = new UifCancelAction(getMasterDsId());
	// action.execute();
	// }
	// public void onDelete(MouseEvent mouseEvent){
	// UifDelAction action = new UifDelAction(getMasterDsId(),getAggVoClazz());
	// action.execute();
	// }

	public abstract String getMasterDsId();

	protected String getAggVoClazz() {
		return LfwExAggVO.class.getName();
	}

	/**
	 * 根据dsRelation自动获取子表ds
	 * 
	 * @return
	 */
	protected String[] getDetailDsIds() {
		LfwWidget widget = AppLifeCycleContext.current().getViewContext()
				.getView();
		if (widget.getViewModels().getDsrelations() != null) {
			DatasetRelation[] rels = widget.getViewModels().getDsrelations()
					.getDsRelations(getMasterDsId());
			if (rels != null) {
				String[] detailDsIds = new String[rels.length];
				for (int i = 0; i < rels.length; i++) {
					detailDsIds[i] = rels[i].getDetailDataset();
				}
				return detailDsIds;
			}
		}
		return null;
	}

	protected List<Dataset> getDetailDs(String[] detailDsIds){
		LfwWidget widget = AppLifeCycleContext.current().getViewContext()
		.getView();
		ArrayList<Dataset> detailDs = new ArrayList<Dataset>();
		if (detailDsIds != null && detailDsIds.length > 0) {
			for (int i = 0; i < detailDsIds.length; i++) {
				Dataset ds = widget.getViewModels().getDataset(detailDsIds[i]);
				if (ds != null)
					detailDs.add(ds);
			}
		}
		return detailDs;
	}
	
	protected boolean bodyNotNull() {
		return true;
	}

	protected void doValidate(Dataset masterDs)
			throws LfwValidateException {
//		IDataValidator validator = new DefaultDataValidator();
//		validator.validate(masterDs, new LfwWidget());		
	}
}
