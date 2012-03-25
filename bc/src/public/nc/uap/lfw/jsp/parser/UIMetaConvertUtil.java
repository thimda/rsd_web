package nc.uap.lfw.jsp.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.exception.LfwParseException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIBorder;
import nc.uap.lfw.jsp.uimeta.UIButton;
import nc.uap.lfw.jsp.uimeta.UICardLayout;
import nc.uap.lfw.jsp.uimeta.UICardPanel;
import nc.uap.lfw.jsp.uimeta.UIChartComp;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIConstant;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIExcelComp;
import nc.uap.lfw.jsp.uimeta.UIFlowhLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowhPanel;
import nc.uap.lfw.jsp.uimeta.UIFlowvLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIFormComp;
import nc.uap.lfw.jsp.uimeta.UIFormGroupComp;
import nc.uap.lfw.jsp.uimeta.UIGridComp;
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

import org.apache.commons.lang.StringUtils;

public class UIMetaConvertUtil implements Convert2JspUtil {

	private PageMeta pagemeta;
    public PageMeta getPagemeta() {
		return pagemeta;
	}

	public void setPagemeta(PageMeta pagemeta) {
		this.pagemeta = pagemeta;
	}

	public String convert2Jsp(String folderPath) {
		UIMetaParserUtil parserUtil = new UIMetaParserUtil();
		parserUtil.setPagemeta(pagemeta);
        UIMeta meta = parserUtil.parseUIMeta(folderPath, null);
        String source = convertMeta2Jsp(meta);
        ClassLoader loader = getClass().getClassLoader();
        InputStream input = loader.getResourceAsStream("html/nodes/template/template.jsp");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            StringBuffer result = new StringBuffer();
            String str = reader.readLine();
            while (str != null) {
                result.append(str);
                str = reader.readLine();
            }
            String rt = result.toString();
            // 增加import标签
            String importStr = "<lfw:import ";
            if (meta != null) {
                if (meta.isJquery() == 1)
                    importStr += " jquery = 'true' ";
                
//                if(meta.isJsEditor() == 1)
//                	importStr += " jsEditor = 'true' ";
//                
//                //excel属性
//                if(meta.isJsExcel() == 1)
//                	importStr += " jsExcel = 'true' ";
                
                if (StringUtils.isNotEmpty(meta.getIncudecss())) {
                    importStr += " includeCss = '" + meta.getIncudecss() + "' ";
                }
                if (StringUtils.isNotEmpty(meta.getIncudejs())) {
                    importStr += " includeJs = '" + meta.getIncudejs() + "' ";
                }
            }
            importStr += "/>";
            rt = rt.replace("$#IMPORT#$", importStr);
            rt = rt.replace("$#BODY#$", source);
            return rt;
        }
        catch (UnsupportedEncodingException e) {
            throw new LfwParseException(e.getMessage(), e);
        }
        catch (IOException e) {
            throw new LfwParseException(e.getMessage(), e);
        } finally {
            if (input != null)
                try {
                    input.close();
                }
                catch (IOException e) {
                    LfwLogger.error(e.getMessage(), e);;
                }
        }
    }

    private String convertMeta2Jsp(UIMeta meta) {
    	StringBuffer buf = new StringBuffer();
        UIElement topLayout = meta.getElement();
        if(topLayout instanceof UIWidget)
        	convertWidget((UIWidget) topLayout, buf);
        else if(topLayout instanceof UILayout)
        	convertLayout((UILayout) topLayout, buf);
        else
        	convertComponent((UIComponent) topLayout, buf);
        Iterator<UIWidget> wit = meta.getDialogMap().values().iterator();
        while (wit.hasNext()) {
            convertWidget(wit.next(), buf);
        }
        return buf.toString();
    }

    private String getNotNullId(String id){
    	return id == null? "" : id;
    }
    
    private void convertLayout(UILayout topLayout, StringBuffer buf) {
      if (topLayout instanceof UICardLayout) {
            UICardLayout cardLayout = (UICardLayout) topLayout;
            String widgetId = cardLayout.getWidgetId();
            String currentItem = cardLayout.getCurrentItem();
            buf.append("<lfw:cardLayout id=\"").append(getNotNullId(cardLayout.getId())).append("\"");
            if (widgetId != null) {
                buf.append(" widget=\"").append(widgetId).append("\"");
            }
            if (currentItem != null) {
                String index = "0";
                if (currentItem.equals("cardpanel2"))
                    index = "1";
                buf.append(" cardIndex=\"").append(index).append("\"");
            }
            buf.append(">\n");
        }
        else if (topLayout instanceof UITabComp) {
            UITabComp tab = (UITabComp) topLayout;
            String widgetId = tab.getWidgetId();
            buf.append("<lfw:tab id=\"").append(tab.getId()).append("\"");
            if (widgetId != null) {
                buf.append(" widget=\"").append(widgetId).append("\"");
            }
            String currentIndex = tab.getCurrentItem();
            if(currentIndex != null)
            	 buf.append(" currentIndex=\"").append(currentIndex).append("\"");
            Integer oneTabHide = tab.getOneTabHide();
            if (oneTabHide != null && oneTabHide.intValue() == 1) {
                buf.append(" oneTabHide=\"true\"");
            }
            buf.append(">\n");
        }
        else if (topLayout instanceof UIShutter) {
            UIShutter tab = (UIShutter) topLayout;
            String widgetId = tab.getWidgetId();
            buf.append("<lfw:outlookbar id=\"").append(tab.getId()).append("\"");
            if (widgetId != null) {
                buf.append(" widget=\"").append(widgetId).append("\"");
            }
            String className = tab.getClassName();
            if (className != null) {
                buf.append(" className=\"").append(className).append("\"");
            }

            String currentItem = tab.getCurrentItem();
            if (currentItem != null) {
                buf.append(" currentIndex=\"").append(currentItem).append("\"");
            }

            buf.append(">\n");
        }
        else if (topLayout instanceof UISplitter) {
            UISplitter tab = (UISplitter) topLayout;
            buf.append("<lfw:spliter id=\"").append(tab.getId()).append("\"");
            String ori = "v";
            if (tab.getOrientation().intValue() == 1)
                ori = "h";
            buf.append(" orientation=\"").append(ori).append("\"");

            Integer boundMode = tab.getBoundMode();
            if (boundMode == 1) {
                buf.append(" boundMode=\"").append("px").append("\"");
            }

            String divideSize = tab.getDivideSize();

            String size = divideSize;
            //boundmode
            if (boundMode == 0) {
                size = "0." + divideSize;
            }
            buf.append(" divideSize=\"").append(size).append("\"");

            String widgetId = tab.getWidgetId();
            if (widgetId != null) {
                buf.append(" widget=\"").append(widgetId).append("\"");
            }

//            String spliterWidth = tab.getSpliterWidth();
//            if (spliterWidth != null) {
//                buf.append(" spliterWidth=\"").append(spliterWidth).append("\"");
//            }

//            Integer hideBar = tab.getHideBar();
//            if (hideBar != null) {
//                boolean hideBarvalue = false;
//                if (hideBar == 1)
//                    hideBarvalue = true;
//                buf.append(" hideBar=\"").append(String.valueOf(hideBarvalue)).append("\"");
//            }

            Integer oneTouch = tab.getOneTouch();
            if (oneTouch != null) {
                boolean oneTouchvalue = false;
                if (oneTouch == 1)
                    oneTouchvalue = true;
                buf.append(" oneTouch=\"").append(String.valueOf(oneTouchvalue)).append("\"");
            }

            Integer inverse = tab.getInverse();
            if (inverse != null) {
                boolean inverseValue = false;
                if (inverse == 1)
                    inverseValue = true;
                buf.append(" inverse=\"").append(String.valueOf(inverseValue)).append("\"");
            }

            buf.append(">\n");
        }
        else if (topLayout instanceof UIFlowvLayout) {
            UIFlowvLayout flowv = (UIFlowvLayout) topLayout;
            buf.append("<lfw:flowvLayout");
            String id = (String) flowv.getAttribute(UIFlowvLayout.ID);
            if (id != null && id != "") {
                buf.append(" id=\"").append(id).append("\"");
            }
            String widgetId = (String) flowv.getAttribute(UIFlowvLayout.WIDGET_ID);
            if (widgetId != null && widgetId != "") {
                buf.append(" widget=\"").append(widgetId).append("\"");
            }
            buf.append(">\n");
        }
        else if (topLayout instanceof UIFlowhLayout) {
            //UIFlowhLayout flowh = (UIFlowhLayout) topLayout;
            buf.append("<lfw:flowhLayout>\n");
        }
        else if (topLayout instanceof UIPanel) {
            UIPanel panel = (UIPanel) topLayout;
            String id = (String) panel.getAttribute("id");
            buf.append("<lfw:panel id=\"").append(id).append("\"");
           
            if (panel.getClassName() != null) {
                buf.append(" className=\"").append(panel.getClassName()).append("\"");
            }
            buf.append(">\n");
        }
        else if (topLayout instanceof UIBorder) {
            UIBorder border = (UIBorder) topLayout;
            buf.append("<lfw:border id=\"");
            buf.append(getNotNullId(border.getId()));
            buf.append("\"");
            if (border.getColor() != null && !"".equals(border.getColor())) {
                buf.append(" color=\"");
                buf.append(border.getColor());
                buf.append("\"");
            }
            if (border.getLeftColor() != null && !"".equals(border.getLeftColor())) {
                buf.append(" leftColor=\"");
                buf.append(border.getLeftColor());
                buf.append("\"");
            }
            if (border.getRightColor() != null && !"".equals(border.getRightColor())) {
                buf.append(" rightColor=\"");
                buf.append(border.getRightColor());
                buf.append("\"");
            }
            if (border.getTopColor() != null && !"".equals(border.getTopColor())) {
                buf.append(" topColor=\"");
                buf.append(border.getTopColor());
                buf.append("\"");
            }
            if (border.getBottomColor() != null && !"".equals(border.getBottomColor())) {
                buf.append(" bottomColor=\"");
                buf.append(border.getBottomColor());
                buf.append("\"");
            }
            if (border.getClassName() != null && !"".equals(border.getClassName())) {
                buf.append(" className=\"");
                buf.append(border.getClassName());
                buf.append("\"");
            }
            if (border.getWidgetId() != null && !"".equals(border.getWidgetId())) {
                buf.append(" widget=\"");
                buf.append(border.getWidgetId());
                buf.append("\"");
            }
            Integer showLeft = border.getShowLeft();
            if (showLeft != null) {
                boolean showLeftValue = true;
                if (showLeft == 1)
                    showLeftValue = false;
                buf.append(" showLeft=\"").append(String.valueOf(showLeftValue)).append("\"");

            }

            Integer showRight = border.getShowRight();
            if (showRight != null) {
                boolean showRightValue = true;
                if (showRight == 1)
                    showRightValue = false;
                buf.append(" showRight=\"").append(String.valueOf(showRightValue)).append("\"");
            }

            Integer showTop = border.getShowTop();
            if (showTop != null) {
                boolean showTopValue = true;
                if (showTop == 1)
                    showTopValue = false;
                buf.append(" showTop=\"").append(String.valueOf(showTopValue)).append("\"");
            }

            Integer showBottom = border.getShowBottom();
            if (showBottom != null) {
                boolean showBottomValue = true;
                if (showBottom == 1)
                    showBottomValue = false;
                buf.append(" showBottom=\"").append(String.valueOf(showBottomValue)).append("\"");
            }

            if (border.getWidth() != null && !"".equals(border.getWidth())) {
                buf.append(" width=\"");
                buf.append(border.getWidth());
                buf.append("\"");
            }
            if (border.getLeftWidth() != null && !"".equals(border.getLeftWidth())) {
                buf.append(" leftWidth=\"").append(border.getLeftWidth());
                buf.append("\"");
            }
            if (border.getRightWidth() != null && !"".equals(border.getRightWidth())) {
                buf.append(" rightWidth=\"").append(border.getRightWidth());
                buf.append("\"");
            }
            if (border.getTopWidth() != null && !"".equals(border.getTopWidth())) {
                buf.append(" topWidth=\"").append(border.getTopWidth());
                buf.append("\"");
            }
            if (border.getBottomWidth() != null && !"".equals(border.getBottomWidth())) {
                buf.append(" bottomWidth=\"").append(border.getBottomWidth()).append("\"");
            }
            buf.append(">\n");
        }

        
        else if (topLayout instanceof UIMenuGroup) {
            UIMenuGroup menuGroup = (UIMenuGroup) topLayout;
            buf.append("<lfw:menubargroup id=\"" + (menuGroup.getId() == null ? "menubargroup1" : menuGroup.getId())
                    + "\">");
        }

        List<UILayoutPanel> list = topLayout.getPanelList();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            convertPanel(list.get(i), buf);
        }

        if (topLayout instanceof UITabComp) {
            UITabComp tab = (UITabComp) topLayout;
            UITabRightPanel rightPanel = tab.getRightPanel();
            if (rightPanel != null && rightPanel.getElement() != null)
                convertPanel(rightPanel, buf);
        }

        if (topLayout instanceof UICardLayout) {
            buf.append("</lfw:cardLayout>\n");
        }
        else if (topLayout instanceof UITabComp) {
            buf.append("</lfw:tab>\n");
        }
        else if (topLayout instanceof UISplitter) {
            buf.append("</lfw:spliter>\n");
        }
        else if (topLayout instanceof UIFlowvLayout) {
            UIFlowvLayout flowv = (UIFlowvLayout) topLayout;
            buf.append("</lfw:flowvLayout>\n");
        }
        else if (topLayout instanceof UIFlowhLayout) {
            //UIFlowhLayout flowh = (UIFlowhLayout) topLayout;
            buf.append("</lfw:flowhLayout>\n");
        }
        else if (topLayout instanceof UIMenuGroup) {
            buf.append("</lfw:menubargroup>\n");
        }

        else if (topLayout instanceof UIPanel) {
            buf.append("</lfw:panel>\n");
        }
        else if (topLayout instanceof UIBorder) {
            buf.append("</lfw:border>\n");
        }
        else if (topLayout instanceof UIShutter) {
            buf.append("</lfw:outlookbar>\n");
        }
    }

    private void convertPanel(UILayoutPanel layoutPanel, StringBuffer buf) {
        if (layoutPanel instanceof UICardPanel) {
            UICardPanel cardPanel = (UICardPanel) layoutPanel;
            buf.append("<lfw:cardPanel id=\"").append(cardPanel.getId()).append("\">\n");
        }
        else if (layoutPanel instanceof UITabItem) {
            UITabItem cardPanel = (UITabItem) layoutPanel;
            buf.append("<lfw:tabitem id=\"").append(cardPanel.getId()).append("\" text=\"").append(
                    cardPanel.getText() == null ? "" : cardPanel.getText()).append("\"");
            if (cardPanel.getState() != null && !(cardPanel.getState().intValue() == -1)) {
                buf.append(" state=\"").append(cardPanel.getState()).append("\"");
            }
            if(cardPanel.getI18nName() != null && !"".equals(cardPanel.getI18nName())){
            	buf.append(" i18nName=\"").append(cardPanel.getI18nName()).append("\"");
            	buf.append(" langDir=\"").append(cardPanel.getLangDir()).append("\"");
            }
            buf.append(">\n");
        }

        else if (layoutPanel instanceof UIShutterItem) {
            UIShutterItem cardPanel = (UIShutterItem) layoutPanel;
            buf.append("<lfw:outlookitem id=\"").append(cardPanel.getId()).append("\" text=\"").append(
                    cardPanel.getText() == null ? "" : cardPanel.getText()).append("\"");
            if (cardPanel.getI18nName() != null) {
                buf.append(" i18nName=\"").append(cardPanel.getI18nName()).append("\"");
            }
            buf.append(">\n");
        }

        else if (layoutPanel instanceof UITabRightPanel) {
        	UITabRightPanel rightPanel = (UITabRightPanel) layoutPanel;
            buf.append("<lfw:tabspace");
            if (rightPanel.getWidth() != null) {
                buf.append(" width=\"").append(rightPanel.getWidth()).append("\"");
            }
            buf.append(">\n");
        }

        else if (layoutPanel instanceof UISplitterTwo) {
            buf.append("<lfw:spliterPanelTwo>\n");
        }
        else if (layoutPanel instanceof UISplitterOne) {
            buf.append("<lfw:spliterPanelOne>\n");
        }
        else if (layoutPanel instanceof UIFlowvPanel) {
            UIFlowvPanel flowvPanel = (UIFlowvPanel) layoutPanel;
            buf.append("<lfw:flowvPanel");
            String height = flowvPanel.getHeight();
            if (height != null && Integer.parseInt(height) != 0) {
                buf.append(" height=\"").append(height).append("\"");
            }
            String anchor = flowvPanel.getAnchor();
            if (anchor != null && !anchor.equals("")) {
                buf.append(" anchor=\"").append(anchor).append("\"");
            }
            buf.append(">\n");
        }
        else if (layoutPanel instanceof UIFlowhPanel) {
            UIFlowhPanel flowhPanel = (UIFlowhPanel) layoutPanel;
            buf.append("<lfw:flowhPanel");
            String width = flowhPanel.getWidth();
            if (width != null && Integer.parseInt(width) != 0) {
                buf.append(" width=\"").append(width).append("\"");
            }
            buf.append(">\n");
        }
        else if (layoutPanel instanceof UIMenuGroupItem) {
            UIMenuGroupItem menuGroupItem = (UIMenuGroupItem) layoutPanel;
            buf.append("<lfw:menubargroupitem state=\"").append(menuGroupItem.getState()).append("\">");
        }

        UIElement ele = layoutPanel.getElement();
        if (ele instanceof UIWidget) {
            convertWidget((UIWidget) ele, buf);
        }
        else if (ele instanceof UILayout) {
            convertLayout((UILayout) ele, buf);
        }
        else
            convertComponent((UIComponent) ele, buf);

        if (layoutPanel instanceof UICardPanel) {
            buf.append("</lfw:cardPanel>\n");
        }
        else if (layoutPanel instanceof UITabItem) {
            buf.append("</lfw:tabitem>\n");
        }
        else if (layoutPanel instanceof UIShutterItem) {
            buf.append("</lfw:outlookitem>\n");
        }
        else if (layoutPanel instanceof UITabRightPanel) {
            buf.append("</lfw:tabspace>\n");
        }
        else if (layoutPanel instanceof UISplitterTwo) {
            buf.append("</lfw:spliterPanelTwo>\n");
        }
        else if (layoutPanel instanceof UISplitterOne) {
            buf.append("</lfw:spliterPanelOne>\n");
        }
        else if (layoutPanel instanceof UIFlowvPanel) {
            buf.append("</lfw:flowvPanel>\n");
        }
        else if (layoutPanel instanceof UIFlowhPanel) {
            buf.append("</lfw:flowhPanel>\n");
        }
        else if (layoutPanel instanceof UIMenuGroupItem) {
            buf.append("</lfw:menubargroupitem>");
        }
    }

    private void convertWidget(UIWidget ele, StringBuffer buf) {
        UIMeta meta = ele.getUimeta();
        buf.append("<lfw:widget id=\"").append(ele.getId()).append("\">\n");
        buf.append(new UIMetaConvertUtil().convertMeta2Jsp(meta));
        buf.append("</lfw:widget>\n");
    }

    private void convertComponent(UIComponent comp, StringBuffer buf) {
        if (comp instanceof UIGridComp) {
            buf.append("<lfw:grid id='").append(comp.getId()).append("' widget='").append(
                    comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
        }
        if (comp instanceof UIExcelComp) {
            buf.append("<lfw:excel id='").append(comp.getId()).append("' widget='").append(
                    comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
        }
        else if (comp instanceof UIFormComp) {
            buf.append("<lfw:form id='").append(comp.getId()).append("' widget='").append(
                    comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
        }
        else if (comp instanceof UIFormGroupComp) {
            buf.append("<lfw:formgroup id='").append(comp.getId()).append("' widget='").append(
                    comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
        }
        else if (comp instanceof UITreeComp) {
            buf.append("<lfw:tree id='").append(comp.getId()).append("' widget='").append(
                    comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
        }
        else if (comp instanceof UIMenubarComp) {
            buf.append("<lfw:menubar id='").append(comp.getId());

            if (comp.getWidgetId() != null) {
                buf.append("' widget='").append(comp.getWidgetId() == null ? "" : comp.getWidgetId());
            }
            buf.append("'/>\n");
        }
        else if (comp instanceof UIButton) {
            buf.append("<lfw:button id=\"").append(comp.getId()).append("\" widget='").append(
                    comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
        }
        else if (comp instanceof UIChartComp) {
            buf.append("<lfw:chart id=\"").append(comp.getId()).append("\" widget='").append(
                    comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
        }
        else if (comp instanceof UILinkComp) {
            buf.append("<lfw:link id=\"").append(comp.getId()).append("\" widget='").append(
                    comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
        }
        else if (comp instanceof UIToolBar) {
            buf.append("<lfw:toolbar id=\"").append(comp.getId()).append("\" widget='").append(
                    comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
        }
        else if (comp instanceof UILabelComp) {
            buf.append("<lfw:label id=\"").append(comp.getId()).append("\" widget='").append(
                    comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
        }
        else if (comp instanceof UIImageComp) {
            buf.append("<lfw:image id=\"").append(comp.getId()).append("\" widget='").append(
                    comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
        }
        else if (comp instanceof UITextField) {
            UITextField text = (UITextField) comp;
            if (text.getType().equals(UIConstant.TEXT_TYPE_COMBO)) {
                buf.append("<lfw:combo id=\"").append(comp.getId()).append("\" widget='").append(
                        comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
            }
            else if (text.getType().equals(UIConstant.TEXT_TYPE_CHECKBOX)) {
                buf.append("<lfw:checkbox id=\"").append(comp.getId()).append("\" widget='").append(
                        comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
            }
            else if (text.getType().equals(UIConstant.TEXT_TYPE_TEXTAREA)) {
                buf.append("<lfw:textarea id=\"").append(comp.getId()).append("\" widget='").append(
                        comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
            }
            else if (text.getType().equals(UIConstant.TEXT_RADIOGROUP)) {
                buf.append("<lfw:radiogroup id=\"").append(comp.getId()).append("\" widget='").append(
                        comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
            }
            else if (text.getType().equals(UIConstant.TEXT_CHECKBOXGROUP)) {
                buf.append("<lfw:checkboxgroup id=\"").append(comp.getId()).append("\" widget='").append(
                        comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
            }
            else if (text.getType().equals(UIConstant.TEXT_RADIOCOMP)) {
                buf.append("<lfw:radio id=\"").append(comp.getId()).append("\" widget='").append(
                        comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
            }
            else if (text.getType().equals(UIConstant.TEXT_TYPE_STRING)) {
                buf.append("<lfw:textcomp id=\"").append(comp.getId()).append("\" widget='").append(
                        comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
            }
        }

        else if (comp instanceof UIIFrame) {
            buf.append("<lfw:iframe id=\"").append(comp.getId()).append("\" widget='").append(
                    comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
        }

        else if (comp instanceof UISelfDefComp) {
            buf.append("<lfw:selfDefComp id=\"").append(comp.getId()).append("\" widget='").append(
                    comp.getWidgetId() == null ? "" : comp.getWidgetId()).append("'/>\n");
        }

        //		if(comp instanceof UIGridComp){
        //			buf.append("</lfw:grid>\n");
        //		}
        //		else if(comp instanceof UIFormComp){
        //			buf.append("</lfw:form>\n");
        //		}
        //		else if(comp instanceof UITreeComp){
        //			buf.append("</lfw:tree>\n");
        //		}
        //		else if(comp instanceof UIMenubarComp){
        //			buf.append("</lfw:menubar>\n");
        //		}
        //		else if(comp instanceof UIButton) {
        //			buf.append("</lfw:button>");
        //		}
        //		else if(comp instanceof UITextField) {
        //			buf.append("</lfw:textcomp>");
        //		}
    }

    public void convert2SwUI(String pageId, String fullPath) {
        PageModel model = new PageModel();
        WebContext ctx = LfwRuntimeEnvironment.getWebContext();
        model.setPageModelId(pageId);
        ctx.setPageId(pageId);
        LfwRuntimeEnvironment.setWebContext(ctx);
        //进行初始化
        model.internalInitialize();

        //PageMeta pm = model.getPageMeta();
        //UIMeta um = new UIMetaParserUtil().parseUIMeta(fullPath);

    }

}
