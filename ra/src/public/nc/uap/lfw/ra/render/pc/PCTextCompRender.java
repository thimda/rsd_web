package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.comp.ReferenceComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.text.ComboBoxComp;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.refnode.BaseRefNode;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UITextField;
import nc.uap.lfw.ra.render.UINormalComponentRender;
import nc.uap.lfw.ra.render.UIRender;
import nc.uap.lfw.reference.util.LfwRefUtil;

/**
 * @author renxh
 * UITextField 渲染器 数字类型 密码类型  日期类型 等等
 */
public class PCTextCompRender extends UINormalComponentRender<UITextField ,TextComp> {


	public PCTextCompRender(UITextField uiEle, TextComp webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<? extends UIElement, ? extends WebElement> parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);
	}

	public String generateBodyHtml() {

		return super.generateBodyHtml();
	}
	@Override
	public String generateBodyHtmlDynamic() {

		return super.generateBodyHtmlDynamic();
	}

	public String generateBodyScript() {

		TextComp text = this.getWebElement();

		StringBuffer textBuf = new StringBuffer();
		String type = text.getEditorType();
		String id = this.getVarId();
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
		else if (type.equals(EditorTypeConst.FILECOMP))
			textBuf.append("FileComp(");
		else 
			textBuf.append("StringTextComp(");

		// 如果设置宽度,并且不是百分比形式则设置控件宽度
//		String width = text.getWidth();
//		if (width.indexOf("%") == -1)
//			width = text.getWidth();

		textBuf.append("document.getElementById('" + getDivId()).append("'), '" + this.getId()).append("', '0', '0', '100%','relative'");
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
			BaseRefNode refNode = (BaseRefNode)getCurrWidget().getViewModels().getRefNode(refcode);
			if (!refNode.isDialog())
				textBuf.append(", null, false");
		}
		textBuf.append("); \n");
		textBuf.append("var widget = pageUI.getWidget('" + this.getCurrWidget().getId() + "');\n");
		textBuf.append("widget.addComponent(" + id + ");\n");
		textBuf.append(id + ".widget = widget;\n");
		if (!text.isShowMark()) {
			if (type.equals(EditorTypeConst.REFERENCE)) {
				ReferenceComp reference = (ReferenceComp) text;
				String value = reference.getValue();
				if (value != null && !value.equals("")) {
					// 确保显示值
					LfwRefUtil.fetchRefShowValue(getCurrWidget(), reference);
					textBuf.append(id).append(".setValue('").append(value).append("');\n");

					textBuf.append(id).append(".setShowValue('").append(reference.getShowValue()).append("');\n");
				}
			}

			if (type.equals(EditorTypeConst.DATETIMETEXT)) {
				textBuf.append(id).append(".setShowTimeBar(true);");
				textBuf.append(id).append(".setValue('").append(text.getValue()).append("');\n");

			}

			if (text.isReadOnly() == true)
				textBuf.append(id).append(".setReadOnly(true);\n");

			if (text.isEnabled() == false)
				textBuf.append(id).append(".setActive(false);\n");

			if (text.isVisible() == false)
				textBuf.append(id).append(".hideV();\n");
		}
		return textBuf.toString();
	}
	

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_TEXT;
	}

	private String generateAttrArr(TextComp text) {
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		if (text.getTip() != null && !"".equals(text.getTip())) {
			buf.append("'tip':'").append(text.getTip()).append("'");
		}
		if (text.getValue() != null && !"".equals(text.getValue())) {
			if (buf.length() > 1)
				buf.append(",");
			buf.append("'value':'").append(text.getValue()).append("'");
		}
		if (null != text.getText() && !"".equals(text.getText())) { // 有标签属性
			if (buf.length() > 1)
				buf.append(",");
			buf.append("'labelText':'").append(text.getText()).append("','labelAlign':'").append(text.getTextAlign()).append("','labelWidth':")
					.append(text.getTextWidth());

		}
//		if (text.getHeight() != null && !"100%".equals(text.getHeight())) {
//			if (buf.length() > 1)
//				buf.append(",");
//			buf.append("'height':'").append(text.getHeight()).append("'");
//		}
		buf.append("}");
		return buf.toString();
	}

	public String getType(){
		TextComp t = getWebElement();
		if(t.getEditorType().equals(EditorTypeConst.REFERENCE)){
			return "reftext";
		}
		else if(t.getEditorType().equals(EditorTypeConst.COMBODATA)){
			return "combotext";
		}
		else if (t.getEditorType().equals(EditorTypeConst.STRINGTEXT)){
			return "stringtext";
		}
		else if (t.getEditorType().equals(EditorTypeConst.CHECKBOX)){
			return "checkboxtext";
		}
		else if (t.getEditorType().equals(EditorTypeConst.CHECKBOXGROUP)){
			return "checkboxgrouptext";
		}
		else if (t.getEditorType().equals(EditorTypeConst.INTEGERTEXT)){
			return "integertext";
		}
		else if (t.getEditorType().equals(EditorTypeConst.DATETEXT)){
			return "datetext";
		}
		else if (t.getEditorType().equals(EditorTypeConst.DECIMALTEXT)){
			return "floattext";
		}
		else if (t.getEditorType().equals(EditorTypeConst.FILECOMP)){
			return "filetext";
		}
		return getControlPlugin().getId();
	}
}
