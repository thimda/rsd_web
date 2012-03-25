package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.ComboBoxContext;
import nc.uap.lfw.core.comp.ctx.FloatTextContext;
import nc.uap.lfw.core.comp.ctx.FormElementContext;
import nc.uap.lfw.core.comp.ctx.ReferenceTextContext;
import nc.uap.lfw.core.comp.ctx.TextContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;


/**
 * 表单元素
 * 
 */
public final class FormElement extends WebComponent {
	private static final long serialVersionUID = -633638779207135081L;
	public static final String SELF_DEF_FUNC = "SELF_DEF_FUNC";
	//LABEL居左，靠左对齐
	public static final String LABELPOS_LEFT_l = "left_l";
	//LABEL居左，靠右对齐
	public static final String LABELPOS_LEFT_r = "left_r";
	//LABEL居右
	public static final String LABELPOS_RIGHT = "right";
	//LABEL居输入框顶部
	public static final String LABELPOS_TOP = "top";
	
	private FormComp parent = null;
	private Integer rowSpan = Integer.valueOf(1);
	private Integer colSpan = Integer.valueOf(1);
	private String width;
	private String height; 
//	private Integer inputWidth = new Integer(100); //element元素的宽度
	private String i18nName;
	private String text;
	// 真实值，Reference中使用
	private String value;
	// 参照使用的显示值
	private String showValue;

	// 显示在每个ele之后的说明文字
	private String description;
	private String langDir;
	// label字体颜色
	private String labelColor = null;
	private String labelPos = LABELPOS_LEFT_l;
	private String editorType;//编辑器类型
	private String refNode;//只有编辑器类型为“Ref”时才有用
	private String relationField;//只有编辑器类型为“Ref”时才有用
	private String dataType;//数据类型
	private String field;//用于帮定数据dataset的具体字段
	private String refComboData; //用于存取聚合字段易用
	private int index; // ComboBox中的选中项索引
	private String dataDivHeight; // 下拉框控件数据区高度
	private String defaultValue; //默认值
	// 对应前台的readOnly属性,enabled代表了前台的是否激活
	private boolean editable = true;
	private boolean imageOnly = false;
	// 字符串输入框的最大字节长度(目前只用于StringText)
	private String maxLength; 
	// 下拉框使用
	private boolean selectOnly = true;
	// 是否必输项
//	private boolean required = false;
	
	private boolean nextLine = false;
	
	/*最大值最小值设置,仅用于IntegerTextComp类型*/
	private String maxValue;
	private String minValue; 
	
	/*精度*/
	private String precision;
	
	// 针对于高级编辑器(需要隐藏的操作条:以js数组形式传入,例[0,1])
	private String hideBarIndices;
	// 针对高级编辑器(需要隐藏的操作条中的图片,以js数组的形式传入,例[[0,1],[],[2]])
	private String hideImageIndices;
	
	// 是否聚焦
	private boolean focus = false;
	
	// 默认显示值（提示用，不是真实值）
	private String tip = null;
	
	// 自定义类型包含控件ID
	private String bindId;
	
	private boolean nullAble = true;
	
	private boolean attachNext = false;
	
	// 输入辅助提示信息（和错误提示相互替代）
	private String inputAssistant = "";
	
//	protected String height = null;
	//文件上传类型的element，加上文件大小限制
	private String sizeLimit;
	
	public String getSizeLimit() {
		return sizeLimit;
	}
	public void setSizeLimit(String sizeLimit) {
		if(this.sizeLimit != sizeLimit){
			this.sizeLimit = sizeLimit;
			setCtxChanged(true);
		}
	}

	
	public String getInputAssistant() {
		return inputAssistant;
	}

	public void setInputAssistant(String inputAssistant) {
		this.inputAssistant = inputAssistant;
	}

	public boolean isAttachNext() {
		return attachNext;
	}

	public void setAttachNext(boolean attachNext) {
		this.attachNext = attachNext;
	}

	public boolean isNullAble() {
		return nullAble;
	}

	public void setNullAble(boolean nullAble) {
		this.nullAble = nullAble;
	}

	public static final int DEFAULT_HEIGHT = 22;
	
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
//	
//	
//	
//	public Integer getInputWidth() {
//		return inputWidth;
//	}
//
//	public void setInputWidth(Integer inputWidth) {
//		this.inputWidth = inputWidth;
//	}

	public FormElement() {
		super();
//		this.width = null;
//		this.enabled = false;
	}
	public FormElement(String id) {
		super(id);
//		this.width = null;
//		this.enabled = false;
	}
	
	public Integer getColSpan() {
		return colSpan;
	}
	public void setColSpan(Integer colSpan) {
		this.colSpan = colSpan;
	}
	public Integer getRowSpan() {
		return rowSpan;
	}
	public void setRowSpan(Integer rowSpan) {
		this.rowSpan = rowSpan;
	}
	public String getLabel() {
		return text;
	}
	public void setLabel(String label) {
		if (this.text != label) {
			this.text = label;
			setCtxChanged(true);
		}
	}
	public String getRefNode() {
		return refNode;
	}
	public void setRefNode(String refNode) {
		this.refNode = refNode;
	}
	public String getRelationField() {
		return relationField;
	}
	public void setRelationField(String relationField) {
		this.relationField = relationField;
	}
	public String getEditorType() {
		return editorType;
	}
	public void setEditorType(String type) {
		this.editorType = type;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Object clone()
	{
		return super.clone();
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		if(editable != this.editable){
			this.editable = editable;
			setCtxChanged(true);
			addCtxChangedProperty("editable");
		}
	}
	public String getRefComboData() {
		return refComboData;
	}
	public void setRefComboData(String refComboData) {
		this.refComboData = refComboData;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public boolean isImageOnly() {
		return imageOnly;
	}
	public void setImageOnly(boolean imageOnly) {
		this.imageOnly = imageOnly;
	}
	public String getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}
	public boolean isSelectOnly() {
		return selectOnly;
	}
	public void setSelectOnly(boolean selectOnly) {
		this.selectOnly = selectOnly;
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
		if (precision != null && !precision.equals(this.precision)) {
			this.precision = precision;
			setCtxChanged(true);
		}
	}
	
	public String getHideBarIndices() {
		return hideBarIndices;
	}
	public void setHideBarIndices(String hideBarIndices) {
		this.hideBarIndices = hideBarIndices;
	}
	public String getHideImageIndices() {
		return hideImageIndices;
	}
	public void setHideImageIndices(String hideImageIndices) {
		this.hideImageIndices = hideImageIndices;
	}
	public boolean isRequired() {
		return !nullAble;
	}
	public void setRequired(boolean required) {
		setNullAble(!required);
	}
	public boolean isNextLine() {
		return nextLine;
	}
	public void setNextLine(boolean nextLine) {
		this.nextLine = nextLine;
	}
//	@Override
//	public String getHeight() {
//		if(this.height == null)
//			return this.rowSpan * DEFAULT_HEIGHT + "";
//		return this.height;
//	}
//	
//	public void setHeight(String height){
//		this.height = height;
//	}
//	
//	@Override
//	public String getWidth() {
//		return this.width;
//	}
	
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}
	public String getDataDivHeight() {
		return dataDivHeight;
	}
	public void setDataDivHeight(String dataDivHeight) {
		this.dataDivHeight = dataDivHeight;
	}
	public String getLabelColor() {
		return labelColor;
	}
	public void setLabelColor(String labelColor) {
		if (labelColor != this.labelColor) {
			this.labelColor = labelColor;
			setCtxChanged(true);
		}
	}
	public String getLangDir() {
		return langDir;
	}
	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}
	public String getI18nName() {
		return i18nName;
	}
	public void setI18nName(String name) {
		i18nName = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text != null && !text.equals(this.text)) {
			this.text = text;
			setCtxChanged(true);
		}
	}

	public boolean isFocus() {
		return focus;
	}

	public void setFocus(boolean focus) {
		if (focus == true) {
			this.focus = focus;
			setCtxChanged(true);
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (value != null && !value.equals(this.value)) {
			this.value = value;
			setCtxChanged(true);
		}
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		if (index != this.index) {
			this.index = index;
			setCtxChanged(true);
		}
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		setCtxChanged(true);
		addCtxChangedProperty("enabled");
	}

	public void setParent(FormComp parent) {
		this.parent = parent;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}
	
	@Override
	public BaseContext getContext() {
		FormElementContext ctx = new FormElementContext();
		ctx.setId(this.getId());
		ctx.setValue(this.getValue());
		ctx.setShowValue(this.getShowValue());
		//if (checkCtxPropertyChanged("enabled"))
		ctx.setEnabled(this.isEnabled());
		ctx.setFocus(this.isFocus());
		ctx.setIndex(this.getIndex());
		if (checkCtxPropertyChanged("editable"))
			ctx.setEditable(this.isEditable());
		ctx.setVisible(this.isVisible());
		ctx.setLabelColor(this.getLabelColor());
		ctx.setPrecision(this.getPrecision());
		ctx.setLabel(this.getLabel());
		if(this.getSizeLimit() != null)
			ctx.setSizeLimit(this.getSizeLimit());
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		FormElementContext feleCtx = (FormElementContext) ctx;
		if (ctx instanceof ReferenceTextContext) {  // 参照输入框
			ReferenceTextContext eleCtx = (ReferenceTextContext) ctx;
			this.setShowValue(eleCtx.getShowValue());
			this.setValue(eleCtx.getValue());
		} else if (ctx instanceof TextContext) {  // 普通输入框
			TextContext eleCtx = (TextContext) ctx;
			this.setValue(eleCtx.getValue());
		} else if (ctx instanceof ComboBoxContext) {  // 下拉输入框
			ComboBoxContext eleCtx = (ComboBoxContext) ctx;
		} else if(ctx instanceof FloatTextContext){//数值型
			FloatTextContext eleCtx = (FloatTextContext) ctx;
			this.setPrecision(eleCtx.getPrecision());
		}
		else {  // RadioGroup、CheckboxGroup等
			BaseContext eleCtx = (BaseContext) ctx;
		}
		this.setEnabled(feleCtx.isEnabled());
		this.setEditable(feleCtx.isEditable());
		this.setVisible(feleCtx.isVisible());
		this.setLabelColor(feleCtx.getLabelColor());
		this.setLabel(feleCtx.getLabel());
		if(feleCtx.getSizeLimit() != null)
			this.setSizeLimit(feleCtx.getSizeLimit());
		this.setCtxChanged(false);
	}

	public FormComp getParent() {
		return parent;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}
	
	public String getShowValue() {
		return showValue;
	}

	public void setShowValue(String showValue) {
		this.showValue = showValue;
	}

	public static String getLABELPOS_LEFT_l() {
		return LABELPOS_LEFT_l;
	}

	public String getLabelPos() {
		return labelPos;
	}

	public void setLabelPos(String labelPos) {
		this.labelPos = labelPos;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
}
