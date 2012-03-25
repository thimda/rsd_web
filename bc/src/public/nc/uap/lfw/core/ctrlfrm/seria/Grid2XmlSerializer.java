package nc.uap.lfw.core.ctrlfrm.seria;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridColumnGroup;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.GridTreeLevel;
import nc.uap.lfw.core.comp.IGridColumn;
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

public class Grid2XmlSerializer extends BaseSerializer<GridComp> implements IViewZone{

	@Override
	public void deSerialize(GridComp grid, Document doc, Element parentNode) {
		Element gridNode = null;
		if(grid.getFrom() == null){
			gridNode = doc.createElement("GridComp");
			parentNode.appendChild(gridNode);
			gridNode.setAttribute("id", grid.getId());
			if(isNotNullString(grid.getHeaderRowHeight()))
				gridNode.setAttribute("headerRowHeight", grid.getHeaderRowHeight());
			if(isNotNullString(grid.getRowHeight()))
				gridNode.setAttribute("rowHeight", grid.getRowHeight());
			if(isNotNullString(grid.getCaption()))
				gridNode.setAttribute("caption", grid.getCaption());
			if(isNotNullString(grid.getDataset()))
				gridNode.setAttribute("dataset", grid.getDataset());
			gridNode.setAttribute("editable", String.valueOf(grid.isEditable()));
			if(isNotNullString(grid.getPageSize()))
				gridNode.setAttribute("pageSize", grid.getPageSize());
			gridNode.setAttribute("multiSelect", String.valueOf(grid.isMultiSelect()));
			gridNode.setAttribute("showHeader", String.valueOf(grid.isShowHeader()));
			gridNode.setAttribute("showColInfo", String.valueOf(grid.isShowColInfo()));
			gridNode.setAttribute("showSumRow", String.valueOf(grid.isShowSumRow()));
			gridNode.setAttribute("pagenationTop", String.valueOf(grid.isPagenationTop()));
			gridNode.setAttribute("sortable", String.valueOf(grid.isSortable()));
			gridNode.setAttribute("visible", String.valueOf(grid.isVisible()));
			gridNode.setAttribute("editable", String.valueOf(grid.isEditable()));
			gridNode.setAttribute("enabled", String.valueOf(grid.isEnabled()));
			if(isNotNullString(grid.getRowRender()))
				gridNode.setAttribute("rowRender", grid.getRowRender());
			if(isNotNullString(grid.getExtendCellEditor()))
				gridNode.setAttribute("extendCellEditor", grid.getExtendCellEditor());
//			if(isNotNullString(grid.getClassName()))
//				gridNode.setAttribute("className", grid.getClassName());
			if(isNotNullString(grid.getGroupColumns()))
				gridNode.setAttribute("groupColumns", grid.getGroupColumns());
			if(isNotNullString(grid.getContextMenu()))
				gridNode.setAttribute("contextMenu", grid.getContextMenu());
			gridNode.setAttribute("showSumRow", String.valueOf(grid.isShowSumRow()));
			gridNode.setAttribute("showNumCol", String.valueOf(grid.isShowNumCol()));
			List<IGridColumn> colums = grid.getColumnList();
			if(colums != null && !colums.isEmpty()){
				for(int j = 0; j < colums.size(); j++)
				{
					if(colums.get(j) instanceof GridColumnGroup){
						GridColumnGroup columngroup = (GridColumnGroup) colums.get(j);
						Element columGroupNode = doc.createElement("ColumnGroup");
						gridNode.appendChild(columGroupNode);
						columGroupNode.setAttribute("id", columngroup.getId());
						if(isNotNullString(columngroup.getI18nName()))
							columGroupNode.setAttribute("i18nName", columngroup.getI18nName());
						if(isNotNullString(columngroup.getText()))
							columGroupNode.setAttribute("text", columngroup.getText());
						columGroupNode.setAttribute("visible", String.valueOf(columngroup.isVisible()));
						List<IGridColumn> groupColumns = columngroup.getChildColumnList();
						if(groupColumns != null && !groupColumns.isEmpty()){
							for (int k = 0; k < groupColumns.size(); k++) {
								GridColumn colum = (GridColumn) groupColumns.get(k);
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
								if(isNotNullString(colum.getMaxLength()))
									columNode.setAttribute("maxLength", colum.getMaxLength());
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
					if(colums.get(j) instanceof GridColumn)
					{
						GridColumn colum = (GridColumn) colums.get(j);
						Element columNode = doc.createElement("Column");
						gridNode.appendChild(columNode);
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
						columNode.setAttribute("showCheckBox", String.valueOf(colum.isShowCheckBox()));
					}	
				}
			}
			GridTreeLevel level = grid.getTopLevel();
			if(level != null){
				Element levelNode = null;
				levelNode = doc.createElement("RecursiveGridLevel");
				gridNode.appendChild(levelNode);
				levelNode.setAttribute("recursiveKeyField", level.getRecursiveKeyField());
				levelNode.setAttribute("recursivePKeyField", level.getRecursivePKeyField());
				if(isNotNullString(level.getId()))	
					levelNode.setAttribute("id", level.getId());
				if(isNotNullString(level.getLabelFields()))
					levelNode.setAttribute("labelFields", level.getLabelFields());
				
				if(isNotNullString(level.getDataset()))
					levelNode.setAttribute("dataset", level.getDataset());

//				if(isNotNullString(level.getLabelDelims()))
//					levelNode.setAttribute("labelDelims", level.getLabelDelims());
//				if(isNotNullString(level.getContextMenu()))
//					levelNode.setAttribute("contextMenu", level.getContextMenu());
				
//				Element parentNode = levelNode;
//				while(level.getChildTreeLevel() != null){
//					level = level.getChildTreeLevel();
//					Element childLevelNode = null;
//					 if(level instanceof RecursiveTreeLevel){
//						childLevelNode = doc.createElement("RecursiveGridLevel");
//						parentNode.appendChild(childLevelNode);
//						childLevelNode.setAttribute("recursiveKeyField", ((RecursiveTreeLevel) level).getRecursiveKeyField());
//						childLevelNode.setAttribute("recursivePKeyField", ((RecursiveTreeLevel) level).getRecursivePKeyField());
//					}
//					if(isNotNullString(level.getId()))	
//						childLevelNode.setAttribute("id", level.getId());
//					if(isNotNullString(level.getDataset()))
//						childLevelNode.setAttribute("dataset", level.getDataset());
//					if(isNotNullString(level.getMasterKeyField()))
//						childLevelNode.setAttribute("masterKeyField", level.getMasterKeyField());
//					if(isNotNullString(level.getLabelFields()))
//						childLevelNode.setAttribute("labelFields", level.getLabelFields());
//					if(isNotNullString(level.getLabelDelims()))
//						childLevelNode.setAttribute("labelDelims", level.getLabelDelims());
//					if(isNotNullString(level.getDetailKeyParameter()))
//						childLevelNode.setAttribute("detailKeyParameter", level.getDetailKeyParameter());
//					if(isNotNullString(level.getContextMenu()))
//						childLevelNode.setAttribute("contextMenu", level.getContextMenu());
//					parentNode = childLevelNode;
//				}
			}
			Map<String, JsListenerConf> jsListeners = grid.getListenerMap();
			if(jsListeners != null && jsListeners.size() > 0)
				PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), gridNode);
			
			//Events
			AMCUtil.addEvents(doc, grid.getEventConfs(), gridNode);
		}
		else{
			
			Map<String, JsListenerConf> jsListeners = grid.getListenerMap();
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
					gridNode = doc.createElement("GridComp");
					parentNode.appendChild(gridNode);
					gridNode.setAttribute("id", grid.getId());
					if(isNotNullString(grid.getConfType()))
						gridNode.setAttribute("confType", grid.getConfType());
					PersistenceUtil.addListeners(doc, jsListeners.values().toArray(new JsListenerConf[0]), gridNode);
					//Events
					AMCUtil.addEvents(doc, grid.getEventConfs(), gridNode);
				}
			}
		}		
	}


	@Override
	public void serialize(Digester digester) {
		String gridClassName = GridComp.class.getName();
		
		//GirdComp解析
		digester.addObjectCreate("Widget/Components/GridComp", gridClassName);
		digester.addSetProperties("Widget/Components/GridComp");
		digester.addSetNext("Widget/Components/GridComp", "addComponent",
				gridClassName);
		
		//兼容grid解析
		digester.addObjectCreate("Widget/Components/Grid", gridClassName);
		digester.addSetProperties("Widget/Components/Grid");
		digester.addSetNext("Widget/Components/Grid", "addComponent",
				gridClassName);

		String gridColumnClassName = GridColumn.class.getName();
		
		//GridComp元素解析
		digester.addObjectCreate("Widget/Components/GridComp/Column",
				gridColumnClassName);
		digester.addSetProperties("Widget/Components/GridComp/Column");
		digester.addSetNext("Widget/Components/GridComp/Column", "addColumn",
				gridColumnClassName);

		//兼容Grid元素解析
		digester.addObjectCreate("Widget/Components/Grid/Column",
				gridColumnClassName);
		digester.addSetProperties("Widget/Components/Grid/Column");
		digester.addSetNext("Widget/Components/Grid/Column", "addColumn",
				gridColumnClassName);

		String gridColumnGroupClassName = GridColumnGroup.class.getName();
		//GridComp元素解析
		digester.addObjectCreate("Widget/Components/GridComp/ColumnGroup",
				gridColumnGroupClassName);
		digester.addSetProperties("Widget/Components/GridComp/ColumnGroup");
		digester.addSetNext("Widget/Components/GridComp/ColumnGroup", "addColumn",
				gridColumnGroupClassName);
		
		//兼容Grid元素解析
		digester.addObjectCreate("Widget/Components/Grid/ColumnGroup",
				gridColumnGroupClassName);
		digester.addSetProperties("Widget/Components/Grid/ColumnGroup");
		digester.addSetNext("Widget/Components/Grid/ColumnGroup", "addColumn",
				gridColumnGroupClassName);

		gridColumnClassName = GridColumn.class.getName();
		//GridComp元素解析
		digester.addObjectCreate("Widget/Components/GridComp/ColumnGroup/Column",
				gridColumnClassName);
		digester.addSetProperties("Widget/Components/GridComp/ColumnGroup/Column");
		digester.addSetNext("Widget/Components/GridComp/ColumnGroup/Column",
				"addColumn", gridColumnClassName);
		
		//多层level的支持，如果有未知的N层怎么解析？现在暂时认为最多有三层
		String recursiveGridLevelClassName = GridTreeLevel.class.getName();
		
		// 递归
		digester.addObjectCreate("Widget/Components/GridComp/RecursiveGridLevel", recursiveGridLevelClassName);
		digester.addSetProperties("Widget/Components/GridComp/RecursiveGridLevel");
		
		// 递归-递归
		digester.addObjectCreate("Widget/Components/GridComp/RecursiveGridLevel/RecursiveGridLevel", recursiveGridLevelClassName);
		digester.addSetProperties("Widget/Components/GridComp/RecursiveGridLevel/RecursiveGridLevel");
		digester.addSetNext("Widget/Components/GridComp/RecursiveGridLevel/RecursiveGridLevel", "setChildTreeLevel");
		
		// 递归-递归-递归
		digester.addObjectCreate("Widget/Components/GridComp/RecursiveGridLevel/RecursiveGridLevel/RecursiveGridLevel", recursiveGridLevelClassName);
		digester.addSetProperties("Widget/Components/GridComp/RecursiveGridLevel/RecursiveGridLevel/RecursiveGridLevel");
		digester.addSetNext("Widget/Components/GridComp/RecursiveGridLevel/RecursiveGridLevel/RecursiveGridLevel", "setChildTreeLevel");

		digester.addSetNext("Widget/Components/GridComp/RecursiveGridLevel", "setTopLevel");
		
		ListenersParser.parseListeners(digester, "Widget/Components/GridComp/Listeners", GridComp.class);
		
		EventConfParser.parseEvents(digester, "Widget/Components/GridComp", GridComp.class);
	}

}
