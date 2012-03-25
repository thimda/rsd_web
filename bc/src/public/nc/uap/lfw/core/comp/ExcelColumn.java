package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.common.StringDataTypeConst;

/**
 * Excel��ͷ������
 * 
 * @author guoweic
 * 
 */
public class ExcelColumn extends WebElement implements IExcelColumn {
	public static final int DEFAULT_WIDTH = 120;
	private static final long serialVersionUID = -958941388322108998L;
	private String field;
	private String langDir;
	private String i18nName;
	private String text;
	private int width = 120;
	private String dataType = StringDataTypeConst.STRING;
	private boolean sortable = true;
	private boolean visible = true;
	private boolean editable = true;
	private String columBgColor;
	private String textAlign;
	private String textColor;
	private boolean fixedHeader = false;
	private String editorType;
	private String renderType;
	private String refComboData;
	private String refNode;
	private boolean imageOnly = false;
	private String maxValue;
	private String minValue;
	private String precision;
	private String maxLength;
	private boolean nullAble = true;
	//	
	// // �Ƿ������
	// private boolean required = false;
	// �Ƿ��Ǻϼ���
	private boolean sumCol = false;
	// �ñ�ͷ�Ƿ����Զ���չ��
	private boolean autoExpand = false;

	private String colmngroup;

	/* ���ڴ���ֶε�������Ϣ */
	// private Map<String, Property> columnProperty;

	public boolean isNullAble() {
		return nullAble;
	}

	public void setNullAble(boolean nullAble) {
		this.nullAble = nullAble;
	}

	public String getColmngroup() {
		return colmngroup;
	}

	public void setColmngroup(String colmngroup) {
		this.colmngroup = colmngroup;
	}

	public String getColumBgColor() {
		return columBgColor;
	}

	public void setColumBgColor(String columBgColor) {
		this.columBgColor = columBgColor;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean columEditable) {
		this.editable = columEditable;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean isHidden) {
		this.visible = isHidden;
	}

	public String getField() {
		return field;
	}

	public void setField(String keyName) {
		this.field = keyName;
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String showName) {
		this.i18nName = showName;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public String getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getEditorType() {
		return editorType;
	}

	public void setEditorType(String editorType) {
		this.editorType = editorType;
	}

	public String getRefComboData() {
		return refComboData;
	}

	public void setRefComboData(String refDataId) {
		this.refComboData = refDataId;
	}

	public String getRenderType() {
		return renderType;
	}

	public void setRenderType(String renderType) {
		this.renderType = renderType;
	}

	public String getRefNode() {
		return refNode;
	}

	public void setRefNode(String refNode) {
		this.refNode = refNode;
	}

	public boolean isImageOnly() {
		return imageOnly;
	}

	public void setImageOnly(boolean imageOnly) {
		this.imageOnly = imageOnly;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public boolean isSumCol() {
		return sumCol;
	}

	public void setSumCol(boolean sumCol) {
		this.sumCol = sumCol;
	}

	public boolean isFixedHeader() {
		return fixedHeader;
	}

	public void setFixedHeader(boolean fixedHeader) {
		this.fixedHeader = fixedHeader;
	}

	public boolean isRequired() {
		return !nullAble;
	}

	public void setRequired(boolean required) {
		setNullAble(!required);
	}

	public boolean isAutoExpand() {
		return autoExpand;
	}

	public void setAutoExpand(boolean autoExpand) {
		this.autoExpand = autoExpand;
	}

	public void mergeProperties(WebElement ele) {
		super.mergeProperties(ele);
		ExcelColumn column = (ExcelColumn) ele;
		String i18nName = column.getI18nName();
		if (i18nName != null)
			this.setI18nName(i18nName);

		boolean autoExpand = column.isAutoExpand();
		this.setAutoExpand(autoExpand);

		boolean sortable = column.isSortable();
		this.setSortable(sortable);
		boolean visiable = column.isVisible();
		this.setVisible(visiable);
		boolean editable = column.isEditable();
		this.setEditable(editable);
		boolean fixedHeader = column.isFixedHeader();
		this.setFixedHeader(fixedHeader);
		boolean imageOnly = column.isImageOnly();
		this.setImageOnly(imageOnly);

		String columnBgColor = column.getColumBgColor();
		if (columnBgColor != null)
			this.columBgColor = columnBgColor;

		String dataType = column.getDataType();
		if (dataType != null)
			this.dataType = column.getDataType();

		String editorType = column.getEditorType();
		if (editorType != null)
			this.editorType = column.getEditorType();

		// Map<String, JsEventHandler> handlerMap = column.getEventHandlerMap();
		// if(handlerMap.size() > 0){
		// Iterator<JsEventHandler> it = handlerMap.values().iterator();
		// while(it.hasNext()){
		// this.addEventHandler(it.next());
		// }
		// }

		String field = column.getField();
		if (field != null)
			this.field = field;
		String maxLength = column.getMaxLength();
		if (maxLength != null)
			this.maxLength = maxLength;

		String maxValue = column.getMaxValue();
		if (maxValue != null)
			this.maxValue = maxValue;

		String minValue = column.getMinValue();
		if (minValue != null)
			this.minValue = minValue;

		String precision = column.getPrecision();
		if (precision != null)
			this.precision = precision;

		String refComboData = column.getRefComboData();
		if (refComboData != null)
			this.refComboData = refComboData;

		String refNode = column.getRefNode();
		if (refNode != null)
			this.refNode = refNode;

		String renderType = column.getRenderType();
		if (renderType != null)
			this.renderType = renderType;

		String textAlign = column.getTextAlign();
		if (textAlign != null)
			this.textAlign = textAlign;

		String textColor = column.getTextColor();
		if (textColor != null)
			this.textColor = textColor;

		int width = column.getWidth();
		if (width != -1)
			this.width = width;
	}

	// public void addProperty(Property p)
	// {
	// if(p == null)
	// return ;
	// if(columnProperty == null)
	// columnProperty = new HashMap<String, Property>();
	// columnProperty.put(p.getName(), p);
	// }
	//	
	// public String getPropertyValueByName(String name)
	// {
	// Property p = getProperty(name);
	// if(p == null)
	// return null;
	// return p.getValue();
	// }

	// public Property getProperty(String name)
	// {
	// if(name == null || name.trim().equals(""))
	// return null;
	// if(columnProperty == null)
	// return null;
	// return columnProperty.get(name);
	// }
	//	
	// public Property[] getProperties()
	// {
	// if(columnProperty == null)
	// return new Property[0];
	// return columnProperty.values().toArray(new Property[0]);
	// }
	//	
	// public void removeProperty(String name)
	// {
	// if(columnProperty == null || name == null || name.trim().equals(""))
	// return ;
	// columnProperty.remove(name);
	// }
	public String getLangDir() {
		return langDir;
	}

	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
