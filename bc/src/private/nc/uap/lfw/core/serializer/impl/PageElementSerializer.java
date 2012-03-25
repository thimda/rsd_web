package nc.uap.lfw.core.serializer.impl;

import nc.uap.lfw.conf.persist.WidgetToXml;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.persistence.PageMetaToXml;
import nc.uap.lfw.core.serializer.IPageElementSerializer;
import nc.uap.lfw.design.impl.UIMetaToXml;
import nc.uap.lfw.jsp.uimeta.UIMeta;

public class PageElementSerializer implements IPageElementSerializer{

	@Override
	public String serializeUIMeta(UIMeta childUIMeta) {
		return UIMetaToXml.toString(childUIMeta);
	}

	@Override
	public String serializeWidget(LfwWidget widget) {
		return WidgetToXml.toString(widget);
	}

	@Override
	public String serializePageMeta(PageMeta pagemeta) {
		return PageMetaToXml.toString(pagemeta);
	}

}
