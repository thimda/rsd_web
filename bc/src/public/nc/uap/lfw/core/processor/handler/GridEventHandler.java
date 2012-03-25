package nc.uap.lfw.core.processor.handler;

import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.event.AbstractServerEvent;
import nc.uap.lfw.core.event.CellEvent;
import nc.uap.lfw.core.event.GridRowEvent;
import nc.uap.lfw.core.event.conf.GridCellListener;
import nc.uap.lfw.core.event.conf.GridListener;
import nc.uap.lfw.core.event.conf.GridRowListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.processor.AbstractEventHandler;

/**
 * @author guoweic
 *
 */
public class GridEventHandler extends AbstractEventHandler<GridComp> {

	public GridEventHandler(LfwPageContext pageCtx) {
		super(pageCtx);
	}

	protected GridComp getSource() {
		String sourceId = getPageCtx().getParameter(LfwPageContext.SOURCE_ID);
		GridComp grid = (GridComp) getPageCtx().getPageMeta().getWidget(getWidgetId()).getViewComponents().getComponent(sourceId);
		return grid;
	}

	protected AbstractServerEvent<GridComp> getServerEvent(String eventName, GridComp grid) {
		JsListenerConf listener = getListenerConf();
		if (listener instanceof GridCellListener) {
			if(GridCellListener.AFTER_EDIT.equals(eventName) ) {
				CellEvent serverEvent = new CellEvent(grid);
				String rowIndex = getPageCtx().getParameter("rowIndex");
				String colIndex = getPageCtx().getParameter("colIndex");
				String newValue = getPageCtx().getParameter("newValue");
				String oldValue = getPageCtx().getParameter("oldValue");
				if(rowIndex != null && !rowIndex.equals(""))
					serverEvent.setRowIndex(Integer.parseInt(rowIndex));
				if(colIndex != null && !rowIndex.equals(""))
					serverEvent.setColIndex(Integer.parseInt(colIndex));
				
				serverEvent.setNewValue(newValue);
				serverEvent.setOldValue(oldValue);
				return serverEvent;
			}
			else{
				CellEvent serverEvent = new CellEvent(grid);
				String rowIndex = getPageCtx().getParameter("rowIndex");
				String colIndex = getPageCtx().getParameter("colIndex");
				if(rowIndex != null)
					serverEvent.setRowIndex(Integer.parseInt(rowIndex));
				if(colIndex != null)
					serverEvent.setColIndex(Integer.parseInt(colIndex));
				return serverEvent;
			}
			
			//return super.getServerEvent(eventName, grid);
		} else if (listener instanceof GridListener) {
			return super.getServerEvent(eventName, grid);
		} else if (listener instanceof GridRowListener) {
			GridRowEvent rowEvent = new GridRowEvent(grid);
			return rowEvent;
		} else if (listener instanceof MouseListener) {
			return super.getServerEvent(eventName, grid);
		} else
			throw new LfwRuntimeException("not implemented");
	}

}
