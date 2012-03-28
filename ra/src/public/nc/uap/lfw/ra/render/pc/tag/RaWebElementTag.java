package nc.uap.lfw.ra.render.pc.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.BasePageModel;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.tags.SimpleTagWithAttribute;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.itf.IUIRender;

/**
 * 页面元素渲染Tag基类。此基类提供了所有控件的常用方法，比如addToBodyScript,addEventSupport等
 * @author dengjt
 *
 */
public abstract class RaWebElementTag extends SimpleTagWithAttribute {
	private String id;
	/**
	 * Tag类调用入口
	 */
	public void doTag() throws JspException, IOException {
		doRender();
	}
	
	protected void doRender() {
		String html = createHtml();
		if(html != null)
			addToBodyHtml(html);
		String script = createScript();
		if(script != null)
			addToBodyScript(script);
	}

	protected String createScript() {
		return getRender().createRenderScript();
	}

	protected String createHtml() {
		return getRender().createRenderHtml();
	}

	protected abstract UIElement getUIElement();
	
	protected abstract WebElement getWebElement();

	protected IUIRender getRender(){
		IUIRender render = ControlFramework.getInstance().getUIRender((UIMeta) getUIMeta(), (UIElement) getUIElement(), getPageMeta(), null, DriverPhase.PC);
		return render;
	}

	public PageMeta getPageMeta() {
		BasePageModel model = (BasePageModel) getJspContext().getAttribute("pageModel");
		//获得此页面的PageMeta
		PageMeta pageMeta = model.getPageMeta();;
		return pageMeta;
	}
	
	public UIMeta getUIMeta() {
		return null;
	}
	
	/**
	 * 将组件生成脚本添加到体脚本内容buf中。
	 */
	protected void addToBodyScript(String script) {
		if (script != null && !script.equals("")) {
			StringBuffer scriptBuf = (StringBuffer) getPageContext().getAttribute(RaPageBodyTag.ALL_SCRIPT);
			scriptBuf.append("\n");
			scriptBuf.append(script);
		}
	}
	
	/**
	 * 将组件生成脚本添加到体脚本内容buf中。
	 */
	protected void addToBodyHtml(String html) {
		try {
			getJspContext().getOut().write(html);
		} 
		catch (IOException e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException("写入Body时报错");
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
