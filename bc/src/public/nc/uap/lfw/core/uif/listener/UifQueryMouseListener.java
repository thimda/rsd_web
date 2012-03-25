package nc.uap.lfw.core.uif.listener;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.uif.delegator.IUifDeletator;
import nc.uap.lfw.core.uif.delegator.UifQueryDelegator;

public abstract class UifQueryMouseListener<T extends WebElement> extends UifMouseListener<T> {

	public UifQueryMouseListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	@Override
	public void onclick(MouseEvent<T> e) {
		IUifDeletator delegator = getDelegator();
		delegator.setGlobalContext(getGlobalContext());
		delegator.execute();
	}

	protected IUifDeletator getDelegator() {
		IUifDeletator delegator = new UifQueryDelegator(getQueryPageId(), getPage(), getTemplateId(), getPWidgetId(), getPDatasetId());
		return delegator;
	}
		
	@UifMethodAnnotation(name="��ѯҳ��", tip="��ѯҳ��ID", defaultValue="#PAGE#.#CPAGE#")
	public abstract String getQueryPageId();
	
	public String getTemplateId() {
		return "query";
	}
	
	public String getPage() {
		return "query.jsp";
	}

	@UifMethodAnnotation(name="Ŀ�길Ƭ��", tip="Ŀ�길Ƭ��ID", defaultValue="main")
	public abstract String getPWidgetId();
	
	@UifMethodAnnotation(name="Ŀ�길���ݼ�", tip="Ŀ�길���ݼ�ID", defaultValue="")
	public abstract String getPDatasetId();
}
