package nc.uap.lfw.core.model;

import nc.uap.lfw.core.page.PageMeta;

/**
 * ��Pagemetaʵ��
 *
 */
public class EmptyPageModel extends PageModel {

	@Override
	protected PageMeta createPageMeta() {
		return new PageMeta();
	}

}
