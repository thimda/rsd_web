package nc.uap.lfw.core.comp.text;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.TextContext;
import nc.uap.lfw.core.event.conf.FocusListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.KeyListener;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.event.conf.TextListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
/**
 * Text控件基类
 * @author dengjt
 *
 */
public class TextComp extends WebComponent {
	
	private static final long serialVersionUID = 4555267873292876642L;
	private String value = "";
	private boolean readOnly = false;
	private String editorType = EditorTypeConst.STRINGTEXT;
	
	private String maxValue;
	private String minValue;//IntegerTextComp中用到
	
	private String precision;//FloatTextComp中用到。
	
	private String sizeLimit;
	
	public String getSizeLimit() {
		return sizeLimit;
	}
	public void setSizeLimit(String sizeLimit) {
		this.sizeLimit = sizeLimit;
	}

	// 是否聚焦
	private String i18nName;
	private String langDir;
	private String text;
	
	// 标签属性
	private boolean focus = false;
	private String textAlign = "left";
	private int textWidth = 0;
	
	// 是否替换为不可见图片显示
	private boolean showMark = false;

	public boolean isShowMark() {
		return showMark;
	}
	public void setShowMark(boolean showMark) {
		this.showMark = showMark;
	}
	public TextComp() {
		super();
//		setWidth("120");
//		setHeight("22");
	}
	public TextComp(String id) {
		super(id);
//		setWidth("120");
//		setHeight("22");
	}
	
	// 默认显示值（提示用，不是真实值）
	private String tip = null;
	
	public String getTextAlign() {
		return textAlign;
	}
	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
	}
	public int getTextWidth() {
		return textWidth;
	}
	public void setTextWidth(int textWidth) {
		this.textWidth = textWidth;
	}
	
	public String getI18nName() {
		return i18nName;
	}
	public void setI18nName(String name) {
		i18nName = name;
	}
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
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		setCtxChanged(true);
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
	public String getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(String maxValue) {
		if(maxValue != null && value.equals(this.value)){
			this.maxValue = maxValue;
			setCtxChanged(true);
		}
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
	public String getEditorType() {
		return editorType;
	}
	public void setEditorType(String type) {
		this.editorType = type;
	}
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}

	@Override
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(MouseListener.class);
		list.add(KeyListener.class);
		list.add(TextListener.class);
		list.add(FocusListener.class);
		return list;
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
	
	@Override
	public BaseContext getContext() {
		TextContext textCtx = new TextContext();
		this.getContext(textCtx);
//		textCtx.setReadOnly(this.isReadOnly());
//		textCtx.setValue(this.getValue());
//		textCtx.setEnabled(this.isEnabled());
//		textCtx.setFocus(this.isFocus());
//		textCtx.setVisible(this.isVisible());
		if(this.getEditorType().equals(EditorTypeConst.INTEGERTEXT)){
			textCtx.setMaxValue(this.getMaxValue());
			textCtx.setMinValue(this.getMinValue());
		}
		return textCtx;
	}
	
	//供继承类使用
	public void getContext(TextContext textCtx){
		textCtx.setReadOnly(this.isReadOnly());
		String value = this.getValue();
		//日期时间类型，要从时间值转到long
		if (value != null && !value.equals("")){
			if (this.editorType.equals(EditorTypeConst.DATETEXT)){
				UFDate date = UFDate.fromPersisted(value.length() == 10 ? value + " 00:00:00" : value);
				value = String.valueOf(date.getMillis());
			}
			else if (this.editorType.equals(EditorTypeConst.DATETIMETEXT)){
				UFDateTime dateTime = new UFDateTime(value.length() == 10 ? value + " 00:00:00" : value); 
				value = String.valueOf(dateTime.getMillis());
			}
		}
		textCtx.setValue(value);
		textCtx.setEnabled(this.isEnabled());
		textCtx.setFocus(this.isFocus());
		textCtx.setVisible(this.isVisible());
		textCtx.setMaxValue(this.getMaxValue());
		textCtx.setMinValue(this.getMinValue());
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		TextContext textCtx = (TextContext) ctx;
		String value = textCtx.getValue();
		//日期时间类型，要从long值转到时间
		if (value != null && !value.equals("")){
			if (this.editorType.equals(EditorTypeConst.DATETEXT)){
				UFDate date = new UFDate(Long.parseLong(value));
				value = date.toString(); 
			}
			else if (this.editorType.equals(EditorTypeConst.DATETIMETEXT)){
				UFDateTime dateTime = new UFDateTime(Long.parseLong(value)); 
				value = dateTime.toString();
			}
		}
		this.setValue(value);
		this.setReadOnly(textCtx.isReadOnly());
		this.setEnabled(textCtx.isEnabled());
		this.setVisible(textCtx.isVisible());
		String maxValue = textCtx.getMaxValue();
		if(maxValue != null && !"".equals(maxValue)){
			if(this.getEditorType() == EditorTypeConst.INTEGERTEXT){
				this.setMaxValue(textCtx.getMaxValue());
			}
		}
		String minValue = textCtx.getMinValue();
		if(minValue != null && !"".equals(minValue)){
			if(this.getEditorType() == EditorTypeConst.INTEGERTEXT){
				this.setMinValue(textCtx.getMinValue());
			}
		}
		
		this.setCtxChanged(false);
	}
	
	public void validate(){
		StringBuffer buffer = new StringBuffer();
		if(this.getId() == null || this.getId().equals("")){
			buffer.append("文本框的ID不能为空!\r\n");
		}
//		if(this.getText() == null || this.getText().equals("")){
//			buffer.append("文本框Text不能为空!\r\n");
//		}
		if(buffer.length() > 0)
			throw new  LfwRuntimeException(buffer.toString());
	}
}
