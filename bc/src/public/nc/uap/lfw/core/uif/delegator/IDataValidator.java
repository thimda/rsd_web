package nc.uap.lfw.core.uif.delegator;

import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.ExcelComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.page.LfwWidget;

public interface IDataValidator {

	/**
	 * ��֤Ƭ��
	 * 
	 * @param ds
	 * @param widget
	 */
	public void validate(Dataset ds, LfwWidget widget);

	/**
	 * ��֤From
	 * 
	 * @param ds
	 * @param formcomp
	 */
	public void validate(Dataset ds, FormComp formcomp);

	/**
	 * ��֤Grid
	 * 
	 * @param ds
	 * @param gridcomp
	 */
	public void validate(Dataset ds, GridComp gridcomp);

	/**
	 * ��֤Excel
	 * 
	 * @param ds
	 * @param excelcomp
	 */
	public void validate(Dataset ds, ExcelComp excelcomp);
}
