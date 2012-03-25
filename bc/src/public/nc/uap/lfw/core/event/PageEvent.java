package nc.uap.lfw.core.event;

import nc.uap.lfw.core.page.PageMeta;


public class PageEvent extends AbstractServerEvent<PageMeta> {

	public PageEvent(PageMeta webElement) {
		super(webElement);
	}

}
