package nc.uap.lfw.ra.util;

import java.util.Map;

import nc.uap.lfw.core.InteractionUtil;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.ChartComp;
import nc.uap.lfw.core.comp.CheckBoxComp;
import nc.uap.lfw.core.comp.CheckboxGroupComp;
import nc.uap.lfw.core.comp.ExcelComp;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IFrameComp;
import nc.uap.lfw.core.comp.ImageComp;
import nc.uap.lfw.core.comp.LabelComp;
import nc.uap.lfw.core.comp.LinkComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.ProgressBarComp;
import nc.uap.lfw.core.comp.RadioComp;
import nc.uap.lfw.core.comp.RadioGroupComp;
import nc.uap.lfw.core.comp.ReferenceComp;
import nc.uap.lfw.core.comp.SelfDefComp;
import nc.uap.lfw.core.comp.TextAreaComp;
import nc.uap.lfw.core.comp.ToolBarComp;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebPartComp;
import nc.uap.lfw.core.comp.text.ComboBoxComp;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.ComboInputItem;
import nc.uap.lfw.core.exception.InputItem;
import nc.uap.lfw.core.exception.IntInputItem;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LifeCyclePhase;
import nc.uap.lfw.core.page.RequestLifeCycleContext;
import nc.uap.lfw.jsp.uimeta.UIBorder;
import nc.uap.lfw.jsp.uimeta.UIBorderTrue;
import nc.uap.lfw.jsp.uimeta.UIButton;
import nc.uap.lfw.jsp.uimeta.UICanvas;
import nc.uap.lfw.jsp.uimeta.UICanvasPanel;
import nc.uap.lfw.jsp.uimeta.UICardLayout;
import nc.uap.lfw.jsp.uimeta.UICardPanel;
import nc.uap.lfw.jsp.uimeta.UIChartComp;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIExcelComp;
import nc.uap.lfw.jsp.uimeta.UIFlowhLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowhPanel;
import nc.uap.lfw.jsp.uimeta.UIFlowvLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIFormComp;
import nc.uap.lfw.jsp.uimeta.UIFormElement;
import nc.uap.lfw.jsp.uimeta.UIFormGroupComp;
import nc.uap.lfw.jsp.uimeta.UIGridComp;
import nc.uap.lfw.jsp.uimeta.UIGridLayout;
import nc.uap.lfw.jsp.uimeta.UIGridPanel;
import nc.uap.lfw.jsp.uimeta.UIGridRowLayout;
import nc.uap.lfw.jsp.uimeta.UIIFrame;
import nc.uap.lfw.jsp.uimeta.UIImageComp;
import nc.uap.lfw.jsp.uimeta.UILabelComp;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UILinkComp;
import nc.uap.lfw.jsp.uimeta.UIMenuGroup;
import nc.uap.lfw.jsp.uimeta.UIMenuGroupItem;
import nc.uap.lfw.jsp.uimeta.UIMenubarComp;
import nc.uap.lfw.jsp.uimeta.UIPanel;
import nc.uap.lfw.jsp.uimeta.UIPanelPanel;
import nc.uap.lfw.jsp.uimeta.UIPartComp;
import nc.uap.lfw.jsp.uimeta.UIProgressBar;
import nc.uap.lfw.jsp.uimeta.UISelfDefComp;
import nc.uap.lfw.jsp.uimeta.UIShutter;
import nc.uap.lfw.jsp.uimeta.UIShutterItem;
import nc.uap.lfw.jsp.uimeta.UISplitter;
import nc.uap.lfw.jsp.uimeta.UISplitterOne;
import nc.uap.lfw.jsp.uimeta.UISplitterTwo;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import nc.uap.lfw.jsp.uimeta.UITabItem;
import nc.uap.lfw.jsp.uimeta.UITabRightPanel;
import nc.uap.lfw.jsp.uimeta.UITextField;
import nc.uap.lfw.jsp.uimeta.UIToolBar;
import nc.uap.lfw.jsp.uimeta.UITreeComp;
import nc.uap.lfw.jsp.uimeta.UIWidget;

public class UIElementFactory {

	/**
	 * 2011-10-11 下午03:11:48 renxh des：根据类型创建UIElement
	 * 
	 * @param elementType
	 * @param widget
	 * @return
	 */
	public UIElement createUIElement(String elementType, String widget) {
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		try{
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			if(elementType == null){
				throw new LfwRuntimeException("容器不支持添加");
			}
			UIElement ele = this.createComponent(elementType, widget);
			if (ele == null)
				ele = this.createLayout(elementType, widget);
			if (ele == null)
				ele = this.createPanel(elementType, widget);
	
			return ele;
		}
		finally{
			RequestLifeCycleContext.get().setPhase(phase);
		}
	}
	
	/**
	 * 2011-10-11 下午03:11:48 renxh des：根据类型创建UIElement
	 * 
	 * @param elementType
	 * @param widget
	 * @return
	 */
	public UIFormElement createFormElement(String eleId, String formId, String widgetId, String eleType) {
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		try{
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			UIFormElement uiFormEle = new UIFormElement();
			uiFormEle.setId(eleId);
			uiFormEle.setFormId(formId);
			uiFormEle.setWidgetId(widgetId);
			uiFormEle.setElementType(eleType);
			if(eleType.equals(EditorTypeConst.RICHEDITOR)){
				uiFormEle.setHeight("100%");
				uiFormEle.setWidth("100%");
				uiFormEle.setEleWidth("80%");
			}
			return uiFormEle;
		}
		finally{
			RequestLifeCycleContext.get().setPhase(phase);
		}
	}
	/**
	 * 2011-10-11 下午05:04:37 renxh des：利用UIElement创建WebElement
	 * 
	 * @param ele
	 * @return
	 */
	public WebComponent createWebComponent(UIElement ele) {
		WebComponent comp = null;
		if (ele instanceof UIButton) {
			UIButton uiEle = (UIButton) ele;
			ButtonComp c = new ButtonComp(uiEle.getId());
			c.setText(uiEle.getId());

			comp = c;
		} else if (ele instanceof UIChartComp) {
			UIChartComp uiEle = (UIChartComp) ele;
			ChartComp c = new ChartComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UIExcelComp) {
			UIExcelComp uiEle = (UIExcelComp) ele;
			ExcelComp c = new ExcelComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UIFormComp) {
			UIFormComp uiEle = (UIFormComp) ele;
			FormComp c = new FormComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UIFormGroupComp) {
			UIFormGroupComp uiEle = (UIFormGroupComp) ele;
			FormComp c = new FormComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UIGridComp) {
			UIGridComp uiEle = (UIGridComp) ele;
			GridComp c = new GridComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UIIFrame) {
			UIIFrame uiEle = (UIIFrame) ele;
			IFrameComp c = new IFrameComp();
			c.setId(uiEle.getId());
			c.setSrc("");
			comp = c;
		} else if (ele instanceof UIImageComp) {
			UIImageComp uiEle = (UIImageComp) ele;
			ImageComp c = new ImageComp();
			c.setId(uiEle.getId());
			c.setImage1("");
			comp = c;
		} else if (ele instanceof UILabelComp) {
			UILabelComp uiEle = (UILabelComp) ele;
			LabelComp c = new LabelComp();
			c.setId(uiEle.getId());
			c.setText(uiEle.getId());
			comp = c;
		} else if (ele instanceof UILinkComp) {
			UILinkComp uiEle = (UILinkComp) ele;
			LinkComp c = new LinkComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UIMenubarComp) {
			UIMenubarComp uiEle = (UIMenubarComp) ele;
			MenubarComp c = new MenubarComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UIProgressBar){
			UIProgressBar uiEle = (UIProgressBar) ele;
			ProgressBarComp c = new ProgressBarComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UISelfDefComp) {
			UISelfDefComp uiEle = (UISelfDefComp) ele;
			SelfDefComp c = new SelfDefComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UITextField) {
			UITextField uiEle = (UITextField) ele;
			String type = uiEle.getType();
			if (type.equals(EditorTypeConst.CHECKBOX)) {
				CheckBoxComp c = new CheckBoxComp();
				c.setId(uiEle.getId());
				c.setEditorType(type);
				comp = c;
			} else if (type.equals(EditorTypeConst.CHECKBOXGROUP)) {
				CheckboxGroupComp c = new CheckboxGroupComp();
				c.setId(uiEle.getId());
				c.setEditorType(type);
				comp = c;
			} else if (type.equals(EditorTypeConst.COMBODATA)) {
				ComboBoxComp c = new ComboBoxComp();
				c.setId(uiEle.getId());
				c.setEditorType(type);
				comp = c;
			} else if (type.equals(EditorTypeConst.RADIOCOMP)) {
				RadioComp c = new RadioComp();
				c.setId(uiEle.getId());
				c.setEditorType(type);
				comp = c;
			} else if (type.equals(EditorTypeConst.RADIOGROUP)) {
				RadioGroupComp c = new RadioGroupComp();
				c.setId(uiEle.getId());
				c.setEditorType(type);
				comp = c;
			} else if (type.equals(EditorTypeConst.TEXTAREA)) {
				TextAreaComp c = new TextAreaComp();
				c.setId(uiEle.getId());
				c.setEditorType(type);
				comp = c;
			} else if (type.equals(EditorTypeConst.REFERENCE)) {
				ReferenceComp c = new ReferenceComp();
				c.setId(uiEle.getId());
				c.setEditorType(type);
				comp = c;
			} else {
				TextComp c = new TextComp();
				c.setId(uiEle.getId());
				c.setEditorType(type);
				comp = c;
			}

		} else if (ele instanceof UIToolBar) {
			UIToolBar uiEle = (UIToolBar) ele;
			ToolBarComp c = new ToolBarComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UITreeComp) {
			UITreeComp uiEle = (UITreeComp) ele;
			TreeViewComp c = new TreeViewComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if(ele instanceof UIPartComp){
			UIPartComp uiHtml = (UIPartComp)ele;
			WebPartComp html = new WebPartComp();
			html.setId(uiHtml.getId());
			html.setContentFetcher("nc.uap.lfw.core.comp.WebPartContentFetcherImpl");
			comp = html;
		}
		return comp;
	}

	public static  Map<String, String>  showInputForSplit(){
		StaticComboData comboData = new StaticComboData();
		
		CombItem item1 = new CombItem("1", "横向");
		CombItem item2 = new CombItem("0", "纵向");
		comboData.addCombItem(item1);
		comboData.addCombItem(item2);
		ComboInputItem ori = new ComboInputItem("orientation", "方向", true);
		ori.setComboData(comboData);
		InteractionUtil.showInputDialog("Splitter布局", new InputItem[] { ori});
		Map<String, String> rs = InteractionUtil.getInputDialogResult();
		return rs;
	}
	
	public static int showInputDialog(String title) {
		InputItem intInputItem = new IntInputItem("panelcount", "容器数量", true);
		InteractionUtil.showInputDialog("确认", new InputItem[] { intInputItem });
		Map<String, String> rs = InteractionUtil.getInputDialogResult();
		int count = 2;
		if (rs != null) {
			try {
				count = Integer.parseInt(rs.get("panelcount"));
			} catch (Exception e) {
				throw new LfwRuntimeException("你输入的信息有误！请输入数字！");
			}
			if(count <= 0 ){
				throw new LfwRuntimeException("你输入的信息有误！输入值应该大于0");
			}
		}
		return count;
	}

	public static Map<String, String> showInputDialogForTable() {
		InputItem row = new IntInputItem("rowcount", "行数", true);
		InputItem cell = new IntInputItem("cellcount", "列数", true);
		// InputItem type = new IntInputItem("type", "类别", true);
		InteractionUtil.showInputDialog("表格行列", new InputItem[] { row, cell });
		Map<String, String> rs = InteractionUtil.getInputDialogResult();
		return rs;
	}

	private Map<String, String> showInputDialogForBorderLayout() {
		StaticComboData comboData = new StaticComboData();
		CombItem item1 = new CombItem("true", "true");
		CombItem item2 = new CombItem("false", "false");
		comboData.addCombItem(item1);
		comboData.addCombItem(item2);
		ComboInputItem top = new ComboInputItem("top", "上", true);
		top.setComboData(comboData);

		ComboInputItem but = new ComboInputItem("buttom", "下", true);
		comboData = new StaticComboData();
		item1 = new CombItem("true", "true");
		item2 = new CombItem("false", "false");
		comboData.addCombItem(item1);
		comboData.addCombItem(item2);
		but.setComboData(comboData);

		ComboInputItem left = new ComboInputItem("left", "左", true);
		comboData = new StaticComboData();
		item1 = new CombItem("true", "true");
		item2 = new CombItem("false", "false");
		comboData.addCombItem(item1);
		comboData.addCombItem(item2);
		left.setComboData(comboData);

		ComboInputItem right = new ComboInputItem("right", "右", true);
		comboData = new StaticComboData();
		item1 = new CombItem("true", "true");
		item2 = new CombItem("false", "false");
		comboData.addCombItem(item1);
		comboData.addCombItem(item2);
		right.setComboData(comboData);

		InteractionUtil.showInputDialog("border布局", new InputItem[] { top, but, left, right });
		Map<String, String> rs = InteractionUtil.getInputDialogResult();
		return rs;
	}
	
	public LifeCyclePhase getPhase() {
		return RequestLifeCycleContext.get().getPhase();
	}
	
	public void setPhase(LifeCyclePhase phase){
		RequestLifeCycleContext.get().setPhase(phase);
	}

	/**
	 * 2011-10-11 下午03:12:11 renxh des：创建布局UIElement
	 * 
	 * @param elementType
	 * @param widget
	 * @return
	 */
	private UIElement createLayout(String elementType, String widget) {
		UILayout layout = null;
		if (elementType.equals(LfwPageContext.SOURCE_TYPE_WIDGT)) {
			UIWidget uiWieget = new UIWidget();
			uiWieget.setId(elementType);
		} 
		else if (elementType.equals(LfwPageContext.SOURCE_TYPE_FLOWVLAYOUT)) {
			layout = new UIFlowvLayout();
			String t = randomT(4);
			int count = showInputDialog("纵向流式布局");
			for (int i = 0; i < count; i++) {
				UIFlowvPanel panel = (UIFlowvPanel) this.createPanel(LfwPageContext.SOURCE_TYPE_FLOWVPANEL, widget);
				panel.setAttribute(UIFlowvPanel.ID, "panelv" + i + t);
				layout.addPanel(panel);
			}

		} 
		else if (elementType.equals(LfwPageContext.SOURCE_TYPE_PANELLAYOUT)) {
			layout = new UIPanel();
			String className = "";
			layout.setAttribute(UIPanel.CLASSNAME, className);

			String t = randomT(4);
			UILayoutPanel panel = (UILayoutPanel) this.createPanel(LfwPageContext.SOURCE_TYPE_PANELPANEL, widget);
			panel.setAttribute(UILayoutPanel.ID, LfwPageContext.SOURCE_TYPE_PANELPANEL+t);
			layout.addPanel(panel);
			
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_FLOWHLAYOUT)) {
			layout = new UIFlowhLayout();
			int count = showInputDialog("横向流式布局");
			String t = randomT(4);
			for (int i = 0; i < count; i++) {
				UIFlowhPanel panel = (UIFlowhPanel) this.createPanel(LfwPageContext.SOURCE_TYPE_FLOWHPANEL, widget);
				panel.setAttribute(UIFlowhPanel.ID, "panelh" + i + t);
				layout.addPanel(panel);
			}

		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_CARDLAYOUT)) {
			UICardLayout card = new UICardLayout();

			String currentItem = "0";
			if (currentItem != null) {
//				
//				String showValue = "cardpanel1";
//				if (currentItem.equals("1"))
//					showValue = "cardpanel2";
				LifeCyclePhase ori = this.getPhase();
				this.setPhase(LifeCyclePhase.nullstatus);
				card.setCurrentItem(currentItem);
				this.setPhase(ori);
				
			}

			int count = showInputDialog("卡片容器");
			String t = randomT(4);
			for (int i = 0; i < count; i++) {
				UICardPanel panel = (UICardPanel) this.createPanel(LfwPageContext.SOURCE_TYPE_CARDPANEL, widget);
				panel.setAttribute(UIFlowhPanel.ID, "cardpanel" + i + t);
				card.addPanel(panel);
			}
			layout = card;

		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_SPLITERLAYOUT)) {
			UISplitter spliter = new UISplitter();

			Map<String, String> oriMap = showInputForSplit();
			
			String orient = oriMap.get("orientation");
			
			if(orient.equals("1")){
				spliter.setOrientation(Integer.valueOf(1));
				spliter.setHideDirection(Integer.valueOf(1));
			}
			else{
				spliter.setOrientation(Integer.valueOf(0));
				spliter.setHideDirection(Integer.valueOf(1));
			}
			String boundMode = null;
			if (boundMode != null && !boundMode.equals(""))
				spliter.setBoundMode(Integer.valueOf(boundMode));
			String inverseValue = null;
			if (inverseValue != null && !inverseValue.equals(""))
				spliter.setInverse(Integer.valueOf(inverseValue));
			String divideSize = null;
			if (divideSize != null)
				spliter.setDivideSize(divideSize);
//			String spliterWidth = null;
//			if (spliterWidth != null)
//				spliter.setSpliterWidth(spliterWidth);
//			else
//				spliter.setSpliterWidth("4");
//			String hideBar = null;
//			if (hideBar != null && hideBar.equals("true"))
//				spliter.setHideBar(Integer.valueOf(1));
//			else
//				spliter.setHideBar(Integer.valueOf(0));

			String oneTouch = null;
			if (oneTouch != null && oneTouch.equals("true")) {
				spliter.setOneTouch(Integer.valueOf(1));
			} else
				spliter.setOneTouch(Integer.valueOf(0));
			
			UISplitterOne splite1 = (UISplitterOne) this.createPanel(LfwPageContext.SOURCE_TYPE_SPLITERONEPANEL, widget);
			splite1.setAttribute(UISplitterOne.ID, "splitter1" + randomT(3));
			
			UISplitterTwo splite2 = (UISplitterTwo) this.createPanel(LfwPageContext.SOURCE_TYPE_SPLITERTWOPANLE, widget);
			splite2.setAttribute(UISplitterTwo.ID, "splitter2" + randomT(3));
			
			spliter.addPanel(splite1);
			spliter.addPanel(splite2);
			
			layout = spliter;
		} 
		else if (elementType.equals(LfwPageContext.SOURCE_TYPE_MENU_GROUP)) {
			layout = new UIMenuGroup();
		} 
		else if (elementType.equals(LfwPageContext.SOURCE_TYPE_BORDER)) {
			layout = new UIBorder();
			String width = "100";
			layout.setAttribute("width", width);
			String leftWidth = "1";
			layout.setAttribute("leftWidth", leftWidth);
			String rightWidth = "1";
			layout.setAttribute("rightWidth", rightWidth);
			String topWidth = "1";
			layout.setAttribute("topWidth", topWidth);
			String bottomWidth = "1";
			layout.setAttribute("bottomWidth", bottomWidth);

			String showLeft = null;
			if (showLeft == null)
				((UIBorder) layout).setShowLeft(0);
			else {
				((UIBorder) layout).setShowLeft(showLeft.equals("true") ? 0 : 1);
			}
			String showRight = null;
			if (showRight == null)
				((UIBorder) layout).setShowRight(0);
			else {
				((UIBorder) layout).setShowRight(showRight.equals("true") ? 0 : 1);
			}

			String showTop = null;
			if (showTop == null)
				((UIBorder) layout).setShowTop(0);
			else {
				((UIBorder) layout).setShowTop(showTop.equals("true") ? 0 : 1);
			}

			String showBottom = null;
			if (showBottom == null)
				((UIBorder) layout).setShowBottom(0);
			else {
				((UIBorder) layout).setShowBottom(showBottom.equals("true") ? 0 : 1);
			}

			String color = "";
			layout.setAttribute(UIBorder.COLOR, color);

			String leftcolor = "";
			layout.setAttribute(UIBorder.LEFTCOLOR, leftcolor);

			String rightcolor = "";
			layout.setAttribute(UIBorder.RIGHTCOLOR, rightcolor);

			String topcolor = "";
			layout.setAttribute(UIBorder.TOPCOLOR, topcolor);

			String bottomcolor = "";
			layout.setAttribute(UIBorder.BOTTOMCOLOR, bottomcolor);

			String className = "";
			layout.setAttribute(UIBorder.CLASSNAME, className);
			String t = randomT(4);
			UIBorderTrue panel = (UIBorderTrue) this.createPanel(LfwPageContext.SOURCE_TYPE_BORDERTRUE, widget);
			panel.setAttribute(UIBorderTrue.ID, LfwPageContext.SOURCE_TYPE_BORDERTRUE+t);
			layout.addPanel(panel);

		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_TAG)) {
			UITabComp tabComp = new UITabComp();
			String currentIndex = "0";
			if (currentIndex != null){
				LifeCyclePhase ori = this.getPhase();
				this.setPhase(LifeCyclePhase.nullstatus);
				tabComp.setCurrentItem(currentIndex);
				this.setPhase(ori);
			}
				

			String oneTabHide = "false";
			if (oneTabHide != null && !oneTabHide.equals("")) {
				tabComp.setOneTabHide(oneTabHide.equals("true") ? 1 : 0);
			}

			int count = showInputDialog("页签容器");
			String t = randomT(4);
			for (int i = 0; i < count; i++) {
				UITabItem item = (UITabItem) this.createPanel(LfwPageContext.SOURCE_TYPE_TABITEM, widget);
				item.setId("UITabItem" + i + t);
				item.setText("页签" + (i + 1));
				tabComp.addPanel(item);
			}
			// UITabRightPanelPanel rightPanel = (UITabRightPanelPanel)
			// this.createPanel(LfwPageContext.SOURCE_TYPE_TABSPACE, widget);
			// rightPanel.setAttribute(UITabRightPanelPanel.ID, "rightPanel" +
			// t);
			// tabComp.setRightPanel(rightPanel);
			layout = tabComp;

		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_OUTLOOKBAR)) {
			layout = new UIShutter();

			String className = "";
			((UIShutter) layout).setClassName(className);

			String currentItem = null;
			if (currentItem != null && !currentItem.equals("")) {
				((UIShutter) layout).setCurrentItem(currentItem);
			}

			int count = showInputDialog("百叶窗容器");
			String t = randomT(4);
			for (int i = 0; i < count; i++) {
				UIShutterItem item = (UIShutterItem) this.createPanel(LfwPageContext.SOURCE_TYPE_OUTLOOKBAR_ITEM, widget);
				item.setId("UIShutterItem" + i + t);
				item.setText("item" + (i + 1));
				layout.addPanel(item);
			}

		}else if (elementType.equals(LfwPageContext.SOURCE_TYPE_CANVASLAYOUT)){
			layout = new UICanvas();
			UICanvasPanel panel = (UICanvasPanel) this.createPanel(LfwPageContext.SOURCE_TYPE_CANVASPANEL, widget);
			panel.setId(LfwPageContext.SOURCE_TYPE_CANVASPANEL + randomT(4));
			layout.addPanel(panel);
		}
		else if (elementType.equals(LfwPageContext.SOURCE_TYPE_GRIDLAYOUT)) {
			int rowcount = 3;
			int cellcount = 3;
			int type = 0;
			Map<String, String> rs = this.showInputDialogForTable();
			if (rs != null && !rs.isEmpty()) {
				try {
					rowcount = Integer.parseInt(rs.get("rowcount"));
				} catch (Exception e) {
					throw new LfwRuntimeException("您输入的信息有误！");
				}
				try {
					cellcount = Integer.parseInt(rs.get("cellcount"));
				} catch (Exception e) {
					throw new LfwRuntimeException("您输入的信息有误！");
				}
				// try {
				// type = Integer.parseInt(rs.get("type"));
				// } catch (Exception e) {
				// throw new LfwRuntimeException("您输入的信息有误！");
				// }
				// if (type > 1 && type < 0) {
				// throw new LfwRuntimeException("您输入的信息有误！0:表格布局，1:自由表单");
				// }
				type = 0;
			}
			if (type == 0) {
				UIGridLayout layout2 = new UIGridLayout();
				layout2.setRowcount(rowcount);
				layout2.setColcount(cellcount);
				// layout2.setGridType(0);
				String t = randomT(4);
				for (int i = 0; i < rowcount; i++) {
					UIGridRowLayout row = new UIGridRowLayout();
					row.setId("row" + i + t);
					row.setAttribute(UIGridLayout.WIDGET_ID, widget);
					for (int j = 0; j < cellcount; j++) {
						UIGridPanel cell = new UIGridPanel();
						cell.setId("cell" + i + j + t);
						cell.setColWidth("120");
						cell.setColHeight("30");
						cell.setAttribute(UIGridLayout.WIDGET_ID, widget);
						row.addPanel(cell);
					}
					layout2.addGridRow(row);
				}

				// layout2.setWidth("100");
				// layout2.setHeight("100");
				layout2.setBorder("1");
				layout = layout2;
			} else {
				UIGridLayout layout2 = new UIGridLayout();
				layout2.setRowcount(rowcount);
				layout2.setColcount(cellcount * 3);
				// layout2.setGridType(1);

				String t = randomT(4);
				for (int i = 0; i < rowcount; i++) {
					UIGridRowLayout row = new UIGridRowLayout();
					row.setId("row" + i + t);
					row.setAttribute(UIGridLayout.WIDGET_ID, widget);
					for (int j = 0; j < cellcount; j++) {
						UIGridPanel cell0 = new UIGridPanel();
						cell0.setId("cell0" + i + j + t);
						cell0.setColWidth("120");
						cell0.setColHeight("30");
						cell0.setCellType("0");
						cell0.setAttribute(UIGridLayout.WIDGET_ID, widget);
						row.addPanel(cell0);

						UIGridPanel cell1 = new UIGridPanel();
						cell1.setId("cell1" + i + j + t);
						cell1.setColWidth("1");
						cell1.setColHeight("30");
						cell1.setCellType("1");
						cell1.setAttribute(UIGridLayout.WIDGET_ID, widget);
						row.addPanel(cell1);

						UIGridPanel cell2 = new UIGridPanel();
						cell2.setId("cell2" + i + j + t);
						cell2.setColWidth("120");
						cell2.setColHeight("30");
						cell2.setCellType("2");
						cell2.setAttribute(UIGridLayout.WIDGET_ID, widget);
						row.addPanel(cell2);
					}
					layout2.addGridRow(row);
				}

				// layout2.setWidth("100%");
				// layout2.setHeight("100%");
				layout2.setBorder("1");
				layout = layout2;
			}

		}
		if (null != layout) {
			layout.setAttribute(UIFlowvLayout.ID, elementType);
			layout.setAttribute(UIFlowvLayout.WIDGET_ID, widget);
		}
		return layout;
	}

	public static String randomT(int length) {
		String t = String.valueOf(System.currentTimeMillis());
		return t.substring(t.length() - length);
	}

	/**
	 * 2011-10-11 下午02:27:17 renxh des：创建控件UIElement
	 * 
	 * @param elementType
	 */
	private UIElement createComponent(String elementType, String widget) {
		UIComponent comp = null;
		if (elementType.equals(LfwPageContext.SOURCE_TYPE_GRID)) {
			comp = new UIGridComp();
		}

		if (elementType.equals(LfwPageContext.SOURCE_TYPE_EXCEL)) {
			comp = new UIExcelComp();
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_TREE)) {
			comp = new UITreeComp();
		}

		else if (elementType.equals(LfwPageContext.SOURCE_TYPE_FORMCOMP)) {
			comp = new UIFormComp();
		}

		else if (elementType.equals(LfwPageContext.SOURCE_TYPE_FORMGROUP)) {
			comp = new UIFormGroupComp();
			String forms = null;
			((UIFormGroupComp) comp).setForms(forms);
		}

		else if (elementType.equals(LfwPageContext.SOURCE_TYPE_MENUBAR_MENUITEM) || elementType.equals(LfwPageContext.SOURCE_TYPE_MENUBAR)) {
			comp = new UIMenubarComp();
		}

		else if (elementType.equals(LfwPageContext.SOURCE_TYPE_IFRAME)) {
			comp = new UIIFrame();
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_HTMLCONTENT)) { // html内容
			UIPartComp hcomp = new UIPartComp();
			comp = hcomp;
			
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_TEXT)) {
			comp = new UITextField();
			String type = "text";
			((UITextField) comp).setType(type);
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_BUTTON)) {
			comp = new UIButton();
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_CHART))
			comp = new UIChartComp();
		else if (elementType.equals(LfwPageContext.SOURCE_TYPE_LINKCOMP)) {
			comp = new UILinkComp();
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_TOOLBAR_BUTTON)) {
			comp = new UIToolBar();
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_LABEL)) {
			comp = new UILabelComp();
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_IMAGECOMP)) {
			comp = new UIImageComp();
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_SELF_DEF_COMP)) {
			comp = new UISelfDefComp();
		}else if (elementType.equals(LfwPageContext.SOURCE_TYPE_PROGRESS_BAR)){
			comp = new UIProgressBar();
		} else {
			String editorType = EditorTypeConst.getEditorTypeByString(elementType);
			if (editorType != null) {
				comp = new UITextField();
				((UITextField) comp).setType(editorType);
			}
		}
		if (comp != null) {
			comp.setId(elementType);
			comp.setWidgetId(widget);
		}
		return comp;
	}
	
	

	/**
	 * 2011-10-11 下午03:12:34 renxh des：创建容器UIElement
	 * 
	 * @param elementType
	 * @param widget
	 * @return
	 */
	private UIElement createPanel(String elementType, String widget) {
		UILayoutPanel panel = null;
		if (elementType.equals(LfwPageContext.SOURCE_TYPE_PANELPANEL)) {
			UIPanelPanel panelLayout = new UIPanelPanel();
			panel = panelLayout;
		}

		else if (elementType.equals(LfwPageContext.SOURCE_TYPE_FLOWVPANEL)) {
			UIFlowvPanel flowvLayout = new UIFlowvPanel();
//			String height = null;
//			if (height != null)
//				flowvLayout.setHeight(height);
//			String anchor = null;
//			if (anchor != null)
//				flowvLayout.setAnchor(anchor);
			panel = flowvLayout;
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_FLOWHPANEL)) {
			UIFlowhPanel flowhLayout = new UIFlowhPanel();
			String width = null;
			if (width != null)
				flowhLayout.setWidth(width);
			panel = flowhLayout;
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_CARDPANEL)) {
			UICardPanel cardPanel = new UICardPanel();
			panel = cardPanel;
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_SPLITERONEPANEL)) {
			UISplitterOne one = new UISplitterOne();
			panel = one;
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_SPLITERTWOPANLE)) {
			UISplitterTwo two = new UISplitterTwo();
			panel = two;
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_BORDERTRUE)) {
			UIBorderTrue border = new UIBorderTrue();
			panel = border;
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_CANVASPANEL)){
			UICanvasPanel canvasPanel = new UICanvasPanel();
			panel = canvasPanel;
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_TABSPACE)) {
			UITabRightPanel item = new UITabRightPanel();
			String width = "10";
			item.setWidth(width);
			panel = item;
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_TABITEM)) {
			UITabItem item = new UITabItem();

			String text = elementType;
			item.setText(text);

			String i18nName = null;
			item.setI18nName(i18nName);

			String state = null;
			if (state != null && !state.equals("")) {
				item.setState(Integer.valueOf(state));
			}
			panel = item;
		}

		else if (elementType.equals(LfwPageContext.SOURCE_TYPE_OUTLOOKBAR_ITEM)) {
			UIShutterItem item = new UIShutterItem();

			String text = elementType;
			item.setText(text);

			String i18nName = null;
			item.setI18nName(i18nName);

			panel = item;
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_MENU_GROUP_ITEM)) {
			UIMenuGroupItem groupItem = new UIMenuGroupItem();
			String state = null;
			if (state != null)
				groupItem.setState(Integer.valueOf(state));
			panel = groupItem;
		} else if (elementType.equals(LfwPageContext.SOURCE_TYPE_GRIDPANEL)) {
			UIGridPanel gridPanel = new UIGridPanel();
			String rowSpan = "1";
			if (rowSpan != null)
				gridPanel.setRowSpan(rowSpan);
			String colSpan = "1";
			if (colSpan != null)
				gridPanel.setColSpan(colSpan);

			String colWidth = null;
			if (colWidth != null)
				gridPanel.setColWidth(colWidth);

			String colHeight = null;
			if (colHeight != null)
				gridPanel.setColHeight(colHeight);

			panel = gridPanel;
		}
		if (panel != null) {
			panel.setAttribute(UILayoutPanel.ID, elementType);
			panel.setAttribute(UILayoutPanel.WIDGET_ID, widget);
		}
		return panel;

	}
}
