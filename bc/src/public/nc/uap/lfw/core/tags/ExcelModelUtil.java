package nc.uap.lfw.core.tags;

import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.RenderTypeConst;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.ExcelColumn;
import nc.uap.lfw.core.comp.ExcelColumnGroup;
import nc.uap.lfw.core.comp.IExcelColumn;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.util.LanguageUtil;

/**
 * Excel模型工具
 * 
 * @author guoweic
 * 
 */
public class ExcelModelUtil {

	public static String generateExcelModel(Dataset ds,
			List<IExcelColumn> headers, LfwWidget widget) {
		StringBuffer buf = new StringBuffer();
		buf.append("var model = new ExcelCompModel();\n");
		Iterator<IExcelColumn> it = headers.iterator();
		while (it.hasNext()) {
			IExcelColumn header = (IExcelColumn) it.next();
			// 单表头
			buf.append(generalExcelHeader(ds, widget, header));
		}

		// if(excel.getRowFilter() != null){
		// buf.append("model.setRowFilter(")
		// .append(excel.getRowFilter())
		// .append(");\n");
		// }
		// else{
		// //尝试自动找到以excelid + Filter 命名的默认Filter
		// buf.append("if(typeof(")
		// .append(excel.getId() + "Filter")
		// .append(") != 'undefined')\n")
		// .append("model.setRowFilter(")
		// .append(excel.getId() + "Filter")
		// .append(");\n");
		// }

		buf.append("model.setDataSet(pageUI.getWidget('" + widget.getId())
				.append("').getDataset('" + ds.getId() + "'));\n");
		return buf.toString();
	}

	public static String generalExcelHeader(Dataset ds, LfwWidget widget, IExcelColumn header) {
		StringBuffer buf = new StringBuffer();
		if (header instanceof ExcelColumn) {
			ExcelColumn curHeader = (ExcelColumn) header;
			fillDataTypeAndEditorType(ds, curHeader);
			String label = null;

			String resId = curHeader.getI18nName();
			String text = curHeader.getText();
			// if(curHeader.getField() != null)
			label = getFieldI18nName(ds, resId, curHeader.getField(), text,
					curHeader.getLangDir());
			// 得到数据类型
			String dataType = curHeader.getDataType();
			if (dataType == null) {
				throw new LfwRuntimeException(
						"ExcelCompHeader must set DataType!");
			}
			// 得到编辑器类型
			String editorType = curHeader.getEditorType();
			// // 如果数据类型是"UFBOOLEAN"并且没有设置编辑类型,则编辑类型设置为"CHECKBOX"
			// if(editorType == null || "".equals(editorType))
			// {
			// if(curHeader.getDataType().equals(StringDataTypeConst.UFBOOLEAN))
			// editorType = EditorTypeConst.CHECKBOX;
			// else
			// editorType = EditorTypeConst.STRINGTEXT;
			// }
			// 得到渲染器
			String renderType = curHeader.getRenderType();
			// // 对于下拉框类型,如果renderType没有设置,需要根据editorType来填充renderType
			// if((renderType == null || "".equals(renderType)) &&
			// editorType.equals(EditorTypeConst.COMBODATA))
			// {
			// renderType = RenderTypeConst.ComboRender;
			// }
			// 根据数据类型设置textAlign
			String textAlign = curHeader.getTextAlign();
			if (textAlign == null) {
				if (dataType.equals(StringDataTypeConst.bOOLEAN)
						|| dataType.equals(StringDataTypeConst.BOOLEAN)
						|| dataType.equals(StringDataTypeConst.UFBOOLEAN))
					textAlign = "center";
				else if (dataType.equals(StringDataTypeConst.Decimal)
						|| dataType.equals(StringDataTypeConst.UFDOUBLE)
						|| dataType.equals(StringDataTypeConst.DATE)
						|| dataType.equals(StringDataTypeConst.INTEGER))
					textAlign = "right";
				else
					textAlign = "left";
			}

			int headerWidth = curHeader.getWidth();
			if (headerWidth == -1)
				headerWidth = ExcelColumn.DEFAULT_WIDTH;
			// keyName, showName, width, dataType, sortable, isHidden,
			// columEditable, defaultValue, columBgColor, textAlign,
			// textColor, isFixedHeader, renderType, editorType, topHeader,
			// groupHeader, isGroupHeader, isSumCol
			buf
					.append(
							"var " + curHeader.getId()
									+ " = new ExcelCompHeader('"
									+ curHeader.getField() + "','" + label
									+ "','" + headerWidth + "','"
									+ dataType + "',")
					.append(
							curHeader.isSortable() + ","
									+ (!curHeader.isVisible()) + ","
									+ curHeader.isEditable() + ",'','")
					.append(
							(curHeader.getColumBgColor() == null ? ""
									: curHeader.getColumBgColor())
									+ "','")
					.append(textAlign + "','")
					.append(
							(curHeader.getTextColor() == null ? ""
									: curHeader.getTextColor())
									+ "',"
									+ curHeader.isFixedHeader()
									+ ","
									+ renderType
									+ ",'"
									+ editorType
									+ "',null,null,null,"
									+ curHeader.isSumCol()
									+ ","
									+ curHeader.isAutoExpand() 
									+","
									+ curHeader.isRequired()
									+ ");\n")
					.append("model.addHeader(" + curHeader.getId() + ");\n");

			if (curHeader.getExtendAttribute("showState") != null) {
				buf.append(curHeader.getId()).append(".setShowState('")
						.append(curHeader.getExtendAttribute("showState"))
						.append("');\n");
			}

			// 设置是否必输项
			if (curHeader.isRequired()) {
				buf.append(curHeader.getId()).append(
						".setRequired(true);\n");
			}
			// 如果是组合数据类型，生成相应的脚本
			if (curHeader.getEditorType() != null
					&& curHeader.getEditorType().equals(
							EditorTypeConst.COMBODATA))
				generateComboDataScript(widget, buf, curHeader);
			// 如果是参照类型，生成相应的脚本
			else if (curHeader.getEditorType() != null
					&& curHeader.getEditorType().equals(
							EditorTypeConst.REFERENCE))
				generateRefereceScript(widget, buf, curHeader);
			// 如果是选择框类型，生成相应的脚本
			else if (curHeader.getEditorType() != null
					&& curHeader.getEditorType().equals(
							EditorTypeConst.CHECKBOX))
				generateCheckBoxScript(widget, buf, curHeader);
			// 如果是整型类型，生成相应的脚本
			else if (curHeader.getEditorType() != null
					&& curHeader.getEditorType().equals(
							EditorTypeConst.INTEGERTEXT))
				generateIntegerTextScript(ds, buf, curHeader);
			// 如果是浮点类型，生成相应的脚本
			else if (curHeader.getEditorType() != null
					&& curHeader.getEditorType().equals(
							EditorTypeConst.DECIMALTEXT))
				generateDecimalTextScript(ds, buf, curHeader);
			// 如果是字符类型,生成相应的脚本
			else if (curHeader.getEditorType() != null
					&& curHeader.getEditorType().equals(
							EditorTypeConst.STRINGTEXT))
				generateStringTextScript(buf, curHeader);
		}
		// 多表头
		else if (header instanceof ExcelColumnGroup) {
			ExcelColumnGroup curHeader = (ExcelColumnGroup) header;
			makeHeaders(widget, buf, curHeader, curHeader, ds);
		}
		return buf.toString();
	}

	private static void generateCheckBoxScript(LfwWidget widget,
			StringBuffer buf, ExcelColumn curHeader) {
		StaticComboData data = (StaticComboData) widget.getViewModels()
				.getComboData(curHeader.getRefComboData());
		if (data != null) {
			CombItem[] items = data.getAllCombItems();
			if (items == null || items.length != 2) {
				throw new LfwRuntimeException(
						"The Combodata is not suitable for header:"
								+ curHeader.getId());
			}
			buf.append(curHeader.getId()).append(".setValuePair([\"").append(
					items[0].getValue()).append("\",\"").append(
					items[1].getValue()).append("\"]").append(");\n");
		} else {
			if (curHeader.getDataType().equals(StringDataTypeConst.UFBOOLEAN)) {
				buf.append(curHeader.getId()).append(
						".setValuePair([\"Y\",\"N\"]);\n");
			} else if (curHeader.getDataType().equals(
					StringDataTypeConst.bOOLEAN)
					|| curHeader.getDataType().equals(
							StringDataTypeConst.BOOLEAN)) {
				buf.append(curHeader.getId()).append(
						".setValuePair([\"true\",\"false\"]);\n");
			}
		}
	}

	private static void generateRefereceScript(LfwWidget widget,
			StringBuffer buf, ExcelColumn curHeader) {
		IRefNode refNode = widget.getViewModels().getRefNode(
				curHeader.getRefNode());

		if (refNode != null) {
			String refId = WebElementTag.RF_PRE + widget.getId() + "_"
					+ refNode.getId();
			buf.append(curHeader.getId()).append(".setNodeInfo(").append(refId)
					.append(");\n");
		}

	}

	/**
	 * 根据dataset中的数据类型填充ExcelColumn的dataType，并且获得对应的EditorType
	 * 
	 * @param ds
	 * @param col
	 * @return
	 */
	private static void fillDataTypeAndEditorType(Dataset ds, ExcelColumn col) {
		if (col.getDataType() == null || col.getDataType().trim().equals("")) {
			Field field = ds.getFieldSet().getField(col.getId());
			if (field != null)
				col.setDataType(field.getDataType());
			else
				col.setDataType(StringDataTypeConst.STRING);
		}
		if (col.getEditorType() == null
				|| col.getEditorType().trim().equals(""))
			col.setEditorType(EditorTypeConst.getEditorTypeByString(col
					.getDataType()));
		if (col.getRenderType() == null
				|| col.getRenderType().trim().equals(""))
			col.setRenderType(RenderTypeConst.getRenderTypeByString(col
					.getDataType()));
	}

	/**
	 * 为组合数据类型生成相关数据脚本
	 * 
	 * @param buf
	 * @param curHeader
	 */
	private static void generateComboDataScript(LfwWidget widget,
			StringBuffer buf, ExcelColumn curHeader) {
		if (curHeader.isImageOnly()) {
			buf.append(curHeader.getId()).append(".setShowImgOnly(true);\n");
		}
		// buf.append(curHeader.getId())
		// .append(".setHeaderComboBoxCaptionValues(")
		// .append(COMBO_PRE + getCurrWidget().getId() + "_")
		// .append(curHeader.getRefComboData())
		// .append(".getNameArray());\n");
		// buf.append(curHeader.getId())
		// .append(".setHeaderComboBoxKeyValues(")
		// .append(COMBO_PRE + getCurrWidget().getId() + "_")
		// .append(curHeader.getRefComboData())
		// .append(".getValueArray());\n");
		buf.append(curHeader.getId()).append(".setHeaderComboBoxComboData(")
				.append(WebElementTag.COMBO_PRE + widget.getId() + "_").append(
						curHeader.getRefComboData()).append(");\n");
	}

	private static void generateStringTextScript(StringBuffer buf,
			ExcelColumn curHeader) {
		String maxLength = curHeader.getMaxLength();
		// if(maxLength == null || "".equals(maxLength))
		// maxLength = getFieldProperty(curHeader.getField(),
		// DatasetConstant.MAX_LENGTH);

		if (maxLength != null && !"".equals(maxLength)) {
			buf.append(curHeader.getId()).append(".setMaxLength(").append(
					maxLength).append(");\n");
		}
	}

	private static void generateDecimalTextScript(Dataset ds, StringBuffer buf,
			ExcelColumn curHeader) {
		String precision = curHeader.getPrecision();
		if (precision == null || "".equals(precision))
			precision = getFieldProperty(ds, curHeader.getField(),
					Field.PRECISION);

		if (precision != null && !"".equals(precision)) {
			buf.append(curHeader.getId()).append(".setPrecision(").append(
					precision).append(");\n");
		}

		String maxFloatValue = curHeader.getMaxValue();
		String minFloatValue = curHeader.getMinValue();
		if (maxFloatValue == null || "".equals(maxFloatValue))
			maxFloatValue = getFieldProperty(ds, curHeader.getField(),
					Field.MAX_VALUE);
		if (minFloatValue == null || "".equals(minFloatValue))
			minFloatValue = getFieldProperty(ds, curHeader.getField(),
					Field.MIN_VALUE);
		if (minFloatValue != null && !"".equals(minFloatValue)) {
			buf.append(curHeader.getId()).append(".setFloatMinValue(").append(
					minFloatValue).append(");\n");
		}

		if (maxFloatValue != null && !"".equals(maxFloatValue)) {
			buf.append(curHeader.getId()).append(".setFloatMaxValue(").append(
					maxFloatValue).append(");\n");
		}
	}

	private static void generateIntegerTextScript(Dataset ds, StringBuffer buf,
			ExcelColumn curHeader) {
		// 首先获取控件的设置属性
		String maxValue = curHeader.getMaxValue();
		String minValue = curHeader.getMinValue();
		// 控件属性没有设置则获取相应的ds field的设置
		if (maxValue == null || "".equals(maxValue))
			maxValue = getFieldProperty(ds, curHeader.getField(),
					Field.MAX_VALUE);
		if (minValue == null || "".equals(minValue))
			minValue = getFieldProperty(ds, curHeader.getField(),
					Field.MIN_VALUE);

		if (minValue != null && !"".equals(minValue)) {
			buf.append(curHeader.getId()).append(".setIntegerMinValue(")
					.append(minValue).append(");\n");
		}
		if (maxValue != null && !"".equals(maxValue)) {
			buf.append(curHeader.getId()).append(".setIntegerMaxValue(")
					.append(maxValue).append(");\n");
		}
	}

	private static String getFieldProperty(Dataset ds, String fieldId,
			String name) {
		Field field = ds.getFieldSet().getField(fieldId);
		if (field == null)
			return null;
		return (String) field.getExtendAttributeValue(name);
	}

	/**
	 * 递归构建多表头
	 * 
	 * @param buf
	 * @param header
	 * @param topHeader
	 *            此多表头的最顶层header
	 */
	private static void makeHeaders(LfwWidget widget, StringBuffer buf,
			IExcelColumn header, ExcelColumnGroup topHeader, Dataset ds) {
		if (header instanceof ExcelColumnGroup) {
			ExcelColumnGroup parentHeader = (ExcelColumnGroup) header;
			if (parentHeader == topHeader) {
				// 注意此处的visible要传入!visible
				buf
						.append(
								"var "
										+ parentHeader.getId()
										+ "= new ExcelCompHeader('"
										+ parentHeader.getId()
										+ "','"
										+ parentHeader.getI18nName()
										+ "','','','',"
										+ (!parentHeader.isVisible())
										+ ",'','','','','','','','',null, null, true);\n")
						.append(
								"model.addHeader(" + parentHeader.getId()
										+ ");\n");
				if (parentHeader.getExtendAttribute("showState") != null) {
					buf.append(parentHeader.getId()).append(".setShowState('")
							.append(
									parentHeader
											.getExtendAttribute("showState"))
							.append("');\n");
				}
			}

			// 得到此组的所有孩子
			List<IExcelColumn> list = parentHeader.getChildColumnList();
			// 记录孩子中是"组"的那个,为继续递归做准备
			IExcelColumn groupHeader = null;
			if (list != null && list.size() > 0) {
				Iterator<IExcelColumn> it = list.iterator();
				while (it.hasNext()) {
					IExcelColumn curHeader = it.next();
					if (curHeader instanceof ExcelColumnGroup) {
						ExcelColumnGroup tempHeader = (ExcelColumnGroup) curHeader;
						// 注意此处的visible要传入!visible
						buf.append("var " + tempHeader.getId()
								+ "= new ExcelCompHeader('"
								+ tempHeader.getId() + "','"
								+ tempHeader.getI18nName() + "','','','',"
								+ (!tempHeader.isVisible())
								+ ",'','','','','','','','',"
								+ topHeader.getId() + ","
								+ parentHeader.getId() + ",true);\n");
						groupHeader = tempHeader;
						if (tempHeader.getExtendAttribute("showState") != null) {
							buf.append(tempHeader.getId()).append(
									".setShowState('").append(
									tempHeader.getExtendAttribute("showState"))
									.append("');\n");
						}
					} else if (curHeader instanceof ExcelColumn) {
						ExcelColumn realHeader = (ExcelColumn) curHeader;
						fillDataTypeAndEditorType(ds, realHeader);
						// 得到数据类型
						String dataType = realHeader.getDataType();

						// 得到编辑器类型
						String editorType = realHeader.getEditorType();
						// 如果数据类型是"UFBOOLEAN"并且没有设置编辑类型,则编辑类型设置为"CHECKBOX"
						if (editorType == null || "".equals(editorType)) {
							if (realHeader.getDataType().equals(
									StringDataTypeConst.UFBOOLEAN))
								editorType = EditorTypeConst.CHECKBOX;
							else
								editorType = "";
						}
						// 得到渲染器
						String renderType = realHeader.getRenderType();
						// 对于下拉框类型,如果renderType没有设置,需要根据editorType来填充renderType
						if ((renderType == null || "".equals(renderType))
								&& editorType.equals(EditorTypeConst.COMBODATA)) {
							renderType = RenderTypeConst.ComboRender;
						}

						String resId = realHeader.getI18nName();
						String text = realHeader.getText();
						String label = getFieldI18nName(ds, resId, realHeader
								.getField(), text, realHeader.getLangDir());

						int headerWidth = realHeader.getWidth();
						if (headerWidth == -1)
							headerWidth = ExcelColumn.DEFAULT_WIDTH;
						buf.append(
								"var " + realHeader.getId()
										+ " = new ExcelCompHeader('"
										+ realHeader.getField() + "','" + label
										+ "','" + headerWidth + "','"
										+ dataType + "',").append(
								realHeader.isSortable() + ","
										+ (!realHeader.isVisible()) + ","
										+ realHeader.isEditable()
										+ ",'','"
										+(realHeader.getColumBgColor()==null?"":realHeader.getColumBgColor())
										+"','center','',"
										+ realHeader.isFixedHeader() + ","
										+ renderType + ",'" + editorType + "',"
										+ topHeader.getId() + ","
										+ parentHeader.getId() + ",true,"
										+ realHeader.isSumCol() 
										+ ","
										+ realHeader.isAutoExpand() 
										+","
										+ realHeader.isRequired()
										+");\n");
						if (realHeader.getExtendAttribute("showState") != null) {
							buf.append(realHeader.getId()).append(
									".setShowState('").append(
									realHeader.getExtendAttribute("showState"))
									.append("');\n");
						}

						// 如果是组合数据类型，生成相应的脚本
						if (realHeader.getEditorType() != null
								&& realHeader.getEditorType().equals(
										EditorTypeConst.COMBODATA))
							generateComboDataScript(widget, buf, realHeader);
						// 如果是参照类型，生成相应的脚本
						else if (realHeader.getEditorType() != null
								&& realHeader.getEditorType().equals(
										EditorTypeConst.REFERENCE))
							generateRefereceScript(widget, buf, realHeader);
						// 如果是整型类型，生成相应的脚本
						else if (realHeader.getEditorType() != null
								&& realHeader.getEditorType().equals(
										EditorTypeConst.INTEGERTEXT))
							generateIntegerTextScript(ds, buf, realHeader);
						// 如果是浮点类型，生成相应的脚本
						else if (realHeader.getEditorType() != null
								&& realHeader.getEditorType().equals(
										EditorTypeConst.DECIMALTEXT))
							generateDecimalTextScript(ds, buf, realHeader);
						else if (realHeader.getEditorType() != null
								&& realHeader.getEditorType().equals(
										EditorTypeConst.STRINGTEXT))
							generateStringTextScript(buf, realHeader);
					}
				}
				// 递归处理仍为ExcelColumnGroup类型的组header
				if (groupHeader != null)
					makeHeaders(widget, buf, groupHeader, topHeader, ds);
			}
		}
	}

	protected static String getFieldI18nName(Dataset ds, String i18nName,
			String fieldId, String defaultI18nName, String langDir) {
		if (i18nName != null && !i18nName.equals("")) {
			if (i18nName.equals("$NULL$"))
				return "";
			return translate(i18nName, defaultI18nName == null ? i18nName
					: defaultI18nName, langDir);
		}
		// Dataset ds = getDataset();
		if (ds == null)
			return defaultI18nName;

		if (fieldId != null) {
			int fldIndex = ds.getFieldSet().nameToIndex(fieldId);
			if (fldIndex == -1)
				throw new LfwRuntimeException("can not find the field:"
						+ fieldId + ",dataset:" + ds.getId());
			Field field = ds.getFieldSet().getField(fldIndex);
			i18nName = field.getI18nName();
			String text = field.getText();
			String defaultValue = text == null ? i18nName : text;
			if (i18nName == null || i18nName.equals(""))
				return defaultI18nName == null ? defaultValue : defaultI18nName;
			else {
				return translate(i18nName,
						defaultI18nName == null ? defaultValue
								: defaultI18nName, langDir);
			}
		} else
			return defaultI18nName;
	}

	/**
	 * 进行多语翻译,如果不能翻译,返回原i18nName
	 * 
	 * @param i18nName
	 * @return
	 */
	private static String translate(String i18nName, String defaultI18nName,
			String langDir) {
		if (i18nName == null && defaultI18nName == null)
			return "";
		// return defaultI18nName;
		// return i18nName;
		if (langDir == null)
			langDir = LfwRuntimeEnvironment.getLangDir();
		return LanguageUtil.getWithDefaultByProductCode(langDir,
				defaultI18nName, i18nName);
	}
}
