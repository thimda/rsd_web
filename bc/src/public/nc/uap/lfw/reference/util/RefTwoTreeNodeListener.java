package nc.uap.lfw.reference.util;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.TreeNodeEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.TreeNodeServerListener;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.core.uif.delegator.IUifDeletator;
import nc.uap.lfw.reference.ReferenceConstant;
import nc.uap.lfw.reference.action.RefOkDelegator;
import nc.uap.lfw.util.LfwClassUtil;

/**
 * 二级树参照处理事件
 * @author zhangxya
 *
 */
public class RefTwoTreeNodeListener extends TreeNodeServerListener {

	public RefTwoTreeNodeListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public void ondbclick(TreeNodeEvent e) {
		Dataset ds = getCurrentContext().getWidget().getViewModels().getDataset(ReferenceConstant.MASTER_DS_ID);
		String refNodeId = (String) getGlobalContext().getWebSession().getAttribute("refNodeId");
		String widgetId = (String) getGlobalContext().getWebSession().getAttribute("widgetId");
		PageMeta parentPm = getGlobalContext().getParentGlobalContext().getPageMeta();
		RefNode refnode = (RefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		IUifDeletator delegator = null;
		if(refnode != null && refnode.getRefnodeDelegator() != null && !refnode.getRefnodeDelegator().equals("")){
			delegator = (IUifDeletator) LfwClassUtil.newInstance( refnode.getRefnodeDelegator());
		}	
		else
			delegator = new RefOkDelegator(ds);
		delegator.setGlobalContext(getGlobalContext());
		delegator.execute();
	}

}
