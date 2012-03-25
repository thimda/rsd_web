package nc.uap.lfw.pa;

import nc.uap.lfw.core.event.ScriptEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.ScriptServerListener;
import nc.uap.lfw.jsp.uimeta.UIFlowhLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowhPanel;

public class PaDragListener extends ScriptServerListener {

	public PaDragListener(LfwPageContext pageCtx, String widgetId) {
		super(pageCtx, widgetId);
	}

	@Override
	public void handlerEvent(ScriptEvent event) {
	//	UIMeta um = null;
		UIFlowhLayout fhl = new UIFlowhLayout();
		fhl.setId("ddd");
		UIFlowhPanel fhp = new UIFlowhPanel();
		fhl.addPanel(fhp);
	}

}
