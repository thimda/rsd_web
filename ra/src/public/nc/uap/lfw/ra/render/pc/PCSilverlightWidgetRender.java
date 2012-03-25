/**
 * 
 */
package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.WebSilverlightWidget;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UISilverlightWidget;
import nc.uap.lfw.ra.render.UINormalComponentRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author zjx Silverlight island render for pc.
 */
public class PCSilverlightWidgetRender extends
		UINormalComponentRender<UISilverlightWidget, WebSilverlightWidget> {

	public PCSilverlightWidgetRender(UISilverlightWidget uiEle,
			WebSilverlightWidget webEle, UIMeta uimeta, PageMeta pageMeta,
			UIRender<? extends UIElement, ? extends WebElement> parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);

	}

	@Override
	public String generateBodyHtml() {
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='width:100%;height:100%;overflow:hidden' id='"
				+ this.getDivId() + "' >");
		sb
				.append("<object id=\""+this.getUiElement().getInstanceName()+"\" data=\"data:application/x-silverlight-2,\" type=\"application/x-silverlight-2\" width=\"100%\" height=\"100%\">");
		sb.append("<param name=\"source\" value=\""+ LfwRuntimeEnvironment.getCorePath().replace("/core", "")+"/" +this.getUiElement().getUrl()+"\"/>");
		sb.append("<param name=\"onError\" value=\"onSilverlightError\" />");
		sb.append("<param name=\"onLoad\" value=\"onSilverlightLoad\" />");
		sb.append("<param name=\"background\" value=\"white\" />");
		sb.append("<param name=\"minRuntimeVersion\" value=\"4.0.50401.0\" />");
		sb.append("<param name=\"autoUpgrade\" value=\"true\" />");
		sb
				.append("<a href=\"http://go.microsoft.com/fwlink/?LinkID=149156&v=4.0.50401.0\" style=\"text-decoration:none\">");
		sb
				.append("	  <img src=\"http://go.microsoft.com/fwlink/?LinkId=161376\" alt=\"Get Microsoft Silverlight\" style=\"border-style:none\"/>");
		sb.append("</a>");
		sb.append("</object>");
		sb.append("</div>");
		return sb.toString();
	}

	@Override
	public String generateBodyHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getDivId()).append(" = $ce('DIV');\n");
		buf.append(getDivId()).append(".style.height = '100%';\n");
		buf.append(getDivId()).append(".style.width = '100%';\n");
		buf.append(getDivId()).append(".style.overflow = 'hidden';\n");
		buf.append(getDivId()).append(".id = '" + getDivId() + "';\n");
		return buf.toString();
	}


	@Override
	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_SilverlightWidget;
	}

	@Override
	public String generateBodyScript() {
		String parentDivId = this.getDivId();
		StringBuffer buf = new StringBuffer();		
		UISilverlightWidget webEle = this.getUiElement();
		String showId = this.getVarId();
		buf.append("var ").append(showId).append(" = new SilverlightWidget(");
		buf.append("document.getElementById(\"").append(parentDivId)
					.append("\"), \"");
		buf.append(id).append("\", \"");
		buf.append(webEle.getLeft() == null ? "0px" : webEle.getLeft()).append(
				"\", \"");
		buf.append(webEle.getTop() == null ? "0px" : webEle.getTop()).append(
				"\", \"");
		buf.append(webEle.getWidth() == null ? "100%" : webEle.getWidth())
				.append("\", \"");
		buf.append(webEle.getHeight() == null ? "100%" : webEle.getHeight())
				.append("\", \"");
		buf.append(
				webEle.getPosition() == null ? "relative" : webEle
						.getPosition()).append("\", \"");
		buf.append(webEle.getClassName() + "\"");
		buf.append(");\n");
		String instanceName = webEle.getInstanceName();
		buf.append(showId).append(".Init('").append(instanceName).append("'");
		buf.append(",'").append(getWidget()).append("');\n");
		buf.append("pageUI.getWidget('" + getWidget() + "').addComponent("
				+ showId + ");\n");
		return buf.toString();
	}

}
