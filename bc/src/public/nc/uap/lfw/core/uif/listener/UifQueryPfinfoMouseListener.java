package nc.uap.lfw.core.uif.listener;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.uif.delegator.IUifDeletator;
import nc.uap.lfw.core.uif.delegator.UifQueryPfinfoDelegator;

/**
 * ������������listener
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
	 * ��ȡҪ���е�ds������widget
	 * @return
	 */
	@UifMethodAnnotation(isWidget=true, isDataset=false, isMultiSel = false, isMust = true, tip = "MAIN", name="Ƭ��")
	public abstract String getWidgetId();
	
	/**
	 * ��ȡҪ���е�ds��id
	 * @return
	 */
	@UifMethodAnnotation(isDataset=true, isMust = true, tip = "�����ݼ�", name="�����ݼ�")
	public abstract String getDatasetId();
}
