package nc.uap.lfw.core.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.base.ExtendAttributeSupport;

/**
 *
 * ��DataSet������Field����
 * 
 */

public class Field extends ExtendAttributeSupport implements Cloneable, Serializable{

	private static final long serialVersionUID = 1L;
	//���ճ������б�ʶ
	public static final String REF_KEY = "$refKey";
	public static final String COMB_KEY = "$combKey";
	public static final String COMB_CAPTION = "$combCaption";
	public static final String DATE_KEY = "$dateKey";
	public static final String DATE_TIME_KEY = "$dateTimeKey";
	public static final String COMB_VALUE = "$combValue";
	public static final String REF_FIELD = "$refField";
	//����Ϊ��dataset�����ù�����Field��property�����õ�һЩ���Գ���ֵ��
	public static String MAX_LENGTH = "$maxLength";
	public static String MAX_VALUE = "$maxValue";
	public static String MIN_VALUE = "$minValue";
	public static String PRECISION = "$precision";
	public static String ENABLE = "$enable";
	public static String HIDE = "$hide";
	
	
	public static final String SOURCE_MD = "MD";
	public static final String SOURCE_ENUM = "EM";
	
    /**����ģ�����ֶε�3���Զ����ֶΣ�ͨ�����������Dataset.getProperty()ȡ��*/
    public static String BILL_TEMPLATE_FIELD_DEF1 = "$bill_template_field_def1";
    public static String BILL_TEMPLATE_FIELD_DEF2 = "$bill_template_field_def2";
    public static String BILL_TEMPLATE_FIELD_DEF3 = "$bill_template_field_def3";
    
    public static String DEFAULT_VALUE_SYSDATE = "@SYSDATE";
	
    /**
     * ���ݴ��ֶο������ɱ���ֶ�
     */
    private String sourceField;
    
	private String langDir;
	/*��ʶ*/
	private String id;
	
	/*��ǩ*/
	private String i18nName;
	
	private String text;
	
	/*�Ƿ�ɿ�*/
	private boolean nullAble = true;
	
	/*��������*/
	private String dataType;
	
	/*����������ݿ��VO��Ӧʱ����Ӧ���ֶλ���������*/
	private String field;
	
	/*���ֶε�ȱʡֵ*/
	private Object defaultValue = null; 
	
	/*���ڴ���ֶε�������Ϣ*/ 
	//private Map<String, Property> fieldProperty;
	/**/
	private boolean isPrimaryKey = false;
	
	private String editFormular;
	
	private String loadFormular;
	
	private String idColName;
	
	/*�Ƿ������ֶ�*/
	private boolean isLock = false;

	//ͬһ���ݼ���ҳǩ��ʾʱ��������ҳǩ�ı�ʶ,ֻ�������˹�ʽʱʹ�á������������������һ�飬��ʱû����������
	private String formularTabCode;
	
	//�Զ���༭��ʽ������ģ����Զ��������ã��Ӷ�Ӧ��field��ȡ��������ֶ�
	private String defEditFormular;
	//��֤��ʽ
	private String validateFormula; 
	
	/*���ֶε�У�����ͣ�email��number��chn��variable��date��phone;
	     û�������򲻻�У�顢����������������Ϊ��������ʽ;
	*/
	private String formater;
	/*����*/
	private String precision;
	/*���ֵ*/
	private String maxValue;
	/*context�Ƿ�ı�*/
	private boolean ctxChanged = false;
	/*�Ѹı��context�����б�*/
	private List<String> ctxChangedProperties = new ArrayList<String>();
	
	/*��ʶ����չ�ֶ�ԭʼ��Դ*/
	private String extSource;
	/*��ʶ����չ�ֶ�ԭʼ��Դ����*/
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
	 * ȱʡ����
	 *
	 */
	public Field(){}
	
	/**
	 * ���ι���
	 * @param id
	 */
	public Field(String id){
		
		this.id = id;
	}
	
	/**
	 * �����ֶα�ʶ
	 * @param id
	 */
	public void setId(String id){
		
		this.id = id;
	}

	/**
	 * ��ȡ�ֶα�ʶ
	 * @return
	 */
	public String getId(){
		
		return this.id;
	}

	/**
	 * ��ȡLabel
	 * @return
	 */
	public String getI18nName(){
		
		return this.i18nName;
	}

	/**
	 * ����Label��Ϣ
	 * @param label
	 */
	public void setI18nName(String label){
		
		this.i18nName = label;
	}

	/**
	 * ���ֶ��Ƿ����Ϊ��
	 * @return
	 */
	public boolean isNullAble(){
		
		return this.nullAble;
	}

	/**
	 * �����Ƿ�ɿձ�־λ
	 * @param nullAble
	 */
	public void setNullAble(boolean nullAble){
		
		this.nullAble = nullAble;
	}

	/**
	 * ������������
	 * @param type
	 */
	public void setDataType(String type){
		
		this.dataType = type;
	}

	/**
	 * ��ȡ��������
	 * @return
	 */
	public String getDataType(){
		
		return this.dataType;
	}

	/**
	 * �����ֶλ���������
	 * @return
	 */
	public String getField() {
		return field;
	}

	/**
	 * ��ȡ�ֶλ���������
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
	 * ʵ�ֿ�¡����
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
	 * �����Ѹı��context����
	 * @param propertyName
	 */
	public void addCtxChangedProperty(String propertyName) {
		this.ctxChangedProperties.add(propertyName);
	}
	
	/**
	 * ����Ѹı��context�����б�
	 */
	public void clearCtxChangedProperties() {
		this.ctxChangedProperties.clear();
	}
	
	/**
	 * �鿴context��ĳ�������Ƿ����仯
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
