package nc.uap.lfw.core.uif.listener;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.uif.delegator.IUifDeletator;
import nc.uap.lfw.core.uif.delegator.UifQueryPfinfoDelegator;

/**
 * 联查审批单据listener
 * @author zhangxya
 *
 * @param <T>
 */
public abstract class UifQueryPfinfoMouseListener<T extends WebElement> extends UifMouseListener<T>  {

	public UifQueryPfinfoMouseListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public void onclick(MouseEvent<T> e) {
		IUifDeletator delegator = getDelegator();
		delegator.setGlobalContext(getGlobalContext());
		delegator.execute();
	}

	protected IUifDeletator getDelegator() {
		IUifDeletator del = new UifQueryPfinfoDelegator(getPageId(), getWidgetId(), getDatasetId());
		return del;
	}
	

	protected String getPageId(){
		return "pfinfo";
	}
	
	/**
	 * 获取要增行的ds所属的widget
	 * @return
	 */
	@UifMethodAnnotation(isWidget=true, isDataset=false, isMultiSel = false, isMust = true, tip = "MAIN", name="片段")
	public abstract String getWidgetId();
	
	/**
	 * 获取要增行的ds的id
	 * @return
	 */
	@UifMethodAnnotation(isDataset=true, isMust = true, tip = "主数据集", name="主数据集")
	public abstract String getDatasetId();
}
