package nc.uap.lfw.core.cmd.util;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.model.plug.TranslatedRow;

/**
 * Plugout输出row转换成目标Dataset row 翻译类
 *
 */
public final class UifRowTranslator {
	/**
	 * plugout 输出行转换成目标Dataset
	 * @param ds 目标dataset
	 * @param transRow
	 * @return
	 */
	public static Row translateRowToRow(Dataset ds, TranslatedRow transRow){
		Row row = ds.getEmptyRow();
		String[] keys = transRow.getKeys();
		for (int i = 0; i < keys.length; i++) {
			row.setValue(ds.nameToIndex(keys[i]), transRow.getValue(keys[i]));
		}
		return row;
	}
	
	public static Row translateRowToRow(Dataset ds, Row row, TranslatedRow transRow){
		String[] keys = transRow.getKeys();
		for (int i = 0; i < keys.length; i++) {
			row.setValue(ds.nameToIndex(keys[i]), transRow.getValue(keys[i]));
		}
		return row;
	}
	
	public static Row translateRowToRow(Dataset ds, Row transRow){
		return (Row) transRow.clone();
	}
}
