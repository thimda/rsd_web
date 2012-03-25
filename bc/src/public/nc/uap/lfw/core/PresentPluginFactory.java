package nc.uap.lfw.core;

import nc.uap.lfw.util.LfwClassUtil;

public final class PresentPluginFactory {
	public static final String UM = "UM";
	public static final String JSP = "JSP";
	public static final String UI = "UI";
	public static final String RA = "RA";
	public static final String HTML = "HTML";
	public static final String APP = "APP";
	public static ControlPlugin getControlPlugin(String path) {
		if(path == null)
			return new AjaxControlPlugin();
		else if(path.equals("/rpc")){
			return new RpcControlPlugin();
		}
		else if(path.endsWith(".app")){
			return (ControlPlugin) LfwClassUtil.newInstance("nc.uap.lfw.app.plugin.AppControlPlugin");
		}
		else{	
			return new PageControlPlugin();
		}
	}
	
	public static PresentPlugin getPresentPlugin(String templateJsp) {
		String type = null;
		if(templateJsp.endsWith(".jsp")){
			type = PresentPluginFactory.JSP;
		}
		else if(templateJsp.endsWith(".ra")){
			type = PresentPluginFactory.RA;
		}
		else if(templateJsp.endsWith(".html")){
			type = PresentPluginFactory.HTML;
		}
		else if(templateJsp.endsWith(".um")){
			type = PresentPluginFactory.UM;
		}
		else if(templateJsp.endsWith(".ui")){
			type = PresentPluginFactory.UI;
		}
		else if(templateJsp.endsWith(".app")){
			type = PresentPluginFactory.APP;
		}
		return getPluginByType(type);
	}
	
	private static PresentPlugin getPluginByType(String type) {
		if(UM.equals(type)){
			return new UmPresentPlugin();//(PresentPlugin) LfwClassUtil.newInstance("nc.uap.lfw.ra.plugin.RaPresentPlugin");
		}
		else if(APP.equals(type)){
			return (PresentPlugin) LfwClassUtil.newInstance("nc.uap.lfw.app.plugin.AppPresentPlugin");
		}
		else if(JSP.equals(type)){
			return new JspPresentPlugin();
		}
		else if(HTML.equals(type)){
			return new HtmlPresentPlugin();
		}
		else if(RA.equals(type)){
			return (PresentPlugin) LfwClassUtil.newInstance("nc.uap.lfw.ra.plugin.RaPresentPlugin");
		}
		return null;
	}
}
