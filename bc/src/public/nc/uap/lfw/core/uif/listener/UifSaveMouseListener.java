package nc.uap.lfw.core.uif.listener;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.uif.delegator.IUifDeletator;
import nc.uap.lfw.core.uif.delegator.UifSaveDelegator;
import nc.uap.lfw.core.vo.LfwExAggVO;

public abstract class UifSaveMouseListener<T extends WebElement> extends UifMouseListener<T> {

	public UifSaveMouseListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}
	
	public void onclick(MouseEvent<T> e) {
		IUifDeletator delegator = getDelegator();
		delegator.setGlobalContext(getGlobalContext());
		delegator.execute();
	}
	
	protected IUifDeletator getDelegator() {
		return new UifSaveDelegator(getWidgetId(), getMasterDsId(), getDetailDsIds(), getAggVoClazz(), bodyNotNull());
	}


	protected String getAggVoClazz()
	{
		return LfwExAggVO.class.getName();
	}
	
	/**
	 * 根据dsRelation自动获取子表ds
	 * @return
	 */
	protected String[] getDetailDsIds()
	{
		LfwWidget widget = getGlobalContext().getWidgetContext(getWidgetId()).getWidget();
		if(widget.getViewModels().getDsrelations() != null){
			DatasetRelation[] rels = widget.getViewModels().getDsrelations().getDsRelations(getMasterDsId());
			if(rels != null)
			{
				String[] detailDsIds = new String[rels.length];
				for (int i = 0; i < rels.length; i++) {
					detailDsIds[i] = rels[i].getDetailDataset();
				}
				return detailDsIds;
			}
		}
		return null;
	}
	@UifMethodAnnotation(isWidget=true,isDataset=false, isMultiSel = false, isMust = true, tip = "MAIN", name="片段")
	protected abstract String getWidgetId();

	@UifMethodAnnotation(isWidget=false,isDataset=true, isMultiSel = false, isMust = true, tip = "MAIN", name="主数据集")
	protected abstract String getMasterDsId();
	
	protected boolean bodyNotNull() {
		return true;
	}

}
