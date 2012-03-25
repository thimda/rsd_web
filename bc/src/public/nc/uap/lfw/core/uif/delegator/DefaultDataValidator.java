package nc.uap.lfw.core.uif.delegator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.ExcelColumn;
import nc.uap.lfw.core.comp.ExcelComp;
import nc.uap.lfw.core.comp.IExcelColumn;
import nc.uap.lfw.core.comp.IDataBinding;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.EmptyRow;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowData;
import nc.uap.lfw.core.exception.LfwValidateException;
import nc.uap.lfw.core.model.formular.DefaultEditFormularService;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.util.Validator;

import org.apache.commons.lang.StringUtils;
/**
 * 默认验证实现
 * @author guoweic
 *
 */
public class DefaultDataValidator implements IDataValidator {

	private void validate(Dataset ds) {

		StringBuffer errorContent = new StringBuffer();
		FieldSet fs = ds.getFieldSet();
		int count = fs.getFieldCount();
		RowData rd = ds.getCurrentRowData();
		if (rd == null)
			return;
		Row[] rows = ds.getCurrentRowData().getRows();
		if (rows == null || rows.length == 0)
			return;
		
		//保存时校验验证公式
		HashMap valueMap = new HashMap();
		HashMap dataTypeMap = new HashMap();
		Field[] fields = ds.getFieldSet().getFields();
		DefaultEditFormularService editFormular = new DefaultEditFormularService();
		for (int j = 0; j < rows.length; j++) {
			Row row = rows[j];
			for (int k = 0; k < fields.length; k++) {
				Field fr = fields[k];
				Object value = row.getValue(k); 
				valueMap.put(fr.getId(), value == null?null:value.toString());
				dataTypeMap.put(fr.getId(), fr.getDataType());
				if(fr.getValidateFormula() != null){
					Map map = editFormular.executeFormular(valueMap, fr.getValidateFormula(), dataTypeMap);
					if(map != null){
						String forumValue = (String) map.get("formular_value");
						if(forumValue != null && !forumValue.equals("$NULL$"))
							errorContent.append(forumValue + "\r\n<br>");
					}
				}
			}
			
		}
			
		
		for (int i = 0; i < count; i++) {
			
			Field field = fs.getField(i);
				for (int j = 0; j < rows.length; j++) {
					Row row = rows[j];
					if (!(row instanceof EmptyRow)) {
						Object value = row.getValue(i); 
						if ( !field.isPrimaryKey()) {
						if (!field.isNullAble() && (!field.getId().endsWith("_mc"))&&( value == null || value.equals(""))) {
							// 行" + (j + 1) + "," +
							errorContent.append("[" + field.getText()
									+ "]:数据不能为空\r\n<br>");
						}
						String val=value==null?null:value.toString();
						errorContent.append(Validator.valid(field, val));
					}
				}
			}
		}
		if (StringUtils.isNotEmpty(errorContent.toString()))
			throw new LfwValidateException(errorContent.toString());
	}
	/**
	 * 验证片段
	 */
	public void validate(Dataset ds, LfwWidget widget) {
		//如果传入的片段为空 验证数据集
		if (widget == null||widget.getId()==null){
			validate(ds);
			return;
		}
		//否则以form和grid中设置的为准
		WebComponent[] comps = widget.getViewComponents().getComponents();
		if (comps != null && comps.length > 0) {
			for (WebComponent comp : comps) {
				if (comp instanceof IDataBinding) {
					IDataBinding dbcomp = (IDataBinding) comp;
					if (ds.getId().equals(dbcomp.getDataset())) {
						validate(ds, dbcomp);
					}
				}
			}
		}
	}
	/**
	 * 验证数据绑定组件如form grid
	 * @param ds
	 * @param compment
	 */
	private void validate(Dataset ds, IDataBinding compment) {
		if (compment instanceof FormComp) {
			validate(ds, (FormComp) compment);
		} else if (compment instanceof GridComp) {
			validate(ds, (GridComp) compment);
		}
	}

	/**
	 * Form验证
	 * 
	 * @param ds
	 * @param formcomp
	 */
	public void validate(Dataset ds, FormComp formcomp) {
		StringBuffer errorContent = new StringBuffer();
		List<FormElement> eles = formcomp.getElementList();
		RowData rd = ds.getCurrentRowData();
		if (rd == null)
			return;
		Row[] rows = rd.getRows();
		if (rows == null || rows.length == 0)
			return;
		if (eles != null && !eles.isEmpty()) {
			for (FormElement ele : eles) {
				for (Row row : rows) {
					if (!(row instanceof EmptyRow)) {
						int i = ds.nameToIndex(ele.getId());
						Field field = ds.getFieldSet().getField(i);
						Object value = row.getValue(i);
						String val=value==null?null:value.toString();
						errorContent.append(Validator.valid(field, val));
						if (!ele.isNullAble()) {
							if (value == null || value.equals("")) {
								errorContent.append("["
										+ StringUtils.defaultIfEmpty(ele
												.getText(), ele.getI18nName())
										+ "]:数据不能为空\r\n<br>");
							}
						}
					}
				}
			}
		}
		if (StringUtils.isNotEmpty(errorContent.toString()))
			throw new LfwValidateException(errorContent.toString());
	}

	/**
	 * 表格数据验证
	 * 
	 * @param ds
	 * @param gridcomp
	 */
	public void validate(Dataset ds, GridComp gridcomp) {
		StringBuffer errorContent = new StringBuffer();
		List<IGridColumn> eles = gridcomp.getColumnList();
		RowData rd = ds.getCurrentRowData();
		if (rd == null)
			return;
		Row[] rows = rd.getRows();
		if (rows == null || rows.length == 0)
			return;
		if (eles != null && !eles.isEmpty()) {
			for (IGridColumn ele : eles) {
				if (ele instanceof GridColumn) {
					GridColumn col = (GridColumn) ele;
						for (Row row : rows) {
							if (!(row instanceof EmptyRow)) {
								int i = ds.nameToIndex(col.getId());
								Object value = row.getValue(i);
								Field field = ds.getFieldSet().getField(i);
								String val=value==null?null:value.toString();
								errorContent.append(Validator.valid(field, val));
								if (!col.isNullAble()) {
								if (value == null || value.equals("")) {
									errorContent.append("["
											+ StringUtils.defaultIfEmpty(col
													.getText(), col
													.getI18nName())
											+ "]:数据不能为空\r\n<br>");
								}
							}
						}
					}
				}
			}
		}
		if (StringUtils.isNotEmpty(errorContent.toString()))
			throw new LfwValidateException(errorContent.toString());
	}

	/**
	 * 表格数据验证
	 * 
	 * @param ds
	 * @param excelcomp
	 */
	public void validate(Dataset ds, ExcelComp excelcomp) {
		StringBuffer errorContent = new StringBuffer();
		List<IExcelColumn> eles = excelcomp.getColumnList();
		RowData rd = ds.getCurrentRowData();
		if (rd == null)
			return;
		Row[] rows = rd.getRows();
		if (rows == null || rows.length == 0)
			return;
		if (eles != null && !eles.isEmpty()) {
			for (IExcelColumn ele : eles) {
				if (ele instanceof ExcelColumn) {
					ExcelColumn col = (ExcelColumn) ele;
						for (Row row : rows) {
							if (!(row instanceof EmptyRow)) {
								int i = ds.nameToIndex(col.getId());
								Object value = row.getValue(i);
								Field field = ds.getFieldSet().getField(i);
								String val=value==null?null:value.toString();
								errorContent.append(Validator.valid(field, val));
								if (!col.isNullAble()) {
								if (value == null || value.equals("")) {
									errorContent.append("["
											+ StringUtils.defaultIfEmpty(col
													.getText(), col
													.getI18nName())
											+ "]:数据不能为空\r\n<br>");
								}
							}
						}
					}
				}
			}
		}
		if (StringUtils.isNotEmpty(errorContent.toString()))
			throw new LfwValidateException(errorContent.toString());
	}

}

