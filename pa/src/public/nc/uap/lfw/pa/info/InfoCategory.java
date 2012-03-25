/**
 * 
 */
package nc.uap.lfw.pa.info;

import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.event.ctx.LfwPageContext;


/**
 * @author wupeng1
 * @version 6.0 2011-8-22
 * @since 1.6
 */
public final class InfoCategory {
	public static final Map<String, BaseInfo> infoMap = new HashMap<String, BaseInfo>();
	static{
		infoMap.put(LfwPageContext.SOURCE_TYPE_BUTTON, new ButtonInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_FORMCOMP, new FormCompInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_FORMELE + "_ref", new ReferenceFormElementInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_FORMELE, new FormElementInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_GRID, new GridCompInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_GRID_HEADER, new GridColumnInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_TREE, new TreeViewCompInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_MENUBAR, new MenubarCompInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_MENUBAR_MENUITEM, new MenuItemInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_LABEL, new LabelCompInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_LINKCOMP, new LinkCompInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_IMAGECOMP, new ImageCompInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_SELF_DEF_COMP, new SelfDefCompInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_IFRAME, new IFrameCompInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_TOOLBAR_BUTTON, new ToolBarCompInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_PROGRESS_BAR, new ProgressBarCompInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_TEXTAREA, new TextAreaCompInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_TEXT, new TextCompInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_SELF_DEF_COMP, new SelfDefCompInfo());
		
		infoMap.put(EditorTypeConst.CHECKBOX, new CheckBoxCompInfo());
		infoMap.put(EditorTypeConst.CHECKBOXGROUP, new CheckboxGroupCompInfo());
		infoMap.put(EditorTypeConst.COMBODATA, new ComboBoxCompInfo());
		infoMap.put(EditorTypeConst.RADIOCOMP, new RadioCompInfo());
		infoMap.put(EditorTypeConst.RADIOGROUP, new RadioGroupCompInfo());
		infoMap.put(EditorTypeConst.REFERENCE, new ReferenceCompInfo());
		infoMap.put(EditorTypeConst.TEXTAREA, new TextAreaCompInfo());
		
		infoMap.put(LfwPageContext.SOURCE_TYPE_HTMLCONTENT, new WebPartCompInfo());
		
		infoMap.put(LfwPageContext.SOURCE_TYPE_FLOWVLAYOUT, new FlowvLayoutInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_FLOWHLAYOUT, new FlowhLayoutInfo());
//		infoMap.put(LfwPageContext.SOURCE_TYPE_BORDERLAYOUT, new BorderLayoutInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_CARDLAYOUT, new CardLayoutInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_GRIDLAYOUT, new GridLayoutInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_GRIDROWPANEL, new GridRowPanelInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_GRIDROW, new GridRowLayoutInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_SPLITERLAYOUT, new SpliterLayoutInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_BORDER, new BoarderInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_PANELLAYOUT, new PanelLayoutInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_MENU_GROUP, new MenuGroupInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_TAG, new TabCompInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_OUTLOOKBAR, new ShutterInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_OUTLOOKBAR_ITEM, new ShutterItemInfo());
		
		infoMap.put(LfwPageContext.SOURCE_TYPE_WIDGT, new WidgetInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_UIMETA, new UIMetaInfo());
		
		infoMap.put(LfwPageContext.SOURCE_TYPE_FLOWVPANEL, new FlowvPanelInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_FLOWHPANEL, new FlowhPanelInfo());
//		infoMap.put(LfwPageContext.SOURCE_TYPE_BORDERPANEL, new BorderPanelInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_CARDPANEL, new CardPanelInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_GRIDPANEL, new GridPanelInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_SPLITERONEPANEL, new SpliterOnePanelInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_SPLITERTWOPANLE, new SpliterTwoPanelInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_BORDERTRUE, new BorderTrueInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_PANELPANEL, new PanelPanelInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_MENU_GROUP_ITEM, new MenuGroupItemInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_TABITEM, new TabItemInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_TABSPACE, new TabRightPanelPanelInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_CANVASLAYOUT, new CanvasInfo());
		infoMap.put(LfwPageContext.SOURCE_TYPE_CANVASPANEL, new CanvasPanelInfo());
		
		
	}
	
	public static BaseInfo getInfo(String type){
		return (BaseInfo) infoMap.get(type);
	}
}
