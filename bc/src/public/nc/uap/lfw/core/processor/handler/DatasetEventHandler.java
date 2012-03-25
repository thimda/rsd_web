package nc.uap.lfw.core.processor.handler;

import nc.uap.lfw.core.common.DatasetConstant;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.ParameterSet;
import nc.uap.lfw.core.event.AbstractServerEvent;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetCellEvent;
import nc.uap.lfw.core.event.DatasetColSingleEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.RowInsertEvent;
import nc.uap.lfw.core.event.conf.DatasetListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.processor.AbstractEventHandler;

public class DatasetEventHandler extends AbstractEventHandler<Dataset> {
	public DatasetEventHandler(LfwPageContext pageCtx) {
		super(pageCtx);
	}

	@Override
	protected AbstractServerEvent<Dataset> getServerEvent(String eventName, Dataset ds) {
		JsListenerConf listener = getListenerConf();
		if(listener instanceof DatasetListener){
			if(eventName.equals(DatasetListener.ON_DATA_LOAD)){
				DataLoadEvent serverEvent = new DataLoadEvent(ds);
				ParameterSet params = ds.getReqParameters();
				String keyValue = params.getParameterValue(DatasetConstant.QUERY_PARAM_KEYVALUE);
//				if(keyValue == null)
//					throw new LfwRuntimeException("目标KeyValue为空");
				
				String currKey = ds.getCurrentKey();
				serverEvent.setOriginalKeyValue(currKey);
				if(currKey != null && !currKey.equals(""))
					serverEvent.setOriginalPageIndex(ds.getCurrentRowSet().getPaginationInfo().getPageIndex());
				if(keyValue != null)
					ds.setCurrentKey(keyValue);
				
				String pageIndexStr = params.getParameterValue(DatasetConstant.QUERY_PARAM_PAGEINDEX);
				int pageIndex = 0;
				if(pageIndexStr != null)
					pageIndex = Integer.parseInt(pageIndexStr);
				ds.getCurrentRowSet().getPaginationInfo().setPageIndex(pageIndex);
				
				return serverEvent;
			}
			else if(eventName.equals(DatasetListener.ON_AFTER_ROW_INSERT)){
				String insertIndex = getPageCtx().getParameter("row_insert_index");
				RowInsertEvent event = new RowInsertEvent(ds);
				event.setInsertedIndex(Integer.valueOf(insertIndex));
				return event;
			} else if (eventName.equals(DatasetListener.ON_AFTER_DATA_CHANGE) || eventName.equals(DatasetListener.ON_BEFORE_DATA_CHANGE)) {
				String colSingle = getPageCtx().getParameter("isColSingle");
				//如果不是列批量修改
				if (colSingle == null || colSingle.equalsIgnoreCase("false")){
					String rowIndex = getPageCtx().getParameter("cellRowIndex");
					String colIndex = getPageCtx().getParameter("cellColIndex");
					String newValue = getPageCtx().getParameter("newValue");
					String oldValue = getPageCtx().getParameter("oldValue");
					DatasetCellEvent event = new DatasetCellEvent(ds);
					event.setRowIndex(rowIndex == null ? -1 : Integer.parseInt(rowIndex));
					event.setColIndex(colIndex == null ? -1 : Integer.parseInt(colIndex));
					event.setNewValue(newValue);
					event.setOldValue(oldValue);
					return event;
				}
				else{
					//处理某一列批量修改
					String rowIndex = getPageCtx().getParameter("cellRowIndices");
					String colIndex = getPageCtx().getParameter("cellColIndex");
					String newValue = getPageCtx().getParameter("newValues");
					String oldValue = getPageCtx().getParameter("oldValues");
					DatasetCellEvent event = new DatasetColSingleEvent(ds);
					String[] strRowIndices = rowIndex.split(",");
					int[] rowIndices = new int[strRowIndices.length];
					for (int i = 0; i < strRowIndices.length; i++){
						rowIndices[i] = Integer.parseInt(strRowIndices[i]); 
					}
					((DatasetColSingleEvent)event).setRowIndices(rowIndices);
					event.setColIndex(colIndex == null ? -1 : Integer.parseInt(colIndex));
					//处理空值
					newValue = newValue.replaceAll(",,", ", ,");
					if (newValue.startsWith(","))
						newValue = " " + newValue;
					if (newValue.endsWith(","))
						newValue = newValue + " ";
					oldValue = oldValue.replaceAll(",,", ", ,");
					if (oldValue.startsWith(","))
						oldValue = " " + oldValue;
					if (oldValue.endsWith(","))
						oldValue = oldValue + " ";
					((DatasetColSingleEvent)event).setNewValues(newValue.split(","));
					((DatasetColSingleEvent)event).setOldValues(oldValue.split(","));
					return (DatasetCellEvent)event;
				}
			}
			else{
				DatasetEvent event = new DatasetEvent(ds);
				return event;
			}
		}
		else{
			return super.getServerEvent(eventName, ds);
		}
	}

	
	protected Dataset getSource(){
		String sourceId = getPageCtx().getParameter(LfwPageContext.SOURCE_ID);
		Dataset ds = getPageCtx().getPageMeta().getWidget(getWidgetId()).getViewModels().getDataset(sourceId);
		return ds;
	}
}
