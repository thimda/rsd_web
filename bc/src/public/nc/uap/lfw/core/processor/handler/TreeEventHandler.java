package nc.uap.lfw.core.processor.handler;

import java.awt.event.MouseListener;

import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.event.AbstractServerEvent;
import nc.uap.lfw.core.event.TreeNodeDragEvent;
import nc.uap.lfw.core.event.TreeNodeEvent;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.TreeContextMenuListener;
import nc.uap.lfw.core.event.conf.TreeNodeListener;
import nc.uap.lfw.core.event.conf.TreeRowListener;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.processor.AbstractEventHandler;

/**
 * @author guoweic
 *
 */
public class TreeEventHandler extends AbstractEventHandler<TreeViewComp> {
	
	public TreeEventHandler(LfwPageContext pageCtx) {
		super(pageCtx);
	}
	
	protected AbstractServerEvent<TreeViewComp> getServerEvent(String eventName, TreeViewComp tree) {
		JsListenerConf listener = getListenerConf();
		if (listener instanceof TreeNodeListener) {
			if (TreeNodeListener.ON_DRAG_START.equals(eventName)) {
				TreeNodeDragEvent serverEvent = new TreeNodeDragEvent(tree);
				String sourceNodeRowId = getPageCtx().getParameter("sourceNodeRowId");
				((TreeNodeDragEvent)serverEvent).setSourceNodeRowId(sourceNodeRowId);
				return serverEvent;
			} 
			else if (TreeNodeListener.ON_DRAG_END.equals(eventName)) {
				TreeNodeDragEvent serverEvent = new TreeNodeDragEvent(tree);
				String sourceNodeRowId = getPageCtx().getParameter("sourceNodeRowId");
				String targetNodeRowId = getPageCtx().getParameter("targetNodeRowId");
				((TreeNodeDragEvent)serverEvent).setSourceNodeRowId(sourceNodeRowId);
				((TreeNodeDragEvent)serverEvent).setTargetNodeRowId(targetNodeRowId);
				return serverEvent;
			} 
			else if (TreeNodeListener.ON_DBCLICK.equals(eventName) || TreeNodeListener.ON_CLICK.equals(eventName)) {
				TreeNodeEvent serverEvent = new TreeNodeEvent(tree);
				String nodeRowId = getPageCtx().getParameter("nodeRowId");
				((TreeNodeEvent)serverEvent).setNodeRowId(nodeRowId);
				return serverEvent;
			}
			else if (TreeNodeListener.ON_CHECKED.equals(eventName)) {
				TreeNodeEvent serverEvent = new TreeNodeEvent(tree);
				String nodeRowId = getPageCtx().getParameter("nodeRowId");
				((TreeNodeEvent)serverEvent).setNodeRowId(nodeRowId);
				return serverEvent;
			} else {
				TreeNodeEvent serverEvent = new TreeNodeEvent(tree);
				return serverEvent;
			}
		} 
		else if (listener instanceof TreeRowListener) {
			return super.getServerEvent(eventName, tree);
		} 
		else if (listener instanceof TreeContextMenuListener) {
			return super.getServerEvent(eventName, tree);
		} 
		else if (listener instanceof MouseListener) {
			return super.getServerEvent(eventName, tree);
		} 
		else
			throw new LfwRuntimeException("not implemented");
	}
	
	protected TreeViewComp getSource() {
		String sourceId = getPageCtx().getParameter(LfwPageContext.SOURCE_ID);
		TreeViewComp tree = (TreeViewComp) getPageCtx().getPageMeta().getWidget(getWidgetId()).getViewComponents().getComponent(sourceId);
		return tree;
	}

}
