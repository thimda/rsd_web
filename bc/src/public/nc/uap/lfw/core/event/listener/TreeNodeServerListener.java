package nc.uap.lfw.core.event.listener;

import nc.uap.lfw.core.event.TreeNodeDragEvent;
import nc.uap.lfw.core.event.TreeNodeEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

public abstract class TreeNodeServerListener extends AbstractServerListener {

	public TreeNodeServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	public void nodeCreated(TreeNodeEvent e) {};
	public void rootNodeCreated(TreeNodeEvent e) {};
	public void afterSelNodeChange(TreeNodeEvent e) {};
	public void beforeSelNodeChange(TreeNodeEvent e) {};
	public void onNodeLoad(TreeNodeEvent e) {};
	public void onChecked(TreeNodeEvent e) {};
	public void onclick(TreeNodeEvent e) {};
	public void ondbclick(TreeNodeEvent e) {};
	public void onDragStart(TreeNodeDragEvent e) {};
	public void onDragEnd(TreeNodeDragEvent e) {};
	public void beforeNodeCaptionChange(TreeNodeEvent e) {};
	
}
