/**
 * 
 */
package nc.uap.lfw.pa;

import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.pa.ctr.vo.PaButtonCompVO;
import nc.uap.lfw.pa.ctr.vo.PaCheckBoxCompVO;
import nc.uap.lfw.pa.ctr.vo.PaCheckboxGroupCompVO;
import nc.uap.lfw.pa.ctr.vo.PaComboBoxCompVO;
import nc.uap.lfw.pa.ctr.vo.PaFormCompVO;
import nc.uap.lfw.pa.ctr.vo.PaFormElementVO;
import nc.uap.lfw.pa.ctr.vo.PaGridColumnVO;
import nc.uap.lfw.pa.ctr.vo.PaGridCompVO;
import nc.uap.lfw.pa.ctr.vo.PaIFrameCompVO;
import nc.uap.lfw.pa.ctr.vo.PaImageCompVO;
import nc.uap.lfw.pa.ctr.vo.PaLabelCompVO;
import nc.uap.lfw.pa.ctr.vo.PaLinkCompVO;
import nc.uap.lfw.pa.ctr.vo.PaMenubarCompVO;
import nc.uap.lfw.pa.ctr.vo.PaProgressBarCompVO;
import nc.uap.lfw.pa.ctr.vo.PaRadioCompVO;
import nc.uap.lfw.pa.ctr.vo.PaRadioGroupCompVO;
import nc.uap.lfw.pa.ctr.vo.PaReferenceCompVO;
import nc.uap.lfw.pa.ctr.vo.PaSelfDefCompVO;
import nc.uap.lfw.pa.ctr.vo.PaTextAreaCompVO;
import nc.uap.lfw.pa.ctr.vo.PaTextCompVO;
import nc.uap.lfw.pa.ctr.vo.PaToolBarCompVO;
import nc.uap.lfw.pa.ctr.vo.PaTreeViewCompVO;
import nc.uap.lfw.pa.ctr.vo.PaWebPartCompVO;
import nc.uap.lfw.pa.vo.PaBorderTrueVO;
import nc.uap.lfw.pa.vo.PaBorderVO;
import nc.uap.lfw.pa.vo.PaCardLayoutVO;
import nc.uap.lfw.pa.vo.PaCardPanelVO;
import nc.uap.lfw.pa.vo.PaFlowhLayoutPanelVO;
import nc.uap.lfw.pa.vo.PaFlowhLayoutVO;
import nc.uap.lfw.pa.vo.PaFlowvLayoutPanelVO;
import nc.uap.lfw.pa.vo.PaFlowvLayoutVO;
import nc.uap.lfw.pa.vo.PaGridLayoutVO;
import nc.uap.lfw.pa.vo.PaGridPanelVO;
import nc.uap.lfw.pa.vo.PaGridRowLayoutVO;
import nc.uap.lfw.pa.vo.PaMenuGroupItemVO;
import nc.uap.lfw.pa.vo.PaMenuGroupVO;
import nc.uap.lfw.pa.vo.PaPanelPanelVO;
import nc.uap.lfw.pa.vo.PaPanelVO;
import nc.uap.lfw.pa.vo.PaShutterItemVO;
import nc.uap.lfw.pa.vo.PaShutterVO;
import nc.uap.lfw.pa.vo.PaSplitterOneVO;
import nc.uap.lfw.pa.vo.PaSplitterTwoVO;
import nc.uap.lfw.pa.vo.PaSplitterVO;
import nc.uap.lfw.pa.vo.PaTabCompVO;
import nc.uap.lfw.pa.vo.PaTabItemVO;
import nc.uap.lfw.pa.vo.PaTabRightPanelPanelVO;
import nc.uap.lfw.pa.vo.PaUIMetaVO;
import nc.uap.lfw.pa.vo.PaWidgetVO;

/**
 * @author wupeng1
 * @version 6.0 2011-8-17
 * @since 1.6
 */
public class PaConstantMap {

	public static Map<String, Class<?>> mappingTable = new HashMap<String, Class<?>>();
	public static Map<String, String> web2ui = new HashMap<String, String>();
	public static Map<String, String> labelNameMap = new HashMap<String, String>();
	
	static{
		
		//组件类型与对应VO的Map
		mappingTable.put(LfwPageContext.SOURCE_TYPE_BUTTON, PaButtonCompVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_GRID, PaGridCompVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_GRID_HEADER, PaGridColumnVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_FORMCOMP, PaFormCompVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_FORMELE, PaFormElementVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_FORMELE + "_ref", PaFormElementVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_TREE, PaTreeViewCompVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_MENUBAR_MENUITEM, PaMenubarCompVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_MENUBAR, PaMenubarCompVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_LABEL, PaLabelCompVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_LINKCOMP, PaLinkCompVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_IMAGECOMP, PaImageCompVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_SELF_DEF_COMP, PaSelfDefCompVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_IFRAME, PaIFrameCompVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_TOOLBAR_BUTTON, PaToolBarCompVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_PROGRESS_BAR, PaProgressBarCompVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_TEXTAREA, PaTextAreaCompVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_TEXT, PaTextCompVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_HTMLCONTENT, PaWebPartCompVO.class);
		
		mappingTable.put(LfwPageContext.SOURCE_TYPE_UIMETA, PaUIMetaVO.class);
		
		mappingTable.put(LfwPageContext.SOURCE_TYPE_FLOWVLAYOUT, PaFlowvLayoutVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_FLOWHLAYOUT, PaFlowhLayoutVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_PANELLAYOUT, PaPanelVO.class);
//		mappingTable.put(LfwPageContext.SOURCE_TYPE_BORDERLAYOUT, PaBorderLayoutVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_CARDLAYOUT, PaCardLayoutVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_GRIDLAYOUT, PaGridLayoutVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_GRIDROW, PaGridRowLayoutVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_SPLITERLAYOUT, PaSplitterVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_BORDER, PaBorderVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_PANELLAYOUT, PaPanelVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_MENU_GROUP, PaMenuGroupVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_TAG, PaTabCompVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_OUTLOOKBAR, PaShutterVO.class);
		
		mappingTable.put(LfwPageContext.SOURCE_TYPE_FLOWVPANEL, PaFlowvLayoutPanelVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_FLOWHPANEL, PaFlowhLayoutPanelVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_PANELPANEL, PaPanelPanelVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_CARDPANEL, PaCardPanelVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_GRIDPANEL, PaGridPanelVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_SPLITERONEPANEL, PaSplitterOneVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_SPLITERTWOPANLE, PaSplitterTwoVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_BORDERTRUE, PaBorderTrueVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_PANELPANEL, PaPanelPanelVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_MENU_GROUP_ITEM, PaMenuGroupItemVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_TABITEM, PaTabItemVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_TABSPACE, PaTabRightPanelPanelVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_OUTLOOKBAR_ITEM, PaShutterItemVO.class);
		mappingTable.put(LfwPageContext.SOURCE_TYPE_WIDGT, PaWidgetVO.class);
		
		mappingTable.put(EditorTypeConst.CHECKBOX, PaCheckBoxCompVO.class);
		mappingTable.put(EditorTypeConst.CHECKBOXGROUP, PaCheckboxGroupCompVO.class);
		mappingTable.put(EditorTypeConst.RADIOCOMP, PaRadioCompVO.class);
		mappingTable.put(EditorTypeConst.RADIOGROUP, PaRadioGroupCompVO.class);
		mappingTable.put(EditorTypeConst.REFERENCE, PaReferenceCompVO.class);
		mappingTable.put(EditorTypeConst.COMBODATA, PaComboBoxCompVO.class);
		mappingTable.put(EditorTypeConst.TEXTAREA, PaTextAreaCompVO.class);
		
		//Web组件类型和UI类型的对应表
		web2ui.put("UIBorder", LfwPageContext.SOURCE_TYPE_BORDER);
//		web2ui.put("UIBorderLayout", LfwPageContext.SOURCE_TYPE_BORDERLAYOUT);
		web2ui.put("UIPanel", LfwPageContext.SOURCE_TYPE_PANELLAYOUT);
//		web2ui.put("UIBorderPanel", LfwPageContext.SOURCE_TYPE_BORDERPANEL);
		web2ui.put("UIBorderTrue", LfwPageContext.SOURCE_TYPE_BORDERTRUE);
		web2ui.put("UIButton", LfwPageContext.SOURCE_TYPE_BUTTON);
		web2ui.put("UICardLayout", LfwPageContext.SOURCE_TYPE_CARDLAYOUT);
		web2ui.put("UICardPanel", LfwPageContext.SOURCE_TYPE_CARDPANEL);
		web2ui.put("UIGridComp", LfwPageContext.SOURCE_TYPE_GRID);
		web2ui.put("UIGridLayout", LfwPageContext.SOURCE_TYPE_GRIDLAYOUT);
		web2ui.put("UIGridRowLayout", LfwPageContext.SOURCE_TYPE_GRIDROW);
		web2ui.put("UIGridRowPanel", LfwPageContext.SOURCE_TYPE_GRIDROWPANEL);
		web2ui.put("UIGridPanel", LfwPageContext.SOURCE_TYPE_GRIDPANEL);
		web2ui.put("UIExcelComp", LfwPageContext.SOURCE_TYPE_EXCEL);
		web2ui.put("UIWidget", LfwPageContext.SOURCE_TYPE_WIDGT);
		web2ui.put("UIFormComp", LfwPageContext.SOURCE_TYPE_FORMCOMP);
		web2ui.put("UIMenubarComp", LfwPageContext.SOURCE_TYPE_MENUBAR);
		web2ui.put("UIPartComp", LfwPageContext.SOURCE_TYPE_HTMLCONTENT);
		web2ui.put("UISelfDefComp", LfwPageContext.SOURCE_TYPE_SELF_DEF_COMP);
		web2ui.put("UIFlowvLayout", LfwPageContext.SOURCE_TYPE_FLOWVLAYOUT);
		web2ui.put("UIFlowvPanel", LfwPageContext.SOURCE_TYPE_FLOWVPANEL);
		web2ui.put("UIFlowhLayout", LfwPageContext.SOURCE_TYPE_FLOWHLAYOUT);
		web2ui.put("UIFlowhPanel", LfwPageContext.SOURCE_TYPE_FLOWHPANEL);
		web2ui.put("UIPanelPanel", LfwPageContext.SOURCE_TYPE_PANELPANEL);
		web2ui.put("UISplitter", LfwPageContext.SOURCE_TYPE_SPLITERLAYOUT);
		web2ui.put("UISplitterOne", LfwPageContext.SOURCE_TYPE_SPLITERONEPANEL);
		web2ui.put("UISplitterTwo", LfwPageContext.SOURCE_TYPE_SPLITERTWOPANLE);
		web2ui.put("UICanvas", LfwPageContext.SOURCE_TYPE_CANVASLAYOUT);
		web2ui.put("UICanvasPanel", LfwPageContext.SOURCE_TYPE_CANVASPANEL);
		
		web2ui.put("UIMeta", LfwPageContext.SOURCE_TYPE_UIMETA);
		web2ui.put("UITreeComp", LfwPageContext.SOURCE_TYPE_TREE);
//		web2ui.put("UITextField", LfwPageContext.SOURCE_TYPE_TEXT);
		web2ui.put("UIFormElement", LfwPageContext.SOURCE_TYPE_FORMELE);
		web2ui.put("UILabelComp", LfwPageContext.SOURCE_TYPE_LABEL);
		web2ui.put("UIIFrame", LfwPageContext.SOURCE_TYPE_IFRAME);
		web2ui.put("UITabComp", LfwPageContext.SOURCE_TYPE_TAG);
		web2ui.put("UITabItem", LfwPageContext.SOURCE_TYPE_TABITEM);
		web2ui.put("UITabRightPanelPanel", LfwPageContext.SOURCE_TYPE_TABSPACE);
		web2ui.put("UIShutter", LfwPageContext.SOURCE_TYPE_OUTLOOKBAR);
		web2ui.put("UIShutterItem", LfwPageContext.SOURCE_TYPE_OUTLOOKBAR_ITEM);
		web2ui.put("UIImageComp", LfwPageContext.SOURCE_TYPE_IMAGECOMP);
		web2ui.put("UIToolBar", LfwPageContext.SOURCE_TYPE_TOOLBAR_BUTTON);
		web2ui.put("UIChartComp", LfwPageContext.SOURCE_TYPE_CHART);
		web2ui.put("UITextField", LfwPageContext.SOURCE_TYPE_TEXTAREA);
		
		web2ui.put("ButtonComp", LfwPageContext.SOURCE_TYPE_BUTTON);
		web2ui.put("GridComp", LfwPageContext.SOURCE_TYPE_GRID);
		web2ui.put("GridColumn", LfwPageContext.SOURCE_TYPE_GRID_HEADER);
		web2ui.put("FormComp", LfwPageContext.SOURCE_TYPE_FORMCOMP);
		web2ui.put("FormElement", LfwPageContext.SOURCE_TYPE_FORMELE);
		web2ui.put("ExcelComp", LfwPageContext.SOURCE_TYPE_EXCEL);
		web2ui.put("MenubarComp", LfwPageContext.SOURCE_TYPE_MENUBAR);
		web2ui.put("WebPartComp", LfwPageContext.SOURCE_TYPE_HTMLCONTENT);
		web2ui.put("SelfDefComp", LfwPageContext.SOURCE_TYPE_SELF_DEF_COMP);
		web2ui.put("IFrameComp", LfwPageContext.SOURCE_TYPE_IFRAME);
		web2ui.put("LabelComp", LfwPageContext.SOURCE_TYPE_LABEL);
		web2ui.put("LinkComp", LfwPageContext.SOURCE_TYPE_LINKCOMP);
		web2ui.put("ToolBarComp", LfwPageContext.SOURCE_TYPE_TOOLBAR_BUTTON);
		web2ui.put("ProgressBarComp", LfwPageContext.SOURCE_TYPE_PROGRESS_BAR);
		web2ui.put("ImageComp", LfwPageContext.SOURCE_TYPE_IMAGECOMP);
		web2ui.put("ChartComp", LfwPageContext.SOURCE_TYPE_CHART);
		web2ui.put("TextAreaComp", LfwPageContext.SOURCE_TYPE_TEXTAREA);
		web2ui.put("TreeViewComp", LfwPageContext.SOURCE_TYPE_TREE);
		
		web2ui.put("TextComp", LfwPageContext.SOURCE_TYPE_TEXT);
		web2ui.put("CheckBoxComp", EditorTypeConst.CHECKBOX);
		web2ui.put("CheckboxGroupComp", EditorTypeConst.CHECKBOXGROUP);
		web2ui.put("ComboBoxComp", EditorTypeConst.COMBODATA);
		web2ui.put("RadioGroupComp", EditorTypeConst.RADIOGROUP);
		web2ui.put("RadioComp", EditorTypeConst.RADIOCOMP);
		web2ui.put("ReferenceComp", EditorTypeConst.REFERENCE);
		web2ui.put("TextAreaComp", EditorTypeConst.TEXTAREA);
		
		labelNameMap.put(LfwPageContext.SOURCE_TYPE_BUTTON, "按钮");
		labelNameMap.put(LfwPageContext.SOURCE_TYPE_IMAGECOMP, "图片");
		labelNameMap.put(LfwPageContext.SOURCE_TYPE_LABEL, "标签");
		labelNameMap.put(LfwPageContext.SOURCE_TYPE_MENUBAR, "菜单");
		labelNameMap.put(LfwPageContext.SOURCE_TYPE_LINKCOMP, "超链接");
		labelNameMap.put(LfwPageContext.SOURCE_TYPE_TEXT, "文本");
		labelNameMap.put(LfwPageContext.SOURCE_TYPE_IFRAME, "子窗口");
		labelNameMap.put(LfwPageContext.SOURCE_TYPE_HTMLCONTENT, "WebPart控件");
		
		
	}
	
}
