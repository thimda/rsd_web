package nc.uap.lfw.pa;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.IFrameComp;
import nc.uap.lfw.core.model.PageModel;

public class PaPageModel extends PageModel {

	@Override
	protected void initPageMetaStruct() {
		super.initPageMetaStruct();
		IFrameComp ifm = (IFrameComp) getPageMeta().getWidget("editor").getViewComponents().getComponent("mainif");
		String url = LfwRuntimeEnvironment.getWebContext().getParameter(PaConstant.PA_URL);
		url = "core/uimeta.um?pageId=test" + "&emode=1";
//		String url2 = LfwRuntimeEnvironment.getWebContext().getParameter(PaConstant.PA_URL);
		ifm.setSrc(url);
		
	}
	
}
