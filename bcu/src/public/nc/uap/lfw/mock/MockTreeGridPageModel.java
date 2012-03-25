package nc.uap.lfw.mock;

import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.RecursiveTreeLevel;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.event.AppRequestProcessor;
import nc.uap.lfw.core.event.conf.DatasetListener;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.event.conf.WidgetRule;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.IUIMeta;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIButton;
import nc.uap.lfw.jsp.uimeta.UIFlowhLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowhPanel;
import nc.uap.lfw.jsp.uimeta.UIFlowvLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIGridComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UITreeComp;
import nc.uap.lfw.jsp.uimeta.UIWidget;

/**
 * 树表型页面生成类
 * @author zhangxya
 *
 */
public class MockTreeGridPageModel extends PageModel {
	private static final String TREE = "tree";
	private static final String TREEDS = "treeDs";
	
	
	private static final String GRID = "grid";
	private static final String GRIDDS = "masterDs";
	
	private static final String CANCELBT = "cancelbt";
	private static final String OKBT = "okbt";
	private static final String WIDGET_ID = "main";
	private static final String ID_FIELD = "id";
	private static final String PID_FIELD = "pid";
	private static final String LABEL_FIELD = "label";
	private static final String LOAD_FIELD = "load";
	@Override
	protected IUIMeta createUIMeta(PageMeta pm) {
		UIMeta uimeta = new UIMeta();
		
		UIWidget widget = new UIWidget();
		widget.setId(WIDGET_ID);
		UIMeta wuimeta = new UIMeta();
		wuimeta.setId(WIDGET_ID + "_meta");
		widget.setUimeta(wuimeta);
		
		constructViewUI(wuimeta);
		
		uimeta.setReference(1);
		uimeta.setElement(widget);
		return uimeta;
	}

	private void constructViewUI(UIMeta wuimeta) {
			
		UIFlowvLayout flowvLayout = new UIFlowvLayout();
		flowvLayout.setId("flowvLayout");
		wuimeta.setElement(flowvLayout);
	
		//center
		UIFlowvPanel centerFlowvPanel = new UIFlowvPanel();
		flowvLayout.addPanel(centerFlowvPanel);
				
		
		UIFlowhLayout flowhLayout = new UIFlowhLayout();
		flowhLayout.setId("flowhlayout");
		
		centerFlowvPanel.setElement(flowhLayout);
		
		UIFlowhPanel uiSplitterOne = new UIFlowhPanel();
		uiSplitterOne.setId("panel1");
		uiSplitterOne.setWidth("200");
		uiSplitterOne.setRightBorder("#");
		flowhLayout.addPanel(uiSplitterOne);
		
		UIFlowhPanel uiSplitterTwo = new UIFlowhPanel();
		uiSplitterTwo.setId("panel2");
		flowhLayout.addPanel(uiSplitterTwo);
		
		UITreeComp treeComp = new UITreeComp();
		treeComp.setId("reftree");
		treeComp.setWidgetId("main");
		uiSplitterOne.setElement(treeComp);
		
		UIGridComp uigridComp = new UIGridComp();
		uigridComp.setId("refgrid");
		uigridComp.setWidgetId("main");
		uiSplitterTwo.setElement(uigridComp);
		
		//bottom
		UIFlowvPanel borderPanelBottom = new UIFlowvPanel();
		borderPanelBottom.setId("bottomvp1");
		borderPanelBottom.setHeight("30");
		flowvLayout.addPanel(borderPanelBottom);

		UIFlowhLayout uiflowhLayout = new UIFlowhLayout();
		uiflowhLayout.setId("flowhLayout");
		borderPanelBottom.setElement(uiflowhLayout);
		
		UIFlowhPanel flowhPanel1 = new UIFlowhPanel();
		flowhPanel1.setAttribute("id", "flowhPanel1");
		uiflowhLayout.addPanel(flowhPanel1);
	
		UIFlowhPanel flowhPanel2 = new UIFlowhPanel();
		flowhPanel2.setAttribute("id", "flowhPanel2");
		uiflowhLayout.addPanel(flowhPanel2);
		flowhPanel2.setWidth("80");
		
		UIButton buttonOk = new UIButton();
		buttonOk.setId("okbt");
		buttonOk.setWidgetId("main");
		flowhPanel2.setElement(buttonOk); 
		
		UIFlowhPanel flowhPanel3 = new UIFlowhPanel();
		uiflowhLayout.addPanel(flowhPanel3);
		flowhPanel3.setAttribute("id", "flowhPanel3");
		flowhPanel3.setWidth("100");
		
		UIButton buttonCanCel = new UIButton();
		buttonCanCel.setId("cancelbt");
		buttonCanCel.setWidgetId("main");
		flowhPanel3.setElement(buttonCanCel);
	}

	@Override
	protected PageMeta createPageMeta() {
		PageMeta pm = new PageMeta();
		pm.setId(getWebContext().getPageId());
		pm.setProcessorClazz(AppRequestProcessor.class.getName());
		LfwWidget widget = new LfwWidget();
		constructWidget(widget);
		widget.setId(WIDGET_ID);
		pm.addWidget(widget);
		
		Dataset ds = new Dataset();
		ds.setId(TREEDS);
		ds.setLazyLoad(false);
		constructTreeDataset(ds);
		widget.getViewModels().addDataset(ds);
		
		TreeViewComp tree = new TreeViewComp();
		tree.setId(TREE);
		constructTree(tree);
		RecursiveTreeLevel level = new RecursiveTreeLevel();
		level.setId("level1");
		constructTreeLevel(level);
		tree.setTopLevel(level);
		widget.getViewComponents().addComponent(tree);
		
		
		Dataset gridDs = new Dataset();
		gridDs.setId(GRIDDS);
		gridDs.setLazyLoad(true);
		constructGridDataset(gridDs);
		widget.getViewModels().addDataset(gridDs);
		
		GridComp grid = new GridComp();
		grid.setId(GRID);
		grid.setDataset(GRIDDS);
		constructGrid(grid);
		
		widget.getViewComponents().addComponent(grid);
		
		
		ButtonComp okbt = new ButtonComp();
		constructOkButton(okbt);
		widget.getViewComponents().addComponent(okbt);
		
		ButtonComp cancelbt = new ButtonComp();
		constructCancelButton(cancelbt);
		widget.getViewComponents().addComponent(cancelbt);
		return pm;
	}

	
	protected void constructGrid(GridComp grid) {
		GridColumn columnId = generateGridColumn(ID_FIELD);
		grid.addColumn(columnId);
		
		GridColumn columnCode = generateGridColumn(LOAD_FIELD);
		grid.addColumn(columnCode);
	}
	
	
	private GridColumn generateGridColumn(String id) {
		GridColumn columnId = new GridColumn();
		columnId.setId(id);
		columnId.setText(id);
		columnId.setWidth(120);
		columnId.setDataType(StringDataTypeConst.STRING);
		columnId.setVisible(true);
		columnId.setEditable(false);
		return columnId;
	}
	
	protected void constructWidget(LfwWidget widget) {
	
	}

	protected void constructTree(TreeViewComp tree) {
		
	}

	protected void constructOkButton(ButtonComp okbt) {
		okbt.setId(OKBT);
		okbt.setText("确定");
		
		EventConf event = new EventConf();
		event.setName(MouseListener.ON_CLICK);
		event.setMethodName("okButtonClick");
		
		EventSubmitRule submitRule = new EventSubmitRule();
		WidgetRule pwr = new WidgetRule();
		pwr.setId("main");
		DatasetRule dsRule = new DatasetRule();
		dsRule.setId(TREEDS);
		submitRule.addWidgetRule(pwr);
		pwr.addDsRule(dsRule);
		event.setSubmitRule(submitRule);
		
		
		EventSubmitRule parentSum = new EventSubmitRule();
		WidgetRule pwidgetRule = new WidgetRule();
		parentSum.addWidgetRule(pwidgetRule);
		pwidgetRule.setId("main");
		submitRule.setParentSubmitRule(parentSum);
		
		event.setJsEventClaszz(MouseListener.class.getName());
		okbt.addEventConf(event);
	}

	protected void constructCancelButton(ButtonComp cancelbt) {
		cancelbt.setId(CANCELBT);
		cancelbt.setText("取消");
		
		EventConf event = new EventConf();
		event.setName(MouseListener.ON_CLICK);
		event.setMethodName("cancelButtonClick");
		event.setJsEventClaszz(MouseListener.class.getName());
		cancelbt.addEventConf(event);
	}

	protected void constructTreeLevel(RecursiveTreeLevel level) {
		level.setMasterKeyField(ID_FIELD);
		level.setLabelFields(LABEL_FIELD);
		level.setRecursiveKeyField(ID_FIELD);
		level.setRecursivePKeyField(PID_FIELD);
		level.setLoadField(LOAD_FIELD);
		level.setDataset(TREEDS);
	}
	
	
	
	protected void constructGridDataset(Dataset ds) {
		FieldSet fs = ds.getFieldSet();
		
		Field idField = new Field();
		idField.setId(ID_FIELD);
		idField.setText("ID");
		fs.addField(idField);
		
		Field pidField = new Field();
		pidField.setId(PID_FIELD);
		pidField.setText("PID");
		fs.addField(pidField);
		
		Field labelField = new Field();
		labelField.setId(LABEL_FIELD);
		labelField.setText("LABEL");
		fs.addField(labelField);
		
		Field loadField = new Field();
		loadField.setId(LOAD_FIELD);
		loadField.setText("LOAD");
		fs.addField(loadField);
	}

	protected void constructTreeDataset(Dataset ds) {
		FieldSet fs = ds.getFieldSet();
		
		Field idField = new Field();
		idField.setId(ID_FIELD);
		idField.setText("ID");
		fs.addField(idField);
		
		Field pidField = new Field();
		pidField.setId(PID_FIELD);
		pidField.setText("PID");
		fs.addField(pidField);
		
		Field labelField = new Field();
		labelField.setId(LABEL_FIELD);
		labelField.setText("LABEL");
		fs.addField(labelField);
		
		Field loadField = new Field();
		loadField.setId(LOAD_FIELD);
		loadField.setText("LOAD");
		fs.addField(loadField);
		
		EventConf event = new EventConf();
		event.setName(DatasetListener.ON_DATA_LOAD);
		event.setMethodName("dataLoad");
		event.setJsEventClaszz(DatasetListener.class.getName());
		ds.addEventConf(event);
		
		
		EventConf onafterRowSelevent = new EventConf();
		onafterRowSelevent.setName(DatasetListener.ON_AFTER_ROW_SELECT);
		onafterRowSelevent.setMethodName("afterRowSel");
		onafterRowSelevent.setJsEventClaszz(DatasetListener.class.getName());
		ds.addEventConf(onafterRowSelevent);
	}
}

