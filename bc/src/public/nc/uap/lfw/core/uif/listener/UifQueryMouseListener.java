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
		
	@UifMethodAnnotation(name="查询页面", tip="查询页面ID", defaultValue="#PAGE#.#CPAGE#")
	public abstract String getQueryPageId();
	
	public String getTemplateId() {
		return "query";
	}
	
	public String getPage() {
		return "query.jsp";
	}

	@UifMethodAnnotation(name="目标父片段", tip="目标父片段ID", defaultValue="main")
	public abstract String getPWidgetId();
	
	@UifMethodAnnotation(name="目标父数据集", tip="目标父数据集ID", defaultValue="")
	public abstract String getPDatasetId();
}
