package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.ComboBoxContext;
import nc.uap.lfw.core.comp.ctx.FloatTextContext;
import nc.uap.lfw.core.comp.ctx.FormElementContext;
import nc.uap.lfw.core.comp.ctx.ReferenceTextContext;
import nc.uap.lfw.core.comp.ctx.TextContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;


/**
 * ��Ԫ��
 * 
 */
public final class FormElement extends WebComponent {
	private static final long serialVersionUID = -633638779207135081L;
	public static final String SELF_DEF_FUNC = "SELF_DEF_FUNC";
	//LABEL���󣬿������
	public static final String LABELPOS_LEFT_l = "left_l";
	//LABEL���󣬿��Ҷ���
	public static final String LABELPOS_LEFT_r = "left_r";
	//LABEL����
	public static final String LABELPOS_RIGHT = "right";
	//LABEL������򶥲�
	public static final String LABELPOS_TOP = "top";
	
	private FormComp parent = null;
	private Integer rowSpan = Integer.valueOf(1);
	private Integer colSpan = Integer.valueOf(1);
	private String width;
	private String height; 
//	private Integer inputWidth = new Integer(100); //elementԪ�صĿ��
	private String i18nName;
	private String text;
	// ��ʵֵ��Reference��ʹ��
	private String value;
	// ����ʹ�õ���ʾֵ
	private String showValue;

	// ��ʾ��ÿ��ele֮���˵������
	private String description;
	private String langDir;
	// label������ɫ
	private String labelColor = null;
	private String labelPos = LABELPOS_LEFT_l;
	private String editorType;//�༭������
	private String refNode;//ֻ�б༭������Ϊ��Ref��ʱ������
	private String relationField;//ֻ�б༭������Ϊ��Ref��ʱ������
	private String dataType;//��������
	private String field;//���ڰﶨ����dataset�ľ����ֶ�
	private String refComboData; //���ڴ�ȡ�ۺ��ֶ�����
	private int index; // ComboBox�е�ѡ��������
	private String dataDivHeight; // ������ؼ��������߶�
	private String defaultValue; //Ĭ��ֵ
	// ��Ӧǰ̨��readOnly����,enabled������ǰ̨���Ƿ񼤻�
	private boolean editable = true;
	private boolean imageOnly = false;
	// �ַ�������������ֽڳ���(Ŀǰֻ����StringText)
	private String maxLength; 
	// ������ʹ��
	private boolean selectOnly = true;
	// �Ƿ������
//	private boolean required = false;
	
	private boolean nextLine = false;
	
	/*���ֵ��Сֵ����,������IntegerTextComp����*/
	private String maxValue;
	private String minValue; 
	
	/*����*/
	private String precision;
	
	// ����ڸ߼��༭��(��Ҫ���صĲ�����:��js������ʽ����,��[0,1])
	private String hideBarIndices;
	// ��Ը߼��༭��(��Ҫ���صĲ������е�ͼƬ,��js�������ʽ����,��[[0,1],[],[2]])
	private String hideImageIndices;
	
	// �Ƿ�۽�
	private boolean focus = false;
	
	// Ĭ����ʾֵ����ʾ�ã�������ʵֵ��
	private String tip = null;
	
	// �Զ������Ͱ����ؼ�ID
	private String bindId;
	
	private boolean nullAble = true;
	
	private boolean attachNext = false;
	
	// ���븨����ʾ��Ϣ���ʹ�����ʾ�໥�����
	private String inputAssistant = "";
	
//	protected String height = null;
	//�ļ��ϴ����͵�element�������ļ���С����
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
		if (ctx instanceof ReferenceTextContext) {  // ���������
			ReferenceTextContext eleCtx = (ReferenceTextContext) ctx;
			this.setShowValue(eleCtx.getShowValue());
			this.setValue(eleCtx.getValue());
		} else if (ctx instanceof TextContext) {  // ��ͨ�����
			TextContext eleCtx = (TextContext) ctx;
			this.setValue(eleCtx.getValue());
		} else if (ctx instanceof ComboBoxContext) {  // ���������
			ComboBoxContext eleCtx = (ComboBoxContext) ctx;
		} else if(ctx instanceof FloatTextContext){//��ֵ��
			FloatTextContext eleCtx = (FloatTextContext) ctx;
			this.setPrecision(eleCtx.getPrecision());
		}
		else {  // RadioGroup��CheckboxGroup��
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
