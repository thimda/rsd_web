package nc.uap.lfw.core.tags;

import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.RenderTypeConst;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridColumnGroup;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.GridTreeLevel;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.ra.render.UIRender;
import nc.uap.lfw.util.LanguageUtil;
import nc.uap.lfw.util.StringUtil;

public class GridModelUtil {

	public static String generateGridModel(Dataset ds, GridComp grid, LfwWidget widget) {
		List<IGridColumn> headers = grid.getColumnList();
		StringBuffer buf = new StringBuffer();
		buf.append("var model = new GridCompModel();\n");
		if(headers == null)
			return buf.toString();
		Iterator<IGridColumn> it = headers.iterator();
		while(it.hasNext())
		{
			IGridColumn header = (IGridColumn)it.next();
			buf.append(generateGridColumn(header, ds, widget, "model"));
		}
		
//			if(grid.getRowFilter() != null){
//				buf.append("model.setRowFilter(")
//				   .append(grid.getRowFilter())
//				   .append(");\n");
//			}
//			else{
//				//�����Զ��ҵ���gridid + Filter ������Ĭ��Filter
//				buf.append("if(typeof(")
//				   .append(grid.getId() + "Filter")
//				   .append(") != 'undefined')\n")
//				   .append("model.setRowFilter(")
//				   .append(grid.getId() + "Filter")
//				   .append(");\n");
//			}
		
		GridTreeLevel topLevel = grid.getTopLevel();
		if(topLevel != null){
			String levelId = UIRender.TL_PRE + widget.getId() + "_" + topLevel.getId();
			String levelScript = genScriptForTreeLevel(levelId, topLevel, widget.getId());
			buf.append(levelScript);
			buf.append("model.setTreeLevel(").append(levelId).append(");\n");
		}
		
		buf.append("model.setDataSet(pageUI.getWidget('" + widget.getId())
		   .append("').getDataset('" + ds.getId() + "'));\n");
		return buf.toString();
	}
	
	public static String generateGridColumn(IGridColumn header, Dataset ds, LfwWidget widget, String modelName){
		StringBuffer buf = new StringBuffer();
		//����ͷ
		if (header instanceof GridColumn) {
			GridColumn curHeader = (GridColumn)header;
			fillDataTypeAndEditorType(ds, curHeader); 
			
			String resId = curHeader.getI18nName();
			String text = curHeader.getText();
			//if(curHeader.getField() != null)
			String label = getFieldI18nName(ds, resId, curHeader.getField(), text, curHeader.getLangDir());
			// �õ���������
			String dataType = curHeader.getDataType();
			if(dataType == null)
			{
				throw new LfwRuntimeException("GridCompHeader must set DataType!");
			}
			// �õ��༭������
			String editorType = curHeader.getEditorType();
//				// �������������"UFBOOLEAN"����û�����ñ༭����,��༭��������Ϊ"CHECKBOX"
//				if(editorType == null || "".equals(editorType))
//				{
//					if(curHeader.getDataType().equals(StringDataTypeConst.UFBOOLEAN))
//						editorType = EditorTypeConst.CHECKBOX;
//					else
//						editorType = EditorTypeConst.STRINGTEXT;
//				}
			// �õ���Ⱦ��
			String renderType = curHeader.getRenderType();
//				// ��������������,���renderTypeû������,��Ҫ����editorType�����renderType
//				if((renderType == null || "".equals(renderType)) && editorType.equals(EditorTypeConst.COMBODATA))
//				{
//					renderType = RenderTypeConst.ComboRender;
//				}
			// ����������������textAlign
			String textAlign = curHeader.getTextAlign();
			if(textAlign == null)
			{
				if(dataType.equals(StringDataTypeConst.bOOLEAN) || dataType.equals(StringDataTypeConst.BOOLEAN) || dataType.equals(StringDataTypeConst.UFBOOLEAN))
					textAlign = "center";
				else if(dataType.equals(StringDataTypeConst.Decimal)|| dataType.equals(StringDataTypeConst.UFDOUBLE) || dataType.equals(StringDataTypeConst.DATE) || dataType.equals(StringDataTypeConst.INTEGER))
					textAlign = "right";
				else
					textAlign = "left";
			}
			
			int headerWidth = curHeader.getWidth();
			if(headerWidth == -1)
				headerWidth = GridColumn.DEFAULT_WIDTH;
			//keyName, showName, width, dataType, sortable, isHidden, columEditable, defaultValue, columBgColor, textAlign, textColor, isFixedHeader, renderType, editorType, topHeader, groupHeader, isGroupHeader, isSumCol
			buf.append("var " + curHeader.getId() + " = new GridCompHeader('" + curHeader.getField() + "','" + label + "','" + headerWidth + "','" + dataType + "',")
			   .append(curHeader.isSortable() + "," + (!curHeader.isVisible()) + "," + curHeader.isEditable() + ",'','")
			   .append((curHeader.getColumBgColor() == null?"":curHeader.getColumBgColor()) + "','")
			   .append(textAlign + "','")
			   .append((curHeader.getTextColor() == null?"":curHeader.getTextColor()) + "'," + curHeader.isFixedHeader() + "," + renderType + ",'" + editorType + "',null,null,null," + curHeader.isSumCol() + "," + curHeader.isAutoExpand()+ "," + curHeader.isShowCheckBox() + ");\n")
			   .append(modelName +  ".addHeader(" + curHeader.getId() + ");\n");
			
			if(curHeader.getExtendAttribute("showState") != null){
				buf.append(curHeader.getId())
					.append(".setShowState('")
					.append(curHeader.getExtendAttribute("showState"))
					.append("');\n");
			}
			
			// �����Ƿ������
			if(curHeader.isRequired())
			{
				buf.append(curHeader.getId())
				   .append(".setRequired(true);\n");
			}
			// ���������������ͣ�������Ӧ�Ľű�
			if(curHeader.getEditorType() != null && curHeader.getEditorType().equals(EditorTypeConst.COMBODATA))
				generateComboDataScript(widget, buf, curHeader);
			//����ǲ������ͣ�������Ӧ�Ľű�
			else if(curHeader.getEditorType() != null && curHeader.getEditorType().equals(EditorTypeConst.REFERENCE))
				generateRefereceScript(widget, buf, curHeader);
			//�����ѡ������ͣ�������Ӧ�Ľű�
			else if(curHeader.getEditorType() != null && curHeader.getEditorType().equals(EditorTypeConst.CHECKBOX))
				generateCheckBoxScript(widget, buf, curHeader);
			// ������������ͣ�������Ӧ�Ľű�
			else if(curHeader.getEditorType() != null && curHeader.getEditorType().equals(EditorTypeConst.INTEGERTEXT))
				generateIntegerTextScript(ds, buf, curHeader);
			// ����Ǹ������ͣ�������Ӧ�Ľű�
			else if(curHeader.getEditorType() != null && curHeader.getEditorType().equals(EditorTypeConst.DECIMALTEXT))
				generateDecimalTextScript(ds, buf, curHeader);
			// ������ַ�����,������Ӧ�Ľű�
			else if(curHeader.getEditorType() != null && curHeader.getEditorType().equals(EditorTypeConst.STRINGTEXT))
				generateStringTextScript(buf, curHeader);
		}
		//���ͷ
		else if(header instanceof GridColumnGroup)
		{
			GridColumnGroup curHeader = (GridColumnGroup)header;
			makeHeaders(widget, buf, curHeader, curHeader, ds, modelName);
		}
		return buf.toString();
	}
	
	private static String genScriptForTreeLevel(String levelShowId, GridTreeLevel level, String widgetId) {
		StringBuffer buf = new StringBuffer();
		String levelId = levelShowId;
		GridTreeLevel rLevel = level;
		buf.append("var ").append(levelId).append(" = ").append("new GridTreeLevel(\"").append(rLevel.getId()).append("\",\"").append(
				rLevel.getRecursiveKeyField()).append("\",\"").append(rLevel.getRecursivePKeyField()).append("\",").append(
				StringUtil.mergeScriptArray(rLevel.getLabelFields())).append(",\"").append(rLevel.getLoadField() == null ? "" : rLevel.getLoadField()).append("\"");
		buf.append(");\n");
//		if (rLevel.getContextMenu() != null) {
//			buf.append(addContextMenu(rLevel.getContextMenu(), levelId));
//		}
		
		return buf.toString();
	}
	
	private static void generateCheckBoxScript(LfwWidget widget, StringBuffer buf, GridColumn curHeader) {
		StaticComboData data = (StaticComboData) widget.getViewModels().getComboData(curHeader.getRefComboData());
		if(data != null)
		{
			CombItem[] items = data.getAllCombItems();
			if(items == null || items.length != 2)
			{
				throw new LfwRuntimeException("The Combodata is not suitable for header:" + curHeader.getId());
			}
			buf.append(curHeader.getId()).append(".setValuePair([\"")
			.append(items[0].getValue())
			.append("\",\"")
			.append(items[1].getValue())
			.append("\"]")
			.append(");\n");
		}
		else 
		{	
			if(curHeader.getDataType().equals(StringDataTypeConst.UFBOOLEAN))
			{
				buf.append(curHeader.getId()).append(".setValuePair([\"Y\",\"N\"]);\n");
			}
			else if(curHeader.getDataType().equals(StringDataTypeConst.bOOLEAN) || curHeader.getDataType().equals(StringDataTypeConst.BOOLEAN))
			{
				buf.append(curHeader.getId()).append(".setValuePair([\"true\",\"false\"]);\n");
			}
		}
	}
	private static void generateRefereceScript(LfwWidget widget, StringBuffer buf, GridColumn curHeader) {
		IRefNode refNode = widget.getViewModels().getRefNode(curHeader.getRefNode());
		if(refNode != null){
			String refId = WebElementTag.RF_PRE + widget.getId() + "_" + refNode.getId();
			buf.append(curHeader.getId()).append(".setNodeInfo(")
			   .append(refId)
			   .append(");\n");
		}
		
	}
	
	
	/**
	 * ����dataset�е������������GridColumn��dataType�����һ�ö�Ӧ��EditorType
	 * @param ds
	 * @param col
	 * @return
	 */
	private static void fillDataTypeAndEditorType(Dataset ds, GridColumn col){
		if(col.getDataType() == null || col.getDataType().trim().equals("")){
			Field field = ds.getFieldSet().getField(col.getId());
			if(field != null)
				col.setDataType(field.getDataType());
			else
				col.setDataType(StringDataTypeConst.STRING);
		}
		if(col.getEditorType() == null || col.getEditorType().trim().equals(""))
			col.setEditorType(EditorTypeConst.getEditorTypeByString(col.getDataType()));
		if(col.getRenderType() == null || col.getRenderType().trim().equals(""))
			col.setRenderType(RenderTypeConst.getRenderTypeByString(col.getDataType()));
	}
	
	/**
	 * Ϊ���������������������ݽű�
	 * @param buf
	 * @param curHeader
	 */
	private static void generateComboDataScript(LfwWidget widget, StringBuffer buf, GridColumn curHeader) 
	{
		if(curHeader.isImageOnly()){
			buf.append(curHeader.getId())
			   .append(".setShowImgOnly(true);\n");
		}
//		buf.append(curHeader.getId())
//		   .append(".setHeaderComboBoxCaptionValues(")
//		   .append(COMBO_PRE + getCurrWidget().getId() + "_")
//		   .append(curHeader.getRefComboData())
//		   .append(".getNameArray());\n");
//		buf.append(curHeader.getId())
//		   .append(".setHeaderComboBoxKeyValues(")
//		   .append(COMBO_PRE + getCurrWidget().getId() + "_")
//		   .append(curHeader.getRefComboData())
//				   .append(".getValueArray());\n");
		buf.append(curHeader.getId())
		   .append(".setHeaderComboBoxComboData(")
		   .append(WebElementTag.COMBO_PRE + widget.getId() + "_")
		   .append(curHeader.getRefComboData())
		   .append(");\n");
	}
	
	private static void generateStringTextScript(StringBuffer buf, GridColumn curHeader)
	{
		String maxLength = curHeader.getMaxLength();
		//if(maxLength == null || "".equals(maxLength))
			//maxLength = getFieldProperty(curHeader.getField(), DatasetConstant.MAX_LENGTH);
		
		if(maxLength != null && !"".equals(maxLength))
		{
			buf.append(curHeader.getId()).append(".setMaxLength(")
			   .append(maxLength)
			   .append(");\n");
		}
	}
	
	private static void generateDecimalTextScript(Dataset ds, StringBuffer buf, GridColumn curHeader)
	{
		String precision = curHeader.getPrecision();
		if(precision == null || "".equals(precision))
//			precision = getFieldProperty(ds, curHeader.getField(), Field.PRECISION);
			precision = ds.getFieldSet().getField(curHeader.getField()).getPrecision();
		
		if(precision != null && !"".equals(precision))
		{
			buf.append(curHeader.getId()).append(".setPrecision(")
			   .append(precision)
			   .append(");\n");
		}
		
		String maxFloatValue = curHeader.getMaxValue();
		String minFloatValue = curHeader.getMinValue();
		if(maxFloatValue == null || "".equals(maxFloatValue))
			maxFloatValue = getFieldProperty(ds, curHeader.getField(), Field.MAX_VALUE);
		if(minFloatValue == null || "".equals(minFloatValue))
			minFloatValue = getFieldProperty(ds, curHeader.getField(), Field.MIN_VALUE);
		if(minFloatValue != null && !"".equals(minFloatValue))
		{
			buf.append(curHeader.getId()).append(".setFloatMinValue(")
			   .append(minFloatValue)
			   .append(");\n");
		}
		
		if(maxFloatValue != null && !"".equals(maxFloatValue))
		{
			buf.append(curHeader.getId()).append(".setFloatMaxValue(")
			   .append(maxFloatValue)
			   .append(");\n");
		}
	}
	
	private static void generateIntegerTextScript(Dataset ds, StringBuffer buf, GridColumn curHeader)
	{
		// ���Ȼ�ȡ�ؼ�����������
		String maxValue = curHeader.getMaxValue();
		String minValue = curHeader.getMinValue();
		// �ؼ�����û���������ȡ��Ӧ��ds field������
		if(maxValue == null || "".equals(maxValue))
			maxValue = getFieldProperty(ds, curHeader.getField(), Field.MAX_VALUE);
		if(minValue == null || "".equals(minValue))
			minValue = getFieldProperty(ds, curHeader.getField(), Field.MIN_VALUE);
		
		if(minValue != null && !"".equals(minValue))
		{
			buf.append(curHeader.getId()).append(".setIntegerMinValue(")
			   .append(minValue)
			   .append(");\n");
		}
		if(maxValue != null && !"".equals(maxValue))
		{
			buf.append(curHeader.getId()).append(".setIntegerMaxValue(")
			   .append(maxValue)
			   .append(");\n");
		}
	}
	

	private static String getFieldProperty(Dataset ds, String fieldId, String name)
	{
		Field field = ds.getFieldSet().getField(fieldId);
		if(field == null)
			return null;
		return (String) field.getExtendAttributeValue(name);
	}
	
	/**
	 * �ݹ鹹�����ͷ
	 * @param buf
	 * @param header
	 * @param topHeader �˶��ͷ�����header
	 */
	private static void makeHeaders(LfwWidget widget, StringBuffer buf, IGridColumn header, GridColumnGroup topHeader, Dataset ds, String modelName)
	{
		if(header instanceof GridColumnGroup)
		{
			GridColumnGroup parentHeader = (GridColumnGroup)header;
			if(parentHeader == topHeader)
			{
				// ע��˴���visibleҪ����!visible
				buf.append("var " +  parentHeader.getId() + "= new GridCompHeader('" + parentHeader.getId() + "','" + parentHeader.getI18nName() + "','','',''," + (!parentHeader.isVisible()) + ",'','','','','','','','',null, null, true);\n")
				   .append(modelName + ".addHeader(" + parentHeader.getId() + ");\n");
				if(parentHeader.getExtendAttribute("showState") != null){
					buf.append(parentHeader.getId())
						.append(".setShowState('")
						.append(parentHeader.getExtendAttribute("showState"))
						.append("');\n");
				}
			}
			
			//�õ���������к���
			List<IGridColumn> list = parentHeader.getChildColumnList();
			//��¼��������"��"���Ǹ�,Ϊ�����ݹ���׼��
			IGridColumn groupHeader = null;
			if(list != null && list.size() > 0)
			{
				Iterator<IGridColumn> it = list.iterator();
				while(it.hasNext())
				{
					IGridColumn curHeader = it.next();
					if(curHeader instanceof GridColumnGroup)
					{
						GridColumnGroup tempHeader = (GridColumnGroup)curHeader;
						// ע��˴���visibleҪ����!visible
						buf.append("var " +  tempHeader.getId() + "= new GridCompHeader('" + tempHeader.getId() + "','" + tempHeader.getI18nName() + "','','',''," + (!tempHeader.isVisible()) + ",'','','','','','','',''," + topHeader.getId() + "," + parentHeader.getId() + ",true);\n");
						groupHeader = tempHeader;
						if(tempHeader.getExtendAttribute("showState") != null){
							buf.append(tempHeader.getId())
								.append(".setShowState('")
								.append(tempHeader.getExtendAttribute("showState"))
								.append("');\n");
						}
					}
					else if(curHeader instanceof GridColumn)
					{
						GridColumn realHeader = (GridColumn)curHeader;
						fillDataTypeAndEditorType(ds, realHeader);
						// �õ���������
						String dataType = realHeader.getDataType();
						
						// �õ��༭������
						String editorType = realHeader.getEditorType();
						// �������������"UFBOOLEAN"����û�����ñ༭����,��༭��������Ϊ"CHECKBOX"
						if(editorType == null || "".equals(editorType))
						{
							if(realHeader.getDataType().equals(StringDataTypeConst.UFBOOLEAN))
								editorType = EditorTypeConst.CHECKBOX;
							else
								editorType = "";
						}
						// �õ���Ⱦ��
						String renderType = realHeader.getRenderType();
						// ��������������,���renderTypeû������,��Ҫ����editorType�����renderType
						if((renderType == null || "".equals(renderType)) && editorType.equals(EditorTypeConst.COMBODATA))
						{
							renderType = RenderTypeConst.ComboRender;
						}
						
						String resId = realHeader.getI18nName();
						String text = realHeader.getText();
						String label = getFieldI18nName(ds, resId, realHeader.getField(), text, realHeader.getLangDir());
						
						int headerWidth = realHeader.getWidth();
						if(headerWidth == -1)
							headerWidth = GridColumn.DEFAULT_WIDTH;
						
						String textAlign = realHeader.getTextAlign();
						if(textAlign == null)
						{
							if(dataType.equals(StringDataTypeConst.bOOLEAN) || dataType.equals(StringDataTypeConst.BOOLEAN) || dataType.equals(StringDataTypeConst.UFBOOLEAN))
								textAlign = "center";
							else if(dataType.equals(StringDataTypeConst.Decimal)|| dataType.equals(StringDataTypeConst.UFDOUBLE) || dataType.equals(StringDataTypeConst.DATE) || dataType.equals(StringDataTypeConst.INTEGER))
								textAlign = "right";
							else
								textAlign = "left";
						}
						
						buf.append("var " + realHeader.getId() + " = new GridCompHeader('" + realHeader.getField() + "','" + label + "','" + headerWidth + "','" + dataType + "',")
						   .append(realHeader.isSortable() + "," + (!realHeader.isVisible()) + "," + realHeader.isEditable() + ",'','','" + textAlign + "',''," + realHeader.isFixedHeader() + "," + renderType + ",'" + editorType + "'," + topHeader.getId() + "," + parentHeader.getId() + ",true," + realHeader.isSumCol() + ");\n");
						if(realHeader.getExtendAttribute("showState") != null){
							buf.append(realHeader.getId())
								.append(".setShowState('")
								.append(realHeader.getExtendAttribute("showState"))
								.append("');\n");
						}
						
						//���������������ͣ�������Ӧ�Ľű�
						if(realHeader.getEditorType() != null && realHeader.getEditorType().equals(EditorTypeConst.COMBODATA))
							generateComboDataScript(widget, buf, realHeader);
						//����ǲ������ͣ�������Ӧ�Ľű�
						else if(realHeader.getEditorType() != null && realHeader.getEditorType().equals(EditorTypeConst.REFERENCE))
							generateRefereceScript(widget, buf, realHeader);
						//�����ѡ������ͣ�������Ӧ�Ľű�
						else if(realHeader.getEditorType() != null && realHeader.getEditorType().equals(EditorTypeConst.CHECKBOX))
							generateCheckBoxScript(widget, buf, realHeader);
						//	������������ͣ�������Ӧ�Ľű�
						else if(realHeader.getEditorType() != null && realHeader.getEditorType().equals(EditorTypeConst.INTEGERTEXT))
							generateIntegerTextScript(ds, buf, realHeader);
						// ����Ǹ������ͣ�������Ӧ�Ľű�
						else if(realHeader.getEditorType() != null && realHeader.getEditorType().equals(EditorTypeConst.DECIMALTEXT))
							generateDecimalTextScript(ds, buf, realHeader);
						else if(realHeader.getEditorType() != null && realHeader.getEditorType().equals(EditorTypeConst.STRINGTEXT))
							generateStringTextScript(buf, realHeader);
					}
				}
				//�ݹ鴦����ΪGridColumnGroup���͵���header
				if(groupHeader != null)
					makeHeaders(widget, buf, groupHeader, topHeader, ds, modelName);
			}
		}
	}

	
	protected static String getFieldI18nName(Dataset ds, String i18nName, String fieldId, String defaultI18nName, String langDir)
	{ 
		if(i18nName != null && !i18nName.equals("")){
			if(i18nName.equals("$NULL$"))
				return "";
			return translate(i18nName, defaultI18nName == null?i18nName:defaultI18nName, langDir);
		}
//		Dataset ds = getDataset();
		if(ds == null) 
			return defaultI18nName;
		
		if(fieldId != null){
			int fldIndex = ds.getFieldSet().nameToIndex(fieldId);
			if(fldIndex == -1)
				throw new LfwRuntimeException("can not find the field:" + fieldId + ",dataset:" + ds.getId());
			Field field = ds.getFieldSet().getField(fldIndex);
			i18nName = field.getI18nName();
			String text = field.getText();
			String defaultValue = text == null? i18nName : text;
			if(i18nName == null || i18nName.equals(""))
				return  defaultI18nName == null?defaultValue:defaultI18nName;
			else{
				return translate(i18nName, defaultI18nName == null?defaultValue:defaultI18nName, langDir);
			}
		}
		else return defaultI18nName;
	}

	/**
	 * ���ж��﷭��,������ܷ���,����ԭi18nName
	 * @param i18nName
	 * @return
	 */
	private static String translate(String i18nName, String defaultI18nName, String langDir){
		if(i18nName == null && defaultI18nName == null)
			return "";
//			return defaultI18nName;
//		return i18nName;
		if(langDir == null)
			langDir = LfwRuntimeEnvironment.getLangDir();
		return LanguageUtil.getWithDefaultByProductCode(langDir, defaultI18nName, i18nName);
	}
}
