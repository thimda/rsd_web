package nc.uap.lfw.reference.util;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.GridRowEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.GridRowServerListener;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.core.uif.delegator.IUifDeletator;
import nc.uap.lfw.reference.ReferenceConstant;
import nc.uap.lfw.reference.action.RefMultiGridOkDelegator;
import nc.uap.lfw.reference.action.RefOkDelegator;
import nc.uap.lfw.util.LfwClassUtil;

/**
 * 多选参照的右边grid类
 * @author zhangxya
 *
 */
public class RefMultiGidRowListener extends GridRowServerListener {

	@Override
	public void onRowDbClick(GridRowEvent e) {
		// TODO Auto-generated method stub
		Dataset ds = getCurrentContext().getWidget().getViewModels().getDataset("rightGridDs");
		String refNodeId = (String) getGlobalContext().getWebSession().getAttribute("refNodeId");
		String widgetId = (String) getGlobalContext().getWebSession().getAttribute("widgetId");
		PageMeta parentPm = getGlobalContext().getParentGlobalContext().getPageMeta();
		RefNode refnode = (RefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		IUifDeletator delegator = null;
		if(refnode != null && refnode.getRefnodeDelegator() != null && !refnode.getRefnodeDelegator().equals("")){
			delegator = (IUifDeletator) LfwClassUtil.newInstance( refnode.getRefnodeDelegator(), new Class[]{Dataset.class}, new Object[]{ds});
		}	
		else
			delegator = new RefMultiGridOkDelegator(ds);
		delegator.setGlobalContext(getGlobalContext());
		delegator.execute();
	}

	public RefMultiGidRowListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
		// TODO Auto-generated constructor stub
	}

}
