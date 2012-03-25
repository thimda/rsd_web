package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * @author guoweic
 *
 */
public class FileListener extends JsListenerConf {

	private static final long serialVersionUID = -4492840067932197637L;

	public static final String ON_UPLOAD = "onUpload";

	@Override
	public String getJsClazz() {
		return "FileListener";
	}
	
	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[1];
		descs[0] = new JsEventDesc(ON_UPLOAD, "fileUploadEvent");
		return descs;
	}
	
	public static EventHandlerConf getOnUploadEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_UPLOAD);
		LfwParameter param = new LfwParameter();
		param.setName("fileUploadEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
