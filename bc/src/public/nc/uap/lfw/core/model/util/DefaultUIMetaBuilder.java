package nc.uap.lfw.core.model.util;

import nc.uap.lfw.core.AbstractPresentPlugin;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.model.IUIMetaBuilder;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.util.PageNodeUtil;
import nc.uap.lfw.jsp.parser.UIMetaParserUtil;
import nc.uap.lfw.jsp.uimeta.UIMeta;

public class DefaultUIMetaBuilder implements IUIMetaBuilder {

	public UIMeta buildUIMeta(PageMeta pm) {
		String folderPath = "/" + (LfwRuntimeEnvironment.isFromLfw() ? AbstractPresentPlugin.WEBTEMP : "") + "/html/nodes/" + (String) LfwRuntimeEnvironment.getWebContext().getRequest().getAttribute(WebConstant.PERSONAL_PAGE_ID_KEY);
		String fullPath = ContextResourceUtil.getCurrentAppPath() + folderPath;
		String umPath = folderPath + "/uimeta.um";
		if(!ContextResourceUtil.resourceExist(umPath))
			return null;
		UIMetaParserUtil parserUtil = new UIMetaParserUtil();
		parserUtil.setPagemeta(pm);
		UIMeta meta = parserUtil.parseUIMeta(fullPath, null);
		return meta;
	}

	public UIMeta buildUIMeta(PageMeta pm, String pageId, String widgetId) {
		String realPath = PageNodeUtil.getPageNodeDir(pageId);
		if(realPath == null){
			realPath = pageId;
		}
		String folderPath = "/" + (LfwRuntimeEnvironment.isFromLfw() ? AbstractPresentPlugin.WEBTEMP : "") + "/html/nodes/" + realPath+"/"+widgetId;
		String fullPath = ContextResourceUtil.getCurrentAppPath() + folderPath;
		String umPath = folderPath + "/uimeta.um";
		if(!ContextResourceUtil.resourceExist(umPath))
			return null;
		UIMetaParserUtil parserUtil = new UIMetaParserUtil();
		parserUtil.setPagemeta(pm);
		UIMeta meta = parserUtil.parseUIMeta(fullPath, widgetId);
		return meta;
	}
	

}
