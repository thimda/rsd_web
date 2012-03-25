package nc.uap.lfw.core.uif.delegator;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowData;
import nc.ui.pub.print.IDataSource;

public class PrintDatasourceForMasterSlave implements IDataSource {

	private static final long serialVersionUID = 1L;

	protected Dataset masterDs = null;
	
	protected Dataset slaveDs = null;
	 
	
	public void setDatasets(Dataset masterDs, Dataset slaveDs){
		this.masterDs = masterDs;
		this.slaveDs = slaveDs;
	}
	
	public String[] getAllDataItemExpress() {
		return null;
	}

	public String[] getAllDataItemNames() {
		return null;
	}

	public String[] getDependentItemExpressByExpress(String itemExpress) {
		return null;
	}

	public String[] getItemValuesByExpress(String itemExpress) {
		String attrName = itemExpress;
		Dataset currDs = null;
		// 以"h_"或者"t_"开头表示是表头或者表尾的数据源,否则是表体的数据源
		if(itemExpress.startsWith("h_") || itemExpress.startsWith("t_")) {
			attrName = attrName.substring(2);
			currDs = this.masterDs;
		} 
		else if(itemExpress.startsWith("b_")) {
			attrName = attrName.substring(2);
			currDs = this.slaveDs;
		}
		else{
			currDs = this.slaveDs;
		}
		if(currDs != null)
		{
			RowData rowData = currDs.getCurrentRowData();
			String[] valueArry = new String[rowData.getRowCount()];
			for(int i = 0; i < valueArry.length; i++)
			{
				Row row = rowData.getRow(i);
				int fieldIndex = getFieldIndex(currDs, attrName);
				if(fieldIndex == -1){
					Logger.warn("can not find field:" + attrName + ",in ds:" + currDs.getId());
					return null;
				}
				Object value = row.getValue(fieldIndex);
				if(value != null){
					Field field = currDs.getFieldSet().getField(fieldIndex);
					Object newValue = processValue(field, attrName, value);
					valueArry[i] = newValue.toString();
				}
			}
			return valueArry;
		}
		return null;
	}
	
	protected int getFieldIndex(Dataset ds, String attrName){
		return ds.getFieldSet().nameToIndex(attrName);
	}

	protected Object processValue(Field field, String attrName, Object value) {
		return value;
	}
	public String getModuleName() {
		return null;
	}

	public boolean isNumber(String itemExpress) {
		return false;
	}

}
