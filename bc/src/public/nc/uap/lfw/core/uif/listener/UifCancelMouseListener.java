package nc.uap.lfw.core.uif.listener;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.uif.delegator.IUifDeletator;
import nc.uap.lfw.core.uif.delegator.UifCancelDelegator;

/**
 * "����"��ť������
 * @author gd 2010-3-26
 *
 */
public abstract class UifCancelMouseListener<T extends WebElement> extends UifMouseListener<T> {

	public UifCancelMouseListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public void onclick(MouseEvent<T> e) {
		IUifDeletator delegator = getDelegator();
		delegator.setGlobalContext(getGlobalContext());
		delegator.execute();
	}

	protected IUifDeletator getDelegator() {
		IUifDeletator delegator = new UifCancelDelegator(getWidgetId(), getDatasetId());
		return delegator;
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
