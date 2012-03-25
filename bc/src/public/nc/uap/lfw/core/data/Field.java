package nc.uap.lfw.core.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.base.ExtendAttributeSupport;

/**
 *
 * 与DataSet关联的Field对象
 * 
 */

public class Field extends ExtendAttributeSupport implements Cloneable, Serializable{

	private static final long serialVersionUID = 1L;
	//参照出来的列标识
	public static final String REF_KEY = "$refKey";
	public static final String COMB_KEY = "$combKey";
	public static final String COMB_CAPTION = "$combCaption";
	public static final String DATE_KEY = "$dateKey";
	public static final String DATE_TIME_KEY = "$dateTimeKey";
	public static final String COMB_VALUE = "$combValue";
	public static final String REF_FIELD = "$refField";
	//以下为在dataset的配置过程中Field的property中配置的一些属性常量值。
	public static String MAX_LENGTH = "$maxLength";
	public static String MAX_VALUE = "$maxValue";
	public static String MIN_VALUE = "$minValue";
	public static String PRECISION = "$precision";
	public static String ENABLE = "$enable";
	public static String HIDE = "$hide";
	
	
	public static final String SOURCE_MD = "MD";
	public static final String SOURCE_ENUM = "EM";
	
    /**单据模板中字段的3个自定义字段，通过这个常量在Dataset.getProperty()取到*/
    public static String BILL_TEMPLATE_FIELD_DEF1 = "$bill_template_field_def1";
    public static String BILL_TEMPLATE_FIELD_DEF2 = "$bill_template_field_def2";
    public static String BILL_TEMPLATE_FIELD_DEF3 = "$bill_template_field_def3";
    
    public static String DEFAULT_VALUE_SYSDATE = "@SYSDATE";
	
    /**
     * 根据此字段可以生成别的字段
     */
    private String sourceField;
    
	private String langDir;
	/*标识*/
	private String id;
	
	/*标签*/
	private String i18nName;
	
	private String text;
	
	/*是否可空*/
	private boolean nullAble = true;
	
	/*数据类型*/
	private String dataType;
	
	/*如果是与数据库或VO对应时，相应的字段或属性名称*/
	private String field;
	
	/*该字段的缺省值*/
	private Object defaultValue = null; 
	
	/*用于存放字段的属性信息*/ 
	//private Map<String, Property> fieldProperty;
	/**/
	private boolean isPrimaryKey = false;
	
	private String editFormular;
	
	private String loadFormular;
	
	private String idColName;
	
	/*是否锁定字段*/
	private boolean isLock = false;

	//同一数据集多页签显示时用于区分页签的标识,只在设置了公式时使用。这个本不属于数据这一块，暂时没有其它方案
	private String formularTabCode;
	
	//自定义编辑公式、可在模板的自定义中设置，从对应的field中取出塞入此字段
	private String defEditFormular;
	//验证公式
	private String validateFormula; 
	
	/*该字段的校验类型，email、number、chn、variable、date、phone;
	     没有设置则不会校验、以上类型以外则认为是正则表达式;
	*/
	private String formater;
	/*精度*/
	private String precision;
	/*最大值*/
	private String maxValue;
	/*context是否改变*/
	private boolean ctxChanged = false;
	/*已改变的context属性列表*/
	private List<String> ctxChangedProperties = new ArrayList<String>();
	
	/*标识可扩展字段原始来源*/
	private String extSource;
	/*标识可扩展字段原始来源属性*/
	private String extSourceAttr;
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
		setCtxChanged(true);
		addCtxChangedProperty("precision");
	}

	private String minValue; 
	

	public String getFormater() { 
		return formater;
	}

	public void setFormater(String formater) {
		this.formater = formater;
	}

	public String getValidateFormula() {
		return validateFormula;
	}

	public void setValidateFormula(String validateFormula) {
		this.validateFormula = validateFormula;
	}

	/**
	 * 缺省构造
	 *
	 */
	public Field(){}
	
	/**
	 * 带参构造
	 * @param id
	 */
	public Field(String id){
		
		this.id = id;
	}
	
	/**
	 * 设置字段标识
	 * @param id
	 */
	public void setId(String id){
		
		this.id = id;
	}

	/**
	 * 获取字段标识
	 * @return
	 */
	public String getId(){
		
		return this.id;
	}

	/**
	 * 获取Label
	 * @return
	 */
	public String getI18nName(){
		
		return this.i18nName;
	}

	/**
	 * 设置Label信息
	 * @param label
	 */
	public void setI18nName(String label){
		
		this.i18nName = label;
	}

	/**
	 * 该字段是否可以为空
	 * @return
	 */
	public boolean isNullAble(){
		
		return this.nullAble;
	}

	/**
	 * 设置是否可空标志位
	 * @param nullAble
	 */
	public void setNullAble(boolean nullAble){
		
		this.nullAble = nullAble;
	}

	/**
	 * 设置数据类型
	 * @param type
	 */
	public void setDataType(String type){
		
		this.dataType = type;
	}

	/**
	 * 获取数据类型
	 * @return
	 */
	public String getDataType(){
		
		return this.dataType;
	}

	/**
	 * 设置字段或属性名称
	 * @return
	 */
	public String getField() {
		return field;
	}

	/**
	 * 获取字段或属性名称
	 * @param key
	 */
	public void setField(String key) {
		this.field = key;
	}
	
	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * 实现克隆方法
	 */
	public Object clone()
	{
		return super.clone();
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public String getEditFormular() {
		return editFormular;
	}

	public void setEditFormular(String formular) {
		this.editFormular = formular;
	}

	public String getLoadFormular() {
		return loadFormular;
	}

	public void setLoadFormular(String loadFormular) {
		this.loadFormular = loadFormular;
	}

	@Override
	public String toString() {
		return "id=" + this.getId() + ";field=" + this.getField() + ";showName=" + this.getI18nName();
	}

	public String getIdColName() {
		return idColName;
	}

	public void setIdColName(String idColName) {
		this.idColName = idColName;
	}

	public String getFormularTabCode() {
		return formularTabCode;
	}

	public void setFormularTabCode(String formularTabCode) {
		this.formularTabCode = formularTabCode;
	}

	public boolean isLock() {
		return isLock;
	}
	

	public void setLock(boolean isLock) {
		this.isLock = isLock;
	}

	public String getLangDir() {
		return langDir;
	}

	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}
	
	public String getDefEditFormular() {
		return defEditFormular;
	}

	public void setDefEditFormular(String defEditFormular) {
		this.defEditFormular = defEditFormular;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSourceField() {
		return sourceField;
	}

	public void setSourceField(String sourceField) {
		this.sourceField = sourceField;
	}

	public boolean isCtxChanged() {
		return ctxChanged ;
	}

	public void setCtxChanged(boolean changed) {
		this.ctxChanged = changed;
		if (!changed)
			this.clearCtxChangedProperties();
	}

	public List<String> getCtxChangedProperties() {
		return ctxChangedProperties;
	}

	public void setCtxChangedProperties(List<String> ctxChangedProperties) {
		this.ctxChangedProperties = ctxChangedProperties;
	}
	
	/**
	 * 增加已改变的context属性
	 * @param propertyName
	 */
	public void addCtxChangedProperty(String propertyName) {
		this.ctxChangedProperties.add(propertyName);
	}
	
	/**
	 * 清空已改变的context属性列表
	 */
	public void clearCtxChangedProperties() {
		this.ctxChangedProperties.clear();
	}
	
	/**
	 * 查看context的某个属性是否发生变化
	 * @param propertyName
	 * @return
	 */
	public boolean checkCtxPropertyChanged(String propertyName) {
		if (this.ctxChangedProperties.indexOf(propertyName) != -1)
			return true;
		return false;
	}

	public String getExtSource() {
		return extSource;
	}

	public void setExtSource(String extSource) {
		this.extSource = extSource;
	}

	public String getExtSourceAttr() {
		return extSourceAttr;
	}

	public void setExtSourceAttr(String extSourceAttr) {
		this.extSourceAttr = extSourceAttr;
	}
}
