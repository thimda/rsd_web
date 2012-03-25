package nc.uap.lfw.core.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.model.parser.WidgetParser;
import nc.uap.lfw.core.page.IUIMeta;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.uimodel.WidgetConfig;
import nc.uap.lfw.jsp.parser.UIMetaParserUtil;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIWidget;

/**
 * @author renxh
 * 自定义页面模型
 */
public class RaSelfDefinePageModel extends PageModel {
	
	
	@Override
	protected PageMeta createPageMeta() {
		String pageId = (String) getWebContext().getPageId();
		String widgetId = (String) getWebContext().getRequest().getParameter(WebConstant.WIDGET_ID);
		if(WebConstant.DEFAULT_WINDOW_ID.equals(pageId)){ // 针对public view的特殊处理
			PageMeta pageMeta = new PageMeta();
			pageMeta.setId(pageId);
			InputStream input;
			try {
				String folderPath = "pagemeta/public/widgetpool/" +widgetId + "/widget.wd";
				String fullPath = ContextResourceUtil.getCurrentAppPath() + folderPath;
				input = new FileInputStream(fullPath);
			} catch (FileNotFoundException e) {
				throw new LfwRuntimeException(e.getMessage());
			}
			LfwWidget widget = WidgetParser.parse(input);
			pageMeta.addWidget(widget);
			return pageMeta;
		}
		else{
			LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
			PageMeta oldPm = super.createPageMeta();
			
			if(pageId != null){
				PageMeta pageMeta = new PageMeta();
				pageMeta.setId(pageId);
				LfwWidget widget = oldPm.getWidget(widgetId);
				WidgetConfig wconf = oldPm.getWidgetConf(widgetId);
				pageMeta.addWidgetConf(wconf);
				pageMeta.setCaption(widget.getCaption());
				pageMeta.addWidget(widget);
				pageMeta.setProcessorClazz("nc.uap.lfw.core.processor.EventRequestProcessor");
				return pageMeta;
			}else{
				throw new LfwRuntimeException("pageId 不能为空！");
			}
		}
		
	}

	@Override
	protected IUIMeta createUIMeta(PageMeta pm) {
		String pageId = (String) getWebContext().getPageId();
		String widgetId = (String) getWebContext().getRequest().getParameter(WebConstant.WIDGET_ID);
		if("defaultWindowId".equals(pageId)){ // 针对public view的特殊处理
			String folderPath = "pagemeta/public/widgetpool/" +widgetId;
			String fullPath = ContextResourceUtil.getCurrentAppPath() + folderPath;
			UIMetaParserUtil parserUtil = new UIMetaParserUtil();
			parserUtil.setPagemeta(pm);
			UIMeta meta = parserUtil.parseUIMeta(fullPath, null);
			
			UIMeta uimeta = new UIMeta();
			UIWidget uiWidget = new UIWidget();
			uiWidget.setId(widgetId);
			uimeta.setElement(uiWidget);
			uiWidget.setUimeta(meta);
			return uimeta;
		}else{
			UIMeta uimeta = new UIMeta();
			UIWidget uiWidget = new UIWidget();
			uiWidget.setId(widgetId);
			uimeta.setElement(uiWidget);
			uiWidget.setUimeta(this.getUIMetaBuilder().buildUIMeta(pm, pageId, widgetId));
			return uimeta;
		}
		
	}
	
	
	

}
