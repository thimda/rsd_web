package nc.uap.lfw.ra.group;

import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.ChartComp;
import nc.uap.lfw.core.comp.CheckBoxComp;
import nc.uap.lfw.core.comp.CheckboxGroupComp;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.ExcelComp;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IFrameComp;
import nc.uap.lfw.core.comp.ImageComp;
import nc.uap.lfw.core.comp.LabelComp;
import nc.uap.lfw.core.comp.LinkComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.RadioComp;
import nc.uap.lfw.core.comp.RadioGroupComp;
import nc.uap.lfw.core.comp.SelfDefComp;
import nc.uap.lfw.core.comp.TextAreaComp;
import nc.uap.lfw.core.comp.ToolBarComp;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.WebPartComp;
import nc.uap.lfw.core.comp.WebSilverlightWidget;
import nc.uap.lfw.core.comp.text.ComboBoxComp;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIBorder;
import nc.uap.lfw.jsp.uimeta.UIBorderTrue;
import nc.uap.lfw.jsp.uimeta.UIButton;
import nc.uap.lfw.jsp.uimeta.UICanvas;
import nc.uap.lfw.jsp.uimeta.UICanvasPanel;
import nc.uap.lfw.jsp.uimeta.UICardLayout;
import nc.uap.lfw.jsp.uimeta.UICardPanel;
import nc.uap.lfw.jsp.uimeta.UIChartComp;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIDiv;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIExcelComp;
import nc.uap.lfw.jsp.uimeta.UIFlowhLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowhPanel;
import nc.uap.lfw.jsp.uimeta.UIFlowvLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIFormComp;
import nc.uap.lfw.jsp.uimeta.UIFormElement;
import nc.uap.lfw.jsp.uimeta.UIGridComp;
import nc.uap.lfw.jsp.uimeta.UIGridLayout;
import nc.uap.lfw.jsp.uimeta.UIGridPanel;
import nc.uap.lfw.jsp.uimeta.UIGridRowLayout;
import nc.uap.lfw.jsp.uimeta.UIGridRowPanel;
import nc.uap.lfw.jsp.uimeta.UIIFrame;
import nc.uap.lfw.jsp.uimeta.UIImageComp;
import nc.uap.lfw.jsp.uimeta.UILabelComp;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UILinkComp;
import nc.uap.lfw.jsp.uimeta.UIMenuGroup;
import nc.uap.lfw.jsp.uimeta.UIMenuGroupItem;
import nc.uap.lfw.jsp.uimeta.UIMenubarComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIPanel;
import nc.uap.lfw.jsp.uimeta.UIPanelPanel;
import nc.uap.lfw.jsp.uimeta.UIPartComp;
import nc.uap.lfw.jsp.uimeta.UISelfDefComp;
import nc.uap.lfw.jsp.uimeta.UIShutter;
import nc.uap.lfw.jsp.uimeta.UIShutterItem;
import nc.uap.lfw.jsp.uimeta.UISilverlightWidget;
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
import nc.uap.lfw.ra.itf.IUIRender;
import nc.uap.lfw.ra.itf.IUIRenderGroup;
import nc.uap.lfw.ra.render.UILayoutPanelRender;
import nc.uap.lfw.ra.render.UILayoutRender;
import nc.uap.lfw.ra.render.UIMetaRender;
import nc.uap.lfw.ra.render.UIRender;
import nc.uap.lfw.ra.render.pc.PCBorderRender;
import nc.uap.lfw.ra.render.pc.PCBorderTrueRender;
import nc.uap.lfw.ra.render.pc.PCButtonCompRender;
import nc.uap.lfw.ra.render.pc.PCCanvasLayoutRender;
import nc.uap.lfw.ra.render.pc.PCCanvasPanelRender;
import nc.uap.lfw.ra.render.pc.PCCardLayoutRender;
import nc.uap.lfw.ra.render.pc.PCCardPanelRender;
import nc.uap.lfw.ra.render.pc.PCChartCompRender;
import nc.uap.lfw.ra.render.pc.PCCheckBoxCompRender;
import nc.uap.lfw.ra.render.pc.PCCheckboxGroupCompRender;
import nc.uap.lfw.ra.render.pc.PCComboCompRender;
import nc.uap.lfw.ra.render.pc.PCContextMenuCompRender;
import nc.uap.lfw.ra.render.pc.PCDivRender;
import nc.uap.lfw.ra.render.pc.PCExcelCompRender;
import nc.uap.lfw.ra.render.pc.PCFlowhLayoutRender;
import nc.uap.lfw.ra.render.pc.PCFlowhPanelRender;
import nc.uap.lfw.ra.render.pc.PCFlowvLayoutRender;
import nc.uap.lfw.ra.render.pc.PCFlowvPanelRender;
import nc.uap.lfw.ra.render.pc.PCFormCompRender;
import nc.uap.lfw.ra.render.pc.PCFormElementRender;
import nc.uap.lfw.ra.render.pc.PCGridCompRender;
import nc.uap.lfw.ra.render.pc.PCGridLayoutRender;
import nc.uap.lfw.ra.render.pc.PCGridPanelRender;
import nc.uap.lfw.ra.render.pc.PCGridRowLayoutRender;
import nc.uap.lfw.ra.render.pc.PCGridRowPanelRender;
import nc.uap.lfw.ra.render.pc.PCIFrameRender;
import nc.uap.lfw.ra.render.pc.PCImageCompRender;
import nc.uap.lfw.ra.render.pc.PCLabelCompRender;
import nc.uap.lfw.ra.render.pc.PCLinkCompRender;
import nc.uap.lfw.ra.render.pc.PCMenuGroupItemRender;
import nc.uap.lfw.ra.render.pc.PCMenuGroupRender;
import nc.uap.lfw.ra.render.pc.PCMenubarCompRender;
import nc.uap.lfw.ra.render.pc.PCOutlookItemRender;
import nc.uap.lfw.ra.render.pc.PCOutlookbarCompRender;
import nc.uap.lfw.ra.render.pc.PCPanelLayoutRender;
import nc.uap.lfw.ra.render.pc.PCPanelPanelRender;
import nc.uap.lfw.ra.render.pc.PCPartCompRender;
import nc.uap.lfw.ra.render.pc.PCRadioCompRender;
import nc.uap.lfw.ra.render.pc.PCRadioGroupCompRender;
import nc.uap.lfw.ra.render.pc.PCSelfDefCompRender;
import nc.uap.lfw.ra.render.pc.PCSilverlightWidgetRender;
import nc.uap.lfw.ra.render.pc.PCSpliterLayoutRender;
import nc.uap.lfw.ra.render.pc.PCSpliterOnePanelRender;
import nc.uap.lfw.ra.render.pc.PCSpliterTwoPanelRender;
import nc.uap.lfw.ra.render.pc.PCTabItemRender;
import nc.uap.lfw.ra.render.pc.PCTabLayoutRender;
import nc.uap.lfw.ra.render.pc.PCTabRightRender;
import nc.uap.lfw.ra.render.pc.PCTextAreaCompRender;
import nc.uap.lfw.ra.render.pc.PCTextCompRender;
import nc.uap.lfw.ra.render.pc.PCToolbarCompRender;
import nc.uap.lfw.ra.render.pc.PCTreeViewCompRender;
import nc.uap.lfw.ra.render.pc.PCWidgetRender;

/**
 * @author renxh 针对PC的一系列渲染器
 */
public class PCUIRenderGroup implements IUIRenderGroup {

//	/**
//	 * 存储通过UIElement为参数生成的render
//	 */
//	Map<UIElement, UIRender<?, ?>> uiMap = new HashMap<UIElement, UIRender<?, ?>>();
//
//	/**
//	 * 存储通过WebElement生成的render
//	 */
//	Map<WebElement, UIRender<?, ?>> webMap = new HashMap<WebElement, UIRender<?, ?>>();
//
//	/**
//	 * 存储已经创建的对话框render
//	 */
//	Map<WebElement, UIRender<?, ?>> dialogUIMap = new HashMap<WebElement, UIRender<?, ?>>();
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * nc.uap.lfw.ra.itf.IUIRenderGroup#getUIRender(nc.uap.lfw.jsp.uimeta.UIElement
//	 * , nc.uap.lfw.core.page.PageMeta, nc.uap.lfw.ra.render.UIRender)
//	 */
	public IUIRender getUIRender(UIMeta uimeta, UIElement type, PageMeta pageMeta, IUIRender parentRender) {
		UIRender<?, ?> render = null;
		if (type instanceof UILayout) {
			render = this.getUILayoutRender(uimeta, type, pageMeta,(UIRender<?, ?>) parentRender);
		} 
		else if (type instanceof UIComponent) {
			render = this.getUIComponentRender(uimeta, type, pageMeta, (UIRender<?, ?>) parentRender);

		} 
		else if (type instanceof UILayoutPanel) {
			render = this.getUILayoutPanelRender(uimeta, type, pageMeta, (UILayoutRender<?, ?>) parentRender);

		}

		return render;
	}

	/**
	 * 2011-7-21 上午10:47:46 renxh des：获得layout的render
	 * 
	 * @param type
	 * @param uimeta 
	 * @param pageMeta
	 * @return
	 */
	private UIRender<?, ?> getUILayoutRender(UIMeta uimeta, UIElement type, PageMeta pageMeta,UIRender<?, ?> parentRender) {
		UIRender<?, ?> render = null;
		if (type instanceof UIWidget) {
			render = new PCWidgetRender((UIWidget) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UIFlowhLayout) {
			render = new PCFlowhLayoutRender((UIFlowhLayout) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UIFlowvLayout) {
			render = new PCFlowvLayoutRender((UIFlowvLayout) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UICardLayout) {
			render = new PCCardLayoutRender((UICardLayout) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UITabComp) {
			render = new PCTabLayoutRender((UITabComp) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UISplitter) {
			render = new PCSpliterLayoutRender((UISplitter) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UIBorder) {
			render = new PCBorderRender((UIBorder) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UIMenuGroup) {
			render = new PCMenuGroupRender((UIMenuGroup) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UIPanel) {
			render = new PCPanelLayoutRender((UIPanel) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UIShutter) {
			render = new PCOutlookbarCompRender((UIShutter) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UIGridLayout) {
			render = new PCGridLayoutRender((UIGridLayout) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UIGridRowLayout) {
			render = new PCGridRowLayoutRender((UIGridRowLayout) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UICanvas) {
			render = new PCCanvasLayoutRender((UICanvas) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UIDiv) {
			render = new PCDivRender((UIDiv) type, uimeta, pageMeta, parentRender);
		}
		return render;
	}

	/**
	 * 2011-7-21 上午10:49:48 renxh des：获得组件的render
	 * 
	 * @param type
	 * @param pageMeta
	 * @return
	 */
	private UIRender<?, ?> getUIComponentRender(UIMeta uimeta, UIElement type, PageMeta pageMeta, UIRender<?, ?> parentRender) {
		UIRender<?, ?> render = null;
		if (type instanceof UIButton) {// 按钮
			UIButton uiComp = (UIButton) type;
			LfwWidget widget = pageMeta.getWidget(uiComp.getWidgetId());
			ButtonComp webComp = (ButtonComp) widget.getViewComponents().getComponent(uiComp.getId());
			if (webComp.getWidget() == null)
				webComp.setWidget(widget);
			render = new PCButtonCompRender(uiComp, webComp, uimeta, pageMeta, parentRender);
		} else if (type instanceof UITextField) { // 输入框
			UITextField uiComp = (UITextField) type;
			TextComp text = (TextComp) pageMeta.getWidget(uiComp.getWidgetId()).getViewComponents().getComponent(uiComp.getId());
			if (text.getEditorType().equals(EditorTypeConst.CHECKBOX)) {
				render = new PCCheckBoxCompRender(uiComp,(CheckBoxComp) text, uimeta, pageMeta, parentRender);
			} else if (text.getEditorType().equals(EditorTypeConst.CHECKBOXGROUP)) {
				render = new PCCheckboxGroupCompRender(uiComp,(CheckboxGroupComp) text, uimeta, pageMeta, parentRender);
			} else if (text.getEditorType().equals(EditorTypeConst.COMBODATA)) {
				render = new PCComboCompRender(uiComp,(ComboBoxComp) text, uimeta, pageMeta, parentRender);
			} else if (text.getEditorType().equals(EditorTypeConst.RADIOCOMP)) {
				render = new PCRadioCompRender(uiComp,(RadioComp) text, uimeta, pageMeta, parentRender);
			} else if (text.getEditorType().equals(EditorTypeConst.RADIOGROUP)) {
				render = new PCRadioGroupCompRender(uiComp,(RadioGroupComp) text, uimeta, pageMeta, parentRender);
			} else if (text.getEditorType().equals(EditorTypeConst.TEXTAREA)) {
				render = new PCTextAreaCompRender(uiComp,(TextAreaComp) text, uimeta, pageMeta, parentRender);
			} else
				render = new PCTextCompRender(uiComp,text, uimeta, pageMeta, parentRender);
		} else if (type instanceof UILabelComp) { // 标签
			UILabelComp uiComp = (UILabelComp) type;
			LfwWidget widget = pageMeta.getWidget(uiComp.getWidgetId());
			LabelComp webComp = (LabelComp) widget.getViewComponents().getComponent(uiComp.getId());
			if (webComp.getWidget() == null)
				webComp.setWidget(widget);
			render = new PCLabelCompRender(uiComp,webComp, uimeta, pageMeta, parentRender);
		} else if (type instanceof UIImageComp) { // 图片
			UIImageComp uiComp = (UIImageComp) type;
			LfwWidget widget = pageMeta.getWidget(uiComp.getWidgetId());
			ImageComp webComp = (ImageComp) widget.getViewComponents().getComponent(uiComp.getId());
			if (webComp.getWidget() == null)
				webComp.setWidget(widget);
			render = new PCImageCompRender(uiComp,webComp, uimeta, pageMeta, parentRender);
		} else if (type instanceof UIMenubarComp) { // 菜单
			UIMenubarComp uiComp = (UIMenubarComp) type;
			LfwWidget widget = pageMeta.getWidget(uiComp.getWidgetId());
			MenubarComp webComp = null;
			if (widget == null) {
				webComp = pageMeta.getViewMenus().getMenuBar(uiComp.getId());
			} else {
				webComp = (MenubarComp) widget.getViewMenus().getMenuBar(uiComp.getId());
			} 
			if (webComp == null)  {
				webComp = (MenubarComp) widget.getViewComponents().getComponent(uiComp.getId());
			}
			if (webComp == null)
				return null;
			if (webComp.getWidget() == null)
				webComp.setWidget(widget);
			render = new PCMenubarCompRender(uiComp,webComp, uimeta, pageMeta, parentRender);

		} else if (type instanceof UIToolBar) { // 工具条
			UIToolBar uiComp = (UIToolBar) type;
			LfwWidget widget = pageMeta.getWidget(uiComp.getWidgetId());
			ToolBarComp webComp = (ToolBarComp) widget.getViewComponents().getComponent(uiComp.getId());
			if (webComp.getWidget() == null)
				webComp.setWidget(widget);
			render = new PCToolbarCompRender(uiComp,webComp, uimeta, pageMeta, parentRender);

		} else if (type instanceof UIFormComp) { // 表单
			UIFormComp uiComp = (UIFormComp) type;
			LfwWidget widget = pageMeta.getWidget(uiComp.getWidgetId());
			FormComp webComp = (FormComp) widget.getViewComponents().getComponent(uiComp.getId());
			if (webComp.getWidget() == null)
				webComp.setWidget(widget);
			render = new PCFormCompRender(uiComp, webComp, uimeta, pageMeta, parentRender);

		}
		else if (type instanceof UIGridComp) { // 表格
			UIGridComp uiComp = (UIGridComp) type;
			LfwWidget widget = pageMeta.getWidget(uiComp.getWidgetId());
			GridComp webComp = (GridComp) widget.getViewComponents().getComponent(uiComp.getId());
			if (webComp != null && webComp.getWidget() == null)
				webComp.setWidget(widget);
			render = new PCGridCompRender(uiComp, webComp, uimeta, pageMeta, parentRender);

		} else if (type instanceof UIIFrame) { // iframe
			UIIFrame uiComp = (UIIFrame) type;
			LfwWidget widget = pageMeta.getWidget(uiComp.getWidgetId());
			IFrameComp webComp = (IFrameComp) widget.getViewComponents().getComponent(uiComp.getId());
			if (webComp != null && webComp.getWidget() == null)
				webComp.setWidget(widget);
			render = new PCIFrameRender(uiComp, webComp, uimeta, pageMeta, parentRender);

		} else if (type instanceof UILinkComp) { // 链接
			UILinkComp uiComp = (UILinkComp) type;
			LfwWidget widget = pageMeta.getWidget(uiComp.getWidgetId());
			LinkComp webComp = (LinkComp) widget.getViewComponents().getComponent(uiComp.getId());
			if (webComp != null && webComp.getWidget() == null)
				webComp.setWidget(widget);
			render = new PCLinkCompRender(uiComp, webComp, uimeta, pageMeta, parentRender);

		} else if (type instanceof UITreeComp) { // 树
			UITreeComp uiComp = (UITreeComp) type;
			LfwWidget widget = pageMeta.getWidget(uiComp.getWidgetId());
			TreeViewComp webComp = (TreeViewComp) widget.getViewComponents().getComponent(uiComp.getId());
			if (webComp != null && webComp.getWidget() == null)
				webComp.setWidget(widget);
			render = new PCTreeViewCompRender(uiComp, webComp, uimeta, pageMeta, parentRender);

		} else if (type instanceof UISelfDefComp) { // 自定义
			UISelfDefComp uiComp = (UISelfDefComp) type;
			LfwWidget widget = pageMeta.getWidget(uiComp.getWidgetId());
			SelfDefComp webComp = (SelfDefComp) widget.getViewComponents().getComponent(uiComp.getId());
			if (webComp != null && webComp.getWidget() == null)
				webComp.setWidget(widget);
			render = new PCSelfDefCompRender(uiComp, webComp, uimeta, pageMeta, parentRender);

		} else if (type instanceof UIExcelComp) { // 自定义
			UIExcelComp uiComp = (UIExcelComp) type;
			LfwWidget widget = pageMeta.getWidget(uiComp.getWidgetId());
			ExcelComp webComp = (ExcelComp) widget.getViewComponents().getComponent(uiComp.getId());
			if (webComp != null && webComp.getWidget() == null)
				webComp.setWidget(widget);
			render = new PCExcelCompRender(uiComp, webComp, uimeta, pageMeta, parentRender);

		}else if (type instanceof UIFormElement) { // 自定义
			UIFormElement uiComp = (UIFormElement) type;
			LfwWidget widget = pageMeta.getWidget(uiComp.getWidgetId());
			FormComp webComp = (FormComp) widget.getViewComponents().getComponent(uiComp.getFormId());
			if (webComp != null && webComp.getWidget() == null)
				webComp.setWidget(widget);
			FormElement fe = webComp.getElementById(uiComp.getId());
			render = new PCFormElementRender(uiComp, fe, uimeta, pageMeta, parentRender);

		}else if(type instanceof UIPartComp){
			UIPartComp uiComp = (UIPartComp) type;
			LfwWidget widget = pageMeta.getWidget(uiComp.getWidgetId());
			WebPartComp webComp = (WebPartComp) widget.getViewComponents().getComponent(uiComp.getId());
			if (webComp != null && webComp.getWidget() == null)
				webComp.setWidget(widget);
			render = new PCPartCompRender(uiComp, webComp, uimeta, pageMeta, (UILayoutPanelRender)parentRender);
		}else if(type instanceof UISilverlightWidget){
			UISilverlightWidget uiComp = (UISilverlightWidget) type;
			LfwWidget widget = pageMeta.getWidget(uiComp.getWidgetId());
			WebSilverlightWidget webComp = (WebSilverlightWidget) widget.getViewComponents().getComponent(uiComp.getId());
			if (webComp != null && webComp.getWidget() == null)
				webComp.setWidget(widget);
			render = new PCSilverlightWidgetRender(uiComp, webComp, uimeta, pageMeta, (UILayoutPanelRender)parentRender);
		}
		else if(type instanceof UIChartComp){
			UIChartComp uiComp = (UIChartComp) type;
			LfwWidget widget = pageMeta.getWidget(uiComp.getWidgetId());
			ChartComp webComp = (ChartComp) widget.getViewComponents().getComponent(uiComp.getId());
			if (webComp != null && webComp.getWidget() == null)
				webComp.setWidget(widget);
			render = new PCChartCompRender(uiComp, webComp, uimeta, pageMeta, (UILayoutPanelRender)parentRender);
		}
		return render;
	}

	/**
	 * 2011-7-21 上午10:51:17 renxh des：获取layoutPanel的render
	 * @param uimeta 
	 * 
	 * @param type
	 * @param pageMeta
	 * @param parentRender
	 * @return
	 */
	private UIRender<?,?> getUILayoutPanelRender(UIMeta uimeta, UIElement type, PageMeta pageMeta, UILayoutRender<?,?> parentRender) {
		UIRender<?,?> render = null;
		if (type instanceof UIMeta) {
			render = (UIRender<?, ?>) new UIMetaRender((UIMeta) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UIFlowhPanel) { // 横向布局的panel
			render = new PCFlowhPanelRender((UIFlowhPanel) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UIFlowvPanel) { // 纵向布局的panel
			render = new PCFlowvPanelRender((UIFlowvPanel) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UICardPanel) { // border布局的panel
			render = new PCCardPanelRender<UICardPanel, WebElement>((UICardPanel) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UITabItem) { // border布局的panel
			render = new PCTabItemRender((UITabItem) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UISplitterOne) {
			if (type instanceof UISplitterTwo) {
				render = new PCSpliterTwoPanelRender((UISplitterTwo) type, uimeta, pageMeta, parentRender);
			} else {
				render = new PCSpliterOnePanelRender((UISplitterOne) type, uimeta, pageMeta, parentRender);
			}
		} else if (type instanceof UIBorderTrue) {
			render = new PCBorderTrueRender((UIBorderTrue) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UIMenuGroupItem) {
			render = new PCMenuGroupItemRender((UIMenuGroupItem) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UIPanelPanel) {
			render = new PCPanelPanelRender((UIPanelPanel) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UIShutterItem) {
			render = new PCOutlookItemRender((UIShutterItem) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UITabRightPanel) {
			render = new PCTabRightRender((UITabRightPanel) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UIGridPanel) {
			render = new PCGridPanelRender((UIGridPanel) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UIGridRowPanel) {
			render = new PCGridRowPanelRender((UIGridRowPanel) type, uimeta, pageMeta, parentRender);
		} else if (type instanceof UICanvasPanel) {
			render = new PCCanvasPanelRender((UICanvasPanel) type, uimeta, pageMeta, parentRender);
		}
		return render;
	}

//
//	/**
//	 * 2011-7-19 下午08:35:51 renxh des：根据uielement对初始化的render进行保存，以备下次使用
//	 * 
//	 * @param type
//	 * @param render
//	 */
//	private void storeUIRender(UIElement type, UIRender<?,?> render) {
//		if (this.getUIRender(type) == null)
//			uiMap.put(type, render);
//	}

	public IUIRender getContextMenuUIRender(UIMeta uimeta, UIElement uiEle,ContextMenuComp webEle, PageMeta pageMeta, IUIRender parentRender) {
		UIRender<?,?> render = new PCContextMenuCompRender((UIComponent)uiEle,(ContextMenuComp) webEle, uimeta, pageMeta, parentRender);
		return render;
	}
//
//	/**
//	 * 2011-8-2 下午07:19:28 renxh des：获得已经创建的对话框render
//	 * 
//	 * @param type
//	 * @return
//	 */
//	private UIRender<?,?> getDialogUIRender(WebElement type) {
//		if (dialogUIMap.containsKey(type)) {
//			LfwLogger.info("get render from map using key :" + type);
//			return (UIRender<?,?>) uiMap.get(type);
//		}
//		return null;
//	}
//
//	/**
//	 * 2011-8-2 下午07:19:48 renxh des：存储已经创建的对话框render
//	 * 
//	 * @param type
//	 * @param render
//	 */
//	private void storeDialogUIRender(WebElement type, UIRender<?,?> render) {
//		if (this.getDialogUIRender(type) == null)
//			dialogUIMap.put(type, render);
//	}

	/**
	 * 获取Render，如果该render已经初始化，则直接取出
	 * 
	 * @param webEle
	 * @param pageMeta
	 * @param parentRender
	 * @return
	 */
	public IUIRender getUIRender(UIMeta uimeta, UIElement uiEle,WebElement webEle, PageMeta pageMeta, IUIRender parentRender) {
		UIRender<?,?> render = this.getUIComponentRender(uimeta, uiEle, webEle, pageMeta, (UIRender<?, ?>) parentRender);
		return render;
	}


//	/**
//	 * 2011-8-2 下午07:20:35 renxh des：存储已经创建的控件的render
//	 * 
//	 * @param webEle
//	 * @param render
//	 */
//	private void storeUIComponentRender(WebElement webEle, UIRender<?,?> render) {
//		if (this.webMap == null) {
//			this.webMap = new HashMap<WebElement, UIRender<?, ?>>();
//		}
//		webMap.put(webEle, render);
//	}

	/**
	 * 根据WebElement获取render
	 * 
	 * @param webComp
	 * @param pageMeta
	 * @param parentRender
	 * @return
	 */
	private UIRender<?,?> getUIComponentRender(UIMeta uimeta, UIElement uiEle, WebElement webComp, PageMeta pageMeta, UIRender<?,?> parentRender) {
		UIRender<?,?> render = null;
		if (webComp instanceof ButtonComp) {// 按钮
			render = new PCButtonCompRender((UIButton)uiEle,(ButtonComp) webComp, uimeta, pageMeta, parentRender);
		} 
		else if(webComp instanceof ChartComp){
			render = new PCChartCompRender((UIChartComp)uiEle, (ChartComp)webComp, uimeta, pageMeta, null);
		}
		else if (webComp instanceof TextComp) { // 输入框
			TextComp text = (TextComp) webComp;
			if (text.getEditorType().equals(EditorTypeConst.CHECKBOX)) {
				render = new PCCheckBoxCompRender((UITextField)uiEle,(CheckBoxComp) text, uimeta, pageMeta, parentRender);
			} 
			else if (text.getEditorType().equals(EditorTypeConst.CHECKBOXGROUP)) {
				render = new PCCheckboxGroupCompRender((UITextField)uiEle,(CheckboxGroupComp) text, uimeta, pageMeta, parentRender);
			} 
			else if (text.getEditorType().equals(EditorTypeConst.COMBODATA)) {
				render = new PCComboCompRender((UITextField)uiEle,(ComboBoxComp) text, uimeta, pageMeta, parentRender);
			} 
			else if (text.getEditorType().equals(EditorTypeConst.RADIOCOMP)) {
				render = new PCRadioCompRender((UITextField)uiEle,(RadioComp) text, uimeta, pageMeta, parentRender);
			} 
			else if (text.getEditorType().equals(EditorTypeConst.RADIOGROUP)) {
				render = new PCRadioGroupCompRender((UITextField)uiEle,(RadioGroupComp) text, uimeta, pageMeta, parentRender);
			} 
			else if (text.getEditorType().equals(EditorTypeConst.TEXTAREA)) {
				render = new PCTextAreaCompRender((UITextField)uiEle,(TextAreaComp) text, uimeta, pageMeta, parentRender);
			} 
			else
				render = new PCTextCompRender((UITextField)uiEle,text, uimeta, pageMeta, parentRender);
		} 
		else if (webComp instanceof LabelComp) { // 标签
			render = new PCLabelCompRender((UILabelComp)uiEle,(LabelComp) webComp, uimeta, pageMeta, parentRender);
		} 
		else if (webComp instanceof ImageComp) { // 图片
			render = new PCImageCompRender((UIImageComp)uiEle,(ImageComp) webComp, uimeta, pageMeta, parentRender);
		} 
		else if (webComp instanceof MenubarComp) { // 菜单
			render = new PCMenubarCompRender((UIMenubarComp)uiEle,(MenubarComp) webComp, uimeta, pageMeta, parentRender);
		} 
		else if (webComp instanceof ToolBarComp) { // 工具条
			render = new PCToolbarCompRender((UIToolBar)uiEle,(ToolBarComp) webComp, uimeta, pageMeta, parentRender);
		} 
		else if (webComp instanceof FormComp) { // 表单
			render = new PCFormCompRender((UIFormComp)uiEle, (FormComp) webComp, uimeta, pageMeta, parentRender);
		} 
		else if (webComp instanceof GridComp) { // 表格
			render = new PCGridCompRender((UIGridComp)uiEle,(GridComp) webComp, uimeta, pageMeta, parentRender);
		} 
		else if (webComp instanceof IFrameComp) { // iframe
			render = new PCIFrameRender((UIIFrame)uiEle,(IFrameComp) webComp, uimeta, pageMeta, parentRender);
		} 
		else if (webComp instanceof LinkComp) { // 链接
			render = new PCLinkCompRender((UILinkComp)uiEle,(LinkComp) webComp, uimeta, pageMeta, parentRender);
		} 
		else if (webComp instanceof TreeViewComp) { // 树
			render = new PCTreeViewCompRender((UITreeComp)uiEle,(TreeViewComp) webComp, uimeta, pageMeta, parentRender);
		} 
		else if (webComp instanceof SelfDefComp) { // 自定义
			render = new PCSelfDefCompRender((UISelfDefComp)uiEle,(SelfDefComp) webComp, uimeta, pageMeta, parentRender);
		} 
		else if (webComp instanceof ExcelComp) { // excel
			render = new PCExcelCompRender((UIExcelComp)uiEle,(ExcelComp) webComp, uimeta, pageMeta, parentRender);
		}
		return render;
	}

}
