package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIMeta;

public class WebPartContentFetcherImpl implements IWebPartContentFetcher {

	@Override
	public String fetchHtml(UIMeta um, PageMeta pm, LfwWidget view) {
		return "<div>Html Body ...</div>";
	}

	@Override
	public String fetchBodyScript(UIMeta um, PageMeta pm, LfwWidget view) {
		return "";
	}

}
