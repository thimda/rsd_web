package nc.uap.lfw.core.serializer;

import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIMeta;

public interface IPageElementSerializer {

	String serializeWidget(LfwWidget widget);

	String serializeUIMeta(UIMeta childUIMeta);

	String serializePageMeta(PageMeta pagemeta);

}
