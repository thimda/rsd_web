package nc.uap.lfw.core.uif.listener;

import nc.uap.lfw.core.InteractionUtil;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.uif.delegator.IUifDeletator;
import nc.uap.lfw.core.uif.delegator.UifDelDelegator;
import nc.uap.lfw.core.vo.LfwExAggVO;

/**
 * "删除"按钮监听器
 * @author gd
 *
 */
public abstract class UifDelMouseListener<T extends WebElement> extends UifMouseListener<T> {
	public UifDelMouseListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	@Override
	public void onclick(MouseEvent<T> e) {
		InteractionUtil.showConfirmDialog("确认对话框", "确认删除吗?");
		if (InteractionUtil.getConfirmDialogResult().equals(Boolean.FALSE)) return;
		
		IUifDeletator delegator = getDelegator();
		delegator.setGlobalContext(getGlobalContext());
		delegator.execute();
	}

	protected IUifDeletator getDelegator() {
		IUifDeletator delegator = new UifDelDelegator(getWidgetId(), getDatasetId(), getAggVoClazz());
		return delegator;
	}

	
	protected String getAggVoClazz()
	{
		return LfwExAggVO.class.getName();
	}
	
	
	/**
	 * 要删除的数据集id
	 * @return
	 */
	@UifMethodAnnotation(isWidget=false,isDataset=true, isMultiSel = false, isMust = true, tip = "MAIN", name="主数据集")
	public abstract String getDatasetId();
	
	/**
	 * 要删除的数据集所属片段的id
	 * @return
	 */
	@UifMethodAnnotation(isWidget=true,isDataset=false, isMultiSel = false, isMust = true, tip = "MAIN", name="片段")
	public abstract String getWidgetId();
}
