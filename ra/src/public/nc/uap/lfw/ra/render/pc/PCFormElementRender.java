package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.base.ExtendAttributeSupport;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.IDataBinding;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIFormElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UpdatePair;
import nc.uap.lfw.ra.render.RenderHelper;
import nc.uap.lfw.ra.render.UINormalComponentRender;
import nc.uap.lfw.ra.render.UIRender;

public class PCFormElementRender extends UINormalComponentRender<UIFormElement, FormElement> {

	// 元素默认行宽
	private static final int ELEMENT_WIDTH = 120;
	// 元素默认行高
	private static final int ELEMENT_HEIGHT = 20;
	
	private String formId = null;
	private String varFormId = null;
	private FormComp form = null;
	private PCFormCompRender formRender = null;

	public PCFormElementRender(UIFormElement uiEle, FormElement webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<?, ?> parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);
		this.id = webEle.getId();
		this.divId = DIV_PRE + this.id + "_ele";
		this.varId = COMP_PRE + this.id + "_ele";
		this.formId = uiEle.getFormId();
		if (widget == null) {
			widget = uiEle.getWidgetId();
		}
		if (this.formId == null || this.formId.equals("")) {
			throw new LfwRuntimeException("formId 不能为 null");
		}

		if (this.form == null) {
			if (pageMeta != null && this.widget != null) {
				LfwWidget oWidget = pageMeta.getWidget(this.widget);
				if (oWidget != null) {
					this.form = (FormComp) oWidget.getViewComponents().getComponent(formId);
				}
			}
		}
		if (this.form != null) {
			if(!this.form.isRendered()){
				this.form.setRendered(true);
				formRender = new PCFormCompRender(null, this.form, uimeta, pageMeta, null);
				this.form.setExtendAttribute(ExtendAttributeSupport.DYN_ATTRIBUTE_KEY + "_formId", formRender.getVarId());
			}
		} 
		else {
			throw new LfwRuntimeException("form 不能为 null");
		}
		varFormId = (String) this.form.getExtendAttributeValue(ExtendAttributeSupport.DYN_ATTRIBUTE_KEY + "_formId");
	}

	@Override
	public String generateBodyHtml() {
//		StringBuffer buf = new StringBuffer();
//		buf.append("<div style=\"width:100%");
//		buf.append(";height:100%");
//		buf.append(";top:0px");
//		buf.append(";left:0px");
//		buf.append(";position:relative");
//		buf.append(";overflow:hidden");
//		buf.append("\" id=\"").append(getDivId()).append("\">\n");
//		buf.append("</div>\n");
//		return buf.toString();
		return super.generateBodyHtml();
	}

	@Override
	public String generateBodyHtmlDynamic() {
//		StringBuffer buf = new StringBuffer();
//		buf.append("var ").append(getDivId()).append(" = $ce('DIV');");
//		buf.append(getDivId()).append(".style.width = '100%';");
//		buf.append(getDivId()).append(".style.height = '100%';");
//		;
//		buf.append(getDivId()).append(".style.top = '0px';");
//		buf.append(getDivId()).append(".style.left = '0px';");
//		buf.append(getDivId()).append(".style.position = 'relative';");
//		buf.append(getDivId()).append(".style.overflow = 'hidden';");
//		buf.append(getDivId()).append(".id = '").append(getDivId()).append("';");
//		return buf.toString();
		return super.generateBodyHtmlDynamic();
	}
	public String generalEditableTailScript() {
		StringBuffer buf = new StringBuffer();
		if(this.isEditMode()){
			if(this.getWidget() != null && LfwRuntimeEnvironment.isWindowEditorMode()){
				return "";
			}
			String widgetId = this.getWidget() == null ? "" : this.getWidget();
			String uiid = this.getUiElement() == null ? "" : (String) this.getUiElement().getId();
			String eleid = form.getId() == null ? "" : form.getId();
			String subeleid = this.getWebElement().getId() == null ? "" : this.getWebElement().getId();
			String type = this.getRenderType(this.getWebElement());
			if (type == null)
				type = "";
			
			if (getDivId() == null) {
				LfwLogger.error("div id is null!" + this.getClass().getName());
			} else {
				buf.append("var params = {widgetid:'" + widgetId + "',uiid:'" + uiid + "',eleid:'" + eleid + "',type:'" + type + "',subeleid:'" + subeleid + "',renderType:'"
						+ 4 + "'};\n");
				buf.append("new EditableListener(document.getElementById('" + getDivId() + "'),params,'component');\n");
			}
			if(this.getDivId() != null){
				buf.append("if(document.getElementById('").append(this.getDivId()).append("'))");
				buf.append("  document.getElementById('").append(this.getDivId()).append("').style.padding = '0px';\n");
			}
		}
		return buf.toString();
	}
	@Override
	public String generateBodyScript() {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(varFormId).append(" = pageUI.getWidget('").append(widget).append("').getComponent('").append(formId).append("');\n");
		buf.append("if(!").append(varFormId).append("){");
		buf.append(this.createForm(false));
		buf.append("}else{" + varFormId + ".renderType=4;\n};\n");
		buf.append(this.createFormElement(false));
		this.createFormElemntDs();
		return buf.toString();
	}

//	@Override
//	public String generateBodyScriptDynamic() {
//		StringBuffer buf = new StringBuffer();
//		buf.append("var ").append(varFormId).append(" = pageUI.getWidget('").append(widget).append("').getComponent('").append(formId).append("');\n");
//		buf.append("if(!").append(varFormId).append("){");
//		buf.append(this.createForm(true));
//		buf.append("};\n");
//		buf.append(this.createFormElement(true));
//		this.createFormElemntDs();
//		return buf.toString();
//	}

	private void fillDataTypeAndEditorType(Dataset ds, FormElement ele) {
		if (ele.getField() != null) {
			if (ele.getDataType() == null || ele.getDataType().trim().equals(""))
				ele.setDataType(ds.getFieldSet().getField(ele.getField()).getDataType());
			if (ele.getEditorType() == null || ele.getEditorType().trim().equals(""))
				ele.setEditorType(EditorTypeConst.getEditorTypeByString(ele.getDataType()));
		}
//		formRender.fillDataTypeAndEditorType(ds, ele);
	}

	private String getFieldProperty(String fieldId, String name) {
		Dataset ds = getDataset();
		Field field = ds.getFieldSet().getField(fieldId);
		if (field == null)
			return null;
		return (String) field.getExtendAttributeValue(name);
//		return formRender.getFieldProperty(fieldId, name);
	}

	private void createFormElemntDs() {
		PcFormRenderUtil.setFromDsScript(form.getId(), varFormId, widget, form.getDataset());
	}

	/**
	 * 创建formElement元素
	 * 
	 * @param isDynamic
	 * @return
	 */
	private String createFormElement(boolean isDynamic) {
		FormElement ele = this.getWebElement();
		UIFormElement uiEle = this.getUiElement();
		StringBuffer buf = new StringBuffer();
		LfwWidget currWidget = this.getCurrWidget();
		String widget = WIDGET_PRE + currWidget.getId();
		
		Dataset ds = currWidget.getViewModels().getDataset(this.form.getDataset());
		fillDataTypeAndEditorType(ds, ele);// 为 ele指定类型
		String eleId = varId;
		buf.append("var ").append(widget).append(" = pageUI.getWidget('").append(currWidget.getId()).append("');\n");
		buf.append("var " + eleId + " = " + varFormId).append(".createElement(\"").append(ele.getId()).append("\",\"").append(ele.getField()).append("\",\"");
		
//		String eleWidth = uiEle.getEleWidth();
//		buf.append(eleWidth).append("\",\"");
//		buf.append(uiEle.getHeight()).append("\",\"");
		
//		String eleWidth = ele.getWidth();//"120";
		String eleWidth = uiEle.getEleWidth();
		
		String eleHeight = ele.getHeight();//"20";
		if (eleWidth == null || eleWidth.equals("") || eleWidth.equals("0")) {
			int formEleWidth = form.getEleWidth();
			if (formEleWidth <= 0)
				eleWidth = ELEMENT_WIDTH + "";
			else
				eleWidth = form.getEleWidth() + "";
		}
		if (eleHeight == null || eleHeight.equals("") || eleHeight.equals("0")) {
			int formEleHeight = form.getRowHeight();
			if (formEleHeight <= 0)
				eleHeight = ELEMENT_HEIGHT + "";
			else
				eleHeight = form.getRowHeight() + "";
		}
		
		
		buf.append(eleWidth).append("\",\"");
		buf.append(eleHeight).append("\",\"");
		
		buf.append(ele.getRowSpan()).append("\",\"");
		buf.append(ele.getColSpan()).append("\",\"");
//		if (this.isEditMode() && ele.getEditorType().equals(EditorTypeConst.RICHEDITOR)) {
//			buf.append(EditorTypeConst.IMAGECOMP).append("\",");
//			// buf.append(ele.getEditorType()).append("\",");
//		} else {
		buf.append(ele.getEditorType()).append("\",");
//		}
		if (ele.getEditorType().equals(EditorTypeConst.REFERENCE)) {
			referenceObj(ele, buf);
		} else if (ele.getEditorType().equals(EditorTypeConst.STRINGTEXT)) {
			stringTextObj(ele, buf);
		} else if (ele.getEditorType().equals(EditorTypeConst.IMAGECOMP)) {
			imageCompObj(ele, buf);
		} else if (ele.getEditorType().equals(EditorTypeConst.INTEGERTEXT)) {
			integerTextObj(ele, buf);
		} else if (ele.getEditorType().equals(EditorTypeConst.DECIMALTEXT)) {
			decimalTextObj(ele, buf);
		} else if (ele.getEditorType().equals(EditorTypeConst.COMBODATA) || ele.getEditorType().equals(EditorTypeConst.RADIOGROUP)
				|| ele.getEditorType().equals(EditorTypeConst.CHECKBOXGROUP)) {
			combOrGroupObj(ele, buf);
		} else if (ele.getEditorType().equals(EditorTypeConst.CHECKBOX)) {
			checkboxObj(ele, buf);
		} else if (ele.getEditorType().equals(EditorTypeConst.RICHEDITOR)) {
			if (this.isEditMode()) {
				String imagePath = "/lfw/frame/themes/webclassic/images/editor/richEditor.gif";
				imageCompObj(ele, buf,imagePath,imagePath);
			} else {
				richEditorObj(ele, buf);
			}

		} else {
			buf.append("null");
		}
		buf.append(",").append(!ele.isEnabled()).append(",").append(!ele.isEditable()).append(",");

		// 参照元素需要提前将Dataset传入
		if (ele.getEditorType().equals(EditorTypeConst.REFERENCE))
			buf.append("\"" + this.getDataset().getId() + "\",");
		else
			buf.append("null,");

		String resId = ele.getI18nName();
		String simpchn = ele.getText() == null ? resId : ele.getText();
		String label = super.getFieldI18nName(resId, ele.getField(), simpchn, ele.getLangDir());
		label = label == null ? "" : label;
		buf.append("\"").append(label).append("\",");

		// labelColor
		if (ele.getLabelColor() != null)
			buf.append("'").append(ele.getLabelColor()).append("',");
		else
			buf.append("null,");

		if (ele.isNextLine())
			buf.append("true,");
		else
			buf.append("false,");

		buf.append(ele.isRequired()).append(",");

		if (ele.getTip() != null && !"".equals(ele.getTip())) {
			buf.append("'").append(ele.getTip()).append("',");
		} else {
			buf.append("null,");
		}

		if (ele.getInputAssistant() != null && !"".equals(ele.getInputAssistant())) {
			buf.append("'").append(ele.getInputAssistant()).append("',");
		} else {
			buf.append("null,");
		}
		if (ele.getDescription() != null && !"".equals(ele.getDescription())) {
			buf.append("'").append(ele.getDescription()).append("',");
		} else {
			buf.append("null,");
		}
		buf.append("'").append(ele.isAttachNext()).append("',");

		String className = uiEle.getClassName();
		if (className != null && !className.equals("")) {
			buf.append("'").append(className).append("'\n");
		} else {
			buf.append(className);
		}

		if (isDynamic) {
			buf.append("," + divId + " \n");
		} else {
			buf.append(",$ge('" + divId + "') \n");
		}
		
		buf.append(",\"" + uiEle.getWidth() + "\");\n");

		buf.append(eleId + ".widget=" + widget + ";\n");

		return buf.toString();
	}

	private void referenceObj(FormElement ele, StringBuffer buf) {
		StringBuffer userObj = new StringBuffer();
		userObj.append("{");
		IRefNode refNode = getCurrWidget().getViewModels().getRefNode(ele.getRefNode());
		if (refNode == null) {
			LfwLogger.error("Form Element 类型为参照，但是没设置参照节点,Element id:" + ele.getId());
			userObj.append("refNode:null");
			ele.setEnabled(false);
		} else {
			String refId = RF_PRE + getCurrWidget().getId() + "_" + refNode.getId();
			userObj.append("refNode:").append(refId);
		}
		userObj.append(",visible:").append(ele.isVisible()).append("}");
		buf.append(userObj.toString());
	}

	private void stringTextObj(FormElement ele, StringBuffer buf) {
		StringBuffer userObj = new StringBuffer();
		userObj.append("{maxLength:");
		String maxLength = ele.getMaxLength();
		if (maxLength == null || maxLength.trim().equals(""))
			maxLength = getFieldProperty(ele.getField(), Field.MAX_LENGTH);
		if (maxLength == null || maxLength.trim().equals(""))
			maxLength = null;
		userObj.append(maxLength).append(",visible:").append(ele.isVisible()).append("}");
		buf.append(userObj.toString());
	}

	private void imageCompObj(FormElement ele, StringBuffer buf, String refImage1, String refImage2) {
		StringBuffer userObj = new StringBuffer();
		userObj.append("{url:'").append(getFieldProperty(ele.getField(), PCFormCompRender.IMG_URL));
		userObj.append("', width:'").append(getFieldProperty(ele.getField(), PCFormCompRender.IMG_WIDTH));
		userObj.append("',height:'").append(getFieldProperty(ele.getField(), PCFormCompRender.IMG_HEIGHT));
		userObj.append("',pkfield:'").append(getFieldProperty(ele.getField(), PCFormCompRender.IMG_PK_FIELD));
		if (refImage1 != null && !refImage1.equals("")) {
			userObj.append("',refImage1:'").append(refImage1);
		}
		if (refImage2 != null && !refImage2.equals("")) {
			userObj.append("',refImage2:'").append(refImage2);
		}
		userObj.append("'}");
		buf.append(userObj.toString());
	}

	private void imageCompObj(FormElement ele, StringBuffer buf) {
		StringBuffer userObj = new StringBuffer();
		userObj.append("{url:'").append(getFieldProperty(ele.getField(), PCFormCompRender.IMG_URL)).append("', width:'").append(
				getFieldProperty(ele.getField(), PCFormCompRender.IMG_WIDTH)).append("',height:'").append(getFieldProperty(ele.getField(), PCFormCompRender.IMG_HEIGHT))
				.append("',pkfield:'").append(getFieldProperty(ele.getField(), PCFormCompRender.IMG_PK_FIELD)).append("'}");
		buf.append(userObj.toString());
	}

	private void integerTextObj(FormElement ele, StringBuffer buf) {
		String maxValue = ele.getMaxValue();
		String minValue = ele.getMinValue();
		StringBuffer userObj = new StringBuffer();
		userObj.append("{");
		if (maxValue == null || "".equals(maxValue))
			maxValue = getFieldProperty(ele.getField(), Field.MAX_VALUE);
		if (minValue == null || "".equals(minValue))
			minValue = getFieldProperty(ele.getField(), Field.MIN_VALUE);
		// if(maxValue != null && !maxValue.trim().equals(""))
		userObj.append("maxValue:").append(maxValue);
		// if(minValue != null && !minValue.trim().equals("")){
		// if(maxValue != null && !maxValue.trim().equals(""))
		userObj.append(",");
		userObj.append("minValue:").append(minValue);
		// }
		userObj.append(",visible:").append(ele.isVisible()).append("}");
		buf.append(userObj.toString());
	}

	private void decimalTextObj(FormElement ele, StringBuffer buf) {
		String precision = ele.getPrecision();
		if (precision == null || precision.trim().equals(""))
			precision = getFieldProperty(ele.getField(), Field.PRECISION);
		if (precision == null || precision.trim().equals(""))
			precision = null;

		StringBuffer userObj = new StringBuffer();
		userObj.append("{precision:'").append(precision).append("',visible:").append(ele.isVisible()).append("}");
		buf.append(userObj.toString());
	}

	private void combOrGroupObj(FormElement ele, StringBuffer buf) {
		StringBuffer userObj = new StringBuffer();
		userObj.append("{comboData:").append(ele.getRefComboData() == null ? "null" : (COMBO_PRE + getCurrWidget().getId() + "_" + ele.getRefComboData())).append(",imageOnly:")
				.append(ele.isImageOnly()).append(",selectOnly:").append(ele.isSelectOnly()).append(",dataDivHeight:").append(
						ele.getDataDivHeight() == null ? null : ele.getDataDivHeight()).append(",visible:").append(ele.isVisible()).append("}");

		buf.append(userObj.toString());
	}

	private void checkboxObj(FormElement ele, StringBuffer buf) {
		StringBuffer userObj = new StringBuffer();
		userObj.append("{valuePair:");
		// 设置comboData的情况
		StaticComboData data = (StaticComboData) getCurrWidget().getViewModels().getComboData(ele.getRefComboData());
		if (data != null) {
			CombItem[] items = data.getAllCombItems();
			if (items == null || items.length != 2)
				throw new LfwRuntimeException("The Combodata is not suitable for ele:" + ele.getId());
			userObj.append("[\"").append(items[0].getValue()).append("\",\"").append(items[1].getValue()).append("\"]");
		} else {
			// 没有设置comboData的情况
			String dataType = ele.getDataType();
			if (dataType.equals(StringDataTypeConst.BOOLEAN) || dataType.equals(StringDataTypeConst.bOOLEAN))
				userObj.append("[\"true\",\"false\"]");
			else if (dataType.equals(StringDataTypeConst.UFBOOLEAN))
				userObj.append("['Y','N']");
		}
		userObj.append(",visible:").append(ele.isVisible()).append("}");
		buf.append(userObj.toString());
	}

	private void richEditorObj(FormElement ele, StringBuffer buf) {
		String hideBarIndices = ele.getHideBarIndices();
		String hideImageIndices = ele.getHideImageIndices();
		buf.append("[");
		if (hideBarIndices != null && !"".equals(hideBarIndices))
			buf.append(hideBarIndices);
		else
			buf.append("");
		buf.append(",");
		if (hideImageIndices != null && !"".equals(hideImageIndices))
			buf.append(hideImageIndices);
		else
			buf.append("");
		buf.append("]");
	}

	private String createForm(boolean isDynamic) {
		FormComp form = this.form;
		StringBuffer buf = new StringBuffer();
		String widget = WIDGET_PRE + this.widget;
		if (isDynamic) {// 动态脚本
			buf.append("var ").append(widget).append(" = pageUI.getWidget('" + this.widget + "');\n");
			buf.append(varFormId).append(" = new AutoFormComp(").append(getDivId());
			buf.append(", \"").append(form.getId());
		} else {
			buf.append("var ").append(widget).append(" = pageUI.getWidget('" + this.widget + "');\n");
			buf.append(varFormId).append(" = new AutoFormComp($ge('").append(getDivId());
			buf.append("'), \"").append(form.getId());
		}

		buf.append("\",4,").append(form.isRenderHiddenEle());
		buf.append(",").append(form.getRowHeight()).append(",").append(form.getColumnCount()).append(");\n");
		buf.append(varFormId + ".widget = " + widget + ";\n");
		buf.append(widget + ".addComponent(" + varFormId + ");\n");
		return buf.toString();
	}

	@Override
	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_FORMELE;
	}

	@Override
	public void notifyDestroy(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		String divId = this.getDivId();
		UIFormElement uiEle = this.getUiElement();
		if (uiEle != null) {
			StringBuffer buf = new StringBuffer();
			if (divId != null) {
				buf.append("window.execDynamicScript2RemoveFormElement2('" + divId + "','" + uiEle.getWidgetId() + "','" + uiEle.getFormId() + "','" + uiEle.getId() + "');\n");
			} else {
				buf.append("alert('删除控件失败！未获得divId')");
			}
			addDynamicScript(buf.toString());
		}
	}

	@Override
	public String generalEditableTailScriptDynamic() {
		StringBuffer buf = new StringBuffer();
		if (this.isEditMode()) {
			if(this.getWidget() != null && LfwRuntimeEnvironment.isWindowEditorMode()){
				return "";
			}
			String widgetId = this.getWidget() == null ? "" : this.getWidget();
			String uiid = this.getUiElement() == null ? "" : (String) this.getUiElement().getId();
			String eleid = form.getId() == null ? "" : form.getId();
			String subeleid = this.getWebElement().getId() == null ? "" : this.getWebElement().getId();
			String renderType = "4";
			String type = this.getRenderType(this.getWebElement());
			if (type == null)
				type = "";

			if (getDivId() == null) {
				LfwLogger.error("div id is null!" + this.getClass().getName());
				throw new LfwRuntimeException("div id is null!" + this.getClass().getName());
			} else {
				buf.append("var params = {widgetid:'" + widgetId + "',uiid:'" + uiid + "',eleid:'" + eleid + "',type:'" + type + "',subeleid:'" + subeleid + "',renderType:'"
						+ renderType + "'};\n");
				buf.append("new EditableListener(" + this.getDivId() + ",params,'component');\n");
			}
			if (this.getDivId() != null) {
				buf.append("if(" + this.getDivId() + ")");
				buf.append(this.getDivId()).append(".style.padding = '0px';\n");
			}
		}
		return buf.toString();
	}

	/**
	 * 获得当前控件所绑定Dataset
	 * 
	 * @return
	 */
	protected Dataset getDataset() {
		WebComponent comp = this.form;
		if (!(comp instanceof IDataBinding))
			throw new LfwRuntimeException("the component is not type of IDataBinding:" + getId());
		Dataset ds = getDatasetById(((IDataBinding) comp).getDataset());
		if (ds == null)
			throw new LfwRuntimeException("can not find dataset by assigned id:" + ((IDataBinding) comp).getDataset());
		return ds;
	}

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		UpdatePair pair = (UpdatePair) obj;
		if(obj instanceof UpdatePair){
			if(pair.getKey().equals(UIComponent.WIDTH)){
				UIComponent uiObj = this.getUiElement();
				String width = uiObj.getWidth();
				if(width == null){
					return;
				}
				
				String formatWidth = RenderHelper.formatMeasureStr(width);
				String divId = getDivId();
				StringBuffer buf = new StringBuffer();
				buf.append("var ").append(divId).append(" = $ge('"+divId+"');\n");
				buf.append(divId).append(".style.width = '").append(formatWidth).append("';\n");
				buf.append("var currForm = pageUI.getWidget('" + this.widget + "').getComponent('" + form.getId() + "');\n");
				buf.append("currForm.updateType4Size('" + this.getId() + "', " + divId + ", null);\n");
				addDynamicScript(buf.toString());
			}
			
			else if(pair.getKey().equals(UIFormElement.ELEWIDTH)){
				UIFormElement uiObj = this.getUiElement();
				String width = uiObj.getEleWidth();
				if(width == null){
					return;
				}
				
//				String formatWidth = RenderHelper.formatMeasureStr(width);
				String divId = getDivId();
				StringBuffer buf = new StringBuffer();
				buf.append("var ").append(divId).append(" = $ge('"+divId+"');\n");
//				buf.append(divId).append(".style.width = '").append(formatWidth).append("';\n");
				buf.append("var currForm = pageUI.getWidget('" + this.widget + "').getComponent('" + form.getId() + "');\n");
				buf.append("currForm.updateType4Size('" + this.getId() + "', " + divId + ",'" + width + "');\n");
				addDynamicScript(buf.toString());
			}
			
			else if(pair.getKey().equals(UIComponent.HEIGHT)){
				UIComponent uiObj = this.getUiElement();
				String height = uiObj.getHeight();
				if(height == null){
					return;
				}
				
				height = RenderHelper.formatMeasureStr(height);
				String divId = getDivId();
				StringBuffer buf = new StringBuffer();
				buf.append("var ").append(divId).append(" = $ge('"+divId+"');\n");
				buf.append(divId).append(".style.height = '").append(height).append("';");
				addDynamicScript(buf.toString());
			}

		}
	}

	@Override
	public String createRenderScript() {
		return wrapByRequired("form", super.createRenderScript());
	}

	@Override
	public String createRenderScriptDynamic() {
		return wrapByRequired("form", super.createRenderScriptDynamic());
	}

}
