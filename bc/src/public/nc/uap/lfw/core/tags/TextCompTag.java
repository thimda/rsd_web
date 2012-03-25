package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.comp.ReferenceComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.text.ComboBoxComp;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.reference.util.LfwRefUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

/**
 * 文件编辑框的Tag，根据不同类型生成不同的文本控件
 * 
 * @author gd 2007-10-10 加入控件宽度设置,如果不是设置为百分比宽度则设置宽度属性值
 * 
 */
public class TextCompTag extends NormalComponentTag {
	
	public TextCompTag() {
		setWidth("120");
		setHeight("22");
	}
	
	public String generateBodyScript() {
		WebComponent component = this.getComponent();
		if (!(component instanceof TextComp))
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "TextCompTag-000000", null, new String[] { this.getId() }));  // 标签配置出错，{0}不是TextComp类型！

		TextComp text = (TextComp) component;
		StringBuffer textBuf = new StringBuffer();
		String type = text.getEditorType();
		String id = getVarShowId();
		textBuf.append("var ").append(id).append(" = new ");

		if (text.isShowMark())
			textBuf.append("TextMarkComp(");
		else if (type.equals(EditorTypeConst.INTEGERTEXT))
			textBuf.append("IntegerTextComp(");
		else if (type.equals(EditorTypeConst.DECIMALTEXT))
			textBuf.append("FloatTextComp(");
		else if (type.equals(EditorTypeConst.PWDTEXT))
			textBuf.append("PswTextComp(");
		else if (type.equals(EditorTypeConst.DATETEXT) || type.equals(EditorTypeConst.DATETIMETEXT))
			textBuf.append("DateTextComp(");
		else if (type.equals(EditorTypeConst.FILEUPLOAD))
			textBuf.append("FileUploadComp(");
		else if (type.equals(EditorTypeConst.REFERENCE))
			textBuf.append("ReferenceTextComp(");
		else
			textBuf.append("StringTextComp(");

//		// 如果设置宽度,并且不是百分比形式则设置控件宽度
//		String width = component.getWidth();
//		if (width.indexOf("%") == -1)
//			width = component.getWidth();

		textBuf.append("document.getElementById('" + getDivShowId())
		       .append("'), '" + this.getId())
		       .append("', '0', '0', '")
		       .append(getWidth())
		       .append("',");
		textBuf.append("'relative'");
		if (type.equals(EditorTypeConst.INTEGERTEXT)) {
			String maxValue = text.getMaxValue();
			String minValue = text.getMinValue();
			if (maxValue != null)
				textBuf.append(", '" + maxValue + "'");
			else
				textBuf.append(", null ");
			if (minValue != null)
				textBuf.append(", '" + minValue + "'");
			else
				textBuf.append(", null ");
		} else if (type.equals(EditorTypeConst.DECIMALTEXT)) {
			String pre = text.getPrecision();
			if (pre != null)
				textBuf.append(", '" + pre + "'");
			else
				textBuf.append(", null");
		} else if (type.equals(EditorTypeConst.COMBODATA)) {
			textBuf.append(", " + ((ComboBoxComp) text).isSelectOnly() + "");
		}
		String attrArr = null;
		if (!text.isShowMark())
			attrArr = generateAttrArr(text);
		if (null != attrArr)
			textBuf.append(", ").append(attrArr);
		if (type.equals(EditorTypeConst.REFERENCE)) {
			if (attrArr == null)
				textBuf.append(", null");
			ReferenceComp reference = (ReferenceComp) text;
			String refcode = reference.getRefcode();
			if (refcode != null) {
				String refId = RF_PRE + getCurrWidget().getId() + "_" + refcode;
				textBuf.append(", " + refId);
			} else
				textBuf.append(", null");
		}
		textBuf.append("); \n");
		textBuf.append("pageUI.getWidget('" + this.getCurrWidget().getId() + "').addComponent(" + id + ");\n");

		if (!text.isShowMark()) {
			if (type.equals(EditorTypeConst.REFERENCE)) {
				ReferenceComp reference = (ReferenceComp) text;
				String value = reference.getValue();
				if (value != null && !value.equals("")) {
					//确保显示值
					LfwRefUtil.fetchRefShowValue(getCurrWidget(), reference);
					textBuf.append(id)
					       .append(".setValue('")
					       .append(value)
					       .append("');\n");
	
					textBuf.append(id)
					       .append(".setShowValue('")
					       .append(reference.getShowValue())
					       .append("');\n");
				}
			}
			
			if (type.equals(EditorTypeConst.DATETIMETEXT)) {
				textBuf.append(id)
			           .append(".setShowTimeBar(true);");
				textBuf.append(id)
				.append(".setValue('")
			       .append(text.getValue())
			       .append("');\n");
				
			}
	
			if (text.isReadOnly() == true)
				textBuf.append(id)
			       .append(".setReadOnly(true);\n");
			
			if (text.isEnabled() == false)
				textBuf.append(id)
			       .append(".setActive(false);\n");
			
			if (text.isVisible() == false)
				textBuf.append(id)
			       .append(".hideV();\n");
		}
		return textBuf.toString();
	}

	private String generateAttrArr(TextComp text) {
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		if (text.getTip() != null && !"".equals(text.getTip())) {
			buf.append("'tip':'")
			   .append(text.getTip())
			   .append("'");
		}
		if (text.getValue() != null && !"".equals(text.getValue())) {
			String value = text.getValue();
			if(value!=null&&value.length()!=0){
				value=value.trim();
				value=value.replaceAll("\r\n", "\n");
				value=value.replaceAll("\\n", "\r\n");
				value=value.replaceAll("\r\n", "\\\\\\r\\\\\\n");
				value=value.replaceAll(",", "\\,");
			}
			if (text.getEditorType().equals(EditorTypeConst.DATETEXT)){
				UFDate date = UFDate.fromPersisted(value.trim().length() == 10 ? value + " 00:00:00" : value);
				value = String.valueOf(date.getMillis());
			}
			else if (text.getEditorType().equals(EditorTypeConst.DATETIMETEXT)){
				UFDateTime dateTime = new UFDateTime(value.trim().length() == 10 ? value.trim() + " 00:00:00" : value.trim()); 
				value = String.valueOf(dateTime.getMillis());
			}
			if (buf.length() > 1)
				buf.append(",");
			buf.append("'value':'")
			   .append(value)
			   .append("'");
		}
		if (null != text.getText() && !"".equals(text.getText())) { // 有标签属性
			if (buf.length() > 1)
				buf.append(",");
			buf.append("'labelText':'")
			   .append(translate(text.getI18nName(), text.getText(), text.getLangDir()))
			   .append("','labelAlign':'")
			   .append(text.getTextAlign())
			   .append("','labelWidth':")
			   .append(text.getTextWidth());

		}
//		if (text.getHeight() != null && !"100%".equals(text.getHeight())) {
		if (buf.length() > 1)
			buf.append(",");
		buf.append("'height':'")
		   .append(getHeight())
		   .append("'");
//		}
		buf.append("}");
		return buf.toString();
	}


	public String generateBody() {
//		 TextComp text = (TextComp) getComponent();
//		 // 如果设置宽度,并且不是百分比形式则设置控件宽度
//		 String width = null;
//		 if(text.getWidth().indexOf("%") == -1)
//		 {
//		 width = text.getWidth();
//		 }
//				
//		 StringBuffer buf = new StringBuffer();
//		 buf.append("<div id=\"")
//		 .append(DIV_PRE)
//		 .append(text.getId())
//		 .append("\" style=\"width:")
//		 .append(width == null? 100:width)
//		 .append(";")
//		 .append("height:22px\"></div>\n");
//				
//		 String type = text.getEditorType();
//		 if(type.equals(EditorTypeConst.FILEUPLOAD)){
//		 if(getPageContext().getAttribute("hasUploadFrame") == null){
//		 getPageContext().setAttribute("hasUploadFrame", "true");
//		 buf.append("<iframe style='width:100px;height:0px;' id='$uploadFrame' name='$uploadFrame'></iframe>");
//		 }
//		 }
//		 return buf.toString();
		return super.generateBody();
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_TEXT;
	}
}
