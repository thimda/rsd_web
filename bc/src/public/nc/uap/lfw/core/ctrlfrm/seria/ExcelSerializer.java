package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.comp.ExcelColumn;
import nc.uap.lfw.core.comp.ExcelColumnGroup;
import nc.uap.lfw.core.comp.ExcelComp;
import nc.uap.lfw.core.comp.IExcelColumn;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.IViewZone;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.model.parser.EventConfParser;
import nc.uap.lfw.core.model.parser.ListenersParser;
import nc.uap.lfw.core.persistence.PersistenceUtil;
import nc.uap.lfw.core.util.AMCUtil;

import org.apache.commons.digester.Digester;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ExcelSerializer extends BaseSerializer<ExcelComp> implements IViewZone{

	@Override
	public void deSerialize(ExcelComp excel, Document doc, Element parentNode) {
		Element excelNode = null;
		if(excel.getFrom() == null){
			excelNode = doc.createElement("ExcelComp");
			parentNode.appendChild(excelNode);
			excelNode.setAttribute("id", excel.getId());
			if(isNotNullString(excel.getHeaderRowHeight()))
				excelNode.setAttribute("headerRowHeight", excel.getHeaderRowHeight());
			if(isNotNullString(excel.getRowHeight()))
				excelNode.setAttribute("rowHeight", excel.getRowHeight());
//			if(isNotNullString(excel.getTop()))
//				excelNode.setAttribute("top", excel.getTop());
//			if(isNotNullString(excel.getLeft()))
//				excelNode.setAttribute("left", excel.getLeft());
//			if(isNotNullString(excel.getPosition()))
//				excelNode.setAttribute("position", excel.getPosition());
			if(isNotNullString(excel.getCaption()))
				excelNode.setAttribute("caption", excel.getCaption());
			if(isNotNullString(excel.getDataset()))
				excelNode.setAttribute("dataset", excel.getDataset());
			excelNode.setAttribute("editable", String.valueOf(excel.isEditable()));
//			if(isNotNullString(excel.getWidth()))
//				excelNode.setAttribute("width", excel.getWidth());
//			if(isNotNullString(excel.getHeight()))
//				excelNode.setAttribute("height", excel.getHeight());
			if(isNotNullString(excel.getPageSize()))
				excelNode.setAttribute("pageSize", excel.getPageSize());
			excelNode.setAttribute("multiSelect", String.valueOf(excel.isMultiSelect()));
			excelNode.setAttribute("showHeader", String.valueOf(excel.isShowHeader()));
			excelNode.setAttribute("showColInfo", String.valueOf(excel.isShowColInfo()));
			excelNode.setAttribute("showSumRow", String.valueOf(excel.isShowSumRow()));
			excelNode.setAttribute("pagenationTop", String.valueOf(excel.isPagenationTop()));
			excelNode.setAttribute("sortable", String.valueOf(excel.isSortable()));
			excelNode.setAttribute("visible", String.valueOf(excel.isVisible()));
			excelNode.setAttribute("editable", String.valueOf(excel.isEditable()));
			excelNode.setAttribute("enabled", String.valueOf(excel.isEnabled()));
//			if(isNotNullString(excel.getClassName()))
//				excelNode.setAttribute("className", excel.getClassName());
			if(isNotNullString(excel.getGroupColumns()))
				excelNode.setAttribute("groupColumns", excel.getGroupColumns());
			if(isNotNullString(excel.getContextMenu()))
				excelNode.setAttribute("contextMenu", excel.getContextMenu());
			excelNode.setAttribute("showSumRow", String.valueOf(excel.isShowSumRow()));
			excelNode.setAttribute("showNumCol", String.valueOf(excel.isShowNumCol()));
			List<IExcelColumn> colums = excel.getColumnList();
			if(colums != null && !colums.isEmpty()){
				for(int j = 0; j < colums.size(); j++)
				{
					if(colums.get(j) instanceof ExcelColumnGroup){
						ExcelColumnGroup columngroup = (ExcelColumnGroup) colums.get(j);
						Element columGroupNode = doc.createElement("ColumnGroup");
						excelNode.appendChild(columGroupNode);
						columGroupNode.setAttribute("id", columngroup.getId());
						if(isNotNullString(columngroup.getI18nName()))
							columGroupNode.setAttribute("i18nName", columngroup.getI18nName());
						if(isNotNullString(columngroup.getText()))
							columGroupNode.setAttribute("text", columngroup.getText());
						columGroupNode.setAttribute("visible", String.valueOf(columngroup.isVisible()));
						List<IExcelColumn> groupColumns = columngroup.getChildColumnList();
						if(groupColumns != null && !groupColumns.isEmpty()){
							for (int k = 0; k < groupColumns.size(); k++) {
								ExcelColumn colum = (ExcelColumn) groupColumns.get(k);
								Element columNode = doc.createElement("Column");
								columGroupNode.appendChild(columNode);
								columNode.setAttribute("id", colum.getId());
								if(isNotNullString(colum.getField()))
									columNode.setAttribute("field", colum.getField());
								if(isNotNullString(colum.getText()))
									columNode.setAttribute("text", colum.getText());
								if(isNotNullString(String.valueOf(colum.getWidth())))
									columNode.setAttribute("width", String.valueOf(colum.getWidth()));
								columNode.setAttribute("autoExpand", String.valueOf(colum.isAutoExpand()));
								if(isNotNullString(colum.getDataType()))
									columNode.setAttribute("dataType", colum.getDataType());
								if(isNotNullString(colum.getEditorType()))
									columNode.setAttribute("editorType", colum.getEditorType());
								if(isNotNullString(colum.getRenderType()))
									columNode.setAttribute("renderType", colum.getRenderType());
								if(isNotNullString(colum.getRefNode()))
									columNode.setAttribute("refNode", colum.getRefNode());
								if(isNotNullString(colum.getRefComboData()))
									columNode.setAttribute("refComboData", colum.getRefComboData());
								if(isNotNullString(colum.getColmngroup()))
									columNode.setAttribute("colmngroup", colum.getColmngroup());
								if(isNotNullString(colum.getI18nName()))
									columNode.setAttribute("i18nName", colum.getI18nName());
								if(isNotNullString(colum.getLangDir()))
									columNode.setAttribute("langDir", colum.getLangDir());
								if(isNotNullString(colum.getColumBgColor()))
									columNode.setAttribute("columBgColor", colum.getColumBgColor());
								if(isNotNullString(colum.getTextAlign()))
									columNode.setAttribute("textAlign", colum.getTextAlign());
								if(isNotNullString(colum.getTextColor()))
									columNode.setAttribute("textColor", colum.getTextColor());
								columNode.setAttribute("visible", String.valueOf(colum.isVisible()));
								columNode.setAttribute("fixedHeader", String.valueOf(colum.isFixedHeader()));
								columNode.setAttribute("imageOnly", String.valueOf(colum.isImageOnly()));
								columNode.setAttribute("sortable", String.valueOf(colum.isSortable()));
								columNode.setAttribute("editable", String.valueOf(colum.isEditable()));
								columNode.setAttribute("nullAble", String.valueOf(colum.isNullAble()));
								columNode.setAttribute("sumCol", String.valueOf(colum.isSumCol()));
							}
						}
					}
					if(colums.get(j) instanceof ExcelColumn)
					{
						ExcelColumn colum = (ExcelColumn) colums.get(j);
						Element columNode = doc.createElement("Column");
						excelNode.appendChild(columNode);
						columNode.setAttribute("id", colum.getId());
						if(isNotNullString(colum.getField()))
							columNode.setAttribute("field", colum.getField());
						if(isNotNullString(colum.getText()))
							columNode.setAttribute("text", colum.getText());
						if(isNotNullString(String.valueOf(colum.getWidth())))
							columNode.setAttribute("width", String.valueOf(colum.getWidth()));
						columNode.setAttribute("autoExpand", String.valueOf(colum.isAutoExpand()));
						if(isNotNullString(colum.getDataType()))
							columNode.setAttribute("dataType", colum.getDataType());
						if(isNotNullString(colum.getEditorType()))
							columNode.setAttribute("editorType", colum.getEditorType());
						if(isNotNullString(colum.getRenderType()))
							columNode.setAttribute("renderType", colum.getRenderType());
						if(isNotNullString(colum.getRefNode()))
							columNode.setAttribute("refNode", colum.getRefNode());
						if(isNotNullString(colum.getRefComboData()))
							columNode.setAttribute("refComboData", colum.getRefComboData());
						if(isNotNullString(colum.getI18nName()))
							columNode.setAttribute("i18nName", colum.getI18nName());
						if(isNotNullString(colum.getLangDir()))
							columNode.setAttribute("langDir", colum.getLangDir());
						if(isNotNullString(colum.getColumBgColor()))
							columNode.setAttribute("columBgColor", colum.getColumBgColor());
						if(isNotNullString(colum.getTextAlign()))
							columNode.setAttribute("textAlign", colum.getTextAlign());
						if(isNotNullString(colum.getTextColor()))
							columNode.setAttribute("textColor", colum.getTextColor());
						columNode.setAttribute("visible", String.valueOf(colum.isVisible()));
						columNode.setAttribute("fixedHeader", String.valueOf(colum.isFixedHeader()));
						columNode.setAttribute("imageOnly", String.valueOf(colum.isImageOnly()));
						columNode.setAttribute("sortable", String.valueOf(colum.isSortable()));
						columNode.setAttribute("editable", String.valueOf(colum.isEditable()));
						columNode.setAttribute("nullAble", String.valueOf(colum.isNullAble()));
						columNode.setAttribute("sumCol", String.valueOf(colum.isSumCol()));
					}	
				}
			}
			Map<String, JsListenerConf> jsListeners = excel.getListenerMap();
			if(jsListeners != null && jsListeners.size() > 0)
				PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), excelNode);
			
			//Events
			AMCUtil.addEvents(doc, excel.getEventConfs(), excelNode);
		}
		else{
			
			Map<String, JsListenerConf> jsListeners = excel.getListenerMap();
			boolean saveFlag = false;
			for (Iterator<String> itwd = jsListeners.keySet().iterator(); itwd.hasNext();) {
				String listenerId = (String) itwd.next();
				JsListenerConf listener = jsListeners.get(listenerId);
				if(listener.getFrom() == null){
					saveFlag = true;
					break;
				}
				else if(listener.getConfType().equals(WebElement.CONF_DEL)){
					saveFlag = true;
					break;
				}
			}
			if(saveFlag){
				if(jsListeners != null && jsListeners.size() > 0){
					excelNode = doc.createElement("ExcelComp");
					parentNode.appendChild(excelNode);
					excelNode.setAttribute("id", excel.getId());
					if(isNotNullString(excel.getConfType()))
						excelNode.setAttribute("confType", excel.getConfType());
					PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), excelNode);
					
					//Events
					AMCUtil.addEvents(doc, excel.getEventConfs(), excelNode);
				}
			}
		}
	}

	@Override
	public void serialize(Digester digester) {
		String excelClassName = ExcelComp.class.getName();
		
		//ExcelComp解析
		digester.addObjectCreate("Widget/Components/ExcelComp", excelClassName);
		digester.addSetProperties("Widget/Components/ExcelComp");
		digester.addSetNext("Widget/Components/ExcelComp", "addComponent",
				excelClassName);

		String excelColumnClassName = ExcelColumn.class.getName();
		
		//ExcelComp元素解析
		digester.addObjectCreate("Widget/Components/ExcelComp/Column",
				excelColumnClassName);
		digester.addSetProperties("Widget/Components/ExcelComp/Column");
		digester.addSetNext("Widget/Components/ExcelComp/Column", "addColumn",
				excelColumnClassName);

		String excelColumnGroupClassName = ExcelColumnGroup.class.getName();
		//ExcelComp元素解析
		digester.addObjectCreate("Widget/Components/ExcelComp/ColumnGroup",
				excelColumnGroupClassName);
		digester.addSetProperties("Widget/Components/ExcelComp/ColumnGroup");
		digester.addSetNext("Widget/Components/ExcelComp/ColumnGroup", "addColumn",
				excelColumnGroupClassName);

		excelColumnClassName = ExcelColumn.class.getName();
		//ExcelComp元素解析
		digester.addObjectCreate("Widget/Components/ExcelComp/ColumnGroup/Column",
				excelColumnClassName);
		digester.addSetProperties("Widget/Components/ExcelComp/ColumnGroup/Column");
		digester.addSetNext("Widget/Components/ExcelComp/ColumnGroup/Column",
				"addColumn", excelColumnClassName);
		ListenersParser.parseListeners(digester, "Widget/Components/ExcelComp/Listeners", ExcelComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/ExcelComp", ExcelComp.class);
	}

}
