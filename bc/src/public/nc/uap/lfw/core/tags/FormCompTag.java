package nc.uap.lfw.core.tags;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.ImageComp;
import nc.uap.lfw.core.comp.LabelComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.refnode.IRefNode;

/**
 * 自动排版FormTag,将按照指定列数自动排版
 * 每一列的标签宽度会按照当列最宽标签宽度显示
 * @author dengjt 2007-1-25
 * @author gd 调整FormCompTag为容器型tag,可以包含体元素
 */
public class FormCompTag extends ContainerComponentTag {
	
	//每个字节占位宽度
	private static final int BYTE_WIDTH = 6;
	//标签距离编辑控件宽度
	private static final int SPACE_WIDTH = 8;
	//元素默认行宽
	private static final int ELEMENT_WIDTH = 120;
	//待替换标志
	private static final String REPLACE_SIGN = "TO_REPLACE_SIGN";
	
	public static final String IMG_TABLE_NAME = "tn";
	public static final String IMAGE_BEAN_ID = "beanid";
	public static final String IMAGE_ONSQL = "onsql";
	public static final String IMG_DATA_FIELD = "df";
	public static final String IMG_PK_FIELD = "pf";
	public static final String IMG_PK_VALUE = "pv";
	public static final String IMG_URL = "IMG_URL";
	public static final String IMG_HEIGHT = "IMG_HEIGHT";
	public static final String IMG_WIDTH = "IMG_WIDTH";
	
	/**
	 * 获得字符串显示宽度
	 * @param label
	 * @return
	 */
	private int getLabelWidth(String label)
	{
		FormComp form = (FormComp) getComponent();
		int minWidth = form.getLabelMinWidth();
		int calWidth = getTrueLabelWidth(label);
		if(minWidth != 0){
			if(minWidth > calWidth)
				calWidth = minWidth;
		}
		return calWidth;
	}
	
	private int getTrueLabelWidth(String label)
	{
		try {
			return SPACE_WIDTH + label.getBytes("GBK").length * BYTE_WIDTH;
		} catch (UnsupportedEncodingException e) {
			return SPACE_WIDTH + label.getBytes().length * BYTE_WIDTH;
		}
	}

	/**
	 * 确定控件在当前行中的位置
	 * @param nowIndex
	 * @param infoArr
	 * @return
	 */
	private int adjustIndex(int nowIndex,final RowSpanInfo[] infoArr)
	{
		if(infoArr == null)
			return nowIndex + 1;
		for (int i = 0; i < infoArr.length; i++) {
			if((nowIndex + 1) >= infoArr[i].index && (nowIndex + 1) < (infoArr[i].index + infoArr[i].size))
				nowIndex += infoArr[i].size;
		}
		return nowIndex + 1;
	}

	public String generateHead() {
		FormComp form = (FormComp) getComponent();
		StringBuffer buf = new StringBuffer();

//		String width = form.getWidth().toString().indexOf("%") == -1 ? form.getWidth() + "px" : form.getWidth();
//		String height = form.getHeight().toString().indexOf("%") == -1 ? form.getHeight() + "px" : form.getHeight();
		
		buf.append("<div id=\"")
		   .append(getDivShowId())
		   .append("\" style=\"width:")
		   .append("100%")
		   .append(";height:")
		   .append("100%")
		   .append(";top:")
		   .append("0")
		   .append("px;left:")
		   .append("0")
		   .append("px;position:")
		   .append("relative");
		buf.append(";overflow-y:auto;overflow-x:hidden;")
		   .append("\">");
		
//		buf.append("<div id=\"")
//		   .append(getDivShowId())
//		   .append("\" style=\"left:0px;top:0px;width:100%;height:100%\">");
		
		if(form.getRenderType() == 1 || form.getRenderType() == 3){
			//dingrf
//			buf.append(generateHeadForType1(form));
		}
//		buf.append("</div>");
		return buf.toString();
	}

	/**
	 * 默认表单布局
	 * @param form
	 * @return
	 */
	private String generateHeadForType1(FormComp form) {
		List<FormElement> list = form.getElementList();
		int colCount = form.getColumnCount().intValue();
		//存放当前行的宽度
		int[] widthArr = new int[colCount];
		
		StringBuffer buf = new StringBuffer();
		//add normal form support
		if(form.isWithForm()){
			buf.append("<form style='height:0px;margin:0px;' name=\'")
			.append(getId())
			.append("' id='")
			.append(getId())
			.append("'>");
		}

		buf.append("<table width=")
		   .append("\"100%\" ");
//		   .append("height=")
//		   .append("\"100%\" ");
//		if(form.getBackgroundColor() != null && !"".equals(form.getBackgroundColor()))
//		{
//			buf.append(" style=\"background-color:")
//			   .append(form.getBackgroundColor())
//			   .append("\"");
//		}
		buf.append(">\n");  
		   
		
		int nowRow = 0;
		HashMap<String, List<RowSpanInfo>> map = new HashMap<String, List<RowSpanInfo>>();
		// 当前控件Index
		int compIndex = 0;
		//记录当前行的跨行信息
		RowSpanInfo[] infoArr = null;
		//记录当前控件所在行的位置索引
		int rowIndex = -1;
		//记录当前行的控件个数
		int count = 0;
		
		int listSize = list.size();
		
		int rowHeight = form.getRowHeight();
		boolean hasTotalHeight = false;
		while (compIndex < listSize) {
			FormElement element = list.get(compIndex);
			// 如果元素不可见,并且form也设置为不渲染隐藏控件,则Form中相当于此控件不存在.之所以还允许定义，是因为一些逻辑的需要
			if(!form.isRenderHiddenEle() && element.isVisible() == false){
				compIndex ++;
				continue;
			}
			int colSpan = element.getColSpan().intValue();
			int rowSpan = element.getRowSpan().intValue();
			
			// 新行开始，加上行标
			if (count == 0) {
//				//如果一个元素占了一行，则需设置所占行数的高度给tr
//				if(colSpan == colCount){
//					if(rowSpan == 0){
//						hasTotalHeight = true;
//						buf.append("<tr>\n");
//					}
//					else	
//						buf.append("<tr height=\"" + (rowSpan * rowHeight) + "px\">\n");
//				}
//				else	
				buf.append("<tr height=\"" + rowHeight + "px\">\n");
			}

			// 加上跨行元素宽度
			if (map.get(nowRow + "") != null) {
				//取出并删除，防止重复取。 dengjt 08-03-03
				infoArr = map.remove(nowRow + "").toArray(new RowSpanInfo[0]);
				for (int i = 0; i < infoArr.length; i++) {
					count += infoArr[i].size;
				}
			}

			
			// 检查越界
			if (count > colCount)
				throw new LfwRuntimeException(
						"element out of bound in the form, please check you configuration");

			// 检查是否一个元素就超出了界限
			if (colSpan > colCount)
				throw new LfwRuntimeException(
						"element out of bound in the form, please check you configuration");

			// 如果加上当前控件越界,则补齐当前行td数，转到下一行
			if (count + colSpan > colCount) {
				for (; count < colCount; count++)
					buf.append("<td>&nbsp;</td>\n");
			} 
			else {  
				//如果是强制下一行，并且当前行并不是新起的行,则根据是否已经是新行进行判断
				if(count != 0 && element.isNextLine()){
					for (; count < colCount; count++)
						buf.append("<td>&nbsp;</td>\n");
					continue;
				}
				
				//增加控件计数
				compIndex ++;
				rowIndex = adjustIndex(rowIndex, infoArr);
				 
				// gd:07-10-30 文字右对齐,距离编辑控件8px
				buf.append("<td valign='top' rowSpan=\"" + (rowSpan == 0? "1" : rowSpan) + "\" colSpan=\"" + colSpan + "\"" + ">");
				
				String resId = element.getI18nName();
				String simpchn = element.getText() == null? resId : element.getText();
				String label = getFieldI18nName(resId, element.getField(), simpchn, element.getLangDir());
				label = label == null ? "" : label;
				int labelWidth = getLabelWidth(label);
				if(widthArr[rowIndex] < labelWidth)
					widthArr[rowIndex] = labelWidth;
				
				renderOneElement(element, buf, rowSpan, label, rowIndex, false);
				while(element.isAttachNext()){
					element = list.get((compIndex ++));
					resId = element.getI18nName();
					simpchn = element.getText() == null? resId : element.getText();
					label = getFieldI18nName(resId, element.getField(), simpchn, element.getLangDir());
					label = label == null ? "" : label;
					renderOneElement(element, buf, 1, label, rowIndex, true);
				}
				
				buf.append("</td>\n");

				count += colSpan;
				if (rowSpan != 1) {
					for (int j = 1; j < rowSpan; j++) {
						//获得当前行RowSpanInfo列表
						List<RowSpanInfo> infoList = map.get(String.valueOf(nowRow + j));
						if (infoList == null) {
							infoList = new ArrayList<RowSpanInfo>();
							map.put(String.valueOf(nowRow + j), infoList);
						}
						RowSpanInfo spanInfo = new RowSpanInfo();
						spanInfo.setSize(colSpan);
						spanInfo.setIndex(count);
						infoList.add(spanInfo);
					}
				}
			}
			if (count == colCount) {
				buf.append("</tr>\n");
				//行记录信息复位
				count = 0;
				rowIndex = -1;
				infoArr = null;
				nowRow++;
			}
			
			//最后一个控件渲染完，填充一行
			if(compIndex == listSize && count != 0){
				for (; count < colCount; count++)
					buf.append("<td>&nbsp;</td>\n");
			}
		}
//		if(!hasTotalHeight)
//			buf.append("<tr height='100%'><td colspan=\"" + colCount + "\"></td></tr>");
		buf.append("</table>");
		if(form.isWithForm())
			buf.append("</form>");
		
		String result = buf.toString();
		for (int i = 0; i < widthArr.length; i++) {
			if(widthArr[i] != 0)
				result = result.replaceAll(REPLACE_SIGN + i, String.valueOf(widthArr[i]));
		}
		return result;
	}

	private void renderOneElement(FormElement element, StringBuffer buf, int rowSpan, String label, int rowIndex, boolean useLabelWidth) {

		if(label != null && !label.equals("")){
			// checkbox不设置label
			if(element.getEditorType().equals(EditorTypeConst.CHECKBOX))
				label = "";
			FormComp form = (FormComp) getComponent();
			if(!(form.getFrom() != null && form.getFrom().equals(Dataset.FROM_NC))){
				if (element.isRequired())
					label = label == null ? "" : label + "<span style='color:red'>*</span>";
			}
			String labelWidth = REPLACE_SIGN + rowIndex;
			if(useLabelWidth){
				labelWidth = "" + getTrueLabelWidth(label);
			}
			buf.append("<div style=\"text-align:right;float:left;padding-top:2px;padding-right:5px;width:" + labelWidth + "px;");
			if (element.getLabelColor() != null && !"".equals(element.getLabelColor()))
				buf.append("color:" + element.getLabelColor());
			buf.append("\">" + label + "</div>");
		}
		buf.append("<div style=\"float:left;");
		if(rowSpan == 0)
			buf.append("height:100%;");
		buf.append("\" id=\"" + DIV_PRE + getId() +  element.getId() + "\"></div>")
		   .append("<div style='text-align:right;float:left;padding-top:3px;padding-left:5px;float:left'>")
		   .append((element.getDescription() == null ? "" : element.getDescription()))
		   .append("</div><div style='text-align:right;float:left;padding-top:3px;padding-left:5px;float:left;color:gray;'>")
		   .append((element.getInputAssistant() == null ? "" : element.getInputAssistant()))
		   .append("</div>");
	}

	public String generateHeadScript() {
		FormComp form = (FormComp) getComponent();
		StringBuffer buf = new StringBuffer();
		String formId = getVarShowId();
	    buf.append(formId)
		   .append(" = new AutoFormComp($ge('")
		   .append(getDivShowId())
		   .append("'), \"")
		   .append(getId())
		   .append("\",")
		   .append(form.getRenderType())
		   .append(",")
		   .append(form.isRenderHiddenEle())
		   .append(",")
		   .append(form.getRowHeight())
		   .append(",")
		   .append(form.getColumnCount())
		   
		   .append(");\n");
		Dataset ds = getDataset();
		List<FormElement> list = form.getElementList();
		
		Iterator<FormElement> it = list.iterator();
		// 当前form所属widget
		String widget = WIDGET_PRE + this.getCurrWidget().getId();
		
		buf.append(formId + ".widget = " + widget + ";\n");
		
		// form自定义元素在产生form控件时自动添加到pageContext中,供jsp页面取用
		List<String> selDefEleIds = new ArrayList<String>(10);
		while(it.hasNext())
		{	
			FormElement ele = it.next();
			if(!form.isRenderHiddenEle() && ele.isVisible() == false)
				continue;
			fillDataTypeAndEditorType(ds, ele);
			if(ele.getEditorType() != null && ele.getEditorType().equals(EditorTypeConst.SELFDEF))
				selDefEleIds.add(ele.getId());
			
			String eleId = ele.getId() + "_ele"; 
			buf.append("var " + eleId + " = " + getVarShowId())
			   .append(".createElement(\"")
			   .append(ele.getId())
			   .append("\",\"")
			   .append(ele.getField())
			   .append("\",\"");
			String eleWidth = "100";//ele.getWidth();
			if(eleWidth == null || eleWidth.equals("") || eleWidth.equals("0")){
				int formEleWidth = form.getEleWidth();
				if(formEleWidth <= 0)
					eleWidth = ELEMENT_WIDTH + "";
				else
					eleWidth = form.getEleWidth() + "";
			}
			
			buf.append(eleWidth)
			   .append("\",\"")
			   .append("22")//ele.getHeight() == null? "" : ele.getHeight()
			   .append("\",\"")
			   .append(ele.getRowSpan())
			   .append("\",\"")
			   .append(ele.getColSpan())
			   .append("\",\"")
			   .append(ele.getEditorType())
			   .append("\",");
			if(ele.getEditorType().equals(EditorTypeConst.REFERENCE))
			{
				StringBuffer userObj = new StringBuffer();
				userObj.append("{");
				IRefNode refNode = getCurrWidget().getViewModels().getRefNode(ele.getRefNode());
				if(refNode == null){
					LfwLogger.error("Form Element 类型为参照，但是没设置参照节点,Element id:" + ele.getId());
					userObj.append("refNode:null");
					ele.setEnabled(false);
				}
				else{
					String refId = RF_PRE + getCurrWidget().getId() + "_" + refNode.getId();
					userObj.append("refNode:").append(refId);
				}
				userObj.append(",visible:")
				       .append(ele.isVisible())
				       .append("}");
				buf.append(userObj.toString());
			}
			else if(ele.getEditorType().equals(EditorTypeConst.STRINGTEXT)) {
				StringBuffer userObj = new StringBuffer();
				userObj.append("{maxLength:");
				String maxLength = ele.getMaxLength();
				if(maxLength == null || maxLength.trim().equals(""))
					maxLength = getFieldProperty(ele.getField(), Field.MAX_LENGTH);
				if(maxLength == null || maxLength.trim().equals(""))
					maxLength = null;
				userObj.append(maxLength)
				       .append(",visible:")
				       .append(ele.isVisible())
				       .append("}");
				buf.append(userObj.toString());
			}
			else if(ele.getEditorType().equals(EditorTypeConst.IMAGECOMP)) {
				StringBuffer userObj = new StringBuffer();
				userObj.append("{url:'")
				       .append(getFieldProperty(ele.getField(), IMG_URL))
				       .append("', width:'")
				       .append(getFieldProperty(ele.getField(), IMG_WIDTH))
				       .append("',height:'")
				       .append(getFieldProperty(ele.getField(), IMG_HEIGHT))
				       .append("',pkfield:'")
				       .append(getFieldProperty(ele.getField(), IMG_PK_FIELD))
				       .append("'}");
				buf.append(userObj.toString());
			}
			
			else if(ele.getEditorType().equals(EditorTypeConst.INTEGERTEXT)){
				String maxValue = ele.getMaxValue();
				String minValue = ele.getMinValue();
				StringBuffer userObj = new StringBuffer();
				userObj.append("{"); 
				if(maxValue == null || "".equals(maxValue))
					maxValue = getFieldProperty(ele.getField(), Field.MAX_VALUE);
				if(minValue == null || "".equals(minValue))
					minValue = getFieldProperty(ele.getField(), Field.MIN_VALUE);
				//if(maxValue != null && !maxValue.trim().equals(""))
					userObj.append("maxValue:")
					       .append(maxValue);
				//if(minValue != null && !minValue.trim().equals("")){
					//if(maxValue != null && !maxValue.trim().equals(""))
						userObj.append(",");
					userObj.append("minValue:")
						   .append(minValue);
				//}
				userObj.append(",visible:")
					   .append(ele.isVisible())
					   .append("}");
				buf.append(userObj.toString());
			}
			else if(ele.getEditorType().equals(EditorTypeConst.DECIMALTEXT)){
				
				String precision = ele.getPrecision();
				if(precision == null || precision.trim().equals(""))
					precision = getFieldProperty(ele.getField(), Field.PRECISION);
				if(precision == null || precision.trim().equals(""))
					precision = null;
				
				StringBuffer userObj = new StringBuffer();
				userObj.append("{precision:'")
					   .append(precision)
					   .append("',visible:")
					   .append(ele.isVisible())
					   .append("}");
				buf.append(userObj.toString());
			}
			else if(ele.getEditorType().equals(EditorTypeConst.COMBODATA) 
					|| ele.getEditorType().equals(EditorTypeConst.RADIOGROUP) 
					|| ele.getEditorType().equals(EditorTypeConst.CHECKBOXGROUP)){
				StringBuffer userObj = new StringBuffer();
				userObj.append("{comboData:")
					   .append(ele.getRefComboData() == null ? "null" : (COMBO_PRE + getCurrWidget().getId() + "_" + ele.getRefComboData()))
					   .append(",imageOnly:")
					   .append(ele.isImageOnly())
					   .append(",selectOnly:")
					   .append(ele.isSelectOnly())
					   .append(",dataDivHeight:")
					   .append(ele.getDataDivHeight() == null ? null : ele.getDataDivHeight())
					   .append(",visible:")
					   .append(ele.isVisible())
					   .append("}");

					   buf.append(userObj.toString());
			}
			else if(ele.getEditorType().equals(EditorTypeConst.CHECKBOX)){
				StringBuffer userObj = new StringBuffer();
				userObj.append("{valuePair:");
				// 设置comboData的情况
				StaticComboData data = (StaticComboData) getCurrWidget().getViewModels().getComboData(ele.getRefComboData());
				if(data != null){
					CombItem[] items = data.getAllCombItems();
					if(items == null || items.length != 2)
						throw new LfwRuntimeException("The Combodata is not suitable for ele:" + ele.getId());
					userObj.append("[\"")
					.append(items[0].getValue())
					.append("\",\"")
					.append(items[1].getValue())
					.append("\"]");
				}
				else
				{
					// 没有设置comboData的情况
					String dataType = ele.getDataType();
					if(dataType.equals(StringDataTypeConst.BOOLEAN)|| dataType.equals(StringDataTypeConst.bOOLEAN))
						userObj.append("[\"true\",\"false\"]");
					else if(dataType.equals(StringDataTypeConst.UFBOOLEAN))
						userObj.append("['Y','N']");
				}
				userObj.append(",visible:")
				 	   .append(ele.isVisible())
				 	   .append("}");
				buf.append(userObj.toString());
			}
			else if(ele.getEditorType().equals(EditorTypeConst.RICHEDITOR)){
				String hideBarIndices = ele.getHideBarIndices();
				String hideImageIndices = ele.getHideImageIndices();
				buf.append("[");
				if(hideBarIndices != null && !"".equals(hideBarIndices))
					buf.append(hideBarIndices);
				else
					buf.append("");
				buf.append(",");
				if(hideImageIndices != null && !"".equals(hideImageIndices))
					buf.append(hideImageIndices);
				else
					buf.append("");
				buf.append("]");
			}
			else
				buf.append("null");
			buf.append(",")
			   .append(!ele.isEnabled())
			   .append(",")
			   .append(!ele.isEditable())
			   .append(",");
			
			//参照元素需要提前将Dataset传入
			if(ele.getEditorType().equals(EditorTypeConst.REFERENCE))
				buf.append("\"" + this.getDataset().getId() + "\",");
			else
				buf.append("null,");
			
			//label	
			String resId = ele.getI18nName();
			String simpchn = ele.getText() == null? resId : ele.getText();
			String label = getFieldI18nName(resId, ele.getField(), simpchn, ele.getLangDir());
			label = label == null ? "" : label;
			buf.append("\"")
			   .append(label)
			   .append("\",");
			
			// labelColor
			if(ele.getLabelColor() != null)
				buf.append("'")
				   .append(ele.getLabelColor())
				   .append("',");
			else
				buf.append("null,");
			
			if(ele.isNextLine())
				buf.append("true,");
			else
				buf.append("false,");
			
			buf.append(ele.isRequired())
			   .append(",");
			
			if (ele.getTip() != null && !"".equals(ele.getTip())) {
				buf.append("'")
				   .append(ele.getTip())
				   .append("',");
			} else {
				buf.append("null,");
			}

			if (ele.getInputAssistant() != null && !"".equals(ele.getInputAssistant())) {
				buf.append("'")
				   .append(ele.getInputAssistant())
				   .append("',");
			} else {
				buf.append("null,");
			}
			if (ele.getDescription() != null && !"".equals(ele.getDescription())) {
				buf.append("'")
				.append(ele.getDescription())
				.append("',");
			} else {
				buf.append("null,");
			}
			buf.append("'")
			.append(ele.isAttachNext())
			.append("',");
			
//			if(ele.getClassName() != null && !ele.getClassName().equals("")){
//				buf.append("'")
//				   .append(ele.getClassName())
//				   .append("'\n");
//			}
//			else
//				buf.append(ele.getClassName());
			buf.append("null");
			buf.append("); \n");
			buf.append(eleId + ".widget=" + widget + ";\n");
			
			// 自定义控件
			if (ele.getEditorType().equals(EditorTypeConst.SELFDEF)) {
				String bindId = ele.getBindId();
				if(bindId == null || bindId.equals("")){
					String ext = (String) ele.getExtendAttributeValue(FormElement.SELF_DEF_FUNC);
					if(ext != null){
						buf.append(eleId)
						   .append(".setContent(")
						   .append(ext)
						   .append("());\n");
					}
				}
				else{
					WebComponent comp = getCurrWidget().getViewComponents().getComponent(bindId);
					if (comp instanceof ButtonComp) {
						ButtonCompTag buttonTag = new ButtonCompTag();
						buttonTag.setJspContext(getJspContext());
						buttonTag.setId(bindId);
						try {
							buttonTag.doTag();
						} catch (Exception e) {
							Logger.error(e.getMessage(), e);
							throw new LfwRuntimeException("解析ButtonComp出错");
						}
						buf.append(eleId)
						   .append(".setContent(")
						   .append(buttonTag.getVarShowId())
						   .append(");\n");
					}
					else if (comp instanceof LabelComp) {
						LabelCompTag labelTag = new LabelCompTag();
						labelTag.setJspContext(getJspContext());
						labelTag.setId(bindId);
						try {
							labelTag.doTag();
						} catch (Exception e) {
							Logger.error(e.getMessage(), e);
							throw new LfwRuntimeException("解析LabelComp出错");
						}
						buf.append(eleId)
						   .append(".setContent(")
						   .append(labelTag.getVarShowId())
						   .append(");\n");
					}
					else if (comp instanceof ImageComp) {
						ImageCompTag imageTag = new ImageCompTag();
						imageTag.setJspContext(getJspContext());
						imageTag.setId(bindId);
						try {
							imageTag.doTag();
						} catch (Exception e) {
							Logger.error(e.getMessage(), e);
							throw new LfwRuntimeException("解析ImageComp出错");
						}
						buf.append(eleId)
						   .append(".setContent(")
						   .append(imageTag.getVarShowId())
						   .append(");\n");
					}
					else {
						SelfDefCompTag selfDefTag = new SelfDefCompTag();
						selfDefTag.setJspContext(getJspContext());
						selfDefTag.setId(bindId);
						try {
							selfDefTag.doTag();
						} catch (Exception e) {
							Logger.error(e.getMessage(), e);
							throw new LfwRuntimeException("解析selfDefComp出错");
						}
						buf.append(eleId)
						   .append(".setContent(")
						   .append(selfDefTag.getVarShowId())
						   .append(");\n");
					}
				}
			}
		}
		
		getJspContext().setAttribute("selDefEleIds", selDefEleIds);
		
		buf.append(widget + ".addComponent(" + getVarShowId() + ");\n");
		return buf.toString();
	}

	
	/**
	 * 设置form的ds绑定
	 */
	public String generateTailScript() {
		FormComp form = (FormComp) getComponent();
		StringBuffer buf1 = new StringBuffer();
		// form tag结束时移除"selDefEleIds"属性
		getJspContext().removeAttribute("selDefEleIds");
		if(form.getRenderType() == 1 || form.getRenderType() == 3){
			buf1.append(generateTailScriptForType1(form));
		}
		else
			buf1.append(generateTailScriptForType2(form));
		
		
		StringBuffer buf = new StringBuffer();
//		buf.append("\n" +COMP_PRE)
//		   .append(getId())
		buf.append("\n" + getVarShowId())
		   .append(".setDataset(" + "pageUI.getWidget(\"" + getCurrWidget().getId() +  "\").getDataset" + "(\"")
		   .append(form.getDataset())
		   .append("\"));\n");
		
		if(!form.isEnabled()){
			buf.append(getVarShowId())
			   .append(".setEditable(false);\n");
		}
		String script = buf.toString();
		
		if(getCurrWidget().isDialog())
			buf1.append(script);
		else{
			StringBuffer dsScript = (StringBuffer) getJspContext().getAttribute(PageModelTag.DS_SCRIPT);
			if(script != null)
				dsScript.append(script);
		}
		
		return buf1.toString();
	}

	private String generateTailScriptForType2(FormComp form) {
		return "";
	}

	private String generateTailScriptForType1(FormComp form) {
		return "";
	}
	
	private class RowSpanInfo
	{
		private int size = 0;
		private int index = 0;
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
	}
	
	/**
	 * 根据dataset中的数据类型填充FormElement的dataType，并且获得对应的EditorType
	 * @param ds
	 * @param ele
	 * @return
	 */
	private void fillDataTypeAndEditorType(Dataset ds, FormElement ele){
		if (ele.getField() != null) {
			if(ele.getDataType() == null || ele.getDataType().trim().equals(""))
				ele.setDataType(ds.getFieldSet().getField(ele.getField()).getDataType());
			if(ele.getEditorType() == null || ele.getEditorType().trim().equals(""))
				ele.setEditorType(EditorTypeConst.getEditorTypeByString(ele.getDataType()));
		}
	}
	
	private String getFieldProperty(String fieldId, String name)
	{
		Dataset ds = getDataset();
		Field field = ds.getFieldSet().getField(fieldId);
		if(field == null)
			return null;
		return (String) field.getExtendAttributeValue(name);
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return null;
	}
}
