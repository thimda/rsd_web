package nc.uap.lfw.ra.render.pc.tag;

import nc.uap.lfw.core.model.BasePageModel;
import nc.uap.lfw.core.page.IUIMeta;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIMeta;

public class JspPageModel extends BasePageModel {
	@Override
	protected IUIMeta createUIMeta(PageMeta pm) {
		UIMeta um = new UIMeta();
		return um;
	}

}
