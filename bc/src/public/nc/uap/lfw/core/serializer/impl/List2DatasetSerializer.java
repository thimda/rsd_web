package nc.uap.lfw.core.serializer.impl;

import java.util.List;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowData;
import nc.uap.lfw.core.data.RowSet;

/**
 * Vector到ds的序列化器
 * @author gd 2010-2-24
 * @version NC6.0
 * @since NC6.0
 */
public class List2DatasetSerializer{

	public void serialize(String keyValue, PaginationInfo pInfo, List<List<Object>> v, Dataset ds) {
		if(v != null)
		{
			RowSet rowSet = ds.getRowSet(keyValue);
			if(rowSet == null){
				rowSet = new RowSet(keyValue);
				ds.addRowSet(keyValue, rowSet);
			}
			if(pInfo != null)
				rowSet.setPaginationInfo(pInfo);
			rowSet.setRowSetChanged(true);
			RowData rowData = rowSet.getCurrentRowData(true);
			rowData.clear();
			for(int i = 0; i < v.size(); i++)
			{
				Row row = ds.getEmptyRow();
				List<Object> data = v.get(i);
				rowData.addRow(row);
				for (int j = 0; j < ds.getFieldSet().getFieldCount(); j++) {
					row.setValue(j, data.get(j));
				}
			}
		}
	}
}
