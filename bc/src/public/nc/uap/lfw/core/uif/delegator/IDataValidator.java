package nc.uap.lfw.core.uif.delegator;

import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.ExcelComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.page.LfwWidget;

public interface IDataValidator {

	/**
	 * 验证片段
	 * 
	 * @param ds
	 * @param widget
	 */
	public void validate(Dataset ds, LfwWidget widget);

	/**
	 * 验证From
	 * 
	 * @param ds
	 * @param formcomp
	 */
	public void validate(Dataset ds, FormComp formcomp);

	/**
	 * 验证Grid
	 * 
	 * @param ds
	 * @param gridcomp
	 */
	public void validate(Dataset ds, GridComp gridcomp);

	/**
	 * 验证Excel
	 * 
	 * @param ds
	 * @param excelcomp
	 */
	public void validate(Dataset ds, ExcelComp excelcomp);
}
