package nc.uap.lfw.ra.render.pc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
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
import nc.uap.lfw.jsp.uimeta.UIDialog;
import nc.uap.lfw.jsp.uimeta.UIFormComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.itf.IUIRender;
import nc.uap.lfw.ra.render.UIComponentRender;
import nc.uap.lfw.ra.render.UIContainerComponentRender;
import nc.uap.lfw.ra.render.UIRender;


/**
 * @author renxh 表单渲染器
 * @param <T>
 * @param <K>
 */
public class PCFormCompRender extends UIContainerComponentRender<UIFormComp, FormComp> {

	public static final String LABEL_POSITION = "label_position";
	public static final String IMG_TABLE_NAME = "tn";
	public static final String IMAGE_BEAN_ID = "beanid";
	public static final String IMAGE_ONSQL = "onsql";
	public static final String IMG_DATA_FIELD = "df";
	public static final String IMG_PK_FIELD = "pf";
	public static final String IMG_PK_VALUE = "pv";
	public static final String IMG_URL = "IMG_URL";
	public static final String IMG_HEIGHT = "IMG_HEIGHT";
	public static final String IMG_WIDTH = "IMG_WIDTH";
	
	public PCFormCompRender(UIFormComp uiEle, FormComp webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<?, ?> parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);
	}

	// 元素默认行宽
	private static final int ELEMENT_WIDTH = 120;
	// 元素默认行高
	private static final int ELEMENT_HEIGHT = 20;

	public String generalHeadHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append("<div id=\"").append(getDivId());
		buf.append("\" style=\"top:0px;left:0px;width:100%;height:100%;overflow:auto;\">\n");
		buf.append(this.generalEditableHeadHtml());
		return buf.toString();
	}

	public String generalHeadHtmlDynamic() {

		FormComp form = this.getWebElement();
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getDivId()).append(" = $ce('DIV');\n");
		buf.append(getDivId()).append(".id = '" + getDivId() + "';\n");

		buf.append(super.generalHeadHtmlDynamic());
		buf.append(getDivId()).append(".style.overflowY = 'auto';\n");
		// 子元素的渲染
		Dataset ds = getDataset();
		List<FormElement> list = form.getElementList();
		Iterator<FormElement> it = list.iterator();
		while (it.hasNext()) {
			FormElement ele = it.next();
			if (ele.isVisible() == false)
				continue;
			fillDataTypeAndEditorType(ds, ele);// 为 ele指定类型

			if (false && ele.getEditorType().equals(EditorTypeConst.SELFDEF)) {
				String bindId = ele.getBindId();
				if (bindId != null && !bindId.equals("")) {
					// 渲染控件
					WebComponent comp = getCurrWidget().getViewComponents().getComponent(bindId);
					comp.setId(bindId);
					IUIRender compRender = ControlFramework.getInstance().getUIRender(getUiMeta(), null, comp, getPageMeta(), this, DriverPhase.PC);
					if(compRender instanceof UIComponentRender){
						try {
							buf.append(compRender.createRenderHtmlDynamic());
							buf.append("if("+ compRender.getDivId() + ")");
							buf.append(getDivId()+".appendChild("+ compRender.getDivId()+");\n");
						} catch (Exception e) {
							Logger.error(e.getMessage(), e);
							throw new LfwRuntimeException("解析Form自定义控件出错");
						}
					}
				}
			}
		}

		// buf.append("</div>");
		return buf.toString();
	}

	public String generalHeadScriptDynamic() {
		return generalHeadScript(true);
	}

	public String generalHeadScript() {
		return generalHeadScript(false);
	}

	private String generalHeadScript(boolean isDynamic) {

		FormComp form = this.getWebElement();
//		boolean hasWebPart = false;
		UIComponent uiComp = this.getUiElement();
		StringBuffer buf = new StringBuffer();
		String formId = getVarId();
		String widget = WIDGET_PRE + this.getCurrWidget().getId();
		buf.append("var ").append(widget).append(" = pageUI.getWidget('" + this.getCurrWidget().getId() + "');\n");
		buf.append("var ").append(formId).append(" = new AutoFormComp($ge('").append(getDivId()).append("')");
		buf.append(", \"").append(getId());

		buf.append("\",").append(form.getRenderType());
		buf.append(",").append(false);
		buf.append(",").append(form.getRowHeight()).append(",").append(form.getColumnCount());
		String attrArr = generateAttrArr(form, uiComp);
		buf.append(",").append(attrArr).append(");\n");
		Dataset ds = getDataset();
		List<FormElement> list = form.getElementList();

		Iterator<FormElement> it = list.iterator();
		// 当前form所属widget

		buf.append(formId + ".widget = " + widget + ";\n");

		// form自定义元素在产生form控件时自动添加到pageContext中,供jsp页面取用
		List<String> selDefEleIds = new ArrayList<String>(10);
		while (it.hasNext()) {
			FormElement ele = it.next();
			if (ele.isVisible() == false) // 隐藏的不需要渲染
				continue;
			fillDataTypeAndEditorType(ds, ele);// 为 ele指定类型
			if (ele.getEditorType() != null && ele.getEditorType().equals(EditorTypeConst.SELFDEF))
				selDefEleIds.add(ele.getId());

			String eleId = ele.getId() + "_ele";
			buf.append("var " + eleId + " = " + getVarId()).append(".createElement(\"").append(ele.getId()).append("\",\"").append(ele.getField()).append("\",\"");
			String eleWidth = ele.getWidth();//"120";
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
			String editorType = ele.getEditorType();
			buf.append(editorType).append("\",");
			if (editorType.equals(EditorTypeConst.REFERENCE)) {
				StringBuffer userObj = new StringBuffer();
				userObj.append("{");
//				RefNode refNode = (RefNode) getCurrWidget().getViewModels().getRefNode(ele.getRefNode());
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
			} else if (editorType.equals(EditorTypeConst.STRINGTEXT)) {
				StringBuffer userObj = new StringBuffer();
				userObj.append("{maxLength:");
				String maxLength = ele.getMaxLength();
				if (maxLength == null || maxLength.trim().equals(""))
					maxLength = getFieldProperty(ele.getField(), Field.MAX_LENGTH);
				if (maxLength == null || maxLength.trim().equals(""))
					maxLength = null;
				userObj.append(maxLength).append(",visible:").append(ele.isVisible()).append("}");
				buf.append(userObj.toString());
			} else if (editorType.equals(EditorTypeConst.FILECOMP)) {
				StringBuffer userObj = new StringBuffer();
				userObj.append("{visible:").append(ele.isVisible()).append(",sizeLimit:").append(ele.getSizeLimit() + "}");
				buf.append(userObj.toString());
			} else if (editorType.equals(EditorTypeConst.IMAGECOMP)) {
				StringBuffer userObj = new StringBuffer();
				userObj.append("{url:'").append(getFieldProperty(ele.getField(), IMG_URL)).append("', width:'").append(
						getFieldProperty(ele.getField(), IMG_WIDTH)).append("',height:'").append(
						getFieldProperty(ele.getField(), IMG_HEIGHT)).append("',pkfield:'").append(
						getFieldProperty(ele.getField(), IMG_PK_FIELD)).append("'}");
				buf.append(userObj.toString());
			}

			else if (editorType.equals(EditorTypeConst.INTEGERTEXT)) {
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
			} else if (editorType.equals(EditorTypeConst.DECIMALTEXT)) {

				String precision = ele.getPrecision();
				if (precision == null || precision.trim().equals(""))
					precision = getFieldProperty(ele.getField(), Field.PRECISION);
				if (precision == null || precision.trim().equals(""))
					precision = null;

				StringBuffer userObj = new StringBuffer();
				userObj.append("{precision:'").append(precision).append("',visible:").append(ele.isVisible()).append("}");
				buf.append(userObj.toString());
			} else if (editorType.equals(EditorTypeConst.COMBODATA) || editorType.equals(EditorTypeConst.RADIOGROUP)
					|| editorType.equals(EditorTypeConst.CHECKBOXGROUP) || editorType.equals(EditorTypeConst.LIST)) {
				StringBuffer userObj = new StringBuffer();
				userObj.append("{comboData:").append(ele.getRefComboData() == null ? "null" : (COMBO_PRE + getCurrWidget().getId() + "_" + ele.getRefComboData())).append(
						",imageOnly:").append(ele.isImageOnly()).append(",selectOnly:").append(ele.isSelectOnly()).append(",dataDivHeight:").append(
						ele.getDataDivHeight() == null ? null : ele.getDataDivHeight()).append(",visible:").append(ele.isVisible()).append("}");

				buf.append(userObj.toString());
			} else if (editorType.equals(EditorTypeConst.CHECKBOX)) {
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
			} else if (editorType.equals(EditorTypeConst.RICHEDITOR)) {
				String toolbarType = ele.getToolbarType();
				StringBuffer userObj = new StringBuffer();
				userObj.append("{");
				if (toolbarType != null)
					userObj.append("toolbarType:'").append(toolbarType).append("'");
				userObj.append("}");
				buf.append(userObj.toString());
			} else
				buf.append("null");
			buf.append(",").append(!ele.isEnabled()).append(",").append(!ele.isEditable()).append(",");

			// 参照元素需要提前将Dataset传入
			if (editorType.equals(EditorTypeConst.REFERENCE))
				buf.append("\"" + this.getDataset().getId() + "\",");
			else
				buf.append("null,");

			String resId = ele.getI18nName();
			String simpchn = ele.getText() == null ? resId : ele.getText();
			String label = getFieldI18nName(resId, ele.getField(), simpchn, ele.getLangDir());
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

			String className = uiComp.getClassName();
			if (className != null && !className.equals("")) {
				buf.append("'").append(className).append("'\n");
			} 
			else
				buf.append(className);
			buf.append("); \n");
			buf.append(eleId + ".widget=" + widget + ";\n");

			// 自定义控件
			if (editorType.equals(EditorTypeConst.SELFDEF)) {
				String bindId = ele.getBindId();
				if (bindId == null || bindId.equals("")) {
					String ext = (String) ele.getExtendAttributeValue(FormElement.SELF_DEF_FUNC);
					if (ext != null) {
						buf.append(eleId).append(".setContent(").append(ext).append("());\n");
					}
				} 
				else {
					// 渲染控件 renxh此处需要研究处理
					WebComponent comp = getCurrWidget().getViewComponents().getComponent(bindId);
					comp.setId(bindId);
					IUIRender compRender = ControlFramework.getInstance().getUIRender(getUiMeta(), null, comp, getPageMeta(), this, DriverPhase.PC);
					if(compRender instanceof UIComponentRender){
						try {
							
								if (isDynamic) {
									buf.append(compRender.createRenderScriptDynamic());
								}
								else{
									String script = compRender.createRenderScript();
									buf.append(script);
								}
							
							
	
						} catch (Exception e) {
							Logger.error(e.getMessage(), e);
							throw new LfwRuntimeException("解析Form自定义控件出错");
						}
					}
				
					buf.append(eleId).append(".setContent(pageUI.getWidget('").append(this.widget).append("').getComponent('").append(((UIComponentRender<UIComponent, WebComponent>) compRender).getId()).append("'));\n");

				}
			}
		}
		this.setContextAttribute("selDefEleIds", selDefEleIds);
		// getJspContext().setAttribute("selDefEleIds", selDefEleIds);

		buf.append(widget + ".addComponent(" + getVarId() + ");\n");
		buf.append("if (IS_IE) try{" + getVarId() + ".pLayout.paint(true);}catch(e){}\n");
		return buf.toString();
	}

	
	public String generalTailHtml() {
		StringBuffer buf = new StringBuffer();
		// buf.append(this.generalEditableTailHtml());
		buf.append("</div>\n");
		return buf.toString();
	}

	public String generalTailScript() {
		return generalTailScript(false);
	}

	private String generalTailScript(boolean isDynamic) {
		FormComp form = this.getWebElement();
		StringBuffer buf1 = new StringBuffer();
		// form tag结束时移除"selDefEleIds"属性
		this.removeContextAttribute("selDefEleIds");
		// getJspContext().removeAttribute("selDefEleIds");
		if (form.getRenderType() == 1 || form.getRenderType() == 3) {
			buf1.append(generateTailScriptForType1(form));
		} else
			buf1.append(generateTailScriptForType2(form));

		StringBuffer buf = new StringBuffer();
		// buf.append("\n" +COMP_PRE)
		// .append(getId())
		buf.append("\npageUI.getWidget('"+ this.getCurrWidget().getId()+"').getComponent('"+getId()+"').setDataset(");
		buf.append("pageUI.getWidget(\"" + getCurrWidget().getId() + "\").getDataset" + "(\"").append(form.getDataset());
		buf.append("\"));\n");

		if (!form.isEnabled()) {
			buf.append("\npageUI.getWidget('"+ this.getCurrWidget().getId()+"').getComponent('"+getId()+"').setEditable(false);\n");
//			buf.append(getVarId()).append(".setEditable(false);\n");
		}
		String script = buf.toString();
		
		LfwWidget widget = getCurrWidget();
		UIDialog dialog = (UIDialog) getUiMeta().getDialog(widget.getId());
//		if (isDynamic)
		if (isDynamic || dialog != null || widget.isDialog())
			buf1.append(script);
		else {
			// StringBuffer dsScript = (StringBuffer)
			// getJspContext().getAttribute(PageModelTag.DS_SCRIPT); 被下面的代码替换

			StringBuffer dsScript = (StringBuffer) this.getContextAttribute(DS_SCRIPT);
			if (dsScript == null) {
				dsScript = new StringBuffer();
				this.setContextAttribute(DS_SCRIPT, dsScript);
			}
			if (script != null)
				dsScript.append(script);
		}

		return buf1.toString();
	}

	public String generalTailScriptDynamic() {
		return generalTailScript(true);// 没有变动，所以调用原始的方法 renxh
	}

	private String generateTailScriptForType2(FormComp form) {
		return "";
	}

	private String generateTailScriptForType1(FormComp form) {
		return "";
	}

	/**
	 * 根据dataset中的数据类型填充FormElement的dataType，并且获得对应的EditorType
	 * 
	 * @param ds
	 * @param ele
	 * @return
	 */
	protected void fillDataTypeAndEditorType(Dataset ds, FormElement ele) {
		if (ele.getField() != null) {
			String eleDt = ele.getDataType();
			if (eleDt == null || eleDt.equals("")){
				Field f = ds.getFieldSet().getField(ele.getField());
				if(f == null){
					LfwLogger.error("从Dataset:" + ds.getId() + "中获取Field:" + ele.getField() + "出错");
					ele.setDataType(StringDataTypeConst.STRING);
				}
				else
					ele.setDataType(ds.getFieldSet().getField(ele.getField()).getDataType());
			}
			
			String editorType = ele.getEditorType();
			if (editorType == null || editorType.trim().equals(""))
				ele.setEditorType(EditorTypeConst.getEditorTypeByString(ele.getDataType()));
		}
	}

	/**
	 * 2011-8-2 下午08:18:06 renxh des：根据字段的id和属性名称获得属性值
	 * 
	 * @param fieldId
	 * @param name
	 * @return
	 */
	protected String getFieldProperty(String fieldId, String name) {
		Dataset ds = getDataset();
		Field field = ds.getFieldSet().getField(fieldId);
		if (field == null)
			return null;
		return (String) field.getExtendAttributeValue(name);
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_FORMCOMP;
	}

	@Override
	protected Dataset getDataset() {
		return super.getDataset();
	}

	// 删除form中的元素
	@Override
	public void notifyRemoveChild(UIMeta uimeta, PageMeta pagemeta, Object obj) {
		if (obj instanceof FormElement) { // 表格列属性
			FormElement element = (FormElement) obj;
			FormComp form = element.getParent();
			StringBuffer buf = new StringBuffer();
			String widget = form.getWidget() != null ? form.getWidget().getId() : this.getWidget();
			buf.append("window.execDynamicScript2RemoveFormElement('" + widget + "','" + form.getId() + "','" + element.getField() + "');");
			form.removeElement(element);
			addDynamicScript(buf.toString());
		}
	}

	@Override
	protected String getFieldI18nName(String name, String fieldId, String defaultI18nName, String langDir) {
		return super.getFieldI18nName(name, fieldId, defaultI18nName, langDir);
	}


	@Override
	protected String setWidgetToComponent() {
		return "pageUI.getWidget('" + this.getWidget() + "').getComponent('" + this.getId() + "').widget = " + WIDGET_PRE + this.getWidget() + "\n";
	}

	private String generateAttrArr(FormComp form, UIComponent uiComp) {
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		if(form != null){
			buf.append("'eleWidth':'").append(form.getEleWidth()).append("'");
		}
		if(uiComp != null){
			if(uiComp.getAttribute(LABEL_POSITION) != null){
				if (buf.length() > 1)
					buf.append(",");
				buf.append("'labelPosition':'").append(uiComp.getAttribute(UIFormComp.LABEL_POSITION)).append("'");
			}
		}
		buf.append("}");
		return buf.toString();
	}
	
}
