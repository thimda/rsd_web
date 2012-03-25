package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.base.ExtendAttributeSupport;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.ToolBarItem;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.ctx.WidgetContext;
import nc.uap.lfw.core.model.util.ElementObserver;
import nc.uap.lfw.core.page.LfwWidget;

import org.json.JSONObject;

/**
 * 动态生成组件脚本工具
 * 
 * @author guoweic
 *
 */
public class DynamicCompUtil {
	private static final String DYNAMICFIELD = ExtendAttributeSupport.DYN_ATTRIBUTE_KEY + "_dynamicfield";
	private static final String REPLACE_COMBODATA = "replace_combodata";
	private LfwPageContext pageCtx;
	private WidgetContext widgetCtx;
	public DynamicCompUtil(LfwPageContext pageCtx, WidgetContext widgetCtx) {
		this.pageCtx = pageCtx;
		this.widgetCtx = widgetCtx;
	}
	/**
	 * 生成增加Toolbar子项的脚本
	 * @return
	 */
	public String generateAddToolbarItemScript(String toolbarId, ToolBarItem item, String widgetId) {
		StringBuffer buf = new StringBuffer();
		ToolbarCompTag tag = new ToolbarCompTag();
		if (item.getType() != null && ToolBarItem.BUTTON_TYPE.equals(item.getType())) {  // 按钮子项
			String itemShowId = "tbi_" + item.getId();
			buf.append("var ")
			    .append(itemShowId)
			    .append(" = ")
			    .append("pageUI.getWidget('" + widgetId + "').getComponent('" + toolbarId + "')")
				.append(".addButton(\"")
				.append(item.getId())
				.append("\", \"")
				.append(tag.translate(item.getI18nName(), item.getText(), item.getLangDir()))
				.append("\", \"")
				.append(tag.translate(item.getTip(), item.getTipI18nName(), item.getLangDir()))
				.append("\", \"")
//				.append(tag.getRealImgPath(item.getRefImg()))
				.append(item.getRealRefImg())
				.append("\", \"")
				.append(item.getAlign()) // 此处配置的align为按钮在Toolbar中的位置
				.append("\", ")
				.append(item.isWithSep())
				.append(");\n");
			
			tag.setId(toolbarId);
			String event = tag.addEventSupport(item, widgetId, itemShowId, toolbarId);
			buf.append(event);
		}
		return buf.toString();
	}
	
	public String modifyGridStruct(String widgetId, Dataset ds, GridComp grid, Field[] fields){
		return modifyGridStruct(widgetId, ds, grid, fields, null, -1, true);
	}
	
	public String modifyGridStruct(String widgetId, Dataset ds, GridComp grid, Field[] fields, boolean clearOriginal){
		return modifyGridStruct(widgetId, ds, grid, fields, null, -1, clearOriginal);
	}
	
	public String modifyGridStruct(String widgetId, Dataset ds, GridComp grid, Field[] fields, int colIndex, boolean clearOriginal){
		return modifyGridStruct(widgetId, ds, grid, fields, null, colIndex, clearOriginal);
	}
	
	public String modifyGridStruct(String widgetId, Dataset ds, GridComp grid, Field[] fields, IGridColumn[] cols, int colIndex, boolean clearOriginal){
		ds.clear();
		if(clearOriginal){
			FieldSet fs = ds.getFieldSet();
			int count = fs.getFieldCount();
			for (int i = count - 1; i >= 0; i--) {
				Field gc = fs.getField(i);
				if(gc.getExtendAttributeValue(DYNAMICFIELD) != null){
					fs.removeField(gc);
				}
			}
			for (int i = 0; i < fields.length; i++) {
				fields[i].setExtendAttribute(DYNAMICFIELD, "1");
				fs.addField(fields[i]);
			}
			
			count = grid.getColumnCount();
			for (int i = count - 1; i >= 0; i --) {
				IGridColumn col = grid.getColumn(i);
				if(((ExtendAttributeSupport)col).getExtendAttributeValue(DYNAMICFIELD) != null)
					grid.removeColumn(col);
			}
			
			if(cols == null){
				cols = generateColsFromFields(fields);
			}
			
			int index = colIndex;
			if(colIndex == -1)
				index = grid.getColumnCount();
			for (int i = 0; i < cols.length; i++) {
				grid.insertColumn(index ++, cols[i]);
			}
		}
		
		/**
		 * 更新到页面初始结构中
		 */
		LfwWidget widget = LfwRuntimeEnvironment.getWebContext().getPageMeta().getWidget(widgetId);
		widget.getViewComponents().getComponentsMap().remove(grid.getId());
		widget.getViewComponents().addComponent((WebComponent) grid.clone());
		
		widget.getViewModels().removeDataset(ds.getId());
		widget.getViewModels().addDataset((Dataset) ds.clone());
		pageCtx.addBeforeExecScript("pageUI.getWidget('" + widgetId + "').getDataset('" + ds.getId() + "').clear()");
		pageCtx.addBeforeExecScript("pageUI.getWidget('" + widgetId + "').getDataset('" + ds.getId() + "').modifyStruct(" + DatasetMetaUtil.generateMeta(ds) + ")");
		String modelScript = GridModelUtil.generateGridModel(ds, grid, widget);
		pageCtx.addBeforeExecScript(modelScript);
		pageCtx.addBeforeExecScript("pageUI.getWidget('" + widgetId + "').getComponent('" + grid.getId() + "').setModel(model)");
		
		ds.setCurrentKey(Dataset.MASTER_KEY);
		return null;
	}
	
	private IGridColumn[] generateColsFromFields(Field[] fields) {
		IGridColumn[] cols = new IGridColumn[fields.length];
		for (int i = 0; i < fields.length; i++) {
			GridColumn col = new GridColumn();
			cols[i] = col;
			col.setId(fields[i].getId());
			col.setText(fields[i].getText());
			col.setI18nName(fields[i].getI18nName());
			String dataType = fields[i].getDataType();
			if(dataType == null || dataType.equals(""))
				dataType = StringDataTypeConst.STRING;
			col.setDataType(dataType);
			col.setEditorType(EditorTypeConst.STRINGTEXT);
			col.setExtendAttribute(DYNAMICFIELD, "1");
			col.setField(fields[i].getId());
		}
		return cols;
	}
	/**
	 * 刷新数据集
	 * @param dataset
	 */
	public void refreshDataset(Dataset dataset){
		refreshDataset(dataset, null);
	}
	
	/**
	 * 刷新数据集
	 * @param dataset
	 * @param pageIndex
	 */
	public void refreshDataset(Dataset dataset, Integer pageIndex) {
		String currentKey = dataset.getCurrentKey();
		dataset.removeRowSet(currentKey);
		if (pageCtx.isParent())
			pageCtx.addExecScript("getPopParent().pageUI.getWidget('" + widgetCtx.getId() + "').getDataset('" + dataset.getId() + "').setCurrentPage('" + currentKey + "')");
		else
			pageCtx.addExecScript("pageUI.getWidget('" + widgetCtx.getId() + "').getDataset('" + dataset.getId() + "').setCurrentPage('" + currentKey + "')");
	}
	
	/**
	 * 更新grid某一列的显示文字
	 * 
	 * @param widgetId
	 * @param gridId
	 * @param columnId
	 * @param text
	 */
	public void replaceGridColumnText(String widgetId,String gridId,String columnId,String text){
		if (pageCtx.isParent()) 
			pageCtx.addExecScript("getPopParent().pageUI.getWidget('" + widgetId + "').getComponent('" + gridId + "').getBasicHeaderById('" + columnId + "').replaceText('" + text + "')");
		else
			pageCtx.addExecScript("pageUI.getWidget('" + widgetId + "').getComponent('" + gridId + "').getBasicHeaderById('" + columnId + "').replaceText('" + text + "')");
	} 
	
	
	/**
	 * 激活某项tab页签
	 * @param tabId
	 */
	public void activeTabItem(String widgetId,String tabId, String tabIndex){
		pageCtx.addExecScript("pageUI.getWidget('" + widgetId + "').getTab('" + tabId + "').activeTab('" + tabIndex + "')");
	}
	
	/**
	 * 动态修改下拉数据值
	 * @param oriId
	 * @param widgetId
	 * @param cd
	 */
	public void replaceComboData(String oriId, String widgetId, ComboData cd){
		StringBuffer buf = new StringBuffer();
		String[] keyValue = getMergedCDkeyValues(cd);
		String pageUI = "pageUI";
		LfwWidget widget = LfwRuntimeEnvironment.getWebContext().getPageMeta().getWidget(widgetId);
		if (widget == null && LfwRuntimeEnvironment.getWebContext().getParentPageMeta() != null){
			widget = LfwRuntimeEnvironment.getWebContext().getParentPageMeta().getWidget(widgetId);
			pageUI = "parent.pageUI";
		}
		if (keyValue == null){
			buf.append(pageUI).append(".getWidget('")
			.append(widgetId)
			.append("').replaceComboData('")
			.append(oriId)
			.append("');\n");
		}
		else{
			buf.append(pageUI).append(".getWidget('")
			.append(widgetId)
			.append("').replaceComboData('")
			.append(oriId)
			.append("',")
			.append(keyValue[1])
			.append(",")
			.append(keyValue[0])
			.append(");\n");
		}
		
		ComboData oriCb = widget.getViewModels().getComboData(oriId);
		oriCb.setExtendAttribute(ElementObserver.OBS_IN, "1");
		oriCb.removeAllComboItems();
		CombItem[] items = cd.getAllCombItems();
		if(items != null){
			for (int i = 0; i < items.length; i++) {
				oriCb.addCombItem(items[i]);
			}
		}
		oriCb.removeExtendAttribute(ElementObserver.OBS_IN);
		
		Integer index = (Integer) pageCtx.getUserObject(REPLACE_COMBODATA + widgetId + "_" + oriId);
		if(index != null){
			pageCtx.removeExecScript(index);
		}
		index = pageCtx.addExecScript(buf.toString());
		pageCtx.addUserObject(REPLACE_COMBODATA + widgetId + "_" + oriId, index);
		
	}
	
	/**
	 * 生成dataset的metaData相关脚本
	 * 
	 * @param dataset
	 */
	public void generateDsMetaDataScript(Dataset dataset){
		int fieldCount = dataset.getFieldSet().getFieldCount();
		//"filed1":"4","filed2":4
		JSONObject jsonObj = new JSONObject();
		JSONObject precisionObj = new JSONObject();
		for(int i = 0; i < fieldCount; i ++){
			Field field = dataset.getFieldSet().getField(i);
			if (field.isCtxChanged() && field.checkCtxPropertyChanged("precision")){
				String precision = field.getPrecision();
				if (precision != null){
					precisionObj.put(field.getId(), precision);
				}
			}
		}
		if (precisionObj.length() > 0)
			jsonObj.put("precision", precisionObj);
		if (jsonObj.length() > 0)
			pageCtx.addBeforeExecScript("pageUI.getWidget('" + widgetCtx.getId() + "').getDataset('" + dataset.getId() + "').setMeta(" + jsonObj.toString() + ");");
	}
	
	private static String[] getMergedCDkeyValues(ComboData cd) {
		CombItem[] items = cd.getAllCombItems();
		if (items == null)
			return null;
		StringBuffer keyBuf = new StringBuffer();
		StringBuffer valueBuf = new StringBuffer();
		keyBuf.append("[");
		valueBuf.append("[");
		for (int i = 0; i < items.length; i++) {
			CombItem item = items[i];
			keyBuf.append("'")
			      .append(item.getValue())
			      .append("'");
			
			valueBuf.append("'")
		      .append(item.getText())
		      .append("'");
			
			if(i != (items.length - 1)){
				keyBuf.append(",");
				valueBuf.append(",");
			}
		}
		keyBuf.append("]");
		valueBuf.append("]");
		return new String[]{keyBuf.toString(), valueBuf.toString()};
	}
}
